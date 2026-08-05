package com.tungsten.fcl.ui.compose

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator

/**
 * Miuix 版任务进度对话框的命令式封装（小步骤 3.2）：API 对齐遗留
 * com.tungsten.fcl.ui.TaskDialog，供 19 处 Java/Kotlin 调用点逐点替换。
 *
 * 与遗留 TaskDialog 的对应关系：
 * - `new TaskDialog(context, TaskCancellationAction.NORMAL)` → `MiuixTaskDialog(context)`
 *   （取消按钮可用、点击后 executor.cancel() + 动作 + dismiss；dismiss 由本类自动完成）；
 * - `new TaskDialog(context, new TaskCancellationAction(AppCompatDialog::dismiss))` → 同上
 *   （遗留的 dismiss 动作在本类中是内置行为，无需再传）；
 * - `setCancel(null)`（按钮置灰）→ `setCancelAction(null)`；
 * - `setExecutor(executor[, autoClose])`、`setTitle(...)`、`show()` 语义一致。
 *
 * 遗留 TaskCancellationAction 现以无参数 Runnable 为取消动作契约；本类继续使用直接的
 * Runnable 取消回调，避免与任一具体对话框类型耦合。
 */
class MiuixTaskDialog @JvmOverloads constructor(
    context: Context,
    private var cancelAction: Runnable? = Runnable {},
) : FCLComposeDialog(context, cancelable = false) {

    private val titleState = mutableStateOf<String?>(null)
    private val dialogState = mutableStateOf<FCLTaskDialogState?>(null)
    private val cancelActionState = mutableStateOf<Runnable?>(cancelAction)

    private var executor: TaskExecutor? = null

    init {
        setDialogContent {
            FCLDialogCard(
                title = titleState.value,
                scrollable = false,
            ) {
                val state = dialogState.value
                if (state != null) {
                    DisposableEffect(state) {
                        state.attach()
                        onDispose { state.dispose() }
                    }
                    FCLTaskDialogContent(
                        state = state,
                        cancelText = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onCancel = cancelActionState.value?.let { action ->
                            { onCancelClicked(action) }
                        },
                    )
                } else {
                    // setExecutor 前的占位（遗留行为：空列表 + 禁用取消按钮）
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    /** 对应遗留 TaskDialog.setTitle。 */
    fun setTitle(title: String?) {
        titleState.value = title
    }

    fun getTitle(): String? = titleState.value

    /** 对应遗留 TaskDialog.setCancel：传 null 取消按钮置灰。 */
    fun setCancelAction(action: Runnable?) {
        cancelAction = action
        cancelActionState.value = action
    }

    @JvmOverloads
    fun setExecutor(executor: TaskExecutor?, autoClose: Boolean = true) {
        this.executor = executor
        if (executor != null) {
            if (autoClose) {
                executor.addTaskListener(object : TaskListener() {
                    override fun onStop(success: Boolean, executor: TaskExecutor) {
                        Schedulers.androidUIThread().execute { dismiss() }
                    }
                })
            }
            dialogState.value = FCLTaskDialogState(context, executor)
        }
    }

    private fun onCancelClicked(action: Runnable) {
        executor?.cancel()
        action.run()
        dismiss()
    }

    override fun dismiss() {
        dialogState.value?.dispose()
        dialogState.value = null
        super.dismiss()
    }
}
