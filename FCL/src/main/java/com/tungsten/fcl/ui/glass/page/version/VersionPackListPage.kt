package com.tungsten.fcl.ui.glass.page.version

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.FileUtils
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Locale
import java.util.stream.Collectors

@Composable
fun VersionPackListPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    folderName: String,
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state = remember(profile, version, folderName) {
        PackListStateHolder(profile, version, folderName)
    }
    var showSearch by remember { mutableStateOf(false) }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { state.importPack(context, it) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = title,
            onBack = onBack,
            actions = {
                IconButton(onClick = { importLauncher.launch(arrayOf("application/zip")) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Import",
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

        Box(modifier = Modifier.weight(1f)) {
            val filtered = state.filteredItems()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filtered,
                    key = { it.file.toString() }
                ) { item ->
                    PackItemCard(
                        backdrop = backdrop,
                        item = item,
                        onDelete = {
                            GlassDialogManager.showAlert(
                                title = context.getString(R.string.button_remove),
                                message = context.getString(R.string.button_remove_confirm),
                                confirmText = context.getString(R.string.button_remove),
                                dismissText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                                onConfirm = { state.delete(item) }
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
                    text = stringResource(R.string.mods_check_updates_empty),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun PackItemCard(
    backdrop: Backdrop,
    item: PackItem,
    onDelete: () -> Unit
) {
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
                    imageVector = Icons.Default.Folder,
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
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (item.isDirectory) "Folder" else "Zip",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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

data class PackItem(
    val file: Path,
    val name: String,
    val isDirectory: Boolean
)

class PackListStateHolder(
    private val profile: Profile,
    private val versionId: String,
    private val folderName: String
) {
    var isLoading by mutableStateOf(false)
        private set
    var query by mutableStateOf("")
    val items = mutableStateListOf<PackItem>()

    init {
        refresh()
    }

    fun refresh() {
        isLoading = true
        Task.runAsync {
            try {
                val dir = profile.getRepository().getRunDirectory(versionId).toPath().resolve(folderName)
                val list = if (Files.isDirectory(dir)) {
                    Files.list(dir).use { stream ->
                        stream
                            .filter { Files.isDirectory(it) || isZipFile(it) }
                            .map { PackItem(it, FileUtils.getName(it), Files.isDirectory(it)) }
                            .sorted(compareBy { it.name.lowercase(Locale.getDefault()) })
                            .collect(Collectors.toList())
                    }
                } else {
                    emptyList()
                }
                Schedulers.androidUIThread().execute {
                    items.clear()
                    items.addAll(list)
                    isLoading = false
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                }
            }
        }.start()
    }

    fun filteredItems(): List<PackItem> {
        val q = query.trim().lowercase(Locale.getDefault())
        return if (q.isEmpty()) items.toList() else items.filter {
            it.name.lowercase(Locale.getDefault()).contains(q)
        }
    }

    fun delete(item: PackItem) {
        Task.runAsync {
            try {
                FileUtils.forceDelete(item.file.toFile())
                Schedulers.androidUIThread().execute { refresh() }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    // ignore
                }
            }
        }.start()
    }

    fun importPack(context: Context, uri: Uri) {
        val name = getDisplayName(context, uri) ?: uri.toString()
        if (!name.lowercase(Locale.getDefault()).endsWith(".zip")) {
            Toast.makeText(context, R.string.world_import_invalid, Toast.LENGTH_SHORT).show()
            return
        }
        isLoading = true
        Task.runAsync {
            try {
                val dir = profile.getRepository().getRunDirectory(versionId).toPath().resolve(folderName)
                Files.createDirectories(dir)
                val target = dir.resolve(name)
                context.contentResolver.openInputStream(uri)?.use { input ->
                    Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
                } ?: throw IOException("Failed to open input stream")
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, R.string.message_success, Toast.LENGTH_SHORT).show()
                    refresh()
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                    Toast.makeText(context, R.string.message_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun getDisplayName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) return cursor.getString(index)
                }
            }
        }
        return uri.path?.let { java.io.File(it).name }
    }

    private fun isZipFile(path: Path): Boolean {
        return path.toString().lowercase(Locale.getDefault()).endsWith(".zip")
    }
}