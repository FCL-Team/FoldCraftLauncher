package com.tungsten.fcl.ui.download.compose

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * 远程资源版本文件列表页（对齐遗留 RemoteModVersionPage + ModVersionAdapter + item_mod_version）：
 * 名称 + 类型/加载器 tag + 发布时间；点击分发（Mod → 依赖下载页；其余 → 直接下载/另存为）。
 */

/** 发布时间格式（迁移自遗留 ModVersionAdapter.FORMATTER，RemoteModDownloadScreen 复用）。 */
@SuppressLint("ConstantLocale")
val REMOTE_MOD_DATE_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

/** 版本 tag（逐条对齐 ModVersionAdapter.getTag :77-108）。 */
fun modVersionTag(context: Context, version: RemoteMod.Version): String {
    val sb = StringBuilder()
    when (version.versionType) {
        RemoteMod.VersionType.Beta, RemoteMod.VersionType.Alpha ->
            sb.append(context.getString(R.string.version_game_snapshot))

        else -> sb.append(context.getString(R.string.version_game_release))
    }
    for (loader in version.loaders) {
        when (loader) {
            ModLoaderType.FORGE ->
                sb.append("   ").append(context.getString(R.string.install_installer_forge))

            ModLoaderType.NEO_FORGED ->
                sb.append("   ").append(context.getString(R.string.install_installer_neoforge))

            ModLoaderType.FABRIC ->
                sb.append("   ").append(context.getString(R.string.install_installer_fabric))

            ModLoaderType.LITE_LOADER ->
                sb.append("   ").append(context.getString(R.string.install_installer_liteloader))

            ModLoaderType.QUILT ->
                sb.append("   ").append(context.getString(R.string.install_installer_quilt))

            else -> {}
        }
    }
    return sb.toString()
}

@Composable
fun RemoteModVersionScreen(
    versions: List<RemoteMod.Version>,
    onSelect: (RemoteMod.Version) -> Unit,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        items(versions, key = { it.modid + "@" + it.name + "@" + it.version }) { version ->
            // 入场动画对齐 ModVersionAdapter:73（animationSpeed×30，见 fclItemEntryModifier）；
            // 按压反馈对齐 anim_scale StateListAnimator（按压缩放，ModVersionAdapter:63）→
            // Miuix Card 可点击重载的 Sink 反馈（component-mapping §3 既定替代）
            FCLCard(
                onClick = { onSelect(version) },
                pressFeedbackType = PressFeedbackType.Sink,
                // 对齐 item_mod_version.xml 的 bg_container_white_clickable +
                // auto_linear_background_tint（ltColor 染色 = primaryContainer）
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                modifier = fclItemEntryModifier()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    // 对齐 item_mod_version.xml：name/tag/date 均 auto_text_tint（autoTint = onPrimary）
                    // 第一行：name + tag 内联（tag marginStart=10dp）
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = version.name,
                            style = MiuixTheme.textStyles.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                        Text(
                            text = modVersionTag(context, version),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f, fill = false),
                        )
                    }
                    // 第二行：date 12sp 单行（name 左缘对齐）
                    Text(
                        text = REMOTE_MOD_DATE_FORMATTER.format(version.datePublished),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
