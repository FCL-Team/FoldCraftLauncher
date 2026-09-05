package com.mio.skin

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogAnimationSwitchBinding
import com.tungsten.fcl.databinding.ItemAnimationBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.theme.ThemeEngine

/** 动画选中回调（SAM 接口，便于 Java 侧 lambda 调用） */
fun interface OnAnimationSelectedListener {
    fun onSelected(animation: PlayerAnimation)
}

/**
 * 动画切换弹窗：列出全部支持的动画并标记当前项，点击即切换并关闭。
 */
class AnimationDialog(
    context: Context,
    private val current: PlayerAnimation?,
    private val onSelected: OnAnimationSelectedListener
) : FCLDialog(context) {

    private val binding = DialogAnimationSwitchBinding.inflate(layoutInflater)
    private val themeColor = ThemeEngine.getInstance().getTheme().getColor()
    private val density = context.resources.displayMetrics.density

    init {
        setContentView(binding.root)
        setCancelable(true)

        val factories = listOf(
            R.string.animation_idle to { IdleAnimation() },
            R.string.animation_walking to { WalkingAnimation() },
            R.string.animation_running to { RunningAnimation() },
            R.string.animation_flying to { FlyingAnimation() },
            R.string.animation_swim to { SwimAnimation() },
            R.string.animation_crouch to { CrouchAnimation() },
            R.string.animation_wave to { WaveAnimation() },
            R.string.animation_hit to { HitAnimation() }
        )
        factories.forEach { (nameRes, factory) ->
            val animation = factory()
            val selected = current != null && animation::class == current::class
            binding.container.addView(createRow(nameRes, animation, selected), rowParams())
        }
    }

    private fun createRow(nameRes: Int, animation: PlayerAnimation, selected: Boolean): View {
        val row = ItemAnimationBinding.inflate(layoutInflater)
        row.text.setText(nameRes)
        row.root.setOnClickListener {
            onSelected.onSelected(animation)
            dismiss()
        }
        row.root.background = if (selected) {
            // 当前动画：主题色圆角底 + 勾选图标
            row.check.setColorFilter(themeColor)
            row.check.visibility = View.VISIBLE
            GradientDrawable().apply {
                cornerRadius = 10 * density
                setColor(ColorUtils.setAlphaComponent(themeColor, 30))
            }
        } else {
            // 普通行：卡片色以对话框背景为基准（资源按亮暗模式解析），暗色下仅微亮避免刺眼
            GradientDrawable().apply {
                cornerRadius = 10 * density
                val dialogColor = ContextCompat.getColor(context, R.color.dialog_background)
                val blend = if (ThemeEngine.isNightMode(context)) 0.07f else 0.65f
                setColor(ColorUtils.blendARGB(dialogColor, Color.WHITE, blend))
            }
        }
        return row.root
    }

    private fun rowParams(): LinearLayoutCompat.LayoutParams {
        val params = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topMargin = (6 * density).toInt()
        return params
    }

    override fun show() {
        window?.setLayout(getScreenWidth() / 2, WindowManager.LayoutParams.WRAP_CONTENT)
        super.show()
    }
}
