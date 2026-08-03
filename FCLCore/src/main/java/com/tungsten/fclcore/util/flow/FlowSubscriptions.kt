package com.tungsten.fclcore.util.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.function.Consumer

/**
 * StateFlow 的 Java 友好订阅入口。
 *
 * Java 调用方无法直接驱动挂起的 `collect`，这里提供回调式适配：
 * 订阅在共享作用域内收集 Flow，每收到一个值就调用 [Consumer.accept]。
 *
 * 线程模型：作用域使用 [Dispatchers.Unconfined]，回调在**发射线程**上同步执行，
 * 与被替代的旧属性监听器语义一致（不做隐式线程切换，
 * 需要切线程的调用方自行在回调内调度）。
 *
 * 注意：[StateFlow] 是合并（conflated）流，高频连续发射可能被合并为一次回调；
 * 消费方应当幂等（进度条刷新、存盘触发等场景均满足）。
 */
object FlowSubscriptions {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    /** 可取消的订阅句柄。 */
    fun interface Subscription {
        fun cancel()
    }

    /**
     * 订阅 Flow 的后续变化（对齐 `addListener` 语义：注册时不为当前值回调）。
     *
     * 实现注意：不能用 `drop(1)`——Unconfined 作用域下，若订阅后、收集协程启动前
     * 值已变化（例如在另一个 Unconfined 回调里 setValue 的重入场景），收集到的
     * 第一个值会是**新值**而非订阅时的当前值，`drop(1)` 会把它误吞。
     * 这里在订阅时刻快照当前值，仅当收集到的首值仍等于快照时才跳过。
     */
    @JvmStatic
    fun <T> subscribe(flow: StateFlow<T>, consumer: Consumer<in T>): Subscription {
        val atSubscribe = flow.value
        val job = scope.launch {
            var first = true
            flow.collect { v ->
                if (first) {
                    first = false
                    if (v == atSubscribe) return@collect
                }
                consumer.accept(v)
            }
        }
        return Subscription { job.cancel() }
    }

    /**
     * 订阅 Flow 并立即回调当前值（对齐 `bind` 语义：先同步当前值，再跟踪后续变化）。
     */
    @JvmStatic
    fun <T> subscribeWithCurrent(flow: StateFlow<T>, consumer: Consumer<in T>): Subscription {
        val job = scope.launch {
            flow.collect { consumer.accept(it) }
        }
        return Subscription { job.cancel() }
    }
}
