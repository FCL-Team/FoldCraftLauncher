package com.mio.skin

import android.content.Context

/**
 * 玩家 GLTF 模型运行时：同时加载 classic/slim 两个模型实例（各约 131KB，
 * 解析毫秒级），切换模型零成本；动画播放/推进/绘制全部转发到当前实例。
 */
class GltfPlayerModel(context: Context) {

    private val classic = GltfModel.load(context, CLASSIC_ASSET, isSlim = false)
    private val slimModel = GltfModel.load(context, SLIM_ASSET, isSlim = true)
    private var active = classic

    var slim = false
        private set

    /** 当前动画 id（clip 名） */
    var animationId: String = SkinAnimations.DEFAULT_ID
        private set

    /** 切换 classic/slim：新实例重放到当前动画（切换仅在换肤时发生，重置进度无感知） */
    fun setSlim(value: Boolean) {
        if (slim == value) {
            return
        }
        slim = value
        active = if (value) slimModel else classic
        active.playAnimation(animationId)
    }

    /** 播放指定烘焙动画（clip 名），未知 id 忽略 */
    fun playAnimation(id: String) {
        if (active.playAnimation(id)) {
            animationId = id
        }
    }

    fun update(deltaSeconds: Float) {
        active.update(deltaSeconds)
    }

    /** 绘制皮肤材质网格（须已绑定皮肤纹理） */
    fun drawSkin(
        positionLocation: Int,
        texCoordLocation: Int,
        normalLocation: Int,
        lightMixLocation: Int,
        mvpMatrixLocation: Int,
        normalMatrixLocation: Int,
        mvpBase: FloatArray,
        modelBase: FloatArray
    ) {
        active.draw(
            positionLocation, texCoordLocation, normalLocation, lightMixLocation,
            mvpMatrixLocation, normalMatrixLocation, mvpBase, modelBase, capeOnly = false
        )
    }

    /** 绘制披风网格（须已绑定披风纹理） */
    fun drawCape(
        positionLocation: Int,
        texCoordLocation: Int,
        normalLocation: Int,
        lightMixLocation: Int,
        mvpMatrixLocation: Int,
        normalMatrixLocation: Int,
        mvpBase: FloatArray,
        modelBase: FloatArray
    ) {
        active.draw(
            positionLocation, texCoordLocation, normalLocation, lightMixLocation,
            mvpMatrixLocation, normalMatrixLocation, mvpBase, modelBase, capeOnly = true
        )
    }

    /**
     * 用皮肤像素重建体素化第二层（仅渲染线程调用）。
     * 两个实例都重建，避免 slim 切换后体素层缺失而退回面片。
     */
    fun rebuildSolidLayers(pixels: IntArray, bitmapWidth: Int) {
        classic.rebuildSolidLayers(pixels, bitmapWidth)
        slimModel.rebuildSolidLayers(pixels, bitmapWidth)
    }

    /** EGL context 重建后调用：两个实例的 VBO id 均可能失效，一并重置 */
    fun resetGpuResources() {
        classic.resetGpuResources()
        slimModel.resetGpuResources()
    }

    companion object {
        private const val CLASSIC_ASSET = "img/skin_model/classic-player.gltf"
        private const val SLIM_ASSET = "img/skin_model/slim-player.gltf"
    }
}
