package com.tungsten.fcllibrary.skin.cube.cape;

import com.tungsten.fcllibrary.skin.cube.CapeCube;

public class Cape extends CapeCube {

    protected float[] capeTexCoordinates;

    public Cape(float scale) {
        super(10.0f * scale, 16.0f * scale, scale, 0.0f * scale, 0.0f * scale, -1.75f * scale);
        this.capeTexCoordinates = new float[] {
                0.1875f, 0.53125f, 0.1875f, 0.03125f, 0.34375f, 0.03125f, 0.34375f, 0.53125f,
                0.015625f, 0.03125f, 0.015625f, 0f, 0.171875f, 0f, 0.171875f, 0.03125f,
                0.171875f, 0.03125f, 0.171875f, 0f, 0.328125f, 0f, 0.328125f, 0.03125f,
                0f, 0.53125f, 0f, 0.03125f, 0.015625f, 0.03125f, 0.015625f, 0.53125f,
                0.171875f, 0.53125f, 0.171875f, 0.03125f, 0.1875f, 0.03125f, 0.1875f, 0.53125f,
                0.015625f, 0.53125f, 0.015625f, 0.03125f, 0.171875f, 0.03125f, 0.171875f, 0.53125f
        };
        addTextures(this.capeTexCoordinates);
    }

}
