package com.tungsten.fcl.setting

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 下载源设置模型迁移（downloadType/autoChooseDownloadType/versionListSource 旧键
 * → versionListSource/fileDownloadSource 枚举名）与 DownloadProviders 装配联动。
 */
@RunWith(AndroidJUnit4::class)
class DownloadSourceMigrationTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FCLPath.loadPaths(context)
        if (!ConfigHolder.isInit()) {
            ConfigHolder.init()
        }
    }

    private fun migrate(json: String): Config {
        val config = Config.fromJson(json)!!
        return config
    }

    @Test
    fun legacyBalancedWithAutoMapsToDefault() {
        val config = migrate("""{"versionListSource": "balanced", "downloadType": "bmclapi", "autoChooseDownloadType": true}""")
        assertEquals(DownloadSource.DEFAULT.name, config.versionListSource)
        assertEquals(DownloadSource.DEFAULT.name, config.fileDownloadSource)
    }

    @Test
    fun legacyOfficialWithManualMojangMapsToOfficial() {
        val config = migrate("""{"versionListSource": "official", "downloadType": "mojang", "autoChooseDownloadType": false}""")
        assertEquals(DownloadSource.OFFICIAL.name, config.versionListSource)
        assertEquals(DownloadSource.OFFICIAL.name, config.fileDownloadSource)
    }

    @Test
    fun legacyMirrorWithManualBmclapiMapsToMirror() {
        val config = migrate("""{"versionListSource": "mirror", "downloadType": "bmclapi", "autoChooseDownloadType": false}""")
        assertEquals(DownloadSource.MIRROR.name, config.versionListSource)
        assertEquals(DownloadSource.MIRROR.name, config.fileDownloadSource)
    }

    @Test
    fun legacyAutoFollowsVersionListSource() {
        val config = migrate("""{"versionListSource": "mirror", "downloadType": "bmclapi", "autoChooseDownloadType": true}""")
        assertEquals(DownloadSource.MIRROR.name, config.versionListSource)
        // 旧版自动模式下载源跟随版本列表源
        assertEquals(DownloadSource.MIRROR.name, config.fileDownloadSource)
    }

    @Test
    fun newFormatValuesAreNotDowngraded() {
        val config = migrate(
            """{"versionListSource": "OFFICIAL", "fileDownloadSource": "MIRROR", "downloadType": "bmclapi", "autoChooseDownloadType": true}"""
        )
        assertEquals(DownloadSource.OFFICIAL.name, config.versionListSource)
        assertEquals(DownloadSource.MIRROR.name, config.fileDownloadSource)
    }

    @Test
    fun emptyConfigFallsBackToDefault() {
        val config = migrate("{}")
        assertEquals(DownloadSource.DEFAULT.name, config.versionListSource)
        assertEquals(DownloadSource.DEFAULT.name, config.fileDownloadSource)
    }

    @Test
    fun unknownLegacyValueFallsBackToDefault() {
        val config = migrate("""{"versionListSource": "whatever"}""")
        assertEquals(DownloadSource.DEFAULT.name, config.versionListSource)
        assertEquals(DownloadSource.DEFAULT.name, config.fileDownloadSource)
    }

    @Test
    fun toJsonPersistsEnumNames() {
        val config = migrate("{}")
        config.versionListSource = DownloadSource.MIRROR.name
        config.fileDownloadSource = DownloadSource.OFFICIAL.name
        val roundTrip = Config.fromJson(config.toJson())!!
        assertEquals(DownloadSource.MIRROR.name, roundTrip.versionListSource)
        assertEquals(DownloadSource.OFFICIAL.name, roundTrip.fileDownloadSource)
    }

    // ---- DownloadProviders 装配与热切换 ----

    @Test(timeout = 60_000)
    fun downloadProvidersReturnsStableWrapperAndHotSwaps() {
        DownloadProviders.init()
        val provider = DownloadProviders.getDownloadProvider()
        assertSame(provider, DownloadProviders.getDownloadProvider())

        val config = ConfigHolder.config()
        val originalVersionListSource = config.versionListSource
        try {
            config.versionListSource = DownloadSource.MIRROR.name
            assertEquals("bmclapi2.bangbang93.com", provider.versionListURLs[0].host)

            config.versionListSource = DownloadSource.OFFICIAL.name
            assertEquals("piston-meta.mojang.com", provider.versionListURLs[0].host)
        } finally {
            config.versionListSource = originalVersionListSource
        }
    }

    @Test(timeout = 60_000)
    fun officialSourceHasSingleCandidateWhileDefaultAggregates() {
        val config = ConfigHolder.config()
        val originalVersionListSource = config.versionListSource
        val originalFileDownloadSource = config.fileDownloadSource
        try {
            val provider = DownloadProviders.getDownloadProvider()

            config.versionListSource = DownloadSource.OFFICIAL.name
            config.fileDownloadSource = DownloadSource.OFFICIAL.name
            assertEquals(1, provider.versionListURLs.size)

            config.versionListSource = DownloadSource.DEFAULT.name
            config.fileDownloadSource = DownloadSource.DEFAULT.name
            assertEquals(2, provider.versionListURLs.size)
        } finally {
            config.versionListSource = originalVersionListSource
            config.fileDownloadSource = originalFileDownloadSource
        }
    }
}
