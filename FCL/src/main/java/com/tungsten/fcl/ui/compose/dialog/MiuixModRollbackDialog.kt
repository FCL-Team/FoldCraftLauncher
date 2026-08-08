package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.mod.LocalModFile
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版 Mod 版本回滚弹窗（3.2 批 2，对应已删除的 ui/manage/ModRollbackDialog + dialog_rollback_mod）。
 *
 * 行为对齐：列表展示可回滚旧版本号，点击某项 dismiss 并回调 onOldVersionSelect；
 * 取消按钮 dismiss；setCancelable(false) 一致。
 * 6.1：Callback 收归本类（原复用遗留 ModRollbackDialog.Callback，旧类已删除）。
 */
class MiuixModRollbackDialog(
    context: Context,
    private val list: List<LocalModFile>,
    private val callback: Callback,
) : FCLComposeDialog(context, cancelable = false) {

    fun interface Callback {
        fun onOldVersionSelect(localModFile: LocalModFile)
    }

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.archive_version),
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
                        .heightIn(max = 320.dp),
                ) {
                    items(list) { localModFile ->
                        Text(
                            text = localModFile.version,
                            style = MiuixTheme.textStyles.body2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    dismiss()
                                    callback.onOldVersionSelect(localModFile)
                                }
                                .padding(10.dp),
                        )
                    }
                }
            }
        }
    }
}
