package com.tungsten.fcl.ui.setting.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 关于页 Compose 界面（小步骤 3.1）：page_setting_about.xml + AboutPage.java 的 Miuix 重构。
 *
 * 6 个跳转条目与 about_desc 说明文本一一对应；链接点击经 onEvent 转
 * [AboutScreenHost]（需要 Activity 上下文 openLink / QQ scheme，对齐遗留行为）。
 * 页面无表单状态，不需要 ViewModel。
 */
@Composable
fun AboutScreen(
    onEvent: (AboutEvent) -> Unit = {},
) {
    // 根布局保持透明（露出用户壁纸），对齐遗留 page_setting_about.xml 透明根。
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
    ) {
        item(key = "links") {
            // 对齐遗留 6 个 auto_linear_background_tint 容器的 ltColor 染色（= primaryContainer）
            FCLCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                ArrowPreference(
                    title = stringResource(R.string.about_launcher),
                    onClick = { onEvent(AboutEvent.OpenLink(AboutScreenHost.URL_LAUNCHER)) },
                )
                ArrowPreference(
                    title = stringResource(R.string.about_developer),
                    onClick = { onEvent(AboutEvent.OpenLink(AboutScreenHost.URL_DEVELOPER)) },
                )
                ArrowPreference(
                    title = stringResource(R.string.community_discord),
                    onClick = { onEvent(AboutEvent.OpenLink(AboutScreenHost.URL_DISCORD)) },
                )
                ArrowPreference(
                    title = stringResource(R.string.community_qq),
                    onClick = { onEvent(AboutEvent.JoinQQGroup) },
                )
                ArrowPreference(
                    title = stringResource(R.string.about_sponsor),
                    onClick = { onEvent(AboutEvent.OpenLink(AboutScreenHost.URL_SPONSOR)) },
                )
                ArrowPreference(
                    title = stringResource(R.string.about_source),
                    onClick = { onEvent(AboutEvent.OpenLink(AboutScreenHost.URL_SOURCE)) },
                )
            }
        }
        item(key = "desc") {
            // 对齐遗留 about_desc 的 auto_text_background_tint（底色 = 主色 color）
            // 与 auto_text_tint（文字 = autoTint = onPrimary）
            FCLCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                insideMargin = PaddingValues(12.dp),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primary),
            ) {
                Text(
                    text = stringResource(R.string.about_desc),
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

/** 关于页一次性事件（链接跳转 / 加 QQ 群）。 */
sealed interface AboutEvent {
    data class OpenLink(val url: String) : AboutEvent
    data object JoinQQGroup : AboutEvent
}

/**
 * 关于页宿主事件处理：AboutPage.java onClick / joinQQGroup 的原样搬运。
 * QQ 群走 mqqopensdkapi scheme 隐式 Intent，异常静默（对齐遗留 :79-86）。
 */
object AboutScreenHost {
    const val URL_LAUNCHER = "https://fcl-team.github.io/"
    const val URL_DEVELOPER = "https://github.com/FCL-Team"
    const val URL_DISCORD = "https://discord.gg/ffhvuXTwyV"
    const val URL_SPONSOR = "https://afdian.com/@tungs"
    const val URL_SOURCE = "https://github.com/FCL-Team/FoldCraftLauncher"
    private const val QQ_GROUP_KEY = "9_Mnxe5x1l6L7giLuRYQyBh0iWBgCUbw"

    fun handle(context: Context, event: AboutEvent) {
        when (event) {
            is AboutEvent.OpenLink -> AndroidUtils.openLink(context, event.url)
            AboutEvent.JoinQQGroup -> joinQQGroup(context)
        }
    }

    private fun joinQQGroup(context: Context) {
        val intent = Intent()
        intent.data = Uri.parse(
            "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$QQ_GROUP_KEY",
        )
        try {
            context.startActivity(intent)
        } catch (ignored: Exception) {
        }
    }
}
