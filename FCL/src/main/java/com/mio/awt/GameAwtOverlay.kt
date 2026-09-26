package com.mio.awt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.TextureView
import android.view.View

import com.tungsten.fcl.R
import com.tungsten.fcl.control.AWTInput
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.keycodes.AWTInputEvent

import kotlin.concurrent.thread
import androidx.core.graphics.createBitmap

/**
 * 游戏内 AWT/Swing 窗口悬浮层
 *
 * 后台轮询游戏 JVM 内 cacio 的虚拟屏缓冲与可见顶层窗口矩形，按窗口矩形裁剪绘制到游戏画面之上；
 * 矩形内触摸转发为 AWT 鼠标输入，矩形外透传给下层视图。
 * 无窗口时不绘制窗口内容，仅显示左上角测试入口；含虚拟屏坐标到本视图坐标的映射（跟随 TextureView 位置与尺寸）。
 */
class GameAwtOverlay(context: Context) : View(context) {

    private var bridge: FCLBridge? = null
    private var textureView: TextureView? = null
    private var pollThread: Thread? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    // 以下状态仅 UI 线程访问
    private var screenW = 0
    private var screenH = 0
    private var bitmap: Bitmap? = null
    private val srcRects = ArrayList<Rect>()
    private val dstRects = ArrayList<RectF>()
    private var cursorVisible = false
    private var cursorX = 0f
    private var cursorY = 0f
    private val chipRect = RectF()
    private val chipText = context.getString(R.string.awt_overlay_test)

    private val paint = Paint().apply { isFilterBitmap = true }
    private val cursorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xE6FFFFFF.toInt()
    }
    private val chipBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x99323232.toInt()
    }
    private val chipTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 14f * resources.displayMetrics.density
    }

    init {
        isClickable = true
    }

    /**
     * 开始轮询
     */
    fun start(bridge: FCLBridge, textureView: TextureView) {
        this.bridge = bridge
        this.textureView = textureView
        pollThread = thread(name = "AwtOverlayPoll", isDaemon = true) { pollLoop() }
    }

    /**
     * 停止轮询
     */
    fun stop() {
        pollThread?.interrupt()
        pollThread = null
    }

    private fun pollLoop() {
        val bridge = bridge ?: return
        while (!Thread.currentThread().isInterrupted) {
            try {
                val info = bridge.nativePollAwtFrame()
                if (info == null || info.size < 3 || info[0] <= 0 || info[1] <= 0) {
                    mainHandler.post { clearWindows() }
                    Thread.sleep(IDLE_INTERVAL)
                } else {
                    mainHandler.post { updateFrame(info) }
                    Thread.sleep(ACTIVE_INTERVAL)
                }
            } catch (e: InterruptedException) {
                break
            }
        }
    }

    private fun clearWindows() {
        srcRects.clear()
        dstRects.clear()
        cursorVisible = false
        invalidate()
    }

    private fun updateFrame(info: IntArray) {
        val tex = textureView ?: return
        val sw = info[0]
        val sh = info[1]
        val count = info[2]
        val rectEnd = 3 + count * 4
        if (tex.width <= 0 || tex.height <= 0 || rectEnd > info.size) {
            clearWindows()
            return
        }
        screenW = sw
        screenH = sh
        if (info.size - rectEnd >= sw * sh) {
            val rgb = info.copyOfRange(rectEnd, rectEnd + sw * sh)
            val old = bitmap
            if (old != null && old.width == sw && old.height == sh) {
                old.setPixels(rgb, 0, sw, 0, 0, sw, sh)
            } else {
                old?.recycle()
                bitmap = createBitmap(sw, sh)
                    .also { it.setPixels(rgb, 0, sw, 0, 0, sw, sh) }
            }
        }
        if (bitmap == null) {
            // 已有窗口但首帧尚未产出，等待下一轮
            return
        }
        val texX = tex.x
        val texY = tex.y
        val scaleX = tex.width.toFloat() / sw
        val scaleY = tex.height.toFloat() / sh
        srcRects.clear()
        dstRects.clear()
        var i = 3
        while (i + 3 < rectEnd) {
            // 窗口矩形可能越出虚拟屏，先裁剪到屏内
            val left = info[i].coerceIn(0, sw)
            val top = info[i + 1].coerceIn(0, sh)
            val right = (info[i] + info[i + 2]).coerceIn(0, sw)
            val bottom = (info[i + 1] + info[i + 3]).coerceIn(0, sh)
            if (right > left && bottom > top) {
                srcRects.add(Rect(left, top, right, bottom))
                dstRects.add(
                    RectF(
                        texX + left * scaleX,
                        texY + top * scaleY,
                        texX + right * scaleX,
                        texY + bottom * scaleY
                    )
                )
            }
            i += 4
        }
        invalidate()
    }

    private fun sendAwtMouse(x: Float, y: Float, buttonDown: Boolean?) {
        val bridge = bridge ?: return
        val tex = textureView ?: return
        if (screenW <= 0 || screenH <= 0 || tex.width <= 0 || tex.height <= 0) {
            return
        }
        val cx = ((x - tex.x) / tex.width * screenW).toInt().coerceIn(0, screenW - 1)
        val cy = ((y - tex.y) / tex.height * screenH).toInt().coerceIn(0, screenH - 1)
        bridge.nativeSendData(AWTInput.EVENT_TYPE_CURSOR_POS, cx, cy, 0, 0)
        if (buttonDown != null) {
            bridge.nativeSendData(
                AWTInput.EVENT_TYPE_MOUSE_BUTTON,
                AWTInputEvent.BUTTON1_DOWN_MASK,
                if (buttonDown) 1 else 0,
                0,
                0
            )
        }
        cursorX = x
        cursorY = y
        cursorVisible = true
    }

    fun showTestDialog() {
        val bridge = bridge ?: return
        android.util.Log.e("FCL-AWT", "showTestDialog triggered")
        val title = context.getString(R.string.awt_test_dialog_title)
        val message = context.getString(R.string.awt_test_dialog_message)
        thread(name = "AwtTestDialog") {
            bridge.nativeShowTestDialog(title, message)
        }
    }

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (event.actionMasked == MotionEvent.ACTION_DOWN && chipRect.contains(x, y)) {
            showTestDialog()
            return true
        }
        // 未命中任何 AWT 窗口矩形时透传给下层（游戏/菜单）
        if (dstRects.lastOrNull { it.contains(x, y) } == null) {
            return false
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> sendAwtMouse(x, y, true)
            MotionEvent.ACTION_MOVE -> sendAwtMouse(x, y, null)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> sendAwtMouse(x, y, false)
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        val frame = bitmap
        if (frame != null) {
            for (i in srcRects.indices) {
                canvas.drawBitmap(frame, srcRects[i], dstRects[i], paint)
            }
        }
        if (cursorVisible) {
            canvas.drawCircle(cursorX, cursorY, CURSOR_RADIUS * resources.displayMetrics.density, cursorPaint)
        }
        drawTestChip(canvas)
    }

    private fun drawTestChip(canvas: Canvas) {
        if (chipRect.isEmpty) {
            val density = resources.displayMetrics.density
            val bounds = Rect()
            chipTextPaint.getTextBounds(chipText, 0, chipText.length, bounds)
            val left = CHIP_MARGIN * density
            val top = CHIP_MARGIN * density
            chipRect.set(
                left,
                top,
                left + bounds.width() + 24f * density,
                top + bounds.height() + 14f * density
            )
        }
        canvas.drawRoundRect(chipRect, chipRect.height() / 2, chipRect.height() / 2, chipBgPaint)
        canvas.drawText(
            chipText,
            chipRect.centerX(),
            chipRect.centerY() - (chipTextPaint.ascent() + chipTextPaint.descent()) / 2,
            chipTextPaint
        )
    }

    companion object {
        private const val IDLE_INTERVAL = 300L
        private const val ACTIVE_INTERVAL = 50L
        private const val CURSOR_RADIUS = 6f
        private const val CHIP_MARGIN = 16f
    }
}
