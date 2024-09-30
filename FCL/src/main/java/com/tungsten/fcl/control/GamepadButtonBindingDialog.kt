package com.tungsten.fcl.control

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcl.R
import com.tungsten.fcl.control.view.KeycodeView
import com.tungsten.fcl.control.view.KeycodeView.OnKeycodeChangeListener
import com.tungsten.fclauncher.keycodes.GamepadKeycodeMap
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.fakefx.collections.ObservableList
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLButton
import com.tungsten.fcllibrary.component.view.FCLLinearLayout
import java.util.Objects
import java.util.function.Consumer

class GamepadButtonBindingDialog(
    context: Context,
    val map: MutableMap<Int,Int>
) : FCLDialog(context) {

    init {
        setCancelable(false)
        setContentView(R.layout.view_gamepad)
        findViewById<FCLButton>(R.id.gamepad_lb)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_L1)
        }
        findViewById<FCLButton>(R.id.gamepad_lt)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_L2)
        }
        findViewById<FCLButton>(R.id.gamepad_rb)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_R1)
        }
        findViewById<FCLButton>(R.id.gamepad_rt)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_R2)
        }

        findViewById<FCLButton>(R.id.gamepad_start)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_START)
        }
        findViewById<FCLButton>(R.id.gamepad_select)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_SELECT)
        }

        findViewById<FCLButton>(R.id.gamepad_a)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_A)
        }
        findViewById<FCLButton>(R.id.gamepad_b)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_B)
        }
        findViewById<FCLButton>(R.id.gamepad_x)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_X)
        }
        findViewById<FCLButton>(R.id.gamepad_y)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_Y)
        }

        findViewById<FCLButton>(R.id.gamepad_lj_l)?.setOnClickListener {
            bindingAction(GamepadKeycodeMap.LEFT_JOYSTICK_LEFT)
        }
        findViewById<FCLButton>(R.id.gamepad_lj_r)?.setOnClickListener {
            bindingAction(GamepadKeycodeMap.LEFT_JOYSTICK_RIGHT)
        }
        findViewById<FCLButton>(R.id.gamepad_lj_u)?.setOnClickListener {
            bindingAction(GamepadKeycodeMap.LEFT_JOYSTICK_UP)
        }
        findViewById<FCLButton>(R.id.gamepad_lj_d)?.setOnClickListener {
            bindingAction(GamepadKeycodeMap.LEFT_JOYSTICK_DOWN)
        }

        findViewById<FCLButton>(R.id.gamepad_lj_p)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_THUMBL)
        }
        findViewById<FCLButton>(R.id.gamepad_rj_p)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_BUTTON_THUMBR)
        }

        findViewById<FCLButton>(R.id.gamepad_dpad_l)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_DPAD_LEFT)
        }
        findViewById<FCLButton>(R.id.gamepad_dpad_r)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_DPAD_RIGHT)
        }
        findViewById<FCLButton>(R.id.gamepad_dpad_u)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_DPAD_UP)
        }
        findViewById<FCLButton>(R.id.gamepad_dpad_d)?.setOnClickListener {
            bindingAction(KeyEvent.KEYCODE_DPAD_DOWN)
        }

        findViewById<FCLButton>(R.id.btn_positive)?.setOnClickListener {
            dismiss()
        }
    }

    private fun bindingAction(
        gamepadKey:Int,
    ){
        val li = FXCollections.observableList(mutableListOf(map.getOrDefault(gamepadKey,-1)))
        SelectKeycodeDialog(
            context,
            li,
            true, true
        ){
            map[gamepadKey] = it.selectionProperty().get()
        }.show()
    }
}
