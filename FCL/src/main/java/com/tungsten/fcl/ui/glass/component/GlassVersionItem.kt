package com.tungsten.fcl.ui.glass.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.version.VersionListItem
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

@Composable
fun GlassVersionItem(
    backdrop: Backdrop,
    item: VersionListItem,
    tint: Color,
    onLaunch: () -> Unit,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected by observeBooleanProperty(item.selectedProperty())
    var expanded by remember { mutableStateOf(false) }

    GlassCard(backdrop = backdrop, modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val bitmap = item.drawable?.toBitmap()
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.version,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.libraries,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (item.tag != null) {
                    Text(
                        text = item.tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = tint.copy(alpha = 0.9f)
                    )
                }
            }
            RadioButton(
                selected = selected,
                onClick = { item.profile.selectedVersion = item.version }
            )
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.launch)) },
                        onClick = { onLaunch(); expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.version_manage_rename)) },
                        onClick = { onRename(); expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.version_manage_duplicate)) },
                        onClick = { onDuplicate(); expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.button_remove)) },
                        onClick = { onDelete(); expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.menu_settings)) },
                        onClick = { onSettings(); expanded = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun observeBooleanProperty(property: BooleanProperty): Boolean {
    var value by remember(property) { mutableStateOf(property.get()) }
    DisposableEffect(property) {
        val listener = ChangeListener<Boolean> { _, _, newValue -> value = newValue }
        property.addListener(listener)
        onDispose { property.removeListener(listener) }
    }
    return value
}
