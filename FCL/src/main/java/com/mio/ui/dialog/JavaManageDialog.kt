package com.mio.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.JavaManager
import com.mio.ui.adapter.ManageJavaItemAdapter
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.util.checkElfIsAndroid
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.DialogManageJavaBinding
import com.tungsten.fcl.game.JarExecutorHelper
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.io.File
import java.io.InputStream
import java.util.concurrent.CompletableFuture

@SuppressLint("NotifyDataSetChanged")
class JavaManageDialog(context: Context, currentJava: String? = null, val onSelected: (String) -> Unit) : FCLDialog(context) {
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
                context, versionList, currentJava
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
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(ConvertUtils.dip2px(context, 10f)))
        binding.cancel.setOnClickListener { if (!isLoading) dismiss() }
        binding.autoSelect.setOnClickListener {
            if (isLoading) return@setOnClickListener
            onSelected.invoke("Auto")
            dismiss()
        }
        // 执行 Jar 文件：点击选择文件执行，长按输入自定义命令行参数直接执行
        binding.jarExecute.setOnClickListener {
            if (isLoading) return@setOnClickListener
            val prefs = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
            if (prefs.getBoolean("showJarExecutorWarnDialog", true)) {
                FCLAlertDialog.Builder(context)
                    .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                    .setMessage(context.getString(R.string.jar_executor_warn))
                    .setPositiveButton {
                        prefs.edit { putBoolean("showJarExecutorWarnDialog", false) }
                    }
                    .setNegativeButton(null)
                    .create()
                    .show()
            } else {
                JarExecutorHelper.start(MainActivity.getInstance())
            }
        }
        binding.jarExecute.setOnLongClickListener {
            if (isLoading) return@setOnLongClickListener true
            val dialog = EditDialog(context, "") { args ->
                JarExecutorHelper.exec(
                    MainActivity.getInstance(),
                    null,
                    JarExecutorHelper.getJava(null),
                    args
                )
            }
            dialog.setTitle(R.string.jar_execute_custom_args)
            dialog.getEditText().apply {
                hint = "-jar xxx"
                setLines(1)
                maxLines = 1
            }
            dialog.show()
            true
        }
        binding.importJava.setOnClickListener {
            if (isLoading) return@setOnClickListener
            MainActivity.getInstance().fileLauncher.launchSingleSelection(
                null,
                listOf(".tar.xz")
            ) { files ->
                if (files == null) return@launchSingleSelection
                val file = files[0]
                val fileName = file.fileName(context)
                if (!fileName.endsWith(".tar.xz")) {
                    FCLAlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.import_java_wrong_file))
                        .setAlertLevel(
                            FCLAlertDialog.AlertLevel.ALERT
                        )
                        .setNegativeButton(null)
                        .create()
                        .show()
                    return@launchSingleSelection
                }
                val inputStream = file.openInputStream(context)
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
                return@thenApplyAsync checkElfIsAndroid(
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
                .setNegativeButton(context.getString(R.string.dialog_positive)) {

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