package com.tungsten.fcl.vr

import android.app.Activity
import android.os.Build
import android.system.Os
import android.util.Log
import android.view.Surface
import androidx.annotation.Keep
import com.mio.data.Renderer
import com.tungsten.fcl.vr.QuestVrPreferences.isTransitionAnimationEnabled
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

/**
 * Quest VR 的 JNI 桥：初始化 vloader 侧共享状态、同步渲染环境变量，
 * 并承接 native 层的回调（OpenXR 等待/首帧/停止）。
 */
object QuestVrBridge {
    private const val TAG = "FCL-QuestVR"
    private var activeRenderer: String? = null

    init {
        System.loadLibrary("vloader")
    }

    /** 由启动面板调用：设置 VR 环境变量、准备 MobileGlues 配置目录并初始化 native 桥 */
    @JvmStatic
    @Synchronized
    fun initialize(activity: Activity, rendererId: String?) {
        require(Renderer.ID_QUEST_MOBILEGLUES == rendererId) {
            "Unsupported Quest VR renderer: $rendererId"
        }
        if (activeRenderer != null && activeRenderer != rendererId) {
            throw IllegalStateException("Changing a VR renderer requires a new game process")
        }
        try {
            Os.setenv("FCL_VR", "1", true)
            Os.setenv("FCL_VR_RENDERER", rendererId, true)
            Os.setenv("POJAV_RENDERER", rendererId, true)
            Os.setenv("LIBGL_ES", "3", true)
            Os.setenv("FCL_VR_TRANSITION_ANIMATION",
                if (isTransitionAnimationEnabled(activity)) "1" else "0", true)
            Os.setenv("VR_MODEL", Build.MODEL, true)
            Os.unsetenv("SFPEW_LAYER")
            val directory = File(activity.filesDir, "quest-vr/mobileglues")
            if (!directory.isDirectory && !directory.mkdirs()) {
                throw IllegalStateException("Cannot create the VR renderer configuration directory")
            }
            // VR 需要 MobileGlues 提供原生 GLES context（ANGLE 的 context 无法绑定 OpenXR）
            val config = "{\"enableANGLE\":0,\"enableNoError\":1," +
                    "\"fsr1Setting\":0,\"enableExtDirectStateAccess\":1," +
                    "\"enableExtTimerQuery\":1,\"enableExtComputeShader\":0," +
                    "\"customGLVersion\":0,\"maxGlslCacheSize\":32}"
            Files.write(File(directory, "config.json").toPath(), config.toByteArray(StandardCharsets.UTF_8))
            Os.setenv("MG_DIR_PATH", directory.absolutePath, true)
            Os.setenv("POJAVEXEC_EGL", "libmobileglues.so", true)
            nativeInitialize(activity)
            activeRenderer = rendererId
            Log.i(TAG, "Android bridge ready: $rendererId on ${Build.MODEL}")
        } catch (e: Exception) {
            throw IllegalStateException("Cannot initialize Quest VR: " + e.message, e)
        }
    }

    @JvmStatic
    fun setResumed(resumed: Boolean) {
        Log.i(TAG, if (resumed) "VR Activity resumed" else "VR Activity paused")
    }

    @JvmStatic
    fun release(activity: Activity) {
        Log.i(TAG, "Activity destroyed; reference retained until OpenXR releases its lease")
    }

    /** 沉浸面板 onCreate 时绑定 Activity（OpenXR 会话须持有它） */
    @JvmStatic
    @Synchronized
    fun bindImmersiveActivity(activity: Activity) {
        check(activeRenderer != null) { "VR startup has not been prepared" }
        nativeInitialize(activity)
        Log.i(TAG, "Immersive Activity bound to the running game")
    }

    // ===== 以下回调由 libvloader.so 经 GetStaticMethodID 调用 =====

    /** OpenXR 初始化线程阻塞等待沉浸面板恢复 */
    @JvmStatic
    @Keep
    fun awaitImmersiveActivity(): Boolean {
        val session = QuestVrSession.get()
        return session != null && session.awaitImmersiveActivity()
    }

    @JvmStatic
    @Keep
    fun onGameFrame() {
        QuestVrSession.get()?.onGameFrame()
    }

    @JvmStatic
    @Keep
    fun onFirstFlatFrame() {
        QuestVrSession.get()?.onFirstFlatFrame()
    }

    @JvmStatic
    @Keep
    fun onOpenXrStopped() {
        QuestVrSession.get()?.onOpenXrStopped()
    }

    @JvmStatic
    external fun nativeSetFlatSurface(surface: Surface?)

    @JvmStatic
    external fun nativeSetImmersive(immersive: Boolean)

    @JvmStatic
    private external fun nativeInitialize(activity: Activity)
}
