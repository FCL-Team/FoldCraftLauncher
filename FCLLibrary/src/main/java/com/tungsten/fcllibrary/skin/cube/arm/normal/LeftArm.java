package com.tungsten.fcllibrary.skin.cube.arm.normal;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 正常体型左臂立方体 - 继承自LimbCube
 * 尺寸：4x12x4（Minecraft标准手臂尺寸）
 * 位置：X轴偏移6（身体左侧），Y轴偏移2（与身体底部对齐）
 * 旋转：主旋转轴X轴，控制手臂摆动（与右臂相反方向）
 */
public class LeftArm extends LimbCube {

    protected float[] leftArmTexCoordinates;
    
    public LeftArm(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, 6.0f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,     // 主旋转轴：X轴（手臂前后摆动）
                -0.5f, 10.0f, -10.0f, // 主角度：步进-0.5（与右臂相反）
                0.0f, 1.0f, 0.0f,     // 副旋转轴：Y轴（手臂内外摆动）
                0.333f, 20.0f, -20.0f); // 副角度：步进0.333
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 左手臂区域：皮肤底部左侧区域（对应UV 0.5-0.75水平，0.75-1.0垂直）
        this.leftArmTexCoordinates = new float[] {
                0.5625f, 1.0f, 0.5625f, 0.90625f, 0.625f, 0.90625f, 0.625f, 1.0f,
                0.5625f, 0.90625f, 0.5625f, 0.8125f, 0.625f, 0.8125f, 0.625f, 0.90625f,
                0.5625f, 0.8125f, 0.5625f, 0.75f, 0.625f, 0.75f, 0.625f, 0.8125f,
                0.625f, 0.8125f, 0.625f, 0.75f, 0.6875f, 0.75f, 0.6875f, 0.8125f,
                0.625f, 1.0f, 0.625f, 0.90625f, 0.6875f, 0.90625f, 0.6875f, 1.0f,
                0.625f, 0.90625f, 0.625f, 0.8125f, 0.6875f, 0.8125f, 0.6875f, 0.90625f,
                0.5f, 1.0f, 0.5f, 0.90625f, 0.5625f, 0.90625f, 0.5625f, 1.0f,
                0.5f, 0.90625f, 0.5f, 0.8125f, 0.5625f, 0.8125f, 0.5625f, 0.90625f,
                0.6875f, 1.0f, 0.6875f, 0.90625f, 0.75f, 0.90625f, 0.75f, 1.0f,
                0.6875f, 0.90625f, 0.6875f, 0.8125f, 0.75f, 0.8125f, 0.75f, 0.90625f
        };
        addTextures(this.leftArmTexCoordinates);
    }
}
