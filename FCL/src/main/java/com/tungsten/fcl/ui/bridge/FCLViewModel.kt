package com.tungsten.fcl.ui.bridge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tungsten.fclcore.util.Logging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
 * - 单一 [uiState]：Compose 页面的唯一渲染数据源，替代原来"View 直接读写共享状态 +
 *   静态单例反向刷新"的网状耦合。页面状态用一个不可变 data class 描述，一律经
 *   [updateState] 以 reducer 方式更新（`updateState { copy(x = v) }`）。
 * - 一次性 [events]：导航、弹 Toast、触发文件选择等"消费一次"的副作用，由 Compose 侧
 *   在 LaunchedEffect 里 collect 后转交 [LegacyBridge] 或宿主 Activity 处理。
 * - 配置对象承接：Config / VersionSetting 等配置对象的 `xxxFlow()`（StateFlow）
 *   直接投影进 UiState（见 docs/migration/bridge-api.md §3 的对照表）。
 *
 * 子类只需：声明 UiState/Event 两个类型 → 传入 Application 与初始状态 → 暴露供 Compose
 * 调用的语义化方法（setXxx / onXxx）。Application 由默认 ViewModelProvider.Factory
 * 经 CreationExtras（APPLICATION_KEY）注入，Compose 侧直接 `viewModel()` 获取，
 * 不需要手搓 initializer 捕获 LocalContext。
 * 具体示例见 ui/bridge/example/LauncherSettingsViewModel.kt。
 *
 * @param S 页面 UI 状态（不可变 data class）
 * @param E 一次性事件（sealed interface / data class）
 */
abstract class FCLViewModel<S : Any, E : Any>(
    application: Application,
    initialState: S,
) : AndroidViewModel(application) {

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
     * 这是配置 StateFlow → UiState 的标准接法：
     * ```kotlin
     * init { config.xxxFlow().observeIntoState { copy(xxx = it) } }
     * ```
     */
    protected fun <T> Flow<T>.observeIntoState(reducer: S.(T) -> S) {
        viewModelScope.launch {
            collect { value -> updateState { reducer(value) } }
        }
    }

    private companion object {
        const val EVENT_BUFFER_CAPACITY = 16
    }
}
