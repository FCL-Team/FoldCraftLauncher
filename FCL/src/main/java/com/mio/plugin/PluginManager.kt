package com.mio.plugin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.mio.datastore.pluginDataStore
import com.mio.manager.RendererManager
import com.tungsten.fcl.FCLApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 插件统一管理：一次扫描已安装的插件应用（含固定包名的 FFmpeg 插件），
 * 维护启用/禁用状态（DataStore 持久化）。
 * RendererPlugin / DriverPlugin / NativeLibPlugin 共享本类的扫描结果，
 * 只保留各自的 meta-data 解析逻辑。
 */
object PluginManager {

    /** 插件类型（一个插件应用可同时声明多种类型） */
    enum class PluginType { RENDERER, DRIVER, NATIVE_LIB, FFMPEG }

    /** FFmpeg 插件为固定包名，无 meta-data 声明 */
    const val FFMPEG_PACKAGE = "net.kdt.pojavlaunch.ffmpeg"

    /** 渲染器/驱动插件的 meta-data 声明开关 */
    const val META_PLUGIN = "fclPlugin"

    /** 渲染器插件 v2（RendererPlugin-v2）的 meta-data 声明，值为 JSON 配置资源 id */
    const val META_PLUGIN_V2 = "fclPlugin_V2"

    /** 原生库插件的 meta-data 声明开关 */
    const val META_NATIVE_PLUGIN = "FCLNativePlugin"

    /** 统一插件模型（应用粒度） */
    data class PluginApp(
        val packageName: String,
        val label: String,
        val versionName: String?,
        val icon: Drawable?,
        val types: Set<PluginType>,
        val appInfo: ApplicationInfo,
        val lastUpdateTime: Long,
    )

    private const val SCAN_FLAGS = PackageManager.GET_META_DATA

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /** 禁用的插件包名集合（内存缓存，DataStore 持久化） */
    private val disabledPackages = MutableStateFlow<Set<String>>(emptySet())

    @Volatile
    private var disabledLoaded = false

    private var isScanInit = false

    private val scannedApps = mutableListOf<PluginApp>()
        get() {
            if (!isScanInit) {
                scan(FCLApp.getAppContext())
            }
            return field
        }

    /** 失效扫描缓存：卸载/安装在系统侧发生，进程内缓存不会自动感知，
     *  需要在合适的时机（如插件管理页回到前台）主动失效后重扫 */
    fun invalidate() {
        isScanInit = false
    }

    /** 已识别的全部插件应用（含已禁用，供管理页展示） */
    fun allApps(context: Context): List<PluginApp> {
        ensureDisabledLoaded(context)
        return scannedApps.toList()
    }

    /** 已识别且未禁用的插件应用，供各插件共享 */
    fun enabledApps(context: Context): List<PluginApp> {
        ensureDisabledLoaded(context)
        return scannedApps.filter { it.packageName !in disabledPackages.value }
    }

    /** 插件是否启用（同步读取内存缓存；持久化数据尚未加载完成时视为启用） */
    @JvmStatic
    fun isEnabled(context: Context, packageName: String): Boolean {
        ensureDisabledLoaded(context)
        return packageName !in disabledPackages.value
    }

    @JvmStatic
    fun setEnabled(context: Context, packageName: String, enabled: Boolean) {
        ensureDisabledLoaded(context)
        disabledPackages.update { current ->
            current.toMutableSet().apply { if (enabled) remove(packageName) else add(packageName) }
        }
        scope.launch {
            context.pluginDataStore.updateData { pref ->
                val disabled = pref.disabledPlugins.toMutableSet()
                if (enabled) disabled.remove(packageName) else disabled.add(packageName)
                pref.copy(disabledPlugins = disabled.toList())
            }
        }
    }

    /** 失效扫描缓存并刷新全部插件列表（禁用/卸载插件后调用） */
    fun refreshAll(context: Context) {
        isScanInit = false
        RendererManager.refresh(context)
        DriverPlugin.refresh(context)
        NativeLibPlugin.refresh(context)
    }

    /** 首次访问时同步加载持久化的禁用集合（小文件，仅一次） */
    private fun ensureDisabledLoaded(context: Context) {
        if (disabledLoaded) return
        synchronized(this) {
            if (disabledLoaded) return
            val pref = runBlocking { context.pluginDataStore.data.first() }
            disabledPackages.value = pref.disabledPlugins.toSet()
            disabledLoaded = true
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun scan(context: Context) {
        isScanInit = true
        scannedApps.clear()
        val pm = context.packageManager
        val apps = mutableListOf<PluginApp>()
        pm.queryIntentActivities(Intent(Intent.ACTION_MAIN), SCAN_FLAGS).forEach { resolved ->
            val info = resolved.activityInfo?.applicationInfo ?: return@forEach
            if (info.flags and ApplicationInfo.FLAG_SYSTEM != 0) return@forEach
            val metaData = info.metaData ?: return@forEach
            val isPlugin = metaData.getBoolean(META_PLUGIN, false) ||
                    metaData.getBoolean(META_NATIVE_PLUGIN, false) ||
                    metaData.containsKey(META_PLUGIN_V2)
            if (!isPlugin) return@forEach
            buildApp(pm, info)?.let { apps.add(it) }
        }
        // FFmpeg 插件：固定包名、无 meta-data 声明，单独探测
        try {
            buildApp(pm, pm.getApplicationInfo(FFMPEG_PACKAGE, 0))?.let { apps.add(it) }
        } catch (_: PackageManager.NameNotFoundException) {
        }
        scannedApps.addAll(apps.distinctBy { it.packageName }.sortedBy { it.label.lowercase() })
    }

    /** 无有效类型声明的包（如仅声明了 fclPlugin 但未提供任何插件内容）返回 null */
    private fun buildApp(pm: PackageManager, info: ApplicationInfo): PluginApp? {
        val types = buildSet {
            val metaData = info.metaData
            if (info.packageName == FFMPEG_PACKAGE) {
                add(PluginType.FFMPEG)
            } else {
                if (metaData?.getString("renderer") != null ||
                    metaData?.containsKey(META_PLUGIN_V2) == true
                ) add(PluginType.RENDERER)
                if (metaData?.getString("driver") != null) add(PluginType.DRIVER)
                if (metaData?.getBoolean(
                        META_NATIVE_PLUGIN,
                        false
                    ) == true
                ) add(PluginType.NATIVE_LIB)
            }
        }
        if (types.isEmpty()) return null
        val packageName = info.packageName
        val label = runCatching { info.loadLabel(pm).toString() }.getOrDefault(packageName)
        val packageInfo = runCatching { pm.getPackageInfo(packageName, 0) }.getOrNull()
        val versionName = packageInfo?.versionName
        val icon = runCatching { info.loadIcon(pm) }.getOrNull()
        return PluginApp(
            packageName,
            label,
            versionName,
            icon,
            types,
            info,
            packageInfo?.lastUpdateTime ?: 0L
        )
    }
}
