package com.tungsten.fcl.game

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonParseException
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.VersionNotFoundException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * FCLGameRepository 版本加载提速（解析缓存、modpack 配置缓存、图标缓存）的验证：
 * 缓存命中行为、版本刷新后缓存清空、版本设置持久化。
 */
@RunWith(AndroidJUnit4::class)
class FCLGameRepositoryTest {

    private lateinit var tempDir: File

    @Before
    fun setup() {
        FCLPath.loadPaths(ApplicationProvider.getApplicationContext<Context>())
        tempDir = File(
            ApplicationProvider.getApplicationContext<Context>().cacheDir,
            "fcl_repo_test_${System.nanoTime()}"
        )
        tempDir.mkdirs()
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    private fun newRepository(): FCLGameRepository {
        return FCLGameRepository(Profile("Test", tempDir), tempDir)
    }

    private fun writeVersion(id: String, mainClass: String? = null) {
        val dir = File(tempDir, "versions/$id")
        dir.mkdirs()
        val json = buildString {
            append("{\"id\":\"$id\"")
            if (mainClass != null) append(",\"mainClass\":\"$mainClass\"")
            append("}")
        }
        File(dir, "$id.json").writeText(json)
    }

    private fun writeModpackConfig(id: String, content: String) {
        val dir = File(tempDir, "versions/$id")
        dir.mkdirs()
        File(dir, "modpack.cfg").writeText(content)
    }

    @Test
    fun refreshLoadsVersions() {
        writeVersion("1.0")
        writeVersion("2.0")
        val repo = newRepository()
        repo.refreshVersions()
        assertTrue(repo.isLoaded())
        assertTrue(repo.hasVersion("1.0"))
        assertTrue(repo.hasVersion("2.0"))
        assertEquals(listOf("1.0", "2.0"), repo.getDisplayVersions().map { it.id }.toList())
    }

    /** 解析缓存：刷新前命中缓存，刷新后清空并反映文件新内容 */
    @Test
    fun resolvedVersionCacheClearedOnRefresh() {
        writeVersion("1.0", mainClass = "com.a.Main")
        val repo = newRepository()
        repo.refreshVersions()
        // 普通版本（无 root 标记）原字段挂载为 patch，mainClass 保留在 patch 中
        assertEquals(
            "com.a.Main",
            repo.getResolvedPreservingPatchesVersion("1.0").getPatches()[0].mainClass
        )
        // 修改 json 后刷新，缓存清空，解析结果更新
        writeVersion("1.0", mainClass = "com.b.Main")
        repo.refreshVersions()
        assertEquals(
            "com.b.Main",
            repo.getResolvedPreservingPatchesVersion("1.0").getPatches()[0].mainClass
        )
    }

    @Test
    fun resolvedVersionForMissingVersionThrows() {
        val repo = newRepository()
        repo.refreshVersions()
        assertThrows(VersionNotFoundException::class.java) {
            repo.getResolvedPreservingPatchesVersion("ghost")
        }
    }

    /** 无 modpack.cfg 时返回 null */
    @Test
    fun readModpackConfigurationReturnsNullWhenAbsent() {
        writeVersion("1.0")
        val repo = newRepository()
        repo.refreshVersions()
        assertNull(repo.readModpackConfiguration<String>("1.0"))
    }

    /** 合法 modpack.cfg 解析成功（ModpackConfiguration 校验要求 manifest 存在） */
    @Test
    fun readModpackConfigurationParsesValidFile() {
        writeVersion("1.0")
        writeModpackConfig(
            "1.0",
            """{"type":"curse","name":"TestPack","version":"1.0.0","overrides":[],"manifest":{}}"""
        )
        val repo = newRepository()
        repo.refreshVersions()
        val config = repo.readModpackConfiguration<String>("1.0")
        assertNotNull(config)
        assertEquals("curse", config!!.type)
        assertEquals("TestPack", config.name)
        assertEquals("1.0.0", config.version)
    }

    /** 损坏的 modpack.cfg 抛出 JSON 解析异常（与基类行为一致，由调用方处理） */
    @Test
    fun readModpackConfigurationBrokenFileThrows() {
        writeVersion("1.0")
        writeModpackConfig("1.0", """{"type":""")
        val repo = newRepository()
        repo.refreshVersions()
        assertThrows(JsonParseException::class.java) {
            repo.readModpackConfiguration<String>("1.0")
        }
    }

    /** 不存在的版本读取 modpack 配置抛 VersionNotFoundException */
    @Test
    fun readModpackConfigurationForMissingVersionThrows() {
        val repo = newRepository()
        repo.refreshVersions()
        assertThrows(VersionNotFoundException::class.java) {
            repo.readModpackConfiguration<String>("ghost")
        }
    }

    /** modpack 配置缓存：刷新后清空并反映文件新内容 */
    @Test
    fun modpackConfigCacheClearedOnRefresh() {
        writeVersion("1.0")
        writeModpackConfig("1.0", """{"type":"curse","manifest":{}}""")
        val repo = newRepository()
        repo.refreshVersions()
        assertEquals("curse", repo.readModpackConfiguration<String>("1.0")!!.type)
        writeModpackConfig("1.0", """{"type":"modrinth","manifest":{}}""")
        repo.refreshVersions()
        assertEquals("modrinth", repo.readModpackConfiguration<String>("1.0")!!.type)
    }

    /** 版本设置修改后持久化到 fclversion.cfg，重新加载的仓库读回一致 */
    @Test
    fun versionSettingPersistsAcrossRepositoryReload() {
        writeVersion("1.0")
        val repo = newRepository()
        repo.refreshVersions()
        // specialize 创建本地设置并注册自动保存监听
        val vs = repo.specializeVersionSetting("1.0")
        assertNotNull(vs)
        vs.java = "Java 17"
        vs.serverIp = "10.0.0.1"
        vs.maxMemory = 4096
        assertTrue(File(tempDir, "versions/1.0/fclversion.cfg").exists())
        // 新仓库从磁盘读回
        val repo2 = newRepository()
        repo2.refreshVersions()
        val vs2 = repo2.getVersionSetting("1.0")
        // JavaManager 未初始化时未知 java 名称回退 Auto（VersionSetting 反序列化行为）
        assertEquals("Auto", vs2.java)
        assertFalse(vs2.isUsesGlobal)
        assertEquals("10.0.0.1", vs2.serverIp)
        assertEquals(4096, vs2.maxMemory)
    }

    /** getVersionSetting 对无本地设置的版本返回全局设置 */
    @Test
    fun getVersionSettingFallsBackToGlobal() {
        writeVersion("1.0")
        val repo = newRepository()
        repo.refreshVersions()
        val vs = repo.getVersionSetting("1.0")
        assertNotNull(vs)
        assertTrue(vs.isUsesGlobal)
    }

    /** 版本图标获取不崩溃（无图标文件时返回默认资源） */
    @Test
    fun getVersionIconImageReturnsDrawable() {
        writeVersion("1.0")
        val repo = newRepository()
        repo.refreshVersions()
        assertNotNull(repo.getVersionIconImage("1.0"))
    }
}
