package com.tungsten.fcl.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.tungsten.fcl.ui.bridge.collectAsState
import com.tungsten.fcllibrary.component.theme.ThemeEngine
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

/**
 * 环境自解析版 [FCLTheme]（小步骤 3.2 抽取，原逻辑位于 LegacyBridge.createComposeView）：
 * - Light/Dark/FollowSystem 读 SharedPreferences "launcher" 的 themeMode
 *   （与 FCLActivity.applySavedNightMode 同一数据源），并监听变更即时重组；
 * - 主色/内容色经 observable 属性桥（[collectAsState]）观察 ThemeEngine 当前主题，
 *   取色器修改主题色后 Compose 侧实时联动；引擎未初始化时回落默认 token。
 *
 * 供独立 Compose 根（LegacyBridge.createComposeView、ui/compose/FCLComposeDialog）
 * 直接使用。
 */
@Composable
fun FCLTheme(context: Context, content: @Composable () -> Unit) {
    val launcherPrefs = remember {
        context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
    }
    val themeMode = remember { mutableIntStateOf(launcherPrefs.getInt("themeMode", 0)) }
    DisposableEffect(launcherPrefs) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
            if (key == "themeMode") themeMode.intValue = sp.getInt("themeMode", 0)
        }
        launcherPrefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose { launcherPrefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
    val mode = when (themeMode.intValue) {
        1 -> FCLThemeMode.Light
        2 -> FCLThemeMode.Dark
        else -> FCLThemeMode.FollowSystem
    }

    // ThemeEngine 主题色：observable 属性 → Compose State（引擎未初始化时回落默认 token）
    val engineTheme = remember { ThemeEngine.getInstance().theme }
    val primary = engineTheme?.colorProperty()?.collectAsState()?.value?.toInt()
        ?.let { Color(it) } ?: FCLThemeTokens.BrandPrimary
    val color2 = engineTheme?.color2Property()?.collectAsState()?.value?.toInt()
        ?.let { Color(it) } ?: FCLThemeTokens.Color2LightDefault
    val color2Dark = engineTheme?.color2DarkProperty()?.collectAsState()?.value?.toInt()
        ?.let { Color(it) } ?: FCLThemeTokens.Color2DarkDefault

    FCLTheme(mode = mode, primary = primary, color2 = color2, color2Dark = color2Dark, content = content)
}
