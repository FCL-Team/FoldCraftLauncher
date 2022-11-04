package com.tungsten.fcllibrary.skin.cape.cube;

import com.tungsten.fcllibrary.skin.cape.CapeCube;

public class Cape extends CapeCube {

    protected float[] cape_texcoords;

    public Cape(float scale) {
        super(10.0f * scale, 16.0f * scale, scale, 0.0f * scale, 0.0f * scale, -1.75f * scale, -0.15f * scale, 0.0f, 1.0f, 0.0f, 3.0f, -3.0f);
        this.cape_texcoords = new float[] {
                0.1875f, 0.53125f, 0.1875f, 0.03125f, 0.34375f, 0.03125f, 0.34375f, 0.53125f,
                0.015625f, 0.03125f, 0.015625f, 0f, 0.171875f, 0f, 0.171875f, 0.03125f,
                0.171875f, 0.03125f, 0.171875f, 0f, 0.328125f, 0f, 0.328125f, 0.03125f,
                0f, 0.53125f, 0f, 0.03125f, 0.015625f, 0.03125f, 0.015625f, 0.53125f,
                0.171875f, 0.53125f, 0.171875f, 0.03125f, 0.1875f, 0.03125f, 0.1875f, 0.53125f,
                0.015625f, 0.53125f, 0.015625f, 0.03125f, 0.171875f, 0.03125f, 0.171875f, 0.53125f
        };
        this.AddTextures(this.cape_texcoords);
    }

}
