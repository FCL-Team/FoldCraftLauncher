@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.tungsten.fclcore.mod.modinfo

import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.ModManager
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import com.tungsten.fclcore.util.io.FileUtils
import kotlinx.serialization.json.decodeFromStream
import java.io.IOException
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class FabricModMetadata(
    val id: String = "",
    val name: String = "",
    val version: String = "",
    val icon: String = "",
    val description: String = "",
    @Serializable(with = FabricModAuthorListSerializer::class)
    val authors: List<FabricModAuthor> = emptyList(),
    val contact: Map<String, String> = emptyMap()
) {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun fromFile(modManager: ModManager, modFile: Path, fs: FileSystem): LocalModFile {
            val mcmod = fs.getPath("fabric.mod.json")
            if (Files.notExists(mcmod))
                throw IOException("File $modFile is not a Fabric mod.")
            val metadata: FabricModMetadata = MOD_METADATA_JSON.decodeFromString(FileUtils.readText(mcmod))
            val authors = metadata.authors.joinToString(", ") { it.name }
            return LocalModFile(
                modManager, modManager.getLocalMod(metadata.id, ModLoaderType.FABRIC), modFile,
                metadata.name, LocalModFile.Description(metadata.description),
                authors, metadata.version, "", metadata.contact["homepage"] ?: "", metadata.icon
            )
        }
    }
}

@Serializable
data class FabricModAuthor(val name: String = "")

/** authors 数组元素兼容字符串与 {"name": ...} 对象两种 JSON 形态 */
internal object FabricModAuthorSerializer : KSerializer<FabricModAuthor> {
    private val elementSerializer = JsonElement.serializer()

    override val descriptor: SerialDescriptor = elementSerializer.descriptor

    override fun deserialize(decoder: Decoder): FabricModAuthor {
        val element = decoder.decodeSerializableValue(elementSerializer)
        return when (element) {
            is JsonPrimitive -> FabricModAuthor(element.content)
            is JsonObject -> FabricModAuthor((element["name"] as? JsonPrimitive)?.content ?: "")
            else -> FabricModAuthor("")
        }
    }

    override fun serialize(encoder: Encoder, value: FabricModAuthor) {
        encoder.encodeSerializableValue(elementSerializer, JsonPrimitive(value.name))
    }
}

internal object FabricModAuthorListSerializer : KSerializer<List<FabricModAuthor>> by ListSerializer(FabricModAuthorSerializer)
