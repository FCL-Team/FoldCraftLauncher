package com.tungsten.fcl.ui.glass.component

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.ui.glass.theme.GlassTheme

@Composable
fun GlassSourceSelector(
    backdrop: Backdrop,
    sources: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        sources.forEach { source ->
            val shape = RoundedCornerShape(GlassTheme.chipCornerRadius)
            val isSelected = source == selected
            Text(
                text = source,
                color = if (isSelected && tint != null) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clickable { onSelect(source) }
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { shape },
                        effects = {
                            vibrancy()
                            blur(4f.dp.toPx())
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                lens(6f.dp.toPx(), 12f.dp.toPx())
                            }
                        },
                        onDrawSurface = {
                            if (isSelected && tint != null) {
                                drawRect(tint, blendMode = androidx.compose.ui.graphics.BlendMode.Hue)
                                drawRect(tint.copy(alpha = 0.75f))
                            } else {
                                drawRect(GlassTheme.surfaceColor())
                            }
                        }
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
