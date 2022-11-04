package com.tungsten.fcllibrary.skin.cape;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class CapeCube {
    protected float[] angle_axis;
    protected float[] face_vertices;
    protected float mAngle;
    protected FloatBuffer mNormalVertexBuffer;
    protected float[] mOffset;
    protected float[] mScale;
    protected ArrayList<FloatBuffer> mTextureBuffers;
    protected FloatBuffer mVertexBuffer;
    protected float max_angle;
    protected float min_angle;
    protected float[] normal_vertices;
    protected float step_value;

    public CapeCube(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        this.mScale = new float[] { 0.0f, 0.0f, 0.0f };
        this.mOffset = new float[] { 0.0f, 0.0f, 0.0f };
        this.mAngle = 0.0f;
        this.step_value = -0.15f;
        this.max_angle = 3.0f;
        this.min_angle = -3.0f;
        this.angle_axis = new float[] { 0.0f, 0.0f, 0.0f };
        this.face_vertices = new float[] { -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f };
        this.normal_vertices = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f };
        this.mTextureBuffers = new ArrayList<FloatBuffer>();
        this.mScale[0] = n;
        this.mScale[1] = n2;
        this.mScale[2] = n3;
        this.mOffset[0] = n4;
        this.mOffset[1] = n5;
        this.mOffset[2] = n6;
        for (int i = 0; i < 24; ++i) {
            this.face_vertices[i * 3] = this.face_vertices[i * 3] * this.mScale[0] / 2.0f;
            this.face_vertices[i * 3 + 1] = this.face_vertices[i * 3 + 1] * this.mScale[1] / 2.0f;
            this.face_vertices[i * 3 + 2] = this.face_vertices[i * 3 + 2] * this.mScale[2] / 2.0f;
        }
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.face_vertices.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        (this.mVertexBuffer = allocateDirect.asFloatBuffer()).put(this.face_vertices);
        this.mVertexBuffer.position(0);
        final ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(this.normal_vertices.length * 4);
        allocateDirect2.order(ByteOrder.nativeOrder());
        (this.mNormalVertexBuffer = allocateDirect2.asFloatBuffer()).put(this.normal_vertices);
        this.mNormalVertexBuffer.position(0);
    }

    public CapeCube(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float step_value, final float n7, final float n8, final float n9, final float max_angle, final float min_angle) {
        this(n, n2, n3, n4, n5, n6);
        this.step_value = step_value;
        this.max_angle = max_angle;
        this.min_angle = min_angle;
        this.angle_axis[0] = n7;
        this.angle_axis[1] = n8;
        this.angle_axis[2] = n9;
    }

    public FloatBuffer AddTextures(final float[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        final FloatBuffer floatBuffer = allocateDirect.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        this.mTextureBuffers.add(floatBuffer);
        return floatBuffer;
    }

    public void ClearAllTextures() {
        this.mTextureBuffers.clear();
    }

    public void draw(final GL10 gl10, final boolean b) {
        gl10.glEnable(3042);
        gl10.glBlendFunc(1, 771);
        gl10.glEnableClientState(32884);
        gl10.glEnableClientState(32885);
        gl10.glEnableClientState(32888);
        gl10.glVertexPointer(3, 5126, 0, (Buffer)this.mVertexBuffer);
        gl10.glNormalPointer(5126, 0, (Buffer)this.mNormalVertexBuffer);
        gl10.glPushMatrix();
        gl10.glTranslatef(this.mOffset[0], this.mOffset[1], this.mOffset[2]);
        gl10.glTranslatef(0.0f, this.mScale[1] / 4.0f * 3.0f, 0.0f);
        gl10.glRotatef(12f, 1f, 0f, 0f);
        gl10.glTranslatef(0.0f, -this.mScale[1] / 4.0f * 3.0f, 0.0f);
        for (int i = 0; i < this.mTextureBuffers.size(); ++i) {
            gl10.glTexCoordPointer(2, 5126, 0, (Buffer)this.mTextureBuffers.get(i));
            for (int j = 0; j < 6; ++j) {
                gl10.glDrawArrays(6, j * 4, 4);
            }
        }
        gl10.glPopMatrix();
        gl10.glDisable(3042);
        gl10.glDisableClientState(32888);
        gl10.glDisableClientState(32884);
    }

    public void setZeroRun() {
        this.mAngle = 0.0f;
        this.step_value = -0.15f;
        this.max_angle = 3.0f;
        this.min_angle = -3.0f;
    }
}
