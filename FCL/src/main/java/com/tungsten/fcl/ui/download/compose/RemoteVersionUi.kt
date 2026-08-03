package com.tungsten.fcl.ui.download.compose

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.download.fabric.FabricAPIRemoteVersion
import com.tungsten.fclcore.download.fabric.FabricRemoteVersion
import com.tungsten.fclcore.download.forge.ForgeRemoteVersion
import com.tungsten.fclcore.download.game.GameRemoteVersion
import com.tungsten.fclcore.download.liteloader.LiteLoaderRemoteVersion
import com.tungsten.fclcore.download.neoforge.NeoForgeRemoteVersion
import com.tungsten.fclcore.download.optifine.OptiFineRemoteVersion
import com.tungsten.fclcore.download.quilt.QuiltAPIRemoteVersion
import com.tungsten.fclcore.download.quilt.QuiltRemoteVersion
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import com.tungsten.fcllibrary.util.LocaleUtils
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType

/**
 * 远程版本行（对齐 RemoteVersionListAdapter + item_remote_version.xml）：
 * 图标 + 版本号 + 类型 tag + 发布日期 + wiki / 镜像地址按钮。
 * 被 VersionInstallScreen（游戏 Tab）与 InstallerListScreen（加载器选择）复用。
 */

/** 行图标（对齐 RemoteVersionListAdapter.getIcon）。 */
@DrawableRes
fun remoteVersionIconRes(version: RemoteVersion): Int = when (version) {
    is LiteLoaderRemoteVersion -> R.drawable.img_chicken
    is OptiFineRemoteVersion -> R.drawable.img_optifine
    is ForgeRemoteVersion -> R.drawable.img_forge
    is NeoForgeRemoteVersion -> R.drawable.img_neoforge
    is FabricRemoteVersion, is FabricAPIRemoteVersion -> R.drawable.img_fabric
    is QuiltRemoteVersion, is QuiltAPIRemoteVersion -> R.drawable.img_quilt
    is GameRemoteVersion -> when (version.versionType) {
        RemoteVersion.Type.RELEASE -> R.drawable.img_grass
        RemoteVersion.Type.PENDING,
        RemoteVersion.Type.UNOBFUSCATED,
        RemoteVersion.Type.SNAPSHOT,
            ->
            if (GameVersionNumber.asGameVersion(version.gameVersion).isAprilFools) {
                R.drawable.april_fools
            } else {
                R.drawable.img_command
            }

        else -> R.drawable.img_craft_table
    }

    else -> R.drawable.img_grass
}

/** 行 tag（对齐 RemoteVersionListAdapter.getTag）。 */
fun remoteVersionTag(context: Context, version: RemoteVersion): String =
    if (version is GameRemoteVersion) {
        when (version.versionType) {
            RemoteVersion.Type.RELEASE -> context.getString(R.string.version_game_release)
            RemoteVersion.Type.UNOBFUSCATED,
            RemoteVersion.Type.PENDING,
            RemoteVersion.Type.SNAPSHOT,
                -> context.getString(R.string.version_game_snapshot)

            else -> context.getString(R.string.version_game_old)
        }
    } else {
        version.gameVersion
    }

/** wiki 按钮可见性（对齐 RemoteVersionListAdapter :75）。 */
fun remoteVersionShowWiki(version: RemoteVersion): Boolean =
    version is GameRemoteVersion &&
        (version.versionType == RemoteVersion.Type.RELEASE ||
            version.versionType == RemoteVersion.Type.SNAPSHOT ||
            version.versionType == RemoteVersion.Type.UNOBFUSCATED)

/** 镜像地址按钮可见性（对齐 RemoteVersionListAdapter :88）。 */
fun remoteVersionShowSave(version: RemoteVersion): Boolean =
    version !is GameRemoteVersion &&
        version !is FabricAPIRemoteVersion &&
        version !is QuiltAPIRemoteVersion

/** Minecraft Wiki 地址后缀（逐条对齐 RemoteVersionListAdapter.getWikiUrlSuffix :195-251）。 */
fun remoteVersionWikiSuffix(context: Context, gameVersion: String): String {
    val id = gameVersion.lowercase()
    when (id) {
        "0.30-1", "0.30-2", "c0.30_01c" ->
            return context.getString(R.string.wiki_game_search, "Classic_0.30")

        "in-20100206-2103" ->
            return context.getString(R.string.wiki_game_search, "Indev_20100206")

        "inf-20100630-1" ->
            return context.getString(R.string.wiki_game_search, "Infdev_20100630")

        "inf-20100630-2" -> return context.getString(R.string.wiki_game_search, "Alpha_v1.0.0")
        "1.19_deep_dark_experimental_snapshot-1" -> return "1.19-exp1"
        "in-20100130" ->
            return context.getString(R.string.wiki_game_search, "Indev_0.31_20100130")

        "b1.6-tb3" ->
            return context.getString(R.string.wiki_game_search, "Beta_1.6_Test_Build_3")
    }
    if (id.startsWith("1.0.0-rc2")) return "RC2"
    if (id.startsWith("2.0")) return context.getString(R.string.wiki_game_search, "2.0")
    if (id.startsWith("b1.8-pre1")) return "Beta_1.8-pre1"
    if (id.startsWith("b1.1-")) return context.getString(R.string.wiki_game_search, "Beta_1.1")
    if (id.startsWith("a1.1.0")) return "Alpha_v1.1.0"
    if (id.startsWith("a1.0.14")) return "Alpha_v1.0.14"
    if (id.startsWith("a1.0.13_01")) return "Alpha_v1.0.13_01"
    if (id.startsWith("in-20100214")) {
        return context.getString(R.string.wiki_game_search, "Indev_20100214")
    }
    if (id.contains("experimental-snapshot")) {
        return id.replace("-experimental-snapshot", "-exp")
    }
    if (id.startsWith("inf-")) return id.replace("inf-", "Infdev_")
    if (id.startsWith("in-")) return id.replace("in-", "Indev_")
    if (id.startsWith("rd-")) return "pre-Classic_$id"
    if (id.startsWith("b")) return id.replace("b", "Beta_")
    if (id.startsWith("a")) return id.replace("a", "Alpha_v")
    if (id.startsWith("c")) return id.replace("c", "Classic_").replace("st", "SURVIVAL_TEST")
    return id
}

@Composable
fun RemoteVersionRow(
    version: RemoteVersion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    /** 镜像地址按钮回调；非空且 [remoteVersionShowSave] 时在条目内部右端渲染（VR-P2-1）。 */
    onSave: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    FCLCard(
        // 对齐 item_remote_version.xml 的 bg_container_white_clickable + auto_tint（ltColor 染色 = primaryContainer）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        // 按压反馈对齐 item_remote_version.xml stateListAnimator=anim_scale → Sink（X-5）
        onClick = onClick,
        pressFeedbackType = PressFeedbackType.Sink,
        // 对齐 item_remote_version.xml marginBottom=10dp
        modifier = modifier.fillMaxWidth().padding(bottom = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // 对齐 item_remote_version.xml padding 10/8
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(remoteVersionIconRes(version)),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 对齐 item_remote_version.xml：version/date 为 auto_text_tint（= onPrimary）
                    Text(
                        text = version.selfVersion,
                        style = MiuixTheme.textStyles.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                    // 对齐 RemoteVersionListAdapter:61-69：tag 为 bg_container_white +
                    // autoBackgroundTint（主色实心 chip = primary）+ auto_text_tint（= onPrimary）
                    Text(
                        text = remoteVersionTag(context, version),
                        fontSize = 11.sp,
                        maxLines = 1,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .background(
                                MiuixTheme.colorScheme.primary,
                                RoundedCornerShape(5.dp),
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                }
                version.releaseDate?.let { date ->
                    Text(
                        text = LocaleUtils.formatDateTime(context, date),
                        // 对齐 item_remote_version.xml date：12sp
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                }
            }
            if (remoteVersionShowWiki(version)) {
                // 对齐 wiki FCLImageButton auto_tint（图标 autoTint = onPrimary）
                IconButton(onClick = {
                    AndroidUtils.openLink(
                        context,
                        context.getString(
                            R.string.wiki_game,
                            remoteVersionWikiSuffix(context, version.gameVersion),
                        ),
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_earth_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onPrimary,
                    )
                }
            }
            if (onSave != null && remoteVersionShowSave(version)) {
                // 对齐 item_remote_version.xml save FCLImageButton auto_tint（= onPrimary），
                // 位于条目内部右端、随卡片一起染色/按压（VR-P2-1）
                IconButton(onClick = onSave) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_jump_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

/**
 * 镜像地址选择弹窗（对齐 RemoteVersionListAdapter save 按钮的原生 AlertDialog 列表，
 * component-mapping.md §3：原生 AlertDialog → Miuix 重写）。
 */
@Composable
fun RemoteVersionSaveDialog(
    version: RemoteVersion,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    FCLDialog(
        show = true,
        onDismissRequest = onDismiss,
        title = stringResource(R.string.message_select_url),
        buttons = listOf(
            FCLDialogButton(
                text = stringResource(R.string.button_cancel),
                onClick = onDismiss,
            ),
        ),
        content = {
            // 滚动由基座 FCLDialog 内容区统一处理，此处不再嵌套 verticalScroll
            Column {
                version.urls.forEach { url ->
                    Text(
                        text = url,
                        style = MiuixTheme.textStyles.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                AndroidUtils.openLink(context, url)
                                onDismiss()
                            }
                            .padding(vertical = 10.dp),
                    )
                }
            }
        },
    )
}
