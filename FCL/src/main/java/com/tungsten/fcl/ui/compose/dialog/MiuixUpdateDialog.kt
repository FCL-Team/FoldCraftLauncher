package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.upgrade.RemoteVersion
import com.tungsten.fcl.upgrade.UpdateChecker
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.util.io.NetworkUtils
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.util.concurrent.CancellationException
import java.util.function.Consumer

/**
 * Miuix 版启动器更新提示弹窗（3.2 批 1，对应 upgrade/UpdateDialog + dialog_update）。
 *
 * 行为对齐：
 * - 展示版本号/日期/类型/更新日志（同一套 string 格式化），内容超高可滚动
 *   （遗留 checkHeight 限高屏幕高 - 30dp，此处用 heightIn 近似）；
 * - 忽略 → UpdateChecker.setIgnore + dismiss；取消/网盘 → dismiss（网盘先开链接）；
 * - 更新 → 按设备架构替换下载地址，任务弹窗下载 APK（内嵌 TaskDialog 保留
 *   MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG 双分支），完成后拉起系统安装器；
 *   失败（非取消）弹 update_failed + 网盘按钮；更新按钮长按打开 GitHub releases（与遗留一致）；
 * - setCancelable(false) 一致。
 */
@OptIn(ExperimentalFoundationApi::class)
class MiuixUpdateDialog(
    context: Context,
    private val version: RemoteVersion,
) : FCLComposeDialog(context, cancelable = false) {

    init {
        setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.update_exist),
                modifier = Modifier.heightIn(max = (LocalConfiguration.current.screenHeightDp - 60).dp),
            ) {
                Text(
                    text = String.format(context.getString(R.string.update_version), version.versionName),
                    style = MiuixTheme.textStyles.body2,
                )
                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    Text(
                        text = String.format(context.getString(R.string.update_type), version.getDisplayType(context)),
                        style = MiuixTheme.textStyles.body2,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = String.format(context.getString(R.string.update_date), version.date),
                        style = MiuixTheme.textStyles.body2,
                        modifier = Modifier.weight(1f),
                    )
                }
                Text(
                    text = String.format(context.getString(R.string.update_description), version.getDisplayDescription(context)),
                    style = MiuixTheme.textStyles.body2,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        text = stringResource(R.string.update_ignore),
                        onClick = {
                            UpdateChecker.setIgnore(context, version.versionCode)
                            dismiss()
                        },
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(R.string.update_netdisk),
                        onClick = {
                            AndroidUtils.openLink(context, version.netdiskUrl)
                            dismiss()
                        },
                    )
                    Spacer(Modifier.width(8.dp))
                    // miuix TextButton 不支持长按，更新按钮自绘（点击更新 / 长按打开 GitHub releases）
                    Text(
                        text = stringResource(R.string.update),
                        color = MiuixTheme.colorScheme.primary,
                        style = MiuixTheme.textStyles.body1,
                        modifier = Modifier
                            .combinedClickable(
                                onClick = { onUpdate() },
                                onLongClick = {
                                    AndroidUtils.openLink(context, "https://github.com/FCL-Team/FoldCraftLauncher/releases/latest")
                                },
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    )
                }
            }
        }
    }

    private fun onUpdate() {
        val ctx = context
        // 内嵌任务弹窗：保留 MiuixTaskDialog 自身开关的双分支（对齐 WorldExportDialog 接入方式）
        val taskDialog: (TaskExecutor) -> Unit = { executor ->
            if (MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG) {
                val dialog = MiuixTaskDialog(ctx)
                dialog.setTitle(ctx.getString(R.string.update_launcher))
                dialog.setExecutor(executor)
                dialog.show()
            } else {
                val dialog = TaskDialog(ctx, TaskCancellationAction(Consumer { d: TaskDialog -> d.dismiss() }))
                dialog.setTitle(ctx.getString(R.string.update_launcher))
                dialog.setExecutor(executor)
                dialog.show()
            }
        }
        Schedulers.androidUIThread().execute {
            val executor = Task.composeAsync<Void> {
                val task = FileDownloadTask(
                    NetworkUtils.toURL(getTargetArchUrl()),
                    File(FCLPath.CACHE_DIR, "FoldCraftLauncher.apk"),
                )
                task.setName("FoldCraftLauncher")
                task.whenComplete(Schedulers.androidUIThread()) { exception ->
                    if (exception == null) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val apkUri = FileProvider.getUriForFile(
                            ctx,
                            ctx.getString(com.tungsten.fcllibrary.R.string.file_browser_provider),
                            File(FCLPath.CACHE_DIR, "FoldCraftLauncher.apk"),
                        )
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                        ctx.startActivity(intent)
                    } else if (exception !is CancellationException) {
                        FCLDialogs.showAlert(
                            ctx,
                            null,
                            ctx.getString(R.string.update_failed) + "\n" + exception.message,
                            positiveText = ctx.getString(R.string.update_netdisk),
                            negativeText = ctx.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                            onResult = { ok ->
                                if (ok) AndroidUtils.openLink(ctx, version.netdiskUrl)
                            },
                            cancelable = false,
                        )
                    }
                }
            }.executor()
            taskDialog(executor)
            executor.start()
        }
        dismiss()
    }

    private fun getTargetArchUrl(): String {
        var url = version.url
        val arch = when (Architecture.getDeviceArchitecture()) {
            Architecture.ARCH_ARM -> "armeabi-v7a"
            Architecture.ARCH_ARM64 -> "arm64-v8a"
            Architecture.ARCH_X86 -> "x86"
            Architecture.ARCH_X86_64 -> "x86_64"
            else -> "all"
        }
        url = url.replace("-all", "-$arch")
        return url
    }
}
