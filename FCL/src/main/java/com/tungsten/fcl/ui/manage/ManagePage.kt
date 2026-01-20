package com.tungsten.fcl.ui.manage

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.util.AnimUtil
import com.mio.util.AnimUtil.Companion.interpolator
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.PageManageVersionBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.ProgressDialog
import com.tungsten.fcl.ui.UIManager.Companion.instance
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fcl.ui.manage.adapter.ManageItemAdapter
import com.tungsten.fcl.ui.manage.item.ManageItem
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.Pair.pair
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.HttpRequest
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.util.logging.Level

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
            left.adapter = ManageItemAdapter(
                context,
                listOf(
                    ManageItem(R.drawable.ic_baseline_cloud_upload_24, R.string.upload_game_log) {
                        uploadGameLog()
                    },
                    ManageItem(R.drawable.ic_baseline_cloud_upload_24, R.string.upload_fcl_log) {
                        uploadFCLLog()
                    },
                    ManageItem(R.drawable.ic_baseline_script_24, R.string.folder_fcl_log) {
                        onBrowse(
                            FCLPath.LOG_DIR
                        )
                    },
                    ManageItem(R.drawable.ic_baseline_videogame_asset_24, R.string.folder_game) {
                        onBrowse("")
                    },
                    ManageItem(R.drawable.ic_outline_extension_24, R.string.folder_mod) {
                        onBrowse("mods")
                    },
                    ManageItem(R.drawable.ic_baseline_settings_24, R.string.folder_config) {
                        onBrowse("config")
                    },
                    ManageItem(R.drawable.ic_baseline_texture_24, R.string.folder_resourcepacks) {
                        onBrowse("resourcepacks")
                    },
                    ManageItem(R.drawable.ic_baseline_application_24, R.string.folder_shaderpacks) {
                        onBrowse("shaderpacks")
                    },
                    ManageItem(R.drawable.ic_baseline_screenshot_24, R.string.folder_screenshots) {
                        onBrowse("screenshots")
                    },
                    ManageItem(R.drawable.ic_baseline_earth_24, R.string.folder_saves) {
                        onBrowse("saves")
                    }

                ))
            right.layoutManager = LinearLayoutManager(context)
            right.adapter = ManageItemAdapter(
                context,
                listOf(
                    ManageItem(R.drawable.ic_baseline_update_24, R.string.version_update) {
                        if (!currentVersionUpgradable.get()) {
                            AnimUtil.playTranslationX(it, 500, 0f, 50f, -50f, 0f)
                                .interpolator(OvershootInterpolator()).start()
                        } else {
                            updateGame()
                        }
                    },
                    ManageItem(R.drawable.ic_baseline_edit_24, R.string.version_manage_rename) {
                        rename()
                    },
                    ManageItem(
                        R.drawable.ic_baseline_content_copy_24,
                        R.string.version_manage_duplicate
                    ) {
                        duplicate()
                    },
                    ManageItem(R.drawable.ic_baseline_output_24, R.string.modpack_export) {
                        export()
                    },
                    ManageItem(
                        R.drawable.ic_baseline_list_24,
                        R.string.version_manage_redownload_assets_index
                    ) {
                        redownloadAssetIndex()
                    },
                    ManageItem(
                        R.drawable.ic_baseline_delete_24,
                        R.string.version_manage_remove_libraries
                    ) {
                        clearLibraries()
                    },
                    ManageItem(
                        R.drawable.ic_baseline_delete_24,
                        R.string.version_manage_clean
                    ) {
                        clearJunkFiles()
                    }
                ))
        }
    }

    private fun onBrowse(path: String) {
        val root =
            if (path.startsWith("/")) File(path) else if (path.isEmpty()) profile.repository.getRunDirectory(
                version
            ) else File(
                profile.repository.getRunDirectory(version), path
            )
        if (!root.exists()) {
            root.mkdirs()
        }
        FileBrowser.Builder(context)
            .setInitDir(root.absolutePath)
            .setLibMode(LibMode.FILE_BROWSER)
            .create()
            .browse(activity, RequestCodes.BROWSE_DIR_CODE, null)
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
        try {
            val logs = Logging.getLogs()
            if (logs.isNullOrEmpty()) {
                Toast.makeText(context, "No FCL logs found", Toast.LENGTH_SHORT).show()
                return
            }
            uploadLog(logs)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to get FCL logs: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun uploadGameLog() {
        val logFile = File(FCLPath.LOG_DIR, "latest_game.log")
        if (!logFile.exists()) {
            Toast.makeText(context, "No game logs found", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            if (logFile.length() > 5 * 1024 * 1024) {
                throw Exception("Log file is too large")
            }
            val logs = FileUtils.readText(logFile)
            uploadLog(logs)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to read log: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadLog(content: String) {
        val progress = ProgressDialog(context)
        val url = LocaleUtils.getLogUploadApiUrl(context)
        activity.lifecycleScope.launch(Dispatchers.Default) {
            val result = runCatching {
                HttpRequest.POST(url)
                    .form(pair("content", content))
                    .string
            }
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    progress.dismiss()
                    try {
                        val response = JSONObject(it)
                        if (response.getBoolean("success")) {
                            val logUrl = response.getString("url")
                            AndroidUtils.copyText(context, logUrl)
                            FCLAlertDialog.Builder(context)
                                .setMessage(
                                    context.getString(
                                        com.tungsten.fcllibrary.R.string.upload_success,
                                        logUrl
                                    )
                                )
                                .setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive)) {
                                    AndroidUtils.openLink(context, logUrl)
                                }
                                .create().show()
                        } else {
                            Logging.LOG.log(
                                Level.SEVERE,
                                "Failed to upload log",
                                response.getString("error")
                            )
                            Toast.makeText(
                                context,
                                context.getString(
                                    com.tungsten.fcllibrary.R.string.upload_failed,
                                    response.getString("error")
                                ),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (ex: Exception) {
                        Logging.LOG.log(Level.SEVERE, "Failed to upload log", ex)
                        Toast.makeText(
                            context,
                            context.getString(
                                com.tungsten.fcllibrary.R.string.upload_failed,
                                ex.toString()
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.onFailure {
                    progress.dismiss()
                    Logging.LOG.log(Level.SEVERE, "Failed to upload log", it)
                    Toast.makeText(
                        context,
                        context.getString(
                            com.tungsten.fcllibrary.R.string.upload_failed,
                            it.toString()
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val profile: Profile
        get() = instance.manageUI.profile

    val version: String
        get() = instance.manageUI.version

}
