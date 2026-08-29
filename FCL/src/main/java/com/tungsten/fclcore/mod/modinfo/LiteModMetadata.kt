@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.tungsten.fclcore.mod.modinfo

import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.util.io.FileUtils
import kotlinx.serialization.Serializable
import java.io.IOException
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class LiteModMetadata(
    val name: String = "",
    val version: String = "",
    val mcversion: String = "",
    val revision: String = "",
    val author: String = "",
    val classTransformerClasses: List<String> = emptyList(),
    val description: String = "",
    val modpackName: String = "",
    val modpackVersion: String = "",
    val checkUpdateUrl: String = "",
    val updateURI: String = ""
) {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun fromFile(modManager: ModManager, modFile: Path, fs: FileSystem): LocalModFile {
            val path = fs.getPath("litemod.json")
            if (Files.notExists(path))
                throw IOException("File $modFile is not a LiteLoader mod.")
            val metadata: LiteModMetadata = MOD_METADATA_JSON.decodeFromString(FileUtils.readText(path))
            return LocalModFile(
                modManager, modManager.getLocalMod(metadata.name, ModLoaderType.LITE_LOADER), modFile,
                metadata.name, LocalModFile.Description(metadata.description), metadata.author,
                metadata.version, metadata.mcversion, metadata.updateURI, ""
            )
        }
    }
}
