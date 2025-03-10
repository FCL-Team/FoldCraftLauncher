package com.mio.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.JavaManager
import com.mio.ui.adapter.ManageJavaItemAdapter
import com.tungsten.fcl.FCLApplication
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogManageJavaBinding
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.browser.options.SelectionMode
import com.tungsten.fcllibrary.component.ResultListener
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

@SuppressLint("NotifyDataSetChanged")
class JavaManageDialog(context: Context, val onSelected: (String) -> Unit) : FCLDialog(context) {
    private val versionList = mutableListOf<JavaVersion>()
    private var isLoading = false

    init {
        setCancelable(false)
        window?.setLayout(ConvertUtils.dip2px(context, 500F), ViewGroup.LayoutParams.MATCH_PARENT)
        val binding = DialogManageJavaBinding.inflate(layoutInflater)
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
                            val path = FileBrowser.getSelectedFiles(data).get(0)
                            val uri = Uri.parse(path)
                            val fileName = if (AndroidUtils.isDocUri(uri)) {
                                AndroidUtils.getFileName(context, uri)
                            } else {
                                File(path).name
                            }
                            if (!fileName.endsWith(".tar.xz")) {
                                return
                            }
                            val inputStream = if (AndroidUtils.isDocUri(uri)) {
                                context.contentResolver.openInputStream(uri)
                            } else {
                                Files.newInputStream(Paths.get(path))
                            }
                            binding.progress.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                            isLoading = true
                            Task.runAsync {
                                try {
                                    val dest = File(FCLPath.JAVA_PATH, fileName)
                                    if (dest.exists()) {
                                        FileUtils.deleteDirectory(dest)
                                    }
                                    RuntimeUtils.uncompressTarXZ(
                                        inputStream,
                                        dest
                                    )
                                    RuntimeUtils.patchJava(context, dest.absolutePath)
                                } catch (e: Throwable) {
                                    Log.e("测试", e.toString())
                                }
                            }.whenComplete(Schedulers.androidUIThread()) {
                                isLoading = false
                                binding.progress.visibility = View.GONE
                                binding.recyclerView.visibility = View.VISIBLE
                                JavaManager.addToJavaVersion(File(FCLPath.JAVA_PATH, fileName))
                                refresh()
                                binding.recyclerView.adapter?.notifyDataSetChanged()
                            }.start()
                        }
                    }

                })
        }
    }

    private fun refresh() {
        versionList.clear()
        versionList.addAll(JavaManager.javaList.stream().filter { !it.isAuto }
            .collect(Collectors.toList<JavaVersion>()))
    }
}