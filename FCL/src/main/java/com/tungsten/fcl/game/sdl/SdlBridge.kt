/*
 * Fold Craft Launcher
 * SDL 集成状态桥（由启动器与游戏 JVM 共享），移植自 ZalithLauncher2 feat/sdl3
 */
package com.tungsten.fcl.game.sdl

import android.app.Activity
import android.view.Surface
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.MainThread
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.libsdl.app.SDL
import org.libsdl.app.SDLActivity
import org.libsdl.app.SDLSurface
import org.lwjgl.glfw.CallbackBridge
import java.lang.ref.WeakReference

/**
 * Owns the SDL integration state shared by the launcher and game JVM.
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

    private val _composeFocus = MutableStateFlow(0)
    val composeFocus = _composeFocus.asStateFlow()

    @JvmStatic
    @Synchronized
    fun setupJNI(): Boolean {
        if (jniReady) {
            return true
        }
        SDL.setupJNI()
        jniReady = true
        return true
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

        if (SDLActivity.getSDLSurface() == null) {
            SDL.initialize()
            SDL.setContext(activity)
            SDLActivity.externalInitialize(SDLSurface(activity), layout, surface)
        } else {
            SDLSurface.setNativeSurface(surface)
        }
    }

    @JvmStatic
    @MainThread
    fun registerSurface(activity: Activity, surface: Surface, layout: ViewGroup?) {
        activityRef = WeakReference(activity)
        layoutRef = WeakReference(layout)
        currentSurface = surface
    }

    @JvmStatic
    fun requestComposeFocus() {
        _composeFocus.update { it + 1 }
    }

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
