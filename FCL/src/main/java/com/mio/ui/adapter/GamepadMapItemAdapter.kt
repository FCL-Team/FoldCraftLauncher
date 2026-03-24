package com.mio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tungsten.fcl.R
import com.tungsten.fcl.control.SelectKeycodeDialog
import com.tungsten.fcl.control.gamepad.GamepadEmulatedButton
import com.tungsten.fcl.control.gamepad.GamepadMap
import com.tungsten.fcl.databinding.ItemGamepadMapBinding
import com.tungsten.fclcore.fakefx.collections.FXCollections

class GamepadMapItemAdapter(val context: Context, val gamepadMap: GamepadMap) :
    RecyclerView.Adapter<GamepadMapItemAdapter.ViewHolder>() {
    private val itemList = mutableListOf<Item>()

    init {
        refresh()
    }

    fun refresh() {
        itemList.apply {
            clear()
            add(Item(fr.spse.gamepad_remapper.R.drawable.button_a, gamepadMap.BUTTON_A))
            add(Item(fr.spse.gamepad_remapper.R.drawable.button_b, gamepadMap.BUTTON_B))
            add(Item(fr.spse.gamepad_remapper.R.drawable.button_x, gamepadMap.BUTTON_X))
            add(Item(fr.spse.gamepad_remapper.R.drawable.button_y, gamepadMap.BUTTON_Y))
            add(Item(fr.spse.gamepad_remapper.R.drawable.button_start, gamepadMap.BUTTON_START))
            add(Item(fr.spse.gamepad_remapper.R.drawable.button_select, gamepadMap.BUTTON_SELECT))
            add(Item(fr.spse.gamepad_remapper.R.drawable.shoulder_left, gamepadMap.SHOULDER_LEFT))
            add(Item(fr.spse.gamepad_remapper.R.drawable.shoulder_right, gamepadMap.SHOULDER_RIGHT))
            add(Item(fr.spse.gamepad_remapper.R.drawable.trigger_left, gamepadMap.TRIGGER_LEFT))
            add(Item(fr.spse.gamepad_remapper.R.drawable.trigger_right, gamepadMap.TRIGGER_RIGHT))
            add(
                Item(
                    fr.spse.gamepad_remapper.R.drawable.stick_left_click,
                    gamepadMap.THUMBSTICK_LEFT
                )
            )
            add(
                Item(
                    fr.spse.gamepad_remapper.R.drawable.stick_right_click,
                    gamepadMap.THUMBSTICK_RIGHT
                )
            )
            add(Item(fr.spse.gamepad_remapper.R.drawable.dpad_up, gamepadMap.DPAD_UP))
            add(Item(fr.spse.gamepad_remapper.R.drawable.dpad_down, gamepadMap.DPAD_DOWN))
            add(Item(fr.spse.gamepad_remapper.R.drawable.dpad_left, gamepadMap.DPAD_LEFT))
            add(Item(fr.spse.gamepad_remapper.R.drawable.dpad_right, gamepadMap.DPAD_RIGHT))
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_gamepad_map, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemGamepadMapBinding.bind(holder.itemView)
        binding.icon.setImageResource(itemList[position].icon)
        binding.remap.setOnClickListener {
            val list = FXCollections.observableList(itemList[position].button.keycodes)
            SelectKeycodeDialog(context, list, false, true).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class Item(
        val icon: Int,
        val button: GamepadEmulatedButton
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}