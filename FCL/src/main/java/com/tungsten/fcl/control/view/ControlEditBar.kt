package com.tungsten.fcl.control.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.view.FCLButton

/**
 * 编辑模式悬浮操作栏：跟随选中控件显示，提供设置、复制、删除操作
 */
class ControlEditBar(context: Context) : LinearLayout(context) {

    /** 操作回调，由 ViewManager 实现并转发到编辑逻辑 */
    interface Listener {
        fun onSettings()
        fun onCopy()
        fun onDelete()
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    /** 淡入显示（由 ViewManager 定位后调用） */
    fun showAnimated() {
        animate().cancel()
        visibility = VISIBLE
        animate().alpha(1f).setDuration(ANIMATION_DURATION).start()
    }

    /** 淡出隐藏 */
    fun hideAnimated() {
        if (visibility != VISIBLE) {
            return
        }
        animate().cancel()
        animate().alpha(0f).setDuration(ANIMATION_DURATION).withEndAction {
            visibility = GONE
        }.start()
    }

    init {
        orientation = HORIZONTAL
        // 按钮经 XML 构造（app:ripple="true"）：代码构造的 FCLButton ripple 恒为 false，
        // 触摸抬起与主题刷新都会把背景覆盖回透明
        listOf(
            R.string.menu_settings to Runnable { listener?.onSettings() },
            R.string.edit_view_clone to Runnable { listener?.onCopy() },
            R.string.edit_view_delete to Runnable { listener?.onDelete() }
        ).forEach { (textRes, action) ->
            val button = LayoutInflater.from(context)
                .inflate(R.layout.item_control_edit_bar_button, this, false) as FCLButton
            button.text = context.getString(textRes)
            button.setOnClickListener { action.run() }
            addView(button)
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 150L
    }
}
