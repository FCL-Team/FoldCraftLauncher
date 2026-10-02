package com.tungsten.fcl.ui.download

import android.content.Context
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.ui.download.common.DownloadPage
import com.tungsten.fcl.ui.download.common.DownloadSearchViewModel
import com.tungsten.fclauncher.utils.FCLPath
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 下载页模式切换恢复的状态机相位验证：
 * 上次搜索失败的模式切回时直接恢复失败态（重试入口），不自动重搜、不静默丢弃失败相位。
 */
@RunWith(AndroidJUnit4::class)
class DownloadPageRestorePhaseTest {

    @Before
    fun setup() {
        FCLPath.loadPaths(ApplicationProvider.getApplicationContext<Context>())
    }

    private fun withReadyActivity(block: (MainActivity) -> Unit) {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.use {
            val deadline = SystemClock.uptimeMillis() + 15000
            var activity: MainActivity? = null
            while (SystemClock.uptimeMillis() < deadline) {
                scenario.onActivity { activity = it }
                val a = activity
                if (a != null && runCatching { a.uiManager.downloadUI }.isSuccess) {
                    // 测试线程上执行回调：onActivity 的 lambda 运行在主线程，
                    // 内部不能再调 runOnMainSync
                    block(a)
                    return@use
                }
                Thread.sleep(100)
            }
            throw AssertionError("MainActivity 未就绪")
        }
    }

    @Test
    fun switchTypeRestoresFailedPhaseWithoutRescanning() {
        withReadyActivity { activity ->
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            lateinit var page: DownloadPage
            lateinit var state: DownloadSearchViewModel.State
            instrumentation.runOnMainSync {
                state = ViewModelProvider(activity)
                    .get(DownloadSearchViewModel::class.java)
                    .getState(DownloadUI.PAGE_ID_DOWNLOAD_MOD)
                // 模拟该模式上次搜索失败的现场
                state.failed = true
                state.result = null
                state.loading = false
                page = DownloadPage(activity)
                page.switchType(DownloadUI.PAGE_ID_DOWNLOAD_MOD)
            }
            // 修复前：result == null 会落到 else 自动重搜——loading 置位、failed 被清掉，
            // 用户切回该模式时网络请求悄然重发；修复后：失败相位被恢复，是否重搜交给重试按钮
            assertFalse("恢复失败态不应发起自动重搜", state.loading)
            assertTrue("失败相位应被保留", state.failed)

            // 等 setFailed 的 UI 落地（androidUIThread 异步投递）后断言失败态视图
            Thread.sleep(200)
            instrumentation.runOnMainSync {
                val content = page.contentView
                assertTrue("失败态应显示重试入口", content.findViewById<View>(R.id.retry).visibility == View.VISIBLE)
                assertTrue("失败态不应显示进度条", content.findViewById<View>(R.id.progress).visibility != View.VISIBLE)
            }
        }
    }
}
