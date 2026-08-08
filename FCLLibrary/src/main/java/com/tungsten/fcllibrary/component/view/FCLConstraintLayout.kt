package com.tungsten.fcllibrary.component.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.lang.ref.WeakReference

class FCLConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var autoTint = false

    private fun applyTheme() {
        if (autoTint) {
            setBackgroundTintList(
                ColorStateList(
                    arrayOf<IntArray?>(intArrayOf()),
                    intArrayOf(ThemeEngine.getInstance().getTheme().getLtColor())
                )
            )
        }
    }

    private fun bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        val ref = WeakReference(this)
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow()) { c ->
            val self = ref.get()
            self?.applyTheme()
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.FCLConstraintLayout) {
            autoTint = getBoolean(R.styleable.FCLConstraintLayout_auto_tint, false)
        }
        bindTheme()
    }

}
