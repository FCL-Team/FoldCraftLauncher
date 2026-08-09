package com.tungsten.fcl.ui.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

/**
 * FCL 旧版 FCLAppBarLayout `scroll|enterAlways|snap` 折叠行为的 Compose 共享实现
 * （对齐 page_download.xml 分页栏、page_install_version.xml 过滤栏）：
 * - 列表上滑 → 顶栏随之上滑折叠（scroll）；
 * - 任意位置回拉 → 顶栏立即展开，先于列表滚动（enterAlways）；
 * - 松手时顶栏处于半折叠态 → 动画吸附到全展开/全折叠（snap）。
 *
 * 用法：父容器挂 `Modifier.nestedScroll(state.nestedScrollConnection)`，
 * 顶栏挂 `Modifier.fclCollapsingBar(state)`。
 */
class FCLCollapsingBarState {
    /** 顶栏高度（px），首次布局时测量。 */
    internal var barHeightPx by mutableFloatStateOf(0f)

    /** 当前偏移（px）：0 = 全展开，-barHeight = 全折叠。 */
    var offsetPx by mutableFloatStateOf(0f)
        private set

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val barHeight = barHeightPx
            if (barHeight <= 0f) return Offset.Zero
            val old = offsetPx
            // delta<0（列表上滑）先折叠顶栏；delta>0（回拉）先展开顶栏（enterAlways）
            val new = (old + available.y).coerceIn(-barHeight, 0f)
            offsetPx = new
            return Offset(0f, new - old)
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            val barHeight = barHeightPx
            // 对齐 snap：松手后半折叠态吸附到最近端点
            if (barHeight > 0f && offsetPx != 0f && offsetPx != -barHeight) {
                val target = if (offsetPx > -barHeight / 2f) 0f else -barHeight
                animate(
                    initialValue = offsetPx,
                    targetValue = target,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                ) { value, _ -> offsetPx = value }
            }
            return Velocity.Zero
        }
    }
}

@Composable
fun rememberFCLCollapsingBarState(): FCLCollapsingBarState =
    remember { FCLCollapsingBarState() }

/**
 * 顶栏折叠 modifier：布局高度随 [FCLCollapsingBarState.offsetPx] 收缩，
 * 内容上滑出可视区（clipToBounds 裁掉越界部分），下方列表随之顶上。
 */
fun Modifier.fclCollapsingBar(state: FCLCollapsingBarState): Modifier =
    this.clipToBounds().layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        if (placeable.height > 0 && state.barHeightPx == 0f) {
            state.barHeightPx = placeable.height.toFloat()
        }
        val offset = state.offsetPx.roundToInt()
        val height = (placeable.height + offset).coerceAtLeast(0)
        layout(placeable.width, height) {
            placeable.placeRelative(0, offset)
        }
    }
