package com.tungsten.fcllibrary.component.ui

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.function.Consumer

/**
 * FCLPage 无生命周期，页面随 ViewPager 回收/覆盖层弹出销毁：
 * 数据流的收集按 attach 状态启停——attach 时开始 collect 并渲染，detach 时取消，
 * 避免页面销毁后仍持有数据回调；StateFlow collect 会立即重放当前值，
 * 页面重建（重新 attach）时自动恢复到最新状态，无需外部补偿调用。
 */
fun <T> FCLPage.observeWhileAttached(flow: Flow<T>, render: (T) -> Unit) {
    var job: Job? = null
    val startCollect: () -> Unit = {
        job?.cancel()
        job = getActivity().lifecycleScope.launch {
            flow.collect { render(it) }
        }
    }
    contentView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            startCollect()
        }

        override fun onViewDetachedFromWindow(v: View) {
            job?.cancel()
            job = null
        }
    })
    // 页面构造器内注册时视图可能已经 attach（之后不再有 attach 事件）
    if (contentView.isAttachedToWindow) {
        startCollect()
    }
}

/** Java 侧入口：页面存续期间观察数据流并渲染 */
object PageFlows {

    @JvmStatic
    fun <T> observe(page: FCLPage, flow: Flow<T>, consumer: Consumer<T>) {
        page.observeWhileAttached(flow) { consumer.accept(it) }
    }
}
