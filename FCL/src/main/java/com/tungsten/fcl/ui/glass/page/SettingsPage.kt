package com.tungsten.fcl.ui.glass.page

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassChip
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.setting.compose.setLiquidGlassPreview
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.theme.ThemePreset

@Composable
fun SettingsPage(
    backdrop: Backdrop,
    currentPreset: ThemePreset,
    onPresetChange: (ThemePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("launcher", Context.MODE_PRIVATE) }
    var themeMode by remember { mutableIntStateOf(sharedPreferences.getInt("themeMode", 0)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GlassTopBar(title = stringResource(R.string.menu_settings))

        ThemePreviewCard(
            backdrop = backdrop,
            preset = currentPreset,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassSectionTitle(text = stringResource(R.string.settings_launcher_theme_preset))
        PresetSelector(
            backdrop = backdrop,
            current = currentPreset,
            onSelect = { preset ->
                onPresetChange(preset)
                ThemeEngine.getInstance().applyAndSave(context, preset.getColor())
                ThemeEngine.getInstance().applyAndSave2(context, preset.getColor2())
                ThemeEngine.getInstance().applyAndSave2Dark(context, preset.getColor2Dark())
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassSectionTitle(text = stringResource(R.string.settings_launcher_theme_mode))
        ThemeModeSelector(
            backdrop = backdrop,
            selected = themeMode,
            onSelect = { mode ->
                themeMode = mode
                sharedPreferences.edit().putInt("themeMode", mode).apply()
                val nightMode = when (mode) {
                    1 -> AppCompatDelegate.MODE_NIGHT_NO
                    2 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                AppCompatDelegate.setDefaultNightMode(nightMode)
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassSectionTitle(text = stringResource(R.string.about))
        GlassCard(
            backdrop = backdrop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = rememberAppVersion(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun rememberAppVersion(): String {
    val context = LocalContext.current
    return remember {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}

@Composable
private fun ThemePreviewCard(
    backdrop: Backdrop,
    preset: ThemePreset,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDarkMode = (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES
    GlassCard(backdrop = backdrop, modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = stringResource(R.string.settings_launcher_theme_preview),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            AndroidView(
                factory = { ctx ->
                    ComposeView(ctx).apply {
                        setLiquidGlassPreview(preset, isDarkMode)
                    }
                },
                update = { view ->
                    view.setLiquidGlassPreview(preset, isDarkMode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

@Composable
private fun PresetSelector(
    backdrop: Backdrop,
    current: ThemePreset,
    onSelect: (ThemePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        ThemePreset.values().forEach { preset ->
            val resId = LocalContext.current.resources.getIdentifier(
                "settings_launcher_theme_preset_" + preset.name.lowercase().replace(" ", "_"),
                "string",
                LocalContext.current.packageName
            )
            val label = if (resId != 0) stringResource(resId) else preset.name
            GlassChip(
                backdrop = backdrop,
                text = label,
                selected = preset == current,
                onClick = { onSelect(preset) },
                tint = Color(preset.getColor())
            )
        }
    }
}

@Composable
private fun ThemeModeSelector(
    backdrop: Backdrop,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val modes = listOf(
        0 to R.string.settings_launcher_theme_mode_follow,
        1 to R.string.settings_launcher_theme_mode_light,
        2 to R.string.settings_launcher_theme_mode_dark
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        modes.forEach { (mode, res) ->
            GlassChip(
                backdrop = backdrop,
                text = stringResource(res),
                selected = selected == mode,
                onClick = { onSelect(mode) }
            )
        }
    }
}
