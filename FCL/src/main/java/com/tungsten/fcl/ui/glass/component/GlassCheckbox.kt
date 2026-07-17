package com.tungsten.fcl.ui.glass.component

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun GlassCheckbox(
    backdrop: Backdrop,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    val shape = RoundedCornerShape(GlassTheme.chipCornerRadius)
    val contentColor = if (checked && tint != null) Color.White else MaterialTheme.colorScheme.onSurface
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onCheckedChange(!checked) }
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
                    if (checked && tint != null) {
                        drawRect(tint, blendMode = androidx.compose.ui.graphics.BlendMode.Hue)
                        drawRect(tint.copy(alpha = 0.75f))
                    } else {
                        drawRect(GlassTheme.surfaceColor())
                    }
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = tint ?: MaterialTheme.colorScheme.primary,
                uncheckedColor = contentColor,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
