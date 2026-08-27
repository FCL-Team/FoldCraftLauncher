package com.tungsten.fcl.ui.account

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast

import com.mio.util.LoginStageTextBinder
import com.mio.util.copyText
import com.mio.util.openLink
import com.mio.util.openLinkWithBuiltinWebView
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogCharacterSelectorBinding
import com.tungsten.fcl.databinding.DialogCreateAccountBinding
import com.tungsten.fcl.databinding.ItemCharacterBinding
import com.tungsten.fcl.databinding.ViewCreateAccountExternalBinding
import com.tungsten.fcl.databinding.ViewCreateAccountMicrosoftBinding
import com.tungsten.fcl.databinding.ViewCreateAccountOfflineBinding
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder.config
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fclcore.auth.CharacterSelector
import com.tungsten.fclcore.auth.NoSelectedCharacterException
import com.tungsten.fclcore.auth.OAuth
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccountFactory
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.authlibinjector.BoundAuthlibInjectorAccountFactory
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccountFactory
import com.tungsten.fclcore.auth.offline.OfflineAccountFactory
import com.tungsten.fclcore.auth.yggdrasil.GameProfile
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilService
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.FCLAdapter
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.concurrent.CancellationException
import java.util.concurrent.CountDownLatch
import java.util.regex.Pattern

/**
 * 创建账户对话框（离线 / 微软 / authlib-injector 外置登录）。
 * 登录方式由外部传入的工厂决定，弹窗内不提供切换功能；
 * 多角色账户登录时经 [DialogCharacterSelector] 阻塞式选择角色。
 */
class CreateAccountDialog : FCLDialog, View.OnClickListener {

    private val binding: DialogCreateAccountBinding =
        DialogCreateAccountBinding.inflate(layoutInflater)
    private val factory: AccountFactory<*>
    private var loginTask: TaskExecutor? = null
    private lateinit var details: Details

    constructor(context: Context, factory: AccountFactory<*>) : super(context) {
        this.factory = factory

        // ViewBinding 分离式 inflate 不产生根节点 LayoutParams，手动补回 XML 中的尺寸，
        // 交由内容视图驱动窗口（与原先 setContentView(R.layout.xxx) 的行为一致）
        binding.root.layoutParams = FrameLayout.LayoutParams(
            ConvertUtils.dip2px(context, 400f),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(binding.root)
        setCancelable(false)

        binding.title.setText(
            when (factory) {
                is OfflineAccountFactory -> R.string.account_create_offline
                is MicrosoftAccountFactory -> R.string.account_create_microsoft
                else -> R.string.account_create_external
            }
        )
        binding.login.setOnClickListener(this)
        binding.cancel.setOnClickListener(this)
        // 微软登录支持长按按钮改用外部浏览器完成认证
        binding.login.setOnLongClickListener {
            (details as? MicrosoftDetails)?.let { it.useExternalBrowser = true; login() }
            true
        }
        initDetails()
    }

    constructor(context: Context, authServer: AuthlibInjectorServer) : this(
        context,
        Accounts.getAccountFactoryByAuthlibInjectorServer(authServer)
    )

    private fun initDetails() {
        details = when (factory) {
            is BoundAuthlibInjectorAccountFactory -> ExternalDetails(context, factory.server)
            is AuthlibInjectorAccountFactory -> ExternalDetails(context)
            is MicrosoftAccountFactory -> MicrosoftDetails(context)
            else -> OfflineDetails(context)
        }
        binding.detailContainer.removeAllViews()
        binding.detailContainer.addView(
            details.view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun login() {
        binding.login.isEnabled = false
        if (factory !is MicrosoftAccountFactory) {
            binding.cancel.isEnabled = false
        }
        // 微软登录期间实时显示各阶段进度；其余类型无阶段可报
        val microsoft = details as? MicrosoftDetails
        microsoft?.showProgress(context.getString(R.string.launch_state_logging_in))

        val username: String?
        val password: String?
        val additionalData: Any?
        try {
            username = details.username
            password = details.password
            additionalData = details.additionalData
        } catch (e: IllegalStateException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            binding.login.isEnabled = true
            binding.cancel.isEnabled = true
            return
        }

        val doCreate = Runnable {
            // Dialog 必须在主线程构造（依赖 Looper），后台任务只引用已建好的选择器
            val selector = DialogCharacterSelector(context)
            loginTask = Task.supplyAsync {
                // factory 为通配符类型，create 结果需显式收窄
                @Suppress("UNCHECKED_CAST")
                val account =
                    factory.create(selector, username, password, microsoft?.progressCallback, additionalData) as Account
                account
            }.whenComplete(Schedulers.androidUIThread()) { account, exception ->
                binding.login.isEnabled = true
                binding.cancel.isEnabled = true
                microsoft?.hideProgress()
                if (exception == null) {
                    Accounts.addAccount(account)
                    Accounts.setSelectedAccount(account)
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
                            .setNegativeButton(context.getString(R.string.dialog_positive), null)
                            .useAutoLink()
                            .create()
                            .show()
                    }
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
                    binding.login.isEnabled = true
                    binding.cancel.isEnabled = true
                }
                .create().show()
        } else {
            doCreate.run()
        }
    }

    private fun onCancel() {
        loginTask?.let {
            OAuth.IS_CANCELED = true
            it.cancel()
        }
        dismiss()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.login -> login()
            binding.cancel -> onCancel()
        }
    }

    companion object {
        private val USERNAME_CHECKER_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$")
    }
}

/**
 * 详情面板契约：必填项缺失时 getter 抛出 [IllegalStateException]，由对话框统一提示。
 */
private interface Details {
    val view: View
    val username: String?
    val password: String?
    val additionalData: Any?
}

/** 离线账户登录面板，仅需填写用户名。 */
private class OfflineDetails(private val context: Context) : Details {

    private val binding = ViewCreateAccountOfflineBinding.inflate(LayoutInflater.from(context))

    override val view: View get() = binding.root

    override val username: String
        get() {
            val name = binding.username.text.toString()
            if (StringUtils.isBlank(name)) {
                throw IllegalStateException(context.getString(R.string.account_create_alert))
            }
            return name
        }

    override val password: String? get() = null

    override val additionalData: Any? get() = null
}

/**
 * 微软账户登录面板：无输入项，设备码登录时自动复制授权码，
 * 认证浏览器由 OAuth 回调事件打开；内嵌进度行实时显示登录阶段。
 */
private class MicrosoftDetails(private val context: Context) : Details {

    private val binding = ViewCreateAccountMicrosoftBinding.inflate(LayoutInflater.from(context))
    private val holder = WeakListenerHolder()
    private val handler = Handler(Looper.getMainLooper())

    /** 登录进度回调：后台线程的阶段通知经主线程调度刷新到内嵌进度行 */
    val progressCallback: LoginStageTextBinder =
        LoginStageTextBinder(context, binding.loginProgress.progressText)

    var useExternalBrowser = false

    init {
        // 设备码请求发出后立即复制到剪贴板，方便用户在浏览器中粘贴
        holder.add(Accounts.OAUTH_CALLBACK.onGrantDeviceCode.registerWeak { event ->
            event?.let {
                handler.post {
                    copyText(context, it.userCode)
                    showProgress(context.getString(R.string.login_state_microsoft_wait_browser))
                }
            }
        })
        holder.add(Accounts.OAUTH_CALLBACK.onOpenBrowser.registerWeak { event ->
            if (useExternalBrowser) {
                openLink(context, event.url)
            } else {
                openLinkWithBuiltinWebView(context, event.url)
            }
        })
    }

    fun showProgress(text: String) {
        binding.loginProgress.root.visibility = View.VISIBLE
        binding.loginProgress.progressText.text = text
    }

    fun hideProgress() {
        binding.loginProgress.root.visibility = View.GONE
    }

    override val view: View get() = binding.root

    override val username: String? get() = null

    override val password: String? get() = null

    override val additionalData: Any? get() = null
}

/** authlib-injector 外置登录面板：服务器信息展示 + 用户名密码。 */
private class ExternalDetails(
    private val context: Context,
    /** 登录的外置验证服务器；未指定时默认取服务器列表第一个 */
    private val server: AuthlibInjectorServer? = config().authlibInjectorServers.orEmpty()
        .firstOrNull()
) : Details {

    private val binding = ViewCreateAccountExternalBinding.inflate(LayoutInflater.from(context))

    init {
        refreshAuthenticateServer(server)
    }

    override val view: View get() = binding.root

    override val username: String
        get() {
            val name = binding.username.text.toString()
            if (StringUtils.isBlank(name)) {
                throw IllegalStateException(context.getString(R.string.account_create_alert))
            }
            return name
        }

    override val password: String
        get() {
            val password = binding.password.text.toString()
            if (StringUtils.isBlank(password)) {
                throw IllegalStateException(context.getString(R.string.account_create_alert))
            }
            return password
        }

    override val additionalData: Any
        get() = server
            ?: throw IllegalStateException(context.getString(R.string.account_create_server_not_select))

    private fun refreshAuthenticateServer(authlibInjectorServer: AuthlibInjectorServer?) {
        if (authlibInjectorServer == null) {
            binding.serverName.setText(R.string.account_create_server_not_select)
            binding.home.visibility = View.GONE
            binding.register.visibility = View.GONE
        } else {
            binding.serverName.text = authlibInjectorServer.name
            val links = authlibInjectorServer.links
            setupLinkButton(binding.home, links["homepage"])
            setupLinkButton(binding.register, links["register"])
        }
    }

    private fun setupLinkButton(button: FCLImageButton, url: String?) {
        if (url == null) {
            button.visibility = View.GONE
        } else {
            button.visibility = View.VISIBLE
            button.setOnClickListener { openLink(context, url) }
        }
    }
}

/**
 * 多角色账户登录时的角色选择对话框，select() 阻塞调用线程（后台登录线程）直至用户选择或取消。
 */
private class DialogCharacterSelector(context: Context) :
    FCLDialog(context), CharacterSelector, View.OnClickListener {

    private val binding = DialogCharacterSelectorBinding.inflate(layoutInflater)
    private val handler = Handler(Looper.getMainLooper())

    private val latch = CountDownLatch(1)
    private var selectedProfile: GameProfile? = null

    init {
        // ViewBinding 分离式 inflate 不产生根节点 LayoutParams，手动补回 XML 中的尺寸，
        // 交由内容视图驱动窗口（与原先 setContentView(R.layout.xxx) 的行为一致）
        binding.root.layoutParams = FrameLayout.LayoutParams(
            ConvertUtils.dip2px(context, 300f),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(binding.root)
        setCancelable(false)
        binding.negative.setOnClickListener(this)
    }

    @Throws(NoSelectedCharacterException::class)
    override fun select(service: YggdrasilService, profiles: List<GameProfile>): GameProfile {
        handler.post {
            binding.list.adapter = Adapter(context, service, profiles) { profile ->
                selectedProfile = profile
                latch.countDown()
            }
            show()
        }

        try {
            latch.await()
            return selectedProfile ?: throw NoSelectedCharacterException()
        } catch (_: InterruptedException) {
            throw NoSelectedCharacterException()
        } finally {
            dismiss()
        }
    }

    override fun onClick(view: View) {
        if (view === binding.negative) {
            latch.countDown()
            dismiss()
        }
    }

    private class Adapter(
        context: Context,
        private val service: YggdrasilService,
        private val profiles: List<GameProfile>,
        private val listener: (GameProfile) -> Unit
    ) : FCLAdapter(context) {

        private class ViewHolder(val binding: ItemCharacterBinding)

        override fun getCount(): Int = profiles.size

        override fun getItem(position: Int): Any = profiles[position]

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val holder: ViewHolder
            val view: View
            if (convertView == null) {
                val itemBinding = ItemCharacterBinding.inflate(LayoutInflater.from(context))
                view = itemBinding.root
                holder = ViewHolder(itemBinding)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            val profile = profiles[position]
            holder.binding.name.text = profile.name
            holder.binding.avatar.imageProperty().bind(
                TexturesLoader.avatarBinding(service, profile.id, ConvertUtils.dip2px(context, 30f))
            )
            holder.binding.parent.setOnClickListener { listener(profile) }
            return view
        }
    }
}
