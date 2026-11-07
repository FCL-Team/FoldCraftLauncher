package com.tungsten.fcl.vr

import android.content.Context

/** Quest VR 启动器侧偏好（存于 "launcher" SharedPreferences） */
object QuestVrPreferences {
    private const val TRANSITION_ANIMATION_KEY = "questVrTransitionAnimation"

    /** 进入/退出沉浸模式时是否播放面板淡入淡出与缩放过渡动画 */
    @JvmStatic
    fun isTransitionAnimationEnabled(context: Context): Boolean =
        context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
            .getBoolean(TRANSITION_ANIMATION_KEY, true)

    @JvmStatic
    fun setTransitionAnimationEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences("launcher", Context.MODE_PRIVATE).edit()
            .putBoolean(TRANSITION_ANIMATION_KEY, enabled).apply()
    }
}
