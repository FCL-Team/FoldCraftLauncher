package com.tungsten.fcl.ui.compose.dialog

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.tungsten.fcl.R
import com.tungsten.fcl.control.AddInputTextDialog
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.control.data.QuickInputTexts
import com.tungsten.fcl.ui.compose.FCLComposeDialog
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.keycodes.FCLKeycodes
import com.tungsten.fclauncher.keycodes.MinecraftKeyBindingMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版游戏内快捷输入面板（3.2 批 3，对应 control/QuickInputDialog + dialog_quick_input
 * + item_input_text）。
 *
 * 行为对齐：
 * - 列表项显示 "备注&*&文本" 的备注段（无备注即全文），点击发送文本段：
 *   光标模式逐字符 sendChar；否则先发聊天绑定键（KEY_T 按下/抬起），50ms 后逐字符输入
 *   并补 KEY_ENTER 按下/抬起，随后 dismiss——按键序列与遗留逐行一致；
 * - 删除按钮 QuickInputTexts.removeInputText + 刷新列表；
 * - "添加"按 [ComposeDialogs.USE_COMPOSE_ADD_INPUT_TEXT] 双分支拉起新增文本弹窗；
 * - 确定（右侧）dismiss；
 * - 窗体对齐遗留：宽 WRAP_CONTENT、高 MATCH_PARENT 的侧边面板；
 * - setCancelable(false) 一致。
 *
 * 运行于游戏内（GameMenu → JVMActivity/ControllerActivity，AppCompatActivity），
 * AppCompatDialog + ComposeView 可用（同批 2 MiuixGameItemBarSettingDialog 先例）。
 */
class MiuixQuickInputDialog(
    private val activity: AppCompatActivity,
    private val menu: GameMenu,
) : FCLComposeDialog(activity, cancelable = false) {

    private val textsState = mutableStateOf<List<String>>(emptyList())

    init {
        refreshList()
        setDialogContent {
            DialogContent()
        }
    }

    override fun show() {
        super.show()
        // 对齐遗留：宽 WRAP_CONTENT、高 MATCH_PARENT
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun refreshList() {
        textsState.value = QuickInputTexts.getInputTexts().toList()
    }

    private fun onTextInput(text: String) {
        if (text.isNotEmpty()) {
            if (menu.cursorMode == FCLBridge.CursorEnabled) {
                text.forEach { s ->
                    menu.input.sendChar(s)
                }
            } else {
                val gameOption = menu.gameOption
                menu.input.sendBoundKeyEvent(
                    gameOption,
                    MinecraftKeyBindingMapper.BINDING_CHAT,
                    FCLKeycodes.KEY_T,
                    true,
                )
                menu.input.sendBoundKeyEvent(
                    gameOption,
                    MinecraftKeyBindingMapper.BINDING_CHAT,
                    FCLKeycodes.KEY_T,
                    false,
                )
                activity.lifecycleScope.launch {
                    delay(50)
                    text.forEach { s ->
                        menu.input.sendChar(s)
                    }
                    menu.input.sendKeyEvent(FCLKeycodes.KEY_ENTER, true)
                    menu.input.sendKeyEvent(FCLKeycodes.KEY_ENTER, false)
                }
            }
        }

        dismiss()
    }

    private fun onAddText() {
        val callback = AddInputTextDialog.Callback { refreshList() }
        if (ComposeDialogs.USE_COMPOSE_ADD_INPUT_TEXT) {
            MiuixAddInputTextDialog(context, callback).show()
        } else {
            AddInputTextDialog(context, callback).show()
        }
    }

    @Composable
    private fun DialogContent() {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp)
                .fillMaxHeight(),
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.quick_input_title),
                    modifier = Modifier.fillMaxWidth(),
                    style = MiuixTheme.textStyles.title4,
                    textAlign = TextAlign.Center,
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                ) {
                    items(textsState.value) { inputText ->
                        InputTextRow(inputText)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        text = stringResource(R.string.quick_input_add),
                        onClick = { onAddText() },
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                        onClick = { dismiss() },
                    )
                }
            }
        }
    }

    @Composable
    private fun InputTextRow(inputText: String) {
        val split = inputText.split("&\\*&".toRegex())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTextInput(if (split.size == 2) split[1] else split[0]) }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = split[0],
                style = MiuixTheme.textStyles.body2,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = {
                QuickInputTexts.removeInputText(inputText)
                refreshList()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_delete_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
