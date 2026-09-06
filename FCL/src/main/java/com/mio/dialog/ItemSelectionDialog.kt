package com.mio.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
        // 宽度：以最长条目与标题为基准自适应，夹在 MD3 区间 [280dp, min(560dp, 屏宽-48dp)]
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
            ConvertUtils.dip2px(context, 280f),
            minOf(maxWidth, maxOf(sample.measuredWidth, titleView.measuredWidth) + ConvertUtils.dip2px(context, 48f))
        )
        // 高度：条目行高实测累加，超出 small 上限（50% 屏）或大对话框上限（90% 屏）时滚动
        val spacing = ConvertUtils.dip2px(context, 10f)
        val contentHeight = items.size * sample.measuredHeight + (items.size - 1) * spacing
        val headTailHeight = ConvertUtils.dip2px(context, 140f)
        val maxContentHeight = ((if (small) 0.5f else 0.9f) * metrics.heightPixels - headTailHeight)
            .toInt()
            .coerceAtLeast(0)
        binding.recyclerView.layoutParams.height = minOf(contentHeight, maxContentHeight)
        window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        binding.recyclerView.adapter = ItemSelectionAdapter(context, items, selectedIndex) { position, item ->
            callback(position, item)
            dismiss()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(ConvertUtils.dip2px(context, 10f)))
        binding.cancel.setOnClickListener { dismiss() }
    }
}