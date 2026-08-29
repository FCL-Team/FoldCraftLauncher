package com.mio.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.ConvertUtils
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sin

/**
 * Material 3 风格的波浪形线性进度指示器：整条进度线是一条几何连续的流动波浪，
 * 已完成区段以次要主题色全振幅描边、未完成区段为低振幅浅色 track 波，
 * 两段在进度交界处平滑过渡；不确定态（进度为负）为全宽流动波浪。
 * 仅作视觉效果与开关（点击行为由外部设置，打开下载面板）。
 */
class WaveProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    /** 0..1；负值表示不确定进度 */
    private var progress = 0f

    /** 参与绘制的进度：逐帧向目标 progress 缓动，避免进度跳变生硬 */
    private var animatedProgress = 0f
    private var phase = 0f

    private val activePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = ConvertUtils.dip2px(context, 5f).toFloat()
    }
    private val trackPaint = Paint(activePaint).apply {
        alpha = 80
    }
    private val path = Path()

    private val animator = ValueAnimator.ofFloat(0f, (2 * PI).toFloat()).apply {
        duration = 1600
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            phase = it.animatedValue as Float
            // 进度平滑过渡：绘制用进度逐帧向目标靠近
            animatedProgress += (progress.coerceIn(0f, 1f) - animatedProgress) * 0.12f
            invalidate()
        }
    }

    init {
        ThemeEngine.getInstance().registerEvent(this, ::refreshTheme)
        refreshTheme()
    }

    private fun refreshTheme() {
        val theme = ThemeEngine.getInstance().getTheme()
        activePaint.color = theme.getColor2()
        trackPaint.color = theme.getColor2()
        trackPaint.alpha = 70
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }

    /** 更新进度（0..1，负值表示不确定） */
    fun setProgress(value: Float) {
        progress = value.coerceIn(-1f, 1f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        if (w <= 0 || height <= 0) return

        val centerY = height / 2f
        val amplitude = height * 0.32f
        val trackAmplitude = amplitude * 0.25f
        val wavelength = w / 2.5f

        if (progress < 0f) {
            // 不确定态：全宽波浪持续流动
            canvas.drawPath(buildWave(0f, w, centerY, { amplitude }, wavelength), activePaint)
            return
        }

        val fillRatio = animatedProgress.coerceIn(0f, 1f)
        val progressX = w * fillRatio
        val gap = wavelength * 0.4f

        // 振幅沿 x 连续：progressX 左侧全幅（活跃）、右侧低幅（track）、交界处平滑过渡，
        // 因此两段独立描边在交界处几何重合，不会断开；圆帽使 active 波两端均为圆头；
        // 靠近条带左右两端的位置振幅逐渐收敛（M3 波浪的端点呼吸）
        fun amplitudeAt(x: Float): Float {
            val base = when {
                x < progressX - gap / 2 -> amplitude
                x > progressX + gap / 2 -> trackAmplitude
                else -> amplitude + (trackAmplitude - amplitude) *
                        ((x - (progressX - gap / 2)) / gap).coerceIn(0f, 1f)
            }
            val tail = wavelength * 0.45f
            return base * min(x / tail, (w - x) / tail).coerceIn(0f, 1f)
        }

        if (fillRatio < 1f) {
            canvas.drawPath(
                buildWave(progressX, w, centerY, ::amplitudeAt, wavelength),
                trackPaint
            )
        }
        if (fillRatio > 0f) {
            canvas.drawPath(
                buildWave(0f, progressX, centerY, ::amplitudeAt, wavelength),
                activePaint
            )
        }
    }

    /** 构建 [fromX, toX] 区间的波浪路径，每个采样点的振幅由 [amplitudeAt] 给出 */
    private fun buildWave(
        fromX: Float,
        toX: Float,
        centerY: Float,
        amplitudeAt: (Float) -> Float,
        wavelength: Float
    ): Path {
        val path = Path()
        var x = fromX
        var first = true
        while (x <= toX || first) {
            val y = centerY +
                    amplitudeAt(x) * sin(2.0 * PI * x / wavelength - phase).toFloat()
            if (first) {
                path.moveTo(x, y)
                first = false
            } else {
                path.lineTo(x, y)
            }
            if (x >= toX) break
            x += 8f
        }
        return path
    }
}