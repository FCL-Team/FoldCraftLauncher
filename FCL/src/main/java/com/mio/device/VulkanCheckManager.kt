package com.mio.device

import com.mio.datastore.VulkanCheckPreference
import com.mio.datastore.vulkanCheckDataStore
import com.tungsten.fcl.FCLApp
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.File

/**
 * 设备 Vulkan 能力检测结果缓存：
 * 驱动状态或检测规则不变时，各 MC 版本的支持结论可直接由 [supportedRanges] 判定
 */
@Serializable
data class VulkanCheckRecord(
    val useTurnip: Boolean,
    val driverPath: String,
    /** 检测到的 Vulkan 版本字符串（如 "1.3.2"），检测失败时为空 */
    val apiVersion: String = "",
    /** 设备可支持的 Minecraft 版本范围，[Range.until] 为排他上界，null 表示无上界 */
    val supportedRanges: List<Range> = emptyList(),
    val version: Int = 0,
) {
    @Serializable
    data class Range(
        val since: String,
        val until: String?
    )

    /** 判断指定 Minecraft 版本是否落在已支持的版本范围内 */
    fun isSupported(mcVersion: String): Boolean {
        return supportedRanges.any { range ->
            !mcVersion.isLowerVer(range.since) &&
                    (range.until == null || mcVersion.isLowerVer(range.until))
        }
    }

    private fun String.isLowerVer(other: String): Boolean =
        GameVersionNumber.compare(this, other) < 0
}

/** 启动前校验的结论 */
enum class VulkanEnsureResult {
    /** 设备满足该版本的 Vulkan 依赖 */
    SUPPORTED,
    /** 设备不满足该版本的 Vulkan 依赖 */
    UNSUPPORTED,
    /** 尚无有效检测结果，需要先执行检测 */
    NEED_CHECK,
}

object VulkanCheckManager {
    private val mutex = Mutex()

    /** 首个强制 Vulkan 渲染的 Minecraft 版本，自此版本起缺失依赖时无法启动 */
    private const val VULKAN_MANDATORY_VERSION = "26.3"

    /**
     * 版本是否带有 Vulkan 渲染后端（26.2 起，快照/pre/rc 归一化后判断）
     */
    fun hasVulkanBackend(gameVersion: String?): Boolean {
        val version = gameVersion?.let(::normalizeMcVersion) ?: return false
        return GameVersionNumber.compare(version, VulkanRequirements.MIN_MC_VERSION) >= 0
    }

    /**
     * 版本是否强制 Vulkan 渲染（26.3 起移除 OpenGL 后端）
     */
    fun isVulkanMandatory(gameVersion: String?): Boolean {
        val version = gameVersion?.let(::normalizeMcVersion) ?: return false
        return GameVersionNumber.compare(version, VULKAN_MANDATORY_VERSION) >= 0
    }

    /**
     * 执行完整检测并保存结果缓存
     */
    suspend fun check(useTurnip: Boolean, driverPath: String?): VulkanCapabilities? {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                val cacheDir = File(FCLApp.getAppContext().cacheDir, "vulkan_check")
                val capabilities = VulkanChecker.checkCapabilities(useTurnip, driverPath, cacheDir)
                saveRecord(
                    VulkanCheckRecord(
                        useTurnip = useTurnip,
                        driverPath = if (useTurnip) driverPath.orEmpty() else "",
                        apiVersion = capabilities?.versionString.orEmpty(),
                        supportedRanges = capabilities?.profileSupport()
                            ?.filter { it.supported }
                            ?.map { VulkanCheckRecord.Range(since = it.since, until = it.until) }
                            ?: emptyList(),
                        version = VulkanRequirements.VULKAN_REQUIREMENTS_VERSION
                    )
                )
                capabilities
            }
        }
    }

    /**
     * 按缓存评估指定版本的 Vulkan 支持，缓存缺失或与当前驱动状态不一致时返回 [VulkanEnsureResult.NEED_CHECK]
     */
    suspend fun ensureSupported(gameVersion: String, useTurnip: Boolean, driverPath: String?): VulkanEnsureResult {
        val mcVersion = normalizeMcVersion(gameVersion)
        val path = if (useTurnip) driverPath.orEmpty() else ""
        val record = loadRecord()?.takeIf { last ->
            //检测器版本、驱动状态一致时设备能力不变，直接按已支持的版本范围判定
            last.version == VulkanRequirements.VULKAN_REQUIREMENTS_VERSION &&
                    last.useTurnip == useTurnip && last.driverPath == path
        } ?: return VulkanEnsureResult.NEED_CHECK
        return if (record.isSupported(mcVersion)) VulkanEnsureResult.SUPPORTED else VulkanEnsureResult.UNSUPPORTED
    }

    suspend fun loadRecord(): VulkanCheckRecord? {
        return runCatching {
            FCLApp.getAppContext().vulkanCheckDataStore.data.first().record
        }.getOrNull()
    }

    private suspend fun saveRecord(record: VulkanCheckRecord) {
        runCatching {
            FCLApp.getAppContext().vulkanCheckDataStore.updateData {
                VulkanCheckPreference(record = record)
            }
        }
    }
}
