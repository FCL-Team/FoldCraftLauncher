package com.tungsten.fcllibrary.skin.body.cube;

import com.tungsten.fcllibrary.skin.body.LimbCube;

public class RLegOverlay extends LimbCube
{
    protected float[] rleg1_texcoords;
    
    public RLegOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, -2.0f * scale, -10.0f * scale, 0.0f * scale, -4.5f, 1.0f, 0.0f, 0.0f, 30.0f, -30.0f, 1.5f, 30.0f, -30.0f, true, 1.0f);
        this.rleg1_texcoords = new float[] {
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
        this.AddTextures(this.rleg1_texcoords);
    }
}
