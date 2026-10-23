package com.mio.skin

import kotlin.math.abs
import kotlin.math.sin

/**
 * 举起双手原地蹦跳动画（"将军我跳不动了"梗）：
 * 双臂高举随蹦跳摆动，周期性起跳落地，落地屈膝缓冲并伴随吃力的身体摇摆。
 */
class HandsUpJumpingAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        // 0.45s 一个蹦跳周期（起跳→落地）
        val t = progress * PI / 0.45f
        val bounce = abs(sin(t))

        // 蹦跳高度
        player.rootY = bounce * 3f

        // 落地屈膝缓冲，起跳蹬直；小腿随跳跃相位轻微交替摆动，膝盖微开合
        val legBend = (1f - bounce) * 0.35f
        val swing = sin(t * 2f) * 0.18f
        player.leftLeg.rotationX = -legBend + swing
        player.rightLeg.rotationX = -legBend - swing
        player.leftLeg.rotationZ = sin(t) * 0.08f
        player.rightLeg.rotationZ = -sin(t) * 0.08f

        // 双臂高举，随蹦跳小幅甩动并外张避开头肩
        player.leftArm.rotationX = PI + sin(t * 2f) * 0.15f
        player.rightArm.rotationX = PI - sin(t * 2f) * 0.15f
        player.leftArm.rotationZ = 0.35f + sin(t) * 0.15f
        player.rightArm.rotationZ = -0.35f - sin(t) * 0.15f

        // 身体吃力的小幅摇摆
        player.rootRotationX = sin(t) * 0.06f
        player.rootRotationZ = sin(t) * 0.04f
    }
}
