package com.tungsten.fcllibrary.skin.cube.arm.slim;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 纤细体型左臂立方体 - 继承自LimbCube
 * 尺寸：3x12x4（比正常体型手臂窄1像素）
 * 位置：X轴偏移5.5（比正常体型更靠近身体），Y轴偏移2
 * 旋转：主旋转轴X轴，控制手臂摆动
 */
public class LeftArmSlim extends LimbCube {

    protected float[] leftArmSlimTexCoordinates;
    
    public LeftArmSlim(float scale) {
        super(3.0f * scale, 12.0f * scale, 4.0f * scale, 5.5f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                -0.5f, 10.0f, -10.0f, // 主角度参数
                0.0f, 1.0f, 0.0f,     // 副旋转轴：Y轴
                0.333f, 20.0f, -20.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 纤细左手臂使用压缩的纹理区域，UV间隔更小
        this.leftArmSlimTexCoordinates = new float[] {
                0.5625f, 1.0f, 0.5625f, 0.90625f, 0.609375f, 0.90625f, 0.609375f, 1.0f,
                0.5625f, 0.90625f, 0.5625f, 0.8125f, 0.609375f, 0.8125f, 0.609375f, 0.90625f,
                0.5625f, 0.8125f, 0.5625f, 0.75f, 0.609375f, 0.75f, 0.609375f, 0.8125f,
                0.609375f, 0.8125f, 0.609375f, 0.75f, 0.65625f, 0.75f, 0.65625f, 0.8125f,
                0.609375f, 1.0f, 0.609375f, 0.90625f, 0.671875f, 0.90625f, 0.671875f, 1.0f,
                0.609375f, 0.90625f, 0.609375f, 0.8125f, 0.671875f, 0.8125f, 0.671875f, 0.90625f,
                0.5f, 1.0f, 0.5f, 0.90625f, 0.5625f, 0.90625f, 0.5625f, 1.0f,
                0.5f, 0.90625f, 0.5f, 0.8125f, 0.5625f, 0.8125f, 0.5625f, 0.90625f,
                0.671875f, 1.0f, 0.671875f, 0.90625f, 0.71875f, 0.90625f, 0.71875f, 1.0f,
                0.671875f, 0.90625f, 0.671875f, 0.8125f, 0.71875f, 0.8125f, 0.71875f, 0.90625f
        };
        addTextures(this.leftArmSlimTexCoordinates);
    }
}
