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
    /** 设备缺失 EXT divisor 但提供 KHR 扩展，可经 vkshim 合成 */
    val needsDivisorShim: Boolean = false,
    /** 设备核心能力位 fillModeNonSolid 缺失，可经 vkshim 降级模拟（线框渲染退化为实心） */
    val needsFillModeEmulation: Boolean = false,
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

    private const val EXT_VERTEX_ATTRIBUTE_DIVISOR = "VK_EXT_vertex_attribute_divisor"
    private const val KHR_VERTEX_ATTRIBUTE_DIVISOR = "VK_KHR_vertex_attribute_divisor"
    private const val FEATURE_FILL_MODE_NON_SOLID = "fillModeNonSolid"

    /**
     * 当前驱动状态下是否应经 vkshim 包装加载（divisor 合成或 fillModeNonSolid 模拟任一成立）；
     * 由最近一次检测/缓存读取刷新，启动组装环境变量时读取。
     * shim 仅包装系统 Vulkan 加载器，Turnip 驱动路径不经过它
     */
    @Volatile
    var needsVulkanShim: Boolean = false
        private set

    /**
     * 版本是否带有 Vulkan 渲染后端（26.2 起，快照/pre/rc 归一化后判断）
     */
    fun hasVulkanBackend(gameVersion: String?): Boolean {
        val version = gameVersion?.let(::normalizeMcVersion) ?: return false
        return GameVersionNumber.compare(version, VulkanRequirements.MIN_MC_VERSION) >= 0
    }

    /**
     * 执行完整检测并保存结果缓存
     */
    suspend fun check(useTurnip: Boolean, driverPath: String?): VulkanCapabilities? {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                val cacheDir = File(FCLApp.getAppContext().cacheDir, "vulkan_check")
                val capabilities = VulkanChecker.checkCapabilities(useTurnip, driverPath, cacheDir)
                // shim 仅包装系统 Vulkan 加载器：请求 Turnip 但加载失败回落系统加载器时，
                // 检测与游戏运行时实际使用的都是系统驱动，shim 同样适用
                val needsDivisorShim = capabilities != null && !capabilities.usedCustomDriver &&
                        EXT_VERTEX_ATTRIBUTE_DIVISOR !in capabilities.extensions &&
                        KHR_VERTEX_ATTRIBUTE_DIVISOR in capabilities.extensions
                val needsFillModeEmulation = capabilities != null && !capabilities.usedCustomDriver &&
                        capabilities.features[FEATURE_FILL_MODE_NON_SOLID] != true
                needsVulkanShim = needsDivisorShim || needsFillModeEmulation
                // shim 生效后游戏可见的能力按补齐后的状态评估：
                // divisor 由 KHR 合成补报；fillModeNonSolid 由降级模拟保证管线可创建
                val effective = capabilities?.let {
                    it.copy(
                        extensions = if (needsDivisorShim) {
                            it.extensions + EXT_VERTEX_ATTRIBUTE_DIVISOR
                        } else it.extensions,
                        features = if (needsFillModeEmulation) {
                            it.features + (FEATURE_FILL_MODE_NON_SOLID to true)
                        } else it.features
                    )
                }
                saveRecord(
                    VulkanCheckRecord(
                        useTurnip = useTurnip,
                        driverPath = if (useTurnip) driverPath.orEmpty() else "",
                        apiVersion = effective?.versionString.orEmpty(),
                        needsDivisorShim = needsDivisorShim,
                        needsFillModeEmulation = needsFillModeEmulation,
                        supportedRanges = effective?.profileSupport()
                            ?.filter { it.supported }
                            ?.map { VulkanCheckRecord.Range(since = it.since, until = it.until) }
                            ?: emptyList(),
                        version = VulkanRequirements.VULKAN_REQUIREMENTS_VERSION
                    )
                )
                effective
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
        } ?: run {
            //无与当前驱动状态一致的记录，shim 门控回到安全侧（不启用）
            needsVulkanShim = false
            return VulkanEnsureResult.NEED_CHECK
        }
        needsVulkanShim = record.needsDivisorShim || record.needsFillModeEmulation
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
