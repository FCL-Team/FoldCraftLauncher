package com.mio.skin

/**
 * 玩家动画基类，语义与 skinview3d 的 PlayerAnimation 一致：
 * [animate] 以当前 progress 计算姿态，随后 progress 累加 delta（秒，已乘 speed）。
 * 动画实例无状态可复用（仅 progress 递增），切换动画时由调用方重置模型姿态。
 */
abstract class PlayerAnimation {

    var speed = 1f
    var progress = 0f

    fun update(player: PlayerModel, deltaTime: Float) {
        val delta = deltaTime * speed
        animate(player, delta)
        progress += delta
    }

    protected abstract fun animate(player: PlayerModel, delta: Float)

    companion object {
        val PI = Math.PI.toFloat()
    }
}
