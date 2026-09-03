package com.tungsten.fcl.setting

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonParser
import com.mio.data.Renderer
import com.tungsten.fcl.FCLApp
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.platform.MemoryUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicInteger

/**
 * VersionSetting 重构（fakefx property → 普通类型字段 + 回调通知）的验证：
 * 默认值、setter 通知、clone 完整性、JSON 序列化/反序列化。
 */
@RunWith(AndroidJUnit4::class)
class VersionSettingTest {

    @Before
    fun setup() {
        FCLPath.loadPaths(ApplicationProvider.getApplicationContext<Context>())
    }

    private fun defaultMemory() = MemoryUtils.findBestRAMAllocation(FCLApp.getAppContext())

    @Test
    fun defaultValues() {
        val vs = VersionSetting()
        assertTrue(vs.isUsesGlobal)
        assertEquals("Auto", vs.java)
        assertEquals(defaultMemory(), vs.maxMemory)
        assertNull(vs.minMemory)
        assertTrue(vs.isAutoMemory)
        assertEquals("", vs.javaArgs)
        assertEquals("", vs.minecraftArgs)
        assertFalse(vs.isNotCheckJVM)
        assertFalse(vs.isNotCheckGame)
        assertEquals("", vs.serverIp)
        assertTrue(vs.isIsolateGameDir)
        assertEquals("default", vs.graphicsBackend)
        assertFalse(vs.isVKDriverSystem)
        assertEquals("00000000", vs.controller)
        assertEquals(Renderer.ID_NGGL4ES, vs.renderer)
        assertEquals("Turnip", vs.driver)
        assertFalse(vs.isNotCheckMod)
        assertFalse(vs.isDebugLog)
        assertFalse(vs.isForceResolution)
    }

    /** 所有可写字段的 setter 都触发一次监听通知（值变化时） */
    @Test
    fun settersNotifyOnChangeListener() {
        val vs = VersionSetting()
        val notified = AtomicInteger(0)
        val listener = Runnable { notified.incrementAndGet() }
        vs.addOnChangeListener(listener)
        try {
            vs.isUsesGlobal = false
            vs.java = "Java 17"
            vs.maxMemory = defaultMemory() + 1
            vs.minMemory = 1024
            vs.isAutoMemory = false
            vs.javaArgs = "-Xmx2G"
            vs.minecraftArgs = "--server localhost"
            vs.isNotCheckJVM = true
            vs.isNotCheckGame = true
            vs.serverIp = "127.0.0.1:25565"
            vs.isIsolateGameDir = false
            vs.graphicsBackend = "vulkan"
            vs.isVKDriverSystem = true
            vs.controller = "12345678"
            vs.renderer = "gl4es"
            vs.driver = "Mesa"
            vs.isNotCheckMod = true
            vs.isDebugLog = true
            vs.isForceResolution = true
            assertEquals(19, notified.get())
        } finally {
            vs.removeOnChangeListener(listener)
        }
    }

    /** 同值写入不触发通知（避免读取路径的幂等赋值反复触发配置保存） */
    @Test
    fun sameValueSetDoesNotNotify() {
        val vs = VersionSetting()
        val notified = AtomicInteger(0)
        val listener = Runnable { notified.incrementAndGet() }
        vs.addOnChangeListener(listener)
        try {
            vs.isUsesGlobal = true
            vs.java = "Auto"
            vs.controller = "00000000"
            vs.driver = "Turnip"
            vs.maxMemory = vs.maxMemory
            assertEquals(0, notified.get())
            vs.maxMemory = vs.maxMemory + 1
            assertEquals(1, notified.get())
        } finally {
            vs.removeOnChangeListener(listener)
        }
    }

    @Test
    fun removeOnChangeListenerStopsNotification() {
        val vs = VersionSetting()
        val notified = AtomicInteger(0)
        val listener = Runnable { notified.incrementAndGet() }
        vs.addOnChangeListener(listener)
        vs.removeOnChangeListener(listener)
        vs.java = "Java 17"
        assertEquals(0, notified.get())
    }

    /** 回调内移除自身监听不崩溃（遍历前复制列表） */
    @Test
    fun listenerCanRemoveItselfDuringNotification() {
        val vs = VersionSetting()
        val otherCount = AtomicInteger(0)
        val selfRemoving = object : Runnable {
            override fun run() {
                vs.removeOnChangeListener(this)
            }
        }
        val other = Runnable { otherCount.incrementAndGet() }
        vs.addOnChangeListener(selfRemoving)
        vs.addOnChangeListener(other)
        vs.java = "Java 17"
        // selfRemoving 在遍历中被移除，不影响后续监听执行
        assertEquals(1, otherCount.get())
        // 再次变更不再触发任何通知（selfRemoving 已移除、other 仍在）
        vs.java = "Java 21"
        assertEquals(2, otherCount.get())
    }

    /** 设置全部字段后 clone 完整复制且相互独立 */
    @Test
    fun cloneCopiesAllFieldsAndIsIndependent() {
        val vs = VersionSetting().apply {
            isUsesGlobal = false
            java = "Java 17"
            maxMemory = 2048
            minMemory = 1024
            isAutoMemory = false
            javaArgs = "-Xmx2G"
            minecraftArgs = "--server localhost"
            isNotCheckJVM = true
            isNotCheckGame = true
            serverIp = "127.0.0.1:25565"
            isIsolateGameDir = false
            graphicsBackend = "vulkan"
            isVKDriverSystem = true
            controller = "12345678"
            renderer = "gl4es"
            driver = "Mesa"
            isNotCheckMod = true
            isDebugLog = true
            isForceResolution = true
        }
        val copy = vs.clone()
        assertNotSame(vs, copy)
        assertEquals(vs.isUsesGlobal, copy.isUsesGlobal)
        assertEquals(vs.java, copy.java)
        assertEquals(vs.maxMemory, copy.maxMemory)
        assertEquals(vs.minMemory, copy.minMemory)
        assertEquals(vs.isAutoMemory, copy.isAutoMemory)
        assertEquals(vs.javaArgs, copy.javaArgs)
        assertEquals(vs.minecraftArgs, copy.minecraftArgs)
        assertEquals(vs.isNotCheckJVM, copy.isNotCheckJVM)
        assertEquals(vs.isNotCheckGame, copy.isNotCheckGame)
        assertEquals(vs.serverIp, copy.serverIp)
        assertEquals(vs.isIsolateGameDir, copy.isIsolateGameDir)
        assertEquals(vs.graphicsBackend, copy.graphicsBackend)
        assertEquals(vs.isVKDriverSystem, copy.isVKDriverSystem)
        assertEquals(vs.controller, copy.controller)
        assertEquals(vs.renderer, copy.renderer)
        assertEquals(vs.driver, copy.driver)
        assertEquals(vs.isNotCheckMod, copy.isNotCheckMod)
        assertEquals(vs.isDebugLog, copy.isDebugLog)
        assertEquals(vs.isForceResolution, copy.isForceResolution)
        // 修改副本不影响原对象
        copy.java = "Changed"
        copy.maxMemory = 1
        assertEquals("Java 17", vs.java)
        assertEquals(2048, vs.maxMemory)
    }

    /** 全部字段序列化往返保持一致（java 名称不识别时回退 Auto，单独用例覆盖） */
    @Test
    fun serializerRoundTripPreservesFields() {
        val vs = VersionSetting().apply {
            isUsesGlobal = false
            java = "Auto"
            maxMemory = 2048
            minMemory = 512
            isAutoMemory = false
            javaArgs = "-Xmx2G"
            minecraftArgs = "--server localhost"
            isNotCheckJVM = true
            isNotCheckGame = true
            serverIp = "127.0.0.1:25565"
            isIsolateGameDir = false
            graphicsBackend = "vulkan"
            isVKDriverSystem = true
            controller = "12345678"
            renderer = "gl4es"
            driver = "Mesa"
            isNotCheckMod = true
            isDebugLog = true
            isForceResolution = true
        }
        val restored = JsonUtils.GSON.fromJson<VersionSetting>(JsonUtils.GSON.toJson(vs), VersionSetting::class.java)
        assertFalse(restored.isUsesGlobal)
        assertEquals("Auto", restored.java)
        assertEquals(2048, restored.maxMemory)
        assertEquals(512, restored.minMemory)
        assertFalse(restored.isAutoMemory)
        assertEquals("-Xmx2G", restored.javaArgs)
        assertEquals("--server localhost", restored.minecraftArgs)
        assertTrue(restored.isNotCheckJVM)
        assertTrue(restored.isNotCheckGame)
        assertEquals("127.0.0.1:25565", restored.serverIp)
        assertFalse(restored.isIsolateGameDir)
        assertEquals("vulkan", restored.graphicsBackend)
        assertTrue(restored.isVKDriverSystem)
        assertEquals("12345678", restored.controller)
        assertEquals("gl4es", restored.renderer)
        assertEquals("Mesa", restored.driver)
        assertTrue(restored.isNotCheckMod)
        assertTrue(restored.isDebugLog)
        assertTrue(restored.isForceResolution)
    }

    /** 空对象反序列化：缺失字段使用反序列化默认值 */
    @Test
    fun deserializeEmptyObjectUsesDefaults() {
        val restored = JsonUtils.GSON.fromJson<VersionSetting>("{}", VersionSetting::class.java)
        // 反序列化默认值（与字段默认值不同处：usesGlobal/isolateGameDir 缺失时为 false）
        assertFalse(restored.isUsesGlobal)
        assertFalse(restored.isIsolateGameDir)
        assertTrue(restored.isAutoMemory)
        assertEquals(defaultMemory(), restored.maxMemory)
        assertNull(restored.minMemory)
        assertEquals("Auto", restored.java)
        assertEquals("00000000", restored.controller)
        assertEquals(Renderer.ID_NGGL4ES, restored.renderer)
        assertEquals("Turnip", restored.driver)
        assertEquals("default", restored.graphicsBackend)
        assertEquals("", restored.javaArgs)
        assertEquals("", restored.minecraftArgs)
    }

    /** 部分字段存在时其余字段使用默认值 */
    @Test
    fun deserializePartialJsonFillsDefaults() {
        val restored = JsonUtils.GSON.fromJson<VersionSetting>(
            """{"maxMemory":1024,"autoMemory":false,"serverIp":"1.2.3.4"}""",
            VersionSetting::class.java
        )
        assertEquals(1024, restored.maxMemory)
        assertFalse(restored.isAutoMemory)
        assertEquals("1.2.3.4", restored.serverIp)
        assertNull(restored.minMemory)
        assertEquals("Auto", restored.java)
    }

    /** maxMemory <= 0 时序列化与反序列化都回退为推荐内存 */
    @Test
    fun nonPositiveMaxMemoryFallsBack() {
        val vs = VersionSetting().apply { maxMemory = 0 }
        val json = JsonParser.parseString(JsonUtils.GSON.toJson(vs)).asJsonObject
        assertEquals(defaultMemory(), json.get("maxMemory").asInt)
        val restored = JsonUtils.GSON.fromJson<VersionSetting>("""{"maxMemory":0}""", VersionSetting::class.java)
        assertEquals(defaultMemory(), restored.maxMemory)
    }

    /** JSON 中 maxMemory 为字符串数字时也能解析 */
    @Test
    fun deserializeMaxMemoryAsStringNumber() {
        val restored = JsonUtils.GSON.fromJson<VersionSetting>("""{"maxMemory":"2048"}""", VersionSetting::class.java)
        assertEquals(2048, restored.maxMemory)
    }

    /** 未知 java 名称回退为 Auto */
    @Test
    fun deserializeUnknownJavaFallsBackToAuto() {
        val restored = JsonUtils.GSON.fromJson<VersionSetting>("""{"java":"Not Installed JDK"}""", VersionSetting::class.java)
        assertEquals("Auto", restored.java)
    }
}
