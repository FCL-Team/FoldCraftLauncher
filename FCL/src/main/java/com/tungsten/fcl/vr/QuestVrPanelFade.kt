package com.tungsten.fcl.vr

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.tungsten.fcl.activity.QuestVrActivity

/** VR/平面面板切换时的黑幕淡入淡出动画（可经设置关闭） */
internal object QuestVrPanelFade {
    private const val TAG = "com.tungsten.fcl.vr.transition"
    private const val FADE_OUT_MS = 180L
    private const val FADE_IN_MS = 220L

    fun fadeOut(activity: Activity, enabled: Boolean, afterFade: Runnable) {
        val content = content(activity)
        if (!enabled || content == null || activity is QuestVrActivity) {
            clear(content)
            afterFade.run()
            return
        }
        clear(content)
        val veil = veil(content)
        veil.fade(0f, 1f, FADE_OUT_MS) {
            if (veil.parent === content && !activity.isFinishing && !activity.isDestroyed) afterFade.run()
        }
    }

    fun fadeIn(activity: Activity, enabled: Boolean) {
        val content = content(activity) ?: return
        clear(content)
        if (!enabled) return
        val veil = veil(content)
        veil.fade(1f, 0f, FADE_IN_MS) { content.removeView(veil) }
    }

    private fun content(activity: Activity): ViewGroup? {
        if (activity.isFinishing || activity.isDestroyed) return null
        return activity.findViewById(android.R.id.content) as? ViewGroup
    }

    private fun veil(content: ViewGroup): Veil {
        val veil = Veil(content.context)
        veil.tag = TAG
        veil.setBackgroundColor(Color.BLACK)
        veil.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        content.addView(veil, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return veil
    }

    private fun clear(content: ViewGroup?) {
        val existing = content?.findViewWithTag<View>(TAG) ?: return
        (existing as? Veil)?.cancelFade()
        content.removeView(existing)
    }

    private class Veil(context: Context) : View(context) {
        private var animator: ValueAnimator? = null
        private var cancelled = false

        fun fade(from: Float, to: Float, duration: Long, finished: Runnable) {
            alpha = from
            animator = ValueAnimator.ofFloat(from, to).apply {
                setDuration(duration)
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { value -> alpha = value.animatedValue as Float }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationCancel(animation: Animator) {
                        cancelled = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (!cancelled) finished.run()
                    }
                })
                start()
            }
        }

        fun cancelFade() {
            cancelled = true
            animator?.cancel()
        }

        override fun onDetachedFromWindow() {
            cancelFade()
            super.onDetachedFromWindow()
        }
    }
}
