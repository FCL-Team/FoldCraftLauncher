package com.tungsten.fcllibrary.component.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fcllibrary.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.lang.ref.WeakReference

class FCLView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var useThemeColor = false

    private fun applyTheme() {
        if (useThemeColor) {
            setBackgroundColor(ThemeEngine.getInstance().getTheme().color2)
        }
    }

    private fun applyThemeDark() {
        if (useThemeColor) {
            setBackgroundColor(ThemeEngine.getInstance().getTheme().color2)
        }
    }

    private fun bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        val ref = WeakReference(this)
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2Flow()) { c ->
            val self = ref.get()
            self?.applyTheme()
        }
    }

    private fun bindThemeDark() {
        // 弱引用自身：对齐原 themeDark.bind(...) 的弱监听语义，视图可被 GC
        val ref = WeakReference(this)
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2DarkFlow()) { c ->
            val self = ref.get()
            self?.applyThemeDark()
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLView)
        useThemeColor = typedArray.getBoolean(R.styleable.FCLView_use_theme_color, false)
        typedArray.recycle()
        bindTheme()
        bindThemeDark()
    }
}
