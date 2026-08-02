package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging.LOG
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.IOException
import java.util.logging.Level
import javax.net.ssl.SSLException

/**
 * Miuix 版添加外置登录（authlib-injector）服务器弹窗（3.2 批 3，对应
 * ui/account/AddAuthlibInjectorServerDialog + dialog_add_authlib_injector_server）。
 *
 * 行为对齐：两步向导（第一步输入 URL，下一步异步 locateServer 解析，期间 next/取消禁用；
 * 成功进第二步展示解析出的 URL 与名称，失败 Toast 分类提示 SSL/IO/其他）；
 * 第二步 prev 返回第一步，确定去重后加入 config().getAuthlibInjectorServers() 并 dismiss；
 * 两个取消按钮均直接 dismiss。setCancelable(false) 一致。
 */
class MiuixAddAuthlibInjectorServerDialog(
    context: Context,
) : FCLComposeDialog(context, cancelable = false) {

    private val stepState = mutableStateOf(1)
    private val urlInputState = mutableStateOf("")
    private val resolvingState = mutableStateOf(false)
    private val serverState = mutableStateOf<AuthlibInjectorServer?>(null)

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.account_add_server),
                buttons = if (stepState.value == 1) {
                    listOf(
                        FCLDialogButton(
                            text = stringResource(R.string.button_next),
                            enabled = !resolvingState.value,
                            onClick = { next() },
                        ),
                        FCLDialogButton(
                            text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                            enabled = !resolvingState.value,
                            onClick = { dismiss() },
                        ),
                    )
                } else {
                    listOf(
                        FCLDialogButton(
                            text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                            onClick = { onPositive() },
                        ),
                        FCLDialogButton(
                            text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                            onClick = { dismiss() },
                        ),
                        FCLDialogButton(
                            text = stringResource(R.string.button_prev),
                            onClick = { stepState.value = 1 },
                        ),
                    )
                },
            ) {
                if (stepState.value == 1) {
                    FirstStep()
                } else {
                    SecondStep()
                }
            }
        }
    }

    @Composable
    private fun FirstStep() {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_add_server_url),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = urlInputState.value,
                onValueChange = { urlInputState.value = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
        }
    }

    @Composable
    private fun SecondStep() {
        val server = serverState.value
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_add_server_url),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = server?.url ?: "",
                style = MiuixTheme.textStyles.body2,
                maxLines = 1,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_add_server_name),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = server?.name ?: "",
                style = MiuixTheme.textStyles.body2,
                maxLines = 1,
            )
        }
    }

    private fun next() {
        resolvingState.value = true
        val url = urlInputState.value
        Task.runAsync {
            serverState.value = AuthlibInjectorServer.locateServer(url)
        }.whenComplete(Schedulers.androidUIThread()) { exception ->
            resolvingState.value = false

            if (exception == null) {
                stepState.value = 2
            } else {
                LOG.log(Level.WARNING, "Failed to resolve auth server: $url", exception)
                Toast.makeText(context, resolveFetchExceptionMessage(exception), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun resolveFetchExceptionMessage(exception: Throwable): String {
        return when (exception) {
            is SSLException -> context.getString(R.string.account_failed_ssl)
            is IOException -> context.getString(R.string.account_failed_connect_injector_server)
            else -> exception.javaClass.name + ": " + exception.localizedMessage
        }
    }

    private fun onPositive() {
        val server = serverState.value ?: return
        if (!ConfigHolder.config().authlibInjectorServers.contains(server)) {
            ConfigHolder.config().authlibInjectorServers.add(server)
        }
        dismiss()
    }
}
