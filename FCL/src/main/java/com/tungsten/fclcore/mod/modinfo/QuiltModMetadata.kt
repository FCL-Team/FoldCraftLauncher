@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.tungsten.fclcore.mod.modinfo

import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.ModManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import com.tungsten.fclcore.util.tree.ZipFileTree
import java.io.IOException
import java.nio.file.Path

@Serializable
data class QuiltModMetadata(
    @SerialName("schema_version")
    val schemaVersion: Int = 0,
    @SerialName("quilt_loader")
    val quiltLoader: QuiltLoader
) {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun fromFile(modManager: ModManager, modFile: Path, tree: ZipFileTree): LocalModFile {
            val path = tree.getEntry("quilt.mod.json")
                ?: throw IOException("File $modFile is not a Quilt mod.")

            val root: QuiltModMetadata = MOD_METADATA_JSON.decodeFromString(tree.readTextEntry(path))
            if (root.schemaVersion != 1)
                throw IOException("File $modFile is not a supported Quilt mod.")

            val loader = root.quiltLoader
            val metadata = loader.metadata
            val contributors = metadata.contributors.entries.joinToString(", ") { (name, value) ->
                "$name (${value.jsonPrimitive.content})"
            }
            return LocalModFile(
                modManager,
                modManager.getLocalMod(loader.id, ModLoaderType.QUILT),
                modFile,
                metadata.name,
                LocalModFile.Description(metadata.description),
                contributors,
                loader.version,
                "",
                (metadata.contact["homepage"] as? JsonPrimitive)?.content ?: "",
                metadata.icon
            )
        }
    }
}

@Serializable
data class QuiltLoader(
    val id: String = "",
    val version: String = "",
    val metadata: QuiltMetadata = QuiltMetadata()
)

@Serializable
data class QuiltMetadata(
    val name: String = "",
    val description: String = "",
    val contributors: Map<String, JsonElement> = emptyMap(),
    val icon: String = "",
    val contact: Map<String, JsonElement> = emptyMap()
)
