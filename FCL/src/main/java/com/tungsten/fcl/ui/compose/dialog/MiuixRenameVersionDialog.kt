package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.util.FutureCallback
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.concurrent.CompletableFuture

/**
 * Miuix 版重命名版本弹窗（3.2 批 1，对应 ui/version/RenameVersionDialog + dialog_rename_version）。
 *
 * 行为对齐：确定后置灰两个按钮并回调输入名；resolve → future.complete(新名) + dismiss；
 * reject → 恢复按钮并 Toast 错误；取消按钮 dismiss（future 不完成，与遗留一致）；
 * setCancelable(false) 一致。[getFuture] 语义与遗留相同。
 */
class MiuixRenameVersionDialog(
    context: Context,
    oldName: String,
    private val callback: FutureCallback<String>,
) : FCLComposeDialog(context, cancelable = false) {

    private val future = CompletableFuture<String>()

    private val nameState = mutableStateOf(oldName)
    private val buttonsEnabledState = mutableStateOf(true)

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.version_manage_rename_message),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        enabled = buttonsEnabledState.value,
                        onClick = { onPositive() },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        enabled = buttonsEnabledState.value,
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                Text(
                    text = stringResource(R.string.version_manage_rename_new),
                    style = MiuixTheme.textStyles.body2,
                )
                TextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        }
    }

    private fun onPositive() {
        buttonsEnabledState.value = false
        callback.call(nameState.value, {
            buttonsEnabledState.value = true
            future.complete(nameState.value)
            dismiss()
        }, { msg ->
            buttonsEnabledState.value = true
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        })
    }

    fun getFuture(): CompletableFuture<String> = future
}
