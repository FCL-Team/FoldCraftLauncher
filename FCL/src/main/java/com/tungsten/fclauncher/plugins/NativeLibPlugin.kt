package com.tungsten.fclauncher.plugins

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.tungsten.fclauncher.utils.FCLPath
import java.nio.file.Path
import java.nio.file.Paths

object NativeLibPlugin {
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

    private var isInit = false

    private const val PACKAGE_FLAGS =
        PackageManager.GET_META_DATA or PackageManager.GET_SHARED_LIBRARY_FILES



    @JvmStatic
    val pluginList: MutableList<NativePlugin> = mutableListOf()
        get() {
            if (!isInit) {
                FCLPath.CONTEXT?.let {
                    init(it)
                }
            }
            return field
        }

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

    @JvmStatic
    fun init(context: Context) {
        isInit = true
        val queryIntentActivities =
            context.packageManager.queryIntentActivities(
                Intent("android.intent.action.MAIN"),
                PACKAGE_FLAGS
            )
        queryIntentActivities.forEach {
            parse(
                packageManager = context.packageManager,
                info = it.activityInfo.applicationInfo
            )
        }
    }

    @JvmStatic
    fun isAvailable(): Boolean {
        return pluginList.isNotEmpty()
    }

    @JvmStatic
    fun refresh(context: Context) {
        pluginList.clear()
        isInit = false
        init(context)
    }

    private fun parse(
        packageManager: PackageManager,
        info: ApplicationInfo
    ) {
        if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            val metaData = info.metaData ?: return
            if (metaData.getBoolean("FCLNativePlugin", false)) {
                val nativeLibraryDir = info.nativeLibraryDir
                val packageName = info.packageName
                val appName = info.loadLabel(packageManager).toString()
                val appVersion = packageManager.getPackageInfo(packageName, 0).versionName ?: ""

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
                    packageName = packageName,
                    appName = appName,
                    appVersion = appVersion,
                    displayName = des,
                    minMCVer = metaData.safeGetString("minMCVer") ?: "",
                    maxMCVer = metaData.safeGetString("maxMCVer") ?: "",
                    path = nativeLibraryDir,
                    envMap = envMap
                )
                pluginList.add(plugin)
            }
        }
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