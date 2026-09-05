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
 * [Entry.id] 为模型内烘焙 clip 名，也是持久化用的稳定标识。
 * 后续通过 Blockbench 向模型加入新 clip 时，在此追加条目与对应字符串即可。
 */
object SkinAnimations {

    const val DEFAULT_ID = "idle"

    class Entry(
        val id: String,
        val nameRes: Int
    )

    val entries = listOf(
        Entry("idle", R.string.animation_idle),
        Entry("idle_sub_1", R.string.animation_idle_variant_1),
        Entry("idle_sub_2", R.string.animation_idle_variant_2),
        Entry("idle_sub_3", R.string.animation_idle_variant_3)
    )

    /** 校验动画 id，未知 id（含旧版持久化值）回退默认待机 */
    fun validId(id: String): String =
        entries.firstOrNull { it.id == id }?.id ?: DEFAULT_ID
}

/** 恢复上次选择的动画（异步读取，读取完成后切换） */
fun Context.restoreSkinAnimation(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        val id = skinAnimationDataStore.data.first().animationId
        renderer.playAnimation(SkinAnimations.validId(id))
    }
}

/** 保存当前动画为下次启动的选择 */
fun Context.saveSkinAnimation(renderer: SkinRenderer) {
    if (this !is LifecycleOwner) return
    lifecycleScope.launch {
        skinAnimationDataStore.updateData {
            SkinAnimationSetting(animationId = renderer.animationId)
        }
    }
}
