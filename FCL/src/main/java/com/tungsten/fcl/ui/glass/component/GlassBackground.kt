package com.tungsten.fcl.ui.glass.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.tungsten.fcllibrary.component.theme.ThemePreset

/**
 * 液态玻璃背景层。
 * 根据当前主题模式和预设色渲染渐变背景，为 GlassNavHost 提供视觉底层。
 */
@Composable
fun GlassBackground(
    preset: ThemePreset,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val tintColor = Color(preset.getColor())
    val backgroundColor = if (isDark) Color(0xFF1A1A2E) else Color(0xFFF0F4F8)
    val secondaryColor = if (isDark) Color(preset.getColor2Dark()) else Color(preset.getColor2())

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        tintColor.copy(alpha = 0.08f),
                        backgroundColor,
                        secondaryColor.copy(alpha = 0.05f)
                    )
                )
            )
    )
}
