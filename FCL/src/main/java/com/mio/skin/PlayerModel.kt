package com.mio.skin

/**
 * 玩家 3D 模型，部件几何尺寸/位置/旋转轴心与 skinview3d 的 PlayerObject 一致：
 * 玩家面向 +Z，总高 32 且纵向以原点为中心（脚底 -16、头顶 +16）。
 *
 * 旋转角均为弧度，由动画（[WalkingAnimation]）逐帧写入；
 * 旋转围绕部件挂点（position）进行，pivot 为部件内网格相对挂点的固定偏移
 * （手臂在肩部、腿在髋部），网格再经自身 offset 偏移。
 */
class PlayerModel {

    /** 部件内的一层网格（BoxMesh + 相对 pivot 的位置偏移） */
    class Mesh(
        val box: BoxMesh,
        val offsetX: Float = 0f,
        val offsetY: Float = 0f,
        val offsetZ: Float = 0f
    )

    /** 可旋转部件，rotation 围绕挂点旋转 */
    class Part(
        val posX: Float,
        val posY: Float,
        val posZ: Float,
        val meshes: List<Mesh>
    ) {
        var pivotX = 0f
        var pivotY = 0f
        var pivotZ = 0f
        var rotationX = 0f
        var rotationY = 0f
        var rotationZ = 0f
    }

    val head = Part(0f, 8f, 0f, listOf(
        Mesh(BoxMesh(8f, 8f, 8f, 0f, 0f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT), offsetY = 4f),
        // 第二层几何放大但 UV 仍按原始部件尺寸偏移
        Mesh(BoxMesh(9f, 9f, 9f, 32f, 0f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 8f, 8f, 8f), offsetY = 4f)
    ))

    val body = Part(0f, 2f, 0f, listOf(
        Mesh(BoxMesh(8f, 12f, 4f, 16f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
        Mesh(BoxMesh(8.5f, 12.5f, 4.5f, 16f, 32f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 8f, 12f, 4f))
    ))

    val rightArm = Part(-5f, 6f, 0f, listOf(
        Mesh(BoxMesh(4f, 12f, 4f, 40f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
        Mesh(BoxMesh(4.5f, 12.5f, 4.5f, 40f, 32f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 4f, 12f, 4f))
    ))

    val leftArm = Part(5f, 6f, 0f, listOf(
        Mesh(BoxMesh(4f, 12f, 4f, 32f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
        Mesh(BoxMesh(4.5f, 12.5f, 4.5f, 48f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 4f, 12f, 4f))
    ))

    val rightLeg = Part(-1.9f, -4f, -0.1f, listOf(
        Mesh(BoxMesh(4f, 12f, 4f, 0f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
        Mesh(BoxMesh(4.5f, 12.5f, 4.5f, 0f, 32f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 4f, 12f, 4f))
    ))

    val leftLeg = Part(1.9f, -4f, -0.1f, listOf(
        Mesh(BoxMesh(4f, 12f, 4f, 16f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)),
        Mesh(BoxMesh(4.5f, 12.5f, 4.5f, 0f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, 4f, 12f, 4f))
    ))

    /** 披风：单层网格，rotationY 恒为 PI（背面朝外），rotationX 由动画驱动 */
    val cape = Part(0f, 8f, -2f, listOf(
        Mesh(BoxMesh(10f, 16f, 1f, 0f, 0f, CAPE_TEXTURE_WIDTH, CAPE_TEXTURE_HEIGHT), offsetY = -8f, offsetZ = 0.5f)
    ))

    val skinParts: List<Part> = listOf(head, body, rightArm, leftArm, rightLeg, leftLeg)

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
     * EGL context 重建后调用：所有网格的 VBO id 已失效，重置以便下次绘制时重新上传。
     */
    fun resetGpuResources() {
        skinParts.forEach { part -> part.meshes.forEach { it.box.resetGpuResources() } }
        cape.meshes.forEach { it.box.resetGpuResources() }
    }

    private fun applySlimArms() {
        val armWidth = if (slim) 3f else 4f
        val armOverlayWidth = if (slim) 3.5f else 4.5f
        rightArm.pivotX = if (slim) -0.5f else -1f
        leftArm.pivotX = if (slim) 0.5f else 1f
        rightArm.meshes[0].box.setGeometry(armWidth, 12f, 4f, 40f, 16f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)
        rightArm.meshes[1].box.setGeometry(
            armOverlayWidth, 12.5f, 4.5f, 40f, 32f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, armWidth, 12f, 4f
        )
        leftArm.meshes[0].box.setGeometry(armWidth, 12f, 4f, 32f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT)
        leftArm.meshes[1].box.setGeometry(
            armOverlayWidth, 12.5f, 4.5f, 48f, 48f, SKIN_TEXTURE_WIDTH, SKIN_TEXTURE_HEIGHT, armWidth, 12f, 4f
        )
    }

    companion object {
        private const val SKIN_TEXTURE_WIDTH = 64f
        private const val SKIN_TEXTURE_HEIGHT = 64f
        private const val CAPE_TEXTURE_WIDTH = 64f
        private const val CAPE_TEXTURE_HEIGHT = 32f
    }
}
