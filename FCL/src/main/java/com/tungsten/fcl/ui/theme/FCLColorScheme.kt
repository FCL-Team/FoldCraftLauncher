package com.tungsten.fcl.ui.theme

import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import top.yukonga.miuix.kmp.theme.Colors
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme

/**
 * FCL 设计令牌常量。
 *
 * 所有色值核实自 docs/migration/design-tokens.md 及其引用的资源文件，
 * 注释中标注来源。深色变体缺失的 token（ui_bg_color / right_menu_color 等，
 * design-tokens §5 明确为空洞）按文档记录的派生规则补齐：以全项目唯一实测的
 * light→dark 表面对（dialog_background #F4F4F4 → #232323）为基准，保留原 alpha。
 */
object FCLThemeTokens {

    /** 品牌主色 `default_theme_color`，FCL/src/main/res/values/colors.xml:3（不分昼夜） */
    val BrandPrimary = Color(0xFF7797CF)

    /** `primary_text` light，FCL/src/main/res/values/colors.xml:9 */
    val PrimaryTextLight = Color(0xFF0E0E0E)

    /** `primary_text` dark，FCL/src/main/res/values-night/colors.xml:3 */
    val PrimaryTextDark = Color(0xFFFFFFFF)

    /** `dialog_background` light，FCLLibrary/src/main/res/values/colors.xml:3；亦为 ui_bg_color 的纯色部分 */
    val SurfaceLight = Color(0xFFF4F4F4)

    /** `dialog_background` dark，FCLLibrary/src/main/res/values-night/colors.xml:3（全项目唯一实测 dark 表面色） */
    val SurfaceDark = Color(0xFF232323)

    /** `ui_bg_color`，FCL/src/main/res/values/colors.xml:6（半透明浮层底色，无 dark 变体） */
    val UiBackgroundLight = Color(0x40F4F4F4)

    /** ui_bg_color 的 dark 补齐：同 alpha(0x40) × dark 表面色 #232323（派生规则见 design-tokens §5） */
    val UiBackgroundDark = Color(0x40232323)

    /** `right_menu_color`，FCL/src/main/res/values/colors.xml:7（无 dark 变体） */
    val RightMenuLight = Color(0x80F4F4F4)

    /** right_menu_color 的 dark 补齐：同 alpha(0x80) × #232323，规则同上 */
    val RightMenuDark = Color(0x80232323)

    /** 列表项/键位描边 `@android:color/darker_gray`，bg_item.xml:13、keycode_view_normal.xml:14 等（无 night 变体，昼夜同色） */
    val StrokeGray = Color(0xFFAAAAAA)

    /** 运行时主题色 2（light）默认值 `theme_color2`，Theme.java:209 */
    val Color2LightDefault = Color(0xFF000000)

    /** 运行时主题色 2（dark）默认值 `theme_color2_dark`，Theme.java:210 */
    val Color2DarkDefault = Color(0xFFFFFFFF)
}

/**
 * 复刻 Theme.java 的 ltColor 派生算法（design-tokens §2）：
 * HSV 中 S −= (1−S)×0.3，V += (1−V)×0.3。见 FCLLibrary Theme.java:40-43。
 */
fun deriveLtColor(color: Color): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    hsv[1] -= (1 - hsv[1]) * 0.3f
    hsv[2] += (1 - hsv[2]) * 0.3f
    return Color(AndroidColor.HSVToColor(hsv))
}

/**
 * 复刻 Theme.java 的 dkColor 派生算法（design-tokens §2）：
 * HSV 中 S += (1−S)×0.3，V −= (1−V)×0.3。见 FCLLibrary Theme.java:44-47。
 */
fun deriveDkColor(color: Color): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    hsv[1] += (1 - hsv[1]) * 0.3f
    hsv[2] -= (1 - hsv[2]) * 0.3f
    return Color(AndroidColor.HSVToColor(hsv))
}

/**
 * 复刻 Theme.getAutoTint()（design-tokens §2）：
 * 亮度 ≥ 0.5 → 黑 #FF000000，否则白 #FFFFFFFF。见 FCLLibrary Theme.java:56。
 */
fun autoTintOn(color: Color): Color =
    if (ColorUtils.calculateLuminance(color.toArgb()) >= 0.5) Color(0xFF000000) else Color(0xFFFFFFFF)

/**
 * FCL 浅色 ColorScheme：以 Miuix `lightColorScheme()` 为底，覆盖有 token 来源的槽位。
 * 未覆盖的槽位（disabled*、error*、surfaceContainer*、slider* 等）沿用 Miuix 默认，
 * 逐项对照见 docs/migration/theme-mapping.md。
 *
 * @param primary 运行时主题色 1（ThemeEngine `color`，默认 #7797CF）
 * @param color2 运行时主题色 2（light，ThemeEngine `color2`，默认 #000000）——引擎用它给文字/图标着色
 */
fun fclLightColorScheme(
    primary: Color = FCLThemeTokens.BrandPrimary,
    color2: Color = FCLThemeTokens.Color2LightDefault,
): Colors {
    val ltColor = deriveLtColor(primary)
    val dkColor = deriveDkColor(primary)
    return lightColorScheme(
        // 品牌主色三件套 + HSV 派生（design-tokens §2）
        primary = primary,
        onPrimary = autoTintOn(primary),
        primaryVariant = dkColor,
        onPrimaryVariant = autoTintOn(dkColor),
        primaryContainer = ltColor,
        onPrimaryContainer = autoTintOn(ltColor),
        // 背景：FCL 页面实际底色是用户壁纸/背景图（design-tokens §6），容器为白色 bg_container_white
        background = Color.White,
        onBackground = color2,
        // 表面：ui_bg_color / dialog_background 的纯色部分 #F4F4F4
        surface = FCLThemeTokens.SurfaceLight,
        onSurface = color2,
        onSurfaceSecondary = color2.copy(alpha = 0.8f),
        onSurfaceVariantSummary = color2.copy(alpha = 0.6f),
        onSurfaceVariantActions = color2.copy(alpha = 0.4f),
        // 描边：@android:color/darker_gray（bg_item.xml 等）
        outline = FCLThemeTokens.StrokeGray,
    )
}

/**
 * FCL 深色 ColorScheme：以 Miuix `darkColorScheme()` 为底，覆盖有 token 来源的槽位。
 * dark 侧 token 稀缺（design-tokens §5 仅 2 项有 night 变体），表面色统一取
 * dialog_background 的 dark 值 #232323；半透明浮层色见 [FCLThemeTokens.UiBackgroundDark]。
 *
 * @param primary 运行时主题色 1（不分昼夜，默认 #7797CF）
 * @param color2Dark 运行时主题色 2（dark，ThemeEngine `color2Dark`，默认 #FFFFFF）
 */
fun fclDarkColorScheme(
    primary: Color = FCLThemeTokens.BrandPrimary,
    color2Dark: Color = FCLThemeTokens.Color2DarkDefault,
): Colors {
    val ltColor = deriveLtColor(primary)
    val dkColor = deriveDkColor(primary)
    return darkColorScheme(
        primary = primary,
        onPrimary = autoTintOn(primary),
        primaryVariant = dkColor,
        onPrimaryVariant = autoTintOn(dkColor),
        primaryContainer = ltColor,
        onPrimaryContainer = autoTintOn(ltColor),
        background = FCLThemeTokens.SurfaceDark,
        onBackground = color2Dark,
        surface = FCLThemeTokens.SurfaceDark,
        onSurface = color2Dark,
        onSurfaceSecondary = color2Dark.copy(alpha = 0.8f),
        // 与 Theme.getAutoHintTint() 的 dark 值 #99FFFFFF 同构（design-tokens §2）
        onSurfaceVariantSummary = color2Dark.copy(alpha = 0.6f),
        onSurfaceVariantActions = color2Dark.copy(alpha = 0.4f),
        outline = FCLThemeTokens.StrokeGray,
    )
}
