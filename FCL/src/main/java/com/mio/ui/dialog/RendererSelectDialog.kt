package com.mio.ui.dialog

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogSelectRendererBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

class RendererSelectDialog(
    context: Context,
    val isGlobal: Boolean,
    val callback: Consumer<String>
) : FCLDialog(context) {

    init {
        val point = Point()
        window?.windowManager?.defaultDisplay?.getSize(point)
        val params = window?.attributes
        params?.width = ConvertUtils.dip2px(context, 500f)
        if (point.y * 2 < point.x) {
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            params?.height = point.y * 1 / 2
        }
        window?.attributes = params
        val binding = DialogSelectRendererBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = context.getString(R.string.settings_fcl_renderer)
        binding.listView.adapter =
            ArrayAdapter(context, R.layout.item_renderer, mutableListOf<String>().apply {
                add(context.getString(R.string.settings_fcl_renderer_gl4es))
                add(context.getString(R.string.settings_fcl_renderer_virgl))
                add(context.getString(R.string.settings_fcl_renderer_vgpu))
                add(context.getString(R.string.settings_fcl_renderer_zink))
                add(context.getString(R.string.settings_fcl_renderer_freedreno))
                add(context.getString(R.string.settings_fcl_renderer_gl4esp))
                RendererPlugin.rendererList.forEach {
                    add(it.des)
                }
            })
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val versionSetting =
                if (isGlobal) Profiles.getSelectedProfile().global else Profiles.getSelectedProfile().versionSetting
            val rendererList = mutableListOf<FCLConfig.Renderer>().apply {
                add(FCLConfig.Renderer.RENDERER_GL4ES)
                add(FCLConfig.Renderer.RENDERER_VIRGL)
                add(FCLConfig.Renderer.RENDERER_VGPU)
                add(FCLConfig.Renderer.RENDERER_ZINK)
                add(FCLConfig.Renderer.RENDERER_FREEDRENO)
                add(FCLConfig.Renderer.RENDERER_GL4ESPLUS)
            }
            if (position > rendererList.size - 1) {
                versionSetting.renderer = FCLConfig.Renderer.RENDERER_CUSTOM
                RendererPlugin.selected =
                    RendererPlugin.rendererList[position - rendererList.size]
                versionSetting.customRenderer = RendererPlugin.selected!!.des
            } else {
                versionSetting.renderer = rendererList[position]
            }
            dismiss()
            callback.accept(binding.listView.adapter.getItem(position).toString())
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}