package com.tungsten.fcl.ui.multiplayer

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.terracotta.Terracotta
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLSwitchPreference
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLCommonUI
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcl.ui.compose.FCLButton
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File

enum class MultiplayerView { MAIN, HOST, GUEST }

/** 联机页（对齐 ui_multiplayer.xml + MultiplayerUI）；旧 View 实现已随批3固化删除。 */
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
        // 左栏 30%（对齐 constraintWidth_percent=0.3）：主题色菜单
        Column(Modifier.fillMaxHeight().fillMaxWidth(0.3f)) {
            Text(
                text = stringResource(R.string.terracotta),
                fontSize = 11.sp,
                color = MiuixTheme.colorScheme.primary,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 10.dp),
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                    .height(1.dp)
                    .background(Color(0xFFA9A9A9))
            )
            MultiplayerMenuItem(
                iconRes = R.drawable.ic_baseline_tune_24,
                textRes = R.string.terracotta_terracotta,
                modifier = Modifier.padding(top = 10.dp),
            ) { page = MultiplayerView.MAIN }
            MultiplayerMenuItem(R.drawable.ic_baseline_host_24, R.string.terracotta_tutorial_host_btn) {
                page = MultiplayerView.HOST
            }
            MultiplayerMenuItem(R.drawable.ic_baseline_guest_24, R.string.terracotta_tutorial_guest_btn) {
                page = MultiplayerView.GUEST
            }
            Spacer(Modifier.weight(1f))
            // 旧页 feedback 入口固定 GONE（MultiplayerUI.onCreate:69 Todo 注释），此处不落 UI
            MultiplayerMenuItem(R.drawable.ic_baseline_easytier_24, R.string.terracotta_easytier) {
                AndroidUtils.openLink(context, "https://easytier.cn/")
            }
        }
        Spacer(Modifier.padding(start = 10.dp))
        when (page) {
            MultiplayerView.MAIN -> MainMultiplayerContent(enabled) { value ->
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
                            prefs.edit().putBoolean("terracotta", true).apply()
                            enabled = true
                            // 对齐旧页：确认后开关置 true 会重走监听器，补通知权限申请
                            if (context is MainActivity && !context.checkNotificationPermission()) {
                                context.requestNotificationPermission()
                            }
                        }
                    }
                }
            }
            MultiplayerView.HOST -> TutorialContent(R.string.terracotta_tutorial_host)
            MultiplayerView.GUEST -> TutorialContent(R.string.terracotta_tutorial_guest)
        }
    }
}

/** 对齐旧左侧透明点击行：20dp 主题色图标 + 主题色文本（padding=10）。 */
@Composable
private fun MultiplayerMenuItem(iconRes: Int, textRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier.fillMaxWidth().clickable(onClick = onClick).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MiuixTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(textRes),
            color = MiuixTheme.colorScheme.primary,
            maxLines = 1,
            modifier = Modifier.padding(start = 10.dp),
        )
    }
}

@Composable
private fun MainMultiplayerContent(enabled: Boolean, onEnabledChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    Column(Modifier.fillMaxWidth(0.7f).fillMaxHeight().verticalScroll(rememberScrollState())) {
        // 设置卡（对齐 bg_container_white + paddingStart/End=10）
        FCLCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            FCLSwitchPreference(
                checked = enabled,
                onCheckedChange = onEnabledChange,
                title = stringResource(R.string.terracotta_enable),
            )
            if (enabled) {
                // extra_layout：分隔线 + 导出日志行
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(1.dp)
                        .background(Color(0xFFA9A9A9))
                )
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.terracotta_export_log_share),
                        color = MiuixTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )
                    FCLButton(onClick = { shareTerracottaLog(context) }) {
                        Text(stringResource(R.string.action_share))
                    }
                }
            }
        }
        // 须知卡（marginTop=10）
        FCLCard(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            Text(
                text = stringResource(R.string.terracotta_confirm),
                color = MiuixTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Composable
private fun TutorialContent(text: Int) {
    Column(Modifier.fillMaxWidth(0.7f).fillMaxHeight().verticalScroll(rememberScrollState())) {
        FCLCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            Text(
                text = stringResource(text),
                color = MiuixTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(10.dp),
            )
        }
    }
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
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addCategory(Intent.CATEGORY_DEFAULT)
    }
    (context as? android.app.Activity)?.startActivity(Intent.createChooser(intent, context.getString(R.string.terracotta_export_log_share)))
}
