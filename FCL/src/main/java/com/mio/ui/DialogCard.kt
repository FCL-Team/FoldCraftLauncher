package com.mio.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/** 对话框内条目卡片背景：亮色近白、暗色深灰固定底色，与对话框背景分层可辨 */
fun dialogCardBackground(context: Context, density: Float): GradientDrawable =
    GradientDrawable().apply {
        cornerRadius = 10 * density
        val dark = ThemeEngine.isNightMode(context)
        setColor(if (dark) "#323232".toColorInt() else "#F8F8F8".toColorInt())
    }

/** 条目卡片投影：使用 DialogCard 背景的条目统一调用，形成轻浮起效果 */
fun applyCardElevation(view: View, density: Float) {
    view.elevation = 3f * density
}

/** 对话框内选中条目背景：主题色半透明圆角底（与 AnimationDialog 选中行一致） */
fun selectedCardBackground(themeColor: Int, density: Float): GradientDrawable =
    GradientDrawable().apply {
        cornerRadius = 10 * density
        setColor(ColorUtils.setAlphaComponent(themeColor, 30))
    }

/**
 * 列表项选中态统一样式：选中项主题色半透明圆角底 + 勾选图标（[check] 非空时一并控制），
 * 未选中项为普通卡片背景且隐藏勾选。列表选择类对话框统一走此方法。
 */
fun applySelectableItemStyle(
    context: Context,
    root: View,
    check: ImageView?,
    selected: Boolean,
    density: Float
) {
    applyCardElevation(root, density)
    root.background = dialogCardBackground(context, density)
    check?.visibility = View.GONE
    if (selected) {
        val themeColor = ThemeEngine.getTheme().getColor()
        root.background = selectedCardBackground(themeColor, density)
        check?.setColorFilter(themeColor)
        check?.visibility = View.VISIBLE
    }
}
