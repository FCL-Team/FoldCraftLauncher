package com.tungsten.fcl.activity.compose

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLButton
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 启动初始化页——运行时下载，fragment_runtime.xml 的 Compose/Miuix 1:1 复原：
 * - 顶部 splash_title 标题卡（同 EULA 页）；
 * - 标题下左侧 70% 为 8 个组件行（lwjgl/cacio/cacio17/java8/17/21/25/jna）：
 *   行内 padding 10dp，组件名文本（无 textSize → 默认 14sp，无 tint → onBackground），
 *   右端 24dp 进度圈（安装中显示，旧 FCLProgressBar 染 dkColor = primaryVariant）
 *   与状态图标（marginStart 10dp，待装 ic_baseline_update_24 / 已装 ic_baseline_done_24，
 *   染 Color.GRAY，对齐 RuntimeFragment.refreshDrawables）互斥显示；行间 1dp 分隔线
 *   （darker_gray = StrokeGray，末行后无分隔线）；
 * - 左右 1dp 竖向分隔线；
 * - 右栏 30%：顶部 splash_runtime_title 说明文本（margin 10/10/10dp）+
 *   右下 splash_runtime_install 实心主题按钮（margin 10/10/8dp，铺满右栏宽度）。
 *
 * 宿主逻辑（组件状态初始化、安装任务推进、失败重试、架构检查）保留在
 * RuntimeFragment，本文件只承载 UI 层。
 */

/** 单个运行时组件的安装状态：[installed] 已就绪；[installing] 安装中（显示进度圈）。 */
class RuntimeComponentState(
    @param:StringRes val nameRes: Int,
) {
    var installed by mutableStateOf(false)
    var installing by mutableStateOf(false)
}

/** 运行时页状态：8 个组件，字段名与 SplashActivity 的就绪标记一一对应。 */
class RuntimeStateHolder {
    val lwjgl = RuntimeComponentState(R.string.splash_runtime_lwjgl)
    val cacio = RuntimeComponentState(R.string.splash_runtime_cacio)
    val cacio17 = RuntimeComponentState(R.string.splash_runtime_cacio17)
    val java8 = RuntimeComponentState(R.string.splash_runtime_java8)
    val java17 = RuntimeComponentState(R.string.splash_runtime_java17)
    val java21 = RuntimeComponentState(R.string.splash_runtime_java21)
    val java25 = RuntimeComponentState(R.string.splash_runtime_java25)
    val jna = RuntimeComponentState(R.string.splash_runtime_jna)

    /** 列表渲染顺序（对齐 fragment_runtime.xml 的行序）。 */
    val components = listOf(lwjgl, cacio, cacio17, java8, java17, java21, java25, jna)

    /** 全部就绪（对齐 RuntimeFragment.isLatest）。 */
    val isLatest: Boolean get() = components.all { it.installed }
}

/** 供遗留 Fragment 嵌入：ComposeView + FCLTheme，与既有迁移模式一致。 */
fun createRuntimeView(context: Context, holder: RuntimeStateHolder, onInstall: () -> Unit): View =
    LegacyBridge.createComposeView(context) {
        RuntimeScreen(holder = holder, onInstall = onInstall)
    }

@Composable
fun RuntimeScreen(
    holder: RuntimeStateHolder,
    onInstall: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SplashTitleBar()
        Row(modifier = Modifier.fillMaxSize()) {
            // 左栏 70%：组件行列表
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            ) {
                holder.components.forEachIndexed { index, component ->
                    if (index > 0) {
                        SplashHorizontalDivider()
                    }
                    RuntimeComponentRow(component)
                }
            }
            SplashVerticalDivider()
            // 右栏 30%：顶部说明文本 + 右下「安装」按钮
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
            ) {
                Text(
                    text = stringResource(R.string.splash_runtime_title),
                    fontSize = 14.sp,
                    color = MiuixTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp),
                )
                FCLButton(
                    onClick = onInstall,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 8.dp),
                ) {
                    Text(text = stringResource(R.string.splash_runtime_install), fontSize = 14.sp)
                }
            }
        }
    }
}

/** 组件行：组件名 + 24dp 进度圈/状态图标（对齐旧布局行结构，进度与图标互斥）。 */
@Composable
private fun RuntimeComponentRow(component: RuntimeComponentState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(component.nameRes),
            fontSize = 14.sp,
            color = MiuixTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        if (component.installing) {
            InfiniteProgressIndicator(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(24.dp),
                color = MiuixTheme.colorScheme.primaryVariant,
            )
        } else {
            Icon(
                painter = painterResource(
                    if (component.installed) R.drawable.ic_baseline_done_24
                    else R.drawable.ic_baseline_update_24
                ),
                contentDescription = null,
                // 对齐 RuntimeFragment.refreshDrawables：状态图标统一染 Color.GRAY
                tint = Color.Gray,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(24.dp),
            )
        }
    }
}

/** 行间 1dp 横向分隔线（对齐旧布局 darker_gray 分隔 View）。 */
@Composable
private fun SplashHorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(FCLThemeTokens.StrokeGray),
    )
}
