package com.tungsten.fcl.ui.compose

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import java.util.function.Consumer

/**
 * 命令式 Compose 对话框基座（小步骤 3.2）：AppCompatDialog + ComposeView，
 * 让未迁移的 Java/Kotlin View 代码可以像遗留 FCLDialog 一样 `show()`/`dismiss()`
 * 一个 Miuix 风格弹窗，无需 Compose 宿主（不依赖 LegacyDialogHost 是否安装）。
 *
 * 与遗留 FCLDialog 对齐的行为：
 * - 构造时应用 ThemeEngine 全屏标记（applyFullscreen）；
 * - window 背景透明，遮罩/圆角由 Compose 卡片（[FCLDialogCard]）自绘；
 * - setCancelable 同时控制返回键与遮罩点击。
 *
 * 生命周期：组合策略 DisposeOnDetachedFromWindow，dismiss 后组合自动释放。
 * 子类在构造后调用 [setDialogContent] 声明内容；内容里的状态应使用 Compose
 * 状态（mutableStateOf），set 类方法只改状态、不重复 setContent。
 */
open class FCLComposeDialog @JvmOverloads constructor(
    context: Context,
    cancelable: Boolean = true,
) : AppCompatDialog(context) {

    private val composeView = ComposeView(context)

    init {
        ThemeEngine.getInstance().applyFullscreen(window, ThemeEngine.getInstance().theme.isFullscreen)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
    }

    internal fun setDialogContent(content: @Composable () -> Unit) {
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        composeView.setContent {
            FCLTheme(context) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    content()
                }
            }
        }
        setContentView(composeView)
    }

    override fun onStart() {
        super.onStart()
        // 遮罩区域铺满宽度，内容（卡片）wrap_content 居中；高度自适应内容
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}

/**
 * Java 友好的通用弹窗工厂（覆盖 FCLAlertDialog / FCLLibrary ProgressDialog 的典型用法）。
 *
 * 这些是迁移期过渡 API：前台已是 Compose 页面的场景应优先走
 * LegacyBridge.requestAlertDialog / requestTaskDialog 通道（bridge-api.md §2.3）。
 */
object FCLDialogs {

    /**
     * 确认/信息弹窗（对应 FCLAlertDialog.Builder 的 title/message/正负按钮形态）。
     *
     * @param onResult true=点了确定；false=取消按钮/返回键/遮罩点击；可为 null。
     * @return 已 show 的对话框句柄，调用方可持有并 dismiss（对齐 FCLAlertDialog 用法）。
     */
    @JvmStatic
    @JvmOverloads
    fun showAlert(
        context: Context,
        title: String?,
        message: String?,
        positiveText: String? = null,
        negativeText: String? = null,
        onResult: Consumer<Boolean>? = null,
        cancelable: Boolean = true,
    ): FCLComposeDialog {
        val dialog = FCLComposeDialog(context, cancelable = cancelable)
        dialog.setOnCancelListener { onResult?.accept(false) }
        dialog.setDialogContent {
            FCLDialogCard(
                title = title,
                summary = message,
                buttons = buildList {
                    add(FCLDialogButton(
                        text = positiveText ?: context.getString(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = {
                            dialog.dismiss()
                            onResult?.accept(true)
                        },
                    ))
                    negativeText?.let { negative ->
                        add(FCLDialogButton(
                            text = negative,
                            onClick = {
                                dialog.dismiss()
                                onResult?.accept(false)
                            },
                        ))
                    }
                },
            )
        }
        dialog.show()
        return dialog
    }

    /**
     * 不确定进度弹窗（对应 FCLLibrary ui/ProgressDialog：不可取消、居中进度指示）。
     * 用法与遗留一致：`val d = FCLDialogs.showProgress(context)` … `d.dismiss()`。
     */
    @JvmStatic
    fun showProgress(context: Context): FCLComposeDialog {
        val dialog = FCLComposeDialog(context, cancelable = false)
        dialog.setDialogContent {
            FCLDialogCard {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        dialog.show()
        return dialog
    }
}
