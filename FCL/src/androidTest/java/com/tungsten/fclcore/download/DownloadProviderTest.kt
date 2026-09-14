package com.tungsten.fclcore.download

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.GameComponentType
import com.tungsten.fclcore.task.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * 下载源体系（DownloadProvider 重构）的核心行为验证：
 * wrapper 缓存与热替换、多源回退、各版本列表真实网络刷新。
 */
@RunWith(AndroidJUnit4::class)
class DownloadProviderTest {

    private val apiRoot = "https://bmclapi2.bangbang93.com"

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        // GetTask 的 ETag 缓存依赖 Settings.init() 完成的 CacheRepository 目录初始化；
        // 测试进程不跑 Splash，需要显式触发
        if (!com.tungsten.fcl.setting.ConfigHolder.isInit()) {
            com.tungsten.fcl.setting.ConfigHolder.init()
        }
    }

    private fun mojang() = MojangDownloadProvider()

    private fun bmclapi() = BMCLAPIDownloadProvider(apiRoot)

    private class FailingVersionList : ComponentVersionList<ComponentRemoteVersion>() {
        override fun hasType() = true
        override fun refreshAsync(): Task<*> = Task.runAsync { throw IOException("simulated failure") }
    }

    private class FailingDownloadProvider : DownloadProvider {
        override fun getVersionListURLs() = throw IOException("simulated failure")
        override fun getAssetObjectCandidates(assetObjectLocation: String) = throw IOException("simulated failure")
        override fun injectURL(baseURL: String) = baseURL
        override fun getVersionList(componentType: GameComponentType) = FailingVersionList()
        override fun getConcurrency() = 1
    }

    // ---- DownloadProviderWrapper ----

    @Test(timeout = 60_000)
    fun wrapperCachesVersionListPerComponentType() {
        val wrapper = DownloadProviderWrapper(mojang())
        val game = wrapper.getVersionList(GameComponentType.GAME)
        val forge = wrapper.getVersionList(GameComponentType.FORGE)

        assertEquals(game, wrapper.getVersionList(GameComponentType.GAME))
        assertEquals(forge, wrapper.getVersionList(GameComponentType.FORGE))
        assertNotEquals(game, forge)
    }

    @Test(timeout = 60_000)
    fun wrapperHotSwapDelegatesToNewProvider() {
        val wrapper = DownloadProviderWrapper(mojang())
        val before = wrapper.getVersionListURLs()[0].host

        wrapper.setProvider(bmclapi())

        val after = wrapper.getVersionListURLs()[0].host
        assertEquals("piston-meta.mojang.com", before)
        assertEquals("bmclapi2.bangbang93.com", after)
        // 资产候选同样跟随新 provider
        assertTrue(wrapper.getAssetObjectCandidates("objects/ab/cd")[0].toString().startsWith(apiRoot))
    }

    @Test(timeout = 120_000)
    fun wrapperRefreshMergesIntoCachedInstance() {
        // 回归锚点：代理列表必须复用同一实例，否则页面刷新写入实例 A、读取来自实例 B，列表恒空
        val wrapper = DownloadProviderWrapper(mojang())
        val game = wrapper.getVersionList(GameComponentType.GAME)

        game.refreshAsync("").test()

        val again = wrapper.getVersionList(GameComponentType.GAME)
        assertEquals(game, again)
        assertTrue(again.getVersions("").isNotEmpty())
    }

    // ---- AutoDownloadProvider 多源回退 ----

    @Test(timeout = 180_000)
    fun multipleSourceFallsBackOnFirstSourceFailure() {
        val auto = AutoDownloadProvider(FailingDownloadProvider(), mojang())
        val game = auto.getVersionList(GameComponentType.GAME)

        game.refreshAsync("").test()

        // 首源失败后从 Mojang 拉取成功
        assertTrue(game.getVersions("").isNotEmpty())
    }

    @Test(timeout = 60_000)
    fun multipleSourceFailsWhenAllSourcesFail() {
        val auto = AutoDownloadProvider(FailingDownloadProvider(), FailingDownloadProvider())
        val game = auto.getVersionList(GameComponentType.GAME)

        // 最后一源失败时任务降级为 MINOR：TaskExecutor 不视为致命失败，
        // test() 以 false 表示失败而非抛异常（与上游一致的静默降级设计）
        assertTrue(!game.refreshAsync("").test())
    }

    @Test(timeout = 60_000)
    fun autoDownloadProviderAggregatesCandidates() {
        val auto = AutoDownloadProvider(listOf(mojang(), bmclapi()), listOf(bmclapi()))

        // 版本列表 URL 聚合两个源
        assertEquals(2, auto.getVersionListURLs().size)
        // 文件下载候选来自 fileProviders（BMCLAPI 的 Modrinth 回退双候选）
        val candidates = auto.injectURLWithCandidates("https://api.modrinth.com/v2/search")
        assertEquals(2, candidates.size)
        assertEquals("https://api.modrinth.com/v2/search", candidates[0].toString())
        assertEquals("https://mod.mcimirror.top/modrinth/v2/search", candidates[1].toString())
    }

    // ---- 版本列表真实网络刷新 ----

    @Test(timeout = 120_000)
    fun gameVersionListRefreshesOverNetwork() {
        // 真实链路：BMCLAPI 的大响应（version_manifest 数百 KB）可能被中途掐断，
        // 多源候选链应回退到 Mojang 拉取成功；安装链路按 gameVersion 分桶读写
        val auto = AutoDownloadProvider(listOf(bmclapi(), mojang()), listOf(bmclapi()))
        val list = auto.getVersionList(GameComponentType.GAME)

        // 大响应在此环境可能被链路间歇性截断（EOF 后自愈清缓存），失败重试一次
        var executor = list.refreshAsync("1.20.1").executor()
        var success = executor.test()
        if (!success) {
            executor = list.refreshAsync("1.20.1").executor()
            success = executor.test()
        }

        assertTrue("success=$success exception=" + executor.exception, success &&
                list.getVersions("1.20.1").isNotEmpty() &&
                list.getVersion("1.20.1", "1.20.1").isPresent() &&
                list.isLoaded("1.20.1"))
    }

    @Test(timeout = 120_000)
    fun gameVersionListRefreshesFromMojangDirectly() {
        val list = mojang().getVersionList(GameComponentType.GAME)

        val executor = list.refreshAsync("").executor()
        val success = executor.test()

        assertTrue("success=$success exception=" + executor.exception, success
                && list.getVersions("").isNotEmpty()
                && list.getVersion("1.20.1", "1.20.1").isPresent())
    }

    @Test(timeout = 120_000)
    fun forgeBmclListRefreshesForGameVersion() {
        val list = bmclapi().getVersionList(GameComponentType.FORGE)

        list.refreshAsync("1.20.1").test()

        assertTrue(list.getVersions("1.20.1").isNotEmpty())
    }

    @Test(timeout = 120_000)
    fun fabricListRefreshesOverNetwork() {
        val list = bmclapi().getVersionList(GameComponentType.FABRIC)

        list.refreshAsync().test()

        assertTrue(list.getVersions("1.20.1").isNotEmpty())
    }

    @Test(timeout = 120_000)
    fun legacyFabricListRefreshesOverNetwork() {
        // 新组件：Legacy Fabric 列表（meta.legacyfabric.net）
        val list = mojang().getVersionList(GameComponentType.LEGACY_FABRIC)

        list.refreshAsync().test()

        assertTrue(list.getVersions("1.12.2").isNotEmpty())
    }

    @Test(timeout = 180_000)
    fun legacyFabricApiListRefreshesOverNetwork() {
        // 新组件：Legacy Fabric API 列表（Modrinth slug legacy-fabric-api）
        val list = mojang().getVersionList(GameComponentType.LEGACY_FABRIC_API)

        list.refreshAsync().test()

        assertTrue(list.getVersions("1.12.2").isNotEmpty())
    }

    @Test(timeout = 120_000)
    fun optiFineListRefreshesOverNetwork() {
        // Mojang provider 的 OptiFine 现在也走 BMCL 列表
        val list = mojang().getVersionList(GameComponentType.OPTIFINE)

        list.refreshAsync().test()

        assertTrue(list.getVersion("1.20.1", "1.20.1_HD_U_I6").isPresent() || list.getVersions("1.20.1").isNotEmpty())
    }

    @Test(timeout = 120_000)
    fun neoforgeOfficialListRefreshesOverNetwork() {
        val list = mojang().getVersionList(GameComponentType.NEO_FORGE)

        list.refreshAsync().test()

        assertTrue(list.getVersions("1.20.1").isNotEmpty())
    }

    @Test(timeout = 120_000)
    fun cleanroomListRefreshesOverNetwork() {
        val list = mojang().getVersionList(GameComponentType.CLEANROOM)

        list.refreshAsync().test()

        assertTrue(list.getVersions("1.12.2").isNotEmpty())
    }

    @Test(timeout = 120_000)
    fun wrapperRefreshSynchronousAndCached() {
        // 页面刷新等价的同步等待路径：refreshAsync → executor.test()，完成后立即可从缓存实例读取。
        // 用 FABRIC（小响应）验证缓存语义，避免依赖 version_manifest 大响应在受限链路上的传输成功率
        val wrapper = DownloadProviderWrapper(bmclapi())
        val fabric = wrapper.getVersionList(GameComponentType.FABRIC)
        // 与安装器列表页一致：按真实 gameVersion 刷新，wrapper 把后端该版本桶合并进缓存实例
        val executor = fabric.refreshAsync("1.20.1").executor()
        val success = executor.test()

        assertTrue("success=$success exception=" + executor.exception, success && fabric.getVersions("1.20.1").isNotEmpty())
        // 缓存实例复用：再次 getVersionList 返回同一实例且数据可见
        assertEquals(fabric, wrapper.getVersionList(GameComponentType.FABRIC))
        assertTrue(wrapper.getVersionList(GameComponentType.FABRIC).getVersions("1.20.1").isNotEmpty())
    }
}
