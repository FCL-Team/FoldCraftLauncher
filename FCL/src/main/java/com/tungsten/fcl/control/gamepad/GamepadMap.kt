package com.tungsten.fcl.control.gamepad

import com.tungsten.fcl.control.FCLInput
import com.tungsten.fclauncher.keycodes.FCLKeycodes


class GamepadMap {
    lateinit var BUTTON_A: GamepadButton
    lateinit var BUTTON_B: GamepadButton
    lateinit var BUTTON_X: GamepadButton
    lateinit var BUTTON_Y: GamepadButton
    lateinit var BUTTON_START: GamepadButton
    lateinit var BUTTON_SELECT: GamepadButton
    lateinit var TRIGGER_RIGHT: GamepadButton
    lateinit var TRIGGER_LEFT: GamepadButton
    lateinit var SHOULDER_RIGHT: GamepadButton
    lateinit var SHOULDER_LEFT: GamepadButton
    lateinit var THUMBSTICK_RIGHT: GamepadButton
    lateinit var THUMBSTICK_LEFT: GamepadButton
    lateinit var DPAD_UP: GamepadButton
    lateinit var DPAD_DOWN: GamepadButton
    lateinit var DPAD_RIGHT: GamepadButton
    lateinit var DPAD_LEFT: GamepadButton
    lateinit var DIRECTION_FORWARD: GamepadEmulatedButton
    lateinit var DIRECTION_BACKWARD: GamepadEmulatedButton
    lateinit var DIRECTION_RIGHT: GamepadEmulatedButton
    lateinit var DIRECTION_LEFT: GamepadEmulatedButton

    fun resetPressedState() {
        BUTTON_A.resetButtonState()
        BUTTON_B.resetButtonState()
        BUTTON_X.resetButtonState()
        BUTTON_Y.resetButtonState()

        BUTTON_START.resetButtonState()
        BUTTON_SELECT.resetButtonState()

        TRIGGER_LEFT.resetButtonState()
        TRIGGER_RIGHT.resetButtonState()

        SHOULDER_LEFT.resetButtonState()
        SHOULDER_RIGHT.resetButtonState()

        THUMBSTICK_LEFT.resetButtonState()
        THUMBSTICK_RIGHT.resetButtonState()

        DPAD_UP.resetButtonState()
        DPAD_RIGHT.resetButtonState()
        DPAD_DOWN.resetButtonState()
        DPAD_LEFT.resetButtonState()
    }

    fun getButtons(): Array<GamepadEmulatedButton> {
        return arrayOf<GamepadEmulatedButton>(
            BUTTON_A, BUTTON_B, BUTTON_X, BUTTON_Y,
            BUTTON_SELECT, BUTTON_START,
            TRIGGER_LEFT, TRIGGER_RIGHT,
            SHOULDER_LEFT, SHOULDER_RIGHT,
            THUMBSTICK_LEFT, THUMBSTICK_RIGHT,
            DPAD_UP, DPAD_RIGHT, DPAD_DOWN, DPAD_LEFT,
            DIRECTION_FORWARD, DIRECTION_BACKWARD,
            DIRECTION_LEFT, DIRECTION_RIGHT
        )
    }

    companion object {

        private fun createEmptyMap(): GamepadMap {
            val gamepadMap = GamepadMap()
            gamepadMap.BUTTON_A = GamepadButton()
            gamepadMap.BUTTON_B = GamepadButton()
            gamepadMap.BUTTON_X = GamepadButton()
            gamepadMap.BUTTON_Y = GamepadButton()

            gamepadMap.BUTTON_START = GamepadButton()
            gamepadMap.BUTTON_SELECT = GamepadButton()

            gamepadMap.TRIGGER_RIGHT = GamepadButton()
            gamepadMap.TRIGGER_LEFT = GamepadButton()

            gamepadMap.SHOULDER_RIGHT = GamepadButton()
            gamepadMap.SHOULDER_LEFT = GamepadButton()

            gamepadMap.DIRECTION_FORWARD = GamepadEmulatedButton()
            gamepadMap.DIRECTION_BACKWARD = GamepadEmulatedButton()
            gamepadMap.DIRECTION_RIGHT = GamepadEmulatedButton()
            gamepadMap.DIRECTION_LEFT = GamepadEmulatedButton()

            gamepadMap.THUMBSTICK_RIGHT = GamepadButton()
            gamepadMap.THUMBSTICK_LEFT = GamepadButton()

            gamepadMap.DPAD_UP = GamepadButton()
            gamepadMap.DPAD_RIGHT = GamepadButton()
            gamepadMap.DPAD_DOWN = GamepadButton()
            gamepadMap.DPAD_LEFT = GamepadButton()
            return gamepadMap
        }

        fun getDefaultGameMap(): GamepadMap {
            val gameMap = createEmptyMap()

            gameMap.BUTTON_A.keycodes.add(FCLKeycodes.KEY_SPACE)
            gameMap.BUTTON_B.keycodes.add(FCLKeycodes.KEY_Q)
            gameMap.BUTTON_X.keycodes.add(FCLKeycodes.KEY_E)
            gameMap.BUTTON_Y.keycodes.add(FCLKeycodes.KEY_F)

            gameMap.DIRECTION_FORWARD.keycodes.add(FCLKeycodes.KEY_W)
            gameMap.DIRECTION_BACKWARD.keycodes.add(FCLKeycodes.KEY_S)
            gameMap.DIRECTION_RIGHT.keycodes.add(FCLKeycodes.KEY_D)
            gameMap.DIRECTION_LEFT.keycodes.add(FCLKeycodes.KEY_A)

            gameMap.DPAD_UP.keycodes.add(FCLKeycodes.KEY_LEFTSHIFT)
            gameMap.DPAD_DOWN.keycodes.add(FCLKeycodes.KEY_O)
            gameMap.DPAD_RIGHT.keycodes.add(FCLKeycodes.KEY_K)
            gameMap.DPAD_LEFT.keycodes.add(FCLKeycodes.KEY_J)

            gameMap.SHOULDER_LEFT.keycodes.add(FCLInput.MOUSE_SCROLL_UP)
            gameMap.SHOULDER_RIGHT.keycodes.add(FCLInput.MOUSE_SCROLL_DOWN)

            gameMap.TRIGGER_LEFT.keycodes.add(FCLInput.MOUSE_LEFT)
            gameMap.TRIGGER_RIGHT.keycodes.add(FCLInput.MOUSE_RIGHT)

            gameMap.THUMBSTICK_LEFT.keycodes.add(FCLKeycodes.KEY_LEFTCTRL)
            gameMap.THUMBSTICK_RIGHT.keycodes.add(FCLKeycodes.KEY_LEFTSHIFT)
            gameMap.THUMBSTICK_RIGHT.isToggleable = true

            gameMap.BUTTON_START.keycodes.add(FCLKeycodes.KEY_ESC)
            gameMap.BUTTON_SELECT.keycodes.add(FCLKeycodes.KEY_TAB)

            return gameMap
        }
    }
}
