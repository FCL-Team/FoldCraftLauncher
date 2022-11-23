package com.tungsten.fcllibrary.skin.body.cube.steve;

import com.tungsten.fcllibrary.skin.body.LimbCube;

public class RArmOverlay extends LimbCube {

    protected float[] rarm1_texcoords;
    
    public RArmOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, -6.0f * scale, 2.0f * scale, 0.0f * scale, 0.5f, 1.0f, 0.0f, 0.0f, 10.0f, -10.0f, -0.333f, 20.0f, -20.0f, true, -1.0f);
        this.rarm1_texcoords = new float[] {
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
        this.AddTextures(this.rarm1_texcoords);
    }
}
