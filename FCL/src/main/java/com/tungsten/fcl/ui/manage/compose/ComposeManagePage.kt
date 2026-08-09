package com.tungsten.fcl.ui.manage.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mio.util.showErrorDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.util.uploadLog
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

/**
 * 迁移期 Compose 版本管理页容器（小步骤 3.3）：ManagePage.kt 的 Compose 化承接，
 * 沿用 3.1 ComposeSettingPage 范式的 FCLCommonPage 壳。
 *
 * 与 3.1 的差异：本页状态只有一个 Boolean（当前版本是否为可更新的整合包，
 * 对齐 ManagePage.currentVersionUpgradable），业务动作全部是遗留静态调用
 * （Versions 静态方法 / FileBrowser / uploadLog），引入 ViewModel 只是空转，
 * 因此状态流与动作实现由本页面类直接持有（经 [ManageActions] 传给 [ManageScreen]）。
 *
 * 弹窗策略：清理 libraries/logs 的确认弹窗与进度弹窗走 3.2 的 Miuix 命令式弹窗
 * （[FCLDialogs.showAlert] / [FCLDialogs.showProgress]，对应遗留 FCLAlertDialog +
 * FCLLibrary ProgressDialog）；重命名/复制/资源重建弹窗在遗留 Versions.* 内部
 * 已接 3.2 Miuix 版本（业务零重写）；上传日志错误提示沿用 DialogUtil.showErrorDialog。
 */
class ComposeManagePage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
) : FCLCommonPage(context, id, parent, R.layout.page_compose_container), VersionLoadable {

    /** 对齐 ManagePage.currentVersionUpgradable（loadVersion 时由 repository.isModpack 驱动）。 */
    private val currentVersionUpgradable = MutableStateFlow(false)

    private var profile: Profile? = null
    private var version: String? = null

    private var composeInstalled = false

    override fun onCreate() {
        // 父类构造期间回调：仅完成 contentView 挂载，ComposeView 推迟到首次 onStart 安装。
        super.onCreate()
    }

    override fun onStart() {
        installComposeViewIfNeeded()
        super.onStart()
    }

    override fun loadVersion(profile: Profile, version: String) {
        this.profile = profile
        this.version = version
        currentVersionUpgradable.value = profile.repository.isModpack(version)
    }

    private fun installComposeViewIfNeeded() {
        if (composeInstalled) return
        composeInstalled = true
        val container = findViewById<FrameLayout>(R.id.compose_container)
        val composeView = LegacyBridge.createComposeView(context) {
            ManageScreen(
                upgradableFlow = currentVersionUpgradable,
                actions = manageActions,
            )
            LegacyBridge.LegacyDialogHost()
        }
        container.addView(
            composeView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

    // ---------- 菜单动作（逐条对齐 ManagePage.kt :151-255，业务逻辑零重写） ----------

    private val manageActions = object : ManageActions {
        override fun onUploadLog() = uploadLatestLog()
        override fun onBrowse(path: String) = browse(path)
        override fun onUpdateGame() {
            val p = profile ?: return
            val v = version ?: return
            Versions.updateVersion(context, parent, p, v)
        }

        override fun onRename() {
            val p = profile ?: return
            val v = version ?: return
            // 对齐 ManagePage.rename()：重命名成功后回写 preferredVersionName
            Versions.renameVersion(context, p, v).thenApply { newName ->
                LegacyBridge.uiManager()?.manageUI?.let { it.preferredVersionName = newName }
            }
        }

        override fun onDuplicate() {
            val p = profile ?: return
            val v = version ?: return
            Versions.duplicateVersion(context, p, v)
        }

        override fun onExport() {
            val p = profile ?: return
            val v = version ?: return
            Versions.exportVersion(context, parent, p, v)
        }

        override fun onRedownloadAssets() {
            val p = profile ?: return
            val v = version ?: return
            Versions.updateGameAssets(context, p, v)
        }

        override fun onClearLibraries() = clearLibraries()
        override fun onClearJunkFiles() = clearJunkFiles()
    }

    /** 对齐 ManagePage.onBrowse(:151-166)：目录不存在先创建，再走 FCLLibrary 文件浏览器。 */
    private fun browse(path: String) {
        val p = profile ?: return
        val v = version ?: return
        val root =
            if (path.startsWith("/")) File(path)
            else if (path.isEmpty()) p.repository.getRunDirectory(v)
            else File(p.repository.getRunDirectory(v), path)
        if (!root.exists()) {
            root.mkdirs()
        }
        FileBrowser.Builder(context)
            .setInitDir(root.absolutePath)
            .setLibMode(LibMode.FILE_BROWSER)
            .create()
            .browse(activity)
    }

    /** 对齐 ManagePage.clearLibraries(:172-195)：确认弹窗 + 后台删除 + 进度弹窗。 */
    private fun clearLibraries() {
        val p = profile ?: return
        FCLDialogs.showAlert(
            context,
            null,
            String.format(context.getString(R.string.version_manage_remove_confirm), "libraries"),
            negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
            onResult = { accepted ->
                if (accepted) {
                    val progress = FCLDialogs.showProgress(context)
                    Task.runAsync {
                        FileUtils.deleteDirectoryQuietly(
                            File(p.repository.baseDirectory, "libraries")
                        )
                    }.whenComplete(Schedulers.androidUIThread()) { _: Exception? ->
                        progress.dismiss()
                    }.start()
                }
            },
        )
    }

    /** 对齐 ManagePage.clearJunkFiles(:197-218)：确认弹窗 + 后台清理 + 进度弹窗。 */
    private fun clearJunkFiles() {
        val p = profile ?: return
        val v = version ?: return
        FCLDialogs.showAlert(
            context,
            null,
            String.format(context.getString(R.string.version_manage_remove_confirm), "logs"),
            negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
            onResult = { accepted ->
                if (accepted) {
                    val progress = FCLDialogs.showProgress(context)
                    Task.runAsync {
                        Versions.cleanVersion(p, v)
                    }.whenComplete(Schedulers.androidUIThread()) { _: Exception? ->
                        progress.dismiss()
                    }.start()
                }
            },
        )
    }

    /** 对齐 ManagePage.uploadLatestLog(:239-255)：不存在/>5MB/读取失败三种错误提示。 */
    private fun uploadLatestLog() {
        val logFile = File(FCLPath.LOG_DIR, "latest_game.log")
        if (!logFile.exists()) {
            showErrorDialog(context, R.string.log_not_found)
            return
        }
        try {
            if (logFile.length() > 5 * 1024 * 1024) {
                showErrorDialog(context, R.string.log_too_large)
                return
            }
            val logs = FileUtils.readText(logFile)
            uploadLog(activity, logs)
        } catch (e: Exception) {
            showErrorDialog(context, "Failed to read log: ${e.message}")
        }
    }
}
