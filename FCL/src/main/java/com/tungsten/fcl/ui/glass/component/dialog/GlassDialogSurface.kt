package com.tungsten.fcl.ui.glass.component.dialog

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.ui.glass.theme.GlassTheme

@Composable
fun GlassDialogSurface(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(GlassTheme.glassCornerRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(12f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(12f.dp.toPx(), 24f.dp.toPx())
                    }
                },
                onDrawSurface = { drawRect(GlassTheme.surfaceColor()) }
            )
            .padding(20.dp)
    ) {
        content()
    }
}
