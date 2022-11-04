package com.tungsten.fcllibrary.skin.body;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class LimbCube {
    protected int[] face_indecies;
    protected float[] face_vertices;
    protected float fixedDir;
    protected boolean isFixOneAxis;
    protected float mMainAngle;
    protected FloatBuffer mNormalVertexBuffer;
    protected float[] mOffset;
    protected float[] mScale;
    protected float mSubAngle;
    protected ArrayList<FloatBuffer> mTextureBuffers;
    protected FloatBuffer mVertexBuffer;
    protected float[] main_angle_axis;
    protected float main_max_angle;
    protected float main_min_angle;
    protected float main_step_value;
    protected float[] normal_vertices;
    protected float[] sub_angle_axis;
    protected float sub_max_angle;
    protected float sub_min_angle;
    protected float sub_step_value;
    private float[] vertices;
    
    public LimbCube(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        this.mScale = new float[] { 0.0f, 0.0f, 0.0f };
        this.mOffset = new float[] { 0.0f, 0.0f, 0.0f };
        this.mMainAngle = 0.0f;
        this.main_step_value = -3.0f;
        this.main_max_angle = 3.0f;
        this.main_min_angle = -3.0f;
        this.main_angle_axis = new float[] { 0.0f, 0.0f, 0.0f };
        this.mSubAngle = 0.0f;
        this.sub_step_value = -0.15f;
        this.sub_max_angle = 3.0f;
        this.sub_min_angle = -3.0f;
        this.sub_angle_axis = new float[] { 0.0f, 0.0f, 0.0f };
        this.isFixOneAxis = true;
        this.fixedDir = 1.0f;
        this.vertices = new float[] { -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, -1.0f };
        this.face_indecies = new int[] { 0, 8, 9, 1, 8, 3, 2, 9, 3, 7, 6, 2, 1, 5, 4, 0, 1, 9, 10, 5, 9, 2, 6, 10, 4, 11, 8, 0, 11, 7, 3, 8, 5, 10, 11, 4, 10, 6, 7, 11 };
        this.face_vertices = new float[this.face_indecies.length * 3];
        this.normal_vertices = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f };
        this.mTextureBuffers = new ArrayList<FloatBuffer>();
        this.mScale[0] = n;
        this.mScale[1] = n2;
        this.mScale[2] = n3;
        this.mOffset[0] = n4;
        this.mOffset[1] = n5;
        this.mOffset[2] = n6;
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.face_vertices.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        (this.mVertexBuffer = allocateDirect.asFloatBuffer()).put(this.face_vertices);
        this.mVertexBuffer.position(0);
        final ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(this.normal_vertices.length * 4);
        allocateDirect2.order(ByteOrder.nativeOrder());
        (this.mNormalVertexBuffer = allocateDirect2.asFloatBuffer()).put(this.normal_vertices);
        this.mNormalVertexBuffer.position(0);
    }
    
    public LimbCube(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float main_step_value, final float n7, final float n8, final float n9, final float main_max_angle, final float main_min_angle, final float sub_step_value, final float sub_max_angle, final float sub_min_angle, final boolean isFixOneAxis, final float fixedDir) {
        this(n, n2, n3, n4, n5, n6);
        this.main_step_value = main_step_value;
        this.main_angle_axis[0] = n7;
        this.main_angle_axis[1] = n8;
        this.main_angle_axis[2] = n9;
        this.main_max_angle = main_max_angle;
        this.main_min_angle = main_min_angle;
        this.sub_step_value = sub_step_value;
        this.sub_max_angle = sub_max_angle;
        this.sub_min_angle = sub_min_angle;
        this.isFixOneAxis = isFixOneAxis;
        this.fixedDir = fixedDir;
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
        gl10.glNormalPointer(5126, 0, (Buffer)this.mNormalVertexBuffer);
        for (int i = 0; i < this.face_indecies.length; ++i) {
            final int n = this.face_indecies[i];
            final float n2 = this.vertices[n * 3 + 1] * this.mScale[1] / 2.0f;
            final float n3 = this.vertices[n * 3 + 2] * this.mScale[2] / 2.0f;
            float n4;
            float n5;
            n4 = n2;
            n5 = n3;
            this.mVertexBuffer.put(i * 3, this.vertices[n * 3] * this.mScale[0] / 2.0f);
            this.mVertexBuffer.put(i * 3 + 1, n4);
            this.mVertexBuffer.put(i * 3 + 2, n5);
        }
        gl10.glVertexPointer(3, 5126, 0, (Buffer)this.mVertexBuffer);
        gl10.glPushMatrix();
        gl10.glTranslatef(this.mOffset[0], this.mOffset[1], this.mOffset[2]);
        if (b) {
            gl10.glTranslatef(0.0f, this.mScale[1] / 4.0f * 3.0f, 0.0f);
            gl10.glRotatef(this.mMainAngle, this.main_angle_axis[0], this.main_angle_axis[1], this.main_angle_axis[2]);
            gl10.glTranslatef(0.0f, -this.mScale[1] / 4.0f * 3.0f, 0.0f);
            this.mMainAngle += this.main_step_value;
            if (this.mMainAngle >= this.main_max_angle) {
                this.main_step_value *= -1.0f;
                this.mMainAngle = this.main_max_angle;
            }
            else if (this.mMainAngle <= this.main_min_angle) {
                this.main_step_value *= -1.0f;
                this.mMainAngle = this.main_min_angle;
            }
            this.mSubAngle += this.sub_step_value;
            if (this.mSubAngle >= this.sub_max_angle) {
                this.sub_step_value *= -1.0f;
                this.mSubAngle = this.sub_max_angle;
            }
            else if (this.mSubAngle <= this.sub_min_angle) {
                this.sub_step_value *= -1.0f;
                this.mSubAngle = this.sub_min_angle;
            }
        }
        for (int j = 0; j < this.mTextureBuffers.size(); ++j) {
            gl10.glTexCoordPointer(2, 5126, 0, (Buffer)this.mTextureBuffers.get(j));
            for (int k = 0; k < 10; ++k) {
                gl10.glDrawArrays(6, k * 4, 4);
            }
        }
        gl10.glPopMatrix();
        gl10.glDisable(3042);
        gl10.glDisableClientState(32888);
        gl10.glDisableClientState(32884);
    }
    
    public void setZeroRun() {
        this.mMainAngle = 1.5f;
        this.main_step_value = 0.7f;
        this.mSubAngle = 0.5f;
        this.sub_step_value = -0.5f;
    }
}
