package com.tungsten.fcl.ui.glass.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcllibrary.component.theme.ThemePreset

@Composable
fun SettingsPage(
    backdrop: Backdrop,
    currentPreset: ThemePreset,
    onPresetChange: (ThemePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}
