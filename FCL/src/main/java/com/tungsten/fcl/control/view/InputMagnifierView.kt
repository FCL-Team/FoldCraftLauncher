package com.tungsten.fcl.control.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.SystemClock
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.TextureView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.control.keyboard.TouchCharInput
import com.tungsten.fcl.setting.MenuSetting
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLCheckBox
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.util.ConvertUtils
import org.libsdl.app.SDLActivity
import androidx.core.graphics.createBitmap
import androidx.core.view.isVisible
import kotlin.math.roundToInt

class InputMagnifierView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewport = MagnifierViewport(context)
    private val magnifierWindow = FrameLayout(context)
    private val showButton = FrameLayout(context)
    private val settingsPanel = LinearLayout(context)
    private val autoShowCheckBox = FCLCheckBox(context)
    private val visibleFrame = Rect()
    private val locationOnScreen = IntArray(2)
    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener { updateInputState() }

    private var registeredObserver: ViewTreeObserver? = null
    private var textureView: TextureView? = null
    private var touchCharInput: TouchCharInput? = null
    private var menuSetting: MenuSetting? = null
    private var frame: Bitmap? = null
    private var dismissedForInput = false
    private var inputOccludesGame = false
    private var lastCaptureTime = 0L

    init {
        isFocusable = false
        descendantFocusability = FOCUS_BLOCK_DESCENDANTS

        magnifierWindow.clipToOutline = true
        magnifierWindow.elevation = dp(6f).toFloat()
        magnifierWindow.addView(
            viewport,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
        val windowParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
            Gravity.CENTER_HORIZONTAL
        )
        windowParams.topMargin = dp(12f)
        windowParams.bottomMargin = dp(12f)
        addView(magnifierWindow, windowParams)

        val toolbar = LinearLayout(context)
        toolbar.orientation = LinearLayout.HORIZONTAL
        toolbar.background = roundedBackground(Color.argb(166, 0, 0, 0), 8f)
        val menuButton =
            createIconButton(R.drawable.ic_baseline_more_horiz_24, R.string.control_info_more)
        menuButton.setOnClickListener { toggleSettingsPanel() }
        toolbar.addView(menuButton, LinearLayout.LayoutParams(dp(40f), dp(40f)))
        val closeButton = createIconButton(R.drawable.ic_baseline_close_24, R.string.close)
        closeButton.setOnClickListener { close() }
        toolbar.addView(closeButton, LinearLayout.LayoutParams(dp(40f), dp(40f)))
        val toolbarParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.END
        )
        toolbarParams.setMargins(dp(8f), dp(8f), dp(8f), dp(8f))
        magnifierWindow.addView(toolbar, toolbarParams)

        settingsPanel.orientation = LinearLayout.VERTICAL
        settingsPanel.background = roundedBackground(Color.argb(217, 0, 0, 0), 8f)
        settingsPanel.setPadding(dp(12f), dp(4f), dp(12f), dp(4f))
        settingsPanel.visibility = GONE
        autoShowCheckBox.setText(R.string.menu_settings_ime_magnifier_auto_show)
        autoShowCheckBox.setTextColor(Color.WHITE)
        autoShowCheckBox.isFocusable = false
        settingsPanel.addView(
            autoShowCheckBox,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        val settingsParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.END
        )
        settingsParams.topMargin = dp(56f)
        settingsParams.rightMargin = dp(8f)
        magnifierWindow.addView(settingsPanel, settingsParams)

        showButton.background = roundedBackground(Color.argb(140, 0, 0, 0), 20f)
        val showIcon =
            createIconButton(R.drawable.ic_baseline_search_24, R.string.game_magnifier_show)
        showIcon.setOnClickListener { setMagnifierVisible(true) }
        showButton.addView(showIcon, LayoutParams(dp(40f), dp(40f)))
        val showParams = LayoutParams(dp(40f), dp(40f), Gravity.BOTTOM or Gravity.END)
        showParams.setMargins(dp(16f), dp(16f), dp(16f), dp(16f))
        addView(showButton, showParams)

        visibility = GONE
        ThemeEngine.getInstance().registerEvent(this) { refreshTheme() }
    }

    fun setup(
        textureView: TextureView,
        touchCharInput: TouchCharInput,
        menuSetting: MenuSetting,
    ) {
        this.textureView = textureView
        this.touchCharInput = touchCharInput
        this.menuSetting = menuSetting
        autoShowCheckBox.isChecked = menuSetting.isImeMagnifierAutoShow
        autoShowCheckBox.setOnCheckedChangeListener { _, checked ->
            menuSetting.isImeMagnifierAutoShow = checked
        }
        updateInputState()
    }

    @JvmOverloads
    fun captureFrame(force: Boolean = false) {
        if (!isMagnifierVisible()) {
            return
        }
        val textureView = textureView ?: return
        val now = SystemClock.elapsedRealtime()
        if (!force && now - lastCaptureTime < CAPTURE_INTERVAL_MS) {
            return
        }
        lastCaptureTime = now
        val width = textureView.width
        val height = textureView.height
        if (!textureView.isAvailable || width <= 0 || height <= 0) {
            return
        }
        var target = frame
        if (target == null || target.width != width || target.height != height) {
            target = createBitmap(width, height)
        }
        val captured = try {
            textureView.getBitmap(target)
        } catch (_: RuntimeException) {
            return
        }
        if (captured != null) {
            frame = captured
            viewport.setFrame(captured)
        }
    }

    fun isMagnifierVisible(): Boolean =
        isVisible && magnifierWindow.isVisible

    private fun close() {
        dismissedForInput = true
        setMagnifierVisible(false)
    }

    private fun toggleSettingsPanel() {
        settingsPanel.visibility = if (settingsPanel.isVisible) GONE else VISIBLE
    }

    private fun setMagnifierVisible(visible: Boolean) {
        magnifierWindow.visibility = if (visible) VISIBLE else GONE
        showButton.visibility = if (visible) GONE else VISIBLE
        if (visible) {
            dismissedForInput = false
            captureFrame(force = true)
        } else {
            settingsPanel.visibility = GONE
        }
    }

    private fun updateInputState() {
        val touchCharInput = touchCharInput ?: return
        val menuSetting = menuSetting ?: return

        rootView.getWindowVisibleDisplayFrame(visibleFrame)
        val inputActive = touchCharInput.isEnabled || SDLActivity.isSDLEditKeyboardShown()
        if (!inputActive) {
            dismissedForInput = false
        }
        val occludes = inputActive && rootView.height > 0
                && rootView.height * 2 / 3 > visibleFrame.bottom
        if (!occludes) {
            inputOccludesGame = false
            visibility = GONE
            return
        }

        getLocationOnScreen(locationOnScreen)
        val availableHeight = maxOf(0, minOf(height, visibleFrame.bottom - locationOnScreen[1]))
        val bottomPadding = maxOf(0, height - availableHeight)
        if (paddingBottom != bottomPadding) {
            setPadding(0, 0, 0, bottomPadding)
        }
        val windowWidth = (width * 0.6f).roundToInt()
        val windowParams = magnifierWindow.layoutParams
        if (windowParams.width != windowWidth) {
            windowParams.width = windowWidth
            magnifierWindow.layoutParams = windowParams
        }
        visibility = VISIBLE
        if (!inputOccludesGame) {
            inputOccludesGame = true
            setMagnifierVisible(!dismissedForInput && menuSetting.isImeMagnifierAutoShow)
        }
    }

    private fun refreshTheme() {
        // 背景仅兜底;描边走 foreground,否则会被 viewport 全幅画面盖住
        magnifierWindow.background = roundedBackground(Color.BLACK, 12f)
        magnifierWindow.foreground = roundedForeground()
    }

    private fun createIconButton(iconRes: Int, descriptionRes: Int): FCLImageButton {
        val button = FCLImageButton(context)
        button.setImageResource(iconRes)
        button.imageTintList = ColorStateList.valueOf(Color.WHITE)
        button.contentDescription = context.getString(descriptionRes)
        button.isFocusable = false
        return button
    }

    private fun roundedBackground(color: Int, radiusDp: Float): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = dp(radiusDp).toFloat()
        return drawable
    }

    private fun roundedForeground(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setColor(Color.TRANSPARENT)
        drawable.cornerRadius = dp(12f).toFloat()
        drawable.setStroke(dp(3f), Color.WHITE)
        return drawable
    }

    private fun dp(value: Float): Int = ConvertUtils.dip2px(context, value)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        registeredObserver = viewTreeObserver.also { it.addOnGlobalLayoutListener(layoutListener) }
    }

    override fun onDetachedFromWindow() {
        val observer = registeredObserver
        if (observer != null && observer.isAlive) {
            observer.removeOnGlobalLayoutListener(layoutListener)
        }
        registeredObserver = null
        super.onDetachedFromWindow()
    }

    private class MagnifierViewport(context: Context) : View(context) {

        private val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        private val sourceRect = Rect()
        private val destinationRect = Rect()
        private val scaleDetector: ScaleGestureDetector

        private var frame: Bitmap? = null
        private var zoom = MIN_ZOOM
        private var sourceCenterX = 0f
        private var sourceCenterY = 1.0f
        private var lastCentroidX = 0f
        private var lastCentroidY = 0f
        private var pendingPanX = 0f
        private var pendingPanY = 0f

        init {
            setBackgroundColor(Color.BLACK)
            isFocusable = false
            isClickable = true
            scaleDetector = ScaleGestureDetector(
                context,
                object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        applyGesture(
                            detector.scaleFactor, detector.focusX, detector.focusY,
                            pendingPanX, pendingPanY
                        )
                        return true
                    }
                }
            )
        }

        fun setFrame(frame: Bitmap) {
            this.frame = frame
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val bmp = frame
            if (bmp == null || bmp.width <= 0 || bmp.height <= 0 || width <= 0 || height <= 0) {
                return
            }
            val sourceWidth = sourceWidth(zoom)
            val sourceHeight = sourceHeight(zoom)
            val centerX = clampedCenterX(sourceWidth)
            val centerY = clampedCenterY(sourceHeight)
            val left =
                clampInt((centerX - sourceWidth / 2.0f).roundToInt(), 0, bmp.width - sourceWidth)
            val top =
                clampInt((centerY - sourceHeight / 2.0f).roundToInt(), 0, bmp.height - sourceHeight)
            sourceRect.set(left, top, left + sourceWidth, top + sourceHeight)
            destinationRect.set(0, 0, width, height)
            canvas.drawBitmap(bmp, sourceRect, destinationRect, paint)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent): Boolean {
            val action = event.actionMasked
            pendingPanX = 0f
            pendingPanY = 0f
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    parent?.requestDisallowInterceptTouchEvent(true)
                    lastCentroidX = event.x
                    lastCentroidY = event.y
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    lastCentroidX = centroid(event, true)
                    lastCentroidY = centroid(event, false)
                }

                MotionEvent.ACTION_MOVE -> {
                    val centroidX = centroid(event, true)
                    val centroidY = centroid(event, false)
                    pendingPanX = centroidX - lastCentroidX
                    pendingPanY = centroidY - lastCentroidY
                    lastCentroidX = centroidX
                    lastCentroidY = centroidY
                }
            }

            scaleDetector.onTouchEvent(event)
            if (action == MotionEvent.ACTION_MOVE && event.pointerCount == 1) {
                applyGesture(1.0f, lastCentroidX, lastCentroidY, pendingPanX, pendingPanY)
            }
            if (action == MotionEvent.ACTION_POINTER_UP) {
                lastCentroidX = centroidExcluding(event, event.actionIndex, true)
                lastCentroidY = centroidExcluding(event, event.actionIndex, false)
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                finishGesture()
            }
            return true
        }

        private fun applyGesture(
            scaleFactor: Float,
            focusX: Float,
            focusY: Float,
            panX: Float,
            panY: Float,
        ) {
            val bmp = frame ?: return
            if (width <= 0 || height <= 0) {
                return
            }
            val oldZoom = zoom
            val newZoom = clampFloat(oldZoom * scaleFactor, MIN_ZOOM, MAX_ZOOM)
            val oldCenterX = clampedCenterX(sourceWidth(oldZoom))
            val oldCenterY = clampedCenterY(sourceHeight(oldZoom))
            val focusOffsetX = focusX - width / 2.0f
            val focusOffsetY = focusY - height / 2.0f
            val newCenterX =
                oldCenterX + focusOffsetX / oldZoom - focusOffsetX / newZoom - panX * PAN_DAMPING / newZoom
            val newCenterY =
                oldCenterY + focusOffsetY / oldZoom - focusOffsetY / newZoom - panY * PAN_DAMPING / newZoom
            zoom = newZoom
            sourceCenterX = clampFloat(newCenterX / bmp.width, 0.0f, 1.0f)
            sourceCenterY = clampFloat(newCenterY / bmp.height, 0.0f, 1.0f)
            invalidate()
        }

        private fun finishGesture() {
            val bmp = frame ?: return
            if (width <= 0 || height <= 0) {
                return
            }
            sourceCenterX = clampedCenterX(sourceWidth(zoom)) / bmp.width
            sourceCenterY = clampedCenterY(sourceHeight(zoom)) / bmp.height
        }

        private fun sourceWidth(zoom: Float): Int {
            val bmp = frame ?: return 1
            return clampInt((width * fitScale(bmp) / zoom).roundToInt(), 1, bmp.width)
        }

        private fun sourceHeight(zoom: Float): Int {
            val bmp = frame ?: return 1
            return clampInt((height * fitScale(bmp) / zoom).roundToInt(), 1, bmp.height)
        }

        /** 画面等比缩入帧内的系数。取样区域 = 视口尺寸 * 该系数 / zoom:
         *  zoom=1 时低分辨率帧整体可见(概览),此后任何放大都立即裁切,
         *  不存在"整帧适配被钉死、捏合无反应"的死区;区域宽高比恒等于视口,不会拉伸 */
        private fun fitScale(bmp: Bitmap): Float =
            1.0f.coerceAtMost(
                (bmp.width / width.toFloat()).coerceAtMost(bmp.height / height.toFloat())
            )

        private fun clampedCenterX(sourceWidth: Int): Float {
            val bmp = frame ?: return 0.0f
            val halfWidth = sourceWidth / 2.0f
            return clampFloat(
                sourceCenterX * bmp.width, halfWidth,
                halfWidth.coerceAtLeast(bmp.width - halfWidth)
            )
        }

        private fun clampedCenterY(sourceHeight: Int): Float {
            val bmp = frame ?: return 0.0f
            val halfHeight = sourceHeight / 2.0f
            return clampFloat(
                sourceCenterY * bmp.height, halfHeight,
                halfHeight.coerceAtLeast(bmp.height - halfHeight)
            )
        }

        private fun centroid(event: MotionEvent, x: Boolean): Float =
            centroidExcluding(event, -1, x)

        private fun centroidExcluding(event: MotionEvent, excludedIndex: Int, x: Boolean): Float {
            var sum = 0.0f
            var count = 0
            for (i in 0 until event.pointerCount) {
                if (i != excludedIndex) {
                    sum += if (x) event.getX(i) else event.getY(i)
                    count++
                }
            }
            return if (count == 0) 0.0f else sum / count
        }

        private fun clampFloat(value: Float, min: Float, max: Float): Float =
            min.coerceAtLeast(max.coerceAtMost(value))

        private fun clampInt(value: Int, min: Int, max: Int): Int =
            min.coerceAtLeast(max.coerceAtMost(value))

        companion object {
            private const val MIN_ZOOM = 1.0f
            private const val MAX_ZOOM = 4.0f

            /** 平移阻尼:画面按手指移动量的一半跟随,小窗口内更容易滑到目标位置 */
            private const val PAN_DAMPING = 0.8f
        }
    }

    companion object {
        /** 逐帧全屏回读开销大,放大镜画面按 ~30fps 节流即可 */
        private const val CAPTURE_INTERVAL_MS = 33L
    }
}
