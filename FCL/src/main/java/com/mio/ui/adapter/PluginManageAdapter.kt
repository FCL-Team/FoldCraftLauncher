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
 * 插件管理列表：置顶 MioLibPatcher 组（主开关 + 3 个功能开关，组内连排圆角），
 * 下方为插件应用条目（图标/名称/包名/版本/类型 + 启用/配置/卸载按钮）。
 * 各条目均复用 item_plugin 布局，按类型控制图标/类型标签/操作按钮的显隐。
 */
class PluginManageAdapter(
    private val onEnableChange: (PluginManager.PluginApp, Boolean) -> Unit,
    private val onConfigure: (PluginManager.PluginApp) -> Unit,
    private val onUninstall: (PluginManager.PluginApp) -> Unit,
    private val onPatcherEnableChange: (Boolean) -> Unit,
    private val onPatcherFeatureChange: (PatcherFeature, Boolean) -> Unit,
) : ListAdapter<PluginManageAdapter.Item, PluginManageAdapter.Holder>(DIFF) {

    /** MioLibPatcher 可配置功能开关（对应系统属性，见 MioLibPatcher README） */
    enum class PatcherFeature(val titleRes: Int, val descRes: Int) {
        ALC10(R.string.plugin_miolibpatcher_alc10, R.string.plugin_miolibpatcher_alc10_desc),
        SABLE_RAPIER(R.string.plugin_miolibpatcher_sable, R.string.plugin_miolibpatcher_sable_desc),
        ASM_BACKPORT(R.string.plugin_miolibpatcher_asm, R.string.plugin_miolibpatcher_asm_desc),
    }

    sealed class Item {
        /** MioLibPatcher 主条目 */
        data class PatcherItem(val enabled: Boolean) : Item()

        /** MioLibPatcher 功能开关条目（主开关关闭时置灰） */
        data class PatcherFeatureItem(
            val feature: PatcherFeature,
            val enabled: Boolean,
            val patcherEnabled: Boolean,
        ) : Item()

        /** 插件应用条目 */
        data class PluginItem(val app: PluginManager.PluginApp, val enabled: Boolean) : Item()
    }

    /** 置顶 MioLibPatcher 组的行数（主行 + 3 个功能开关） */
    private val headerCount = PatcherFeature.entries.size + 1

    /** 供间距装饰器判断：组内相邻行间留 1dp 缝（绘制分割线） */
    fun isNextInSameGroup(position: Int): Boolean =
        position < headerCount - 1 && position + 1 < itemCount

    class Holder(val binding: ItemPluginBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPluginBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val binding = holder.binding
        // 置顶组连排圆角，其余条目独立圆角；颜色 tint 取主题浅色，主题切换时跟随刷新
        binding.root.setBackgroundResource(
            when {
                position == 0 -> R.drawable.bg_item_rounded_top
                position == headerCount - 1 -> R.drawable.bg_item_rounded_bottom
                position < headerCount -> R.drawable.bg_item_rounded_middle
                else -> R.drawable.bg_item_rounded
            }
        )
        ThemeEngine.getInstance().unregisterEvent(binding.root)
        ThemeEngine.getInstance().registerEvent(binding.root) {
            binding.root.backgroundTintList =
                ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
        }
        binding.root.backgroundTintList =
            ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)

        when (val item = getItem(position)) {
            is Item.PatcherItem -> bindPatcher(binding, item)
            is Item.PatcherFeatureItem -> bindPatcherFeature(binding, item)
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
        binding.config.isVisible = false
        binding.uninstall.isVisible = false
        bindSwitch(binding, item.enabled) { checked ->
            onPatcherEnableChange(checked)
        }
    }

    private fun bindPatcherFeature(binding: ItemPluginBinding, item: Item.PatcherFeatureItem) {
        binding.icon.isVisible = false
        binding.name.setText(item.feature.titleRes)
        binding.subText.setText(item.feature.descRes)
        binding.typeText.isVisible = false
        binding.config.isVisible = false
        binding.uninstall.isVisible = false
        // 主开关关闭时功能开关整体置灰
        binding.switchView.isEnabled = item.patcherEnabled
        bindSwitch(binding, item.enabled) { checked ->
            onPatcherFeatureChange(item.feature, checked)
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
                    oldItem is Item.PatcherFeatureItem && newItem is Item.PatcherFeatureItem ->
                        oldItem.feature == newItem.feature
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