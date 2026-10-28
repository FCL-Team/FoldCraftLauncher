package com.mio.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
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
            Glide.with(view).load(drawable).centerCrop()
                .into(object : CustomViewTarget<View, Drawable>(view) {
                    override fun onResourceCleared(placeholder: Drawable?) {
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                    }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                // centerCrop 产物的 Bitmap 归 Glide 位图池所有：请求被清除（视图 detach、
                // 同视图再次加载）或内存紧张触发池回收时会 recycle，而 background 会被
                // 视图持续绘制，直接引用会导致 "Canvas: trying to use a recycled bitmap" 崩溃，
                // 必须复制为自持位图后再设背景
                val source = (resource as? BitmapDrawable)?.bitmap
                if (source != null && !source.isRecycled) {
                    view.background = BitmapDrawable(
                        view.resources,
                        source.copy(source.config ?: Bitmap.Config.ARGB_8888, false)
                    )
                }
            }

                })

        }
    }
}