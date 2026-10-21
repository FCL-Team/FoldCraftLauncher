package com.mio.skin

import kotlin.math.cos
import kotlin.math.sin

/**
 * 挥击动画，公式与 skinview3d 的 HitAnimation 一致：
 * 右臂大幅挥击，身体轻微转动，左臂小摆动并前后移动。
 */
class HitAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        val t = progress * 18f

        // 右臂挥击
        player.rightArm.rotationX = -0.4537860552f * 2f + 2f * sin(t + PI) * 0.3f
        val basicArmRotationZ = 0.01f * PI + 0.06f
        player.rightArm.rotationZ = -cos(t) * 0.403f + basicArmRotationZ

        // 身体轻微转动
        player.body.rotationY = -cos(t) * 0.06f

        // 左臂小摆动并前后移动
        player.leftArm.rotationX = sin(t + PI) * 0.077f
        player.leftArm.rotationZ = -cos(t) * 0.015f + 0.13f - 0.05f
        player.leftArm.posZ = cos(t) * 0.3f
        player.leftArm.posX = 5f - cos(t) * 0.05f
    }
}
