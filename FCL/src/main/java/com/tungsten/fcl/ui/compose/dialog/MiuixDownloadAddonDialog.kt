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
import com.tungsten.fcl.ui.download.common.DownloadAddonDialog
import com.tungsten.fclcore.util.platform.OperatingSystem
import top.yukonga.miuix.kmp.basic.TextField

/**
 * Miuix 版下载附加内容文件名确认弹窗（3.2 批 1，对应 DownloadAddonDialog + dialog_download_addon）。
 *
 * 行为对齐：确定时校验 OperatingSystem.isNameValid，非法弹 Toast 不关闭；
 * 合法回调 onPositive(文件名) 并 dismiss；取消直接 dismiss；setCancelable(false) 一致。
 * 复用遗留 [DownloadAddonDialog.Callback]，调用点 lambda 无需改动。
 */
class MiuixDownloadAddonDialog(
    context: Context,
    name: String,
    private val callback: DownloadAddonDialog.Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val nameState = mutableStateOf(name)

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.archive_name),
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
        if (!OperatingSystem.isNameValid(nameState.value)) {
            Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
        } else {
            callback.onPositive(nameState.value)
            dismiss()
        }
    }
}
