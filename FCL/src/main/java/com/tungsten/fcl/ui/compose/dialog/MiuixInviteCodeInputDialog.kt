package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.terracotta.Terracotta
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import net.burningtnt.terracotta.TerracottaAndroidAPI
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版联机邀请码输入弹窗（3.2 批 2，对应 control/MultiplayerDialog.java:283
 * WaitingUI.InviteCodeInputDialog + dialog_input_invite_code）。
 *
 * 行为对齐：输入时实时校验——空串隐藏提示；TERRACOTTA_LEGACY/PCL2CE 黄色提示、
 * SCAFFOLDING 绿色提示、其余红色无效提示；确定时 parseRoomCode 非空才回调
 * onPositive 并 dismiss，否则 Toast 无效提示；取消 dismiss；show() 时清空输入；
 * setCancelable(false) 一致。
 */
class MiuixInviteCodeInputDialog(
    context: Context,
    private val listener: Listener,
) : FCLComposeDialog(context, cancelable = false) {

    fun interface Listener {
        fun onPositive(code: String)
    }

    private val codeState = mutableStateOf("")
    private val validationState = mutableStateOf<Pair<Int, Color>?>(null)

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.terracotta_status_waiting_guest_prompt_title),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { onPositiveClick() },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                FCLTextField(
                    value = codeState.value,
                    onValueChange = {
                        codeState.value = it
                        refreshValidation()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.terracotta_code_hint),
                    singleLine = true,
                )
                validationState.value?.let { (textRes, color) ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(textRes),
                        style = MiuixTheme.textStyles.body2,
                        color = color,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    override fun show() {
        super.show()
        codeState.value = ""
        refreshValidation()
    }

    private fun onPositiveClick() {
        if (Terracotta.parseRoomCode(codeState.value) != null) {
            listener.onPositive(codeState.value)
            dismiss()
        } else {
            Toast.makeText(context, context.getString(R.string.terracotta_status_waiting_guest_prompt_invalid), Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshValidation() {
        val code = codeState.value
        if (code.isEmpty()) {
            validationState.value = null
            return
        }
        validationState.value = when (Terracotta.parseRoomCode(code)) {
            TerracottaAndroidAPI.RoomType.TERRACOTTA_LEGACY ->
                R.string.terracotta_status_waiting_guest_prompt_terracotta_legacy to Color.Yellow
            TerracottaAndroidAPI.RoomType.PCL2CE ->
                R.string.terracotta_status_waiting_guest_prompt_pcl2ce to Color.Yellow
            TerracottaAndroidAPI.RoomType.SCAFFOLDING ->
                R.string.terracotta_status_waiting_guest_prompt_scaffolding to Color.Green
            else ->
                R.string.terracotta_status_waiting_guest_prompt_invalid to Color.Red
        }
    }
}
