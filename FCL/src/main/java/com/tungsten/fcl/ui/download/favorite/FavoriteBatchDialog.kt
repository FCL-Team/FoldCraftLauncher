package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogFavoriteBatchBinding
import com.tungsten.fcl.databinding.DialogFavoriteBatchItemBinding
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLCheckBox
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 一键下载对话框：阶段一展示解析进度；阶段二列出与当前版本/加载器匹配的收藏模组
 * （默认全选，展示将下载的文件名），用户勾选确认后回调，由调用方提交批量下载。
 */
class FavoriteBatchDialog(context: Context) : FCLDialog(context) {

    /** 一条可下载的收藏模组：展示名 + 匹配到的版本 */
    data class Entry(val title: String, val version: RemoteMod.Version)

    private lateinit var binding: DialogFavoriteBatchBinding
    private val entries = mutableListOf<Entry>()
    private var onDownload: ((List<RemoteMod.Version>) -> Unit)? = null

    init {
        setCancelable(true)
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.WRAP_CONTENT)
        binding = DialogFavoriteBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.negative.setOnClickListener { dismiss() }
        binding.positive.setOnClickListener {
            val selected = entries.filterIndexed { index, _ ->
                binding.listContainer.getChildAt(index).findViewById<FCLCheckBox>(R.id.check).isChecked
            }.map { it.version }
            if (selected.isNotEmpty()) {
                val callback = onDownload
                dismiss()
                callback?.invoke(selected)
            }
        }
        showParsing()
    }

    /** 阶段一：解析进度 */
    fun showParsing() {
        binding.parsing.visibility = View.VISIBLE
        binding.scroll.visibility = View.GONE
        binding.skippedNote.visibility = View.GONE
        binding.positive.visibility = View.GONE
    }

    /** 阶段二：展示可下载模组（默认全选），无匹配被跳过的数量显示在底部 */
    fun showResult(items: List<Entry>, skippedTitles: List<String>, callback: (List<RemoteMod.Version>) -> Unit) {
        onDownload = callback
        entries.clear()
        entries.addAll(items)
        val inflater = LayoutInflater.from(context)
        binding.listContainer.removeAllViews()
        entries.forEach { entry ->
            val item = DialogFavoriteBatchItemBinding.inflate(inflater, binding.listContainer, false)
            item.title.text = entry.title
            item.versionName.text = entry.version.file().filename()
            item.check.isChecked = true
            item.check.setOnCheckedChangeListener { _, _ -> updatePositiveState() }
            binding.listContainer.addView(item.root)
        }
        binding.parsing.visibility = View.GONE
        binding.scroll.visibility = View.VISIBLE
        binding.positive.visibility = View.VISIBLE
        updatePositiveState()
        if (skippedTitles.isNotEmpty()) {
            binding.skippedNote.text = context.getString(R.string.favorite_batch_skipped_note, skippedTitles.size)
            binding.skippedNote.visibility = View.VISIBLE
        }
    }

    private fun updatePositiveState() {
        var any = false
        for (index in 0 until binding.listContainer.childCount) {
            if (binding.listContainer.getChildAt(index).findViewById<FCLCheckBox>(R.id.check).isChecked) {
                any = true
                break
            }
        }
        binding.positive.isEnabled = any
        binding.positive.alpha = if (any) 1f else 0.45f
    }
}
