package com.mio.plugin

import com.mio.data.Renderer

object RendererPlugin : AbstractPlugin<Renderer>() {

    /** 插件渲染器列表（懒初始化，内置渲染器见 RendererManager） */
    @JvmStatic
    val rendererList: List<Renderer>
        get() = items

    override fun parse(app: PluginManager.PluginApp) {
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
        items.removeIf { it.id == renderer.id }
        items.add(renderer)
    }
}
