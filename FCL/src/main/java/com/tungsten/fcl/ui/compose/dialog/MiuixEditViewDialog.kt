package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.tungsten.fcl.R
import com.tungsten.fcl.control.EditViewDialog
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.BaseInfoData
import com.tungsten.fcl.control.data.ButtonEventData
import com.tungsten.fcl.control.data.ButtonStyles
import com.tungsten.fcl.control.data.ControlButtonData
import com.tungsten.fcl.control.data.ControlDirectionData
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fcl.control.data.CustomControl
import com.tungsten.fcl.control.data.DirectionEventData
import com.tungsten.fcl.control.data.DirectionStyles
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.fclDialogTextButtonColors
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcl.ui.compose.fclSwitchColors
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版控件（按钮/方向键）编辑弹窗（3.2 批 4，对应 control/EditViewDialog + dialog_edit_view
 * + view_edit_button_info/event(+_child) + view_edit_direction_info/event）。
 *
 * 行为对齐：
 * - 标题按 ViewType 区分（edit_button_title/edit_direction_title）；左侧 info/event 两图标
 *   切换子页（默认 info 页）；底部 确定/取消/克隆/删除 四按钮，克隆按 cloneable 显隐；
 * - 确定回调 onPositive(data.clone())（对齐遗留 details.getView()），克隆回调
 *   onClone(customControl.cloneView())，删除回调 onDelete()，均随后 dismiss；
 * - 按钮信息页：文本输入；可见性/尺寸类型/宽高参照下拉（选项顺序与遗留 Spinner 一致）；
 *   X/Y 位置滑杆 0..1000（值为实际百分比×10，文本显示 "x.x %"）；宽高滑杆按尺寸类型
 *   切换区间（百分比 1..1000 / 绝对 1..屏宽|屏高 dp），两套值独立保存、切换时换回，
 *   等价于遗留的解绑/重绑；点击数值文本弹 EditDialog 数字输入（正则 \d+(\.\d+)?$，
 *   百分比超 100 截断、×10 取整，与遗留 openTextEditDialog 一致）；样式行显示当前样式名，
 *   「Set」弹 Miuix 样式选择弹窗，选择后写回 data.style 并刷新名称；
 * - 按钮事件页：pointerFollow/movable 两 Switch + TabRow 四套事件子页（按下/长按/点击/双击），
 *   各含 7 Switch + 输出文本 + 键码选择（MiuixSelectKeycodeDialog，临时拷贝即时写回 outputKeycodes）
 *   + 绑定分组（MiuixViewGroupDialog，回调 id 列表
 *   setBindViewGroup，与遗留 :387 一致）；
 * - 方向键信息页同构：单一尺寸滑杆（绝对区间上限为屏高 dp），写宽时同步高
 *   （setAbsoluteHeight(absoluteWidth)/setPercentageHeight(percentageWidth.clone())，
 *   对齐遗留 :565-572 的监听）；事件页：四方向键码（临时列表拷贝，确认后 setAll 回写）、
 *   潜行开关、潜行键码（单选，selectionProperty 双向绑定）、跟随选项下拉；
 * - 数据直改传入的 cloneView（调用点已 clone），所有控件写入即时落到 data；
 * - setCancelable(false) 一致；窗体对齐遗留 500dp × MATCH_PARENT 侧边面板。
 *
 * 回调直接复用遗留 [EditViewDialog.Callback] 接口（Java 调用点匿名类零改动兼容）。
 * 遗留基于 observable 双向绑定，本实现以 Compose 状态为 UI 真源、每次变更即时写入 data
 * （该 clone 仅本弹窗编辑，无并发写入方），对外行为等价。
 * 下拉以标准 Compose Popup 实现（工程未引入 compose-material）。
 */
class MiuixEditViewDialog(
    context: Context,
    private val customControl: CustomControl,
    private val menu: GameMenu,
    private val callback: EditViewDialog.Callback,
    private val cloneable: Boolean,
) : FCLComposeDialog(context, cancelable = false) {

    private val isButton = customControl.type == CustomControl.ViewType.CONTROL_BUTTON
    private val pageState = mutableIntStateOf(0)
    private val styleNameState = mutableStateOf(
        when (customControl) {
            is ControlButtonData -> customControl.style.name
            is ControlDirectionData -> customControl.style.name
            else -> ""
        }
    )

    init {
        setDialogContent {
            DialogContent()
        }
    }

    override fun show() {
        super.show()
        // 对齐遗留：宽 500dp、高 MATCH_PARENT 侧边面板
        window?.setLayout(ConvertUtils.dip2px(context, 500f), ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun onPositive() {
        val view = when (val data = customControl) {
            is ControlButtonData -> data.clone()
            is ControlDirectionData -> data.clone()
            else -> data.cloneView()
        }
        callback.onPositive(view)
        dismiss()
    }

    @Composable
    private fun DialogContent() {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            // 对齐遗留 dialog_background（#F4F4F4 / #232323）→ surface token
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.surface),
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(if (isButton) R.string.edit_button_title else R.string.edit_direction_title),
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 5.dp),
                    ) {
                        Spacer(Modifier.weight(1f))
                        PageSwitchIcon(
                            icon = R.drawable.ic_baseline_settings_24,
                            onClick = { pageState.intValue = 0 },
                        )
                        Spacer(Modifier.weight(1f))
                        PageSwitchIcon(
                            icon = R.drawable.ic_baseline_keyboard_24,
                            onClick = { pageState.intValue = 1 },
                        )
                        Spacer(Modifier.weight(1f))
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                            .padding(end = 5.dp),
                    ) {
                        if (pageState.intValue == 0) {
                            SectionHeader(stringResource(R.string.edit_view_info))
                            if (isButton) {
                                ButtonInfoPage(customControl as ControlButtonData)
                            } else {
                                DirectionInfoPage(customControl as ControlDirectionData)
                            }
                        } else {
                            SectionHeader(stringResource(R.string.edit_view_event))
                            if (isButton) {
                                ButtonEventPage(customControl as ControlButtonData)
                            } else {
                                DirectionEventPage(customControl as ControlDirectionData)
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (cloneable) {
                        TextButton(
                            text = stringResource(R.string.edit_view_clone),
                            onClick = {
                                callback.onClone(customControl.cloneView())
                                dismiss()
                            },
                            colors = fclDialogTextButtonColors(),
                        )
                    }
                    TextButton(
                        text = stringResource(R.string.edit_view_delete),
                        onClick = {
                            callback.onDelete()
                            dismiss()
                        },
                        colors = fclDialogTextButtonColors(),
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { onPositive() },
                        colors = fclDialogTextButtonColors(),
                    )
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                        colors = fclDialogTextButtonColors(),
                    )
                }
            }
        }
    }

    @Composable
    private fun PageSwitchIcon(icon: Int, onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                // 对齐遗留 dialog_edit_view：info/event 图标 darker_gray 静态描边色，
                // 旧版无选中态变色（既不用 primary 也不用 color2）
                tint = MiuixTheme.colorScheme.outline,
            )
        }
    }

    @Composable
    private fun SectionHeader(title: String) {
        Text(text = title, style = MiuixTheme.textStyles.footnote1)
    }

    // ---------- 按钮信息页 ----------

    @Composable
    private fun ButtonInfoPage(data: ControlButtonData) {
        val baseInfo = data.baseInfo
        val textState = remember { mutableStateOf(data.text) }
        LabeledTextFieldRow(
            label = stringResource(R.string.edit_button_text),
            value = textState.value,
            onValueChange = {
                textState.value = it
                data.text = it
            },
        )

        val visibilityOptions = listOf(
            stringResource(R.string.view_info_visibility_always),
            stringResource(R.string.view_info_visibility_game),
            stringResource(R.string.view_info_visibility_menu),
        )
        val visibilityTypes = BaseInfoData.VisibilityType.values()
        val visibilityState = remember { mutableIntStateOf(visibilityTypes.indexOf(baseInfo.visibilityType)) }
        LabeledDropdownRow(
            label = stringResource(R.string.view_info_visibility),
            options = visibilityOptions,
            selectedIndex = visibilityState.intValue,
            onSelected = {
                visibilityState.intValue = it
                baseInfo.visibilityType = visibilityTypes[it]
            },
        )

        PositionSliders(baseInfo)

        val sizeTypeState = rememberSizeTypeState(baseInfo)

        val referenceOptions = listOf(
            stringResource(R.string.view_info_reference_width),
            stringResource(R.string.view_info_reference_height),
        )
        val references = BaseInfoData.PercentageSize.Reference.values()
        if (sizeTypeState.intValue == 0) {
            val widthReferenceState = remember { mutableIntStateOf(references.indexOf(baseInfo.percentageWidth.reference)) }
            LabeledDropdownRow(
                label = stringResource(R.string.view_info_width_reference),
                options = referenceOptions,
                selectedIndex = widthReferenceState.intValue,
                onSelected = {
                    widthReferenceState.intValue = it
                    baseInfo.percentageWidth.reference = references[it]
                },
            )
        }

        SizeSliderRow(
            label = stringResource(R.string.view_info_width),
            sizeTypeIndex = sizeTypeState.intValue,
            percentageSize = baseInfo.percentageWidth,
            getAbsolute = { baseInfo.absoluteWidth },
            setAbsolute = { baseInfo.absoluteWidth = it },
            absoluteMax = ConvertUtils.px2dip(context, AndroidUtils.getScreenWidth().toFloat()),
        )

        if (sizeTypeState.intValue == 0) {
            val heightReferenceState = remember { mutableIntStateOf(references.indexOf(baseInfo.percentageHeight.reference)) }
            LabeledDropdownRow(
                label = stringResource(R.string.view_info_height_reference),
                options = referenceOptions,
                selectedIndex = heightReferenceState.intValue,
                onSelected = {
                    heightReferenceState.intValue = it
                    baseInfo.percentageHeight.reference = references[it]
                },
            )
        }

        SizeSliderRow(
            label = stringResource(R.string.view_info_height),
            sizeTypeIndex = sizeTypeState.intValue,
            percentageSize = baseInfo.percentageHeight,
            getAbsolute = { baseInfo.absoluteHeight },
            setAbsolute = { baseInfo.absoluteHeight = it },
            absoluteMax = ConvertUtils.px2dip(context, AndroidUtils.getScreenHeight().toFloat()),
        )

        StyleRow(
            styleName = styleNameState.value,
            onClick = {
                val targetStyle = ButtonStyles.findStyleByName(data.style.name)
                val dialog = MiuixButtonStyleDialog(context, true, targetStyle) { style ->
                    data.style = style
                    styleNameState.value = style.name
                }
                dialog.setGameMenu(menu)
                dialog.show()
            },
        )
    }

    // ---------- 方向键信息页 ----------

    @Composable
    private fun DirectionInfoPage(data: ControlDirectionData) {
        val baseInfo = data.baseInfo

        val visibilityOptions = listOf(
            stringResource(R.string.view_info_visibility_always),
            stringResource(R.string.view_info_visibility_game),
            stringResource(R.string.view_info_visibility_menu),
        )
        val visibilityTypes = BaseInfoData.VisibilityType.values()
        val visibilityState = remember { mutableIntStateOf(visibilityTypes.indexOf(baseInfo.visibilityType)) }
        LabeledDropdownRow(
            label = stringResource(R.string.view_info_visibility),
            options = visibilityOptions,
            selectedIndex = visibilityState.intValue,
            onSelected = {
                visibilityState.intValue = it
                baseInfo.visibilityType = visibilityTypes[it]
            },
        )

        PositionSliders(baseInfo)

        val sizeTypeState = rememberSizeTypeState(baseInfo)

        val referenceOptions = listOf(
            stringResource(R.string.view_info_reference_width),
            stringResource(R.string.view_info_reference_height),
        )
        val references = BaseInfoData.PercentageSize.Reference.values()
        if (sizeTypeState.intValue == 0) {
            val sizeReferenceState = remember { mutableIntStateOf(references.indexOf(baseInfo.percentageWidth.reference)) }
            LabeledDropdownRow(
                label = stringResource(R.string.view_info_reference),
                options = referenceOptions,
                selectedIndex = sizeReferenceState.intValue,
                onSelected = {
                    sizeReferenceState.intValue = it
                    baseInfo.percentageWidth.reference = references[it]
                    // 遗留同步监听也会在参照变更时把宽克隆到高
                    baseInfo.percentageHeight = baseInfo.percentageWidth.clone()
                },
            )
        }

        // 单一尺寸滑杆：写宽时同步高（对齐遗留 :565-572 的同步监听）
        SizeSliderRow(
            label = stringResource(R.string.view_info_size),
            sizeTypeIndex = sizeTypeState.intValue,
            percentageSize = baseInfo.percentageWidth,
            getAbsolute = { baseInfo.absoluteWidth },
            setAbsolute = {
                baseInfo.absoluteWidth = it
                baseInfo.absoluteHeight = it
            },
            absoluteMax = ConvertUtils.px2dip(context, AndroidUtils.getScreenHeight().toFloat()),
            onPercentageChanged = {
                baseInfo.percentageHeight = baseInfo.percentageWidth.clone()
            },
        )

        StyleRow(
            styleName = styleNameState.value,
            onClick = {
                val target = DirectionStyles.findStyleByName(data.style.name)
                val dialog = MiuixDirectionStyleDialog(context, true, target) { style ->
                    data.style = style
                    styleNameState.value = style.name
                }
                dialog.setGameMenu(menu)
                dialog.show()
            },
        )
    }

    // ---------- 信息页公共件 ----------

    /** X/Y 位置滑杆（0..1000，值为百分比×10），点击数值弹数字输入。 */
    @Composable
    private fun PositionSliders(baseInfo: BaseInfoData) {
        val xState = remember { mutableIntStateOf(baseInfo.xPosition) }
        SliderRow(
            label = stringResource(R.string.view_info_x),
            value = xState.intValue,
            min = 0,
            max = 1000,
            display = "${xState.intValue / 10f} %",
            onValueChange = {
                xState.intValue = it
                baseInfo.xPosition = it
            },
            onValueClick = {
                openNumberEditDialog(isPercentage = true) {
                    xState.intValue = it
                    baseInfo.xPosition = it
                }
            },
        )
        val yState = remember { mutableIntStateOf(baseInfo.yPosition) }
        SliderRow(
            label = stringResource(R.string.view_info_y),
            value = yState.intValue,
            min = 0,
            max = 1000,
            display = "${yState.intValue / 10f} %",
            onValueChange = {
                yState.intValue = it
                baseInfo.yPosition = it
            },
            onValueClick = {
                openNumberEditDialog(isPercentage = true) {
                    yState.intValue = it
                    baseInfo.yPosition = it
                }
            },
        )
    }

    /** 尺寸类型下拉（百分比/绝对），选项顺序与遗留一致。 */
    @Composable
    private fun rememberSizeTypeState(baseInfo: BaseInfoData): androidx.compose.runtime.MutableIntState {
        val sizeTypeOptions = listOf(
            stringResource(R.string.view_info_size_type_percentage),
            stringResource(R.string.view_info_size_type_absolute),
        )
        val sizeTypeState = remember {
            mutableIntStateOf(if (baseInfo.sizeType == BaseInfoData.SizeType.PERCENTAGE) 0 else 1)
        }
        LabeledDropdownRow(
            label = stringResource(R.string.view_info_size_type),
            options = sizeTypeOptions,
            selectedIndex = sizeTypeState.intValue,
            onSelected = {
                sizeTypeState.intValue = it
                baseInfo.sizeType = if (it == 0) BaseInfoData.SizeType.PERCENTAGE else BaseInfoData.SizeType.ABSOLUTE
            },
        )
        return sizeTypeState
    }

    /**
     * 宽/高/尺寸滑杆行：百分比与绝对两套值独立保存（对齐遗留解绑/重绑语义，
     * 切换尺寸类型后换回另一套上次的值）；点击数值弹数字输入。
     */
    @Composable
    private fun SizeSliderRow(
        label: String,
        sizeTypeIndex: Int,
        percentageSize: BaseInfoData.PercentageSize,
        getAbsolute: () -> Int,
        setAbsolute: (Int) -> Unit,
        absoluteMax: Int,
        onPercentageChanged: () -> Unit = {},
    ) {
        val percentageState = remember { mutableIntStateOf(percentageSize.size) }
        val absoluteState = remember { mutableIntStateOf(getAbsolute()) }
        val isPercentage = sizeTypeIndex == 0
        val value = if (isPercentage) percentageState.intValue else absoluteState.intValue
        val max = if (isPercentage) 1000 else absoluteMax
        SliderRow(
            label = label,
            value = value,
            min = 1,
            max = max,
            display = if (isPercentage) "${value / 10f} %" else "$value dp",
            onValueChange = { v ->
                if (isPercentage) {
                    percentageState.intValue = v
                    percentageSize.size = v
                    onPercentageChanged()
                } else {
                    absoluteState.intValue = v
                    setAbsolute(v)
                }
            },
            onValueClick = {
                openNumberEditDialog(isPercentage = isPercentage) { v ->
                    if (isPercentage) {
                        percentageState.intValue = v
                        percentageSize.size = v
                        onPercentageChanged()
                    } else {
                        absoluteState.intValue = v
                        setAbsolute(v)
                    }
                }
            },
        )
    }

    @Composable
    private fun StyleRow(styleName: String, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.edit_view_style),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = styleName,
                style = MiuixTheme.textStyles.body2,
                maxLines = 1,
            )
            TextButton(
                text = stringResource(R.string.menu_control_set),
                onClick = onClick,
                colors = fclDialogTextButtonColors(),
            )
        }
    }

    /** 数字输入弹窗（原生 EditDialog 保留）：解析规则与遗留 openTextEditDialog 逐行一致。 */
    private fun openNumberEditDialog(isPercentage: Boolean, onSet: (Int) -> Unit) {
        val dialog = EditDialog(context) { s ->
            if (s.matches(Regex("\\d+(\\.\\d+)?$"))) {
                var progress = s.toFloat()
                if (isPercentage) {
                    progress = if (progress > 100) 100f else progress
                    onSet((progress * 10).toInt())
                } else {
                    onSet(progress.toInt())
                }
            }
        }
        dialog.getEditText().inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        dialog.show()
    }

    // ---------- 按钮事件页 ----------

    @Composable
    private fun ButtonEventPage(data: ControlButtonData) {
        val event = data.event

        val pointerFollowState = remember { mutableStateOf(event.isPointerFollow) }
        LabeledSwitchRow(
            label = stringResource(R.string.edit_button_event_follow),
            checked = pointerFollowState.value,
            onCheckedChange = {
                pointerFollowState.value = it
                event.isPointerFollow = it
            },
        )
        val movableState = remember { mutableStateOf(event.isMovable) }
        LabeledSwitchRow(
            label = stringResource(R.string.edit_button_event_movable),
            checked = movableState.value,
            onCheckedChange = {
                movableState.value = it
                event.isMovable = it
            },
        )

        val events = listOf(event.pressEvent, event.longPressEvent, event.clickEvent, event.doubleClickEvent)
        val tabState = remember { mutableIntStateOf(0) }
        TabRow(
            tabs = listOf(
                stringResource(R.string.edit_button_event_press),
                stringResource(R.string.edit_button_event_long_press),
                stringResource(R.string.edit_button_event_click),
                stringResource(R.string.edit_button_event_double_click),
            ),
            selectedTabIndex = tabState.intValue,
            onTabSelected = { tabState.intValue = it },
        )
        key(tabState.intValue) {
            ButtonEventChild(events[tabState.intValue])
        }
    }

    /** 单套事件子页：7 Switch + 输出文本 + 键码选择 + 绑定分组。 */
    @Composable
    private fun ButtonEventChild(e: ButtonEventData.Event) {
        EventSwitchRow(stringResource(R.string.edit_button_event_auto_keep), e.isAutoKeep) { e.isAutoKeep = it }
        EventSwitchRow(stringResource(R.string.edit_button_event_auto_click), e.isAutoClick) { e.isAutoClick = it }
        EventSwitchRow(stringResource(R.string.edit_button_event_open_menu), e.isOpenMenu) { e.isOpenMenu = it }
        EventSwitchRow(stringResource(R.string.edit_button_event_touch_mode), e.isSwitchTouchMode) { e.isSwitchTouchMode = it }
        EventSwitchRow(stringResource(R.string.edit_button_event_mouse_mode), e.isSwitchMouseMode) { e.isSwitchMouseMode = it }
        EventSwitchRow(stringResource(R.string.edit_button_event_input), e.isInput) { e.isInput = it }
        EventSwitchRow(stringResource(R.string.edit_button_event_quick_input), e.isQuickInput) { e.isQuickInput = it }

        val outputTextState = remember { mutableStateOf(e.outputText) }
        LabeledTextFieldRow(
            label = stringResource(R.string.edit_button_event_output_text),
            value = outputTextState.value,
            onValueChange = {
                outputTextState.value = it
                e.outputText = it
            },
        )

        LabeledButtonRow(
            label = stringResource(R.string.edit_button_event_keycodes),
            onClick = {
                // 临时拷贝 + 每次变更即时写回（对齐遗留直改 outputKeycodesList 的生效时机）
                MiuixSelectKeycodeDialog(context, ArrayList(e.getOutputKeycodes()), false, true)
                    .apply { onChanged = { selected -> e.setOutputKeycodes(selected) } }
                    .show()
            },
        )

        LabeledButtonRow(
            label = stringResource(R.string.edit_button_event_bind_group),
            onClick = {
                val selectedViewGroups = ArrayList<ControlViewGroup>()
                for (vg in menu.controller.viewGroups()) {
                    if (e.getBindViewGroups().contains(vg.id)) {
                        selectedViewGroups.add(vg)
                    }
                }
                // 3.2 批 4 接入点：Miuix 绑定分组选择弹窗
                MiuixViewGroupDialog(context, menu, true, selectedViewGroups) { viewGroups ->
                    e.setBindViewGroup(viewGroups.map { it.id })
                }.show()
            },
        )
    }

    @Composable
    private fun EventSwitchRow(label: String, initial: Boolean, onWrite: (Boolean) -> Unit) {
        val state = remember { mutableStateOf(initial) }
        LabeledSwitchRow(
            label = label,
            checked = state.value,
            onCheckedChange = {
                state.value = it
                onWrite(it)
            },
        )
    }

    // ---------- 方向键事件页 ----------

    @Composable
    private fun DirectionEventPage(data: ControlDirectionData) {
        val event = data.event

        DirectionKeycodeRow(stringResource(R.string.edit_direction_event_up), event.getUpKeycodes()) { event.setUpKeycode(it) }
        DirectionKeycodeRow(stringResource(R.string.edit_direction_event_down), event.getDownKeycodes()) { event.setDownKeycode(it) }
        DirectionKeycodeRow(stringResource(R.string.edit_direction_event_left), event.getLeftKeycodes()) { event.setLeftKeycode(it) }
        DirectionKeycodeRow(stringResource(R.string.edit_direction_event_right), event.getRightKeycodes()) { event.setRightKeycode(it) }

        val sneakState = remember { mutableStateOf(event.isSneak) }
        LabeledSwitchRow(
            label = stringResource(R.string.edit_direction_event_sneak),
            checked = sneakState.value,
            onCheckedChange = {
                sneakState.value = it
                event.isSneak = it
            },
        )

        LabeledButtonRow(
            label = stringResource(R.string.edit_direction_event_sneak_code),
            onClick = {
                val list = arrayListOf(event.sneakKeycode)
                // Miuix 键码弹窗（对齐遗留 selectionProperty 单向 bind：初始同值、跟随后续变更）
                val dialog = MiuixSelectKeycodeDialog(context, list, true, false)
                FlowSubscriptions.subscribe(dialog.selectionFlow()) { n -> event.sneakKeycode = n.toInt() }
                dialog.show()
            },
        )

        val followOptions = DirectionEventData.FollowOption.values()
        val followOptionStrings = listOf(
            stringResource(R.string.edit_direction_event_follow_fix),
            stringResource(R.string.edit_direction_event_follow_center),
            stringResource(R.string.edit_direction_event_follow_always),
        )
        val followState = remember { mutableIntStateOf(followOptions.indexOf(event.followOption)) }
        LabeledDropdownRow(
            label = stringResource(R.string.edit_direction_event_follow),
            options = followOptionStrings,
            selectedIndex = followState.intValue,
            onSelected = {
                followState.intValue = it
                event.followOption = followOptions[it]
            },
        )
    }

    /** 四方向键码行：临时列表拷贝，确认后整体回写（与遗留 :594-629 一致）。 */
    @Composable
    private fun DirectionKeycodeRow(
        label: String,
        target: List<Int>,
        onWrite: (List<Int>) -> Unit,
    ) {
        LabeledButtonRow(
            label = label,
            onClick = {
                val list = ArrayList(target)
                // Miuix 键码弹窗（临时列表拷贝、确认后回写）
                MiuixSelectKeycodeDialog(context, list, false, false) {
                    onWrite(list.toList())
                }.show()
            },
        )
    }

    // ---------- 行级公共件 ----------

    @Composable
    private fun LabeledSwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
            )
            Switch(checked = checked, onCheckedChange = onCheckedChange, colors = fclSwitchColors())
        }
    }

    @Composable
    private fun LabeledTextFieldRow(label: String, value: String, onValueChange: (String) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(12.dp))
            FCLTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
        }
    }

    @Composable
    private fun LabeledButtonRow(label: String, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
            )
            TextButton(
                text = stringResource(R.string.menu_control_set),
                onClick = onClick,
                colors = fclDialogTextButtonColors(),
            )
        }
    }

    @Composable
    private fun LabeledDropdownRow(label: String, options: List<String>, selectedIndex: Int, onSelected: (Int) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
            )
            OptionDropdown(
                options = options,
                selectedIndex = selectedIndex,
                onSelected = onSelected,
            )
        }
    }

    /** 滑杆行：标签 + 滑杆 + 可点击数值文本（点击弹数字输入）。 */
    @Composable
    private fun SliderRow(
        label: String,
        value: Int,
        min: Int,
        max: Int,
        display: String,
        onValueChange: (Int) -> Unit,
        onValueClick: () -> Unit,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(12.dp))
            Slider(
                value = value.coerceIn(min, max).toFloat(),
                onValueChange = { onValueChange(it.toInt().coerceIn(min, max)) },
                valueRange = min.toFloat()..max.toFloat(),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = display,
                style = MiuixTheme.textStyles.body2,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(60.dp)
                    .clickable { onValueClick() },
            )
        }
    }
}

/**
 * 选项下拉（文件内私有）：TextButton 展示当前项，点击弹出 Popup 列表选择。
 * 对齐遗留 FCLSpinner 的单选语义。
 */
@Composable
private fun OptionDropdown(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    Box {
        TextButton(
            text = options[selectedIndex],
            onClick = { expanded.value = true },
            colors = fclDialogTextButtonColors(),
        )
        if (expanded.value) {
            Popup(onDismissRequest = { expanded.value = false }) {
                Card {
                    Column {
                        options.forEachIndexed { index, option ->
                            Text(
                                text = option,
                                style = MiuixTheme.textStyles.body2,
                                modifier = Modifier
                                    .clickable {
                                        onSelected(index)
                                        expanded.value = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
