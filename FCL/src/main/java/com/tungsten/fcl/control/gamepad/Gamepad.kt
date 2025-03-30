package com.tungsten.fcl.control.gamepad

import android.content.Context
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import com.google.gson.Gson
import com.tungsten.fcl.control.FCLInput
import com.tungsten.fclauncher.utils.FCLPath
import fr.spse.gamepad_remapper.GamepadHandler
import fr.spse.gamepad_remapper.RemapperManager
import fr.spse.gamepad_remapper.RemapperView
import java.io.File
import java.lang.ref.WeakReference

class Gamepad(val context: Context, val fclInput: FCLInput) : GamepadHandler {
    var manager: RemapperManager = RemapperManager(
        context, RemapperView.Builder(null)
            .remapA(true)
            .remapB(true)
            .remapX(true)
            .remapY(true)
            .remapLeftJoystick(true)
            .remapRightJoystick(true)
            .remapStart(true)
            .remapSelect(true)
            .remapLeftShoulder(true)
            .remapRightShoulder(true)
            .remapLeftTrigger(true)
            .remapRightTrigger(true)
            .remapDpad(true)
    )
    lateinit var currentMap: GamepadMap
    private var xAxis = 0.0f
    private var yAxis = 0.0f
    private var axisZ = 0.0f
    private var axisRZ = 0.0f

    companion object {
        private var fclInput: WeakReference<FCLInput>? = null

        @JvmStatic
        fun getFCLInput(): FCLInput? {
            return fclInput?.get()
        }

        @JvmStatic
        fun isGamepadEvent(event: MotionEvent): Boolean {
            return (event.source and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                    && event.action == MotionEvent.ACTION_MOVE
        }

        @JvmStatic
        fun isGamepadEvent(event: KeyEvent): Boolean {
            val isGamepad =
                ((event.source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                        || ((event.device != null) && ((event.device.sources and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD))
            val isDpadEvent =
                event.isFromSource(InputDevice.SOURCE_GAMEPAD) && (event.device == null || event.device.keyboardType != InputDevice.KEYBOARD_TYPE_ALPHABETIC)
            return isGamepad || isDpadEvent
        }
    }

    init {
        Companion.fclInput = WeakReference(fclInput)
        refreshMapper()
    }

    fun refreshMapper() {
        val file = File(FCLPath.FILES_DIR, "gamepad.json")
        if (file.exists()) {
            try {
                currentMap = Gson().fromJson(file.readText(), GamepadMap::class.java)
            } catch (_: Throwable) {
                currentMap = GamepadMap.getDefaultGameMap()
            }
        } else {
            currentMap = GamepadMap.getDefaultGameMap()
        }
    }

    fun saveMapper() {
        val file = File(FCLPath.FILES_DIR, "gamepad.json")
        file.writeText(Gson().toJson(currentMap))
    }

    fun handleMotionEventInput(event: MotionEvent): Boolean {
        return manager.handleMotionEventInput(context, event, this)
    }

    fun handleKeyEvent(event: KeyEvent): Boolean {
        return manager.handleKeyEventInput(context, event, this)
    }


    override fun handleGamepadInput(code: Int, value: Float) {
        val isKeyEventDown = value == 1f
        when (code) {
            KeyEvent.KEYCODE_BUTTON_A ->
                currentMap.BUTTON_A.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_B ->
                currentMap.BUTTON_B.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_X ->
                currentMap.BUTTON_X.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_Y ->
                currentMap.BUTTON_Y.update(isKeyEventDown)

            //Shoulders
            KeyEvent.KEYCODE_BUTTON_L1 ->
                currentMap.SHOULDER_LEFT.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_R1 ->
                currentMap.SHOULDER_RIGHT.update(isKeyEventDown)


            //Triggers
            KeyEvent.KEYCODE_BUTTON_L2 ->
                currentMap.TRIGGER_LEFT.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_R2 ->
                currentMap.TRIGGER_RIGHT.update(isKeyEventDown)


            //L3 || R3
            KeyEvent.KEYCODE_BUTTON_THUMBL ->
                currentMap.THUMBSTICK_LEFT.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_THUMBR ->
                currentMap.THUMBSTICK_RIGHT.update(isKeyEventDown)

            //DPAD
            KeyEvent.KEYCODE_DPAD_UP ->
                currentMap.DPAD_UP.update(isKeyEventDown)

            KeyEvent.KEYCODE_DPAD_DOWN ->
                currentMap.DPAD_DOWN.update(isKeyEventDown)

            KeyEvent.KEYCODE_DPAD_LEFT ->
                currentMap.DPAD_LEFT.update(isKeyEventDown)

            KeyEvent.KEYCODE_DPAD_RIGHT ->
                currentMap.DPAD_RIGHT.update(isKeyEventDown)

            KeyEvent.KEYCODE_DPAD_CENTER -> {
                currentMap.DPAD_RIGHT.update(false)

                currentMap.DPAD_LEFT.update(false)
                currentMap.DPAD_UP.update(false)
                currentMap.DPAD_DOWN.update(false)
            }

            //Start/select
            KeyEvent.KEYCODE_BUTTON_START ->
                currentMap.BUTTON_START.update(isKeyEventDown)

            KeyEvent.KEYCODE_BUTTON_SELECT ->
                currentMap.BUTTON_SELECT.update(isKeyEventDown)

            /* Now, it is time for motionEvents */
            MotionEvent.AXIS_HAT_X -> {
                currentMap.DPAD_RIGHT.update(value > 0.85)
                currentMap.DPAD_LEFT.update(value < -0.85)
            }

            MotionEvent.AXIS_HAT_Y -> {
                currentMap.DPAD_DOWN.update(value > 0.85)
                currentMap.DPAD_UP.update(value < -0.85)
            }

            // Left joystick
            MotionEvent.AXIS_X -> {
                xAxis = value
                fclInput.handleLeftJoyStick(xAxis, yAxis)
            }

            MotionEvent.AXIS_Y -> {
                yAxis = value
                fclInput.handleLeftJoyStick(xAxis, yAxis)
            }

            // Right joystick
            MotionEvent.AXIS_Z -> {
                axisZ = value
                fclInput.handleRightJoyStick(axisZ, axisRZ)
            }

            MotionEvent.AXIS_RZ -> {
                axisRZ = value
                fclInput.handleRightJoyStick(axisZ, axisRZ)
            }

            // Triggers
            MotionEvent.AXIS_RTRIGGER ->
                currentMap.TRIGGER_RIGHT.update(value > 0.5)

            MotionEvent.AXIS_LTRIGGER ->
                currentMap.TRIGGER_LEFT.update(value > 0.5)
        }
    }

    fun resetMapper() {
        manager = RemapperManager(
            context, RemapperView.Builder(null)
                .remapA(true)
                .remapB(true)
                .remapX(true)
                .remapY(true)
                .remapLeftJoystick(true)
                .remapRightJoystick(true)
                .remapStart(true)
                .remapSelect(true)
                .remapLeftShoulder(true)
                .remapRightShoulder(true)
                .remapLeftTrigger(true)
                .remapRightTrigger(true)
                .remapDpad(true)
        )
    }
}