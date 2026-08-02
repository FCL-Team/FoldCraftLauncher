package com.tungsten.fcl.ui.download.compose

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.dialog.ComposeDialogs
import com.tungsten.fcl.ui.compose.dialog.MiuixDownloadAddonDialog
import com.tungsten.fcl.ui.download.common.DownloadAddonDialog
import com.tungsten.fcl.ui.download.common.RemoteModVersionPage
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.NetworkUtils
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File
import java.util.concurrent.CancellationException

/**
 * 下载动作桥接（业务零重写）：
 * - [callbackFor] 对齐 DownloadPage 构造器按 pageId 分发的 DownloadCallback（:229-245）；
 * - [downloadAddon] 对齐 DownloadPage.download 静态方法（:342-386）；
 * - [saveAs] 对齐 RemoteModVersionPage.saveAs（:69-87）。
 *
 * 与遗留实现的差异仅两处且均沿用既有迁移开关：
 * TaskDialog → MiuixTaskDialog（USE_COMPOSE_TASK_DIALOG）、
 * DownloadAddonDialog → MiuixDownloadAddonDialog（USE_COMPOSE_DOWNLOAD_ADDON）。
 */
object RemoteModActions {

    /** 各 Tab 的下载回调（WORLD 为 null → 点击版本项走"另存为"，对齐基类构造 default 分支）。 */
    fun callbackFor(
        context: Context,
        parent: FCLUILayout,
        tab: DownloadTab,
    ): RemoteModVersionPage.DownloadCallback? = when (tab) {
        DownloadTab.MODPACK -> RemoteModVersionPage.DownloadCallback { profile, _, file ->
            Versions.downloadModpackImpl(context, parent, profile, file)
        }

        DownloadTab.MOD -> RemoteModVersionPage.DownloadCallback { profile, version, file ->
            downloadAddon(context, profile, version, file, "mods")
        }

        DownloadTab.RESOURCE_PACK -> RemoteModVersionPage.DownloadCallback { profile, version, file ->
            downloadAddon(context, profile, version, file, "resourcepacks")
        }

        DownloadTab.SHADER_PACK -> RemoteModVersionPage.DownloadCallback { profile, version, file ->
            downloadAddon(context, profile, version, file, "shaderpacks")
        }

        DownloadTab.WORLD -> null
    }

    /** 版本项点击分发（对齐 RemoteModVersionPage.download :61-67）。 */
    fun downloadOrSaveAs(
        context: Context,
        parent: FCLUILayout,
        tab: DownloadTab,
        version: Profile.ProfileVersion,
        file: RemoteMod.Version,
    ) {
        val callback = callbackFor(context, parent, tab)
        if (callback == null) {
            saveAs(context, file)
        } else {
            callback.download(version.profile, version.version, file)
        }
    }

    /** 附加内容下载（改名确认 → 任务进度 → 成功/取消/失败提示），对齐 DownloadPage.download。 */
    fun downloadAddon(
        context: Context,
        profile: Profile,
        version: String?,
        file: RemoteMod.Version,
        subdirectoryName: String,
    ) {
        val targetVersion = version ?: profile.selectedVersion
        val runDirectory =
            if (profile.repository.hasVersion(targetVersion)) {
                profile.repository.getRunDirectory(targetVersion).toPath()
            } else {
                profile.repository.baseDirectory.toPath()
            }

        val addonCallback = DownloadAddonDialog.Callback { name ->
            val dest = runDirectory.resolve(subdirectoryName).resolve(name)
            Schedulers.androidUIThread().execute {
                val executor = Task.composeAsync {
                    FileDownloadTask(NetworkUtils.toURL(file.file.url), dest.toFile())
                        .apply { setName(file.name) }
                }.whenComplete(Schedulers.androidUIThread()) { exception ->
                    if (exception != null) {
                        if (exception is CancellationException) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.message_cancelled),
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            val builder = FCLAlertDialog.Builder(context)
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                            builder.setCancelable(false)
                            builder.setTitle(context.getString(R.string.install_failed_downloading))
                            builder.setMessage(DownloadProviders.localizeErrorMessage(context, exception))
                            builder.setNegativeButton(
                                context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                                null,
                            )
                            builder.create().show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.install_success),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }.executor()
                if (MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG) {
                    val taskDialog = MiuixTaskDialog(context)
                    taskDialog.setTitle(context.getString(R.string.message_downloading))
                    taskDialog.setExecutor(executor)
                    taskDialog.show()
                } else {
                    val taskDialog = TaskDialog(
                        context,
                        TaskCancellationAction(AppCompatDialog::dismiss),
                    )
                    taskDialog.setTitle(context.getString(R.string.message_downloading))
                    taskDialog.setExecutor(executor)
                    taskDialog.show()
                }
                executor.start()
            }
        }
        if (ComposeDialogs.USE_COMPOSE_DOWNLOAD_ADDON) {
            MiuixDownloadAddonDialog(context, file.file.filename, addonCallback).show()
        } else {
            DownloadAddonDialog(context, file.file.filename, addonCallback).show()
        }
    }

    /** 另存为（目录选择 → 任务进度下载），对齐 RemoteModVersionPage.saveAs。 */
    fun saveAs(context: Context, file: RemoteMod.Version) {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true) { files ->
            val folder = files?.get(0) ?: return@launchSingleSelection
            Schedulers.androidUIThread().execute {
                val executor = Task.composeAsync {
                    FileDownloadTask(
                        NetworkUtils.toURL(file.file.url),
                        File(folder, file.file.filename),
                        file.file.integrityCheck,
                    ).apply { setName(file.name) }
                }.executor()
                if (MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG) {
                    val dialog = MiuixTaskDialog(context)
                    dialog.setTitle(context.getString(R.string.message_downloading))
                    dialog.setExecutor(executor)
                    dialog.show()
                } else {
                    val dialog = TaskDialog(
                        context,
                        TaskCancellationAction(AppCompatDialog::dismiss),
                    )
                    dialog.setTitle(context.getString(R.string.message_downloading))
                    dialog.setExecutor(executor)
                    dialog.show()
                }
                executor.start()
            }
        }
    }
}
