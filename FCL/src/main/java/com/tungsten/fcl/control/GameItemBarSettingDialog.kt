package com.tungsten.fcl.control

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mio.datastore.GameItemBarSetting
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.DialogItembarSettingBinding
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.util.ConvertUtils

class GameItemBarSettingDialog(
    context: Context,
    val setting: GameItemBarSetting,
    val callback: (GameItemBarSetting) -> Unit
) : FCLDialog(context) {
    init {
        window?.setLayout(ConvertUtils.dip2px(context, 400f), ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(R.drawable.bg_game_menu)
        val binding = DialogItembarSettingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.slideSelection.isChecked = setting.slideSelection
        binding.swapHands.isChecked = setting.doubleTapSwapHands

        binding.slideSelection.setOnCheckedChangeListener { _, isChecked ->
            callback(setting.copy(slideSelection = isChecked))
        }
        binding.swapHands.setOnCheckedChangeListener { _, isChecked ->
            callback(setting.copy(doubleTapSwapHands = isChecked))
        }
        binding.close.setOnClickListener { dismiss() }
    }
}