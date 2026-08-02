package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.util.StringUtils
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File

/**
 * Miuix 版新建游戏版本/安装整合包入口弹窗（3.2 批 2，对应 ui/version/AddProfileDialog + dialog_add_profile）。
 *
 * 行为对齐：编辑按钮拉起目录单选并把结果写入路径；确定时校验名称/路径非空（Toast
 * input_not_empty）与名称不重名（Toast profile_already_exist），通过后
 * Profiles.add(Profile(name, File(path))) 并 dismiss（Compose 版本列表页自带
 * Profiles 列表监听，无需手动刷新）；取消直接 dismiss；setCancelable(false) 一致。
 */
class MiuixAddProfileDialog(
    context: Context,
) : FCLComposeDialog(context, cancelable = false) {

    private val nameState = mutableStateOf("")
    private val pathState = mutableStateOf("")

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.version_new_profile),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.profile_name),
                        style = MiuixTheme.textStyles.body2,
                    )
                    Spacer(Modifier.width(8.dp))
                    TextField(
                        value = nameState.value,
                        onValueChange = { nameState.value = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.profile_path),
                        style = MiuixTheme.textStyles.body2,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = pathState.value,
                        style = MiuixTheme.textStyles.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { onEditPath() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_edit_24),
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }

    private fun onEditPath() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true) { files ->
            pathState.value = files[0]
        }
    }

    private fun onPositive() {
        val name = nameState.value
        val path = pathState.value
        if (StringUtils.isBlank(name) || StringUtils.isBlank(path)) {
            Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
        } else if (Profiles.profiles.stream().anyMatch { it.name == name }) {
            Toast.makeText(context, context.getString(R.string.profile_already_exist), Toast.LENGTH_SHORT).show()
        } else {
            Profiles.addProfile(Profile(name, File(path)))
            dismiss()
        }
    }
}
