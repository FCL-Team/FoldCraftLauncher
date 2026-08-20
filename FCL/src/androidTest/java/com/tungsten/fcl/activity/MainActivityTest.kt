package com.tungsten.fcl.activity

import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fclauncher.utils.FCLPath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 主界面 ViewPager2 重构与右菜单双指手势的验证：
 * UIManager 8 个页面、switchUI 切换、right_menu 双指滑动区域判定与显隐。
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setup() {
        FCLPath.loadPaths(ApplicationProvider.getApplicationContext<Context>())
    }

    /** 启动 MainActivity 并等待 uiManager 初始化，block 在测试线程执行 */
    private fun withMainActivity(block: (MainActivity) -> Unit) {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.use {
            block(waitForUiManager(scenario))
        }
    }

    /** 在主线程执行 UI 操作（runOnMainSync 内抛出的断言异常会传播到测试线程） */
    private fun onMain(action: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action)
    }

    private fun waitForUiManager(scenario: ActivityScenario<MainActivity>): MainActivity {
        val deadline = SystemClock.uptimeMillis() + 15000
        var activity: MainActivity? = null
        while (SystemClock.uptimeMillis() < deadline) {
            scenario.onActivity { activity = it }
            val a = activity
            if (a != null) {
                var ready = false
                InstrumentationRegistry.getInstrumentation().runOnMainSync {
                    // lateinit 属性外部无法用 ::isInitialized，访问失败即未初始化
                    ready = runCatching {
                        a.uiManager
                        true
                    }.getOrDefault(false)
                }
                if (ready) return a
            }
            Thread.sleep(100)
        }
        fail("MainActivity uiManager 未初始化")
        error("unreachable")
    }

    /** 等待主线程条件满足（条件放最后，支持尾随 lambda） */
    private fun waitForCondition(timeoutMs: Long = 8000, condition: () -> Boolean) {
        val deadline = SystemClock.uptimeMillis() + timeoutMs
        while (SystemClock.uptimeMillis() < deadline) {
            var result = false
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                result = condition()
            }
            if (result) return
            Thread.sleep(100)
        }
        fail("等待条件超时")
    }

    /** 注入双指水平滑动事件序列（不经过真实触摸输入，直接分发） */
    private fun swipe(activity: MainActivity, startX: Float, endX: Float, y: Float) {
        val downTime = SystemClock.uptimeMillis()
        fun obtain(action: Int, pointerCount: Int, x: Float, eventTime: Long): MotionEvent {
            val props = Array(pointerCount) {
                MotionEvent.PointerProperties().apply { id = it }
            }
            val coords = Array(pointerCount) { i ->
                MotionEvent.PointerCoords().apply {
                    this.x = if (i == 0) x else x + 40f
                    this.y = if (i == 0) y else y - 40f
                    pressure = 1f
                    size = 1f
                }
            }
            return MotionEvent.obtain(
                downTime, eventTime, action, pointerCount,
                props, coords, 0, 0, 1f, 1f, 0, 0, 0, 0
            )
        }
        val pointerDown = MotionEvent.ACTION_POINTER_DOWN or (1 shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
        val pointerUp = MotionEvent.ACTION_POINTER_UP or (1 shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
        val steps = 12
        activity.dispatchTouchEvent(obtain(MotionEvent.ACTION_DOWN, 1, startX, downTime))
        activity.dispatchTouchEvent(obtain(pointerDown, 2, startX, downTime + 10))
        for (i in 1..steps) {
            val x = startX + (endX - startX) * i / steps
            activity.dispatchTouchEvent(obtain(MotionEvent.ACTION_MOVE, 2, x, downTime + 20 + i * 16L))
        }
        activity.dispatchTouchEvent(obtain(pointerUp, 2, endX, downTime + 250))
        activity.dispatchTouchEvent(obtain(MotionEvent.ACTION_UP, 1, endX, downTime + 260))
    }

    @Test
    fun uiManagerHasAllEightUIs() {
        withMainActivity { activity ->
            onMain {
                val ui = activity.uiManager
                assertNotNull(ui.mainUI)
                assertNotNull(ui.manageUI)
                assertNotNull(ui.downloadUI)
                assertNotNull(ui.controllerUI)
                assertNotNull(ui.multiplayerUI)
                assertNotNull(ui.settingUI)
                assertNotNull(ui.accountUI)
                assertNotNull(ui.versionUI)
                // 初始页为主界面
                assertEquals(ui.mainUI, ui.currentUI)
                // 右菜单初始可见
                assertEquals(View.VISIBLE, activity.binding.rightMenu.visibility)
            }
        }
    }

    @Test
    fun switchUIChangesCurrentUI() {
        withMainActivity { activity ->
            val ui = activity.uiManager
            onMain {
                assertEquals(ui.mainUI, ui.currentUI)
                ui.switchUI(ui.settingUI)
            }
            waitForCondition { ui.currentUI === ui.settingUI }
            onMain { ui.switchUI(ui.mainUI) }
            waitForCondition { ui.currentUI === ui.mainUI }
        }
    }

    @Test
    fun twoFingerSwipeOutsideRightMenuDoesNotToggle() {
        withMainActivity { activity ->
            onMain {
                val width = activity.binding.root.width
                val y = activity.binding.root.height * 0.5f
                val before = activity.binding.rightMenu.visibility
                // 起点在屏幕左侧（right_menu 区域外），左滑不触发
                swipe(activity, startX = width * 0.1f, endX = width * 0.01f, y = y)
                assertEquals(before, activity.binding.rightMenu.visibility)
            }
        }
    }

    @Test
    fun twoFingerSwipeInRightMenuToggles() {
        withMainActivity { activity ->
            onMain {
                val width = activity.binding.root.width
                val y = activity.binding.root.height * 0.5f
                // 初始可见：区域内右滑隐藏
                swipe(activity, startX = width * 0.9f, endX = width * 0.99f, y = y)
            }
            waitForCondition { activity.binding.rightMenu.visibility == View.GONE }
            onMain {
                val width = activity.binding.root.width
                val y = activity.binding.root.height * 0.5f
                // 隐藏后区域内左滑重新显示
                swipe(activity, startX = width * 0.9f, endX = width * 0.7f, y = y)
                assertEquals(View.VISIBLE, activity.binding.rightMenu.visibility)
                // 已显示时再左滑保持显示
                swipe(activity, startX = width * 0.9f, endX = width * 0.7f, y = y)
                assertEquals(View.VISIBLE, activity.binding.rightMenu.visibility)
            }
        }
    }
}
