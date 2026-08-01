package com.tungsten.fcl.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcl.ui.theme.FCLThemeMode
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcllibrary.component.FCLActivity
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.Locale

/**
 * 临时测试入口（小步骤 2.2 主题基座验证用，迁移完成后移除）。
 *
 * 不挂载到任何现有页面，独立 Activity；adb 启动：
 * `adb shell am start -n com.tungsten.fcl.debug/com.tungsten.fcl.activity.ThemeTestActivity`
 * （release 包去掉 applicationId 的 .debug 后缀）
 */
class ThemeTestActivity : FCLActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var mode by remember { mutableStateOf(FCLThemeMode.FollowSystem) }
            FCLTheme(mode = mode) {
                ThemeTestScreen(mode = mode, onModeChange = { mode = it })
            }
        }
    }
}

@Composable
private fun ThemeTestScreen(mode: FCLThemeMode, onModeChange: (FCLThemeMode) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(text = "Miuix 主题基座测试页", style = MiuixTheme.textStyles.title2)
        Text(
            text = "当前模式：${mode.name}（点下方按钮切换 Light/Dark）",
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        )

        Spacer(Modifier.height(12.dp))

        // 模式切换
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(text = "跟随系统", onClick = { onModeChange(FCLThemeMode.FollowSystem) })
            TextButton(text = "浅色", onClick = { onModeChange(FCLThemeMode.Light) })
            TextButton(text = "深色", onClick = { onModeChange(FCLThemeMode.Dark) })
        }

        Spacer(Modifier.height(12.dp))

        // 按钮：主色（brand #7797CF）与默认次要样式
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}, colors = ButtonDefaults.buttonColorsPrimary()) {
                Text(text = "主色按钮")
            }
            Button(onClick = {}) {
                Text(text = "次要按钮")
            }
        }

        Spacer(Modifier.height(12.dp))

        // 行内 Switch + Card 容器
        var inlineChecked by remember { mutableStateOf(true) }
        Card(
            modifier = Modifier.fillMaxWidth(),
            insideMargin = PaddingValues(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "行内 Switch（basic）", modifier = Modifier.weight(1f))
                Switch(checked = inlineChecked, onCheckedChange = { inlineChecked = it })
            }
        }

        Spacer(Modifier.height(12.dp))

        // SwitchPreference：0.9.3 中 SuperSwitch 的等价物（miuix-preference 模块）
        var prefChecked by remember { mutableStateOf(false) }
        Card(modifier = Modifier.fillMaxWidth()) {
            SwitchPreference(
                checked = prefChecked,
                onCheckedChange = { prefChecked = it },
                title = "SwitchPreference（= 0.8.8 的 SuperSwitch）",
                summary = "来自 miuix-preference 模块，设置页标准行",
            )
        }

        Spacer(Modifier.height(12.dp))

        // 令牌色板：直读当前 ColorScheme，验证 design-tokens 映射
        Text(text = "令牌色板（MiuixTheme.colorScheme）", style = MiuixTheme.textStyles.headline1)
        Spacer(Modifier.height(8.dp))
        ColorSwatch("primary (#7797CF)", MiuixTheme.colorScheme.primary)
        ColorSwatch("primaryVariant (dkColor 派生)", MiuixTheme.colorScheme.primaryVariant)
        ColorSwatch("primaryContainer (ltColor 派生)", MiuixTheme.colorScheme.primaryContainer)
        ColorSwatch("onPrimary (autoTint)", MiuixTheme.colorScheme.onPrimary)
        ColorSwatch("background", MiuixTheme.colorScheme.background)
        ColorSwatch("surface", MiuixTheme.colorScheme.surface)
        ColorSwatch("outline (darker_gray)", MiuixTheme.colorScheme.outline)
        // 与 FCLTheme 内部的 dark 判定保持一致（模式覆盖优先于系统设置）
        val dark = when (mode) {
            FCLThemeMode.Light -> false
            FCLThemeMode.Dark -> true
            FCLThemeMode.FollowSystem -> androidx.compose.foundation.isSystemInDarkTheme()
        }
        ColorSwatch(
            "ui_bg_color 补齐（半透明浮层）",
            if (dark) FCLThemeTokens.UiBackgroundDark else FCLThemeTokens.UiBackgroundLight,
        )
    }
}

@Composable
private fun ColorSwatch(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, androidx.compose.foundation.shape.RoundedCornerShape(5.dp)),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "$label  ${String.format(Locale.US, "#%08X", color.toArgb())}",
            style = MiuixTheme.textStyles.body2,
        )
    }
}
