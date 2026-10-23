package com.mio.skin

import kotlin.math.abs
import kotlin.math.sin

/**
 * 「邦友的酒」式旋转 Q 弹：整体匀速自转，坐姿（双腿前抬随节奏晃动、身体轻微下沉），
 * 模型像果冻一样在触地相位被压扁、弹起相位拉伸回弹，不产生跳跃位移。
 */
class SpinJumpAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        // 整体匀速自转
        player.rootRotationY = progress * SPIN_SPEED

        // 坐姿：双腿前抬、微开并随节奏晃动，身体轻微下沉贴地
        val swing = sin(progress * PI / 0.45f) * 0.12f
        player.leftLeg.rotationX = -LEG_LIFT + swing
        player.rightLeg.rotationX = -LEG_LIFT - swing
        player.leftLeg.rotationZ = LEG_SPREAD
        player.rightLeg.rotationZ = -LEG_SPREAD
        player.rootY = SIT_SINK

        // 双臂前伸搭在腿上，随节奏轻微晃动
        player.leftArm.rotationX = -ARM_REACH + swing * 0.6f
        player.rightArm.rotationX = -ARM_REACH + swing * 0.6f
        player.leftArm.rotationZ = ARM_OUT
        player.rightArm.rotationZ = -ARM_OUT

        // Q 弹形变：0.45s 一个压弹周期，触地狠压且横向大幅拉宽，弹起时纵向拉伸、横向恢复原状
        val t = progress * PI / 0.45f
        val bounce = abs(sin(t))
        player.rootScaleY = 0.68f + bounce * 0.44f
        player.rootScaleXZ = 1.30f - bounce * 0.30f
    }

    companion object {
        /** 自转角速度（弧度/秒），0.75 圈/秒 */
        const val SPIN_SPEED = (1.5 * Math.PI).toFloat()

        /** 坐姿大腿前抬角度（弧度）、双臂前伸搭腿角度与双腿微开角 */
        private const val LEG_LIFT = (Math.PI / 2.0).toFloat()
        private const val ARM_REACH = (Math.PI / 3.0).toFloat()
        private const val LEG_SPREAD = 0.08f

        /** 双臂微外张避免与躯干穿模 */
        private const val ARM_OUT = 0.05f

        /** 坐姿整体下沉量（像素） */
        private const val SIT_SINK = -2f
    }
}
