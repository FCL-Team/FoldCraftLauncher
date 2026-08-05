package com.tungsten.fcl.ui.glass.page.version

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.StringUtils
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale

class VersionModListStateHolder(
    private val profile: Profile,
    private val versionId: String
) {
    var isLoading by mutableStateOf(false)
        private set
    var isModded by mutableStateOf(false)
        private set
    var query by mutableStateOf("")
    var showEnabled by mutableStateOf(true)
    var showDisabled by mutableStateOf(true)
    var editMode by mutableStateOf(false)
    val items = mutableStateListOf<ModListItem>()
    val selected = mutableStateListOf<ModListItem>()

    init {
        checkModded()
        if (isModded) loadMods()
    }

    private fun checkModded() {
        val repository = profile.getRepository()
        val resolved = repository.getResolvedPreservingPatchesVersion(versionId)
        val analyzer = LibraryAnalyzer.analyze(
            resolved,
            repository.getGameVersion(resolved).orElse(null)
        )
        isModded = analyzer.hasModLoader()
    }

    fun loadMods() {
        if (!isModded) return
        isLoading = true
        Task.runAsync {
            try {
                val repository = profile.getRepository()
                val manager = repository.getModManager(versionId)
                manager.refreshMods()
                val loaded = manager.getMods().map { it.toListItem() }
                Schedulers.androidUIThread().execute {
                    items.clear()
                    items.addAll(loaded)
                    selected.clear()
                    editMode = false
                    isLoading = false
                }
            } catch (e: IOException) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                }
            }
        }.start()
    }

    fun filteredItems(): List<ModListItem> {
        val q = query.trim().lowercase(Locale.getDefault())
        return items.filter { item ->
            val active = item.mod.isActive
            val matchesFilter = (showEnabled && active) || (showDisabled && !active)
            val matchesQuery = q.isEmpty() ||
                    item.title.lowercase(Locale.getDefault()).contains(q) ||
                    item.mod.fileName.lowercase(Locale.getDefault()).contains(q)
            matchesFilter && matchesQuery
        }
    }

    fun toggleActive(item: ModListItem) {
        Task.runAsync {
            try {
                item.mod.setActive(!item.mod.isActive)
                Schedulers.androidUIThread().execute {
                    item.isActive = item.mod.isActive
                }
            } catch (e: IOException) {
                // Ignore: failed to toggle mod state.
            }
        }.start()
    }

    fun delete(item: ModListItem, context: Context) {
        Task.runAsync {
            try {
                val manager = profile.getRepository().getModManager(versionId)
                manager.removeMods(item.mod)
                Schedulers.androidUIThread().execute {
                    items.remove(item)
                    selected.remove(item)
                }
            } catch (e: IOException) {
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, R.string.message_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun deleteSelected(context: Context) {
        val toDelete = selected.toList()
        if (toDelete.isEmpty()) return
        Task.runAsync {
            try {
                val manager = profile.getRepository().getModManager(versionId)
                manager.removeMods(*toDelete.map { it.mod }.toTypedArray())
                Schedulers.androidUIThread().execute {
                    items.removeAll(toDelete)
                    selected.clear()
                    editMode = false
                }
            } catch (e: IOException) {
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, R.string.message_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun selectAll() {
        selected.clear()
        selected.addAll(filteredItems())
    }

    fun invertSelection() {
        val filtered = filteredItems()
        val newSelection = filtered.filter { !selected.contains(it) }
        selected.clear()
        selected.addAll(newSelection)
    }

    fun addMods(context: Context, uris: List<Uri>) {
        if (uris.isEmpty()) return
        isLoading = true
        Task.runAsync {
            val manager = profile.getRepository().getModManager(versionId)
            val modsDir = manager.modsDirectory
            val succeeded = mutableListOf<String>()
            val failed = mutableListOf<String>()
            uris.forEach { uri ->
                val name = getDisplayName(context, uri) ?: uri.toString()
                if (!isModFileName(name)) {
                    failed.add(name)
                    return@forEach
                }
                try {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        Files.createDirectories(modsDir)
                        val target = modsDir.resolve(name)
                        Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
                        succeeded.add(name)
                    } ?: failed.add(name)
                } catch (e: Exception) {
                    failed.add(name)
                }
            }
            Schedulers.androidUIThread().execute {
                val messages = mutableListOf<String>()
                if (succeeded.isNotEmpty()) {
                    messages.add(context.getString(R.string.mods_add_success, succeeded.joinToString(", ")))
                }
                if (failed.isNotEmpty()) {
                    messages.add(context.getString(R.string.mods_add_failed, failed.joinToString(", ")))
                }
                if (messages.isNotEmpty()) {
                    Toast.makeText(context, messages.joinToString("\n"), Toast.LENGTH_LONG).show()
                }
                loadMods()
            }
        }.start()
    }

    private fun getDisplayName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) return cursor.getString(index)
                }
            }
        }
        return uri.path?.let { java.io.File(it).name }
    }

    private fun isModFileName(name: String): Boolean {
        val lower = name.lowercase(Locale.getDefault())
        return lower.endsWith(".jar") || lower.endsWith(".zip") || lower.endsWith(".litemod")
    }

    private fun LocalModFile.toListItem(): ModListItem {
        val titleBuilder = StringBuilder(name)
        if (StringUtils.isNotBlank(version)) {
            titleBuilder.append(" ").append(version)
        }
        val subtitleBuilder = StringBuilder(fileName)
        if (StringUtils.isNotBlank(gameVersion)) {
            subtitleBuilder.append(", ").append("Game Version: ").append(gameVersion)
        }
        if (StringUtils.isNotBlank(authors)) {
            subtitleBuilder.append(", ").append("Author(s): ").append(authors)
        }
        return ModListItem(
            mod = this,
            title = titleBuilder.toString(),
            subtitle = subtitleBuilder.toString(),
            tag = modLoaderType.displayName(),
            isActive = isActive
        )
    }

    private fun ModLoaderType.displayName(): String = when (this) {
        ModLoaderType.FORGE -> "Forge"
        ModLoaderType.NEO_FORGED -> "NeoForge"
        ModLoaderType.FABRIC -> "Fabric"
        ModLoaderType.LITE_LOADER -> "LiteLoader"
        ModLoaderType.QUILT -> "Quilt"
        else -> ""
    }
}

data class ModListItem(
    val mod: LocalModFile,
    var title: String,
    var subtitle: String,
    var tag: String,
    var isActive: Boolean = mod.isActive
)