package com.mio.data

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce

/**
 * 已安装模组目录变化的响应式信号（tick）：
 * 下载模组落盘（单个/一键含前置/收藏批量）与模组更新完成后 [notifyChanged]，
 * 模组管理页 collect [events] 自动重载列表。
 *
 * 典型时序：下载发生在下载页（管理页 detach、错过实时 tick），
 * 重进管理页时 collect 重新开始并重放当前 tick → 恰好触发一次重载；
 * 管理页可见期间（如模组更新临时页）则实时重载。
 */
@OptIn(FlowPreview::class)
object ModsChanged {

    private val _tick = MutableStateFlow(0)

    @get:JvmName("getTickFlow")
    @JvmStatic
    val tick: StateFlow<Int> = _tick.asStateFlow()

    /** 收敛 300ms 内的连续变化（批量下载逐文件落盘），只触发一次重载 */
    @get:JvmName("getEventsFlow")
    @JvmStatic
    val events: Flow<Int> = tick.debounce(300)

    @JvmStatic
    fun notifyChanged() {
        _tick.value += 1
    }
}
