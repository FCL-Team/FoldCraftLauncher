package com.tungsten.fcl.ui.download.version

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fclcore.game.GameComponentType
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * DownloadListViewModel 状态机验证（真实 DownloadProviders + 真机网络）：
 * Idle → Loaded 收敛且清单非空；isLoaded 命中时 load 直接 Loaded（快路径，不重拉网络）；
 * force 重走网络。清单单例跨 VM 共享（DownloadProviderWrapper 缓存），与页面行为一致。
 */
@RunWith(AndroidJUnit4::class)
class DownloadListViewModelTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        if (!ConfigHolder.isInit()) {
            ConfigHolder.init()
        }
    }

    private suspend fun awaitLoaded(vm: DownloadListViewModel, timeoutMs: Long = 30_000): DownloadListViewModel.State.Loaded =
        withContext(Dispatchers.Default) {
            val deadline = System.currentTimeMillis() + timeoutMs
            while (System.currentTimeMillis() < deadline) {
                val state = vm.state.value
                if (state is DownloadListViewModel.State.Loaded) return@withContext state
                if (state is DownloadListViewModel.State.Failed) throw AssertionError("清单加载失败", state.error)
                Thread.sleep(100)
            }
            throw AssertionError("等待 Loaded 超时，当前状态: ${vm.state.value}")
        }

    @Test
    fun loadGameManifestConvergesToLoaded() = runBlocking {
        val vm = DownloadListViewModel()
        vm.load("", GameComponentType.GAME, false)
        val state = awaitLoaded(vm)
        // Mojang 全量清单（含未列出版本）必然非空
        assertTrue("游戏清单不应为空", state.versions.isNotEmpty())
    }

    /** isLoaded 命中（前序已拉过清单）：新 VM 的 load 直接 Loaded，不经过 Loading（不发网络） */
    @Test
    fun loadWithLoadedListSkipsNetworkPhase() = runBlocking {
        // 保证清单已加载（无论同进程前序测试是否运行过）
        val warm = DownloadListViewModel()
        warm.load("", GameComponentType.GAME, false)
        awaitLoaded(warm)

        val vm = DownloadListViewModel()
        vm.load("", GameComponentType.GAME, false)
        // 快路径同步回填：短暂等待内必须已是 Loaded，不能出现 Loading（Loading 意味着发起了网络任务）
        withContext(Dispatchers.Default) {
            val deadline = System.currentTimeMillis() + 1_500
            while (vm.state.value is DownloadListViewModel.State.Idle && System.currentTimeMillis() < deadline) {
                Thread.sleep(50)
            }
        }
        val loaded = vm.state.value as? DownloadListViewModel.State.Loaded
        assertTrue(
            "快路径应直接 Loaded（实际 ${vm.state.value::class.java.simpleName}）",
            loaded != null
        )
        assertTrue(loaded!!.versions.isNotEmpty())
    }

    @Test
    fun forceReloadGoesBackToLoaded() = runBlocking {
        val vm = DownloadListViewModel()
        vm.load("", GameComponentType.GAME, false)
        awaitLoaded(vm)
        vm.load("", GameComponentType.GAME, force = true)
        val state = awaitLoaded(vm)
        assertTrue(state.versions.isNotEmpty())
    }

    @Test
    fun loadComponentListForGameVersion() = runBlocking {
        val vm = DownloadListViewModel()
        // 设备上真实存在的组合：1.20.1 + forge
        vm.load("1.20.1", GameComponentType.fromPatchId("forge"), false)
        val state = awaitLoaded(vm)
        assertTrue("1.20.1 的 forge 清单不应为空", state.versions.isNotEmpty())
        assertTrue(state.versions.all { it.gameVersion == "1.20.1" })
    }
}
