package com.tungsten.fcl.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.annotation.Nullable
import com.tungsten.fcl.vr.QuestVrSession
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode
import com.tungsten.fcllibrary.component.FCLActivity
import org.lwjgl.glfw.CallbackBridge

/** VR 沉浸面板：Horizon 侧 OpenXR 渲染期间持有焦点，Back 键转译为 ESC 发给游戏 */
class QuestVrActivity : FCLActivity() {
    private var session: QuestVrSession? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vrSession = QuestVrSession.get()
        if (vrSession == null || !vrSession.attachImmersive(this)) {
            finish()
            return
        }
        session = vrSession
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        session?.let { if (!isFinishing) it.immersiveResumed(this) }
    }

    override fun onPause() {
        session?.immersivePaused(this)
        super.onPause()
    }

    override fun onDestroy() {
        session?.immersiveDestroyed(this)
        super.onDestroy()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val currentSession = session
        if (event.keyCode == KeyEvent.KEYCODE_BACK && currentSession != null && !currentSession.isExiting) {
            if (event.action == KeyEvent.ACTION_UP) {
                CallbackBridge.sendKeycode(LwjglGlfwKeycode.KEY_ESCAPE.toInt(), 0.toChar(), 0, 0, true)
                CallbackBridge.sendKeycode(LwjglGlfwKeycode.KEY_ESCAPE.toInt(), 0.toChar(), 0, 0, false)
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}
