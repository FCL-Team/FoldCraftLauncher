package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.control.AddInputTextDialog
import com.tungsten.fcl.control.data.QuickInputTexts
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.util.StringUtils
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版新增快捷输入文本弹窗（3.2 批 3，对应 control/AddInputTextDialog + dialog_add_input_text）。
 *
 * 行为对齐：确定时文本为空 Toast quick_input_empty、已存在 Toast quick_input_exist，
 * 否则备注非空时按 "备注&*&文本" 拼接入库（QuickInputTexts.addInputText）+ 回调 + dismiss；
 * 取消直接 dismiss。setCancelable(false) 一致。
 */
class MiuixAddInputTextDialog(
    context: Context,
    private val callback: AddInputTextDialog.Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val textState = mutableStateOf("")
    private val remarksState = mutableStateOf("")

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.quick_input_add),
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
                InputRow(stringResource(R.string.quick_input_text), textState.value) {
                    textState.value = it
                }
                InputRow(stringResource(R.string.quick_input_remarks), remarksState.value) {
                    remarksState.value = it
                }
            }
        }
    }

    @Composable
    private fun InputRow(label: String, value: String, onValueChange: (String) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
        }
    }

    private fun onPositive() {
        val text = textState.value
        if (StringUtils.isBlank(text)) {
            Toast.makeText(context, context.getString(R.string.quick_input_empty), Toast.LENGTH_SHORT).show()
        } else if (QuickInputTexts.getInputTexts().contains(text)) {
            Toast.makeText(context, context.getString(R.string.quick_input_exist), Toast.LENGTH_SHORT).show()
        } else {
            val remarksText = remarksState.value
            QuickInputTexts.addInputText(
                if (StringUtils.isNotBlank(remarksText)) "$remarksText&*&$text" else text,
            )
            callback.onTextAdd()
            dismiss()
        }
    }
}
