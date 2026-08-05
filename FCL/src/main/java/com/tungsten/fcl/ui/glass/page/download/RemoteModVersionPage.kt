package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.glass.LocalFCLUILayout
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import java.util.stream.Collectors

@Composable
fun RemoteModVersionPage(
    backdrop: Backdrop,
    type: RemoteContentType,
    mod: RemoteMod,
    targetGameVersion: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val parent = LocalFCLUILayout.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var isLoading by remember { mutableStateOf(true) }
    var versions by remember { mutableStateOf<List<RemoteMod.Version>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var pendingVersion by remember { mutableStateOf<RemoteMod.Version?>(null) }

    LaunchedEffect(mod, targetGameVersion, type) {
        isLoading = true
        error = null
        val repository = type.getRepository(type.defaultSource(context), context)
        Task.supplyAsync {
            mod.getData().loadVersions(repository)
                .filter { it.gameVersions.contains(targetGameVersion) }
                .sorted(Comparator.comparing(RemoteMod.Version::getDatePublished).reversed())
                .collect(Collectors.toList())
        }.whenComplete(Schedulers.androidUIThread()) { list, exception ->
            isLoading = false
            if (exception == null && list != null) {
                versions = list
            } else {
                error = exception?.message ?: context.getString(R.string.download_failed_empty)
                showToast(context, error)
            }
        }.start()
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = mod.title + " - " + targetGameVersion)

        if (error != null) {
            GlassEmptyState(text = error ?: stringResource(R.string.download_failed_empty), modifier = Modifier.weight(1f))
        } else if (versions.isEmpty() && !isLoading) {
            GlassEmptyState(text = stringResource(R.string.download_failed_empty), modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(versions) { version ->
                    VersionFileCard(
                        backdrop = backdrop,
                        version = version,
                        tint = tintColor,
                        onDownload = {
                            pendingVersion = version
                        }
                    )
                }
            }
        }

        pendingVersion?.let { version ->
            AlertDialog(
                onDismissRequest = { pendingVersion = null },
                title = { Text(stringResource(R.string.dependencies)) },
                text = {
                    val deps = version.dependencies
                    if (deps.isNullOrEmpty()) {
                        Text(stringResource(R.string.message_no_dependency))
                    } else {
                        Column {
                            deps.forEach { dep ->
                                Text("• [${dep.type}] ${dep.id}")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val profile = Profiles.getSelectedProfile()
                        val selectedVersion = Profiles.getSelectedVersion() ?: ""
                        downloadVersion(context, type, parent, profile, selectedVersion, version)
                        pendingVersion = null
                    }) {
                        Text(stringResource(R.string.download))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { pendingVersion = null }) {
                        Text(stringResource(R.string.button_cancel))
                    }
                }
            )
        }

        GlassButton(
            backdrop = backdrop,
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.dialog_negative), color = Color.White)
        }
    }
}

@Composable
private fun VersionFileCard(
    backdrop: Backdrop,
    version: RemoteMod.Version,
    tint: Color,
    onDownload: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = version.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = version.versionType?.name ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            GlassButton(
                backdrop = backdrop,
                onClick = onDownload,
                tint = tint,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = stringResource(R.string.download), color = Color.White)
            }
        }
    }
}

private fun downloadVersion(
    context: Context,
    type: RemoteContentType,
    parent: FCLUILayout?,
    profile: Profile,
    selectedVersion: String,
    version: RemoteMod.Version
) {
    if (type == RemoteContentType.MODPACK) {
        // Modpack handles its own download + UI on UI thread
        Versions.downloadModpackImpl(context, parent, profile, version)
        return
    }

    val task = type.downloadTask(context, selectedVersion, version)
        .whenComplete(Schedulers.androidUIThread()) { file, exception ->
            if (exception == null && file != null) {
                try {
                    type.install(context, parent, profile, selectedVersion, version, file)
                } catch (e: Exception) {
                    showToast(context, e.message)
                }
            } else {
                showToast(context, exception?.message)
            }
        }

    val dialog = TaskDialog(context, TaskCancellationAction { task.cancel() })
    dialog.setTitle(context.getString(R.string.message_downloading))
    val executor = task.executor()
    dialog.setExecutor(executor)
    dialog.show()
    executor.start()
}

private fun showToast(context: Context, message: String?) {
    if (!message.isNullOrBlank()) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
