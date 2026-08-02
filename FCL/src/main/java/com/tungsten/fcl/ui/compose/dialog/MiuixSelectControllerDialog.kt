package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogButtonsRow
import com.tungsten.fclcore.observable.InvalidationListener
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版选择控制器弹窗（3.2 批 4，对应 control/SelectControllerDialog
 * + dialog_select_controller + control/SelectableControllerListAdapter + item_controller_selectable）。
 *
 * 行为对齐：
 * - 控制器单选列表：RadioButton + 名称/版本/描述（遗留 adapter 的绑定字段），
 *   点 RadioButton 更新选中项（遗留仅 RadioButton 可点，行本身无点击监听，保持一致）；
 * - 初始选中构造参数 id 对应项（遗留循环不 break，等价取最后一个匹配项），无匹配则 null；
 * - 监听 Controllers 列表失效：未初始化直接返回；列表空且当前有选中则置 null，
 *   否则选中项不在列表中时回退到第 0 项（对齐遗留 selectedController.invalidated）；
 * - 确定按钮回调选中项后 dismiss（顺序同遗留 onClick）；
 * - 窗体对齐遗留 XML：宽 400dp、列表固定高 160dp；
 * - setCancelable(false) 一致。
 *
 * 有意偏差：遗留列表项用 observable 属性绑定（控制器属性改名可实时刷新条目文字），
 * 本实现读取快照值；弹窗存活期间控制器属性几乎不会被编辑，影响可忽略。
 */
class MiuixSelectControllerDialog(
    context: Context,
    id: String,
    private val callback: Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val controllersState = mutableStateOf(Controllers.getControllers().toList())
    // 遗留循环不 break，命中多个时最后一个生效（id 唯一，正常只命中一个）
    private val selectedState = mutableStateOf(Controllers.getControllers().lastOrNull { it.id == id })

    private val controllersListener = InvalidationListener { onControllersInvalidated() }

    init {
        Controllers.getControllers().addListener(controllersListener)
        setDialogContent {
            DialogContent()
        }
    }

    override fun onStop() {
        super.onStop()
        Controllers.getControllers().removeListener(controllersListener)
    }

    /** 对齐遗留 SelectControllerDialog.selectedController 的 invalidated 回退逻辑。 */
    private fun onControllersInvalidated() {
        if (!Controllers.isInitialized()) return

        val list = Controllers.getControllers().toList()
        controllersState.value = list
        val selected = selectedState.value
        if (list.isEmpty()) {
            if (selected != null) {
                selectedState.value = null
            }
        } else if (!list.contains(selected)) {
            selectedState.value = list[0]
        }
    }

    private fun onPositive() {
        callback.onControllerSelected(selectedState.value)
        dismiss()
    }

    @Composable
    private fun DialogContent() {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = stringResource(R.string.control_select),
                modifier = Modifier.fillMaxWidth(),
                style = MiuixTheme.textStyles.title4,
                textAlign = TextAlign.Center,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(vertical = 10.dp),
            ) {
                items(controllersState.value) { controller ->
                    ControllerRow(controller)
                }
            }
            FCLDialogButtonsRow(
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { onPositive() },
                    ),
                ),
            )
        }
    }

    @Composable
    private fun ControllerRow(controller: Controller) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = controller === selectedState.value,
                onClick = { selectedState.value = controller },
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = controller.name,
                        style = MiuixTheme.textStyles.body2,
                        maxLines = 1,
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = controller.version,
                        style = MiuixTheme.textStyles.footnote1,
                        maxLines = 1,
                    )
                }
                Text(
                    text = controller.description,
                    style = MiuixTheme.textStyles.footnote1,
                    maxLines = 1,
                )
            }
        }
    }

    fun interface Callback {
        fun onControllerSelected(controller: Controller?)
    }
}
