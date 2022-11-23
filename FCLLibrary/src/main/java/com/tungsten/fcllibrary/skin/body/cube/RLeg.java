package com.tungsten.fcllibrary.skin.body.cube;

import com.tungsten.fcllibrary.skin.body.LimbCube;

public class RLeg extends LimbCube {

    protected float[] rleg_texcoords;
    
    public RLeg(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, -2.0f * scale, -10.0f * scale, 0.0f * scale, -1.5f, 1.0f, 0.0f, 0.0f, 30.0f, -30.0f, 0.5f, 30.0f, -30.0f, true, 1.0f);
        this.rleg_texcoords = new float[] {
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
        this.AddTextures(this.rleg_texcoords);
    }
}
