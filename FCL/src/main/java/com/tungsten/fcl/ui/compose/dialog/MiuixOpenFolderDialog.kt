package com.tungsten.fcl.ui.compose.dialog

import android.annotation.SuppressLint
import android.widget.ListView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.adapter.FileBrowserAdapter
import com.tungsten.fcllibrary.browser.adapter.FileBrowserListener
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Miuix 版游戏内打开文件夹弹窗（3.2 批 2，对应 control/OpenFolderDialog + dialog_open_folder）。
 *
 * 行为对齐：标题 + 当前路径 + 文件列表（FileBrowser/FileBrowserAdapter 复杂 View 组件，
 * 按 component-mapping「保留原生 + AndroidView 包装」策略托管，进目录逻辑不变）；
 * 返回上级按钮仅在离开初始目录时显示；导入按钮拉起多选并逐个复制进当前目录，
 * 导入期间三按钮禁用并显示进度（可取消，job.cancel）；单文件失败弹 FCLAlertDialog
 * （dialog_alert 不在本批范围）并继续；关闭 dismiss；遗留未显式 setCancelable，默认 true 一致。
 *
 * 运行于游戏内（JVMActivity，AppCompatActivity），AppCompatDialog + ComposeView 可用。
 */
class MiuixOpenFolderDialog(
    private val activity: FCLActivity,
    private val initialPath: String,
) : FCLComposeDialog(activity) {

    private var internalPath: String = initialPath
    private var listView: ListView? = null
    private var job: Job? = null

    private val pathState = mutableStateOf(initialPath)
    private val backVisibleState = mutableStateOf(false)
    private val progressState = mutableStateOf(false)
    private val buttonsEnabledState = mutableStateOf(true)

    init {
        setDialogContent {
            val buttonsEnabled = buttonsEnabledState.value
            FCLDialogCard(
                title = stringResource(R.string.ingame_folder_browse),
                scrollable = false,
                buttons = buildList {
                    add(FCLDialogButton(
                        text = stringResource(R.string.ingame_folder_import),
                        enabled = buttonsEnabled,
                        onClick = { onImport() },
                    ))
                    add(FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.close),
                        enabled = buttonsEnabled,
                        onClick = { dismiss() },
                    ))
                    if (backVisibleState.value) {
                        add(FCLDialogButton(
                            text = stringResource(R.string.button_back),
                            enabled = buttonsEnabled,
                            onClick = { onBack() },
                        ))
                    }
                },
            ) {
                Text(
                    text = pathState.value,
                    style = MiuixTheme.textStyles.footnote1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp),
                ) {
                    AndroidView(
                        factory = { ctx ->
                            ListView(ctx).also {
                                listView = it
                                refreshFiles()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (progressState.value) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            CircularProgressIndicator()
                            TextButton(
                                text = stringResource(R.string.button_cancel),
                                onClick = {
                                    job?.cancel()
                                    job = null
                                    progressState.value = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    private fun refreshFiles() {
        pathState.value = internalPath
        listView?.adapter = FileBrowserAdapter(
            context,
            FileBrowser.Builder(context)
                .setLibMode(LibMode.FILE_BROWSER)
                .setInitDir(internalPath)
                .create(),
            File(internalPath).toPath(),
            ArrayList(),
            object : FileBrowserListener {
                override fun onEnterDir(path: String) {
                    internalPath = path
                    refreshFiles()
                }

                override fun onSelect(adapter: FileBrowserAdapter, path: String) {}
            }
        )

        val currentPath = Paths.get(internalPath).normalize().toAbsolutePath()
        val rootPath = Paths.get(initialPath).normalize().toAbsolutePath()
        backVisibleState.value = currentPath != rootPath
    }

    private fun onImport() {
        val targetDir = internalPath
        activity.fileLauncher.launchMultiSelection(targetDir, null) {
            importFiles(it, targetDir)
        }
    }

    private fun onBack() {
        val root = Paths.get(initialPath).normalize().toAbsolutePath()
        val current = Paths.get(internalPath).normalize().toAbsolutePath()
        val parent = current.parent
        if (parent != null && parent.normalize().startsWith(root)) {
            internalPath = parent.toString()
            refreshFiles()
        }
    }

    @SuppressLint("Recycle")
    private fun importFiles(paths: List<String>, targetDir: String) {
        job = activity.lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                buttonsEnabledState.value = false
                progressState.value = true
            }

            try {
                paths.forEach { path ->
                    ensureActive()
                    val uri = path.toUri()
                    val (inputStream, name) = if (AndroidUtils.isDocUri(uri)) {
                        context.contentResolver.openInputStream(uri) to AndroidUtils.getFileName(
                            context,
                            uri
                        )
                    } else {
                        Files.newInputStream(Paths.get(path)) to File(path).name
                    }

                    runCatching {
                        inputStream?.use { stream ->
                            val outPath = Paths.get(targetDir, name)
                            Files.newOutputStream(outPath).use { out ->
                                stream.copyTo(out)
                            }

                            withContext(Dispatchers.Main) {
                                refreshFiles()
                            }
                        }
                    }.onFailure { e ->
                        withContext(Dispatchers.Main) {
                            FCLAlertDialog.Builder(activity)
                                .setMessage(
                                    activity.getString(
                                        R.string.ingame_folder_import_failed,
                                        name,
                                        e.stackTraceToString()
                                    )
                                )
                                .setPositiveButton(
                                    activity.getString(com.tungsten.fcllibrary.R.string.close)
                                ) {}.create().show()
                        }
                    }
                }
            } catch (_: CancellationException) {
            } catch (_: Exception) {
            }

            withContext(Dispatchers.Main) {
                buttonsEnabledState.value = true
                progressState.value = false
            }

            job = null
        }
    }
}
