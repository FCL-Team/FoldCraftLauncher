package com.tungsten.fcl.vr

import android.app.Activity
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tungsten.fcl.activity.QuestFlatActivity
import com.tungsten.fcl.activity.QuestVrActivity
import com.tungsten.fcl.control.GameLaunchMetadata

/** 沉浸面板与平面面板之间的 Activity 切换（Quest 上经 Home 中转的启动链） */
object QuestVrTransitions {
    private fun destination(from: Context, target: Class<out Activity>, animate: Boolean,
                            newTask: Boolean): Intent {
        val intent = Intent(from, target).setAction(Intent.ACTION_MAIN)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        if (newTask || from !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (target == QuestFlatActivity::class.java) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (from is Activity) GameLaunchMetadata.copy(from.intent, intent)
        if (!animate) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        return intent
    }

    private fun start(from: Context, intent: Intent, animate: Boolean) {
        val options: Bundle? = if (animate) null
        else ActivityOptions.makeCustomAnimation(from, 0, 0).toBundle()
        from.startActivity(intent, options)
        if (!animate && from is Activity) from.overridePendingTransition(0, 0)
    }

    /** 切入沉浸面板（OpenXR 渲染） */
    fun immersive(from: Activity, animate: Boolean) {
        start(from, destination(from, QuestVrActivity::class.java, animate, true), animate)
    }

    /** 回到平面面板；从 VR 返回时须经 Home 的 pending intent 中转以保留任务栈 */
    fun flat(from: Context, returningFromVr: Boolean, animate: Boolean) {
        val panel = destination(from, QuestFlatActivity::class.java, animate, returningFromVr)
        if (!returningFromVr) {
            start(from, panel, animate)
            return
        }
        val pending = PendingIntent.getActivity(from, 1505, panel,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra("extra_launch_in_home_pending_intent", pending)
        if (!animate) home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        start(from, home, animate)
    }

    fun fadeOutPanel(activity: Activity, enabled: Boolean, afterFade: Runnable) {
        QuestVrPanelFade.fadeOut(activity, enabled, afterFade)
    }

    fun fadeInPanel(activity: Activity, enabled: Boolean) {
        QuestVrPanelFade.fadeIn(activity, enabled)
    }
}
