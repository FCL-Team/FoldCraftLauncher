package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.download.common.DownloadAddonDialog
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.util.io.NetworkUtils
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.component.view.FCLAlertDialog
import java.nio.file.Path
import java.util.concurrent.CancellationException

enum class RemoteContentType {
    MOD,
    MODPACK,
    RESOURCE_PACK,
    SHADER_PACK;

    fun titleRes(): Int = when (this) {
        MOD -> R.string.mods
        MODPACK -> R.string.modpack
        RESOURCE_PACK -> R.string.resourcepack
        SHADER_PACK -> R.string.shaderpack
    }

    fun iconRes(): Int = when (this) {
        MOD -> R.drawable.ic_outline_extension_24
        MODPACK -> R.drawable.ic_baseline_application_24
        RESOURCE_PACK -> R.drawable.ic_baseline_texture_24
        SHADER_PACK -> R.drawable.ic_baseline_tune_24
    }

    fun getRepository(sourceName: String, context: Context): RemoteModRepository {
        val modrinthName = context.getString(R.string.mods_modrinth)
        return when (this) {
            MOD -> if (sourceName == modrinthName) ModrinthRemoteModRepository.MODS else CurseForgeRemoteModRepository.MODS
            MODPACK -> if (sourceName == modrinthName) ModrinthRemoteModRepository.MODPACKS else CurseForgeRemoteModRepository.MODPACKS
            RESOURCE_PACK -> if (sourceName == modrinthName) ModrinthRemoteModRepository.RESOURCE_PACKS else CurseForgeRemoteModRepository.RESOURCE_PACKS
            SHADER_PACK -> if (sourceName == modrinthName) ModrinthRemoteModRepository.SHADER_PACKS else CurseForgeRemoteModRepository.SHADER_PACKS
        }
    }

    fun supportedSources(context: Context): List<String> {
        return listOf(context.getString(R.string.mods_modrinth), context.getString(R.string.mods_curseforge))
    }

    fun defaultSource(context: Context): String {
        return context.getString(R.string.mods_modrinth)
    }

    fun sortType(sourceName: String, context: Context): RemoteModRepository.SortType {
        val modrinthName = context.getString(R.string.mods_modrinth)
        return if (sourceName == modrinthName) RemoteModRepository.SortType.NAME else RemoteModRepository.SortType.POPULARITY
    }

    fun installCallback(context: Context, parent: FCLUILayout?): (Profile, String, RemoteMod.Version) -> Unit {
        return { profile, version, file ->
            when (this) {
                MOD -> downloadToSubdirectory(context, profile, version, file, "mods")
                MODPACK -> Versions.downloadModpackImpl(context, parent, profile, file)
                RESOURCE_PACK -> downloadToSubdirectory(context, profile, version, file, "resourcepacks")
                SHADER_PACK -> downloadToSubdirectory(context, profile, version, file, "shaderpacks")
            }
        }
    }

    fun supportsModLoader(): Boolean = this == MOD

    companion object {
        private fun downloadToSubdirectory(
            context: Context,
            profile: Profile,
            version: String,
            file: RemoteMod.Version,
            subdirectoryName: String
        ) {
            val actualVersion = version.ifBlank { profile.selectedVersion ?: "" }
            val runDirectory: Path = if (profile.repository.hasVersion(actualVersion)) {
                profile.repository.getRunDirectory(actualVersion).toPath()
            } else {
                profile.repository.baseDirectory.toPath()
            }

            DownloadAddonDialog(context, file.file.filename) { name ->
                val dest = runDirectory.resolve(subdirectoryName).resolve(name)

                val taskDialog = TaskDialog(context, TaskCancellationAction(AppCompatDialog::dismiss))
                taskDialog.setTitle(context.getString(R.string.message_downloading))
                Schedulers.androidUIThread().execute {
                    val executor = Task.composeAsync {
                        val task = FileDownloadTask(NetworkUtils.toURL(file.file.url), dest.toFile())
                        task.setName(file.name)
                        task
                    }.whenComplete(Schedulers.androidUIThread()) { exception ->
                        if (exception != null) {
                            if (exception is CancellationException) {
                                Toast.makeText(context, context.getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show()
                            } else {
                                val builder = FCLAlertDialog.Builder(context)
                                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                                builder.setCancelable(false)
                                builder.setTitle(context.getString(R.string.install_failed_downloading))
                                builder.setMessage(DownloadProviders.localizeErrorMessage(context, exception))
                                builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                                builder.create().show()
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.install_success), Toast.LENGTH_SHORT).show()
                        }
                    }.executor()
                    taskDialog.setExecutor(executor)
                    taskDialog.show()
                    executor.start()
                }
            }.show()
        }
    }
}
