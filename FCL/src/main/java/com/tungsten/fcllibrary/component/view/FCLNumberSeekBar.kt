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

/** 数值滑条：轨道在数值文本两侧断开且断口圆角，点按数值弹出输入对话框精确设值，左右加减按钮步进 */
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

    /** 左右加减按钮图标（Material add/remove，与全局按钮图标同源），主题色随 refreshTheme 染色 */
    private val minusIcon = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_remove_24)?.mutate()
    private val plusIcon = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_add_24)?.mutate()

    /** 加减按钮底色画笔：常态为主题色（与填充轨道一致），按压为亮主题色 */
    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ThemeEngine.getTheme().dkColor
    }
    private val buttonPressedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ThemeEngine.getTheme().ltColor
    }

    /** 本次触摸起始命中的按钮（0 非按钮 / ZONE_MINUS / ZONE_PLUS），命中后整段手势由按钮消费，不再进入拖动与文本点按 */
    private var touchZone = 0

    /** 当前呈按压视觉状态的按钮（滑出热区即释放，滑回恢复并续发长按连发） */
    private var pressedZone = 0

    /** 长按连发：按压期间按固定间隔重复步进 */
    private val stepRunnable = object : Runnable {
        override fun run() {
            if (pressedZone != 0) {
                step(pressedZone)
                postDelayed(this, REPEAT_INTERVAL_MS)
            }
        }
    }

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
        // 左右各预留加减按钮区域：强制最小水平内边距，保证轨道 / 数值文本与按钮不重叠，
        // 滑块定位与文本钳制（computeThumbX / computeTextCenterX）同样基于 padding，随之一致内移
        val minPadding = (MIN_H_PADDING_DP * resources.displayMetrics.density).toInt()
        setPadding(maxOf(paddingLeft, minPadding), paddingTop, maxOf(paddingRight, minPadding), paddingBottom)
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
        buttonPaint.color = theme.dkColor
        buttonPressedPaint.color = theme.ltColor
        minusIcon?.setTint(theme.autoTint)
        plusIcon?.setTint(theme.autoTint)
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
        drawButtons(canvas)
    }

    /** 绘制左右加减按钮：主题色圆形底 + Material 加减图标（对比色随数值文本），按压亮主题色，禁用半透明。
     *  圆底右/左缘锚定在轨道起点/终点内侧留 GAP 间隔，保证与轨道紧凑相邻 */
    private fun drawButtons(canvas: Canvas) {
        val diameter = maxOf(0f, minOf(BUTTON_SIZE_DP * resources.displayMetrics.density, height - 2f * resources.displayMetrics.density))
        val radius = diameter / 2f
        val iconSize = diameter * BUTTON_ICON_RATIO
        val gap = GAP_DP * resources.displayMetrics.density
        val cy = height / 2f
        val alpha = if (isEnabled) 255 else DISABLED_ALPHA
        buttonPaint.alpha = alpha
        buttonPressedPaint.alpha = alpha
        minusIcon?.alpha = alpha
        plusIcon?.alpha = alpha
        val buttons = listOf(
            Triple(ZONE_MINUS, paddingStart - gap - radius, minusIcon),
            Triple(ZONE_PLUS, width - paddingEnd + gap + radius, plusIcon),
        )
        for ((zoneId, cx, icon) in buttons) {
            canvas.drawCircle(cx, cy, radius, if (pressedZone == zoneId) buttonPressedPaint else buttonPaint)
            icon?.let {
                val left = (cx - iconSize / 2f).roundToInt()
                val top = (cy - iconSize / 2f).roundToInt()
                it.setBounds(left, top, left + iconSize.roundToInt(), top + iconSize.roundToInt())
                it.draw(canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (handleButtonTouch(event)) {
            return true
        }
        if (gestureDetector.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    /** 命中检测：x 落在轨道起点左侧为减、终点右侧为加（该区域无轨道与文本，与文本点按热区天然不相交），否则 0 */
    private fun hitZone(x: Float): Int = when {
        x < paddingStart -> ZONE_MINUS
        x > width - paddingEnd -> ZONE_PLUS
        else -> 0
    }

    /**
     * 加减按钮触控：按下立即步进一次，长按按 REPEAT_DELAY_MS 起以固定间隔连发；
     * 滑出热区释放按压并暂停连发，滑回恢复；禁用时不拦截。返回 true 表示事件已被按钮消费
     */
    private fun handleButtonTouch(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchZone = if (isEnabled) hitZone(event.x) else 0
                if (touchZone == 0) {
                    return false
                }
                pressedZone = touchZone
                step(touchZone)
                startRepeat()
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchZone == 0) {
                    return false
                }
                val inside = hitZone(event.x) == touchZone
                if (inside && pressedZone == 0) {
                    pressedZone = touchZone
                    startRepeat()
                } else if (!inside && pressedZone != 0) {
                    pressedZone = 0
                    stopRepeat()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (touchZone == 0) {
                    return false
                }
                stopRepeat()
                pressedZone = 0
                touchZone = 0
            }
            else -> return touchZone != 0
        }
        invalidate()
        return true
    }

    /** 加减步进：一次一个显示单位（进度按 scale 倍存储时步长即 scale），结果钳制在量程内 */
    private fun step(zone: Int) {
        progress = (progress + zone * scale).coerceIn(min, max)
    }

    private fun startRepeat() {
        removeCallbacks(stepRunnable)
        postDelayed(stepRunnable, REPEAT_DELAY_MS)
    }

    private fun stopRepeat() {
        removeCallbacks(stepRunnable)
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(stepRunnable)
        super.onDetachedFromWindow()
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

    private companion object {
        /** 最小水平内边距（dp）：容纳圆底直径上限 + 间隔 + 少量外侧留白，同时是加减按钮的触控热区宽度 */
        const val MIN_H_PADDING_DP = 32f

        /** 按钮圆形底直径上限（dp），实际按控件高度收缩（高度 - 2dp） */
        const val BUTTON_SIZE_DP = 26f

        /** 按钮图标边长占圆形底直径的比例 */
        const val BUTTON_ICON_RATIO = 0.7f

        /** 圆底与轨道之间的间隔（dp） */
        const val GAP_DP = 2f

        /** 长按连发起始延迟与重复间隔（毫秒） */
        const val REPEAT_DELAY_MS = 400L
        const val REPEAT_INTERVAL_MS = 100L

        /** 禁用态按钮透明度（0~255） */
        const val DISABLED_ALPHA = 102

        /** 按钮热区标识 */
        const val ZONE_MINUS = -1
        const val ZONE_PLUS = 1
    }
}
