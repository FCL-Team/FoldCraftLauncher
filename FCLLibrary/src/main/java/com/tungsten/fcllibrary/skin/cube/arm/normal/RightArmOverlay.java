package com.tungsten.fcllibrary.skin.cube.arm.normal;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class RightArmOverlay extends LimbCube {

    protected float[] rightArmOverlayTexCoordinates;
    
    public RightArmOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, -6.0f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                0.5f, 10.0f, -10.0f,
                -0.333f, 20.0f, -20.0f);
        this.rightArmOverlayTexCoordinates = new float[] {
                0.6875f, 0.75f, 0.6875f, 0.65625f, 0.75f, 0.65625f, 0.75f, 0.75f,
                0.6875f, 0.65625f, 0.6875f, 0.5625f, 0.75f, 0.5625f, 0.75f, 0.65625f,
                0.6875f, 0.5625f, 0.6875f, 0.5f, 0.75f, 0.5f, 0.75f, 0.5625f,
                0.75f, 0.5625f, 0.75f, 0.5f, 0.8125f, 0.5f, 0.8125f, 0.5625f,
                0.75f, 0.75f, 0.75f, 0.65625f, 0.8125f, 0.65625f, 0.8125f, 0.75f,
                0.75f, 0.65625f, 0.75f, 0.5625f, 0.8125f, 0.5625f, 0.8125f, 0.65625f,
                0.625f, 0.75f, 0.625f, 0.65625f, 0.6875f, 0.65625f, 0.6875f, 0.75f,
                0.625f, 0.65625f, 0.625f, 0.5625f, 0.6875f, 0.5625f, 0.6875f, 0.65625f,
                0.8125f, 0.75f, 0.8125f, 0.65625f, 0.875f, 0.65625f, 0.875f, 0.75f,
                0.8125f, 0.65625f, 0.8125f, 0.5625f, 0.875f, 0.5625f, 0.875f, 0.65625f
        };
        addTextures(this.rightArmOverlayTexCoordinates);
    }
}
