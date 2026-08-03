package com.tungsten.fcl.ui.setting.compose

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tungsten.fcl.R
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.basic.TextButton
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SliderPreference
import com.tungsten.fcl.ui.compose.FCLSwitchPreference
import top.yukonga.miuix.kmp.preference.WindowSpinnerPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 启动器设置页 Compose 界面（小步骤 3.1）：page_setting_launcher.xml（1073 行，
 * 全工程最大布局）的 Miuix 重构。
 *
 * 组件选型（theme-mapping.md §1.2 结论）：设置行一律 miuix-preference 的
 * SwitchPreference / ArrowPreference / SliderPreference / WindowSpinnerPreference；
 * 三个 Card 分组与遗留布局的三个 bg_container_white 容器一一对应，组内顺序与
 * page_setting_launcher.xml 完全一致。
 *
 * 行为承接：
 * - Composable 只读 uiState、只调 ViewModel 语义化方法（bridge-api.md §3.1）；
 * - 取色器为 Miuix 0.9.3 自带 ColorPicker（替换 FCLColorPickerDialog，三回调语义不变）；
 * - 文件选择/权限/弹窗等经 onEvent 转 LauncherSettingHost（宿主侧遗留逻辑）。
 */
@Composable
fun LauncherSettingScreen(
    onEvent: (LauncherSettingEvent) -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: LauncherSettingViewModel = viewModel(initializer = {
        LauncherSettingViewModel(context.applicationContext as Application)
    })
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onEvent(it) }
    }

    // 根布局保持透明：页面真实底是用户壁纸（design-tokens §6），卡片浮于其上，
    // 与遗留 page_setting_launcher.xml 的透明根 + 白色卡片结构一致。
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
    ) {
        // ---------- 分组一：通用（对齐布局第一个容器 :21-228） ----------
        // 容器底色对齐遗留 auto_linear_background_tint 的 ltColor 染色（= primaryContainer，随主色联动）
        item(key = "general") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                WindowSpinnerPreference(
                    items = languageItems(),
                    selectedIndex = state.languageIndex,
                    title = stringResource(R.string.settings_launcher_language),
                    onSelectedIndexChange = viewModel::setLanguage,
                )
                ArrowPreference(
                    title = stringResource(R.string.settings_launcher_upgrade),
                    summary = stringResource(R.string.settings_launcher_upgrade_check),
                    onClick = viewModel::onCheckUpdate,
                )
                ArrowPreference(
                    title = stringResource(R.string.settings_launcher_cache),
                    summary = stringResource(R.string.settings_launcher_clear_cache),
                    onClick = viewModel::onClearCache,
                )
                ArrowPreference(
                    title = stringResource(R.string.settings_launcher_debug),
                    summary = stringResource(R.string.settings_launcher_launcher_log_export),
                    onClick = viewModel::onExportLog,
                )
                ArrowPreference(
                    title = stringResource(R.string.settings_launcher_request_recording_permission),
                    summary = stringResource(R.string.settings_launcher_request),
                    onClick = viewModel::onRequestAudioPermission,
                )
                FCLSwitchPreference(
                    checked = state.autoExitLauncher,
                    onCheckedChange = viewModel::setAutoExitLauncher,
                    title = stringResource(R.string.settings_launcher_exit_after_launching),
                )
            }
        }

        item(key = "spacer1") { Spacer(Modifier.height(12.dp)) }

        // ---------- 分组二：外观（对齐布局第二个容器 :229-945） ----------
        item(key = "appearance") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                WindowSpinnerPreference(
                    items = themeModeItems(),
                    selectedIndex = state.themeModeIndex,
                    title = stringResource(R.string.settings_launcher_theme_mode),
                    onSelectedIndexChange = viewModel::setThemeMode,
                )
                ColorSettingRow(
                    title = stringResource(R.string.settings_launcher_theme),
                    color = Color(state.themeColor),
                    onReset = { viewModel.resetThemeColor(ColorTarget.PRIMARY) },
                    onFetch = { viewModel.fetchColorFromBackground(ColorTarget.PRIMARY) },
                    onSet = { viewModel.openColorPicker(ColorTarget.PRIMARY) },
                )
                ColorSettingRow(
                    title = stringResource(R.string.settings_launcher_theme2),
                    color = Color(state.themeColor2),
                    onReset = { viewModel.resetThemeColor(ColorTarget.COLOR2) },
                    onFetch = { viewModel.fetchColorFromBackground(ColorTarget.COLOR2) },
                    onSet = { viewModel.openColorPicker(ColorTarget.COLOR2) },
                )
                ColorSettingRow(
                    title = stringResource(R.string.settings_launcher_theme2_dark),
                    color = Color(state.themeColor2Dark),
                    onReset = { viewModel.resetThemeColor(ColorTarget.COLOR2_DARK) },
                    onFetch = { viewModel.fetchColorFromBackground(ColorTarget.COLOR2_DARK) },
                    onSet = { viewModel.openColorPicker(ColorTarget.COLOR2_DARK) },
                )
                FileSettingRow(
                    title = stringResource(R.string.settings_launcher_background_lt),
                    onReset = { viewModel.onResetBackground(true) },
                    onSet = viewModel::onPickBackgroundLight,
                )
                FileSettingRow(
                    title = stringResource(R.string.settings_launcher_background_dk),
                    onReset = { viewModel.onResetBackground(false) },
                    onSet = viewModel::onPickBackgroundDark,
                )
                FileSettingRow(
                    title = stringResource(R.string.settings_launcher_background_video),
                    onReset = viewModel::onResetLiveBackground,
                    onSet = viewModel::onPickLiveBackground,
                )
                SliderPreference(
                    value = state.videoBackgroundVolume.toFloat(),
                    onValueChange = { viewModel.setVideoBackgroundVolume(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_background_video_volume),
                    valueText = state.videoBackgroundVolume.toString(),
                    valueRange = 0f..100f,
                )
                FileSettingRow(
                    title = stringResource(R.string.settings_launcher_cursor),
                    onReset = viewModel::onResetCursor,
                    onSet = viewModel::onPickCursor,
                )
                FileSettingRow(
                    title = stringResource(R.string.settings_launcher_menu_icon),
                    onReset = viewModel::onResetMenuIcon,
                    onSet = viewModel::onPickMenuIcon,
                )
                FCLSwitchPreference(
                    checked = state.ignoreNotch,
                    onCheckedChange = viewModel::setIgnoreNotch,
                    title = stringResource(R.string.settings_launcher_ignore_notch),
                )
                FCLSwitchPreference(
                    checked = state.closeSkinModel,
                    onCheckedChange = viewModel::setCloseSkinModel,
                    title = stringResource(R.string.settings_launcher_close_skin_view),
                )
                SliderPreference(
                    value = state.animationSpeed.toFloat(),
                    onValueChange = { viewModel.setAnimationSpeed(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_animation_speed),
                    valueText = state.animationSpeed.toString(),
                    valueRange = 1f..20f,
                )
                SliderPreference(
                    value = state.vibrationDuration.toFloat(),
                    onValueChange = { viewModel.setVibrationDuration(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_vibrate_duration),
                    valueText = state.vibrationDuration.toString(),
                    valueRange = 20f..500f,
                )
                FCLSwitchPreference(
                    checked = state.disableFullscreenInput,
                    onCheckedChange = viewModel::setDisableFullscreenInput,
                    title = stringResource(R.string.settings_disable_fullscreen_input),
                )
                BasicComponent(
                    title = stringResource(R.string.settings_launcher_custom_launcher_name),
                    bottomAction = {
                        FCLTextField(
                            value = state.customLauncherName,
                            onValueChange = viewModel::setCustomLauncherName,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )
                    },
                )
                FCLSwitchPreference(
                    checked = state.allowScreenshots,
                    onCheckedChange = viewModel::setAllowScreenshots,
                    title = stringResource(R.string.settings_launcher_allow_screenshot),
                )
            }
        }

        item(key = "spacer2") { Spacer(Modifier.height(12.dp)) }

        // ---------- 分组三：下载（对齐布局第三个容器 :946-1073） ----------
        item(key = "download") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                FCLSwitchPreference(
                    checked = state.autoChooseDownloadType,
                    onCheckedChange = viewModel::setAutoChooseDownloadType,
                    title = stringResource(R.string.settings_launcher_download_source),
                    summary = stringResource(R.string.settings_launcher_download_source_auto),
                )
                // 对齐 sourceAuto/source 可见性绑定（visibilityProperty().bind(checkProperty[.not()])）
                if (state.autoChooseDownloadType) {
                    WindowSpinnerPreference(
                        items = listOf(
                            DropdownItem(stringResource(R.string.download_provider_official)),
                            DropdownItem(stringResource(R.string.download_provider_balanced)),
                            DropdownItem(stringResource(R.string.download_provider_mirror)),
                        ),
                        selectedIndex = state.versionListSourceIndex,
                        title = stringResource(R.string.settings_launcher_download_source),
                        onSelectedIndexChange = viewModel::setVersionListSource,
                    )
                } else {
                    WindowSpinnerPreference(
                        items = listOf(
                            DropdownItem(stringResource(R.string.download_provider_mojang)),
                            DropdownItem(stringResource(R.string.download_provider_bmclapi)),
                        ),
                        selectedIndex = state.downloadTypeIndex,
                        title = stringResource(R.string.settings_launcher_download_source),
                        onSelectedIndexChange = viewModel::setDownloadType,
                    )
                }
                FCLSwitchPreference(
                    checked = state.autoDownloadThreads,
                    onCheckedChange = viewModel::setAutoDownloadThreads,
                    title = stringResource(R.string.settings_launcher_download),
                    summary = stringResource(R.string.settings_launcher_download_threads_auto),
                )
                SliderPreference(
                    value = state.downloadThreads.toFloat(),
                    onValueChange = { viewModel.setDownloadThreads(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_download_threads),
                    valueText = state.downloadThreads.toString(),
                    valueRange = 1f..128f,
                )
            }
        }
    }

    // 取色器弹窗（Miuix ColorPicker 替换 FCLColorPickerDialog）
    state.colorPicker?.let { picker ->
        ColorPickerDialog(
            title = when (picker.target) {
                ColorTarget.PRIMARY -> stringResource(R.string.settings_launcher_theme)
                ColorTarget.COLOR2 -> stringResource(R.string.settings_launcher_theme2)
                ColorTarget.COLOR2_DARK -> stringResource(R.string.settings_launcher_theme2_dark)
            },
            color = Color(picker.currentColor),
            onColorChanged = { viewModel.onColorPicking(it.toArgb()) },
            onConfirm = viewModel::confirmColorPicker,
            onDismiss = viewModel::dismissColorPicker,
        )
    }
}

/** 主题色设置行：色板预览 + 重置/取色按钮 + 整行点击打开取色器（对应遗留 Reset/Extract/Set 三按钮）。 */
@Composable
private fun ColorSettingRow(
    title: String,
    color: Color,
    onReset: () -> Unit,
    onFetch: () -> Unit,
    onSet: () -> Unit,
) {
    ArrowPreference(
        title = title,
        startAction = {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(1.dp, MiuixTheme.colorScheme.outline, CircleShape),
            )
        },
        endActions = {
            TextButton(
                text = stringResource(R.string.button_reset),
                onClick = onReset,
            )
            TextButton(
                text = stringResource(R.string.settings_launcher_theme_fetch_background),
                onClick = onFetch,
            )
        },
        onClick = onSet,
    )
}

/** 文件类设置行（背景/指针/菜单图标）：重置按钮 + 整行点击选择文件（对应遗留 Reset/Set 两按钮）。 */
@Composable
private fun FileSettingRow(
    title: String,
    onReset: () -> Unit,
    onSet: () -> Unit,
) {
    ArrowPreference(
        title = title,
        endActions = {
            TextButton(
                text = stringResource(R.string.button_reset),
                onClick = onReset,
            )
        },
        onClick = onSet,
    )
}

/** 语言列表（9 项，顺序与 LauncherSettingPage.java:116-125 一致）。 */
@Composable
private fun languageItems(): List<DropdownItem> = listOf(
    stringResource(R.string.settings_launcher_language_system),
    stringResource(R.string.settings_launcher_language_english),
    stringResource(R.string.settings_launcher_language_simplified_chinese),
    stringResource(R.string.settings_launcher_language_russian),
    stringResource(R.string.settings_launcher_language_brazilian_portuguese),
    stringResource(R.string.settings_launcher_language_persian),
    stringResource(R.string.settings_launcher_language_ukrainian),
    stringResource(R.string.settings_launcher_language_german),
    stringResource(R.string.settings_launcher_language_traditional_chinese_hk),
).map { DropdownItem(it) }

/** 主题模式列表（跟随系统/浅色/深色，顺序与 LauncherSettingPage.java:132-135 一致）。 */
@Composable
private fun themeModeItems(): List<DropdownItem> = listOf(
    stringResource(R.string.settings_launcher_theme_mode_follow),
    stringResource(R.string.settings_launcher_theme_mode_light),
    stringResource(R.string.settings_launcher_theme_mode_dark),
).map { DropdownItem(it) }
