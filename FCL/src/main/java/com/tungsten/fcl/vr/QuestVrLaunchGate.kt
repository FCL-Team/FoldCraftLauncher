package com.tungsten.fcl.vr

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * VR 启动阶段门：LOADING（游戏启动中）→ SWITCHING（Vivecraft 已请求 OpenXR，等待沉浸面板恢复）
 * → READY（沉浸面板已接管）/ FLAT（无 OpenXR 请求或已退出，回到平面面板）/ FAILED
 */
class QuestVrLaunchGate {
    enum class State { LOADING, FLAT, SWITCHING, READY, FAILED }

    private val lock = ReentrantLock()
    private val stateChanged = lock.newCondition()
    private var currentState = State.LOADING
    private var immersiveResumed = false

    val state: State
        get() = lock.withLock { currentState }

    val wantsImmersive: Boolean
        get() = lock.withLock {
            currentState == State.SWITCHING || currentState == State.READY
        }

    /**
     * 阻塞等待沉浸面板就绪（由 VLoader 的 OpenXR 初始化线程调用）；
     * 首次进入时执行 request 发起面板切换，超时按失败处理
     */
    @Throws(InterruptedException::class)
    fun awaitReady(request: Runnable, timeoutMillis: Long): Boolean {
        val first = lock.withLock {
            val needsRequest = currentState == State.LOADING || currentState == State.FLAT
                    || (currentState == State.READY && !immersiveResumed)
            if (needsRequest) currentState = State.SWITCHING
            needsRequest
        }
        if (first) {
            try {
                request.run()
            } catch (failure: RuntimeException) {
                fail()
                throw failure
            }
        }
        var remaining = TimeUnit.MILLISECONDS.toNanos(timeoutMillis)
        lock.withLock {
            while (currentState == State.SWITCHING) {
                if (remaining <= 0) {
                    fail()
                    break
                }
                try {
                    remaining = stateChanged.awaitNanos(remaining)
                } catch (interrupted: InterruptedException) {
                    fail()
                    throw interrupted
                }
            }
            return currentState == State.READY && immersiveResumed
        }
    }

    /** 沉浸面板已恢复，OpenXR 可以开始渲染 */
    fun ready(): Boolean = lock.withLock {
        if (currentState != State.SWITCHING && currentState != State.READY) return false
        currentState = State.READY
        immersiveResumed = true
        stateChanged.signalAll()
        true
    }

    fun paused() {
        lock.withLock { immersiveResumed = false }
    }

    /** 游戏未经 OpenXR 请求就开始渲染：回落平面面板 */
    fun firstGameFrame(): Boolean = lock.withLock {
        if (currentState != State.LOADING) return false
        currentState = State.FLAT
        true
    }

    /** OpenXR 停止（正常退出或初始化失败），回到平面面板；返回值表示状态是否发生变化 */
    fun flat(): Boolean = lock.withLock {
        if (currentState == State.FAILED) return false
        val changed = currentState != State.FLAT
        currentState = State.FLAT
        immersiveResumed = false
        stateChanged.signalAll()
        changed
    }

    fun fail() {
        lock.withLock {
            currentState = State.FAILED
            stateChanged.signalAll()
        }
    }
}
