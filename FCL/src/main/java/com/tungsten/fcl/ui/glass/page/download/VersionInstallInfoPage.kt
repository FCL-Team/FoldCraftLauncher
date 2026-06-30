package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.InstallerItem
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.StringUtils
import java.util.Arrays

@Composable
fun VersionInstallInfoPage(
    backdrop: Backdrop,
    gameVersion: String,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val group = remember { InstallerItem.InstallerItemGroup(context, gameVersion) }
    var name by remember { mutableStateOf(gameVersion) }
    var nameManuallyModified by remember { mutableStateOf(false) }
    val selectedVersions = VersionInstallState.selectedVersions

    fun applySelections() {
        group.getLibraries().forEach { item ->
            val id = item.getLibraryId()
            val remoteVersion = selectedVersions[id]
            if (remoteVersion != null) {
                item.libraryVersion.set(remoteVersion.selfVersion)
                item.removable.set(true)
            } else {
                item.libraryVersion.set(null)
                item.removable.set(false)
            }
        }
    }

    fun generateVersionName(): String {
        val nameBuilder = StringBuilder(gameVersion)
        Arrays.stream(LibraryAnalyzer.LibraryType.values())
            .filter { selectedVersions.containsKey(it.patchId) }
            .map { getLoaderName(context, it) }
            .filter { it != null }
            .forEach { nameBuilder.append("-").append(it) }
        return nameBuilder.toString()
    }

    fun refreshVersionName() {
        if (!nameManuallyModified) {
            name = generateVersionName()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GlassTopBar(title = stringResource(R.string.install_new_game))

        Text(
            text = stringResource(R.string.archive_name),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassTextField(
            backdrop = backdrop,
            value = name,
            onValueChange = {
                name = it
                nameManuallyModified = it != generateVersionName()
            },
            hint = stringResource(R.string.archive_name),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        InstallerItemGroupCompose(
            backdrop = backdrop,
            group = group,
            tint = tintColor,
            onSelectLibrary = { item, libraryId ->
                if (libraryId == LibraryAnalyzer.LibraryType.FABRIC_API.getPatchId()) {
                    FCLAlertDialog.Builder(context)
                        .setCancelable(false)
                        .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                        .setMessage(context.getString(R.string.install_installer_fabric_api_warning))
                        .setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                        .create()
                        .show()
                }

                if (item.incompatibleLibraryName.get() == null && item.dependencyName.get() == null) {
                    onNavigate(FCLGlassDownloadRoute.InstallerVersionSelect(gameVersion, libraryId))
                }
            },
            onRemoveLibrary = { _, libraryId ->
                selectedVersions.remove(libraryId)
                applySelections()
                refreshVersionName()
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GlassButton(
            backdrop = backdrop,
            onClick = {
                val profile = Profiles.getSelectedProfile()
                when {
                    StringUtils.isBlank(name) -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.input_not_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    profile.getRepository().versionIdConflicts(name) -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.install_new_game_already_exists),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    !FCLGameRepository.isValidVersionId(name) -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.install_new_game_malformed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> installVersion(context, profile, name, gameVersion, selectedVersions)
                }
            },
            tint = tintColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.button_install),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun getLoaderName(context: Context, libraryType: LibraryAnalyzer.LibraryType): String? {
    return when (libraryType) {
        LibraryAnalyzer.LibraryType.FORGE -> context.getString(R.string.install_installer_forge)
        LibraryAnalyzer.LibraryType.NEO_FORGE -> context.getString(R.string.install_installer_neoforge)
        LibraryAnalyzer.LibraryType.CLEANROOM -> context.getString(R.string.install_installer_cleanroom)
        LibraryAnalyzer.LibraryType.FABRIC -> context.getString(R.string.install_installer_fabric)
        LibraryAnalyzer.LibraryType.LITELOADER -> context.getString(R.string.install_installer_liteloader)
        LibraryAnalyzer.LibraryType.OPTIFINE -> context.getString(R.string.install_installer_optifine)
        LibraryAnalyzer.LibraryType.QUILT -> context.getString(R.string.install_installer_quilt)
        else -> null
    }
}

private fun installVersion(
    context: Context,
    profile: Profile,
    name: String,
    gameVersion: String,
    selectedVersions: Map<String, RemoteVersion>
) {
    val builder = profile.getDependency().gameBuilder()
    builder.name(name)
    builder.gameVersion(gameVersion)
    for ((libraryId, remoteVersion) in selectedVersions) {
        if (libraryId != LibraryAnalyzer.LibraryType.MINECRAFT.getPatchId()) {
            builder.version(remoteVersion)
        }
    }
    val task: Task<*> = builder.buildAsync()
    val dialog = TaskDialog(context, TaskCancellationAction { it.dismiss() })
    dialog.setTitle(context.getString(R.string.install_new_game))
    val executor = task.executor()
    dialog.setExecutor(executor, true)
    dialog.show()
    executor.start()
}
