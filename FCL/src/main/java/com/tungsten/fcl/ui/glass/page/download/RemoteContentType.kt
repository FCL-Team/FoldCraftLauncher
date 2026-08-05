package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.download.common.DownloadAddonDialog
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.NetworkUtils
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File
import java.nio.file.Path

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

    fun downloadTask(
        context: Context,
        version: String,
        remoteVersion: RemoteMod.Version
    ): Task<File> {
        val dest = File.createTempFile("fcl_download", "_" + remoteVersion.file.filename, context.cacheDir)
        return Task.composeAsync {
            val task = FileDownloadTask(
                NetworkUtils.toURL(remoteVersion.file.url),
                dest,
                remoteVersion.file.integrityCheck
            )
            task.setName(remoteVersion.name)
            task
        }.thenApplyAsync { dest }
    }

    fun install(
        context: Context,
        parent: FCLUILayout?,
        profile: Profile,
        version: String,
        remoteVersion: RemoteMod.Version,
        file: File
    ) {
        when (this) {
            MOD -> installToSubdirectory(context, profile, version, remoteVersion, file, "mods")
            MODPACK -> Versions.downloadModpackImpl(context, parent, profile, remoteVersion)
            RESOURCE_PACK -> installToSubdirectory(context, profile, version, remoteVersion, file, "resourcepacks")
            SHADER_PACK -> installToSubdirectory(context, profile, version, remoteVersion, file, "shaderpacks")
        }
    }

    fun supportsModLoader(): Boolean = this == MOD

    companion object {
        private fun installToSubdirectory(
            context: Context,
            profile: Profile,
            version: String,
            remoteVersion: RemoteMod.Version,
            file: File,
            subdirectoryName: String
        ) {
            val actualVersion = version.ifBlank { profile.selectedVersion ?: "" }
            val runDirectory: Path = if (profile.repository.hasVersion(actualVersion)) {
                profile.repository.getRunDirectory(actualVersion).toPath()
            } else {
                profile.repository.baseDirectory.toPath()
            }

            DownloadAddonDialog(context, remoteVersion.file.filename) { name ->
                try {
                    val dest = runDirectory.resolve(subdirectoryName).resolve(name).toFile()
                    dest.parentFile?.mkdirs()
                    FileUtils.moveFile(file, dest)
                    Toast.makeText(context, context.getString(R.string.install_success), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }.show()
        }
    }
}
