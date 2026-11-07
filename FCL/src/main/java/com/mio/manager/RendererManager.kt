package com.mio.manager

import android.content.Context
import com.mio.data.Renderer
import com.mio.plugin.DriverPlugin
import com.mio.plugin.RendererPlugin
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R

object RendererManager {
    lateinit var RENDERER_GL4ES: Renderer
    lateinit var RENDERER_VGPU: Renderer
    lateinit var RENDERER_ZINK: Renderer
    lateinit var RENDERER_NGGL4ES: Renderer
    lateinit var RENDERER_MOBILEGLUES: Renderer
    lateinit var RENDERER_QUEST_MOBILEGLUES: Renderer
    private var isInit = false

    @JvmStatic
    val rendererList: MutableList<Renderer> = mutableListOf()
        get() {
            if (!isInit) {
                init(FCLApp.getAppContext())
            }
            return field
        }

    fun init(context: Context) {
        if (isInit) return
        isInit = true
        rendererList.clear()
        RENDERER_GL4ES = Renderer(
            "Holy-GL4ES",
            context.getString(R.string.settings_fcl_renderer_gl4es),
            "libgl4es_114.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_GL4ES,
            "",
            "1.21.4"
        )

        RENDERER_VGPU = Renderer(
            "VGPU",
            context.getString(R.string.settings_fcl_renderer_vgpu),
            "libvgpu.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_VGPU,
            "",
            "1.16.5"
        )

        RENDERER_ZINK = Renderer(
            "Zink",
            context.getString(R.string.settings_fcl_renderer_zink),
            "libglxshim.so",
            "libEGL_mesa.so",
            "",
            null,
            null,
            Renderer.ID_ZINK,
            "",
            "26.3-snapshot-3",
            displayMaxMCver = "26.2"
        )

        RENDERER_NGGL4ES = Renderer(
            "Krypton Wrapper",
            context.getString(R.string.settings_fcl_renderer_nggl4es),
            "libng_gl4es.so",
            "libEGL.so",
            "",
            null,
            null,
            Renderer.ID_NGGL4ES,
            "",
            "26.3-snapshot-3",
            displayMaxMCver = "26.2"
        )

        // SFPEW 兼容层对内置 MG 硬编码启用，组合后无 MC 版本下界（旧版固定管线由层补齐），
        // 故注册信息不再声明 minMCver（版本门控与列表展示均按无下界处理）
        RENDERER_MOBILEGLUES = Renderer(
            "MobileGlues",
            context.getString(R.string.settings_fcl_renderer_mobileglues),
            "libmobileglues.so",
            "libmobileglues.so",
            "",
            null,
            null,
            Renderer.ID_MOBILEGLUES,
            "",
            ""
        )

        // Quest VR 专用：GL 出口经 libquest_mobileglues.so 适配层（保护 OpenXR 交换链纹理），
        // EGL 仍由内置 MobileGlues 承担；仅 VR 版本设置可选
        RENDERER_QUEST_MOBILEGLUES = Renderer(
            "MobileGlues VR",
            context.getString(R.string.settings_vr_renderer_mobileglues),
            "libquest_mobileglues.so",
            "libmobileglues.so",
            "",
            null,
            null,
            Renderer.ID_QUEST_MOBILEGLUES,
            "",
            "",
            vrCompatible = true,
            vrOnly = true
        )

        RendererPlugin.init(context)
        addRenderer()
        DriverPlugin.init(context)
    }

    private fun addRenderer() {
        rendererList.add(RENDERER_NGGL4ES)
        rendererList.add(RENDERER_GL4ES)
        rendererList.add(RENDERER_VGPU)
        rendererList.add(RENDERER_ZINK)
        rendererList.add(RENDERER_MOBILEGLUES)
        rendererList.add(RENDERER_QUEST_MOBILEGLUES)
        rendererList.addAll(RendererPlugin.rendererList)
    }

    fun refresh(context: Context) {
        RendererPlugin.refresh(context)
        rendererList.clear()
        addRenderer()
    }

    /** 原位替换同 id 的插件渲染器实例（v2 环境变量配置变化后调用），已初始化时才生效 */
    fun replaceRenderer(renderer: Renderer) {
        if (!isInit) return
        rendererList.removeIf { it.id == renderer.id }
        rendererList.add(renderer)
    }

    @JvmStatic
    fun getRenderer(id: String): Renderer {
        return rendererList.find { it.id == id } ?: RENDERER_NGGL4ES
    }

    /** 按模式过滤可选渲染器：VR 模式仅 vrCompatible，普通模式隐藏 vrOnly 条目 */
    @JvmStatic
    fun getRenderersForMode(vrEnabled: Boolean): List<Renderer> = rendererList.filter {
        if (vrEnabled) it.vrCompatible else !it.vrOnly
    }

    /** 同 getRenderer，但回落值按模式兜底（非法/跨模式 id 时切到该模式的默认渲染器） */
    @JvmStatic
    fun getRendererForMode(id: String, vrEnabled: Boolean): Renderer {
        val allowed = getRenderersForMode(vrEnabled)
        return allowed.find { it.id == id }
            ?: if (vrEnabled) RENDERER_QUEST_MOBILEGLUES else RENDERER_NGGL4ES
    }

}