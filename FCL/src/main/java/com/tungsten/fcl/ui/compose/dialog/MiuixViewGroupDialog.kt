package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.control.EditViewGroupDialog
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLCornerRadius
import com.tungsten.fcl.ui.compose.FCLCheckBox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.UUID

/**
 * Miuix 版分组管理/选择弹窗（3.2 批 4，对应 control/ViewGroupDialog + dialog_manage_view_groups
 * + item_view_group + control/ViewGroupAdapter 的条目交互）。
 *
 * 构造签名与另一代理约定固定，遗留调用点（GameMenu / MiuixEditViewDialog）直接使用。
 *
 * 行为对齐：
 * - select 模式：条目 CheckBox 多选，确定回调当前选择列表后 dismiss；
 *   「添加分组」按钮隐藏（遗留 GONE 一致）；
 * - 管理模式：条目上/下移（controller.swapViewGroups + controller.updateViewGroup，语义与
 *   ViewGroupAdapter 的 Collections.swap 一致）、编辑（弹 MiuixEditViewGroupDialog）、删除（FCLAlertDialog 确认后
 *   controller.removeViewGroup）；
 * - 「添加分组」弹 Miuix 新增分组弹窗，确认后 new ControlViewGroup(UUID)
 *   设置名称/可见性并 addViewGroup，与遗留逐行一致（遗留传入弹窗的临时 ControlViewGroup
 *   同样未被复用，此处保留该语义）；
 * - 分组数据来自 gameMenu.controller.viewGroups()，操作后刷新快照；
 * - setCancelable(false) 一致；窗体对齐遗留 400dp × MATCH_PARENT 侧边面板。
 *
 * 有意偏差：遗留 CheckBox 实时改写传入的 selectedGroups（observable ObservableList），
 * 确定时回调的是同一实例；本实现内部持有选择快照，确定时回调快照内容。
 * 两个真实调用点（GameMenu:926 传空列表+null 回调；EditViewDialog:387 仅以回调结果
 * setBindViewGroup）均不依赖实时外部改写，行为等价。
 *
 * 运行于游戏内（GameMenu → JVMActivity/ControllerActivity，AppCompatActivity），
 * AppCompatDialog + ComposeView 可用（同批 2 MiuixGameItemBarSettingDialog 先例）。
 */
class MiuixViewGroupDialog(
    context: Context,
    private val gameMenu: GameMenu,
    private val select: Boolean,
    selectedGroups: List<ControlViewGroup>,
    private val callback: ((List<ControlViewGroup>) -> Unit)?,
) : FCLComposeDialog(context, cancelable = false) {

    private val groupsState = mutableStateOf<List<ControlViewGroup>>(emptyList())
    private val selectedState = mutableStateOf(selectedGroups.toList())

    init {
        refreshList()
        setDialogContent {
            DialogContent()
        }
    }

    override fun show() {
        super.show()
        // 对齐遗留：宽 400dp、高 MATCH_PARENT 侧边面板
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun refreshList() {
        groupsState.value = gameMenu.controller.viewGroups().toList()
    }

    private fun onAddViewGroup() {
        val callback = EditViewGroupDialog.Callback { name, visibility ->
            val viewGroup = ControlViewGroup(UUID.randomUUID().toString())
            viewGroup.name = name
            viewGroup.visibility = visibility
            gameMenu.controller.addViewGroup(viewGroup)
            refreshList()
        }
        // 3.2 批 4 接入点：Miuix 新增分组弹窗
        MiuixEditViewGroupDialog(context, gameMenu, ControlViewGroup(UUID.randomUUID().toString()), callback).show()
    }

    private fun onEditViewGroup(group: ControlViewGroup) {
        val callback = EditViewGroupDialog.Callback { name, visibility ->
            group.name = name
            group.visibility = visibility
            gameMenu.controller.updateViewGroup(group)
            refreshList()
        }
        // 3.2 批 4 接入点：Miuix 编辑分组弹窗
        MiuixEditViewGroupDialog(context, gameMenu, group, callback).show()
    }

    private fun onDeleteViewGroup(group: ControlViewGroup) {
        val builder = FCLAlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
        builder.setMessage(context.getString(R.string.menu_control_view_group_delete))
        builder.setPositiveButton {
            gameMenu.controller.removeViewGroup(group)
            refreshList()
        }
        builder.setNegativeButton(null)
        builder.create().show()
    }

    private fun onMove(index: Int, up: Boolean) {
        val list = gameMenu.controller.viewGroups()
        val group = list[index]
        val pos = if (up) index - 1 else index + 1
        if (pos < 0 || pos > list.size - 1) {
            return
        }
        gameMenu.controller.swapViewGroups(index, pos)
        gameMenu.controller.updateViewGroup(group)
        refreshList()
    }

    private fun onToggleSelect(group: ControlViewGroup, checked: Boolean) {
        selectedState.value = if (checked) {
            if (selectedState.value.any { it.id == group.id }) selectedState.value else selectedState.value + group
        } else {
            selectedState.value.filterNot { it.id == group.id }
        }
    }

    @Composable
    private fun DialogContent() {
        FCLCard(
            cornerRadius = FCLCornerRadius.Dialog,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.menu_controls_groups),
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    textAlign = TextAlign.Center,
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                ) {
                    itemsIndexed(groupsState.value, key = { _, group -> group.id }) { index, group ->
                        ViewGroupRow(index, group)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!select) {
                        TextButton(
                            text = stringResource(R.string.menu_control_view_group_add),
                            onClick = { onAddViewGroup() },
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = {
                            callback?.invoke(selectedState.value)
                            dismiss()
                        },
                    )
                }
            }
        }
    }

    @Composable
    private fun ViewGroupRow(index: Int, group: ControlViewGroup) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_keyboard_24),
                contentDescription = null,
                // 对齐遗留 item_view_group：android:tint="@android:color/darker_gray"（静态，不随主题）
                tint = FCLThemeTokens.StrokeGray,
            )
            Text(
                text = group.name,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
            )
            if (select) {
                val checked = selectedState.value.any { it.id == group.id }
                FCLCheckBox(
                    state = if (checked) ToggleableState.On else ToggleableState.Off,
                    onClick = { onToggleSelect(group, !checked) },
                )
            } else {
                IconButton(onClick = { onMove(index, true) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_arrow_upward_24),
                        contentDescription = null,
                        // 对齐遗留 item_view_group：android:tint="@android:color/darker_gray"（静态，不随主题）
                        tint = FCLThemeTokens.StrokeGray,
                    )
                }
                IconButton(onClick = { onMove(index, false) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_arrow_downward_24),
                        contentDescription = null,
                        // 对齐遗留 item_view_group：android:tint="@android:color/darker_gray"（静态，不随主题）
                        tint = FCLThemeTokens.StrokeGray,
                    )
                }
                IconButton(onClick = { onEditViewGroup(group) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_edit_24),
                        contentDescription = null,
                        // 对齐遗留 item_view_group：android:tint="@android:color/darker_gray"（静态，不随主题）
                        tint = FCLThemeTokens.StrokeGray,
                    )
                }
                IconButton(onClick = { onDeleteViewGroup(group) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_delete_24),
                        contentDescription = null,
                        // 对齐遗留 item_view_group：android:tint="@android:color/darker_gray"（静态，不随主题）
                        tint = FCLThemeTokens.StrokeGray,
                    )
                }
            }
        }
    }
}
