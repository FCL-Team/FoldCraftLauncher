package com.tungsten.fcl.ui.glass.page.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.fakefx.collections.ListChangeListener

class AccountListStateHolder {
    val accounts = mutableStateListOf<Account>()
    var selectedAccount: Account? by mutableStateOf(Accounts.getSelectedAccount())
        private set

    private var listListener: ListChangeListener<Account>? = null
    private var selectedListener: ChangeListener<Account>? = null

    fun startObserving() {
        accounts.clear()
        accounts.addAll(Accounts.getAccounts())
        selectedAccount = Accounts.getSelectedAccount()

        listListener = ListChangeListener { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    accounts.addAll(change.addedSubList)
                }
                if (change.wasRemoved()) {
                    accounts.removeAll(change.removed.toSet())
                }
            }
        }
        selectedListener = ChangeListener { _, _, new ->
            selectedAccount = new
        }

        Accounts.getAccounts().addListener(listListener)
        Accounts.selectedAccountProperty().addListener(selectedListener)
    }

    fun stopObserving() {
        listListener?.let { Accounts.getAccounts().removeListener(it) }
        selectedListener?.let { Accounts.selectedAccountProperty().removeListener(it) }
    }

    fun select(account: Account) {
        Accounts.setSelectedAccount(account)
    }

    fun remove(account: Account) {
        Accounts.getAccounts().remove(account)
    }
}
