package com.tungsten.fclauncher.plugins

import android.content.Context
import com.mio.data.Renderer
import com.mio.manager.PluginManager
import com.tungsten.fcl.FCLApp

object RendererPlugin {
    private var isInit = false;

    @JvmStatic
    val rendererList: MutableList<Renderer> = mutableListOf()
        get() {
            if (!isInit) {
                init(FCLApp.getAppContext())
            }
            return field
        }

    @JvmStatic
    fun init(context: Context) {
        isInit = true
        PluginManager.enabledApps(context).forEach {
            parse(it)
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

    private fun parse(app: PluginManager.PluginApp) {
        val metaData = app.appInfo.metaData ?: return
        if (metaData.getBoolean(PluginManager.META_PLUGIN, false)) {
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
                    maxMCVer
                )
            )
        }
    }

    private fun addRenderer(renderer: Renderer) {
        rendererList.removeIf { it.id == renderer.id }
        rendererList.add(renderer)
    }
}
