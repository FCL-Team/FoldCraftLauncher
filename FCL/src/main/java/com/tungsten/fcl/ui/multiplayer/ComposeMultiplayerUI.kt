package com.tungsten.fcl.ui.multiplayer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLSwitchPreference
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcl.terracotta.Terracotta
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLCommonUI
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File

enum class MultiplayerView { MAIN, HOST, GUEST }

class ComposeMultiplayerUI(
    context: Context,
    parent: FCLUILayout,
) : FCLCommonUI(context, parent, R.layout.page_compose_container) {
    override fun onCreate() {
        super.onCreate()
        val container = findViewById<android.widget.FrameLayout>(R.id.compose_container)
        container.addView(LegacyBridge.createComposeView(context) { MultiplayerScreen() })
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

}

@Composable
private fun MultiplayerScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("third_party", Context.MODE_PRIVATE) }
    var enabled by remember { mutableStateOf(prefs.getBoolean("terracotta", false)) }
    var page by remember { mutableStateOf(MultiplayerView.MAIN) }

    Row(Modifier.fillMaxSize().padding(10.dp)) {
        Column(Modifier.fillMaxHeight().fillMaxWidth(0.3f)) {
            TextLabel(stringResource(R.string.terracotta), 11.sp, Modifier.padding(horizontal = 10.dp))
            Spacer(Modifier.padding(top = 5.dp).height(1.dp).fillMaxWidth().background(Color.DarkGray))
            MultiplayerMenuItem(stringResource(R.string.terracotta_terracotta), "⚙", page == MultiplayerView.MAIN) { page = MultiplayerView.MAIN }
            MultiplayerMenuItem(stringResource(R.string.terracotta_tutorial_host_btn), "⌂", page == MultiplayerView.HOST) { page = MultiplayerView.HOST }
            MultiplayerMenuItem(stringResource(R.string.terracotta_tutorial_guest_btn), "↗", page == MultiplayerView.GUEST) { page = MultiplayerView.GUEST }
            Spacer(Modifier.weight(1f))
            // The legacy feedback action is intentionally hidden (MultiplayerUI.onCreate line 69).
            MultiplayerMenuItem(stringResource(R.string.terracotta_easytier), "↗", false) {
                AndroidUtils.openLink(context, "https://easytier.cn/")
            }
        }
        Spacer(Modifier.width(10.dp))
        when (page) {
            MultiplayerView.MAIN -> MainMultiplayerContent(context, prefs, enabled) { value ->
                val noticeVersion = prefs.getInt("terracotta_user_notice", 0)
                if (noticeVersion >= Terracotta.getUserNoticeVersion() || !value) {
                    prefs.edit().putBoolean("terracotta", value).apply()
                    enabled = value
                    if (value && context is MainActivity && !context.checkNotificationPermission()) {
                        context.requestNotificationPermission()
                    }
                } else {
                    enabled = false
                    LegacyBridge.requestAlertDialog(
                        null,
                        context.getString(R.string.terracotta_status_uninitialized_desc),
                        context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                        null,
                    ) { accepted ->
                        if (accepted) {
                            prefs.edit().putInt("terracotta_user_notice", Terracotta.getUserNoticeVersion()).apply()
                            enabled = true
                            prefs.edit().putBoolean("terracotta", true).apply()
                        }
                    }
                }
            }
            MultiplayerView.HOST -> TutorialContent(R.string.terracotta_tutorial_host)
            MultiplayerView.GUEST -> TutorialContent(R.string.terracotta_tutorial_guest)
        }
    }
}

@Composable
private fun MainMultiplayerContent(
    context: Context,
    prefs: android.content.SharedPreferences,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
) {
    Column(Modifier.fillMaxWidth(0.7f).fillMaxHeight().verticalScroll(rememberScrollState())) {
        FCLCard(Modifier.fillMaxWidth()) {
            FCLSwitchPreference(
                checked = enabled,
                onCheckedChange = onEnabledChange,
                title = stringResource(R.string.terracotta_enable),
            )
            if (enabled) {
                Spacer(Modifier.height(1.dp).fillMaxWidth().background(Color.DarkGray))
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextLabel(stringResource(R.string.terracotta_export_log_share), 14.sp, Modifier.weight(1f))
                    top.yukonga.miuix.kmp.basic.Button(
                        onClick = { shareTerracottaLog(context) },
                    ) {
                        top.yukonga.miuix.kmp.basic.Text(stringResource(R.string.action_share))
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        FCLCard(Modifier.fillMaxWidth()) {
            TextLabel(stringResource(R.string.terracotta_confirm), 14.sp, Modifier.padding(10.dp))
        }
    }
}

@Composable
private fun TutorialContent(text: Int) {
    Column(Modifier.fillMaxWidth(0.7f).fillMaxHeight().verticalScroll(rememberScrollState())) {
        FCLCard(Modifier.fillMaxWidth()) { TextLabel(stringResource(text), 14.sp, Modifier.padding(10.dp)) }
    }
}

@Composable
private fun MultiplayerMenuItem(title: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextLabel(icon, 20.sp, Modifier.size(20.dp), selected)
        Spacer(Modifier.width(10.dp))
        TextLabel(title, 14.sp, Modifier, selected)
    }
}

@Composable
private fun TextLabel(text: String, size: androidx.compose.ui.unit.TextUnit, modifier: Modifier, selected: Boolean = false) {
    top.yukonga.miuix.kmp.basic.Text(
        text = text,
        modifier = modifier,
        fontSize = size,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        color = top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme.onSurface,
    )
}

private fun shareTerracottaLog(context: Context) {
    val file = File(FCLPath.LOG_DIR, "terracotta.log")
    if (!file.exists()) {
        Toast.makeText(context, context.getString(R.string.terracotta_export_log_share_null), Toast.LENGTH_SHORT).show()
        return
    }
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = AndroidUtils.getMimeType(file.path)
        putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.getString(com.tungsten.fcllibrary.R.string.file_browser_provider), file))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addCategory(Intent.CATEGORY_DEFAULT)
    }
    (context as? android.app.Activity)?.startActivity(Intent.createChooser(intent, context.getString(R.string.terracotta_export_log_share)))
}
