package com.tungsten.fcl.ui

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.ui.FCLPage
import com.tungsten.fcllibrary.component.ui.observeWhileAttached
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CopyOnWriteArrayList

/**
 * PageFlows.observeWhileAttached 的生命周期验证：
 * 未 attach 不收集、attach 立即重放当前值、变化继续渲染、detach 取消、re-attach 重放最新值。
 */
@RunWith(AndroidJUnit4::class)
class PageFlowsTest {

    @Before
    fun setup() {
        FCLPath.loadPaths(ApplicationProvider.getApplicationContext<Context>())
    }

    private fun withMainActivity(block: (MainActivity) -> Unit) {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.use {
            val deadline = SystemClock.uptimeMillis() + 15000
            var activity: MainActivity? = null
            while (SystemClock.uptimeMillis() < deadline) {
                scenario.onActivity { activity = it }
                val a = activity
                if (a != null) {
                    var ready = false
                    InstrumentationRegistry.getInstrumentation().runOnMainSync {
                        ready = runCatching { a.uiManager; true }.getOrDefault(false)
                    }
                    if (ready) {
                        block(a)
                        return@use
                    }
                }
                Thread.sleep(100)
            }
            fail("MainActivity uiManager 未初始化")
        }
    }

    private fun onMain(action: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action)
    }

    private fun newPage(activity: MainActivity): FCLPage =
        object : FCLPage(activity, FCLPage.PAGE_ID_TEMP, R.layout.item_remote_version) {}

    @Test
    fun collectsOnlyWhileAttachedAndReplaysOnReattach() {
        withMainActivity { activity ->
            val state = MutableStateFlow(1)
            val rendered = CopyOnWriteArrayList<Int>()
            val page = newPage(activity)
            page.observeWhileAttached(state) { rendered.add(it) }

            val root = activity.findViewById<ViewGroup>(android.R.id.content)
            // 未 attach：不收集
            onMain { state.value = 2 }
            Thread.sleep(300)
            assertEquals("未 attach 时不应收集", 0, rendered.size)

            // attach：立即重放当前值，后续变化继续渲染
            onMain {
                root.addView(
                    page.contentView,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
            }
            waitForCondition { rendered.contains(2) }
            onMain { state.value = 3 }
            waitForCondition { rendered.contains(3) }

            // detach：收集取消，发射不再渲染
            onMain { root.removeView(page.contentView) }
            Thread.sleep(300)
            onMain { state.value = 4 }
            Thread.sleep(300)
            assertEquals("detach 后不应继续渲染", listOf(2, 3), rendered)

            // 重新 attach：重放最新值 4
            onMain {
                root.addView(
                    page.contentView,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
            }
            waitForCondition { rendered.contains(4) }
            assertEquals(listOf(2, 3, 4), rendered)
        }
    }

    @Test
    fun registersWhenAlreadyAttached() {
        withMainActivity { activity ->
            val root = activity.findViewById<ViewGroup>(android.R.id.content)
            val page = newPage(activity)
            val state = MutableStateFlow(7)
            val rendered = CopyOnWriteArrayList<Int>()
            // 先 attach 再注册：注册时立即启动收集并重放
            onMain { root.addView(page.contentView) }
            Thread.sleep(200)
            page.observeWhileAttached(state) { rendered.add(it) }
            waitForCondition { rendered.contains(7) }
        }
    }

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
}
