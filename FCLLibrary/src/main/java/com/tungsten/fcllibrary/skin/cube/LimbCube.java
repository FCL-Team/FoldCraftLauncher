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

    public LimbCube(float n1, float n2, float n3, float n4, float n5, float n6, float n7, float n8, float n9, float mainStepValue, float mainMaxAngle, float mainMinAngle, float subStepValue, float subMaxAngle, float subMinAngle) {
        this.scale = new float[3];
        this.scale[0] = n1;
        this.scale[1] = n2;
        this.scale[2] = n3;
        this.offset = new float[3];
        this.offset[0] = n4;
        this.offset[1] = n5;
        this.offset[2] = n6;
        this.mainAngleAxis = new float[3];
        this.mainAngleAxis[0] = n7;
        this.mainAngleAxis[1] = n8;
        this.mainAngleAxis[2] = n9;
        this.mainAngle = 0.0f;
        this.mainStepValue = mainStepValue;
        this.mainMaxAngle = mainMaxAngle;
        this.mainMinAngle = mainMinAngle;
        this.subAngleAxis = new float[3];
        this.subAngle = 0.0f;
        this.subStepValue = subStepValue;
        this.subMaxAngle = subMaxAngle;
        this.subMinAngle = subMinAngle;
        this.vertices = new float[] {
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,

                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, -1.0f,
                -1.0f, 0.0f, -1.0f
        };
        this.faceIndecies = new int[] {
                0, 8, 9, 1,
                8, 3, 2, 9,
                3, 7, 6, 2,
                0, 4, 5, 1,
                1, 9, 10, 5,
                9, 2, 6, 10,
                4, 11, 8, 0,
                11, 7, 3, 8,
                5, 10, 11, 4,
                10, 6, 7, 11
        };
        this.faceVertices = new float[this.faceIndecies.length * 3];
        this.normalVertices = new float[] {
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,

                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f
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
            final int   n  = this.faceIndecies[i];
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
            gl10.glTranslatef(0.0f, this.scale[1] / 4.0f * 3.0f, 0.0f);
            gl10.glRotatef(this.mainAngle, this.mainAngleAxis[0], this.mainAngleAxis[1], this.mainAngleAxis[2]);
            gl10.glTranslatef(0.0f, -this.scale[1] / 4.0f * 3.0f, 0.0f);
            this.mainAngle += this.mainStepValue;
            if (this.mainAngle >= this.mainMaxAngle) {
                this.mainStepValue *= -1.0f;
                this.mainAngle = this.mainMaxAngle;
            } else if (this.mainAngle <= this.mainMinAngle) {
                this.mainStepValue *= -1.0f;
                this.mainAngle = this.mainMinAngle;
            }
            this.subAngle += this.subStepValue;
            if (this.subAngle >= this.subMaxAngle) {
                this.subStepValue *= -1.0f;
                this.subAngle = this.subMaxAngle;
            } else if (this.subAngle <= this.subMinAngle) {
                this.subStepValue *= -1.0f;
                this.subAngle = this.subMinAngle;
            }
        }
        for (int i = 0; i < this.textureBuffers.size(); ++i) {
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.textureBuffers.get(i));
            for (int j = 0; j < 10; ++j) {
                gl10.glDrawArrays(6, j * 4, 4);
            }
        }
        gl10.glPopMatrix();
        gl10.glDisable(GL10.GL_BLEND);
        gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
