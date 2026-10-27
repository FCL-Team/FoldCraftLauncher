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

    /**
     * 体素化第二层的定义：SolidSkinLayer 与皮肤规范 UV 矩形（[宽, 高, 深, u, v]），
     * 以及体素输出空间的包围盒（第二层盒子的 min/max，单位 MC 像素）。
     * name 沿用 3D Skin Layers 的部件命名，供体素层的缩放校正识别头/身。
     */
    class SolidLayerPart(
        val layer: SolidSkinLayer,
        val name: String,
        val def: IntArray,
        val boundsMin: FloatArray,
        val boundsMax: FloatArray
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

        /** 体素化第二层（仅六个第二层部件；3D 皮肤层开启时替代第二层面片绘制） */
        var solidLayer: SolidLayerPart? = null
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

    /** 玩家整体偏移与倾斜（飞行俯身/游泳下潜/奔跑的跳跃躲闪倾斜、旋转弹跳的自转，其余动画为 0） */
    var rootX = 0f
    var rootY = 0f
    var rootZ = 0f
    var rootRotationX = 0f
    var rootRotationY = 0f
    var rootRotationZ = 0f

    /** 玩家整体形变（Q 弹压扁拉伸），1 为原状；以脚底为支点应用 */
    var rootScaleY = 1f
    var rootScaleXZ = 1f

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
        // 体素化第二层：def 为皮肤规范 UV 矩形（宽,高,深,u,v），bounds 为第二层盒子
        // 的包围盒——体素尺寸 = 盒尺寸/def，体素天然比第一层像素大（帽子 9/8），
        // 与 3D Skin Layers 的缩放校正观感一致
        head.solidLayer = SolidLayerPart(
            SolidSkinLayer(), "Hat_Layer", intArrayOf(8, 8, 8, 32, 0),
            floatArrayOf(-4.5f, -4.5f, -4.5f), floatArrayOf(4.5f, 4.5f, 4.5f)
        )
        body.solidLayer = SolidLayerPart(
            SolidSkinLayer(), "Body_Layer", intArrayOf(8, 12, 4, 16, 32),
            floatArrayOf(-4.25f, -6.25f, -2.25f), floatArrayOf(4.25f, 6.25f, 2.25f)
        )
        rightArm.solidLayer = armSolidLayer(rightArm, "Right_Arm_Layer", 40, 32)
        leftArm.solidLayer = armSolidLayer(leftArm, "Left_Arm_Layer", 48, 48)
        rightLeg.solidLayer = SolidLayerPart(
            SolidSkinLayer(), "Right_Leg_Layer", intArrayOf(4, 12, 4, 0, 32),
            floatArrayOf(-2.25f, -6.25f, -2.25f), floatArrayOf(2.25f, 6.25f, 2.25f)
        )
        leftLeg.solidLayer = SolidLayerPart(
            SolidSkinLayer(), "Left_Leg_Layer", intArrayOf(4, 12, 4, 0, 48),
            floatArrayOf(-2.25f, -6.25f, -2.25f), floatArrayOf(2.25f, 6.25f, 2.25f)
        )
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
        rootRotationX = 0f
        rootRotationY = 0f
        rootRotationZ = 0f
        rootScaleY = 1f
        rootScaleXZ = 1f
    }

    /**
     * 用皮肤像素重建全部体素化第二层（仅渲染线程调用，皮肤纹理更新时触发）。
     */
    fun rebuildSolidLayers(pixels: IntArray, bitmapWidth: Int) {
        skinPixels = pixels
        skinBitmapWidth = bitmapWidth
        rebuildAllSolidLayers()
    }

    /**
     * EGL context 重建后调用：所有网格的 VBO id 已失效，重置以便下次绘制时重新上传。
     */
    fun resetGpuResources() {
        skinParts.forEach { part -> part.meshes.forEach { it.box.resetGpuResources() } }
        cape.meshes.forEach { it.box.resetGpuResources() }
        skinParts.forEach { part -> part.solidLayer?.layer?.resetGpuResources() }
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
        // 手臂体素层的定义随 slim 变化（def 宽 3/4、包围盒 ±1.75/±2.25），layer 复用保留 VBO
        rightArm.solidLayer = armSolidLayer(rightArm, "Right_Arm_Layer", 40, 32)
        leftArm.solidLayer = armSolidLayer(leftArm, "Left_Arm_Layer", 48, 48)
        rebuildAllSolidLayers()
    }

    private fun armSolidLayer(part: Part, name: String, u: Int, v: Int): SolidLayerPart {
        val half = if (slim) 1.75f else 2.25f
        val width = if (slim) 3 else 4
        return SolidLayerPart(
            part.solidLayer?.layer ?: SolidSkinLayer(), name, intArrayOf(width, 12, 4, u, v),
            floatArrayOf(-half, -6.25f, -2.25f), floatArrayOf(half, 6.25f, 2.25f)
        )
    }

    private fun rebuildAllSolidLayers() {
        val pixels = skinPixels ?: return
        skinParts.forEach { part ->
            part.solidLayer?.let { s ->
                s.layer.rebuild(s.boundsMin, s.boundsMax, s.name, s.def, pixels, skinBitmapWidth)
            }
        }
    }

    companion object {
        private const val SKIN_TEXTURE_WIDTH = 64f
        private const val SKIN_TEXTURE_HEIGHT = 64f
        private const val CAPE_TEXTURE_WIDTH = 64f
        private const val CAPE_TEXTURE_HEIGHT = 32f
    }
}
