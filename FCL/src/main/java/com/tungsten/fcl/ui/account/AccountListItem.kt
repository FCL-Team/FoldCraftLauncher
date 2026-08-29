package com.tungsten.fcl.ui.account

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.UIManager
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
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.binding.Bindings
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding
import com.tungsten.fclcore.fakefx.beans.binding.StringBinding
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty
import com.tungsten.fclcore.fakefx.beans.property.StringProperty
import com.tungsten.fclcore.fakefx.beans.value.ObservableBooleanValue
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.skin.InvalidSkinException
import com.tungsten.fclcore.util.skin.NormalizedSkin
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Optional
import java.util.concurrent.CancellationException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Level
import kotlin.coroutines.resume

class AccountListItem(
    private val context: Context,
    val account: Account
) {
    val title: StringProperty = SimpleStringProperty()
    val subtitle: StringProperty = SimpleStringProperty()
    val image: ObjectProperty<Drawable> = SimpleObjectProperty()
    val texture: ObjectProperty<Array<Bitmap>> = SimpleObjectProperty()

    init {
        val loginTypeName =
            Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account))
        if (account is AuthlibInjectorAccount) {
            val server = account.server
            subtitle.bind(
                Bindings.concat(
                    loginTypeName, ", ", context.getString(R.string.account_injector_server), ": ",
                    Bindings.createStringBinding({ server.name }, server)
                )
            )
        } else {
            subtitle.set(loginTypeName)
        }

        val characterName: StringBinding =
            Bindings.createStringBinding({ account.character }, account)
        if (account is OfflineAccount) {
            title.bind(characterName)
        } else {
            title.bind(
                if (account.username.isEmpty()) characterName
                else Bindings.concat(account.username, " - ", characterName)
            )
        }

        image.bind(TexturesLoader.avatarBinding(account, ConvertUtils.dip2px(context, 30f)))
        texture.bind(TexturesLoader.textureBinding(account))
    }

    fun refreshAsync(): Task<*> {
        return Task.runAsync {
            runBlocking { refresh() }
        }
    }

    private suspend fun refresh() = withContext(Dispatchers.IO) {
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

    fun canUploadSkin(): ObservableBooleanValue {
        if (account is YggdrasilAccount) {
            if (account is AuthlibInjectorAccount) {
                val profile: ObjectBinding<Optional<CompleteGameProfile>> =
                    account.yggdrasilService.profileRepository.binding(account.uuid)
                return Bindings.createBooleanBinding({
                    val uploadableTextures = profile.get()
                        .map { AuthlibInjectorAccount.getUploadableTextures(it) }
                        .orElse(emptySet())
                    uploadableTextures.contains(TextureType.SKIN)
                }, profile)
            } else {
                return Bindings.createBooleanBinding({ true })
            }
        } else if (account is OfflineAccount || account is MicrosoftAccount) {
            return Bindings.createBooleanBinding({ true })
        } else {
            return Bindings.createBooleanBinding({ false })
        }
    }

    /**
     * 上传皮肤。确认选择皮肤文件后通过 [onUploading]（主线程）通知调用方显示进度，
     * 整个上传流程结束（成功或失败）后返回。
     */
    suspend fun uploadSkin(onUploading: () -> Unit) {
        when (account) {
            is OfflineAccount -> {
                withContext(Dispatchers.Main) {
                    OfflineAccountSkinDialog(context, this@AccountListItem).show()
                }
            }

            is MicrosoftAccount -> {
                withContext(Dispatchers.Main) {
                    MicrosoftAccountSkinDialog(context, this@AccountListItem).show()
                }
            }

            !is YggdrasilAccount -> Unit
            else -> {
                val selectedFile = withContext(Dispatchers.Main) { selectSkinFile() } ?: return
                try {
                    withContext(Dispatchers.Main) { onUploading() }
                    refresh()
                    withContext(Dispatchers.IO) {
                        val skinImg: Bitmap = BitmapFactory.decodeFile(selectedFile)
                            ?: throw InvalidSkinException("Failed to read skin image")
                        val skin = NormalizedSkin(skinImg)
                        val model = if (skin.isSlim) "slim" else ""
                        LOG.info("Uploading skin [$selectedFile], model [$model]")
                        account.uploadSkin(model, File(selectedFile).toPath())
                    }
                    refresh()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        val builder1 = FCLAlertDialog.Builder(context)
                        builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                        builder1.setMessage(Accounts.localizeErrorMessage(context, e))
                        builder1.setNegativeButton(
                            context.getString(com.tungsten.fcl.R.string.dialog_positive),
                            null
                        )
                        builder1.create().show()
                    }
                }
            }
        }
    }

    private suspend fun selectSkinFile(): String? = suspendCancellableCoroutine { cont ->
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, listOf(".png")) {
            cont.resume(it?.get(0)?.toFile(context, File(FCLPath.CACHE_DIR))?.absolutePath)
        }
    }

    fun refreshSkinBinding() {
        image.unbind()
        texture.unbind()
        image.bind(TexturesLoader.avatarBinding(account, ConvertUtils.dip2px(context, 30f)))
        texture.bind(TexturesLoader.textureBinding(account))
        MainActivity.getInstance().refreshAvatar(account)
        UIManager.instance.mainUI.refreshSkin(account)
    }

    fun remove() {
        if (account is OfflineAccount) {
            // 一并清理本地皮肤/披风文件
            File(FCLPath.SKIN_DIR, "${account.uuid}.png").delete()
            File(FCLPath.SKIN_DIR, "${account.uuid}_cape.png").delete()
        }
        Accounts.getAccounts().remove(account)
    }

    companion object {
        @Throws(
            CancellationException::class,
            AuthenticationException::class,
            InterruptedException::class
        )
        fun logIn(account: Account): AuthInfo {
            if (account is ClassicAccount) {
                val latch = CountDownLatch(1)
                val res = AtomicReference<AuthInfo>(null)
                Schedulers.androidUIThread().execute {
                    val activity = FCLApp.getActivity()
                    if (activity == null) {
                        latch.countDown()
                        return@execute
                    }
                    val dialog = ClassicAccountLoginDialog(
                        activity, account,
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
                val res = AtomicReference<AuthInfo>(null)
                Schedulers.androidUIThread().execute {
                    val activity = FCLApp.getActivity()
                    if (activity == null) {
                        latch.countDown()
                        return@execute
                    }
                    val dialog = OAuthAccountLoginDialog(
                        activity, account,
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
            }
            return account.logIn()
        }
    }
}
