package com.tungsten.fcl.ui.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import com.mio.util.showErrorDialog
import com.mio.util.showItemSelectionDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.ControllerActivity
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageControllerManagerBinding
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.util.LayoutConverter
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.function.ExceptionalConsumer
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.ui.FCLPage
import com.tungsten.fcllibrary.ui.ProgressDialog
import java.io.File
import java.io.IOException
import java.util.logging.Level
import com.mio.util.getMimeType

/**
 * 控制布局管理页：左侧布局列表 + 右侧布局信息。
 * 列表选择、信息修改均通过回调直接刷新右侧信息与列表，不使用 fakefx property。
 */
class ControllerManagePage(context: Context, id: Int) :
    FCLPage(context, id, R.layout.page_controller_manager), View.OnClickListener {

    /** 当前选中的布局 */
    var selectedController: Controller? = null
        private set

    private var binding: PageControllerManagerBinding =
        PageControllerManagerBinding.bind(contentView)
    private lateinit var adapter: EditableControllerListAdapter

    init {
        Controllers.addCallback(::setup)
    }

    private fun setup() {
        // 校验选中项：列表为空则清空，不在列表中则选第一个
        val controllers = Controllers.getControllers()
        selectedController = when {
            controllers.isEmpty() -> null
            controllers.contains(selectedController) -> selectedController
            else -> controllers[0]
        }

        binding.importController.setOnClickListener(this)
        binding.createController.setOnClickListener(this)
        binding.downloadController.setOnClickListener(this)
        binding.upload.setOnClickListener(this)
        binding.share.setOnClickListener(this)
        binding.editInfo.setOnClickListener(this)
        binding.editController.setOnClickListener(this)

        adapter = EditableControllerListAdapter(
            context,
            controllers,
            getSelected = { selectedController },
            onSelect = { controller ->
                selectedController = controller
                refreshInfo()
                adapter.notifyDataSetChanged()
            },
            onDelete = ::removeController
        )
        binding.controllerList.adapter = adapter

        refreshInfo()

        binding.progress.visibility = View.GONE
    }

    /** 用选中布局的信息刷新右侧显示 */
    private fun refreshInfo() {
        val controller = selectedController
        binding.infoLayout.visibility = if (controller == null) View.GONE else View.VISIBLE
        binding.name.text = controller?.name.orEmpty()
        binding.version.text = controller?.version.orEmpty()
        binding.author.text = controller?.author.orEmpty()
        binding.description.text = controller?.description.orEmpty()
    }

    fun addController(controller: Controller) {
        Schedulers.androidUIThread().execute {
            Controllers.addController(controller)
            selectedController = controller
            refreshInfo()
            adapter.notifyDataSetChanged()
        }
    }

    fun removeController(controller: Controller) {
        Schedulers.androidUIThread().execute {
            Controllers.removeControllers(controller)
            if (controller == selectedController) {
                selectedController = null
            }
            refreshInfo()
            adapter.notifyDataSetChanged()
        }
    }

    fun changeControllerInfo(old: Controller, newValue: Controller) {
        old.name = newValue.name
        old.version = newValue.version
        old.versionCode = newValue.versionCode
        old.author = newValue.author
        old.description = newValue.description
        if (old.id != newValue.id) {
            try {
                old.changeId(newValue.id)
            } catch (e: IOException) {
                Logging.LOG.log(Level.SEVERE, "Failed to change controller id!", e.message)
            }
        }
        old.saveToDisk()
        refreshInfo()
        adapter.notifyDataSetChanged()
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

    override fun onClick(view: View) {
        when (view) {
            binding.importController -> importController()
            binding.createController -> ControllerInfoDialog(
                context,
                true,
                Controller(""),
                ::addController
            ).show()

            binding.downloadController -> UIManager.instance.controllerUI.showPage(1)
            binding.upload -> {
                val page = ControllerUploadPage(
                    context,
                    PAGE_ID_TEMP,
                    selectedController!!
                )
                UIManager.instance.controllerUI.showTempPage(page)
            }

            binding.share -> shareController()
            binding.editInfo -> ControllerInfoDialog(
                context,
                false,
                selectedController
            ) { controller ->
                changeControllerInfo(selectedController!!, controller)
            }.show()

            binding.editController -> {
                val intent = Intent(context, ControllerActivity::class.java)
                val bundle = Bundle()
                bundle.putString("controller", selectedController!!.id)
                intent.putExtras(bundle)
                activity.startActivity(intent)
            }
        }
    }

    private fun importController() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(
            null,
            arrayListOf(".json")
        ) { files ->
            if (files == null) return@launchSingleSelection
            try {
                val content = FileUtils.readText(files[0].toFile(activity, File(FCLPath.CACHE_DIR)))
                val controller = GsonBuilder().setPrettyPrinting().create()
                    .fromJson(content, Controller::class.java)
                if (controller.name == "Error") {
                    Toast.makeText(
                        context,
                        context.getString(R.string.control_import_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    addController(controller)
                }
            } catch (e: Throwable) {
                showErrorDialog(
                    context,
                    context.getString(R.string.control_import_failed) + "\n" + e.message
                )
                Logging.LOG.log(Level.SEVERE, "Failed to import controller", e)
            }
        }
    }

    private fun shareController() {
        showItemSelectionDialog(
            context,
            context.getString(R.string.control_share_choose),
            listOf(
                context.getString(R.string.control_share_direct),
                context.getString(R.string.control_share_zl2)
            ),
            true
        ) { _: Int, selected: String ->
            if (selected == context.getString(R.string.control_share_direct)) {
                shareDirect()
            } else if (selected == context.getString(R.string.control_share_zl2)) {
                shareAsZl2()
            }
        }
    }

    /** 直接分享原始 FCL 控制布局 JSON 文件 */
    private fun shareDirect() {
        val file = File(FCLPath.CONTROLLER_DIR, selectedController!!.fileName)
        shareFile(file, R.string.control_share, getMimeType(file.absolutePath))
    }

    /** 转换为 ZL2 格式后分享 */
    private fun shareAsZl2() {
        if (!LayoutConverter.isSupported()) {
            Toast.makeText(context, R.string.control_convert_unsupported, Toast.LENGTH_LONG).show()
            return
        }
        val dialog = ProgressDialog(context)
        dialog.show()
        val controller = selectedController!!
        val input = File(FCLPath.CONTROLLER_DIR, controller.fileName)
        // 输出到公共目录 FCL/share/，便于文件管理器定位
        val output = File(FCLPath.SHARE_DIR, controller.id + "_zl2.json")
        output.delete()
        Task.supplyAsync {
            LayoutConverter.convertFclToZl2(input, output)
        }.thenAcceptAsync(
            Schedulers.androidUIThread(),
            ExceptionalConsumer { error ->
                if (error == null) {
                    shareFile(output, R.string.control_share_zl2_title, "application/json")
                } else {
                    showErrorDialog(
                        context,
                        context.getString(R.string.control_convert_failed) + "\n" + error
                    )
                }
            }
        ).whenComplete(
            Schedulers.androidUIThread()
        ) { exception ->
            dialog.dismiss()
            if (exception != null) {
                Logging.LOG.log(Level.SEVERE, "Failed to convert controller to ZL2", exception)
                showErrorDialog(
                    context,
                    context.getString(R.string.control_convert_failed) + "\n" + exception.message
                )
            }
        }.start()
    }

    /** 通过系统分享面板分享指定文件 */
    private fun shareFile(file: File, chooserTitleRes: Int, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val uri = FileProvider.getUriForFile(
            context,
            context.getString(R.string.file_browser_provider),
            file
        )
        intent.type = mimeType
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        activity.startActivity(Intent.createChooser(intent, context.getString(chooserTitleRes)))
    }
}
