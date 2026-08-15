package com.tungsten.fcllibrary.skin.cube.arm.slim;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 纤细体型右臂外套/覆盖层立方体 - 继承自LimbCube
 * 尺寸：略大于纤细右臂，形成外套效果
 * 位置：与纤细右臂重合，渲染在右臂上方
 */
public class RightArmSlimOverlay extends LimbCube {

    protected float[] rightArmSlimOverlayTexCoordinates;
    
    public RightArmSlimOverlay(float scale) {
        super(3.1764708f * scale, 12.705883f * scale, 4.2352943f * scale, -5.5f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                0.5f, 10.0f, -10.0f, // 主角度参数
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴
                -0.333f, 20.0f, -20.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        this.rightArmSlimOverlayTexCoordinates = new float[] {
                0.6875f, 0.75f, 0.6875f, 0.65625f, 0.734375f, 0.65625f, 0.734375f, 0.75f,
                0.6875f, 0.65625f, 0.6875f, 0.5625f, 0.734375f, 0.5625f, 0.734375f, 0.65625f,
                0.6875f, 0.5625f, 0.6875f, 0.5f, 0.734375f, 0.5f, 0.734375f, 0.5625f,
                0.734375f, 0.5625f, 0.734375f, 0.5f, 0.78125f, 0.5f, 0.78125f, 0.5625f,
                0.734375f, 0.75f, 0.734375f, 0.65625f, 0.796875f, 0.65625f, 0.796875f, 0.75f,
                0.734375f, 0.65625f, 0.734375f, 0.5625f, 0.796875f, 0.5625f, 0.796875f, 0.65625f,
                0.625f, 0.75f, 0.625f, 0.65625f, 0.6875f, 0.65625f, 0.6875f, 0.75f,
                0.625f, 0.65625f, 0.625f, 0.5625f, 0.6875f, 0.5625f, 0.6875f, 0.65625f,
                0.796875f, 1.0f, 0.796875f, 0.90625f, 0.84375f, 0.90625f, 0.84375f, 1.0f,
                0.796875f, 0.90625f, 0.796875f, 0.8125f, 0.84375f, 0.8125f, 0.84375f, 0.90625f
        };
        addTextures(this.rightArmSlimOverlayTexCoordinates);
    }
}
