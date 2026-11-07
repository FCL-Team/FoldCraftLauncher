package com.tungsten.fcl.control

import android.content.Intent

/**
 * 游戏启动时随 Intent 传递的控制栈元数据（控件组与联机玩家名），
 * 供 VR 场景跨面板/跨 Activity 重建 Intent 时透传。
 */
class GameLaunchMetadata private constructor(
    private val controller: String?,
    private val player: String?,
) {
    fun applyTo(destination: Intent) {
        if (controller != null) destination.putExtra(CONTROLLER, controller)
        if (player != null) destination.putExtra(PLAYER, player)
    }

    companion object {
        private const val CONTROLLER = "controller"
        private const val PLAYER = "TERRACOTTA_PLAYER"

        /** 读取 Intent 中的控件组 id，缺省回落 fallbackController */
        @JvmStatic
        fun resolveController(intent: Intent?, fallbackController: String?): String? {
            val selected = intent?.getStringExtra(CONTROLLER)
            return selected ?: fallbackController
        }

        @JvmStatic
        fun capture(intent: Intent?, fallbackController: String?): GameLaunchMetadata =
            GameLaunchMetadata(resolveController(intent, fallbackController),
                intent?.getStringExtra(PLAYER))

        @JvmStatic
        fun copy(source: Intent?, destination: Intent) {
            capture(source, null).applyTo(destination)
        }
    }
}
