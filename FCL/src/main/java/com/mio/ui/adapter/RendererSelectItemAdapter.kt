package com.mio.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.data.Renderer
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemRendererSelectBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * 渲染器选择对话框列表适配器。
 * item 为带边框的卡片：标题为渲染器描述，两行副标题分别为支持的 MC 版本范围（未知时显示"未知"）
 * 与来源（内置渲染器显示"内置"，插件渲染器显示插件应用名），所有行高一致。
 */
class RendererSelectItemAdapter(
    val context: Context,
    private val renderers: List<Renderer>,
    private val currentId: String,
    private val callback: (Renderer) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_renderer_select, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ItemRendererSelectBinding.bind(holder.itemView)
        val renderer = renderers[position]
        binding.title.text = renderer.des
        val ver = when {
            renderer.minMCver.isNotEmpty() && renderer.maxMCver.isNotEmpty() ->
                "${renderer.minMCver}~${renderer.maxMCver}"
            renderer.minMCver.isNotEmpty() -> ">=${renderer.minMCver}"
            renderer.maxMCver.isNotEmpty() -> "<=${renderer.maxMCver}"
            else -> ""
        }
        binding.version.text = context.getString(R.string.supported_mc_version) + " " +
            ver.ifEmpty { context.getString(R.string.message_unknown) }
        binding.source.text = context.getString(R.string.renderer_source) + " " +
            renderer.source.ifEmpty { context.getString(R.string.renderer_source_builtin) }
        if (renderer.isEqual(currentId)) {
            binding.check.visibility = View.VISIBLE
            binding.check.imageTintList = ColorStateList.valueOf(ThemeEngine.getTheme().getColor2())
        } else {
            binding.check.visibility = View.GONE
        }
        binding.root.setOnClickListener { callback(renderer) }
    }

    override fun getItemCount(): Int = renderers.size
}
