package com.tungsten.fcl.ui.manage

import android.content.Context
import android.content.res.ColorStateList
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.util.AnimUtil
import com.mio.util.AnimUtil.Companion.interpolator
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.PageManageVersionBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.ProgressDialog
import com.tungsten.fcllibrary.util.LocaleUtils
import com.tungsten.fcl.ui.UIManager.Companion.instance
import android.widget.Toast
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.HttpRequest
import androidx.core.util.Pair
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.ui.manage.adapter.ManageItemAdapter
import com.tungsten.fcl.ui.manage.item.ManageItem
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File

class ManagePage(context: Context, id: Int, parent: FCLUILayout, resId: Int) :
    FCLCommonPage(context, id, parent, resId), VersionLoadable {
    private val currentVersionUpgradable: BooleanProperty = SimpleBooleanProperty()

    private lateinit var binding: PageManageVersionBinding

    init {
        create()
    }

    override fun refresh(vararg param: Any): Task<*>? {
        return null
    }

    override fun loadVersion(profile: Profile, version: String) {
        currentVersionUpgradable.set(profile.repository.isModpack(version))
    }

    private fun create() {
        binding = PageManageVersionBinding.bind(contentView).apply {
            ThemeEngine.getInstance().registerEvent(left) {
                left.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()), intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                )
            }
            ThemeEngine.getInstance().registerEvent(right) {
                right.backgroundTintList = ColorStateList(
                    arrayOf(intArrayOf()), intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                )
            }
            left.layoutManager = LinearLayoutManager(context)
            left.adapter = ManageItemAdapter(context, mutableListOf<ManageItem>().apply {
                add(ManageItem(R.drawable.ic_baseline_script_24, R.string.folder_fcl_log) {
                    onBrowse(
                        FCLPath.LOG_DIR
                    )
                })
                add(ManageItem(R.drawable.ic_baseline_cloud_upload_24, R.string.upload_fcl_log) {
                    uploadFCLLog()
                })
                add(ManageItem(R.drawable.ic_baseline_videogame_asset_24, R.string.folder_game) {
                    onBrowse("")
                })
                add(ManageItem(R.drawable.ic_outline_extension_24, R.string.folder_mod) {
                    onBrowse("mods")
                })
                add(ManageItem(R.drawable.ic_baseline_settings_24, R.string.folder_config) {
                    onBrowse("config")
                })
                add(ManageItem(R.drawable.ic_baseline_texture_24, R.string.folder_resourcepacks) {
                    onBrowse("resourcepacks")
                })
                add(ManageItem(R.drawable.ic_baseline_application_24, R.string.folder_shaderpacks) {
                    onBrowse("shaderpacks")
                })
                add(ManageItem(R.drawable.ic_baseline_screenshot_24, R.string.folder_screenshots) {
                    onBrowse("screenshots")
                })
                add(ManageItem(R.drawable.ic_baseline_earth_24, R.string.folder_saves) {
                    onBrowse("saves")
                })
                add(ManageItem(R.drawable.ic_baseline_script_24, R.string.folder_log) {
                    onBrowse("logs")
                })
                add(ManageItem(R.drawable.ic_baseline_cloud_upload_24, R.string.upload_game_log) {
                    uploadGameLog()
                })
            })
            right.layoutManager = LinearLayoutManager(context)
            right.adapter = ManageItemAdapter(context, mutableListOf<ManageItem>().apply {
                add(ManageItem(R.drawable.ic_baseline_update_24, R.string.version_update) {
                    if (!currentVersionUpgradable.get()) {
                        AnimUtil.playTranslationX(it, 500, 0f, 50f, -50f, 0f)
                            .interpolator(OvershootInterpolator()).start()
                    } else {
                        updateGame()
                    }
                })
                add(ManageItem(R.drawable.ic_baseline_edit_24, R.string.version_manage_rename) {
                    rename()
                })
                add(
                    ManageItem(
                        R.drawable.ic_baseline_content_copy_24,
                        R.string.version_manage_duplicate
                    ) {
                        duplicate()
                    })
                add(ManageItem(R.drawable.ic_baseline_output_24, R.string.modpack_export) {
                    export()
                })
                add(
                    ManageItem(
                        R.drawable.ic_baseline_list_24,
                        R.string.version_manage_redownload_assets_index
                    ) {
                        redownloadAssetIndex()
                    })
                add(
                    ManageItem(
                        R.drawable.ic_baseline_delete_24,
                        R.string.version_manage_remove_libraries
                    ) {
                        clearLibraries()
                    })
                add(ManageItem(R.drawable.ic_baseline_delete_24, R.string.version_manage_clean) {
                    clearJunkFiles()
                })
            })
        }
    }

    private fun onBrowse(dir: String) {
        val builder = FileBrowser.Builder(context)
        builder.setLibMode(LibMode.FILE_BROWSER)
        builder.setInitDir(
            if (dir.startsWith("/")) dir else File(
                profile.repository.getRunDirectory(
                    version
                ), dir
            ).absolutePath
        )
        builder.create().browse(activity, RequestCodes.BROWSE_DIR_CODE, null)
    }

    private fun redownloadAssetIndex() {
        Versions.updateGameAssets(context, profile, version)
    }

    private fun clearLibraries() {
        val builder = FCLAlertDialog.Builder(context)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
        builder.setMessage(
            String.format(
                context.getString(R.string.version_manage_remove_confirm),
                "libraries"
            )
        )
        builder.setPositiveButton {
            val progress = ProgressDialog(context)
            Task.runAsync {
                FileUtils.deleteDirectoryQuietly(
                    File(
                        profile.repository.baseDirectory, "libraries"
                    )
                )
            }.whenComplete(Schedulers.androidUIThread()) { _: Exception? ->
                progress.dismiss()
            }.start()
        }
        builder.setNegativeButton(null)
        builder.create().show()
    }

    private fun clearJunkFiles() {
        val builder = FCLAlertDialog.Builder(context)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
        builder.setMessage(
            String.format(
                context.getString(R.string.version_manage_remove_confirm),
                "logs"
            )
        )
        builder.setPositiveButton {
            val progress = ProgressDialog(context)
            Task.runAsync {
                Versions.cleanVersion(
                    profile, version
                )
            }.whenComplete(Schedulers.androidUIThread()) { _: Exception? ->
                progress.dismiss()
            }.start()
        }
        builder.setNegativeButton(null)
        builder.create().show()
    }

    private fun updateGame() {
        Versions.updateVersion(context, parent, profile, version)
    }

    private fun export() {
        Versions.exportVersion(context, parent, profile, version)
    }

    private fun rename() {
        Versions.renameVersion(context, profile, version)
            .thenApply {
                instance.manageUI.preferredVersionName = it
            }
    }

    private fun duplicate() {
        Versions.duplicateVersion(context, profile, version)
    }

    private fun uploadFCLLog() {
        val logs = Logging.getLogs()
        if (logs.isEmpty()) {
            Toast.makeText(context, "No logs found", Toast.LENGTH_SHORT).show()
            return
        }
        uploadLog(logs)
    }

    private fun uploadGameLog() {
        val runDir = profile.repository.getRunDirectory(version)
        val logFile = File(runDir, "logs/latest.log")
        if (!logFile.exists()) {
            Toast.makeText(context, "No game logs found", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val logs = FileUtils.readText(logFile)
            uploadLog(logs)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to read log: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadLog(content: String) {
        val progress = ProgressDialog(context)
        val url = LocaleUtils.getLogUploadApiUrl(context)
        Task.runAsync {
            HttpRequest.POST(url)
                .form(Pair("content", content))
                .execute()
                .asString()
        }.whenComplete(Schedulers.androidUIThread()) { result: String?, e: Exception? ->
            progress.dismiss()
            if (e != null) {
                Toast.makeText(context, context.getString(R.string.upload_failed, e.message), Toast.LENGTH_LONG).show()
            } else if (result != null) {
                try {
                    val response = com.google.gson.JsonParser.parseString(result).asJsonObject
                    if (response.get("success").asBoolean) {
                        val url = response.get("url").asString
                        AndroidUtils.copyToClipboard(context, url)
                        FCLAlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.upload_success, url))
                            .setPositiveButton(context.getString(R.string.dialog_positive)) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                            .create().show()
                    } else {
                        Toast.makeText(context, context.getString(R.string.upload_failed, response.get("error").asString), Toast.LENGTH_LONG).show()
                    }
                } catch (ex: Exception) {
                    Toast.makeText(context, context.getString(R.string.upload_failed, ex.message), Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    val profile: Profile
        get() = instance.manageUI.profile

    val version: String
        get() = instance.manageUI.version

}
