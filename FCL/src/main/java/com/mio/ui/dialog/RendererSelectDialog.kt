package com.mio.ui.dialog

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.mio.manager.RendererManager
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogSelectRendererBinding
import com.tungsten.fcl.setting.Profiles
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
        val ratio = point.x.toFloat() / point.y.toFloat()
        if (ratio >= 1.5f) {
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            params?.height = point.y * 1 / 2
        }
        window?.attributes = params
        val binding = DialogSelectRendererBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = context.getString(R.string.settings_fcl_renderer)
        binding.listView.adapter = getAdapter()
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val versionSetting =
                if (isGlobal) Profiles.getSelectedProfile().global else Profiles.getSelectedProfile().versionSetting
            versionSetting.renderer = RendererManager.rendererList[position].id
            dismiss()
            callback.accept(binding.listView.adapter.getItem(position).toString())
        }
        binding.refresh.setOnClickListener {
            RendererManager.refresh(context)
            binding.listView.adapter = getAdapter()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
    private fun getAdapter(): ArrayAdapter<String> {
        return ArrayAdapter(context, R.layout.item_renderer, mutableListOf<String>().apply {
            RendererManager.rendererList.forEach {
                val ver = when {
                    it.minMCver.isNotEmpty() && it.maxMCver.isNotEmpty() -> "${it.minMCver}~${it.maxMCver}"
                    it.minMCver.isNotEmpty() -> ">=${it.minMCver}"
                    it.maxMCver.isNotEmpty() -> "<=${it.maxMCver}"
                    else -> ""
                }
                add(if (ver.isNotEmpty()) "${it.des} (${context.getString(R.string.supported_mc_version)} $ver)" else it.des)
            }
        })
    }
}