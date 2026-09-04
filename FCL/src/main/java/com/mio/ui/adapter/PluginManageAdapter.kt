package com.mio.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mio.plugin.PluginManager
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemPluginBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * 插件管理列表：图标/名称/包名/版本/类型 + 启用开关 + 卸载按钮。
 * 行背景圆角随主题 tint，插件图标保持原样不着色。
 */
class PluginManageAdapter(
    private val onEnableChange: (PluginManager.PluginApp, Boolean) -> Unit,
    private val onUninstall: (PluginManager.PluginApp) -> Unit,
) : ListAdapter<PluginManageAdapter.Item, PluginManageAdapter.Holder>(DIFF) {

    /** 列表条目：插件应用 + 当前启用状态（启用状态参与 Diff 比较） */
    data class Item(val app: PluginManager.PluginApp, val enabled: Boolean)

    class Holder(val binding: ItemPluginBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPluginBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        val context = binding.root.context

        // 行背景圆角形状，颜色 tint 取主题浅色，主题切换时跟随刷新
        binding.root.setBackgroundResource(R.drawable.bg_item_rounded)
        ThemeEngine.getInstance().unregisterEvent(binding.root)
        ThemeEngine.getInstance().registerEvent(binding.root) {
            binding.root.backgroundTintList =
                ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
        }
        binding.root.backgroundTintList =
            ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)

        item.app.icon?.let { binding.icon.setImageDrawable(it) }
        binding.name.text = item.app.label
        binding.subText.text = buildString {
            append(item.app.packageName)
            item.app.versionName?.takeIf { it.isNotEmpty() }?.let { append(" · v").append(it) }
        }
        binding.typeText.text = typeLabel(context, item.app.types)

        // 复用时先摘掉监听再设状态，避免滚动中误触发开关回调
        binding.switchView.setOnCheckedChangeListener(null)
        binding.switchView.isChecked = item.enabled
        binding.switchView.setOnCheckedChangeListener { _, checked ->
            onEnableChange(item.app, checked)
        }
        binding.uninstall.setOnClickListener { onUninstall(item.app) }
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
                return oldItem.app.packageName == newItem.app.packageName
            }

            // 不用 data class equals：重扫后 icon/appInfo 是新实例（引用不同），
            // 会导致每次重扫都整表误判重绑；这里只比较实际展示的状态
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.enabled == newItem.enabled &&
                        oldItem.app.label == newItem.app.label &&
                        oldItem.app.versionName == newItem.app.versionName &&
                        oldItem.app.types == newItem.app.types &&
                        oldItem.app.lastUpdateTime == newItem.app.lastUpdateTime
            }
        }
    }
}
