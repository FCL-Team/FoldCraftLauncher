package com.tungsten.fcl.ui.manage

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mio.data.ModsChanged
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fclauncher.utils.FCLPath
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 模组列表重载时机的回归验证（对应用户反馈："重新进入模组管理页时列表会无条件刷新"）：
 * - 没有任何落盘/更新时，重进本页不得重扫（observe 每次 attach 都会重放 tick，必须比较后才重扫）
 * - tick 前进过（下载/更新落盘）时，重进本页必须补一次重扫
 */
@RunWith(AndroidJUnit4::class)
class ModListPageRefreshTest {

    private val manageModTabIndex = 3

    private lateinit var tempDir: File
    private lateinit var testProfile: Profile
    private var originalProfile: Profile? = null

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        if (!ConfigHolder.isInit()) {
            ConfigHolder.init()
        }
        Profiles.init()
        // 自给自足：管理页要求"选中 Profile + 选中版本"（App 主页菜单有同款校验：
        // 无版本时跳版本列表而非管理页；测试直连 UIManager 必须自备，否则 loadVersion(null) 会 NPE）。
        // 用带假版本的临时 profile，避免依赖设备上的应用数据。
        tempDir = File(context.cacheDir, "fcl_modlist_test_${System.nanoTime()}")
        File(tempDir, "versions/1.0").mkdirs()
        File(tempDir, "versions/1.0/1.0.json").writeText("{\"id\":\"1.0\",\"mainClass\":\"com.test.Main\"}")
        originalProfile = Profiles.getSelectedProfile()
        testProfile = Profile("ModListTest_${System.nanoTime()}", tempDir)
        Profiles.addProfile(testProfile)
        Profiles.setSelectedProfile(testProfile)
        testProfile.repository.refreshVersions()
        testProfile.selectedVersion = "1.0"
    }

    @After
    fun tearDown() {
        originalProfile?.let { Profiles.setSelectedProfile(it) }
        Profiles.removeProfile(testProfile)
        tempDir.deleteRecursively()
    }

    private fun onMain(action: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(action)
    }

    /** 打开管理页并切到模组页签，返回该 ModListPage 实例 */
    private fun openModTab(uiManager: UIManager): ModListPage {
        onMain {
            uiManager.switchUI(uiManager.manageUI)
            uiManager.manageUI.tabLayout.selectTab(uiManager.manageUI.tabLayout.getTabAt(manageModTabIndex))
        }
        // 首次加载 + observe 首次重放（debounce 300ms）都落定
        SystemClock.sleep(2500)
        var page: ModListPage? = null
        onMain { page = uiManager.manageUI.getPage(manageModTabIndex) as ModListPage }
        return page!!
    }

    private fun withUiManager(block: (MainActivity, UIManager) -> Unit) {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            val deadline = SystemClock.uptimeMillis() + 15000
            var activity: MainActivity? = null
            while (SystemClock.uptimeMillis() < deadline) {
                scenario.onActivity { activity = it }
                val a = activity
                if (a != null) {
                    var ui: UIManager? = null
                    onMain { ui = runCatching { a.uiManager }.getOrNull() }
                    val uiManager = ui
                    if (uiManager != null) {
                        block(a, uiManager)
                        return@use
                    }
                }
                Thread.sleep(100)
            }
            throw AssertionError("UIManager 未就绪")
        }
    }

    @Test
    fun reenteringModTabWithoutChangesDoesNotRescan() {
        withUiManager { _, uiManager ->
            val first = openModTab(uiManager)
            val firstCount = first.scanCount
            assertTrue("首次进入应完成至少一次扫描", firstCount >= 1)

            // 切到下载页再切回（模拟"重新进入模组管理页"）
            onMain { uiManager.switchUI(uiManager.downloadUI) }
            SystemClock.sleep(1500)
            val second = openModTab(uiManager)

            if (second === first) {
                // 页面实例存活（用户反馈的场景）：无落盘时不得重扫
                assertEquals("重进模组页且无变化时不应重扫列表", firstCount, second.scanCount)
            }
        }
    }

    @Test
    fun reenteringModTabAfterChangeRescansOnce() {
        withUiManager { _, uiManager ->
            val first = openModTab(uiManager)
            // 先快照计数：second 与 first 可能是同一实例，断言里现取会自己和自己比
            val beforeCount = first.scanCount
            onMain { uiManager.switchUI(uiManager.downloadUI) }
            SystemClock.sleep(1500)

            // 模拟下载模组落盘：离开期间 tick 前进
            onMain { ModsChanged.notifyChanged() }
            SystemClock.sleep(500)

            val second = openModTab(uiManager)
            if (second === first) {
                assertEquals("离开期间有落盘时，重进应恰好补一次重扫", beforeCount + 1, second.scanCount)
            } else {
                // 实例被平台重建：新实例必然加载一次
                assertTrue(second.scanCount >= 1)
            }
        }
    }
}
