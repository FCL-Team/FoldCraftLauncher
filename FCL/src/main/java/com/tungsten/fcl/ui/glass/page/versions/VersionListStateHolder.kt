package com.tungsten.fcl.ui.glass.page.versions

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.JsonParseException
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.version.VersionListItem
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.fakefx.beans.binding.Bindings
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import java.util.stream.Collectors

class VersionListStateHolder {
    var query by mutableStateOf("")
    var selectedCategory by mutableStateOf(VersionCategory.ALL)
    var isLoading by mutableStateOf(false)
    val versions = mutableStateListOf<VersionListItem>()
    var selectedProfile by mutableStateOf(Profiles.getSelectedProfile())

    fun refresh() {
        isLoading = true
        val profile = selectedProfile
        Task.runAsync {
            profile.repository.refreshVersionsAsync().start()
        }.start()
    }

    suspend fun loadVersions(context: Context, profile: Profile) {
        if (profile != selectedProfile) return
        isLoading = true
        val repository = profile.repository
        val loaded = withContext(Dispatchers.IO) {
            repository.displayVersions
                .parallel()
                .map { version: Version ->
                    val game = profile.repository.getGameVersion(version.id)
                    val libraries = StringBuilder(game.orElse(context.getString(R.string.message_unknown)))
                    val analyzer = LibraryAnalyzer.analyze(
                        profile.repository.getResolvedPreservingPatchesVersion(version.id),
                        game.orElse(null)
                    )
                    for (mark in analyzer) {
                        val libraryId = mark.libraryId
                        val libraryVersion = mark.libraryVersion
                        if (libraryId == LibraryAnalyzer.LibraryType.MINECRAFT.patchId) continue
                        val resName = "install_installer_" + libraryId.replace("-", "_")
                        if (AndroidUtils.hasStringId(context, resName)) {
                            libraries.append(", ")
                                .append(AndroidUtils.getLocalizedText(context, resName))
                            if (libraryVersion != null) {
                                libraries.append(": ")
                                    .append(libraryVersion.replace(("(?i)$libraryId").toRegex(), ""))
                            }
                        }
                    }
                    var tag: String? = null
                    try {
                        val config: ModpackConfiguration<*>? =
                            profile.repository.readModpackConfiguration<Any?>(version.id)
                        if (config != null) tag = config.version
                    } catch (e: IOException) {
                        Logging.LOG.log(java.util.logging.Level.WARNING, "Failed to read modpack configuration from $version", e)
                    } catch (e: JsonParseException) {
                        Logging.LOG.log(java.util.logging.Level.WARNING, "Failed to read modpack configuration from $version", e)
                    }
                    VersionListItem(
                        profile,
                        version.id,
                        libraries.toString(),
                        tag,
                        repository.getVersionIconImage(version.id)
                    )
                }
                .collect(Collectors.toList())
        }
        if (profile != selectedProfile) return
        versions.clear()
        versions.addAll(loaded)
        versions.forEach { item ->
            item.selectedProperty().bind(
                Bindings.createBooleanBinding({
                    profile.selectedVersionProperty().get() == item.version
                }, profile.selectedVersionProperty())
            )
        }
        isLoading = false
    }

    fun filteredVersions(): List<VersionListItem> {
        var list = versions.toList()
        val q = query.trim().lowercase(Locale.getDefault())
        if (q.isNotEmpty()) {
            list = list.filter { it.version.lowercase(Locale.getDefault()).contains(q) }
        }
        return when (selectedCategory) {
            VersionCategory.ALL -> list
            VersionCategory.FABRIC -> list.filter { it.libraries.contains("Fabric") }
            VersionCategory.FORGE -> list.filter { it.libraries.contains("Forge") && !it.libraries.contains("NeoForge") }
            VersionCategory.NEOFORGE -> list.filter { it.libraries.contains("NeoForge") }
            VersionCategory.OTHER -> list.filter {
                !it.libraries.contains("Fabric") && !it.libraries.contains("Forge") && !it.libraries.contains("NeoForge")
            }
        }
    }

    fun launchVersion(context: Context, item: VersionListItem) {
        Versions.launch(context, item.profile, item.version)
    }

    fun renameVersion(context: Context, item: VersionListItem) {
        Versions.renameVersion(context, item.profile, item.version)
    }

    fun deleteVersion(context: Context, item: VersionListItem) {
        Versions.deleteVersion(context, item.profile, item.version)
    }

    fun duplicateVersion(context: Context, item: VersionListItem) {
        Versions.duplicateVersion(context, item.profile, item.version)
    }
}

enum class VersionCategory {
    ALL, FABRIC, FORGE, NEOFORGE, OTHER
}
