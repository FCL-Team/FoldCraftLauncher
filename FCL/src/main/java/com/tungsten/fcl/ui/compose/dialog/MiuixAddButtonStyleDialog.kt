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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.control.data.ButtonStyles
import com.tungsten.fcl.control.data.ControlButtonStyle
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
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版新增/编辑按钮样式弹窗（3.2 批 4，对应 control/AddButtonStyleDialog
 * + dialog_add_button_style + view_button_style ×2）。
 *
 * 行为对齐：
 * - 编辑模型与遗留一致：beforeStyle 非空取 clone，否则新建 ControlButtonStyle("")，
 *   全程直接改该 observable 属性模型，确定时把整个 style 交给回调；
 * - TabRow 切换普通/按压两套样式页（各 3 滑杆 + 3 颜色），初始选中普通页；
 * - 滑杆沿用 FCLPreciseSeekBar（AndroidView 承载），区间对齐遗留 XML：
 *   字号 2~30（"sp"）、描边宽 0~100（"dp"）、圆角 0~500（"dp"），滑杆/数字输入即时
 *   写入样式属性（对齐遗留双向绑定；样式 revision 驱动重组回读）；
 * - 点数值文本弹 EditDialog 数字输入（沿用原生）：正则 \d+(\.\d+)?$ 校验、
 *   isPercentage 时 ×10 且封顶 100，各字段 isPercentage 取值与遗留一致（字号 false）；
 * - 3 组颜色按钮弹 FCLColorPickerDialog（沿用原生），onPositive 写回属性；
 * - 预览按钮（AndroidView AppCompatButton + GradientDrawable）按下切按压样式、抬起还原，
 *   样式属性任何变化即时重绘（style 失效监听驱动）；
 * - 名称输入单向写入 style.name（对齐遗留 nameProperty().bind(editText)）；
 * - 确定校验与遗留一致：非编辑模式重名 Toast style_warning_exist、名称空白 Toast
 *   style_warning_name，否则 dismiss + 回调 style；取消直接 dismiss；
 * - setCancelable(false) 一致。
 *
 * 有意偏差：设置区滚动高度由遗留固定 120dp 调整为 260dp（120dp 在卡片内边距下
 * 不足两行滑杆，可滚动语义不变）；确定/取消按钮按 FCLDialogCard 约定右对齐排列
 * （遗留为左确定右取消）。
 */
class MiuixAddButtonStyleDialog(
    context: Context,
    beforeStyle: ControlButtonStyle?,
    private val isEdit: Boolean,
    private val callback: (ControlButtonStyle) -> Unit,
) : FCLComposeDialog(context, cancelable = false) {

    private val style: ControlButtonStyle = beforeStyle?.clone() ?: ControlButtonStyle("")

    private val nameState = mutableStateOf(style.name)
    private val tabState = mutableIntStateOf(0)
    private val revisionState = mutableIntStateOf(0)

    /** 弹窗存活期 Flow 订阅（dismiss 统一取消，防共享作用域泄漏）。 */
    private val subscriptions = mutableListOf<FlowSubscriptions.Subscription>()

    /**
     * 滑杆行描述（阶段 4b：样式属性已 StateFlow 化，SeekSpec 改为 get/set + 镜像流，
     * 滑杆/数字输入 → 镜像流 → set，样式 revision 驱动重组，对齐原双向绑定语义）。
     */
    private inner class SeekSpec(
        val labelRes: Int,
        val get: () -> Int,
        val set: (Int) -> Unit,
        min: Int,
        max: Int,
        val isPercentage: Boolean,
        val div10: Boolean,
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

    private val normalSeeks = listOf(
        SeekSpec(R.string.style_button_text_size, { style.textSize }, { style.textSize = it }, 2, 30, isPercentage = false, div10 = false, unit = "sp"),
        SeekSpec(R.string.style_button_stroke_width, { style.strokeWidth }, { style.strokeWidth = it }, 0, 100, isPercentage = true, div10 = true, unit = "dp"),
        SeekSpec(R.string.style_button_corner_radius, { style.cornerRadius }, { style.cornerRadius = it }, 0, 500, isPercentage = true, div10 = true, unit = "dp"),
    )
    private val pressedSeeks = listOf(
        SeekSpec(R.string.style_button_text_size, { style.textSizePressed }, { style.textSizePressed = it }, 2, 30, isPercentage = false, div10 = false, unit = "sp"),
        SeekSpec(R.string.style_button_stroke_width, { style.strokeWidthPressed }, { style.strokeWidthPressed = it }, 0, 100, isPercentage = true, div10 = true, unit = "dp"),
        SeekSpec(R.string.style_button_corner_radius, { style.cornerRadiusPressed }, { style.cornerRadiusPressed = it }, 0, 500, isPercentage = true, div10 = true, unit = "dp"),
    )
    private val normalColors = listOf(
        ColorSpec(R.string.style_button_text_color, { style.textColor }, { style.textColor = it }),
        ColorSpec(R.string.style_button_stroke_color, { style.strokeColor }, { style.strokeColor = it }),
        ColorSpec(R.string.style_button_fill_color, { style.fillColor }, { style.fillColor = it }),
    )
    private val pressedColors = listOf(
        ColorSpec(R.string.style_button_text_color, { style.textColorPressed }, { style.textColorPressed = it }),
        ColorSpec(R.string.style_button_stroke_color, { style.strokeColorPressed }, { style.strokeColorPressed = it }),
        ColorSpec(R.string.style_button_fill_color, { style.fillColorPressed }, { style.fillColorPressed = it }),
    )

    init {
        // 任何样式属性变化 → 预览/数值/色块重绘（对齐遗留 style.addListener(changeButtonStyle)）
        subscriptions += FlowSubscriptions.subscribe(style.revisionFlow()) { revisionState.intValue++ }
        setDialogContent {
            DialogContent()
        }
    }

    override fun dismiss() {
        subscriptions.forEach { it.cancel() }
        super.dismiss()
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
        if (!isEdit && ButtonStyles.getStyles().any { it.name == style.name }) {
            Toast.makeText(context, context.getString(R.string.style_warning_exist), Toast.LENGTH_SHORT).show()
        } else if (StringUtils.isBlank(style.name)) {
            Toast.makeText(context, context.getString(R.string.style_warning_name), Toast.LENGTH_SHORT).show()
        } else {
            dismiss()
            callback(style)
        }
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
                    onClick = { dismiss() },
                ),
            ),
        ) {
            // 名称行 + 预览按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.style_name),
                    style = MiuixTheme.textStyles.body2,
                )
                Spacer(Modifier.width(8.dp))
                FCLTextField(
                    value = nameState.value,
                    onValueChange = {
                        nameState.value = it
                        style.name = it
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                Spacer(Modifier.width(10.dp))
                ButtonStylePreview(
                    style = style,
                    revision = revisionState.intValue,
                )
            }
            Spacer(Modifier.height(10.dp))
            TabRow(
                tabs = listOf(
                    stringResource(R.string.style_button_normal),
                    stringResource(R.string.style_button_pressed),
                ),
                selectedTabIndex = tabState.intValue,
                onTabSelected = { tabState.intValue = it },
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                if (tabState.intValue == 0) {
                    StylePage(normalSeeks, normalColors)
                } else {
                    StylePage(pressedSeeks, pressedColors)
                }
            }
        }
    }

    @Composable
    private fun StylePage(seeks: List<SeekSpec>, colors: List<ColorSpec>) {
        Column(Modifier.fillMaxWidth()) {
            seeks.forEach { SeekRow(it) }
            colors.forEach { ColorRow(it) }
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
                text = if (spec.div10) "${spec.get() / 10f} ${spec.unit}"
                else "${spec.get()} ${spec.unit}",
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
