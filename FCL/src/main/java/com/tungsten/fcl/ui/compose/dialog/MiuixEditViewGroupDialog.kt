package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.tungsten.fcl.R
import com.tungsten.fcl.control.EditViewGroupDialog
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.util.StringUtils
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版新增/编辑分组弹窗（3.2 批 4，对应 control/EditViewGroupDialog + dialog_edit_view_group）。
 *
 * 行为对齐：
 * - 名称输入初值取 viewGroup.name；可见性二选一（VISIBLE/INVISIBLE），初值取 viewGroup.visibility；
 * - 确定校验与遗留同序：先查重（controller.viewGroups() 中存在同名且非自身 → Toast
 *   menu_control_view_group_exist），再查空白（StringUtils.isBlank → Toast
 *   menu_control_view_group_empty），通过则 dismiss 并回调 onPositive(name, visibility)；
 * - 取消直接 dismiss；setCancelable(false) 一致。
 *
 * 回调直接复用遗留 [EditViewGroupDialog.Callback] 接口（Java 调用点 lambda 零改动兼容，
 * 参数类型与遗留一致为 ControlViewGroup.Visibility）。
 * 可见性下拉以标准 Compose Popup 实现（工程未引入 compose-material，无 DropdownMenu 可用）。
 */
class MiuixEditViewGroupDialog(
    context: Context,
    private val menu: GameMenu,
    private val viewGroup: ControlViewGroup,
    private val callback: EditViewGroupDialog.Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val nameState = mutableStateOf(viewGroup.name)
    private val visibilityIndexState = mutableIntStateOf(
        if (viewGroup.visibility == ControlViewGroup.Visibility.VISIBLE) 0 else 1
    )

    init {
        setDialogContent {
            val visibilityOptions = listOf(
                stringResource(R.string.menu_control_view_group_visible),
                stringResource(R.string.menu_control_view_group_invisible),
            )
            FCLDialogCard(
                title = stringResource(R.string.menu_control_view_group_add),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.menu_control_view_group_name),
                        style = MiuixTheme.textStyles.body2,
                    )
                    Spacer(Modifier.width(12.dp))
                    TextField(
                        value = nameState.value,
                        onValueChange = { nameState.value = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.menu_control_view_group_visibility),
                        style = MiuixTheme.textStyles.body2,
                    )
                    Spacer(Modifier.weight(1f))
                    OptionDropdown(
                        options = visibilityOptions,
                        selectedIndex = visibilityIndexState.intValue,
                        onSelected = { visibilityIndexState.intValue = it },
                    )
                }
            }
        }
    }

    private fun onPositive() {
        val name = nameState.value
        if (menu.controller.viewGroups().any { it.name == name && viewGroup.name != name }) {
            Toast.makeText(context, context.getString(R.string.menu_control_view_group_exist), Toast.LENGTH_SHORT).show()
        } else if (StringUtils.isBlank(name)) {
            Toast.makeText(context, context.getString(R.string.menu_control_view_group_empty), Toast.LENGTH_SHORT).show()
        } else {
            dismiss()
            callback.onPositive(
                name,
                if (visibilityIndexState.intValue == 0) ControlViewGroup.Visibility.VISIBLE else ControlViewGroup.Visibility.INVISIBLE,
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
