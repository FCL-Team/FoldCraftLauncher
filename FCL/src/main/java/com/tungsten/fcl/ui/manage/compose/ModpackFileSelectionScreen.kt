package com.tungsten.fcl.ui.manage.compose

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.fclCheckboxColors
import com.tungsten.fcl.ui.download.compose.ComposeTempPage
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.ui.manage.ModpackTypeSelectionPage
import com.tungsten.fclcore.mod.ModAdviser
import com.tungsten.fclcore.mod.ModpackExportInfo
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackExportTask
import com.tungsten.fclcore.mod.multimc.MultiMCInstanceConfiguration
import com.tungsten.fclcore.mod.multimc.MultiMCModpackExportTask
import com.tungsten.fclcore.mod.server.ServerModpackExportTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.FCLCheckBoxTreeItem
import com.tungsten.fcllibrary.component.view.FCLUILayout
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.util.Objects

/**
 * 整合包导出内容选择页（对齐 ui/manage/ModpackFileSelectionPage + page_modpack_file.xml）：
 * 进度 → 复选树（直接复用 [FCLCheckBoxTreeItem] 级联/半选语义）→ 下一步导出。
 *
 * 导出任务构造（getExportTask 及三个类型分支）原样从旧页搬入 holder；
 * Mcbbs/MultiMC/ServerModpackExportTask 业务类零改动。
 */
class ModpackFileSelectionStateHolder(
    private val context: Context,
    private val profile: Profile,
    private val version: String,
    private val modpackType: String,
    private val adviser: ModAdviser,
    private val exportInfo: ModpackExportInfo,
    private val modpackFile: File,
) {

    var loading by mutableStateOf(true); private set
    var rootItem by mutableStateOf<FCLCheckBoxTreeItem<String>?>(null); private set

    /** 对齐 onStart :82-102（后台构建树 → 主线程展示）。 */
    fun load() {
        Thread {
            val root = getTreeItem(profile.repository.getRunDirectory(version), "minecraft")
            Schedulers.androidUIThread().execute {
                rootItem = root
                loading = false
            }
        }.start()
    }

    /** 对齐 getTreeItem :145-195（含 SUGGESTED 预选、版本文件/ natives 隐藏、空目录剔除）。 */
    private fun getTreeItem(file: File, basePath: String): FCLCheckBoxTreeItem<String>? {
        if (!file.exists()) return null

        var state = ModAdviser.ModSuggestion.SUGGESTED
        if (basePath.length > "minecraft/".length) {
            state = adviser.advise(
                StringUtils.substringAfter(basePath, "minecraft/") + if (file.isDirectory) "/" else "",
                file.isDirectory,
            )
            if (file.isFile && Objects.equals(FileUtils.getNameWithoutExtension(file), version)) {
                // Ignore <version>.json, <version>.jar
                state = ModAdviser.ModSuggestion.HIDDEN
            }
            if (file.isDirectory && Objects.equals(file.name, "$version-natives")) {
                // Ignore <version>-natives
                state = ModAdviser.ModSuggestion.HIDDEN
            }
            if (state == ModAdviser.ModSuggestion.HIDDEN) return null
        }

        val list = ArrayList<FCLCheckBoxTreeItem<String>>()
        val item = FCLCheckBoxTreeItem(StringUtils.substringAfterLast(basePath, "/"), null, list)
        if (state == ModAdviser.ModSuggestion.SUGGESTED) item.setSelected(true)

        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (it in files) {
                    val subItem = getTreeItem(it, basePath + "/" + it.name)
                    if (subItem != null) {
                        item.setSelected(subItem.isSelected || item.isSelected)
                        if (!subItem.isSelected) {
                            item.setIndeterminate(true)
                        }
                        FlowSubscriptions.subscribe(subItem.selectedFlow()) { item.checkProperty() }
                        FlowSubscriptions.subscribe(subItem.indeterminateFlow()) { item.checkProperty() }
                        item.subItem.add(subItem)
                    }
                }
            }
            if (!item.isSelected) item.setIndeterminate(false)

            // Empty folder need not to be displayed.
            if (item.subItem.size == 0) {
                return null
            }
        }

        translation[basePath]?.let { item.setComment(it) }
        item.setExpanded("minecraft" == basePath)

        return item
    }

    /** 对齐 getFilesNeeded :197-204。 */
    private fun getFilesNeeded(item: FCLCheckBoxTreeItem<String>?, basePath: String, list: MutableList<String>) {
        if (item == null) return
        if (item.isSelected || item.isIndeterminate) {
            if (basePath.length > "minecraft/".length) {
                list.add(StringUtils.substringAfter(basePath, "minecraft/"))
            }
            item.subItem.forEach { getFilesNeeded(it, basePath + "/" + it.data, list) }
        }
    }

    /** 对齐 finish :104-143（白名单 → MiuixTaskDialog + 成功/失败弹窗）。 */
    fun finish() {
        val list = ArrayList<String>()
        getFilesNeeded(rootItem, "minecraft", list)
        exportInfo.setWhitelist(list)

        val miuixTaskDialog = MiuixTaskDialog(context)
        miuixTaskDialog.setTitle(context.getString(R.string.message_doing))

        val task = getExportTask()
        val executor = task.executor(object : TaskListener() {
            override fun onStop(success: Boolean, executor: TaskExecutor) {
                Schedulers.androidUIThread().execute {
                    if (success) {
                        FCLDialogs.showAlert(
                            context = context,
                            title = null,
                            message = context.getString(R.string.message_success),
                            onResult = {
                                ManagePageManager.instance?.dismissAllTempPagesCreatedByPage(ManagePageManager.PAGE_ID_MANAGE_MANAGE)
                            },
                            cancelable = false,
                        )
                    } else {
                        val exception = executor.exception ?: return@execute
                        FCLDialogs.showAlert(
                            context = context,
                            title = context.getString(R.string.message_failed),
                            message = StringUtils.getStackTrace(exception),
                            cancelable = false,
                        )
                    }
                }
            }
        })
        miuixTaskDialog.setExecutor(executor)
        miuixTaskDialog.show()
        executor.start()
    }

    /** 对齐 getExportTask :206-244。 */
    private fun getExportTask(): Task<*> {
        return object : Task<Any>() {
            private lateinit var exportTask: Task<*>

            override fun doPreExecute(): Boolean {
                return true
            }

            override fun preExecute() {
                exportTask = when (modpackType) {
                    ModpackTypeSelectionPage.MODPACK_TYPE_MCBBS -> exportAsMcbbs()
                    ModpackTypeSelectionPage.MODPACK_TYPE_MULTIMC -> exportAsMultiMC()
                    ModpackTypeSelectionPage.MODPACK_TYPE_SERVER -> exportAsServer()
                    else -> throw IllegalStateException("Unrecognized modpack type $modpackType")
                }
            }

            override fun getDependents(): MutableCollection<Task<*>> {
                return mutableListOf(exportTask)
            }

            override fun execute() {
            }
        }
    }

    /** 对齐 exportAsMcbbs :245-260。 */
    private fun exportAsMcbbs(): Task<*> {
        return object : Task<Void>() {
            private lateinit var dependency: Task<*>

            override fun execute() {
                dependency = McbbsModpackExportTask(profile.repository, version, exportInfo, modpackFile)
            }

            override fun getDependencies(): MutableCollection<Task<*>> {
                return mutableListOf(dependency)
            }
        }
    }

    /** 对齐 exportAsMultiMC :262-304。 */
    private fun exportAsMultiMC(): Task<*> {
        return object : Task<Void>() {
            private lateinit var dependency: Task<*>

            override fun execute() {
                val vs: VersionSetting = profile.getVersionSetting(version)
                dependency = MultiMCModpackExportTask(
                    profile.repository, version, exportInfo.whitelist,
                    MultiMCInstanceConfiguration(
                        "OneSix",
                        exportInfo.name + "-" + exportInfo.version,
                        null,
                        null,
                        "",
                        "",
                        null,
                        exportInfo.description,
                        null,
                        exportInfo.javaArguments,
                        false,
                        854,
                        480,
                        vs.maxMemory,
                        exportInfo.minMemory,
                        false,
                        /* showConsoleOnError */ true,
                        /* autoCloseConsole */ false,
                        /* overrideMemory */ true,
                        /* overrideJavaLocation */ false,
                        /* overrideJavaArgs */ true,
                        /* overrideConsole */ true,
                        /* overrideCommands */ true,
                        /* overrideWindow */ true,
                        /* iconKey */ null, // TODO
                    ),
                    modpackFile,
                )
            }

            override fun getDependencies(): MutableCollection<Task<*>> {
                return mutableListOf(dependency)
            }
        }
    }

    /** 对齐 exportAsServer :306-319。 */
    private fun exportAsServer(): Task<*> {
        return object : Task<Void>() {
            private lateinit var dependency: Task<*>

            override fun execute() {
                dependency = ServerModpackExportTask(profile.repository, version, exportInfo, modpackFile)
            }

            override fun getDependencies(): MutableCollection<Task<*>> {
                return mutableListOf(dependency)
            }
        }
    }

    /** 对齐 TRANSLATION :338-353。 */
    private val translation = mapOf(
        "minecraft/fclversion.cfg" to context.getString(R.string.modpack_files_fclversion_cfg),
        "minecraft/servers.dat" to context.getString(R.string.modpack_files_servers_dat),
        "minecraft/saves" to context.getString(R.string.modpack_files_saves),
        "minecraft/mods" to context.getString(R.string.modpack_files_mods),
        "minecraft/config" to context.getString(R.string.modpack_files_config),
        "minecraft/liteconfig" to context.getString(R.string.modpack_files_liteconfig),
        "minecraft/resourcepacks" to context.getString(R.string.modpack_files_resourcepacks),
        "minecraft/resources" to context.getString(R.string.modpack_files_resourcepacks),
        "minecraft/options.txt" to context.getString(R.string.modpack_files_options_txt),
        "minecraft/optionsshaders.txt" to context.getString(R.string.modpack_files_optionsshaders_txt),
        "minecraft/mods/VoxelMods" to context.getString(R.string.modpack_files_mods_voxelmods),
        "minecraft/dumps" to context.getString(R.string.modpack_files_dumps),
        "minecraft/blueprints" to context.getString(R.string.modpack_files_blueprints),
        "minecraft/scripts" to context.getString(R.string.modpack_files_scripts),
    )
}

/** 临时页壳（对齐 ModpackFileSelectionPage）。 */
class ComposeModpackFileSelectionPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val profile: Profile,
    private val version: String,
    private val modpackType: String,
    private val adviser: ModAdviser,
    private val exportInfo: ModpackExportInfo,
    private val modpackFile: File,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val holder = remember {
            ModpackFileSelectionStateHolder(context, profile, version, modpackType, adviser, exportInfo, modpackFile)
        }
        // 对齐旧页 onStart 时机：进入页面即开始构建文件树
        LaunchedEffect(Unit) { holder.load() }
        ModpackFileSelectionScreen(holder)
    }
}

@Composable
fun ModpackFileSelectionScreen(holder: ModpackFileSelectionStateHolder) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
    ) {
        val root = holder.rootItem
        if (holder.loading || root == null) {
            // 对齐 FCLProgressBar 居中
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            // 对齐 list（bg_container_white + ltColor 染色 = primaryContainer，marginBottom 10dp）
            FCLCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    ModpackTreeNode(root, depth = 0)
                }
            }
            Spacer(Modifier.height(10.dp))
            // 对齐 next（FCLButton ripple = 主色实心按钮）
            Button(
                onClick = holder::finish,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_next))
            }
        }
    }
}

/**
 * 复选树节点（对齐 FCLCheckBoxTreeAdapter + item_check_box_tree.xml）。
 *
 * 半选（indeterminate）对齐遗留视觉：FCLCheckBox 半选渲染为勾选态
 * （FCLCheckBox.indeterminateFlow 订阅里 setChecked(true)），
 * 故 checked = selected || indeterminate；点击切换写回 selected 并清半选，
 * 级联/父项回传由 [FCLCheckBoxTreeItem] 内置订阅完成（与旧版同一套语义）。
 */
@Composable
private fun ModpackTreeNode(item: FCLCheckBoxTreeItem<String>, depth: Int) {
    val expanded by item.expandedFlow().collectAsState()
    val selected by item.selectedFlow().collectAsState()
    val indeterminate by item.indeterminateFlow().collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 对齐 item_check_box_tree.xml：行 paddingStart 10dp + 子列表 marginStart 15dp 逐层缩进
            .padding(start = (10 + depth * 15).dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.subItem.isNotEmpty()) {
            // 对齐 switch_view（auto_tint = onPrimary，无子项时 INVISIBLE 占位）；
            // 图标资源在 FCLLibrary（nonTransitiveRClass，需经其 R 引用）
            Icon(
                painter = painterResource(
                    if (expanded) com.tungsten.fcllibrary.R.drawable.ic_baseline_arrow_drop_down_24
                    else com.tungsten.fcllibrary.R.drawable.ic_baseline_arrow_right_24,
                ),
                contentDescription = null,
                modifier = Modifier.clickable { item.setExpanded(!expanded) },
                tint = MiuixTheme.colorScheme.onPrimary,
            )
        } else {
            Spacer(Modifier.width(24.dp))
        }
        Spacer(Modifier.width(10.dp))
        Checkbox(
            state = if (selected || indeterminate) ToggleableState.On else ToggleableState.Off,
            onClick = {
                // 对齐 FCLCheckBox.addCheckedChangeListener：从当前显示态取反写 selected + 清半选
                item.setSelected(!(selected || indeterminate))
                item.setIndeterminate(false)
            },
            colors = fclCheckboxColors(),
        )
        Spacer(Modifier.width(10.dp))
        // 对齐 text/comment（auto_text_tint = onPrimary）
        Text(
            text = item.text,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
            maxLines = 1,
        )
        item.comment?.let {
            Spacer(Modifier.width(10.dp))
            Text(
                text = it,
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onPrimary,
                maxLines = 1,
            )
        }
    }
    if (expanded) {
        // 对齐 item_check_box_tree.xml：子列表 marginTop=1px + 兄弟项 dividerHeight=1px（透明）
        val px1 = with(LocalDensity.current) { 1.toDp() }
        item.subItem.forEach {
            Spacer(Modifier.height(px1))
            ModpackTreeNode(it, depth + 1)
        }
    }
}
