package com.tungsten.fcl.ui.glass.page.version

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.CompressingUtils
import java.io.IOException
import java.nio.file.Files

class ModInfoStateHolder(
    private val profile: Profile,
    private val versionId: String,
    private val modFileName: String
) {
    var mod by mutableStateOf<LocalModFile?>(null)
        private set
    var icon by mutableStateOf<Bitmap?>(null)
        private set
    var isLoading by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        load()
    }

    private fun load() {
        Task.runAsync {
            try {
                val manager = profile.getRepository().getModManager(versionId)
                manager.refreshMods()
                val found = manager.getMods().find { it.fileName == modFileName }
                Schedulers.androidUIThread().execute {
                    mod = found
                    isLoading = false
                    if (found == null) {
                        errorMessage = "Mod not found"
                    }
                }
                found?.let { loadIcon(it) }
            } catch (e: IOException) {
                Schedulers.androidUIThread().execute {
                    errorMessage = e.message
                    isLoading = false
                }
            }
        }.start()
    }

    private fun loadIcon(mod: LocalModFile) {
        val logoPath = mod.logoPath
        if (logoPath.isNullOrBlank()) return
        Task.runAsync {
            try {
                CompressingUtils.createReadOnlyZipFileSystem(mod.file).use { fs ->
                    val path = fs.getPath(logoPath)
                    if (Files.exists(path)) {
                        val bytes = Files.readAllBytes(path)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        Schedulers.androidUIThread().execute {
                            icon = bitmap
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore icon loading failures.
            }
        }.start()
    }

    fun loaderDisplayName(): String = when (mod?.modLoaderType) {
        ModLoaderType.FORGE -> "Forge"
        ModLoaderType.NEO_FORGED -> "NeoForge"
        ModLoaderType.FABRIC -> "Fabric"
        ModLoaderType.LITE_LOADER -> "LiteLoader"
        ModLoaderType.QUILT -> "Quilt"
        else -> ""
    }
}