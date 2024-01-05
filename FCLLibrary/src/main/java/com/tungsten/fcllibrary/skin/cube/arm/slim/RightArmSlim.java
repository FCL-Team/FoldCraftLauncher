package com.tungsten.fcllibrary.skin.cube.arm.slim;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class RightArmSlim extends LimbCube {

    protected float[] rightArmSlimTexCoordinates;
    
    public RightArmSlim(float scale) {
        super(3.0f * scale, 12.0f * scale, 4.0f * scale, -5.5f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                0.5f, 10.0f, -10.0f,
                -0.333f, 20.0f, -20.0f);
        this.rightArmSlimTexCoordinates = new float[] {
                0.6875f, 0.5f, 0.6875f, 0.40625f, 0.734375f, 0.40625f, 0.734375f, 0.5f,
                0.6875f, 0.40625f, 0.6875f, 0.3125f, 0.734375f, 0.3125f, 0.734375f, 0.40625f,
                0.6875f, 0.3125f, 0.6875f, 0.25f, 0.734375f, 0.25f, 0.734375f, 0.3125f,
                0.734375f, 0.3125f, 0.734375f, 0.25f, 0.78125f, 0.25f, 0.78125f, 0.3125f,
                0.734375f, 0.5f, 0.734375f, 0.40625f, 0.796875f, 0.40625f, 0.796875f, 0.5f,
                0.734375f, 0.40625f, 0.734375f, 0.3125f, 0.796875f, 0.3125f, 0.796875f, 0.40625f,
                0.625f, 0.5f, 0.625f, 0.40625f, 0.6875f, 0.40625f, 0.6875f, 0.5f,
                0.625f, 0.40625f, 0.625f, 0.3125f, 0.6875f, 0.3125f, 0.6875f, 0.40625f,
                0.796875f, 0.5f, 0.796875f, 0.40625f, 0.84375f, 0.40625f, 0.84375f, 0.5f,
                0.796875f, 0.40625f, 0.796875f, 0.3125f, 0.84375f, 0.3125f, 0.84375f, 0.40625f
        };
        addTextures(this.rightArmSlimTexCoordinates);
    }
}
