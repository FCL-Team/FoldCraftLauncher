/*
 * Zalith Launcher 2
 * Copyright (C) 2025 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.tungsten.fcl.game.sdl

import android.view.KeyEvent
import android.view.MotionEvent
import org.lwjgl.glfw.CallbackBridge

// Reference AAMC (https://github.com/AngelAuraMC/Amethyst-Android/blob/21cfcdb15e21771810c830036097197ba9f21941/app_pojavlauncher/src/main/java/net/kdt/pojavlaunch/customcontrols/gamepad/direct/DirectGamepad.java)

private const val GLFW_RELEASE: Byte = 0
private const val GLFW_PRESS: Byte = 1

private const val GLFW_GAMEPAD_BUTTON_A: Int = 0
private const val GLFW_GAMEPAD_BUTTON_B: Int = 1
private const val GLFW_GAMEPAD_BUTTON_X: Int = 2
private const val GLFW_GAMEPAD_BUTTON_Y: Int = 3
private const val GLFW_GAMEPAD_BUTTON_LEFT_BUMPER: Int = 4
private const val GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER: Int = 5
private const val GLFW_GAMEPAD_BUTTON_BACK: Int = 6
private const val GLFW_GAMEPAD_BUTTON_START: Int = 7
// Home 键，未被使用，因为 Android 会自己响应 Home 键事件
private const val GLFW_GAMEPAD_BUTTON_GUIDE: Int = 8
private const val GLFW_GAMEPAD_BUTTON_LEFT_THUMB: Int = 9
private const val GLFW_GAMEPAD_BUTTON_RIGHT_THUMB: Int = 10
private const val GLFW_GAMEPAD_BUTTON_DPAD_UP: Int = 11
private const val GLFW_GAMEPAD_BUTTON_DPAD_RIGHT: Int = 12
private const val GLFW_GAMEPAD_BUTTON_DPAD_DOWN: Int = 13
private const val GLFW_GAMEPAD_BUTTON_DPAD_LEFT: Int = 14

private const val GLFW_GAMEPAD_AXIS_LEFT_X: Int = 0
private const val GLFW_GAMEPAD_AXIS_LEFT_Y: Int = 1
private const val GLFW_GAMEPAD_AXIS_RIGHT_X: Int = 2
private const val GLFW_GAMEPAD_AXIS_RIGHT_Y: Int = 3
private const val GLFW_GAMEPAD_AXIS_LEFT_TRIGGER: Int = 4
private const val GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER: Int = 5

private const val PRESS_THRESHOLD = 0.85f

fun handleGamepadKeyEvent(event: KeyEvent) {
    if (!CallbackBridge.sGamepadDirectInput) return
    val isDown = event.action == KeyEvent.ACTION_DOWN
    handleGamepadInput(event.keyCode, if (isDown) 1.0f else 0.0f)
}

fun handleGamepadMotionEvent(event: MotionEvent) {
    if (!CallbackBridge.sGamepadDirectInput) return
    handleGamepadInput(MotionEvent.AXIS_X, event.getAxisValue(MotionEvent.AXIS_X))
    handleGamepadInput(MotionEvent.AXIS_Y, event.getAxisValue(MotionEvent.AXIS_Y))
    handleGamepadInput(MotionEvent.AXIS_Z, event.getAxisValue(MotionEvent.AXIS_Z))
    handleGamepadInput(MotionEvent.AXIS_RZ, event.getAxisValue(MotionEvent.AXIS_RZ))
    handleGamepadInput(MotionEvent.AXIS_LTRIGGER, event.getAxisValue(MotionEvent.AXIS_LTRIGGER))
    handleGamepadInput(MotionEvent.AXIS_RTRIGGER, event.getAxisValue(MotionEvent.AXIS_RTRIGGER))
    handleGamepadInput(MotionEvent.AXIS_HAT_X, event.getAxisValue(MotionEvent.AXIS_HAT_X))
    handleGamepadInput(MotionEvent.AXIS_HAT_Y, event.getAxisValue(MotionEvent.AXIS_HAT_Y))
}

private fun handleGamepadInput(keycode: Int, value: Float) {
    var gKeycode = -1
    var gAxis = -1
    when (keycode) {
        KeyEvent.KEYCODE_BUTTON_A -> gKeycode = GLFW_GAMEPAD_BUTTON_A
        KeyEvent.KEYCODE_BUTTON_B -> gKeycode = GLFW_GAMEPAD_BUTTON_B
        KeyEvent.KEYCODE_BUTTON_X -> gKeycode = GLFW_GAMEPAD_BUTTON_X
        KeyEvent.KEYCODE_BUTTON_Y -> gKeycode = GLFW_GAMEPAD_BUTTON_Y
        KeyEvent.KEYCODE_BUTTON_L1 -> gKeycode = GLFW_GAMEPAD_BUTTON_LEFT_BUMPER
        KeyEvent.KEYCODE_BUTTON_R1 -> gKeycode = GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER
        KeyEvent.KEYCODE_BUTTON_L2, MotionEvent.AXIS_LTRIGGER -> gAxis = GLFW_GAMEPAD_AXIS_LEFT_TRIGGER
        KeyEvent.KEYCODE_BUTTON_R2, MotionEvent.AXIS_RTRIGGER -> gAxis = GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER
        KeyEvent.KEYCODE_BUTTON_THUMBL -> gKeycode = GLFW_GAMEPAD_BUTTON_LEFT_THUMB
        KeyEvent.KEYCODE_BUTTON_THUMBR -> gKeycode = GLFW_GAMEPAD_BUTTON_RIGHT_THUMB
        KeyEvent.KEYCODE_BUTTON_START -> gKeycode = GLFW_GAMEPAD_BUTTON_START
        KeyEvent.KEYCODE_BUTTON_SELECT -> gKeycode = GLFW_GAMEPAD_BUTTON_BACK
        KeyEvent.KEYCODE_DPAD_UP -> gKeycode = GLFW_GAMEPAD_BUTTON_DPAD_UP
        KeyEvent.KEYCODE_DPAD_DOWN -> gKeycode = GLFW_GAMEPAD_BUTTON_DPAD_DOWN
        KeyEvent.KEYCODE_DPAD_LEFT -> gKeycode = GLFW_GAMEPAD_BUTTON_DPAD_LEFT
        KeyEvent.KEYCODE_DPAD_RIGHT -> gKeycode = GLFW_GAMEPAD_BUTTON_DPAD_RIGHT
        KeyEvent.KEYCODE_DPAD_CENTER -> {
            // Behave the same way as the Gamepad here, as GLFW doesn't have a keycode
            // for the dpad center.
            CallbackBridge.sGamepadButtonBuffer.put(GLFW_GAMEPAD_BUTTON_DPAD_UP, GLFW_RELEASE)
            CallbackBridge.sGamepadButtonBuffer.put(GLFW_GAMEPAD_BUTTON_DPAD_DOWN, GLFW_RELEASE)
            CallbackBridge.sGamepadButtonBuffer.put(GLFW_GAMEPAD_BUTTON_DPAD_LEFT, GLFW_RELEASE)
            CallbackBridge.sGamepadButtonBuffer.put(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT, GLFW_RELEASE)
            return
        }
        MotionEvent.AXIS_X -> gAxis = GLFW_GAMEPAD_AXIS_LEFT_X
        MotionEvent.AXIS_Y -> gAxis = GLFW_GAMEPAD_AXIS_LEFT_Y
        MotionEvent.AXIS_Z -> gAxis = GLFW_GAMEPAD_AXIS_RIGHT_X
        MotionEvent.AXIS_RZ -> gAxis = GLFW_GAMEPAD_AXIS_RIGHT_Y
        MotionEvent.AXIS_HAT_X -> {
            CallbackBridge.sGamepadButtonBuffer.put(
                GLFW_GAMEPAD_BUTTON_DPAD_LEFT,
                if (value < -PRESS_THRESHOLD) GLFW_PRESS else GLFW_RELEASE
            )
            CallbackBridge.sGamepadButtonBuffer.put(
                GLFW_GAMEPAD_BUTTON_DPAD_RIGHT,
                if (value > PRESS_THRESHOLD) GLFW_PRESS else GLFW_RELEASE
            )
            return
        }
        MotionEvent.AXIS_HAT_Y -> {
            CallbackBridge.sGamepadButtonBuffer.put(
                GLFW_GAMEPAD_BUTTON_DPAD_UP,
                if (value < -PRESS_THRESHOLD) GLFW_PRESS else GLFW_RELEASE
            )
            CallbackBridge.sGamepadButtonBuffer.put(
                GLFW_GAMEPAD_BUTTON_DPAD_DOWN,
                if (value > PRESS_THRESHOLD) GLFW_PRESS else GLFW_RELEASE
            )
            return
        }
        else -> return
    }
    if (gKeycode != -1) {
        CallbackBridge.sGamepadButtonBuffer.put(gKeycode, if (value > PRESS_THRESHOLD) GLFW_PRESS else GLFW_RELEASE)
    }
    if (gAxis != -1) {
        CallbackBridge.sGamepadAxisBuffer.put(gAxis, value)
    }
}