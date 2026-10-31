package com.mio.ui.dialog

import android.content.Context
import android.view.WindowManager
import com.mio.plugin.RendererPlugin
import com.tungsten.fcl.databinding.DialogRendererAdvancedBinding
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 渲染器选择对话框的"高级设置"页：
 * SFPEW 兼容层开关仅对外部渲染器插件生效（内置渲染器行为已知，按上游 AAMC 规则硬编码，
 * 默认启用、仅自带 FPE 的内置渲染器关闭），另有层全局项 relaxed context（strict 为默认）。
 */
class RendererAdvancedDialog(context: Context) : FCLDialog(context) {

    init {
        val params = window?.attributes
        params?.width = ConvertUtils.dip2px(context, 500f)
        window?.attributes = params
        val binding = DialogRendererAdvancedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = context.getString(R.string.renderer_sfpew_advanced)

        binding.pluginLayerSwitch.setOnCheckedChangeListener(null)
        binding.pluginLayerSwitch.isChecked = RendererPlugin.isSfpewPluginLayerEnabled()
        binding.pluginLayerSwitch.setOnCheckedChangeListener { _, checked ->
            RendererPlugin.setSfpewPluginLayerEnabled(context, checked)
        }

        binding.relaxedSwitch.setOnCheckedChangeListener(null)
        binding.relaxedSwitch.isChecked = RendererPlugin.isSfpewRelaxedContextEnabled()
        binding.relaxedSwitch.setOnCheckedChangeListener { _, checked ->
            RendererPlugin.setSfpewRelaxedContextEnabled(context, checked)
        }

        binding.done.setOnClickListener {
            dismiss()
        }
    }
}
