package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 右腿立方体 - 继承自LimbCube
 * 尺寸：4x12x4（Minecraft标准腿部尺寸）
 * 位置：X轴偏移-2（身体右侧），Y轴偏移-10（身体下方）
 * 旋转：主旋转轴X轴，控制腿部摆动（行走动画）
 */
public class RightLeg extends LimbCube {

    protected float[] rightLegTexCoordinates;
    
    public RightLeg(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, -2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴（腿部摆动）
                -1.5f, 30.0f, -30.0f, // 主角度：步进-1.5（与左腿相反）
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴（腿部内外摆动）
                0.5f, 30.0f, -30.0f); // 副角度：步进0.5
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 右腿区域：皮肤左下角区域（对应UV 0-0.25水平，0.25-0.5垂直）
        this.rightLegTexCoordinates = new float[] {
                0.0625f, 0.5f, 0.0625f, 0.40625f, 0.125f, 0.40625f, 0.125f, 0.5f,
                0.0625f, 0.40625f, 0.0625f, 0.3125f, 0.125f, 0.3125f, 0.125f, 0.40625f,
                0.0625f, 0.3125f, 0.0625f, 0.25f, 0.125f, 0.25f, 0.125f, 0.3125f,
                0.125f, 0.3125f, 0.125f, 0.25f, 0.1875f, 0.25f, 0.1875f, 0.3125f,
                0.125f, 0.5f, 0.125f, 0.40625f, 0.1875f, 0.40625f, 0.1875f, 0.5f,
                0.125f, 0.40625f, 0.125f, 0.3125f, 0.1875f, 0.3125f, 0.1875f, 0.40625f,
                0.0f, 0.5f, 0.0f, 0.40625f, 0.0625f, 0.40625f, 0.0625f, 0.5f,
                0.0f, 0.40625f, 0.0f, 0.3125f, 0.0625f, 0.3125f, 0.0625f, 0.40625f,
                0.1875f, 0.5f, 0.1875f, 0.40625f, 0.25f, 0.40625f, 0.25f, 0.5f,
                0.1875f, 0.40625f, 0.1875f, 0.3125f, 0.25f, 0.3125f, 0.25f, 0.40625f
        };
        addTextures(this.rightLegTexCoordinates);
    }
}
