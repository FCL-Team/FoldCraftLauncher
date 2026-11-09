/*
 * Fold Craft Launcher
 * SDL 集成状态桥（由启动器与游戏 JVM 共享），移植自 ZalithLauncher2 feat/sdl3
 * 手柄热插拔主动同步参考 ZL2 PR #2204（https://github.com/ZalithLauncher/ZalithLauncher2/pull/2204）
 */
package com.tungsten.fcl.game.sdl

import android.app.Activity
import android.content.Context
import android.hardware.input.InputManager
import android.view.InputDevice
import android.view.Surface
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.MainThread
import com.tungsten.fclcore.util.Logging
import java.lang.ref.WeakReference
import java.util.logging.Level
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.libsdl.app.HIDDeviceManager
import org.libsdl.app.SDL
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLControllerManager
import org.libsdl.app.SDLSurface
import org.lwjgl.glfw.CallbackBridge

/**
 * SDL 集成状态桥，由启动器与游戏 JVM 共享（移植自 ZalithLauncher2 feat/sdl3）。
 */
@Keep
object SdlBridge {
    private val _enabled = MutableStateFlow(false)
    val enabled = _enabled.asStateFlow()
    private var activityRef: WeakReference<Activity>? = null
    private var layoutRef: WeakReference<ViewGroup>? = null
    private var currentSurface: Surface? = null

    /** 当前注册 Surface 的来源 */
    private var currentSource: Any? = null

    /** 每次注册新 Surface 递增，供生命周期观测 */
    private var surfaceGeneration = 0L
    private var jniReady = false
    private var sdlInitialized = false

    /** HID 管理器进程级只创建一次，其广播接收器在退出游戏后仍需存活以持续感知插拔 */
    @Volatile
    private var hidAcquired = false

    @Volatile
    private var inputListenerRegistered = false

    @JvmStatic
    @Synchronized
    fun setupJNI(): Boolean {
        if (jniReady) {
            return true
        }
        SDL.setupJNI()
        jniReady = true

        acquireHIDDeviceManager()

        // JNI 就绪后立即同步一次设备列表：
        // SDL 的 Android 后端初始化扫描受 3 秒节流且跨 SDL_Quit 不重置，
        // 可能错过已连接的手柄或残留上一局的失效设备；
        // 此处的差异会直接转为热插拔事件，保证 Controlify 初始发现可用
        if (SDLControllerManager.pollInputDevicesSafely("jni-setup")) {
            Logging.LOG.info("SDL: Input devices synced after SDL JNI setup")
        }
        return true
    }

    /**
     * 创建 SDL 的 HID 设备管理器（USB/蓝牙插拔广播、hidapi 通道）。
     * 标准流程由 SDLActivity.onCreate 创建，本项目不走该流程，须在 JNI 就绪后手动创建；
     * Controlify 会强制启用 HIDAPI，缺少它时 hid_init 将静默失败
     */
    private fun acquireHIDDeviceManager() {
        if (hidAcquired) return
        val context = SDL.getContext() ?: return
        hidAcquired = true // 只尝试一次，失败也不重试，避免反复注册广播
        try {
            HIDDeviceManager.acquire(context)
            Logging.LOG.info("SDL: HIDDeviceManager acquired, USB/BT hotplug monitoring enabled")
        } catch (t: Throwable) {
            // 创建失败不影响 SDL 其余功能，仅失去 hidapi 与 USB/蓝牙插拔通知
            Logging.LOG.log(Level.WARNING, "SDL: HIDDeviceManager acquire failed", t)
        }
    }

    /** 注册系统输入设备监听，手柄插拔时主动同步 SDL 设备列表 */
    private fun registerInputDeviceListener(context: Context) {
        if (inputListenerRegistered) return
        val inputManager = context.getSystemService(InputManager::class.java) ?: return
        inputListenerRegistered = true

        val listener = object : InputManager.InputDeviceListener {
            override fun onInputDeviceAdded(deviceId: Int) {
                syncInputDevices("added", deviceId)
            }

            override fun onInputDeviceRemoved(deviceId: Int) {
                syncInputDevices("removed", deviceId)
            }

            override fun onInputDeviceChanged(deviceId: Int) {
                syncInputDevices("changed", deviceId)
            }
        }
        inputManager.registerInputDeviceListener(listener, null)
    }

    /** 设备变化同步：移除事件查不到设备信息，须无条件同步；新增设备先过滤掉非手柄类 */
    private fun syncInputDevices(action: String, deviceId: Int) {
        // SDL 原生未加载时无法同步，仅靠游戏启动时 setupJNI 的枚举兜底
        if (!sdlEnabled) return
        val device = InputDevice.getDevice(deviceId)
        if (device != null && !device.isGamepadDevice()) return

        Logging.LOG.info("SDL: Input device $action: ${device?.descriptor ?: deviceId}")
        SDLControllerManager.pollInputDevicesSafely(action)
    }

    /** 手柄类输入设备（游戏手柄或摇杆源） */
    private fun InputDevice?.isGamepadDevice(): Boolean {
        if (this == null) return false
        return supportsSource(InputDevice.SOURCE_GAMEPAD) || supportsSource(InputDevice.SOURCE_JOYSTICK)
    }

    @JvmStatic
    @Synchronized
    fun markSdlInitialized(): Boolean {
        if (sdlInitialized) return false
        sdlInitialized = true
        return true
    }

    @JvmStatic
    @Synchronized
    fun clearSdlInitialized() {
        sdlInitialized = false
    }

    @JvmStatic
    @Volatile
    var sdlEnabled: Boolean = false
        set(value) {
            field = value
            _enabled.value = value
        }

    /**
     * SDL 请求唤起输入法时，启动器侧是否响应
     */
    @JvmStatic
    fun getSdlImeAutoShowEnabled(): Boolean = SdlSettings.sdlAutoShowIme.value

    @JvmStatic
    @MainThread
    fun prepareSurface(activity: Activity, surface: Surface, layout: ViewGroup?, source: Any? = null) {
        activityRef = WeakReference(activity)
        layoutRef = WeakReference(layout)
        currentSurface = surface
        currentSource = source
        surfaceGeneration++

        registerInputDeviceListener(activity)

        if (SDLActivity.getSDLSurface() == null) {
            SDL.initialize()
            SDL.setContext(activity)
            SDLActivity.externalInitialize(SDLSurface(activity), layout, surface)
        } else {
            SDLSurface.setNativeSurface(surface)
        }

        // 游戏侧 setupJNI 先于本方法时拿不到 context 而错过 HID 创建，在此兜底（幂等）
        acquireHIDDeviceManager()
    }

    @JvmStatic
    @MainThread
    fun registerSurface(activity: Activity, surface: Surface, layout: ViewGroup?) {
        activityRef = WeakReference(activity)
        layoutRef = WeakReference(layout)
        currentSurface = surface
    }

    /**
     * 游戏是否运行在 SDL 渲染路径（SDL 窗口已创建，MC 26.3+）
     * 仅手柄子系统使用 SDL 时（如 MC 26.2 挂 Controlify）返回 false，
     * 此时游戏输入仍走 GLFW 桥，启动器不应把键盘切换委托给 SDL 输入通道
     */
    @JvmStatic
    external fun isSdlRenderActive(): Boolean

    /**
     * 运行时窗口尺寸变化后同步 SDL 渲染分辨率。
     * SDL 窗口尺寸不消费 GLFW 窗口事件，不改视图尺寸的调整（如分辨率缩放滑条）
     * 须显式重设，否则游戏 viewport 与渲染 buffer 不一致，画面留黑边、点击与 UI 错位
     */
    @JvmStatic
    @MainThread
    fun syncResolution(width: Int, height: Int) {
        if (!sdlEnabled) return
        val sdlSurface = SDLActivity.getSDLSurface() ?: return
        sdlSurface.surfaceChanged()
        sdlSurface.nativeResize(width, height)
    }

    /**
     * 激活/关闭 native 侧 SDL 文本输入通道
     * 游戏侧通道被模组（自绘输入界面）关闭时，启动器显式唤起输入法需代为激活，
     * 否则输入法提交的文本会在 native 层被丢弃
     */
    @JvmStatic
    external fun setNativeTextInputActive(active: Boolean): Boolean

    /**
     * native 侧 SDL 文本输入通道当前是否激活（SDL_TextInputActive）
     * 与 Java 侧激活镜像相互独立：镜像经 showTextInput JNI 回调同步，该回调受
     * SDL_ENABLE_SCREEN_KEYBOARD hint 支配（MC 运行中会把它设回 0）而可能缺失
     */
    @JvmStatic
    external fun isNativeTextInputActive(): Boolean

    @JvmStatic
    @MainThread
    fun beginSurfaceDestroy(source: Any?, surface: Surface?): Boolean {
        return source != null && currentSource === source && surface != null && currentSurface === surface
    }

    @JvmStatic
    @MainThread
    fun unregisterSurface(surface: Surface?) {
        if (surface != null && currentSurface === surface) {
            currentSurface = null
            currentSource = null
        }
    }

    @JvmStatic
    @MainThread
    @Synchronized
    fun reset() {
        currentSurface = null
        currentSource = null
        surfaceGeneration = 0L
        activityRef = null
        layoutRef = null
        jniReady = false
        sdlInitialized = false
        sdlEnabled = false
        CallbackBridge.clearSdlBridgeState()
        SDLSurface.clearNativeSurface()
        SDL.initialize()
    }

    @JvmStatic
    external fun initializeControllerSubsystems()
}
