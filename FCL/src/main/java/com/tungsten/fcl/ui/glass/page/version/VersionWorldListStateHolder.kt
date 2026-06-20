package com.tungsten.fcl.ui.glass.page.version

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.game.World
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale

class VersionWorldListStateHolder(
    private val profile: Profile,
    private val versionId: String
) {
    var isLoading by mutableStateOf(false)
        private set
    var showAll by mutableStateOf(false)
    var gameVersion by mutableStateOf<String?>(null)
        private set
    val worlds = mutableStateListOf<World>()
    var query by mutableStateOf("")

    init {
        refresh()
    }

    fun refresh() {
        isLoading = true
        Task.runAsync {
            try {
                val repository = profile.getRepository()
                val gv = repository.getGameVersion(versionId).orElse(null)
                val savesDir = repository.getRunDirectory(versionId).toPath().resolve("saves")
                val list = World.getWorlds(savesDir)
                Schedulers.androidUIThread().execute {
                    gameVersion = gv
                    worlds.clear()
                    worlds.addAll(list)
                    isLoading = false
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                }
            }
        }.start()
    }

    fun filteredWorlds(): List<World> {
        var list = if (showAll) worlds else worlds.filter {
            it.gameVersion == null || it.gameVersion == gameVersion
        }
        val q = query.trim().lowercase(Locale.getDefault())
        if (q.isNotEmpty()) {
            list = list.filter {
                it.worldName.lowercase(Locale.getDefault()).contains(q) ||
                        it.fileName.lowercase(Locale.getDefault()).contains(q)
            }
        }
        return list
    }

    fun delete(context: Context, world: World) {
        Task.runAsync {
            try {
                FileUtils.forceDelete(world.file.toFile())
                Schedulers.androidUIThread().execute { refresh() }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, R.string.message_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    fun importWorld(context: Context, sourceUri: Uri, name: String) {
        isLoading = true
        Task.runAsync {
            try {
                val repository = profile.getRepository()
                val savesDir = repository.getRunDirectory(versionId).toPath().resolve("saves")
                val tempZip = File(context.cacheDir, "import_world.zip").toPath()
                Files.deleteIfExists(tempZip)
                context.contentResolver.openInputStream(sourceUri)?.use { input ->
                    Files.copy(input, tempZip, StandardCopyOption.REPLACE_EXISTING)
                } ?: throw IOException("Failed to open input stream")
                val world = World(tempZip)
                world.install(savesDir, name)
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, R.string.message_success, Toast.LENGTH_SHORT).show()
                    refresh()
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                    val message = when (e) {
                        is java.nio.file.FileAlreadyExistsException -> context.getString(R.string.world_import_already_exists)
                        is IOException -> context.getString(R.string.world_import_failed, e.localizedMessage ?: "")
                        else -> context.getString(R.string.world_import_failed, e.localizedMessage ?: "")
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    fun exportWorld(context: Context, world: World, fileName: String, treeUri: Uri) {
        isLoading = true
        Task.runAsync {
            try {
                val tempZip = File(context.cacheDir, "export_world.zip").toPath()
                Files.deleteIfExists(tempZip)
                world.export(tempZip, world.worldName)

                val finalName = if (fileName.lowercase(Locale.getDefault()).endsWith(".zip")) fileName else "$fileName.zip"
                val docUri = DocumentsContract.createDocument(
                    context.contentResolver,
                    treeUri,
                    "application/zip",
                    finalName
                ) ?: throw IOException("Failed to create export document")

                context.contentResolver.openOutputStream(docUri)?.use { output ->
                    Files.copy(tempZip, output)
                } ?: throw IOException("Failed to open output stream")

                Schedulers.androidUIThread().execute {
                    isLoading = false
                    Toast.makeText(context, R.string.message_success, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Schedulers.androidUIThread().execute {
                    isLoading = false
                    Toast.makeText(context, R.string.message_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}