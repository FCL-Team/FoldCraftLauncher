package com.tungsten.fcl.ui.bridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tungsten.fclcore.fakefx.beans.property.Property
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue
import com.tungsten.fclcore.util.Logging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.logging.Level

/**
 * 迁移期统一 ViewModel 基类（小步骤 2.3，基于 lifecycle-viewmodel-compose 2.10.0）。
 *
 * 范式（对 interaction-map.md G4/G10 的承接）：
 * - 单一 [uiState]：Compose 页面的唯一渲染数据源，替代原来"View 直接读写 fakefx 属性 +
 *   静态单例反向刷新"的网状耦合。页面状态用一个不可变 data class 描述，一律经
 *   [updateState] 以 reducer 方式更新（`updateState { copy(x = v) }`）。
 * - 一次性 [events]：导航、弹 Toast、触发文件选择等"消费一次"的副作用，由 Compose 侧
 *   在 LaunchedEffect 里 collect 后转交 [LegacyBridge] 或宿主 Activity 处理。
 * - fakefx 承接：遗留配置对象（Config / VersionSetting 等）仍是 fakefx Property，
 *   用 [asStateFlow] / [asMutableStateFlow] 转成 Flow 后投影进 UiState（单向）或直接
 *   双向桥接（见 docs/migration/bridge-api.md §3 的对照表）。
 *
 * 子类只需：声明 UiState/Event 两个类型 → 传入初始状态 → 暴露供 Compose 调用的
 * 语义化方法（setXxx / onXxx）。具体示例见 ui/bridge/example/LauncherSettingsViewModel.kt。
 *
 * @param S 页面 UI 状态（不可变 data class）
 * @param E 一次性事件（sealed interface / data class）
 */
abstract class FCLViewModel<S : Any, E : Any>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    /** 页面唯一 UI 状态流；Compose 侧用 collectAsStateWithLifecycle() 订阅。 */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    /** 当前状态快照（仅 ViewModel 内部使用）。 */
    protected val currentState: S get() = _uiState.value

    /** 以 reducer 方式更新状态：`updateState { copy(field = newValue) }`。 */
    protected fun updateState(reducer: S.() -> S) {
        _uiState.update(reducer)
    }

    private val _events = MutableSharedFlow<E>(extraBufferCapacity = EVENT_BUFFER_CAPACITY)

    /** 一次性事件流（导航 / Toast / 文件选择等）；无订阅者时事件缓存在 buffer 中。 */
    val events: SharedFlow<E> = _events.asSharedFlow()

    /** 发送一次性事件；buffer 满（极端情况）时丢弃并记警告，不挂起调用方。 */
    protected fun sendEvent(event: E) {
        if (!_events.tryEmit(event)) {
            Logging.LOG.log(Level.WARNING, "FCLViewModel: event buffer full, dropped $event")
        }
    }

    /**
     * 把任意 Flow 投影进 [uiState]：每来一个新值就 `updateState { reducer(it) }`。
     * 这是 fakefx 属性 → UiState 的标准接法：
     * ```kotlin
     * init { config.xxxProperty().asStateFlow().observeIntoState { copy(xxx = it) } }
     * ```
     */
    protected fun <T> Flow<T>.observeIntoState(reducer: S.(T) -> S) {
        viewModelScope.launch {
            collect { value -> updateState { reducer(value) } }
        }
    }

    /** fakefx 属性 → 只读 StateFlow 的便捷封装（scope 固定为 viewModelScope）。 */
    protected fun <T> ObservableValue<T>.asStateFlow(
        started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
    ): StateFlow<T> = toStateFlow(viewModelScope, started)

    /** fakefx Property → 可变 StateFlow 的便捷封装（双向，scope 固定为 viewModelScope）。 */
    protected fun <T> Property<T>.asMutableStateFlow(): MutableStateFlow<T> =
        toMutableStateFlow(viewModelScope)

    private companion object {
        const val EVENT_BUFFER_CAPACITY = 16
    }
}
