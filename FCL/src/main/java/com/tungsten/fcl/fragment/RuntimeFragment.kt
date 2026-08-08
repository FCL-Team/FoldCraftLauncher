package com.tungsten.fcl.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.SplashActivity
import com.tungsten.fcl.activity.compose.RuntimeComponentState
import com.tungsten.fcl.activity.compose.RuntimeStateHolder
import com.tungsten.fcl.activity.compose.createRuntimeView
import com.tungsten.fcl.util.RuntimeUtils
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.FCLFragment
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 运行时下载页。UI 已迁移 Compose/Miuix（activity/compose/RuntimeScreen.kt，
 * 经 LegacyBridge.createComposeView 嵌入）；本类保留宿主逻辑：组件状态初始化、
 * 安装任务并发推进、失败重试（错误弹窗后 installing 复位，可重新点击安装）、
 * 全部就绪后推进 SplashActivity.enterLauncher()。
 * 旧 fragment_runtime.xml 已随 Compose 固化删除。
 */
class RuntimeFragment : FCLFragment() {

    private val holder = RuntimeStateHolder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { initState() }
            check()
        }
        return createRuntimeView(requireContext(), holder, ::onInstallClick)
    }

    private fun initState() {
        val activity = activity as SplashActivity
        holder.lwjgl.installed = activity.lwjgl
        holder.cacio.installed = activity.cacio
        holder.cacio17.installed = activity.cacio17
        holder.java8.installed = activity.java8
        holder.java17.installed = activity.java17
        holder.java21.installed = activity.java21
        holder.java25.installed = activity.java25
        holder.jna.installed = activity.jna
    }

    private val isLatest: Boolean
        get() = holder.isLatest

    private fun check() {
        if (isLatest) {
            (activity as SplashActivity).enterLauncher()
        }
    }

    private var installing = false

    private fun install() {
        if (installing) return
        installing = true
        // 任务顺序与旧 install() 一致：lwjgl → cacio → cacio17 → java8/17/21/25 → jna，
        // 各组件并发安装；仅 Java 组件失败时弹错误弹窗（对齐旧行为）
        installComponent(holder.lwjgl) {
            RuntimeUtils.install(context, FCLPath.LWJGL_DIR, "app_runtime/lwjgl")
        }
        installComponent(holder.cacio) {
            RuntimeUtils.install(context, FCLPath.CACIOCAVALLO_8_DIR, "app_runtime/caciocavallo")
        }
        installComponent(holder.cacio17) {
            RuntimeUtils.install(context, FCLPath.CACIOCAVALLO_17_DIR, "app_runtime/caciocavallo17")
        }
        installComponent(holder.java8, showError = true) {
            RuntimeUtils.installJava(context, FCLPath.JAVA_8_PATH, "app_runtime/java/jre8")
        }
        installComponent(holder.java17, showError = true) {
            RuntimeUtils.installJava(context, FCLPath.JAVA_17_PATH, "app_runtime/java/jre17")
        }
        installComponent(holder.java21, showError = true) {
            RuntimeUtils.installJava(context, FCLPath.JAVA_21_PATH, "app_runtime/java/jre21")
        }
        installComponent(holder.java25, showError = true) {
            RuntimeUtils.installJava(context, FCLPath.JAVA_25_PATH, "app_runtime/java/jre25")
        }
        installComponent(holder.jna) {
            RuntimeUtils.installJna(context, FCLPath.JNA_PATH, "app_runtime/jna")
        }
    }

    /** 单个组件安装：未就绪才启动；完成后复位进度并 check()（对齐旧各组件块的控制流）。 */
    private fun installComponent(
        component: RuntimeComponentState,
        showError: Boolean = false,
        task: () -> Unit,
    ) {
        if (component.installed) return
        component.installing = true
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    task()
                    component.installed = true
                }.exceptionOrNull()?.let {
                    if (showError) showErrorDialog(it.toString())
                }
            }
            component.installing = false
            check()
        }
    }

    private fun onInstallClick() {
        val deviceArch = Architecture.archAsString(Architecture.getDeviceArchitecture())
        if (!isJavaArchSupported(deviceArch)) {
            showErrorDialog(
                getString(
                    R.string.missing_runtime_arch_files,
                    deviceArch,
                    "FCL-release-x.x.x.x-$deviceArch.apk",
                    "FCL-release-x.x.x.x-all.apk"
                )
            )
            return
        }
        install()
    }

    private fun isJavaArchSupported(arch: String): Boolean {
        try {
            val javaDirs = listOf("jre8", "jre17", "jre21", "jre25")
            val assetManager = requireContext().assets
            var supportedCount = 0
            for (javaDir in javaDirs) {
                val dirPath = "app_runtime/java/$javaDir"
                val files = assetManager.list(dirPath)
                if (files != null) {
                    val expectedFile = "bin-$arch.tar.xz"
                    if (files.contains(expectedFile)) {
                        supportedCount++
                    }
                }
            }
            return supportedCount > 0
        } catch (e: Exception) {
            showErrorDialog(e.toString())
            return false
        }
    }

    private fun showErrorDialog(message: String) {
        installing = false
        lifecycleScope.launch(Dispatchers.Main) {
            FCLAlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton {
                }
                .create()
                .show()
        }
    }
}
