package com.tungsten.fcl.ui.compose.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tungsten.fcl.R
import com.tungsten.fcl.control.view.KeycodeView
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDialogButtonsRow
import com.tungsten.fclcore.observable.property.SimpleIntegerProperty
import top.yukonga.miuix.kmp.basic.Card

/**
 * Miuix 版键码选择弹窗（4.1，对应 control/SelectKeycodeDialog + dialog_select_keycode，
 * 3.2 批 2 时 deferred 的唯一弹窗）。
 *
 * 处置决策（对齐 component-mapping.md §1.5）：KeycodeView 与 view_keyboard（1502 行 XML）
 * 属红线保留原生——KeycodeView 自带手写触摸判定（10px/200ms 点按识别，interaction-map §9
 * 阈值），整体 Compose 重写需逐键转写 100+ 键码且要复刻手势阈值，风险高收益低。
 * 因此采用 **Miuix 外壳 + AndroidView 包装原生键盘**：弹窗卡片/确定按钮 Compose 化，
 * dialog_select_keycode 布局原样 inflate 进 AndroidView，KeycodeView 触摸事件链零改动。
 *
 * 与遗留实现对齐的行为：
 * - setCancelable(false)（构造参数 cancelable = false）；
 * - 递归遍历 parent_layout 全部 KeycodeView 注册多选/单选监听（单选禁止取消选中，
 *   取消时 setSelectedWithoutCallback(true) 回弹）；
 * - mouse 段按参数显隐；确定回调后 dismiss；
 * - 原生确定按钮行隐藏，由 Miuix 卡片按钮区替代。
 *
 * 卡片宽度说明：view_keyboard 固定 600dp，超出 FCLDialogCard 的 560dp 上限，
 * 故此处自建 Card（wrapContentWidth，上限 640dp），不用 FCLDialogCard。
 *
 * 运行于游戏内（GameMenu/EditViewDialog → ControllerActivity），
 * AppCompatDialog + ComposeView 可用（同批 2/批 4 各 Miuix 游戏内弹窗）。
 */
class MiuixSelectKeycodeDialog(
    context: Context,
    private val list: MutableList<Int>,
    private val singleSelection: Boolean,
    mouse: Boolean,
) : FCLComposeDialog(context, cancelable = false) {

    private val selectionProperty = SimpleIntegerProperty(this, "selection", -1)

    /** 与遗留 SelectKeycodeDialog.container 对齐：parent_layout 引用，供单选时整体刷新选中态。 */
    private var container: ViewGroup? = null

    private var onConfirm: (MiuixSelectKeycodeDialog) -> Unit = {}

    /**
     * 多选模式下每次增删键码后的回调（阶段 4b 新增：数据层列表已快照化，
     * 需要即时回写时由调用方设置；默认空实现对齐遗留"临时列表确认后回读"用法）。
     */
    var onChanged: (List<Int>) -> Unit = {}

    fun selectionProperty(): SimpleIntegerProperty {
        return selectionProperty
    }

    init {
        if (singleSelection) {
            selectionProperty.set(list[0])
        }

        setDialogContent {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .wrapContentWidth()
                    .widthIn(max = 640.dp),
                insideMargin = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            ) {
                Column {
                    // 键盘区限高随屏幕收缩：游戏内横屏（屏高常仅 360~410dp）下固定 400dp
                    // 会把确定按钮挤出屏幕；预留 ~150dp 给卡片边距与按钮区
                    AndroidView(
                        modifier = Modifier.heightIn(
                            max = minOf(
                                400.dp,
                                (LocalConfiguration.current.screenHeightDp - 150).dp,
                            ),
                        ),
                        factory = { ctx ->
                            LayoutInflater.from(ctx)
                                .inflate(R.layout.dialog_select_keycode, null)
                                .apply {
                                    // 原生确定按钮行由 Miuix 按钮区替代
                                    (findViewById<View>(R.id.positive).parent as? View)?.visibility =
                                        View.GONE
                                    findViewById<View>(R.id.mouse).visibility =
                                        if (mouse) View.VISIBLE else View.GONE
                                    val parent =
                                        findViewById<ViewGroup>(R.id.parent_layout)
                                    container = parent
                                    initializeAllButtons(parent)
                                    checkSelection(parent)
                                }
                        },
                    )
                    FCLDialogButtonsRow(
                        listOf(
                            FCLDialogButton(
                                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                                onClick = {
                                    onConfirm(this@MiuixSelectKeycodeDialog)
                                    dismiss()
                                },
                            ),
                        ),
                    )
                }
            }
        }
    }

    constructor(
        context: Context,
        list: MutableList<Int>,
        singleSelection: Boolean,
        mouse: Boolean,
        onConfirm: (MiuixSelectKeycodeDialog) -> Unit,
    ) : this(context, list, singleSelection, mouse) {
        this.onConfirm = onConfirm
    }

    /** 与遗留 SelectKeycodeDialog.checkSelection 完全一致的递归选中态同步。 */
    private fun checkSelection(container: ViewGroup) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is KeycodeView) {
                val l = ArrayList<Int>()
                if (singleSelection) {
                    l.add(selectionProperty.get())
                } else {
                    l.addAll(list)
                }
                child.checkSelection(l)
            } else if (child is ViewGroup) {
                checkSelection(child)
            }
        }
    }

    /** 与遗留 SelectKeycodeDialog.initializeAllButtons 完全一致的递归监听注册。 */
    private fun initializeAllButtons(container: ViewGroup) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is KeycodeView) {
                child.setOnKeycodeChangeListener(object :
                    KeycodeView.OnKeycodeChangeListener {
                    override fun onKeycodeAdd(view: KeycodeView, keycode: Int) {
                        if (singleSelection) {
                            selectionProperty.set(keycode)
                            this@MiuixSelectKeycodeDialog.container?.let { checkSelection(it) }
                        } else {
                            list.add(keycode)
                            onChanged(list.toList())
                        }
                    }

                    override fun onKeycodeRemove(view: KeycodeView, keycode: Int) {
                        if (singleSelection) {
                            view.setSelectedWithoutCallback(true)
                        } else {
                            for (j in list.indices) {
                                if (list[j] == keycode) {
                                    list.removeAt(j)
                                    onChanged(list.toList())
                                    break
                                }
                            }
                        }
                    }
                })
            } else if (child is ViewGroup) {
                initializeAllButtons(child)
            }
        }
    }
}
