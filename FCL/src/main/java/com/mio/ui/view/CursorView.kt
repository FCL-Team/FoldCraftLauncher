package com.mio.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tungsten.fcllibrary.util.ConvertUtils

class CursorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    var offsetX = 0
        set(value) {
            field = ConvertUtils.dip2px(context, value.toFloat())
        }

    var offsetY = 0
        set(value) {
            field = ConvertUtils.dip2px(context, value.toFloat())
        }

    override fun setX(x: Float) {
        super.setX(x + offsetX)
    }

    override fun setY(y: Float) {
        super.setY(y + offsetY)
    }

    override fun getX(): Float {
        return super.getX() - offsetX
    }

    override fun getY(): Float {
        return super.getY() - offsetY
    }
}