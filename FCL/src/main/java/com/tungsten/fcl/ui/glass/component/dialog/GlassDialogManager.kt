package compackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidxpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.authpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

sealed class DialogRequest(
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

sealed class DialogRequest(
    val id: String = java.util.UUID.randomUUID().toString()
) {
    data class Alert(
        val title: Stringpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

sealed class DialogRequest(
    val id: String = java.util.UUID.randomUUID().toString()
) {
    data class Alert(
        val title: String,
        val message: String,
        val confirmText: String,
        val dismisspackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

sealed class DialogRequest(
    val id: String = java.util.UUID.randomUUID().toString()
) {
    data class Alert(
        val title: String,
        val message: String,
        val confirmText: String,
        val dismissText: String? = null,
        val onConfirm: () -> Unit = {},
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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

package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val initialValue: String = "package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onDismiss: () -> Unit =package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        valpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val selected: T?package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val confirmTextpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onCancel: (() -> Unit)?package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        valpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onDismiss: () -> Unit =package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val accountpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
    ) : DialogRequestpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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

package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onDismiss: () -> Unit =package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val profilepackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onDismiss: () -> Unit =package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        val onpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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

object Glasspackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
    private val _requests =package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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

    var current bypackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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

    fun show(requestpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        if (current == null)package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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
        request?.let { _requests.remove(itpackage com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

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

    fun show