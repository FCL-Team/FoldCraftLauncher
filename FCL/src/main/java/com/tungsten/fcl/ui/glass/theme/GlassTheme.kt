package com.tungsten.fcl.ui.glass.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object GlassTheme {
    val glassCornerRadius = 28.dp
    val buttonCornerRadius = 50.dp
    val chipCornerRadius = 50.dp

    @Composable
    fun backgroundColor(): Color {
        return if (isSystemInDarkTheme()) Color(0xFF1A1A2E) else Color(0xFFF0F4F8)
    }

    @Composable
    fun surfaceColor(): Color {
        return Color.White.copy(alpha = 0.45f)
    }

    @Composable
    fun onSurfaceColor(): Color {
        return if (isSystemInDarkTheme()) Color.White else Color(0xFF1A1A2E)
    }
}
