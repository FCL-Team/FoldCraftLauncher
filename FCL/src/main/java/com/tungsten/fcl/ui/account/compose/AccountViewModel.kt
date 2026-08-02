package com.tungsten.fcl.ui.account.compose

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.ui.account.AccountListItem
import com.tungsten.fcl.ui.bridge.FCLViewModel
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer
import com.tungsten.fclcore.auth.offline.OfflineAccount
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 账户页 ViewModel（小步骤 3.5）：AccountUI.refresh + AccountListAdapter + ServerListAdapter
 * 数据侧的 Compose 化承接。
 *
 * 行为对齐（interaction-map §8.1/§8.2 逐条）：
 * - 账户列表数据构建：Accounts.getAccounts() → AccountListItem（标题/副标题/头像/纹理的
 *   observable 绑定全部留在 AccountListItem，业务零重写，对齐 AccountUI.refresh :61-76）；
 * - 列表自动刷新：阶段 4a 起 Accounts 列表已 StateFlow 化，accountsSignalFlow 任何变化
 *   （成员增删或账户内部变更，对齐原 extractor 冒泡）即重建条目列表，替代旧 Adapter 的
 *   clear+addAll+notifyDataSetChanged 与静态单例反向 refresh（G10 承接）；
 * - 选中态：Accounts.selectedAccountFlow 单向承接，点击单选 =
 *   写 Accounts.setSelectedAccount（对齐 AccountListAdapter.kt:63-66）；
 * - 外置登录服务器列表：config().authlibInjectorServersFlow() 监听自动刷新
 *   （对齐 ServerListAdapter :33-36）；
 * - 删除账户/删除服务器/离线账户改 UUID 的确认弹窗收敛为 UiState 内的 dialog 状态，
 *   确认逻辑逐行对齐旧 Adapter（replaceAccount + setSelectedAccount）。
 */
class AccountViewModel(
    private val application: Application,
) : FCLViewModel<AccountUiState, AccountEvent>(AccountUiState()) {

    init {
        Accounts.selectedAccountFlow()
            .observeIntoState { copy(selectedAccount = it) }
        // collect 立即发射当前值，初始 reload 已覆盖；viewModelScope 取消即退订（对齐 onCleared removeListener）
        viewModelScope.launch {
            Accounts.accountsSignalFlow().collect { reloadAccounts() }
        }
        viewModelScope.launch {
            ConfigHolder.config().authlibInjectorServersFlow().collect { reloadServers() }
        }
    }

    /** 重建账户条目列表（对齐 AccountUI.refresh :63-71；也承接 refreshHook 的手动刷新）。 */
    fun reloadAccounts() {
        val items = Accounts.getAccounts().map { AccountListItem(application, it) }
        updateState { copy(accounts = items) }
    }

    private fun reloadServers() {
        updateState { copy(servers = ConfigHolder.config().authlibInjectorServers.toList()) }
    }

    /** 单选/整行点击 = 设为当前账户（对齐 AccountListAdapter.kt:63-66）。 */
    fun onSelectAccount(item: AccountListItem) {
        Accounts.setSelectedAccount(item.account)
    }

    /** 添加离线/微软账户入口：弹窗属一次性副作用，交宿主（对齐 AccountUI.onClick :77-90）。 */
    fun onAddAccount(factory: AccountFactory<*>) {
        sendEvent(AccountEvent.CreateAccount(factory))
    }

    /** 添加外置登录服务器入口（对齐 AccountUI.onClick :91-99）。 */
    fun onAddAuthlibServer() {
        sendEvent(AccountEvent.AddAuthlibServer)
    }

    /** 点击服务器条目 = 创建绑定该服务器的账户（对齐 ServerListAdapter :74-82）。 */
    fun onServerClick(server: AuthlibInjectorServer) {
        sendEvent(AccountEvent.CreateAccountForServer(server))
    }

    /** 离线账户皮肤按钮 = 打开皮肤设置弹窗（对齐 AccountListAdapter.kt:119-126）。 */
    fun onOfflineSkinClick(item: AccountListItem) {
        sendEvent(AccountEvent.OpenOfflineSkin(item))
    }

    /** 皮肤按钮长按（仅离线账户）= 选本地 .png 皮肤（对齐 AccountListAdapter.kt:191-205）。 */
    fun onPickLocalSkin(item: AccountListItem) {
        if (item.account is OfflineAccount) {
            sendEvent(AccountEvent.PickLocalSkin(item))
        }
    }

    /** 刷新凭据/上传皮肤失败：弹错误提示（对齐 AccountListAdapter.kt:76-84 的 FCLAlertDialog）。 */
    fun onActionError(exception: Exception) {
        sendEvent(AccountEvent.ShowError(Accounts.localizeErrorMessage(application, exception)))
    }

    // ---------- 确认弹窗状态机 ----------

    fun onDeleteAccountClick(item: AccountListItem) {
        updateState { copy(dialog = AccountDialogState.DeleteAccount(item)) }
    }

    fun onDeleteServerClick(server: AuthlibInjectorServer) {
        updateState { copy(dialog = AccountDialogState.DeleteServer(server)) }
    }

    fun onEditUuidClick(item: AccountListItem) {
        if (item.account is OfflineAccount) {
            updateState {
                copy(dialog = AccountDialogState.EditUuid(item, item.account.uuid.toString()))
            }
        }
    }

    fun onDialogDismiss() {
        updateState { copy(dialog = AccountDialogState.None) }
    }

    fun onEditUuidTextChange(text: String) {
        val current = currentState.dialog as? AccountDialogState.EditUuid ?: return
        updateState { copy(dialog = current.copy(text = text)) }
    }

    /** 确认删除账户（对齐 AccountListAdapter.kt:175-189：remove + 列表监听自动刷新）。 */
    fun onConfirmDeleteAccount() {
        val current = currentState.dialog as? AccountDialogState.DeleteAccount ?: return
        current.item.remove()
        updateState { copy(dialog = AccountDialogState.None) }
    }

    /** 确认删除外置服务器（对齐 ServerListAdapter :83-90，Flow 监听自动刷新）。 */
    fun onConfirmDeleteServer() {
        val current = currentState.dialog as? AccountDialogState.DeleteServer ?: return
        ConfigHolder.config().removeAuthlibInjectorServer(current.server)
        updateState { copy(dialog = AccountDialogState.None) }
    }

    /**
     * 确认修改离线账户 UUID（对齐 AccountListAdapter.kt:154-173）：
     * 非法 UUID → Toast 且弹窗保持打开；合法 → 同用户名/皮肤新建账户并替换、设为选中。
     */
    fun onConfirmEditUuid() {
        val current = currentState.dialog as? AccountDialogState.EditUuid ?: return
        val uuid = runCatching { UUID.fromString(current.text) }.getOrNull()
        if (uuid == null) {
            sendEvent(AccountEvent.ShowToast(R.string.message_failed))
            return
        }
        val account = current.item.account as OfflineAccount
        Accounts.FACTORY_OFFLINE.create(account.username, uuid).apply {
            skin = account.skin
            Accounts.replaceAccount(account.uuid, this)
            Accounts.setSelectedAccount(this)
        }
        updateState { copy(dialog = AccountDialogState.None) }
    }
}

/** 账户页 UI 状态。 */
data class AccountUiState(
    /** 账户条目（标题/副标题/头像/纹理由各条目 observable 属性就地观察）。 */
    val accounts: List<AccountListItem> = emptyList(),
    /** 外置登录服务器列表。 */
    val servers: List<AuthlibInjectorServer> = emptyList(),
    val selectedAccount: Account? = null,
    val dialog: AccountDialogState = AccountDialogState.None,
)

/** 账户页确认弹窗状态（删除账户/删除服务器/离线账户改 UUID）。 */
sealed interface AccountDialogState {
    data object None : AccountDialogState
    data class DeleteAccount(val item: AccountListItem) : AccountDialogState
    data class DeleteServer(val server: AuthlibInjectorServer) : AccountDialogState
    data class EditUuid(val item: AccountListItem, val text: String) : AccountDialogState
}

/** 账户页一次性事件（弹窗/文件选择/Toast 等，交宿主处理）。 */
sealed interface AccountEvent {
    data class CreateAccount(val factory: AccountFactory<*>) : AccountEvent
    data object AddAuthlibServer : AccountEvent
    data class CreateAccountForServer(val server: AuthlibInjectorServer) : AccountEvent
    data class OpenOfflineSkin(val item: AccountListItem) : AccountEvent
    data class PickLocalSkin(val item: AccountListItem) : AccountEvent
    data class ShowError(val message: String) : AccountEvent
    data class ShowToast(val resId: Int) : AccountEvent
}
