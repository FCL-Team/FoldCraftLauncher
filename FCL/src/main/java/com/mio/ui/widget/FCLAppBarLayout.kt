package com.mio.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import androidx.core.content.withStyledAttributes
import com.tungsten.fcllibrary.R

class FCLAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppBarLayout(context, attrs, defStyleAttr) {

    private var autoTint = false

    private val theme: IntegerProperty = object : IntegerPropertyBase() {
        override fun invalidated() {
            get()
            if (autoTint) {
                setBackgroundTintList(
                    ColorStateList(
                        arrayOf<IntArray?>(intArrayOf()),
                        intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                    )
                )
            }
        }

        override fun getBean(): Any {
            return this
        }

        override fun getName(): String {
            return "theme"
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.FCLAppBarLayout) {
            autoTint = getBoolean(
                R.styleable.FCLAppBarLayout_auto_tint,
                false
            )
        }
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty())
    }


}