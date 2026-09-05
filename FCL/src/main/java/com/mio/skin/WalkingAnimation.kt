package com.mio.skin

import kotlin.math.cos
import kotlin.math.sin

/**
 * 走路动画，公式与 skinview3d 的 WalkingAnimation 一致。
 * 覆盖腿/臂摆动、头部小幅摆动与披风摆动。
 */
class WalkingAnimation(var headBobbing: Boolean = true) : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        val t = progress * 8f

        // 腿部摆动
        player.leftLeg.rotationX = sin(t) * 0.5f
        player.rightLeg.rotationX = sin(t + PI) * 0.5f

        // 手臂摆动（与对侧腿同相）
        player.leftArm.rotationX = sin(t + PI) * 0.5f
        player.rightArm.rotationX = sin(t) * 0.5f
        val basicArmRotationZ = PI * 0.02f
        player.leftArm.rotationZ = cos(t) * 0.03f + basicArmRotationZ
        player.rightArm.rotationZ = cos(t + PI) * 0.03f - basicArmRotationZ

        if (headBobbing) {
            // 头部以不同频率与幅度摆动
            player.head.rotationY = sin(t / 4f) * 0.2f
            player.head.rotationX = sin(t / 5f) * 0.1f
        } else {
            player.head.rotationY = 0f
            player.head.rotationX = 0f
        }

        // 披风绕 X 轴固定倾角（PI*0.06 = 10.8°）+ 低频摆动
        player.cape.rotationX = sin(t / 1.5f) * 0.06f + PI * 0.06f
    }
}
