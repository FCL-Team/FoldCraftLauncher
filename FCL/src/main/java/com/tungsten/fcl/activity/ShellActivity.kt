package com.tungsten.fcl.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.KeyListener
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.activity.compose.setContent
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.compose.ComposeActivities
import com.tungsten.fcl.activity.compose.ShellScreen
import com.tungsten.fcl.activity.compose.ShellStateHolder
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcl.util.ShellUtil
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.view.FCLEditText
import java.io.File

/**
 * 内置 Shell 终端（3.7 迁移：Compose/Miuix 重写，带开关回滚）。
 *
 * [ComposeActivities.USE_COMPOSE_SHELL] = true 时挂 [ShellScreen]；
 * false 回滚遗留 activity_shell.xml 路径。ShellUtil 生命周期（onCreate 启动 /
 * onDestroy interrupt）两条路径共用，manifest adjustResize 不变。
 */
class ShellActivity : FCLActivity() {

    private var logWindow: EditText? = null
    private var editText: FCLEditText? = null
    private var composeState: ShellStateHolder? = null
    private lateinit var shellUtil: ShellUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ComposeActivities.USE_COMPOSE_SHELL) {
            val state = ShellStateHolder { cmd -> shellUtil.append(cmd) }
            composeState = state
            setContent {
                FCLTheme(this) {
                    ShellScreen(state)
                }
            }
        } else {
            setContentView(R.layout.activity_shell)
            logWindow = findViewById(R.id.shell_log_window)
            editText = findViewById(R.id.shell_input)
        }
        appendLog("Welcome to use Fold Craft Launcher!\n")
        appendLog("Here is the shell command line!\n")
        shellUtil = ShellUtil(File(FCLPath.FILES_DIR).parent) { output ->
            runOnUiThread { appendLog("\t$output\n") }
        }
        shellUtil.start()
        if (!ComposeActivities.USE_COMPOSE_SHELL) {
            setupLegacyInput()
        }
    }

    /** 遗留路径的输入框/日志窗接线（Compose 路径由 ShellScreen 接管）。 */
    private fun setupLegacyInput() {
        val input = editText ?: return
        val log = logWindow ?: return
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable) {
                val cmd = input.text?.toString() ?: return
                if (cmd.endsWith("\n")) {
                    appendLog("->$cmd")
                    input.setText("")
                    if (cmd.contains("clear")) {
                        log.setText("")
                        return
                    }
                    shellUtil.append(cmd)
                }
            }
        })
        log.setOnClickListener { input.requestFocus() }
        log.keyListener = object : KeyListener {
            override fun getInputType() = 0

            override fun onKeyDown(view: View, text: Editable, keyCode: Int, event: KeyEvent) = true

            override fun onKeyUp(view: View, text: Editable, keyCode: Int, event: KeyEvent) = true

            override fun onKeyOther(view: View, text: Editable, event: KeyEvent) = true

            override fun clearMetaKeyState(view: View, content: Editable, states: Int) {}
        }
    }

    private fun appendLog(str: String) {
        composeState?.appendLog(str) ?: logWindow?.append(str)
    }

    override fun onDestroy() {
        super.onDestroy()
        shellUtil.interrupt()
    }
}
