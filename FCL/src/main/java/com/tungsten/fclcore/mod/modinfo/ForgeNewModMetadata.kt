@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.tungsten.fclcore.mod.modinfo

import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.CompressingUtils
import com.tungsten.fclcore.util.io.FileUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.tomlj.Toml
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable
import java.io.IOException
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.StringJoiner
import java.util.jar.Attributes
import java.util.jar.Manifest

@Serializable
data class ForgeNewModMetadata(
    val modLoader: String = "",
    val loaderVersion: String = "",
    val logoFile: String = "",
    val license: String = "",
    val mods: List<Mod> = emptyList()
) {
    @Serializable
    data class Mod(
        val modId: String = "",
        val version: String = "",
        val displayName: String = "",
        val side: String = "",
        val displayURL: String = "",
        @Serializable(with = ModAuthorsSerializer::class)
        val authors: String = "",
        val description: String = ""
    )

    companion object {
        private val LOG = Logging.LOG

        @JvmStatic
        @Throws(IOException::class)
        fun fromForgeFile(modManager: ModManager, modFile: Path, fs: FileSystem): LocalModFile =
            fromFile(modManager, modFile, fs, ModLoaderType.FORGE)

        @JvmStatic
        @Throws(IOException::class)
        fun fromNeoForgeFile(modManager: ModManager, modFile: Path, fs: FileSystem): LocalModFile =
            fromFile(modManager, modFile, fs, ModLoaderType.NEO_FORGED)

        private fun fromFile(
            modManager: ModManager,
            modFile: Path,
            fs: FileSystem,
            modLoaderType: ModLoaderType
        ): LocalModFile {
            if (modLoaderType != ModLoaderType.FORGE && modLoaderType != ModLoaderType.NEO_FORGED) {
                throw IOException("Invalid mod loader: $modLoaderType")
            }

            if (modLoaderType == ModLoaderType.NEO_FORGED) {
                try {
                    return fromFile0("META-INF/neoforge.mods.toml", modLoaderType, modManager, modFile, fs)
                } catch (ignored: Exception) {
                }
            }

            try {
                return fromFile0("META-INF/mods.toml", modLoaderType, modManager, modFile, fs)
            } catch (ignored: Exception) {
            }

            try {
                return fromEmbeddedMod(modManager, modFile, fs, modLoaderType)
            } catch (ignored: Exception) {
            }

            throw IOException("File $modFile is not a Forge 1.13+ or NeoForge mod.")
        }

        private fun fromFile0(
            tomlPath: String,
            modLoaderType: ModLoaderType,
            modManager: ModManager,
            modFile: Path,
            fs: FileSystem
        ): LocalModFile {
            val modToml = fs.getPath(tomlPath)
            if (Files.notExists(modToml))
                throw IOException("File $modFile is not a Forge 1.13+ or NeoForge mod.")
            val tomlParseResult: TomlParseResult = Toml.parse(FileUtils.readText(modToml))
            if (tomlParseResult.hasErrors()) {
                val ioException = IOException("Mod $modFile $tomlPath is malformed..")
                tomlParseResult.errors().forEach(ioException::addSuppressed)
                throw ioException
            }
            val metadata = MOD_METADATA_JSON.decodeFromString<ForgeNewModMetadata>(tomlParseResult.toJson())
            if (metadata.mods.isEmpty())
                throw IOException("Mod $modFile $tomlPath is malformed..")
            val mod = metadata.mods.first()
            val manifestMF = fs.getPath("META-INF/MANIFEST.MF")
            var jarVersion: String? = ""
            if (Files.exists(manifestMF)) {
                try {
                    Files.newInputStream(manifestMF).use { input ->
                        jarVersion = Manifest(input).mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION)
                    }
                } catch (e: IOException) {
                    LOG.warning("Failed to parse MANIFEST.MF in file $modFile")
                }
            }

            val type = analyzeLoader(tomlParseResult, mod.modId, modLoaderType)

            return LocalModFile(
                modManager, modManager.getLocalMod(mod.modId, type), modFile, mod.displayName,
                LocalModFile.Description(mod.description), mod.authors,
                if (jarVersion != null) mod.version.replace("\${file.jarVersion}", jarVersion) else mod.version,
                "",
                mod.displayURL,
                metadata.logoFile
            )
        }

        private fun fromEmbeddedMod(
            modManager: ModManager,
            modFile: Path,
            fs: FileSystem,
            modLoaderType: ModLoaderType
        ): LocalModFile {
            val manifestFile = fs.getPath("META-INF/MANIFEST.MF")
            if (Files.notExists(manifestFile))
                throw IOException("Missing MANIFEST.MF in file $modFile")

            val manifest: Manifest = Files.newInputStream(manifestFile).use { Manifest(it) }

            var embeddedModFiles: List<Path> = emptyList()

            val embeddedDependenciesMod = manifest.mainAttributes.getValue("Embedded-Dependencies-Mod")
            if (embeddedDependenciesMod != null) {
                val embeddedModFile = fs.getPath(embeddedDependenciesMod)
                if (Files.notExists(embeddedModFile)) {
                    LOG.warning("Missing embedded-dependencies-mod: $embeddedDependenciesMod")
                    throw IOException()
                }
                embeddedModFiles = listOf(embeddedModFile)
            } else {
                val jarInJarMetadata = fs.getPath("META-INF/jarjar/metadata.json")
                if (Files.exists(jarInJarMetadata)) {
                    val metadata: JarInJarMetadata =
                        MOD_METADATA_JSON.decodeFromString(FileUtils.readText(jarInJarMetadata))
                    embeddedModFiles = ArrayList()
                    for (jar in metadata.jars) {
                        val path = fs.getPath(jar.path)
                        if (Files.exists(path)) {
                            embeddedModFiles += path
                        } else {
                            LOG.warning("Missing embedded-dependencies-mod: ${jar.path}")
                        }
                    }
                }
            }

            if (embeddedModFiles.isEmpty()) {
                throw IOException("Missing embedded mods")
            }

            val tempFile = Files.createTempFile("hmcl-", ".zip")
            try {
                for (embeddedModFile in embeddedModFiles) {
                    Files.copy(embeddedModFile, tempFile, StandardCopyOption.REPLACE_EXISTING)
                    try {
                        CompressingUtils.createReadOnlyZipFileSystem(tempFile).use { embeddedFs ->
                            return fromFile(modManager, modFile, embeddedFs, modLoaderType)
                        }
                    } catch (ignored: Exception) {
                    }
                }
            } finally {
                Files.deleteIfExists(tempFile)
            }

            throw IOException()
        }

        private fun analyzeLoader(toml: TomlParseResult, modID: String, loader: ModLoaderType): ModLoaderType {
            var dependencies: List<Map<String, Any>>? = null
            try {
                val tomlArray = toml.getArray("dependencies.$modID")
                if (tomlArray != null) {
                    dependencies = tomlArray.toList().map { (it as TomlTable).toMap() }
                }
            } catch (ignored: ClassCastException) { // https://github.com/HMCL-dev/HMCL/issues/5068
            }

            if (dependencies == null) {
                try {
                    val tomlArray = toml.getArray("dependencies") // ??? I have no idea why some of the Forge mods use [[dependencies]]
                    if (tomlArray != null) {
                        dependencies = tomlArray.toList().map { (it as TomlTable).toMap() }
                    }
                } catch (e: ClassCastException) {
                    try {
                        val table = toml.getTable("dependencies") ?: return loader
                        val tomlArray = table.getArray(modID)
                        if (tomlArray != null) {
                            dependencies = tomlArray.toList().map { (it as TomlTable).toMap() }
                        }
                    } catch (ignored: Throwable) {
                    }
                }

                if (dependencies == null) {
                    return loader
                }
            }

            var result: ModLoaderType? = null
            loop@ for (dependency in dependencies) {
                when (dependency["modId"] as? String) {
                    "forge" -> {
                        result = ModLoaderType.FORGE
                        break@loop
                    }
                    "neoforge" -> {
                        result = ModLoaderType.NEO_FORGED
                        break@loop
                    }
                }
            }

            return if (result != null) {
                if (result != loader)
                    LOG.warning("Loader mismatch for mod $modID, found $result, expecting $loader")
                result
            } else {
                LOG.warning("Cannot determine the mod loader for mod $modID, expected $loader")
                loader
            }
        }
    }
}

/** mods.toml 的 authors 兼容字符串与字符串数组两种 JSON 形态 */
internal object ModAuthorsSerializer : KSerializer<String> {
    private val elementSerializer = JsonElement.serializer()

    override val descriptor: SerialDescriptor = elementSerializer.descriptor

    override fun deserialize(decoder: Decoder): String {
        val element = decoder.decodeSerializableValue(elementSerializer)
        return when (element) {
            is JsonPrimitive -> element.content
            is JsonArray -> {
                val joiner = StringJoiner(", ")
                for (child in element) {
                    if (child !is JsonPrimitive) {
                        return element.toString()
                    }
                    joiner.add(child.content)
                }
                joiner.toString()
            }
            else -> element.toString()
        }
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeSerializableValue(elementSerializer, JsonPrimitive(value))
    }
}

/** Jar-in-Jar 嵌入依赖元数据（META-INF/jarjar/metadata.json） */
@Serializable
internal data class JarInJarMetadata(val jars: List<EmbeddedJarMetadata> = emptyList())

@Serializable
internal data class EmbeddedJarMetadata(val path: String = "", val isObfuscated: Boolean = false)
