package com.tungsten.fcl.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.plugin.PluginManager
import com.mio.ui.adapter.PluginManageAdapter
import com.mio.ui.adapter.SpacingItemDecoration
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.PageSettingPluginBinding
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLPage

/**
 * 插件管理页：列出所有已识别的插件应用（含已禁用），
 * 支持启用/禁用（立即刷新各插件列表）与卸载（跳转系统卸载）。
 */
class PluginManagePage(context: Context?, id: Int) :
    FCLPage(context, id, R.layout.page_setting_plugin) {

    private lateinit var binding: PageSettingPluginBinding
    private lateinit var adapter: PluginManageAdapter

    init {
        create()
    }

    private fun create() {
        binding = PageSettingPluginBinding.bind(contentView)
        binding.pluginList.layoutManager = LinearLayoutManager(context)
        val rowSpacing = (8 * context.resources.displayMetrics.density).toInt()
        binding.pluginList.addItemDecoration(
            SpacingItemDecoration(rowSpacing, null) {
                ThemeEngine.getInstance().getTheme().getColor()
            }
        )
        ThemeEngine.getInstance().registerEvent(binding.pluginList) {
            binding.pluginList.invalidate()
        }
        adapter = PluginManageAdapter(
            onEnableChange = { app, enabled ->
                PluginManager.setEnabled(context, app.packageName, enabled)
                PluginManager.refreshAll(context)
                reload()
            },
            onUninstall = ::uninstall,
        )
        binding.pluginList.adapter = adapter
        reload()
    }

    /** 宿主 Activity onResume 时同步：卸载/安装在系统侧发生，进程内扫描缓存不会自动感知，
     *  主动失效后重扫；插件集合或版本变化时联动刷新各插件运行时列表（选择器不再出现已卸载的插件） */
    fun onHostResume() {
        val before = PluginManager.allApps(context)
        PluginManager.invalidate()
        val after = PluginManager.allApps(context)
        if (before.map { it.packageName to it.lastUpdateTime } != after.map { it.packageName to it.lastUpdateTime }) {
            PluginManager.refreshAll(context)
        }
        reload()
    }

    private fun reload() {
        val items = PluginManager.allApps(context).map {
            PluginManageAdapter.Item(it, PluginManager.isEnabled(context, it.packageName))
        }
        adapter.submitList(items)
        binding.pluginList.isVisible = items.isNotEmpty()
        binding.emptyView.isVisible = items.isEmpty()
    }

    private fun uninstall(app: PluginManager.PluginApp) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_DELETE,
                    Uri.parse("package:${app.packageName}")
                )
            )
        } catch (_: Exception) {
        }
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }
}
