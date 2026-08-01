package com.tungsten.fcl.ui.bridge

import com.tungsten.fclcore.fakefx.beans.property.Property
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * fakefx 属性体系 → Kotlin Flow 的桥接适配器（小步骤 2.3）。
 *
 * 背景（interaction-map.md G4）：fakefx 双向绑定渗透全部表单（VersionSettingPage 单页
 * 17 组 FXUtils.bindXxx / bindBidirectional 绑定），迁移到 Compose 后统一转为 State/Flow，
 * 遗留数据层（Config / VersionSetting / Theme 等）的 fakefx Property 保持不变，
 * 由本文件的适配器在边界处转换——**不改 FCLCore/FCL 既有数据类**。
 */

/**
 * fakefx [ObservableValue] → 只读 [StateFlow]（单向承接）。
 *
 * 等价于旧代码中 View 属性 `bind(observable)` 的单向绑定：属性的每次变更流入 Flow。
 * 监听通过 callbackFlow 注册，Flow 关闭时自动 removeListener，不会泄漏。
 * 同值（equals）不重复发射。
 */
fun <T> ObservableValue<T>.toStateFlow(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
): StateFlow<T> = callbackFlow {
    val listener = ChangeListener<T> { _, _, newValue -> trySend(newValue) }
    addListener(listener)
    trySend(value)
    awaitClose { removeListener(listener) }
}.distinctUntilChanged().stateIn(scope, started, value)

/**
 * fakefx [Property] → 可变 [MutableStateFlow]（双向承接）。
 *
 * 等价于旧代码的 `bindBidirectional`：
 * - 写 `flow.value = x` → 回写 Property（若值有变化）；
 * - Property 被其他遗留代码修改 → 流入 Flow。
 *
 * 回环防护：StateFlow 对 equals 同值合并（Property 监听器把回写的值再次塞进 Flow 时
 * 不会触发新的收集），且收集侧写入前先做 `value != newValue` 判断，不会死循环。
 * scope 取消时（如 ViewModel.onCleared）自动 removeListener。
 *
 * 典型用法（Compose 表单字段）：
 * ```kotlin
 * val threads = config.downloadThreadsProperty().toMutableStateFlow(viewModelScope)
 * // Compose: SliderPreference(value = threads.collectAsStateWithLifecycle().value, ...)
 * ```
 */
fun <T> Property<T>.toMutableStateFlow(scope: CoroutineScope): MutableStateFlow<T> {
    val flow = MutableStateFlow(value)
    val listener = ChangeListener<T> { _, _, newValue -> flow.value = newValue }
    addListener(listener)
    scope.launch {
        try {
            flow.collect { newValue ->
                if (value != newValue) value = newValue
            }
        } finally {
            removeListener(listener)
        }
    }
    return flow
}
