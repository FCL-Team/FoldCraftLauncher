package com.tungsten.fcl.ui.compose

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * 抖动反馈（小步骤 3.3）：对齐 `com.mio.util.AnimUtil.playTranslationX` 的横向抖动动画
 * （遗留用法：ProfileListAdapter 禁止操作反馈 0→50→-50→50→-50→0 / 700ms / Overshoot；
 * ManagePage 版本更新不可用反馈 0→50→-50→0 / 500ms / Overshoot）。
 *
 * 用法：
 * ```kotlin
 * val shakeState = rememberShakeState()
 * Row(modifier = Modifier.shake(shakeState)) { ... }
 * shakeState.shake() // 触发一次抖动
 * ```
 */
class ShakeState internal constructor() {
    var trigger by mutableIntStateOf(0)
        private set

    /** 触发一次抖动动画。 */
    fun shake() {
        trigger++
    }
}

@Composable
fun rememberShakeState(): ShakeState = remember { ShakeState() }

private val OvershootEasing = Easing { OvershootInterpolator().getInterpolation(it) }

/**
 * 横向抖动修饰符。[offsets] 为位移关键帧（px），首尾一般为 0f；
 * 默认与 ProfileListAdapter 的错误反馈一致（0→50→-50→50→-50→0，700ms）。
 */
@Composable
fun Modifier.shake(
    state: ShakeState,
    durationMillis: Int = 700,
    offsets: FloatArray = floatArrayOf(0f, 50f, -50f, 50f, -50f, 0f),
): Modifier = composed {
    val offset = remember { Animatable(0f) }
    LaunchedEffect(state.trigger) {
        if (state.trigger <= 0 || offsets.size < 2) return@LaunchedEffect
        offset.snapTo(offsets.first())
        offset.animateTo(
            targetValue = offsets.last(),
            animationSpec = keyframes {
                this.durationMillis = durationMillis
                val step = durationMillis / (offsets.size - 1)
                offsets.forEachIndexed { index, value ->
                    value at (index * step) using OvershootEasing
                }
            },
        )
    }
    graphicsLayer { translationX = offset.value }
}
