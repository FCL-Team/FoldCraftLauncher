/*
 * Fold Craft Launcher
 * 首次手柄输入时的输入模式选择弹窗（对齐 ZalithLauncher2 的 GamepadModePromptDialog）
 */
package com.tungsten.fcl.game.sdl

import android.app.Activity
import android.view.LayoutInflater
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogGamepadModePromptBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils
import java.lang.ref.WeakReference

/**
 * 首次手柄输入时弹窗选择输入模式（确认前所有手柄输入都会被吞掉）。
 */
class GamepadModePromptDialog private constructor(activity: Activity) : FCLDialog(activity), View.OnClickListener {

    private var mode: GamepadInputMode
    private val binding = DialogGamepadModePromptBinding.inflate(LayoutInflater.from(activity))

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setLayout(ConvertUtils.dip2px(activity, 380f), ViewGroup.LayoutParams.WRAP_CONTENT)
        setContentView(binding.root)

        binding.rowMapped.setOnClickListener(this)
        binding.rowSdl.setOnClickListener(this)
        binding.radioMapped.setOnClickListener(this)
        binding.radioSdl.setOnClickListener(this)
        binding.positive.setOnClickListener(this)

        // 默认选中当前设置的模式
        mode = SdlSettings.gamepadInputMode.value
        binding.radioMapped.isChecked = mode == GamepadInputMode.MAPPED
        binding.radioSdl.isChecked = mode == GamepadInputMode.SDL_DIRECT
    }

    private fun select(newMode: GamepadInputMode) {
        mode = newMode
        binding.radioMapped.isChecked = newMode == GamepadInputMode.MAPPED
        binding.radioSdl.isChecked = newMode == GamepadInputMode.SDL_DIRECT
    }

    private fun confirm() {
        SdlSettings.setGamepadInputMode(mode)
        SdlSettings.setGamepadInputModePrompted(true)
        dismiss()
    }

    /**
     * The dialog is not cancelable and swallows gamepad input from the game, so the gamepad
     * itself must be able to operate it: D-pad up/down selects, A/Cross or Start confirms.
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> select(GamepadInputMode.MAPPED)
            KeyEvent.KEYCODE_DPAD_DOWN -> select(GamepadInputMode.SDL_DIRECT)
            KeyEvent.KEYCODE_BUTTON_A, KeyEvent.KEYCODE_BUTTON_START -> confirm()
            else -> return super.onKeyDown(keyCode, event)
        }
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.row_mapped, R.id.radio_mapped -> select(GamepadInputMode.MAPPED)
            R.id.row_sdl, R.id.radio_sdl -> select(GamepadInputMode.SDL_DIRECT)
            R.id.positive -> confirm()
        }
    }

    companion object {
        private var sInstance: WeakReference<GamepadModePromptDialog>? = null

        /**
         * 首次手柄输入时弹窗选择输入模式。
         * @return true 表示弹窗已显示或显示中，调用方应吞掉本次手柄输入；
         *         false 表示无需弹窗，按原逻辑处理
         */
        @JvmStatic
        fun checkAndShow(activity: Activity): Boolean {
            if (SdlSettings.isGamepadInputModePrompted()) {
                return false
            }
            val existing = sInstance?.get()
            if (existing != null && existing.isShowing) {
                return true
            }
            if (activity.isFinishing || activity.isDestroyed) {
                return false
            }
            val dialog = GamepadModePromptDialog(activity)
            sInstance = WeakReference(dialog)
            dialog.show()
            return true
        }
    }
}