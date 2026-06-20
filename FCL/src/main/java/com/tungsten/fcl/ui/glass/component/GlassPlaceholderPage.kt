package com.tungsten.fcl.ui.glass.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kyant.backdrop.Backdrop

@Composable
fun GlassPlaceholderPage(
    backdrop: Backdrop,
    title: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = title)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
