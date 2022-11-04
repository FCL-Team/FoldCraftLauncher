package com.tungsten.fcllibrary.skin.body.cube;

import com.tungsten.fcllibrary.skin.body.LimbCube;

public class LLeg extends LimbCube {

    protected float[] lleg_texcoords;
    
    public LLeg(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, 2.0f * scale, -10.0f * scale, 0.0f * scale, 4.5f, 1.0f, 0.0f, 0.0f, 30.0f, -30.0f, -1.5f, 30.0f, -30.0f, true, 1.0f);
        this.lleg_texcoords = new float[] {
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
        this.AddTextures(this.lleg_texcoords);
    }
}
