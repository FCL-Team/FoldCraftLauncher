package com.mio.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.JavaManager
import com.mio.ui.adapter.ManageJavaItemAdapter
import com.mio.util.AndroidUtil
import com.tungsten.fcl.FCLApplication
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogManageJavaBinding
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.browser.options.SelectionMode
import com.tungsten.fcllibrary.component.ResultListener
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

@SuppressLint("NotifyDataSetChanged")
class JavaManageDialog(context: Context, val onSelected: (String) -> Unit) : FCLDialog(context) {
    private val versionList = mutableListOf<JavaVersion>()
    private var isLoading = false
    private val binding: DialogManageJavaBinding

    init {
        setCancelable(false)
        window?.setLayout(ConvertUtils.dip2px(context, 500F), ViewGroup.LayoutParams.MATCH_PARENT)
        binding = DialogManageJavaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        refresh()
        binding.recyclerView.adapter =
            ManageJavaItemAdapter(
                context, versionList
            ) { java, isDelete ->
                if (isDelete) {
                    FCLAlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.button_remove_confirm))
                        .setAlertLevel(
                            FCLAlertDialog.AlertLevel.ALERT
                        ).setPositiveButton {
                            JavaManager.remove(java.name)
                            refresh()
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                        }.setNegativeButton(null)
                        .create()
                        .show()
                } else {
                    onSelected.invoke(java.name)
                    dismiss()
                }
            }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.cancel.setOnClickListener { if (!isLoading) dismiss() }
        binding.autoSelect.setOnClickListener {
            if (isLoading) return@setOnClickListener
            onSelected.invoke("Auto")
            dismiss()
        }
        binding.importJava.setOnClickListener {
            if (isLoading) return@setOnClickListener
            val builder = FileBrowser.Builder(getContext())
            builder.setLibMode(LibMode.FILE_CHOOSER)
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION)
            builder.create().browse(
                FCLApplication.getCurrentActivity(),
                RequestCodes.SELECT_JAVA_CODE,
                object : ResultListener.Listener {
                    override fun onActivityResult(
                        requestCode: Int,
                        resultCode: Int,
                        data: Intent?
                    ) {
                        if (requestCode == RequestCodes.SELECT_JAVA_CODE && resultCode == Activity.RESULT_OK && data != null) {
                            val path = FileBrowser.getSelectedFiles(data)[0]
                            val uri = path.toUri()
                            val fileName = if (AndroidUtils.isDocUri(uri)) {
                                AndroidUtils.getFileName(context, uri)
                            } else {
                                File(path).name
                            }
                            if (!fileName.endsWith(".tar.xz")) {
                                FCLAlertDialog.Builder(context)
                                    .setMessage(context.getString(R.string.import_java_wrong_file))
                                    .setAlertLevel(
                                        FCLAlertDialog.AlertLevel.ALERT
                                    )
                                    .setNegativeButton(null)
                                    .create()
                                    .show()
                                return
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
                }
            )
        }
    }

    private fun doImport(inputStream: InputStream?, fileName: String) {
        binding.progress.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        isLoading = true
        CompletableFuture.supplyAsync {
            try {
                val dest = File(FCLPath.JAVA_PATH, fileName)
                JavaManager.remove(fileName)
                RuntimeUtils.uncompressTarXZ(
                    inputStream,
                    dest
                )
                RuntimeUtils.patchJava(context, dest.absolutePath)
            } catch (_: Throwable) {
                return@supplyAsync false
            } finally {
                inputStream?.close()
            }
            return@supplyAsync true
        }.thenApplyAsync {
            if (it) {
                return@thenApplyAsync AndroidUtil.checkElfIsAndroid(
                    File(
                        FCLPath.JAVA_PATH,
                        fileName
                    ).resolve("bin/java")
                )
            }
            return@thenApplyAsync false
        }.thenAcceptAsync {
            Schedulers.androidUIThread().execute {
                isLoading = false
                binding.progress.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                val javaDir = File(
                    FCLPath.JAVA_PATH,
                    fileName
                )
                if (it) {
                    addJava(javaDir)
                } else {
                    FCLAlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.import_java_error))
                        .setAlertLevel(
                            FCLAlertDialog.AlertLevel.ALERT
                        )
                        .setPositiveButton(context.getString(R.string.mod_check_continue)) {
                            addJava(javaDir)
                        }
                        .setNegativeButton(context.getString(R.string.button_cancel)) {
                            FileUtils.deleteDirectory(
                                javaDir
                            )
                        }
                        .create()
                        .show()
                }
            }
        }
    }

    private fun addJava(javaDir: File) {
        if (JavaManager.addToJavaVersion(
                javaDir
            )
        ) {
            refresh()
            binding.recyclerView.adapter?.notifyDataSetChanged()
        } else {
            FileUtils.deleteDirectory(
                javaDir
            )
            FCLAlertDialog.Builder(context)
                .setMessage(context.getString(R.string.import_java_error_not_valid))
                .setAlertLevel(
                    FCLAlertDialog.AlertLevel.ALERT
                )
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
    }
}