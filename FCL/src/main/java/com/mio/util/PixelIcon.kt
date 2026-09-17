package com.mio.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import java.util.concurrent.ConcurrentHashMap

/** 像素风图标尺寸上限：不超过该尺寸的图标按像素风处理，放大绘制用最近邻插值保持锐利 */
const val PIXEL_ICON_MAX_SIZE = 128

/**
 * 按位图尺寸选择绘制插值：不超过 [PIXEL_ICON_MAX_SIZE] 的像素风小图关闭双线性，
 * 放大时保持像素锐利；更高分辨率的图标保留双线性，缩小时避免走样
 */
fun applyPixelFilter(drawable: BitmapDrawable, bitmap: Bitmap) {
    drawable.paint.isFilterBitmap = bitmap.width > PIXEL_ICON_MAX_SIZE || bitmap.height > PIXEL_ICON_MAX_SIZE
}

private val iconStates = ConcurrentHashMap<Int, Drawable.ConstantState>()

/**
 * 加载内置位图图标：禁用解码时的密度预缩放，保留像素风图标的原始像素，缩放统一推迟到绘制阶段。
 * 每次返回独立 Drawable 实例，避免共享实例的 bounds 被修改后互相污染
 */
fun pixelAwareIcon(context: Context, @DrawableRes id: Int): Drawable {
    val state = iconStates.computeIfAbsent(id) { key ->
        val resources = context.resources
        val opts = BitmapFactory.Options().apply { inScaled = false }
        val bitmap = BitmapFactory.decodeResource(resources, key, opts)
        val drawable = if (bitmap != null)
            BitmapDrawable(resources, bitmap).apply { applyPixelFilter(this, bitmap) }
        else
        // 位图解码失败时回落资源加载（vector 等非位图资源）
            AppCompatResources.getDrawable(context, key)
                ?: error("Drawable resource not found: ${resources.getResourceEntryName(key)}")
        drawable.constantState!!
    }
    return state.newDrawable()
}
