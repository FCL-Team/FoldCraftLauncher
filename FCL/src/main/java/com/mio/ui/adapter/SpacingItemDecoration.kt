package com.mio.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 列表行间垂直间距装饰器（替代手工分隔线），最后一行不添加间距。
 */
class SpacingItemDecoration(private val spacingPx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount ?: return
        if (parent.getChildAdapterPosition(view) != itemCount - 1) {
            outRect.bottom = spacingPx
        }
    }
}
