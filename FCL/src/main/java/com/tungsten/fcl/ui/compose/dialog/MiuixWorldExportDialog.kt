package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.game.World
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.platform.OperatingSystem
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.nio.file.Paths

/**
 * Miuix 版世界导出弹窗（3.2 批 1，对应 ui/manage/WorldExportDialog + dialog_world_export 本体；
 * 其内嵌任务弹窗此前已接 MiuixTaskDialog）。
 *
 * 行为对齐：
 * - 初始值：文件名 = 世界名 + ".zip"，世界名 = 世界名；
 * - 确定按钮禁用条件一致：世界名为空 || 文件名空白 || 文件名非法 || 目标文件已存在；
 * - 确定后执行导出任务（MiuixTaskDialog，标题 message_doing），成功弹 message_success
 *   （确定后 dismissAllTempPagesCreatedByPage(PAGE_ID_MANAGE_WORLD)），失败弹 message_failed +
 *   堆栈（executor.exception 为 null 时不弹，与遗留一致）；随后 dismiss 本弹窗；
 * - 取消按钮 dismiss；setCancelable(false) 一致。
 */
class MiuixWorldExportDialog(
    context: Context,
    private val world: World,
    private val parent: String,
) : FCLComposeDialog(context, cancelable = false) {

    private val fileNameState = mutableStateOf(world.worldName + ".zip")
    private val nameState = mutableStateOf(world.worldName)

    init {
        setDialogContent {
            val positiveEnabled = nameState.value.isNotEmpty()
                    && !StringUtils.isBlank(fileNameState.value)
                    && OperatingSystem.isNameValid(fileNameState.value)
                    && !File(parent, fileNameState.value).exists()
            FCLDialogCard(
                title = stringResource(R.string.world_export),
                buttons = listOf(
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        enabled = positiveEnabled,
                        onClick = { onPositive() },
                    ),
                    FCLDialogButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                        onClick = { dismiss() },
                    ),
                ),
            ) {
                Text(
                    text = stringResource(R.string.archive_name),
                    style = MiuixTheme.textStyles.body2,
                )
                FCLTextField(
                    value = fileNameState.value,
                    onValueChange = { fileNameState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Text(
                    text = stringResource(R.string.world_name),
                    style = MiuixTheme.textStyles.body2,
                )
                FCLTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        }
    }

    private fun onPositive() {
        val ctx = context
        val task = Task.runAsync(
            AndroidUtils.getLocalizedText(ctx, "world.export.wizard", nameState.value)
        ) {
            world.export(
                Paths.get(File(parent, fileNameState.value).absolutePath),
                nameState.value,
            )
        }
        val executor = task.executor(object : TaskListener() {
            override fun onStop(success: Boolean, executor: TaskExecutor) {
                Schedulers.androidUIThread().execute {
                    if (success) {
                        FCLDialogs.showAlert(
                            ctx,
                            null,
                            ctx.getString(R.string.message_success),
                            positiveText = ctx.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                            onResult = {
                                ManagePageManager.instance
                                    ?.dismissAllTempPagesCreatedByPage(ManagePageManager.PAGE_ID_MANAGE_WORLD)
                            },
                            cancelable = false,
                        )
                    } else {
                        val exception = executor.exception ?: return@execute
                        FCLDialogs.showAlert(
                            ctx,
                            ctx.getString(R.string.message_failed),
                            StringUtils.getStackTrace(exception),
                            positiveText = ctx.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                            cancelable = false,
                        )
                    }
                }
            }
        })
        val taskDialog = MiuixTaskDialog(ctx)
        taskDialog.setTitle(ctx.getString(R.string.message_doing))
        taskDialog.setExecutor(executor)
        taskDialog.show()
        executor.start()
        dismiss()
    }
}
