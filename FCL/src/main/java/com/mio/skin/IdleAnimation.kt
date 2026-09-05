package com.mio.skin

import kotlin.math.cos
import kotlin.math.sin

/**
 * 待机动画，公式与 skinview3d 的 IdleAnimation 一致：手臂轻微摆动 + 披风微动。
 */
class IdleAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        val t = progress * 2f

        // 手臂 z 向轻微摆动
        val basicArmRotationZ = PI * 0.02f
        player.leftArm.rotationZ = cos(t) * 0.03f + basicArmRotationZ
        player.rightArm.rotationZ = cos(t + PI) * 0.03f - basicArmRotationZ

        // 披风绕 X 轴固定倾角 + 微动
        player.cape.rotationX = sin(t) * 0.01f + PI * 0.06f
    }
}
