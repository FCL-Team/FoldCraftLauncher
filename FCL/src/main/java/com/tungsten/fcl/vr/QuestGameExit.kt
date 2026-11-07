package com.tungsten.fcl.vr

import android.app.Activity
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import android.os.ResultReceiver
import com.tungsten.fcl.activity.QuestLauncherReturnActivity
import com.tungsten.fclcore.util.Logging
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level

/** VR 游戏退出链：标记退出状态、导航回启动器任务栈，确认面板恢复后结束游戏进程 */
object QuestGameExit {
    const val EXTRA_ACK = "com.tungsten.fcl.quest.RETURN_ACK"
    const val EXTRA_TASK = "com.tungsten.fcl.quest.LAUNCHER_TASK"
    const val EXTRA_CODE = "com.tungsten.fcl.quest.EXIT_CODE"
    const val EXTRA_LOG = "com.tungsten.fcl.quest.EXIT_LOG"

    private val RETURNING = AtomicBoolean()

    /**
     * 从游戏进程导航回启动器：经 QuestLauncherReturnActivity 恢复启动器任务，
     * 收到恢复确认（或超时兜底）后关闭游戏面板并结束进程
     */
    @JvmStatic
    fun returnToLauncher(context: Context, fromVr: Boolean, animate: Boolean,
                         exitCode: Int, logPath: String?, closeGameActivities: Runnable) {
        if (!RETURNING.compareAndSet(false, true)) return
        QuestGameBootstrap.markReturning(context)
        val main = Handler(Looper.getMainLooper())
        val handoff = HandlerThread("QuestGameExit")
        handoff.start()
        val background = Handler(handoff.looper)
        val killGame = Runnable {
            if (QuestGameBootstrap.isGameProcess()) Process.killProcess(Process.myPid())
            else handoff.quitSafely()
        }
        val ack: ResultReceiver = object : ResultReceiver(background) {
            override fun onReceiveResult(resultCode: Int, data: Bundle?) {
                if (resultCode != Activity.RESULT_OK) return
                background.removeCallbacksAndMessages(null)
                main.post {
                    try {
                        closeGameActivities.run()
                    } finally {
                        killGame.run()
                    }
                }
                background.postDelayed(killGame, 500)
            }
        }
        val destination = Intent(context, QuestLauncherReturnActivity::class.java)
            .setAction(Intent.ACTION_MAIN)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            .putExtra(EXTRA_ACK, ack)
            .putExtra(EXTRA_TASK, QuestGameBootstrap.launcherTaskId)
            .putExtra(EXTRA_CODE, exitCode)
            .putExtra(EXTRA_LOG, logPath)
        val open = Runnable {
            try {
                var navigation: Intent = destination
                if (fromVr) {
                    // 沉浸模式退出须经 Home 中转，Horizon 才会关闭 VR 焦点
                    val pending = PendingIntent.getActivity(context, 1507, destination,
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    navigation = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("extra_launch_in_home_pending_intent", pending)
                }
                if (!animate) navigation.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                context.startActivity(navigation, if (animate) null
                else ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle())
            } catch (failure: RuntimeException) {
                Logging.LOG.log(Level.WARNING, "Cannot navigate from the game to the launcher", failure)
                try {
                    context.startActivity(destination)
                } catch (fallback: RuntimeException) {
                    Logging.LOG.log(Level.WARNING, "Cannot open launcher return activity", fallback)
                }
            }
        }
        if (Looper.myLooper() == Looper.getMainLooper()) open.run() else main.post(open)
        background.postDelayed(killGame, 15000)
    }
}
