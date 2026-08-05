package com.tungsten.fcl.ui.glass.page.version

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.glass.FCLGlassRoute
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun VersionModListPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    navController: NavController,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember(profile, version) { VersionModListStateHolder(profile, version) }
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var showSearch by remember { mutableStateOf(false) }

    val fileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            state.addMods(context, uris)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.mods_manage),
            onBack = onBack,
            actions = {
                if (state.editMode) {
                    IconButton(onClick = { state.selectAll() }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Select All",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { state.deleteSelected(context) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { state.editMode = false; state.selected.clear() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    IconButton(onClick = { fileLauncher.launch(arrayOf("*/*")) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Mod",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { state.editMode = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        )

        if (showSearch) {
            GlassSearchBar(
                backdrop = backdrop,
                query = state.query,
                onQueryChange = { state.query = it },
                hint = stringResource(R.string.search),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        if (!state.isModded) {
            GlassEmptyState(
                text = stringResource(R.string.mods_not_modded),
                modifier = Modifier.weight(1f)
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                GlassCheckbox(
                    backdrop = backdrop,
                    checked = state.showEnabled,
                    onCheckedChange = { state.showEnabled = it },
                    label = stringResource(R.string.enabled),
                    tint = tintColor
                )
                GlassCheckbox(
                    backdrop = backdrop,
                    checked = state.showDisabled,
                    onCheckedChange = { state.showDisabled = it },
                    label = stringResource(R.string.disabled),
                    tint = tintColor
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                val filteredItems = state.filteredItems()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredItems,
                        key = { it.mod.fileName }
                    ) { item ->
                        val selected = state.selected.contains(item)
                        ModItemCard(
                            backdrop = backdrop,
                            item = item,
                            selected = selected,
                            editMode = state.editMode,
                            tint = tintColor,
                            onSelectToggle = {
                                if (selected) state.selected.remove(item) else state.selected.add(item)
                            },
                            onActiveToggle = { state.toggleActive(item) },
                            onInfo = {
                                navController.navigate(
                                    FCLGlassRoute.ModInfo(
                                        modFileName = item.mod.fileName,
                                        profileName = profile.name,
                                        version = version
                                    )
                                )
                            },
                            onDelete = { state.delete(item, context) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                if (state.isLoading && filteredItems.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (!state.isLoading && filteredItems.isEmpty()) {
                    GlassEmptyState(
                        text = stringResource(R.string.mods_check_updates_empty),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ModItemCard(
    backdrop: Backdrop,
    item: ModListItem,
    selected: Boolean,
    editMode: Boolean,
    tint: Color,
    onSelectToggle: () -> Unit,
    onActiveToggle: () -> Unit,
    onInfo: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth(),
        tint = if (selected) tint else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (editMode) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onSelectToggle() }
                )
            }

            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                )
                if (item.tag.isNotBlank()) {
                    Text(
                        text = item.tag,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) Color.White.copy(alpha = 0.8f)
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) Color.White.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2
                )
            }

            if (!editMode) {
                Switch(
                    checked = item.isActive,
                    onCheckedChange = { onActiveToggle() }
                )
                IconButton(onClick = onInfo) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}