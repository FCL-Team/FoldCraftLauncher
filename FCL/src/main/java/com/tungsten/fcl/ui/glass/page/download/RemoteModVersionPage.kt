package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.glass.LocalFCLUILayout
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
                            downloadVersion(context, type, version, parent)
                        }
                    )
                }
            }
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
    version: RemoteMod.Version,
    parent: FCLUILayout?
) {
    val profile = Profiles.getSelectedProfile()
    val selectedVersion = Profiles.getSelectedVersion() ?: ""
    type.installCallback(context, parent).invoke(profile, selectedVersion, version)
}
