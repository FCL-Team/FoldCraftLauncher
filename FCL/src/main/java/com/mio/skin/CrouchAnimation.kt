package com.mio.skin

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sin

/**
 * 潜行动画，公式与 skinview3d 的 CrouchAnimation 一致：
 * 身体前倾下蹲，手臂前伸，双腿后移，披风同步压低。
 *
 * [showProgress] 为 false 时按 skinview3d 默认行为对进度取整（潜行姿态阶跃切换），
 * 为 true 时连续平滑过渡。
 */
class CrouchAnimation(var showProgress: Boolean = false, var runOnce: Boolean = false) : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        var pr = progress * 8f
        if (runOnce) {
            pr = pr.coerceIn(-1f, 1f)
        }
        if (!showProgress) {
            pr = floor(pr)
        }
        val s = abs(sin(pr * PI / 2f))

        // 身体前倾下蹲（位置为玩家根坐标系，含 skin 根 y+8）
        player.body.rotationX = 0.4537860552f * s
        player.body.posZ = (1.3256181f - 3.4500310377f) * s
        player.body.posY = 2f - 2.103677462f * s

        // 披风下压
        player.cape.posY = 8f - 1.851236166577372f * s
        player.cape.rotationX = PI * 0.06f + 0.294220265771f * s
        player.cape.posZ = -2f + (3.786619432f - 3.4500310377f) * s

        // 头部下移
        player.head.posY = 8f - 3.618325234674f * s

        // 手臂前伸
        val armOffsetZ = (3.618325234674f - 3.4500310377f) * s
        player.leftArm.posZ = armOffsetZ
        player.rightArm.posZ = armOffsetZ
        player.leftArm.rotationX = 0.410367746202f * s
        player.rightArm.rotationX = 0.410367746202f * s
        player.leftArm.rotationZ = 0.1f
        player.rightArm.rotationZ = -0.1f
        player.leftArm.posY = 6f - 2.53943318f * s
        player.rightArm.posY = 6f - 2.53943318f * s

        // 双腿后移
        player.rightLeg.posZ = -3.4500310377f * s
        player.leftLeg.posZ = -3.4500310377f * s
    }
}
