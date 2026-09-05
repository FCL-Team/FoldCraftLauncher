package com.mio.skin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLSurface
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.TextureView
import android.view.ViewConfiguration
import com.tungsten.fclcore.util.Logging
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.math.sqrt

/**
 * 皮肤 3D 预览容器（TextureView + EGL14 + 独立渲染线程），
 * 替代旧 GLSurfaceView 实现：setOpaque(false) 参与视图层级透明合成，
 * Choreographer 驱动 vsync 步进的连续渲染（等价 RENDERMODE_CONTINUOUSLY）。
 *
 * 生命周期：onResume/onPause 控制帧循环（surface 未就绪时记忆状态）；
 * surface 销毁时在 UI 线程同步等待渲染线程释放 EGL，避免SurfaceTexture 过早释放。
 * 手势与旧版一致：单指拖动旋转、双指捏合缩放（0.7~2.0）。
 */
class SkinViewer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextureView(context, attrs), TextureView.SurfaceTextureListener {

    private var renderer: SkinRenderer? = null
    private var density = 1f

    /** 双击模型回调（UI 线程，SAM 接口便于 Java 侧 lambda 调用） */
    fun interface OnDoubleClickListener {
        fun onDoubleClick()
    }

    var onDoubleClick: OnDoubleClickListener? = null

    // 触摸状态（仅 UI 线程访问）
    private var previousX = 0f
    private var previousY = 0f
    private var primaryPointerId = 0
    private var secondaryPointerId = 0
    private var pinchStartDistance = 0.0
    private var pinchStartScale = 1f
    private var lastTapTime = 0L
    private var lastTapX = 0f
    private var lastTapY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    // 渲染线程（renderHandler 仅在 renderThread 非 null 时有效）
    private var renderThread: HandlerThread? = null
    private var renderHandler: Handler? = null
    private var choreographer: Choreographer? = null
    private var frameCallback: Choreographer.FrameCallback? = null
    private var lastFrameNanos = 0L
    @Volatile
    private var frameLoopRunning = false
    @Volatile
    private var resumeRequested = false

    // EGL 资源（仅渲染线程访问）
    private var eglDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface = EGL14.EGL_NO_SURFACE

    init {
        setOpaque(false)
        isClickable = true
        surfaceTextureListener = this
    }

    /** 绑定渲染器与手势密度（须在 surface 可用前调用） */
    fun setRenderer(renderer: SkinRenderer, density: Float) {
        this.renderer = renderer
        this.density = density
    }

    fun onResume() {
        resumeRequested = true
        renderHandler?.post { startFrameLoop() }
    }

    fun onPause() {
        resumeRequested = false
        renderHandler?.post { stopFrameLoop() }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        try {
            if (event.pointerCount == 1) {
                val x = event.x
                val y = event.y
                if (event.action == MotionEvent.ACTION_DOWN) {
                    // 双击检测：两次按下间隔短且位置接近
                    val now = System.currentTimeMillis()
                    val dx = x - lastTapX
                    val dy = y - lastTapY
                    if (now - lastTapTime < DOUBLE_TAP_INTERVAL && dx * dx + dy * dy < touchSlop * touchSlop) {
                        lastTapTime = 0L
                        onDoubleClick?.onDoubleClick()
                    } else {
                        lastTapTime = now
                        lastTapX = x
                        lastTapY = y
                    }
                }
                if (event.action == MotionEvent.ACTION_MOVE) {
                    renderer?.rotateStep((x - previousX) / density, (y - previousY) / density)
                }
                previousX = x
                previousY = y
            } else if (event.pointerCount == 2) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN ->
                        primaryPointerId = event.getPointerId(event.actionIndex)
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        secondaryPointerId = event.getPointerId(event.actionIndex)
                        val deltaX = event.getX(event.findPointerIndex(primaryPointerId)) -
                            event.getX(event.findPointerIndex(secondaryPointerId))
                        val deltaY = event.getY(event.findPointerIndex(primaryPointerId)) -
                            event.getY(event.findPointerIndex(secondaryPointerId))
                        pinchStartDistance = sqrt((deltaX * deltaX + deltaY * deltaY).toDouble())
                        pinchStartScale = renderer?.getScale() ?: 1f
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.getX(event.findPointerIndex(primaryPointerId)) -
                            event.getX(event.findPointerIndex(secondaryPointerId))
                        val deltaY = event.getY(event.findPointerIndex(primaryPointerId)) -
                            event.getY(event.findPointerIndex(secondaryPointerId))
                        val distance = sqrt((deltaX * deltaX + deltaY * deltaY).toDouble())
                        val delta = (distance - pinchStartDistance).toFloat()
                        val diagonal = sqrt((width * width + height * height).toDouble()).toFloat()
                        renderer?.setScale(pinchStartScale + delta / diagonal)
                    }
                }
            }
        } catch (ignored: Throwable) {
        }
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        startRenderThread(surface, width, height)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        renderHandler?.post { renderer?.onSurfaceChanged(width, height) }
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        stopRenderThread()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    // ---- 渲染线程管理 ----

    private fun startRenderThread(surface: SurfaceTexture, width: Int, height: Int) {
        val thread = HandlerThread("SkinRenderThread")
        thread.start()
        renderThread = thread
        val handler = Handler(thread.looper)
        renderHandler = handler
        handler.post {
            if (!initEGL(surface)) {
                Logging.LOG.warning("SkinViewer: EGL init failed")
                releaseRenderThread()
                return@post
            }
            renderer?.onSurfaceCreated()
            renderer?.onSurfaceChanged(width, height)
            setupFrameLoop()
            if (resumeRequested) {
                startFrameLoop()
            }
        }
    }

    private fun stopRenderThread() {
        val thread = renderThread ?: return
        renderThread = null
        val handler = renderHandler
        renderHandler = null
        // 等渲染线程释放完 EGL 再让框架回收 SurfaceTexture
        val latch = CountDownLatch(1)
        handler?.post {
            stopFrameLoop()
            releaseEGL()
            latch.countDown()
        }
        try {
            latch.await(1, TimeUnit.SECONDS)
        } catch (ignored: InterruptedException) {
        }
        thread.quitSafely()
    }

    /** EGL 初始化失败时由渲染线程自行清理退出 */
    private fun releaseRenderThread() {
        stopFrameLoop()
        releaseEGL()
        renderThread?.quitSafely()
        renderThread = null
        renderHandler = null
    }

    private fun initEGL(surface: SurfaceTexture): Boolean {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            return false
        }
        val version = IntArray(2)
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            return false
        }
        val configAttribs = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_DEPTH_SIZE, 16,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        val counts = IntArray(1)
        if (!EGL14.eglChooseConfig(eglDisplay, configAttribs, 0, configs, 0, 1, counts, 0) || counts[0] == 0) {
            return false
        }
        val contextAttribs = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
        eglContext = EGL14.eglCreateContext(eglDisplay, configs[0], EGL14.EGL_NO_CONTEXT, contextAttribs, 0)
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            return false
        }
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, configs[0], surface, intArrayOf(EGL14.EGL_NONE), 0)
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            return false
        }
        return EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun releaseEGL() {
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
            if (eglSurface != EGL14.EGL_NO_SURFACE) {
                EGL14.eglDestroySurface(eglDisplay, eglSurface)
            }
            if (eglContext != EGL14.EGL_NO_CONTEXT) {
                EGL14.eglDestroyContext(eglDisplay, eglContext)
            }
        }
        eglSurface = EGL14.EGL_NO_SURFACE
        eglContext = EGL14.EGL_NO_CONTEXT
        EGL14.eglReleaseThread()
        eglDisplay = EGL14.EGL_NO_DISPLAY
    }

    // ---- 帧循环（渲染线程） ----

    private fun setupFrameLoop() {
        val choreographer = Choreographer.getInstance()
        this.choreographer = choreographer
        val callback = object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (frameLoopRunning) {
                    // 帧率上限 60：距上一渲染帧不足帧间隔时跳过本次 vsync（高刷新率屏幕下生效）
                    if (lastFrameNanos == 0L || frameTimeNanos - lastFrameNanos >= FRAME_INTERVAL_NANOS) {
                        val delta = if (lastFrameNanos == 0L) {
                            1f / 60f
                        } else {
                            (frameTimeNanos - lastFrameNanos) / 1_000_000_000f
                        }
                        lastFrameNanos = frameTimeNanos
                        try {
                            renderer?.onDrawFrame(delta)
                            if (!EGL14.eglSwapBuffers(eglDisplay, eglSurface)) {
                                // surface 已失效，停止出帧
                                stopFrameLoop()
                                return
                            }
                        } catch (e: Throwable) {
                            Logging.LOG.warning("SkinViewer: render frame failed: $e")
                        }
                    }
                    if (frameLoopRunning) {
                        choreographer.postFrameCallback(this)
                    }
                }
            }
        }
        frameCallback = callback
    }

    private fun startFrameLoop() {
        val callback = frameCallback ?: return
        if (frameLoopRunning) {
            return
        }
        frameLoopRunning = true
        lastFrameNanos = 0L
        choreographer?.postFrameCallback(callback)
    }

    private fun stopFrameLoop() {
        frameLoopRunning = false
        frameCallback?.let { choreographer?.removeFrameCallback(it) }
    }

    companion object {
        private const val DOUBLE_TAP_INTERVAL = 300L

        /** 帧率上限 60fps 的帧间隔（略低于 16.67ms，容忍 vsync 时间戳抖动） */
        private const val FRAME_INTERVAL_NANOS = 16_000_000L
    }
}
