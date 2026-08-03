package com.tungsten.fcl.ui.download.modpack.compose

import android.content.Context
import android.text.Html
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.game.ManuallyCreatedModpackException
import com.tungsten.fcl.game.ModpackHelper
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcl.ui.download.DownloadPageManager
import com.tungsten.fcl.ui.download.modpack.ModpackInstaller
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fclcore.mod.Modpack
import com.tungsten.fclcore.mod.server.ServerModpackManifest
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.CompressingUtils
import com.tungsten.fclcore.util.io.FileUtils
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.logging.Level

/**
 * 本地/远程整合包信息确认页（对齐 ModpackPage/LocalModpackPage/RemoteModpackPage
 * + page_modpack.xml）：进度 → 信息卡（命名输入 + 名称/版本/作者）+ 简介/安装按钮。
 *
 * 业务层零改动：安装任务构造与执行全部走既有 [ModpackInstaller] 静态接口；
 * 本文件只搬 UI 状态与校验/对话框调用。
 */

/** 共享 UI 契约（本地/远程两个 holder 实现）。 */
interface ModpackInfoUi {
    val loading: Boolean
    val nameText: String
    val nameEnabled: Boolean
    val modpackName: String
    val modpackVersion: String
    val modpackAuthor: String
    val describeVisible: Boolean
    fun onNameChange(text: String)
    fun onDescribe()
    fun onInstallClick()
}

/** 对齐 ModpackPage.onClick install 分支：安装前的整合包下载警告确认框。 */
internal fun showModpackInstallWarnDialog(context: Context, onConfirm: () -> Unit) {
    FCLDialogs.showAlert(
        context = context,
        title = context.getString(R.string.modpack_download_warn_title),
        message = context.getString(R.string.modpack_download_warn_msg),
        negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
        onResult = { confirmed -> if (confirmed == true) onConfirm() },
    )
}

/** 对齐 LocalModpackPage。 */
class LocalModpackStateHolder(
    private val context: Context,
    private val profile: Profile,
    private val updateVersion: String?,
    private val modpackFile: File,
) : ModpackInfoUi {

    override var loading by mutableStateOf(true); private set
    override var nameText by mutableStateOf(updateVersion ?: ""); private set
    override val nameEnabled = updateVersion == null
    override var modpackName by mutableStateOf(""); private set
    override var modpackVersion by mutableStateOf(""); private set
    override var modpackAuthor by mutableStateOf(""); private set
    override var describeVisible by mutableStateOf(false); private set

    /** 对齐 installAsVersion（仅内部状态：普通包按版本安装，手动创建的包装为新游戏）。 */
    private var installAsVersion = true
    private var isManuallyCreated = false
    private var manifest: Modpack? = null
    private lateinit var charset: Charset

    override fun onNameChange(text: String) {
        nameText = text
    }

    /** 对齐 onStart :52-131（编码探测 → 读清单 → 三分支 UI）。 */
    fun load() {
        if (updateVersion != null) {
            nameText = updateVersion
        }
        // 对齐 :63-73 的 supplyAsync + thenApplyAsync 两步（同为后台执行，
        // 合并为单个 Callable，异常传播路径不变）
        Task.supplyAsync {
            val encoding = if (!ModpackHelper.isFileModpackByExtension(modpackFile)) {
                StandardCharsets.UTF_8
            } else {
                CompressingUtils.findSuitableEncoding(modpackFile.toPath())
            }
            charset = encoding
            manifest = ModpackHelper.readModpackManifest(modpackFile.toPath(), encoding)
            manifest
        }.whenComplete(Schedulers.androidUIThread()) { _, exception ->
            when {
                exception is ManuallyCreatedModpackException -> {
                    loading = false
                    modpackName = modpackFile.name
                    installAsVersion = false
                    if (updateVersion == null) {
                        nameText = FileUtils.getNameWithoutExtension(modpackFile)
                    }
                    this.manifest = null
                    isManuallyCreated = true
                    describeVisible = false
                    // 对齐 :85-98：警告框，确定=留在本页，取消=关页
                    FCLDialogs.showAlert(
                        context = context,
                        title = context.getString(R.string.install_modpack),
                        message = context.getString(R.string.modpack_type_manual_warning),
                        negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onResult = { confirmed -> if (confirmed != true) dismissPage() },
                        cancelable = false,
                    )
                }

                exception != null -> {
                    Logging.LOG.log(Level.WARNING, "Failed to read modpack manifest", exception)
                    // 对齐 :105-117：错误框，确定=关页
                    FCLDialogs.showAlert(
                        context = context,
                        title = context.getString(R.string.message_error),
                        message = context.getString(R.string.modpack_task_install_error),
                        onResult = { dismissPage() },
                        cancelable = false,
                    )
                }

                else -> {
                    loading = false
                    val m = manifest ?: return@whenComplete
                    modpackName = m.name ?: ""
                    modpackVersion = m.version ?: ""
                    modpackAuthor = m.author ?: ""
                    if (updateVersion == null) {
                        nameText = (m.name ?: "").trim()
                    }
                    describeVisible = true
                }
            }
        }.start()
    }

    override fun onInstallClick() {
        showModpackInstallWarnDialog(context, ::doInstall)
    }

    /** 对齐 onInstall :134-177（命名三重校验 + ModpackInstaller 对接）。 */
    private fun doInstall() {
        val name: String
        if (updateVersion != null) {
            name = updateVersion
        } else {
            val str = nameText
            if (installAsVersion) {
                if (StringUtils.isBlank(str)) {
                    Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
                    return
                } else if (profile.repository.versionIdConflicts(str)) {
                    Toast.makeText(context, context.getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show()
                    return
                } else if (!FCLGameRepository.isValidVersionId(str)) {
                    Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
                if (StringUtils.isBlank(str)) {
                    Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
                    return
                } else if (ModpackHelper.isExternalGameNameConflicts(str) || Profiles.profiles.any { it.name == str }) {
                    Toast.makeText(context, context.getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show()
                    return
                } else if (!FCLGameRepository.isValidVersionId(str)) {
                    Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
                    return
                }
            }
            name = str
        }
        val task: Task<*>? = if (isManuallyCreated) {
            ModpackInstaller.getModpackInstallTask(profile, modpackFile, name, charset)
        } else if (updateVersion == null) {
            ModpackInstaller.getModpackInstallTask(context, profile, modpackFile, manifest, name)
        } else {
            ModpackInstaller.getModpackInstallTask(context, profile, updateVersion, modpackFile, manifest, name)
        }
        if (task != null) {
            ModpackInstaller.installModpack(context, task, updateVersion != null)
        }
    }

    /** 对齐 onDescribe :180-191。 */
    override fun onDescribe() {
        val m = manifest ?: return
        FCLDialogs.showAlert(
            context = context,
            title = context.getString(R.string.modpack_description),
            message = Html.fromHtml(m.description, Html.FROM_HTML_MODE_LEGACY).toString(),
            cancelable = false,
        )
    }

    private fun dismissPage() {
        if (updateVersion == null) {
            DownloadPageManager.instance?.dismissCurrentTempPage()
        } else {
            ManagePageManager.instance?.dismissCurrentTempPage()
        }
    }
}

/** 对齐 RemoteModpackPage。 */
class RemoteModpackStateHolder(
    private val context: Context,
    private val profile: Profile,
    private val updateVersion: String?,
    private val serverManifest: ServerModpackManifest,
) : ModpackInfoUi {

    override var loading by mutableStateOf(true); private set
    override var nameText by mutableStateOf(updateVersion ?: ""); private set
    override val nameEnabled = updateVersion == null
    override var modpackName by mutableStateOf(""); private set
    override var modpackVersion by mutableStateOf(""); private set
    override var modpackAuthor by mutableStateOf(""); private set
    override var describeVisible by mutableStateOf(false); private set

    private var modpack: Modpack? = null

    override fun onNameChange(text: String) {
        nameText = text
    }

    /** 对齐 onStart :36-75。 */
    fun load() {
        if (updateVersion != null) {
            nameText = updateVersion
        }
        modpack = try {
            serverManifest.toModpack(null)
        } catch (e: IOException) {
            // 对齐 :45-58：清单畸形错误框，确定=关页
            FCLDialogs.showAlert(
                context = context,
                title = context.getString(R.string.message_error),
                message = context.getString(R.string.modpack_type_server_malformed),
                onResult = { dismissPage() },
                cancelable = false,
            )
            return
        }
        loading = false
        describeVisible = true
        modpackName = serverManifest.name ?: ""
        modpackVersion = serverManifest.version ?: ""
        modpackAuthor = serverManifest.author ?: ""
        if (updateVersion == null) {
            nameText = (serverManifest.name ?: "").trim()
        }
    }

    override fun onInstallClick() {
        showModpackInstallWarnDialog(context, ::doInstall)
    }

    /** 对齐 onInstall :78-103。 */
    private fun doInstall() {
        val name: String
        if (updateVersion != null) {
            name = updateVersion
        } else {
            val str = nameText
            if (StringUtils.isBlank(str)) {
                Toast.makeText(context, context.getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show()
                return
            } else if (profile.repository.versionIdConflicts(str)) {
                Toast.makeText(context, context.getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show()
                return
            } else if (!FCLGameRepository.isValidVersionId(str)) {
                Toast.makeText(context, context.getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show()
                return
            }
            name = str
        }
        val task: Task<*>? = if (updateVersion == null) {
            ModpackInstaller.getModpackInstallTask(context, profile, serverManifest, modpack, name)
        } else {
            ModpackInstaller.getModpackInstallTask(context, profile, updateVersion, null, serverManifest, modpack, name)
        }
        ModpackInstaller.installModpack(context, task, updateVersion != null)
    }

    /** 对齐 onDescribe :106-115。 */
    override fun onDescribe() {
        FCLDialogs.showAlert(
            context = context,
            title = context.getString(R.string.modpack_description),
            message = Html.fromHtml(serverManifest.description, Html.FROM_HTML_MODE_LEGACY).toString(),
            cancelable = false,
        )
    }

    private fun dismissPage() {
        if (updateVersion == null) {
            DownloadPageManager.instance?.dismissCurrentTempPage()
        } else {
            ManagePageManager.instance?.dismissCurrentTempPage()
        }
    }
}

/** 共享页面（对齐 page_modpack.xml）。 */
@Composable
fun ModpackInfoScreen(ui: ModpackInfoUi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
    ) {
        if (ui.loading) {
            // 对齐 FCLProgressBar 居中
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            // 对齐 info_layout ScrollView（bg_container_white + ltColor 染色 = primaryContainer）
            FCLCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    // 名称输入行（对齐 :37-68：标签 + 200dp FCLEditText）
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ModpackInfoLabel(stringResource(R.string.archive_name))
                        Spacer(Modifier.weight(1f))
                        FCLTextField(
                            value = ui.nameText,
                            onValueChange = ui::onNameChange,
                            modifier = Modifier.width(200.dp),
                            singleLine = true,
                            enabled = ui.nameEnabled,
                        )
                    }
                    ModpackInfoDivider()
                    ModpackInfoRow(stringResource(R.string.modpack_name), ui.modpackName)
                    ModpackInfoDivider()
                    ModpackInfoRow(stringResource(R.string.archive_version), ui.modpackVersion)
                    ModpackInfoDivider()
                    ModpackInfoRow(stringResource(R.string.archive_author), ui.modpackAuthor)
                }
            }
            // 对齐 describe / install（FCLButton ripple = 主色实心按钮）
            if (ui.describeVisible) {
                Button(
                    onClick = ui::onDescribe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColorsPrimary(),
                ) {
                    Text(text = stringResource(R.string.modpack_description))
                }
            }
            Button(
                onClick = ui::onInstallClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_install))
            }
        }
    }
}

@Composable
private fun ModpackInfoLabel(text: String) {
    // 对齐 FCLTextView auto_text_tint（autoTint = onPrimary）
    Text(
        text = text,
        style = MiuixTheme.textStyles.body2,
        color = MiuixTheme.colorScheme.onPrimary,
        maxLines = 1,
    )
}

@Composable
private fun ModpackInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ModpackInfoLabel(label)
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
            maxLines = 1,
        )
    }
}

@Composable
private fun ModpackInfoDivider() {
    // 对齐布局内 1dp @android:color/darker_gray 分隔线
    HorizontalDivider(thickness = 1.dp, color = Color(0xFFAAAAAA))
}
