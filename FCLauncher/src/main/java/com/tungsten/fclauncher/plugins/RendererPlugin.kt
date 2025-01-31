package com.tungsten.fclauncher.plugins

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.tungsten.fclauncher.utils.FCLPath

object RendererPlugin {
    data class Renderer(
        val name: String,
        val des: String,
        val glName: String,
        val eglName: String,
        val path: String,
        val boatEnv: List<String>,
        val pojavEnv: List<String>
    )

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
    var selected: Renderer? = null

    @JvmStatic
    fun init(context: Context) {
        isInit = true
        val queryIntentActivities =
            context.packageManager.queryIntentActivities(Intent("android.intent.action.MAIN"), PACKAGE_FLAGS)
        queryIntentActivities.forEach {
            parse(it.activityInfo.applicationInfo)
        }
    }

    @JvmStatic
    fun isAvailable(): Boolean {
        return rendererList.isNotEmpty()
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
                rendererList.add(
                    Renderer(
                        renderer[0],
                        des,
                        renderer[1],
                        renderer[2],
                        nativeLibraryDir,
                        boatEnv,
                        pojavEnv
                    )
                )
            }
        }
    }
}