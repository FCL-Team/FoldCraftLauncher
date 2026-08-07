package com.tungsten.fcl.ui.controller.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mio.util.showErrorDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.ControllerActivity
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.control.download.ControllerCategory
import com.tungsten.fcl.control.download.ControllerIndex
import com.tungsten.fcl.control.download.ControllerVersion
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixControllerInfoDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixControllerUploadDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixOldVersionDialog
import com.tungsten.fcl.ui.controller.ControllerPageManager
import com.tungsten.fcl.ui.download.compose.ComposeTempPage
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.function.ExceptionalConsumer
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.NetworkUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Text
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.regex.Pattern
import com.tungsten.fclcore.util.io.Zipper

abstract class ControllerComposePage(context: Context, id: Int, parent: FCLUILayout) : FCLCommonPage(context, id, parent, R.layout.page_compose_container) {
    private var installed = false
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
            selected = selected?.let { old -> list.firstOrNull { it.id == old.id } } ?: list.firstOrNull()
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
                    Button(onClick = { host.startActivity(Intent(host, ControllerActivity::class.java).putExtra("controller", item.id)) }) { Text("编辑控制器") }
                    Button(onClick = { MiuixControllerInfoDialog(host, false, item) { copyInfo(item, it) }.show() }) { Text("编辑信息") }
                    Button(onClick = { ControllerPageManager.instance?.showTempPage(ComposeControllerUploadPage(host, PageManager.PAGE_ID_TEMP, ControllerPageManager.instance!!.parent, item)) }) { Text("上传") }
                    Button(onClick = { shareDirect(host, item) }) { Text("分享") }
                }
            }
        }
    }

    private fun importController(context: Context) {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, arrayListOf(".json")) { files ->
            val path = files?.firstOrNull()?.takeIf { it.isNotBlank() } ?: return@launchSingleSelection
            try {
                val uri = Uri.parse(path)
                val localPath = if (com.tungsten.fcl.util.AndroidUtils.isDocUri(uri)) com.tungsten.fcl.util.AndroidUtils.copyFileToDir(getActivity(), uri, File(FCLPath.CACHE_DIR)) else path
                val controller = GsonBuilder().setPrettyPrinting().create().fromJson(File(localPath).readText(), Controller::class.java)
                require(controller != null && !controller.id.isNullOrBlank()) { "控制布局内容无效" }
                Controllers.addController(controller)
            } catch (e: Throwable) { showErrorDialog(context, "导入控制布局失败：${e.message ?: e::class.java.simpleName}") }
        }
    }

    private fun shareDirect(context: Context, controller: Controller) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.getString(com.tungsten.fcllibrary.R.string.file_browser_provider), File(FCLPath.CONTROLLER_DIR, controller.fileName)))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        getActivity().startActivity(Intent.createChooser(intent, context.getString(R.string.control_share)))
    }
}

class ComposeControllerRepoPage(context: Context, id: Int, parent: FCLUILayout) : ControllerComposePage(context, id, parent) {
    private val github = "https://raw.githubusercontent.com/FCL-Team/FCL-Controllers/main/"
    private val china = "https://repo.miawa.cn/fcl_controllers/"

    @Composable override fun Content() {
        val host = getContext()
        var query by remember { mutableStateOf("") }
        var source by remember { mutableStateOf(0) }
        var lang by remember { mutableStateOf(0) }
        var device by remember { mutableStateOf(0) }
        var category by remember { mutableStateOf(0) }
        var categories by remember { mutableStateOf(emptyList<ControllerCategory>()) }
        var results by remember { mutableStateOf(emptyList<ControllerIndex>()) }
        var loading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }
        fun search() {
            loading = true; error = null
            val head = if (source == 0) github else china
            Task.supplyAsync {
                val indexes: ArrayList<ControllerIndex> = JsonUtils.GSON.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(head + "index.json")), object : TypeToken<ArrayList<ControllerIndex>>() {}.type)
                val loaded: ArrayList<ControllerCategory> = JsonUtils.GSON.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(head + "category.json")), object : TypeToken<ArrayList<ControllerCategory>>() {}.type)
                val filtered = indexes.filter { item ->
                    (item.lang == "all" || lang == 0 || item.lang.contains(Locale.getDefault().language)) && item.device.contains(device) && (category == 0 || item.categories.contains(category)) && matches(query, item.name)
                }
                listOf(filtered, loaded)
            }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<List<List<Any>>, Exception> { data ->
                @Suppress("UNCHECKED_CAST")
                results = data[0] as List<ControllerIndex>
                @Suppress("UNCHECKED_CAST")
                categories = data[1] as List<ControllerCategory>
                loading = false
            }).whenComplete(Schedulers.androidUIThread()) { _, ex -> if (ex != null) { loading = false; error = ex.message } }.start()
        }
        LaunchedEffect(Unit) { search() }
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            FCLTextField(query, { query = it }, Modifier.fillMaxWidth(), label = "搜索（支持 regex:）", singleLine = true)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                Button(onClick = { source = 1 - source; search() }) { Text(if (source == 0) "GitHub" else "国内源") }
                Button(onClick = { lang = 1 - lang; search() }) { Text(if (lang == 0) "全部语言" else "当前语言") }
                Button(onClick = { device = (device + 1) % 3; search() }) { Text("设备 $device") }
                Button(onClick = { category = 0; search() }) { Text("分类全部") }
                Button(onClick = { checkUpdates(host, source) }) { Text("检查更新") }
            }
            if (loading) Text("加载中…")
            error?.let { Text("加载失败：$it") }
            LazyColumn(Modifier.weight(1f)) {
                items(results, key = { it.id }) { item ->
                    FCLCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.clickable { ControllerPageManager.instance?.showTempPage(ComposeControllerDownloadPage(host, PageManager.PAGE_ID_TEMP, ControllerPageManager.instance!!.parent, source, item, categories)) }) {
                            Text(item.name, Modifier.padding(8.dp)); Text(item.introduction, Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }

    private fun matches(query: String, name: String): Boolean {
        if (StringUtils.isBlank(query)) return true
        return if (query.startsWith("regex:")) runCatching { Pattern.compile(query.removePrefix("regex:")).matcher(name).find() }.getOrDefault(false) else name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
    }

    private fun checkUpdates(context: Context, source: Int) {
        val head = if (source == 0) github else china
        Task.supplyAsync {
            val indexes: ArrayList<ControllerIndex> = JsonUtils.GSON.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(head + "index.json")), object : TypeToken<ArrayList<ControllerIndex>>() {}.type)
            Controllers.getControllers().mapNotNull { controller ->
                val index = indexes.firstOrNull { it.id == controller.id } ?: return@mapNotNull null
                val version: ControllerVersion = JsonUtils.GSON.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(head + "repo_json/${index.id}/version.json")), ControllerVersion::class.java)
                if (version.latest.versionCode > controller.versionCode) "${controller.name}: ${controller.version} → ${version.latest.versionName}" else null
            }
        }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<List<String>, Exception> { updates ->
            FCLDialogs.showAlert(context, context.getString(R.string.update_exist), if (updates.isEmpty()) context.getString(R.string.update_not_exist) else updates.joinToString("\n"), context.getString(com.tungsten.fcllibrary.R.string.dialog_positive))
        }).start()
    }
}

class ComposeControllerDownloadPage(context: Context, id: Int, parent: FCLUILayout, private val source: Int, private val index: ControllerIndex, private val categories: List<ControllerCategory>) : ComposeTempPage(context, id, parent) {
    private val base = (if (source == 0) "https://raw.githubusercontent.com/FCL-Team/FCL-Controllers/main/" else "https://repo.miawa.cn/fcl_controllers/") + "repo_json/${index.id}/"
    @Composable override fun Content() {
        val host = getContext(); var version by remember { mutableStateOf<ControllerVersion?>(null) }; var failed by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(Unit) { withContext(Dispatchers.IO) { runCatching { JsonUtils.GSON.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(base + "version.json")), ControllerVersion::class.java) }.onSuccess { version = it }.onFailure { failed = it.message } } }
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(index.name); Text(index.introduction, Modifier.padding(vertical = 8.dp)); Text("分类：" + categories.flatMap { category -> if (index.categories.contains(category.id)) listOf(category.getText(host)) else emptyList() }.joinToString("、"))
            failed?.let { Text("加载失败：$it") }
            version?.let { info ->
                Text("作者：${info.author}"); Text(info.description, Modifier.padding(vertical = 8.dp)); Text("最新：${info.latest.versionName}")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick = { download(host, info.latest.versionCode) }) { Text("下载最新") }; Button(onClick = { MiuixOldVersionDialog(host, ArrayList(info.history.sortedByDescending { item -> item.versionCode })) { download(host, it) }.show() }) { Text("历史版本") } }
            }
        }
    }

    private fun download(context: Context, versionCode: Int) {
        val old = Controllers.getControllers().firstOrNull { it.id == index.id }
        if (old != null) { FCLDialogs.showAlert(context, context.getString(R.string.control_download_exist), null, context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), context.getString(com.tungsten.fcllibrary.R.string.dialog_negative), java.util.function.Consumer { if (it) downloadFile(context, versionCode, old) }) } else downloadFile(context, versionCode, null)
    }

    private fun downloadFile(context: Context, versionCode: Int, old: Controller?) {
        FileUtils.deleteDirectoryQuietly(File(FCLPath.CACHE_DIR, "control"))
        val destination = File(FCLPath.CONTROLLER_DIR, "${index.id}.json"); val cache = File(FCLPath.CACHE_DIR + "/control", "${index.id}.json")
        val dialog = MiuixTaskDialog(context, Runnable {})
        dialog.setTitle(context.getString(R.string.message_downloading))
        val executor = Task.composeAsync {
            if (old != null) { FileUtils.copyFile(destination, cache); Controllers.removeControllers(old) }
            FileDownloadTask(NetworkUtils.toURL(base + "versions/$versionCode.json"), destination).apply { name = index.name }
        }.whenComplete(Schedulers.defaultScheduler()) { _, exception ->
            if (exception != null) {
                if (cache.exists() && old != null) { FileUtils.copyFile(cache, destination); Controllers.addController(old) }
                Schedulers.androidUIThread().execute { FCLDialogs.showAlert(context, context.getString(R.string.install_failed_downloading), DownloadProviders.localizeErrorMessage(context, exception), context.getString(com.tungsten.fcllibrary.R.string.dialog_positive)) }
            } else {
                FileUtils.deleteDirectoryQuietly(File(FCLPath.CACHE_DIR, "control"))
                Controllers.addController(GsonBuilder().setPrettyPrinting().create().fromJson(FileUtils.readText(destination), Controller::class.java))
                Schedulers.androidUIThread().execute { Toast.makeText(context, context.getString(R.string.install_success), Toast.LENGTH_SHORT).show() }
            }
        }.executor()
        dialog.setExecutor(executor); dialog.show(); executor.start()
    }
}

class ComposeControllerUploadPage(context: Context, id: Int, parent: FCLUILayout, private val controller: Controller) : ComposeTempPage(context, id, parent) {
    @Composable override fun Content() {
        val host = LocalContext.current
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(controller.name ?: ""); Text(controller.version ?: "", Modifier.padding(vertical = 4.dp)); Text(controller.description ?: "")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 12.dp)) {
                Button(onClick = { joinQQ(host) }) { Text("加入 QQ 群") }
                Button(onClick = { MiuixControllerUploadDialog(host, getActivity(), controller) { name, author, intro, description, lang, devices, screenshots, icon -> share(host, name, author, intro, description, lang, devices, screenshots, icon) }.show() }) { Text("制作上传包") }
            }
        }
    }

    private fun joinQQ(context: Context) { runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3Dy9zEb5_DHSGdOYyigFdwsNHx9-9kALbX"))) } }

    private fun share(context: Context, name: String, author: String, intro: String, description: String, lang: String, devices: ArrayList<Int>, screenshots: ArrayList<String>, iconPath: String) {
        val progress = com.tungsten.fcl.ui.compose.FCLDialogs.showProgress(context)
        Task.supplyAsync {
            val root = File(FCLPath.CACHE_DIR + "/control/upload", controller.id); FileUtils.deleteDirectoryQuietly(root); root.mkdirs()
            Files.write(File.createTempFile("index", ".json").toPath(), JsonUtils.GSON.toJson(com.tungsten.fcl.control.download.ControllerIndex(controller.id, lang, name, intro, devices, arrayListOf())).toByteArray(StandardCharsets.UTF_8))
            FileUtils.copyFile(File(FCLPath.CONTROLLER_DIR, controller.fileName), File(root, "versions/${controller.versionCode}.json"))
            FileUtils.copyFile(File(iconPath), File(root, "icon.png"))
            screenshots.forEachIndexed { i, path -> FileUtils.copyFile(File(path), File(root, "screenshots/${String.format("%02d", i + 1)}.png")) }
            File(root, "version.json").writeText(JsonUtils.GSON.toJson(ControllerVersion(screenshots.size, description, author, ControllerVersion.VersionInfo(controller.versionCode, controller.version), arrayListOf())))
            val zip = File(FCLPath.CACHE_DIR + "/control/upload", "${controller.id}.zip"); Zipper(zip.toPath()).use { it.putDirectory(root.toPath(), controller.id) }; zip
        }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<File, Exception> { file ->
            val intent = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, context.getString(com.tungsten.fcllibrary.R.string.file_browser_provider), file)); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            getActivity().startActivity(Intent.createChooser(intent, context.getString(com.tungsten.fcllibrary.R.string.crash_reporter_share)))
        }).whenComplete(Schedulers.androidUIThread()) { _, _ -> progress.dismiss() }.start()
    }
}

private fun copyInfo(old: Controller, value: Controller) { old.name = value.name; old.version = value.version; old.versionCode = value.versionCode; old.author = value.author; old.description = value.description; old.saveToDisk() }
