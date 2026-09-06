package com.mio.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/** 对话框内条目卡片背景：以对话框背景为基准，亮色向黑微调、暗色向白微亮，与对话框分层可辨 */
fun dialogCardBackground(context: Context, density: Float): GradientDrawable = GradientDrawable().apply {
    cornerRadius = 10 * density
    val dialogColor = ContextCompat.getColor(context, R.color.dialog_background)
    val dark = ThemeEngine.isNightMode(context)
    val blend = if (dark) 0.07f else 0.02f
    setColor(ColorUtils.blendARGB(dialogColor, if (dark) Color.WHITE else Color.BLACK, blend))
}

/** 对话框内选中条目背景：主题色半透明圆角底（与 AnimationDialog 选中行一致） */
fun selectedCardBackground(themeColor: Int, density: Float): GradientDrawable = GradientDrawable().apply {
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
    root.background = dialogCardBackground(context, density)
    check?.visibility = View.GONE
    if (selected) {
        val themeColor = ThemeEngine.getTheme().getColor()
        root.background = selectedCardBackground(themeColor, density)
        check?.setColorFilter(themeColor)
        check?.visibility = View.VISIBLE
    }
}