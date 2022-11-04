package com.tungsten.fcllibrary.skin.body.cube;

import com.tungsten.fcllibrary.skin.body.MainCube;

public class Head extends MainCube {

    protected float[] head_texcoords;
    
    public Head(float scale) {
        super(8.0f * scale, 8.0f * scale, 8.0f * scale, 0.0f * scale, 12.0f * scale, 0.0f * scale, 0.25f, 0.0f, 1.0f, 0.0f, 5.0f, -5.0f);
        this.AddTextures(this.head_texcoords = new float[] {
                0.125f, 0.25f, 0.125f, 0.125f, 0.25f, 0.125f, 0.25f, 0.25f,
                0.125f, 0.125f, 0.125f, 0.0f, 0.25f, 0.0f, 0.25f, 0.125f,
                0.25f, 0.125f, 0.25f, 0.0f, 0.375f, 0.0f, 0.375f, 0.125f,
                0.25f, 0.25f, 0.25f, 0.125f, 0.375f, 0.125f, 0.375f, 0.25f,
                0.0f, 0.25f, 0.0f, 0.125f, 0.125f, 0.125f, 0.125f, 0.25f,
                0.375f, 0.25f, 0.375f, 0.125f, 0.5f, 0.125f, 0.5f, 0.25f });
    }
}
