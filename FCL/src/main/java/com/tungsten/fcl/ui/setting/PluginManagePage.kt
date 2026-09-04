package com.tungsten.fcl.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.plugin.MioLibPatcherManager
import com.mio.plugin.PluginManager
import com.mio.plugin.RendererPlugin
import com.mio.ui.adapter.PluginManageAdapter
import com.mio.ui.adapter.PluginManageAdapter.Item
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.ui.dialog.MioLibPatcherDialog
import com.mio.ui.dialog.RendererEnvDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.PageSettingPluginBinding
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLPage

/**
 * 插件管理页：置顶 MioLibPatcher 条目（启用/禁用开关 + 配置对话框），
 * 下方列出所有已识别的插件应用（含已禁用），
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
            onPatcherEnableChange = { enabled ->
                MioLibPatcherManager.setEnabled(enabled)
                reload()
            },
            onPatcherConfigure = ::showPatcherConfig,
            onEnableChange = { app, enabled ->
                PluginManager.setEnabled(context, app.packageName, enabled)
                PluginManager.refreshAll(context)
                reload()
            },
            onConfigure = ::showEnvConfig,
            onUninstall = ::uninstall,
        )
        binding.pluginList.adapter = adapter
        reload()
    }

    /** MioLibPatcher 功能开关配置对话框（ALC10 / Sable Rapier / ASM 后门） */
    private fun showPatcherConfig() {
        MioLibPatcherDialog(
            context,
            MioLibPatcherManager.isAlc10(),
            MioLibPatcherManager.isSablerapier(),
            MioLibPatcherManager.isAsmBackport(),
        ) { alc10, sablerapier, asmBackport ->
            MioLibPatcherManager.setAlc10(alc10)
            MioLibPatcherManager.setSablerapier(sablerapier)
            MioLibPatcherManager.setAsmBackport(asmBackport)
            reload()
        }.show()
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
        val items = buildList {
            add(Item.PatcherItem(MioLibPatcherManager.isEnabled()))
            addAll(PluginManager.allApps(context).map {
                Item.PluginItem(it, PluginManager.isEnabled(context, it.packageName))
            })
        }
        adapter.submitList(items)
        // 置顶条目常驻，插件为空时仅显示空态文案
        binding.emptyView.isVisible = items.none { it is Item.PluginItem }
    }

    /** v2 渲染器插件的环境变量配置对话框（确认后立即保存并刷新渲染器列表） */
    private fun showEnvConfig(app: PluginManager.PluginApp) {
        val specs = RendererPlugin.getConfigurableEnvs(app.packageName)
        if (specs.isEmpty()) return
        RendererEnvDialog(
            context,
            app.label,
            specs,
        ) { values ->
            RendererPlugin.updateEnvConfigs(context, app.packageName, values)
        }.show()
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
