package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard

/**
 * Miuix 版登录失败（服务器断开）弹窗（3.2 批 1，对应 LauncherHelper.SkipLoginDialog + dialog_skip_login）。
 *
 * 行为对齐：重试/跳过/取消三个按钮均先执行动作再 dismiss；setCancelable(false) 一致。
 * 动作（含 future 完成）由调用点 LauncherHelper 以 Runnable 注入，避免回依赖其私有方法。
 */
class MiuixSkipLoginDialog(
    context: Context,
    private val onRetry: Runnable,
    private val onSkip: Runnable,
    private val onCancel: Runnable,
) : FCLComposeDialog(context, cancelable = false) {

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.account_failed),
                summary = stringResource(R.string.account_failed_server_disconnected),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { onCancel.run(); dismiss() },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.action_skip),
                        onClick = { onSkip.run(); dismiss() },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.action_retry),
                        onClick = { onRetry.run(); dismiss() },
                    ),
                ),
            )
        }
    }
}

/**
 * Miuix 版凭证过期提示重新登录弹窗（3.2 批 1，对应 LauncherHelper.TipReLoginLoginDialog + dialog_tip_relogin）。
 *
 * 行为对齐：跳过/确定按钮均先执行动作再 dismiss；setCancelable(false) 一致。
 */
class MiuixTipReLoginDialog(
    context: Context,
    private val onSkip: Runnable,
    private val onOk: Runnable,
) : FCLComposeDialog(context, cancelable = false) {

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.account_failed),
                summary = stringResource(R.string.account_failed_expired),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { onOk.run(); dismiss() },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.action_skip),
                        onClick = { onSkip.run(); dismiss() },
                    ),
                ),
            )
        }
    }
}
