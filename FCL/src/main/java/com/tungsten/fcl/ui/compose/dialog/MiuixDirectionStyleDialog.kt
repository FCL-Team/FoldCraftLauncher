package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.view.ViewGroup
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.control.AddDirectionStyleDialog
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.BaseInfoData
import com.tungsten.fcl.control.data.ControlDirectionStyle
import com.tungsten.fcl.control.data.DirectionStyles
import com.tungsten.fcl.control.view.ControlDirection
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版方向键样式管理/选择弹窗（3.2 批 4，对应 control/DirectionStyleDialog
 * + dialog_manage_direction_style + control/DirectionStyleAdapter + item_direction_style）。
 *
 * 行为对齐：
 * - 样式列表数据来自 DirectionStyles.getStyles()；初始选中逻辑与 DirectionStyleAdapter
 *   一致（initStyle 非空且列表中存在同名样式则选中 initStyle，否则选中第一个）；
 * - 单选 RadioButton 仅 select 模式显示；删除按钮仅非 select 模式显示
 *   （与遗留 DirectionStyleAdapter 的可见性互斥逻辑一致，注意与按钮样式域不同）；
 * - "编辑"按钮仅 select 模式显示；添加/编辑按 [ComposeDialogs.USE_COMPOSE_ADD_DIRECTION_STYLE]
 *   双分支拉起新增/编辑弹窗，回调逻辑（addStyle / 原位替换 / 选中新样式 / menu 内
 *   同名控件样式同步）与遗留 onClick 逐条一致；编辑分支同样把 GameMenu 透传给子弹窗；
 * - 删除先弹 FCLAlertDialog 确认（沿用原生），确认后 removeStyles + checkStyles + 刷新，
 *   且不重置当前选中（对齐遗留 notifyDataSetChanged 语义）；
 * - 确定：dismiss 后仅 select 模式回调当前选中样式；
 * - 预览复刻遗留 Adapter：ControlDirection 展示模式构造器 + ABSOLUTE 60×60（AndroidView 承载）；
 * - 窗体对齐遗留：宽 WRAP_CONTENT、高 MATCH_PARENT 的侧边面板；setCancelable(false) 一致。
 *
 * 构造签名以 Kotlin 函数类型承载回调（Java 调用点传表达式 lambda 可兼容 Function1）。
 */
class MiuixDirectionStyleDialog(
    context: Context,
    private val select: Boolean,
    private val initStyle: ControlDirectionStyle?,
    private val callback: ((ControlDirectionStyle) -> Unit)?,
) : FCLComposeDialog(context, cancelable = false) {

    private var menu: GameMenu? = null

    private val stylesState = mutableStateOf<List<ControlDirectionStyle>>(emptyList())
    private val selectedStyleState = mutableStateOf<ControlDirectionStyle?>(null)

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
        val styles = DirectionStyles.getStyles()
        stylesState.value = styles.toList()
        selectedStyleState.value =
            if (initStyle != null && styles.any { it.name == initStyle.name }) initStyle
            else styles.firstOrNull()
    }

    /** 删除路径专用：只刷新列表数据，保留当前选中（对齐遗留 notifyDataSetChanged）。 */
    private fun reloadListKeepSelection() {
        stylesState.value = DirectionStyles.getStyles().toList()
    }

    private fun onAddStyle() {
        val onResult: (ControlDirectionStyle) -> Unit = { style ->
            DirectionStyles.addStyle(style)
            refreshList()
        }
        // 3.2 批 4 接入点：新增方向键样式弹窗按开关双分支
        if (ComposeDialogs.USE_COMPOSE_ADD_DIRECTION_STYLE) {
            MiuixAddDirectionStyleDialog(context, null, false, onResult).show()
        } else {
            AddDirectionStyleDialog(context, null, false) { style -> onResult(style) }.show()
        }
    }

    private fun onEditStyle() {
        val before = selectedStyleState.value ?: return
        val onResult: (ControlDirectionStyle) -> Unit = { style ->
            val i = DirectionStyles.getStyles().indexOf(before)
            val beforeName = before.name
            DirectionStyles.removeStyles(before)
            DirectionStyles.addStyle(style, i)
            refreshList()
            selectedStyleState.value = style
            menu?.viewGroup?.viewData?.let { viewData ->
                viewData.directionList().forEach {
                    val name = it.style.name
                    if (name == style.name || name == beforeName) {
                        it.style = style
                    }
                }
            }
        }
        // 3.2 批 4 接入点：编辑方向键样式弹窗按开关双分支（GameMenu 透传一致）
        if (ComposeDialogs.USE_COMPOSE_ADD_DIRECTION_STYLE) {
            MiuixAddDirectionStyleDialog(context, before, true, onResult)
                .apply { setGameMenu(menu) }
                .show()
        } else {
            AddDirectionStyleDialog(context, before, true) { style -> onResult(style) }
                .apply { setGameMenu(menu) }
                .show()
        }
    }

    private fun onDeleteStyle(style: ControlDirectionStyle) {
        FCLAlertDialog.Builder(context)
            .setCancelable(false)
            .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
            .setMessage(context.getString(R.string.style_warning_delete))
            .setPositiveButton {
                DirectionStyles.removeStyles(style)
                DirectionStyles.checkStyles()
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
                listState.scrollToItem(DirectionStyles.findStyleIndexByName(initStyle.name))
            }
        }
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp)
                .fillMaxHeight(),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.menu_controls_direction_style),
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
                    )
                    if (select) {
                        TextButton(
                            text = stringResource(R.string.menu_control_style_edit),
                            onClick = { onEditStyle() },
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { onPositive() },
                    )
                }
            }
        }
    }

    @Composable
    private fun StyleRow(style: ControlDirectionStyle) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DirectionStylePreview(style = style, revision = 0)
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
                )
            } else {
                IconButton(onClick = { onDeleteStyle(style) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_delete_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

/**
 * 方向键样式预览（复刻遗留 DirectionStyleAdapter/AddDirectionStyleDialog 的
 * ControlDirection 展示模式 + ABSOLUTE 60×60）。
 *
 * 与遗留 Adapter 的差异：这里统一传 style 的 clone（遗留添加弹窗路径本来就用 clone；
 * Adapter 路径直接传原对象），避免展示 View 内部状态写回共享样式对象，纯展示语义不变。
 *
 * @param revision 样式变更计数：读取它使样式属性变化时触发 update 重绘。
 */
@Composable
internal fun DirectionStylePreview(
    style: ControlDirectionStyle,
    revision: Int,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.size(60.dp),
        factory = { ControlDirection(it, null) },
        update = { direction ->
            @Suppress("UNUSED_EXPRESSION")
            revision
            direction.data.setStyle(style.clone())
            direction.data.baseInfo.sizeType = BaseInfoData.SizeType.ABSOLUTE
            direction.data.baseInfo.absoluteWidth = 60
            direction.data.baseInfo.absoluteHeight = 60
        },
    )
}
