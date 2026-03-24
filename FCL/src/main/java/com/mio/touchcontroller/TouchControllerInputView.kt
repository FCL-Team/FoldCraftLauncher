package com.mio.touchcontroller

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.CompletionInfo
import android.view.inputmethod.CorrectionInfo
import android.view.inputmethod.CursorAnchorInfo
import android.view.inputmethod.EditorBoundsInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedText
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputContentInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.view.inputmethod.EditorInfoCompat
import com.tungsten.fcl.control.FCLInput
import com.tungsten.fclauncher.keycodes.FCLKeycodes
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import top.fifthlight.touchcontroller.proxy.client.LauncherProxyClient
import top.fifthlight.touchcontroller.proxy.message.FloatRect
import top.fifthlight.touchcontroller.proxy.message.input.TextInputState
import top.fifthlight.touchcontroller.proxy.message.input.TextRange
import top.fifthlight.touchcontroller.proxy.message.input.compositionText
import top.fifthlight.touchcontroller.proxy.message.input.doArrowLeft
import top.fifthlight.touchcontroller.proxy.message.input.doArrowRight
import top.fifthlight.touchcontroller.proxy.message.input.doBackspace
import top.fifthlight.touchcontroller.proxy.message.input.doDelete
import top.fifthlight.touchcontroller.proxy.message.input.doShiftLeft
import top.fifthlight.touchcontroller.proxy.message.input.doShiftRight
import top.fifthlight.touchcontroller.proxy.message.input.selectionText

class TouchControllerInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    var disableFullScreenInput: Boolean = false
    var client: LauncherProxyClient? = null
        set(value) {
            val prev = field
            if (prev != null) {
                prev.inputHandler = null
            }
            field = value
            if (value != null) {
                value.inputHandler = inputHandler
            }
        }
    private var width: Int = -1
    private var height: Int = -1
    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
    var fclInput: FCLInput? = null
    private val inputMethodManager: InputMethodManager by lazy {
        context.getSystemService() ?: error("No InputMethodManager service")
    }

    private var inputState: TextInputState? = null
    private var cursorRect = MutableStateFlow<FloatRect?>(null)
    private var inputAreaRect = MutableStateFlow<FloatRect?>(null)
    private var inputConnection: InputConnectionImpl? = null
    private val inputHandler = object : LauncherProxyClient.InputHandler {
        override fun updateState(textInputState: TextInputState?) {
            post {
                val prevState = inputState
                inputState = textInputState
                if (textInputState != null) {
                    visibility = VISIBLE
                    focusable = FOCUSABLE
                }
                when {
                    prevState == null && textInputState != null -> {
                        isFocusableInTouchMode = true
                        clearFocus()
                        requestFocus()
                        inputMethodManager.showSoftInput(
                            this@TouchControllerInputView,
                            InputMethodManager.SHOW_IMPLICIT
                        )
                    }

                    prevState != null && textInputState == null -> {
                        clearFocus()
                        inputMethodManager.hideSoftInputFromWindow(
                            windowToken,
                            InputMethodManager.HIDE_IMPLICIT_ONLY
                        )
                    }
                }
                if (textInputState != null) {
                    inputConnection?.updateState(textInputState)
                } else {
                    visibility = GONE
                    focusable = NOT_FOCUSABLE
                }
            }
        }

        override fun updateCursor(cursorRect: FloatRect?) {
            this@TouchControllerInputView.cursorRect.value = cursorRect
        }

        override fun updateArea(inputAreaRect: FloatRect?) {
            this@TouchControllerInputView.inputAreaRect.value = inputAreaRect
        }
    }

    override fun onCheckIsTextEditor() = true

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val client = client ?: return null
        val state = inputState ?: return null
        outAttrs.apply {
            initialSelStart = state.selection.start
            initialSelEnd = state.selection.end
            EditorInfoCompat.setInitialSurroundingText(this, state.text)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            imeOptions = if (disableFullScreenInput) {
                EditorInfo.IME_FLAG_NO_FULLSCREEN
            } else { 0 }
        }
        return InputConnectionImpl(state) {
            client.updateTextInputState(it)
        }.also {
            inputConnection = it
        }
    }

    private inner class InputConnectionImpl(
        private var state: TextInputState,
        private val onStateChanged: (TextInputState) -> Unit,
    ) : InputConnection {
        private val scope = MainScope()
        private val view: TouchControllerInputView
            get() = this@TouchControllerInputView
        private val fclInput: FCLInput?
            get() = this@TouchControllerInputView.fclInput
        private val clipboardManager: ClipboardManager? by lazy {
            context.getSystemService()
        }
        private var inBatchEdit: Int = 0
        private var delayedNewStateByBatchEdit: TextInputState? = null
        private var extractTextToken: Int? = null
        private var hasZeroExtractToken = false

        private fun TextRange.isEmpty() = length == 0
        private fun String.removeRange(range: TextRange) =
            substring(0, range.start) + substring(range.end)

        private fun String.substring(range: TextRange) = substring(range.start, range.end)
        private fun String.replaceRange(range: TextRange, newText: CharSequence) =
            substring(0, range.start) + newText + substring(range.end)

        private fun String.insertAt(index: Int, text: CharSequence) =
            substring(0, index) + text + substring(index)

        private operator fun TextRange.minus(other: TextRange): TextRange {
            val e1 = this.end
            val s2 = other.start
            val e2 = other.end

            val newStart = if (this.start < s2) {
                this.start
            } else {
                maxOf(this.start, e2) - other.length
            }

            val part1 = minOf(e1, s2) - this.start
            val part2 = e1 - maxOf(this.start, e2)

            val newLength = maxOf(0, part1) + maxOf(0, part2)
            return TextRange(newStart, newLength)
        }

        private fun FCLInput.sendKeyPress(keyCode: Int) {
            sendKeyEvent(keyCode, true)
            sendKeyEvent(keyCode, false)
        }

        init {
            refreshState()
            scope.launch {
                cursorRect.combine(inputAreaRect, ::Pair).collect { (cursorRect, inputAreaRect) ->
                    val state = state
                    inputMethodManager.updateCursorAnchorInfo(view, CursorAnchorInfo.Builder().apply {
                        setSelectionRange(state.selection.start, state.selection.end)
                        if (!state.composition.isEmpty()) {
                            setComposingText(state.composition.start, state.compositionText)
                        }
                        cursorRect?.let {
                            setInsertionMarkerLocation(
                                cursorRect.left * width,
                                cursorRect.top * height,
                                (cursorRect.left + cursorRect.width) * width,
                                (cursorRect.top + cursorRect.height) * height,
                                CursorAnchorInfo.FLAG_HAS_VISIBLE_REGION,
                            )
                        }
                        inputAreaRect?.let {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                setEditorBoundsInfo(EditorBoundsInfo.Builder().apply {
                                    setEditorBounds(
                                        RectF(
                                            inputAreaRect.left * width,
                                            inputAreaRect.top * height,
                                            (inputAreaRect.left + inputAreaRect.width) * width,
                                            (inputAreaRect.top + inputAreaRect.height) * height,
                                        )
                                    )
                                }.build())
                            }
                        }
                        setMatrix(view.matrix)
                    }.build())
                }
            }
        }

        private fun refreshState() {
            inputMethodManager.updateSelection(
                view,
                state.selection.start,
                state.selection.end,
                state.composition.start,
                state.composition.end
            )
            val extractedText = getExtractedText()
            if (hasZeroExtractToken) {
                inputMethodManager.updateExtractedText(view, 0, extractedText)
            }
            extractTextToken?.let { token ->
                inputMethodManager.updateExtractedText(view, token, extractedText)
            }
        }

        fun updateState(newState: TextInputState) {
            if (inBatchEdit > 0) {
                delayedNewStateByBatchEdit = newState
                return
            }
            if (state == newState) {
                return
            }
            if (state.text != newState.text) {
                inputMethodManager.restartInput(view)
            }
            state = newState
            refreshState()
        }

        private fun updateState(updater: (TextInputState) -> TextInputState) =
            updateState(refresh = true, updater = updater)

        private fun updateState(
            refresh: Boolean = true,
            updater: (TextInputState) -> TextInputState
        ): TextInputState {
            val newState = updater(state)
            state = newState
            if (inBatchEdit == 0) {
                if (refresh) {
                    refreshState()
                }
                onStateChanged(newState)
            }
            return newState
        }

        override fun beginBatchEdit(): Boolean {
            inBatchEdit++
            return true
        }

        override fun clearMetaKeyStates(states: Int): Boolean {
            return true
        }

        override fun closeConnection() {
            scope.cancel()
        }

        override fun commitCompletion(text: CompletionInfo?): Boolean {
            return true
        }

        override fun commitContent(
            inputContentInfo: InputContentInfo,
            flags: Int,
            opts: Bundle?,
        ): Boolean {
            return false
        }

        override fun commitCorrection(correctionInfo: CorrectionInfo?): Boolean {
            return false
        }

        private fun TextInputState.commitTextAsNewState(
            text: CharSequence,
            newCursorPosition: Int
        ) =
            if (composition.isEmpty()) {
                val newText = this.text.replaceRange(selection, text)
                val finalCursorPosition = when {
                    newCursorPosition > 0 -> selection.start + text.length + newCursorPosition - 1
                    else -> selection.start - newCursorPosition
                }.coerceIn(0..newText.length)
                TextInputState(
                    text = newText,
                    selection = TextRange(finalCursorPosition),
                    composition = TextRange.EMPTY,
                )
            } else {
                val newText = this.text.replaceRange(composition, text)
                val finalCursorPosition = when {
                    newCursorPosition > 0 -> composition.start + text.length + newCursorPosition - 1
                    else -> composition.start - newCursorPosition
                }.coerceIn(0..newText.length)
                TextInputState(
                    text = newText,
                    selection = TextRange(finalCursorPosition),
                    composition = TextRange.EMPTY,
                )
            }

        override fun commitText(text: CharSequence, newCursorPosition: Int): Boolean {
            var enterCount = 0
            val filteredText = text.filter {
                val isNewLine = it == '\n'
                if (isNewLine) {
                    enterCount++
                }
                !isNewLine
            }
            if (filteredText.isNotEmpty()) {
                updateState { currentState ->
                    currentState.commitTextAsNewState(filteredText, newCursorPosition)
                }
            }
            repeat(enterCount) {
                fclInput?.sendKeyPress(FCLKeycodes.KEY_ENTER)
            }
            return true
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            updateState { currentState ->
                val limitedBeforeLength = minOf(beforeLength, currentState.selection.start)
                val limitedAfterLength =
                    minOf(afterLength, currentState.text.length - currentState.selection.end)
                val beforeText =
                    currentState.text.substring(
                        0,
                        currentState.selection.start - limitedBeforeLength
                    )
                val selectedText = currentState.text.substring(currentState.selection)
                val afterText =
                    currentState.text.substring(currentState.selection.end + limitedAfterLength)
                val removedLeftRange =
                    TextRange(
                        currentState.selection.start - limitedBeforeLength,
                        limitedBeforeLength
                    )
                val removedRightRange = TextRange(currentState.selection.end, limitedAfterLength)
                TextInputState(
                    text = beforeText + selectedText + afterText,
                    selection = TextRange(
                        start = beforeText.length,
                        length = selectedText.length,
                    ),
                    composition = currentState.composition - removedRightRange - removedLeftRange,
                )
            }
            return true
        }

        override fun deleteSurroundingTextInCodePoints(
            beforeLength: Int,
            afterLength: Int
        ): Boolean {
            val currentState = state
            val text = currentState.text
            val selectionStart = currentState.selection.start

            var remainingBefore = beforeLength
            var charCountBefore = 0
            var index = selectionStart - 1

            while (remainingBefore > 0 && index >= 0) {
                val codePoint = Character.codePointBefore(text, index + 1)
                val charCount = Character.charCount(codePoint)
                charCountBefore += charCount
                index -= charCount
                remainingBefore--
            }

            var remainingAfter = afterLength
            var charCountAfter = 0
            index = selectionStart

            while (remainingAfter > 0 && index < text.length) {
                val codePoint = Character.codePointAt(text, index)
                val charCount = Character.charCount(codePoint)
                charCountAfter += charCount
                index += charCount
                remainingAfter--
            }

            return deleteSurroundingText(charCountBefore, charCountAfter)
        }

        override fun endBatchEdit(): Boolean {
            inBatchEdit--
            if (inBatchEdit == 0) {
                delayedNewStateByBatchEdit?.let { delayed ->
                    updateState(delayed)
                    delayedNewStateByBatchEdit = null
                } ?: run {
                    refreshState()
                    onStateChanged(state)
                }
            }
            return inBatchEdit > 0
        }

        override fun finishComposingText(): Boolean {
            updateState { currentState ->
                currentState.copy(composition = TextRange.EMPTY)
            }
            return true
        }

        override fun getCursorCapsMode(reqModes: Int): Int {
            return state.let { state ->
                TextUtils.getCapsMode(
                    state.text, if (state.selectionLeft) {
                        state.selection.start
                    } else {
                        state.selection.end
                    }, reqModes
                )
            }
        }

        private fun getExtractedText() = ExtractedText().apply {
            state.let {
                text = it.text
                selectionStart = it.selection.start
                selectionEnd = it.selection.end
                startOffset = 0
                partialStartOffset = -1
                partialEndOffset = 0
            }
        }

        override fun getExtractedText(request: ExtractedTextRequest, flags: Int): ExtractedText {
            if (request.token == 0) {
                hasZeroExtractToken = true
            } else {
                this.extractTextToken = request.token
            }
            return getExtractedText()
        }

        override fun getHandler() = null

        override fun getSelectedText(flags: Int): CharSequence? {
            return if (!state.selection.isEmpty()) {
                state.text.substring(state.selection)
            } else {
                null
            }
        }

        override fun getTextAfterCursor(n: Int, flags: Int): CharSequence {
            val start = state.selection.end
            val end = (start + n).coerceAtMost(state.text.length)
            return state.text.substring(start, end)
        }

        override fun getTextBeforeCursor(n: Int, flags: Int): CharSequence {
            val end = state.selection.start
            val start = (end - n).coerceAtLeast(0)
            return state.text.substring(start, end)
        }

        override fun performContextMenuAction(id: Int): Boolean {
            when (id) {
                android.R.id.selectAll -> {
                    updateState { state ->
                        TextInputState(
                            text = state.text,
                            selection = TextRange(0, state.text.length),
                            composition = TextRange.EMPTY,
                        )
                    }
                }

                android.R.id.cut -> {
                    val cutText = state.selectionText
                    updateState(state.let { state ->
                        TextInputState(
                            text = state.text.removeRange(state.selection),
                            selection = TextRange(state.selection.start),
                            composition = state.composition - state.selection,
                        )
                    })
                    clipboardManager?.setPrimaryClip(ClipData.newPlainText(null, cutText))
                }

                android.R.id.copy -> {
                    clipboardManager?.setPrimaryClip(
                        ClipData.newPlainText(
                            null,
                            state.selectionText
                        )
                    )
                }

                android.R.id.paste -> {
                    clipboardManager?.primaryClip?.takeIf { it.itemCount > 0 }
                        ?.getItemAt(0)?.text?.toString()?.let { text ->
                            updateState(state.commitTextAsNewState(text, 1))
                        }
                }

                else -> return false
            }
            return true
        }

        override fun performEditorAction(editorAction: Int): Boolean {
            return false
        }

        override fun performPrivateCommand(action: String?, data: Bundle?): Boolean {
            return false
        }

        override fun reportFullscreenMode(enabled: Boolean): Boolean {
            if (!inputMethodManager.isFullscreenMode) {
                extractTextToken = null
            }
            return true
        }

        override fun requestCursorUpdates(cursorUpdateMode: Int): Boolean {
            return false
        }

        override fun sendKeyEvent(event: KeyEvent): Boolean {
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.action == KeyEvent.ACTION_UP) {
                    return true
                }
                fclInput?.sendKeyPress(FCLKeycodes.KEY_ENTER)
            } else if (event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (event.action == KeyEvent.ACTION_UP) {
                    return true
                }
                if (event.isShiftPressed) {
                    updateState(TextInputState::doShiftLeft)
                } else {
                    updateState(TextInputState::doArrowLeft)
                }
            } else if (event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (event.action == KeyEvent.ACTION_UP) {
                    return true
                }
                if (event.isShiftPressed) {
                    updateState(TextInputState::doShiftRight)
                } else {
                    updateState(TextInputState::doArrowRight)
                }
            } else if (event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (event.action == KeyEvent.ACTION_UP) {
                    return true
                }
                updateState(TextInputState::doBackspace)
            } else if (event.keyCode == KeyEvent.KEYCODE_FORWARD_DEL) {
                if (event.action == KeyEvent.ACTION_UP) {
                    return true
                }
                updateState(TextInputState::doDelete)
            } else {
                fclInput?.handleKeyEvent(event)
            }
            return true
        }

        override fun setComposingRegion(start: Int, end: Int): Boolean {
            updateState(refresh = false) { currentState ->
                currentState.copy(
                    composition = TextRange(start, end - start)
                )
            }
            return true
        }

        override fun setComposingText(text: CharSequence, newCursorPosition: Int): Boolean {
            updateState { currentState ->
                if (!currentState.composition.isEmpty()) {
                    val newText = currentState.text.replaceRange(currentState.composition, text)
                    val finalCursorPosition = when {
                        newCursorPosition > 0 -> currentState.composition.start + text.length + newCursorPosition - 1
                        else -> currentState.composition.start - newCursorPosition
                    }.coerceIn(0..newText.length)
                    TextInputState(
                        text = newText,
                        selection = TextRange(finalCursorPosition),
                        composition = TextRange(currentState.composition.start, text.length),
                    )
                } else {
                    val newText = currentState.text.replaceRange(currentState.selection, text)
                    val finalCursorPosition = when {
                        newCursorPosition > 0 -> currentState.selection.start + text.length + newCursorPosition - 1
                        else -> currentState.selection.start - newCursorPosition
                    }.coerceIn(0..newText.length)
                    TextInputState(
                        text = newText,
                        selection = TextRange(finalCursorPosition),
                        composition = TextRange(currentState.selection.start, text.length),
                    )
                }
            }
            return true
        }

        override fun setSelection(start: Int, end: Int): Boolean {
            updateState { currentState ->
                currentState.copy(
                    selection = TextRange(start, end - start)
                )
            }
            return true
        }
    }
}