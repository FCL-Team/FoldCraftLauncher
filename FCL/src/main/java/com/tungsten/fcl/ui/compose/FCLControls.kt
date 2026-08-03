package com.tungsten.fcl.ui.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.BasicComponentColors
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.CheckboxColors
import top.yukonga.miuix.kmp.basic.CheckboxDefaults
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SliderColors
import top.yukonga.miuix.kmp.basic.SliderDefaults
import top.yukonga.miuix.kmp.basic.SwitchColors
import top.yukonga.miuix.kmp.basic.SwitchDefaults
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TextFieldColors
import top.yukonga.miuix.kmp.basic.TextFieldDefaults
import top.yukonga.miuix.kmp.preference.SliderPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import kotlin.math.roundToInt

/**
 * FCL 旧版控件染色语义在 Miuix 控件上的共享实现。
 *
 * 配色依据（均已从 FCLLibrary 源码核实）：
 * - FCLEditText.applyTheme（auto_edit_tint）：背景为 AppCompat 下划线 drawable，
 *   tint 聚焦 = color（primary）/ 未聚焦 = dkColor（primaryVariant）；
 *   文字 = autoTint（onPrimary）；hint = autoHintTint（autoTint 的 0.6 alpha）；
 *   光标 = color（primary）。——不是实心填充盒。
 * - FCLSwitch.applyTheme：thumb checked = dkColor / unchecked = color 不透明；
 *   track checked = color / unchecked = Color.GRAY；文字 = autoTint。
 *   tint 列表不含 state_enabled 项，禁用态沿用同色。
 * - FCLCheckBox.applyTheme：buttonTint checked = dkColor / unchecked = color；
 *   文字 = autoTint（auto_hint_tint 时）。禁用态同样沿用同色。
 *
 * Miuix 0.9.3 能力边界（源码核实）：
 * - TextField 背景恒为 squircle 实心填充（无下划线样式）；文字色不能经 colors 配置
 *   （取 textStyle.color），光标可经 cursorBrush 配置。因此用透明背景 + 自绘底部
 *   指示线逼近旧版 AppCompat 下划线观感。
 * - Switch / Checkbox 的全部色槽均可经 colors 配置。
 * - Miuix Checkbox 未勾选态只能画实心圆（无描边态），无法复刻旧版 primary 描边方框，
 *   取 ltColor（primaryContainer）实心圆为折中。
 *
 * 圆角与滑杆（PR #1714 review 对齐旧版观感）：
 * - 圆角走 [FCLCornerRadius] 两档：页面卡片 5dp（bg_container_white）、弹窗卡片 8dp
 *   （dialog_background），替代 Miuix 默认 16dp squircle；统一经 [FCLCard] 接入。
 * - 滑杆走 [FCLSliderPreference] / [FCLSlider]：高度收敛为 20dp（Miuix 默认 28dp），
 *   并自带数值文本（对齐 FCLNumberSeekBar 的滑杆 + 数值形态，suffix 语义保留）。
 */

/** FCLSwitch 配色：thumb checked=dkColor / unchecked=primary，track checked=primary / unchecked=GRAY。 */
@Composable
fun fclSwitchColors(): SwitchColors {
    val scheme = MiuixTheme.colorScheme
    return SwitchDefaults.switchColors(
        checkedThumbColor = scheme.primaryVariant,
        uncheckedThumbColor = scheme.primary,
        disabledCheckedThumbColor = scheme.primaryVariant,
        disabledUncheckedThumbColor = scheme.primary,
        checkedTrackColor = scheme.primary,
        uncheckedTrackColor = Color.Gray,
        disabledCheckedTrackColor = scheme.primary,
        disabledUncheckedTrackColor = Color.Gray,
    )
}

/**
 * FCLCheckBox 配色：勾选 = dkColor 底 + onPrimaryVariant 对勾；
 * 未勾选 Miuix 无法画描边框，取 ltColor（primaryContainer）实心圆折中（见文件头说明）。
 */
@Composable
fun fclCheckboxColors(): CheckboxColors {
    val scheme = MiuixTheme.colorScheme
    return CheckboxDefaults.checkboxColors(
        checkedForegroundColor = scheme.onPrimaryVariant,
        uncheckedForegroundColor = scheme.onPrimaryContainer,
        disabledCheckedForegroundColor = scheme.onPrimaryVariant,
        disabledUncheckedForegroundColor = scheme.onPrimaryContainer,
        checkedBackgroundColor = scheme.primaryVariant,
        uncheckedBackgroundColor = scheme.primaryContainer,
        disabledCheckedBackgroundColor = scheme.primaryVariant,
        disabledUncheckedBackgroundColor = scheme.primaryContainer,
    )
}

/**
 * FCLEditText 配色：透明背景（下划线由 [FCLTextField] 自绘），
 * label/hint = autoHintTint（onPrimary 0.6 alpha，Theme.getAutoHintTint 的 #99 同构），
 * 透明边框（抑制 Miuix 聚焦 squircle 描边）。
 */
@Composable
fun fclTextFieldColors(): TextFieldColors {
    val scheme = MiuixTheme.colorScheme
    return TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        labelColor = scheme.onPrimary.copy(alpha = 0.6f),
        borderColor = Color.Transparent,
    )
}

/** FCLEditText 光标色 = color（primary）。 */
@Composable
fun fclCursorBrush(): Brush = SolidColor(MiuixTheme.colorScheme.primary)

/**
 * FCLEditText 的 Miuix 包装：透明底 + 底部指示线（聚焦 = primary 2dp，未聚焦 = primaryVariant 1dp），
 * 文字 = onPrimary，hint = onPrimary 0.6 alpha，光标 = primary。
 * 参数为 Miuix [TextField]（String 重载）的直通子集，业务行为零改动。
 */
@Composable
fun FCLTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    useLabelAsPlaceholder: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val scheme = MiuixTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val underlineColor = if (isFocused) scheme.primary else scheme.primaryVariant
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.drawBehind {
            val strokeWidth = (if (isFocused) 2.dp else 1.dp).toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = underlineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth,
            )
        },
        colors = fclTextFieldColors(),
        label = label,
        useLabelAsPlaceholder = useLabelAsPlaceholder,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = MiuixTheme.textStyles.main.copy(color = scheme.onPrimary),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        interactionSource = interactionSource,
        cursorBrush = fclCursorBrush(),
    )
}

/**
 * 接入 FCLSwitch 染色的 [SwitchPreference] 包装：开关 thumb/track 颜色统一走 [fclSwitchColors]，
 * 其余参数直通 Miuix [SwitchPreference]，业务行为零改动。
 */
@Composable
fun FCLSwitchPreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summary: String? = null,
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    enabled: Boolean = true,
) {
    SwitchPreference(
        checked = checked,
        onCheckedChange = onCheckedChange,
        title = title,
        modifier = modifier,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        switchColors = fclSwitchColors(),
        enabled = enabled,
    )
}

// ---------- 圆角体系（design-tokens.md §4.1：旧版仅 5dp/8dp 两档） ----------

/**
 * FCL 圆角 token。Miuix 组件默认 16dp squircle（CardDefaults.CornerRadius），
 * 旧版 FCL 全项目只有两档（design-tokens §4.1 实测）：
 * - [Card]：5dp——列表项 / 白色容器（bg_item.xml、bg_container_white.xml、fcl_button.xml）；
 * - [Dialog]：8dp——对话框 / 面板（dialog_background.xml、bg_game_menu.xml）。
 */
object FCLCornerRadius {
    /** 页面分组卡片、列表项卡片：对齐 bg_container_white / bg_item 的 5dp。 */
    val Card = 5.dp

    /** 对话框 / 弹层卡片：对齐 dialog_background.xml 的 8dp。 */
    val Dialog = 8.dp
}

/**
 * Miuix [Card] 的 FCL 包装：圆角默认 [FCLCornerRadius.Card]（5dp，对齐旧版容器），
 * 替代 Miuix 默认 16dp squircle。弹窗容器卡片显式传 `cornerRadius = FCLCornerRadius.Dialog`。
 * 其余参数直通 Miuix [Card]，业务行为零改动。
 */
@Composable
fun FCLCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = FCLCornerRadius.Card,
    insideMargin: PaddingValues = CardDefaults.InsideMargin,
    colors: CardColors = CardDefaults.defaultColors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        cornerRadius = cornerRadius,
        insideMargin = insideMargin,
        colors = colors,
        content = content,
    )
}

/** [FCLCard] 的可点击重载，参数直通 Miuix [Card] 可点击重载。 */
@Composable
fun FCLCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = FCLCornerRadius.Card,
    insideMargin: PaddingValues = CardDefaults.InsideMargin,
    colors: CardColors = CardDefaults.defaultColors(),
    pressFeedbackType: PressFeedbackType = PressFeedbackType.None,
    showIndication: Boolean = false,
    holdDownState: Boolean = false,
    onClick: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        cornerRadius = cornerRadius,
        insideMargin = insideMargin,
        colors = colors,
        pressFeedbackType = pressFeedbackType,
        showIndication = showIndication,
        holdDownState = holdDownState,
        onClick = onClick,
        onLongPress = onLongPress,
        content = content,
    )
}

// ---------- 滑杆体系（对齐旧版 FCLNumberSeekBar：滑杆 + 数值文本，行高收敛） ----------

/**
 * FCL 滑杆高度。Miuix 默认 28dp（SliderDefaults.MinHeight）观感过大；
 * 旧版 AppCompat SeekBar 行高更薄，取 20dp 逼近（thumb 半径 = height/2，随高度同步缩小）。
 */
val FCLSliderHeight = 20.dp

/** 滑杆数值文本默认格式：整数值去小数，附带 [suffix]（对齐 FCLNumberSeekBar 的 suffix 语义，如 "MB"/"%"）。 */
private fun formatSliderValue(value: Float, suffix: String): String =
    if (value == value.roundToInt().toFloat()) {
        "${value.roundToInt()}$suffix"
    } else {
        "${"%.1f".format(value)}$suffix"
    }

/**
 * Miuix [SliderPreference] 的 FCL 包装，对齐旧版 FCLNumberSeekBar 形态：
 * - 数值显示：[valueText] 显式传入优先；缺省时由 [value] + [suffix] 自动生成
 *   （旧版数值直接画在滑杆 thumb 处，Miuix 无此能力，取 SliderPreference 自带尾部数值区）；
 * - 尺寸收敛：滑杆高 [FCLSliderHeight]（Miuix 默认 28dp），行内边距收窄为 16/10dp
 *   （Miuix 默认四向 16dp）。
 * 其余参数直通 Miuix [SliderPreference]，业务行为零改动。
 */
@Composable
fun FCLSliderPreference(
    value: Float,
    onValueChange: (Float) -> Unit,
    title: String,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    valueText: String? = null,
    suffix: String = "",
    summary: String? = null,
    titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
    summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
    enabled: Boolean = true,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    sliderColors: SliderColors = SliderDefaults.sliderColors(),
) {
    SliderPreference(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        summary = summary,
        summaryColor = summaryColor,
        valueText = valueText ?: formatSliderValue(value, suffix),
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        sliderHeight = FCLSliderHeight,
        sliderColors = sliderColors,
        insideMargin = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
    )
}

/**
 * Miuix [Slider] 的 FCL 包装（弹窗内裸滑杆用）：高度收敛为 [FCLSliderHeight]，
 * 其余参数直通 Miuix [Slider]。数值文本由调用方在行内自排（对齐旧版
 * FCLNumberSeekBar + 旁侧 Text 的形态，见 MiuixEditViewDialog.SliderRow）。
 */
@Composable
fun FCLSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        height = FCLSliderHeight,
    )
}
