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
import com.tungsten.fcl.control.ButtonStyleDialog
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.ControlButtonStyle
import com.tungsten.fcl.control.data.ControlDirectionStyle
import com.tungsten.fcl.control.data.DirectionStyles
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog
import com.tungsten.fcllibrary.component.view.FCLPreciseSeekBar
import top.yukonga.miuix.kmp.basic.RadioButton
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
 * - 按钮页：interval 滑杆（0~200，"%"）+ 按钮样式选择行（弹按钮样式选择弹窗，
 *   按 [ComposeDialogs.USE_COMPOSE_BUTTON_STYLE] 双分支，GameMenu 透传一致；
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

    private inner class SeekSpec(
        val labelRes: Int,
        val property: IntegerProperty,
        min: Int,
        max: Int,
        val isPercentage: Boolean,
        val unit: String,
    ) {
        val seekBar: FCLPreciseSeekBar = FCLPreciseSeekBar(context).apply {
            setMin(min)
            setMax(max)
            setProgress(property.get())
            property.bindBidirectional(progressProperty())
            progressProperty().addListener { revisionState.intValue++ }
        }
    }

    private inner class ColorSpec(val labelRes: Int, val property: IntegerProperty)

    private val intervalSeek =
        SeekSpec(R.string.style_button_interval, style.buttonStyle.intervalProperty(), 0, 200, isPercentage = true, unit = "%")

    private val rockerSeeks = listOf(
        SeekSpec(R.string.style_rocker_size, style.rockerStyle.rockerSizeProperty(), 100, 900, isPercentage = true, unit = "%"),
        SeekSpec(R.string.style_rocker_bg_stroke_width, style.rockerStyle.bgStrokeWidthProperty(), 0, 50, isPercentage = true, unit = "dp"),
        SeekSpec(R.string.style_rocker_bg_corner_radius, style.rockerStyle.bgCornerRadiusProperty(), 0, 500, isPercentage = true, unit = "%"),
        SeekSpec(R.string.style_rocker_stroke_width, style.rockerStyle.rockerStrokeWidthProperty(), 0, 50, isPercentage = true, unit = "dp"),
        SeekSpec(R.string.style_rocker_corner_radius, style.rockerStyle.rockerCornerRadiusProperty(), 0, 500, isPercentage = true, unit = "%"),
    )

    private val rockerColors = listOf(
        ColorSpec(R.string.style_rocker_bg_stroke_color, style.rockerStyle.bgStrokeColorProperty()),
        ColorSpec(R.string.style_rocker_bg_fill_color, style.rockerStyle.bgFillColorProperty()),
        ColorSpec(R.string.style_rocker_stroke_color, style.rockerStyle.rockerStrokeColorProperty()),
        ColorSpec(R.string.style_rocker_fill_color, style.rockerStyle.rockerFillColorProperty()),
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
        // 再建立 style.buttonStyle → 本地 buttonStyle 的单向绑定（遗留 bind 方向一致）
        style.buttonStyle.textSizeProperty().bind(buttonStyle.textSizeProperty())
        style.buttonStyle.textColorProperty().bind(buttonStyle.textColorProperty())
        style.buttonStyle.strokeWidthProperty().bind(buttonStyle.strokeWidthProperty())
        style.buttonStyle.strokeColorProperty().bind(buttonStyle.strokeColorProperty())
        style.buttonStyle.cornerRadiusProperty().bind(buttonStyle.cornerRadiusProperty())
        style.buttonStyle.fillColorProperty().bind(buttonStyle.fillColorProperty())
        style.buttonStyle.textSizePressedProperty().bind(buttonStyle.textSizePressedProperty())
        style.buttonStyle.textColorPressedProperty().bind(buttonStyle.textColorPressedProperty())
        style.buttonStyle.strokeWidthPressedProperty().bind(buttonStyle.strokeWidthPressedProperty())
        style.buttonStyle.strokeColorPressedProperty().bind(buttonStyle.strokeColorPressedProperty())
        style.buttonStyle.cornerRadiusPressedProperty().bind(buttonStyle.cornerRadiusPressedProperty())
        style.buttonStyle.fillColorPressedProperty().bind(buttonStyle.fillColorPressedProperty())
        // 任何样式属性变化 → 预览/数值/色块重绘（对齐遗留 style.addListener(changeDirectionStyle)）
        style.addListener { revisionState.intValue++ }
        setDialogContent {
            DialogContent()
        }
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
        // 3.2 批 4 接入点：按钮样式选择弹窗按开关双分支
        if (ComposeDialogs.USE_COMPOSE_BUTTON_STYLE) {
            MiuixButtonStyleDialog(context, true, buttonStyle, onSelected)
                .apply { setGameMenu(menu) }
                .show()
        } else {
            ButtonStyleDialog(context, true, buttonStyle, object : ButtonStyleDialog.Callback {
                override fun onStyleSelect(style: ControlButtonStyle) = onSelected(style)
            })
                .apply { setGameMenu(menu) }
                .show()
        }
    }

    private fun openTextEditDialog(property: IntegerProperty, isPercentage: Boolean) {
        val dialog = EditDialog(context) { s ->
            if (s.matches(Regex("\\d+(\\.\\d+)?$"))) {
                var progress = s.toFloat()
                if (isPercentage) {
                    progress = if (progress > 100) 100f else progress
                    property.set((progress * 10).toInt())
                } else {
                    property.set(progress.toInt())
                }
            }
        }
        dialog.getEditText().inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        dialog.show()
    }

    private fun openColorPicker(property: IntegerProperty) {
        val dialog = FCLColorPickerDialog(context, property.get(), object : FCLColorPickerDialog.Listener {
            override fun onColorChanged(color: Int) {
            }

            override fun onPositive(destColor: Int) {
                property.set(destColor)
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
            scrollable = false,
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
                text = "${spec.property.get() / 10f} ${spec.unit}",
                style = MiuixTheme.textStyles.body2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(60.dp)
                    .clickable { openTextEditDialog(spec.seekBar.progressProperty(), spec.isPercentage) },
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
                text = getHex(spec.property.get()),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(spec.property.get()))
                    .border(1.dp, Color.Gray),
            )
            Spacer(Modifier.width(10.dp))
            TextButton(
                text = stringResource(R.string.menu_control_set),
                onClick = { openColorPicker(spec.property) },
            )
        }
    }

    private fun getHex(color: Int): String = "#" + String.format("%08X", color)
}
