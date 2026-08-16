package com.tungsten.fcl.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclcore.mod.RemoteModRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ModTranslations 后台预加载（preload）的验证：幂等性与数据一致性。
 */
@RunWith(AndroidJUnit4::class)
class ModTranslationsTest {

    @Test
    fun preloadIsIdempotent() {
        val before = ModTranslations.MOD.getMods()
        // 预加载与重复预加载不抛异常
        ModTranslations.MOD.preload()
        ModTranslations.MOD.preload()
        // 多次调用后数据实例一致（内部缓存复用）
        assertSame(before, ModTranslations.MOD.getMods())
    }

    @Test
    fun getTranslationsByRepositoryType() {
        assertEquals(
            ModTranslations.MOD,
            ModTranslations.getTranslationsByRepositoryType(RemoteModRepository.Type.MOD)
        )
        assertEquals(
            ModTranslations.MODPACK,
            ModTranslations.getTranslationsByRepositoryType(RemoteModRepository.Type.MODPACK)
        )
        // 其他类型使用空翻译
        assertEquals(
            ModTranslations.EMPTY,
            ModTranslations.getTranslationsByRepositoryType(RemoteModRepository.Type.RESOURCE_PACK)
        )
        assertEquals(
            ModTranslations.EMPTY,
            ModTranslations.getTranslationsByRepositoryType(RemoteModRepository.Type.SHADER_PACK)
        )
    }

    @Test
    fun emptyTranslationsReturnsEmptyMods() {
        assertEquals(0, ModTranslations.EMPTY.getMods().size)
    }
}
