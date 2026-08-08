package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.util.FutureCallback
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcl.ui.compose.FCLCheckBox
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版复制版本弹窗（3.2 批 1，对应 ui/version/DuplicateVersionDialog + dialog_duplicate_version）。
 *
 * 行为对齐：
 * - 输入校验顺序一致：空白 → input_not_empty；id 冲突 → install_new_game_already_exists；
 *   非法 id → install_new_game_malformed（均 Toast 且不关闭）；
 * - 校验通过后置灰两个按钮，回调 ArrayList(新名, 是否复制存档)；
 *   resolve → 恢复按钮并 dismiss；reject → 恢复按钮并 Toast 错误；
 * - 取消按钮 dismiss；setCancelable(false) 一致。
 */
class MiuixDuplicateVersionDialog(
    context: Context,
    private val profile: Profile,
    version: String,
    private val callback: FutureCallback<ArrayList<Any>>,
) : FCLComposeDialog(context, cancelable = false) {

    private val nameState = mutableStateOf(version)
    private val copySavesState = mutableStateOf(false)
    private val buttonsEnabledState = mutableStateOf(true)

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.version_manage_duplicate),
                summary = stringResource(R.string.version_manage_duplicate_confirm),
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
                    text = stringResource(R.string.version_manage_duplicate_prompt),
                    style = MiuixTheme.textStyles.body2,
                )
                FCLTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { copySavesState.value = !copySavesState.value },
                ) {
                    FCLCheckBox(
                        state = if (copySavesState.value) ToggleableState.On else ToggleableState.Off,
                        onClick = { copySavesState.value = !copySavesState.value },
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.version_manage_duplicate_duplicate_save),
                        style = MiuixTheme.textStyles.body2,
                    )
                }
            }
        }
    }

    private fun onPositive() {
        val newVersionName = nameState.value
        when {
            StringUtils.isBlank(newVersionName) ->
                Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
            profile.repository.versionIdConflicts(newVersionName) ->
                Toast.makeText(context, context.getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show()
            !FCLGameRepository.isValidVersionId(newVersionName) ->
                Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
            else -> {
                buttonsEnabledState.value = false
                val res = ArrayList<Any>()
                res.add(newVersionName)
                res.add(copySavesState.value)
                callback.call(res, {
                    buttonsEnabledState.value = true
                    dismiss()
                }, { msg ->
                    buttonsEnabledState.value = true
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}
