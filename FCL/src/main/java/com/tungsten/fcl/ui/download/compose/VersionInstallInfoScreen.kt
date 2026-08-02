package com.tungsten.fcl.ui.download.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.InstallerItem
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.download.version.InstallFailureAlert
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.observable.InvalidationListener
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.function.ExceptionalRunnable
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File

/**
 * 安装信息页（安装向导第 2 步，对齐 VersionInstallInfoPage + page_installer.xml）：
 * 版本名输入（自动命名/手动修改标记）+ 加载器选择卡片列表 + 安装按钮。
 *
 * 行为对齐（interaction-map §5.6）：
 * - 名称 TextWatcher：与自动生成名不一致即标记"手动修改"停止自动命名（:89-95,152-157）；
 * - 加载器项点击 → InstallerListPage 临时页选版本（Fabric API 先弹警告，:116-123）；
 * - 已选加载器可移除（:134-138）；不兼容互斥由 InstallerItem.InstallerItemGroup 内置
 *   observable 联动（业务零重写，直接复用）；
 * - 安装：名称三重校验 Toast → GameBuilder 异步 + 任务进度 → 成功/失败对话框（:193-255），
 *   失败走共享 [InstallFailureAlert.alertFailureMessage]（跨类复用，零重写）。
 */
class VersionInstallInfoStateHolder(
    private val context: Context,
    private val gameVersion: String,
) {

    /** 单个加载器条目的 Compose 状态（observable 属性单向桥接，对齐 InstallerItemSkin 绑定）。 */
    inner class LoaderUi(val item: InstallerItem) {
        var libraryVersion by mutableStateOf(item.libraryVersion.get())
        var incompatibleLibraryName by mutableStateOf(item.incompatibleLibraryName.get())
        var incompatibleWithGame by mutableStateOf(item.incompatibleWithGame.get())
        var removable by mutableStateOf(item.removable.get())
        var installable by mutableStateOf(item.installable.get())

        init {
            // 监听器随临时页销毁整体回收，无需反注册
            item.libraryVersion.addListener(InvalidationListener {
                libraryVersion = item.libraryVersion.get()
            })
            item.incompatibleLibraryName.addListener(InvalidationListener {
                incompatibleLibraryName = item.incompatibleLibraryName.get()
            })
            item.incompatibleWithGame.addListener(InvalidationListener {
                incompatibleWithGame = item.incompatibleWithGame.get()
            })
            item.removable.addListener(InvalidationListener {
                removable = item.removable.get()
            })
            item.installable.addListener(InvalidationListener {
                installable = item.installable.get()
            })
        }
    }

    private val group = InstallerItem.InstallerItemGroup(context, gameVersion)

    /** 加载器条目（对齐 :112 过滤 "game"——InstallerItemGroup 本就不含 game）。 */
    val loaders: List<LoaderUi> = group.libraries.map { LoaderUi(it) }

    /** 已选加载器版本（对齐 map 字段）。 */
    private val selectedVersions = HashMap<String, RemoteVersion>()

    var nameText by mutableStateOf(gameVersion)
        private set
    private var nameManuallyModified = false

    /** 名称输入（对齐 TextWatcher :89-95）。 */
    fun onNameChange(text: String) {
        nameText = text
        if (text != generateVersionName()) {
            nameManuallyModified = true
        }
    }

    /** 加载器版本选定回调（对齐 InstallerListPage 回调 :127-130 + reload :262-273）。 */
    fun onLoaderSelected(libraryId: String, remoteVersion: RemoteVersion) {
        selectedVersions[libraryId] = remoteVersion
        val ui = loaders.firstOrNull { it.item.libraryId == libraryId } ?: return
        ui.item.libraryVersion.set(remoteVersion.selfVersion)
        ui.item.removable.set(true)
        refreshVersionName()
    }

    /** 移除已选加载器（对齐 removeAction :134-138 + reload）。 */
    fun onLoaderRemove(ui: LoaderUi) {
        selectedVersions.remove(ui.item.libraryId)
        ui.item.libraryVersion.set(null)
        ui.item.removable.set(false)
        refreshVersionName()
    }

    /** 加载器项点击（对齐 action :114-133：守卫 + Fabric API 警告 + 打开选择页）。 */
    fun onLoaderClick(ui: LoaderUi, openInstallerList: (libraryId: String) -> Unit) {
        // 对齐 InstallerItemSkin：仅 select 可见（installable 且无互斥）时可点击
        if (!(ui.installable && ui.incompatibleLibraryName == null)) return
        if (ui.item.libraryId == LibraryAnalyzer.LibraryType.FABRIC_API.patchId) {
            FCLDialogs.showAlert(
                context = context,
                title = null,
                message = context.getString(R.string.install_installer_fabric_api_warning),
                cancelable = false,
            )
        }
        openInstallerList(ui.item.libraryId)
    }

    /** 自动版本名（对齐 generateVersionName :142-150）。 */
    private fun generateVersionName(): String {
        val nameBuilder = StringBuilder(gameVersion)
        LibraryAnalyzer.LibraryType.values()
            .filter { selectedVersions.containsKey(it.patchId) }
            .mapNotNull { loaderName(it) }
            .forEach { nameBuilder.append("-").append(it) }
        return nameBuilder.toString()
    }

    /** 对齐 refreshVersionName :152-157。 */
    private fun refreshVersionName() {
        if (nameManuallyModified) return
        nameText = generateVersionName()
    }

    /** 对齐 getLoaderName :159-178。 */
    private fun loaderName(type: LibraryAnalyzer.LibraryType): String? = when (type) {
        LibraryAnalyzer.LibraryType.FORGE -> context.getString(R.string.install_installer_forge)
        LibraryAnalyzer.LibraryType.NEO_FORGE -> context.getString(R.string.install_installer_neoforge)
        LibraryAnalyzer.LibraryType.CLEANROOM -> context.getString(R.string.install_installer_cleanroom)
        LibraryAnalyzer.LibraryType.FABRIC -> context.getString(R.string.install_installer_fabric)
        LibraryAnalyzer.LibraryType.LITELOADER -> context.getString(R.string.install_installer_liteloader)
        LibraryAnalyzer.LibraryType.QUILT -> context.getString(R.string.install_installer_quilt)
        LibraryAnalyzer.LibraryType.OPTIFINE -> context.getString(R.string.install_installer_optifine)
        else -> null
    }

    /** 安装（对齐 onClick install 分支 :193-255）。 */
    fun install(onInstallSuccess: () -> Unit) {
        val name = nameText
        if (StringUtils.isBlank(name)) {
            Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
        } else if (Profiles.getSelectedProfile().repository.versionIdConflicts(name)) {
            Toast.makeText(context, context.getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show()
        } else if (!FCLGameRepository.isValidVersionId(name)) {
            Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
        } else {
            val builder = Profiles.getSelectedProfile().dependency.gameBuilder()
            builder.name(name)
            builder.gameVersion(gameVersion)
            for ((key, value) in selectedVersions) {
                if (LibraryAnalyzer.LibraryType.MINECRAFT.patchId != key) {
                    builder.version(value)
                }
            }
            val task = builder.buildAsync()
                .whenComplete { Profiles.getSelectedProfile().repository.refreshVersions() }
                .thenRunAsync(Schedulers.androidUIThread(), ExceptionalRunnable {
                    val profile = Profiles.getSelectedProfile()
                    profile.selectedVersion = name
                    if (selectedVersions.isNotEmpty()) {
                        if (selectedVersions.containsKey(LibraryAnalyzer.LibraryType.OPTIFINE.patchId) &&
                            selectedVersions.size == 1
                        ) {
                            return@ExceptionalRunnable
                        }
                        File(
                            profile.repository.getRunDirectory(profile.selectedVersion),
                            "mods",
                        ).mkdirs()
                    }
                })
            Schedulers.androidUIThread().execute {
                val executor = task.executor(object : TaskListener() {
                    override fun onStop(success: Boolean, executor: TaskExecutor) {
                        Schedulers.androidUIThread().execute {
                            if (success) {
                                FCLDialogs.showAlert(
                                    context = context,
                                    title = null,
                                    message = context.getString(R.string.install_success),
                                    cancelable = false,
                                    onResult = { onInstallSuccess() },
                                )
                            } else {
                                val exception = executor.exception ?: return@execute
                                // 失败弹窗走共享静态实现（ModpackInstaller 跨类复用同一份）
                                InstallFailureAlert.alertFailureMessage(context, exception) {}
                            }
                        }
                    }
                })
                val pane = MiuixTaskDialog(context)
                pane.setTitle(context.getString(R.string.install_new_game))
                pane.setExecutor(executor)
                pane.show()
                executor.start()
            }
        }
    }
}

/** 加载器条目状态文案（对齐 InstallerItemSkin 的 state 绑定 :262-274）。 */
fun loaderStateText(context: Context, ui: VersionInstallInfoStateHolder.LoaderUi): String {
    val version = ui.libraryVersion
    val incompatible = ui.incompatibleLibraryName
    return when {
        ui.incompatibleWithGame -> AndroidUtils.getLocalizedText(
            context,
            "install_installer_change_version",
            version,
        )

        incompatible != null -> AndroidUtils.getLocalizedText(
            context,
            "install_installer_incompatible",
            AndroidUtils.getLocalizedText(
                context,
                "install_installer_" + incompatible.replace("-", "_"),
            ),
        )

        version == null -> context.getString(R.string.install_installer_not_installed)
        else -> version
    }
}

/** 加载器图标（对齐 InstallerItem.getDrawable :88-108）。 */
fun installerIconRes(libraryId: String): Int = when (libraryId) {
    "forge" -> R.drawable.img_forge
    "cleanroom" -> R.drawable.img_cleanroom
    "neoforge" -> R.drawable.img_neoforge
    "liteloader" -> R.drawable.img_chicken
    "optifine" -> R.drawable.img_optifine
    "fabric", "fabric-api" -> R.drawable.img_fabric
    "quilt", "quilt-api" -> R.drawable.img_quilt
    else -> R.drawable.img_grass
}

@Composable
fun VersionInstallInfoScreen(
    holder: VersionInstallInfoStateHolder,
    onOpenInstallerList: (libraryId: String) -> Unit,
    onInstallSuccess: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // 名称栏（对齐 name_bar：标签 + 输入框 + 安装按钮）
        Card(
            // 对齐 page_installer.xml name_bar 的 bg_container_white +
            // VersionInstallInfoPage:80-81 registerEvent（ltColor 染色 = primaryContainer）
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.archive_name),
                    style = MiuixTheme.textStyles.body2,
                )
                Spacer(Modifier.width(10.dp))
                TextField(
                    value = holder.nameText,
                    onValueChange = holder::onNameChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                IconButton(onClick = { holder.install(onInstallSuccess) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_download_24),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }
            }
        }
        Spacer(Modifier.width(10.dp))

        // 加载器选择列表（对齐 InstallerItemGroup.getView）
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(holder.loaders, key = { it.item.libraryId }) { ui ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    // 对齐 InstallerItemSkin 的 item 容器（InstallerItem:258-259
                    // registerEvent ltColor 染色 = primaryContainer）
                    colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { holder.onLoaderClick(ui, onOpenInstallerList) }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(installerIconRes(ui.item.libraryId)),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = androidx.compose.ui.graphics.Color.Unspecified,
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ui.item.name,
                                style = MiuixTheme.textStyles.body1,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = loaderStateText(context, ui),
                                fontSize = 11.sp,
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            )
                        }
                        if (ui.removable) {
                            IconButton(onClick = { holder.onLoaderRemove(ui) }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_baseline_close_24),
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.onSurface,
                                )
                            }
                        }
                        // select 按钮可见性 = installable 且无互斥（对齐 :276-278）
                        if (ui.installable && ui.incompatibleLibraryName == null) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}
