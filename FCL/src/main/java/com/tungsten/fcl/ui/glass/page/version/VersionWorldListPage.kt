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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.game.World
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VersionWorldListPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember(profile, version) { VersionWorldListStateHolder(profile, version) }
    val tintColor = androidx.compose.ui.graphics.Color(ThemeEngine.getInstance().getTheme().getColor())
    var showSearch by remember { mutableStateOf(false) }
    var pendingExportWorld by remember { mutableStateOf<World?>(null) }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { sourceUri ->
            Task.runAsync {
                try {
                    val tempZip = File(context.cacheDir, "import_world.zip").toPath()
                    Files.deleteIfExists(tempZip)
                    context.contentResolver.openInputStream(sourceUri)?.use { input ->
                        Files.copy(input, tempZip, StandardCopyOption.REPLACE_EXISTING)
                    } ?: throw java.io.IOException("Failed to open input stream")
                    val world = World(tempZip)
                    Schedulers.androidUIThread().execute {
                        GlassDialogManager.showInput(
                            title = context.getString(R.string.world_name),
                            initialValue = world.worldName,
                            hint = context.getString(R.string.world_name_enter),
                            confirmText = context.getString(R.string.button_install),
                            dismissText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                            onConfirm = { name -> state.importWorld(context, sourceUri, name) }
                        ) {}
                    }
                } catch (e: Exception) {
                    Schedulers.androidUIThread().execute {
                        android.widget.Toast.makeText(
                            context,
                            context.getString(R.string.world_import_invalid),
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.start()
        }
    }

    val exportTreeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { treeUri ->
        val world = pendingExportWorld ?: return@rememberLauncherForActivityResult
        pendingExportWorld = null
        treeUri?.let { uri ->
            GlassDialogManager.showInput(
                title = context.getString(R.string.world_export),
                initialValue = "${world.worldName}.zip",
                hint = "file name",
                confirmText = context.getString(R.string.button_export),
                dismissText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                onConfirm = { fileName -> state.exportWorld(context, world, fileName, uri) }
            ) {}
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.world_manage),
            onBack = onBack,
            actions = {
                IconButton(onClick = { importLauncher.launch(arrayOf("application/zip")) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Import World",
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            GlassCheckbox(
                backdrop = backdrop,
                checked = state.showAll,
                onCheckedChange = { state.showAll = it },
                label = stringResource(R.string.world_show_all),
                tint = tintColor
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            val filtered = state.filteredWorlds()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filtered,
                    key = { it.fileName }
                ) { world ->
                    WorldItemCard(
                        backdrop = backdrop,
                        world = world,
                        onExport = {
                            pendingExportWorld = world
                            exportTreeLauncher.launch(null)
                        },
                        onDelete = {
                            GlassDialogManager.showAlert(
                                title = context.getString(R.string.button_remove),
                                message = context.getString(R.string.version_manage_remove_confirm, world.worldName),
                                confirmText = context.getString(R.string.button_remove),
                                dismissText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                                onConfirm = { state.delete(context, world) }
                            ) {}
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            if (state.isLoading && filtered.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (!state.isLoading && filtered.isEmpty()) {
                GlassEmptyState(
                    text = stringResource(R.string.world_import_invalid),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun WorldItemCard(
    backdrop: Backdrop,
    world: World,
    onExport: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat(context.getString(R.string.world_time), Locale.getDefault()) }
    val subtitle = stringResource(
        R.string.world_description,
        world.fileName,
        dateFormat.format(Date(world.lastPlayed)),
        world.gameVersion ?: context.getString(R.string.message_unknown)
    )

    GlassCard(
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    text = world.worldName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2
                )
            }

            IconButton(onClick = onExport) {
                Icon(
                    imageVector = Icons.Default.FileUpload,
                    contentDescription = "Export",
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