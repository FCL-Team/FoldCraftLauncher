package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.OAuthServer
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fclcore.auth.CharacterSelector
import com.tungsten.fclcore.auth.NoSelectedCharacterException
import com.tungsten.fclcore.auth.OAuth
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccountFactory
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.authlibinjector.BoundAuthlibInjectorAccountFactory
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccountFactory
import com.tungsten.fclcore.auth.offline.OfflineAccountFactory
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.concurrent.CancellationException
import java.util.regex.Pattern
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Miuix 版创建账户弹窗（3.2 批 3，对应 ui/account/CreateAccountDialog + dialog_create_account
 * + view_create_account_offline/microsoft/external）。
 *
 * 行为对齐：
 * - 三 Tab（离线/微软/外置）切换登录方式并写入 preferredLoginType，详情区随 factory 切换；
 *   无 method switcher 时按 factory 类型定标题（离线/微软/外置）；
 * - 登录按钮：表单校验失败 Toast（account_create_alert / server_not_select）；
 *   离线用户名非法先弹 FCLAlertDialog 确认（遗留嵌套弹窗沿用，dialog_alert 不在本批）；
 *   **长按登录 = 微软 OAuth 走外部浏览器**（useExternalBrowser）；
 * - 取消按钮：登录任务进行中置 OAuth.IS_CANCELED 并 cancel，再 dismiss；微软登录中取消键保持可用
 *   （对齐遗留 `if (!(factory instanceof MicrosoftAccountFactory)) cancel.setEnabled(false)`）；
 * - 设备码事件总线：onGrantDeviceCode 自动复制用户码、onOpenBrowser 按 useExternalBrowser
 *   打开外部浏览器/内置 WebView（WeakListenerHolder 同款）；
 * - 异步创建成功 → addAccount + setSelectedAccount + AccountUI.refresh + dismiss；
 *   NoSelectedCharacterException/CancellationException → dismiss；其余失败 → FCLAlertDialog；
 * - 角色选择走 MiuixCharacterSelectorDialog（阻塞式 CountDownLatch 契约不变）。
 *
 * 有意偏差（附录 D 风格登记）：遗留 TabLayout 首个 Tab 在监听器注册前已被静默选中，
 * 当 preferred 方式非离线时遗留呈现"高亮 Tab=离线、内容=preferred"的不一致；
 * Miuix 版 TabRow 初始索引与实际显示的 factory 详情保持一致（功能等价，内容不变）。
 * setCancelable(false) 一致。
 */
class MiuixCreateAccountDialog : FCLComposeDialog {

    companion object {
        private val USERNAME_CHECKER_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$")
    }

    private var factory: AccountFactory<*>
    private val showMethodSwitcher: Boolean

    private val selectedTabState = mutableIntStateOf(0)
    private val detailsTypeState = mutableStateOf(DetailsType.OFFLINE)
    private val usernameState = mutableStateOf("")
    private val passwordState = mutableStateOf("")
    private val serverState = mutableStateOf<AuthlibInjectorServer?>(null)
    private val loginEnabledState = mutableStateOf(true)
    private val cancelEnabledState = mutableStateOf(true)

    private var loginTask: TaskExecutor? = null
    private var useExternalBrowser = false
    private val deviceCode = MutableStateFlow<OAuthServer.GrantDeviceCodeEvent?>(null)
    private val deviceCodeSubscription: FlowSubscriptions.Subscription
    private val holder = WeakListenerHolder()

    private enum class DetailsType { OFFLINE, MICROSOFT, EXTERNAL }

    constructor(context: Context, factory: AccountFactory<*>?) : super(context, cancelable = false) {
        showMethodSwitcher = factory == null
        val resolved: AccountFactory<*> = factory ?: try {
            Accounts.getAccountFactory(ConfigHolder.config().preferredLoginType)
        } catch (e: IllegalArgumentException) {
            Accounts.FACTORY_OFFLINE
        }
        this.factory = resolved

        holder.add(Accounts.OAUTH_CALLBACK.onGrantDeviceCode.registerWeak { value -> deviceCode.value = value })
        deviceCodeSubscription = FlowSubscriptions.subscribeWithCurrent(deviceCode) { dc ->
            Handler(Looper.getMainLooper()).post {
                if (dc != null) {
                    AndroidUtils.copyText(context, dc.userCode)
                }
            }
        }
        holder.add(Accounts.OAUTH_CALLBACK.onOpenBrowser.registerWeak { event ->
            if (useExternalBrowser) {
                AndroidUtils.openLink(context, event.url)
            } else {
                AndroidUtils.openLinkWithBuiltinWebView(context, event.url)
            }
        })

        serverState.value = when (resolved) {
            is BoundAuthlibInjectorAccountFactory -> resolved.server
            is AuthlibInjectorAccountFactory ->
                if (ConfigHolder.config().authlibInjectorServers.size == 0) null
                else ConfigHolder.config().authlibInjectorServers[0]
            else -> null
        }
        detailsTypeState.value = detailsTypeOf(resolved)
        selectedTabState.intValue = when (resolved) {
            is MicrosoftAccountFactory -> 1
            is AuthlibInjectorAccountFactory -> 2
            else -> 0
        }

        setDialogContent {
            DialogContent()
        }
    }

    constructor(context: Context, authServer: AuthlibInjectorServer) :
            this(context, Accounts.getAccountFactoryByAuthlibInjectorServer(authServer))

    private fun detailsTypeOf(factory: AccountFactory<*>): DetailsType = when (factory) {
        is MicrosoftAccountFactory -> DetailsType.MICROSOFT
        is AuthlibInjectorAccountFactory -> DetailsType.EXTERNAL
        // 绑定服务器的入口（服务器列表"添加账户"）：必须走 EXTERNAL 表单，
        // 否则按 OFFLINE 分支登录会把 password=null 传给 Bound 工厂（NPE）
        is BoundAuthlibInjectorAccountFactory -> DetailsType.EXTERNAL
        else -> DetailsType.OFFLINE
    }

    private fun titleRes(): Int {
        if (showMethodSwitcher) return R.string.account_create
        return when (factory) {
            is OfflineAccountFactory -> R.string.account_create_offline
            is MicrosoftAccountFactory -> R.string.account_create_microsoft
            else -> R.string.account_create_external
        }
    }

    @Composable
    private fun DialogContent() {
        FCLDialogCard(
            title = stringResource(titleRes()),
            scrollable = false,
        ) {
            if (showMethodSwitcher) {
                TabRow(
                    tabs = listOf(
                        stringResource(R.string.account_methods_offline),
                        stringResource(R.string.account_methods_microsoft),
                        stringResource(R.string.account_methods_authlib_injector),
                    ),
                    selectedTabIndex = selectedTabState.intValue,
                    onTabSelected = { onTabSelected(it) },
                )
                Spacer(Modifier.height(8.dp))
            }
            when (detailsTypeState.value) {
                DetailsType.OFFLINE -> OfflineDetails()
                DetailsType.MICROSOFT -> MicrosoftDetails()
                DetailsType.EXTERNAL -> ExternalDetails()
            }
            Spacer(Modifier.height(12.dp))
            ButtonsRow()
        }
    }

    @Composable
    private fun OfflineDetails() {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_create_username),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
        }
    }

    @Composable
    private fun MicrosoftDetails() {
        Text(
            text = stringResource(R.string.account_methods_microsoft_hint),
            style = MiuixTheme.textStyles.footnote1,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        )
    }

    @Composable
    private fun ExternalDetails() {
        val server = serverState.value
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_create_server),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = server?.name ?: stringResource(R.string.account_create_server_not_select),
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            val homeLink = server?.links?.get("homepage")
            if (homeLink != null) {
                IconButton(onClick = { AndroidUtils.openLink(context, homeLink) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_home_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
            val registerLink = server?.links?.get("register")
            if (registerLink != null) {
                IconButton(onClick = { AndroidUtils.openLink(context, registerLink) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_person_add_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_create_username),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = usernameState.value,
                onValueChange = { usernameState.value = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_create_password),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )
        }
    }

    /** 登录（左，支持长按）+ 取消（右）按钮区；长按登录 = 微软 OAuth 外部浏览器。 */
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
                        onLongClick = {
                            if (detailsTypeState.value == DetailsType.MICROSOFT) {
                                login(external = true)
                            }
                        },
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            TextButton(
                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                onClick = { onCancel() },
                enabled = cancelEnabledState.value,
            )
        }
    }

    private fun onTabSelected(position: Int) {
        selectedTabState.intValue = position
        val newMethod: AccountFactory<*> = when (position) {
            1 -> Accounts.FACTORY_MICROSOFT
            2 -> Accounts.FACTORY_AUTHLIB_INJECTOR
            else -> Accounts.FACTORY_OFFLINE
        }
        ConfigHolder.config().preferredLoginType = Accounts.getLoginType(newMethod)
        factory = newMethod
        detailsTypeState.value = detailsTypeOf(newMethod)
        if (newMethod is AuthlibInjectorAccountFactory) {
            serverState.value = if (ConfigHolder.config().authlibInjectorServers.size == 0) null
            else ConfigHolder.config().authlibInjectorServers[0]
        }
    }

    private fun login(external: Boolean) {
        if (external) {
            useExternalBrowser = true
        }
        loginEnabledState.value = false
        if (factory !is MicrosoftAccountFactory) {
            cancelEnabledState.value = false
        }

        val username: String?
        val password: String?
        val additionalData: Any?
        try {
            when (detailsTypeState.value) {
                DetailsType.OFFLINE -> {
                    if (StringUtils.isBlank(usernameState.value)) {
                        throw IllegalStateException(context.getString(R.string.account_create_alert))
                    }
                    username = usernameState.value
                    password = null
                    additionalData = null
                }

                DetailsType.MICROSOFT -> {
                    username = null
                    password = null
                    additionalData = null
                }

                DetailsType.EXTERNAL -> {
                    if (StringUtils.isBlank(usernameState.value) || StringUtils.isBlank(passwordState.value)) {
                        throw IllegalStateException(context.getString(R.string.account_create_alert))
                    }
                    if (serverState.value == null) {
                        throw IllegalStateException(context.getString(R.string.account_create_server_not_select))
                    }
                    username = usernameState.value
                    password = passwordState.value
                    additionalData = serverState.value
                }
            }
        } catch (e: IllegalStateException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            loginEnabledState.value = true
            cancelEnabledState.value = true
            return
        }

        val doCreate = Runnable {
            deviceCode.value = null

            val selector: CharacterSelector = MiuixCharacterSelectorDialog(context)
            loginTask = Task.supplyAsync { factory.create(selector, username, password, null, additionalData) }
                .whenComplete(Schedulers.androidUIThread()) { account, exception ->
                    if (exception == null) {
                        Accounts.addAccount(account)
                        // select the new account
                        Accounts.setSelectedAccount(account)

                        loginEnabledState.value = true
                        cancelEnabledState.value = true
                        UIManager.instance.accountUI.refresh().start()
                        dismiss()
                    } else {
                        if (exception is NoSelectedCharacterException || exception is CancellationException) {
                            dismiss()
                        } else {
                            FCLAlertDialog.Builder(context)
                                .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                                .setMessage(Accounts.localizeErrorMessage(context, exception))
                                .setCancelable(false)
                                .setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                                .useAutoLink()
                                .create()
                                .show()
                        }
                        loginEnabledState.value = true
                        cancelEnabledState.value = true
                    }
                }.executor(true)
        }

        if (factory is OfflineAccountFactory && username != null &&
            !USERNAME_CHECKER_PATTERN.matcher(username).matches()
        ) {
            FCLAlertDialog.Builder(context)
                .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                .setTitle(context.getString(R.string.message_warning))
                .setMessage(context.getString(R.string.account_methods_offline_name_invalid))
                .setCancelable(false)
                .setPositiveButton { doCreate.run() }
                .setNegativeButton {
                    loginEnabledState.value = true
                    cancelEnabledState.value = true
                }
                .create().show()
        } else {
            doCreate.run()
        }
    }

    override fun dismiss() {
        deviceCodeSubscription.cancel()
        super.dismiss()
    }

    private fun onCancel() {
        loginTask?.let {
            OAuth.IS_CANCELED = true
            it.cancel()
        }
        dismiss()
    }
}
