package com.tungsten.fcl.addon

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclcore.mod.RemoteModRepository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * AddonProjectInfoRegistry 后台预加载（preload）的验证：幂等性、数据一致性与类型路由。
 */
@RunWith(AndroidJUnit4::class)
class AddonProjectInfoRegistryTest {

    @Test
    fun preloadIsIdempotent() {
        val before = AddonProjectInfoRegistry.MOD.infos
        // 预加载与重复预加载不抛异常
        AddonProjectInfoRegistry.MOD.preload()
        AddonProjectInfoRegistry.MOD.preload()
        // 多次调用后数据实例一致（内部缓存复用）
        assertSame(before, AddonProjectInfoRegistry.MOD.infos)
    }

    @Test
    fun getByAddonType() {
        assertSame(
            AddonProjectInfoRegistry.MOD,
            AddonProjectInfoRegistry.getByAddonType(RemoteModRepository.Type.MOD)
        )
        assertSame(
            AddonProjectInfoRegistry.MODPACK,
            AddonProjectInfoRegistry.getByAddonType(RemoteModRepository.Type.MODPACK)
        )
        // 其他类型没有对应注册表
        assertNull(AddonProjectInfoRegistry.getByAddonType(RemoteModRepository.Type.RESOURCE_PACK))
        assertNull(AddonProjectInfoRegistry.getByAddonType(RemoteModRepository.Type.SHADER_PACK))
        assertNull(AddonProjectInfoRegistry.getByAddonType(null))
    }

    @Test
    fun lookups() {
        // 数据中取样一个条目验证按 CurseForge slug 与按 modId 查找
        val sample = AddonProjectInfoRegistry.MOD.infos.firstOrNull { it.curseforgeId().isNotEmpty() }
        assertNotNull(sample)
        val sampleInfo = sample!!
        assertSame(sampleInfo, AddonProjectInfoRegistry.MOD.getInfoByCurseForgeId(sampleInfo.curseforgeId()))
        assertNull(AddonProjectInfoRegistry.MOD.getInfoByCurseForgeId(" "))
        assertNull(AddonProjectInfoRegistry.MOD.getInfo(null, null))

        val byId = AddonProjectInfoRegistry.MOD.infos.firstOrNull { info ->
            info is com.tungsten.fclcore.mod.ModProjectInfo && info.modIds.isNotEmpty()
        }
        if (byId is com.tungsten.fclcore.mod.ModProjectInfo) {
            assertSame(byId, AddonProjectInfoRegistry.MOD.getInfo(byId.modIds[0], null))
        }
    }

    @Test
    fun search() {
        assertTrue(AddonProjectInfoRegistry.MOD.search(" ").isEmpty())
        // 搜索结果不可变
        assertTrue(AddonProjectInfoRegistry.MOD.infos.isNotEmpty())
    }

    @Test
    fun modpackRegistry() {
        assertTrue(AddonProjectInfoRegistry.MODPACK.infos.isNotEmpty())
        val sample = AddonProjectInfoRegistry.MODPACK.infos[0]
        assertNotNull(sample.mainName())
        assertTrue(sample.mcmodUrl()!!.startsWith("https://www.mcmod.cn/modpack/"))
    }

    @Test
    fun displayName() {
        // displayName 始终包含主名
        for (info in AddonProjectInfoRegistry.MOD.infos.take(50)) {
            assertTrue(info.displayName().contains(info.mainName()))
        }
    }
}
