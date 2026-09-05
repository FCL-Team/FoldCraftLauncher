package com.mio.skin

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mio.datastore.SkinAnimationSetting
import com.mio.datastore.skinAnimationDataStore
import com.tungsten.fcl.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 皮肤动画注册表：动画切换弹窗与持久化共用的动画条目，
 * [Entry.id] 为持久化用的稳定标识。
 */
object SkinAnimations {

    class Entry(
        val id: String,
        val nameRes: Int,
        val factory: () -> PlayerAnimation,
        val type: Class<out PlayerAnimation>
    )

    val entries = listOf(
        Entry("idle", R.string.animation_idle, { IdleAnimation() }, IdleAnimation::class.java),
        Entry("walking", R.string.animation_walking, { WalkingAnimation() }, WalkingAnimation::class.java),
        Entry("running", R.string.animation_running, { RunningAnimation() }, RunningAnimation::class.java),
        Entry(
            "hands_up_running",
            R.string.animation_hands_up_running,
            { HandsUpRunningAnimation() },
            HandsUpRunningAnimation::class.java
        ),
        Entry(
            "hands_up_jumping",
            R.string.animation_hands_up_jumping,
            { HandsUpJumpingAnimation() },
            HandsUpJumpingAnimation::class.java
        ),
        Entry("flying", R.string.animation_flying, { FlyingAnimation() }, FlyingAnimation::class.java),
        Entry("swim", R.string.animation_swim, { SwimAnimation() }, SwimAnimation::class.java),
        Entry("crouch", R.string.animation_crouch, { CrouchAnimation() }, CrouchAnimation::class.java),
        Entry("wave", R.string.animation_wave, { WaveAnimation() }, WaveAnimation::class.java),
        Entry("hit", R.string.animation_hit, { HitAnimation() }, HitAnimation::class.java)
    )

    /** 按 id 创建动画实例，未知 id 回退走路 */
    fun byId(id: String): PlayerAnimation =
        entries.firstOrNull { it.id == id }?.factory?.invoke() ?: WalkingAnimation()

    /** 动画实例对应的持久化 id */
    fun idOf(animation: PlayerAnimation): String =
        entries.firstOrNull { it.type == animation.javaClass }?.id ?: "walking"
}

/** 恢复上次选择的动画（异步读取，读取完成后切换） */
fun Context.restoreSkinAnimation(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        val id = skinAnimationDataStore.data.first().animationId
        renderer.playAnimation(SkinAnimations.byId(id))
    }
}

/** 保存当前动画为下次启动的选择 */
fun Context.saveSkinAnimation(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        skinAnimationDataStore.updateData {
            SkinAnimationSetting(animationId = SkinAnimations.idOf(renderer.animation))
        }
    }
}
