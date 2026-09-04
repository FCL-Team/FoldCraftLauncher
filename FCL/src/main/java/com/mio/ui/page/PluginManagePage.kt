package com.mio.ui.page

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.manager.PluginManager
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
class PluginManagePage(context: Context?, id: Int) : FCLPage(context, id, R.layout.page_setting_plugin) {

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

    /** 宿主 Activity onResume 时同步：重扫插件列表（覆盖从系统卸载页返回后列表过期） */
    fun onHostResume() {
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
            context.startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:${app.packageName}")))
        } catch (_: Exception) {
        }
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }
}
