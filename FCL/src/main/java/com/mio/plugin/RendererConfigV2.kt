@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.mio.plugin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * RendererPlugin v2 规范的渲染器配置（插件 APK 内 @string/config 资源中的 JSON），
 * 结构与 ZalithLauncher/RendererPlugin-v2 的 DSL 序列化结果一一对应。
 */
@Serializable
data class RendererConfigV2(
    @SerialName("displayName")
    val displayName: String,
    @SerialName("rendererId")
    val rendererId: String,
    @SerialName("rendererGLPath")
    val rendererGLPath: String,
    @SerialName("rendererEGLPath")
    val rendererEGLPath: String,
    @SerialName("dlopenLibPaths")
    val dlopenLibPaths: List<String> = emptyList(),
    @SerialName("env")
    val env: List<EnvV2> = emptyList(),
    @SerialName("minMCVer")
    val minMCVer: String? = null,
    @SerialName("maxMCVer")
    val maxMCVer: String? = null,
)

@Serializable
sealed interface EnvV2

/** 固定环境变量，不可配置 */
@Serializable
@SerialName("NormalEnv")
data class NormalEnvV2(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String,
) : EnvV2

/** 预设选项中选择值的环境变量；check 为 null 表示始终启用，否则为启用开关的默认状态 */
@Serializable
@SerialName("SelectableEnv")
data class SelectableEnvV2(
    @SerialName("key")
    val key: String,
    @SerialName("title")
    val title: MetaStringV2? = null,
    @SerialName("check")
    val check: Boolean? = true,
    @SerialName("items")
    val items: EnvItemsV2,
) : EnvV2

/** 用户自由输入值的环境变量，值为空则不启用 */
@Serializable
@SerialName("CustomizableEnv")
data class CustomizableEnvV2(
    @SerialName("key")
    val key: String,
    @SerialName("title")
    val title: MetaStringV2? = null,
    @SerialName("defaultValue")
    val defaultValue: String? = null,
) : EnvV2

/** 开关式环境变量，toggle 决定默认是否启用 */
@Serializable
@SerialName("ToggleableEnv")
data class ToggleableEnvV2(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String,
    @SerialName("title")
    val title: MetaStringV2? = null,
    @SerialName("toggle")
    val toggle: Boolean = true,
) : EnvV2

/** selectable 环境变量的候选值：defaultValue 视为列表中的一项 */
@Serializable
data class EnvItemsV2(
    @SerialName("defaultValue")
    val defaultValue: String,
    @SerialName("values")
    val values: List<String> = emptyList(),
)

/** 标题的资源索引：启动器通过插件 meta-data 中同名项的资源 id 读取本地化文本 */
@Serializable
data class MetaStringV2(
    @SerialName("key")
    val key: String,
)

/** v2 插件的 JSON 容忍未知字段（与官方 DSL 的解析行为一致） */
val rendererV2Json: Json = Json {
    ignoreUnknownKeys = true
}
