package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.download.VersionList
import com.tungsten.fclcore.task.Schedulers

@Composable
fun InstallerVersionSelectPage(
    backdrop: Backdrop,
    gameVersion: String,
    libraryId: String,
    onVersionSelected: (RemoteVersion) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var isLoading by remember { mutableStateOf(true) }
    var versions by remember { mutableStateOf<List<RemoteVersion>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(gameVersion, libraryId) {
        isLoading = true
        error = null
        versions = emptyList()
        val provider = DownloadProviders.getDownloadProvider()
        @Suppress("UNCHECKED_CAST")
        val versionList = provider.getVersionListById(libraryId) as VersionList<RemoteVersion>
        versionList.refreshAsync(gameVersion).whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception != null) {
                    error = exception.message
                } else {
                    versions = versionList.getVersions(gameVersion)
                        .sorted()
                        .toList()
                }
                isLoading = false
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.install_installer_game) + " - " + libraryId
        )

        if (error != null) {
            GlassEmptyState(
                text = error ?: stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else if (versions.isEmpty() && !isLoading) {
            GlassEmptyState(
                text = stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(versions, key = { it.selfVersion }) { version ->
                    GlassCard(
                        backdrop = backdrop,
                        modifier = Modifier.fillMaxWidth(),
                        tint = tintColor
                    ) {
                        Column(modifier = Modifier.clickable { onVersionSelected(version) }) {
                            Text(
                                text = version.selfVersion,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
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
            Text(
                text = stringResource(R.string.dialog_negative),
                color = Color.White
            )
        }
    }
}
