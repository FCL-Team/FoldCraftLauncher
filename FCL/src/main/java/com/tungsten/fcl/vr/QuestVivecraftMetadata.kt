package com.tungsten.fcl.vr

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.tomlj.Toml
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.jar.Manifest

/** 解析并校验 Vivecraft jar 的 mod 描述符（Fabric/Quilt/Forge/NeoForge）与 Android OpenXR 支持 */
internal object QuestVivecraftMetadata {
    const val OPENXR_CLASS = "org/vivecraft/client_vr/provider/openxr/MCOpenXR.class"
    private const val MAX_DESCRIPTOR_BYTES = 512 * 1024

    /** 校验 jar 满足目标实例的 MC/加载器版本依赖；不满足时抛 ServiceException(INCOMPATIBLE) */
    @JvmStatic
    @Throws(IOException::class)
    fun validate(jar: JarView, target: QuestVivecraftTarget): String {
        if (jar.entry(VivecraftInspector.ANDROID_LOADER_CLASS) == null || jar.entry(OPENXR_CLASS) == null) {
            throw incompatible("The JAR does not contain the Android VLoader and OpenXR provider.")
        }
        return try {
            val metadata: JsonObject = when (target.loader) {
                QuestVivecraftTarget.Loader.FABRIC -> {
                    val fabric = json(jar, "fabric.mod.json")
                    requireVivecraft(fabric, "id")
                    val depends = fabric.getAsJsonObject("depends")
                        ?: throw incompatible("Missing Fabric dependencies.")
                    requireVersion("minecraft", target.minecraftVersion, depends.get("minecraft"))
                    requireVersion("fabricloader", target.loaderVersion, depends.get("fabricloader"))
                    fabric
                }

                QuestVivecraftTarget.Loader.QUILT -> {
                    val quiltLoader = json(jar, "quilt.mod.json").getAsJsonObject("quilt_loader")
                        ?: throw incompatible("Missing Quilt loader descriptor.")
                    requireVivecraft(quiltLoader, "id")
                    val depends = quiltLoader.getAsJsonArray("depends")
                    requireVersion("minecraft", target.minecraftVersion, dependency(depends, "minecraft", "id", "versions"))
                    requireVersion("quilt_loader", target.loaderVersion, dependency(depends, "quilt_loader", "id", "versions"))
                    quiltLoader
                }

                QuestVivecraftTarget.Loader.FORGE, QuestVivecraftTarget.Loader.NEOFORGE -> {
                    val entry = if (target.loader == QuestVivecraftTarget.Loader.NEOFORGE &&
                        jar.entry("META-INF/neoforge.mods.toml") != null
                    ) "META-INF/neoforge.mods.toml" else "META-INF/mods.toml"
                    val parsed = Toml.parse(read(jar, entry))
                    if (parsed.hasErrors()) throw incompatible("Malformed Forge/NeoForge descriptor.")
                    val toml = JsonParser.parseString(parsed.toJson()).getAsJsonObject()
                    var mod: JsonObject? = null
                    toml.getAsJsonArray("mods")?.forEach { element ->
                        val candidate = element.asJsonObject
                        if (string(candidate, "modId") == "vivecraft") mod = candidate
                    }
                    val vivecraftMod = mod ?: throw incompatible("The mod ID is not vivecraft.")
                    val depends = toml.getAsJsonObject("dependencies")?.getAsJsonArray("vivecraft")
                    requireVersion("minecraft", target.minecraftVersion,
                        dependency(depends, "minecraft", "modId", "versionRange"))
                    val loader = if (target.loader == QuestVivecraftTarget.Loader.NEOFORGE) "neoforge" else "forge"
                    requireVersion(loader, target.loaderVersion, dependency(depends, loader, "modId", "versionRange"))
                    vivecraftMod
                }

                else -> throw incompatible("This instance has no supported mod loader.")
            }
            var version = string(metadata, "version")
            if (version.contains("\${file.jarVersion}")) {
                val manifest = read(jar, "META-INF/MANIFEST.MF")
                val implementation = Manifest(ByteArrayInputStream(
                    manifest.toByteArray(StandardCharsets.UTF_8))).mainAttributes.getValue("Implementation-Version")
                    ?: throw incompatible("Missing mod version in manifest.")
                version = version.replace("\${file.jarVersion}", implementation)
            }
            if (version.isEmpty() || version.length > 200) throw incompatible("Missing or invalid mod version.")
            version
        } catch (e: QuestVivecraftService.ServiceException) {
            throw e
        } catch (e: RuntimeException) {
            throw incompatible("Unreadable mod dependencies: " + e.javaClass.simpleName)
        }
    }

    /** jar 是否为 Vivecraft（无论哪个加载器的描述符），供已装模组扫描 */
    @JvmStatic
    fun containsVivecraft(jar: JarView): Boolean {
        jar.forEachEntry { name ->
            if (name.startsWith("org/vivecraft/") && name.endsWith(".class")) return true
        }
        try {
            if (jar.entry("fabric.mod.json") != null && string(json(jar, "fabric.mod.json"), "id") == "vivecraft") return true
            if (jar.entry("quilt.mod.json") != null) {
                val quiltLoader = json(jar, "quilt.mod.json").getAsJsonObject("quilt_loader")
                if (quiltLoader != null && string(quiltLoader, "id") == "vivecraft") return true
            }
            for (entry in arrayOf("META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
                if (jar.entry(entry) == null) continue
                val parsed = Toml.parse(read(jar, entry))
                if (parsed.hasErrors()) throw IllegalArgumentException("Malformed mod metadata")
                val mods = JsonParser.parseString(parsed.toJson()).getAsJsonObject().getAsJsonArray("mods")
                mods?.forEach { element ->
                    if (string(element.asJsonObject, "modId") == "vivecraft") return true
                }
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Cannot inspect mod descriptor", e)
        } catch (e: RuntimeException) {
            throw IllegalArgumentException("Cannot inspect mod descriptor", e)
        }
        return false
    }

    private fun dependency(dependencies: JsonArray?, id: String, idKey: String, versionKey: String): JsonElement? {
        dependencies?.forEach { element ->
            if (element.isJsonObject && id == string(element.asJsonObject, idKey)) {
                return element.asJsonObject.get(versionKey)
            }
        }
        return null
    }

    @Throws(IOException::class)
    private fun requireVersion(dependencyId: String, version: String, rules: JsonElement?) {
        if (!QuestModVersionRules.matches(version, rules)) {
            throw incompatible(
                "$dependencyId $version does not satisfy ${rules?.toString() ?: "missing dependency"}"
            )
        }
    }

    @Throws(IOException::class)
    private fun requireVivecraft(`object`: JsonObject, key: String) {
        if (string(`object`, key) != "vivecraft") throw incompatible("The mod ID is not vivecraft.")
    }

    @Throws(IOException::class)
    private fun json(jar: JarView, entry: String): JsonObject =
        JsonParser.parseString(read(jar, entry)).asJsonObject

    @JvmStatic
    fun string(`object`: JsonObject, key: String): String {
        val value = `object`.get(key)
        return if (value == null || value.isJsonNull) "" else value.asString
    }

    @Throws(IOException::class)
    private fun read(jar: JarView, name: String): String {
        val entry = jar.entry(name) ?: throw incompatible("Missing $name")
        return jar.readText(entry, MAX_DESCRIPTOR_BYTES)
            ?: throw incompatible("Mod descriptor too large: $name")
    }

    private fun incompatible(message: String): IOException =
        QuestVivecraftService.ServiceException(QuestVivecraftService.Error.INCOMPATIBLE, message)
}
