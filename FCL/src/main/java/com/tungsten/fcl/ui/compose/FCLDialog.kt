package com.tungsten.fcl.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Card
import androidx.compose.foundation.layout.ColumnScope
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.window.WindowDialog

/**
 * FCL 统一对话框基座（小步骤 3.2）。
 *
 * 设计目标：用一套 API 覆盖遗留 FCLAlertDialog / FCLLibrary ProgressDialog 的使用模式：
 * - 标题（title）+ 摘要（summary，对应 FCLAlertDialog.setMessage）；
 * - 内容槽（content，对应自定义 setContentView，如进度条/列表/取色器）；
 * - 按钮区（buttons，对应 setPositiveButton/setNegativeButton/setNeutralButton/setExtraButton，
 *   按声明顺序从右往左排列——首个为主按钮）；
 * - 取消策略（onDismissRequest = null 即 setCancelable(false)，对齐 Miuix WindowDialog 语义）。
 *
 * 两个渲染形态：
 * - [FCLDialog]：Compose 页面内使用，基于 Miuix window/WindowDialog（平台 Dialog window，
 *   无 Scaffold 依赖，见 theme-mapping.md §1.2 与 bridge-api.md §2.3）；
 * - [FCLDialogCard]：纯卡片 UI，供 [FCLComposeDialog]（自带平台 window 的命令式封装，
 *   供 Java 遗留代码直接 show/dismiss）渲染同一份视觉。
 */

/** 对话框按钮模型。Java 侧可 `new FCLDialogButton("确定", () -> {...})` 直接构造。 */
class FCLDialogButton @JvmOverloads constructor(
    val text: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
)

/**
 * Compose 页面内使用的通用对话框（WindowDialog 封装）。
 *
 * @param show 是否显示（WindowDialog 自带进出场动画，直接传状态即可）。
 * @param onDismissRequest 点遮罩/返回键回调；传 null = 不可取消（setCancelable(false) 语义）。
 * @param buttons 按钮列表，从右往左排列（第一个是最右侧主按钮）；为空则不渲染按钮区。
 * @param content 内容槽，位于 summary 与按钮区之间。
 */
@Composable
fun FCLDialog(
    show: Boolean,
    onDismissRequest: (() -> Unit)?,
    title: String? = null,
    summary: String? = null,
    buttons: List<FCLDialogButton> = emptyList(),
    content: (@Composable () -> Unit)? = null,
) {
    WindowDialog(
        show = show,
        title = title,
        summary = summary,
        onDismissRequest = onDismissRequest,
    ) {
        FCLDialogBody(summary = null, buttons = buttons, content = content?.let { c -> { c() } })
    }
}

/**
 * 对话框卡片 UI（自带 window 的命令式对话框 [FCLComposeDialog] 使用）。
 * 视觉对齐遗留 dialog_alert：minWidth 350dp、padding 15dp、标题居中。
 */
@Composable
fun FCLDialogCard(
    title: String? = null,
    summary: String? = null,
    buttons: List<FCLDialogButton> = emptyList(),
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .padding(24.dp)
            .widthIn(min = 350.dp, max = 560.dp)
            .fillMaxWidth(),
        insideMargin = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
        ) {
            title?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(12.dp))
            }
            FCLDialogBody(summary = summary, buttons = buttons) {
                content?.invoke(this)
            }
        }
    }
}

/** summary + 内容槽 + 按钮区的公共部分（[FCLDialog] 的 summary 由 WindowDialog 自绘，传 null）。 */
@Composable
private fun FCLDialogBody(
    summary: String?,
    buttons: List<FCLDialogButton>,
    content: (@Composable ColumnScope.() -> Unit)?,
) {
    Column {
        summary?.let {
            Text(
                text = it,
                modifier = Modifier.fillMaxWidth(),
                style = MiuixTheme.textStyles.body2,
            )
            Spacer(Modifier.height(12.dp))
        }
        content?.invoke(this)
        if (buttons.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            FCLDialogButtonsRow(buttons)
        }
    }
}

/** 按钮区：右对齐横排，[buttons] 首个元素在最右（主按钮位）。 */
@Composable
fun FCLDialogButtonsRow(buttons: List<FCLDialogButton>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        buttons.asReversed().forEachIndexed { index, button ->
            if (index > 0) Spacer(Modifier.width(8.dp))
            TextButton(
                text = button.text,
                onClick = button.onClick,
                enabled = button.enabled,
            )
        }
    }
}
