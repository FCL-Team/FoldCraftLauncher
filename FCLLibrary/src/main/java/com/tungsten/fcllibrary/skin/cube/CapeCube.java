package com.tungsten.fcllibrary.skin.cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * 披风立方体类 - 用于渲染玩家披风
 * 披风是一个扁平的矩形面片，从玩家背部垂下
 */
public class CapeCube {

    protected float[] scale;           // 缩放参数 [x, y, z]
    protected float[] offset;          // 位置偏移 [x, y, z]
    protected float[] faceVertices;    // 顶点坐标数组
    protected float[] normalVertices;  // 法向量数组
    protected FloatBuffer vertexBuffer;           // 顶点缓冲
    protected FloatBuffer normalVertexBuffer;     // 法向量缓冲
    protected ArrayList<FloatBuffer> textureBuffers; // 纹理缓冲列表

    /**
     * 构造披风立方体
     * 
     * @param sizeX 披风宽度（X轴尺寸）
     * @param sizeY 披风高度（Y轴尺寸，垂直方向）
     * @param sizeZ 披风厚度（Z轴尺寸，通常很小）
     * @param offsetX X轴偏移
     * @param offsetY Y轴偏移
     * @param offsetZ Z轴偏移
     */
    public CapeCube(float sizeX, float sizeY, float sizeZ, float offsetX, float offsetY, float offsetZ) {
        this.scale = new float[3];
        this.scale[0] = sizeX;
        this.scale[1] = sizeY;
        this.scale[2] = sizeZ;
        this.offset = new float[3];
        this.offset[0] = offsetX;
        this.offset[1] = offsetY;
        this.offset[2] = offsetZ;
        
        // GL顶点坐标数组（右手坐标系）
        // 每个面4个顶点，每个顶点由x, y, z三个分量组成
        // 面顺序：前(Face 0)、上(Face 1)、下(Face 2)、右(Face 3)、左(Face 4)、后(Face 5)
        this.faceVertices = new float[] {
                // Face 0: 前面 (Z=+1)
                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,

                // Face 1: 上面 (Y=+1)
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,

                // Face 2: 下面 (Y=-1)
                1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,

                // Face 3: 右面 (X=+1)
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                // Face 4: 左面 (X=-1)
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                // Face 5: 后面 (Z=-1)
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f
        };
        
        // 法向量数组 - 每个面4个顶点共享同一个法向量方向
        // 法向量指向面的外侧，用于光照计算
        this.normalVertices = new float[] {
                // Face 0: 前面 - 法向量指向+Z
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // Face 1: 上面 - 法向量指向+Y
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // Face 2: 下面 - 法向量指向-Y
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,

                // Face 3: 右面 - 法向量指向+X
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // Face 4: 左面 - 法向量指向-X
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                // Face 5: 后面 - 法向量指向-Z
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f
        };
        
        // 根据缩放参数调整顶点坐标
        for (int i = 0; i < 24; ++i) {
            this.faceVertices[i * 3    ] = this.faceVertices[i * 3    ] * this.scale[0] / 2.0f;
            this.faceVertices[i * 3 + 1] = this.faceVertices[i * 3 + 1] * this.scale[1] / 2.0f;
            this.faceVertices[i * 3 + 2] = this.faceVertices[i * 3 + 2] * this.scale[2] / 2.0f;
        }
        
        // 创建顶点缓冲
        final ByteBuffer allocateDirectFace = ByteBuffer.allocateDirect(this.faceVertices.length * 4);
        allocateDirectFace.order(ByteOrder.nativeOrder());
        this.vertexBuffer = allocateDirectFace.asFloatBuffer();
        this.vertexBuffer.put(this.faceVertices);
        this.vertexBuffer.position(0);
        
        // 创建法向量缓冲
        final ByteBuffer allocateDirectNormal = ByteBuffer.allocateDirect(this.normalVertices.length * 4);
        allocateDirectNormal.order(ByteOrder.nativeOrder());
        this.normalVertexBuffer = allocateDirectNormal.asFloatBuffer();
        this.normalVertexBuffer.put(this.normalVertices);
        this.normalVertexBuffer.position(0);
        
        this.textureBuffers = new ArrayList<>();
    }

    /**
     * 添加纹理坐标缓冲
     * @param texture 纹理坐标数组
     * @return 创建的纹理缓冲
     */
    public FloatBuffer addTextures(final float[] texture) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(texture.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        final FloatBuffer floatBuffer = allocateDirect.asFloatBuffer();
        floatBuffer.put(texture);
        floatBuffer.position(0);
        this.textureBuffers.add(floatBuffer);
        return floatBuffer;
    }

    /**
     * 清空所有纹理缓冲
     */
    public void clearAllTextures() {
        this.textureBuffers.clear();
    }

    /**
     * 绘制披风
     * @param gl10 OpenGL ES 1.0上下文
     */
    public void draw(final GL10 gl10) {
        // 启用混合模式（用于半透明效果）
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        // 启用顶点数组、法向量数组和纹理坐标数组
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // 设置顶点指针和法向量指针
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.vertexBuffer);
        gl10.glNormalPointer(GL10.GL_FLOAT, 0, this.normalVertexBuffer);
        
        // 保存当前矩阵并应用变换
        gl10.glPushMatrix();
        gl10.glTranslatef(this.offset[0], this.offset[1], this.offset[2]);
        
        // 披风倾斜效果：绕X轴旋转12度
        gl10.glTranslatef(0.0f, this.scale[1] / 4.0f * 3.0f, 0.0f);
        gl10.glRotatef(12f, 1f, 0f, 0f);
        gl10.glTranslatef(0.0f, -this.scale[1] / 4.0f * 3.0f, 0.0f);
        
        // 绘制所有纹理层
        for (int i = 0; i < this.textureBuffers.size(); ++i) {
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.textureBuffers.get(i));
            for (int j = 0; j < 6; ++j) {
                gl10.glDrawArrays(GL10.GL_TRIANGLE_FAN, j * 4, 4);
            }
        }
        
        // 恢复矩阵并禁用相关状态
        gl10.glPopMatrix();
        gl10.glDisable(GL10.GL_BLEND);
        gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
