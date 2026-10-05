package com.tungsten.fcl.control

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tungsten.fcl.control.data.QuickInputTexts
import com.tungsten.fcl.databinding.DialogQuickInputBinding
import com.tungsten.fcl.game.sdl.SdlBridge
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.keycodes.FCLKeycodes
import com.tungsten.fclauncher.keycodes.MinecraftKeyBindingMapper
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.libsdl.app.SDLActivity

class QuickInputDialog(private val activity: AppCompatActivity, private val menu: GameMenu) :
    FCLDialog(activity),
    View.OnClickListener {
    private val binding: DialogQuickInputBinding

    init {
        setCancelable(false)
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        binding = DialogQuickInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addText.setOnClickListener(this)
        binding.positive.setOnClickListener(this)

        refreshList(menu)
    }

    private fun refreshList(menu: GameMenu) {
        val adapter = InputTextAdapter(
            context,
            QuickInputTexts.getInputTexts()
        ) {
            if (it.isNotEmpty()) {
                if (menu.cursorMode == FCLBridge.CursorEnabled) {
                    sendText(it)
                } else {
                    val gameOption = menu.gameOption
                    menu.input.sendBoundKeyEvent(
                        gameOption,
                        MinecraftKeyBindingMapper.BINDING_CHAT,
                        FCLKeycodes.KEY_T,
                        true
                    )
                    menu.input.sendBoundKeyEvent(
                        gameOption,
                        MinecraftKeyBindingMapper.BINDING_CHAT,
                        FCLKeycodes.KEY_T,
                        false
                    )
                    activity.lifecycleScope.launch {
                        if (!awaitTextInputChannel()) return@launch
                        sendText(it)
                        menu.input.sendKeyEvent(FCLKeycodes.KEY_ENTER, true)
                        menu.input.sendKeyEvent(FCLKeycodes.KEY_ENTER, false)

                    }
                }
            }

            dismiss()
        }
        binding.list.setAdapter(adapter)
    }

    private fun sendText(text: String) {
        if (SdlBridge.sdlEnabled && SdlBridge.isSdlRenderActive()) {
            // SDL 渲染路径（MC 26.3+）不注册 GLFW 字符回调，字符须经 SDL 文本输入通道提交
            Log.i(TAG, "commit text via SDL, length=${text.length}")
            SDLActivity.commitTextInput(text)
        } else {
            text.forEach { s ->
                menu.input.sendChar(s)
            }
        }
    }

    /**
     * 等待聊天栏打开后游戏侧激活 SDL 文本输入通道；GLFW 路径无通道概念，仅保留原 50ms 延时。
     * 判定用 native 真实激活状态，Java 侧镜像受软键盘 hint 影响可能缺失
     */
    private suspend fun awaitTextInputChannel(): Boolean {
        if (!(SdlBridge.sdlEnabled && SdlBridge.isSdlRenderActive())) {
            delay(50)
            return true
        }
        var waited = 0L
        while (waited < CHANNEL_WAIT_TIMEOUT_MS && !SdlBridge.isNativeTextInputActive()) {
            delay(CHANNEL_POLL_INTERVAL_MS)
            waited += CHANNEL_POLL_INTERVAL_MS
        }
        if (!SdlBridge.isNativeTextInputActive()) {
            Log.w(TAG, "native text input channel not active after ${waited}ms, text dropped")
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addText -> AddInputTextDialog(
                context
            ) { refreshList(menu) }.show()

            binding.positive -> dismiss()
        }
    }

    companion object {
        private const val TAG = "FCL"
        private const val CHANNEL_WAIT_TIMEOUT_MS = 1000L
        private const val CHANNEL_POLL_INTERVAL_MS = 16L
    }
}
