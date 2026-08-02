package com.tungsten.fcl.ui.compose

import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tungsten.fcl.ui.bridge.collectAsState
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/**
 * 列表项入场动画（小步骤 4.2）：统一承接遗留 Adapter onBind 的入场动画
 * （`AnimUtil.playTranslationX(-100f→0f)`，时长 = 主题 `animationSpeed × 30`ms，
 * 见 interaction-map §10.4 与各 Adapter 调用点：VersionListAdapter:94-99、
 * RemoteModListAdapter、RemoteVersionListAdapter:108、ModVersionAdapter:73、
 * ModGameVersionAdapter:69）。
 *
 * Compose 侧按 interaction-map §10.4 的既定替代方案用 LazyColumn item 动画承接：
 * `animateItem(fadeInSpec = tween(animationSpeed × 30))`，placement/fadeOut 关闭，
 * 避免旧版没有的额外动效（对齐 3.3 VersionListScreen 首处落地的模式）。
 *
 * 时长经 fakefx `animationSpeedProperty` 桥观察（[collectAsState]），
 * 启动器设置页拖动动画速度后即时生效；引擎未初始化时回落默认值 8（Theme.java:213）。
 */
@Composable
fun LazyItemScope.fclItemEntryModifier(): Modifier {
    val speed = ThemeEngine.getInstance().theme?.animationSpeedProperty()
        ?.collectAsState()?.value?.toInt() ?: 8
    return Modifier.animateItem(
        fadeInSpec = tween(speed * 30),
        placementSpec = null,
        fadeOutSpec = null,
    )
}
