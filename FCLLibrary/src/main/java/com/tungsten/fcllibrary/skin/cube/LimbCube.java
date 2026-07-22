package com.tungsten.fcllibrary.skin.cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class LimbCube {

    protected float[] scale;
    protected float[] offset;
    protected int[] faceIndecies;
    protected float[] faceVertices;
    protected float[] normalVertices;
    protected float[] vertices;
    protected float[] mainAngleAxis;
    protected float mainAngle;
    protected float mainMaxAngle;
    protected float mainMinAngle;
    protected float mainStepValue;
    protected float[] subAngleAxis;
    protected float subAngle;
    protected float subMaxAngle;
    protected float subMinAngle;
    protected float subStepValue;
    protected FloatBuffer vertexBuffer;
    protected FloatBuffer normalVertexBuffer;
    protected ArrayList<FloatBuffer> textureBuffers;

    /**
     * 可旋转肢体立方体 - 用于手臂、腿部等需要动画的肢体部位
     *
     * @param scaleX        X轴缩放尺寸
     * @param scaleY        Y轴缩放尺寸（肢体长度方向）
     * @param scaleZ        Z轴缩放尺寸
     * @param offsetX       场景中的X轴偏移
     * @param offsetY       场景中的Y轴偏移
     * @param offsetZ       场景中的Z轴偏移
     * @param mainAxisX     主旋转轴X分量
     * @param mainAxisY     主旋转轴Y分量
     * @param mainAxisZ     主旋转轴Z分量
     * @param mainStepValue 主角度步进值（每帧旋转增量）
     * @param mainMaxAngle  主角度最大值
     * @param mainMinAngle  主角度最小值
     * @param subAxisX      副旋转轴X分量
     * @param subAxisY      副旋转轴Y分量
     * @param subAxisZ      副旋转轴Z分量
     * @param subStepValue  副角度步进值
     * @param subMaxAngle   副角度最大值
     * @param subMinAngle   副角度最小值
     */
    public LimbCube(float scaleX, float scaleY, float scaleZ, float offsetX, float offsetY, float offsetZ,
                    float mainAxisX, float mainAxisY, float mainAxisZ,
                    float mainStepValue, float mainMaxAngle, float mainMinAngle,
                    float subAxisX, float subAxisY, float subAxisZ,
                    float subStepValue, float subMaxAngle, float subMinAngle) {
        this.scale = new float[3];
        this.scale[0] = scaleX;
        this.scale[1] = scaleY;
        this.scale[2] = scaleZ;
        this.offset = new float[3];
        this.offset[0] = offsetX;
        this.offset[1] = offsetY;
        this.offset[2] = offsetZ;

        // 主旋转轴 - 用于肢体摆动（如手臂前后摆动）
        this.mainAngleAxis = new float[3];
        this.mainAngleAxis[0] = mainAxisX;
        this.mainAngleAxis[1] = mainAxisY;
        this.mainAngleAxis[2] = mainAxisZ;
        this.mainAngle = 0.0f;
        this.mainStepValue = mainStepValue;
        this.mainMaxAngle = mainMaxAngle;
        this.mainMinAngle = mainMinAngle;

        // 副旋转轴 - 用于次要摆动（如手臂内外摆动）
        this.subAngleAxis = new float[3];
        this.subAngleAxis[0] = subAxisX;
        this.subAngleAxis[1] = subAxisY;
        this.subAngleAxis[2] = subAxisZ;
        this.subAngle = 0.0f;
        this.subStepValue = subStepValue;
        this.subMaxAngle = subMaxAngle;
        this.subMinAngle = subMinAngle;

        // 基础顶点坐标（归一化）- 12个顶点
        // 前4个：前面4顶点 (Z=+1)
        // 中间4个：后面4顶点 (Z=-1)
        // 后4个：中间层4顶点 (Y=0) - 用于连接身体的关节点
        this.vertices = new float[]{
                -1.0f, -1.0f, 1.0f,  // 顶点0: 前下左
                1.0f, -1.0f, 1.0f,   // 顶点1: 前下右
                1.0f, 1.0f, 1.0f,    // 顶点2: 前上右
                -1.0f, 1.0f, 1.0f,   // 顶点3: 前上左

                -1.0f, -1.0f, -1.0f, // 顶点4: 后下左
                1.0f, -1.0f, -1.0f,  // 顶点5: 后下右
                1.0f, 1.0f, -1.0f,   // 顶点6: 后上右
                -1.0f, 1.0f, -1.0f,  // 顶点7: 后上左

                -1.0f, 0.0f, 1.0f,   // 顶点8: 中前左（关节点）
                1.0f, 0.0f, 1.0f,    // 顶点9: 中前右（关节点）
                1.0f, 0.0f, -1.0f,   // 顶点10: 中后右（关节点）
                -1.0f, 0.0f, -1.0f   // 顶点11: 中后左（关节点）
        };

        // 面索引数组 - 通过索引引用vertices数组构建10个面
        // 每个面4个顶点索引，按顺时针顺序排列
        this.faceIndecies = new int[]{
                0, 8, 9, 1,   // 面0: 下前侧
                8, 3, 2, 9,   // 面1: 上前侧
                3, 7, 6, 2,   // 面2: 上后侧
                0, 4, 5, 1,   // 面3: 下后侧
                1, 9, 10, 5,  // 面4: 右下侧
                9, 2, 6, 10,  // 面5: 右上侧
                4, 11, 8, 0,  // 面6: 左下侧
                11, 7, 3, 8,  // 面7: 左上侧
                5, 10, 11, 4, // 面8: 后端面（连接身体）
                10, 6, 7, 11  // 面9: 后端面上部
        };

        this.faceVertices = new float[this.faceIndecies.length * 3];

        // 法向量数组 - 每个面4个法向量，共10个面
        // 面0-1: 前面(Z=+1) - 法向量指向+Z
        // 面2-3: 上面(Y=+1) - 法向量指向+Y
        // 面4-5: 右面(X=+1) - 法向量指向+X
        // 面6-7: 左面(X=-1) - 法向量指向-X
        // 面8-9: 后面(Z=-1) - 法向量指向-Z（连接身体的端面）
        this.normalVertices = new float[]{
                // 面0: 下前侧 (Z=+1)
                0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                // 面1: 上前侧 (Z=+1)
                0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,

                // 面2: 上后侧 (Y=+1)
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                // 面3: 下后侧 (Y=-1)
                0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,

                // 面4: 右下侧 (X=+1)
                1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                // 面5: 右上侧 (X=+1)
                1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

                // 面6: 左下侧 (X=-1)
                -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                // 面7: 左上侧 (X=-1)
                -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

                // 面8: 后端面下部 (Z=-1) - 连接身体
                0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,
                // 面9: 后端面上部 (Z=-1) - 连接身体
                0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f
        };
        final ByteBuffer allocateDirectFace = ByteBuffer.allocateDirect(this.faceVertices.length * 4);
        allocateDirectFace.order(ByteOrder.nativeOrder());
        this.vertexBuffer = allocateDirectFace.asFloatBuffer();
        this.vertexBuffer.put(this.faceVertices);
        this.vertexBuffer.position(0);
        final ByteBuffer allocateDirectNormal = ByteBuffer.allocateDirect(this.normalVertices.length * 4);
        allocateDirectNormal.order(ByteOrder.nativeOrder());
        this.normalVertexBuffer = allocateDirectNormal.asFloatBuffer();
        this.normalVertexBuffer.put(this.normalVertices);
        this.normalVertexBuffer.position(0);
        this.textureBuffers = new ArrayList<>();
    }

    public FloatBuffer addTextures(final float[] texture) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(texture.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        final FloatBuffer floatBuffer = allocateDirect.asFloatBuffer();
        floatBuffer.put(texture);
        floatBuffer.position(0);
        this.textureBuffers.add(floatBuffer);
        return floatBuffer;
    }

    public void clearAllTextures() {
        this.textureBuffers.clear();
    }

    public void draw(final GL10 gl10, final boolean isRunning) {
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glNormalPointer(GL10.GL_FLOAT, 0, this.normalVertexBuffer);
        for (int i = 0; i < this.faceIndecies.length; ++i) {
            final int n = this.faceIndecies[i];
            final float n2 = this.vertices[n * 3 + 1] * this.scale[1] / 2.0f;
            final float n3 = this.vertices[n * 3 + 2] * this.scale[2] / 2.0f;
            this.vertexBuffer.put(i * 3, this.vertices[n * 3] * this.scale[0] / 2.0f);
            this.vertexBuffer.put(i * 3 + 1, n2);
            this.vertexBuffer.put(i * 3 + 2, n3);
        }
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, this.vertexBuffer);
        gl10.glPushMatrix();
        gl10.glTranslatef(this.offset[0], this.offset[1], this.offset[2]);
        if (isRunning) {
            // 应用主旋转（手臂前后摆动）
            gl10.glTranslatef(0.0f, this.scale[1] / 4.0f * 3.0f, 0.0f);
            gl10.glRotatef(this.mainAngle, this.mainAngleAxis[0], this.mainAngleAxis[1], this.mainAngleAxis[2]);
            gl10.glTranslatef(0.0f, -this.scale[1] / 4.0f * 3.0f, 0.0f);

            // 更新主角度
            this.mainAngle += this.mainStepValue;
            if (this.mainAngle >= this.mainMaxAngle) {
                this.mainStepValue *= -1.0f;
                this.mainAngle = this.mainMaxAngle;
            } else if (this.mainAngle <= this.mainMinAngle) {
                this.mainStepValue *= -1.0f;
                this.mainAngle = this.mainMinAngle;
            }
        }
        for (int i = 0; i < this.textureBuffers.size(); ++i) {
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.textureBuffers.get(i));
            for (int j = 0; j < 10; ++j) {
                gl10.glDrawArrays(GL10.GL_TRIANGLE_FAN, j * 4, 4);
            }
        }
        gl10.glPopMatrix();
        gl10.glDisable(GL10.GL_BLEND);
        gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
