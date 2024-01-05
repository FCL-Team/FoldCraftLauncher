package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class LeftLeg extends LimbCube {

    protected float[] leftLegTexCoordinates;
    
    public LeftLeg(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, 2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                1.5f, 30.0f, -30.0f,
                -0.5f, 30.0f, -30.0f);
        this.leftLegTexCoordinates = new float[] {
                0.3125f, 1.0f, 0.3125f, 0.90625f, 0.375f, 0.90625f, 0.375f, 1.0f,
                0.3125f, 0.90625f, 0.3125f, 0.8125f, 0.375f, 0.8125f, 0.375f, 0.90625f,
                0.3125f, 0.8125f, 0.3125f, 0.75f, 0.375f, 0.75f, 0.375f, 0.8125f,
                0.375f, 0.8125f, 0.375f, 0.75f, 0.4375f, 0.75f, 0.4375f, 0.8125f,
                0.375f, 1.0f, 0.375f, 0.90625f, 0.4375f, 0.90625f, 0.4375f, 1.0f,
                0.375f, 0.90625f, 0.375f, 0.8125f, 0.4375f, 0.8125f, 0.4375f, 0.90625f,
                0.25f, 1.0f, 0.25f, 0.90625f, 0.3125f, 0.90625f, 0.3125f, 1.0f,
                0.25f, 0.90625f, 0.25f, 0.8125f, 0.3125f, 0.8125f, 0.3125f, 0.90625f,
                0.4375f, 1.0f, 0.4375f, 0.90625f, 0.5f, 0.90625f, 0.5f, 1.0f,
                0.4375f, 0.90625f, 0.4375f, 0.8125f, 0.5f, 0.8125f, 0.5f, 0.90625f
        };
        addTextures(this.leftLegTexCoordinates);
    }
}
