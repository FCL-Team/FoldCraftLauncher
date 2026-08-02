package com.tungsten.fcl.activity.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField

/**
 * Shell 终端页状态（3.7，对应 ShellActivity 的日志窗 + 输入框状态）。
 *
 * @param onCommand 提交命令回调（由 Activity 接入 ShellUtil），参数为含结尾 \n 的完整命令，
 *                  与遗留 afterTextChanged 中 shellUtil.append(cmd) 的参数形态一致。
 */
class ShellStateHolder(
    private val onCommand: (String) -> Unit,
) {
    /** 日志全文（终端语义是追加流，用整串而非行列表，与原 EditText.append 一致）。 */
    var log by mutableStateOf("")
        private set

    var input by mutableStateOf("")

    fun appendLog(str: String) {
        log += str
    }

    fun clearLog() {
        log = ""
    }

    /** 对齐遗留逻辑：回车提交 → 回显 "->cmd"；含 "clear" 清屏且不发给 shell。 */
    fun submit() {
        val cmd = input + "\n"
        input = ""
        appendLog("->$cmd")
        if (cmd.contains("clear")) {
            clearLog()
            return
        }
        onCommand(cmd)
    }
}

/**
 * Miuix 版 Shell 终端页（3.7，对应 ShellActivity + activity_shell.xml）。
 *
 * 行为对齐（interaction-map §1.6）：
 * - 黑底白字终端观感保留；日志可选中（原 textIsSelectable）；新输出自动滚到底部；
 * - 点击日志区 → 输入框获焦弹键盘；回车（IME Send 或插入 \n）即提交；
 * - manifest 的 adjustResize 不变，Compose 侧无需手动键盘避让。
 */
@Composable
fun ShellScreen(state: ShellStateHolder) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(state.log) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding(),
    ) {
        SelectionContainer(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { focusRequester.requestFocus() },
        ) {
            Text(
                text = state.log,
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(10.dp),
            )
        }
        TextField(
            value = state.input,
            onValueChange = { value ->
                // 兼容会把回车插成 \n 的键盘（对齐遗留 TextWatcher 检测结尾 \n）
                if (value.endsWith("\n")) {
                    state.input = value.dropLast(1)
                    state.submit()
                } else {
                    state.input = value
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 10.dp)
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { state.submit() }),
        )
    }
}
