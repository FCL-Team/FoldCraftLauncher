package com.tungsten.fcl.ui.compose.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.ButtonStyles
import com.tungsten.fcl.control.data.ControlButtonStyle
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.fclDialogTextButtonColors
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.RadioButtonDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版按钮样式管理/选择弹窗（3.2 批 4，对应 control/ButtonStyleDialog
 * + dialog_manage_button_style + control/ButtonStyleAdapter + item_button_style）。
 *
 * 行为对齐：
 * - 样式列表数据来自 ButtonStyles.getStyles()；初始选中逻辑与 ButtonStyleAdapter 一致
 *   （initStyle 非空且列表中存在同名样式则选中 initStyle，否则选中第一个）；
 * - 单选 RadioButton 仅 select 模式显示（删除按钮两种模式都显示，与遗留 Adapter 一致）；
 * - "编辑"按钮仅 select 模式显示；添加/编辑拉起 Miuix 新增/编辑弹窗，
 *   回调逻辑（addStyle / 原位替换 / 选中新样式 / menu 内
 *   同名控件样式同步）与遗留 onClick 逐条一致；
 * - 删除先弹 FCLAlertDialog 确认（沿用原生），确认后 removeStyles + checkStyles + 刷新，
 *   且不重置当前选中（对齐遗留 notifyDataSetChanged 语义，区别于 refreshList 的重建选中）；
 * - 确定：dismiss 后仅 select 模式回调当前选中样式；
 * - 预览按钮复刻遗留 Adapter：AppCompatButton + GradientDrawable，DOWN 切按压样式、
 *   UP/CANCEL 还原普通样式（AndroidView 承载，交互逐行一致）；
 * - 窗体对齐遗留：宽 WRAP_CONTENT、高 MATCH_PARENT 的侧边面板（同 MiuixQuickInputDialog）；
 * - setCancelable(false) 一致。
 *
 * 构造签名以 Kotlin 函数类型承载回调（Java 调用点传表达式 lambda 可兼容 Function1）。
 */
class MiuixButtonStyleDialog(
    context: Context,
    private val select: Boolean,
    private val initStyle: ControlButtonStyle?,
    private val callback: ((ControlButtonStyle) -> Unit)?,
) : FCLComposeDialog(context, cancelable = false) {

    private var menu: GameMenu? = null

    private val stylesState = mutableStateOf<List<ControlButtonStyle>>(emptyList())
    private val selectedStyleState = mutableStateOf<ControlButtonStyle?>(null)

    init {
        refreshList()
        setDialogContent {
            DialogContent()
        }
    }

    override fun show() {
        super.show()
        // 对齐遗留：宽 WRAP_CONTENT、高 MATCH_PARENT
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun setGameMenu(menu: GameMenu?) {
        this.menu = menu
    }

    /** 对齐遗留 refreshList：重建 Adapter 会按 initStyle/首个 重新推导选中。 */
    private fun refreshList() {
        val styles = ButtonStyles.getStyles()
        stylesState.value = styles.toList()
        selectedStyleState.value =
            if (initStyle != null && styles.any { it.name == initStyle.name }) initStyle
            else styles.firstOrNull()
    }

    /** 删除路径专用：只刷新列表数据，保留当前选中（对齐遗留 notifyDataSetChanged）。 */
    private fun reloadListKeepSelection() {
        stylesState.value = ButtonStyles.getStyles().toList()
    }

    private fun onAddStyle() {
        val onResult: (ControlButtonStyle) -> Unit = { style ->
            ButtonStyles.addStyle(style)
            refreshList()
        }
        // 3.2 批 4 接入点：Miuix 新增按钮样式弹窗
        MiuixAddButtonStyleDialog(context, null, false, onResult).show()
    }

    private fun onEditStyle() {
        val before = selectedStyleState.value ?: return
        val onResult: (ControlButtonStyle) -> Unit = { style ->
            val i = ButtonStyles.getStyles().indexOf(before)
            val beforeName = before.name
            ButtonStyles.removeStyles(before)
            ButtonStyles.addStyle(style, i)
            refreshList()
            selectedStyleState.value = style
            menu?.viewGroup?.viewData?.let { viewData ->
                viewData.getButtonList().forEach {
                    val name = it.style.name
                    if (name == style.name || name == beforeName) {
                        it.style = style
                    }
                }
            }
        }
        // 3.2 批 4 接入点：Miuix 编辑按钮样式弹窗
        MiuixAddButtonStyleDialog(context, before, true, onResult).show()
    }

    private fun onDeleteStyle(style: ControlButtonStyle) {
        FCLAlertDialog.Builder(context)
            .setCancelable(false)
            .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
            .setMessage(context.getString(R.string.style_warning_delete))
            .setPositiveButton {
                ButtonStyles.removeStyles(style)
                ButtonStyles.checkStyles()
                reloadListKeepSelection()
            }
            .setNegativeButton(null)
            .create()
            .show()
    }

    private fun onPositive() {
        dismiss()
        if (select) {
            selectedStyleState.value?.let { callback?.invoke(it) }
        }
    }

    @Composable
    private fun DialogContent() {
        val listState = rememberLazyListState()
        // 对齐遗留 listView.setSelection：initStyle 非空时定位到初始选中项
        LaunchedEffect(Unit) {
            if (initStyle != null) {
                listState.scrollToItem(ButtonStyles.findStyleIndexByName(initStyle.name))
            }
        }
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp)
                .fillMaxHeight(),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            // 对齐遗留 dialog_background（#F4F4F4 / #232323）→ surface token
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.surface),
        ) {
            androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.menu_controls_button_style),
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    textAlign = TextAlign.Center,
                )
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                ) {
                    items(stylesState.value, key = { it.name }) { style ->
                        StyleRow(style)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        text = stringResource(R.string.menu_control_style_add),
                        onClick = { onAddStyle() },
                        colors = fclDialogTextButtonColors(),
                    )
                    if (select) {
                        TextButton(
                            text = stringResource(R.string.menu_control_style_edit),
                            onClick = { onEditStyle() },
                            colors = fclDialogTextButtonColors(),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { onPositive() },
                        colors = fclDialogTextButtonColors(),
                    )
                }
            }
        }
    }

    @Composable
    private fun StyleRow(style: ControlButtonStyle) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ButtonStylePreview(style = style, revision = 0)
            Spacer(Modifier.width(10.dp))
            Text(
                text = style.name,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (select) {
                RadioButton(
                    selected = selectedStyleState.value === style,
                    onClick = { selectedStyleState.value = style },
                    // 对齐遗留 FCLRadioButton：按钮圆点 tint = dkColor（primaryVariant），不用 primary
                    colors = RadioButtonDefaults.radioButtonColors(
                        selectedColor = MiuixTheme.colorScheme.primaryVariant,
                    ),
                )
            }
            IconButton(onClick = { onDeleteStyle(style) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_delete_24),
                    contentDescription = null,
                    // 对齐遗留 item_button_style：darker_gray 静态描边色，不用 color2
                    tint = MiuixTheme.colorScheme.outline,
                )
            }
        }
    }
}

/**
 * 按钮样式预览（复刻遗留 ButtonStyleAdapter/AddButtonStyleDialog 的 AppCompatButton
 * + GradientDrawable + OnTouch DOWN/UP 切换按压样式）。
 *
 * @param revision 样式变更计数：读取它使样式属性变化时触发 update 重绘。
 */
@SuppressLint("ClickableViewAccessibility")
@Composable
internal fun ButtonStylePreview(
    style: ControlButtonStyle,
    revision: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier.size(50.dp),
        factory = { AppCompatButton(it) },
        update = { button ->
            @Suppress("UNUSED_EXPRESSION")
            revision
            val drawableNormal = GradientDrawable().apply {
                cornerRadius = ConvertUtils.dip2px(context, style.cornerRadius / 10f).toFloat()
                setStroke(ConvertUtils.dip2px(context, style.strokeWidth / 10f), style.strokeColor)
                setColor(style.fillColor)
            }
            val drawablePressed = GradientDrawable().apply {
                cornerRadius = ConvertUtils.dip2px(context, style.cornerRadiusPressed / 10f).toFloat()
                setStroke(ConvertUtils.dip2px(context, style.strokeWidthPressed / 10f), style.strokeColorPressed)
                setColor(style.fillColorPressed)
            }
            button.gravity = Gravity.CENTER
            button.setPadding(0, 0, 0, 0)
            button.text = "S"
            button.setAllCaps(false)
            button.textSize = style.textSize.toFloat()
            button.setTextColor(style.textColor)
            button.background = drawableNormal
            button.setOnTouchListener { view, event ->
                val v = view as AppCompatButton
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        v.textSize = style.textSizePressed.toFloat()
                        v.setTextColor(style.textColorPressed)
                        v.background = drawablePressed
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.textSize = style.textSize.toFloat()
                        v.setTextColor(style.textColor)
                        v.background = drawableNormal
                    }
                }
                true
            }
        },
    )
}
