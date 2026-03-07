package com.tungsten.fcl.control

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogOpenFolderBinding
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.RequestCodes
import com.tungsten.fcllibrary.browser.FileBrowser
import com.tungsten.fcllibrary.browser.adapter.FileBrowserAdapter
import com.tungsten.fcllibrary.browser.adapter.FileBrowserListener
import com.tungsten.fcllibrary.browser.options.LibMode
import com.tungsten.fcllibrary.browser.options.SelectionMode
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class OpenFolderDialog(
    val activity: Activity,
    val initialPath: String
) : FCLDialog(activity), View.OnClickListener {
    private var internalPath: String = initialPath
    private val binding: DialogOpenFolderBinding
    private var job: Job? = null

    init {
        val width = (AndroidUtils.getScreenWidth() * 0.7).toInt()
        val height = (AndroidUtils.getScreenHeight() * 0.9).toInt()
        window!!.setLayout(width, height)
        binding = DialogOpenFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeButton.setOnClickListener(this)
        binding.importButton.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)
        binding.cancelProgress.setOnClickListener(this)
        binding.path.isSelected = true

        binding.backButton.visibility = View.GONE

        refreshFiles()
    }

    private fun refreshFiles() {
        binding.path.text = internalPath
        binding.filesLayout.adapter = FileBrowserAdapter(
            context,
            FileBrowser.Builder(context)
                .setLibMode(LibMode.FILE_BROWSER)
                .setInitDir(internalPath)
                .create(),
            File(internalPath).toPath(),
            ArrayList<String>(),
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

        binding.backButton.visibility = if (currentPath == rootPath) View.GONE else View.VISIBLE
    }

    override fun onClick(v: View) {
        when (v) {
            binding.closeButton -> dismiss()
            binding.importButton -> {
                val targetDir = internalPath
                FileBrowser.Builder(context)
                    .setLibMode(LibMode.FILE_CHOOSER)
                    .setSelectionMode(SelectionMode.MULTIPLE_SELECTION)
                    .create()
                    .browse(
                        activity,
                        RequestCodes.SELECT_IMGAME_IMPORT
                    ) { requestCode, resultCode, data ->
                        if (requestCode == RequestCodes.SELECT_IMGAME_IMPORT && resultCode == Activity.RESULT_OK && data != null) {
                            importFiles(FileBrowser.getSelectedFiles(data), targetDir)
                        }
                    }
            }

            binding.backButton -> {
                val root = Paths.get(initialPath).normalize().toAbsolutePath()
                val current = Paths.get(internalPath).normalize().toAbsolutePath()

                val parent = current.parent
                if (parent != null && parent.normalize().startsWith(root)) {
                    internalPath = parent.toString()
                    refreshFiles()
                }
            }

            binding.cancelProgress -> {
                job?.cancel()
                job = null
                binding.progress.visibility = View.GONE
            }
        }
    }


    @SuppressLint("Recycle")
    private fun importFiles(
        paths: List<String>,
        targetDir: String
    ) {
        job = lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                binding.backButton.isEnabled = false
                binding.importButton.isEnabled = false
                binding.closeButton.isEnabled = false
                binding.progress.visibility = View.VISIBLE
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
                                    activity.getString(R.string.close)
                                ) {}.create().show()
                        }
                    }
                }
            } catch (_: CancellationException) {
            } catch (_: Exception) {
            }

            withContext(Dispatchers.Main) {
                binding.backButton.isEnabled = true
                binding.importButton.isEnabled = true
                binding.closeButton.isEnabled = true
                binding.progress.visibility = View.GONE
            }

            job = null
        }
    }
}