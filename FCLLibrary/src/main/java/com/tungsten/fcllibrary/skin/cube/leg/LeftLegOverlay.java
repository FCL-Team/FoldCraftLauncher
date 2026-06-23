package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 左腿外套/覆盖层立方体 - 继承自LimbCube
 * 尺寸：略大于左腿（4.24x12.71x4.24），形成靴子效果
 * 位置：与左腿重合，渲染在左腿上方
 */
public class LeftLegOverlay extends LimbCube {

    protected float[] leftLegOverlayTexCoordinates;
    
    public LeftLegOverlay(float scale) {
        super(4.2352943f * scale, 12.705883f * scale, 4.2352943f * scale, 2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                1.5f, 30.0f, -30.0f,  // 主角度参数
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴
                -0.5f, 30.0f, -30.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 左腿外套区域：皮肤底部左侧区域（对应UV 0-0.25水平，0.75-1.0垂直）
        this.leftLegOverlayTexCoordinates = new float[] {
                0.0625f, 1.0f, 0.0625f, 0.90625f, 0.125f, 0.90625f, 0.125f, 1.0f,
                0.0625f, 0.90625f, 0.0625f, 0.8125f, 0.125f, 0.8125f, 0.125f, 0.90625f,
                0.0625f, 0.8125f, 0.0625f, 0.75f, 0.125f, 0.75f, 0.125f, 0.8125f,
                0.125f, 0.8125f, 0.125f, 0.75f, 0.1875f, 0.75f, 0.1875f, 0.8125f,
                0.125f, 1.0f, 0.125f, 0.90625f, 0.1875f, 0.90625f, 0.1875f, 1.0f,
                0.125f, 0.90625f, 0.125f, 0.8125f, 0.1875f, 0.8125f, 0.1875f, 0.90625f,
                0.0f, 1.0f, 0.0f, 0.90625f, 0.0625f, 0.90625f, 0.0625f, 1.0f,
                0.0f, 0.90625f, 0.0f, 0.8125f, 0.0625f, 0.8125f, 0.0625f, 0.90625f,
                0.1875f, 1.0f, 0.1875f, 0.90625f, 0.25f, 0.90625f, 0.25f, 1.0f,
                0.1875f, 0.90625f, 0.1875f, 0.8125f, 0.25f, 0.8125f, 0.25f, 0.90625f
        };
        addTextures(this.leftLegOverlayTexCoordinates);
    }
}
