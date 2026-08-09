package com.tungsten.fcl.ui.controller.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.FCLDropdownField
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixControllerInfoDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixControllerUploadDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixOldVersionDialog
import com.tungsten.fcl.ui.controller.ControllerPageManager
import com.tungsten.fcl.ui.download.compose.ComposeTempPage
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.LayoutConverter
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.function.ExceptionalConsumer
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.NetworkUtils
import com.tungsten.fclcore.util.io.Zipper
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.tungsten.fcl.ui.compose.FCLButton
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.logging.Level
import java.util.regex.Pattern

/** 控件仓库源地址（原 ControllerRepoPage 常量，旧页删除后迁至此处）。 */
private const val CONTROLLER_GITHUB = "https://raw.githubusercontent.com/FCL-Team/FCL-Controllers/main/"
private const val CONTROLLER_GIT_CN = "https://repo.miawa.cn/fcl_controllers/"

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

/** 控件管理页（对齐 page_controller_manager.xml + item_controller_editable.xml + ControllerManagePage）。 */
class ComposeControllerManagePage(context: Context, id: Int, parent: FCLUILayout) : ControllerComposePage(context, id, parent) {

    @Composable
    override fun Content() {
        val context = getContext()
        // 对齐旧页 Controllers.addCallback(this::init)：控件系统未初始化完成前
        // 不触碰 getControllers()/findControllerById（否则把 null 塞进列表导致卡死/崩溃）
        var initialized by remember { mutableStateOf(Controllers.isInitialized()) }
        LaunchedEffect(Unit) {
            if (!initialized) {
                Controllers.addCallback { initialized = true }
            }
        }
        if (!initialized) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return
        }
        val signal by Controllers.controllersSignalFlow().collectAsStateWithLifecycle()
        var list by remember { mutableStateOf(Controllers.getControllers()) }
        var selected by remember { mutableStateOf<Controller?>(null) }
        // 对齐旧 validateSelectedController：列表为空置 null，选中项不在列表中回退第一项
        LaunchedEffect(signal) {
            list = Controllers.getControllers()
            selected = when {
                list.isEmpty() -> null
                selected !in list -> list.first()
                else -> selected
            }
        }
        // 对齐旧 init：首次进入选中第一项
        LaunchedEffect(Unit) {
            if (selected == null && list.isNotEmpty()) selected = list.first()
        }

        Row(Modifier.fillMaxSize().padding(10.dp)) {
            // 左栏 30%（对齐 constraintWidth_percent=0.3）
            Column(Modifier.weight(3f).fillMaxHeight()) {
                // use_theme_color：主题色文本
                Text(
                    text = stringResource(R.string.control_list),
                    fontSize = 11.sp,
                    color = MiuixTheme.colorScheme.primary,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 10.dp),
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                        .height(1.dp)
                        .background(MiuixTheme.colorScheme.primary)
                )
                LazyColumn(Modifier.weight(1f).padding(top = 10.dp)) {
                    items(list, key = { it.id ?: "" }) { controller ->
                        ControllerRow(
                            controller = controller,
                            selected = selected == controller,
                            onSelect = { selected = controller },
                            onDelete = { confirmDelete(context, controller) },
                        )
                    }
                }
                // 导入/创建/下载（透明点击行：20dp 图标 + 主题色文本）
                ActionRow(R.drawable.ic_baseline_input_24, R.string.control_import, Modifier.padding(top = 10.dp)) {
                    importController { selected = it }
                }
                ActionRow(com.tungsten.fcllibrary.R.drawable.ic_baseline_add_24, R.string.control_create) {
                    MiuixControllerInfoDialog(context, true, Controller("")) {
                        Controllers.addController(it)
                        selected = it
                    }.show()
                }
                ActionRow(R.drawable.ic_baseline_download_24, R.string.control_download) {
                    ControllerPageManager.instance?.switchPage(ControllerPageManager.PAGE_ID_CONTROLLER_REPO)
                }
            }
            // 右栏 70% 信息面板（选中项为 null 时隐藏，对齐 info_layout visibility 绑定）
            val current = selected
            Column(Modifier.weight(7f).fillMaxHeight().padding(start = 10.dp)) {
                if (current != null) {
                    FCLCard(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                    ) {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            ManageInfoRow(R.string.control_info_name, current.name ?: "")
                            ManageDivider()
                            ManageInfoRow(R.string.control_info_version, current.version ?: "")
                            ManageDivider()
                            ManageInfoRow(R.string.control_info_author, current.author ?: "")
                            ManageDivider()
                            Column(Modifier.padding(10.dp)) {
                                Text(stringResource(R.string.control_info_description), color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                                Text(
                                    text = current.description ?: "",
                                    color = MiuixTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(top = 10.dp),
                                )
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(top = 10.dp)) {
                        FCLButton(
                            onClick = {
                                ControllerPageManager.instance?.showTempPage(
                                    ComposeControllerUploadPage(context, PageManager.PAGE_ID_TEMP, parent, current)
                                )
                            },
                            modifier = Modifier.weight(1f).padding(end = 5.dp),
                        ) { Text(stringResource(R.string.control_upload), maxLines = 1) }
                        FCLButton(
                            onClick = { showShareChoice(context, current) },
                            modifier = Modifier.weight(1f).padding(start = 5.dp),
                        ) { Text(stringResource(R.string.control_share), maxLines = 1) }
                    }
                    FCLButton(
                        onClick = {
                            MiuixControllerInfoDialog(context, false, current) { changeControllerInfo(current, it) }.show()
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    ) { Text(stringResource(R.string.control_info_edit)) }
                    FCLButton(
                        onClick = {
                            context.startActivity(Intent(context, ControllerActivity::class.java).putExtra("controller", current.id))
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    ) { Text(stringResource(R.string.control_edit)) }
                }
            }
        }
    }

    /** 对齐旧删除确认（control_delete）。 */
    private fun confirmDelete(context: Context, controller: Controller) {
        FCLDialogs.showAlert(
            context,
            null,
            context.getString(R.string.control_delete),
            negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
            onResult = { if (it) Controllers.removeControllers(controller) },
            cancelable = false,
        )
    }

    /** 对齐旧 importController：doc Uri 拷缓存，name=="Error" 视为解析失败。 */
    private fun importController(onImported: (Controller) -> Unit) {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, arrayListOf(".json")) { files ->
            var path = files?.get(0) ?: return@launchSingleSelection
            val uri = Uri.parse(path)
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(getActivity(), uri, File(FCLPath.CACHE_DIR))
            }
            try {
                val content = FileUtils.readText(File(path))
                val controller = GsonBuilder().setPrettyPrinting().create().fromJson(content, Controller::class.java)
                if (controller.name == "Error") {
                    Toast.makeText(context, context.getString(R.string.control_import_failed), Toast.LENGTH_SHORT).show()
                } else {
                    Controllers.addController(controller)
                    onImported(controller)
                }
            } catch (e: Throwable) {
                showErrorDialog(context, context.getString(R.string.control_import_failed) + "\n" + e.message)
                Logging.LOG.log(Level.SEVERE, "Failed to import controller", e)
            }
        }
    }

    /** 对齐旧 changeControllerInfo：含 id 变更时的 changeId 迁移。 */
    private fun changeControllerInfo(old: Controller, value: Controller) {
        old.name = value.name
        old.version = value.version
        old.versionCode = value.versionCode
        old.author = value.author
        old.description = value.description
        if (old.id != value.id) {
            try {
                old.changeId(value.id)
            } catch (e: IOException) {
                Logging.LOG.log(Level.SEVERE, "Failed to change controller id!", e.message)
            }
        }
        old.saveToDisk()
    }

    /** 对齐旧分享选择弹窗：直接分享 / 转换 ZL2 分享。 */
    private fun showShareChoice(context: Context, controller: Controller) {
        val dialog = FCLComposeDialog(context)
        dialog.setDialogContent {
            FCLDialogCard(
                title = stringResource(R.string.control_share_choose),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(R.string.control_share_direct),
                        onClick = {
                            dialog.dismiss()
                            shareDirect(context, controller)
                        },
                    ),
                    FCLDialogButton(
                        text = stringResource(R.string.control_share_zl2),
                        onClick = {
                            dialog.dismiss()
                            shareAsZl2(context, controller)
                        },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dialog.dismiss() },
                    ),
                ),
            )
        }
        dialog.show()
    }

    /** 对齐旧 shareDirect：直接分享原始 FCL 控制布局 JSON。 */
    private fun shareDirect(context: Context, controller: Controller) {
        val file = File(FCLPath.CONTROLLER_DIR, controller.fileName)
        shareFile(context, file, R.string.control_share, AndroidUtils.getMimeType(file.absolutePath))
    }

    /** 对齐旧 shareAsZl2：ProgressDialog + 转换任务 + 失败错误弹窗。 */
    private fun shareAsZl2(context: Context, controller: Controller) {
        if (!LayoutConverter.isSupported()) {
            Toast.makeText(context, R.string.control_convert_unsupported, Toast.LENGTH_LONG).show()
            return
        }
        val dialog = FCLDialogs.showProgress(context)
        val input = File(FCLPath.CONTROLLER_DIR, controller.fileName)
        // 输出到公共目录 FCL/share/，便于文件管理器定位
        val output = File(FCLPath.SHARE_DIR, controller.id + "_zl2.json")
        output.delete()
        Task.supplyAsync {
            LayoutConverter.convertFclToZl2(input, output)
        }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<String, Exception> { error ->
            if (error == null) {
                shareFile(context, output, R.string.control_share_zl2_title, "application/json")
            } else {
                showErrorDialog(context, context.getString(R.string.control_convert_failed) + "\n" + error)
            }
        }).whenComplete(Schedulers.androidUIThread()) { _, exception ->
            dialog.dismiss()
            if (exception != null) {
                Logging.LOG.log(Level.SEVERE, "Failed to convert controller to ZL2", exception)
                showErrorDialog(context, context.getString(R.string.control_convert_failed) + "\n" + exception.message)
            }
        }.start()
    }

    /** 对齐旧 shareFile：系统分享面板。 */
    private fun shareFile(context: Context, file: File, chooserTitleRes: Int, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val uri = FileProvider.getUriForFile(context, context.getString(com.tungsten.fcllibrary.R.string.file_browser_provider), file)
        intent.type = mimeType
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        getActivity().startActivity(Intent.createChooser(intent, context.getString(chooserTitleRes)))
    }
}

/** 对齐 item_controller_editable.xml：透明行，选中高亮（bg_container_transparent_selected 语义）。 */
@Composable
private fun ControllerRow(controller: Controller, selected: Boolean, onSelect: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .background(if (selected) MiuixTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f).padding(end = 10.dp)) {
            Text(
                text = controller.name ?: "",
                fontSize = 14.sp,
                color = MiuixTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = controller.version ?: "",
                fontSize = 11.sp,
                color = MiuixTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_baseline_close_24),
            contentDescription = null,
            tint = MiuixTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp).clickable(onClick = onDelete),
        )
    }
}

/** 对齐旧导入/创建/下载透明点击行。 */
@Composable
private fun ActionRow(iconRes: Int, textRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MiuixTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(textRes),
            color = MiuixTheme.colorScheme.primary,
            maxLines = 1,
            modifier = Modifier.padding(start = 10.dp),
        )
    }
}

/** 管理页信息卡行：label ←→ 值（padding=10）。 */
@Composable
private fun ManageInfoRow(labelRes: Int, value: String) {
    Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(labelRes), color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
        Spacer(Modifier.weight(1f))
        Text(value, color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
    }
}

@Composable
private fun ManageDivider() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFA9A9A9)))
}

/** 控件仓库页（对齐 page_controller_repo.xml + ControllerRepoPage + item_remote_version.xml）。 */
class ComposeControllerRepoPage(context: Context, id: Int, parent: FCLUILayout) : ControllerComposePage(context, id, parent) {

    // 旧 refreshCategory 标志：换源或失败后重拉分类（非 Compose 状态，不需要触发重组）
    private var refreshCategory = true

    @Composable
    override fun Content() {
        val context = getContext()
        var query by remember { mutableStateOf("") }
        var source by remember { mutableIntStateOf(if (LocaleUtils.isChinese(context)) 1 else 0) }
        var lang by remember { mutableIntStateOf(0) }
        var device by remember { mutableIntStateOf(0) }
        var categories by remember { mutableStateOf(listOf(ControllerCategory(0, null))) }
        var categoryIndex by remember { mutableIntStateOf(0) }
        var results by remember { mutableStateOf<List<ControllerIndex>>(emptyList()) }
        var loading by remember { mutableStateOf(false) }
        var failed by remember { mutableStateOf(false) }

        fun search() {
            loading = true
            failed = false
            val head = if (source == 0) CONTROLLER_GITHUB else CONTROLLER_GIT_CN
            val name = query
            val langSel = lang
            val deviceSel = device
            val categoryId = categories.getOrNull(categoryIndex)?.id ?: 0
            Task.supplyAsync {
                val indexStr = NetworkUtils.doGet(NetworkUtils.toURL(head + "index.json"))
                val categoryStr = NetworkUtils.doGet(NetworkUtils.toURL(head + "category.json"))
                val allIndexes: ArrayList<ControllerIndex> =
                    JsonUtils.GSON.fromJson(indexStr, TypeToken.getParameterized(ArrayList::class.java, ControllerIndex::class.java).type)
                val loaded: ArrayList<ControllerCategory> =
                    JsonUtils.GSON.fromJson(categoryStr, TypeToken.getParameterized(ArrayList::class.java, ControllerCategory::class.java).type)
                loaded.add(0, ControllerCategory(0, null))
                val filtered = allIndexes.filter { i ->
                    (i.lang == "all" || langSel == 0 ||
                        LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(i.lang)) &&
                        i.device.contains(deviceSel) &&
                        (categoryId == 0 || i.categories.contains(categoryId))
                }
                arrayOf<Any>(filterByName(name, filtered), loaded)
            }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<Array<Any>, Exception> { data ->
                @Suppress("UNCHECKED_CAST")
                val loaded = data[1] as ArrayList<ControllerCategory>
                if (refreshCategory) {
                    categories = loaded
                    categoryIndex = 0
                    refreshCategory = false
                }
                @Suppress("UNCHECKED_CAST")
                results = data[0] as ArrayList<ControllerIndex>
                loading = false
            }).whenComplete(Schedulers.androidUIThread()) { _, exception ->
                if (exception != null) {
                    loading = false
                    failed = true
                    refreshCategory = true
                }
            }.start()
        }

        // 对齐旧 onCreate：进入即搜索 + 注册控件变更回调做静默更新检查
        LaunchedEffect(Unit) {
            Controllers.addCallback { checkUpdate(context, if (LocaleUtils.isChinese(context)) 1 else 0, false) }
            search()
        }

        Column(Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
            // 标题栏（对齐 repo_title：50dp 白卡居中 16sp）
            FCLCard(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.control_download), fontSize = 16.sp, color = MiuixTheme.colorScheme.onPrimary)
                }
            }
            Row(Modifier.fillMaxSize().padding(top = 10.dp)) {
                // 左栏 30%：搜索条件卡 + 检查更新/搜索按钮
                Column(Modifier.weight(3f).fillMaxHeight()) {
                    FCLCard(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                    ) {
                        Column(Modifier.verticalScroll(rememberScrollState()).padding(10.dp)) {
                            Text(stringResource(R.string.mods_name), color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                            FCLTextField(
                                value = query,
                                onValueChange = { query = it },
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                singleLine = true,
                                enabled = !loading,
                            )
                            FCLDropdownField(
                                label = stringResource(R.string.settings_launcher_download_source),
                                items = listOf(
                                    stringResource(R.string.control_download_source_github),
                                    stringResource(R.string.control_download_source_cn),
                                ),
                                selectedIndex = source,
                                onSelectedIndexChange = {
                                    source = it
                                    refreshCategory = true
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                enabled = !loading,
                            )
                            FCLDropdownField(
                                label = stringResource(R.string.settings_launcher_language),
                                items = listOf(
                                    stringResource(R.string.curse_category_0),
                                    stringResource(R.string.control_download_lang_current),
                                ),
                                selectedIndex = lang,
                                onSelectedIndexChange = { lang = it },
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                enabled = !loading,
                            )
                            FCLDropdownField(
                                label = stringResource(R.string.mods_category),
                                items = categories.map { it.getText(context) },
                                selectedIndex = categoryIndex,
                                onSelectedIndexChange = { categoryIndex = it },
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                enabled = !loading,
                            )
                            FCLDropdownField(
                                label = stringResource(R.string.search_device),
                                items = listOf(
                                    stringResource(R.string.control_download_device_phone),
                                    stringResource(R.string.control_download_device_pad),
                                    stringResource(R.string.control_download_device_other),
                                ),
                                selectedIndex = device,
                                onSelectedIndexChange = { device = it },
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                enabled = !loading,
                            )
                        }
                    }
                    FCLButton(
                        onClick = { checkUpdate(context, source, true) },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    ) { Text(stringResource(R.string.update_check), maxLines = 1) }
                    FCLButton(
                        onClick = { search() },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    ) { Text(stringResource(R.string.search), maxLines = 1) }
                }
                // 右栏：结果列表 / 进度 / 失败重试
                Box(Modifier.weight(7f).fillMaxHeight().padding(start = 10.dp)) {
                    when {
                        loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                        failed -> Icon(
                            painter = painterResource(R.drawable.ic_baseline_refresh_24),
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center).clickable { search() },
                        )
                        else -> LazyColumn(Modifier.fillMaxSize()) {
                            items(results, key = { it.id }) { item ->
                                RepoResultCard(context, parent, source, categories, item)
                            }
                        }
                    }
                }
            }
        }
    }

    /** 对齐旧 searchControl：空串全部，regex: 前缀正则，非法正则返回空。 */
    private fun filterByName(queryString: String, list: List<ControllerIndex>): ArrayList<ControllerIndex> {
        val result = ArrayList<ControllerIndex>()
        if (StringUtils.isBlank(queryString)) {
            result.addAll(list)
            return result
        }
        val predicate: (String) -> Boolean
        if (queryString.startsWith("regex:")) {
            predicate = try {
                val pattern = Pattern.compile(queryString.substring("regex:".length))
                ({ s: String -> pattern.matcher(s).find() })
            } catch (e: Throwable) {
                Logging.LOG.log(Level.WARNING, "Illegal regular expression", e)
                return result
            }
        } else {
            val lower = queryString.lowercase(Locale.ROOT)
            predicate = { s: String -> s.lowercase(Locale.ROOT).contains(lower) }
        }
        for (index in list) {
            if (predicate(index.name)) result.add(index)
        }
        return result
    }

    /** 对齐旧 checkUpdate：逐个弹更新提示（name: old ===> new + 更新按钮）。 */
    private fun checkUpdate(context: Context, source: Int, toast: Boolean) {
        val head = if (source == 0) CONTROLLER_GITHUB else CONTROLLER_GIT_CN
        if (toast) {
            Toast.makeText(context, context.getString(R.string.update_checking), Toast.LENGTH_SHORT).show()
        }
        Task.supplyAsync {
            val data = ArrayList<Array<String>>()
            val indexStr = NetworkUtils.doGet(NetworkUtils.toURL(head + "index.json"))
            val indexes: ArrayList<ControllerIndex> =
                JsonUtils.GSON.fromJson(indexStr, TypeToken.getParameterized(ArrayList::class.java, ControllerIndex::class.java).type)
            for (controller in Controllers.getControllers()) {
                val index = indexes.firstOrNull { it.id == controller.id } ?: continue
                // 单个控件的 version.json 拉取/解析失败只跳过本项并记日志：
                // 旧实现任一失败即整体报"控件更新失败"（误报），有更新/已是最新都被掩盖
                try {
                    val versionStr = NetworkUtils.doGet(NetworkUtils.toURL(head + "repo_json/" + index.id + "/version.json"))
                    val version = JsonUtils.GSON.fromJson(versionStr, ControllerVersion::class.java)
                    if (version != null && version.latest != null && version.latest.versionCode > controller.versionCode) {
                        data.add(
                            arrayOf(
                                controller.id,
                                controller.name,
                                controller.version,
                                version.latest.versionName,
                                head + "repo_json/" + index.id + "/versions/" + version.latest.versionCode + ".json",
                            )
                        )
                    }
                } catch (e: Throwable) {
                    Logging.LOG.log(Level.WARNING, "Failed to check update for controller " + controller.id, e)
                }
            }
            data
        }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<ArrayList<Array<String>>, Exception> { s ->
            if (s.isEmpty()) {
                if (toast) {
                    Toast.makeText(context, context.getString(R.string.update_not_exist), Toast.LENGTH_SHORT).show()
                }
            } else {
                for (d in s) {
                    FCLDialogs.showAlert(
                        context,
                        context.getString(R.string.update_exist),
                        d[1] + ": " + d[2] + " ===> " + d[3],
                        positiveText = context.getString(R.string.update),
                        negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onResult = { if (it) downloadControllerFile(context, d[0], d[1], d[4]) },
                        cancelable = false,
                    )
                }
            }
        }).whenComplete(Schedulers.androidUIThread()) { _, exception ->
            if (exception != null && toast) {
                Toast.makeText(context, context.getString(R.string.update_check_failed), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}

/** 对齐 item_remote_version.xml 的仓库条目卡。 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RepoResultCard(
    context: Context,
    parent: FCLUILayout,
    source: Int,
    categories: List<ControllerCategory>,
    item: ControllerIndex,
) {
    val repoUrl = if (source == 0) CONTROLLER_GITHUB else CONTROLLER_GIT_CN
    val tag = ControllerCategory.getLocaledCategories(context, ArrayList(categories), item.categories)
        .joinToString("   ")
    FCLCard(
        onClick = {
            ControllerPageManager.instance?.showTempPage(
                ComposeControllerDownloadPage(
                    context, PageManager.PAGE_ID_TEMP, parent, source,
                    ControllerCategory.getLocaledCategories(context, ArrayList(categories), item.categories),
                    item,
                )
            )
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                model = repoUrl + "repo_json/" + item.id + "/icon.png",
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Column(Modifier.padding(start = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (tag.isNotEmpty()) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            color = MiuixTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }
                Text(
                    text = item.introduction,
                    fontSize = 12.sp,
                    color = MiuixTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** 控件下载页（对齐 page_controller_download.xml + ControllerDownloadPage）。 */
class ComposeControllerDownloadPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    source: Int,
    private val categories: ArrayList<String>,
    private val index: ControllerIndex,
) : ComposeTempPage(context, id, parent) {

    private val base = (if (source == 0) CONTROLLER_GITHUB else CONTROLLER_GIT_CN) + "repo_json/" + index.id + "/"

    @Composable
    override fun Content() {
        val context = getContext()
        var versionInfo by remember { mutableStateOf<ControllerVersion?>(null) }
        var screenshots by remember { mutableStateOf<List<String>>(emptyList()) }
        var devicesText by remember { mutableStateOf("") }
        var loading by remember { mutableStateOf(true) }
        var failed by remember { mutableStateOf(false) }
        var reloadTick by remember { mutableIntStateOf(0) }

        // 对齐旧 refresh()：拉 version.json，历史版本按 versionCode 倒序
        LaunchedEffect(reloadTick) {
            loading = true
            failed = false
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    val v = JsonUtils.GSON.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(base + "version.json")), ControllerVersion::class.java)
                    v.history.sortByDescending { it.versionCode }
                    v
                }.getOrNull()
            }
            if (result == null) {
                loading = false
                failed = true
                return@LaunchedEffect
            }
            versionInfo = result
            screenshots = (1..result.screenshot).map { i -> base + "screenshots/" + (if (i < 10) "0$i" else "$i") + ".png" }
            val allDevices = listOf(
                context.getString(R.string.control_download_device_phone),
                context.getString(R.string.control_download_device_pad),
                context.getString(R.string.control_download_device_other),
            )
            devicesText = index.device.filter { it in 0..2 }.joinToString("  ") { allDevices[it] }
            loading = false
        }

        when {
            loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            failed -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_refresh_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onPrimary,
                    modifier = Modifier.clickable { reloadTick++ },
                )
            }
            else -> {
                val info = versionInfo ?: return
                Column(Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
                    // 头部卡：图标 + 名称/分类 + 简介
                    FCLCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            @OptIn(ExperimentalGlideComposeApi::class)
                            GlideImage(
                                model = base + "icon.png",
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                            )
                            Column(Modifier.padding(start = 10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(index.name, fontSize = 14.sp, color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                                    val tag = categories.joinToString("   ")
                                    if (tag.isNotEmpty()) {
                                        Text(
                                            text = tag,
                                            fontSize = 11.sp,
                                            color = MiuixTheme.colorScheme.onPrimary,
                                            maxLines = 1,
                                            modifier = Modifier.padding(start = 10.dp),
                                        )
                                    }
                                }
                                Text(index.introduction, fontSize = 12.sp, color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                            }
                        }
                    }
                    Row(Modifier.weight(1f).padding(top = 10.dp)) {
                        // 截图列表
                        LazyColumn(Modifier.weight(1f).padding(end = 5.dp)) {
                            items(screenshots) { url ->
                                @OptIn(ExperimentalGlideComposeApi::class)
                                GlideImage(
                                    model = url,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                                )
                            }
                        }
                        // 信息卡：作者/版本/设备 + 描述
                        FCLCard(
                            modifier = Modifier.weight(1f).padding(start = 5.dp),
                            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                        ) {
                            Column(Modifier.verticalScroll(rememberScrollState())) {
                                ManageInfoRow(R.string.control_info_author, info.author ?: "")
                                ManageDivider()
                                ManageInfoRow(R.string.control_info_version, info.latest.versionName ?: "")
                                ManageDivider()
                                ManageInfoRow(R.string.control_info_device, devicesText)
                                ManageDivider()
                                Column(Modifier.padding(10.dp)) {
                                    Text(stringResource(R.string.control_info_description), color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                                    Text(
                                        text = info.description ?: "",
                                        color = MiuixTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(top = 10.dp),
                                    )
                                }
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(top = 10.dp)) {
                        FCLButton(
                            onClick = {
                                // 对齐旧 history：空历史 Toast，否则弹历史版本选择
                                if (info.history.isEmpty()) {
                                    Toast.makeText(context, context.getString(R.string.control_download_history_empty), Toast.LENGTH_SHORT).show()
                                } else {
                                    MiuixOldVersionDialog(context, ArrayList(info.history)) { download(context, it) }.show()
                                }
                            },
                            modifier = Modifier.weight(1f).padding(end = 5.dp),
                        ) { Text(stringResource(R.string.control_download_history), maxLines = 1) }
                        FCLButton(
                            onClick = { download(context, info.latest.versionCode) },
                            modifier = Modifier.weight(1f).padding(start = 5.dp),
                        ) { Text(stringResource(R.string.control_download_latest), maxLines = 1) }
                    }
                }
            }
        }
    }

    /** 对齐旧 download()：已存在同名控件先弹覆盖确认。 */
    private fun download(context: Context, versionCode: Int) {
        if (Controllers.findControllerById(index.id).id == index.id) {
            FCLDialogs.showAlert(
                context,
                null,
                context.getString(R.string.control_download_exist),
                negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                onResult = {
                    if (it) downloadControllerFile(context, index.id, index.name, base + "versions/" + versionCode + ".json")
                },
                cancelable = false,
            )
        } else {
            downloadControllerFile(context, index.id, index.name, base + "versions/" + versionCode + ".json")
        }
    }
}

/**
 * 控件文件下载（对齐旧 downloadFile 链路）：
 * 备份旧文件 → 下载 → 失败还原 + 提示（取消时 Toast），成功加入控件列表 + Toast。
 * 旧实现经 ControllerManagePage 强转增删控件，Compose 页直接走 Controllers（经 signal Flow 刷新）。
 */
private fun downloadControllerFile(context: Context, id: String, name: String, url: String) {
    FileUtils.deleteDirectoryQuietly(File(FCLPath.CACHE_DIR + "/control"))
    val destPath = FCLPath.CONTROLLER_DIR + "/" + id + ".json"
    val cache = FCLPath.CACHE_DIR + "/control/" + id + ".json"
    val exist = File(destPath).exists()
    val old = if (exist) Controllers.findControllerById(id) else null
    val dialog = MiuixTaskDialog(context, Runnable {})
    dialog.setTitle(context.getString(R.string.message_downloading))
    val executor = Task.composeAsync {
        if (exist && old != null) {
            FileUtils.copyFile(File(destPath), File(cache))
            Controllers.removeControllers(old)
        }
        FileDownloadTask(NetworkUtils.toURL(url), File(destPath)).apply { setName(name) }
    }.whenComplete(Schedulers.defaultScheduler()) { _, exception ->
        if (exception != null) {
            if (File(cache).exists() && old != null) {
                FileUtils.copyFile(File(cache), File(destPath))
                Controllers.addController(old)
            }
            Schedulers.androidUIThread().execute {
                if (exception is CancellationException) {
                    Toast.makeText(context, context.getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show()
                } else {
                    FCLDialogs.showAlert(
                        context,
                        context.getString(R.string.install_failed_downloading),
                        DownloadProviders.localizeErrorMessage(context, exception),
                        cancelable = false,
                    )
                }
            }
        } else {
            FileUtils.deleteDirectoryQuietly(File(FCLPath.CACHE_DIR + "/control"))
            Controllers.addController(
                GsonBuilder().setPrettyPrinting().create().fromJson(FileUtils.readText(File(destPath)), Controller::class.java)
            )
            Schedulers.androidUIThread().execute {
                Toast.makeText(context, context.getString(R.string.install_success), Toast.LENGTH_SHORT).show()
            }
        }
    }.executor()
    dialog.setExecutor(executor)
    dialog.show()
    executor.start()
}

/** 控件上传页（对齐 page_controller_upload.xml + ControllerUploadPage）。 */
class ComposeControllerUploadPage(context: Context, id: Int, parent: FCLUILayout, private val controller: Controller) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val context = getContext()
        Column(Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
            // 头部卡：名称 + 版本 tag + 简介
            FCLCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(controller.name ?: "", fontSize = 14.sp, color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                        Text(
                            text = controller.version ?: "",
                            fontSize = 11.sp,
                            color = MiuixTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                    Text(controller.description ?: "", fontSize = 12.sp, color = MiuixTheme.colorScheme.onPrimary, maxLines = 1)
                }
            }
            // 上传要求说明卡（weight=1）
            FCLCard(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 10.dp),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Text(
                    text = stringResource(R.string.control_upload_requirement),
                    color = MiuixTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(10.dp),
                )
            }
            Row(Modifier.fillMaxWidth().padding(top = 10.dp)) {
                FCLButton(
                    onClick = { joinQQ(context) },
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                ) { Text(stringResource(R.string.community_qq), maxLines = 1) }
                FCLButton(
                    onClick = {
                        MiuixControllerUploadDialog(context, getActivity(), controller) { name, author, intro, description, lang, devices, screenshots, icon ->
                            share(context, name, author, intro, description, lang, devices, screenshots, icon)
                        }.show()
                    },
                    modifier = Modifier.weight(1f).padding(start = 5.dp),
                ) { Text(stringResource(R.string.action_share), maxLines = 1) }
            }
        }
    }

    private fun joinQQ(context: Context) {
        runCatching {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3Dy9zEb5_DHSGdOYyigFdwsNHx9-9kALbX"),
                )
            )
        }
    }

    /** 对齐旧 share()：打包 index/version/截图/图标/版本文件为 zip 后系统分享。 */
    private fun share(context: Context, name: String, author: String, intro: String, description: String, lang: String, devices: ArrayList<Int>, screenshots: ArrayList<String>, iconPath: String?) {
        val dialog = FCLDialogs.showProgress(context)
        Task.supplyAsync {
            val root = File(FCLPath.CACHE_DIR + "/control/upload/" + controller.id)
            FileUtils.deleteDirectoryQuietly(root)
            File(File(FCLPath.CACHE_DIR + "/control/upload"), controller.id + ".zip").let {
                if (it.exists()) it.delete()
            }
            val indexJson = ControllerIndex(controller.id, lang, name, intro, devices, arrayListOf())
            val versionJson = ControllerVersion(
                screenshots.size, description, author,
                ControllerVersion.VersionInfo(controller.versionCode, controller.version), arrayListOf(),
            )
            File(root, "index.json").also { it.parentFile?.mkdirs() }.writeText(JsonUtils.GSON.toJson(indexJson))
            File(root, "version.json").writeText(JsonUtils.GSON.toJson(versionJson))
            for (i in 1..screenshots.size) {
                val num = if (i < 10) "0$i" else "$i"
                FileUtils.copyFile(File(screenshots[i - 1]), File(root, "screenshots/$num.png"))
            }
            if (iconPath != null) {
                FileUtils.copyFile(File(iconPath), File(root, "icon.png"))
            }
            FileUtils.copyFile(
                File(FCLPath.CONTROLLER_DIR + "/" + controller.fileName),
                File(root, "versions/" + controller.versionCode + ".json"),
            )
            val zip = File(FCLPath.CACHE_DIR + "/control/upload/" + controller.id + ".zip")
            Zipper(zip.toPath()).use { it.putDirectory(root.toPath(), controller.id) }
            zip
        }.thenAcceptAsync(Schedulers.androidUIThread(), ExceptionalConsumer<File, Exception> { file ->
            val intent = Intent(Intent.ACTION_SEND)
            val uri = FileProvider.getUriForFile(context, context.getString(com.tungsten.fcllibrary.R.string.file_browser_provider), file)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            getActivity().startActivity(Intent.createChooser(intent, context.getString(com.tungsten.fcllibrary.R.string.crash_reporter_share)))
        }).whenComplete(Schedulers.androidUIThread()) { _, exception ->
            dialog.dismiss()
            if (exception != null) {
                Logging.LOG.log(Level.SEVERE, "Failed to export controller and its info!", exception.message)
            }
        }.start()
    }
}
