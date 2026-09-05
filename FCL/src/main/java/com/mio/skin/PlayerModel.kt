package com.mio.skin

/**
 * 玩家 3D 模型，部件几何尺寸/位置/旋转轴心与 skinview3d 的 PlayerObject 一致：
 * 玩家面向 +Z，总高 32 且纵向以原点为中心（脚底 -16、头顶 +16）。
 *
 * 旋转角均为弧度，由动画（[PlayerAnimation] 子类）逐帧写入；
 * 旋转围绕部件挂点（pos）进行，pivot 为部件内网格相对挂点的固定偏移
 * （手臂在肩部、腿在髋部），网格再经自身 offset 偏移。
 * root 平移/旋转为玩家整体变换（奔跑动画的跳跃/躲闪/倾斜）。
 */
class PlayerModel {

    /** 部件内的一层网格（BoxMesh + 相对 pivot 的位置偏移） */
    class Mesh(
        val box: BoxMesh,
        val offsetX: Float = 0f,
        val offsetY: Float = 0f,
        val offsetZ: Float = 0f
    )

    /** 可旋转部件，rotation 围绕挂点旋转；挂点位置可被动画修改（resetJoints 恢复） */
    class Part(
        val baseX: Float,
        val baseY: Float,
        val baseZ: Float,
        val meshes: List<Mesh>
    ) {
        var posX = baseX
        var posY = baseY
        var posZ = baseZ
        var pivotX = 0f
        var pivotY = 0f
        var pivotZ = 0f
        var rotationX = 0f
        var rotationY = 0f
        var rotationZ = 0f

        /** 第二层轮廓挤出网格（与第二层网格同矩阵绘制，无则为 null） */
        var extrusion: OverlayExtrusion? = null
    }

    val head = Part(
        0f, 8f, 0f, listOf(
            Mesh(
                BoxMesh(8f, 8f, 8f, 0f, 0f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT),
                offsetY = 4f
            ),
            // 第二层几何放大但 UV 仍按原始部件尺寸偏移
            Mesh(
                BoxMesh(9f, 9f, 9f, 32f, 0f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 8f, 8f, 8f),
                offsetY = 4f
            )
        )
    )

    val body = Part(
        0f, 2f, 0f, listOf(
            Mesh(BoxMesh(8f, 12f, 4f, 16f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
            Mesh(
                BoxMesh(
                    8.5f,
                    12.5f,
                    4.5f,
                    16f,
                    32f,
                    SKIN_TEXTURE_WIDTH,
                    SKIN_TEXTURE_HEIGHT,
                    8f,
                    12f,
                    4f
                )
            )
        )
    )

    val rightArm = Part(
        -5f, 6f, 0f, listOf(
            Mesh(BoxMesh(4f, 12f, 4f, 40f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
            Mesh(
                BoxMesh(
                    4.5f,
                    12.5f,
                    4.5f,
                    40f,
                    32f,
                    SKIN_TEXTURE_WIDTH,
                    SKIN_TEXTURE_HEIGHT,
                    4f,
                    12f,
                    4f
                )
            )
        )
    )

    val leftArm = Part(
        5f, 6f, 0f, listOf(
            Mesh(BoxMesh(4f, 12f, 4f, 32f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
            Mesh(
                BoxMesh(
                    4.5f,
                    12.5f,
                    4.5f,
                    48f,
                    48f,
                    SKIN_TEXTURE_WIDTH,
                    SKIN_TEXTURE_HEIGHT,
                    4f,
                    12f,
                    4f
                )
            )
        )
    )

    val rightLeg = Part(
        -1.9f, -4f, -0.1f, listOf(
            Mesh(BoxMesh(4f, 12f, 4f, 0f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
            Mesh(
                BoxMesh(
                    4.5f,
                    12.5f,
                    4.5f,
                    0f,
                    32f,
                    SKIN_TEXTURE_WIDTH,
                    SKIN_TEXTURE_HEIGHT,
                    4f,
                    12f,
                    4f
                )
            )
        )
    )

    val leftLeg = Part(
        1.9f, -4f, -0.1f, listOf(
            Mesh(BoxMesh(4f, 12f, 4f, 16f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
            Mesh(
                BoxMesh(
                    4.5f,
                    12.5f,
                    4.5f,
                    0f,
                    48f,
                    SKIN_TEXTURE_WIDTH,
                    SKIN_TEXTURE_HEIGHT,
                    4f,
                    12f,
                    4f
                )
            )
        )
    )

    /** 披风：单层网格，rotationY 恒为 PI（背面朝外），rotationX 由动画驱动 */
    val cape = Part(
        0f, 8f, -2f, listOf(
            Mesh(
                BoxMesh(10f, 16f, 1f, 0f, 0f, CAPE_TEXTURE_WIDTH, CAPE_TEXTURE_HEIGHT),
                offsetY = -8f,
                offsetZ = 0.5f
            )
        )
    )

    val skinParts: List<Part> = listOf(head, body, rightArm, leftArm, rightLeg, leftLeg)

    /**
     * 第二层轮廓挤出网格：为 overlay 不透明像素的边缘生成侧壁（颜色取临近色块），
     * 使第二层边缘实心化，增强 3D 效果。
     */
    val headExtrusion =
        OverlayExtrusion(9f, 9f, 9f, 8f, 8f, 8f, 32f, 0f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)
    val bodyExtrusion = OverlayExtrusion(
        8.5f,
        12.5f,
        4.5f,
        8f,
        12f,
        4f,
        16f,
        32f,
        SKIN_TEXTURE_WIDTH,
        SKIN_TEXTURE_HEIGHT
    )
    val rightArmExtrusion = OverlayExtrusion(
        4.5f,
        12.5f,
        4.5f,
        4f,
        12f,
        4f,
        40f,
        32f,
        SKIN_TEXTURE_WIDTH,
        SKIN_TEXTURE_HEIGHT
    )
    val leftArmExtrusion = OverlayExtrusion(
        4.5f,
        12.5f,
        4.5f,
        4f,
        12f,
        4f,
        48f,
        48f,
        SKIN_TEXTURE_WIDTH,
        SKIN_TEXTURE_HEIGHT
    )
    val rightLegExtrusion = OverlayExtrusion(
        4.5f,
        12.5f,
        4.5f,
        4f,
        12f,
        4f,
        0f,
        32f,
        SKIN_TEXTURE_WIDTH,
        SKIN_TEXTURE_HEIGHT
    )
    val leftLegExtrusion = OverlayExtrusion(
        4.5f,
        12.5f,
        4.5f,
        4f,
        12f,
        4f,
        0f,
        48f,
        SKIN_TEXTURE_WIDTH,
        SKIN_TEXTURE_HEIGHT
    )
    val overlayExtrusions: List<OverlayExtrusion> = listOf(
        headExtrusion,
        bodyExtrusion,
        rightArmExtrusion,
        leftArmExtrusion,
        rightLegExtrusion,
        leftLegExtrusion
    )

    /** 玩家整体偏移与倾斜（奔跑动画的跳跃/躲闪/倾斜，其余动画为 0） */
    var rootX = 0f
    var rootY = 0f
    var rootZ = 0f
    var rootRotationZ = 0f

    private var skinPixels: IntArray? = null
    private var skinBitmapWidth = 0

    var slim = false
        private set

    init {
        rightArm.pivotX = -1f
        rightArm.pivotY = -4f
        leftArm.pivotX = 1f
        leftArm.pivotY = -4f
        rightLeg.pivotY = -6f
        leftLeg.pivotY = -6f
        cape.rotationY = Math.PI.toFloat()
        // 挤出网格跟随所属部件的第二层网格矩阵
        head.extrusion = headExtrusion
        body.extrusion = bodyExtrusion
        rightArm.extrusion = rightArmExtrusion
        leftArm.extrusion = leftArmExtrusion
        rightLeg.extrusion = rightLegExtrusion
        leftLeg.extrusion = leftLegExtrusion
    }

    /**
     * 切换 slim/classic 模型：手臂宽 3/4（第二层 ±0.5），
     * UV 相应变化，pivot 内移保持手臂内侧面贴住身体。
     */
    fun setSlim(value: Boolean) {
        if (slim == value) {
            return
        }
        slim = value
        applySlimArms()
    }

    /**
     * 重置姿态：部件位置/旋转与整体变换恢复默认（切换动画时调用，
     * 语义同 skinview3d 的 resetJoints）。
     */
    fun resetJoints() {
        skinParts.forEach { part ->
            part.posX = part.baseX
            part.posY = part.baseY
            part.posZ = part.baseZ
            part.rotationX = 0f
            part.rotationY = 0f
            part.rotationZ = 0f
        }
        cape.posX = cape.baseX
        cape.posY = cape.baseY
        cape.posZ = cape.baseZ
        cape.rotationX = 0f
        cape.rotationY = Math.PI.toFloat()
        cape.rotationZ = 0f
        rootX = 0f
        rootY = 0f
        rootZ = 0f
        rootRotationZ = 0f
    }

    /**
     * 用皮肤像素重建全部第二层挤出网格（仅渲染线程调用，皮肤纹理更新时触发）。
     */
    fun rebuildOverlayExtrusions(pixels: IntArray, bitmapWidth: Int) {
        skinPixels = pixels
        skinBitmapWidth = bitmapWidth
        overlayExtrusions.forEach { it.rebuild(pixels, bitmapWidth) }
    }

    /**
     * EGL context 重建后调用：所有网格的 VBO id 已失效，重置以便下次绘制时重新上传。
     */
    fun resetGpuResources() {
        skinParts.forEach { part -> part.meshes.forEach { it.box.resetGpuResources() } }
        cape.meshes.forEach { it.box.resetGpuResources() }
        overlayExtrusions.forEach { it.resetGpuResources() }
    }

    private fun applySlimArms() {
        val armWidth = if (slim) 3f else 4f
        val armOverlayWidth = if (slim) 3.5f else 4.5f
        rightArm.pivotX = if (slim) -0.5f else -1f
        leftArm.pivotX = if (slim) 0.5f else 1f
        rightArm.meshes[0].box.setGeometry(
            armWidth,
            12f,
            4f,
            40f,
            16f,
            SKIN_TEXTURE_WIDTH,
            SKIN_TEXTURE_HEIGHT
        )
        rightArm.meshes[1].box.setGeometry(
            armOverlayWidth,
            12.5f,
            4.5f,
            40f,
            32f,
            SKIN_TEXTURE_WIDTH,
            SKIN_TEXTURE_HEIGHT,
            armWidth,
            12f,
            4f
        )
        leftArm.meshes[0].box.setGeometry(
            armWidth,
            12f,
            4f,
            32f,
            48f,
            SKIN_TEXTURE_WIDTH,
            SKIN_TEXTURE_HEIGHT
        )
        leftArm.meshes[1].box.setGeometry(
            armOverlayWidth,
            12.5f,
            4.5f,
            48f,
            48f,
            SKIN_TEXTURE_WIDTH,
            SKIN_TEXTURE_HEIGHT,
            armWidth,
            12f,
            4f
        )
        // 手臂挤出网格几何随 slim 变化，setGeometry 内部会用缓存的皮肤像素重建
        rightArmExtrusion.setGeometry(armOverlayWidth, 12.5f, 4.5f, armWidth, 12f, 4f, 40f, 32f)
        leftArmExtrusion.setGeometry(armOverlayWidth, 12.5f, 4.5f, armWidth, 12f, 4f, 48f, 48f)
    }

    companion object {
        private const val SKIN_TEXTURE_WIDTH = 64f
        private const val SKIN_TEXTURE_HEIGHT = 64f
        private const val CAPE_TEXTURE_WIDTH = 64f
        private const val CAPE_TEXTURE_HEIGHT = 32f
    }
}
