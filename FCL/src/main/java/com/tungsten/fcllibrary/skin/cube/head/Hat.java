package com.tungsten.fcllibrary.skin.cube.head;

import com.tungsten.fcllibrary.skin.cube.MainCube;

/**
 * 帽子立方体 - 继承自MainCube
 * 尺寸：9x9x9（比头部稍大，形成帽子效果）
 * 位置：与头部重合，渲染在头部上方
 */
public class Hat extends MainCube {

    protected float[] hatTexCoordinates;
    
    public Hat(float scale) {
        super(9.0f * scale, 9.0f * scale, 9.0f * scale, 0.0f * scale, 12.0f * scale, 0.0f * scale);
        
        // 纹理坐标数组（UV坐标，归一化0-1）
        // 帽子区域：皮肤右上角8x8像素块（对应UV 0.5-0.75范围）
        // 面顺序：前(Face 0)、上(Face 1)、下(Face 2)、右(Face 3)、左(Face 4)、后(Face 5)
        this.hatTexCoordinates = new float[] {
                // Face 0: 前面 - 皮肤右上区域第二行
                0.625f, 0.25f,    // 左下 uv(40,16)
                0.625f, 0.125f,   // 左上 uv(40,8)
                0.75f, 0.125f,    // 右上 uv(48,8)
                0.75f, 0.25f,     // 右下 uv(48,16)

                // Face 1: 上面 - 皮肤右上区域第一行
                0.625f, 0.125f,   // 后左 uv(40,8)
                0.625f, 0.0f,     // 前左 uv(40,0)
                0.75f, 0.0f,      // 前右 uv(48,0)
                0.75f, 0.125f,    // 后右 uv(48,8)

                // Face 2: 下面 - 皮肤顶部右中区域
                0.75f, 0.125f,    // 后右
                0.75f, 0.0f,      // 前右
                0.875f, 0.0f,     // 前左
                0.875f, 0.125f,   // 后左

                // Face 3: 右面 - 皮肤顶部最右侧区域
                0.75f, 0.25f,     // 后下
                0.75f, 0.125f,    // 后上
                0.875f, 0.125f,   // 前上
                0.875f, 0.25f,    // 前下

                // Face 4: 左面 - 皮肤顶部右侧区域
                0.5f, 0.25f,      // 前下
                0.5f, 0.125f,     // 前上
                0.625f, 0.125f,   // 后上
                0.625f, 0.25f,    // 后下

                // Face 5: 后面 - 皮肤顶部最右侧边缘
                0.875f, 0.25f,    // 左下
                0.875f, 0.125f,   // 左上
                1.0f, 0.125f,     // 右上
                1.0f, 0.25f       // 右下
        };
        addTextures(this.hatTexCoordinates);
    }
}
