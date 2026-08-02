package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogButtonsRow
import com.tungsten.fcl.ui.controller.ControllerInfoDialog
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.platform.OperatingSystem
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版控制器信息弹窗（3.2 批 4，对应 ui/controller/ControllerInfoDialog + dialog_controller_info）。
 *
 * 行为对齐：
 * - 标题按 create 取 control_create / control_info_edit；
 * - 「更多信息」CheckBox 展开/收起更多信息区（版本/版本代码/作者/描述），默认收起；
 * - versionCode 仅允许数字输入（对齐遗留 setIntegerFilter(1) 的 ^[0-9]*$ 过滤）；
 * - 确定时名称校验（OperatingSystem.isNameValid 且不为 "Error"），不合法 Toast
 *   control_info_name_invalid 且不关闭；作者变更则重新生成随机 id；versionCode 空白按 "1"
 *   解析；controllerVersion 沿用原控制器；随后回调 + dismiss；
 * - 取消直接 dismiss；
 * - 固定宽 400dp、setCancelable(false) 一致。
 *
 * 有意偏差：
 * - 遗留展开/收起时动态改窗口高度（MATCH_PARENT / 200dp），Compose 用 AnimatedVisibility
 *   条件渲染，窗口始终 WRAP_CONTENT 自适应（附录 D 登记）；
 * - 遗留按钮布局为确定在左、取消在右，本实现按既有 Miuix 弹窗惯例主按钮（确定）居右；
 * - 遗留输入框 hint（input_hint_not_empty / input_hint_optional）未在 Miuix TextField 复刻，
 *   不影响交互逻辑。
 */
class MiuixControllerInfoDialog(
    context: Context,
    create: Boolean,
    private val controller: Controller,
    private val callback: ControllerInfoDialog.Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val title = if (create) R.string.control_create else R.string.control_info_edit

    private val nameState = mutableStateOf(controller.name)
    private val versionState = mutableStateOf(controller.version)
    private val versionCodeState = mutableStateOf(controller.versionCode.toString())
    private val authorState = mutableStateOf(controller.author)
    private val descriptionState = mutableStateOf(controller.description)
    private val moreInfoState = mutableStateOf(false)

    init {
        setDialogContent {
            DialogContent()
        }
    }

    private fun onPositive() {
        val name = nameState.value
        if (!OperatingSystem.isNameValid(name) || name == "Error") {
            Toast.makeText(context, context.getString(R.string.control_info_name_invalid), Toast.LENGTH_SHORT).show()
        } else {
            // 作者变更重新生成随机 id（对齐遗留 :99-101）
            var id = controller.id
            if (authorState.value != controller.author) {
                id = Controller.generateRandomId()
            }
            val newController = Controller(
                id,
                name,
                versionState.value,
                (if (StringUtils.isBlank(versionCodeState.value)) "1" else versionCodeState.value).toInt(),
                authorState.value,
                descriptionState.value,
                controller.controllerVersion,
            )
            callback.onInfoGenerate(newController)
            dismiss()
        }
    }

    @Composable
    private fun DialogContent() {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column {
                Text(
                    text = stringResource(title),
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(10.dp))
                // 表单区限高可滚动（展开更多信息 + 多行描述时可能超高），
                // 标题与按钮区钉在滚动区外，横屏小屏下按钮不被挤出
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                ) {
                    InputRow(
                        label = stringResource(R.string.control_info_name),
                        value = nameState.value,
                    ) { nameState.value = it }
                    Row(
                        modifier = Modifier
                            .clickable { moreInfoState.value = !moreInfoState.value }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            state = if (moreInfoState.value) ToggleableState.On else ToggleableState.Off,
                            onClick = { moreInfoState.value = !moreInfoState.value },
                        )
                        Text(
                            text = stringResource(R.string.control_info_more),
                            style = MiuixTheme.textStyles.body2,
                        )
                    }
                    AnimatedVisibility(visible = moreInfoState.value) {
                        Column {
                            InputRow(
                                label = stringResource(R.string.control_info_version),
                                value = versionState.value,
                            ) { versionState.value = it }
                            InputRow(
                                label = stringResource(R.string.control_info_version_code),
                                value = versionCodeState.value,
                            ) { input ->
                                // 对齐遗留 setIntegerFilter(1)：仅允许数字
                                if (input.all { it.isDigit() }) versionCodeState.value = input
                            }
                            InputRow(
                                label = stringResource(R.string.control_info_author),
                                value = authorState.value,
                            ) { authorState.value = it }
                            InputRow(
                                label = stringResource(R.string.control_info_description),
                                value = descriptionState.value,
                                singleLine = false,
                            ) { descriptionState.value = it }
                        }
                    }
                }
                FCLDialogButtonsRow(
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
                )
            }
        }
    }

    @Composable
    private fun InputRow(
        label: String,
        value: String,
        singleLine: Boolean = true,
        onValueChange: (String) -> Unit,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(250.dp),
                singleLine = singleLine,
            )
        }
    }
}
