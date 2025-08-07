package com.tungsten.fclauncher.plugins

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import com.mio.data.Renderer
import com.tungsten.fclauncher.utils.FCLPath

object RendererPlugin {
    private var isInit = false;
    private const val PACKAGE_FLAGS =
        PackageManager.GET_META_DATA or PackageManager.GET_SHARED_LIBRARY_FILES

    @JvmStatic
    val rendererList: MutableList<Renderer> = mutableListOf()
        get() {
            if (!isInit) {
                init(FCLPath.CONTEXT)
            }
            return field
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
            parse(it.activityInfo.applicationInfo)
        }
    }

    @JvmStatic
    fun isAvailable(): Boolean {
        return rendererList.isNotEmpty()
    }

    @JvmStatic
    fun refresh(context: Context) {
        rendererList.clear()
        isInit = false
        init(context)
    }

    private fun parse(info: ApplicationInfo) {
        if (info.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            val metaData = info.metaData ?: return
            if (metaData.getBoolean("fclPlugin", false)) {
                val rendererString = metaData.getString("renderer") ?: return
                val des = metaData.getString("des") ?: return
                val boatEnvString = metaData.getString("boatEnv") ?: return
                val pojavEnvString = metaData.getString("pojavEnv") ?: return
                val nativeLibraryDir = info.nativeLibraryDir
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
                        info.packageName,
                        minMCVer,
                        maxMCVer
                    )
                )
            }
        }
    }

    private fun addRenderer(renderer: Renderer) {
        rendererList.removeIf { it.id == renderer.id }
        rendererList.add(renderer)
    }

    private fun Bundle.safeGetString(key: String): String? {
        return if (containsKey(key)) {
            return runCatching { getString(key) }.getOrNull()
                ?: runCatching { getFloat(key).toString() }.getOrNull()
        } else null
    }
}