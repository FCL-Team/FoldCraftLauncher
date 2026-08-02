package com.tungsten.fcl.ui.bridge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tungsten.fclcore.observable.value.ChangeListener
import com.tungsten.fclcore.observable.value.ObservableValue

/**
 * observable [ObservableValue] → Compose [State] 的轻量适配器（小步骤 3.1）。
 *
 * 与 [toStateFlow] 的区别：本适配器直接产出 Compose State，不经过 Flow/协程，
 * 适合在 Composable 里就地观察遗留 observable 属性（如 ThemeEngine 的主题色）。
 * 监听在组合离开（onDispose）时自动 removeListener，不会泄漏。
 * ViewModel 内请继续使用 [toStateFlow] / [toMutableStateFlow]（见 FakeFxStateFlow.kt）。
 */
@Composable
fun <T> ObservableValue<T>.collectAsState(): State<T> {
    val state = remember(this) { mutableStateOf(value) }
    DisposableEffect(this) {
        val listener = ChangeListener<T> { _, _, newValue -> state.value = newValue }
        addListener(listener)
        onDispose { removeListener(listener) }
    }
    return state
}
