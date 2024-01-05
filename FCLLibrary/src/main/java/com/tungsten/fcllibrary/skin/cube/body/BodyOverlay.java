package com.tungsten.fcllibrary.skin.cube.body;

import com.tungsten.fcllibrary.skin.cube.MainCube;

public class BodyOverlay extends MainCube {

    protected float[] jacketTexCoordinates;
    
    public BodyOverlay(float scale) {
        super(8.470589f * scale, 12.705883f * scale, 4.2352943f * scale, 0.0f * scale, 2.0f * scale, 0.0f * scale);
        this.jacketTexCoordinates = new float[] {
                0.3125f, 0.75f, 0.3125f, 0.5625f, 0.4375f, 0.5625f, 0.4375f, 0.75f,
                0.3125f, 0.5625f, 0.3125f, 0.5f, 0.4375f, 0.5f, 0.4375f, 0.5625f,
                0.4375f, 0.5625f, 0.4375f, 0.5f, 0.5625f, 0.5f, 0.5625f, 0.5625f,
                0.4375f, 0.75f, 0.4375f, 0.5625f, 0.5f, 0.5625f, 0.5f, 0.75f,
                0.25f, 0.75f, 0.25f, 0.5625f, 0.3125f, 0.5625f, 0.3125f, 0.75f,
                0.5f, 0.75f, 0.5f, 0.5625f, 0.625f, 0.5625f, 0.625f, 0.75f
        };
        addTextures(this.jacketTexCoordinates);
    }
}
