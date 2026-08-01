package com.tungsten.fcl.ui.setting.compose

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.upgrade.UpdateChecker
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import java.io.File
import java.io.IOException

/**
 * 启动器设置页宿主事件处理器（小步骤 3.1）：承接 [LauncherSettingEvent] 中需要
 * Activity/MainActivity 能力的一次性副作用。
 *
 * 本类代码基本是 LauncherSettingPage.java onClick/onCheckedChanged 中对应分支的
 * 原样搬运（文件选择回调、权限请求、Window flags、MainActivity 联动），保证行为等价；
 * 弹窗优先走 LegacyBridge.requestAlertDialog（Compose 前台时以 Miuix 弹窗呈现），
 * 槽位被占时回退遗留 FCLAlertDialog（bridge-api.md §2.3）。
 */
object LauncherSettingHost {

    fun handle(context: Context, event: LauncherSettingEvent) {
        when (event) {
            LauncherSettingEvent.CheckUpdate -> checkUpdate(context)
            LauncherSettingEvent.RequestAudioPermission -> requestAudioPermission(context)
            LauncherSettingEvent.PickBackgroundLight -> pickBackgroundImage(context, light = true)
            LauncherSettingEvent.PickBackgroundDark -> pickBackgroundImage(context, light = false)
            is LauncherSettingEvent.ResetBackgroundImage -> resetBackgroundImage(context, event.light)
            LauncherSettingEvent.PickLiveBackground -> pickLiveBackground(context)
            LauncherSettingEvent.ResetLiveBackground -> resetLiveBackground()
            LauncherSettingEvent.PickCursor -> pickImageFile(context, prefix = "cursor")
            LauncherSettingEvent.PickMenuIcon -> pickImageFile(context, prefix = "menu_icon")
            is LauncherSettingEvent.ApplyIgnoreNotch -> applyIgnoreNotch(context, event.enabled)
            LauncherSettingEvent.SyncLiveBackgroundVolume ->
                (context as? MainActivity)?.setLiveBackgroundVolume()

            is LauncherSettingEvent.ShowAlert ->
                showAlert(context, event.message, event.isError)

            // 对齐 LauncherSettingPage.java:524-537：先用页面 Activity 上下文 setLanguage
            // （当前 Activity 资源配置即时切到新语言），再以新语言弹"重启生效"提示。
            LauncherSettingEvent.ShowRestartHint -> {
                LocaleUtils.setLanguage(context)
                showAlert(context, context.getString(R.string.message_warn_restart_after_change))
            }
        }
    }

    /** 对齐 LauncherSettingPage.java:244-255（isChecking 防重入 + 失败弹窗）。 */
    private fun checkUpdate(context: Context) {
        if (UpdateChecker.getInstance().isChecking()) return
        UpdateChecker.getInstance().checkManually(context)
            .whenComplete(Schedulers.androidUIThread()) { e: Exception? ->
                if (e != null) {
                    showAlert(
                        context,
                        context.getString(R.string.update_check_failed) + "\n" + e,
                        isError = true,
                    )
                }
            }.start()
    }

    /** 对齐 LauncherSettingPage.java:287-302。 */
    private fun requestAudioPermission(context: Context) {
        val activity = context as? MainActivity ?: return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                activity.permissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(intent)
                } catch (ignored: Exception) {
                }
            }
        }
    }

    /** 对齐 LauncherSettingPage.java:360-373（背景图选择 + SAF 拷贝 + applyAndSave）。 */
    private fun pickBackgroundImage(context: Context, light: Boolean) {
        val activity = context as? MainActivity ?: return
        activity.fileLauncher.launchSingleSelection(null, listOf(".png", ".jpg", ".jpeg")) { files ->
            var path = files[0]
            val uri = Uri.parse(path)
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(activity, uri, File(FCLPath.CACHE_DIR))
            }
            ThemeEngine.getInstance().applyAndSave(
                context,
                activity.binding.background,
                if (light) path else null,
                if (light) null else path,
            )
        }
    }

    /** 对齐 LauncherSettingPage.java:479-494（子线程删文件，失败 Toast，UI 线程 applyAndSave）。 */
    private fun resetBackgroundImage(context: Context, light: Boolean) {
        val activity = context as? MainActivity ?: return
        Thread {
            val file = File(if (light) FCLPath.LT_BACKGROUND_PATH else FCLPath.DK_BACKGROUND_PATH)
            if (!file.delete() && file.exists()) {
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, context.getString(R.string.message_failed), Toast.LENGTH_SHORT).show()
                }
            }
            Schedulers.androidUIThread().execute {
                ThemeEngine.getInstance().applyAndSave(context, activity.binding.background, null, null)
            }
        }.start()
    }

    /** 对齐 LauncherSettingPage.java:374-390（视频背景选择 + setupLiveBackground）。 */
    private fun pickLiveBackground(context: Context) {
        val activity = context as? MainActivity ?: return
        activity.fileLauncher.launchSingleSelection(null, listOf(".mp4")) { files ->
            val path = files[0]
            val uri = Uri.parse(path)
            if (AndroidUtils.isDocUri(uri)) {
                AndroidUtils.copyFile(activity, uri, File(FCLPath.LIVE_BACKGROUND_PATH))
            } else {
                try {
                    FileUtils.copyFile(File(path), File(FCLPath.LIVE_BACKGROUND_PATH))
                } catch (ignore: IOException) {
                }
            }
            activity.setupLiveBackground()
        }
    }

    /** 对齐 LauncherSettingPage.java:448-454。 */
    private fun resetLiveBackground() {
        try {
            FileUtils.forceDelete(File(FCLPath.LIVE_BACKGROUND_PATH))
            MainActivity.getInstance().setupLiveBackground()
        } catch (ignore: IOException) {
        }
    }

    /** 对齐 LauncherSettingPage.java:391-438（指针图/菜单图标选择：先删旧再拷贝改名）。
     *  @param prefix "cursor" 或 "menu_icon"
     */
    private fun pickImageFile(context: Context, prefix: String) {
        val activity = context as? MainActivity ?: return
        activity.fileLauncher.launchSingleSelection(null, listOf(".png", ".gif")) { files ->
            val path = files[0]
            val uri = Uri.parse(path)
            val fileName = AndroidUtils.getFileName(context, uri)
            val type = if (fileName.endsWith(".gif")) "gif" else "png"
            deleteImageFiles(prefix)
            if (AndroidUtils.isDocUri(uri)) {
                AndroidUtils.copyFile(activity, uri, File(FCLPath.FILES_DIR, "$prefix.$type"))
            } else {
                try {
                    FileUtils.copyFile(File(path), File(FCLPath.FILES_DIR, "$prefix.$type"))
                } catch (ignore: IOException) {
                }
            }
        }
    }

    private fun deleteImageFiles(prefix: String) {
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(FCLPath.FILES_DIR, "$prefix.png"))
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(FCLPath.FILES_DIR, "$prefix.gif"))
        } catch (ignored: IOException) {
        }
    }

    /** 对齐 LauncherSettingPage.java:556-558。 */
    private fun applyIgnoreNotch(context: Context, enabled: Boolean) {
        val activity = context as? MainActivity ?: return
        ThemeEngine.getInstance().applyAndSave(context, activity.window, enabled)
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        )
    }

    /**
     * 通用提示弹窗：优先 LegacyBridge.requestAlertDialog（Miuix WindowDialog），
     * 槽位占用时回退遗留 FCLAlertDialog（对齐各调用点的单按钮形态）。
     */
    private fun showAlert(context: Context, message: String, isError: Boolean = false) {
        val positive = context.getString(com.tungsten.fcllibrary.R.string.dialog_positive)
        val accepted = LegacyBridge.requestAlertDialog(null, message, positive, null, null)
        if (!accepted) {
            val builder = FCLAlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setAlertLevel(
                if (isError) FCLAlertDialog.AlertLevel.ALERT else FCLAlertDialog.AlertLevel.INFO,
            )
            builder.setMessage(message)
            builder.setNegativeButton(positive, null)
            builder.create().show()
        }
    }
}
