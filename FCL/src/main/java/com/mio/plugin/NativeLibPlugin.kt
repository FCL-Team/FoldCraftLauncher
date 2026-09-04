package com.mio.plugin

import java.nio.file.Path
import java.nio.file.Paths

object NativeLibPlugin : AbstractPlugin<NativeLibPlugin.NativePlugin>() {

    data class NativePlugin(
        val packageName: String,
        val appName: String,
        val appVersion: String,
        val displayName: String,
        val minMCVer: String,
        val maxMCVer: String,
        val path: String,
        val envMap: Map<String, String>
    )

    /** 原生库插件列表（懒初始化） */
    @JvmStatic
    val pluginList: List<NativePlugin>
        get() = items

    @JvmStatic
    fun getPaths(split: String): String {
        return pluginList.joinToString(split) { it.path }
    }

    /**
     * 获取所有未禁用的原生库插件的 JVM 环境参数
     */
    @JvmStatic
    fun getJVMEnv(): Map<String, String> {
        return buildMap {
            pluginList.forEach { plugin ->
                putAll(plugin.envMap)
            }
        }
    }

    override fun parse(app: PluginManager.PluginApp) {
        val metaData = app.appInfo.metaData ?: return
        if (!metaData.getBoolean(PluginManager.META_NATIVE_PLUGIN, false)) return

        val nativeLibraryDir = app.appInfo.nativeLibraryDir
        val environment = metaData.getString("environment") ?: return
        val des = metaData.getString("des") ?: ""

        val envMap = if (environment.isNotEmpty()) {
            val entries = environment.split(" ")
            buildMap {
                entries.forEach { entry ->
                    put(parseEntry(entry, nativeLibraryDir))
                }
            }
        } else {
            emptyMap()
        }

        val plugin = NativePlugin(
            packageName = app.packageName,
            appName = app.label,
            appVersion = app.versionName ?: "",
            displayName = des,
            minMCVer = metaData.safeGetString("minMCVer") ?: "",
            maxMCVer = metaData.safeGetString("maxMCVer") ?: "",
            path = nativeLibraryDir,
            envMap = envMap
        )
        items.add(plugin)
    }

    private const val NATIVE_LIB_DIR_PLACEHOLDER = "{nativeLibraryDir}"

    private fun parseEntry(
        entry: String,
        nativeLibraryDir: String
    ): Pair<String, String> {
        var (key, value) = entry.split("=")

        if (value.startsWith(NATIVE_LIB_DIR_PLACEHOLDER)) {
            if (value == NATIVE_LIB_DIR_PLACEHOLDER) {
                value = nativeLibraryDir
            } else {
                val path = safePath(
                    baseDir = nativeLibraryDir,
                    input = value.removePrefix(NATIVE_LIB_DIR_PLACEHOLDER)
                )
                value = path?.toAbsolutePath()?.toString() ?: nativeLibraryDir
            }
        }

        return Pair(key, value)
    }

    private fun <K, V> MutableMap<K, V>.put(value: Pair<K, V>) {
        this[value.first] = value.second
    }

    private fun safePath(baseDir: String, input: String): Path? {
        return try {
            val basePath = Paths.get(baseDir).normalize().toAbsolutePath()
            val resolvedPath = basePath.resolve(input).normalize().toAbsolutePath()

            if (resolvedPath.startsWith(basePath)) {
                resolvedPath
            } else {
                null //阻止路径穿越
            }
        } catch (_: Exception) {
            null //无效的路径
        }
    }
}
