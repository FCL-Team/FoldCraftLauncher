package com.mio.ui.adapter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 列表行间垂直间距装饰器（替代手工分隔线），最后一行不添加间距。
 * [spacingFor] 按行返回底部间距：分组列表可用它返回小间距（如 1dp）形成组内分割线，
 * 或返回 0 让组内行无缝拼接；null 时所有行统一用 [spacingPx]。
 * [dividerColor] 非 null 时在小于默认间距的缝隙处绘制分割线（每次绘制动态取色，
 * 主题切换后需对列表 invalidate 触发重绘）。
 */
class SpacingItemDecoration @JvmOverloads constructor(
    private val spacingPx: Int,
    private val spacingFor: ((RecyclerView, Int) -> Int)? = null,
    private val dividerColor: (() -> Int)? = null
) : RecyclerView.ItemDecoration() {

    private val dividerPaint = Paint()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount ?: return
        val position = parent.getChildAdapterPosition(view)
        if (position != itemCount - 1) {
            outRect.bottom = spacingFor?.invoke(parent, position) ?: spacingPx
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val colorProvider = dividerColor ?: return
        val itemCount = parent.adapter?.itemCount ?: return
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position == RecyclerView.NO_POSITION || position == itemCount - 1) continue
            val spacing = spacingFor?.invoke(parent, position) ?: spacingPx
            // 只在小于默认间距的缝隙处绘制分割线（组内行间）
            if (spacing <= 0 || spacing >= spacingPx) continue
            dividerPaint.color = colorProvider()
            dividerPaint.strokeWidth = spacing.toFloat()
            val top = child.bottom.toFloat()
            c.drawLine(
                child.left.toFloat(),
                top + spacing / 2f,
                child.right.toFloat(),
                top + spacing / 2f,
                dividerPaint
            )
        }
    }
}