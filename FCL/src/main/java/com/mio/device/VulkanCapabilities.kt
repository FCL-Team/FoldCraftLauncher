package com.mio.device

import androidx.annotation.Keep
import com.tungsten.fclcore.util.Logging
import java.io.File
import java.util.logging.Level

private const val TAG = "VulkanChecker"

/**
 * 设备的原始 Vulkan 支持情况
 */
@Keep
data class VulkanCapabilities(
    val apiVersionMajor: Int,
    val apiVersionMinor: Int,
    val apiVersionPatch: Int,
    val extensions: List<String>,
    val features: Map<String, Boolean>,
    /** 检测时是否实际经自定义驱动（Turnip）加载；请求 Turnip 但加载失败回落系统加载器时为 false */
    val usedCustomDriver: Boolean = false
) {
    /** Vulkan 版本字符串 */
    val versionString: String
        get() = "$apiVersionMajor.$apiVersionMinor.$apiVersionPatch"

    /** 检查 Vulkan 版本是否至少为 1.2 */
    val isVersionSupported: Boolean
        get() = apiVersionMajor > 1 || (apiVersionMajor == 1 && apiVersionMinor >= 2)
}

@Keep
fun interface VulkanLogCallback {
    fun log(level: String, message: String)
}

@Keep
object VulkanChecker {
    init {
        try {
            System.loadLibrary("vulkan_check")
            nativeSetLogCallback { level, msg ->
                when (level) {
                    "INFO" -> Logging.LOG.info("$TAG: $msg")
                    "WARN" -> Logging.LOG.warning("$TAG: $msg")
                    else -> Logging.LOG.log(Level.WARNING, "$TAG: $msg")
                }
            }
        } catch (e: UnsatisfiedLinkError) {
            Logging.LOG.log(Level.WARNING, "$TAG: Failed to load vulkan_check library", e)
        }
    }

    /**
     * 查询设备 Vulkan 支持情况，[useTurnip] 时经 liblinkerhook 加载 [driverPath] 下的 Turnip 驱动
     * @return 如果不支持 Vulkan 或初始化失败，返回 null
     */
    fun checkCapabilities(
        useTurnip: Boolean,
        driverPath: String?,
        cacheDir: File?
    ): VulkanCapabilities? {
        return try {
            nativeCheckVulkan(useTurnip, driverPath, cacheDir?.absolutePath)
        } catch (e: UnsatisfiedLinkError) {
            Logging.LOG.log(Level.WARNING, "$TAG: Native library or method not found", e)
            null
        } catch (e: Exception) {
            Logging.LOG.log(Level.WARNING, "$TAG: Native check failed", e)
            null
        } finally {
            cacheDir?.deleteRecursively()
        }
    }

    @JvmStatic
    private external fun nativeSetLogCallback(callback: VulkanLogCallback)

    @JvmStatic
    private external fun nativeCheckVulkan(
        useTurnip: Boolean,
        driverPath: String?,
        cacheDir: String?
    ): VulkanCapabilities?
}
