package com.tungsten.fcllibrary.component.theme

import android.content.Context
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ThemeData 重构（fakefx 属性 → 不可变 data class + DataStore 持久化）的验证：
 * 派生值计算、动态亮暗取色、持久化往返。
 */
@RunWith(AndroidJUnit4::class)
class ThemeDataTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        FCLPath.loadPaths(context)
    }

    private fun themeData(
        color: Int = Color.parseColor("#7797CF"),
        colorDark: Int = Color.DKGRAY,
        color2: Int = Color.BLACK,
        color2Dark: Int = Color.WHITE
    ) = ThemeData(color, colorDark, color2, color2Dark,
        fullscreen = false, closeSkinModel = false, animationSpeed = 8,
        backgroundLt = BitmapDrawableStub(), backgroundDk = BitmapDrawableStub())

    /** 占位背景（测试不依赖真实图片资源） */
    private class BitmapDrawableStub : android.graphics.drawable.BitmapDrawable() {
        override fun getIntrinsicWidth(): Int = 1
        override fun getIntrinsicHeight(): Int = 1
    }

    @Test
    fun derivedColorsFollowColor() {
        val base = Color.parseColor("#7797CF")
        val theme = themeData(color = base)
        // ltColor 提亮、dkColor 压暗（与主色不同，且互不相同）
        assertNotEquals(base, theme.ltColor)
        assertNotEquals(base, theme.dkColor)
        assertNotEquals(theme.ltColor, theme.dkColor)
        // 亮度关系：提亮后更亮、压暗后更暗
        val ltLum = luminance(theme.ltColor)
        val dkLum = luminance(theme.dkColor)
        val baseLum = luminance(base)
        assertTrue("ltColor 应比主色亮", ltLum > baseLum)
        assertTrue("dkColor 应比主色暗", dkLum < baseLum)
    }

    @Test
    fun copyRecalculatesDerivedColors() {
        val theme = themeData(color = Color.parseColor("#7797CF"))
        val ltBefore = theme.ltColor
        val changed = theme.copy(color = Color.parseColor("#FF0000"))
        // 主色变化后派生值重算（不再等于旧派生值）
        assertNotEquals(ltBefore, changed.ltColor)
        // 未变化字段保持不变
        assertEquals(theme.color2, changed.color2)
        assertEquals(theme.animationSpeed, changed.animationSpeed)
    }

    @Test
    fun autoTintContrastsWithColor() {
        // 亮色主色 → 黑色文字；暗色主色 → 白色文字
        val lightTheme = themeData(color = Color.parseColor("#FFFFFF"))
        assertEquals(Color.BLACK, lightTheme.autoTint)
        assertEquals(0x99000000.toInt(), lightTheme.autoHintTint)
        val darkTheme = themeData(color = Color.parseColor("#000000"))
        assertEquals(Color.WHITE, darkTheme.autoTint)
        assertEquals(0x99FFFFFF.toInt(), darkTheme.autoHintTint)
    }

    @Test
    fun getColor2ReturnsRawValueWithoutActivity() {
        // 无前台 Activity 时（仪器测试环境）回退亮色值，不抛异常
        val theme = themeData(color2 = 0x112233, color2Dark = 0x445566)
        assertEquals(0x112233, theme.getColor2())
        assertEquals(0x112233, theme.color2)
        assertEquals(0x445566, theme.color2Dark)
    }

    @Test
    fun saveAndLoadRoundTrip() {
        val theme = themeData(
            color = 0x123456,
            color2 = 0x654321,
            color2Dark = 0xAABBCC,
        ).copy(fullscreen = true, closeSkinModel = true, animationSpeed = 5)
        ThemeData.saveTheme(context, theme)
        // saveTheme 异步写入，等待 DataStore 落盘完成
        runBlocking { context.themeDataStore.data.first { it.color == 0x123456 } }
        val loaded = ThemeData.getTheme(context)
        assertEquals(0x123456, loaded.color)
        assertEquals(0x654321, loaded.color2)
        assertEquals(0xAABBCC, loaded.color2Dark)
        assertTrue(loaded.fullscreen)
        assertTrue(loaded.closeSkinModel)
        assertEquals(5, loaded.animationSpeed)
        // 背景非空（无自定义背景时回退内置默认图）
        assertTrue(loaded.backgroundLt.intrinsicWidth > 0)
        assertTrue(loaded.backgroundDk.intrinsicWidth > 0)
    }

    @Test
    fun saveKeepsRawColor2NotDynamic() {
        // saveTheme 持久化原始 color2（而非按亮暗动态取值），加载后原始值不变
        val theme = themeData(color2 = 0x111111, color2Dark = 0x222222)
        ThemeData.saveTheme(context, theme)
        runBlocking { context.themeDataStore.data.first { it.color2 == 0x111111 } }
        val loaded = ThemeData.getTheme(context)
        assertEquals(0x111111, loaded.color2)
        assertEquals(0x222222, loaded.color2Dark)
    }

    private fun luminance(color: Int): Float {
        val r = Color.red(color) / 255f
        val g = Color.green(color) / 255f
        val b = Color.blue(color) / 255f
        return 0.2126f * r + 0.7152f * g + 0.0722f * b
    }
}
