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
        val screenHeight = context.resources.displayMetrics.heightPixels
        // small 模式：按条目数估算内容高度，条目少时抬升到屏幕 35%（自动放大），条目多时封顶 50% 滚动
        val contentHeight = items.size * ConvertUtils.dip2px(context, 50f)
        val height = if (small) {
            contentHeight.coerceIn(
                (screenHeight * 0.35f).toInt(),
                (screenHeight * 0.5f).toInt()
            )
        } else {
            ViewGroup.LayoutParams.MATCH_PARENT
        }
        window?.setLayout(ConvertUtils.dip2px(context, 500f), height)
        val binding = DialogItemSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = title
        binding.recyclerView.adapter = ItemSelectionAdapter(context, items, selectedIndex) { position, item ->
            callback(position, item)
            dismiss()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(ConvertUtils.dip2px(context, 10f)))
        binding.cancel.setOnClickListener { dismiss() }
    }
}