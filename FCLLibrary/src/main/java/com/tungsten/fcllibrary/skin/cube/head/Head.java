package com.tungsten.fcllibrary.skin.cube.head;

import com.tungsten.fcllibrary.skin.cube.MainCube;

/**
 * 头部立方体 - 继承自MainCube
 * 尺寸：8x8x8（Minecraft标准头部尺寸）
 * 位置：Y轴偏移12（位于身体上方）
 */
public class Head extends MainCube {

    protected float[] headTexCoordinates;
    
    public Head(float scale) {
        super(8.0f * scale, 8.0f * scale, 8.0f * scale, 0.0f * scale, 12.0f * scale, 0.0f * scale);
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // Minecraft皮肤纹理格式：64x64像素
        // 头部区域：左上角8x8像素块（对应UV 0.125-0.25范围）
        // 每个面4个顶点，每个顶点2个坐标(u, v)，顶点顺序与faceVertices对应
        // 面顺序：前(Face 0)、上(Face 1)、下(Face 2)、右(Face 3)、左(Face 4)、后(Face 5)
        this.headTexCoordinates = new float[] {
                // Face 0: 前面 - 皮肤左上角第二行 (8-16像素)
                0.125f, 0.25f,    // 左下 uv(8,16)
                0.125f, 0.125f,   // 左上 uv(8,8)
                0.25f, 0.125f,    // 右上 uv(16,8)
                0.25f, 0.25f,     // 右下 uv(16,16)

                // Face 1: 上面 - 皮肤左上角第一行 (0-8像素)
                0.125f, 0.125f,   // 后左 uv(8,8)
                0.125f, 0.0f,     // 前左 uv(8,0)
                0.25f, 0.0f,      // 前右 uv(16,0)
                0.25f, 0.125f,    // 后右 uv(16,8)

                // Face 2: 下面 - 皮肤顶部中间区域
                0.25f, 0.125f,    // 后右
                0.25f, 0.0f,      // 前右
                0.375f, 0.0f,     // 前左
                0.375f, 0.125f,   // 后左

                // Face 3: 右面 - 皮肤顶部右侧区域
                0.25f, 0.25f,     // 后下
                0.25f, 0.125f,    // 后上
                0.375f, 0.125f,   // 前上
                0.375f, 0.25f,    // 前下

                // Face 4: 左面 - 皮肤顶部左侧区域
                0.0f, 0.25f,      // 前下
                0.0f, 0.125f,     // 前上
                0.125f, 0.125f,   // 后上
                0.125f, 0.25f,    // 后下

                // Face 5: 后面 - 皮肤顶部中右区域
                0.375f, 0.25f,    // 左下
                0.375f, 0.125f,   // 左上
                0.5f, 0.125f,     // 右上
                0.5f, 0.25f       // 右下
        };
        addTextures(this.headTexCoordinates);
    }
}
