@file:OptIn(InternalSerializationApi::class)

package com.mio.datastore

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.mio.device.VulkanCheckRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class GameItemBarSetting(val slideSelection: Boolean, val doubleTapSwapHands: Boolean)

/**
 * 功能引导持久化数据（DataStore）：
 * @param shownTags 已展示过的引导标识列表（GuideTag 的类名），避免引导重复弹出
 */
@Serializable
data class GuidePreference(
    val shownTags: List<String> = emptyList(),
)

@Serializable
data class LaunchCountSetting(
    val totalLaunchCount: Int = 0,
    val pityCounter: Int = 0,
    val pendingPopup: Boolean = false,
)

/**
 * 模组列表排序字段：DEFAULT 为扫描加载顺序（增量显示），其余为手动排序维度（全量加载后排序显示）
 */
enum class ModSortField {
    DEFAULT, NAME, FILE_NAME, SIZE, DATE
}

@Serializable
data class ModSortPreference(
    val field: ModSortField = ModSortField.DEFAULT,
    val ascending: Boolean = true,
)

/**
 * 模组列表排序偏好（全局生效），DataStore 持久化。
 */

/**
 * 插件管理持久化数据（DataStore）：
 * @param disabledPlugins 被禁用的插件包名列表
 * @param rendererEnvPrefs v2 渲染器插件的用户环境变量配置，
 *   外层 key 为插件包名，内层 key 为环境变量名；值按类型存：
 *   selectable 存选中值、customizable 存输入文本、toggleable 存 "true"/"false"
 * @param miolibpatcherEnabled MioLibPatcher 是否注入（false 时不加 -javaagent）
 * @param miolibpatcherAlc10 启用 ALC10 补丁（-Dmiolibpatcher.alc10=true）
 * @param miolibpatcherSablerapier Sable Rapier 补丁是否启用，默认 true（不设置
 *   属性，补丁默认生效）；关闭时设置 -Dmiolibpatcher.sablerapier=false 强制禁用
 * @param miolibpatcherAsmBackport 启用 ASM 5.0.4 后门（-Dmiolibpatcher.asmBackport=true）
 */
@Serializable
data class PluginPreference(
    val disabledPlugins: List<String> = emptyList(),
    val rendererEnvPrefs: Map<String, Map<String, String>> = emptyMap(),
    val miolibpatcherEnabled: Boolean = true,
    val miolibpatcherAlc10: Boolean = false,
    val miolibpatcherSablerapier: Boolean = true,
    val miolibpatcherAsmBackport: Boolean = false,
)

@Serializable
data class SkinAnimationSetting(
    val animationId: String = "walking",
    val solidLayerEnabled: Boolean = true,
    val upperBodySeparated: Boolean = true,
)

/**
 * Vulkan 检测持久化数据（DataStore）：
 * @param record 最近一次设备 Vulkan 能力检测结果，null 表示尚未检测过
 */
@Serializable
data class VulkanCheckPreference(
    val record: VulkanCheckRecord? = null,
)

/**
 * 版本列表置顶持久化数据（DataStore）：
 * @param pinned 置顶版本表，外层 key 为 Profile 名称，value 为该 Profile 的置顶版本 id
 *   列表（列表顺序即置顶显示顺序）
 */
@Serializable
data class VersionPinPreference(
    val pinned: Map<String, List<String>> = emptyMap(),
)

/**
 * 应用设置总集：全部领域设置合并为单个 DataStore 文件持久化，
 * 各领域为独立 data class，字段默认值即该领域出厂设置。
 */
@Serializable
data class AppSettingsPreference(
    val gameItemBar: GameItemBarSetting = GameItemBarSetting(slideSelection = true, doubleTapSwapHands = true),
    val guide: GuidePreference = GuidePreference(),
    val launchCount: LaunchCountSetting = LaunchCountSetting(),
    val modSort: ModSortPreference = ModSortPreference(),
    val plugin: PluginPreference = PluginPreference(),
    val skinAnimation: SkinAnimationSetting = SkinAnimationSetting(),
    val vulkanCheck: VulkanCheckPreference = VulkanCheckPreference(),
    val versionPin: VersionPinPreference = VersionPinPreference(),
)

object AppSettingsSerializer : Serializer<AppSettingsPreference> {
    // 忽略未知 key：字段改名或删除后旧数据仍可解码，避免整体回退默认值丢失其余设置
    private val json = Json { ignoreUnknownKeys = true }

    override val defaultValue: AppSettingsPreference = AppSettingsPreference()

    override suspend fun readFrom(input: InputStream): AppSettingsPreference {
        return try {
            json.decodeFromString<AppSettingsPreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettingsPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}

/** 旧版分文件设置的文件名，仅迁移使用 */
private val LEGACY_FILE_NAMES = listOf(
    "game_item_bar_settings.json",
    "guide_settings.json",
    "launch_count_settings.json",
    "mod_sort_settings.json",
    "plugin_settings.json",
    "skin_animation_settings.json",
    "vulkan_check.json",
)

val Context.appSettingsDataStore: DataStore<AppSettingsPreference> by dataStore(
    fileName = "app_settings.json",
    serializer = AppSettingsSerializer,
    produceMigrations = { listOf(LegacySettingsMigration(it)) },
)

/**
 * 旧版分文件设置（每领域一个 json）的一次性迁移：首次读写时读入合并，
 * 完成后删除旧文件；旧文件全部消失即视为迁移完成。
 */
private class LegacySettingsMigration(context: Context) : DataMigration<AppSettingsPreference> {
    private val dir = File(context.applicationContext.filesDir, "datastore")

    private fun legacyFiles(): List<File> =
        dir.listFiles { file -> file.isFile && file.name in LEGACY_FILE_NAMES }?.toList().orEmpty()

    private inline fun <reified T> readLegacy(fileName: String): T? =
        runCatching { Json.decodeFromString<T>(File(dir, fileName).readText()) }.getOrNull()

    override suspend fun shouldMigrate(currentData: AppSettingsPreference): Boolean =
        legacyFiles().isNotEmpty()

    override suspend fun migrate(currentData: AppSettingsPreference): AppSettingsPreference {
        return currentData.copy(
            gameItemBar = readLegacy<GameItemBarSetting>("game_item_bar_settings.json") ?: currentData.gameItemBar,
            guide = readLegacy<GuidePreference>("guide_settings.json") ?: currentData.guide,
            launchCount = readLegacy<LaunchCountSetting>("launch_count_settings.json") ?: currentData.launchCount,
            modSort = readLegacy<ModSortPreference>("mod_sort_settings.json") ?: currentData.modSort,
            plugin = readLegacy<PluginPreference>("plugin_settings.json") ?: currentData.plugin,
            skinAnimation = readLegacy<SkinAnimationSetting>("skin_animation_settings.json") ?: currentData.skinAnimation,
            vulkanCheck = readLegacy<VulkanCheckPreference>("vulkan_check.json") ?: currentData.vulkanCheck,
        )
    }

    override suspend fun cleanUp() {
        // 旧数据文件与写入残留的临时文件一并删除
        dir.listFiles { file -> file.isFile && LEGACY_FILE_NAMES.any { file.name.startsWith(it) } }
            ?.forEach { it.delete() }
    }
}
