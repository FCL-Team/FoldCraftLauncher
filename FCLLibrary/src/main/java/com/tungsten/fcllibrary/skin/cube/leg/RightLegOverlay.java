package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 右腿外套/覆盖层立方体 - 继承自LimbCube
 * 尺寸：略大于右腿（4.24x12.71x4.24），形成靴子效果
 * 位置：与右腿重合，渲染在右腿上方
 */
public class RightLegOverlay extends LimbCube {
    
    protected float[] rightLegOverlayTexCoordinates;
    
    public RightLegOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, -2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                -1.5f, 30.0f, -30.0f, // 主角度参数
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴
                0.5f, 30.0f, -30.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 右腿外套区域：皮肤左侧中间区域（对应UV 0-0.25水平，0.5-0.75垂直）
        this.rightLegOverlayTexCoordinates = new float[] {
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
        addTextures(this.rightLegOverlayTexCoordinates);
    }
}
