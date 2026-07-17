package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.download.version.VersionInstallInfoPage
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.download.VersionList
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import java.io.File

enum class LoaderType(val patchId: String?, val displayNameRes: Int?) {
    VANILLA(null, null),
    FORGE("forge", R.string.install_installer_forge),
    FABRIC("fabric", R.string.install_installer_fabric),
    OPTIFINE("optifine", R.string.install_installer_optifine),
    QUILT("quilt", R.string.install_installer_quilt),
    NEOFORGE("neoforge", R.string.install_installer_neoforge);

    fun getDisplayName(context: Context): String {
        return displayNameRes?.let { context.getString(it) } ?: "Vanilla"
    }
}

class VersionInstallStateHolder(
    private val profile: Profile,
    private val parentVersion: String? = null
) {
    var gameVersions by mutableStateOf<List<RemoteVersion>>(emptyList())
        private set
    var selectedGameVersion by mutableStateOf<RemoteVersion?>(null)
        private set
    var selectedLoader by mutableStateOf(LoaderType.VANILLA)
        private set
    var loaderVersions by mutableStateOf<List<RemoteVersion>>(emptyList())
        private set
    var selectedLoaderVersion by mutableStateOf<RemoteVersion?>(null)
        private set
    var isInstalling by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var versionName by mutableStateOf("")
        private set
    var versionNameManuallySet by mutableStateOf(false)
        private set

    val loaders: List<LoaderType> = LoaderType.entries

    init {
        loadGameVersions()
    }

    fun setSelectedGameVersion(version: RemoteVersion?) {
        selectedGameVersion = version
        if (!versionNameManuallySet) {
            updateGeneratedName()
        }
        loadLoaderVersions()
    }

    fun setSelectedLoader(loader: LoaderType) {
        selectedLoader = loader
        if (!versionNameManuallySet) {
            updateGeneratedName()
        }
        loadLoaderVersions()
    }

    fun setSelectedLoaderVersion(version: RemoteVersion?) {
        selectedLoaderVersion = version
    }

    fun setVersionName(name: String) {
        versionName = name
        versionNameManuallySet = true
    }

    private fun updateGeneratedName() {
        val gameVersion = selectedGameVersion?.gameVersion ?: return
        versionName = if (selectedLoader == LoaderType.VANILLA) {
            gameVersion
        } else {
            "$gameVersion-${selectedLoader.patchId}"
        }
    }

    fun loadGameVersions() {
        errorMessage = null
        val versionList = DownloadProviders.getDownloadProvider().getVersionListById("game")
        versionList.refreshAsync("").whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception != null) {
                    errorMessage = exception.message
                } else {
                    gameVersions = versionList.getVersions("").filterIsInstance<RemoteVersion>().sorted()
                    selectedGameVersion = parentVersion?.let { parent ->
                        gameVersions.find { it.gameVersion == parent }
                    } ?: gameVersions.firstOrNull()
                    if (!versionNameManuallySet) {
                        updateGeneratedName()
                    }
                    loadLoaderVersions()
                }
            }
        }
    }

    fun loadLoaderVersions() {
        loaderVersions = emptyList()
        selectedLoaderVersion = null
        val gameVersion = selectedGameVersion?.gameVersion ?: return
        val patchId = selectedLoader.patchId ?: return

        @Suppress("UNCHECKED_CAST")
        val versionList = DownloadProviders.getDownloadProvider().getVersionListById(patchId) as? VersionList<RemoteVersion>
            ?: return
        versionList.refreshAsync(gameVersion).whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception != null) {
                    errorMessage = exception.message
                } else {
                    loaderVersions = versionList.getVersions(gameVersion).sorted().toList()
                    selectedLoaderVersion = loaderVersions.firstOrNull()
                }
            }
        }
    }

    fun install(context: Context, onComplete: () -> Unit) {
        val name = versionName.trim()
        val gameVersion = selectedGameVersion?.gameVersion ?: return

        when {
            StringUtils.isBlank(name) -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.input_not_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
            profile.repository.versionIdConflicts(name) -> {
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
            else -> {
                isInstalling = true
                val builder = profile.dependency.gameBuilder()
                builder.name(name)
                builder.gameVersion(gameVersion)
                selectedLoaderVersion?.let { builder.version(it) }

                val task: Task<*> = builder.buildAsync()
                val dialog = TaskDialog(context, TaskCancellationAction { it.dismiss() })
                dialog.setTitle(context.getString(R.string.install_new_game))

                val executor = task.executor(object : TaskListener() {
                    override fun onStop(success: Boolean, executor: TaskExecutor) {
                        Schedulers.androidUIThread().execute {
                            isInstalling = false
                            if (success) {
                                profile.repository.refreshVersions()
                                profile.setSelectedVersion(name)
                                if (selectedLoader != LoaderType.VANILLA && selectedLoader != LoaderType.OPTIFINE) {
                                    File(profile.repository.getRunDirectory(name), "mods").mkdirs()
                                }
                                FCLAlertDialog.Builder(context)
                                    .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                                    .setCancelable(false)
                                    .setMessage(context.getString(R.string.install_success))
                                    .setNegativeButton(
                                        context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                                        onComplete
                                    )
                                    .create()
                                    .show()
                            } else {
                                val ex = executor.exception
                                if (ex != null) {
                                    VersionInstallInfoPage.alertFailureMessage(context, ex) {}
                                }
                            }
                        }
                    }
                })
                dialog.setExecutor(executor, true)
                dialog.show()
                executor.start()
            }
        }
    }
}
