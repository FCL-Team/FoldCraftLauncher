package com.tungsten.fcl.activity.compose

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLButton
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 启动初始化页——用户协议（EULA），fragment_eula.xml 的 Compose/Miuix 1:1 复原：
 * - 顶部 splash_title 标题卡（白底染主色 = primary，文字 autoTint = onPrimary，padding 10dp）；
 * - 标题下左侧 70% 为 EULA 滚动文本（padding 10dp，无 textSize → 默认 14sp，
 *   无 auto_text_tint → 默认文字色，对齐 onBackground）；加载中在左栏居中显示进度圈
 *   （旧 FCLProgressBar 染 dkColor = primaryVariant，旧布局 bias 0.35 即左 70% 区域中心）；
 * - 左右 1dp 分隔线（@android:color/darker_gray = StrokeGray）；
 * - 右下 splash_eula_next 实心主题按钮（margin 10/10/8dp，铺满右栏宽度）。
 *
 * 宿主逻辑（eula.txt 异步加载、isFirstLaunch 落盘、推进 SplashActivity.start()）
 * 保留在 EulaFragment，本文件只承载 UI 层。
 */

/** EULA 页状态：[eulaText] 为 null 表示加载中（显示进度圈）。 */
class EulaStateHolder {
    var eulaText by mutableStateOf<String?>(null)
}

/** 供遗留 Fragment（Java/Kotlin）嵌入：ComposeView + FCLTheme，与既有迁移模式一致。 */
fun createEulaView(context: Context, holder: EulaStateHolder, onNext: Runnable): View =
    LegacyBridge.createComposeView(context) {
        EulaScreen(holder = holder, onNext = onNext::run)
    }

@Composable
fun EulaScreen(
    holder: EulaStateHolder,
    onNext: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SplashTitleBar()
            Row(modifier = Modifier.fillMaxSize()) {
                // 左栏 70%：EULA 滚动文本
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        text = holder.eulaText.orEmpty(),
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(10.dp),
                    )
                }
                SplashVerticalDivider()
                // 右栏 30%：右下「下一步」按钮
                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxHeight(),
                ) {
                    FCLButton(
                        onClick = onNext,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 8.dp),
                    ) {
                        Text(text = stringResource(R.string.splash_eula_next), fontSize = 14.sp)
                    }
                }
            }
        }
        // 加载进度圈：对齐旧 ConstraintLayout horizontal_bias=0.35（左 70% 区域中心）/
        // vertical_bias=0.5（相对整页高度）→ BiasAlignment(-0.3, 0)；旧 FCLProgressBar 染 dkColor
        if (holder.eulaText == null) {
            InfiniteProgressIndicator(
                modifier = Modifier.align(BiasAlignment(-0.3f, 0f)),
                color = MiuixTheme.colorScheme.primaryVariant,
            )
        }
    }
}

/** splash 两页共用标题卡（对齐 fragment_eula.xml / fragment_runtime.xml 的 title）。 */
@Composable
internal fun SplashTitleBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MiuixTheme.colorScheme.primary)
            .padding(10.dp),
    ) {
        Text(
            text = stringResource(R.string.splash_title),
            fontSize = 14.sp,
            color = MiuixTheme.colorScheme.onPrimary,
        )
    }
}

/** splash 两页共用 1dp 竖向分隔线（对齐旧布局 split：darker_gray 全高）。 */
@Composable
internal fun SplashVerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(FCLThemeTokens.StrokeGray),
    )
}
