package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.control.download.ControllerVersion
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版手柄历史版本下载弹窗（3.2 批 1，对应 OldVersionDialog + dialog_download_controllor）。
 *
 * 行为对齐：列表展示历史版本名（遗留固定高 180dp），点击回调 download(versionCode)
 * 并 dismiss；取消按钮 dismiss；setCancelable(false) 一致。
 * [Callback] 签名与遗留 OldVersionDialog.Callback 一致，调用点方法引用无需改动。
 */
class MiuixOldVersionDialog(
    context: Context,
    versionInfos: ArrayList<ControllerVersion.VersionInfo>,
    private val callback: Callback,
) : FCLComposeDialog(context, cancelable = false) {

    private val versions: List<ControllerVersion.VersionInfo> = versionInfos

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.control_download_history),
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                ) {
                    items(versions) { info ->
                        Text(
                            text = info.versionName,
                            style = MiuixTheme.textStyles.body2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    callback.download(info.versionCode)
                                    dismiss()
                                }
                                .padding(10.dp),
                        )
                    }
                }
            }
        }
    }

    fun interface Callback {
        fun download(versionCode: Int)
    }
}
