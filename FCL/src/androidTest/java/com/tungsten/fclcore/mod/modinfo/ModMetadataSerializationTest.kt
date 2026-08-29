package com.tungsten.fclcore.mod.modinfo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fclcore.game.GameRepository
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.util.io.CompressingUtils
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Proxy
import java.nio.file.FileSystem
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * modinfo 元数据从 Java record（Gson）迁移到 Kotlin data class（kotlinx.serialization）
 * 的稳定性验证，全部用例运行在 Android 运行时（ART）上。
 *
 * 迁移前后的关键行为差异（旧坑回归）：
 * - record + Gson 在真机走 Unsafe 分配，缺失字段为 null 且构造器不执行；
 *   data class + kotlinx 缺失字段/显式 null 均落构造器默认值。
 * - 历史崩溃场景一：fabric.mod.json 缺 name → LocalModFile.name 为 null →
 *   ModInfoObject 的 new StringBuilder(name) NPE。
 * - 历史崩溃场景二：mcmod.info 缺 authors/authorList 数组 → 组件为 null →
 *   组装链 `.length` NPE。
 */
@RunWith(AndroidJUnit4::class)
class ModMetadataSerializationTest {

    /** 直接引用生产解析配置：配置漂移（如关掉 coerceInputValues）时测试立即失败 */
    private val json: Json = MOD_METADATA_JSON

    // ==================== FabricModMetadata ====================

    @Test
    fun fabricCompleteJsonParsesAllFields() {
        val metadata = json.decodeFromString<FabricModMetadata>(
            """
            {
              "id": "example_mod",
              "name": "Example Mod",
              "version": "1.2.3",
              "icon": "assets/icon.png",
              "description": "A test mod",
              "authors": [{"name": "Alice"}, {"name": "Bob"}],
              "contact": {"homepage": "https://example.com"}
            }
            """.trimIndent()
        )
        assertEquals("example_mod", metadata.id)
        assertEquals("Example Mod", metadata.name)
        assertEquals("1.2.3", metadata.version)
        assertEquals("assets/icon.png", metadata.icon)
        assertEquals("A test mod", metadata.description)
        assertEquals(listOf("Alice", "Bob"), metadata.authors.map { it.name })
        assertEquals("https://example.com", metadata.contact["homepage"])
    }

    /** 回归核心：全缺失 → 全默认 ""。旧 record+Gson 下 name 为 null 直接 NPE */
    @Test
    fun fabricMissingFieldsFallBackToDefaults() {
        val metadata = json.decodeFromString<FabricModMetadata>("{}")
        assertEquals("", metadata.id)
        assertEquals("", metadata.name)
        assertEquals("", metadata.version)
        assertEquals("", metadata.icon)
        assertEquals("", metadata.description)
        assertTrue(metadata.authors.isEmpty())
        assertTrue(metadata.contact.isEmpty())
    }

    /** coerceInputValues：显式 JSON null 归默认值，而非置 null */
    @Test
    fun fabricExplicitNullCoercesToDefault() {
        val metadata = json.decodeFromString<FabricModMetadata>(
            """{"id": "m", "name": null, "version": null, "description": null}"""
        )
        assertEquals("m", metadata.id)
        assertEquals("", metadata.name)
        assertEquals("", metadata.version)
        assertEquals("", metadata.description)
    }

    @Test
    fun fabricUnknownKeysIgnored() {
        val metadata = json.decodeFromString<FabricModMetadata>(
            """{"id": "m", "custom_field": 123, "another": {"x": 1}}"""
        )
        assertEquals("m", metadata.id)
    }

    /** authors 元素为纯字符串形态 */
    @Test
    fun fabricAuthorsAsStringElements() {
        val metadata = json.decodeFromString<FabricModMetadata>("""{"authors": ["Alice", "Bob"]}""")
        assertEquals(listOf("Alice", "Bob"), metadata.authors.map { it.name })
    }

    /** authors 元素为对象形态，且缺 name 的元素归默认值 */
    @Test
    fun fabricAuthorsMixedElements() {
        val metadata = json.decodeFromString<FabricModMetadata>(
            """{"authors": [{"name": "Alice"}, {}, {"name": "Bob"}]}"""
        )
        assertEquals(listOf("Alice", "", "Bob"), metadata.authors.map { it.name })
    }

    @Test
    fun fabricAuthorsMissingYieldsEmptyList() {
        val metadata = json.decodeFromString<FabricModMetadata>("{}")
        assertTrue(metadata.authors.isEmpty())
    }

    // ==================== ForgeNewModMetadata ====================

    /** mods.toml 转 JSON 后的典型形态 */
    @Test
    fun forgeNewCompleteParse() {
        val metadata = json.decodeFromString<ForgeNewModMetadata>(
            """
            {
              "modLoader": "javafml",
              "loaderVersion": "[47,)",
              "license": "MIT",
              "mods": [
                {"modId": "example", "version": "1.0", "displayName": "Example",
                 "authors": "Alice", "description": "desc"}
              ]
            }
            """.trimIndent()
        )
        assertEquals("javafml", metadata.modLoader)
        assertEquals(1, metadata.mods.size)
        assertEquals("example", metadata.mods[0].modId)
        assertEquals("Alice", metadata.mods[0].authors)
        assertEquals("1.0", metadata.mods[0].version)
    }

    /** authors 为字符串数组形态 */
    @Test
    fun forgeNewAuthorsAsStringArray() {
        val metadata = json.decodeFromString<ForgeNewModMetadata>(
            """{"mods": [{"modId": "m", "authors": ["Alice", "Bob"]}]}"""
        )
        assertEquals("Alice, Bob", metadata.mods[0].authors)
    }

    /** authors 数组含非字符串元素时整体回退为其 JSON 文本（保留旧 deserializer 行为） */
    @Test
    fun forgeNewAuthorsArrayWithNonPrimitiveFallsBackToJsonText() {
        val metadata = json.decodeFromString<ForgeNewModMetadata>(
            """{"mods": [{"modId": "m", "authors": ["Alice", {"nested": true}]}]}"""
        )
        assertTrue(metadata.mods[0].authors.startsWith("["))
        assertTrue(metadata.mods[0].authors.contains("Alice"))
    }

    /** mods 数组缺失 → 空列表（fromFile0 的 isEmpty 校验据此抛 malformed） */
    @Test
    fun forgeNewModsMissingYieldsEmptyList() {
        val metadata = json.decodeFromString<ForgeNewModMetadata>("""{"modLoader": "javafml"}""")
        assertTrue(metadata.mods.isEmpty())
    }

    /** Mod 组件字段缺失 → 默认 ""（旧 record+Gson 为 null） */
    @Test
    fun forgeNewModComponentMissingFieldsDefault() {
        val metadata = json.decodeFromString<ForgeNewModMetadata>("""{"mods": [{}]}""")
        assertEquals("", metadata.mods[0].modId)
        assertEquals("", metadata.mods[0].displayName)
        assertEquals("", metadata.mods[0].authors)
    }

    // ==================== ForgeOldModMetadata ====================

    /** 顶层数组形态（老模组常见） */
    @Test
    fun forgeOldTopLevelArrayForm() {
        val element = json.parseToJsonElement(
            """[{"modid": "example", "name": "Example", "version": "2.0", "mcversion": "1.12.2"}]"""
        )
        assertTrue(element is JsonArray)
        val parsed = json.decodeFromJsonElement<List<ForgeOldModMetadata>>(element.jsonArray)
        assertEquals(1, parsed.size)
        assertEquals("example", parsed[0].modid)
        assertEquals("Example", parsed[0].name)
        assertEquals("1.12.2", parsed[0].mcversion)
    }

    /** {"modList": [...]} 对象形态 */
    @Test
    fun forgeOldModListObjectForm() {
        val lst = json.decodeFromString<ForgeOldModMetadataLst>(
            """{"modListVersion": 0, "modList": [{"modid": "example", "name": "Example"}]}"""
        )
        assertEquals("example", lst.modList.single().modid)
    }

    /**
     * 回归：mcmod.info 缺 authors/authorList 数组字段。
     * record+Gson 时组件为 null，fromFile 组装链 `.length` 直接 NPE；迁移后为空列表。
     */
    @Test
    fun forgeOldMissingAuthorArraysDoNotCrash() {
        val metadata = json.decodeFromString<ForgeOldModMetadata>(
            """{"modid": "example", "name": "Example"}"""
        )
        assertTrue(metadata.authors.isEmpty())
        assertTrue(metadata.authorList.isEmpty())
        assertEquals("", resolveAuthors(metadata))
    }

    /** authors 组装优先级：author → authors → authorList → credits */
    @Test
    fun forgeOldAuthorPriorityChain() {
        assertEquals("single", resolveAuthors(json.decodeFromString("""{"author": "single"}""")))
        assertEquals("A, B", resolveAuthors(json.decodeFromString("""{"authors": ["A", "B"]}""")))
        assertEquals("C, D", resolveAuthors(json.decodeFromString("""{"authorList": ["C", "D"]}""")))
        assertEquals("credits here", resolveAuthors(json.decodeFromString("""{"credits": "credits here"}""")))
    }

    private fun resolveAuthors(m: ForgeOldModMetadata): String {
        var authors = m.author
        if (authors.isBlank() && m.authors.isNotEmpty()) authors = m.authors.joinToString(", ")
        if (authors.isBlank() && m.authorList.isNotEmpty()) authors = m.authorList.joinToString(", ")
        if (authors.isBlank()) authors = m.credits
        return authors
    }

    // ==================== LiteModMetadata ====================

    @Test
    fun liteCompleteAndMissingFields() {
        val full = json.decodeFromString<LiteModMetadata>(
            """{"name": "litemod", "version": "1.0", "mcversion": "1.12.2", "author": "someone"}"""
        )
        assertEquals("litemod", full.name)
        assertEquals("1.12.2", full.mcversion)

        val empty = json.decodeFromString<LiteModMetadata>("{}")
        assertEquals("", empty.name)
        assertEquals("", empty.description)
    }

    // ==================== QuiltModMetadata ====================

    /** 完整嵌套解析 + contributors/contact 的 JsonElement 取值 */
    @Test
    fun quiltFullParse() {
        val root = json.decodeFromString<QuiltModMetadata>(
            """
            {
              "schema_version": 1,
              "quilt_loader": {
                "id": "example_quilt",
                "version": "3.1.4",
                "metadata": {
                  "name": "Example Quilt Mod",
                  "description": "desc",
                  "contributors": {"Alice": "Author", "Bob": "Contributor"},
                  "icon": "icon.png",
                  "contact": {"homepage": "https://example.com"}
                }
              }
            }
            """.trimIndent()
        )
        assertEquals(1, root.schemaVersion)
        assertEquals("example_quilt", root.quiltLoader.id)
        val metadata = root.quiltLoader.metadata
        assertEquals("Example Quilt Mod", metadata.name)
        assertEquals(
            "Alice (Author), Bob (Contributor)",
            metadata.contributors.entries.joinToString(", ") { (name, value) -> "$name (${value.jsonPrimitive.content})" }
        )
        assertEquals("https://example.com", (metadata.contact["homepage"] as JsonPrimitive).content)
    }

    /** schema_version 缺失 → 0 → fromFile 抛 not supported */
    @Test
    fun quiltMissingSchemaVersionIsRejectedByFromFile() {
        zipFs("quilt.mod.json" to """{"quilt_loader": {"id": "m", "version": "1", "metadata": {}}}""").use { fs ->
            val ex = assertThrows(IOException::class.java) {
                QuiltModMetadata.fromFile(newModManager(), tempFile(), fs)
            }
            assertTrue(ex.message!!.contains("not a supported Quilt mod"))
        }
    }

    /** quilt_loader 必填组件缺失 → kotlinx 抛序列化异常（旧 record+Gson 为 null 后嵌套 NPE） */
    @Test
    fun quiltMissingLoaderFailsWithSerializationError() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<QuiltModMetadata>("""{"schema_version": 1}""")
        }
    }

    // ==================== fromFile 端到端（真实 zip） ====================

    /**
     * 历史崩溃场景端到端回归：fabric.mod.json 缺 name 的自制 mod，
     * 扫描入列表时 name 必须为 "" 而非 null（旧版 ModInfoObject NPE）。
     */
    @Test
    fun fabricFromFileEndToEndWithMissingName() {
        zipFs("fabric.mod.json" to """{"id": "sakura", "version": "1.1.0"}""").use { fs ->
            val localModFile = FabricModMetadata.fromFile(newModManager(), tempFile(), fs)
            assertEquals("", localModFile.name)
            assertEquals("sakura", localModFile.id)
            assertEquals("1.1.0", localModFile.version)
            assertEquals("", localModFile.description.toString())
        }
    }

    /** fabric authors 字符串数组形态 + 无 contact 时 url 兜底空串 */
    @Test
    fun fabricFromFileEndToEndWithStringAuthorsAndNoContact() {
        zipFs(
            "fabric.mod.json" to
                """{"id": "m2", "name": "Mod Two", "version": "0.1", "authors": ["Alice"], "description": "d"}"""
        ).use { fs ->
            val localModFile = FabricModMetadata.fromFile(newModManager(), tempFile(), fs)
            assertEquals("Mod Two", localModFile.name)
            assertEquals("Alice", localModFile.authors)
            assertEquals("", localModFile.url)
        }
    }

    /** mcmod.info 顶层数组形态端到端 + authors 数组缺失不 NPE（回归） */
    @Test
    fun forgeOldFromFileEndToEndArrayForm() {
        zipFs(
            "mcmod.info" to
                """[{"modid": "oldmod", "name": "Old Mod", "version": "0.9", "mcversion": "1.7.10", "credits": "team"}]"""
        ).use { fs ->
            val localModFile = ForgeOldModMetadata.fromFile(newModManager(), tempFile(), fs)
            assertEquals("Old Mod", localModFile.name)
            assertEquals("0.9", localModFile.version)
            assertEquals("1.7.10", localModFile.gameVersion)
            assertEquals("team", localModFile.authors)
        }
    }

    /** mcmod.info 缺 authors 数组端到端：组装链安全落到 credits/""（回归） */
    @Test
    fun forgeOldFromFileEndToEndWithoutAuthorArrays() {
        zipFs("mcmod.info" to """[{"modid": "bare", "name": "Bare Mod"}]""").use { fs ->
            val localModFile = ForgeOldModMetadata.fromFile(newModManager(), tempFile(), fs)
            assertEquals("Bare Mod", localModFile.name)
            assertEquals("", localModFile.authors)
        }
    }

    /** LiteLoader 端到端 */
    @Test
    fun liteFromFileEndToEnd() {
        zipFs("litemod.json" to """{"name": "litetest", "version": "1", "mcversion": "1.12.2"}""").use { fs ->
            val localModFile = LiteModMetadata.fromFile(newModManager(), tempFile(), fs)
            assertEquals("litetest", localModFile.name)
            assertEquals("1.12.2", localModFile.gameVersion)
        }
    }

    /** 非 mod 文件（zip 内无元数据）→ IOException */
    @Test
    fun fromFileWithNonModZipThrows() {
        zipFs("random.txt" to "hello").use { fs ->
            assertThrows(IOException::class.java) {
                FabricModMetadata.fromFile(newModManager(), tempFile(), fs)
            }
        }
    }

    // ==================== 公共工具 ====================

    /** fromFile 只把该路径用于记录与后缀判断，无需真实存在 */
    private fun tempFile(): Path {
        val cacheDir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
        return File(cacheDir, "modtest_${System.nanoTime()}.jar").toPath()
    }

    /** 在临时 zip（jar）内写入条目并返回只读文件系统 */
    private fun zipFs(vararg entries: Pair<String, String>): FileSystem {
        val cacheDir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir
        val file = File(cacheDir, "modtest_${System.nanoTime()}.jar")
        ZipOutputStream(FileOutputStream(file)).use { zos ->
            entries.forEach { (name, content) ->
                zos.putNextEntry(ZipEntry(name))
                zos.write(content.toByteArray(Charsets.UTF_8))
                zos.closeEntry()
            }
        }
        return CompressingUtils.createReadOnlyZipFileSystem(file.toPath())
    }

    /** GameRepository 不会被 fromFile 链路触达，用动态代理占位 */
    private fun newModManager(): ModManager {
        val repository = Proxy.newProxyInstance(
            GameRepository::class.java.classLoader,
            arrayOf(GameRepository::class.java)
        ) { _, _, _ -> throw UnsupportedOperationException() } as GameRepository
        return ModManager(repository, "test")
    }
}
