package com.tungsten.fcllibrary.skin.cube.arm.normal;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 正常体型右臂外套/覆盖层立方体 - 继承自LimbCube
 * 尺寸：略大于右臂（4.24x12.71x4.24），形成外套效果
 * 位置：与右臂重合，渲染在右臂上方
 */
public class RightArmOverlay extends LimbCube {

    protected float[] rightArmOverlayTexCoordinates;
    
    public RightArmOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, -6.0f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                0.5f, 10.0f, -10.0f, // 主角度参数
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴
                -0.333f, 20.0f, -20.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 右臂外套区域：皮肤底部右侧区域（对应UV 0.625-0.875水平，0.5-0.75垂直）
        this.rightArmOverlayTexCoordinates = new float[] {
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
        addTextures(this.rightArmOverlayTexCoordinates);
    }
}
