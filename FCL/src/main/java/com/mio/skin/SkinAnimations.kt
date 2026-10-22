package com.mio.skin

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mio.datastore.appSettingsDataStore
import com.tungsten.fcl.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 皮肤动画注册表：动画切换弹窗与持久化共用的动画条目，
 * [Entry.id] 为持久化用的稳定标识。
 * 新增动画时实现 [PlayerAnimation] 子类并在此追加条目与对应字符串即可。
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
        Entry("hit", R.string.animation_hit, { HitAnimation() }, HitAnimation::class.java),
        Entry(
            "spin_jump",
            R.string.animation_spin_jump,
            { SpinJumpAnimation() },
            SpinJumpAnimation::class.java
        )
    )

    /** 按 id 创建动画实例，未知 id（含旧版持久化值）回退走路 */
    fun byId(id: String): PlayerAnimation =
        entries.firstOrNull { it.id == id }?.factory?.invoke() ?: WalkingAnimation()

    /** 动画实例对应的持久化 id */
    fun idOf(animation: PlayerAnimation): String =
        entries.firstOrNull { it.type == animation.javaClass }?.id ?: "walking"
}

/** 恢复上次选择的动画与 3D 皮肤层开关（异步读取，读取完成后应用） */
fun Context.restoreSkinSettings(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        val setting = appSettingsDataStore.data.first().skinAnimation
        renderer.playAnimation(SkinAnimations.byId(setting.animationId))
        renderer.overlayEnabled = setting.solidLayerEnabled
    }
}

/** 恢复 3D 皮肤层开关（异步读取，读取完成后应用，不含动画选择），
 *  供主界面之外其他使用皮肤模型的地方保持一致的显示效果 */
fun Context.restoreSkinSwitches(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        renderer.overlayEnabled = appSettingsDataStore.data.first().skinAnimation.solidLayerEnabled
    }
}

/** 保存当前动画与 3D 皮肤层开关为下次启动的选择 */
fun Context.saveSkinSettings(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        appSettingsDataStore.updateData {
            it.copy(skinAnimation = it.skinAnimation.copy(
                animationId = SkinAnimations.idOf(renderer.animation),
                solidLayerEnabled = renderer.overlayEnabled
            ))
        }
    }
}
