package com.mio.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mio.plugin.PluginManager
import com.mio.plugin.RendererPlugin
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemPluginBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * 插件管理列表：置顶 MioLibPatcher 条目（主开关 + 配置按钮，功能开关在
 * 配置对话框中设置），下方为插件应用条目（图标/名称/包名/版本/类型 + 启用/配置/卸载按钮）。
 * 各条目均复用 item_plugin 布局，按类型控制图标/类型标签/操作按钮的显隐。
 */
class PluginManageAdapter(
    private val onPatcherEnableChange: (Boolean) -> Unit,
    private val onPatcherConfigure: () -> Unit,
    private val onEnableChange: (PluginManager.PluginApp, Boolean) -> Unit,
    private val onConfigure: (PluginManager.PluginApp) -> Unit,
    private val onUninstall: (PluginManager.PluginApp) -> Unit,
) : ListAdapter<PluginManageAdapter.Item, PluginManageAdapter.Holder>(DIFF) {

    sealed class Item {
        /** MioLibPatcher 条目 */
        data class PatcherItem(val enabled: Boolean) : Item()

        /** 插件应用条目 */
        data class PluginItem(val app: PluginManager.PluginApp, val enabled: Boolean) : Item()
    }

    class Holder(val binding: ItemPluginBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPluginBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val binding = holder.binding
        // 行背景圆角形状，颜色 tint 取主题浅色，主题切换时跟随刷新
        binding.root.setBackgroundResource(R.drawable.bg_item_rounded)
        ThemeEngine.getInstance().unregisterEvent(binding.root)
        ThemeEngine.getInstance().registerEvent(binding.root) {
            binding.root.backgroundTintList =
                ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
        }
        binding.root.backgroundTintList =
            ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)

        when (val item = getItem(position)) {
            is Item.PatcherItem -> bindPatcher(binding, item)
            is Item.PluginItem -> bindPlugin(binding, item)
        }
    }

    private fun bindPatcher(binding: ItemPluginBinding, item: Item.PatcherItem) {
        // 内置组件图标以主题色着色（插件图标保持原样，见 bindPlugin）
        binding.icon.setImageResource(R.drawable.ic_outline_extension_24)
        ThemeEngine.getInstance().unregisterEvent(binding.icon)
        ThemeEngine.getInstance().registerEvent(binding.icon) {
            binding.icon.imageTintList =
                ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().autoTint)
        }
        binding.icon.imageTintList =
            ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().autoTint)
        binding.icon.isVisible = true
        binding.name.setText(R.string.plugin_miolibpatcher_name)
        binding.subText.setText(R.string.plugin_miolibpatcher_desc)
        binding.typeText.isVisible = false
        binding.uninstall.isVisible = false
        // 禁用状态下不可配置
        binding.config.isVisible = item.enabled
        binding.config.setOnClickListener { if (item.enabled) onPatcherConfigure() }
        bindSwitch(binding, item.enabled) { checked ->
            onPatcherEnableChange(checked)
        }
    }

    private fun bindPlugin(binding: ItemPluginBinding, item: Item.PluginItem) {
        val context = binding.root.context
        // 清掉重用自 MioLibPatcher 行的主题色 tint，插件图标保持原样
        binding.icon.imageTintList = null
        item.app.icon?.let { binding.icon.setImageDrawable(it) }
        binding.icon.isVisible = true
        binding.name.text = item.app.label
        binding.subText.text = buildString {
            append(item.app.packageName)
            item.app.versionName?.takeIf { it.isNotEmpty() }?.let { append(" · v").append(it) }
        }
        binding.typeText.isVisible = true
        binding.typeText.text = typeLabel(context, item.app.types)
        binding.uninstall.isVisible = true
        binding.uninstall.setOnClickListener { onUninstall(item.app) }
        bindSwitch(binding, item.enabled) { checked ->
            onEnableChange(item.app, checked)
        }

        // v2 渲染器插件的可配置环境变量入口（禁用状态下不可配置）
        val configurable = item.enabled && RendererPlugin.hasConfigurableEnvs(item.app.packageName)
        binding.config.isVisible = configurable
        binding.config.setOnClickListener { if (configurable) onConfigure(item.app) }
    }

    /** 复用时先摘掉监听再设状态，避免滚动中误触发开关回调 */
    private fun bindSwitch(binding: ItemPluginBinding, checked: Boolean, onChange: (Boolean) -> Unit) {
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = checked
        binding.switchView.setOnCheckedChangeListener { _, isChecked ->
            onChange(isChecked)
        }
    }

    private fun typeLabel(context: Context, types: Set<PluginManager.PluginType>): String {
        return types.joinToString(" / ") {
            when (it) {
                PluginManager.PluginType.RENDERER -> context.getString(R.string.plugin_type_renderer)
                PluginManager.PluginType.DRIVER -> context.getString(R.string.plugin_type_driver)
                PluginManager.PluginType.NATIVE_LIB -> context.getString(R.string.plugin_type_native)
                PluginManager.PluginType.FFMPEG -> "FFmpeg"
            }
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return when {
                    oldItem is Item.PluginItem && newItem is Item.PluginItem ->
                        oldItem.app.packageName == newItem.app.packageName
                    oldItem is Item.PatcherItem && newItem is Item.PatcherItem -> true
                    else -> false
                }
            }

            // 插件条目不用 data class equals：重扫后 icon/appInfo 是新实例（引用不同），
            // 会导致每次重扫都整表误判重绑；这里只比较实际展示的状态
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return when {
                    oldItem is Item.PluginItem && newItem is Item.PluginItem ->
                        oldItem.enabled == newItem.enabled &&
                                oldItem.app.label == newItem.app.label &&
                                oldItem.app.versionName == newItem.app.versionName &&
                                oldItem.app.types == newItem.app.types &&
                                oldItem.app.lastUpdateTime == newItem.app.lastUpdateTime
                    else -> oldItem == newItem
                }
            }
        }
    }
}