package com.mio.skin

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.min

/**
 * 体素化第二层网格（复刻 3D Skin Layers 的 SolidPixelWrapper，同 Axolotl 实现）：
 * 第二层每个不透明像素生成一个六面贴图的真实立方体，相邻不透明体素之间做面剔除，
 * 替换 GLTF 的零厚度面片层，使第二层边缘实心化。
 *
 * 体素构建于网格局部空间（与 GLTF 网格顶点同空间，Blockbench 的 Y 向下约定，
 * 面命名沿用 3D Skin Layers：down/up/north/south/west/east，UV 锚点记录时对
 * 上下/东西做轴向翻转）。立方体间留微量重叠（epsilon）消除接缝；UV 内缩
 * 1/4096 像素防止 NEAREST 采样渗色。顶点带面法线，片元侧做简易方向光照。
 * 所有 GL 调用须在渲染线程执行。
 */
class SolidSkinLayer {

    private var positions = FloatArray(0)
    private var normals = FloatArray(0)
    private var uvs = FloatArray(0)
    private var vertexCount = 0
    private var vboPositions = 0
    private var vboNormals = 0
    private var vboUvs = 0

    /**
     * 用皮肤像素重建体素网格（仅渲染线程调用）。
     * [boundsMin]/[boundsMax] 为网格局部包围盒（像素单位），[def] 为 [宽, 高, 深, u, v]，
     * [layerName] 用于头/身的缩放校正。
     */
    fun rebuild(
        boundsMin: FloatArray,
        boundsMax: FloatArray,
        layerName: String,
        def: IntArray,
        pixels: IntArray,
        bitmapWidth: Int
    ) {
        val width = def[0]
        val height = def[1]
        val depth = def[2]
        val voxel = FloatArray(3) { (boundsMax[it] - boundsMin[it]) / def[it] }
        val voxels = HashMap<Int, Voxel>()

        // 遍历六面的 UV 像素，不透明像素聚合成体素，并记录各面 UV 锚点
        for (face in 0 until 6) {
            val faceWidth = if (face == WEST || face == EAST) depth else width
            val faceHeight = if (face == DOWN || face == UP) depth else height
            for (u in 0 until faceWidth) {
                for (v in 0 until faceHeight) {
                    val p = facePixel(face, u, v, width, height, depth, def[3], def[4]) ?: continue
                    if (!isOpaque(pixels, bitmapWidth, p.tu, p.tv)) {
                        continue
                    }
                    val key = voxelKey(p.x, p.y, p.z)
                    val entry = voxels.getOrPut(key) { Voxel(p.x, p.y, p.z) }
                    // 同一体素可被多个源面命中：各面保留自己的锚点，避免采样到错位邻居像素
                    entry.anchors[flipFace(face)] = floatArrayOf(p.tu.toFloat(), p.tv.toFloat())
                }
            }
        }
        if (voxels.isEmpty()) {
            positions = FloatArray(0)
            normals = FloatArray(0)
            uvs = FloatArray(0)
            vertexCount = 0
            if (vboPositions != 0) {
                resetGpuResources()
            }
            return
        }

        val pos = ArrayList<Float>(1024)
        val nrm = ArrayList<Float>(1024)
        val uv = ArrayList<Float>(1024)
        // 相邻立方体微量重叠：GLTF 网格边界常带小数坐标，投影后固定间隙仍然可见
        val epsilon = 0.01f
        val min = FloatArray(3)
        val max = FloatArray(3)
        for (voxelEntry in voxels.values) {
            for (c in 0 until 3) {
                min[c] = boundsMin[c] + voxelEntry.pos[c] * voxel[c] - voxel[c] * epsilon
                max[c] = min[c] + voxel[c] + voxel[c] * epsilon * 2
            }
            addCube(pos, nrm, uv, min, max, voxelEntry, voxels)
        }
        applyScaleCorrection(pos, layerName)
        positions = pos.toFloatArray()
        normals = nrm.toFloatArray()
        uvs = uv.toFloatArray()
        vertexCount = uvs.size / 2
        if (vboPositions != 0) {
            uploadToGpu()
        }
    }

    fun draw(
        positionLocation: Int,
        texCoordLocation: Int,
        normalLocation: Int,
        lightMixLocation: Int,
        mvpMatrixLocation: Int,
        normalMatrixLocation: Int,
        mvpMatrix: FloatArray,
        normalMatrix: FloatArray
    ) {
        if (vertexCount == 0) {
            return
        }
        if (vboPositions == 0) {
            uploadToGpu()
        }
        GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(normalMatrixLocation, 1, false, normalMatrix, 0)
        GLES20.glUniform1f(lightMixLocation, 1f)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboNormals)
        GLES20.glVertexAttribPointer(normalLocation, 3, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glEnableVertexAttribArray(normalLocation)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboPositions)
        GLES20.glVertexAttribPointer(positionLocation, 3, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboUvs)
        GLES20.glVertexAttribPointer(texCoordLocation, 2, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun resetGpuResources() {
        if (vboPositions != 0) {
            GLES20.glDeleteBuffers(3, intArrayOf(vboPositions, vboNormals, vboUvs), 0)
            vboPositions = 0
            vboNormals = 0
            vboUvs = 0
        }
    }

    // ---- 体素构建（移植自 Axolotl solid-skin-layer.ts）----

    /** 单个体素：网格坐标 (x, y, z) 与各可见面的 UV 锚点（像素坐标） */
    private class Voxel(x: Int, y: Int, z: Int) {
        val pos = intArrayOf(x, y, z)
        val anchors = HashMap<Int, FloatArray>()
    }

    private class FacePixel(val x: Int, val y: Int, val z: Int, val tu: Int, val tv: Int)

    /**
     * 面上 (u, v) 格点的体素坐标与纹理像素（Minecraft 标准盒式 UV）。
     * 越出部件 UV 矩形的格点返回 null。
     */
    private fun facePixel(
        face: Int, u: Int, v: Int,
        width: Int, height: Int, depth: Int, u0: Int, v0: Int
    ): FacePixel? {
        return when (face) {
            DOWN -> FacePixel(u, height - 1, depth - 1 - v, u0 + depth + u, v0 + v)
            UP -> FacePixel(u, 0, depth - 1 - v, u0 + depth + width + u, v0 + v)
            NORTH -> FacePixel(width - 1 - u, height - 1 - v, 0, u0 + depth + u, v0 + depth + v)
            SOUTH -> FacePixel(u, height - 1 - v, depth - 1, u0 + depth + width + depth + u, v0 + depth + v)
            WEST -> FacePixel(width - 1, height - 1 - v, depth - 1 - u, u0 + u, v0 + depth + v)
            EAST -> FacePixel(0, height - 1 - v, u, u0 + depth + width + u, v0 + depth + v)
            else -> null
        }
    }

    /** 体素坐标轴翻转（down↔up、west↔east）：源面命名与几何面命名轴向相反 */
    private fun flipFace(face: Int): Int = when (face) {
        DOWN -> UP
        UP -> DOWN
        WEST -> EAST
        EAST -> WEST
        else -> face
    }

    private fun voxelKey(x: Int, y: Int, z: Int): Int = x or (y shl 8) or (z shl 16)

    private fun isOpaque(pixels: IntArray, bitmapWidth: Int, u: Int, v: Int): Boolean {
        if (u < 0 || v < 0 || u >= bitmapWidth || v * bitmapWidth + u >= pixels.size) {
            return false
        }
        return pixels[v * bitmapWidth + u] ushr 24 >= ALPHA_THRESHOLD
    }

    /** 生成一个体素的可见面（相邻体素不透明时剔除该面） */
    private fun addCube(
        pos: ArrayList<Float>,
        nrm: ArrayList<Float>,
        uv: ArrayList<Float>,
        min: FloatArray,
        max: FloatArray,
        voxel: Voxel,
        voxels: HashMap<Int, Voxel>
    ) {
        // 某面无专属锚点时依次回退（正常路径每个可见面都有锚点）
        val fallback = voxel.anchors[NORTH] ?: voxel.anchors[SOUTH]
            ?: voxel.anchors[WEST] ?: voxel.anchors[EAST]
            ?: voxel.anchors[DOWN] ?: voxel.anchors[UP]
            ?: return
        fun faceUv(face: Int, du: Int, dv: Int): FloatArray {
            val anchor = voxel.anchors[face] ?: fallback
            // UV 内缩 1/4096 像素：精确边界在 NEAREST 采样下可能圆整到相邻像素
            val inset = 1f / 4096f
            return floatArrayOf(
                (anchor[0] + du + (if (du == 0) inset else -inset)) / TEXTURE_SIZE,
                (anchor[1] + dv + (if (dv == 0) inset else -inset)) / TEXTURE_SIZE
            )
        }
        fun hasVoxel(x: Int, y: Int, z: Int) = voxels.containsKey(voxelKey(x, y, z))
        val x = voxel.pos[0]
        val y = voxel.pos[1]
        val z = voxel.pos[2]

        if (!hasVoxel(x, y, z - 1)) {
            addQuad(pos, nrm, uv,
                floatArrayOf(max[0], min[1], min[2], min[0], min[1], min[2], min[0], max[1], min[2], max[0], max[1], min[2]),
                floatArrayOf(0f, 0f, -1f),
                faceUv(NORTH, 1, 1), faceUv(NORTH, 0, 1), faceUv(NORTH, 0, 0), faceUv(NORTH, 1, 0))
        }
        if (!hasVoxel(x, y, z + 1)) {
            addQuad(pos, nrm, uv,
                floatArrayOf(min[0], min[1], max[2], max[0], min[1], max[2], max[0], max[1], max[2], min[0], max[1], max[2]),
                floatArrayOf(0f, 0f, 1f),
                faceUv(SOUTH, 0, 1), faceUv(SOUTH, 1, 1), faceUv(SOUTH, 1, 0), faceUv(SOUTH, 0, 0))
        }
        if (!hasVoxel(x, y - 1, z)) {
            addQuad(pos, nrm, uv,
                floatArrayOf(min[0], min[1], min[2], max[0], min[1], min[2], max[0], min[1], max[2], min[0], min[1], max[2]),
                floatArrayOf(0f, -1f, 0f),
                faceUv(DOWN, 1, 1), faceUv(DOWN, 0, 1), faceUv(DOWN, 0, 0), faceUv(DOWN, 1, 0))
        }
        if (!hasVoxel(x, y + 1, z)) {
            addQuad(pos, nrm, uv,
                floatArrayOf(min[0], max[1], max[2], max[0], max[1], max[2], max[0], max[1], min[2], min[0], max[1], min[2]),
                floatArrayOf(0f, 1f, 0f),
                faceUv(UP, 1, 0), faceUv(UP, 0, 0), faceUv(UP, 0, 1), faceUv(UP, 1, 1))
        }
        if (!hasVoxel(x - 1, y, z)) {
            addQuad(pos, nrm, uv,
                floatArrayOf(min[0], min[1], min[2], min[0], min[1], max[2], min[0], max[1], max[2], min[0], max[1], min[2]),
                floatArrayOf(-1f, 0f, 0f),
                faceUv(WEST, 0, 1), faceUv(WEST, 1, 1), faceUv(WEST, 1, 0), faceUv(WEST, 0, 0))
        }
        if (!hasVoxel(x + 1, y, z)) {
            addQuad(pos, nrm, uv,
                floatArrayOf(max[0], min[1], max[2], max[0], min[1], min[2], max[0], max[1], min[2], max[0], max[1], max[2]),
                floatArrayOf(1f, 0f, 0f),
                faceUv(EAST, 0, 1), faceUv(EAST, 1, 1), faceUv(EAST, 1, 0), faceUv(EAST, 0, 0))
        }
    }

    /** 生成一面四边形（两个三角形，顶点顺序同 Axolotl：0,1,2,0,2,3） */
    private fun addQuad(
        pos: ArrayList<Float>,
        nrm: ArrayList<Float>,
        uv: ArrayList<Float>,
        corners: FloatArray,
        normal: FloatArray,
        uv0: FloatArray,
        uv1: FloatArray,
        uv2: FloatArray,
        uv3: FloatArray
    ) {
        val quadUvs = arrayOf(uv0, uv1, uv2, uv3)
        for (index in intArrayOf(0, 1, 2, 0, 2, 3)) {
            pos.add(corners[index * 3])
            pos.add(corners[index * 3 + 1])
            pos.add(corners[index * 3 + 2])
            nrm.add(normal[0])
            nrm.add(normal[1])
            nrm.add(normal[2])
            uv.add(quadUvs[index][0])
            uv.add(quadUvs[index][1])
        }
    }

    /** 缩放校正（复刻 Axolotl）：头放大 1.05、四肢 1.02（仅 x/z）、身 1.0 无操作 */
    private fun applyScaleCorrection(pos: ArrayList<Float>, layerName: String) {
        val isHead = layerName == "Hat_Layer"
        val isBody = layerName == "Body_Layer"
        if (isBody) {
            return
        }
        val sx = if (isHead) 1.05f else 1.02f
        val sy = if (isHead) 1.05f else 1f
        val sz = if (isHead) 1.05f else 1.02f
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var minZ = Float.MAX_VALUE
        var maxX = -Float.MAX_VALUE
        var maxY = -Float.MAX_VALUE
        var maxZ = -Float.MAX_VALUE
        for (i in pos.indices step 3) {
            minX = min(minX, pos[i]); maxX = max(maxX, pos[i])
            minY = min(minY, pos[i + 1]); maxY = max(maxY, pos[i + 1])
            minZ = min(minZ, pos[i + 2]); maxZ = max(maxZ, pos[i + 2])
        }
        val cx = (minX + maxX) / 2f
        val cy = (minY + maxY) / 2f
        val cz = (minZ + maxZ) / 2f
        for (i in pos.indices step 3) {
            pos[i] = cx + (pos[i] - cx) * sx
            pos[i + 1] = cy + (pos[i + 1] - cy) * sy
            pos[i + 2] = cz + (pos[i + 2] - cz) * sz
        }
    }

    // ---- GPU 资源 ----

    private fun uploadToGpu() {
        val ids = IntArray(3)
        GLES20.glGenBuffers(3, ids, 0)
        vboPositions = ids[0]
        vboNormals = ids[1]
        vboUvs = ids[2]
        uploadBuffer(vboPositions, positions)
        uploadBuffer(vboNormals, normals)
        uploadBuffer(vboUvs, uvs)
    }

    private fun uploadBuffer(id: Int, data: FloatArray) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id)
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            data.size * 4,
            ByteBuffer.allocateDirect(data.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(data)
                .apply { position(0) },
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    companion object {
        private const val TEXTURE_SIZE = 64f

        // 与片元着色器 alpha<0.1 丢弃阈值一致
        private const val ALPHA_THRESHOLD = 26

        // 面序号（3D Skin Layers 的模型空间面命名）
        private const val DOWN = 0
        private const val UP = 1
        private const val NORTH = 2
        private const val SOUTH = 3
        private const val WEST = 4
        private const val EAST = 5
    }
}
