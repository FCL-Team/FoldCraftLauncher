package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.util.StringUtils
import top.yukonga.miuix.kmp.basic.TextField

/**
 * Miuix 版整合包链接输入弹窗（3.2 批 1，对应 ModpackUrlDialog + dialog_modpack_url）。
 *
 * 行为对齐：确定时仅当输入非空白才回调 onPositive(url) 并 dismiss（空白静默忽略）；
 * 取消直接 dismiss；setCancelable(false) 一致。[Callback] 签名与遗留 ModpackUrlDialog.Callback 一致。
 */
class MiuixModpackUrlDialog(
    context: Context,
    private val callback: Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val urlState = mutableStateOf("")

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.modpack_choose_remote_tooltip),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = {
                            if (StringUtils.isNotBlank(urlState.value)) {
                                callback.onPositive(urlState.value)
                                dismiss()
                            }
                        },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                TextField(
                    value = urlState.value,
                    onValueChange = { urlState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        }
    }

    fun interface Callback {
        fun onPositive(urlString: String)
    }
}
