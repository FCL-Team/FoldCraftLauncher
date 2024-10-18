package com.mio.util

import android.content.Context
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.plugins.RendererPlugin
import java.util.function.Consumer

class RendererUtil {
    companion object {
        @JvmStatic
        fun openRendererMenu(
            context: Context,
            view: View,
            width: Int,
            height: Int,
            callback: Consumer<String>
        ) {
            val listView = ListView(context)
            var popupWindow: PopupWindow? = null
            listView.adapter =
                ArrayAdapter(context, R.layout.item_renderer, mutableListOf<String>().apply {
                    add(context.getString(R.string.settings_fcl_renderer_gl4es))
                    add(context.getString(R.string.settings_fcl_renderer_virgl))
                    add(context.getString(R.string.settings_fcl_renderer_ltw))
                    add(context.getString(R.string.settings_fcl_renderer_vgpu))
                    add(context.getString(R.string.settings_fcl_renderer_zink))
                    add(context.getString(R.string.settings_fcl_renderer_freedreno))
                    RendererPlugin.rendererList.forEach {
                        add(it.des)
                    }
                })
            listView.setOnItemClickListener { _, _, position, _ ->
                val versionSetting =
                    Profiles.getSelectedProfile().versionSetting
                val rendererList = mutableListOf<FCLConfig.Renderer>().apply {
                    add(FCLConfig.Renderer.RENDERER_GL4ES)
                    add(FCLConfig.Renderer.RENDERER_VIRGL)
                    add(FCLConfig.Renderer.RENDERER_LTW)
                    add(FCLConfig.Renderer.RENDERER_VGPU)
                    add(FCLConfig.Renderer.RENDERER_ZINK)
                    add(FCLConfig.Renderer.RENDERER_FREEDRENO)
                }
                if (position > rendererList.size - 1) {
                    versionSetting.renderer = FCLConfig.Renderer.RENDERER_CUSTOM
                    RendererPlugin.selected =
                        RendererPlugin.rendererList[position - rendererList.size]
                    versionSetting.customRenderer = RendererPlugin.selected!!.des
                } else {
                    versionSetting.renderer = rendererList[position]
                }
                popupWindow?.dismiss()
                callback.accept(listView.adapter.getItem(position).toString())
            }
            popupWindow = PopupWindow(
                listView,
                width,
                height
            ).apply {
                isClippingEnabled = false
                isOutsideTouchable = true
                enterTransition = Slide(Gravity.TOP)
                exitTransition = Slide(Gravity.TOP)
                showAsDropDown(view)
            }
        }
    }
}