package com.mio.skin

import kotlin.math.cos
import kotlin.math.sin

/**
 * 举起双手奔跑动画：腿部/跳跃/躲闪/披风沿用奔跑公式，
 * 双臂高举过头并随步伐轻摆。
 */
class HandsUpRunningAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        val t = progress * 15f + PI * 0.5f

        // 腿部大摆幅（同奔跑）
        player.leftLeg.rotationX = cos(t + PI) * 1.3f
        player.rightLeg.rotationX = cos(t) * 1.3f

        // 双臂高举过头，向外张开避开头部，并大幅交替甩动（逃跑感）
        player.leftArm.rotationX = PI + sin(t) * 0.8f
        player.rightArm.rotationX = PI + sin(t + PI) * 0.8f
        player.leftArm.rotationZ = 0.35f
        player.rightArm.rotationZ = -0.35f

        // 跳跃与躲闪
        player.rootY = cos(t * 2f)
        player.rootX = cos(t) * 0.15f
        player.rootRotationZ = cos(t + PI) * 0.01f

        // 披风（同奔跑）
        player.cape.rotationX = sin(t * 2f) * 0.1f + PI * 0.3f
    }
}
