package com.tungsten.fcl.ui.download

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.ui.FCLPage
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 临时页栈的挂载/卸载契约验证：
 * 弹出后视图必须【立即】离开覆盖层——留在覆盖层期间的旧页仍可接收触摸，
 * 会用该页旧模式下的回调发起操作（曾导致模组被下到光影包目录、收藏存错类别）。
 */
@RunWith(AndroidJUnit4::class)
class DownloadUITempPageTest {

    @Before
    fun setup() {
        FCLPath.loadPaths(ApplicationProvider.getApplicationContext<Context>())
    }

    private fun withDownloadUI(block: (MainActivity, DownloadUI) -> Unit) {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.use {
            val deadline = SystemClock.uptimeMillis() + 15000
            var activity: MainActivity? = null
            while (SystemClock.uptimeMillis() < deadline) {
                scenario.onActivity { activity = it }
                val a = activity
                if (a != null) {
                    var ui: DownloadUI? = null
                    InstrumentationRegistry.getInstrumentation().runOnMainSync {
                        ui = runCatching { a.uiManager.downloadUI }.getOrNull()
                    }
                    val downloadUI = ui
                    if (downloadUI != null) {
                        block(a, downloadUI)
                        return@use
                    }
                }
                Thread.sleep(100)
            }
            throw AssertionError("DownloadUI 未就绪")
        }
    }

    private fun onMain(action: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action)
    }

    @Test
    fun dismissedTempPageViewLeavesOverlayImmediately() {
        withDownloadUI { activity, downloadUI ->
            val page = object : FCLPage(activity, FCLPage.PAGE_ID_TEMP, R.layout.item_remote_version) {}
            onMain { downloadUI.showTempPage(page) }
            assertNotNull("临时页应挂到覆盖层", page.contentView.parent)

            onMain { downloadUI.dismissCurrentTempPage() }
            // 关键断言：立即脱离视图树，不留淡出期可点击窗口
            assertNull("弹出后视图必须立即离开覆盖层（否则旧页可被误触）", page.contentView.parent)
        }
    }

    @Test
    fun dismissAllTempPagesClearsEveryView() {
        withDownloadUI { activity, downloadUI ->
            val pages = List(3) {
                object : FCLPage(activity, FCLPage.PAGE_ID_TEMP, R.layout.item_remote_version) {}
            }
            onMain { pages.forEach { downloadUI.showTempPage(it) } }
            pages.forEach { assertNotNull(it.contentView.parent) }

            onMain { downloadUI.dismissAllTempPages() }
            pages.forEach { assertNull("清栈后所有临时页视图都应离开覆盖层", it.contentView.parent) }
        }
    }
}
