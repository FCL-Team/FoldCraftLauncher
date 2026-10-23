package com.mio.skin

import kotlin.math.cos
import kotlin.math.sin

/**
 * 奔跑动画，公式与 skinview3d 的 RunningAnimation 一致：
 * 更大的肢体摆幅 + 跳跃（rootY）、左右躲闪（rootX）与身体倾斜（rootRotationZ）。
 */
class RunningAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        val t = progress * 15f + PI * 0.5f

        // 腿部大摆幅
        player.leftLeg.rotationX = cos(t + PI) * 1.3f
        player.rightLeg.rotationX = cos(t) * 1.3f

        // 手臂摆动
        player.leftArm.rotationX = cos(t) * 1.5f
        player.rightArm.rotationX = cos(t + PI) * 1.5f
        val basicArmRotationZ = PI * 0.1f
        player.leftArm.rotationZ = cos(t) * 0.1f + basicArmRotationZ
        player.rightArm.rotationZ = cos(t + PI) * 0.1f - basicArmRotationZ

        // 跳跃
        player.rootY = cos(t * 2f)
        // 跑动躲闪
        player.rootX = cos(t) * 0.15f
        // 身体轻微倾斜
        player.rootRotationZ = cos(t + PI) * 0.01f

        // 披风：更高频率、更大幅度、更大的基础倾角
        player.cape.rotationX = sin(t * 2f) * 0.1f + PI * 0.3f
    }
}
