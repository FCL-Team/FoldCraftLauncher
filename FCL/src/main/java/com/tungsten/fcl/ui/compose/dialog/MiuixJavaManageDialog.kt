package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.mio.JavaManager
import com.mio.util.checkElfIsAndroid
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

/**
 * Miuix 版 Java 运行时管理弹窗（3.2 批 2，对应 com/mio/ui/dialog/JavaManageDialog + dialog_manage_java）。
 *
 * 行为对齐：
 * - 列表 = JavaManager.javaList 中非 auto 项按版本号分段数值升序（同遗留 refresh() 比较器）；
 * - 行 = 名称（内置运行时带 version 文件时追加“(internal)”并不显示删除钮）+ 版本号；
 * - 点行 → onSelected(name) + dismiss；点删除 → FCLAlertDialog 确认后 JavaManager.remove + 刷新；
 * - 导入按钮拉起 .tar.xz 单选：后缀不对 → 错误提示；重名 → 覆盖确认（取消关闭流）；
 *   导入期间列表换为进度指示且三按钮禁用；解压+patch 后 checkElfIsAndroid 失败 →
 *   提示继续/取消（取消删除目录）；addToJavaVersion 失败 → 删除目录并提示；
 * - 自动选择 → onSelected("Auto") + dismiss；取消按钮仅非导入中可 dismiss；
 * - setCancelable(false) 一致。
 *
 * 嵌套确认/错误弹窗沿用遗留 FCLAlertDialog（dialog_alert 不在本批范围，后续批次统一迁移）。
 */
class MiuixJavaManageDialog(
    context: Context,
    private val onSelected: (String) -> Unit,
) : FCLComposeDialog(context, cancelable = false) {

    private val versionList = mutableListOf<JavaVersion>()
    private val versionListState = mutableStateOf<List<JavaVersion>>(emptyList())
    private val loadingState = mutableStateOf(false)

    init {
        refresh()
        setDialogContent {
            val loading = loadingState.value
            FCLDialogCard(
                title = "Java",
                scrollable = false,
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(R.string.button_cancel),
                        enabled = !loading,
                        onClick = { dismiss() },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.settings_game_java_version_auto),
                        enabled = !loading,
                        onClick = {
                            onSelected("Auto")
                            dismiss()
                        },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.import_java),
                        enabled = !loading,
                        onClick = { onImport() },
                    ),
                ),
            ) {
                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                    ) {
                        items(versionListState.value) { java ->
                            JavaRow(java)
                        }
                    }
                }
            }
        }
    }

    @androidx.compose.runtime.Composable
    private fun JavaRow(java: JavaVersion) {
        val internal = File(FCLPath.JAVA_PATH, java.name).resolve("version").exists()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelected(java.name)
                    dismiss()
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = if (internal) "${java.name} (${context.getString(R.string.internal)})" else java.name,
                    style = MiuixTheme.textStyles.body2,
                )
                Text(
                    text = java.versionName,
                    style = MiuixTheme.textStyles.footnote1,
                )
            }
            if (!internal) {
                IconButton(onClick = { onDelete(java) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_delete_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }

    private fun onDelete(java: JavaVersion) {
        FCLAlertDialog.Builder(context)
            .setMessage(context.getString(R.string.button_remove_confirm))
            .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
            .setPositiveButton {
                JavaManager.remove(java.name)
                refresh()
            }
            .setNegativeButton(null)
            .create()
            .show()
    }

    private fun onImport() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(
            null,
            listOf(".tar.xz")
        ) { files ->
            val path = files[0]
            val uri = path.toUri()
            val fileName = if (AndroidUtils.isDocUri(uri)) {
                AndroidUtils.getFileName(context, uri)
            } else {
                File(path).name
            }
            if (!fileName.endsWith(".tar.xz")) {
                FCLAlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.import_java_wrong_file))
                    .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                    .setNegativeButton(null)
                    .create()
                    .show()
                return@launchSingleSelection
            }
            val inputStream = if (AndroidUtils.isDocUri(uri)) {
                context.contentResolver.openInputStream(uri)
            } else {
                Files.newInputStream(Paths.get(path))
            }
            if (JavaManager.javaList.any { it.name == fileName }) {
                FCLAlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.import_java_overwrite_wrong))
                    .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                    .setPositiveButton(context.getString(R.string.button_overwrite)) {
                        doImport(inputStream, fileName)
                    }
                    .setNegativeButton(context.getString(R.string.button_cancel)) {
                        inputStream?.close()
                    }
                    .create()
                    .show()
            } else {
                doImport(inputStream, fileName)
            }
        }
    }

    private fun doImport(inputStream: InputStream?, fileName: String) {
        loadingState.value = true
        CompletableFuture.supplyAsync {
            try {
                val dest = File(FCLPath.JAVA_PATH, fileName)
                JavaManager.remove(fileName)
                RuntimeUtils.uncompressTarXZ(inputStream, dest)
                RuntimeUtils.patchJava(context, dest.absolutePath)
            } catch (_: Throwable) {
                return@supplyAsync false
            } finally {
                inputStream?.close()
            }
            return@supplyAsync true
        }.thenApplyAsync {
            if (it) {
                return@thenApplyAsync checkElfIsAndroid(
                    File(FCLPath.JAVA_PATH, fileName).resolve("bin/java")
                )
            }
            return@thenApplyAsync false
        }.thenAcceptAsync {
            Schedulers.androidUIThread().execute {
                loadingState.value = false
                val javaDir = File(FCLPath.JAVA_PATH, fileName)
                if (it) {
                    addJava(javaDir)
                } else {
                    FCLAlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.import_java_error))
                        .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                        .setPositiveButton(context.getString(R.string.mod_check_continue)) {
                            addJava(javaDir)
                        }
                        .setNegativeButton(context.getString(R.string.button_cancel)) {
                            FileUtils.deleteDirectory(javaDir)
                        }
                        .create()
                        .show()
                }
            }
        }
    }

    private fun addJava(javaDir: File) {
        if (JavaManager.addToJavaVersion(javaDir)) {
            refresh()
        } else {
            FileUtils.deleteDirectory(javaDir)
            FCLAlertDialog.Builder(context)
                .setMessage(context.getString(R.string.import_java_error_not_valid))
                .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                .setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive)) {
                }
                .create()
                .show()
        }
    }

    private fun refresh() {
        versionList.clear()
        versionList.addAll(JavaManager.javaList.filter { !it.isAuto }
            .sortedWith(Comparator { v1, v2 ->
                val parts1 = v1.versionName.split('.').map { it.toIntOrNull() ?: 0 }
                val parts2 = v2.versionName.split('.').map { it.toIntOrNull() ?: 0 }
                val maxLength = maxOf(parts1.size, parts2.size)
                for (i in 0 until maxLength) {
                    val p1 = parts1.getOrElse(i) { 0 }
                    val p2 = parts2.getOrElse(i) { 0 }
                    if (p1 != p2) return@Comparator p1.compareTo(p2)
                }
                0
            }))
        versionListState.value = versionList.toList()
    }
}
