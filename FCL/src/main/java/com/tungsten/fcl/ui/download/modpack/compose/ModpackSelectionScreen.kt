package com.tungsten.fcl.ui.download.modpack.compose

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixModpackUrlDialog
import com.tungsten.fcl.ui.download.DownloadPageManager
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.mod.server.ServerModpackManifest
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.GetTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fcllibrary.component.view.FCLUILayout
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files

/**
 * 整合包安装向导入口页（对齐 ModpackSelectionPage + page_modpack_selection.xml）：
 * 本地文件 / 远程链接两个入口卡片。
 *
 * 行为对齐（ModpackSelectionPage.java）：
 * - 本地：文件选择器（.zip/.mrpack/.7z/.rar），doc Uri 先拷入缓存目录，
 *   然后"替换"当前临时页打开本地整合包信息页（:63-90）；
 * - 远程：[MiuixModpackUrlDialog] 输入链接；以 server-manifest.json 结尾走
 *   GetTask 解析清单 → 远程整合包信息页，否则按压缩包下载到临时文件 →
 *   本地整合包信息页（:92-151）；
 * - 下载/解析进度弹窗沿用遗留 TaskDialog 语义，此处直接落 MiuixTaskDialog
 *   （bridge-api §6 登记的 ModpackSelectionPage×2 替换点，本步骤一并完成）；
 * - 双 PageManager 分支：updateVersion == null 走 DownloadPageManager，
 *   否则走 ManagePageManager（dismiss 当前页 + 压新页，保持"替换"语义）。
 */
class ModpackSelectionStateHolder(
    private val context: Context,
    private val activity: Activity,
    private val parent: FCLUILayout,
    private val profile: Profile,
    private val updateVersion: String?,
) {

    /** 对齐 :63-90。 */
    fun onChooseLocalFile() {
        val suffix = arrayListOf(".zip", ".mrpack", ".7z", ".rar")
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, false) { files ->
            if (files == null) return@launchSingleSelection
            var path: String? = files[0]
            val uri = Uri.parse(path)
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(activity, uri, File(FCLPath.CACHE_DIR))
            }
            if (path == null) return@launchSingleSelection
            val selectedFile = File(path)
            Schedulers.androidUIThread().execute { showLocalPage(selectedFile) }
        }
    }

    /** 对齐 :92-151。 */
    fun onChooseRemoteFile() {
        val urlCallback = MiuixModpackUrlDialog.Callback { urlString ->
            try {
                val url = URL(urlString)
                if (urlString.endsWith("server-manifest.json")) {
                    // if urlString ends with .json, we assume that the url is server-manifest.json
                    val executor = GetTask(url).whenComplete(Schedulers.androidUIThread()) { result, e ->
                        val manifest = JsonUtils.fromMaybeMalformedJson(result, ServerModpackManifest::class.java)
                        if (manifest == null) {
                            Toast.makeText(context, context.getString(R.string.modpack_type_server_malformed), Toast.LENGTH_SHORT).show()
                        } else if (e == null) {
                            showRemotePage(manifest)
                        } else {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }.executor()
                    val taskDialog = MiuixTaskDialog(context)
                    taskDialog.setTitle(context.getString(R.string.message_downloading))
                    taskDialog.setExecutor(executor)
                    taskDialog.show()
                    executor.start()
                } else {
                    // otherwise we still consider the file as modpack zip file
                    // since casually the url may not ends with ".zip"
                    val modpack = Files.createTempFile("modpack", ".zip")
                    val executor = FileDownloadTask(url, modpack.toFile(), null)
                        .whenComplete(Schedulers.androidUIThread()) { e ->
                            if (e == null) {
                                showLocalPage(modpack.toFile())
                            } else {
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }.executor()
                    val taskDialog = MiuixTaskDialog(context)
                    taskDialog.setTitle(context.getString(R.string.message_downloading))
                    taskDialog.setExecutor(executor)
                    taskDialog.show()
                    executor.start()
                }
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        MiuixModpackUrlDialog(context, urlCallback).show()
    }

    /** 对齐 :79-88 / :105-112 / :130-137：dismiss 当前临时页 + 压入新页（双 PageManager 分支）。 */
    private fun showLocalPage(file: File) {
        val page = ComposeLocalModpackPage(context, PageManager.PAGE_ID_TEMP, parent, profile, updateVersion, file)
        if (updateVersion == null) {
            DownloadPageManager.instance?.dismissCurrentTempPage()
            DownloadPageManager.instance?.showTempPage(page)
        } else {
            ManagePageManager.instance?.dismissCurrentTempPage()
            ManagePageManager.instance?.showTempPage(page)
        }
    }

    private fun showRemotePage(manifest: ServerModpackManifest) {
        val page = ComposeRemoteModpackPage(context, PageManager.PAGE_ID_TEMP, parent, profile, updateVersion, manifest)
        if (updateVersion == null) {
            DownloadPageManager.instance?.dismissCurrentTempPage()
            DownloadPageManager.instance?.showTempPage(page)
        } else {
            ManagePageManager.instance?.dismissCurrentTempPage()
            ManagePageManager.instance?.showTempPage(page)
        }
    }
}

@Composable
fun ModpackSelectionScreen(holder: ModpackSelectionStateHolder) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // 对齐 page_modpack_selection.xml local（bg_container_white_clickable + 15dp padding）
        ModpackEntryCard(
            text = stringResource(R.string.modpack_choose_local),
            onClick = holder::onChooseLocalFile,
        )
        Spacer(Modifier.height(10.dp))
        // 对齐 remote
        ModpackEntryCard(
            text = stringResource(R.string.modpack_choose_remote),
            onClick = holder::onChooseRemoteFile,
        )
    }
}

@Composable
private fun ModpackEntryCard(text: String, onClick: () -> Unit) {
    FCLCard(
        modifier = Modifier.fillMaxWidth(),
        // 对齐 auto_linear_background_tint（ltColor = primaryContainer）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        onClick = onClick,
        // 对齐 bg_container_white_clickable 的按压反馈 → Sink（component-mapping X-5 既定替代）
        pressFeedbackType = PressFeedbackType.Sink,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 对齐 FCLTextView auto_text_tint（autoTint = onPrimary）
            Text(
                text = text,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onPrimary,
                maxLines = 1,
            )
            Spacer(Modifier.weight(1f))
            // 对齐 FCLImageView auto_src_tint（autoTint = onPrimary）
            Icon(
                painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
                contentDescription = null,
                tint = MiuixTheme.colorScheme.onPrimary,
            )
        }
    }
}
