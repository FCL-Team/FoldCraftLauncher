package com.tungsten.fcl.vr

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ScrollView

/**
 * VR 启动日志滚动视图：跟随尾部自动滚动，用户上滑即暂停跟随，
 * 下滑到底或显式 resumeFollowing 后恢复跟随。
 */
class QuestVrLogScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.scrollViewStyle,
) : ScrollView(context, attrs, defStyleAttr) {

    fun interface OnFollowTailChangedListener {
        fun onFollowTailChanged(followingTail: Boolean)
    }

    private var followingTail = true
    private var handlingUserScroll = false
    private var userFling = false
    private var followTailChangedListener: OnFollowTailChangedListener? = null

    // 布局后回落尾部：内容追加与视图尺寸变化都不打断跟随状态
    private val followAfterLayout = ViewTreeObserver.OnPreDrawListener {
        if (followingTail && isShown && !isLayoutRequested) {
            scrollTo(scrollX, tailBottom())
        }
        true
    }

    init {
        isSmoothScrollingEnabled = false
        overScrollMode = OVER_SCROLL_NEVER
    }

    val isFollowingTail: Boolean get() = followingTail

    fun setOnFollowTailChangedListener(listener: OnFollowTailChangedListener?) {
        followTailChangedListener = listener
    }

    fun resumeFollowing() {
        userFling = false
        fling(0)
        setFollowingTail(true)
        invalidate()
    }

    private fun setFollowingTail(following: Boolean) {
        if (followingTail == following) return
        followingTail = following
        if (following) userFling = false
        val listener = followTailChangedListener ?: return
        val previous = handlingUserScroll
        handlingUserScroll = false
        try {
            listener.onFollowTailChanged(following)
        } finally {
            handlingUserScroll = previous
        }
    }

    private fun tailBottom(): Int {
        if (childCount == 0) return 0
        val content = getChildAt(0)
        return maxOf(0, content.bottom + paddingBottom - height)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnPreDrawListener(followAfterLayout)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val observer = viewTreeObserver
        observer.removeOnPreDrawListener(followAfterLayout)
        observer.addOnPreDrawListener(followAfterLayout)
    }

    override fun onDetachedFromWindow() {
        viewTreeObserver.removeOnPreDrawListener(followAfterLayout)
        super.onDetachedFromWindow()
    }

    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(x, y, oldX, oldY)
        if (!handlingUserScroll) return
        if (y < oldY) setFollowingTail(false)
        else if (y > oldY && y >= tailBottom()) setFollowingTail(true)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) userFling = false
        return super.dispatchTouchEvent(event)
    }

    /** 仅在用户手势/滚轮/按键/无障碍滚动期间判定跟随状态变化 */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val previous = handlingUserScroll
        handlingUserScroll = event.actionMasked != MotionEvent.ACTION_DOWN
        try {
            return super.onTouchEvent(event)
        } finally {
            handlingUserScroll = previous
        }
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val previous = handlingUserScroll
        handlingUserScroll = event.actionMasked == MotionEvent.ACTION_SCROLL
        try {
            return super.onGenericMotionEvent(event)
        } finally {
            handlingUserScroll = previous
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val previous = handlingUserScroll
        val key = event.keyCode
        handlingUserScroll = event.action == KeyEvent.ACTION_DOWN && when (key) {
            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_PAGE_UP, KeyEvent.KEYCODE_PAGE_DOWN,
            KeyEvent.KEYCODE_MOVE_HOME, KeyEvent.KEYCODE_MOVE_END,
            KeyEvent.KEYCODE_SPACE -> true
            else -> false
        }
        try {
            return super.dispatchKeyEvent(event)
        } finally {
            handlingUserScroll = previous
        }
    }

    override fun performAccessibilityAction(action: Int, arguments: Bundle?): Boolean {
        val scroll = action == AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                || action == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                || action == AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP.id
                || action == AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN.id
        if (scroll) userFling = true
        val previous = handlingUserScroll
        handlingUserScroll = scroll
        try {
            val handled = super.performAccessibilityAction(action, arguments)
            if (scroll && !handled) userFling = false
            return handled
        } finally {
            handlingUserScroll = previous
        }
    }

    override fun fling(velocityY: Int) {
        userFling = handlingUserScroll && (!followingTail || velocityY < 0)
        super.fling(velocityY)
    }

    override fun computeScroll() {
        val previous = handlingUserScroll
        handlingUserScroll = userFling
        try {
            super.computeScroll()
        } finally {
            handlingUserScroll = previous
        }
    }
}
