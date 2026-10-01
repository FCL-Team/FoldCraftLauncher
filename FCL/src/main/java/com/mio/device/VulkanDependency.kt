package com.mio.device

import com.tungsten.fclcore.util.versioning.GameVersionNumber

/** 版本号比较（升序为正） */
private fun compareVersion(a: String, b: String): Int = GameVersionNumber.compare(a, b)

private fun String.isLowerVer(other: String): Boolean = compareVersion(this, other) < 0

private fun String.isBiggerVer(other: String): Boolean = compareVersion(this, other) > 0

/** 匹配版本号末尾的数字段 */
private val NUMBER_SUFFIX = Regex("^(.*?)(\\d+)$")

/** 版本号的数字尾段自增 */
private fun String.nextNumberVersion(): String? {
    val match = NUMBER_SUFFIX.find(this) ?: return null
    val number = match.groupValues[2].toIntOrNull() ?: return null
    return match.groupValues[1] + (number + 1)
}

/** 版本号的数字尾段自减 */
private fun String.prevNumberVersion(): String? {
    val match = NUMBER_SUFFIX.find(this) ?: return null
    val number = match.groupValues[2].toIntOrNull() ?: return null
    if (number <= 0) return null
    return match.groupValues[1] + (number - 1)
}

/** 快照、pre、rc 版本的编号后缀 */
private val PRERELEASE_SUFFIX = Regex("^(.+)-(?:snapshot|pre|rc)-\\d+$")

/**
 * 将快照、pre、rc 版本归一化为对应的目标正式版本
 */
fun normalizeMcVersion(mcVersion: String): String {
    return PRERELEASE_SUFFIX.find(mcVersion)?.groupValues?.get(1) ?: mcVersion
}

/**
 * Minecraft 版本对 Vulkan 功能/扩展的依赖级别
 */
enum class VulkanDependencyLevel {
    /** 依赖此条目，缺失时无法以 Vulkan 启动 */
    REQUIRED,
    /** 可选使用此条目，缺失时仍可以 Vulkan 启动 */
    OPTIONAL,
    /** 不使用此条目 */
    UNUSED
}

/**
 * Minecraft 版本范围
 */
sealed interface McVersionSpan {
    /** 范围的起始版本 */
    val since: String
    /** 范围的展示文本 */
    val displayText: String
    /** 范围的排他上界（不含），null 表示无上界，用于切分评估版本段 */
    val exclusiveEnd: String?

    operator fun contains(mcVersion: String): Boolean
}

/**
 * 单个 Minecraft 版本，仅匹配该 release 版本号本身（其快照、pre、rc 归一化后匹配）
 */
data class McSingleVersion(val version: String) : McVersionSpan {
    override val since: String
        get() = version
    override val displayText: String
        get() = version
    override val exclusiveEnd: String?
        get() = version.nextNumberVersion()

    override fun contains(mcVersion: String): Boolean {
        return normalizeMcVersion(mcVersion) == version
    }
}

/**
 * Minecraft 版本区间，两端均包含且按版本号精确匹配
 */
data class McVersionRange(
    val from: String,
    val to: String
) : McVersionSpan {
    override val since: String
        get() = from
    override val displayText: String
        get() = "$from-$to"
    override val exclusiveEnd: String?
        get() = to.nextNumberVersion()

    override fun contains(mcVersion: String): Boolean {
        val version = normalizeMcVersion(mcVersion)
        return !version.isLowerVer(from) && !version.isBiggerVer(to)
    }
}

/**
 * 不封顶的 Minecraft 版本范围，自起始版本起（含）直至最新
 */
data class McVersionOnward(override val since: String) : McVersionSpan {
    override val displayText: String
        get() = "$since+"
    override val exclusiveEnd: String?
        get() = null

    override fun contains(mcVersion: String): Boolean {
        return !normalizeMcVersion(mcVersion).isLowerVer(since)
    }
}

/**
 * 对单个 Vulkan 功能/扩展的包装
 */
data class VulkanDependency(
    val name: String,
    /** 依赖此条目的 Minecraft 版本区间 */
    val requiredIn: List<McVersionSpan> = emptyList(),
    /** 可选使用此条目的 Minecraft 版本区间 */
    val optionalIn: List<McVersionSpan> = emptyList(),
    /** 不使用此条目的 Minecraft 版本区间 */
    val unusedIn: List<McVersionSpan> = emptyList()
) {
    /** 查询指定 Minecraft 版本对此条目的依赖级别 */
    fun levelAt(mcVersion: String): VulkanDependencyLevel {
        return when {
            requiredIn.any { mcVersion in it } -> VulkanDependencyLevel.REQUIRED
            optionalIn.any { mcVersion in it } -> VulkanDependencyLevel.OPTIONAL
            else -> VulkanDependencyLevel.UNUSED
        }
    }
}

class VulkanDependencyBuilder(private val name: String) {
    private val requiredIn = mutableListOf<McVersionSpan>()
    private val optionalIn = mutableListOf<McVersionSpan>()
    private val unusedIn = mutableListOf<McVersionSpan>()

    /** 标注单个版本依赖此条目 */
    fun requiredAt(version: String) {
        requiredIn += McSingleVersion(version)
    }

    /** 标注 [from] 至 [to] 版本（含两端）依赖此条目 */
    fun requiredBetween(from: String, to: String) {
        requiredIn += McVersionRange(from, to)
    }

    /** 标注自 [since] 版本起（含）依赖此条目 */
    fun requiredFrom(since: String) {
        requiredIn += McVersionOnward(since)
    }

    /** 标注单个版本可选使用此条目 */
    fun optionalAt(version: String) {
        optionalIn += McSingleVersion(version)
    }

    /** 标注 [from] 至 [to] 版本（含两端）可选使用此条目 */
    fun optionalBetween(from: String, to: String) {
        optionalIn += McVersionRange(from, to)
    }

    /** 标注自 [since] 版本起（含）可选使用此条目 */
    fun optionalFrom(since: String) {
        optionalIn += McVersionOnward(since)
    }

    /** 标注单个版本不使用此条目 */
    fun unusedAt(version: String) {
        unusedIn += McSingleVersion(version)
    }

    /** 标注 [from] 至 [to] 版本（含两端）不使用此条目 */
    fun unusedBetween(from: String, to: String) {
        unusedIn += McVersionRange(from, to)
    }

    /** 标注自 [since] 版本起（含）不使用此条目 */
    fun unusedFrom(since: String) {
        unusedIn += McVersionOnward(since)
    }

    fun build(): VulkanDependency = VulkanDependency(name, requiredIn, optionalIn, unusedIn)
}

private fun vulkanDependency(name: String, block: VulkanDependencyBuilder.() -> Unit): VulkanDependency {
    return VulkanDependencyBuilder(name).apply(block).build()
}

/**
 * 各 Minecraft 版本运行 Vulkan 后端所依赖的扩展与功能
 */
object VulkanRequirements {
    /** 当前 Vulkan 检测器版本，依赖表调整时递增以使旧缓存失效 */
    const val VULKAN_REQUIREMENTS_VERSION = 1

    /** 首个提供 Vulkan 后端的 Minecraft 版本 */
    const val MIN_MC_VERSION = "26.2"

    /** 各版本依赖的 Vulkan 扩展 */
    val EXTENSIONS: List<VulkanDependency> = listOf(
        vulkanDependency("VK_KHR_dynamic_rendering") {
            requiredBetween(MIN_MC_VERSION, "26.3")
        },
        vulkanDependency("VK_KHR_push_descriptor") {
            requiredBetween(MIN_MC_VERSION, "26.3")
        },
        vulkanDependency("VK_KHR_synchronization2") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("VK_EXT_vertex_attribute_divisor") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("VK_KHR_swapchain") { requiredFrom(MIN_MC_VERSION) }
    )

    /** 各版本依赖的 Vulkan 功能 */
    val FEATURES: List<VulkanDependency> = listOf(
        vulkanDependency("multiDrawIndirect") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("fillModeNonSolid") {
            requiredAt(MIN_MC_VERSION)
            optionalFrom("26.3")
        },
        vulkanDependency("drawIndirectFirstInstance") {
            unusedAt(MIN_MC_VERSION)
            requiredFrom("26.3")
        },
        vulkanDependency("samplerAnisotropy") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("shaderDrawParameters") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("timelineSemaphore") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("hostQueryReset") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("synchronization2") { requiredFrom(MIN_MC_VERSION) },
        vulkanDependency("dynamicRendering") {
            requiredBetween(MIN_MC_VERSION, "26.3")
        },
        vulkanDependency("vertexAttributeInstanceRateDivisor") { requiredFrom(MIN_MC_VERSION) }
    )

    /** 依赖需求发生变化的 Minecraft 版本分界点（升序），由各标注范围的起点与终点共同构成 */
    val PROFILE_VERSIONS: List<String> = (EXTENSIONS + FEATURES)
        .flatMap { it.requiredIn + it.optionalIn + it.unusedIn }
        .flatMap { listOfNotNull(it.since, it.exclusiveEnd) }
        .distinct()
        .sortedWith { a, b -> compareVersion(a, b) }
}

/**
 * 设备对单个功能/扩展的支持情况（针对特定 Minecraft 版本）
 */
data class VulkanDependencyStatus(
    val dependency: VulkanDependency,
    val level: VulkanDependencyLevel,
    /** 设备是否支持此条目 */
    val supported: Boolean
)

/**
 * 设备针对特定 Minecraft 版本的 Vulkan 支持情况
 */
data class VulkanSupport(
    val mcVersion: String,
    val extensionStatuses: List<VulkanDependencyStatus>,
    val featureStatuses: List<VulkanDependencyStatus>
) {
    private val allStatuses: List<VulkanDependencyStatus>
        get() = extensionStatuses + featureStatuses

    /** 该版本依赖但设备缺失的功能/扩展 */
    val missingRequired: List<VulkanDependencyStatus>
        get() = allStatuses.filter { it.level == VulkanDependencyLevel.REQUIRED && !it.supported }

    /** 该版本可选使用但设备缺失的功能/扩展 */
    val missingOptional: List<VulkanDependencyStatus>
        get() = allStatuses.filter { it.level == VulkanDependencyLevel.OPTIONAL && !it.supported }

    /** 设备能否以 Vulkan 启动该版本 */
    val isSupported: Boolean
        get() = missingRequired.isEmpty()
}

/**
 * 一类依赖需求相同的 Minecraft 版本范围的 Vulkan 支持情况
 */
data class VulkanProfileSupport(
    val since: String,
    /** 排他上界（不含），null 表示无上界 */
    val until: String?,
    val supported: Boolean
) {
    /** 版本范围的展示文本 */
    val versionRangeText: String
        get() = when (until) {
            null -> "$since+"
            since.nextNumberVersion() -> since
            else -> until.prevNumberVersion()?.let { "$since-$it" } ?: "$since~$until"
        }
}

/**
 * 设备是否支持指定功能/扩展，与 Minecraft 版本无关
 */
fun VulkanCapabilities.supports(dependency: VulkanDependency): Boolean {
    val isExtension = VulkanRequirements.EXTENSIONS.any { it.name == dependency.name }
    return if (isExtension) {
        dependency.name in extensions
    } else {
        features[dependency.name] == true
    }
}

/**
 * 评估设备对指定 Minecraft 版本的 Vulkan 支持情况
 */
fun VulkanCapabilities.supportFor(mcVersion: String): VulkanSupport {
    fun statusOf(dependency: VulkanDependency): VulkanDependencyStatus {
        return VulkanDependencyStatus(dependency, dependency.levelAt(mcVersion), supports(dependency))
    }

    return VulkanSupport(
        mcVersion = mcVersion,
        extensionStatuses = VulkanRequirements.EXTENSIONS.map(::statusOf),
        featureStatuses = VulkanRequirements.FEATURES.map(::statusOf)
    )
}

/**
 * 评估设备对各 Minecraft 版本范围的 Vulkan 支持情况，相邻且支持情况一致的区间会被合并；
 * Vulkan 1.2 以下不满足运行基线，所有版本范围均视为不支持
 */
fun VulkanCapabilities.profileSupport(): List<VulkanProfileSupport> {
    val versions = VulkanRequirements.PROFILE_VERSIONS
    if (!isVersionSupported) {
        return listOf(VulkanProfileSupport(since = versions.first(), until = null, supported = false))
    }
    return versions.mapIndexed { index, since ->
        VulkanProfileSupport(
            since = since,
            until = versions.getOrNull(index + 1),
            supported = supportFor(since).isSupported
        )
    }.fold(mutableListOf()) { merged, profile ->
        val last = merged.lastOrNull()
        if (last != null && last.supported == profile.supported) {
            merged[merged.lastIndex] = last.copy(until = profile.until)
        } else {
            merged.add(profile)
        }
        merged
    }
}
