package com.mio.download

import com.mio.util.acquireDownloadWakeLock
import com.mio.util.releaseDownloadWakeLock
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** 一条进行中的下载：title 为列表标题，task 提供进度/消息，executor 用于取消 */
data class DownloadTaskInfo(
    val id: Long,
    val title: String,
    val task: Task<*>,
    val executor: TaskExecutor
)

/**
 * 全局下载管理器：非阻塞下载的注册中心。
 * 简单文件下载不再弹模态对话框，改为提交到此处，
 * 主界面的悬浮按钮/侧滑面板据此实时显示进度并可取消；
 * 下载期间持后台保活锁，任务链结束（成功/失败/取消）自动移除并释放。
 */
object DownloadManager {

    private val _tasks = MutableStateFlow<List<DownloadTaskInfo>>(emptyList())
    val tasks: StateFlow<List<DownloadTaskInfo>> = _tasks.asStateFlow()

    private var idCounter = 0L

    @JvmStatic
    fun submit(title: String, task: Task<*>, executor: TaskExecutor): DownloadTaskInfo {
        val info = DownloadTaskInfo(++idCounter, title, task, executor)
        _tasks.update { it + info }
        acquireDownloadWakeLock()
        executor.addTaskListener(object : TaskListener() {
            override fun onStop(success: Boolean, executor: TaskExecutor) {
                Schedulers.androidUIThread().execute {
                    _tasks.update { list -> list.filterNot { it.id == info.id } }
                    releaseDownloadWakeLock()
                    executor.removeTaskListener(this)
                }
            }
        })
        return info
    }

    @JvmStatic
    fun cancel(info: DownloadTaskInfo) {
        info.executor.cancel()
    }
}