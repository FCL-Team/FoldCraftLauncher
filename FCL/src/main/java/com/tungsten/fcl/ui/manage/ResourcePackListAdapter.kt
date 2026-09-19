package com.tungsten.fcl.ui.manage

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.ViewHolder
import com.mio.minecraft.ResourcePack
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemResourcePackBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * 资源包列表适配器：点击切换选中（多选删除），checkbox 切换启用/禁用（写入 options.txt），
 * 长按弹出重命名，info 展示详情
 */
class ResourcePackListAdapter(
    private val context: Context,
    private val onSelectionChanged: (Int) -> Unit,
    private val onEnableChanged: (ResourcePack, Boolean) -> Unit,
    private val onRename: (ResourcePack) -> Unit,
    private val onInfo: (ResourcePack) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<ResourcePack>()

    /** 以文件路径记录选中，启用状态刷新会生成新实例，不能按对象保存 */
    private val selected = LinkedHashSet<String>()

    /** 占位图标，theme tint 每次绑定刷新 */
    private val fallbackIcon = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_texture_24)!!

    private fun key(pack: ResourcePack) = pack.file.absolutePath

    fun currentSelected(): List<ResourcePack> = items.filter { key(it) in selected }

    fun selectAll() {
        selected.clear()
        items.forEach { selected.add(key(it)) }
        notifyDataSetChanged()
        onSelectionChanged(selected.size)
    }

    fun selectInvert() {
        val inverted = items.filter { key(it) !in selected }.map { key(it) }
        selected.clear()
        selected.addAll(inverted)
        notifyDataSetChanged()
        onSelectionChanged(selected.size)
    }

    fun clearSelection() {
        selected.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ResourcePack>) {
        items.clear()
        items.addAll(list)
        selected.retainAll(items.map { key(it) }.toSet())
        notifyDataSetChanged()
        onSelectionChanged(selected.size)
    }

    /** 单项状态更新（如启用/禁用写盘后），保持选中与滚动位置不变 */
    fun updateItem(pack: ResourcePack) {
        val index = items.indexOfFirst { key(it) == key(pack) }
        if (index >= 0) {
            items[index] = pack
            notifyItemChanged(index)
        }
    }

    private fun toggleSelected(pack: ResourcePack) {
        if (!selected.remove(key(pack))) selected.add(key(pack))
        val position = items.indexOfFirst { key(it) == key(pack) }
        if (position >= 0) notifyItemChanged(position)
        onSelectionChanged(selected.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemResourcePackBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ItemResourcePackBinding.bind(holder.itemView)
        val pack = items[position]

        applyBackground(binding, pack)
        ThemeEngine.getInstance().unregisterEvent(binding.parent)
        ThemeEngine.getInstance().registerEvent(binding.parent) {
            applyBackground(binding, pack)
        }
        binding.parent.setOnClickListener {
            toggleSelected(pack)
        }
        binding.parent.setOnLongClickListener {
            onRename(pack)
            true
        }

        binding.check.setOnCheckedChangeListener(null)
        binding.check.isChecked = pack.enabled
        binding.check.setOnCheckedChangeListener { _, checked ->
            onEnableChanged(pack, checked)
        }

        if (pack.icon != null) {
            binding.icon.setImageBitmap(pack.icon)
        } else {
            fallbackIcon.setTint(ThemeEngine.getInstance().getTheme().getColor())
            binding.icon.setImageDrawable(fallbackIcon)
        }

        binding.name.text = pack.name
        binding.description.text = pack.description
        binding.description.visibility =
            if (pack.description.isNullOrBlank()) View.GONE else View.VISIBLE
        if (pack.isValid) {
            binding.tag.text = pack.packFormat
            binding.tag.visibility =
                if (pack.packFormat.isNullOrBlank()) View.GONE else View.VISIBLE
        } else {
            binding.tag.text = context.getString(R.string.resourcepack_invalid)
            binding.tag.visibility = View.VISIBLE
        }

        binding.info.setOnClickListener { onInfo(pack) }
    }

    private fun applyBackground(
        binding: ItemResourcePackBinding,
        pack: ResourcePack
    ) {
        binding.parent.backgroundTintList = ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(
                if (key(pack) in selected) ThemeEngine.getInstance().getTheme().getColor()
                else ThemeEngine.getInstance().getTheme().ltColor
            )
        )
    }

    override fun getItemCount(): Int = items.size
}
