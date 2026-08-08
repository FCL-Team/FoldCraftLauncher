package com.tungsten.fcl.ui.setting.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLButton
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLSliderPreference
import com.tungsten.fcl.ui.compose.FCLSwitch
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.preference.WindowSpinnerPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 启动器设置页 Compose 界面（小步骤 3.1）：page_setting_launcher.xml（1073 行，
 * 全工程最大布局）的 Miuix 重构。
 *
 * 组件选型（44 控件 1:1 对齐旧 XML）：
 * - spinner/seekbar 行走 WindowSpinnerPreference / FCLSliderPreference
 *   （seekbar 的 suffix 语义保留：%/00/MS）；
 * - 按钮行按旧 XML 还原为 label + 实心 FCLButton（5dp 主题色底，对齐 fcl_button.xml）：
 *   通用组 check_update/clear_cache/export_log/request_audio_record 为 label + 单按钮；
 *   主题色三行为 色块预览 + label + 重置/取色/设置三按钮，按钮按旧
 *   layout_constraintWidth_percent 0.13/0.25/0.11 占行宽、相邻 marginStart 10dp，
 *   minWidth=0（Miuix 默认 minWidth 会按比例宽度撑回去、挤没末位按钮文字）；
 *   背景/指针/菜单图标五行为 label + 重置/设置两按钮（间距 10dp）；
 * - 开关行为 label(+summary) + 共享 FCLSwitch（Material 34×14 轨道 + 20dp 滑块，
 *   对齐旧 FCLSwitch 形态，不用 Miuix SwitchPreference 的 MIUI 形态）；
 * - 自定义名称行为 label + FCLEditText（weight 1、marginStart 20dp）的横向行；
 * - 三个 Card 分组与遗留三个 bg_container_white 容器一一对应，组内顺序与
 *   page_setting_launcher.xml 完全一致。
 *
 * 行为承接：
 * - Composable 只读 uiState、只调 ViewModel 语义化方法（bridge-api.md §3.1）；
 * - 取色器复原旧弹窗：直接复用 FCLLibrary 的 FCLColorPickerDialog（View 弹窗），
 *   三回调语义逐行对齐旧 LauncherSettingPage（:303-360）——onColorChanged 实时预览 /
 *   onPositive applyAndSave 保存 / onNegative 还原初始色，经 ViewModel 既有
 *   openColorPicker/onColorPicking/confirmColorPicker/dismissColorPicker 落地；
 * - 文件选择/权限/弹窗等经 onEvent 转 LauncherSettingHost（宿主侧遗留逻辑）。
 */
@Composable
fun LauncherSettingScreen(
    onEvent: (LauncherSettingEvent) -> Unit = {},
) {
    // Application 由默认 Factory 经 CreationExtras 注入（FCLViewModel 已改 AndroidViewModel）
    val viewModel: LauncherSettingViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
            FCLCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                WindowSpinnerPreference(
                    items = languageItems(),
                    selectedIndex = state.languageIndex,
                    title = stringResource(R.string.settings_launcher_language),
                    onSelectedIndexChange = viewModel::setLanguage,
                )
                ActionSettingRow(
                    title = stringResource(R.string.settings_launcher_upgrade),
                    actionText = stringResource(R.string.settings_launcher_upgrade_check),
                    onClick = viewModel::onCheckUpdate,
                )
                ActionSettingRow(
                    title = stringResource(R.string.settings_launcher_cache),
                    actionText = stringResource(R.string.settings_launcher_clear_cache),
                    onClick = viewModel::onClearCache,
                )
                ActionSettingRow(
                    title = stringResource(R.string.settings_launcher_debug),
                    actionText = stringResource(R.string.settings_launcher_launcher_log_export),
                    onClick = viewModel::onExportLog,
                )
                ActionSettingRow(
                    title = stringResource(R.string.settings_launcher_request_recording_permission),
                    actionText = stringResource(R.string.settings_launcher_request),
                    onClick = viewModel::onRequestAudioPermission,
                )
                SwitchSettingRow(
                    checked = state.autoExitLauncher,
                    onCheckedChange = viewModel::setAutoExitLauncher,
                    title = stringResource(R.string.settings_launcher_exit_after_launching),
                )
            }
        }

        item(key = "spacer1") { Spacer(Modifier.height(12.dp)) }

        // ---------- 分组二：外观（对齐布局第二个容器 :229-945） ----------
        item(key = "appearance") {
            FCLCard(
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
                    onSet = { showLegacyColorPicker(context, viewModel, ColorTarget.PRIMARY) },
                )
                ColorSettingRow(
                    title = stringResource(R.string.settings_launcher_theme2),
                    color = Color(state.themeColor2),
                    onReset = { viewModel.resetThemeColor(ColorTarget.COLOR2) },
                    onFetch = { viewModel.fetchColorFromBackground(ColorTarget.COLOR2) },
                    onSet = { showLegacyColorPicker(context, viewModel, ColorTarget.COLOR2) },
                )
                ColorSettingRow(
                    title = stringResource(R.string.settings_launcher_theme2_dark),
                    color = Color(state.themeColor2Dark),
                    onReset = { viewModel.resetThemeColor(ColorTarget.COLOR2_DARK) },
                    onFetch = { viewModel.fetchColorFromBackground(ColorTarget.COLOR2_DARK) },
                    onSet = { showLegacyColorPicker(context, viewModel, ColorTarget.COLOR2_DARK) },
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
                FCLSliderPreference(
                    value = state.videoBackgroundVolume.toFloat(),
                    onValueChange = { viewModel.setVideoBackgroundVolume(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_background_video_volume),
                    suffix = "%",
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
                SwitchSettingRow(
                    checked = state.ignoreNotch,
                    onCheckedChange = viewModel::setIgnoreNotch,
                    title = stringResource(R.string.settings_launcher_ignore_notch),
                )
                SwitchSettingRow(
                    checked = state.closeSkinModel,
                    onCheckedChange = viewModel::setCloseSkinModel,
                    title = stringResource(R.string.settings_launcher_close_skin_view),
                )
                FCLSliderPreference(
                    value = state.animationSpeed.toFloat(),
                    onValueChange = { viewModel.setAnimationSpeed(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_animation_speed),
                    suffix = "00",
                    valueRange = 1f..20f,
                )
                FCLSliderPreference(
                    value = state.vibrationDuration.toFloat(),
                    onValueChange = { viewModel.setVibrationDuration(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_vibrate_duration),
                    suffix = "MS",
                    valueRange = 20f..500f,
                )
                SwitchSettingRow(
                    checked = state.disableFullscreenInput,
                    onCheckedChange = viewModel::setDisableFullscreenInput,
                    title = stringResource(R.string.settings_disable_fullscreen_input),
                )
                LauncherNameRow(
                    value = state.customLauncherName,
                    onValueChange = viewModel::setCustomLauncherName,
                )
                SwitchSettingRow(
                    checked = state.allowScreenshots,
                    onCheckedChange = viewModel::setAllowScreenshots,
                    title = stringResource(R.string.settings_launcher_allow_screenshot),
                )
            }
        }

        item(key = "spacer2") { Spacer(Modifier.height(12.dp)) }

        // ---------- 分组三：下载（对齐布局第三个容器 :946-1073） ----------
        item(key = "download") {
            FCLCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                SwitchSettingRow(
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
                SwitchSettingRow(
                    checked = state.autoDownloadThreads,
                    onCheckedChange = viewModel::setAutoDownloadThreads,
                    title = stringResource(R.string.settings_launcher_download),
                    summary = stringResource(R.string.settings_launcher_download_threads_auto),
                )
                FCLSliderPreference(
                    value = state.downloadThreads.toFloat(),
                    onValueChange = { viewModel.setDownloadThreads(it.toInt()) },
                    title = stringResource(R.string.settings_launcher_download_threads),
                    valueText = state.downloadThreads.toString(),
                    valueRange = 1f..128f,
                )
            }
        }
    }
}

/**
 * 复原旧调色盘：直接弹出 FCLLibrary 的 [FCLColorPickerDialog]（View 弹窗，仍然存活）。
 * 三回调语义逐行对齐旧 LauncherSettingPage（:303-360，按 [target] 分发到
 * applyColor/applyColor2/applyColor2Dark 系列）：
 * - onColorChanged → 实时预览（仅 apply 不 save）；
 * - onPositive → applyAndSave 落盘；
 * - onNegative → 还原初始色（仅 apply）。
 * 落地走 ViewModel 既有 openColorPicker/onColorPicking/confirmColorPicker/
 * dismissColorPicker 流程（语义完全一致），ViewModel 零改动。
 */
private fun showLegacyColorPicker(
    context: Context,
    viewModel: LauncherSettingViewModel,
    target: ColorTarget,
) {
    viewModel.openColorPicker(target)
    val initColor = viewModel.uiState.value.colorPicker?.initColor ?: return
    FCLColorPickerDialog(context, initColor, object : FCLColorPickerDialog.Listener {
        override fun onColorChanged(color: Int) = viewModel.onColorPicking(color)

        override fun onPositive(destColor: Int) = viewModel.confirmColorPicker()

        override fun onNegative(initColor: Int) = viewModel.dismissColorPicker()
    }).show()
}

/** 行容器统一规格：对齐旧 XML 行（minHeight 48dp、padding 12/8dp、垂直居中）。 */
private fun Modifier.settingRow(): Modifier =
    fillMaxWidth()
        .heightIn(min = 48.dp)
        .padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 8.dp)

/** 行标签：对齐 Miuix BasicComponent 标题（textStyles.main + onBackground，单行省略）。 */
@Composable
private fun SettingRowTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier,
        style = MiuixTheme.textStyles.main,
        color = MiuixTheme.colorScheme.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * 开关设置行：label(+summary) + 共享 [FCLSwitch]（Material 34×14 轨道 + 20dp 圆形滑块，
 * 对齐旧 FCLSwitch 形态），替代 Miuix SwitchPreference 的 MIUI 形态开关；
 * 行高/内边距与相邻行一致（统一 [settingRow]）。
 */
@Composable
private fun SwitchSettingRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    summary: String? = null,
) {
    Row(
        modifier = Modifier.settingRow(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            SettingRowTitle(title)
            if (summary != null) {
                Text(
                    text = summary,
                    style = MiuixTheme.textStyles.footnote1,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        FCLSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/** 标签 + 单按钮行（对齐旧 XML label + FCLButton：check_update/clear_cache/export_log/request_audio_record）。 */
@Composable
private fun ActionSettingRow(
    title: String,
    actionText: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.settingRow(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingRowTitle(title, modifier = Modifier.weight(1f))
        FCLButton(
            onClick = onClick,
            colors = ButtonDefaults.buttonColorsPrimary(),
        ) {
            Text(text = actionText, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

/**
 * 主题色设置行：色块预览 + 标签 + 重置/取色/设置三个实心 FCLButton（对应遗留
 * reset/fetch/set 三按钮）。约束关系对齐旧 XML：标签填满剩余宽度，按钮链式排在行尾，
 * 宽度分别为行宽的 13%/25%/11%（layout_constraintWidth_percent），相邻间距 10dp（marginStart）。
 */
@Composable
private fun ColorSettingRow(
    title: String,
    color: Color,
    onReset: () -> Unit,
    onFetch: () -> Unit,
    onSet: () -> Unit,
) {
    // 整行点击直接打开调色盘（遗留 binding.theme 点击语义）；最右侧「设置」按钮
    // 因 Miuix 最小宽度挤压无法显示文字，按维护者要求移除，行点击替代
    Row(
        modifier = Modifier.settingRow().clickable(onClick = onSet),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, MiuixTheme.colorScheme.outline, CircleShape),
        )
        SettingRowTitle(title, modifier = Modifier.weight(1f))
        FCLButton(
            onClick = onReset,
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(0.13f),
            // 旧 XML 按行宽 13%/25% 定宽：Miuix 默认 minWidth 会按比例宽度撑回去挤没文字，
            // 故两按钮均传 0
            minWidth = 0.dp,
            colors = ButtonDefaults.buttonColorsPrimary(),
        ) {
            Text(
                text = stringResource(R.string.button_reset),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        FCLButton(
            onClick = onFetch,
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(0.25f),
            minWidth = 0.dp,
            colors = ButtonDefaults.buttonColorsPrimary(),
        ) {
            Text(
                text = stringResource(R.string.settings_launcher_theme_fetch_background),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** 文件类设置行：标签 + 重置/设置两个实心 FCLButton（对应遗留 Reset/Set 两按钮，间距 10dp）。 */
@Composable
private fun FileSettingRow(
    title: String,
    onReset: () -> Unit,
    onSet: () -> Unit,
) {
    Row(
        modifier = Modifier.settingRow(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingRowTitle(title, modifier = Modifier.weight(1f))
        FCLButton(
            onClick = onReset,
            colors = ButtonDefaults.buttonColorsPrimary(),
        ) {
            Text(
                text = stringResource(R.string.button_reset),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        FCLButton(
            onClick = onSet,
            modifier = Modifier.padding(start = 10.dp),
            colors = ButtonDefaults.buttonColorsPrimary(),
        ) {
            Text(
                text = stringResource(R.string.button_set),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** 自定义启动器名称行（对齐旧 XML：label + FCLEditText weight 1、marginStart 20dp、singleLine）。 */
@Composable
private fun LauncherNameRow(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.settingRow(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SettingRowTitle(stringResource(R.string.settings_launcher_custom_launcher_name))
        FCLTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp),
            singleLine = true,
        )
    }
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
