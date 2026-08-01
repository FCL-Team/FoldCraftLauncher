package com.tungsten.fcl.ui.bridge.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tungsten.fcl.R
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SliderPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 桥接层示例（小步骤 2.3）：LauncherSettingsViewModel 的 Compose 消费范式。
 *
 * 要点：
 * - `viewModel()` 来自 lifecycle-viewmodel-compose 2.10.0（零参 ViewModel 直接用默认工厂；
 *   需要构造参数的 ViewModel 用 `viewModel(initializer = { ... })`，见 bridge-api.md §3.4）；
 * - UiState 用 collectAsStateWithLifecycle() 订阅（生命周期感知，页面不可见时停收）；
 * - 一次性事件在 LaunchedEffect 里 collect 后交给 onEvent（宿主转 LegacyBridge/Activity）；
 * - 组件选型按 0.9.3 结论（theme-mapping.md §1.2）：设置行一律用 miuix-preference 的
 *   *Preference 家族（SwitchPreference / SliderPreference / ArrowPreference），
 *   对应 0.8.8 时代的 SuperSwitch/SuperSpinner 等；
 * - 图片用 GlideImage（glide-compose 1.0.0-alpha.6，决策见 bridge-api.md §4）。
 *
 * 宿主接入示例（遗留 View 体系内嵌 Compose，见 LegacyBridge.createComposeView）：
 * ```kotlin
 * val view = LegacyBridge.createComposeView(context) {
 *     LauncherSettingsScreen(onEvent = { event ->
 *         when (event) {
 *             LauncherSettingsEvent.PickBackgroundImage -> {
 *                 // 转交 MainActivity.fileLauncher.launchSingleSelection(...)
 *             }
 *         }
 *     })
 *     LegacyBridge.LegacyDialogHost()  // 每个 Compose 根安装一次，承接遗留弹窗请求
 * }
 * parent.addView(view)
 * ```
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LauncherSettingsScreen(
    viewModel: LauncherSettingsViewModel = viewModel(),
    onEvent: (LauncherSettingsEvent) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onEvent(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(text = "下载设置（桥接层示例）", style = MiuixTheme.textStyles.title2)
        Spacer(Modifier.height(12.dp))

        // fakefx 双向绑定承接：checkAutoThreads ↔ config.autoDownloadThreadsProperty
        Card(modifier = Modifier.fillMaxWidth()) {
            SwitchPreference(
                checked = state.autoDownloadThreads,
                onCheckedChange = viewModel::setAutoDownloadThreads,
                title = "自动选择下载线程数",
                summary = "勾选时重置为默认线程数（承接 check_auto_threads 联动）",
            )
        }
        Spacer(Modifier.height(12.dp))

        // SeekBar 双向绑定承接：threads ↔ config.downloadThreadsProperty（范围 1~128）
        Card(modifier = Modifier.fillMaxWidth()) {
            SliderPreference(
                value = state.downloadThreads.toFloat(),
                onValueChange = { viewModel.setDownloadThreads(it.toInt()) },
                title = "下载线程数",
                valueText = state.downloadThreads.toString(),
                enabled = !state.autoDownloadThreads,
                valueRange = 1f..128f,
            )
        }
        Spacer(Modifier.height(12.dp))

        // 一次性事件范式：点击 → sendEvent → 宿主走 MainActivity.fileLauncher
        Card(modifier = Modifier.fillMaxWidth()) {
            ArrowPreference(
                title = "选择浅色背景图",
                summary = "点击发送一次性事件，由宿主转交遗留文件选择器",
                onClick = viewModel::onPickBackgroundImage,
            )
        }
        Spacer(Modifier.height(12.dp))

        // 图片加载决策用法示例：GlideImage（glide-compose 1.0.0-alpha.6）
        Card(modifier = Modifier.fillMaxWidth(), insideMargin = PaddingValues(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GlideImage(
                    model = R.mipmap.ic_launcher,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "GlideImage 示例（model = R.mipmap.ic_launcher）",
                    style = MiuixTheme.textStyles.body2,
                )
            }
        }
    }
}
