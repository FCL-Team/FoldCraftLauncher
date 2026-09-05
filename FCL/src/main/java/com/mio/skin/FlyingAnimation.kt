package com.mio.skin

/**
 * 飞行动画，公式与 skinview3d 的 FlyingAnimation 一致：
 * 玩家俯身转为水平飞行姿态并展开双臂（鞘翅网格未移植，姿态部分完整）。
 */
class FlyingAnimation : PlayerAnimation() {

    override fun animate(player: PlayerModel, delta: Float) {
        val t = if (progress > 0f) progress * 20f else 0f
        val startProgress = ((t * t) / 100f).coerceIn(0f, 1f)

        // 身体逐渐转为水平（绕 X 轴俯身 90°）
        player.rootRotationX = startProgress * PI / 2f
        player.head.rotationX = if (startProgress > 0.5f) PI / 4f - player.rootRotationX else 0f

        // 双臂向两侧展开
        val basicArmRotationZ = PI * 0.25f * startProgress
        player.leftArm.rotationZ = basicArmRotationZ
        player.rightArm.rotationZ = -basicArmRotationZ
    }
}
