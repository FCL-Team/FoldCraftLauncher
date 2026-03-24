package com.mio.ui.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.ui.adapter.GamepadMapItemAdapter
import com.tungsten.fcl.control.FCLInput
import com.tungsten.fcl.databinding.DialogGamepadMapBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils

class GamepadMapDialog(context: Context, fclInput: FCLInput) : FCLDialog(context) {
    init {
        window?.setLayout(ConvertUtils.dip2px(context, 400F), ViewGroup.LayoutParams.MATCH_PARENT)
        val binding = DialogGamepadMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter = GamepadMapItemAdapter(context, fclInput.gamepad.currentMap)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.confirm.setOnClickListener {
            fclInput.gamepad.saveMapper()
            dismiss()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}