package com.tungsten.fcl.ui.glass.page

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.main.Announcement
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.HttpRequest

@Composable
fun HomePage(
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var announcement by remember { mutableStateOf<Announcement?>(null) }
    var announcementVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        loadAnnouncement(context) { loaded ->
            if (loaded.shouldDisplay(context)) {
                announcement = loaded
                announcementVisible = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GlassTopBar(title = stringResource(R.string.app_name))

        if (announcementVisible && announcement != null) {
            AnnouncementCard(
                backdrop = backdrop,
                announcement = announcement!!,
                onDismiss = {
                    announcementVisible = false
                    announcement?.hide(context)
                }
            )
        }

        GlassSectionTitle(text = stringResource(R.string.account))
        AccountCard(backdrop = backdrop, tint = tintColor)

        Spacer(modifier = Modifier.height(24.dp))

        GlassSectionTitle(text = stringResource(R.string.launch))
        GlassButton(
            backdrop = backdrop,
            onClick = {
                com.tungsten.fcl.ui.version.Versions.launch(
                    context,
                    Profiles.getSelectedProfile()
                )
            },
            tint = tintColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.launch),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AnnouncementCard(
    backdrop: Backdrop,
    announcement: Announcement,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = AndroidUtils.getLocalizedText(
                    context,
                    "announcement",
                    announcement.getDisplayTitle(context)
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = announcement.getDisplayContent(context),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = AndroidUtils.getLocalizedText(context, "update_date", announcement.getDate()),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = stringResource(R.string.button_hide),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable(onClick = onDismiss)
            )
        }
    }
}

@Composable
private fun AccountCard(
    backdrop: Backdrop,
    tint: Color
) {
    val account = Accounts.getSelectedAccount()
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        tint = tint
    ) {
        Column {
            Text(
                text = account?.username ?: stringResource(R.string.account_state_no_account),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = account?.let {
                    Accounts.getLocalizedLoginTypeName(
                        LocalContext.current,
                        Accounts.getAccountFactory(it)
                    )
                } ?: stringResource(R.string.account_methods_offline),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun loadAnnouncement(context: Context, onLoaded: (Announcement) -> Unit) {
    try {
        val url = if (LocaleUtils.isChinese(context)) {
            com.tungsten.fcl.ui.main.MainUI.ANNOUNCEMENT_URL_CN
        } else {
            com.tungsten.fcl.ui.main.MainUI.ANNOUNCEMENT_URL
        }
        Task.supplyAsync {
            HttpRequest.HttpGetRequest.GET(url).getJson(Announcement::class.java)
        }.thenAcceptAsync(Schedulers.androidUIThread()) { loaded ->
            if (loaded != null) {
                onLoaded(loaded)
            }
        }.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
