package com.tungsten.fcllibrary.skin.cube.leg;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 左腿立方体 - 继承自LimbCube
 * 尺寸：4x12x4（Minecraft标准腿部尺寸）
 * 位置：X轴偏移2（身体左侧），Y轴偏移-10（身体下方）
 * 旋转：主旋转轴X轴，控制腿部摆动（行走动画）
 */
public class LeftLeg extends LimbCube {

    protected float[] leftLegTexCoordinates;
    
    public LeftLeg(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, 2.0f * scale, -10.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴（腿部摆动）
                1.5f, 30.0f, -30.0f,  // 主角度：步进1.5（与右腿相反）
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴（腿部内外摆动）
                -0.5f, 30.0f, -30.0f); // 副角度：步进-0.5
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 左腿区域：皮肤底部中间区域（对应UV 0.25-0.5水平，0.75-1.0垂直）
        this.leftLegTexCoordinates = new float[] {
                0.3125f, 1.0f, 0.3125f, 0.90625f, 0.375f, 0.90625f, 0.375f, 1.0f,
                0.3125f, 0.90625f, 0.3125f, 0.8125f, 0.375f, 0.8125f, 0.375f, 0.90625f,
                0.3125f, 0.8125f, 0.3125f, 0.75f, 0.375f, 0.75f, 0.375f, 0.8125f,
                0.375f, 0.8125f, 0.375f, 0.75f, 0.4375f, 0.75f, 0.4375f, 0.8125f,
                0.375f, 1.0f, 0.375f, 0.90625f, 0.4375f, 0.90625f, 0.4375f, 1.0f,
                0.375f, 0.90625f, 0.375f, 0.8125f, 0.4375f, 0.8125f, 0.4375f, 0.90625f,
                0.25f, 1.0f, 0.25f, 0.90625f, 0.3125f, 0.90625f, 0.3125f, 1.0f,
                0.25f, 0.90625f, 0.25f, 0.8125f, 0.3125f, 0.8125f, 0.3125f, 0.90625f,
                0.4375f, 1.0f, 0.4375f, 0.90625f, 0.5f, 0.90625f, 0.5f, 1.0f,
                0.4375f, 0.90625f, 0.4375f, 0.8125f, 0.5f, 0.8125f, 0.5f, 0.90625f
        };
        addTextures(this.leftLegTexCoordinates);
    }
}
