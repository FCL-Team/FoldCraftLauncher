package com.tungsten.fcl.ui.manage.compose

import android.content.Context
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.net.toUri
import com.mio.util.showErrorDialog
import com.mio.util.showItemSelectionDialog
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Profiles.getSelectedProfile
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.dialog.MiuixDriverSelectDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixJavaManageDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixRendererSelectDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixSelectControllerDialog
import com.tungsten.fcl.ui.controller.ControllerPageManager
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.dialog.FullEditDialog
import java.io.File
import java.util.Locale

/**
 * 版本设置页宿主事件处理器（小步骤 3.3b）：承接 [VersionSettingEvent] 中需要
 * Activity/MainActivity/遗留对话框能力的一次性副作用。
 *
 * 本类代码基本是 VersionSettingPage.kt onClick(:442-587) 各分支的原样搬运，
 * 保证行为等价；弹窗统一走 Miuix 路径（6.1 已固化开关）。
 */
object VersionSettingHost {

    /** 不需要 ViewModel 回调的事件统一入口（带回调的事件在 Screen 侧单独接线）。 */
    fun handle(context: Context, event: VersionSettingEvent, globalSetting: Boolean) {
        when (event) {
            VersionSettingEvent.ShowInstallJava -> showInstallLink(
                context,
                github = "https://github.com/FCL-Team/FoldCraftLauncher/releases/tag/java",
                netdisk = "https://pan.quark.cn/s/c86012deb8c5",
            )

            VersionSettingEvent.JumpToControllerRepo -> jumpToControllerRepo()
            VersionSettingEvent.ShowRendererSelect -> showRendererSelect(context, globalSetting)

            VersionSettingEvent.ShowInstallRenderer -> showInstallLink(
                context,
                github = "https://github.com/ShirosakiMio/FCLRendererPlugin/releases/tag/Renderer",
                netdisk = "https://pan.quark.cn/s/a9f6e9d860d9",
            )

            VersionSettingEvent.ShowDriverSelect ->
                // 弹窗内部直写 VersionSetting.driver，文本由 VM 属性流自动回流，
                // 遗留回调里的 setText 不再需要（对齐 :540-545）
                MiuixDriverSelectDialog(context, globalSetting) { }.show()

            VersionSettingEvent.ShowInstallDriver -> showInstallLink(
                context,
                github = "https://github.com/FCL-Team/FCLDriverPlugin/releases/tag/Turnip",
                netdisk = "https://pan.quark.cn/s/d87c59695250",
            )

            VersionSettingEvent.EditEnvVars -> editEnvVars(context)
            VersionSettingEvent.ShowVulkanDriverSystemInfo ->
                FCLDialogs.showAlert(
                    context,
                    null,
                    context.getString(R.string.message_vulkan_driver_system),
                )

            VersionSettingEvent.ToastControllersLoading ->
                Toast.makeText(
                    context,
                    context.getString(R.string.message_data_is_loading),
                    Toast.LENGTH_SHORT,
                ).show()

            else -> Unit // 带 ViewModel 回调的事件在 Screen 的 events collect 里处理
        }
    }

    /** 对齐 :477-492。 */
    fun showJavaManage(context: Context, onSelected: (String) -> Unit) {
        MiuixJavaManageDialog(context, onSelected).show()
    }

    /** 对齐 :449-461。 */
    fun showControllerSelect(context: Context, currentId: String, onSelected: (String) -> Unit) {
        val callback = MiuixSelectControllerDialog.Callback { it?.let { c -> onSelected(c.id) } }
        MiuixSelectControllerDialog(context, currentId, callback).show()
    }

    /** 对齐 :469-476：跨 UI 跳转控制器仓库页（interaction-map G11 原样保留）。 */
    private fun jumpToControllerRepo() {
        val uiManager = MainActivity.getInstance().uiManager
        MainActivity.getInstance().binding.controller.setSelected(true)
        uiManager.controllerUI.runAfterInit {
            uiManager.controllerUI.pageManager
                .switchPage(ControllerPageManager.PAGE_ID_CONTROLLER_REPO)
        }
    }

    /** 对齐 :508-518：图形后端三选一。 */
    fun showGraphicsBackendSelect(context: Context, onSelected: (String) -> Unit) {
        showItemSelectionDialog(
            context,
            context.getString(R.string.settings_fcl_graphics_backend),
            listOf("default", "opengl", "vulkan"),
            false,
        ) { _, backendName: String ->
            onSelected(backendName)
        }
    }

    /** 对齐 :519-539（含全局设置警告）。 */
    private fun showRendererSelect(context: Context, globalSetting: Boolean) {
        val rendererCallback = java.util.function.Consumer<String> { _: String? ->
            if (globalSetting && getSelectedProfile().versionSetting != null &&
                !getSelectedProfile().versionSetting.isGlobal
            ) {
                FCLDialogs.showAlert(
                    context,
                    null,
                    context.getString(R.string.message_warn_renderer_global_setting),
                )
            }
            // 弹窗内部直写 VersionSetting.renderer，文本由 VM 属性流自动回流
        }
        MiuixRendererSelectDialog(context, globalSetting, rendererCallback).show()
    }

    /** 对齐 :493-506/546-574：安装来源二选一（Github/网盘）→ openLink。 */
    private fun showInstallLink(context: Context, github: String, netdisk: String) {
        showItemSelectionDialog(
            context,
            context.getString(R.string.message_install_plugin),
            listOf("Github", context.getString(R.string.settings_download_netdisk)),
        ) { pos, _ ->
            AndroidUtils.openLink(
                context, when (pos) {
                    0 -> github
                    1 -> netdisk
                    else -> return@showItemSelectionDialog
                },
            )
        }
    }

    /** 对齐 :390-417：SAF 单选 .png，doc Uri 拷贝到缓存目录后回调最终路径。 */
    fun pickIcon(context: Context, onPicked: (String?) -> Unit) {
        val activity = MainActivity.getInstance()
        activity.fileLauncher.launchSingleSelection(null, listOf(".png")) { files ->
            var path: String? = files?.get(0) ?: return@launchSingleSelection
            val uri = path!!.toUri()
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(activity, uri, File(FCLPath.CACHE_DIR))
            }
            onPicked(path)
        }
    }

    /** 对齐 :227-252：解析 "WxH" 写 SP，取消回滚开关（由 onCancel 回调 VM）。 */
    fun editForceResolution(context: Context, onCancel: () -> Unit) {
        val preferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        val dialog = EditDialog(context) { str ->
            try {
                val split = str.lowercase(Locale.getDefault()).split("x".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()
                if (split.size == 2) {
                    val w = split[0].toInt()
                    val h = split[1].toInt()
                    preferences.edit {
                        putString("force_resolution", w.toString() + "x" + h)
                    }
                }
            } catch (e: Exception) {
                showErrorDialog(context, e.toString())
            }
        }
        dialog.getEditText().setText(preferences.getString("force_resolution", "1920x1080"))
        dialog.onCancelListener = { onCancel() }
        dialog.show()
    }

    /** 对齐 :210-215：FullEditDialog 全屏编辑（JVM/游戏参数）。 */
    fun fullEditText(context: Context, current: String, onConfirm: (String) -> Unit) {
        val dialog = FullEditDialog(context) { str: String? ->
            onConfirm(str ?: "")
        }
        dialog.getEditText().setText(current)
        dialog.show()
    }

    /** 对齐 :576-586：环境变量全屏编辑（校验规则 getEnvironmentFromString 原样保留）。 */
    private fun editEnvVars(context: Context) {
        val preferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        val dialog = FullEditDialog(context, true) {
            val env = getEnvironmentFromString(it)
            preferences.edit {
                putString("env", env.joinToString("\n"))
            }
        }
        dialog.binding.editText.setText(preferences.getString("env", ""))
        dialog.show()
    }

    /** 对齐 VersionSettingPage.getEnvironmentFromString(:589-629)，原样搬运。 */
    private fun getEnvironmentFromString(input: String): List<String> {
        val result = mutableListOf<String>()
        val lines = input.trim().lines()

        lines.forEachIndexed { _, rawLine ->
            val line = rawLine.trim()

            // 跳过空行
            if (line.isEmpty()) {
                return@forEachIndexed
            }

            // 检查是否包含 '='
            val firstEq = line.indexOf('=')
            if (firstEq == -1) {
                return@forEachIndexed
            }

            val name = line.substring(0, firstEq).trim()
            val value = line.substring(firstEq + 1).trim() // 值可以为空

            // 变量名不能为空
            if (name.isEmpty()) {
                return@forEachIndexed
            }

            // 变量名规则：字母、数字、下划线，且不能以数字开头
            val validNameRegex = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")
            if (!validNameRegex.matches(name)) {
                return@forEachIndexed
            }

            // 长度检查（通常环境变量名不超过 255 字符）
            if (name.length > 255) {
                return@forEachIndexed
            }
            result.add("$name=$value")
        }

        return result
    }
}
