package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class RightLegOverlay extends LimbCube {
    protected float[] rightLegOverlayTexCoordinates;
    
    public RightLegOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, -2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                -1.5f, 30.0f, -30.0f,
                0.5f, 30.0f, -30.0f);
        this.rightLegOverlayTexCoordinates = new float[] {
                0.0625f, 0.75f, 0.0625f, 0.65625f, 0.125f, 0.65625f, 0.125f, 0.75f,
                0.0625f, 0.65625f, 0.0625f, 0.5625f, 0.125f, 0.5625f, 0.125f, 0.65625f,
                0.0625f, 0.5625f, 0.0625f, 0.5f, 0.125f, 0.5f, 0.125f, 0.5625f,
                0.125f, 0.5625f, 0.125f, 0.5f, 0.1875f, 0.5f, 0.1875f, 0.5625f,
                0.125f, 0.75f, 0.125f, 0.65625f, 0.1875f, 0.65625f, 0.1875f, 0.75f,
                0.125f, 0.65625f, 0.125f, 0.5625f, 0.1875f, 0.5625f, 0.1875f, 0.65625f,
                0.0f, 0.75f, 0.0f, 0.65625f, 0.0625f, 0.65625f, 0.0625f, 0.75f,
                0.0f, 0.65625f, 0.0f, 0.5625f, 0.0625f, 0.5625f, 0.0625f, 0.65625f,
                0.1875f, 0.75f, 0.1875f, 0.65625f, 0.25f, 0.65625f, 0.25f, 0.75f,
                0.1875f, 0.65625f, 0.1875f, 0.5625f, 0.25f, 0.5625f, 0.25f, 0.65625f
        };
        addTextures(this.rightLegOverlayTexCoordinates);
    }
}
