@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.tungsten.fclcore.mod.modinfo

import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.FileUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import java.io.IOException
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class ForgeOldModMetadata(
    val modid: String = "",
    val name: String = "",
    val description: String = "",
    val author: String = "",
    val version: String = "",
    val logoFile: String = "",
    val mcversion: String = "",
    val url: String = "",
    val updateUrl: String = "",
    val credits: String = "",
    val authorList: List<String> = emptyList(),
    val authors: List<String> = emptyList()
) {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun fromFile(modManager: ModManager, modFile: Path, fs: FileSystem): LocalModFile {
            val mcmod = fs.getPath("mcmod.info")
            if (Files.notExists(mcmod))
                throw IOException("File $modFile is not a Forge mod.")

            // mcmod.info 顶层兼容数组与 {modList: [...]} 对象两种形态，取第一条
            val element = MOD_METADATA_JSON.parseToJsonElement(FileUtils.readText(mcmod))
            val metadata: ForgeOldModMetadata = when (element) {
                is JsonArray -> MOD_METADATA_JSON.decodeFromJsonElement<List<ForgeOldModMetadata>>(element).firstOrNull()
                else -> MOD_METADATA_JSON.decodeFromJsonElement<ForgeOldModMetadataLst>(element).modList.firstOrNull()
            } ?: throw IOException("Mod $modFile `mcmod.info` is malformed")

            var authors = metadata.author
            if (authors.isBlank() && metadata.authors.isNotEmpty())
                authors = metadata.authors.joinToString(", ")
            if (authors.isBlank() && metadata.authorList.isNotEmpty())
                authors = metadata.authorList.joinToString(", ")
            if (authors.isBlank())
                authors = metadata.credits

            return LocalModFile(
                modManager, modManager.getLocalMod(metadata.modid, ModLoaderType.FORGE), modFile,
                metadata.name, LocalModFile.Description(metadata.description),
                authors, metadata.version, metadata.mcversion,
                if (metadata.url.isBlank()) metadata.updateUrl else metadata.url,
                metadata.logoFile
            )
        }
    }
}
