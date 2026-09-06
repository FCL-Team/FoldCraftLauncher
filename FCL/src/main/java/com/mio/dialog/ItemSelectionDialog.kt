package com.mio.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.ui.dialogCardBackground
import com.mio.ui.selectedCardBackground
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogItemSelectionBinding
import com.tungsten.fcl.databinding.ItemTextBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 单项选择对话框。条目为无描边卡片（与动画选择弹窗一致），[selectedIndex] 指定当前项用主题色高亮；
 * 条目少时窗口自动放大到屏幕三分之一以上，条目多时封顶并滚动。
 */
class ItemSelectionDialog(
    context: Context,
    title: String,
    items: List<String>,
    small: Boolean,
    selectedIndex: Int = -1,
    callback: (Int, String) -> Unit
) : FCLDialog(context) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class ItemSelectionAdapter(
        val context: Context,
        val items: List<String>,
        val selectedIndex: Int,
        val callback: (Int, String) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {
        private val density = context.resources.displayMetrics.density
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_text, parent, false)
            )
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            val binding = ItemTextBinding.bind(holder.itemView)
            binding.text.text = items[position]
            binding.root.background = if (position == selectedIndex) {
                selectedCardBackground(ThemeEngine.getTheme().getColor(), density)
            } else {
                dialogCardBackground(context, density)
            }
            binding.root.setOnClickListener { callback(position, items[position]) }
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }

    init {
        val binding = DialogItemSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = title
        // 宽度：以最长条目与标题为基准自适应，下限 320dp，上限与屏幕左右留 24dp 边距
        val metrics = context.resources.displayMetrics
        val longest = items.maxByOrNull { it.length } ?: ""
        val sample = LayoutInflater.from(context).inflate(R.layout.item_text, null, false) as ViewGroup
        sample.findViewById<FCLTextView>(R.id.text).text = longest
        sample.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val titleView = binding.title
        titleView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val maxWidth = minOf(
            ConvertUtils.dip2px(context, 560f),
            metrics.widthPixels - ConvertUtils.dip2px(context, 48f)
        )
        val width = maxOf(
            ConvertUtils.dip2px(context, 320f),
            minOf(maxWidth, maxOf(sample.measuredWidth, titleView.measuredWidth) + ConvertUtils.dip2px(context, 48f))
        )
        // 高度：条目行高实测累加；窗口高度 = 内容 + 标题/按钮区（约 90dp），超出 small 上限（50% 屏）
        // 或大对话框上限（90% 屏）时列表滚动
        val spacing = ConvertUtils.dip2px(context, 12f)
        val rowHeight = sample.measuredHeight.coerceAtLeast(ConvertUtils.dip2px(context, 48f))
        val contentHeight = items.size * rowHeight + (items.size - 1) * spacing
        val headTailHeight = ConvertUtils.dip2px(context, 90f)
        val maxWindowHeight = ((if (small) 0.5f else 0.9f) * metrics.heightPixels).toInt()
        val windowHeight = minOf(contentHeight + headTailHeight, maxWindowHeight)
        binding.recyclerView.layoutParams.height = minOf(contentHeight, maxWindowHeight - headTailHeight)
            .coerceAtLeast(0)
        window?.setLayout(width, windowHeight)
        binding.recyclerView.adapter = ItemSelectionAdapter(context, items, selectedIndex) { position, item ->
            callback(position, item)
            dismiss()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(ConvertUtils.dip2px(context, 12f)))
        binding.cancel.setOnClickListener { dismiss() }
    }
}