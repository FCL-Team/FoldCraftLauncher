package com.tungsten.fcl.control.gamepad

import android.view.KeyEvent

open class GamepadEmulatedButton {
    var keycodes = mutableListOf<Int>()
    var isDown: Boolean = false

    fun update(event: KeyEvent) {
        val isKeyDown = (event.action == KeyEvent.ACTION_DOWN)
        update(isKeyDown)
    }

    fun update(isKeyDown: Boolean) {
        if (isKeyDown != isDown) {
            isDown = isKeyDown
            onDownStateChanged(isDown)
        }
    }

    open fun resetButtonState() {
        if(isDown) {
            keycodes.forEach {
                Gamepad.getFCLInput()?.sendKeyEvent(it, false)
            }
        }
        isDown = false
    }

    open fun onDownStateChanged(isDown: Boolean) {
        keycodes.forEach {
            Gamepad.getFCLInput()?.sendKeyEvent(it, isDown)
        }
    }
}
