package com.mio.plugin

import android.content.Context
import android.os.Bundle
import com.mio.data.Renderer
import com.mio.datastore.appSettingsDataStore
import com.mio.manager.RendererManager
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
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
        /** 标题（一般为变量名） */
        val title: String,
        /** selectable 候选值（含默认值），其他类型为空 */
        val options: List<String> = emptyList(),
        /** customizable 提示用默认值 */
        val defaultValue: String? = null,
        /** selectable 是否带启用开关（check 非 null 时） */
        val checkable: Boolean = false,
        /** 当前开关状态（selectable / toggleable） */
        val enabled: Boolean,
        /** 当前值（selectable 选中值 / customizable 输入文本） */
        val value: String,
        /** 标题下的功能说明（可空；内置渲染器有，v2 插件暂无） */
        val description: String? = null,
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
            runBlocking { context.appSettingsDataStore.data.first().plugin.rendererEnvPrefs }
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
        storeEnvPrefs(context, packageName, flat)
        val entry = v2Entries[packageName] ?: return
        val renderer = buildV2Renderer(packageName, entry)
        addRenderer(renderer)
        RendererManager.replaceRenderer(renderer)
    }

    /** 更新内存缓存并异步落盘某键的扁平环境变量配置（插件与内置渲染器共用） */
    private fun storeEnvPrefs(context: Context, key: String, flat: Map<String, String>) {
        envPrefs = envPrefs.toMutableMap().apply { put(key, flat) }
        saveScope.launch {
            runCatching {
                context.appSettingsDataStore.updateData { pref ->
                    pref.copy(plugin = pref.plugin.copy(
                        rendererEnvPrefs = pref.plugin.rendererEnvPrefs.toMutableMap()
                            .apply { put(key, flat) }
                    ))
                }
            }.onFailure { e ->
                Logging.LOG.log(Level.SEVERE, "保存插件环境变量配置失败", e)
            }
        }
    }

    // ---------- 内置渲染器配置（MobileGlues / SimpleFPEWrapper） ----------
    // 内置渲染器没有插件包名，复用 envPrefs 存储并以 "builtin:" 前缀伪键区分；
    // 配置入口为渲染器选择列表的齿轮图标，编辑用与插件页相同的 RendererEnvDialog

    @JvmField
    val BUILTIN_MOBILEGLUES_KEY = "builtin:mobileglues"

    @JvmField
    val BUILTIN_SFPEW_KEY = "builtin:sfpew"

    private fun builtinKey(rendererId: String): String? = when (rendererId) {
        Renderer.ID_MOBILEGLUES -> BUILTIN_MOBILEGLUES_KEY
        Renderer.ID_SFPEW -> BUILTIN_SFPEW_KEY
        else -> null
    }

    /** 内置渲染器是否提供可配置环境变量（渲染器列表齿轮图标的显隐依据） */
    fun hasBuiltinEnvConfig(rendererId: String): Boolean = builtinKey(rendererId) != null

    /** 内置渲染器的可配置环境变量列表（已应用用户配置的当前值） */
    fun builtinEnvSpecs(context: Context, rendererId: String): List<EnvSpec> {
        val key = builtinKey(rendererId) ?: return emptyList()
        ensureInit()
        val prefs = envPrefs[key].orEmpty()
        return when (rendererId) {
            Renderer.ID_MOBILEGLUES -> listOf(
                EnvSpec(
                    key = "MG_COUNT_LAUNCH",
                    type = EnvType.TOGGLEABLE,
                    title = "MG_COUNT_LAUNCH",
                    description = context.getString(R.string.renderer_config_mg_count_launch_desc),
                    enabled = prefs["MG_COUNT_LAUNCH$ENABLED_SUFFIX"]?.toBooleanStrictOrNull() ?: true,
                    value = "1"
                ),
                EnvSpec(
                    key = "enableANGLE",
                    type = EnvType.SELECTABLE,
                    title = "enableANGLE",
                    description = context.getString(R.string.renderer_config_mg_angle_desc),
                    options = listOf("0", "1", "2", "3"),
                    checkable = false,
                    enabled = true,
                    value = prefs["enableANGLE"] ?: "1"
                ),
                EnvSpec(
                    key = "enableNoError",
                    type = EnvType.SELECTABLE,
                    title = "enableNoError",
                    description = context.getString(R.string.renderer_config_mg_noerror_desc),
                    options = listOf("0", "1", "2", "3"),
                    checkable = false,
                    enabled = true,
                    value = prefs["enableNoError"] ?: "0"
                ),
                EnvSpec(
                    key = "angleDepthClearFixMode",
                    type = EnvType.TOGGLEABLE,
                    title = "angleDepthClearFixMode",
                    description = context.getString(R.string.renderer_config_mg_depthfix_desc),
                    enabled = prefs["angleDepthClearFixMode$ENABLED_SUFFIX"]?.toBooleanStrictOrNull() ?: false,
                    value = "1"
                ),
                EnvSpec(
                    key = "customGLVersion",
                    type = EnvType.SELECTABLE,
                    title = "customGLVersion",
                    description = context.getString(R.string.renderer_config_mg_glversion_desc),
                    options = listOf("0", "46", "45", "44", "43", "42", "41", "40", "33", "32"),
                    checkable = false,
                    enabled = true,
                    value = prefs["customGLVersion"] ?: "0"
                ),
                EnvSpec(
                    key = "maxGlslCacheSize",
                    type = EnvType.CUSTOMIZABLE,
                    title = "maxGlslCacheSize",
                    description = context.getString(R.string.renderer_config_mg_glslcache_desc),
                    defaultValue = "32",
                    enabled = true,
                    value = prefs["maxGlslCacheSize"] ?: "32"
                ),
                EnvSpec(
                    key = "enableExtTimerQuery",
                    type = EnvType.TOGGLEABLE,
                    title = "enableExtTimerQuery",
                    description = context.getString(R.string.renderer_config_mg_ext_timer_desc),
                    enabled = prefs["enableExtTimerQuery$ENABLED_SUFFIX"]?.toBooleanStrictOrNull() ?: true,
                    value = "1"
                ),
                EnvSpec(
                    key = "enableExtComputeShader",
                    type = EnvType.TOGGLEABLE,
                    title = "enableExtComputeShader",
                    description = context.getString(R.string.renderer_config_mg_ext_compute_desc),
                    enabled = prefs["enableExtComputeShader$ENABLED_SUFFIX"]?.toBooleanStrictOrNull() ?: false,
                    value = "1"
                ),
                EnvSpec(
                    key = "enableExtDirectStateAccess",
                    type = EnvType.TOGGLEABLE,
                    title = "enableExtDirectStateAccess",
                    description = context.getString(R.string.renderer_config_mg_ext_dsa_desc),
                    enabled = prefs["enableExtDirectStateAccess$ENABLED_SUFFIX"]?.toBooleanStrictOrNull() ?: false,
                    value = "1"
                ),
                EnvSpec(
                    key = "fsr1Setting",
                    type = EnvType.SELECTABLE,
                    title = "fsr1Setting",
                    description = context.getString(R.string.renderer_config_mg_fsr1_desc),
                    options = listOf("0", "1", "2", "3", "4"),
                    checkable = false,
                    enabled = true,
                    value = prefs["fsr1Setting"] ?: "0"
                )
            )

            Renderer.ID_SFPEW -> listOf(
                EnvSpec(
                    key = "SFPEW_RELAXED_CONTEXT",
                    type = EnvType.TOGGLEABLE,
                    title = "SFPEW_RELAXED_CONTEXT",
                    description = context.getString(R.string.renderer_config_sfpew_relaxed_context_desc),
                    enabled = prefs["SFPEW_RELAXED_CONTEXT$ENABLED_SUFFIX"]?.toBooleanStrictOrNull() ?: true,
                    value = "1"
                )
            )

            else -> emptyList()
        }
    }

    /** 启动链路读取内置渲染器的扁平配置（FCLauncher 注入 env 用） */
    @JvmStatic
    fun builtinEnvPrefs(rendererId: String): Map<String, String> {
        val key = builtinKey(rendererId) ?: return emptyMap()
        ensureInit()
        return envPrefs[key].orEmpty()
    }

    /** 开关型配置的启用状态读取（键名后缀约定收口于此，供 Java 侧注入 env / 写 MG 配置用；未配置回落默认值） */
    @JvmStatic
    fun toggleEnabled(prefs: Map<String, String>, key: String, def: Boolean): Boolean =
        prefs[key + ENABLED_SUFFIX]?.toBooleanStrictOrNull() ?: def

    /** 配置键是否被用户显式配置过（值键或开关键任一存在；未配置的键交由渲染器自管默认值） */
    @JvmStatic
    fun isConfigured(prefs: Map<String, String>, key: String): Boolean =
        prefs.containsKey(key) || prefs.containsKey(key + ENABLED_SUFFIX)

    /** 内置渲染器配置对话框确认后保存（无 v2 渲染器重建） */
    fun updateBuiltinEnvConfigs(context: Context, rendererId: String, values: Map<String, EnvValue>) {
        val key = builtinKey(rendererId) ?: return
        ensureInit()
        val flat = mutableMapOf<String, String>()
        values.forEach { (k, v) ->
            v.value?.let { flat[k] = it }
            v.enabled?.let { flat[k + ENABLED_SUFFIX] = it.toString() }
        }
        storeEnvPrefs(context, key, flat)
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
