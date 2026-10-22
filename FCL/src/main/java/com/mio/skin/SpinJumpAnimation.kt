package com.mio.skin

import kotlin.math.abs
import kotlin.math.sin

/**
 * 「邦友的酒」式旋转弹跳：整体匀速自转 + 欢快上下蹦跳，双臂水平展开随节奏扇动。
 * 自转角速度与点击效果的固定播放时长（[SkinRenderer] EFFECT_DURATION）配合，
 * 恰好转完整圈数，效果结束复位时无角度跳变。
 */
class SpinJumpAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        // 整体匀速自转
        player.rootRotationY = progress * SPIN_SPEED

        // 上下弹跳（0.45s 一跳）与落地屈膝缓冲
        val t = progress * PI / 0.45f
        val bounce = abs(sin(t))
        player.rootY = bounce * 2.5f
        val legBend = (1f - bounce) * 0.4f
        player.leftLeg.rotationX = -legBend
        player.rightLeg.rotationX = -legBend

        // 双臂水平展开，随节奏上下扇动
        val flap = sin(t) * 0.25f
        player.leftArm.rotationZ = PI / 2f + flap
        player.rightArm.rotationZ = -PI / 2f - flap

        // 身体随节奏轻微摇摆
        player.rootRotationZ = sin(t) * 0.06f
    }

    companion object {
        /** 自转角速度（弧度/秒），1 圈/秒 */
        const val SPIN_SPEED = (2.0 * Math.PI).toFloat()
    }
}
