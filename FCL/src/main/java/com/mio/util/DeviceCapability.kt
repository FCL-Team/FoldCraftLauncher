package com.mio.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.GLES20
import android.os.Build
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.platform.MemoryUtils
import java.util.logging.Level

/**
 * 只读探测：设备架构/内存/GPU/GLES/Vulkan 信息，用于渲染器兼容性展示页。
 * 不改变任何设置，不影响渲染器实际选择逻辑。
 */
data class GpuInfo(
    val vendor: String,
    val renderer: String,
    val glVersion: String
)

object DeviceCapability {

    fun getAndroidVersionString(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    fun getArchString(): String {
        return Architecture.archAsStringAndroid(Architecture.getDeviceArchitecture())
    }

    fun getTotalRamMb(context: Context): Int = MemoryUtils.getTotalDeviceMemory(context)

    fun getFreeRamMb(context: Context): Int = MemoryUtils.getFreeDeviceMemory(context)

    fun getRecommendedRamMb(context: Context): Int = MemoryUtils.findBestRAMAllocation(context)

    /** 通过一次性 EGL 上下文读取 GL_VENDOR / GL_RENDERER / GL_VERSION，探测失败时返回 null。 */
    fun getGpuInfo(): GpuInfo? {
        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            Logging.LOG.log(Level.WARNING, "DeviceCapability: Failed to get EGL display")
            return null
        }
        if (!EGL14.eglInitialize(eglDisplay, null, 0, null, 0)) {
            Logging.LOG.log(Level.WARNING, "DeviceCapability: Failed to initialize EGL")
            return null
        }
        try {
            val eglAttributes = intArrayOf(
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_NONE
            )
            val configs = arrayOfNulls<EGLConfig>(1)
            val numConfigs = IntArray(1)
            if (!EGL14.eglChooseConfig(eglDisplay, eglAttributes, 0, configs, 0, 1, numConfigs, 0) || numConfigs[0] == 0) {
                Logging.LOG.log(Level.WARNING, "DeviceCapability: Failed to choose an EGL config")
                return null
            }
            val contextAttributes = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
            val eglContext = EGL14.eglCreateContext(eglDisplay, configs[0], EGL14.EGL_NO_CONTEXT, contextAttributes, 0)
            if (eglContext == EGL14.EGL_NO_CONTEXT) {
                Logging.LOG.log(Level.WARNING, "DeviceCapability: Failed to create EGL context")
                return null
            }
            try {
                if (!EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, eglContext)) {
                    Logging.LOG.log(Level.WARNING, "DeviceCapability: Failed to make EGL context current")
                    return null
                }
                val vendor = GLES20.glGetString(GLES20.GL_VENDOR) ?: return null
                val renderer = GLES20.glGetString(GLES20.GL_RENDERER) ?: return null
                val version = GLES20.glGetString(GLES20.GL_VERSION) ?: ""
                return GpuInfo(vendor, renderer, version)
            } finally {
                EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
                EGL14.eglDestroyContext(eglDisplay, eglContext)
            }
        } finally {
            EGL14.eglTerminate(eglDisplay)
        }
    }

    /** 系统上报的 OpenGL ES 版本（不需要 EGL 上下文），格式如 "3.2"。 */
    fun getDeclaredGlEsVersion(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val reqGlEsVersion = am.deviceConfigurationInfo.reqGlEsVersion
        val major = (reqGlEsVersion and -0x10000) shr 16
        val minor = reqGlEsVersion and 0xffff
        return "$major.$minor"
    }

    fun hasVulkanSupport(context: Context): Boolean {
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_LEVEL) ||
                pm.hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_VERSION)
    }

    /** Vulkan 版本号，形如 "1.1"；不支持 Vulkan 时返回 null。 */
    fun getVulkanVersion(context: Context): String? {
        val pm = context.packageManager
        val feature = pm.systemAvailableFeatures.find {
            it.name == PackageManager.FEATURE_VULKAN_HARDWARE_VERSION
        } ?: return null
        val version = feature.version
        if (version <= 0) return null
        val major = (version shr 22) and 0x7F
        val minor = (version shr 12) and 0x3FF
        return "$major.$minor"
    }
}
