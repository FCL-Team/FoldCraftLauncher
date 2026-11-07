package com.tungsten.fcl.vr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Surface
import com.mio.service.GameService
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.JVMActivity
import com.tungsten.fcl.activity.QuestFlatActivity
import com.tungsten.fcl.activity.QuestVrActivity
import com.tungsten.fcl.activity.QuestVrLoadingActivity
import com.tungsten.fcl.control.GameLaunchMetadata
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.bridge.FCLBridgeCallback
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import org.lwjgl.glfw.CallbackBridge
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level

/**
 * Quest VR 会话：持有游戏进程内唯一的 VR 会话状态，协调启动面板、
 * 平面面板（Quest Home 内 2D 显示）与沉浸面板（OpenXR 渲染）之间的切换与退出。
 */
class QuestVrSession private constructor(
    activity: QuestVrLoadingActivity,
    private val bridge: FCLBridge,
) : FCLBridgeCallback {
    private val application: Context = activity.applicationContext
    private val transitionAnimation = QuestVrPreferences.isTransitionAnimationEnabled(application)
    private val launchMetadata = GameLaunchMetadata.capture(activity.intent, bridge.controller)
    private val main = Handler(Looper.getMainLooper())
    private val gate = QuestVrLaunchGate()
    private val started = AtomicBoolean()
    private val exiting = AtomicBoolean()
    private val recentLog = StringBuilder()
    private val startTime = SystemClock.elapsedRealtime()
    private var loading = WeakReference(activity)
    private var immersive = WeakReference<QuestVrActivity>(null)
    private var flat = WeakReference<JVMActivity>(null)

    @Volatile
    private var flatMenu = WeakReference<GameMenu>(null)
    private var loadingResumed = false
    private var flatResumed = false
    private var immersiveResumed = false

    @Volatile
    private var hasImmersiveActivity = false
    private var handoffLaunched = false
    private var flatLaunchPending = false
    private var firstLog = true

    @Volatile
    private var cursorMode = FCLBridge.CursorEnabled

    @Volatile
    private var phase = R.string.qv_start_loading

    val instanceName: String
        get() = File(bridge.gameDir.orEmpty()).name

    fun elapsedSeconds(): Long = (SystemClock.elapsedRealtime() - startTime) / 1000

    /** 启动阶段文案资源 id（随游戏日志关键字推进） */
    fun phase(): Int = phase

    val isExiting: Boolean get() = exiting.get()

    val isImmersiveReady: Boolean get() = gate.state == QuestVrLaunchGate.State.READY

    fun wantsImmersive(): Boolean = gate.wantsImmersive

    val isFlat: Boolean get() = gate.state == QuestVrLaunchGate.State.FLAT

    /** 保活通知点击时回到的 Activity 类型 */
    fun notificationActivity(): Class<out Activity> = when {
        gate.wantsImmersive -> QuestVrActivity::class.java
        isFlat -> QuestFlatActivity::class.java
        else -> QuestVrLoadingActivity::class.java
    }

    fun loadingResumed(resumed: Boolean) {
        loadingResumed = resumed
        if (!resumed && gate.state == QuestVrLaunchGate.State.SWITCHING) handoffLaunched = false
        if (!resumed || exiting.get()) return
        if (started.compareAndSet(false, true)) {
            loading.get()?.let { GameService.start(it) }
            CallbackBridge.windowWidth = FCLBridge.DEFAULT_WIDTH
            CallbackBridge.windowHeight = FCLBridge.DEFAULT_HEIGHT
            windowFocus(true)
            bridge.setSurfaceDestroyed(false)
            bridge.execute(null, this)
            bridge.pushEventWindow(FCLBridge.DEFAULT_WIDTH, FCLBridge.DEFAULT_HEIGHT)
        }
        if (isImmersiveReady) {
            loading.get()?.let { QuestVrTransitions.immersive(it, transitionAnimation) }
            finishLoading()
            return
        }
        launchImmersiveIfRequested()
        if (isFlat) showFlat(false)
    }

    /** VLoader 初始化线程阻塞等待沉浸面板就绪；必须在非主线程调用 */
    fun awaitImmersiveActivity(): Boolean {
        check(Looper.myLooper() != Looper.getMainLooper()) {
            "OpenXR cannot wait on the Android UI thread"
        }
        return try {
            val ready = gate.awaitReady({
                QuestVrBridge.nativeSetImmersive(true)
                phase = R.string.qv_start_switching
                onLog("[FCL-QuestVR] Entering VR; waiting for the immersive Activity to resume.\n")
                main.post {
                    handoffLaunched = false
                    flatLaunchPending = false
                    launchImmersiveIfRequested()
                }
            }, 30000)
            if (!ready && !exiting.get() && gate.state == QuestVrLaunchGate.State.FAILED) {
                fail("Timed out entering the immersive VR Activity", null)
            }
            ready && !exiting.get()
        } catch (interrupted: InterruptedException) {
            Thread.currentThread().interrupt()
            fail("Interrupted while entering immersive VR", interrupted)
            false
        }
    }

    private fun launchImmersiveIfRequested() {
        if (exiting.get() || handoffLaunched || gate.state != QuestVrLaunchGate.State.SWITCHING) return
        val source = when {
            immersiveResumed -> immersive.get()
            flatResumed -> flat.get()
            loadingResumed -> loading.get()
            else -> null
        }.usableOrNull() ?: return
        handoffLaunched = true
        QuestVrTransitions.fadeOutPanel(source, transitionAnimation) {
            if (exiting.get() || !gate.wantsImmersive) return@fadeOutPanel
            val sourceResumed = when (source) {
                immersive.get() -> immersiveResumed
                flat.get() -> flatResumed
                loading.get() -> loadingResumed
                else -> false
            }
            if (!sourceResumed) {
                handoffLaunched = false
                return@fadeOutPanel
            }
            try {
                QuestVrTransitions.immersive(source, transitionAnimation)
            } catch (failure: RuntimeException) {
                fail("Cannot open the immersive VR Activity", failure)
            }
        }
    }

    fun attachImmersive(activity: QuestVrActivity): Boolean {
        if (exiting.get() || !gate.wantsImmersive) return false
        return try {
            restoreLaunchMetadata(activity)
            QuestVrBridge.bindImmersiveActivity(activity)
            immersive = WeakReference(activity)
            hasImmersiveActivity = true
            true
        } catch (failure: RuntimeException) {
            fail("Cannot bind the immersive VR Activity", failure)
            false
        } catch (failure: LinkageError) {
            fail("Cannot bind the immersive VR Activity", failure)
            false
        }
    }

    fun immersiveResumed(activity: QuestVrActivity) {
        if (exiting.get() || immersive.get() !== activity) return
        immersiveResumed = true
        if (!gate.wantsImmersive) {
            showFlat(true)
            return
        }
        QuestVrBridge.setResumed(true)
        windowFocus(true)
        if (gate.ready()) {
            GameService.start(activity)
            finishLoading()
        }
    }

    fun immersivePaused(activity: QuestVrActivity) {
        if (immersive.get() !== activity) return
        immersiveResumed = false
        gate.paused()
        if (!exiting.get() && gate.wantsImmersive) {
            QuestVrBridge.setResumed(false)
            windowFocus(false)
        }
    }

    /** 游戏未经 OpenXR 请求就开始渲染：回落平面面板 */
    fun onGameFrame() {
        if (exiting.get() || !gate.firstGameFrame()) return
        onLog("[FCL-QuestVR] Game rendering started without an OpenXR request; opening the flat game panel.\n")
        main.post { showFlat(false) }
    }

    fun onOpenXrStopped() {
        if (exiting.get()) return
        val changed = gate.flat()
        QuestVrBridge.nativeSetImmersive(false)
        if (changed) onLog("[FCL-QuestVR] OpenXR stopped; returning the running game to Quest Home.\n")
        main.post {
            if (!isFlat) return@post
            handoffLaunched = false
            showFlat(hasImmersiveActivity)
        }
    }

    private fun showFlat(fromVr: Boolean) {
        if (exiting.get() || !isFlat || flatLaunchPending
            || (flatResumed && (!fromVr || !immersiveResumed))
        ) return
        val context: Context = (if (fromVr) immersive.get() else loading.get()).usableOrNull()
            ?: flat.get().usableOrNull() ?: application
        flatLaunchPending = true
        try {
            QuestVrTransitions.flat(context, fromVr, transitionAnimation)
        } catch (failure: RuntimeException) {
            flatLaunchPending = false
            fail("Cannot return the game to a Quest Home panel", failure)
        }
    }

    fun attachFlat(activity: JVMActivity, menu: GameMenu): Boolean {
        if (exiting.get()) return false
        flat = WeakReference(activity)
        flatMenu = WeakReference(menu)
        menu.appendSessionLog(logPreview)
        menu.onCursorModeChange(cursorMode)
        return true
    }

    /** 各面板共享启动元数据（控件组/联机玩家名），面板重建时补回 Intent */
    fun restoreLaunchMetadata(activity: Activity) {
        val intent = activity.intent ?: Intent().also { activity.intent = it }
        launchMetadata.applyTo(intent)
    }

    fun flatResumed(activity: JVMActivity) {
        if (exiting.get() || flat.get() !== activity) return
        flatResumed = true
        flatLaunchPending = false
        when {
            isFlat -> {
                QuestVrTransitions.fadeInPanel(activity, transitionAnimation)
                windowFocus(true)
                GameService.start(activity)
                finishLoading()
            }

            isImmersiveReady -> QuestVrTransitions.immersive(activity, transitionAnimation)
            else -> launchImmersiveIfRequested()
        }
    }

    fun flatPaused(activity: JVMActivity) {
        if (flat.get() !== activity) return
        flatResumed = false
        if (gate.state == QuestVrLaunchGate.State.SWITCHING) handoffLaunched = false
        if (isFlat) windowFocus(false)
    }

    fun flatSurface(activity: JVMActivity, surface: Surface?) {
        if (flat.get() === activity && !exiting.get()) QuestVrBridge.nativeSetFlatSurface(surface)
    }

    fun onFirstFlatFrame() {
        main.post {
            if (!exiting.get()) flatMenu.get()?.onGraphicOutput()
        }
    }

    fun flatDestroyed(activity: JVMActivity) {
        if (flat.get() !== activity) return
        QuestVrBridge.nativeSetFlatSurface(null)
        flat.clear()
        flatMenu.clear()
        flatResumed = false
        flatLaunchPending = false
    }

    private fun finishLoading() {
        loading.get().usableOrNull()?.let { panel ->
            panel.finish()
            if (!transitionAnimation) panel.overridePendingTransition(0, 0)
        }
    }

    fun loadingDestroyed(activity: QuestVrLoadingActivity) {
        if (loading.get() !== activity) return
        loading.clear()
        if (gate.state == QuestVrLaunchGate.State.LOADING && !exiting.get()) {
            fail("VR loading window was closed", null)
        }
    }

    fun immersiveDestroyed(activity: QuestVrActivity) {
        QuestVrBridge.release(activity)
        if (immersive.get() !== activity) return
        immersive.clear()
        immersiveResumed = false
        if (!exiting.get() && gate.wantsImmersive) {
            fail("The active immersive Activity was destroyed before OpenXR stopped", null)
        } else if (!exiting.get()) {
            onLog("[FCL-QuestVR] Inactive immersive Activity released; flat game continues.\n")
        }
    }

    private fun windowFocus(focused: Boolean) {
        val value = if (focused) 1 else 0
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_FOCUSED, value)
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_HOVERED, value)
        if (focused) CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_VISIBLE, 1)
    }

    fun cancel() {
        onLog("[FCL-QuestVR] Session stopped by the user.\n")
        onExit(0)
    }

    private fun fail(message: String, failure: Throwable?) {
        onLog("[FCL-QuestVR] $message${failure?.let { ": $it" }.orEmpty()}\n")
        onExit(1)
    }

    override fun onCursorModeChange(mode: Int) {
        cursorMode = mode
        main.post { flatMenu.get()?.onCursorModeChange(mode) }
    }

    @Synchronized
    override fun onLog(log: String) {
        if (gate.wantsImmersive && (log.contains("Vivecraft: OpenXR init failed")
                    || log.contains("Vivecraft: Failed to initialize VR"))
        ) onOpenXrStopped()
        recentLog.append(log)
        if (recentLog.length > 12000) recentLog.delete(0, recentLog.length - 12000)
        if (gate.state == QuestVrLaunchGate.State.LOADING) {
            when {
                log.contains("Reloading ResourceManager") || log.contains("resource packs") ->
                    phase = R.string.qv_start_resources

                log.contains("Loading Minecraft") || log.contains("Fabric Loader") ->
                    phase = R.string.qv_start_mods
            }
        }
        if (flatMenu.get() != null) {
            main.post { flatMenu.get()?.appendSessionLog(log) }
        }
        try {
            val file = File(bridge.logPath)
            if (firstLog) {
                FileUtils.writeText(file, log)
                firstLog = false
            } else {
                FileUtils.writeTextWithAppendMode(file, log)
            }
        } catch (failure: IOException) {
            Logging.LOG.log(Level.WARNING, "Cannot write Quest VR game log", failure)
        }
    }

    /** 近期日志预览（隐藏 accessToken 等敏感启动参数） */
    @get:Synchronized
    val logPreview: String
        get() = SENSITIVE_ARGUMENT.replace(recentLog, "$1[hidden]")

    override fun onExit(code: Int) {
        if (!exiting.compareAndSet(false, true)) return
        val fromVr = gate.wantsImmersive
        gate.fail()
        val shutdown = Runnable {
            val context: Context = (if (flatResumed) flat.get() else immersive.get()).usableOrNull()
                ?: loading.get().usableOrNull() ?: flat.get().usableOrNull() ?: application
            GameService.stop(context)
            QuestGameExit.returnToLauncher(context, fromVr || immersiveResumed, transitionAnimation,
                code, bridge.logPath) {
                loading.get().usableOrNull()?.finish()
                flat.get().usableOrNull()?.finish()
                immersive.get().usableOrNull()?.finishAndRemoveTask()
            }
        }
        if (Looper.myLooper() == Looper.getMainLooper()) shutdown.run() else main.post(shutdown)
    }

    companion object {
        private val SENSITIVE_ARGUMENT =
            Regex("(?i)(--(?:accessToken|clientId|xuid|uuid|username)[ =]+)\\S+")

        @Volatile
        private var current: QuestVrSession? = null

        @JvmStatic
        fun get(): QuestVrSession? = current

        /** 游戏进程内准备/复用会话；并发启动不同会话视为错误 */
        @JvmStatic
        fun prepare(activity: QuestVrLoadingActivity, bridge: FCLBridge): QuestVrSession {
            val session = current?.also {
                check(it.bridge === bridge && !it.isExiting) {
                    "A different VR game session is already running"
                }
            } ?: QuestVrSession(activity, bridge).also {
                current = it
                QuestVrBridge.initialize(activity, bridge.vrRendererId)
            }
            session.loading = WeakReference(activity)
            return session
        }

        /** 仍可用于界面跳转的 Activity（未结束、未销毁），否则 null */
        private fun <T : Activity> T?.usableOrNull(): T? =
            this?.takeIf { !it.isFinishing && !it.isDestroyed }
    }
}
