package com.tungsten.fcl.ui.manage.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLCheckBox
import com.tungsten.fcl.ui.compose.FCLDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLSliderHeight
import com.tungsten.fcl.ui.compose.FCLSwitchPreference
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fclcore.util.platform.MemoryUtils
import kotlinx.coroutines.flow.StateFlow
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.ProgressIndicatorDefaults
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SliderDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 版本设置页 Compose 界面（小步骤 3.3b）：page_version_setting.xml（763 行，
 * 全工程第二大布局）的 Miuix 重构。三个 Card 分组与遗留三个 bg_container_white
 * 容器一一对应，组内顺序与 XML 完全一致；专用设置卡片（开关 + 图标行）对应
 * special_setting_layout，仅单版本页显示（对齐 :175 的 GONE/VISIBLE）。
 *
 * 组件选型（bridge-api.md §3.3 对照表）：
 * - FXUtils.bindBoolean × 10 → SwitchPreference（check_auto_allocate 复原为圆圈套球
 *   FCLCheckBox；写即回写 observable 属性）；
 * - FXUtils.bindString × 4 → BasicComponent + TextField（逐键回写，等价 EditText 绑定）；
 * - barMemory/maxMemory 双向 → memory_state 文本 + Slider + 可点数值文本行（范围
 *   0..设备总内存，对齐 FCLNumberSeekBar.setMax(MemoryUtils.getTotalDeviceMemory)），
 *   点击数值弹输入弹窗（对齐 FCLNumberSeekBar.java:122-136）；
 * - 可见性绑定（settingLayout / driverContainer / disableProperty）→ if (state.x)。
 *
 * 染色对齐旧版 page_version_setting.xml / FCLLibrary 组件主题（见文件底部
 * "旧版染色通道对齐" 一组 helper）：
 * - 容器 auto_linear_background_tint → ltColor（primaryContainer）；
 * - 文本 auto_text_tint / FCLImageButton auto_tint → autoTint（onPrimary）；
 * - FCLSwitch thumb/track、FCLEditText 背景、FCLNumberSeekBar/FCLProgressBar
 *   进度 → color/dkColor（primary / primaryVariant）。
 *
 * 与遗留的有意偏差：
 * - JVM/游戏参数的长按全屏编辑 → 行尾编辑按钮（Compose TextField 长按被文本选择占用）；
 * - 内存条 firstProgress(已用)/secondProgress(已用+将分配) 双段 → 双层
 *   LinearProgressIndicator（底层半透明=将分配总量，上层=已用）。
 */
@Composable
fun VersionSettingScreen(
    globalSetting: Boolean,
    notifyRunDirectoryChange: Boolean,
    loadRequests: StateFlow<VersionSettingLoadRequest?>,
    memoryTicks: StateFlow<Int>,
) {
    val context = LocalContext.current
    // Application 经 CreationExtras 注入，不再捕获组合期 LocalContext；
    // 同一 Activity ViewModelStore 内全局页/管理页各一个实例，key 必须区分
    val viewModel: VersionSettingViewModel = viewModel(
        key = if (globalSetting) "versionSetting_global" else "versionSetting_manage",
        initializer = {
            VersionSettingViewModel(
                checkNotNull(this[APPLICATION_KEY]),
                globalSetting,
                notifyRunDirectoryChange,
            )
        },
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // PageManager 的 loadVersion 经 StateFlow 补放（组合建立前后都安全）
    val loadRequest by loadRequests.collectAsStateWithLifecycle()
    LaunchedEffect(loadRequest) {
        loadRequest?.let { viewModel.loadVersion(it.profile, it.versionId) }
    }
    val memoryTick by memoryTicks.collectAsStateWithLifecycle()
    LaunchedEffect(memoryTick) {
        if (memoryTick > 0) viewModel.refreshMemory()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                // 需要 ViewModel 回调的事件在此接线
                VersionSettingEvent.PickIcon ->
                    VersionSettingHost.pickIcon(context, viewModel::onIconPicked)

                VersionSettingEvent.ShowJavaManage ->
                    VersionSettingHost.showJavaManage(context, viewModel::onJavaSelected)

                VersionSettingEvent.ShowControllerSelect ->
                    VersionSettingHost.showControllerSelect(
                        context,
                        viewModel.currentControllerId(),
                        viewModel::onControllerSelected,
                    )

                VersionSettingEvent.ShowGraphicsBackendSelect ->
                    VersionSettingHost.showGraphicsBackendSelect(context, viewModel::onGraphicsBackendSelected)

                VersionSettingEvent.EditForceResolution ->
                    VersionSettingHost.editForceResolution(context, viewModel::onForceResolutionDialogCancel)

                is VersionSettingEvent.FullEditArgs ->
                    VersionSettingHost.fullEditText(context, event.current) { newText ->
                        when (event.target) {
                            ArgsTarget.JVM -> viewModel.setJavaArgs(newText)
                            ArgsTarget.MINECRAFT -> viewModel.setMinecraftArgs(newText)
                        }
                    }

                else -> VersionSettingHost.handle(context, event, globalSetting)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
    ) {
        // ---------- 专用设置卡片（special_setting_layout，仅单版本页，对齐 :17-90） ----------
        if (!state.globalSetting) {
            item(key = "special") {
                ThemedCard(modifier = Modifier.fillMaxWidth()) {
                    ThemedSwitchPreference(
                        checked = state.enableSpecificSettings,
                        onCheckedChange = viewModel::setEnableSpecificSettings,
                        title = stringResource(R.string.settings_type_special_enable),
                        enabled = !state.modpack,
                    )
                    if (state.versionId != null) {
                        BasicComponent(
                            title = stringResource(R.string.settings_icon),
                            titleColor = autoTintComponentColors(),
                            endActions = {
                                val iconBitmap = remember(state.iconDrawable) {
                                    runCatching { state.iconDrawable?.toBitmap()?.asImageBitmap() }.getOrNull()
                                }
                                if (iconBitmap != null) {
                                    Image(
                                        bitmap = iconBitmap,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                    )
                                }
                                IconButton(onClick = viewModel::onEditIcon) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_baseline_edit_24),
                                        contentDescription = null,
                                        tint = MiuixTheme.colorScheme.onPrimary,
                                    )
                                }
                                IconButton(onClick = viewModel::onDeleteIcon) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_baseline_close_24),
                                        contentDescription = null,
                                        tint = MiuixTheme.colorScheme.onPrimary,
                                    )
                                }
                            },
                        )
                    }
                }
            }
            item(key = "spacer0") { Spacer(Modifier.height(12.dp)) }
        }

        // settingLayout.visibilityProperty().bind(enableSpecificSettings)（全局页常显）
        if (state.globalSetting || state.enableSpecificSettings) {
            // ---------- 分组一：游戏（对齐 :98-287） ----------
            item(key = "game") {
                ThemedCard(modifier = Modifier.fillMaxWidth()) {
                    ValueSettingRow(
                        title = stringResource(R.string.settings_game_java_version),
                        value = if (state.javaName == "Auto") {
                            stringResource(R.string.settings_game_java_version_auto)
                        } else {
                            state.javaName
                        },
                        onEdit = viewModel::onEditJava,
                        onInstall = viewModel::onInstallJava,
                    )
                    ThemedSwitchPreference(
                        checked = state.isolateGameDir,
                        onCheckedChange = viewModel::setIsolateGameDir,
                        title = stringResource(R.string.settings_game_working_directory),
                        enabled = !state.modpack,
                    )
                    MemoryBlock(
                        state = state,
                        onAutoMemoryChange = viewModel::setAutoMemory,
                        onMaxMemoryChange = viewModel::setMaxMemory,
                    )
                    TextSettingRow(
                        title = stringResource(R.string.settings_advanced_server_ip),
                        value = state.serverIp,
                        onValueChange = viewModel::setServerIp,
                    )
                }
            }

            item(key = "spacer1") { Spacer(Modifier.height(12.dp)) }

            // ---------- 分组二：控制器/渲染（对齐 :289-547） ----------
            item(key = "render") {
                ThemedCard(modifier = Modifier.fillMaxWidth()) {
                    ValueSettingRow(
                        title = stringResource(R.string.settings_fcl_controller),
                        value = state.controllerName,
                        onEdit = viewModel::onEditController,
                        onInstall = viewModel::onInstallController,
                    )
                    ValueSettingRow(
                        title = stringResource(R.string.settings_fcl_graphics_backend),
                        value = state.graphicsBackend,
                        onEdit = viewModel::onEditGraphicsBackend,
                    )
                    ValueSettingRow(
                        title = stringResource(R.string.settings_fcl_renderer),
                        value = state.rendererDes,
                        onEdit = viewModel::onEditRenderer,
                        onInstall = viewModel::onInstallRenderer,
                    )
                    ThemedSwitchPreference(
                        checked = state.pojavBigCore,
                        onCheckedChange = viewModel::setPojavBigCore,
                        title = stringResource(R.string.settings_fcl_pojav_bigcore),
                    )
                    ThemedSwitchPreference(
                        checked = state.vkDriverSystem,
                        onCheckedChange = viewModel::setVkDriverSystem,
                        title = stringResource(R.string.settings_fcl_vulkan_driver_system),
                    )
                    // driverContainer 显隐：系统 Vulkan 驱动开启时隐藏驱动行
                    if (!state.vkDriverSystem) {
                        ValueSettingRow(
                            title = stringResource(R.string.settings_fcl_driver),
                            value = state.driver,
                            onEdit = viewModel::onEditDriver,
                            onInstall = viewModel::onInstallDriver,
                        )
                    }
                }
            }

            item(key = "spacer2") { Spacer(Modifier.height(12.dp)) }

            // ---------- 分组三：高级（对齐 :549-756） ----------
            item(key = "advanced") {
                ThemedCard(modifier = Modifier.fillMaxWidth()) {
                    ThemedSwitchPreference(
                        checked = state.notCheckGame,
                        onCheckedChange = viewModel::setNotCheckGame,
                        title = stringResource(R.string.settings_advanced_dont_check_game_completeness),
                    )
                    ThemedSwitchPreference(
                        checked = state.notCheckJVM,
                        onCheckedChange = viewModel::setNotCheckJVM,
                        title = stringResource(R.string.settings_advanced_dont_check_jvm_validity),
                    )
                    ThemedSwitchPreference(
                        checked = state.notCheckMod,
                        onCheckedChange = viewModel::setNotCheckMod,
                        title = stringResource(R.string.settings_advanced_dont_check_mod),
                    )
                    ThemedSwitchPreference(
                        checked = state.debugLog,
                        onCheckedChange = viewModel::setDebugLog,
                        title = stringResource(R.string.settings_advanced_debug_log),
                    )
                    TextSettingRow(
                        title = stringResource(R.string.settings_advanced_minecraft_arguments),
                        value = state.minecraftArgs,
                        onValueChange = viewModel::setMinecraftArgs,
                        onFullEdit = { viewModel.onFullEditArgs(ArgsTarget.MINECRAFT) },
                    )
                    TextSettingRow(
                        title = stringResource(R.string.settings_advanced_jvm_args),
                        value = state.javaArgs,
                        onValueChange = viewModel::setJavaArgs,
                        onFullEdit = { viewModel.onFullEditArgs(ArgsTarget.JVM) },
                    )
                    BasicComponent(
                        title = stringResource(R.string.settings_advanced_env),
                        titleColor = autoTintComponentColors(),
                        endActions = {
                            IconButton(onClick = viewModel::onEditEnv) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_baseline_settings_24),
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.onPrimary,
                                )
                            }
                        },
                        onClick = viewModel::onEditEnv,
                    )
                    TextSettingRow(
                        title = stringResource(R.string.settings_advanced_custom_uuid),
                        value = state.uuid,
                        onValueChange = viewModel::setUuid,
                    )
                    ThemedSwitchPreference(
                        checked = state.forceResolution,
                        onCheckedChange = viewModel::setForceResolution,
                        title = stringResource(R.string.settings_advanced_force_resolution),
                    )
                }
            }
        }
    }
}

/**
 * 内存区块（对齐 :118-173 + XML :174-250）：「内存」label + 自动分配复选框
 * （check_auto_allocate，圆圈套球 FCLCheckBox）+ memory_state/分配滑杆行（点击数值
 * 弹输入弹窗，对齐 FCLNumberSeekBar.java:122-136）+ 内存占用条 + 两行信息文本
 * （文本格式串与遗留 AndroidUtils.getLocalizedText 调用一致）。
 */
@Composable
private fun MemoryBlock(
    state: VersionSettingUiState,
    onAutoMemoryChange: (Boolean) -> Unit,
    onMaxMemoryChange: (Int) -> Unit,
) {
    val context = LocalContext.current
    // 「内存」label（对齐 XML :181-187 的 FCLTextView）
    BasicComponent(
        title = stringResource(R.string.settings_memory),
        titleColor = autoTintComponentColors(),
    )
    // check_auto_allocate 复选框（对齐 XML :188-194；旧 CheckBox 文本随框一并响应点击）
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FCLCheckBox(
            state = if (state.autoMemory) ToggleableState.On else ToggleableState.Off,
            onClick = { onAutoMemoryChange(!state.autoMemory) },
        )
        Text(
            text = stringResource(R.string.settings_memory_auto_allocate),
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
            modifier = Modifier.clickable { onAutoMemoryChange(!state.autoMemory) },
        )
    }

    // memory_state + bar_memory（对齐 XML :196-219）：状态文本 + 滑杆 + 可点数值文本
    var showMemoryInput by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(
                if (state.autoMemory) R.string.settings_memory_lower_bound else R.string.settings_memory,
            ),
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
            maxLines = 1,
        )
        // 对齐 XML bar_memory marginStart=10dp
        Spacer(Modifier.width(10.dp))
        Slider(
            value = state.maxMemory.toFloat(),
            onValueChange = { onMaxMemoryChange(it.toInt()) },
            modifier = Modifier.weight(1f),
            valueRange = 0f..state.totalMemoryMB.toFloat(),
            height = FCLSliderHeight,
            // 对齐 FCLNumberSeekBar：thumb/progress tint = dkColor（= primaryVariant）
            colors = SliderDefaults.sliderColors(
                foregroundColor = MiuixTheme.colorScheme.primaryVariant,
                thumbColor = MiuixTheme.colorScheme.primaryVariant,
            ),
        )
        // 数值文本（对齐 FCLNumberSeekBar 画在 thumb 处的 progress+suffix）：点击弹数值输入框
        Text(
            text = "${state.maxMemory}MB",
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 10.dp)
                .clickable { showMemoryInput = true },
        )
    }
    if (showMemoryInput) {
        MemoryInputDialog(
            totalMemoryMB = state.totalMemoryMB,
            onConfirm = onMaxMemoryChange,
            onDismiss = { showMemoryInput = false },
        )
    }

    // 空闲内存在拖动/刷新时重取（对齐遗留 StringBinding 每次失效重算）；context 补 key
    val freeMemoryMB = remember(context, state.usedMemoryMB, state.maxMemory, state.autoMemory) {
        MemoryUtils.getFreeDeviceMemory(context)
    }
    val maxMemoryBytes = state.maxMemory * 1024L * 1024L
    val allocatedBytes = FCLGameRepository.getAllocatedMemory(
        maxMemoryBytes,
        freeMemoryMB * 1024L * 1024L,
        state.autoMemory,
    )
    val allocatedMB = (allocatedBytes / 1024.0 / 1024.0).toInt()
    val infoText = AndroidUtils.getLocalizedText(
        context,
        "settings_memory_used_per_total",
        state.usedMemoryMB / 1024.0,
        state.totalMemoryMB / 1024.0,
    )
    val allocateText = AndroidUtils.getLocalizedText(
        context,
        if (maxMemoryBytes / 1024.0 / 1024.0 > freeMemoryMB) {
            if (state.autoMemory) "settings_memory_allocate_auto_exceeded" else "settings_memory_allocate_manual_exceeded"
        } else {
            if (state.autoMemory) "settings_memory_allocate_auto" else "settings_memory_allocate_manual"
        },
        maxMemoryBytes / 1024.0 / 1024.0 / 1024.0,
        allocatedBytes / 1024.0 / 1024.0 / 1024.0,
        freeMemoryMB / 1024.0,
    )
    val usedFraction = (state.usedMemoryMB / state.totalMemoryMB.toFloat()).coerceIn(0f, 1f)
    val secondFraction = (
        (state.usedMemoryMB + if (state.autoMemory) allocatedMB else state.maxMemory) /
            state.totalMemoryMB.toFloat()
        ).coerceIn(0f, 1f)

    BasicComponent(
        title = infoText,
        titleColor = autoTintComponentColors(),
        summary = allocateText,
        summaryColor = autoTintComponentColors(),
        bottomAction = {
            // 对齐 FCLProgressBar：progress/secondaryProgress tint = dkColor（= primaryVariant）
            val progressColors = ProgressIndicatorDefaults.progressIndicatorColors(
                foregroundColor = MiuixTheme.colorScheme.primaryVariant,
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                // 底层：已用 + 将分配（对齐 memoryBar.secondProgress）
                LinearProgressIndicator(
                    progress = secondFraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.35f),
                    colors = progressColors,
                )
                // 上层：已用（对齐 memoryBar.firstProgress）
                LinearProgressIndicator(
                    progress = usedFraction,
                    modifier = Modifier.fillMaxWidth(),
                    colors = progressColors,
                )
            }
        },
    )
}

/**
 * 内存数值输入弹窗（对齐 FCLNumberSeekBar.java:122-136 单击数值区弹出的 EditDialog）：
 * 标题 "(min ~ max)"、数字键盘、初始为空（旧版 defaultText=""）；确认时非数字或
 * 越界（旧版判定 min..max 之外）静默忽略，空白输入不确认也不收起（对齐 EditDialog
 * positive 空白不响应）；弹窗不可取消（对齐 EditDialog setCancelable(false)）。
 */
@Composable
private fun MemoryInputDialog(
    totalMemoryMB: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var input by remember { mutableStateOf("") }
    FCLDialog(
        show = true,
        onDismissRequest = null,
        title = "(0 ~ $totalMemoryMB)",
        buttons = listOf(
            FCLDialogButton(
                stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                onClick = {
                    if (input.isNotBlank()) {
                        input.toIntOrNull()?.let { value ->
                            if (value in 0..totalMemoryMB) onConfirm(value)
                        }
                        onDismiss()
                    }
                },
            ),
            FCLDialogButton(
                stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                onClick = onDismiss,
            ),
        ),
    ) {
        FCLTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

/** 带当前值与编辑/安装按钮的设置行（对齐 XML 中"标题 + 值 + 设置/下载图标按钮"行）。 */
@Composable
private fun ValueSettingRow(
    title: String,
    value: String,
    onEdit: () -> Unit,
    onInstall: (() -> Unit)? = null,
) {
    BasicComponent(
        title = title,
        titleColor = autoTintComponentColors(),
        summary = value,
        summaryColor = autoTintComponentColors(),
        endActions = {
            RowActionIcon(icon = R.drawable.ic_baseline_settings_24, onClick = onEdit)
            if (onInstall != null) {
                RowActionIcon(icon = R.drawable.ic_baseline_download_24, onClick = onInstall)
            }
        },
        onClick = onEdit,
    )
}

/** 文本输入设置行（对齐 XML 中"标题 + FCLEditText"行）；onFullEdit 非空时提供全屏编辑按钮
 *  （承接遗留长按 FCLEditText 弹 FullEditDialog）。 */
@Composable
private fun TextSettingRow(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onFullEdit: (() -> Unit)? = null,
) {
    BasicComponent(
        title = title,
        titleColor = autoTintComponentColors(),
        endActions = {
            if (onFullEdit != null) {
                RowActionIcon(icon = R.drawable.ic_baseline_edit_24, onClick = onFullEdit)
            }
        },
        bottomAction = {
            // 对齐 FCLEditText：透明底 + 下划线（聚焦 primary / 未聚焦 primaryVariant），
            // 文字 autoTint（onPrimary）、光标 primary——共享实现见 FCLControls.kt
            FCLTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        },
    )
}

@Composable
private fun RowActionIcon(icon: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            // 对齐 FCLImageButton auto_tint="true"：图标染 autoTint（= onPrimary）
            tint = MiuixTheme.colorScheme.onPrimary,
        )
    }
}

// ---------- 旧版染色通道对齐（page_version_setting.xml / FCLLibrary 组件主题） ----------

/** 分组卡片：对齐旧版容器 auto_linear_background_tint="true" → ltColor（= primaryContainer）。 */
@Composable
private fun ThemedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    FCLCard(
        modifier = modifier,
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        content()
    }
}

/**
 * SwitchPreference 包装：对齐旧版 FCLSwitch 染色——
 * 文本 autoTint（= onPrimary）；thumb/track 颜色走共享 [fclSwitchColors]
 * （thumb checked=dkColor / unchecked=primary，track checked=primary / unchecked=Gray，
 * 禁用态旧版不变色）。
 */
@Composable
private fun ThemedSwitchPreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    summary: String? = null,
    enabled: Boolean = true,
) {
    FCLSwitchPreference(
        checked = checked,
        onCheckedChange = onCheckedChange,
        title = title,
        titleColor = autoTintComponentColors(),
        summary = summary,
        summaryColor = autoTintComponentColors(),
        enabled = enabled,
    )
}

/** 旧版 auto_text_tint / FCLSwitch 文本 → ThemeEngine autoTint（= onPrimary，禁用态旧版不变色）。 */
@Composable
private fun autoTintComponentColors() = BasicComponentColors(
    color = MiuixTheme.colorScheme.onPrimary,
    disabledColor = MiuixTheme.colorScheme.onPrimary,
)
