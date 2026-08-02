package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.CompressingUtils
import com.tungsten.fclcore.util.io.FileUtils
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Files

/**
 * Miuix 版 Mod 详情弹窗（3.2 批 2，对应 ui/manage/ModInfoDialog + dialog_mod_info）。
 *
 * 行为对齐：异步从 Mod 压缩包读取 logo（无 logo 或读取失败用 img_command 占位；
 * 遗留无 logoPath 时完全不走加载、ImageView 保持空白——此处同样不触发加载）；
 * 名称 + 加载器标签/版本 + 文件名 + 描述；website 按钮仅在 url 非空时显示并打开链接；
 * 确定 dismiss；setCancelable(false) 一致。
 *
 * 注意：构造参数为 [LocalModFile] 而非遗留的 ModListPage.ModInfoObject——
 * ModInfoObject.getModInfo() 是包私有，compose.dialog 包不可见；调用点
 * （ui.manage 包内）直接传 modInfoObject.modInfo。
 */
class MiuixModInfoDialog(
    context: Context,
    private val modInfo: LocalModFile,
) : FCLComposeDialog(context, cancelable = false) {

    private val iconState = mutableStateOf<ImageBitmap?>(null)

    init {
        if (StringUtils.isNotBlank(modInfo.logoPath)) {
            Task.supplyAsync {
                try {
                    CompressingUtils.createReadOnlyZipFileSystem(modInfo.file).use { fs ->
                        val iconPath = fs.getPath(modInfo.logoPath)
                        if (Files.exists(iconPath)) {
                            val stream = ByteArrayOutputStream()
                            Files.copy(iconPath, stream)
                            return@supplyAsync ByteArrayInputStream(stream.toByteArray())
                        }
                    }
                } catch (_: Exception) {
                }
                null
            }.whenComplete(Schedulers.androidUIThread()) { stream, _ ->
                iconState.value = if (stream != null) {
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                } else {
                    null
                }
            }.start()
        }

        setDialogContent {
            val tag = getTag()
            FCLDialogCard(
                buttons = buildList {
                    add(FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { dismiss() },
                    ))
                    if (StringUtils.isNotBlank(modInfo.url)) {
                        add(FCLDialogButton(
                            text = stringResource(R.string.mods_url),
                            onClick = { AndroidUtils.openLink(context, modInfo.url) },
                        ))
                    }
                },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = iconState.value
                    if (icon != null) {
                        Image(
                            bitmap = icon,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )
                    } else if (StringUtils.isNotBlank(modInfo.logoPath)) {
                        Image(
                            painter = painterResource(R.drawable.img_command),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )
                    } else {
                        Spacer(Modifier.size(30.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    androidx.compose.foundation.layout.Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = modInfo.name,
                                style = MiuixTheme.textStyles.body2,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = tag,
                                style = MiuixTheme.textStyles.body2,
                                fontSize = 11.sp,
                                maxLines = 1,
                            )
                        }
                        Text(
                            text = FileUtils.getName(modInfo.file),
                            style = MiuixTheme.textStyles.body2,
                            maxLines = 1,
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = modInfo.description.toString(),
                    style = MiuixTheme.textStyles.body2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                )
            }
        }
    }

    private fun getTag(): String {
        val modLoaderType = getModLoader(modInfo.modLoaderType)
        val split = if (modLoaderType.isEmpty()) "" else "   "
        return modLoaderType + split + modInfo.version
    }

    private fun getModLoader(modLoaderType: ModLoaderType): String = when (modLoaderType) {
        ModLoaderType.FORGE -> context.getString(R.string.install_installer_forge)
        ModLoaderType.NEO_FORGED -> context.getString(R.string.install_installer_neoforge)
        ModLoaderType.FABRIC -> context.getString(R.string.install_installer_fabric)
        ModLoaderType.LITE_LOADER -> context.getString(R.string.install_installer_liteloader)
        ModLoaderType.QUILT -> context.getString(R.string.install_installer_quilt)
        else -> ""
    }
}
