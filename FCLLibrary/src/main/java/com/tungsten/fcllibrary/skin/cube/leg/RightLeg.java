package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

public class RightLeg extends LimbCube {

    protected float[] rightLegTexCoordinates;
    
    public RightLeg(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, -2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,
                -1.5f, 30.0f, -30.0f,
                0.5f, 30.0f, -30.0f);
        this.rightLegTexCoordinates = new float[] {
                0.0625f, 0.5f, 0.0625f, 0.40625f, 0.125f, 0.40625f, 0.125f, 0.5f,
                0.0625f, 0.40625f, 0.0625f, 0.3125f, 0.125f, 0.3125f, 0.125f, 0.40625f,
                0.0625f, 0.3125f, 0.0625f, 0.25f, 0.125f, 0.25f, 0.125f, 0.3125f,
                0.125f, 0.3125f, 0.125f, 0.25f, 0.1875f, 0.25f, 0.1875f, 0.3125f,
                0.125f, 0.5f, 0.125f, 0.40625f, 0.1875f, 0.40625f, 0.1875f, 0.5f,
                0.125f, 0.40625f, 0.125f, 0.3125f, 0.1875f, 0.3125f, 0.1875f, 0.40625f,
                0.0f, 0.5f, 0.0f, 0.40625f, 0.0625f, 0.40625f, 0.0625f, 0.5f,
                0.0f, 0.40625f, 0.0f, 0.3125f, 0.0625f, 0.3125f, 0.0625f, 0.40625f,
                0.1875f, 0.5f, 0.1875f, 0.40625f, 0.25f, 0.40625f, 0.25f, 0.5f,
                0.1875f, 0.40625f, 0.1875f, 0.3125f, 0.25f, 0.3125f, 0.25f, 0.40625f
        };
        addTextures(this.rightLegTexCoordinates);
    }
}
