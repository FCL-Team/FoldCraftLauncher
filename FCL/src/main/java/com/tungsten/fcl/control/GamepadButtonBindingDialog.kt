package com.tungsten.fcl.control

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ViewGamepadBinding
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fcllibrary.component.dialog.FCLDialog

class GamepadButtonBindingDialog(
    context: Context,
    val map: MutableMap<Int, Int>
) : FCLDialog(context) {

    init {
        setCancelable(false)
        DataBindingUtil.inflate<ViewGamepadBinding>(
            layoutInflater,
            R.layout.view_gamepad,
            null,
            false
        ).apply {
            dialog = this@GamepadButtonBindingDialog
            setContentView(root)
        }
    }

    fun bindingAction(gamepadKey: Int) {
        val list = FXCollections.observableList(mutableListOf(map.getOrDefault(gamepadKey, -1)))
        SelectKeycodeDialog(
            context,
            list,
            true, true
        ) {
            map[gamepadKey] = it.selectionProperty().get()
        }.show()
    }
}
