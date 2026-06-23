package com.tungsten.fcllibrary.skin.cube.arm.normal;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 正常体型左臂外套/覆盖层立方体 - 继承自LimbCube
 * 尺寸：略大于左臂（4.24x12.71x4.24），形成外套效果
 * 位置：与左臂重合，渲染在左臂上方
 */
public class LeftArmOverlay extends LimbCube {

    protected float[] leftArmOverlayTexCoordinates;
    
    public LeftArmOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, 6.0f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                -0.5f, 10.0f, -10.0f, // 主角度参数
                0.0f, 1.0f, 0.0f,     // 副旋转轴：Y轴
                0.333f, 20.0f, -20.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 左臂外套区域：皮肤底部右侧区域（对应UV 0.75-1.0水平，0.75-1.0垂直）
        this.leftArmOverlayTexCoordinates = new float[] {
                0.8125f, 1.0f, 0.8125f, 0.90625f, 0.875f, 0.90625f, 0.875f, 1.0f,
                0.8125f, 0.90625f, 0.8125f, 0.8125f, 0.875f, 0.8125f, 0.875f, 0.90625f,
                0.8125f, 0.8125f, 0.8125f, 0.75f, 0.875f, 0.75f, 0.875f, 0.8125f,
                0.875f, 0.8125f, 0.875f, 0.75f, 0.9375f, 0.75f, 0.9375f, 0.8125f,
                0.875f, 1.0f, 0.875f, 0.90625f, 0.9375f, 0.90625f, 0.9375f, 1.0f,
                0.875f, 0.90625f, 0.875f, 0.8125f, 0.9375f, 0.8125f, 0.9375f, 0.90625f,
                0.75f, 1.0f, 0.75f, 0.90625f, 0.8125f, 0.90625f, 0.8125f, 1.0f,
                0.75f, 0.90625f, 0.75f, 0.8125f, 0.8125f, 0.8125f, 0.8125f, 0.90625f,
                0.9375f, 1.0f, 0.9375f, 0.90625f, 1.0f, 0.90625f, 1.0f, 1.0f,
                0.9375f, 0.90625f, 0.9375f, 0.8125f, 1.0f, 0.8125f, 1.0f, 0.90625f
        };
        addTextures(this.leftArmOverlayTexCoordinates);
    }
}
