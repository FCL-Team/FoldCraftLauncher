package com.tungsten.fcl.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import com.tungsten.fcl.activity.compose.ShellScreen
import com.tungsten.fcl.activity.compose.ShellStateHolder
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcl.util.ShellUtil
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.FCLActivity
import java.io.File

/**
 * 内置 Shell 终端（3.7 迁移 Compose/Miuix；批 3 开关已固化，旧 activity_shell.xml View 路径已删除）。
 *
 * 挂 [ShellScreen]；ShellUtil 生命周期（onCreate 启动 / onDestroy interrupt）不变，
 * manifest adjustResize 不变。
 */
class ShellActivity : FCLActivity() {

    private lateinit var composeState: ShellStateHolder
    private lateinit var shellUtil: ShellUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val state = ShellStateHolder { cmd -> shellUtil.append(cmd) }
        composeState = state
        setContent {
            FCLTheme(this) {
                ShellScreen(state)
            }
        }
        appendLog("Welcome to use Fold Craft Launcher!\n")
        appendLog("Here is the shell command line!\n")
        shellUtil = ShellUtil(File(FCLPath.FILES_DIR).parent) { output ->
            runOnUiThread { appendLog("\t$output\n") }
        }
        shellUtil.start()
    }

    private fun appendLog(str: String) {
        composeState.appendLog(str)
    }

    override fun onDestroy() {
        super.onDestroy()
        shellUtil.interrupt()
    }
}
