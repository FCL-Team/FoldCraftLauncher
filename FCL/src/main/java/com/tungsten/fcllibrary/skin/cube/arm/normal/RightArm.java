package com.tungsten.fcllibrary.skin.cube.arm.normal;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 正常体型右臂立方体 - 继承自LimbCube
 * 尺寸：4x12x4（Minecraft标准手臂尺寸）
 * 位置：X轴偏移-6（身体右侧），Y轴偏移2（与身体底部对齐）
 * 旋转：主旋转轴X轴，控制手臂摆动
 */
public class RightArm extends LimbCube {

    protected float[] rightArmTexCoordinates;

    public RightArm(float scale) {
        super(4.0f * scale, 12.0f * scale, 4.0f * scale, -6.0f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴（手臂前后摆动）
                0.5f, 10.0f, -10.0f, // 主角度：步进0.5，范围-10到10度
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴（手臂内外摆动）
                -0.333f, 20.0f, -20.0f); // 副角度：步进-0.333，范围-20到20度

        // 纹理坐标数组（UV坐标，归一化0-1）
        // 右手臂区域：皮肤右半部分中间区域（对应UV 0.625-0.875水平，0.25-0.5垂直）
        // LimbCube使用10个面，每个面4个顶点，每个顶点2个坐标(u, v)
        this.rightArmTexCoordinates = new float[]{
                0.6875f, 0.5f, 0.6875f, 0.40625f, 0.75f, 0.40625f, 0.75f, 0.5f,
                0.6875f, 0.40625f, 0.6875f, 0.3125f, 0.75f, 0.3125f, 0.75f, 0.40625f,
                0.6875f, 0.3125f, 0.6875f, 0.25f, 0.75f, 0.25f, 0.75f, 0.3125f,
                0.75f, 0.3125f, 0.75f, 0.25f, 0.8125f, 0.25f, 0.8125f, 0.3125f,
                0.75f, 0.5f, 0.75f, 0.40625f, 0.8125f, 0.40625f, 0.8125f, 0.5f,
                0.75f, 0.40625f, 0.75f, 0.3125f, 0.8125f, 0.3125f, 0.8125f, 0.40625f,
                0.625f, 0.5f, 0.625f, 0.40625f, 0.6875f, 0.40625f, 0.6875f, 0.5f,
                0.625f, 0.40625f, 0.625f, 0.3125f, 0.6875f, 0.3125f, 0.6875f, 0.40625f,
                0.8125f, 0.5f, 0.8125f, 0.40625f, 0.875f, 0.40625f, 0.875f, 0.5f,
                0.8125f, 0.40625f, 0.8125f, 0.3125f, 0.875f, 0.3125f, 0.875f, 0.40625f
        };
        addTextures(this.rightArmTexCoordinates);
    }
}
