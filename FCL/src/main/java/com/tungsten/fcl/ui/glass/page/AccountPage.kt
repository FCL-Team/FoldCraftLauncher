package com.tungsten.fcl.ui.glass.page

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.account.AccountListItem
import com.tungsten.fcl.ui.account.CreateAccountDialog
import com.tungsten.fcl.ui.glass.component.GlassAccountItem
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.page.account.AccountListStateHolder
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.CredentialExpiredException
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task

@Composable
fun AccountPage(
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val stateHolder = remember { AccountListStateHolder() }

    DisposableEffect(Unit) {
        stateHolder.startObserving()
        onDispose { stateHolder.stopObserving() }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = stringResource(R.string.account))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            items(stateHolder.accounts, key = { it.identifier }) { account ->
                GlassAccountItem(
                    backdrop = backdrop,
                    account = account,
                    selected = stateHolder.selectedAccount == account,
                    onSelect = { stateHolder.select(account) },
                    onRefresh = { refreshAccount(context, account) },
                    onDelete = { stateHolder.remove(account) }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            GlassButton(
                backdrop = backdrop,
                onClick = { CreateAccountDialog(context, Accounts.FACTORY_OFFLINE).show() },
                tint = tintColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.account_create_offline), color = Color.White)
            }
            GlassButton(
                backdrop = backdrop,
                onClick = { CreateAccountDialog(context, Accounts.FACTORY_MICROSOFT).show() },
                tint = tintColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.account_create_microsoft), color = Color.White)
            }
        }
    }
}

private fun refreshAccount(context: Context, account: Account) {
    Task.runAsync {
        account.clearCache()
        try {
            account.logIn()
        } catch (e: CredentialExpiredException) {
            try {
                AccountListItem.logIn(account)
            } catch (e1: java.util.concurrent.CancellationException) {
                // ignore cancellation
            }
        }
    }.whenComplete(Schedulers.androidUIThread()) { _, exception ->
        if (exception != null) {
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
        }
    }.start()
}
