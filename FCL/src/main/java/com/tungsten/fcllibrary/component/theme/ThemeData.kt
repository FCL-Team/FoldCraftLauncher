package com.tungsten.fcllibrary.component.theme

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.ColorUtils
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 主题数据（不可变 data class，替代原 fakefx 属性版 Theme）。
 *
 * 亮暗切换不改变数据本身，动态取色见 [getColor2] 与 [getBackground]；
 * ltColor/dkColor/autoTint 等由主色派生，随 [color] 变化自动重算。
 */
data class ThemeData(
    val color: Int,
    /** 亮色模式次要色（原始值，Java 侧经 [getColor2] 动态取色或 [_getColor2] 取原始值） */
    @get:JvmName("_getColor2")
    val color2: Int,
    val color2Dark: Int,
    /** Java getter 命名为 isFullscreen（Kotlin Boolean 属性默认 getFullscreen） */
    @get:JvmName("isFullscreen")
    val fullscreen: Boolean,
    /** Java getter 命名为 isCloseSkinModel */
    @get:JvmName("isCloseSkinModel")
    val closeSkinModel: Boolean,
    val animationSpeed: Int,
    val backgroundLt: BitmapDrawable,
    val backgroundDk: BitmapDrawable
) {
    /** 主色的亮色变体（HSV 提亮，浅色背景/按压态用） */
    val ltColor: Int
        get() = deriveColor(color, brighten = true)

    /** 主色的暗色变体（HSV 压暗，选中态/进度条等用） */
    val dkColor: Int
        get() = deriveColor(color, brighten = false)

    /** 与主色对比的自动文字色（黑/白） */
    val autoTint: Int
        get() = if (ColorUtils.calculateLuminance(color) >= 0.5) Color.BLACK else Color.WHITE

    /** 半透明自动文字色（提示文字用） */
    val autoHintTint: Int
        get() = if (ColorUtils.calculateLuminance(color) >= 0.5) 0x99000000.toInt() else 0x99FFFFFF.toInt()

    /** 按当前亮暗模式取次要色（亮色用 [color2]，暗色用 [color2Dark]） */
    fun getColor2(): Int {
        val activity = com.tungsten.fcl.FCLApplication.getCurrentActivity()
        return if (activity != null && ThemeEngine.isNightMode(activity)) color2Dark else color2
    }

    /** 按当前亮暗模式取背景 */
    fun getBackground(context: Context): BitmapDrawable {
        val dark = ThemeEngine.isNightMode(context)
        return if (dark) backgroundDk else backgroundLt
    }

    private fun deriveColor(base: Int, brighten: Boolean): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(base, hsv)
        if (brighten) {
            hsv[1] -= (1 - hsv[1]) * 0.3f
            hsv[2] += (1 - hsv[2]) * 0.3f
        } else {
            hsv[1] += (1 - hsv[1]) * 0.3f
            hsv[2] -= (1 - hsv[2]) * 0.3f
        }
        return Color.HSVToColor(hsv)
    }

    companion object {
        /** 从 SharedPreferences("theme") 加载主题（背景缺失时回退内置默认图） */
        @JvmStatic
        fun getTheme(context: Context): ThemeData {
            val preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE)
            val color = preferences.getInt("theme_color", Color.parseColor("#7797CF"))
            val color2 = preferences.getInt("theme_color2", Color.parseColor("#000000"))
            val color2Dark = preferences.getInt("theme_color2_dark", Color.parseColor("#FFFFFF"))
            val fullscreen = preferences.getBoolean("fullscreen", false)
            val closeSkinModel = preferences.getBoolean("close_skin_model", false)
            val animationSpeed = preferences.getInt("animation_speed", 8)
            val lt = ImageUtil.load(context.filesDir.absolutePath + "/background/lt.png")
                .orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_light))
            val dk = ImageUtil.load(context.filesDir.absolutePath + "/background/dk.png")
                .orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark))
            return ThemeData(
                color, color2, color2Dark, fullscreen, closeSkinModel, animationSpeed,
                BitmapDrawable(context.resources, lt),
                BitmapDrawable(context.resources, dk)
            )
        }

        /** 持久化主题（仅持久化可配置字段，派生色与背景不保存） */
        @JvmStatic
        fun saveTheme(context: Context, theme: ThemeData) {
            context.getSharedPreferences("theme", Context.MODE_PRIVATE).edit()
                .putInt("theme_color", theme.color)
                .putInt("theme_color2", theme.color2)
                .putInt("theme_color2_dark", theme.color2Dark)
                .putBoolean("fullscreen", theme.fullscreen)
                .putInt("animation_speed", theme.animationSpeed)
                .putBoolean("close_skin_model", theme.closeSkinModel)
                .apply()
        }
    }
}
