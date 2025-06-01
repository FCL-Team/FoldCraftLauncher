package com.mio.ui.dialog

import android.content.Context
import android.graphics.Point
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogSelectRendererBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

class DriverSelectDialog(
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
        binding.title.text = context.getString(R.string.settings_fcl_driver)
        binding.listView.adapter =
            ArrayAdapter(context, R.layout.item_renderer, mutableListOf<String>().apply {
                DriverPlugin.driverList.forEach {
                    add(it.driver)
                }
            })
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val versionSetting =
                if (isGlobal) Profiles.getSelectedProfile().global else Profiles.getSelectedProfile().versionSetting
            versionSetting.driver = DriverPlugin.driverList[position].driver
            DriverPlugin.selected = DriverPlugin.driverList[position]
            dismiss()
            callback.accept(binding.listView.adapter.getItem(position).toString())
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}