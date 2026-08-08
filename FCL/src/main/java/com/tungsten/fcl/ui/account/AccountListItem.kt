package com.tungsten.fcl.ui.account

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.compose.dialog.MiuixOAuthAccountLoginDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixOfflineAccountSkinDialog
import com.tungsten.fcl.ui.main.compose.ComposeMainUI
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AuthInfo
import com.tungsten.fclcore.auth.AuthenticationException
import com.tungsten.fclcore.auth.ClassicAccount
import com.tungsten.fclcore.auth.CredentialExpiredException
import com.tungsten.fclcore.auth.OAuthAccount
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount
import com.tungsten.fclcore.auth.offline.OfflineAccount
import com.tungsten.fclcore.auth.yggdrasil.CompleteGameProfile
import com.tungsten.fclcore.auth.yggdrasil.TextureType
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilAccount
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fclcore.util.skin.InvalidSkinException
import com.tungsten.fclcore.util.skin.NormalizedSkin
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.Optional
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Level

/**
 * 账户条目（merge 版）：承接上游 d7546cd1 的 Kotlin 化与文件选择 null 安全，
 * 状态层保持本分支的 StateFlow 契约（fakefx 已彻底移除）：
 * - 标题/副标题订阅 account/server 的 revisionFlow；头像订阅 TexturesLoader 的
 *   avatarFlow（4c API）；
 * - uploadSkin 文件选择改用 fileLauncher 回调（替代遗留 browse + CountDownLatch），
 *   取消选择回调 null 时 future 以 null 完成；对外签名仍为 CompletableFuture，
 *   Compose 调用方（AccountScreen）契约不变。
 */
class AccountListItem(
    private val context: Context,
    val account: Account
) {
    private val _title = MutableStateFlow("")
    private val _subtitle = MutableStateFlow("")
    private val _image = MutableStateFlow<Drawable?>(null)
    private var imageSubscription: FlowSubscriptions.Subscription? = null

    val title: String
        get() = _title.value

    init {
        val loginTypeName =
            Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account))
        if (account is AuthlibInjectorAccount) {
            val server = account.server
            FlowSubscriptions.subscribeWithCurrent(server.revisionFlow()) {
                _subtitle.value =
                    loginTypeName + ", " + context.getString(R.string.account_injector_server) + ": " + server.name
            }
        } else {
            _subtitle.value = loginTypeName
        }

        if (account is OfflineAccount || account.username.isEmpty()) {
            FlowSubscriptions.subscribeWithCurrent(account.revisionFlow()) {
                _title.value = account.character
            }
        } else {
            val prefix = account.username + " - "
            FlowSubscriptions.subscribeWithCurrent(account.revisionFlow()) {
                _title.value = prefix + account.character
            }
        }

        bindSkinFlows()
    }

    private fun bindSkinFlows() {
        imageSubscription = FlowSubscriptions.subscribeWithCurrent(
            TexturesLoader.avatarFlow(account, ConvertUtils.dip2px(context, 30f))
        ) { _image.value = it }
    }

    /**
     * 纹理快照（对齐上游微软皮肤管理弹窗对 texture binding 的用法）：
     * 当前账户皮肤/披风位图数组（[0]=皮肤，[1]=披风，可能为 null）。
     */
    fun textureSnapshot(): Array<out Bitmap?>? {
        return TexturesLoader.textureFlow(account).value
    }

    fun refreshAsync(): Task<*> {
        return Task.runAsync {
            account.clearCache()
            try {
                account.logIn()
            } catch (_: CredentialExpiredException) {
                try {
                    logIn(account)
                } catch (_: CancellationException) {
                    // ignore cancellation
                } catch (e1: Exception) {
                    LOG.log(Level.WARNING, "Failed to refresh $account with password", e1)
                    throw e1
                }
            } catch (e: AuthenticationException) {
                LOG.log(Level.WARNING, "Failed to refresh $account with token", e)
                throw e
            }
        }
    }

    fun canUploadSkin(): StateFlow<Boolean> {
        if (account is YggdrasilAccount) {
            if (account is AuthlibInjectorAccount) {
                val profile: StateFlow<Optional<CompleteGameProfile>> =
                    account.yggdrasilService.profileRepository.bindingFlow(account.uuid)
                val result = MutableStateFlow(canUploadSkin(profile.value))
                FlowSubscriptions.subscribe(profile) { result.value = canUploadSkin(it) }
                return result
            }
            return MutableStateFlow(true)
        }
        if (account is OfflineAccount || account is MicrosoftAccount) {
            return MutableStateFlow(true)
        }
        return MutableStateFlow(false)
    }

    private fun canUploadSkin(profile: Optional<CompleteGameProfile>): Boolean {
        val uploadableTextures = profile
            .map(AuthlibInjectorAccount::getUploadableTextures)
            .orElse(emptySet())
        return uploadableTextures.contains(TextureType.SKIN)
    }

    /**
     * @return future of the skin upload task, completes with null if no file is selected
     */
    fun uploadSkin(): CompletableFuture<Task<*>?> {
        val future = CompletableFuture<Task<*>?>()
        when (account) {
            is OfflineAccount -> {
                // GL 预览 AndroidView 保留原生渲染
                MiuixOfflineAccountSkinDialog(context, this).show()
                future.complete(null)
            }

            is MicrosoftAccount -> {
                AndroidUtils.openLink(
                    context,
                    "https://www.minecraft.net/msaprofile/mygames/editskin"
                )
                future.complete(null)
            }

            !is YggdrasilAccount -> future.complete(null)

            else -> {
                // when 条件分支已 smart-cast，无需显式 as
                val yggAccount = account
                // 上游 d7546cd1：fileLauncher 回调式文件选择（移除遗留 browse + CountDownLatch
                // 阻塞），取消选择回调 null 直接以 null 完成
                Schedulers.androidUIThread().execute {
                    MainActivity.getInstance().fileLauncher.launchSingleSelection(
                        null,
                        listOf(".png")
                    ) { files ->
                        val selectedFile = files?.get(0)
                        if (selectedFile == null) {
                            future.complete(null)
                            return@launchSingleSelection
                        }
                        future.complete(
                            refreshAsync()
                                .thenRunAsync<Exception> {
                                    val skinImg: Bitmap = BitmapFactory.decodeFile(selectedFile)
                                        ?: throw InvalidSkinException("Failed to read skin image")
                                    val skin = NormalizedSkin(skinImg)
                                    val model = if (skin.isSlim) "slim" else ""
                                    LOG.info("Uploading skin [$selectedFile], model [$model]")
                                    yggAccount.uploadSkin(model, File(selectedFile).toPath())
                                }
                                .thenComposeAsync(refreshAsync())
                                .whenComplete(Schedulers.androidUIThread()) { e: Exception? ->
                                    if (e != null) {
                                        val builder = FCLAlertDialog.Builder(context)
                                        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                                        builder.setMessage(Accounts.localizeErrorMessage(context, e))
                                        builder.setNegativeButton(
                                            context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                                            null
                                        )
                                        builder.create().show()
                                    }
                                }
                        )
                    }
                }
            }
        }
        return future
    }

    fun refreshSkinBinding() {
        // 重绑语义保留：cancel 旧订阅 + 新订阅（对齐原 unbind/bind）
        imageSubscription?.cancel()
        bindSkinFlows()
        MainActivity.getInstance().refreshAvatar(account)
        // mainUI 固定为 ComposeMainUI（旧 MainUI 已删除），refreshSkin 契约不变
        (UIManager.instance.mainUI as? ComposeMainUI)?.refreshSkin(account)
    }

    fun remove() {
        Accounts.removeAccount(account)
    }

    fun titleFlow(): MutableStateFlow<String> = _title

    fun subtitleFlow(): MutableStateFlow<String> = _subtitle

    fun imageFlow(): MutableStateFlow<Drawable?> = _image

    companion object {
        @Throws(
            CancellationException::class,
            AuthenticationException::class,
            InterruptedException::class
        )
        @JvmStatic
        fun logIn(account: Account): AuthInfo {
            if (account is ClassicAccount) {
                val latch = CountDownLatch(1)
                val res = AtomicReference<AuthInfo?>(null)
                Schedulers.androidUIThread().execute {
                    val dialog = ClassicAccountLoginDialog(
                        FCLPath.CONTEXT, account,
                        { authInfo ->
                            res.set(authInfo)
                            latch.countDown()
                        },
                        { latch.countDown() }
                    )
                    dialog.show()
                }
                latch.await()
                return Optional.ofNullable(res.get()).orElseThrow { CancellationException() }
            } else if (account is OAuthAccount) {
                val latch = CountDownLatch(1)
                val res = AtomicReference<AuthInfo?>(null)
                Schedulers.androidUIThread().execute {
                    // success/failed 回调契约不变，latch 阻塞语义由本方法持有，与弹窗实现解耦
                    MiuixOAuthAccountLoginDialog(
                        FCLPath.CONTEXT, account,
                        { authInfo ->
                            res.set(authInfo)
                            latch.countDown()
                        },
                        { latch.countDown() }
                    ).show()
                }
                latch.await()
                return Optional.ofNullable(res.get()).orElseThrow { CancellationException() }
            }
            return account.logIn()
        }
    }
}
