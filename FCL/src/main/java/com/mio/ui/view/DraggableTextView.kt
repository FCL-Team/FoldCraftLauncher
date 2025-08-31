package com.mio.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.edit
import com.tungsten.fcl.FCLApplication
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine

class DraggableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("launcher", Context.MODE_PRIVATE).apply {
            val xx = getFloat("fpsX", -1f)
            val yy = getFloat("fpsY", -1f)
            if (xx != -1f && yy != -1f) {
                post {
                    x = xx
                    y = yy
                }
            }
        }

    init {
        setTextColor(ThemeEngine.getInstance().theme.color2)
    }

    private var lastX = 0f
    private var lastY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX
                lastY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.rawX - lastX
                val deltaY = event.rawY - lastY
                x += deltaX
                y += deltaY
                lastX = event.rawX
                lastY = event.rawY
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                sharedPreferences.edit {
                    putFloat("fpsX", x)
                    putFloat("fpsY", y)
                }
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun resetPosition() {
        sharedPreferences.edit {
            putFloat("fpsX", -1f)
            putFloat("fpsY", -1f)
        }
        val activity = FCLApplication.getCurrentActivity()
        x = (AndroidUtils.getScreenWidth(activity) - width) / 2f
        y = (AndroidUtils.getScreenHeight(activity) - height) / 2f
    }
}