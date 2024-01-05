package com.tungsten.fcllibrary.skin.cube.arm.normal;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class LeftArm extends LimbCube {

    protected float[] leftArmTexCoordinates;
    
    public LeftArm(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, 6.0f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                -0.5f, 10.0f, -10.0f,
                0.333f, 20.0f, -20.0f);
        this.leftArmTexCoordinates = new float[] {
                0.5625f, 1.0f, 0.5625f, 0.90625f, 0.625f, 0.90625f, 0.625f, 1.0f,
                0.5625f, 0.90625f, 0.5625f, 0.8125f, 0.625f, 0.8125f, 0.625f, 0.90625f,
                0.5625f, 0.8125f, 0.5625f, 0.75f, 0.625f, 0.75f, 0.625f, 0.8125f,
                0.625f, 0.8125f, 0.625f, 0.75f, 0.6875f, 0.75f, 0.6875f, 0.8125f,
                0.625f, 1.0f, 0.625f, 0.90625f, 0.6875f, 0.90625f, 0.6875f, 1.0f,
                0.625f, 0.90625f, 0.625f, 0.8125f, 0.6875f, 0.8125f, 0.6875f, 0.90625f,
                0.5f, 1.0f, 0.5f, 0.90625f, 0.5625f, 0.90625f, 0.5625f, 1.0f,
                0.5f, 0.90625f, 0.5f, 0.8125f, 0.5625f, 0.8125f, 0.5625f, 0.90625f,
                0.6875f, 1.0f, 0.6875f, 0.90625f, 0.75f, 0.90625f, 0.75f, 1.0f,
                0.6875f, 0.90625f, 0.6875f, 0.8125f, 0.75f, 0.8125f, 0.75f, 0.90625f
        };
        addTextures(this.leftArmTexCoordinates);
    }
}
