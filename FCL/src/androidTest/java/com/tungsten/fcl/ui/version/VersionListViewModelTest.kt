package com.tungsten.fcl.ui.version

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mio.cache.VersionCache
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * VersionListViewModel 的加载语义验证（专用假版本 profile，不依赖设备真实数据、不受其他测试污染）：
 * selectedProfile 驱动自动加载、收敛后条目与仓库 displayVersions 一致且按真实版本降序、
 * forceRefresh（刷新按钮通道）后重新收敛。
 */
@RunWith(AndroidJUnit4::class)
class VersionListViewModelTest {

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
        tempDir = File(context.cacheDir, "fcl_vm_test_${System.nanoTime()}")
        tempDir.mkdirs()
        for (id in listOf("1.0", "2.0", "3.0")) {
            val dir = File(tempDir, "versions/$id")
            dir.mkdirs()
            File(dir, "$id.json").writeText("{\"id\":\"$id\",\"mainClass\":\"com.test.Main$id\"}")
        }
        originalProfile = Profiles.getSelectedProfile()
        testProfile = Profile("VMTest_${System.nanoTime()}", tempDir)
        Profiles.addProfile(testProfile)
        Profiles.setSelectedProfile(testProfile)
        // 同步刷新（setSelectedProfile 的异步刷新不等待）
        testProfile.repository.refreshVersions()
        assertTrue("测试前置：仓库应已加载", testProfile.repository.isLoaded)
    }

    @After
    fun tearDown() {
        VersionCache.invalidate(testProfile)
        originalProfile?.let { Profiles.setSelectedProfile(it) }
        Profiles.removeProfile(testProfile)
        tempDir.deleteRecursively()
    }

    private fun expectedIds(): Set<String> =
        testProfile.repository.displayVersions.map { it.id }
            .collect(java.util.stream.Collectors.toList()).toSet()

    @Test
    fun autoLoadsEntriesForSelectedProfile() = runBlocking {
        val vm = VersionListViewModel()
        val finalState = withTimeout(60_000) {
            var current = vm.state.value
            while (current.profile !== testProfile || current.loading || current.entries.map { it.id }.toSet() != expectedIds()) {
                delay(100)
                current = vm.state.value
            }
            current
        }
        assertEquals(testProfile, finalState.profile)
        assertTrue(finalState.entries.isNotEmpty())
        // 排序：真实游戏版本降序（VM 内 sortEntries 语义，极简 json 版本号相同时按 id 倒序）
        val sorted = finalState.entries.sortedWith(
            compareByDescending<VersionCache.Entry> { it.gameVersion }.thenByDescending { it.id }
        )
        assertEquals(sorted.map { it.id }, finalState.entries.map { it.id })
    }

    /** forceRefresh（刷新按钮通道：invalidate + 仓库重扫 + tick）后重新收敛到一致状态 */
    @Test
    fun forceRefreshConvergesBackToConsistentState() = runBlocking {
        val vm = VersionListViewModel()
        withTimeout(60_000) {
            while (vm.state.value.profile !== testProfile || vm.state.value.entries.isEmpty() || vm.state.value.loading) {
                delay(100)
            }
        }
        vm.forceRefresh()
        withTimeout(60_000) {
            var current = vm.state.value
            while (current.loading || current.entries.map { it.id }.toSet() != expectedIds()) {
                delay(100)
                current = vm.state.value
            }
            assertTrue(current.entries.isNotEmpty())
        }
    }
}
