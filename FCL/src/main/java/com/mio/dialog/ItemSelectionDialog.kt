package com.mio.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.ui.applySelectableItemStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogItemSelectionBinding
import com.tungsten.fcl.databinding.ItemTextBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
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
            // 选中态统一样式：选中项主题色底，其余普通卡片（条目无勾选图标）
            applySelectableItemStyle(context, binding.root, null, position == selectedIndex, density)
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
        // 高度：先按实际宽度测量空白根布局得到标题 + 按钮 + 内边距高度（列表暂空为 0），
        // 再叠加条目行高实测值；超出 small 上限（50% 屏）或大对话框上限（90% 屏）时列表滚动。
        // 窗口高度交给 WRAP_CONTENT：根为 wrap 的线性布局，实际显示高度与内容精确一致，不会裁剪按钮
        binding.root.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val headTailHeight = binding.root.measuredHeight
        val spacing = ConvertUtils.dip2px(context, 12f)
        val rowHeight = sample.measuredHeight.coerceAtLeast(ConvertUtils.dip2px(context, 48f))
        val contentHeight = items.size * rowHeight + (items.size - 1) * spacing
        val maxWindowHeight = ((if (small) 0.5f else 0.9f) * metrics.heightPixels).toInt()
        binding.recyclerView.layoutParams.height = minOf(contentHeight, maxWindowHeight - headTailHeight)
            .coerceAtLeast(0)
        window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        binding.recyclerView.adapter = ItemSelectionAdapter(context, items, selectedIndex) { position, item ->
            callback(position, item)
            dismiss()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(ConvertUtils.dip2px(context, 12f)))
        binding.cancel.setOnClickListener { dismiss() }
    }
}