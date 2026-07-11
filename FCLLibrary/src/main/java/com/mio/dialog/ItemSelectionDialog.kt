package com.mio.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tungsten.fcllibrary.R
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.databinding.DialogItemSelectionBinding
import com.tungsten.fcllibrary.databinding.ItemTextBinding
import com.tungsten.fcllibrary.util.ConvertUtils

class ItemSelectionDialog(
    context: Context,
    title: String,
    items: List<String>,
    small: Boolean,
    callback: (Int, String) -> Unit
) : FCLDialog(context) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class ItemSelectionAdapter(
        val context: Context,
        val items: List<String>,
        val callback: (Int, String) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_text, parent, false)
            )
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            val binding = ItemTextBinding.bind(holder.itemView)
            binding.text.text = items[position]
            binding.root.setOnClickListener { callback(position, items[position]) }
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }

    init {
        window?.windowManager
        window?.setLayout(
            ConvertUtils.dip2px(context, 500f),
            if (small) ConvertUtils.dip2px(context, 200f) else ViewGroup.LayoutParams.MATCH_PARENT
        )
        val binding = DialogItemSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.title.text = title
        binding.recyclerView.adapter = ItemSelectionAdapter(context, items) { position, item ->
            callback(position, item)
            dismiss()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.cancel.setOnClickListener { dismiss() }
    }
}