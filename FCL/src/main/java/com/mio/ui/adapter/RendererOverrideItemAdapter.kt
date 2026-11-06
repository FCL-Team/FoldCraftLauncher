package com.mio.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.manager.RendererManager
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemRendererOverrideBinding
import com.tungsten.fcl.setting.Profile

/**
 * 渲染器覆盖对话框的版本行：每行一个 FCLSpinner，
 * 第 0 项为「跟随设置」（清除覆盖），其余项依次对应 [RendererManager.rendererList]。
 */
class RendererOverrideItemAdapter(
    private val context: Context,
    private val profile: Profile,
    versions: List<String>
) : RecyclerView.Adapter<RendererOverrideItemAdapter.Holder>() {

    /** index 0 = 跟随设置；index-1 映射 [rendererIds] */
    private val displayItems: List<String> =
        listOf(context.getString(R.string.renderer_follow_settings)) + RendererManager.rendererList.map { it.des }
    private val rendererIds: List<String> = RendererManager.rendererList.map { it.id }

    private data class Row(val versionId: String, var overrideId: String?)

    private val rows: List<Row> = versions.map { versionId ->
        val stored = profile.getRendererOverride(versionId)
        val overrideId = profile.getEffectiveRendererOverride(versionId)
        // 失效覆盖（渲染器已卸载/禁用）当场清除，配置与界面、启动行为保持一致
        if (stored != overrideId) {
            profile.setRendererOverride(versionId, null)
        }
        Row(versionId, overrideId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRendererOverrideBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = rows.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    inner class Holder(private val binding: ItemRendererOverrideBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val row = rows[position]
            binding.versionName.text = row.versionId
            binding.spinner.setItems(displayItems)
            // setItems/setSelection 均为程序性设置，不触发监听，行复用安全；
            // overrideId 均为有效覆盖，必然映射到 rendererIds
            binding.spinner.setSelection(row.overrideId?.let { rendererIds.indexOf(it) + 1 } ?: 0)
            binding.spinner.setOnItemSelectedListener { index, _ ->
                val newOverride = if (index <= 0) null else rendererIds.getOrNull(index - 1)
                if (newOverride != row.overrideId) {
                    row.overrideId = newOverride
                    profile.setRendererOverride(row.versionId, newOverride)
                }
            }
        }
    }
}
