package com.mio.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.edit
import androidx.core.content.withStyledAttributes
import com.tungsten.fcl.R
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine

class DraggableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("DraggableTextView", Context.MODE_PRIVATE)
    var isMoving = false
    var saveKey = ""

    init {
        context.withStyledAttributes(attrs, R.styleable.DraggableTextView) {
            saveKey = getString(R.styleable.DraggableTextView_save_key) ?: "default"
        }
        setTextColor(ThemeEngine.getInstance().getTheme().getColor2())
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
                isMoving = true
                val deltaX = event.rawX - lastX
                val deltaY = event.rawY - lastY
                lastX = event.rawX
                lastY = event.rawY
                val maxX = (parent as View).width - width
                val maxY = (parent as View).height - height
                val newX = x + deltaX
                val newY = y + deltaY
                x = newX.coerceIn(0f, maxX.toFloat())
                y = newY.coerceIn(0f, maxY.toFloat())
                updateSavedPosition(x, y)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                performClick()
                updateSavedPosition(x, y)
                isMoving = false
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun resetPosition() {
        updateSavedPosition(-1f, -1f)
        x = (AndroidUtils.getScreenWidth() - width) / 2f
        y = (AndroidUtils.getScreenHeight() - height) / 2f
    }

    fun initPosition() {
        post {
            val (xx, yy) = getSavedPosition()
            if (xx != -1f && yy != -1f) {
                post {
                    x = xx
                    y = yy
                }
            }
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (!isMoving)
            initPosition()
    }

    private fun updateSavedPosition(x: Float, y: Float) {
        sharedPreferences.edit {
            putFloat("${saveKey}_x", x)
            putFloat("${saveKey}_y", y)
        }
    }

    private fun getSavedPosition(): Pair<Float, Float> {
        return Pair(
            sharedPreferences.getFloat("${saveKey}_x", -1f),
            sharedPreferences.getFloat("${saveKey}_y", -1f)
        )
    }
}