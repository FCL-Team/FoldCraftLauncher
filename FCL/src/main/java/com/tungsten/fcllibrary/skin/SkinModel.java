package com.tungsten.fcllibrary.skin;

import com.tungsten.fcllibrary.skin.cube.arm.normal.LeftArm;
import com.tungsten.fcllibrary.skin.cube.arm.normal.LeftArmOverlay;
import com.tungsten.fcllibrary.skin.cube.arm.normal.RightArm;
import com.tungsten.fcllibrary.skin.cube.arm.normal.RightArmOverlay;
import com.tungsten.fcllibrary.skin.cube.arm.slim.LeftArmSlim;
import com.tungsten.fcllibrary.skin.cube.arm.slim.LeftArmSlimOverlay;
import com.tungsten.fcllibrary.skin.cube.arm.slim.RightArmSlim;
import com.tungsten.fcllibrary.skin.cube.arm.slim.RightArmSlimOverlay;
import com.tungsten.fcllibrary.skin.cube.body.Body;
import com.tungsten.fcllibrary.skin.cube.body.BodyOverlay;
import com.tungsten.fcllibrary.skin.cube.cape.Cape;
import com.tungsten.fcllibrary.skin.cube.head.Hat;
import com.tungsten.fcllibrary.skin.cube.head.Head;
import com.tungsten.fcllibrary.skin.cube.leg.LeftLeg;
import com.tungsten.fcllibrary.skin.cube.leg.LeftLegOverlay;
import com.tungsten.fcllibrary.skin.cube.leg.RightLeg;
import com.tungsten.fcllibrary.skin.cube.leg.RightLegOverlay;

import javax.microedition.khronos.opengles.GL10;

public class SkinModel {

    private Head head;
    private Hat hat;
    private Body body;
    private BodyOverlay bodyOverlay;
    private LeftArm leftArm;
    private LeftArmOverlay leftArmOverlay;
    private RightArm rightArm;
    private RightArmOverlay rightArmOverlay;
    private LeftArmSlim leftArmSlim;
    private LeftArmSlimOverlay leftArmSlimOverlay;
    private RightArmSlim rightArmSlim;
    private RightArmSlimOverlay rightArmSlimOverlay;
    private LeftLeg leftLeg;
    private LeftLegOverlay leftLegOverlay;
    private RightLeg rightLeg;
    private RightLegOverlay rightLegOverlay;
    private Cape cape;
    private final float[] rotate;
    private final float[] rotateStep;
    private boolean isRunning;
    private float scale;

    public SkinModel() {
        this.rotate = new float[] {
                0.0f,
                0.0f,
                0.0f
        };
        this.rotateStep = new float[] {
                2.0f,
                2.0f,
                2.0f
        };
        this.isRunning = false;
        this.scale = 1f;
        initModel();
    }

    private void initModel() {
        this.head = new Head(scale);
        this.hat = new Hat(scale);
        this.body = new Body(scale);
        this.bodyOverlay = new BodyOverlay(scale);
        this.leftArm = new LeftArm(scale);
        this.rightArm = new RightArm(scale);
        this.leftArmOverlay = new LeftArmOverlay(scale);
        this.rightArmOverlay = new RightArmOverlay(scale);
        this.leftArmSlim = new LeftArmSlim(scale);
        this.rightArmSlim = new RightArmSlim(scale);
        this.leftArmSlimOverlay = new LeftArmSlimOverlay(scale);
        this.rightArmSlimOverlay = new RightArmSlimOverlay(scale);
        this.leftLeg = new LeftLeg(scale);
        this.rightLeg = new RightLeg(scale);
        this.leftLegOverlay = new LeftLegOverlay(scale);
        this.rightLegOverlay = new RightLegOverlay(scale);
        this.cape = new Cape(scale);
    }

    public void reset() {
        initModel();
    }

    public void setScale(float scale) {
        this.scale = scale;
        initModel();
    }

    public float getScale() {
        return scale;
    }

    public void setRotate(float n1, float n2, float n3) {
        this.rotate[0] = n1;
        this.rotate[1] = n2;
        this.rotate[2] = n3;
    }
    
    public void setRotateStep(float n1, float n2) {
        if (Math.abs(n1) >= 1.0f) {
            final float[] rotate = this.rotate;
            rotate[1] += this.rotateStep[1] * n1;
        }
        if (Math.abs(n2) >= 1.0f) {
            final float[] rotate = this.rotate;
            rotate[0] += this.rotateStep[0] * n2;
        }
    }
    
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setXRotation(int n) {
        this.rotate[0] = n;
    }

    public void setYRotation(int n) {
        this.rotate[1] = n;
    }

    public void setZRotation(int n) {
        this.rotate[2] = n;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public float getXRotation() {
        return this.rotate[0];
    }

    public float getYRotation() {
        return this.rotate[1];
    }

    public float getZRotation() {
        return this.rotate[2];
    }
    
    public void drawBodyModel(final GL10 gl10, boolean slim) {
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glRotatef(this.rotate[0], 1.0f, 0.0f, 0.0f);
        gl10.glRotatef(this.rotate[1], 0.0f, 1.0f, 0.0f);
        gl10.glRotatef(this.rotate[2], 0.0f, 0.0f, 1.0f);
        this.head.draw(gl10);
        this.hat.draw(gl10);
        this.body.draw(gl10);
        this.bodyOverlay.draw(gl10);
        this.leftLeg.draw(gl10, this.isRunning);
        this.rightLeg.draw(gl10, this.isRunning);
        this.leftLegOverlay.draw(gl10, this.isRunning);
        this.rightLegOverlay.draw(gl10, this.isRunning);
        if (!slim) {
            this.leftArm.draw(gl10, this.isRunning);
            this.rightArm.draw(gl10, this.isRunning);
            this.leftArmOverlay.draw(gl10, this.isRunning);
            this.rightArmOverlay.draw(gl10, this.isRunning);
        } else {
            this.leftArmSlim.draw(gl10, this.isRunning);
            this.rightArmSlim.draw(gl10, this.isRunning);
            this.leftArmSlimOverlay.draw(gl10, this.isRunning);
            this.rightArmSlimOverlay.draw(gl10, this.isRunning);
        }
    }

    public void drawCapeModel(GL10 gl10) {
        this.cape.draw(gl10);
    }
}
