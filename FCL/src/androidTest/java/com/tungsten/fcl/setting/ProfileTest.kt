package com.tungsten.fcl.setting

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.gson.JsonUtils
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * Profile 重构（fakefx property → 普通类型字段 + 监听回调）的验证：
 * 序列化/反序列化、selectedVersion 校验与通知、游戏目录切换。
 */
@RunWith(AndroidJUnit4::class)
class ProfileTest {

    private lateinit var tempDir: File

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        tempDir = File(context.cacheDir, "profile_test_${System.nanoTime()}")
        tempDir.mkdirs()
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    private fun writeVersion(id: String, parent: File = tempDir, mainClass: String? = null) {
        val dir = File(parent, "versions/$id")
        dir.mkdirs()
        val json = buildString {
            append("{\"id\":\"$id\"")
            if (mainClass != null) append(",\"mainClass\":\"$mainClass\"")
            append("}")
        }
        File(dir, "$id.json").writeText(json)
    }

    @Test
    fun serializerRoundTripPreservesFields() {
        val global = VersionSetting().apply {
            maxMemory = 2048
            serverIp = "1.2.3.4"
            isUsesGlobal = false
        }
        val profile = Profile("TestProfile", File(tempDir, "game"), global, "1.0")
        val restored = JsonUtils.GSON.fromJson<Profile>(JsonUtils.GSON.toJson(profile), Profile::class.java)
        assertEquals(File(tempDir, "game"), restored.gameDir)
        assertEquals("1.0", restored.selectedVersion)
        assertEquals(2048, restored.globalVersionSetting.maxMemory)
        assertEquals("1.2.3.4", restored.globalVersionSetting.serverIp)
        assertFalse(restored.globalVersionSetting.isUsesGlobal)
        // 反序列化时名称固定为 "Default"（Serializer 不持久化 name）
        assertEquals("Default", restored.name)
    }

    @Test
    fun selectedVersionSetterNotifiesAndSameValueSkips() {
        val profile = Profile("Test", tempDir)
        val notified = AtomicInteger(0)
        val listener = Runnable { notified.incrementAndGet() }
        profile.addSelectedVersionListener(listener)
        try {
            profile.selectedVersion = "1.0"
            assertEquals(1, notified.get())
            // 同值不通知
            profile.selectedVersion = "1.0"
            assertEquals(1, notified.get())
            profile.selectedVersion = "2.0"
            assertEquals(2, notified.get())
        } finally {
            profile.removeSelectedVersionListener(listener)
        }
        profile.selectedVersion = "3.0"
        assertEquals(2, notified.get())
    }

    /** 回调内增删监听不崩溃（遍历前复制列表） */
    @Test
    fun listenerCanRemoveItselfDuringNotification() {
        val profile = Profile("Test", tempDir)
        val otherCount = AtomicInteger(0)
        val selfRemoving = object : Runnable {
            override fun run() {
                profile.removeSelectedVersionListener(this)
            }
        }
        val other = Runnable { otherCount.incrementAndGet() }
        profile.addSelectedVersionListener(selfRemoving)
        profile.addSelectedVersionListener(other)
        profile.selectedVersion = "1.0"
        assertEquals(1, otherCount.get())
    }

    /** repository 未加载时不校验 selectedVersion */
    @Test
    fun selectedVersionKeptWhenRepositoryNotLoaded() {
        val profile = Profile("Test", tempDir)
        profile.selectedVersion = "ghost"
        assertEquals("ghost", profile.selectedVersion)
    }

    /** 加载后选中不存在的版本会回退到第一个版本 */
    @Test
    fun selectedVersionInvalidFallsBackToFirst() {
        writeVersion("1.0")
        val profile = Profile("Test", tempDir)
        profile.repository.refreshVersions()
        assertTrue(profile.repository.isLoaded())
        profile.selectedVersion = "ghost"
        assertEquals("1.0", profile.selectedVersion)
    }

    /** 加载后无任何版本时，选中不存在的版本被清空为 null */
    @Test
    fun selectedVersionInvalidClearedWhenNoVersions() {
        val profile = Profile("Test", tempDir)
        profile.repository.refreshVersions()
        assertTrue(profile.repository.isLoaded())
        profile.selectedVersion = "ghost"
        assertNull(profile.selectedVersion)
    }

    /** 有效版本不受校验影响 */
    @Test
    fun selectedVersionValidKept() {
        writeVersion("1.0")
        val profile = Profile("Test", tempDir)
        profile.repository.refreshVersions()
        profile.selectedVersion = "1.0"
        assertEquals("1.0", profile.selectedVersion)
    }

    /** 切换游戏目录后仓库指向新目录 */
    @Test
    fun gameDirSwitchChangesRepositoryDirectory() {
        val dir1 = File(tempDir, "dir1")
        val dir2 = File(tempDir, "dir2")
        dir1.mkdirs()
        dir2.mkdirs()
        val profile = Profile("Test", dir1)
        profile.repository.refreshVersions()
        assertFalse(profile.repository.hasVersion("1.0"))
        writeVersion("1.0", parent = dir2)
        profile.gameDir = dir2
        profile.repository.refreshVersions()
        assertTrue(profile.repository.hasVersion("1.0"))
    }
}