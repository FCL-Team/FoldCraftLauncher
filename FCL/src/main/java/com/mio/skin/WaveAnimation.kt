package com.mio.skin

import kotlin.math.sin

/**
 * 挥手动画，公式与 skinview3d 的 WaveAnimation 一致：抬起一侧手臂挥动。
 */
class WaveAnimation(whichArm: String = "left") : PlayerAnimation() {

    private val left = whichArm == "left"

    override fun animate(player: PlayerModel, delta: Float) {
        val t = progress * 2f * PI * 0.5f

        val arm = if (left) player.leftArm else player.rightArm
        // 抬起手臂（原实现即为此值，单位弧度）
        arm.rotationX = 180f
        arm.rotationZ = sin(t) * 0.5f
    }
}
