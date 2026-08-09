package com.mio.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import androidx.core.content.withStyledAttributes
import com.tungsten.fcllibrary.R
import java.lang.ref.WeakReference

class FCLAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppBarLayout(context, attrs, defStyleAttr) {

    private var autoTint = false

    private fun applyTheme() {
        if (autoTint) {
            setBackgroundTintList(
                ColorStateList(
                    arrayOf<IntArray?>(intArrayOf()),
                    intArrayOf(ThemeEngine.getInstance().getTheme().ltColor)
                )
            )
        }
    }

    init {
        elevation = 0f
        stateListAnimator = null
        context.withStyledAttributes(attrs, R.styleable.FCLAppBarLayout) {
            autoTint = getBoolean(
                R.styleable.FCLAppBarLayout_auto_tint,
                false
            )
        }
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        val ref = WeakReference(this)
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow()) { c ->
            ref.get()?.applyTheme()
        }
    }


}
