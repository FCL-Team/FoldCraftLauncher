package com.mio.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView

/** 来源徽标配色：主题次色实底 + 亮度对比色文字与平台 LOGO（搜索列表与收藏列表共用） */
fun applySourceBadgeStyle(badge: FCLTextView, curseforge: Boolean) {
    val context: Context = badge.context
    val color = ThemeEngine.getInstance().getTheme().getColor2()
    val contentColor = if (ColorUtils.calculateLuminance(color) >= 0.5f) Color.BLACK else Color.WHITE
    val background = GradientDrawable()
    background.shape = GradientDrawable.RECTANGLE
    background.cornerRadius = context.resources.displayMetrics.density * 16
    background.setColor(color)
    val logo = ContextCompat.getDrawable(
        context,
        if (curseforge) R.drawable.img_platform_curseforge else R.drawable.img_platform_modrinth
    )!!
    logo.mutate().setTint(contentColor)
    // PNG 原图 102×102，compound drawable 不缩放，须显式 bounds（与 Zalith 的 iconSize 12dp 一致）
    val logoSize = (context.resources.displayMetrics.density * 12).toInt()
    logo.setBounds(0, 0, logoSize, logoSize)
    badge.background = background
    badge.setTextColor(contentColor)
    badge.setCompoundDrawablesRelative(logo, null, null, null)
}
