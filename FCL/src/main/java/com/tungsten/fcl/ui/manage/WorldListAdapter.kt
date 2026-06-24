package com.tungsten.fcl.ui.manage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.ViewHolder
import com.tungsten.fcl.databinding.ItemWorldBinding
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections

class WorldListAdapter(private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    private val listProperty: ListProperty<WorldListItem> =
        SimpleListProperty(FXCollections.observableArrayList())

    fun listProperty(): ListProperty<WorldListItem> {
        return listProperty
    }

    init {
        listProperty.addListener { _: Observable? ->
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemWorldBinding.inflate(LayoutInflater.from(context)).root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemWorldBinding.bind(holder.itemView)
        val worldListItem = listProperty[position]
        binding.parent.setOnClickListener { worldListItem.showInfo() }
        binding.name.stringProperty().bind(worldListItem.titleProperty())
        binding.description.stringProperty().bind(worldListItem.subtitleProperty())
        binding.datapack.setOnClickListener { worldListItem.manageDatapacks() }
        binding.export.setOnClickListener { worldListItem.export() }
        binding.delete.setOnClickListener { worldListItem.delete() }
    }

    override fun getItemCount(): Int {
        return listProperty.size
    }
}
