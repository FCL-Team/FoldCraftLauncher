package com.tungsten.fcl.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ActivityQuestVrLoadingBinding
import com.tungsten.fcl.vr.QuestGameBootstrap
import com.tungsten.fcl.vr.QuestVrLogScrollView
import com.tungsten.fcl.vr.QuestVrSession
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.io.IOException
import java.util.logging.Level

/** VR 启动面板：`:game` 进程内还原启动参数并启动 JVM，展示阶段/日志并确认退出 */
class QuestVrLoadingActivity : FCLActivity() {
    private val main = Handler(Looper.getMainLooper())
    private var session: QuestVrSession? = null
    private lateinit var binding: ActivityQuestVrLoadingBinding
    private lateinit var logScroll: QuestVrLogScrollView
    private var latestLogButton: View? = null
    private var confirmingStop = false
    private var lastLog = ""

    private val refresh: Runnable = object : Runnable {
        override fun run() {
            val currentSession = session
            if (currentSession == null || currentSession.isExiting || isDestroyed) return
            binding.qvStartPhase.setText(currentSession.phase())
            binding.qvStartElapsed.text =
                getString(R.string.qv_start_elapsed, currentSession.elapsedSeconds())
            refreshStartupLog()
            main.postDelayed(this, 350)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bridge: FCLBridge = try {
            QuestGameBootstrap.restore(this, intent)
        } catch (failure: IOException) {
            restoreFailed(failure); return
        } catch (failure: RuntimeException) {
            restoreFailed(failure); return
        }
        if (!bridge.isVrEnabled) {
            finish()
            if (QuestGameBootstrap.isGameProcess()) android.os.Process.killProcess(android.os.Process.myPid())
            return
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityQuestVrLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImageUtil.loadInto(binding.qvStartBackground,
            ThemeEngine.getInstance().getTheme().getBackground(this))
        logScroll = binding.qvStartLogScroll
        latestLogButton = binding.qvStartLatestLog
        logScroll.setOnFollowTailChangedListener { following ->
            latestLogButton?.visibility = if (following) View.GONE else View.VISIBLE
            if (following) refreshStartupLog()
        }
        binding.qvStartLatestLog.setOnClickListener {
            logScroll.resumeFollowing()
            refreshStartupLog()
        }
        binding.qvStartShowLog.setOnClickListener { toggleLog() }
        binding.qvStartCancel.setOnClickListener { confirmStop() }
        try {
            val prepared = QuestVrSession.prepare(this, bridge)
            session = prepared
            binding.qvStartInstance.text = prepared.instanceName
        } catch (failure: RuntimeException) {
            prepareFailed(failure); return
        } catch (failure: LinkageError) {
            prepareFailed(failure); return
        }
    }

    private fun restoreFailed(failure: Exception) {
        Logging.LOG.log(Level.WARNING, "Cannot restore the VR game launch", failure)
        Toast.makeText(this, getString(R.string.qv_start_restore_failed), Toast.LENGTH_LONG).show()
        finish()
        if (QuestGameBootstrap.isGameProcess()) android.os.Process.killProcess(android.os.Process.myPid())
    }

    private fun prepareFailed(failure: Throwable) {
        val failed = QuestVrSession.get()
        if (failed != null) {
            failed.onLog("[FCL-QuestVR] Cannot prepare VR startup: $failure\n")
            failed.onExit(1)
        }
        finish()
    }

    private fun toggleLog() {
        val show = logScroll.visibility != View.VISIBLE
        logScroll.visibility = if (show) View.VISIBLE else View.GONE
        binding.qvStartShowLog.setText(if (show) R.string.qv_start_hide_log else R.string.qv_start_show_log)
        latestLogButton?.visibility = View.GONE
        if (show) {
            logScroll.resumeFollowing()
            refreshStartupLog()
        }
    }

    private fun refreshStartupLog() {
        val currentSession = session ?: return
        if (logScroll.visibility != View.VISIBLE || !logScroll.isFollowingTail) return
        val text = currentSession.logPreview
        if (text != lastLog) {
            binding.qvStartLog.text = text
            lastLog = text
        }
    }

    override fun onResume() {
        super.onResume()
        session?.let {
            if (!isFinishing) {
                it.loadingResumed(true)
                main.post(refresh)
            }
        }
    }

    override fun onPause() {
        main.removeCallbacks(refresh)
        session?.loadingResumed(false)
        super.onPause()
    }

    override fun onDestroy() {
        main.removeCallbacks(refresh)
        Glide.with(applicationContext).clear(binding.qvStartBackground)
        session?.loadingDestroyed(this)
        super.onDestroy()
    }

    private fun confirmStop() {
        val currentSession = session
        if (currentSession == null || currentSession.isExiting || confirmingStop) return
        confirmingStop = true
        val dialog = FCLAlertDialog.Builder(this)
            .setTitle(getString(R.string.qv_start_cancel))
            .setMessage(getString(R.string.qv_start_cancel_message))
            .setPositiveButton(getString(R.string.qv_start_cancel)) { currentSession.cancel() }
            .setNegativeButton(getString(R.string.button_cancel), null).create()
        dialog.setOnDismissListener { confirmingStop = false }
        dialog.show()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.action == KeyEvent.ACTION_UP) confirmStop()
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}
