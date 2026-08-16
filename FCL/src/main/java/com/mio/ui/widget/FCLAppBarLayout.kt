package com.mio.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import androidx.core.content.withStyledAttributes
import com.tungsten.fcl.R

class FCLAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppBarLayout(context, attrs, defStyleAttr) {

    private var autoTint = false

    init {
        elevation = 0f
        stateListAnimator = null
        context.withStyledAttributes(attrs, R.styleable.FCLAppBarLayout) {
            autoTint = getBoolean(
                R.styleable.FCLAppBarLayout_auto_tint,
                false
            )
        }
        ThemeEngine.getInstance().registerEvent(this) {
            if (autoTint) {
                setBackgroundTintList(
                    ColorStateList(
                        arrayOf<IntArray?>(intArrayOf()),
                        intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                    )
                )
            }
        }
    }

}
