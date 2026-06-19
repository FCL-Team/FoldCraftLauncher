package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.InstallerItem
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener

@Composable
fun InstallerItemGroupCompose(
    backdrop: Backdrop,
    group: InstallerItem.InstallerItemGroup,
    tint: Color,
    onSelectLibrary: (InstallerItem, String) -> Unit,
    onRemoveLibrary: (InstallerItem, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        group.getLibraries().forEach { item ->
            InstallerItemCard(
                backdrop = backdrop,
                item = item,
                tint = tint,
                onSelect = { onSelectLibrary(item, item.getLibraryId()) },
                onRemove = { onRemoveLibrary(item, item.getLibraryId()) }
            )
        }
    }
}

@Composable
private fun InstallerItemCard(
    backdrop: Backdrop,
    item: InstallerItem,
    tint: Color,
    onSelect: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    var version by remember { mutableStateOf(item.libraryVersion.get() ?: "") }
    var incompatible by remember { mutableStateOf(item.incompatibleLibraryName.get()) }
    var dependency by remember { mutableStateOf(item.dependencyName.get()) }
    var removable by remember { mutableStateOf(item.removable.get()) }
    var incompatibleWithGame by remember { mutableStateOf(item.incompatibleWithGame.get()) }

    DisposableEffect(item) {
        val versionListener = InvalidationListener { version = item.libraryVersion.get() ?: "" }
        val incompatibleListener = InvalidationListener { incompatible = item.incompatibleLibraryName.get() }
        val dependencyListener = InvalidationListener { dependency = item.dependencyName.get() }
        val removableListener = ChangeListener<Boolean> { _, _, new -> removable = new }
        val incompatibleWithGameListener = ChangeListener<Boolean> { _, _, new -> incompatibleWithGame = new }

        item.libraryVersion.addListener(versionListener)
        item.incompatibleLibraryName.addListener(incompatibleListener)
        item.dependencyName.addListener(dependencyListener)
        item.removable.addListener(removableListener)
        item.incompatibleWithGame.addListener(incompatibleWithGameListener)

        onDispose {
            item.libraryVersion.removeListener(versionListener)
            item.incompatibleLibraryName.removeListener(incompatibleListener)
            item.dependencyName.removeListener(dependencyListener)
            item.removable.removeListener(removableListener)
            item.incompatibleWithGame.removeListener(incompatibleWithGameListener)
        }
    }

    val iconBitmap = item.getIcon()?.toBitmap()
    val canSelect = !incompatibleWithGame && incompatible == null && dependency == null

    GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.getName(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = when {
                        incompatibleWithGame -> context.getString(R.string.install_installer_change_version, version)
                        dependency != null -> context.getString(
                            R.string.install_installer_depend,
                            getLocalizedLoaderName(context, dependency)
                        )
                        incompatible != null -> context.getString(
                            R.string.install_installer_incompatible,
                            getLocalizedLoaderName(context, incompatible)
                        )
                        version.isEmpty() -> context.getString(R.string.install_installer_not_installed)
                        else -> version
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = when {
                        incompatibleWithGame || dependency != null || incompatible != null -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    }
                )
            }
            if (removable && version.isNotEmpty()) {
                GlassButton(
                    backdrop = backdrop,
                    onClick = onRemove,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_remove),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            GlassButton(
                backdrop = backdrop,
                onClick = onSelect,
                tint = if (canSelect) tint else null,
                enabled = canSelect
            ) {
                Text(
                    text = if (version.isNotEmpty()) stringResource(R.string.install_change_version) else stringResource(R.string.menu_control_select),
                    color = if (canSelect) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

private fun getLocalizedLoaderName(context: android.content.Context, patchId: String?): String {
    if (patchId == null) return ""
    val key = "install_installer_" + patchId.replace(".", "_").replace("-", "_")
    return AndroidUtils.getLocalizedText(context, key)
}
