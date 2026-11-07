package com.tungsten.fcl.activity

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import com.tungsten.fcl.vr.QuestGameExit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 游戏进程退出后的中转面板：恢复主进程的启动器任务，收到焦点确认后 ACK 给
 * QuestGameExit（由其结束游戏进程）；异常退出时带退出码转开崩溃页。
 */
class QuestLauncherReturnActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ack = intent.getParcelableExtra<ResultReceiver>(QuestGameExit.EXTRA_ACK)
        val taskId = intent.getIntExtra(QuestGameExit.EXTRA_TASK, -1)
        val exitCode = intent.getIntExtra(QuestGameExit.EXTRA_CODE, 0)
        val logPath = intent.getStringExtra(QuestGameExit.EXTRA_LOG)
        val application = application
        val handler = Handler(Looper.getMainLooper())
        val acknowledged = AtomicBoolean()
        val callback = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                if (activity !is MainActivity || !acknowledged.compareAndSet(false, true)) return
                application.unregisterActivityLifecycleCallbacks(this)
                handler.removeCallbacksAndMessages(null)
                activity.window.decorView.post {
                    ack?.send(RESULT_OK, null)
                    if (exitCode != 0) {
                        val crash = Intent(activity, JVMCrashActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                            .putExtra("isGame", true).putExtra("exitCode", exitCode)
                            .putExtra("logPath", logPath)
                            .putExtra(QuestGameExit.EXTRA_TASK, activity.taskId)
                        activity.startActivity(crash)
                    }
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        }
        application.registerActivityLifecycleCallbacks(callback)
        // 启动器任务长时间未恢复时放弃监听，避免回调常驻
        handler.postDelayed({ application.unregisterActivityLifecycleCallbacks(callback) }, 15000)
        // 主进程存活时按窗口焦点轮询兜底（Activity 回调可能因任务恢复时序错过）
        handler.post(object : Runnable {
            private var remaining = 200

            override fun run() {
                if (acknowledged.get() || remaining-- <= 0) return
                try {
                    val main = MainActivity.getInstance()
                    if (!main.isFinishing && !main.isDestroyed && main.hasWindowFocus()) {
                        callback.onActivityResumed(main)
                        return
                    }
                } catch (ignored: RuntimeException) {
                }
                handler.postDelayed(this, 50)
            }
        })
        val launcher = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        var restored = false
        val manager = getSystemService(ActivityManager::class.java)
        if (manager != null && taskId != -1) {
            for (task in manager.appTasks) {
                if (task.taskInfo.taskId != taskId) continue
                task.startActivity(this, launcher, null)
                restored = true
                break
            }
        }
        if (!restored) startActivity(Intent(this, SplashActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION))
        finish()
        overridePendingTransition(0, 0)
    }
}
