package com.tungsten.fcl.ui.version

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.mio.cache.VersionCache
import com.mio.ui.applySelectableItemStyle
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.tungsten.fcllibrary.util.ConvertUtils

/**
 * 长按切换实例按钮弹出的快速切换/启动下拉（锚定在实例名上的小窗口）：
 * 每行展示实例图标、实例名与 MC/加载器版本，行点击切换当前选中实例，
 * 行尾启动按钮直接启动该实例且不改变当前选中。
 */
class VersionQuickSwitchPopup(
    private val context: Context,
    private val anchor: View,
    private val profile: Profile,
    private val entries: List<VersionCache.Entry>,
    private val onSwitch: (String) -> Unit,
    private val onLaunch: (String) -> Unit,
) {

    private val density = context.resources.displayMetrics.density
    private val popup = PopupWindow(context)

    /** 弹窗与条目半透明程度（0xB3 ≈ 70%） */
    private val panelAlpha = 0xB3

    fun show() {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dip(4), dip(4), dip(4), dip(4))
        }
        entries.forEachIndexed { index, entry ->
            val row = buildRow(entry)
            if (index > 0) {
                (row.layoutParams as LinearLayout.LayoutParams).topMargin = dip(6)
            }
            container.addView(row)
        }

        val scroll = ScrollView(context).apply {
            addView(
                container,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // 宽度不小于 220dp，保证 MC/加载器版本可读；高度按内容、上限半屏
        val width = maxOf(anchor.width.takeIf { it > 0 } ?: 0, dip(220))
        container.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val maxHeight = (context.resources.displayMetrics.heightPixels * 0.5f).toInt()
        val height = container.measuredHeight.coerceAtMost(maxHeight)

        popup.contentView = scroll
        popup.width = width
        popup.height = height
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.elevation = dip(8).toFloat()
        popup.setBackgroundDrawable(GradientDrawable().apply {
            setColor(
                ColorUtils.setAlphaComponent(
                    ContextCompat.getColor(context, R.color.dialog_background),
                    panelAlpha
                )
            )
            cornerRadius = 10 * density
        })
        // 向左偏移一点，避免贴住右边缘
        popup.showAsDropDown(anchor, -dip(8), dip(4))
    }

    private fun buildRow(entry: VersionCache.Entry): View {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dip(8), dip(6), dip(4), dip(6))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            isClickable = true
            isFocusable = true
            setOnClickListener {
                dismiss()
                onSwitch(entry.id)
            }
        }
        // 选中项主题色高亮，其余为无描边卡片（与选择类弹窗一致），并整体半透明
        val selected = entry.id == profile.selectedVersion
        applySelectableItemStyle(context, row, null, selected, density)
        if (!selected) {
            row.background.alpha = panelAlpha
        }

        // 实例图标（与实例列表页一致：drawable 作背景铺满）
        row.addView(AppCompatImageView(context).apply {
            setBackgroundDrawable(entry.newIcon())
            layoutParams = LinearLayout.LayoutParams(dip(30), dip(30)).apply { marginEnd = dip(8) }
        })

        // 实例名 + MC/加载器版本（副标题格式与实例列表页一致）
        row.addView(LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            addView(FCLTextView(context).apply {
                text = entry.id
                textSize = 14f
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
            })
            addView(FCLTextView(context).apply {
                text = entry.libraries
                textSize = 11f
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
            })
        })

        row.addView(FCLImageButton(context).apply {
            setImageResource(R.drawable.ic_baseline_play_arrow_24)
            setUseThemeColor(true)
            setNoPadding(true)
            contentDescription = context.getString(R.string.version_launch)
            layoutParams = LinearLayout.LayoutParams(dip(38), dip(38)).apply { marginStart = dip(6) }
            setOnClickListener {
                dismiss()
                onLaunch(entry.id)
            }
        })
        return row
    }

    private fun dismiss() {
        popup.dismiss()
    }

    private fun dip(dp: Int): Int = ConvertUtils.dip2px(context, dp.toFloat())
}
