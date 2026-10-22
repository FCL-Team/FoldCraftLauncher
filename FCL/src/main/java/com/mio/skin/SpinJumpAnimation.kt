package com.mio.skin

import kotlin.math.abs
import kotlin.math.sin

/**
 * 「邦友的酒」式旋转 Q 弹：整体匀速自转，模型像果冻一样在触地相位被压扁、
 * 弹起相位拉伸回弹，双臂水平展开随节奏扇动，不产生跳跃位移。
 */
class SpinJumpAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        // 整体匀速自转
        player.rootRotationY = progress * SPIN_SPEED

        // Q 弹形变：0.45s 一个压弹周期，触地压扁（Y 压、XZ 鼓），弹起时拉伸回原
        val t = progress * PI / 0.45f
        val bounce = abs(sin(t))
        player.rootScaleY = 0.82f + bounce * 0.26f
        player.rootScaleXZ = 1.12f - bounce * 0.17f

        // 双臂水平展开，随节奏上下扇动
        val flap = sin(t) * 0.25f
        player.leftArm.rotationZ = PI / 2f + flap
        player.rightArm.rotationZ = -PI / 2f - flap
    }

    companion object {
        /** 自转角速度（弧度/秒），1 圈/秒 */
        const val SPIN_SPEED = (2.0 * Math.PI).toFloat()
    }
}
