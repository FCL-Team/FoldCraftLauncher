package com.mio.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View
import java.io.File
import java.util.Optional

class ImageUtil {
    companion object {
        @JvmStatic
        fun getSize(path: String): Pair<Int, Int> {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)
            return Pair(options.outWidth, options.outHeight)
        }

        @JvmStatic
        fun getBitmapMemorySize(path: String): Long {
            val size = getSize(path)
            return size.first * size.second * 4L
        }

        @JvmStatic
        fun load(path: String): Optional<Bitmap> {
            if (!File(path).exists() or (getBitmapMemorySize(path) > 104857600)) {
                return Optional.empty<Bitmap>()
            }
            return Optional.ofNullable(BitmapFactory.decodeFile(path))
        }

        @JvmStatic
        fun loadInto(view: View, drawable: Drawable) {
            // 派生独立实例：多个视图共享同一 Drawable 实例时 setBounds 会互相覆盖
            // （共享的底层 bitmap 数据只读，不受影响）
            val source = drawable.constantState?.newDrawable() ?: drawable
            view.background = CenterCropDrawable(source)
        }
    }
}