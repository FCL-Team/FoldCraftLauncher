package com.tungsten.fcl.ui.download.compose

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.tungsten.fcl.ui.download.common.ModVersionAdapter
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 远程资源版本文件列表页（对齐 RemoteModVersionPage + ModVersionAdapter + item_mod_version）：
 * 名称 + 类型/加载器 tag + 发布时间；点击分发（Mod → 依赖下载页；其余 → 直接下载/另存为）。
 */

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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(version) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = version.name,
                            style = MiuixTheme.textStyles.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = modVersionTag(context, version),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MiuixTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = ModVersionAdapter.FORMATTER.format(version.datePublished),
                        fontSize = 11.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                    )
                }
            }
        }
    }
}
