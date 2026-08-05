package com.tungsten.fcl.ui.controller.compose

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.gson.GsonBuilder
import com.mio.util.showErrorDialog
import com.mio.util.showItemSelectionDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.ControllerActivity
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.dialog.MiuixControllerInfoDialog
import com.tungsten.fcl.ui.controller.ControllerPageManager
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.function.ExceptionalConsumer
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.ui.ProgressDialog
import com.tungsten.fclauncher.utils.FCLPath
import java.io.File
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Text

abstract class ControllerComposePage(context: Context, id: Int, parent: FCLUILayout) : FCLCommonPage(context, id, parent, R.layout.page_compose_container) {
    private var installed = false
    override fun onCreate() { super.onCreate() }
    override fun onStart() {
        if (!installed) {
            installed = true
            findViewById<FrameLayout>(R.id.compose_container).addView(
                LegacyBridge.createComposeView(getContext()) { Content(); LegacyBridge.LegacyDialogHost() },
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        super.onStart()
    }
    @Composable abstract fun Content()
    override fun refresh(vararg param: Any?): Task<*>? = null
}

class ComposeControllerManagePage(context: Context, id: Int, parent: FCLUILayout) : ControllerComposePage(context, id, parent) {
    @Composable override fun Content() {
        val host = getContext()
        val signal by Controllers.controllersSignalFlow().collectAsStateWithLifecycle()
        var list by remember { mutableStateOf(Controllers.getControllers().filterNotNull()) }
        var selected by remember { mutableStateOf<Controller?>(list.firstOrNull() ?: Controllers.DEFAULT_CONTROLLER) }
        fun reload() {
            list = Controllers.getControllers().filterNotNull()
            selected = selected?.let { old -> list.firstOrNull { it.id == old.id } }
                ?: list.firstOrNull() ?: Controllers.DEFAULT_CONTROLLER
        }
        LaunchedEffect(signal) { reload() }
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { importController(host) }) { Text("导入 JSON") }
                Button(onClick = { MiuixControllerInfoDialog(host, true, Controller("")) { Controllers.addController(it) }.show() }) { Text("创建") }
                Button(onClick = { ControllerPageManager.instance?.switchPage(ControllerPageManager.PAGE_ID_CONTROLLER_REPO) }) { Text("仓库") }
            }
            LazyColumn(Modifier.weight(1f)) { items(list, key = { it.id }) { item -> Text(item.name ?: "", Modifier.fillMaxWidth().clickable { selected = item }.padding(12.dp)) } }
            selected?.let { item ->
                Text("${item.name ?: ""}  ${item.version ?: ""}")
                Text(item.description ?: "", Modifier.padding(vertical = 4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { openEditor(host, item) }) { Text("编辑控制器") }
                    Button(onClick = { MiuixControllerInfoDialog(host, false, item) { copyInfo(item, it) }.show() }) { Text("编辑信息") }
                    Button(onClick = { showLegacyUpload(item) }) { Text("上传") }
                    Button(onClick = { showShareOptions(host, item) }) { Text("分享") }
                }
            }
        }
    }
    private fun importController(context: Context) {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, arrayListOf(".json")) { files ->
            val path = files?.firstOrNull()?.takeIf { it.isNotBlank() } ?: return@launchSingleSelection
            try {
                val uri = path.toUri()
                val localPath = if (com.tungsten.fcl.util.AndroidUtils.isDocUri(uri)) {
                    com.tungsten.fcl.util.AndroidUtils.copyFileToDir(getActivity(), uri, File(FCLPath.CACHE_DIR))
                } else path
                val controller = GsonBuilder().setPrettyPrinting().create().fromJson(File(localPath).readText(), Controller::class.java)
                require(controller != null && !controller.id.isNullOrBlank()) { "控制布局内容无效" }
                Controllers.addController(controller)
            } catch (e: Throwable) {
                showErrorDialog(context, "导入控制布局失败：${e.message ?: e::class.java.simpleName}")
            }
        }
    }

    private fun openEditor(context: Context, controller: Controller) {
        context.startActivity(Intent(context, ControllerActivity::class.java).putExtra("controller", controller.id))
    }

    private fun showLegacyUpload(controller: Controller) {
        val manager = ControllerPageManager.instance ?: return
        val page = com.tungsten.fcl.ui.controller.ControllerUploadPage(
            getContext(),
            PageManager.PAGE_ID_TEMP,
            manager.parent,
            R.layout.page_controller_upload,
            controller,
        )
        manager.showTempPage(page)
    }

    private fun showShareOptions(context: Context, controller: Controller) {
        showItemSelectionDialog(
            context,
            context.getString(R.string.control_share_choose),
            listOf(
                context.getString(R.string.control_share_direct),
                context.getString(R.string.control_share_zl2),
            ),
            true,
        ) { _, selected ->
            when (selected) {
                context.getString(R.string.control_share_direct) -> shareDirect(context, controller)
                context.getString(R.string.control_share_zl2) -> shareAsZl2(context, controller)
            }
            kotlin.Unit
        }
    }

    private fun shareDirect(context: Context, controller: Controller) {
        val file = File(FCLPath.CONTROLLER_DIR, controller.fileName)
        shareFile(context, file, "application/json")
    }

    private fun shareAsZl2(context: Context, controller: Controller) {
        if (!com.tungsten.fcl.util.LayoutConverter.isSupported()) {
            Toast.makeText(context, R.string.control_convert_unsupported, Toast.LENGTH_LONG).show()
            return
        }
        val dialog = ProgressDialog(context)
        dialog.show()
        val input = File(FCLPath.CONTROLLER_DIR, controller.fileName)
        val output = File(FCLPath.SHARE_DIR, "${controller.id}_zl2.json")
        output.delete()
        Task.supplyAsync {
            com.tungsten.fcl.util.LayoutConverter.convertFclToZl2(input, output)
        }.thenAcceptAsync(
            Schedulers.androidUIThread(),
            ExceptionalConsumer<String, Exception> { error ->
                if (error == null) {
                    shareFile(context, output, "application/json")
                } else {
                    showErrorDialog(context, "${context.getString(R.string.control_convert_failed)}\n$error")
                }
            },
        ).whenComplete(Schedulers.androidUIThread()) { _, exception ->
            dialog.dismiss()
            if (exception != null) {
                showErrorDialog(context, "${context.getString(R.string.control_convert_failed)}\n${exception.message}")
            }
        }.start()
    }

    private fun shareFile(context: Context, file: File, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    context.getString(com.tungsten.fcllibrary.R.string.file_browser_provider),
                    file,
                ),
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        getActivity().startActivity(Intent.createChooser(intent, context.getString(R.string.control_share)))
    }
}

private fun copyInfo(old: Controller, value: Controller) {
    old.name = value.name
    old.version = value.version
    old.versionCode = value.versionCode
    old.author = value.author
    old.description = value.description
    old.saveToDisk()
}
