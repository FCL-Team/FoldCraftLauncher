package com.tungsten.fcllibrary.component.theme

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicInteger

/**
 * ThemeEngine 重构（Java 单例 → Kotlin object + StateFlow）的验证：
 * 初始化、亮暗判断、applyXxx 更新与刷新回调、监听注册/注销。
 */
@RunWith(AndroidJUnit4::class)
class ThemeEngineTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        FCLPath.loadPaths(context)
        // 清理 launcher 偏好中的主题模式，避免用例间相互影响
        context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
            .edit().remove("themeMode").apply()
    }

    @Test
    fun setupThemeEngineIsIdempotent() {
        ThemeEngine.setupThemeEngine(context)
        val first = ThemeEngine.getTheme()
        ThemeEngine.setupThemeEngine(context)
        // 二次调用不覆盖（幂等）
        assertEquals(first, ThemeEngine.getTheme())
        assertNotNull(first)
        // StateFlow 同步持有当前主题
        runBlocking { assertEquals(first, ThemeEngine.theme.first()) }
    }

    @Test
    fun getThemeAvailableAfterSetup() {
        ThemeEngine.setupThemeEngine(context)
        assertNotNull(ThemeEngine.getTheme())
    }

    @Test
    fun isNightModeFollowsThemeModeSetting() {
        val prefs = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        // 强制亮色
        prefs.edit().putInt("themeMode", 1).apply()
        assertFalse(ThemeEngine.isNightMode(context))
        // 强制暗色
        prefs.edit().putInt("themeMode", 2).apply()
        assertTrue(ThemeEngine.isNightMode(context))
        // 跟随系统（themeMode=0）：取系统 uiMode
        prefs.edit().putInt("themeMode", 0).apply()
        val systemNight = (context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES
        assertEquals(systemNight, ThemeEngine.isNightMode(context))
        // 清理
        prefs.edit().remove("themeMode").apply()
    }

    @Test
    fun applyColorUpdatesStateFlowAndNotifies() {
        ThemeEngine.setupThemeEngine(context)
        val notified = AtomicInteger(0)
        val view = View(context)
        ThemeEngine.registerEvent(view) { notified.incrementAndGet() }
        try {
            // registerEvent 注册后立即执行一次（post 异步，等待主线程）
            waitForMainThread()
            assertEquals(1, notified.get())
            val oldColor = ThemeEngine.getTheme().color
            ThemeEngine.applyColor(0x123456)
            // 立即执行（handler.post 异步——等待主线程）
            waitForMainThread()
            assertEquals(0x123456, ThemeEngine.getTheme().color)
            assertTrue(notified.get() >= 2)
            assertNotEquals(oldColor, ThemeEngine.getTheme().color)
        } finally {
            ThemeEngine.unregisterEvent(view)
        }
    }

    @Test
    fun applyColor2AndColor2DarkUpdateIndependently() {
        ThemeEngine.setupThemeEngine(context)
        ThemeEngine.applyColor2(0x111111)
        ThemeEngine.applyColor2Dark(0x222222)
        waitForMainThread()
        val theme = ThemeEngine.getTheme()
        assertEquals(0x111111, theme.color2)
        assertEquals(0x222222, theme.color2Dark)
        // 主色不受影响
        assertTrue(theme.color != 0x111111)
    }

    @Test
    fun unregisterEventStopsNotifications() {
        ThemeEngine.setupThemeEngine(context)
        val notified = AtomicInteger(0)
        val view = View(context)
        ThemeEngine.registerEvent(view) { notified.incrementAndGet() }
        // 注册时立即执行一次（post 异步，等待主线程）
        waitForMainThread()
        assertEquals(1, notified.get())
        ThemeEngine.unregisterEvent(view)
        val count = notified.get()
        ThemeEngine.applyColor(ThemeEngine.getTheme().color + 1)
        waitForMainThread()
        assertEquals(count, notified.get())
    }

    @Test
    fun refreshListenersTriggeredOnApply() {
        ThemeEngine.setupThemeEngine(context)
        val notified = AtomicInteger(0)
        val listener = Runnable { notified.incrementAndGet() }
        ThemeEngine.addRefreshListener(listener)
        try {
            ThemeEngine.applyColor(ThemeEngine.getTheme().color + 1)
            waitForMainThread()
            assertTrue(notified.get() >= 1)
        } finally {
            ThemeEngine.removeRefreshListener(listener)
        }
    }

    @Test
    fun refreshThemeNotifiesBothChannels() {
        ThemeEngine.setupThemeEngine(context)
        val viewNotified = AtomicInteger(0)
        val listenerNotified = AtomicInteger(0)
        val view = View(context)
        ThemeEngine.registerEvent(view) { viewNotified.incrementAndGet() }
        val listener = Runnable { listenerNotified.incrementAndGet() }
        ThemeEngine.addRefreshListener(listener)
        try {
            // 注册时各自立即执行一次（post 异步，等待主线程）
            waitForMainThread()
            assertEquals(1, viewNotified.get())
            ThemeEngine.refreshTheme()
            waitForMainThread()
            assertTrue(viewNotified.get() >= 2)
            assertTrue(listenerNotified.get() >= 1)
        } finally {
            ThemeEngine.unregisterEvent(view)
            ThemeEngine.removeRefreshListener(listener)
        }
    }

    @Test
    fun setCloseSkinModelAndAnimationSpeedUpdate() {
        ThemeEngine.setupThemeEngine(context)
        ThemeEngine.setCloseSkinModel(true)
        ThemeEngine.setAnimationSpeed(3)
        waitForMainThread()
        assertTrue(ThemeEngine.getTheme().closeSkinModel)
        assertEquals(3, ThemeEngine.getTheme().animationSpeed)
    }

    @Test
    fun getSystemAutoTintMatchesNightMode() {
        val prefs = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        prefs.edit().putInt("themeMode", 1).apply()
        assertEquals(Color.BLACK, ThemeEngine.getSystemAutoTint(context))
        prefs.edit().putInt("themeMode", 2).apply()
        assertEquals(Color.WHITE, ThemeEngine.getSystemAutoTint(context))
        prefs.edit().remove("themeMode").apply()
    }

    @Test
    fun getBackgroundFollowsNightMode() {
        ThemeEngine.setupThemeEngine(context)
        val theme = ThemeEngine.getTheme()
        val prefs = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        prefs.edit().putInt("themeMode", 1).apply()
        assertEquals(theme.backgroundLt, theme.getBackground(context))
        prefs.edit().putInt("themeMode", 2).apply()
        assertEquals(theme.backgroundDk, theme.getBackground(context))
        prefs.edit().remove("themeMode").apply()
    }

    /** 等待主线程执行完 handler.post 的回调 */
    private fun waitForMainThread() {
        Thread.sleep(100)
    }
}
