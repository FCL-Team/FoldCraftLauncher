package com.tungsten.fcllibrary.component.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.withStyledAttributes
import com.tungsten.fcl.R
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fcllibrary.component.dialog.EditDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import kotlin.math.roundToInt

/** 数值滑条：轨道在数值文本两侧断开且断口圆角，点按数值弹出输入对话框精确设值 */
class FCLNumberSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatSeekBar(context, attrs) {

    private var fromUserOrSystem = false
    private var visibilityProperty: BooleanProperty? = null
    private var disableProperty: BooleanProperty? = null
    private var progressProperty: IntegerProperty? = null

    private var suffix: String = ""
    private var scale = 1
    private var thumbDrawable: ShapeDrawable? = null

    /** 数值文本画笔，颜色与进度条主题色对比（autoTint） */
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ThemeEngine.getTheme().autoTint
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    /** 填充段画笔（文本左侧轨道段），颜色为主题色（dkColor） */
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ThemeEngine.getTheme().dkColor
    }

    /** 未填充段画笔（文本右侧轨道段），颜色为弱化对比色 */
    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ThemeEngine.getTheme().autoHintTint
    }

    private val textBounds = Rect()

    private val barRect = RectF()

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            // 热区与绘制的文本区域一致（两端钳制偏移后的位置）
            textPaint.textSize = height / 1.5f
            val text = displayText(progress)
            val textWidth = textPaint.measureText(text)
            val centerX = computeTextCenterX(textWidth)
            if (event.x >= centerX - textWidth / 2f && event.x <= centerX + textWidth / 2f) {
                val dialog = EditDialog(context) { s ->
                    // 非数字或越界输入直接忽略；带缩放时输入为显示值，换算回实际进度
                    s.toFloatOrNull()?.let { (it * scale).roundToInt() }?.takeIf { it in min..max }?.let { progress = it }
                }
                dialog.appendTitle("(${displayValue(min)} ~ ${displayValue(max)})")
                dialog.getEditText().inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
                dialog.show()
                return true
            }
            return false
        }
    })

    init {
        // 透明占位轨道（bg_number_seekbar_track）：屏蔽样式默认材质轨道，视觉由本控件自绘
        progressDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_number_seekbar_track)
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.FCLNumberSeekBar) {
                suffix = getString(R.styleable.FCLNumberSeekBar_suffix) ?: ""
            }
        }
        ThemeEngine.registerEvent(this) { refreshTheme() }
    }

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private fun refreshTheme() {
        val theme = ThemeEngine.getTheme()
        textPaint.color = theme.autoTint
        fillPaint.color = theme.dkColor
        trackPaint.color = theme.autoHintTint
    }

    /** 动态设置数值后缀（% / dp 等），触发 thumb 重建 */
    fun setSuffix(suffix: String) {
        this.suffix = suffix
        thumbDrawable = null
        invalidate()
    }

    /** 设置数值缩放：显示值 = 进度 / scale（进度以实际值 10 倍存储时传 10），点按输入按显示值自动换算 */
    fun setValueScale(scale: Int) {
        this.scale = scale
        thumbDrawable = null
        invalidate()
    }

    /** 进度换算为显示值文本：scale=1 显示原值，否则带一位小数（如 875 → 87.5） */
    private fun displayValue(value: Int): String =
        if (scale == 1) "$value" else (value / scale.toFloat()).toString()

    private fun displayText(value: Int): String = displayValue(value) + suffix

    /** 挂载拖动监听：仅用户操作时同步进度属性，程序性变化（setMax/setMin 造成的进度钳制）不进入属性，避免误触发监听 */
    fun addProgressListener() {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    fromUserOrSystem = true
                    progressProperty().set(progress)
                    fromUserOrSystem = false
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        // 程序性设置进度时同步属性，触发监听与保存（onProgressChanged 仅用户操作才同步）
        progressProperty().set(progress)
    }

    fun setProgressValue(progressValue: Int) {
        progressProperty().set(progressValue)
    }

    fun getProgressValue(): Int {
        return progressProperty?.get() ?: -1
    }

    fun progressProperty(): IntegerProperty {
        if (progressProperty == null) {
            progressProperty = object : IntegerPropertyBase() {

                override fun invalidated() {
                    Schedulers.androidUIThread().execute {
                        if (!fromUserOrSystem) {
                            setProgress(get())
                        }
                    }
                }

                override fun getBean(): Any = this

                override fun getName(): String = "progress"
            }
        }

        return progressProperty!!
    }

    fun setVisibilityValue(visibility: Boolean) {
        visibilityProperty().set(visibility)
    }

    fun getVisibilityValue(): Boolean {
        return visibilityProperty == null || visibilityProperty!!.get()
    }

    fun visibilityProperty(): BooleanProperty {
        if (visibilityProperty == null) {
            visibilityProperty = object : BooleanPropertyBase() {

                override fun invalidated() {
                    Schedulers.androidUIThread().execute {
                        val visible = get()
                        this@FCLNumberSeekBar.visibility = if (visible) VISIBLE else GONE
                    }
                }

                override fun getBean(): Any = this

                override fun getName(): String = "visibility"
            }
        }

        return visibilityProperty!!
    }

    fun setDisableValue(disableValue: Boolean) {
        disableProperty().set(disableValue)
    }

    fun getDisableValue(): Boolean {
        return disableProperty == null || disableProperty!!.get()
    }

    fun disableProperty(): BooleanProperty {
        if (disableProperty == null) {
            disableProperty = object : BooleanPropertyBase() {

                override fun invalidated() {
                    Schedulers.androidUIThread().execute {
                        val disable = get()
                        isEnabled = !disable
                    }
                }

                override fun getBean(): Any = this

                override fun getName(): String = "disable"
            }
        }

        return disableProperty!!
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textPaint.textSize = height / 1.5f
        ensureThumb()
        val text = displayText(progress)
        val textWidth = textPaint.measureText(text)
        val textY = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
        // 数值文本居中绘制在滑块处（CENTER 对齐），贴近两端时会超出布局被裁剪，
        // 中心点钳制使文本完整落在 padding 范围内（点击热区与轨道断口走同一函数保持一致）
        val centerX = computeTextCenterX(textWidth)
        // 轨道自绘：8dp 胶囊条在文本两侧断开，断口圆角朝向文本
        val barHeight = 8f * resources.displayMetrics.density
        val barTop = (height - barHeight) / 2f
        val barRadius = barHeight / 2f
        val textLeft = centerX - textWidth / 2f
        val textRight = centerX + textWidth / 2f
        if (textLeft > paddingStart) {
            barRect.set(paddingStart.toFloat(), barTop, textLeft, barTop + barHeight)
            canvas.drawRoundRect(barRect, barRadius, barRadius, fillPaint)
        }
        if (width - paddingEnd > textRight) {
            barRect.set(textRight, barTop, width - paddingEnd.toFloat(), barTop + barHeight)
            canvas.drawRoundRect(barRect, barRadius, barRadius, trackPaint)
        }
        canvas.drawText(text, centerX, textY, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun computeThumbX(): Float {
        val width = (width - paddingStart - paddingEnd).toFloat()
        val progressRatio = (progress - min).toFloat() / (max - min)
        return paddingStart + width * progressRatio
    }

    /** 数值文本中心点：滑块位置，贴近两端时向内偏移使文本完整落在 padding 范围内 */
    private fun computeTextCenterX(textWidth: Float): Float {
        var centerX = computeThumbX()
        if (centerX - textWidth / 2f < paddingStart) {
            centerX = paddingStart + textWidth / 2f
        }
        if (centerX + textWidth / 2f > width - paddingEnd) {
            centerX = width - paddingEnd - textWidth / 2f
        }
        return centerX
    }

    private fun maxText(): String = displayText(max)

    /** thumb 为透明胶囊（宽度容纳最大数值文本），仅承载滑块定位 */
    private fun ensureThumb() {
        if (thumbDrawable != null) {
            return
        }
        val maxText = maxText()
        textPaint.getTextBounds(maxText, 0, maxText.length, textBounds)
        val viewHeight = height
        val drawable = ShapeDrawable(OvalShape()).apply {
            paint.color = Color.TRANSPARENT
            setIntrinsicHeight(viewHeight)
            setIntrinsicWidth(textBounds.width())
        }
        thumbDrawable = drawable
        thumb = drawable
    }
}
