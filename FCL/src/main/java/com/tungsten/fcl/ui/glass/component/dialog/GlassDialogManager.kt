package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.setting.Profile
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory

sealed class DialogRequest(
    val id: String = java.util.UUID.randomUUID().toString()
) {
    data class Alert(
        val title: String,
        val message: String,
        val confirmText: String,
        val dismissText: String? = null,
        val onConfirm: () -> Unit = {},
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class Input(
        val title: String,
        val initialValue: String = "",
        val hint: String = "",
        val confirmText: String,
        val dismissText: String = "",
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class Selection<T>(
        val title: String,
        val items: List<T>,
        val selected: T? = null,
        val itemText: (T) -> String,
        val confirmText: String? = null,
        val dismissText: String = "",
        val onSelect: (T) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class Progress(
        val title: String,
        val message: String = "",
        val progress: Float? = null,
        val onCancel: (() -> Unit)? = null
    ) : DialogRequest()

    data class CreateAccount(
        val factory: AccountFactory<*>? = null,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class ReLogin(
        val account: Account,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class AuthlibServer(
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class RenameVersion(
        val profile: Profile,
        val version: String,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class DuplicateVersion(
        val profile: Profile,
        val version: String,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class AddProfile(
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()
}

object GlassDialogManager {
    private val _requests = mutableStateListOf<DialogRequest>()
    val requests: List<DialogRequest> get() = _requests

    var current by mutableStateOf<DialogRequest?>(null)
        private set

    fun show(request: DialogRequest) {
        _requests.add(request)
        if (current == null) current = request
    }

    fun dismiss(request: DialogRequest? = current) {
        request?.let { _requests.remove(it) }
        current = _requests.firstOrNull()
    }

    fun showAlert(
        title: String,
        message: String,
        confirmText: String,
        dismissText: String? = null,
        onConfirm: () -> Unit = {},
        onDismiss: () -> Unit = {}
    ) = show(DialogRequest.Alert(title, message, confirmText, dismissText, onConfirm, onDismiss))

    fun showInput(
        title: String,
        initialValue: String = "",
        hint: String = "",
        confirmText: String,
        dismissText: String = "",
        onConfirm: (String) -> Unit,
        onDismiss: () -> Unit = {}
    ) = show(DialogRequest.Input(title, initialValue, hint, confirmText, dismissText, onConfirm, onDismiss))

    fun <T> showSelection(
        title: String,
        items: List<T>,
        selected: T? = null,
        itemText: (T) -> String = { it.toString() },
        confirmText: String? = null,
        dismissText: String = "",
        onSelect: (T) -> Unit,
        onDismiss: () -> Unit = {}
    ) = show(DialogRequest.Selection(title, items, selected, itemText, confirmText, dismissText, onSelect, onDismiss))

    fun showProgress(
        title: String,
        message: String = "",
        progress: Float? = null,
        onCancel: (() -> Unit)? = null
    ) = show(DialogRequest.Progress(title, message, progress, onCancel))

    fun showCreateAccount(factory: AccountFactory<*>? = null, onDismiss: () -> Unit = {}) =
        show(DialogRequest.CreateAccount(factory, onDismiss))

    fun showReLogin(account: Account, onDismiss: () -> Unit = {}) =
        show(DialogRequest.ReLogin(account, onDismiss))

    fun showAuthlibServer(onDismiss: () -> Unit = {}) =
        show(DialogRequest.AuthlibServer(onDismiss))

    fun showRenameVersion(profile: Profile, version: String, onDismiss: () -> Unit = {}) =
        show(DialogRequest.RenameVersion(profile, version, onDismiss))

    fun showDuplicateVersion(profile: Profile, version: String, onDismiss: () -> Unit = {}) =
        show(DialogRequest.DuplicateVersion(profile, version, onDismiss))

    fun showAddProfile(onDismiss: () -> Unit = {}) =
        show(DialogRequest.AddProfile(onDismiss))
}
