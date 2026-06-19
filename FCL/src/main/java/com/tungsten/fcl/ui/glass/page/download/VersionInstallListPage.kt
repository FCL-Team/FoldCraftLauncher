package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateListOf
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
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.versioning.GameVersionNumber

@Composable
fun VersionInstallListPage(
    backdrop: Backdrop,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var query by remember { mutableStateOf("") }
    var checkRelease by remember { mutableStateOf(true) }
    var checkSnapshot by remember { mutableStateOf(false) }
    var checkOld by remember { mutableStateOf(false) }
    var checkAprilFools by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val versions = remember { mutableStateListOf<RemoteVersion>() }

    fun filterVersions(list: List<RemoteVersion>): List<RemoteVersion> {
        val q = query.lowercase()
        return list.filter { version ->
            val passesType = when (version.versionType) {
                RemoteVersion.Type.RELEASE -> checkRelease
                RemoteVersion.Type.SNAPSHOT,
                RemoteVersion.Type.PENDING,
                RemoteVersion.Type.UNOBFUSCATED -> {
                    if (checkSnapshot) true
                    else if (checkAprilFools) isAprilFoolsVersion(version.gameVersion)
                    else false
                }
                RemoteVersion.Type.OLD -> {
                    if (checkOld) true
                    else if (checkAprilFools) isAprilFoolsVersion(version.gameVersion)
                    else false
                }
                else -> true
            }
            passesType && version.gameVersion.contains(q)
        }.sorted()
    }

    fun refreshList() {
        isLoading = true
        versions.clear()
        val versionList = DownloadProviders.getDownloadProvider().getVersionListById("game")
        versionList.refreshAsync("").whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception == null) {
                    val all = versionList.getVersions("").filterIsInstance<RemoteVersion>()
                    val filtered = filterVersions(all)
                    if (filtered.isEmpty() && !(checkRelease && checkSnapshot && checkOld)) {
                        checkRelease = true
                        checkSnapshot = true
                        checkOld = true
                        versions.addAll(filterVersions(all))
                    } else {
                        versions.addAll(filtered)
                    }
                }
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { refreshList() }

    val filtered = remember(versions, query, checkRelease, checkSnapshot, checkOld, checkAprilFools) {
        filterVersions(versions.toList())
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = stringResource(R.string.install_installer_game))

        FilterRow(
            backdrop = backdrop,
            checkRelease = checkRelease,
            onReleaseChange = { checkRelease = it },
            checkSnapshot = checkSnapshot,
            onSnapshotChange = { checkSnapshot = it },
            checkOld = checkOld,
            onOldChange = { checkOld = it },
            checkAprilFools = checkAprilFools,
            onAprilFoolsChange = { checkAprilFools = it },
            tint = tintColor,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassSearchBar(
            backdrop = backdrop,
            query = query,
            onQueryChange = { query = it },
            hint = stringResource(R.string.search),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassButton(
            backdrop = backdrop,
            onClick = { refreshList() },
            tint = tintColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.action_refresh),
                color = Color.White
            )
        }

        if (filtered.isEmpty() && !isLoading) {
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
                items(filtered, key = { it.selfVersion }) { version ->
                    RemoteVersionCard(
                        backdrop = backdrop,
                        version = version,
                        tint = tintColor,
                        onClick = {
                            onNavigate(FCLGlassDownloadRoute.VersionInstallInfo(version.gameVersion))
                        }
                    )
                }
            }
        }
    }
}

private fun isAprilFoolsVersion(version: String): Boolean {
    return try {
        GameVersionNumber.asGameVersion(version).isAprilFools()
    } catch (e: Exception) {
        false
    }
}

@Composable
private fun FilterRow(
    backdrop: Backdrop,
    checkRelease: Boolean,
    onReleaseChange: (Boolean) -> Unit,
    checkSnapshot: Boolean,
    onSnapshotChange: (Boolean) -> Unit,
    checkOld: Boolean,
    onOldChange: (Boolean) -> Unit,
    checkAprilFools: Boolean,
    onAprilFoolsChange: (Boolean) -> Unit,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        GlassCheckbox(backdrop, checkRelease, onReleaseChange, stringResource(R.string.version_game_release), tint = tint)
        GlassCheckbox(backdrop, checkSnapshot, onSnapshotChange, stringResource(R.string.version_game_snapshot), tint = tint)
        GlassCheckbox(backdrop, checkOld, onOldChange, "Old", tint = tint)
        GlassCheckbox(backdrop, checkAprilFools, onAprilFoolsChange, stringResource(R.string.version_game_april_fools), tint = tint)
    }
}

@Composable
private fun RemoteVersionCard(
    backdrop: Backdrop,
    version: RemoteVersion,
    tint: Color,
    onClick: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth(),
        tint = tint
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            Text(
                text = version.gameVersion,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = version.versionType.name,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
