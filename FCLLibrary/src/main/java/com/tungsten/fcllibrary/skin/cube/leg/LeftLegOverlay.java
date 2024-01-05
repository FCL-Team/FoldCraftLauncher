package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class LeftLegOverlay extends LimbCube {

    protected float[] leftLegOverlayTexCoordinates;
    
    public LeftLegOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, 2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                1.5f, 30.0f, -30.0f,
                -0.5f, 30.0f, -30.0f);
        this.leftLegOverlayTexCoordinates = new float[] {
                0.0625f, 1.0f, 0.0625f, 0.90625f, 0.125f, 0.90625f, 0.125f, 1.0f,
                0.0625f, 0.90625f, 0.0625f, 0.8125f, 0.125f, 0.8125f, 0.125f, 0.90625f,
                0.0625f, 0.8125f, 0.0625f, 0.75f, 0.125f, 0.75f, 0.125f, 0.8125f,
                0.125f, 0.8125f, 0.125f, 0.75f, 0.1875f, 0.75f, 0.1875f, 0.8125f,
                0.125f, 1.0f, 0.125f, 0.90625f, 0.1875f, 0.90625f, 0.1875f, 1.0f,
                0.125f, 0.90625f, 0.125f, 0.8125f, 0.1875f, 0.8125f, 0.1875f, 0.90625f,
                0.0f, 1.0f, 0.0f, 0.90625f, 0.0625f, 0.90625f, 0.0625f, 1.0f,
                0.0f, 0.90625f, 0.0f, 0.8125f, 0.0625f, 0.8125f, 0.0625f, 0.90625f,
                0.1875f, 1.0f, 0.1875f, 0.90625f, 0.25f, 0.90625f, 0.25f, 1.0f,
                0.1875f, 0.90625f, 0.1875f, 0.8125f, 0.25f, 0.8125f, 0.25f, 0.90625f
        };
        addTextures(this.leftLegOverlayTexCoordinates);
    }
}
