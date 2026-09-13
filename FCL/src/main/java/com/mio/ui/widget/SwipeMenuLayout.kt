package com.mio.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * 左滑菜单容器（可复用）：第一个子 View 为菜单层（垫底，靠右），第二个子 View 为内容层（在上）。
 * 内容层右往左滑动（translationX 0 → -menuWidth）露出右侧菜单；松手按位移与速度吸附开/合。
 * 打开时点按内容仅关闭菜单、不触发内容点击；水平拖动期间请求父级（RecyclerView）不拦截触摸。
 * 菜单层为通用子 View，后续新增操作直接往菜单层加按钮即可。
 */
class SwipeMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    interface OnMenuStateChangeListener {
        fun onMenuOpened()
        fun onMenuClosed()
    }

    var onMenuStateChangeListener: OnMenuStateChangeListener? = null

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val minFlingVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity

    /** 打开状态下内容层与菜单层之间的间隔 */
    private val menuGap = resources.displayMetrics.density * 8f

    /** 菜单完全打开时内容层的位移目标 */
    private val openOffset: Float
        get() = if (menuWidth <= 0) 0f else -(menuWidth + menuGap)

    private var velocityTracker: VelocityTracker? = null
    private var downX = 0f
    private var downY = 0f
    private var startTranslation = 0f
    private var dragging = false
    private var menuOpen = false

    private val menuView: View?
        get() = if (childCount > 1) getChildAt(0) else null

    private val contentView: View?
        get() = if (childCount > 1) getChildAt(1) else null

    private val menuWidth: Int
        get() = menuView?.measuredWidth ?: 0

    fun isMenuOpen(): Boolean = menuOpen

    override fun onFinishInflate() {
        super.onFinishInflate()
        // 菜单层垫底，内容层在上
        if (childCount > 1) {
            getChildAt(1).bringToFront()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 重布局后把内容层位置夹回有效区间（回收复用/动画中途布局的情况）
        contentView?.let { it.translationX = if (menuOpen) openOffset else 0f }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                startTranslation = contentView?.translationX ?: 0f
                dragging = false
                contentView?.animate()?.cancel()
                if (velocityTracker == null) velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(ev)
                // 菜单打开时仅接管内容区域的触摸（点按内容只关闭菜单，不透传点击）；
                // 露出的菜单条区域不拦截，保证菜单按钮（如收藏）可正常点击
                if (menuOpen && ev.x < width - menuWidth) {
                    requestParentDisallowIntercept(true)
                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.addMovement(ev)
                val dx = ev.x - downX
                val dy = ev.y - downY
                if (abs(dx) > touchSlop && abs(dx) > abs(dy)) {
                    dragging = true
                    requestParentDisallowIntercept(true)
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (!menuOpen && !dragging) return false
                downX = ev.x
                startTranslation = contentView?.translationX ?: 0f
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.addMovement(ev)
                if (dragging || menuOpen) {
                    dragging = true
                    contentView?.translationX = clampTranslation(startTranslation + (ev.x - downX))
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                requestParentDisallowIntercept(false)
                if (dragging) {
                    velocityTracker?.addMovement(ev)
                    velocityTracker?.computeCurrentVelocity(1000)
                    val velocityX = velocityTracker?.xVelocity ?: 0f
                    settle(velocityX)
                    dragging = false
                } else if (ev.actionMasked == MotionEvent.ACTION_UP && menuOpen) {
                    // 点按已打开的内容区域：仅关闭菜单
                    closeMenu()
                }
                releaseTracker()
            }
        }
        return true
    }

    private fun clampTranslation(offset: Float): Float =
        if (openOffset >= 0f) 0f else offset.coerceIn(openOffset, 0f)

    private fun settle(velocityX: Float) {
        val open = when {
            abs(velocityX) > minFlingVelocity -> velocityX < 0
            else -> -(contentView?.translationX ?: 0f) > -openOffset / 2f
        }
        animateTo(if (open) openOffset else 0f)
    }

    fun openMenu() {
        animateTo(openOffset)
    }

    fun closeMenu() {
        animateTo(0f)
    }

    private fun animateTo(target: Float) {
        val content = contentView ?: return
        content.animate().cancel()
        val opening = target < 0f && !menuOpen
        val closing = target == 0f && menuOpen
        content.animate()
            .translationX(target)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                private var cancelled = false

                override fun onAnimationCancel(animation: Animator) {
                    cancelled = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (cancelled) return
                    menuOpen = target < 0f
                    if (opening) onMenuStateChangeListener?.onMenuOpened()
                    if (closing) onMenuStateChangeListener?.onMenuClosed()
                }
            })
            .start()
    }

    private fun requestParentDisallowIntercept(disallow: Boolean) {
        parent?.requestDisallowInterceptTouchEvent(disallow)
    }

    private fun releaseTracker() {
        velocityTracker?.recycle()
        velocityTracker = null
    }
}
