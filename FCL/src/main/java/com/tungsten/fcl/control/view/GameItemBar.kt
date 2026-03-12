package com.tungsten.fcl.control.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fcl.setting.GameOption
import com.tungsten.fcl.setting.GameOption.GameOptionListener
import com.tungsten.fclauncher.keycodes.FCLKeycodes
import com.tungsten.fclauncher.keycodes.MinecraftKeyBindingMapper

class GameItemBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var gameMenu: GameMenu
    private lateinit var gameOption: GameOption
    var optionListener: GameOptionListener? = null
        private set
    private var position = 0
    private var lastClickTime = 0L
    private var lastClickPosition = 0

    private var restoreBackgroundRunnable: Runnable? = null

    init {
        isClickable = true
        isFocusable = true
    }

    fun notifySize(size: Int) {
        post {
            val params = layoutParams
            params.width = size * 9
            params.height = size
            setLayoutParams(params)
        }
    }

    fun setup(gameMenu: GameMenu, gameOption: GameOption) {
        this.gameMenu = gameMenu
        this.gameOption = gameOption
        val width =
            (gameMenu.touchPad.width * gameMenu.bridge!!.scaleFactor).toInt()
        val height =
            (gameMenu.touchPad.height * gameMenu.bridge!!.scaleFactor).toInt()
        optionListener = GameOptionListener { manually: Boolean ->
            notifySize(
                if (gameMenu.menuSetting.itemBarScale == 0) gameOption.getGuiScale(
                    width,
                    height,
                    0
                ) * 20 else gameMenu.menuSetting.itemBarScale
            )
            if (manually) {
                restoreBackgroundRunnable?.let {
                    removeCallbacks(it)
                }
                setBackgroundColor(-0x7f010000)
                restoreBackgroundRunnable = Runnable {
                    setBackgroundColor(0x00000000)
                    restoreBackgroundRunnable = null
                }
                postDelayed(restoreBackgroundRunnable, 1500)
            }
        }
        optionListener!!.onOptionChanged(false)
        gameOption.addGameOptionListener(optionListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val height = height
                if (event.x >= 0 && event.x <= 9 * height) {
                    position = ((event.x / height).toInt() + 1).coerceIn(1, 9)
                    sendHotbarKey(position, true)
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime < 200 && position == lastClickPosition) {
                        swapHands()
                        lastClickTime = 0
                        lastClickPosition = 0
                    } else {
                        lastClickTime = currentTime
                        lastClickPosition = position
                    }
                }
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (position in 1..9) {
                    sendHotbarKey(position, false)
                }
                position = 0
            }

            else -> {}
        }
        return true
    }

    private fun swapHands() {
        gameMenu.input.sendBoundKeyEvent(
            gameOption,
            MinecraftKeyBindingMapper.BINDING_SWAP_HANDS,
            FCLKeycodes.KEY_F,
            true
        )
        gameMenu.input.sendBoundKeyEvent(
            gameOption,
            MinecraftKeyBindingMapper.BINDING_SWAP_HANDS,
            FCLKeycodes.KEY_F,
            false
        )
    }

    private fun sendHotbarKey(position: Int, press: Boolean) {
        val bindings = arrayOf(
            MinecraftKeyBindingMapper.BINDING_HOTBAR_1 to FCLKeycodes.KEY_1,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_2 to FCLKeycodes.KEY_2,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_3 to FCLKeycodes.KEY_3,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_4 to FCLKeycodes.KEY_4,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_5 to FCLKeycodes.KEY_5,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_6 to FCLKeycodes.KEY_6,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_7 to FCLKeycodes.KEY_7,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_8 to FCLKeycodes.KEY_8,
            MinecraftKeyBindingMapper.BINDING_HOTBAR_9 to FCLKeycodes.KEY_9
        )

        val (binding, keycode) = bindings[position - 1]
        gameMenu.input.sendBoundKeyEvent(gameOption, binding, keycode, press)
    }
}
