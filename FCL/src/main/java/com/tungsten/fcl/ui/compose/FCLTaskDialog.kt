package com.tungsten.fcl.ui.compose

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.TaskLabels
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue
import com.tungsten.fclcore.task.FetchTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.Lang
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.function.Consumer

/**
 * Miuix 版任务进度对话框（小步骤 3.2，interaction-map G5）：替代遗留
 * com.tungsten.fcl.ui.TaskDialog + TaskListPane（dialog_task.xml + ListView）。
 *
 * 三部分：
 * - [FCLTaskDialogState]：订阅 [TaskExecutor] 事件（阶段/任务增删/进度/失败）与
 *   下载速度事件总线（FetchTask.speedEvent），暴露 Compose 快照状态；
 *   显示名解析与遗留共用 [TaskLabels]；
 * - [FCLTaskDialogContent]：纯 Composable 渲染（阶段行 + 任务进度行 + 速度 + 取消按钮）；
 * - [MiuixTaskDialog]：命令式封装（继承 [FCLComposeDialog]），API 对齐遗留 TaskDialog
 *   （setTitle/setExecutor/setCancelAction），供 Java 调用点逐点替换。
 *
 * 与遗留实现的差异（有意）：
 * - TaskExecutor 无 removeTaskListener，dispose 后事件监听靠 disposed 标志短路
 *   （遗留 TaskListPane 同样不反注册，生命周期一致）；
 * - 进度 < 0 视为不确定进度（LinearProgressIndicator progress = null），
 *   遗留 FCLProgressBar 直接显示负值等价于空条，语义一致。
 */

// ---------------------------------------------------------------------------
// 状态
// ---------------------------------------------------------------------------

class FCLTaskDialogState(
    private val context: Context,
    private val executor: TaskExecutor,
) {

    enum class StageStatus { PENDING, ACTIVE, DONE, FAILED }

    class StageEntry(val stage: String, val message: String) {
        var status by mutableStateOf(StageStatus.PENDING)
        var count by mutableIntStateOf(0)
        var total by mutableIntStateOf(0)

        val label: String
            get() = if (total > 0) String.format("%s - %d/%d", message, count, total) else message
    }

    class TaskEntry(val task: Task<*>, val name: String, val indent: Boolean) {
        /** null = 不确定进度 */
        var progress by mutableStateOf<Float?>(null)
        var message by mutableStateOf<String?>(null)
        var failure by mutableStateOf<String?>(null)

        internal val bindings = mutableListOf<Pair<ObservableValue<*>, ChangeListener<*>>>()
    }

    /** 渲染行：阶段行与任务行按遗留 listBox 的交错顺序存放（任务插在其阶段之后）。 */
    sealed interface Row
    class StageRow(val entry: StageEntry) : Row
    class TaskRow(val entry: TaskEntry) : Row

    val rows = mutableStateListOf<Row>()
    var speed by mutableStateOf<String?>(null)
        private set

    private val ui = Schedulers.androidUIThread()
    @Volatile
    private var disposed = false
    private var attached = false

    private val speedHandler = Consumer<FetchTask.SpeedEvent> { event ->
        var speed = event.speed.toDouble()
        var unit = "B/s"
        if (speed > 1024) {
            speed /= 1024
            unit = "KB/s"
        }
        if (speed > 1024) {
            speed /= 1024
            unit = "MB/s"
        }
        val text = String.format("%.1f %s", speed, unit)
        ui.execute { if (!disposed) this.speed = text }
    }

    private val listener = object : TaskListener() {
        override fun onStart() {
            ui.execute {
                if (disposed) return@execute
                rows.clear()
                Lang.removingDuplicates(executor.stages).forEach { stage ->
                    rows.add(StageRow(StageEntry(stage, TaskLabels.resolveStageMessage(context, stage))))
                }
            }
        }

        override fun onReady(task: Task<*>) {
            val stage = task.stage ?: return
            ui.execute {
                if (disposed) return@execute
                findStage(stage)?.status = StageStatus.ACTIVE
            }
        }

        override fun onRunning(task: Task<*>) {
            if (!task.significance.shouldShow() || task.name == null) return

            TaskLabels.resolveTaskName(context, task)

            ui.execute {
                if (disposed) return@execute
                val stageRow = task.inheritedStage?.let { stage -> rows.filterIsInstance<StageRow>().firstOrNull { it.entry.stage == stage } }
                val entry = TaskEntry(task, task.name, indent = stageRow != null)
                bindTask(entry)
                val index = stageRow?.let { rows.indexOf(it) + 1 } ?: 0
                rows.add(index, TaskRow(entry))
            }
        }

        override fun onFinished(task: Task<*>) {
            task.stage?.let { stage ->
                ui.execute { if (!disposed) findStage(stage)?.status = StageStatus.DONE }
            }
            ui.execute {
                if (disposed) return@execute
                removeTask(task)
            }
        }

        override fun onFailed(task: Task<*>, throwable: Throwable) {
            task.stage?.let { stage ->
                ui.execute { if (!disposed) findStage(stage)?.status = StageStatus.FAILED }
            }
            // 与遗留一致：失败节点保留在列表中，显示错误信息、进度清零
            ui.execute {
                if (disposed) return@execute
                rows.filterIsInstance<TaskRow>().firstOrNull { it.entry.task === task }?.entry?.let { entry ->
                    unbindTask(entry)
                    entry.failure = throwable.localizedMessage
                    entry.progress = 0f
                }
            }
        }

        override fun onPropertiesUpdate(task: Task<*>) {
            if (task is Task.CountTask) {
                val stage = task.countStage ?: return
                ui.execute {
                    if (disposed) return@execute
                    findStage(stage)?.let { it.count += 1 }
                }
                return
            }
            val stage = task.stage ?: return
            ui.execute {
                if (disposed) return@execute
                val total = Lang.tryCast(task.properties["total"], Integer::class.java)
                    .map { it.toInt() }
                    .orElse(0)
                findStage(stage)?.total = total
            }
        }
    }

    private fun findStage(stage: String): StageEntry? =
        rows.filterIsInstance<StageRow>().firstOrNull { it.entry.stage == stage }?.entry

    private fun bindTask(entry: TaskEntry) {
        val progressListener = ChangeListener<Number> { _, _, newValue ->
            val v = newValue.toDouble()
            entry.progress = if (v < 0) null else v.toFloat().coerceIn(0f, 1f)
        }
        entry.task.progressProperty().addListener(progressListener)
        entry.bindings += entry.task.progressProperty() to progressListener

        val messageListener = ChangeListener<String> { _, _, newValue ->
            entry.message = newValue
        }
        entry.task.messageProperty().addListener(messageListener)
        entry.bindings += entry.task.messageProperty() to messageListener
    }

    private fun unbindTask(entry: TaskEntry) {
        entry.bindings.forEach { (observable, listener) ->
            @Suppress("UNCHECKED_CAST")
            (observable as ObservableValue<Any>).removeListener(listener as ChangeListener<Any>)
        }
        entry.bindings.clear()
    }

    private fun removeTask(task: Task<*>) {
        val row = rows.filterIsInstance<TaskRow>().firstOrNull { it.entry.task === task } ?: return
        unbindTask(row.entry)
        rows.remove(row)
    }

    /** 开始订阅执行器事件与速度总线。Compose 侧在 DisposableEffect 中调用，配对 [dispose]。 */
    fun attach() {
        if (attached) return
        attached = true
        executor.addTaskListener(listener)
        FetchTask.speedEvent.channel(FetchTask.SpeedEvent::class.java).registerWeak(speedHandler)
    }

    /** 停止订阅（幂等）。TaskExecutor 不支持反注册 TaskListener，用 disposed 标志短路。 */
    fun dispose() {
        if (disposed) return
        disposed = true
        FetchTask.speedEvent.channel(FetchTask.SpeedEvent::class.java).unregister(speedHandler)
        rows.filterIsInstance<TaskRow>().forEach { unbindTask(it.entry) }
    }
}

// ---------------------------------------------------------------------------
// 渲染
// ---------------------------------------------------------------------------

/**
 * 任务对话框内容（标题由外层 [FCLDialog]/[FCLDialogCard] 提供）。
 *
 * @param onCancel 取消按钮回调；传 null 时按钮置灰（对应遗留 setCancel(null)）。
 */
@Composable
fun FCLTaskDialogContent(
    state: FCLTaskDialogState,
    cancelText: String,
    onCancel: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 280.dp),
        ) {
            items(state.rows.size) { index ->
                when (val row = state.rows[index]) {
                    is FCLTaskDialogState.StageRow -> StageRowItem(row.entry)
                    is FCLTaskDialogState.TaskRow -> TaskRowItem(row.entry)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = state.speed ?: "",
                style = MiuixTheme.textStyles.footnote1,
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onCancel?.invoke() },
                enabled = onCancel != null,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(cancelText)
            }
        }
    }
}

@Composable
private fun StageRowItem(entry: FCLTaskDialogState.StageEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(
                when (entry.status) {
                    FCLTaskDialogState.StageStatus.PENDING -> R.drawable.ic_baseline_more_horiz_24
                    FCLTaskDialogState.StageStatus.ACTIVE -> R.drawable.ic_baseline_arrow_forward_24
                    FCLTaskDialogState.StageStatus.DONE -> R.drawable.ic_baseline_done_24
                    FCLTaskDialogState.StageStatus.FAILED -> R.drawable.ic_baseline_close_24
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MiuixTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = entry.label,
            style = MiuixTheme.textStyles.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TaskRowItem(entry: FCLTaskDialogState.TaskEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (entry.indent) 28.dp else 0.dp,
                top = 4.dp,
                bottom = 8.dp,
            ),
    ) {
        Text(
            text = entry.name,
            style = MiuixTheme.textStyles.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = entry.progress,
            modifier = Modifier.fillMaxWidth(),
        )
        entry.failure?.let {
            Spacer(Modifier.height(2.dp))
            Text(
                text = it,
                style = MiuixTheme.textStyles.footnote1,
                color = MiuixTheme.colorScheme.error,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        } ?: entry.message?.let {
            Spacer(Modifier.height(2.dp))
            Text(
                text = it,
                style = MiuixTheme.textStyles.footnote1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
