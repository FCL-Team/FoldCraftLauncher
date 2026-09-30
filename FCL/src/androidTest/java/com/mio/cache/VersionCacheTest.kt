package com.mio.cache

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * VersionCache 仓库化的行为验证：全量派生数据填充、指纹增量复用（未变条目复用同一实例）、
 * 来源变化后重算、invalidate 冷加载、并发 refresh 的 Mutex 合并。
 */
@RunWith(AndroidJUnit4::class)
class VersionCacheTest {

    private lateinit var tempDir: File
    private lateinit var profile: Profile

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        tempDir = File(context.cacheDir, "fcl_cache_test_${System.nanoTime()}")
        tempDir.mkdirs()
        profile = Profile("CacheTest", tempDir)
    }

    @After
    fun tearDown() {
        VersionCache.invalidate(profile)
        tempDir.deleteRecursively()
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

    private fun loadRepository(): List<String> {
        writeVersion("1.0", mainClass = "com.a.Main")
        writeVersion("2.0", mainClass = "com.b.Main")
        profile.repository.refreshVersions()
        return profile.repository.displayVersions.map { it.id }.collect(java.util.stream.Collectors.toList())
    }

    @Test
    fun refreshPopulatesEntriesForAllVersions() = runBlocking {
        loadRepository()
        val entries = VersionCache.refresh(profile)
        assertEquals(2, entries.size)
        val byId = entries.associateBy { it.id }
        assertEquals(setOf("1.0", "2.0"), byId.keys)
        for (entry in byId.values) {
            assertNotNull(entry.gameVersion)
            // 极简 json 无安装器：组件摘要非空（未知游戏版本占位）、无整合包标签、Mod 数 0
            assertTrue(entry.libraries.isNotEmpty())
            assertEquals(null, entry.tag)
            assertEquals(0, entry.modCount)
        }
        // 快照已写入，get 可读
        assertEquals(2, VersionCache.get(profile).size)
    }

    /** 指纹未变化的条目跨 refresh 复用同一实例（增量跳过的核心行为） */
    @Test
    fun secondRefreshReusesUnchangedEntries() = runBlocking {
        loadRepository()
        val first = VersionCache.refresh(profile).associateBy { it.id }
        val second = VersionCache.refresh(profile).associateBy { it.id }
        assertSame(first["1.0"], second["1.0"])
        assertSame(first["2.0"], second["2.0"])
    }

    /** 来源（版本 json）变化后仅重算该版本，其余条目仍复用 */
    @Test
    fun changedVersionJsonRecomputesOnlyThatEntry() = runBlocking {
        loadRepository()
        val first = VersionCache.refresh(profile).associateBy { it.id }
        // 重写 json 并显式前推 mtime（模拟器存储 mtime 粒度可能到秒）
        val jsonFile = File(tempDir, "versions/1.0/1.0.json")
        jsonFile.writeText("{\"id\":\"1.0\",\"mainClass\":\"com.c.Main\"}")
        jsonFile.setLastModified(System.currentTimeMillis() + 60_000)
        val second = VersionCache.refresh(profile).associateBy { it.id }
        assertNotSame(first["1.0"], second["1.0"])
        assertSame(first["2.0"], second["2.0"])
    }

    /** invalidate 后走冷加载，全部条目重新计算 */
    @Test
    fun invalidateForcesFullRecompute() = runBlocking {
        loadRepository()
        val first = VersionCache.refresh(profile).associateBy { it.id }
        VersionCache.invalidate(profile)
        assertEquals(0, VersionCache.get(profile).size)
        val second = VersionCache.refresh(profile).associateBy { it.id }
        assertEquals(2, second.size)
        assertNotSame(first["1.0"], second["1.0"])
        assertNotSame(first["2.0"], second["2.0"])
    }

    /** 并发 refresh 由 Mutex 合并：两个调用方拿到同一份（复用）快照 */
    @Test
    fun concurrentRefreshSharesSnapshot() = runBlocking {
        loadRepository()
        val first = VersionCache.refresh(profile).associateBy { it.id }
        val results = List(2) { async { VersionCache.refresh(profile) } }.map { it.await() }
        assertEquals(2, results[0].size)
        assertEquals(2, results[1].size)
        val second = results[0].associateBy { it.id }
        val third = results[1].associateBy { it.id }
        // 并发方各自进锁后按指纹复用，实例一致
        assertSame(second["1.0"], third["1.0"])
        assertSame(second["2.0"], third["2.0"])
        assertSame(first["1.0"], second["1.0"])
    }
}
