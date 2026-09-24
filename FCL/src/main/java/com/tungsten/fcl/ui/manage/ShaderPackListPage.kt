package com.tungsten.fcl.ui.manage

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.minecraft.ShaderPack
import com.mio.minecraft.listShaderPacks
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageManageShaderPackBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.ui.FCLPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.Locale

class ShaderPackListPage(context: Context?, id: Int) :
    FCLPage(context, id, R.layout.page_manage_shader_pack), VersionLoadable, View.OnClickListener {

    private lateinit var binding: PageManageShaderPackBinding
    private lateinit var adapter: ShaderPackListAdapter

    private var profile: Profile? = null
    private var versionId: String? = null

    /** 全部已解析的光影包，搜索时在其中过滤 */
    private var allPacks: List<ShaderPack> = emptyList()
    private var query: String = ""

    init {
        binding = PageManageShaderPackBinding.bind(contentView)
        adapter = ShaderPackListAdapter(
            context!!,
            onSelectionChanged = { count -> switchLayout(count > 0) },
            onRename = { rename(it) },
            onInfo = { showInfo(it) },
        )
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        binding.add.setOnClickListener(this)
        binding.download.setOnClickListener(this)
        binding.refresh.setOnClickListener(this)
        binding.delete.setOnClickListener(this)
        binding.selectAll.setOnClickListener(this)
        binding.selectInvert.setOnClickListener(this)
        binding.cancel.setOnClickListener(this)
        binding.searchFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                query = s?.toString() ?: ""
                applyFilter()
            }
        })
    }

    override fun refresh(vararg param: Any): Task<*>? = null

    override fun loadVersion(profile: Profile, version: String?) {
        this.profile = profile
        this.versionId = version
        refresh()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.add -> add()
            binding.download -> download()
            binding.refresh -> refresh()
            binding.delete -> deleteSelected()
            binding.selectAll -> adapter.selectAll()
            binding.selectInvert -> adapter.selectInvert()
            binding.cancel -> adapter.clearSelection()
        }
    }

    /** 跳转到下载页的光影 tab */
    private fun download() {
        val main = MainActivity.getInstance()
        main.refreshMenuView(null)
        main.binding.download.isSelected = true
        main.uiManager.downloadUI.showDownloadPage(DownloadUI.PAGE_ID_DOWNLOAD_SHADER_PACK)
    }

    fun refresh() {
        val dir = shaderPacksDir() ?: return
        setLoading(true)
        MainActivity.getInstance().lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { listShaderPacks(dir) }.getOrElse { emptyList() }
            }
            setLoading(false)
            allPacks = result
            applyFilter()
        }
    }

    private fun shaderPacksDir(): File? {
        val profile = this.profile ?: return null
        val version = this.versionId ?: return null
        return File(profile.repository.getRunDirectory(version), "shaderpacks")
    }

    private fun applyFilter() {
        val q = query.trim()
        val filtered = if (q.isEmpty()) allPacks else allPacks.filter {
            it.name.contains(q, ignoreCase = true) || it.file.name.contains(q, ignoreCase = true)
        }
        adapter.submitList(filtered)
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.add.setEnabled(!loading)
        binding.download.setEnabled(!loading)
        binding.refresh.setEnabled(!loading)
    }

    private fun switchLayout(selected: Boolean) {
        binding.normalLayout.visibility = if (selected) View.GONE else View.VISIBLE
        binding.selectedLayout.visibility = if (selected) View.VISIBLE else View.GONE
    }

    private fun add() {
        val dir = shaderPacksDir() ?: return
        MainActivity.getInstance().fileLauncher.launchMultiSelection(null, listOf(".zip")) { files ->
            if (files.isNullOrEmpty()) return@launchMultiSelection
            MainActivity.getInstance().lifecycleScope.launch {
                val (succeeded, failed) = withContext(Dispatchers.IO) {
                    val ok = mutableListOf<String>()
                    val bad = mutableListOf<String>()
                    dir.mkdirs()
                    for (file in files) {
                        val name = file.fileName(activity)
                        try {
                            val input = file.openInputStream(activity) ?: throw IOException()
                            val target = uniqueTarget(dir, name)
                            target.outputStream().use { output -> input.copyTo(output) }
                            ok.add(name)
                        } catch (_: Exception) {
                            bad.add(name)
                        }
                    }
                    ok to bad
                }
                val prompt = mutableListOf<String>()
                if (succeeded.isNotEmpty())
                    prompt.add(context.getString(R.string.shaderpack_add_success, succeeded.joinToString(", ")))
                if (failed.isNotEmpty())
                    prompt.add(context.getString(R.string.shaderpack_add_failed, failed.joinToString(", ")))
                if (prompt.isNotEmpty()) {
                    FCLAlertDialog.Builder(context)
                        .setAlertLevel(if (failed.isEmpty()) FCLAlertDialog.AlertLevel.INFO else FCLAlertDialog.AlertLevel.ALERT)
                        .setMessage(prompt.joinToString("\n"))
                        .setPositiveButton(context.getString(R.string.dialog_positive), null)
                        .create()
                        .show()
                }
                refresh()
            }
        }
    }

    private fun deleteSelected() {
        val selected = adapter.currentSelected()
        if (selected.isEmpty()) return
        FCLAlertDialog.Builder(context)
            .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
            .setMessage(context.getString(R.string.button_remove_confirm))
            .setPositiveButton(context.getString(R.string.button_remove)) {
                MainActivity.getInstance().lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        selected.forEach { it.file.deleteRecursively() }
                    }
                    refresh()
                }
            }
            .setNegativeButton(context.getString(R.string.button_cancel), null)
            .create()
            .show()
    }

    private fun rename(pack: ShaderPack) {
        val dialog = EditDialog(context, pack.name) { newName ->
            val trimmed = newName.trim()
            if (trimmed.isEmpty()) return@EditDialog
            MainActivity.getInstance().lifecycleScope.launch {
                val success = withContext(Dispatchers.IO) {
                    val parent = pack.file.parentFile ?: return@withContext false
                    val target = if (pack.isDirectory) File(parent, trimmed)
                    else File(parent, "$trimmed.${pack.file.extension}")
                    if (target.exists()) return@withContext false
                    pack.file.renameTo(target)
                }
                if (success) {
                    refresh()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.shaderpack_rename_exists),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        dialog.setTitle(context.getString(R.string.version_manage_rename))
        dialog.show()
    }

    private fun showInfo(pack: ShaderPack) {
        val message = StringBuilder()
        message.append(
            context.getString(
                R.string.shaderpack_info_type,
                context.getString(
                    if (pack.isDirectory) R.string.shaderpack_info_type_folder
                    else R.string.shaderpack_info_type_zip
                )
            )
        )
        message.append("\n").append(context.getString(R.string.shaderpack_info_file_name, pack.file.name))
        pack.fileSize?.let {
            message.append("\n").append(context.getString(R.string.shaderpack_info_file_size, formatFileSize(it)))
        }
        FCLAlertDialog.Builder(context)
            .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
            .setTitle(context.getString(R.string.shaderpack_manage))
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.dialog_positive), null)
            .create()
            .show()
    }

    /** 重名导入时追加序号，避免覆盖已有光影包 */
    private fun uniqueTarget(dir: File, fileName: String): File {
        val base = fileName.substringBeforeLast('.')
        val ext = fileName.substringAfterLast('.', "")
        var index = 0
        while (true) {
            val name = if (index == 0) fileName else if (ext.isEmpty()) "$base ($index)" else "$base ($index).$ext"
            val target = File(dir, name)
            if (!target.exists()) return target
            index++
        }
    }

    private fun formatFileSize(size: Long): String = when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f KB", size / 1024.0)
        size < 1024L * 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f MB", size / 1024.0 / 1024.0)
        else -> String.format(Locale.getDefault(), "%.1f GB", size / 1024.0 / 1024.0 / 1024.0)
    }
}
