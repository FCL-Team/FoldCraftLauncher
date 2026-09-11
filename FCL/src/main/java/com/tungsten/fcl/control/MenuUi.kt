package com.tungsten.fcl.control

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView

/** 菜单条目卡片背景：半透明深色，圆角与左/右菜单抽屉背景均可区分 */
fun menuCardBackground(density: Float): GradientDrawable = GradientDrawable().apply {
    cornerRadius = 10 * density
    setColor(ColorUtils.setAlphaComponent(Color.WHITE, 10))
}

/** 菜单条目顶部间距，卡片之间留白 */
class SpacingDecoration(private val topSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = topSpacing
    }
}