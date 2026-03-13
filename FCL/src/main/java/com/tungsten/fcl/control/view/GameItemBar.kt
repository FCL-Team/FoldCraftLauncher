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
    private var restore: Runnable? = null
    private var lastMovePosition = 0
    private var lastClickTime = 0L
    private var lastClickPosition = 0

    init {
        isClickable = true
        isFocusable = true
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
                restore?.let {
                    removeCallbacks(it)
                }
                setBackgroundColor(-0x7f010000)
                restore = Runnable {
                    setBackgroundColor(0x00000000)
                    restore = null
                }
                postDelayed(restore, 1500)
            }
        }
        optionListener!!.onOptionChanged(false)
        gameOption.addGameOptionListener(optionListener)
    }

    fun notifySize(size: Int) {
        post {
            val params = layoutParams
            params.width = size * 9
            params.height = size
            setLayoutParams(params)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                runIfInPosition(event) {
                    sendHotbarKey(it)
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime < 200 && it == lastClickPosition) {
                        swapHands()
                        lastClickTime = 0
                        lastClickPosition = 0
                    } else {
                        lastClickTime = currentTime
                        lastClickPosition = it
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                runIfInPosition(event) {
                    if (lastMovePosition != it) {
                        sendHotbarKey(it)
                    }
                    lastMovePosition = it
                }
            }
        }
        return true
    }

    fun runIfInPosition(e: MotionEvent, func: (position: Int) -> Unit) {
        if (e.x >= 0 && e.x <= 9 * height) {
            func(((e.x / height).toInt() + 1).coerceIn(1, 9))
        }
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

    private fun sendHotbarKey(position: Int) {
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
        gameMenu.input.sendBoundKeyEvent(gameOption, binding, keycode, true)
        gameMenu.input.sendBoundKeyEvent(gameOption, binding, keycode, false)
    }

    fun dropItem() {
        gameMenu.input.sendBoundKeyEvent(
            gameOption,
            MinecraftKeyBindingMapper.BINDING_DROP,
            FCLKeycodes.KEY_Q,
            true
        )
        gameMenu.input.sendBoundKeyEvent(
            gameOption,
            MinecraftKeyBindingMapper.BINDING_DROP,
            FCLKeycodes.KEY_Q,
            false
        )
    }
}
