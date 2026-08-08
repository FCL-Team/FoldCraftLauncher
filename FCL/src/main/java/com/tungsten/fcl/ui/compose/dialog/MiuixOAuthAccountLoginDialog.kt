package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.OAuthServer
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclcore.auth.AuthInfo
import com.tungsten.fclcore.auth.OAuthAccount
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import kotlinx.coroutines.flow.MutableStateFlow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.function.Consumer
import java.util.logging.Level

/**
 * Miuix 版 OAuth 重新登录弹窗（3.2 批 3，对应 ui/account/OAuthAccountLoginDialog + dialog_relogin_oauth）。
 *
 * **阻塞契约保持不变**：调用方 AccountListItem.logIn 在后台线程以 CountDownLatch
 * 同步等待本对话框结果，本类保持相同的回调语义——登录成功 success.accept(authInfo) +
 * dismiss，取消 failed.run() + dismiss，二者均在 UI 线程恰好触发一次，latch 正常放行；
 * 登录失败弹 FCLAlertDialog（遗留沿用）并恢复按钮可用，对话框不关闭、latch 不放行，
 * 与遗留逐行等价。
 *
 * 其余行为对齐：登录按钮**长按 = 外部浏览器**走 OAuth（useExternalBrowser）；
 * 设备码事件 onGrantDeviceCode 自动复制用户码、onOpenBrowser 按 useExternalBrowser
 * 打开外部浏览器/内置 WebView；登录中双按钮禁用。setCancelable(false) 一致。
 */
class MiuixOAuthAccountLoginDialog(
    context: Context,
    private val account: OAuthAccount,
    private val success: Consumer<AuthInfo>,
    private val failed: Runnable,
) : FCLComposeDialog(context, cancelable = false) {

    private val deviceCode = MutableStateFlow<OAuthServer.GrantDeviceCodeEvent?>(null)
    private val deviceCodeSubscription: FlowSubscriptions.Subscription
    private val holder = WeakListenerHolder()
    private var useExternalBrowser = false

    private val loginEnabledState = mutableStateOf(true)
    private val cancelEnabledState = mutableStateOf(true)

    init {
        deviceCodeSubscription = FlowSubscriptions.subscribeWithCurrent(deviceCode) { dc ->
            Schedulers.androidUIThread().execute {
                if (dc != null) {
                    AndroidUtils.copyText(context, dc.userCode)
                }
            }
        }
        setOnDismissListener { deviceCodeSubscription.cancel() }
        holder.add(Accounts.OAUTH_CALLBACK.onGrantDeviceCode.registerWeak { deviceCode.value = it })
        holder.add(Accounts.OAUTH_CALLBACK.onOpenBrowser.registerWeak { event ->
            if (useExternalBrowser) {
                AndroidUtils.openLink(context, event.url)
            } else {
                AndroidUtils.openLinkWithBuiltinWebView(context, event.url)
            }
        })

        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.account_login_refresh),
                summary = stringResource(R.string.account_login_refresh_microsoft_hint),
            ) {
                ButtonsRow()
            }
        }
    }

    /** 登录（左，支持长按）+ 取消（右）按钮区；长按登录 = OAuth 走外部浏览器。 */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ButtonsRow() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_login),
                color = if (loginEnabledState.value) MiuixTheme.colorScheme.primary
                else MiuixTheme.colorScheme.disabledOnSecondary,
                style = MiuixTheme.textStyles.button,
                modifier = Modifier
                    .combinedClickable(
                        enabled = loginEnabledState.value,
                        onClick = { login(external = false) },
                        onLongClick = { login(external = true) },
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            TextButton(
                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                onClick = {
                    failed.run()
                    dismiss()
                },
                enabled = cancelEnabledState.value,
            )
        }
    }

    private fun login(external: Boolean) {
        if (external) {
            useExternalBrowser = true
        }
        loginEnabledState.value = false
        cancelEnabledState.value = false
        Task.supplyAsync(account::logInWhenCredentialsExpired)
            .whenComplete(Schedulers.androidUIThread()) { authInfo, exception ->
                if (exception == null) {
                    success.accept(authInfo)
                    dismiss()
                } else {
                    LOG.log(Level.INFO, "Failed to login when credentials expired: $account", exception)
                    FCLAlertDialog.Builder(context)
                        .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                        .setMessage(Accounts.localizeErrorMessage(context, exception))
                        .setCancelable(false)
                        .setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                        .create()
                        .show()
                }
                loginEnabledState.value = true
                cancelEnabledState.value = true
            }.start()
    }
}
