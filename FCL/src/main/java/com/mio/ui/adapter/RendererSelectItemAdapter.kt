package com.mio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.data.Renderer
import com.mio.ui.applySelectableItemStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemRendererSelectBinding

/**
 * 渲染器选择对话框列表适配器。
 * item 为无描边卡片（背景与动画选择弹窗一致）：标题为渲染器描述，两行副标题分别为支持的 MC 版本范围
 * （未知时显示"未知"）与来源（内置渲染器显示"内置"，插件渲染器显示插件应用名），所有行高一致。
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
        // 选中态统一样式：当前渲染器主题色底 + 勾选，其余普通卡片
        applySelectableItemStyle(
            context,
            binding.root,
            binding.check,
            renderers[position].isEqual(currentId),
            context.resources.displayMetrics.density
        )
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
        binding.root.setOnClickListener { callback(renderer) }
    }

    override fun getItemCount(): Int = renderers.size
}
