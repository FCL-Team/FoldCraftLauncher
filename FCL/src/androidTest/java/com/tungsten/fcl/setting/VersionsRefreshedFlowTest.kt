package com.tungsten.fcl.setting

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Profiles.versionsRefreshed tick 流的行为验证：
 * RefreshedVersionsEvent 仅在选中 Profile 的仓库刷新时 bump tick，其他仓库不触发。
 * （替代已删除的 versionsListeners Consumer 轨道的响应式出口）
 */
@RunWith(AndroidJUnit4::class)
class VersionsRefreshedFlowTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        if (!ConfigHolder.isInit()) {
            ConfigHolder.init()
        }
        Profiles.init()
    }

    /** 轮询等待 tick 前进（事件在后台线程触发），超时失败 */
    private fun awaitTick(from: Int, timeoutMs: Long = 5000): Int {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (Profiles.versionsRefreshed.value > from) return Profiles.versionsRefreshed.value
            Thread.sleep(50)
        }
        return Profiles.versionsRefreshed.value
    }

    private fun awaitTickStable(from: Int, stableMs: Long = 1500) {
        val deadline = System.currentTimeMillis() + stableMs
        while (System.currentTimeMillis() < deadline) {
            assertEquals("tick 不应在无选中仓库刷新时前进", from.toLong(), Profiles.versionsRefreshed.value.toLong())
            Thread.sleep(50)
        }
    }

    @Test
    fun tickBumpsOnSelectedRepositoryRefresh() {
        val before = Profiles.versionsRefreshed.value
        assertTrue(before >= 0)
        Profiles.getSelectedProfile().repository.refreshVersions()
        val after = awaitTick(before)
        assertEquals("选中仓库刷新后 tick 应 +1", before + 1L, after.toLong())
    }

    @Test
    fun tickDoesNotBumpForOtherRepository() {
        val before = Profiles.versionsRefreshed.value
        val otherDir = File(ApplicationProvider.getApplicationContext<Context>().cacheDir, "other_repo_${System.nanoTime()}")
        otherDir.mkdirs()
        try {
            val other = Profile("OtherRepo", otherDir)
            // 其他仓库的刷新事件不应驱动 tick
            other.repository.refreshVersions()
            awaitTickStable(before)
        } finally {
            otherDir.deleteRecursively()
        }
    }
}
