package com.mio.plugin

import android.content.Context
import android.os.Bundle
import com.mio.data.Renderer
import com.mio.datastore.pluginDataStore
import com.mio.manager.RendererManager
import com.tungsten.fcl.FCLApp
import com.tungsten.fclcore.util.Logging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.logging.Level

object RendererPlugin : AbstractPlugin<Renderer>() {

    /** 插件渲染器列表（懒初始化，内置渲染器见 RendererManager） */
    @JvmStatic
    val rendererList: List<Renderer>
        get() = items

    /** v2 路径前缀，启动器替换为插件 nativeLibraryDir */
    private const val NATIVE_PREFIX = "**|"

    /** v1 启动链路约定：eglName/glName 以 / 开头时运行时拼接插件 nativeLibraryDir */
    private const val V1_DIR_PREFIX = "/"

    /** 可配置环境变量在配置表中的启用状态键后缀（selectable 专用） */
    private const val ENABLED_SUFFIX = "@enabled"

    /** 可配置环境变量类型（供配置对话框区分控件） */
    enum class EnvType { SELECTABLE, CUSTOMIZABLE, TOGGLEABLE }

    /** 单个可配置环境变量在对话框中展示/编辑所需的完整信息 */
    data class EnvSpec(
        val key: String,
        val type: EnvType,
        val title: String,
        /** selectable 候选值（含默认值），其他类型为空 */
        val options: List<String>,
        /** customizable 提示用默认值 */
        val defaultValue: String?,
        /** selectable 是否带启用开关（check 非 null 时） */
        val checkable: Boolean,
        /** 当前开关状态（selectable / toggleable） */
        val enabled: Boolean,
        /** 当前值（selectable 选中值 / customizable 输入文本） */
        val value: String,
    )

    /** 对话框确认后单项的回传值；enabled/value 为 null 表示该项无此状态 */
    data class EnvValue(val enabled: Boolean? = null, val value: String? = null)

    /** 已解析的 v2 插件配置（包名 → 条目），init/refresh 时重建 */
    private class V2Entry(
        val config: RendererConfigV2,
        val nativeLibraryDir: String,
        /** envKey → 插件内本地化标题 */
        val titles: Map<String, String>,
        /** 插件应用名（渲染器来源显示） */
        val label: String,
    )

    private val v2Entries = mutableMapOf<String, V2Entry>()

    /** 用户环境变量配置（包名 → 扁平键值），init 时从 DataStore 读入，UI 修改后同步更新 */
    private var envPrefs: Map<String, Map<String, String>> = emptyMap()

    private val saveScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onInit(context: Context) {
        v2Entries.clear()
        envPrefs = runCatching {
            runBlocking { context.pluginDataStore.data.first().rendererEnvPrefs }
        }.getOrDefault(emptyMap())
    }

    override fun parse(app: PluginManager.PluginApp) {
        val metaData = app.appInfo.metaData ?: return
        // v2 优先：同时声明新旧架构的插件只加载 v2 配置
        if (metaData.containsKey(PluginManager.META_PLUGIN_V2)) {
            parseV2(app, metaData)
            return
        }
        if (metaData.getBoolean(PluginManager.META_PLUGIN, false)) {
            parseV1(app, metaData)
        }
    }

    private fun parseV1(app: PluginManager.PluginApp, metaData: Bundle) {
        val rendererString = metaData.getString("renderer") ?: return
        val des = metaData.getString("des") ?: return
        val boatEnvString = metaData.getString("boatEnv") ?: return
        val pojavEnvString = metaData.getString("pojavEnv") ?: return
        val nativeLibraryDir = app.appInfo.nativeLibraryDir
        val renderer = rendererString.split(":")
        val boatEnv = boatEnvString.split(":")
        val pojavEnv = pojavEnvString.split(":")
        val minMCVer = metaData.safeGetString("minMCVer") ?: ""
        val maxMCVer = metaData.safeGetString("maxMCVer") ?: ""
        addRenderer(
            Renderer(
                renderer[0],
                des,
                renderer[1],
                renderer[2],
                nativeLibraryDir,
                boatEnv,
                pojavEnv,
                app.packageName,
                minMCVer,
                maxMCVer,
                source = app.label
            )
        )
    }

    private fun parseV2(app: PluginManager.PluginApp, metaData: Bundle) {
        val configResId = metaData.getInt(PluginManager.META_PLUGIN_V2)
        if (configResId == 0) return
        val entry = runCatching {
            val pm = FCLApp.getAppContext().packageManager
            val resources = pm.getResourcesForApplication(app.appInfo)
            val config = rendererV2Json.decodeFromString<RendererConfigV2>(resources.getString(configResId))
            val titles = mutableMapOf<String, String>()
            config.env.forEach { env ->
                val titleKey = when (env) {
                    is SelectableEnvV2 -> env.title?.key
                    is CustomizableEnvV2 -> env.title?.key
                    is ToggleableEnvV2 -> env.title?.key
                    else -> null
                } ?: return@forEach
                runCatching {
                    val id = metaData.getInt(titleKey)
                    if (id != 0) titles[titleKey] = resources.getString(id)
                }
            }
            V2Entry(config, app.appInfo.nativeLibraryDir, titles, app.label)
        }.getOrNull() ?: return
        v2Entries[app.packageName] = entry
        addRenderer(buildV2Renderer(app.packageName, entry))
    }

    /** 按当前用户配置把 v2 配置转成 v1 语义的 Renderer（path 非空分支） */
    private fun buildV2Renderer(packageName: String, entry: V2Entry): Renderer {
        val config = entry.config
        return Renderer(
            config.displayName,
            config.displayName,
            toV1LibPath(config.rendererGLPath),
            toV1LibPath(config.rendererEGLPath),
            entry.nativeLibraryDir,
            null,
            buildPojavEnv(entry, envPrefs[packageName].orEmpty()),
            packageName,
            config.minMCVer ?: "",
            config.maxMCVer ?: "",
            config.rendererId,
            entry.label
        )
    }

    /** 是否存在可配置环境变量（决定插件管理页条目是否显示设置按钮） */
    fun hasConfigurableEnvs(packageName: String): Boolean {
        ensureInit()
        val entry = v2Entries[packageName] ?: return false
        return entry.config.env.any { it !is NormalEnvV2 }
    }

    /** 可配置环境变量列表（供配置对话框展示；无 v2 配置的插件返回空） */
    fun getConfigurableEnvs(packageName: String): List<EnvSpec> {
        ensureInit()
        val entry = v2Entries[packageName] ?: return emptyList()
        val prefs = envPrefs[packageName].orEmpty()
        return entry.config.env.mapNotNull { env ->
            when (env) {
                is SelectableEnvV2 -> EnvSpec(
                    env.key,
                    EnvType.SELECTABLE,
                    env.title?.let { entry.titles[it.key] } ?: env.key,
                    buildList {
                        add(env.items.defaultValue)
                        addAll(env.items.values)
                    }.distinct(),
                    env.items.defaultValue,
                    env.check != null,
                    if (env.check == null) true
                    else prefs[env.key + ENABLED_SUFFIX]?.toBooleanStrictOrNull() ?: env.check,
                    prefs[env.key] ?: env.items.defaultValue
                )

                is CustomizableEnvV2 -> EnvSpec(
                    env.key,
                    EnvType.CUSTOMIZABLE,
                    env.title?.let { entry.titles[it.key] } ?: env.key,
                    emptyList(),
                    env.defaultValue,
                    false,
                    true,
                    prefs[env.key] ?: env.defaultValue.orEmpty()
                )

                is ToggleableEnvV2 -> EnvSpec(
                    env.key,
                    EnvType.TOGGLEABLE,
                    env.title?.let { entry.titles[it.key] } ?: env.key,
                    emptyList(),
                    null,
                    false,
                    prefs[env.key + ENABLED_SUFFIX]?.toBooleanStrictOrNull() ?: env.toggle,
                    env.value
                )

                is NormalEnvV2 -> null
            }
        }
    }

    /** 全量保存某插件的环境变量配置：更新内存缓存、异步落盘并重建该插件渲染器 */
    fun updateEnvConfigs(context: Context, packageName: String, values: Map<String, EnvValue>) {
        ensureInit()
        val flat = mutableMapOf<String, String>()
        values.forEach { (key, v) ->
            v.value?.let { flat[key] = it }
            v.enabled?.let { flat[key + ENABLED_SUFFIX] = it.toString() }
        }
        envPrefs = envPrefs.toMutableMap().apply { put(packageName, flat) }
        saveScope.launch {
            runCatching {
                context.pluginDataStore.updateData { pref ->
                    pref.copy(
                        rendererEnvPrefs = pref.rendererEnvPrefs.toMutableMap()
                            .apply { put(packageName, flat) }
                    )
                }
            }.onFailure { e ->
                Logging.LOG.log(Level.SEVERE, "保存插件环境变量配置失败", e)
            }
        }
        val entry = v2Entries[packageName] ?: return
        val renderer = buildV2Renderer(packageName, entry)
        addRenderer(renderer)
        RendererManager.replaceRenderer(renderer)
    }

    /** 触发 AbstractPlugin 的懒初始化（插件扫描 + v1/v2 解析） */
    private fun ensureInit() {
        @Suppress("UNUSED_EXPRESSION")
        items
    }

    /** 生成最终 pojavEnv：可配置项按用户配置取值，DLOPEN 与 dlopenLibPaths 合并为一条 */
    private fun buildPojavEnv(entry: V2Entry, prefs: Map<String, String>): List<String> {
        val dir = entry.nativeLibraryDir
        val result = mutableListOf<String>()
        val dlopen = mutableListOf<String>()
        fun addDlopen(raw: String) {
            raw.split(',').map { it.trim() }.filter { it.isNotEmpty() }.forEach { dlopen.add(it) }
        }
        entry.config.env.forEach { env ->
            when (env) {
                is NormalEnvV2 -> {
                    if (env.key == "DLOPEN") addDlopen(resolveLibValue(env.key, env.value, dir))
                    else result.add("${env.key}=${resolveLibValue(env.key, env.value, dir)}")
                }

                is SelectableEnvV2 -> {
                    val enabled = if (env.check == null) true
                    else prefs[env.key + ENABLED_SUFFIX]?.toBooleanStrictOrNull() ?: env.check
                    if (enabled) {
                        val value = prefs[env.key] ?: env.items.defaultValue
                        if (value.isNotEmpty()) result.add("${env.key}=$value")
                    }
                }

                is CustomizableEnvV2 -> {
                    val value = prefs[env.key] ?: env.defaultValue
                    if (!value.isNullOrEmpty()) result.add("${env.key}=$value")
                }

                is ToggleableEnvV2 -> {
                    val enabled = prefs[env.key + ENABLED_SUFFIX]?.toBooleanStrictOrNull() ?: env.toggle
                    if (enabled) result.add("${env.key}=${resolveLibValue(env.key, env.value, dir)}")
                }
            }
        }
        entry.config.dlopenLibPaths.forEach { addDlopen(resolveLibValue("DLOPEN", it, dir)) }
        if (dlopen.isNotEmpty()) result.add("DLOPEN=${dlopen.distinct().joinToString(",")}")
        return result
    }

    /**
     * 解析环境变量值中的 `**|` 插件库路径：
     * v1 启动链路会对 LIB_MESA_NAME/MESA_LIBRARY/DLOPEN 的值自动拼接插件 nativeLibraryDir，
     * 这些 key 只保留库文件名；其余 key 直接替换为绝对路径。
     */
    private fun resolveLibValue(key: String, raw: String, nativeDir: String): String {
        val joinsDir = key == "LIB_MESA_NAME" || key == "MESA_LIBRARY" || key == "DLOPEN"
        if (raw.startsWith(NATIVE_PREFIX)) {
            val lib = raw.removePrefix(NATIVE_PREFIX)
            return if (joinsDir) lib else "$nativeDir/$lib"
        }
        if (joinsDir && raw.startsWith("/")) return raw.substringAfterLast('/')
        return raw
    }

    /** v2 库路径转 v1 语义：`**|x` → `/x`（运行时由启动链路拼接插件 nativeLibraryDir） */
    private fun toV1LibPath(raw: String): String =
        if (raw.startsWith(NATIVE_PREFIX)) V1_DIR_PREFIX + raw.removePrefix(NATIVE_PREFIX) else raw

    private fun addRenderer(renderer: Renderer) {
        items.removeIf { it.id == renderer.id }
        items.add(renderer)
    }
}
