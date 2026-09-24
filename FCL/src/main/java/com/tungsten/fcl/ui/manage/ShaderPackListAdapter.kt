package com.tungsten.fcl.ui.manage

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.minecraft.ShaderPack
import com.mio.ui.adapter.ViewHolder
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemShaderPackBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.util.Locale

/**
 * 光影包列表适配器：点击切换选中（多选删除），长按弹出重命名，info 展示详情
 */
class ShaderPackListAdapter(
    private val context: Context,
    private val onSelectionChanged: (Int) -> Unit,
    private val onRename: (ShaderPack) -> Unit,
    private val onInfo: (ShaderPack) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<ShaderPack>()

    /** 以文件路径记录选中，刷新会生成新实例，不能按对象保存 */
    private val selected = LinkedHashSet<String>()

    private fun key(pack: ShaderPack) = pack.file.absolutePath

    fun currentSelected(): List<ShaderPack> = items.filter { key(it) in selected }

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
    fun submitList(list: List<ShaderPack>) {
        items.clear()
        items.addAll(list)
        selected.retainAll(items.map { key(it) }.toSet())
        notifyDataSetChanged()
        onSelectionChanged(selected.size)
    }

    private fun toggleSelected(pack: ShaderPack) {
        if (!selected.remove(key(pack))) selected.add(key(pack))
        val position = items.indexOfFirst { key(it) == key(pack) }
        if (position >= 0) notifyItemChanged(position)
        onSelectionChanged(selected.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemShaderPackBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ItemShaderPackBinding.bind(holder.itemView)
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

        binding.tag.text = if (pack.isValid) pack.fileSize?.let { formatFileSize(it) }
        else context.getString(R.string.shaderpack_invalid)
        binding.tag.visibility =
            if (binding.tag.text.isNullOrBlank()) View.GONE else View.VISIBLE

        binding.name.text = pack.name
        binding.info.setOnClickListener { onInfo(pack) }
    }

    private fun applyBackground(
        binding: ItemShaderPackBinding,
        pack: ShaderPack
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

    private fun formatFileSize(size: Long): String = when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f KB", size / 1024.0)
        size < 1024L * 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f MB", size / 1024.0 / 1024.0)
        else -> String.format(Locale.getDefault(), "%.1f GB", size / 1024.0 / 1024.0 / 1024.0)
    }
}
