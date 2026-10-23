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

        // Q 弹形变：0.45s 一个压弹周期，触地压扁（Y 压、XZ 鼓），弹起时拉伸回原
        val t = progress * PI / 0.45f
        val bounce = abs(sin(t))
        player.rootScaleY = 0.82f + bounce * 0.26f
        player.rootScaleXZ = 1.12f - bounce * 0.17f
    }

    companion object {
        /** 自转角速度（弧度/秒），0.75 圈/秒 */
        const val SPIN_SPEED = (1.5 * Math.PI).toFloat()

        /** 坐姿大腿前抬角度（弧度）与双腿微开角 */
        private const val LEG_LIFT = (Math.PI / 2.0).toFloat()
        private const val LEG_SPREAD = 0.08f

        /** 坐姿整体下沉量（像素） */
        private const val SIT_SINK = -2f
    }
}
