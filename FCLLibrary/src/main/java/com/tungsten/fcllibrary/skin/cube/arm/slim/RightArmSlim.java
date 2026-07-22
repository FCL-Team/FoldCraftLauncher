package com.tungsten.fcllibrary.skin.cube.arm.slim;

import com.tungsten.fcllibrary.skin.cube.LimbCube;

/**
 * 纤细体型右臂立方体 - 继承自LimbCube
 * 尺寸：3x12x4（比正常体型手臂窄1像素）
 * 位置：X轴偏移-5.5（比正常体型更靠近身体），Y轴偏移2
 * 旋转：主旋转轴X轴，控制手臂摆动
 */
public class RightArmSlim extends LimbCube {

    protected float[] rightArmSlimTexCoordinates;
    
    public RightArmSlim(float scale) {
        super(3.0f * scale, 12.0f * scale, 4.0f * scale, -5.5f * scale, 2.0f * scale, 0.0f * scale,
                1.0f, 0.0f, 0.0f,    // 主旋转轴：X轴
                0.5f, 10.0f, -10.0f, // 主角度参数
                0.0f, 1.0f, 0.0f,    // 副旋转轴：Y轴
                -0.333f, 20.0f, -20.0f); // 副角度参数
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 纤细右手臂使用压缩的纹理区域，UV间隔更小
        this.rightArmSlimTexCoordinates = new float[] {
                0.6875f, 0.5f, 0.6875f, 0.40625f, 0.734375f, 0.40625f, 0.734375f, 0.5f,
                0.6875f, 0.40625f, 0.6875f, 0.3125f, 0.734375f, 0.3125f, 0.734375f, 0.40625f,
                0.6875f, 0.3125f, 0.6875f, 0.25f, 0.734375f, 0.25f, 0.734375f, 0.3125f,
                0.734375f, 0.3125f, 0.734375f, 0.25f, 0.78125f, 0.25f, 0.78125f, 0.3125f,
                0.734375f, 0.5f, 0.734375f, 0.40625f, 0.796875f, 0.40625f, 0.796875f, 0.5f,
                0.734375f, 0.40625f, 0.734375f, 0.3125f, 0.796875f, 0.3125f, 0.796875f, 0.40625f,
                0.625f, 0.5f, 0.625f, 0.40625f, 0.6875f, 0.40625f, 0.6875f, 0.5f,
                0.625f, 0.40625f, 0.625f, 0.3125f, 0.6875f, 0.3125f, 0.6875f, 0.40625f,
                0.796875f, 0.5f, 0.796875f, 0.40625f, 0.84375f, 0.40625f, 0.84375f, 0.5f,
                0.796875f, 0.40625f, 0.796875f, 0.3125f, 0.84375f, 0.3125f, 0.84375f, 0.40625f
        };
        addTextures(this.rightArmSlimTexCoordinates);
    }
}
