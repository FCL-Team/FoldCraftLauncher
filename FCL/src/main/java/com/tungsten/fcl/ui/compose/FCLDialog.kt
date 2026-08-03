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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLCornerRadius
import androidx.compose.foundation.layout.ColumnScope
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextButtonColors
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
 * 长内容处理（真机 bug 修复）：标题/按钮区钉在内容槽上下、不参与滚动；
 * 内容槽（含 summary）以 weight(1f, fill = false) 限高 + verticalScroll 滚动，
 * 短内容时收缩为 wrap_content（视觉与修复前一致），长内容时按钮始终可见不被挤出。
 * 内容里自带滚动/限高（如 heightIn 限高的 LazyColumn）的弹窗传 scrollable = false，
 * 此时内容槽仅限高（weight 区域给有界最大高度），不再叠加 verticalScroll，避免双重滚动。
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
        // 对齐遗留 dialog_background（#F4F4F4 / #232323）→ surface token，
        // 不用 Miuix 默认 background（light 为纯白）
        backgroundColor = MiuixTheme.colorScheme.surface,
    ) {
        // WindowDialog 的内容槽是普通 Column（手机端不限高、不滚动），
        // 这里自包一层 Column 让 FCLDialogBody 的 weight 限高生效。
        Column {
            FCLDialogBody(summary = null, buttons = buttons, content = content?.let { c -> { c() } })
        }
    }
}

/**
 * 对话框卡片 UI（自带 window 的命令式对话框 [FCLComposeDialog] 使用）。
 * 视觉对齐遗留 dialog_alert：minWidth 350dp、padding 15dp、标题居中。
 *
 * @param scrollable 内容槽是否叠加 verticalScroll（默认 true）。内容自带滚动/限高
 * （如 heightIn 限高的 LazyColumn）时传 false，避免双重滚动/无限高度测量；
 * 两种模式下内容槽都会被 weight(1f, fill = false) 限制在标题与按钮区之间。
 * @param bottomContent 钉在内容槽之下、按钮区之上的自定义区域（不参与滚动），
 * 供需要自定义按钮形态（如长按）的弹窗使用。
 */
@Composable
fun FCLDialogCard(
    title: String? = null,
    summary: String? = null,
    buttons: List<FCLDialogButton> = emptyList(),
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    bottomContent: (@Composable () -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    FCLCard(
        cornerRadius = FCLCornerRadius.Dialog,
        modifier = modifier
            .padding(24.dp)
            .widthIn(min = 350.dp, max = 560.dp)
            .fillMaxWidth(),
        insideMargin = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        // 对齐遗留 dialog_background（#F4F4F4 / #232323）→ surface token，
        // 不用 Miuix Card 默认 surfaceContainer（light 为纯白）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.surface),
    ) {
        Column {
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
            FCLDialogBody(
                summary = summary,
                buttons = buttons,
                scrollable = scrollable,
                bottomContent = bottomContent,
                content = content,
            )
        }
    }
}

/**
 * summary + 内容槽 + 按钮区的公共部分（[FCLDialog] 的 summary 由 WindowDialog 自绘，传 null）。
 *
 * 内容槽（含 summary）放在 weight(1f, fill = false) 区域内：短内容收缩、视觉不变；
 * 长内容被压在标题与按钮区之间滚动，按钮区始终钉在底部可见。
 * 调用方必须处于 ColumnScope（[FCLDialog] / [FCLDialogCard] 均自包 Column）。
 */
@Composable
private fun ColumnScope.FCLDialogBody(
    summary: String?,
    buttons: List<FCLDialogButton>,
    scrollable: Boolean = true,
    bottomContent: (@Composable () -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)?,
) {
    if (summary != null || content != null) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
        ) {
            summary?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.body2,
                )
                Spacer(Modifier.height(12.dp))
            }
            content?.invoke(this)
        }
    }
    bottomContent?.let {
        Spacer(Modifier.height(12.dp))
        it()
    }
    if (buttons.isNotEmpty()) {
        Spacer(Modifier.height(12.dp))
        FCLDialogButtonsRow(buttons)
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
                colors = fclDialogTextButtonColors(),
            )
        }
    }
}

/**
 * 遗留 FCLButton 默认（非 ripple）配色的 Compose 等价（FCLButton.java applyTheme）：
 * 透明底 + ltColor（= primaryContainer）文字，跟随主题色实时变化；
 * 禁用时旧版 setTextColor 为纯色、无状态列表，文字仍显示 ltColor，
 * 故 disabledTextColor 同样取 primaryContainer（对齐旧视觉，不用 Miuix 默认灰）。
 * Miuix TextButton 默认 secondaryVariant/onSecondaryVariant 灰系不跟随主题色，不能使用。
 */
@Composable
fun fclDialogTextButtonColors(): TextButtonColors = ButtonDefaults.textButtonColors(
    color = Color.Transparent,
    disabledColor = Color.Transparent,
    textColor = MiuixTheme.colorScheme.primaryContainer,
    disabledTextColor = MiuixTheme.colorScheme.primaryContainer,
)
