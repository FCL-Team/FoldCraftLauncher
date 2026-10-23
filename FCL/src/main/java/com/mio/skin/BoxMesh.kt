package com.mio.skin

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Minecraft 标准盒式 UV 的立方体网格（6 面 × 2 三角形，GL_TRIANGLES 绘制）。
 *
 * 几何尺寸与 UV 尺寸分离：第二层（overlay）盒子几何放大（如帽子 9³）但 UV 矩形
 * 仍按原始部件尺寸（8×8×8）偏移，与 skinview3d 一致。
 * UV 矩形偏移公式与 skinview3d 的 setUVs 一致；纹理由 GLUtils.texImage2D 上传
 * （v=0 对应图片首行），因此换算时直接使用图片像素坐标，不做 three.js 的 1-y 翻转。
 * 所有 GL 调用须在渲染线程执行。
 */
class BoxMesh(
    sizeX: Float, sizeY: Float, sizeZ: Float,
    u: Float, v: Float,
    textureWidth: Float, textureHeight: Float,
    uvWidth: Float = sizeX,
    uvHeight: Float = sizeY,
    uvDepth: Float = sizeZ
) {
    val vertexCount = 36

    private var positions = FloatArray(vertexCount * 3)
    private var uvs = FloatArray(vertexCount * 2)
    private var vboPositions = 0
    private var vboUvs = 0

    init {
        build(sizeX, sizeY, sizeZ, u, v, textureWidth, textureHeight, uvWidth, uvHeight, uvDepth)
    }

    /**
     * 重建几何（slim 切换时手臂尺寸与 UV 变化），已上传 VBO 则同步重传。
     */
    fun setGeometry(
        sizeX: Float, sizeY: Float, sizeZ: Float,
        u: Float, v: Float,
        textureWidth: Float, textureHeight: Float,
        uvWidth: Float = sizeX,
        uvHeight: Float = sizeY,
        uvDepth: Float = sizeZ
    ) {
        build(sizeX, sizeY, sizeZ, u, v, textureWidth, textureHeight, uvWidth, uvHeight, uvDepth)
        if (vboPositions != 0) {
            uploadToGpu()
        }
    }

    private fun build(
        sizeX: Float, sizeY: Float, sizeZ: Float,
        u: Float, v: Float,
        tw: Float, th: Float,
        uw: Float, uh: Float, ud: Float
    ) {
        val hx = sizeX / 2f
        val hy = sizeY / 2f
        val hz = sizeZ / 2f
        // 每面 12 个分量（左上/右上/左下/右下四角 xyz）+ 4 个分量（UV 矩形 u1,v1,u2,v2，图片像素坐标）
        // 面顺序：前(+Z)、后(-Z)、右(-X)、左(+X)、上(+Y)、下(-Y)，
        // UV 矩形偏移遵循 Minecraft 皮肤规范（同 skinview3d setUVs，宽高深用 UV 尺寸）：
        // 右(u,v+d) 前(u+d,v+d) 左(u+w+d,v+d) 后(u+2d+w,v+d) 上(u+d,v) 下(u+w+d,v)
        val faces = arrayOf(
            floatArrayOf(
                -hx, hy, hz, hx, hy, hz, -hx, -hy, hz, hx, -hy, hz,
                u + ud, v + ud, u + uw + ud, v + ud + uh
            ),
            floatArrayOf(
                hx, hy, -hz, -hx, hy, -hz, hx, -hy, -hz, -hx, -hy, -hz,
                u + uw + ud * 2, v + ud, u + uw * 2 + ud * 2, v + ud + uh
            ),
            floatArrayOf(
                -hx, hy, -hz, -hx, hy, hz, -hx, -hy, -hz, -hx, -hy, hz,
                u, v + ud, u + ud, v + ud + uh
            ),
            floatArrayOf(
                hx, hy, hz, hx, hy, -hz, hx, -hy, hz, hx, -hy, -hz,
                u + uw + ud, v + ud, u + uw + ud * 2, v + ud + uh
            ),
            floatArrayOf(
                -hx, hy, -hz, hx, hy, -hz, -hx, hy, hz, hx, hy, hz,
                u + ud, v, u + uw + ud, v + ud
            ),
            floatArrayOf(
                -hx, -hy, -hz, hx, -hy, -hz, -hx, -hy, hz, hx, -hy, hz,
                u + uw + ud, v, u + uw * 2 + ud, v + ud
            )
        )
        var vi = 0
        var ti = 0
        for (face in faces) {
            val u1 = face[12] / tw
            val v1 = face[13] / th
            val u2 = face[14] / tw
            val v2 = face[15] / th
            // 两个三角形：(左上, 左下, 右下) 与 (左上, 右下, 右上)
            for (i in CORNER_INDICES) {
                positions[vi++] = face[i * 3]
                positions[vi++] = face[i * 3 + 1]
                positions[vi++] = face[i * 3 + 2]
                uvs[ti++] = if (i == 0 || i == 2) u1 else u2
                uvs[ti++] = if (i < 2) v1 else v2
            }
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
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            positions.size * 4,
            toFloatBuffer(positions),
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboUvs)
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            uvs.size * 4,
            toFloatBuffer(uvs),
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    /** 绑定 VBO 并设置顶点属性后绘制（调用方已启用顶点属性数组） */
    fun draw(positionLocation: Int, texCoordLocation: Int) {
        if (vboPositions == 0) {
            uploadToGpu()
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboPositions)
        GLES20.glVertexAttribPointer(positionLocation, 3, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboUvs)
        GLES20.glVertexAttribPointer(texCoordLocation, 2, GLES20.GL_FLOAT, false, 0, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
    }

    /**
     * EGL context 重建后调用：旧 context 的 VBO 已随之销毁，
     * 仅重置 id 标记，下次 draw 时自动重新上传。
     */
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

    companion object {
        // 每面两个三角形的角点索引（角点顺序：0 左上、1 右上、2 左下、3 右下）
        private val CORNER_INDICES = intArrayOf(0, 2, 3, 0, 3, 1)
    }
}
