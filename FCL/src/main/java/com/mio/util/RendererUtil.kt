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
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

class RendererUtil {
    companion object {
        @JvmStatic
        fun openRendererMenu(
            context: Context,
            view: View,
            x: Int,
            y: Int,
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
                    add(context.getString(R.string.settings_fcl_renderer_gl4esp))
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
                enterTransition = Slide(Gravity.BOTTOM)
                exitTransition = Slide(Gravity.BOTTOM)
                showAtLocation(view, Gravity.START or Gravity.TOP, x, y)
            }
        }

        @JvmStatic
        fun openDriverMenu(
            context: Context,
            view: View,
            callback: Consumer<String>
        ) {
            val listView = ListView(context)
            var popupWindow: PopupWindow? = null
            listView.adapter =
                ArrayAdapter(context, R.layout.item_renderer, mutableListOf<String>().apply {
                    DriverPlugin.driverList.forEach {
                        add(it.driver)
                    }
                })
            listView.setOnItemClickListener { _, _, position, _ ->
                val versionSetting =
                    Profiles.getSelectedProfile().versionSetting
                versionSetting.driver = DriverPlugin.driverList[position].driver
                DriverPlugin.selected = DriverPlugin.driverList[position]
                popupWindow?.dismiss()
                callback.accept(listView.adapter.getItem(position).toString())
            }
            popupWindow = PopupWindow(
                listView,
                ConvertUtils.dip2px(context, 200F),
                ConvertUtils.dip2px(context, 300F)
            ).apply {
                isClippingEnabled = false
                isOutsideTouchable = true
                enterTransition = Slide(Gravity.TOP)
                exitTransition = Slide(Gravity.TOP)
                val pos = intArrayOf(-1, -1)
                view.getLocationInWindow(pos)
                showAtLocation(view, Gravity.START, pos[0], pos[1] - 50)
            }
        }
    }
}