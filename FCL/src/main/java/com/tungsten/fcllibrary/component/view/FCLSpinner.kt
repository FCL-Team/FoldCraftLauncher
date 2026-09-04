package com.tungsten.fcllibrary.component.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RotateDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 自定义下拉选择控件（替代原 AppCompatSpinner 包装版 FCLSpinner）。
 *
 * 箭头指示器为随控件绘制的 compound drawable，不再依赖框架 Spinner 背景与内部弹窗，
 * 修复可滑动页面（ScrollView/RecyclerView）中指示器错位、随滚动抖动的问题；
 * 下拉为 ListPopupWindow 独立弹窗，只在用户点选时回调，
 * 程序性 [setItems]/[setSelection] 永不触发监听，无伪回调，列表行复用无需清监听。
 */
class FCLSpinner<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {

    /** 用户点选回调（程序性 setSelection 不会触发） */
    fun interface OnItemSelectedListener<T> {
        fun onItemSelected(index: Int, item: T)
    }

    private var items: List<T> = emptyList()
    private var selectedIndex = -1
    private var listener: OnItemSelectedListener<T>? = null

    /** 文字随主题 autoTint（与主色对比的黑/白）；白底对话框场景应关闭以继承默认文字色 */
    private var autoTextTint = true

    private val arrow: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_drop_down_24)!!.mutate()

    /** RotateDrawable 包裹箭头以支持 level 驱动的旋转过渡（5000 = 180°） */
    private val arrowWrapper = RotateDrawable().apply { setDrawable(arrow) }

    private var arrowAnimator: ValueAnimator? = null

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.FCLSpinner)
            autoTextTint = typedArray.getBoolean(R.styleable.FCLSpinner_auto_text_tint, true)
            typedArray.recycle()
        }
        gravity = Gravity.CENTER_VERTICAL
        textSize = 14f
        setSingleLine(true)
        ellipsize = TextUtils.TruncateAt.END
        val padding = ConvertUtils.dip2px(context, 8f)
        setPadding(padding, padding, padding, padding)
        compoundDrawablePadding = ConvertUtils.dip2px(context, 2f)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrowWrapper, null)
        if (background == null) {
            // 布局未指定背景时套系统涟漪，保留按压反馈
            val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
            setBackgroundResource(typedArray.getResourceId(0, 0))
            typedArray.recycle()
        }
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme)
        refreshTheme()
        setOnClickListener { showPopup() }
    }

    /** 主题刷新：开启时文字取与主色对比色、箭头取半透明对比色；
     *  关闭时文字继承布局/主题默认色，箭头跟随当前文字色 */
    private fun refreshTheme() {
        if (autoTextTint) {
            setTextColor(ThemeEngine.getTheme().autoTint)
            arrow.setTint(ThemeEngine.getTheme().autoHintTint)
        } else {
            arrow.setTint(textColors.defaultColor)
        }
    }

    /** 设置文字是否随主题着色（默认开），立即生效 */
    fun setAutoTextTint(autoTextTint: Boolean) {
        this.autoTextTint = autoTextTint
        refreshTheme()
    }

    private fun showPopup() {
        if (items.isEmpty()) return
        val popup = ListPopupWindow(context)
        popup.setAdapter(ArrayAdapter(context, R.layout.item_spinner_dropdown, items))
        popup.setAnchorView(this)
        // 弹窗底色取当前亮暗模式的主色，与条目背景（FCLCheckedTextView 主题化）一致
        popup.setBackgroundDrawable(ColorDrawable(ThemeEngine.getTheme().color))
        popup.isModal = true
        // 弹窗过渡：自 anchor 下落淡入，收回上滑淡出（点击外部关闭同样生效）
        popup.setAnimationStyle(R.style.FCLSpinnerPopupAnimation)
        popup.setOnDismissListener { animateArrow(false) }
        popup.setOnItemClickListener { _, _, position, _ ->
            popup.dismiss()
            selectedIndex = position
            refreshText()
            items.getOrNull(position)?.let { listener?.onItemSelected(position, it) }
        }
        popup.show()
        animateArrow(true)
    }

    /** 箭头旋转过渡：展开转 180° 朝上，收起转回（level 0..5000 映射 0..180°） */
    private fun animateArrow(expanded: Boolean) {
        arrowAnimator?.cancel()
        arrowAnimator = ValueAnimator.ofInt(arrowWrapper.level, if (expanded) 5000 else 0).apply {
            duration = 200
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                arrowWrapper.level = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        arrowAnimator?.cancel()
        arrowAnimator = null
    }

    private fun refreshText() {
        text = items.getOrNull(selectedIndex)?.toString().orEmpty()
    }

    /** 设置条目（内部持有副本），选中项重置为第 0 项，不触发监听 */
    fun setItems(items: List<T>?) {
        this.items = items.orEmpty().toList()
        selectedIndex = if (this.items.isEmpty()) -1 else 0
        refreshText()
    }

    fun getItems(): List<T> = items

    /** 程序性选中，只更新显示，不触发监听 */
    fun setSelection(index: Int) {
        selectedIndex = index.coerceIn(-1, items.lastIndex)
        refreshText()
    }

    fun getSelectedIndex(): Int = selectedIndex

    fun getSelectedItem(): T? = items.getOrNull(selectedIndex)

    fun setOnItemSelectedListener(listener: OnItemSelectedListener<T>?) {
        this.listener = listener
    }
}
