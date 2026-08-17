package com.tungsten.fcllibrary.component.theme

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.ColorUtils
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
        /** 从 DataStore 加载主题（背景缺失时回退内置默认图；首次迁移旧 SharedPreferences 配置） */
        @JvmStatic
        fun getTheme(context: Context): ThemeData {
            val pref = runBlocking { context.themeDataStore.data.first() }
            // 一次性迁移：DataStore 尚未写入过且旧 SharedPreferences 有主题配置时迁移并回写
            val migrated = pref == ThemePreference() &&
                    context.getSharedPreferences("theme", Context.MODE_PRIVATE)
                        .contains("theme_color")
            val effective = if (migrated) migrateFromSharedPreferences(context) else pref
            val lt = ImageUtil.load(context.filesDir.absolutePath + "/background/lt.png")
                .orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_light))
            val dk = ImageUtil.load(context.filesDir.absolutePath + "/background/dk.png")
                .orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark))
            return ThemeData(
                effective.color, effective.color2, effective.color2Dark,
                effective.fullscreen, effective.closeSkinModel, effective.animationSpeed,
                BitmapDrawable(context.resources, lt),
                BitmapDrawable(context.resources, dk)
            )
        }

        /** 读取旧 SharedPreferences 配置并写入 DataStore（一次性迁移） */
        private fun migrateFromSharedPreferences(context: Context): ThemePreference {
            val old = context.getSharedPreferences("theme", Context.MODE_PRIVATE)
            val migrated = ThemePreference(
                color = old.getInt("theme_color", ThemePreference().color),
                color2 = old.getInt("theme_color2", ThemePreference().color2),
                color2Dark = old.getInt("theme_color2_dark", ThemePreference().color2Dark),
                fullscreen = old.getBoolean("fullscreen", ThemePreference().fullscreen),
                closeSkinModel = old.getBoolean(
                    "close_skin_model",
                    ThemePreference().closeSkinModel
                ),
                animationSpeed = old.getInt("animation_speed", ThemePreference().animationSpeed)
            )
            runBlocking {
                context.themeDataStore.updateData { migrated }
            }
            return migrated
        }

        /** 持久化主题（仅持久化可配置字段，派生色与背景不保存）。
         *  异步写入不阻塞调用线程（设置页回调在 UI 线程，同步等待会导致卡顿）；
         *  写入失败静默忽略（与原 SharedPreferences.apply 语义一致） */
        @JvmStatic
        fun saveTheme(context: Context, theme: ThemeData) {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    context.themeDataStore.updateData {
                        ThemePreference(
                            color = theme.color,
                            color2 = theme.color2,
                            color2Dark = theme.color2Dark,
                            fullscreen = theme.fullscreen,
                            closeSkinModel = theme.closeSkinModel,
                            animationSpeed = theme.animationSpeed
                        )
                    }
                }
            }
        }
    }
}
