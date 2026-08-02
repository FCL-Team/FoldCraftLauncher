package com.tungsten.fcl.ui.manage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.ViewHolder
import com.tungsten.fcl.databinding.ItemWorldBinding
import com.tungsten.fcl.util.FlowList
import com.tungsten.fclcore.util.flow.FlowSubscriptions

class WorldListAdapter(private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    private val listProperty: FlowList<WorldListItem> = FlowList()

    fun listProperty(): FlowList<WorldListItem> {
        return listProperty
    }

    init {
        FlowSubscriptions.subscribe(listProperty.flow()) { _: List<WorldListItem> ->
            notifyDataSetChanged()
        }
    }

    // 视图回收重绑时持有的订阅（存于 itemView.tag），重绑前 cancel 旧订阅
    //（对齐原 bind 重复调用先解绑的语义）。
    private class BindingSubscriptions {
        var title: FlowSubscriptions.Subscription? = null
        var subtitle: FlowSubscriptions.Subscription? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemWorldBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemWorldBinding.bind(holder.itemView)
        val worldListItem = listProperty.get()[position]
        val subscriptions =
            holder.itemView.tag as? BindingSubscriptions ?: BindingSubscriptions().also {
                holder.itemView.tag = it
            }
        binding.parent.setOnClickListener { worldListItem.showInfo() }
        subscriptions.title?.cancel()
        binding.name.stringFlow().value = worldListItem.titleFlow().value
        subscriptions.title = FlowSubscriptions.subscribe(worldListItem.titleFlow()) { v ->
            binding.name.stringFlow().value = v
        }
        subscriptions.subtitle?.cancel()
        binding.description.stringFlow().value = worldListItem.subtitleFlow().value
        subscriptions.subtitle = FlowSubscriptions.subscribe(worldListItem.subtitleFlow()) { v ->
            binding.description.stringFlow().value = v
        }
        binding.datapack.setOnClickListener { worldListItem.manageDatapacks() }
        binding.export.setOnClickListener { worldListItem.export() }
        binding.delete.setOnClickListener { worldListItem.delete() }
    }

    override fun getItemCount(): Int {
        return listProperty.size()
    }
}
