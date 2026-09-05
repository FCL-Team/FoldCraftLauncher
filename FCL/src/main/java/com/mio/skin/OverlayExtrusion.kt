package com.mio.skin

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * 第二层（overlay）不透明像素的侧壁挤出网格：为轮廓边缘（自身不透明而相邻像素透明的
 * 像素）生成从第二层表面到第一层表面的侧壁，颜色取该像素自身的纹理色块（UV 指向像素
 * 中心），使第二层边缘看起来是实心的，增强 3D 效果。
 *
 * 几何与 UV 尺寸关系同 [BoxMesh]：外表面为第二层放大尺寸，内表面贴合第一层尺寸，
 * UV 矩形按第一层尺寸偏移。网格由皮肤像素重建（渲染线程调用）。
 */
class OverlayExtrusion(
    outerX: Float, outerY: Float, outerZ: Float,
    innerX: Float, innerY: Float, innerZ: Float,
    u: Float, v: Float,
    private val textureWidth: Float, private val textureHeight: Float
) {
    private var outerX = outerX
    private var outerY = outerY
    private var outerZ = outerZ
    private var innerX = innerX
    private var innerY = innerY
    private var innerZ = innerZ
    private var u = u
    private var v = v

    private var positions = FloatArray(0)
    private var uvs = FloatArray(0)
    private var vertexCount = 0
    private var vboPositions = 0
    private var vboUvs = 0
    private var skinPixels: IntArray? = null
    private var skinBitmapWidth = 0
    private var faces: List<Face> = emptyList()

    init {
        rebuildFaces()
    }

    /**
     * 重建几何（slim 切换时手臂尺寸变化），已有皮肤像素则同步重建网格。
     */
    fun setGeometry(
        outerX: Float, outerY: Float, outerZ: Float,
        innerX: Float, innerY: Float, innerZ: Float,
        u: Float, v: Float
    ) {
        this.outerX = outerX
        this.outerY = outerY
        this.outerZ = outerZ
        this.innerX = innerX
        this.innerY = innerY
        this.innerZ = innerZ
        this.u = u
        this.v = v
        rebuildFaces()
        val pixels = skinPixels
        if (pixels != null) {
            rebuild(pixels, skinBitmapWidth)
        }
    }

    /** 用皮肤像素重建挤出网格（仅渲染线程调用） */
    fun rebuild(pixels: IntArray, bitmapWidth: Int) {
        skinPixels = pixels
        skinBitmapWidth = bitmapWidth
        val pos = ArrayList<Float>(256)
        val uv = ArrayList<Float>(128)
        for (face in faces) {
            val px0 = face.u1.toInt()
            val py0 = face.v1.toInt()
            for (j in 0 until face.ph) {
                for (i in 0 until face.pw) {
                    val pixel = pixels[(py0 + j) * bitmapWidth + px0 + i]
                    if (pixel ushr 24 < ALPHA_THRESHOLD) {
                        continue
                    }
                    // 侧壁 UV 用像素中心（NEAREST 采样稳定取到该像素色块）
                    val cu = (face.u1 + i + 0.5f) / textureWidth
                    val cv = (face.v1 + j + 0.5f) / textureHeight
                    // 仅在相邻像素透明（或越出 UV 矩形）时生成该方向的侧壁
                    if (isTransparent(face, pixels, bitmapWidth, i, j - 1)) {
                        addWall(pos, uv, face, i, j, i + 1, j, cu, cv)
                    }
                    if (isTransparent(face, pixels, bitmapWidth, i, j + 1)) {
                        addWall(pos, uv, face, i, j + 1, i + 1, j + 1, cu, cv)
                    }
                    if (isTransparent(face, pixels, bitmapWidth, i - 1, j)) {
                        addWall(pos, uv, face, i, j, i, j + 1, cu, cv)
                    }
                    if (isTransparent(face, pixels, bitmapWidth, i + 1, j)) {
                        addWall(pos, uv, face, i + 1, j, i + 1, j + 1, cu, cv)
                    }
                }
            }
        }
        positions = pos.toFloatArray()
        uvs = uv.toFloatArray()
        vertexCount = uvs.size / 2
        if (vboPositions != 0) {
            uploadToGpu()
        }
    }

    /** 六个面的外/内表面角点与 UV 矩形（角点顺序同 BoxMesh：左上/右上/左下/右下） */
    private fun rebuildFaces() {
        val hx = outerX / 2f
        val hy = outerY / 2f
        val hz = outerZ / 2f
        val w = innerX
        val h = innerY
        val d = innerZ
        faces = listOf(
            face(floatArrayOf(-hx, hy, hz, hx, hy, hz, -hx, -hy, hz, hx, -hy, hz),
                u + d, v + d, w, h, 2, 1f),
            face(floatArrayOf(hx, hy, -hz, -hx, hy, -hz, hx, -hy, -hz, -hx, -hy, -hz),
                u + w + d * 2, v + d, w, h, 2, -1f),
            face(floatArrayOf(-hx, hy, -hz, -hx, hy, hz, -hx, -hy, -hz, -hx, -hy, hz),
                u, v + d, d, h, 0, -1f),
            face(floatArrayOf(hx, hy, hz, hx, hy, -hz, hx, -hy, hz, hx, -hy, -hz),
                u + w + d, v + d, d, h, 0, 1f),
            face(floatArrayOf(-hx, hy, -hz, hx, hy, -hz, -hx, hy, hz, hx, hy, hz),
                u + d, v, w, d, 1, 1f),
            face(floatArrayOf(-hx, -hy, -hz, hx, -hy, -hz, -hx, -hy, hz, hx, -hy, hz),
                u + w + d, v, w, d, 1, -1f)
        )
    }

    /**
     * 内表面角点：仅法线轴移动到第一层表面，切向坐标与外边缘对齐——
     * 侧壁为正交直壁，不产生斜面衔接。
     */
    private fun face(
        outerCorners: FloatArray, u1: Float, v1: Float, pw: Float, ph: Float,
        axis: Int, sign: Float
    ): Face {
        val innerHalf = floatArrayOf(innerX / 2f, innerY / 2f, innerZ / 2f)
        val innerCorners = FloatArray(12)
        for (i in 0 until 4) {
            for (k in 0 until 3) {
                innerCorners[i * 3 + k] = if (k == axis) sign * innerHalf[axis] else outerCorners[i * 3 + k]
            }
        }
        return Face(outerCorners, innerCorners, u1, v1, pw.toInt(), ph.toInt())
    }

    /** UV 矩形内 (i,j) 格点在面上的三维坐标（fi: 0..1 水平，fj: 0..1 垂直） */
    private fun corner(face: Face, outer: Boolean, i: Int, j: Int, out: FloatArray, offset: Int) {
        val pts = if (outer) face.outer else face.inner
        val fi = i.toFloat() / face.pw
        val fj = j.toFloat() / face.ph
        for (k in 0 until 3) {
            val top = pts[k] + (pts[3 + k] - pts[k]) * fi
            val bottom = pts[6 + k] + (pts[9 + k] - pts[6 + k]) * fi
            out[offset + k] = top + (bottom - top) * fj
        }
    }

    /** 面上 (i,j) 像素的邻位是否透明（越出 UV 矩形视为透明） */
    private fun isTransparent(face: Face, pixels: IntArray, bitmapWidth: Int, i: Int, j: Int): Boolean {
        if (i < 0 || i >= face.pw || j < 0 || j >= face.ph) {
            return true
        }
        val pixel = pixels[(face.v1.toInt() + j) * bitmapWidth + face.u1.toInt() + i]
        return pixel ushr 24 < ALPHA_THRESHOLD
    }

    /** 生成一面侧壁（外边 E1E2 到内边 I1I2 两个三角形，UV 全用像素中心） */
    private fun addWall(
        pos: ArrayList<Float>, uv: ArrayList<Float>,
        face: Face, i1: Int, j1: Int, i2: Int, j2: Int,
        cu: Float, cv: Float
    ) {
        val e1 = FloatArray(3); val e2 = FloatArray(3)
        val i1p = FloatArray(3); val i2p = FloatArray(3)
        corner(face, true, i1, j1, e1, 0)
        corner(face, true, i2, j2, e2, 0)
        corner(face, false, i1, j1, i1p, 0)
        corner(face, false, i2, j2, i2p, 0)
        for (v in arrayOf(e1, e2, i1p, e2, i2p, i1p)) {
            pos.add(v[0]); pos.add(v[1]); pos.add(v[2])
            uv.add(cu); uv.add(cv)
        }
    }

    private fun uploadToGpu() {
        if (vboPositions == 0) {
            val ids = IntArray(2)
            GLES20.glGenBuffers(2, ids, 0)
            vboPositions = ids[0]
            vboUvs = ids[1]
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboPositions)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, positions.size * 4, toFloatBuffer(positions), GLES20.GL_STATIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboUvs)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, uvs.size * 4, toFloatBuffer(uvs), GLES20.GL_STATIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    /** 绘制挤出网格（须已绑定皮肤纹理，顶点属性已启用）；无内容时为空操作 */
    fun draw(positionLocation: Int, texCoordLocation: Int) {
        if (vertexCount == 0) {
            return
        }
        if (vboPositions == 0) {
            uploadToGpu()
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboPositions)
        GLES20.glVertexAttribPointer(positionLocation, 3, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboUvs)
        GLES20.glVertexAttribPointer(texCoordLocation, 2, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
    }

    /** EGL context 重建后调用：VBO id 失效，重置以便下次绘制时重新上传 */
    fun resetGpuResources() {
        vboPositions = 0
        vboUvs = 0
    }

    private fun toFloatBuffer(array: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(array.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(array)
            .apply { position(0) }

    private class Face(
        val outer: FloatArray,
        val inner: FloatArray,
        val u1: Float,
        val v1: Float,
        val pw: Int,
        val ph: Int
    )

    companion object {
        // 与片元着色器 alpha<0.1 丢弃阈值一致
        private const val ALPHA_THRESHOLD = 26
    }
}
