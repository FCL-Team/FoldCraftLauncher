package com.mio.util

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import kotlin.math.max
import androidx.core.graphics.withTranslation

/**
 * 等比缩放居中裁剪的 Drawable 包装器：绘制时按目标 bounds 实时计算缩放矩阵，
 * 视图尺寸变化（旋转、折叠、息屏前后的分辨率切换）后仍保持正确比例，
 * 不依赖加载时刻定格的视图尺寸。
 */
class CenterCropDrawable(private val wrapped: Drawable) : Drawable() {

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        if (bounds.isEmpty) return
        val srcWidth = wrapped.intrinsicWidth
        val srcHeight = wrapped.intrinsicHeight
        if (srcWidth <= 0 || srcHeight <= 0) {
            // 无固有尺寸（纯色等），退化为拉伸铺满
            wrapped.bounds = bounds
            wrapped.draw(canvas)
            return
        }
        val scale = max(
            bounds.width().toFloat() / srcWidth,
            bounds.height().toFloat() / srcHeight
        )
        val dx = bounds.exactCenterX() - srcWidth * scale / 2f
        val dy = bounds.exactCenterY() - srcHeight * scale / 2f
        canvas.withTranslation(dx, dy) {
            scale(scale, scale)
            wrapped.setBounds(0, 0, srcWidth, srcHeight)
            wrapped.draw(this)
        }
    }

    override fun setAlpha(alpha: Int) {
        wrapped.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        wrapped.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = wrapped.opacity

    override fun getIntrinsicWidth(): Int = wrapped.intrinsicWidth

    override fun getIntrinsicHeight(): Int = wrapped.intrinsicHeight
}
