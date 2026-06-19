package com.tungsten.fcl.ui.setting.compose

import androidx.compose.ui.platform.ComposeView
import com.tungsten.fcllibrary.component.theme.ThemePreset

/**
 * 在 Java 设置页面中设置液态玻璃主题预览的便捷函数。
 */
fun ComposeView.setLiquidGlassPreview(preset: ThemePreset, isDark: Boolean) {
    setContent {
        LiquidGlassThemePreview(preset = preset, isDark = isDark)
    }
}
