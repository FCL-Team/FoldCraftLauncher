package com.tungsten.fcl.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * FCL 主题模式，对应 LauncherSettingPage 的主题 spinner（跟随系统/浅色/深色，
 * design-tokens §5 通道 2）。
 */
enum class FCLThemeMode {
    Light,
    Dark,
    FollowSystem,
}

/**
 * FCL 主题入口：封装 MiuixTheme，向 Compose 树提供 FCL 的 ColorScheme。
 *
 * 颜色 token 全部来自 docs/migration/design-tokens.md（见 FCLColorScheme.kt）；
 * 文字样式暂沿用 Miuix 默认 TextStyles（字号层级映射属后续步骤，见 theme-mapping.md）。
 *
 * @param mode Light / Dark / FollowSystem。FollowSystem 走 compose 的 isSystemInDarkTheme()，
 *             与现有 AppCompatDelegate.setDefaultNightMode 的 uiMode 结果一致。
 * @param primary 预留：用户自定义主题色 1（design-tokens §6，ThemeEngine `color`，默认 #7797CF）。
 * @param color2 预留：用户自定义主题色 2（light，ThemeEngine `color2`，默认 #000000）。
 * @param color2Dark 预留：用户自定义主题色 2（dark，ThemeEngine `color2Dark`，默认 #FFFFFF）。
 *
 * TODO(ThemeEngine 对接)：正式接入时从 SharedPreferences "theme" 读取
 * theme_color / theme_color2 / theme_color2_dark（Theme.java:205-233）作为上述参数，
 * 并以 State 观察取色器修改；壁纸/视频背景（design-tokens §6）在页面层 Box 底置实现，不进 ColorScheme；
 * 模式切换需与 FCLActivity.applySavedNightMode 的 AppCompatDelegate 状态保持同源。
 */
@Composable
fun FCLTheme(
    mode: FCLThemeMode = FCLThemeMode.FollowSystem,
    primary: Color = FCLThemeTokens.BrandPrimary,
    color2: Color = FCLThemeTokens.Color2LightDefault,
    color2Dark: Color = FCLThemeTokens.Color2DarkDefault,
    content: @Composable () -> Unit,
) {
    val dark = when (mode) {
        FCLThemeMode.Light -> false
        FCLThemeMode.Dark -> true
        FCLThemeMode.FollowSystem -> isSystemInDarkTheme()
    }
    val colors = if (dark) {
        fclDarkColorScheme(primary = primary, color2Dark = color2Dark)
    } else {
        fclLightColorScheme(primary = primary, color2 = color2)
    }
    MiuixTheme(colors = colors, content = content)
}
