package com.mio.skin

import android.graphics.Bitmap
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.yggdrasil.TextureModel
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task

/**
 * 皮肤纹理加载器：替代原先基于 fakefx ObjectBinding/AsyncMappedBinding 的加载链
 * （那条链跨三个线程回调，异步结果与页面重建之间存在覆盖竞态，曾导致模型概率性停在默认皮肤）。
 *
 * 现为明确的回调式加载：IO 线程执行 [TexturesLoader.loadSkinAndCape]，结果统一投递到主线程，
 * 代际号保证只有最新一次请求的结果生效；textures 属性（微软/正版账户 profile 需异步加载）
 * 就绪后自动重载。
 *
 * 线程契约：[load] / [release] 必须在主线程调用。
 */
class SkinTextureLoader(private val renderer: SkinRenderer) {

    private var generation = 0

    /** 最近一次请求的账户，回调生效与发起请求时都会同步，用于跳过同账户重复加载 */
    private var lastAccount: Account? = null

    /** 监听 textures 属性变化（profile 异步就绪后触发重载），账户切换/释放时解绑 */
    private var texturesBinding: ObjectBinding<*>? = null
    private var texturesListener: InvalidationListener? = null

    /**
     * 加载 [account] 的皮肤到渲染器；同账户且非强制时跳过。
     * [force] 用于皮肤文件变更/账户刷新后的强制重载。
     */
    fun load(account: Account?, force: Boolean) {
        if (account === lastAccount && !force) {
            return
        }
        generation++
        val gen = generation
        unbindTexturesListener()
        if (account == null) {
            lastAccount = null
            renderer.updateTexture(TexturesLoader.getDefaultSkin(TextureModel.ALEX).image(), null)
            return
        }
        lastAccount = account
        val binding = account.textures
        val listener = InvalidationListener { if (gen == generation) load(account, true) }
        binding.addListener(listener)
        texturesBinding = binding
        texturesListener = listener

        val task: Task<Array<Bitmap>> = Task.supplyAsync { TexturesLoader.loadSkinAndCape(account) }
        task.thenAcceptAsync<Exception>(Schedulers.androidUIThread()) { result ->
            if (gen == generation) {
                lastAccount = account
                renderer.updateTexture(result[0], result[1])
            }
        }.start()
    }

    /** 页面 detach 时调用：解绑监听并使在答回调失效 */
    fun release() {
        generation++
        unbindTexturesListener()
    }

    private fun unbindTexturesListener() {
        val listener = texturesListener
        val binding = texturesBinding
        if (listener != null && binding != null) {
            binding.removeListener(listener)
        }
        texturesListener = null
        texturesBinding = null
    }
}
