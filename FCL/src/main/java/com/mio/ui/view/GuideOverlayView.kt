package com.mio.ui.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.mio.ui.applyCardElevation
import com.mio.ui.dialogCardBackground
import com.mio.util.GuideStep
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 功能引导全屏遮罩：暗化背景中在目标控件处挖圆角矩形高亮洞并绘制主题色描边
 * （带轻微呼吸动效），旁侧浮动卡片气泡展示描述文案、步骤指示与操作按钮。
 * 点击洞内或遮罩推进下一步，"跳过"结束剩余全部，由 GuideUtil 挂载到 decorView。
 */
class GuideOverlayView private constructor(context: Context) : FrameLayout(context) {

    private var steps: List<GuideStep> = emptyList()
    private var onStepShown: ((GuideStep) -> Unit)? = null
    private var onSkipped: ((List<GuideStep>) -> Unit)? = null
    private var index = 0

    private val scrimPaint = Paint().apply { color = SCRIM_COLOR }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ConvertUtils.dip2px(context, 2f).toFloat()
    }
    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private var holeRect: RectF? = null
    private val strokeRect = RectF()

    private lateinit var bubble: LinearLayout
    private lateinit var descView: TextView
    private lateinit var stepView: TextView
    private lateinit var skipButton: TextView
    private lateinit var nextButton: TextView

    /** 描边呼吸动效进度（0~1），控制描边外扩量与透明度 */
    private val pulseAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1200
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            val hole = holeRect ?: return@addUpdateListener
            strokeRect.set(hole)
            val expand = ConvertUtils.dip2px(context, 2f) * (it.animatedValue as Float)
            strokeRect.inset(-expand, -expand)
            // 只重绘描边周边区域，避免全屏反复重绘
            invalidate(
                (strokeRect.left - 8).toInt(), (strokeRect.top - 8).toInt(),
                (strokeRect.right + 8).toInt(), (strokeRect.bottom + 8).toInt(),
            )
        }
    }

    init {
        setWillNotDraw(false)
        // CLEAR 挖洞依赖离屏合成，软件层最稳妥；遮罩为静态绘制，开销可忽略
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        buildBubble()
        ThemeEngine.registerEvent(this) { refreshThemeColors() }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val hole = holeRect ?: return
        val save = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), scrimPaint)
        val r = ConvertUtils.dip2px(context, HOLE_CORNER_RADIUS_DP.toFloat()).toFloat()
        canvas.drawRoundRect(hole, r, r, clearPaint)
        canvas.restoreToCount(save)
        canvas.drawRoundRect(strokeRect, r, r, strokePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            advance()
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 旋转等尺寸变化后重新定位当前步骤
        if (w > 0 && h > 0 && steps.isNotEmpty()) {
            post { showStep(index, notify = false) }
        }
    }

    private fun start(steps: List<GuideStep>, onStepShown: (GuideStep) -> Unit, onSkipped: (List<GuideStep>) -> Unit) {
        this.steps = steps
        this.onStepShown = onStepShown
        this.onSkipped = onSkipped
        alpha = 0f
        animate().alpha(1f).setDuration(250).start()
        post { showStep(0) }
    }

    private fun showStep(i: Int, notify: Boolean = true) {
        index = i
        val step = steps[i]
        val target = step.target
        // 不可见的步骤静默跳过且不记录 tag（下次仍会展示）
        if (!target.isShown || target.width == 0 || target.height == 0) {
            if (i + 1 < steps.size) showStep(i + 1, notify) else dismiss()
            return
        }
        if (notify) onStepShown?.invoke(step)
        calculateHole(target)
        updateBubble(step)
        layoutBubble()
        pulseAnimator.start()
        invalidate()
    }

    private fun advance() {
        if (index + 1 < steps.size) showStep(index + 1) else dismiss()
    }

    private fun calculateHole(target: View) {
        val loc = IntArray(2)
        target.getLocationOnScreen(loc)
        val self = IntArray(2)
        getLocationOnScreen(self)
        val pad = ConvertUtils.dip2px(context, HOLE_PADDING_DP.toFloat()).toFloat()
        holeRect = RectF(
            loc[0] - self[0] - pad,
            loc[1] - self[1] - pad,
            loc[0] - self[0] + target.width + pad,
            loc[1] - self[1] + target.height + pad,
        )
        holeRect?.let { strokeRect.set(it) }
    }

    private fun buildBubble() {
        val density = resources.displayMetrics.density
        descView = TextView(context).apply {
            textSize = 14f
            setLineSpacing(ConvertUtils.dip2px(context, 2f).toFloat(), 1f)
        }
        stepView = TextView(context).apply { textSize = 12f }
        skipButton = textButton(context.getString(R.string.action_skip))
        nextButton = textButton(context.getString(R.string.button_next))
        nextButton.setOnClickListener { advance() }
        skipButton.setOnClickListener {
            onSkipped?.invoke(steps.drop(index + 1))
            dismiss()
        }
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(stepView, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            addView(skipButton)
            addView(nextButton, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                marginStart = ConvertUtils.dip2px(context, 4f)
            })
        }
        bubble = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val padding = ConvertUtils.dip2px(context, 16f)
            setPadding(padding, padding, padding, ConvertUtils.dip2px(context, 8f))
            applyCardElevation(this, density)
            addView(descView)
            addView(row, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = ConvertUtils.dip2px(context, 8f)
            })
        }
        addView(bubble, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.TOP or Gravity.START
        })
    }

    private fun updateBubble(step: GuideStep) {
        descView.text = step.text
        stepView.text = "${index + 1}/${steps.size}"
        nextButton.text = if (index == steps.size - 1) {
            context.getString(R.string.button_done)
        } else {
            context.getString(R.string.button_next)
        }
        // 步骤切换时气泡淡入并轻微上移，提供位置变化的连续感
        bubble.alpha = 0f
        bubble.translationY = ConvertUtils.dip2px(context, 8f).toFloat()
        bubble.animate().alpha(1f).translationY(0f).setDuration(200).start()
    }

    private fun layoutBubble() {
        val hole = holeRect ?: return
        val margin = ConvertUtils.dip2px(context, 16f)
        val gap = ConvertUtils.dip2px(context, 16f)
        // 固定卡片宽度，文字自动换行；高度以真实布局结果为准（手动 measure 与布局 pass 不一致）
        val bubbleWidth = (width - margin * 2).coerceAtMost(ConvertUtils.dip2px(context, BUBBLE_WIDTH_DP.toFloat()))
        val lp = bubble.layoutParams as LayoutParams
        if (lp.width != bubbleWidth) {
            lp.width = bubbleWidth
            bubble.requestLayout()
        }
        bubble.post {
            val bubbleHeight = bubble.height
            if (bubbleHeight <= 0) return@post
            // 水平对齐洞中心并 clamp 到屏幕内
            val x = (hole.centerX() - bubbleWidth / 2f)
                .coerceIn(margin.toFloat(), (width - bubbleWidth - margin).toFloat())
            // 垂直优先放洞下方，空间不足放上方，均放不下时取洞外较大空隙
            var y = hole.bottom + gap
            if (y + bubbleHeight > height - margin) {
                y = hole.top - gap - bubbleHeight
                if (y < margin) {
                    y = if (hole.top >= height - hole.bottom) margin.toFloat()
                    else (height - bubbleHeight - margin).toFloat()
                }
            }
            if (lp.leftMargin != x.toInt() || lp.topMargin != y.toInt()) {
                lp.leftMargin = x.toInt()
                lp.topMargin = y.toInt()
                bubble.requestLayout()
            }
        }
    }

    private fun refreshThemeColors() {
        val theme = ThemeEngine.getTheme()
        strokePaint.color = theme.opaqueLtColor
        // 气泡为固定底色（亮近白/暗深灰），文字按亮暗模式取反差色；
        // autoTint 与主题主色对比，浅色气泡上会得到白色文字导致看不清
        val dark = ThemeEngine.isNightMode(context)
        descView.setTextColor(if (dark) Color.WHITE else Color.BLACK)
        val hintColor = if (dark) 0x99FFFFFF.toInt() else 0x99000000.toInt()
        stepView.setTextColor(hintColor)
        skipButton.setTextColor(hintColor)
        nextButton.setTextColor(theme.opaqueColor)
        bubble.background = dialogCardBackground(context, resources.displayMetrics.density)
    }

    private fun dismiss() {
        pulseAnimator.cancel()
        animate().alpha(0f).setDuration(200).withEndAction {
            (parent as? ViewGroup)?.removeView(this)
            ThemeEngine.unregisterEvent(this)
        }.start()
    }

    private fun textButton(label: String): TextView {
        return TextView(context).apply {
            text = label
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            isClickable = true
            val padding = ConvertUtils.dip2px(context, 8f)
            setPadding(padding, padding / 2, padding, padding / 2)
            val value = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
            setBackgroundResource(value.resourceId)
        }
    }

    companion object {
        private const val SCRIM_COLOR = 0xB3000000.toInt()
        private const val HOLE_PADDING_DP = 8
        private const val HOLE_CORNER_RADIUS_DP = 12
        private const val BUBBLE_WIDTH_DP = 340

        /** 挂载到 Activity decorView 并开始展示引导步骤 */
        fun show(
            activity: Activity,
            steps: List<GuideStep>,
            onStepShown: (GuideStep) -> Unit,
            onSkipped: (List<GuideStep>) -> Unit,
        ) {
            val overlay = GuideOverlayView(activity)
            (activity.window.decorView as? ViewGroup)?.addView(
                overlay,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
            )
            overlay.start(steps, onStepShown, onSkipped)
        }
    }
}
