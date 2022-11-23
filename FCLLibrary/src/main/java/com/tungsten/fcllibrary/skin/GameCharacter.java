package com.tungsten.fcllibrary.skin;

import com.tungsten.fcllibrary.skin.body.BodyPart;
import com.tungsten.fcllibrary.skin.body.cube.Body;
import com.tungsten.fcllibrary.skin.body.cube.BodyOverlay;
import com.tungsten.fcllibrary.skin.body.cube.Hat;
import com.tungsten.fcllibrary.skin.body.cube.Head;
import com.tungsten.fcllibrary.skin.body.cube.LLeg;
import com.tungsten.fcllibrary.skin.body.cube.LLegOverlay;
import com.tungsten.fcllibrary.skin.body.cube.RLeg;
import com.tungsten.fcllibrary.skin.body.cube.RLegOverlay;
import com.tungsten.fcllibrary.skin.body.cube.alex.AlexLArm;
import com.tungsten.fcllibrary.skin.body.cube.alex.AlexLArmOverlay;
import com.tungsten.fcllibrary.skin.body.cube.alex.AlexRArm;
import com.tungsten.fcllibrary.skin.body.cube.alex.AlexRArmOverlay;
import com.tungsten.fcllibrary.skin.body.cube.steve.LArm;
import com.tungsten.fcllibrary.skin.body.cube.steve.LArmOverlay;
import com.tungsten.fcllibrary.skin.body.cube.steve.RArm;
import com.tungsten.fcllibrary.skin.body.cube.steve.RArmOverlay;
import com.tungsten.fcllibrary.skin.cape.CapePart;
import com.tungsten.fcllibrary.skin.cape.cube.Cape;

import javax.microedition.khronos.opengles.GL10;

public class GameCharacter {
    public static int selected_resource;
    private boolean isAlexSkin;
    private boolean isRunning;
    private AlexLArm mAlexLArm;
    private AlexLArmOverlay mAlexLArmOverlay;
    private AlexRArm mAlexRArm;
    private AlexRArmOverlay mAlexRArmOverlay;
    private Body mBody;
    private BodyOverlay mBodyOverlay;
    private boolean mBodyOverlayVisible;
    private boolean mBodyVisible;
    private boolean mCheckAlexOrSteve;
    private Hat mHat;
    private boolean mHatVisible;
    private Head mHead;
    private boolean mHeadVisible;
    private LArm mLArm;
    private LArmOverlay mLArmOverlay;
    private boolean mLArmOverlayVisible;
    private boolean mLArmVisible;
    private LLeg mLLeg;
    private LLegOverlay mLLegOverlay;
    private boolean mLLegOverlayVisible;
    private boolean mLLegVisible;
    private RArm mRArm;
    private RArmOverlay mRArmOverlay;
    private boolean mRArmOverlayVisible;
    private boolean mRArmVisible;
    private RLeg mRLeg;
    private RLegOverlay mRLegOverlay;
    private boolean mRLegOverlayVisible;
    private boolean mRLegVisible;
    private Cape mCape;
    private boolean mCapeVisible;
    private float[] mRotate;
    private float[] rotate_step;

    public float scale = 1f;

    public GameCharacter() {
        this.isAlexSkin = false;
        this.isRunning = false;
        this.mBodyOverlayVisible = true;
        this.mBodyVisible = true;
        this.mHatVisible = true;
        this.mHeadVisible = true;
        this.mLArmOverlayVisible = true;
        this.mLArmVisible = true;
        this.mLLegOverlayVisible = true;
        this.mLLegVisible = true;
        this.mRArmOverlayVisible = true;
        this.mRArmVisible = true;
        this.mRLegOverlayVisible = true;
        this.mRLegVisible = true;
        this.mCapeVisible = true;
        this.mRotate = new float[] { 0.0f, 0.0f, 0.0f };
        this.rotate_step = new float[] { 2.0f, 2.0f, 2.0f };
        this.mHead = new Head(scale);
        this.mHat = new Hat(scale);
        this.mBody = new Body(scale);
        this.mBodyOverlay = new BodyOverlay(scale);
        this.mLArm = new LArm(scale);
        this.mRArm = new RArm(scale);
        this.mLArmOverlay = new LArmOverlay(scale);
        this.mRArmOverlay = new RArmOverlay(scale);
        this.mLLeg = new LLeg(scale);
        this.mRLeg = new RLeg(scale);
        this.mLLegOverlay = new LLegOverlay(scale);
        this.mRLegOverlay = new RLegOverlay(scale);
        this.mCape = new Cape(scale);
    }
    
    public GameCharacter(final int selected_resource) {
        this.isAlexSkin = false;
        this.isRunning = false;
        this.mBodyOverlayVisible = true;
        this.mBodyVisible = true;
        this.mHatVisible = true;
        this.mHeadVisible = true;
        this.mLArmOverlayVisible = true;
        this.mLArmVisible = true;
        this.mLLegOverlayVisible = true;
        this.mLLegVisible = true;
        this.mRArmOverlayVisible = true;
        this.mRArmVisible = true;
        this.mRLegOverlayVisible = true;
        this.mRLegVisible = true;
        this.mCapeVisible = true;
        this.mRotate = new float[] { 0.0f, 0.0f, 0.0f };
        this.rotate_step = new float[] { 2.0f, 2.0f, 2.0f };
        GameCharacter.selected_resource = selected_resource;
        this.mHead = new Head(scale);
        this.mHat = new Hat(scale);
        this.mBody = new Body(scale);
        this.mBodyOverlay = new BodyOverlay(scale);
        this.mLArm = new LArm(scale);
        this.mRArm = new RArm(scale);
        this.mLArmOverlay = new LArmOverlay(scale);
        this.mRArmOverlay = new RArmOverlay(scale);
        this.mLLeg = new LLeg(scale);
        this.mRLeg = new RLeg(scale);
        this.mLLegOverlay = new LLegOverlay(scale);
        this.mRLegOverlay = new RLegOverlay(scale);
        this.mCape = new Cape(scale);
    }
    
    public GameCharacter(final boolean mCheckAlexOrSteve) {
        this.isAlexSkin = false;
        this.isRunning = false;
        this.mBodyOverlayVisible = true;
        this.mBodyVisible = true;
        this.mHatVisible = true;
        this.mHeadVisible = true;
        this.mLArmOverlayVisible = true;
        this.mLArmVisible = true;
        this.mLLegOverlayVisible = true;
        this.mLLegVisible = true;
        this.mRArmOverlayVisible = true;
        this.mRArmVisible = true;
        this.mRLegOverlayVisible = true;
        this.mRLegVisible = true;
        this.mCapeVisible = true;
        this.mRotate = new float[] { 0.0f, 0.0f, 0.0f };
        this.rotate_step = new float[] { 2.0f, 2.0f, 2.0f };
        if (!(this.mCheckAlexOrSteve = mCheckAlexOrSteve)) {
            this.mHead = new Head(scale);
            this.mHat = new Hat(scale);
            this.mBody = new Body(scale);
            this.mBodyOverlay = new BodyOverlay(scale);
            this.mLArm = new LArm(scale);
            this.mRArm = new RArm(scale);
            this.mLArmOverlay = new LArmOverlay(scale);
            this.mRArmOverlay = new RArmOverlay(scale);
            this.mLLeg = new LLeg(scale);
            this.mRLeg = new RLeg(scale);
            this.mLLegOverlay = new LLegOverlay(scale);
            this.mRLegOverlay = new RLegOverlay(scale);
            this.mCape = new Cape(scale);
            return;
        }
        this.mHead = new Head(scale);
        this.mHat = new Hat(scale);
        this.mBody = new Body(scale);
        this.mBodyOverlay = new BodyOverlay(scale);
        this.mAlexLArm = new AlexLArm(scale);
        this.mAlexRArm = new AlexRArm(scale);
        this.mAlexLArmOverlay = new AlexLArmOverlay(scale);
        this.mAlexRArmOverlay = new AlexRArmOverlay(scale);
        this.mLLeg = new LLeg(scale);
        this.mRLeg = new RLeg(scale);
        this.mLLegOverlay = new LLegOverlay(scale);
        this.mRLegOverlay = new RLegOverlay(scale);
        this.mCape = new Cape(scale);
    }
    
    public GameCharacter(final boolean mCheckAlexOrSteve, final int selected_resource) {
        this.isAlexSkin = false;
        this.isRunning = false;
        this.mBodyOverlayVisible = true;
        this.mBodyVisible = true;
        this.mHatVisible = true;
        this.mHeadVisible = true;
        this.mLArmOverlayVisible = true;
        this.mLArmVisible = true;
        this.mLLegOverlayVisible = true;
        this.mLLegVisible = true;
        this.mRArmOverlayVisible = true;
        this.mRArmVisible = true;
        this.mRLegOverlayVisible = true;
        this.mRLegVisible = true;
        this.mCapeVisible = true;
        this.mRotate = new float[] { 0.0f, 0.0f, 0.0f };
        this.rotate_step = new float[] { 2.0f, 2.0f, 2.0f };
        this.mCheckAlexOrSteve = mCheckAlexOrSteve;
        GameCharacter.selected_resource = selected_resource;
        if (!mCheckAlexOrSteve) {
            this.mHead = new Head(scale);
            this.mHat = new Hat(scale);
            this.mBody = new Body(scale);
            this.mBodyOverlay = new BodyOverlay(scale);
            this.mLArm = new LArm(scale);
            this.mRArm = new RArm(scale);
            this.mLArmOverlay = new LArmOverlay(scale);
            this.mRArmOverlay = new RArmOverlay(scale);
            this.mLLeg = new LLeg(scale);
            this.mRLeg = new RLeg(scale);
            this.mLLegOverlay = new LLegOverlay(scale);
            this.mRLegOverlay = new RLegOverlay(scale);
            this.mCape = new Cape(scale);
            return;
        }
        this.mHead = new Head(scale);
        this.mHat = new Hat(scale);
        this.mBody = new Body(scale);
        this.mBodyOverlay = new BodyOverlay(scale);
        this.mAlexLArm = new AlexLArm(scale);
        this.mAlexRArm = new AlexRArm(scale);
        this.mAlexLArmOverlay = new AlexLArmOverlay(scale);
        this.mAlexRArmOverlay = new AlexRArmOverlay(scale);
        this.mLLeg = new LLeg(scale);
        this.mRLeg = new RLeg(scale);
        this.mLLegOverlay = new LLegOverlay(scale);
        this.mRLegOverlay = new RLegOverlay(scale);
        this.mCape = new Cape(scale);
    }
    
    public void ResetRunForBody() {
        if (!this.mCheckAlexOrSteve) {
            this.mHead = new Head(scale);
            this.mHat = new Hat(scale);
            this.mBody = new Body(scale);
            this.mBodyOverlay = new BodyOverlay(scale);
            this.mLArm = new LArm(scale);
            this.mRArm = new RArm(scale);
            this.mLArmOverlay = new LArmOverlay(scale);
            this.mRArmOverlay = new RArmOverlay(scale);
            this.mLLeg = new LLeg(scale);
            this.mRLeg = new RLeg(scale);
            this.mLLegOverlay = new LLegOverlay(scale);
            this.mRLegOverlay = new RLegOverlay(scale);
            return;
        }
        this.mHead = new Head(scale);
        this.mHat = new Hat(scale);
        this.mBody = new Body(scale);
        this.mBodyOverlay = new BodyOverlay(scale);
        this.mAlexLArm = new AlexLArm(scale);
        this.mAlexRArm = new AlexRArm(scale);
        this.mAlexLArmOverlay = new AlexLArmOverlay(scale);
        this.mAlexRArmOverlay = new AlexRArmOverlay(scale);
        this.mLLeg = new LLeg(scale);
        this.mRLeg = new RLeg(scale);
        this.mLLegOverlay = new LLegOverlay(scale);
        this.mRLegOverlay = new RLegOverlay(scale);
    }

    public void setScale(float scale) {
        this.scale = scale;
        if (!mCheckAlexOrSteve) {
            this.mHead = new Head(scale);
            this.mHat = new Hat(scale);
            this.mBody = new Body(scale);
            this.mBodyOverlay = new BodyOverlay(scale);
            this.mLArm = new LArm(scale);
            this.mRArm = new RArm(scale);
            this.mLArmOverlay = new LArmOverlay(scale);
            this.mRArmOverlay = new RArmOverlay(scale);
            this.mLLeg = new LLeg(scale);
            this.mRLeg = new RLeg(scale);
            this.mLLegOverlay = new LLegOverlay(scale);
            this.mRLegOverlay = new RLegOverlay(scale);
            this.mCape = new Cape(scale);
            return;
        }
        this.mHead = new Head(scale);
        this.mHat = new Hat(scale);
        this.mBody = new Body(scale);
        this.mBodyOverlay = new BodyOverlay(scale);
        this.mAlexLArm = new AlexLArm(scale);
        this.mAlexRArm = new AlexRArm(scale);
        this.mAlexLArmOverlay = new AlexLArmOverlay(scale);
        this.mAlexRArmOverlay = new AlexRArmOverlay(scale);
        this.mLLeg = new LLeg(scale);
        this.mRLeg = new RLeg(scale);
        this.mLLegOverlay = new LLegOverlay(scale);
        this.mRLegOverlay = new RLegOverlay(scale);
        this.mCape = new Cape(scale);
    }
    
    public void setRotate(final float n, final float n2, final float n3) {
        this.mRotate[0] = n;
        this.mRotate[1] = n2;
        this.mRotate[2] = n3;
    }
    
    public void setRotateStep(final float n, final float n2) {
        if (Math.abs(n) >= 1.0f) {
            final float[] mRotate = this.mRotate;
            mRotate[1] += this.rotate_step[1] * n;
        }
        if (Math.abs(n2) >= 1.0f) {
            final float[] mRotate2 = this.mRotate;
            mRotate2[0] += this.rotate_step[0] * n2;
        }
    }
    
    public void setRunning(final boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    public void drawBody(final GL10 gl10) {
        gl10.glMatrixMode(5888);
        gl10.glRotatef(this.mRotate[0], 1.0f, 0.0f, 0.0f);
        gl10.glRotatef(this.mRotate[1], 0.0f, 1.0f, 0.0f);
        gl10.glRotatef(this.mRotate[2], 0.0f, 0.0f, 1.0f);
        if (this.mBodyVisible) {
            this.mBody.draw(gl10, this.isRunning);
        }
        if (this.mHeadVisible) {
            this.mHead.draw(gl10, this.isRunning);
        }
        if (this.mLLegVisible) {
            this.mLLeg.draw(gl10, this.isRunning);
        }
        if (this.mRLegVisible) {
            this.mRLeg.draw(gl10, this.isRunning);
        }
        if (this.mRArm != null) {
            if (this.mLArmVisible) {
                this.mLArm.draw(gl10, this.isRunning);
            }
            if (this.mRArmVisible) {
                this.mRArm.draw(gl10, this.isRunning);
            }
            if (this.mHatVisible) {
                this.mHat.draw(gl10, this.isRunning);
            }
            if (this.mLArmOverlayVisible) {
                this.mLArmOverlay.draw(gl10, this.isRunning);
            }
            if (this.mRArmOverlayVisible) {
                this.mRArmOverlay.draw(gl10, this.isRunning);
            }
        }
        else {
            if (this.mLArmVisible) {
                this.mAlexLArm.draw(gl10, this.isRunning);
            }
            if (this.mRArmVisible) {
                this.mAlexRArm.draw(gl10, this.isRunning);
            }
            if (this.mHatVisible) {
                this.mHat.draw(gl10, this.isRunning);
            }
            if (this.mLArmOverlayVisible) {
                this.mAlexLArmOverlay.draw(gl10, this.isRunning);
            }
            if (this.mRArmOverlayVisible) {
                this.mAlexRArmOverlay.draw(gl10, this.isRunning);
            }
        }
        if (this.mLLegOverlayVisible) {
            this.mLLegOverlay.draw(gl10, this.isRunning);
        }
        if (this.mRLegOverlayVisible) {
            this.mRLegOverlay.draw(gl10, this.isRunning);
        }
        if (this.mBodyOverlayVisible) {
            this.mBodyOverlay.draw(gl10, this.isRunning);
        }
    }

    public void drawCape(final GL10 gl10) {
        if (this.mCapeVisible) {
            this.mCape.draw(gl10, this.isRunning);
        }
    }
    
    public Boolean getRunning() {
        return this.isRunning;
    }
    
    public float getXRotation() {
        return this.mRotate[0];
    }
    
    public float getYRotation() {
        return this.mRotate[1];
    }
    
    public float getZRotation() {
        return this.mRotate[2];
    }
    
    public void hideBodyPart(final BodyPart bodyPart, final boolean b) {
        if (bodyPart == BodyPart.BODY) {
            this.mBodyVisible = b;
        }
        if (bodyPart == BodyPart.HAT) {
            this.mHatVisible = b;
        }
        if (bodyPart == BodyPart.HEAD) {
            this.mHeadVisible = b;
        }
        if (bodyPart == BodyPart.JACKET) {
            this.mBodyOverlayVisible = b;
        }
        if (bodyPart == BodyPart.LEFT_ARM) {
            this.mLArmVisible = b;
        }
        if (bodyPart == BodyPart.RIGHT_ARM) {
            this.mRArmVisible = b;
        }
        if (bodyPart == BodyPart.LEFT_LEG) {
            this.mLLegVisible = b;
        }
        if (bodyPart == BodyPart.RIGHT_LEG) {
            this.mRLegVisible = b;
        }
        if (bodyPart == BodyPart.LEFT_SLEEVE) {
            this.mLArmOverlayVisible = b;
        }
        if (bodyPart == BodyPart.RIGHT_SLEEVE) {
            this.mRArmOverlayVisible = b;
        }
        if (bodyPart == BodyPart.LEFT_LEG_OVERLAY) {
            this.mLLegOverlayVisible = b;
        }
        if (bodyPart == BodyPart.RIGHT_LEG_OVERLAY) {
            this.mRLegOverlayVisible = b;
        }
    }

    public void hideCapePart(final CapePart capePart, final boolean b) {
        if (capePart == CapePart.CAPE) {
            this.mCapeVisible = b;
        }
    }
    
    public void setXRotation(final int n) {
        this.mRotate[0] = n;
    }
    
    public void setYRotation(final int n) {
        this.mRotate[1] = n;
    }
    
    public void setZRotation(final int n) {
        this.mRotate[2] = n;
    }
    
    public void showAllBodyParts() {
        this.mHeadVisible = true;
        this.mHatVisible = true;
        this.mBodyVisible = true;
        this.mBodyOverlayVisible = true;
        this.mLArmVisible = true;
        this.mRArmVisible = true;
        this.mLArmOverlayVisible = true;
        this.mRArmOverlayVisible = true;
        this.mLLegVisible = true;
        this.mRLegVisible = true;
        this.mLLegOverlayVisible = true;
        this.mRLegOverlayVisible = true;
        this.mCapeVisible = true;
    }
}
