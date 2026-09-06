package com.mio.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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