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
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassChip
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun VersionInstallPage(
    backdrop: Backdrop,
    profile: com.tungsten.fcl.setting.Profile,
    parentVersion: String? = null,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val state = remember { VersionInstallStateHolder(profile, parentVersion) }
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())

    var checkRelease by remember { mutableStateOf(true) }
    var checkSnapshot by remember { mutableStateOf(false) }
    var checkOld by remember { mutableStateOf(false) }
    var checkAprilFools by remember { mutableStateOf(false) }

    val filteredGameVersions = remember(
        state.gameVersions,
        checkRelease,
        checkSnapshot,
        checkOld,
        checkAprilFools
    ) {
        state.gameVersions.filter { version ->
            passesGameVersionFilter(version, checkRelease, checkSnapshot, checkOld, checkAprilFools)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.install_new_game),
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            item {
                VersionNameField(
                    backdrop = backdrop,
                    value = state.versionName,
                    onValueChange = { state.setVersionName(it) },
                    hint = stringResource(R.string.archive_name)
                )
            }

            item {
                GlassSectionTitle(text = stringResource(R.string.install_installer_game))
            }

            item {
                GameVersionFilterRow(
                    backdrop = backdrop,
                    checkRelease = checkRelease,
                    onReleaseChange = { checkRelease = it },
                    checkSnapshot = checkSnapshot,
                    onSnapshotChange = { checkSnapshot = it },
                    checkOld = checkOld,
                    onOldChange = { checkOld = it },
                    checkAprilFools = checkAprilFools,
                    onAprilFoolsChange = { checkAprilFools = it },
                    tint = tintColor
                )
            }

            items(filteredGameVersions, key = { it.gameVersion }) { version ->
                SelectableVersionCard(
                    backdrop = backdrop,
                    version = version,
                    selected = state.selectedGameVersion == version,
                    tint = tintColor,
                    onClick = { state.setSelectedGameVersion(version) }
                )
            }

            item {
                GlassSectionTitle(text = "Loader")
            }

            item {
                LoaderChipRow(
                    backdrop = backdrop,
                    loaders = state.loaders,
                    selected = state.selectedLoader,
                    tint = tintColor,
                    onSelect = { state.setSelectedLoader(it) }
                )
            }

            if (state.selectedLoader != LoaderType.VANILLA) {
                item {
                    GlassSectionTitle(
                        text = stringResource(
                            state.selectedLoader.displayNameRes ?: R.string.install_installer_game
                        )
                    )
                }

                items(state.loaderVersions, key = { it.selfVersion }) { version ->
                    SelectableVersionCard(
                        backdrop = backdrop,
                        version = version,
                        selected = state.selectedLoaderVersion == version,
                        tint = tintColor,
                        onClick = { state.setSelectedLoaderVersion(version) }
                    )
                }
            }
        }

        GlassButton(
            backdrop = backdrop,
            onClick = { state.install(context, onComplete) },
            enabled = state.selectedGameVersion != null && !state.isInstalling,
            tint = tintColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.button_install),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun VersionNameField(
    backdrop: Backdrop,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    Column {
        Text(
            text = stringResource(R.string.archive_name),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        GlassTextField(
            backdrop = backdrop,
            value = value,
            onValueChange = onValueChange,
            hint = hint
        )
    }
}

@Composable
private fun GameVersionFilterRow(
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
        GlassCheckbox(
            backdrop = backdrop,
            checked = checkRelease,
            onCheckedChange = onReleaseChange,
            label = stringResource(R.string.version_game_release),
            tint = tint
        )
        GlassCheckbox(
            backdrop = backdrop,
            checked = checkSnapshot,
            onCheckedChange = onSnapshotChange,
            label = stringResource(R.string.version_game_snapshot),
            tint = tint
        )
        GlassCheckbox(
            backdrop = backdrop,
            checked = checkOld,
            onCheckedChange = onOldChange,
            label = "Old",
            tint = tint
        )
        GlassCheckbox(
            backdrop = backdrop,
            checked = checkAprilFools,
            onCheckedChange = onAprilFoolsChange,
            label = stringResource(R.string.version_game_april_fools),
            tint = tint
        )
    }
}

@Composable
private fun LoaderChipRow(
    backdrop: Backdrop,
    loaders: List<LoaderType>,
    selected: LoaderType,
    tint: Color,
    onSelect: (LoaderType) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        loaders.forEach { loader ->
            GlassChip(
                backdrop = backdrop,
                text = loader.getDisplayName(context),
                selected = selected == loader,
                onClick = { onSelect(loader) },
                tint = tint
            )
        }
    }
}

@Composable
private fun SelectableVersionCard(
    backdrop: Backdrop,
    version: RemoteVersion,
    selected: Boolean,
    tint: Color,
    onClick: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth(),
        tint = if (selected) tint else null
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            Text(
                text = version.gameVersion,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = version.versionType.name,
                color = if (selected) Color.White.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun passesGameVersionFilter(
    version: RemoteVersion,
    checkRelease: Boolean,
    checkSnapshot: Boolean,
    checkOld: Boolean,
    checkAprilFools: Boolean
): Boolean {
    return when (version.versionType) {
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
}

private fun isAprilFoolsVersion(version: String): Boolean {
    return try {
        GameVersionNumber.asGameVersion(version).isAprilFools()
    } catch (e: Exception) {
        false
    }
}
