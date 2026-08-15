package com.tungsten.fcl.setting

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * Profiles.selectedProfile 迁移为 Repository 单例 + StateFlow 的深度验证：
 * StateFlow 值更新、监听通知、选中项校验回退、collect 行为。
 */
@RunWith(AndroidJUnit4::class)
class ProfilesTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        if (!ConfigHolder.isInit()) {
            ConfigHolder.init()
        }
        Profiles.init()
    }

    @Test
    fun init后选中配置的profile() {
        val selected = Profiles.getSelectedProfile()
        assertNotNull(selected)
        // StateFlow 当前值与 getSelectedProfile 一致
        assertEquals(selected, Profiles.selectedProfile.value)
        // 选中项必须在 profiles 列表中
        assertTrue(Profiles.profiles.contains(selected))
    }

    @Test
    fun setSelectedProfile更新StateFlow并通知监听器() {
        // 选择不同于当前值的 profile（StateFlow 相同值不重新发射）
        val current = Profiles.selectedProfile.value
        val target = Profiles.profiles.first { it != current }
        val notified = AtomicInteger(0)
        val listener = Runnable { notified.incrementAndGet() }
        Profiles.addSelectedProfileListener(listener)
        try {
            Profiles.setSelectedProfile(target)
            // StateFlow 与读取 API 同步更新
            assertEquals(target, Profiles.selectedProfile.value)
            assertEquals(target, Profiles.getSelectedProfile())
            // 监听器收到一次通知（同步回调）
            assertEquals(1, notified.get())
        } finally {
            Profiles.removeSelectedProfileListener(listener)
        }
    }

    @Test
    fun setSelectedProfile相同值不重复通知() {
        val current = Profiles.getSelectedProfile()
        var notified = 0
        val listener = Runnable { notified++ }
        Profiles.addSelectedProfileListener(listener)
        try {
            Profiles.setSelectedProfile(current)
            // StateFlow 相同值不重新发射，监听器不重复通知
            assertEquals(0, notified)
        } finally {
            Profiles.removeSelectedProfileListener(listener)
        }
    }

    @Test
    fun 设置不存在的profile回退第一个() {
        val ghost = Profile("ghost", File("/sdcard/ghost_dir"))
        Profiles.setSelectedProfile(ghost)
        // 不在列表中的 profile 被回退为第一个
        assertEquals(Profiles.profiles[0], Profiles.getSelectedProfile())
        assertEquals(Profiles.profiles[0], Profiles.selectedProfile.value)
    }

    @Test
    fun 删除选中profile后回退() {
        val first = Profiles.profiles[0]
        Profiles.profiles.remove(first)
        try {
            // 列表变化校验：选中项不在列表时回退第一个
            assertEquals(Profiles.profiles[0], Profiles.getSelectedProfile())
        } finally {
            Profiles.profiles.add(0, first)
        }
    }

    @Test
    fun selectedProfile可收集() {
        // 选择不同于当前值的 profile（StateFlow 相同值不重新发射）
        val current = Profiles.selectedProfile.value
        val target = Profiles.profiles.first { it != current }
        val values = mutableListOf<Profile?>()
        runBlocking {
            val job = launch {
                Profiles.selectedProfile.collect { values.add(it) }
            }
            // 让 collect 协程先启动并收到初始值，再更新
            yield()
            Profiles.setSelectedProfile(target)
            delay(100)
            job.cancel()
        }
        // 首次收集立即发出当前值，更新后再发一次
        assertEquals(2, values.size)
        assertEquals(target, values.last())
    }
}
