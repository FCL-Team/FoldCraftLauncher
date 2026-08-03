package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.ControlButtonStyle
import com.tungsten.fcl.control.data.ControlDirectionStyle
import com.tungsten.fcl.control.data.DirectionStyles
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.ui.compose.fclDialogTextButtonColors
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog
import com.tungsten.fcllibrary.component.view.FCLPreciseSeekBar
import kotlinx.coroutines.flow.MutableStateFlow
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.RadioButtonDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版新增/编辑方向键样式弹窗（3.2 批 4，对应 control/AddDirectionStyleDialog
 * + dialog_add_direction_style + view_direction_style_button/rocker）。
 *
 * 行为对齐：
 * - 编辑模型与遗留一致：beforeStyle 非空取 clone，否则新建 ControlDirectionStyle("")；
 *   本地 buttonStyle（ControlButtonStyle("")）先复制 style.buttonStyle 的 12 个字段、
 *   再被 style.buttonStyle 各属性单向 bind，快照 beforeButtonStyle = buttonStyle.clone()；
 * - 类型切换：遗留为 FCLSpinner，这里用两个单选项（按钮 BUTTON / 摇杆 ROCKER），
 *   切换即写入 style.styleType 并切换子页，初始取 style.styleType；
 * - 按钮页：interval 滑杆（0~200，"%"）+ 按钮样式选择行（弹 Miuix 按钮样式选择弹窗，
 *   GameMenu 透传一致；
 *   回调把名称 + 12 个字段写回本地 buttonStyle，预览经失效监听即时刷新）；
 * - 摇杆页：5 滑杆（尺寸 100~900 "%"、背景描边宽 0~50 "dp"、背景圆角 0~500 "%"、
 *   描边宽 0~50 "dp"、圆角 0~500 "%"）+ 4 颜色（背景描边/背景填充/描边/填充），
 *   滑杆沿用 FCLPreciseSeekBar 双向绑定，点数值弹 EditDialog（isPercentage 全 true，
 *   与遗留一致），颜色弹 FCLColorPickerDialog；
 * - 预览复刻遗留 changeDirectionStyle：ControlDirection 展示模式 + clone 样式 +
 *   ABSOLUTE 60×60，样式属性任何变化即时重绘；
 * - **取消还原语义与遗留一致**：dismiss 前把快照的 12 个字段写回本地 buttonStyle
 *   （经单向绑定还原 style.buttonStyle；名称不还原，与遗留相同）；
 * - 确定校验与遗留一致：非编辑模式重名 Toast style_warning_exist、名称空白 Toast
 *   style_warning_name，否则 dismiss + 回调 style；
 * - setCancelable(false) 一致。
 *
 * 有意偏差：类型选择由 Spinner 改为两个单选项（任务允许）；设置区滚动高度由遗留
 * 固定 120dp 调整为 260dp（可滚动语义不变）；确定/取消按钮按 FCLDialogCard 约定右对齐。
 */
class MiuixAddDirectionStyleDialog(
    context: Context,
    beforeStyle: ControlDirectionStyle?,
    private val isEdit: Boolean,
    private val callback: (ControlDirectionStyle) -> Unit,
) : FCLComposeDialog(context, cancelable = false) {

    private val style: ControlDirectionStyle = beforeStyle?.clone() ?: ControlDirectionStyle("")

    private var menu: GameMenu? = null

    private val nameState = mutableStateOf(style.name)
    private val typeState = mutableStateOf(style.styleType)
    private val revisionState = mutableIntStateOf(0)

    /** 本地按钮样式（对齐遗留 buttonStyle 字段），被 style.buttonStyle 单向绑定。 */
    private val buttonStyle = ControlButtonStyle("")

    /** 按钮样式行显示名（对齐遗留 buttonStyleText 绑定 buttonStyle.nameProperty）。 */
    private val buttonStyleNameState = mutableStateOf(buttonStyle.name)

    /** 取消还原用的快照（对齐遗留 beforeStyle 字段：buttonStyle 初始化后的 clone）。 */
    private val beforeButtonStyle: ControlButtonStyle

    /** 弹窗存活期 Flow 订阅（dismiss 统一取消，防共享作用域泄漏）。 */
    private val subscriptions = mutableListOf<FlowSubscriptions.Subscription>()

    /**
     * 滑杆行描述（阶段 4b：样式属性已 StateFlow 化，SeekSpec 改为 get/set，
     * 滑杆/数字输入 → set，样式 revision 驱动重组，对齐原双向绑定语义）。
     */
    private inner class SeekSpec(
        val labelRes: Int,
        val get: () -> Int,
        val set: (Int) -> Unit,
        min: Int,
        max: Int,
        val isPercentage: Boolean,
        val unit: String,
    ) {
        val seekBar: FCLPreciseSeekBar = FCLPreciseSeekBar(context).apply {
            setMin(min)
            setMax(max)
            setProgress(get())
            // 订阅挂到弹窗存活期列表（共享作用域，dismiss 统一取消，对齐原 addListener 不即时回调）
            subscriptions += FlowSubscriptions.subscribe(progressFlow()) { set(it) }
            subscriptions += FlowSubscriptions.subscribe(progressFlow()) { revisionState.intValue++ }
        }
    }

    private inner class ColorSpec(val labelRes: Int, val get: () -> Int, val set: (Int) -> Unit)

    private val intervalSeek =
        SeekSpec(R.string.style_button_interval, { style.buttonStyle.interval }, { style.buttonStyle.interval = it }, 0, 200, isPercentage = true, unit = "%")

    private val rockerSeeks = listOf(
        SeekSpec(R.string.style_rocker_size, { style.rockerStyle.rockerSize }, { style.rockerStyle.rockerSize = it }, 100, 900, isPercentage = true, unit = "%"),
        SeekSpec(R.string.style_rocker_bg_stroke_width, { style.rockerStyle.bgStrokeWidth }, { style.rockerStyle.bgStrokeWidth = it }, 0, 50, isPercentage = true, unit = "dp"),
        SeekSpec(R.string.style_rocker_bg_corner_radius, { style.rockerStyle.bgCornerRadius }, { style.rockerStyle.bgCornerRadius = it }, 0, 500, isPercentage = true, unit = "%"),
        SeekSpec(R.string.style_rocker_stroke_width, { style.rockerStyle.rockerStrokeWidth }, { style.rockerStyle.rockerStrokeWidth = it }, 0, 50, isPercentage = true, unit = "dp"),
        SeekSpec(R.string.style_rocker_corner_radius, { style.rockerStyle.rockerCornerRadius }, { style.rockerStyle.rockerCornerRadius = it }, 0, 500, isPercentage = true, unit = "%"),
    )

    private val rockerColors = listOf(
        ColorSpec(R.string.style_rocker_bg_stroke_color, { style.rockerStyle.bgStrokeColor }, { style.rockerStyle.bgStrokeColor = it }),
        ColorSpec(R.string.style_rocker_bg_fill_color, { style.rockerStyle.bgFillColor }, { style.rockerStyle.bgFillColor = it }),
        ColorSpec(R.string.style_rocker_stroke_color, { style.rockerStyle.rockerStrokeColor }, { style.rockerStyle.rockerStrokeColor = it }),
        ColorSpec(R.string.style_rocker_fill_color, { style.rockerStyle.rockerFillColor }, { style.rockerStyle.rockerFillColor = it }),
    )

    init {
        // 对齐遗留：本地 buttonStyle 先复制 style.buttonStyle 的 12 个字段
        buttonStyle.textSize = style.buttonStyle.textSize
        buttonStyle.textColor = style.buttonStyle.textColor
        buttonStyle.strokeWidth = style.buttonStyle.strokeWidth
        buttonStyle.strokeColor = style.buttonStyle.strokeColor
        buttonStyle.cornerRadius = style.buttonStyle.cornerRadius
        buttonStyle.fillColor = style.buttonStyle.fillColor
        buttonStyle.textSizePressed = style.buttonStyle.textSizePressed
        buttonStyle.textColorPressed = style.buttonStyle.textColorPressed
        buttonStyle.strokeWidthPressed = style.buttonStyle.strokeWidthPressed
        buttonStyle.strokeColorPressed = style.buttonStyle.strokeColorPressed
        buttonStyle.cornerRadiusPressed = style.buttonStyle.cornerRadiusPressed
        buttonStyle.fillColorPressed = style.buttonStyle.fillColorPressed
        beforeButtonStyle = buttonStyle.clone()
        // 再建立 style.buttonStyle → 本地 buttonStyle 的单向绑定（遗留 bind 方向一致；
        // subscribeWithCurrent = bind 的"先同步当前值再跟随"语义，同值写入 no-op 天然防回环）
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.textSizeFlow()) { style.buttonStyle.textSize = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.textColorFlow()) { style.buttonStyle.textColor = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.strokeWidthFlow()) { style.buttonStyle.strokeWidth = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.strokeColorFlow()) { style.buttonStyle.strokeColor = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.cornerRadiusFlow()) { style.buttonStyle.cornerRadius = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.fillColorFlow()) { style.buttonStyle.fillColor = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.textSizePressedFlow()) { style.buttonStyle.textSizePressed = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.textColorPressedFlow()) { style.buttonStyle.textColorPressed = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.strokeWidthPressedFlow()) { style.buttonStyle.strokeWidthPressed = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.strokeColorPressedFlow()) { style.buttonStyle.strokeColorPressed = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.cornerRadiusPressedFlow()) { style.buttonStyle.cornerRadiusPressed = it }
        subscriptions += FlowSubscriptions.subscribeWithCurrent(buttonStyle.fillColorPressedFlow()) { style.buttonStyle.fillColorPressed = it }
        // 任何样式属性变化 → 预览/数值/色块重绘（对齐遗留 style.addListener(changeDirectionStyle)）
        subscriptions += FlowSubscriptions.subscribe(style.revisionFlow()) { revisionState.intValue++ }
        setDialogContent {
            DialogContent()
        }
    }

    override fun dismiss() {
        subscriptions.forEach { it.cancel() }
        super.dismiss()
    }

    fun setGameMenu(menu: GameMenu?) {
        this.menu = menu
    }

    private fun openButtonStyleDialog() {
        val onSelected: (ControlButtonStyle) -> Unit = { selected ->
            buttonStyle.name = selected.name
            buttonStyleNameState.value = selected.name
            buttonStyle.textSize = selected.textSize
            buttonStyle.textColor = selected.textColor
            buttonStyle.strokeWidth = selected.strokeWidth
            buttonStyle.strokeColor = selected.strokeColor
            buttonStyle.cornerRadius = selected.cornerRadius
            buttonStyle.fillColor = selected.fillColor
            buttonStyle.textSizePressed = selected.textSizePressed
            buttonStyle.textColorPressed = selected.textColorPressed
            buttonStyle.strokeWidthPressed = selected.strokeWidthPressed
            buttonStyle.strokeColorPressed = selected.strokeColorPressed
            buttonStyle.cornerRadiusPressed = selected.cornerRadiusPressed
            buttonStyle.fillColorPressed = selected.fillColorPressed
            // 预览刷新由 style 失效监听驱动（对齐遗留回调末尾的 changeDirectionStyle）
        }
        // 3.2 批 4 接入点：Miuix 按钮样式选择弹窗
        MiuixButtonStyleDialog(context, true, buttonStyle, onSelected)
            .apply { setGameMenu(menu) }
            .show()
    }

    private fun openTextEditDialog(property: MutableStateFlow<Int>, isPercentage: Boolean) {
        val dialog = EditDialog(context) { s ->
            if (s.matches(Regex("\\d+(\\.\\d+)?$"))) {
                var progress = s.toFloat()
                if (isPercentage) {
                    progress = if (progress > 100) 100f else progress
                    property.value = (progress * 10).toInt()
                } else {
                    property.value = progress.toInt()
                }
            }
        }
        dialog.getEditText().inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        dialog.show()
    }

    private fun openColorPicker(get: () -> Int, set: (Int) -> Unit) {
        val dialog = FCLColorPickerDialog(context, get(), object : FCLColorPickerDialog.Listener {
            override fun onColorChanged(color: Int) {
            }

            override fun onPositive(destColor: Int) {
                set(destColor)
            }

            override fun onNegative(initColor: Int) {
            }
        })
        dialog.show()
    }

    private fun onPositive() {
        if (!isEdit && DirectionStyles.getStyles().any { it.name == style.name }) {
            Toast.makeText(context, context.getString(R.string.style_warning_exist), Toast.LENGTH_SHORT).show()
        } else if (StringUtils.isBlank(style.name)) {
            Toast.makeText(context, context.getString(R.string.style_warning_name), Toast.LENGTH_SHORT).show()
        } else {
            dismiss()
            callback(style)
        }
    }

    private fun onNegative() {
        // 对齐遗留取消语义：把快照的 12 个字段写回本地 buttonStyle（经单向绑定还原
        // style.buttonStyle；名称不还原）
        buttonStyle.textSize = beforeButtonStyle.textSize
        buttonStyle.textColor = beforeButtonStyle.textColor
        buttonStyle.strokeWidth = beforeButtonStyle.strokeWidth
        buttonStyle.strokeColor = beforeButtonStyle.strokeColor
        buttonStyle.cornerRadius = beforeButtonStyle.cornerRadius
        buttonStyle.fillColor = beforeButtonStyle.fillColor
        buttonStyle.textSizePressed = beforeButtonStyle.textSizePressed
        buttonStyle.textColorPressed = beforeButtonStyle.textColorPressed
        buttonStyle.strokeWidthPressed = beforeButtonStyle.strokeWidthPressed
        buttonStyle.strokeColorPressed = beforeButtonStyle.strokeColorPressed
        buttonStyle.cornerRadiusPressed = beforeButtonStyle.cornerRadiusPressed
        buttonStyle.fillColorPressed = beforeButtonStyle.fillColorPressed
        dismiss()
    }

    @Composable
    private fun DialogContent() {
        FCLDialogCard(
            title = stringResource(R.string.menu_control_style_add),
            modifier = Modifier.width(400.dp),
            // 不关基座滚动：横屏小屏下整体（名称行+TabRow+260dp 固定高样式区）可超高，
            // 交给基座内容区限高滚动、按钮钉底（内层 260dp 固定高滚动区有界，不冲突）
            buttons = listOf(
                FCLDialogButton(
                    text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                    onClick = { onPositive() },
                ),
                FCLDialogButton(
                    text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                    onClick = { onNegative() },
                ),
            ),
        ) {
            // 名称行 + 预览
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.style_name),
                    style = MiuixTheme.textStyles.body2,
                )
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = nameState.value,
                    onValueChange = {
                        nameState.value = it
                        style.name = it
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                Spacer(Modifier.width(10.dp))
                DirectionStylePreview(
                    style = style,
                    revision = revisionState.intValue,
                )
            }
            Spacer(Modifier.height(10.dp))
            // 类型切换（对齐遗留 FCLSpinner：按钮 / 摇杆）
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.style_direction),
                    style = MiuixTheme.textStyles.body2,
                )
                Spacer(Modifier.weight(1f))
                TypeOption(
                    label = stringResource(R.string.style_direction_button),
                    type = ControlDirectionStyle.Type.BUTTON,
                )
                Spacer(Modifier.width(12.dp))
                TypeOption(
                    label = stringResource(R.string.style_direction_rocker),
                    type = ControlDirectionStyle.Type.ROCKER,
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                if (typeState.value == ControlDirectionStyle.Type.BUTTON) {
                    ButtonPage()
                } else {
                    RockerPage()
                }
            }
        }
    }

    @Composable
    private fun TypeOption(label: String, type: ControlDirectionStyle.Type) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                typeState.value = type
                style.styleType = type
            },
        ) {
            RadioButton(
                selected = typeState.value == type,
                onClick = {
                    typeState.value = type
                    style.styleType = type
                },
                // 对齐遗留 FCLRadioButton：按钮圆点 tint = dkColor（primaryVariant），不用 primary
                colors = RadioButtonDefaults.radioButtonColors(
                    selectedColor = MiuixTheme.colorScheme.primaryVariant,
                ),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
            )
        }
    }

    @Composable
    private fun ButtonPage() {
        Column(Modifier.fillMaxWidth()) {
            SeekRow(intervalSeek)
            // 按钮样式选择行
            @Suppress("UNUSED_EXPRESSION")
            revisionState.intValue // 订阅选择回写（名称随 12 字段绑定传播触发刷新）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.style_button),
                    style = MiuixTheme.textStyles.body2,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = buttonStyleNameState.value,
                    style = MiuixTheme.textStyles.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(10.dp))
                TextButton(
                    text = stringResource(R.string.menu_control_set),
                    onClick = { openButtonStyleDialog() },
                    colors = fclDialogTextButtonColors(),
                )
            }
        }
    }

    @Composable
    private fun RockerPage() {
        Column(Modifier.fillMaxWidth()) {
            rockerSeeks.forEach { SeekRow(it) }
            rockerColors.forEach { ColorRow(it) }
        }
    }

    @Composable
    private fun SeekRow(spec: SeekSpec) {
        @Suppress("UNUSED_EXPRESSION")
        revisionState.intValue // 订阅进度变化（滑杆/数字输入回写）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(spec.labelRes),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.weight(1f))
            AndroidView(
                factory = { spec.seekBar },
                modifier = Modifier.width(160.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "${spec.get() / 10f} ${spec.unit}",
                style = MiuixTheme.textStyles.body2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(60.dp)
                    .clickable { openTextEditDialog(spec.seekBar.progressFlow(), spec.isPercentage) },
            )
        }
    }

    @Composable
    private fun ColorRow(spec: ColorSpec) {
        @Suppress("UNUSED_EXPRESSION")
        revisionState.intValue // 订阅取色回写
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(spec.labelRes),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = getHex(spec.get()),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(spec.get()))
                    // 描边对齐遗留 darker_gray 体系（bg_item.xml 等）→ outline token（昼夜适配）
                    .border(1.dp, MiuixTheme.colorScheme.outline),
            )
            Spacer(Modifier.width(10.dp))
            TextButton(
                text = stringResource(R.string.menu_control_set),
                onClick = { openColorPicker(spec.get, spec.set) },
                colors = fclDialogTextButtonColors(),
            )
        }
    }

    private fun getHex(color: Int): String = "#" + String.format("%08X", color)
}
