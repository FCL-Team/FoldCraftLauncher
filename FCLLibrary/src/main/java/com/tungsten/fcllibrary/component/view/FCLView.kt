package com.tungsten.fcllibrary.component.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase
import com.tungsten.fcllibrary.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine

class FCLView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var useThemeColor = false
    private val theme: IntegerProperty = object : IntegerPropertyBase() {
        override fun invalidated() {
            get()
            if (useThemeColor) {
                setBackgroundColor(ThemeEngine.getInstance().getTheme().color2)
            }
        }

        override fun getBean(): Any {
            return this
        }

        override fun getName(): String {
            return "theme"
        }
    }

    private val themeDark: IntegerProperty = object : IntegerPropertyBase() {
        override fun invalidated() {
            get()
            if (useThemeColor) {
                setBackgroundColor(ThemeEngine.getInstance().getTheme().color2)
            }
        }

        override fun getBean(): Any {
            return this
        }

        override fun getName(): String {
            return "themeDark"
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLView)
        useThemeColor = typedArray.getBoolean(R.styleable.FCLView_use_theme_color, false)
        typedArray.recycle()
        theme.bind(ThemeEngine.getInstance().getTheme().color2Property())
        themeDark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty())
    }
}