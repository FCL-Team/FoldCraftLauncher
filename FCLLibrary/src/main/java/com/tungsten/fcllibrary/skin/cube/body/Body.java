package com.tungsten.fcllibrary.skin.cube.body;

import com.tungsten.fcllibrary.skin.cube.MainCube;

/**
 * 身体立方体 - 继承自MainCube
 * 尺寸：8x12x4（Minecraft标准身体尺寸）
 * 位置：Y轴偏移2（位于腿部上方）
 */
public class Body extends MainCube {

    protected float[] bodyTexCoordinates;

    public Body(float scale) {
        super(8.0f * scale, 12.0f * scale, 4.0f * scale, 0.0f * scale, 2.0f * scale, 0.0f * scale);

        // 纹理坐标数组（UV坐标，归一化0-1）
        // 身体区域：皮肤顶部中间8x12像素块（对应UV 0.25-0.4375水平，0.25-0.5垂直）
        // 面顺序：前(Face 0)、上(Face 1)、下(Face 2)、右(Face 3)、左(Face 4)、后(Face 5)
        this.bodyTexCoordinates = new float[]{
                // Face 0: 前面 - 皮肤顶部中间区域
                0.3125f, 0.5f,      // 左下 uv(20,32)
                0.3125f, 0.3125f,   // 左上 uv(20,20)
                0.4375f, 0.3125f,   // 右上 uv(28,20)
                0.4375f, 0.5f,      // 右下 uv(28,32)

                // Face 1: 上面 - 皮肤顶部区域
                0.3125f, 0.3125f,   // 后左 uv(20,20)
                0.3125f, 0.25f,     // 前左 uv(20,16)
                0.4375f, 0.25f,     // 前右 uv(28,16)
                0.4375f, 0.3125f,   // 后右 uv(28,20)

                // Face 2: 下面 - 皮肤顶部中右区域
                0.4375f, 0.3125f,   // 后右
                0.4375f, 0.25f,     // 前右
                0.5625f, 0.25f,     // 前左
                0.5625f, 0.3125f,   // 后左

                // Face 3: 右面 - 皮肤右侧区域
                0.4375f, 0.5f,      // 后下
                0.4375f, 0.3125f,   // 后上
                0.5f, 0.3125f,      // 前上
                0.5f, 0.5f,         // 前下

                // Face 4: 左面 - 皮肤左侧区域
                0.25f, 0.5f,        // 前下
                0.25f, 0.3125f,     // 前上
                0.3125f, 0.3125f,   // 后上
                0.3125f, 0.5f,      // 后下

                // Face 5: 后面 - 皮肤中右区域
                0.5f, 0.5f,         // 左下
                0.5f, 0.3125f,      // 左上
                0.625f, 0.3125f,    // 右上
                0.625f, 0.5f        // 右下
        };
        addTextures(this.bodyTexCoordinates);
    }
}
