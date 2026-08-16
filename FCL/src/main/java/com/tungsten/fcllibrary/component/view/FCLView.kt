package com.tungsten.fcllibrary.component.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine

class FCLView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var useThemeColor = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLView)
        useThemeColor = typedArray.getBoolean(R.styleable.FCLView_use_theme_color, false)
        typedArray.recycle()
        ThemeEngine.getInstance().registerEvent(this) {
            if (useThemeColor) {
                setBackgroundColor(ThemeEngine.getInstance().getTheme().getColor2())
            }
        }
    }
}
