package com.tungsten.fcl.ui.manage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tungsten.fcl.R
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.ListProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fcllibrary.component.FCLAdapter
import com.tungsten.fcllibrary.component.view.FCLImageButton
import com.tungsten.fcllibrary.component.view.FCLLinearLayout
import com.tungsten.fcllibrary.component.view.FCLTextView

class WorldListAdapter(context: Context?) : FCLAdapter(context) {
    private val listProperty: ListProperty<WorldListItem> =
        SimpleListProperty<WorldListItem>(FXCollections.observableArrayList<WorldListItem?>())

    fun listProperty(): ListProperty<WorldListItem> {
        return listProperty
    }

    init {
        listProperty.addListener(InvalidationListener { observable: Observable? ->
            notifyDataSetChanged()
        })
    }

    private class ViewHolder {
        var parent: FCLLinearLayout? = null
        var name: FCLTextView? = null
        var description: FCLTextView? = null
        var datapack: FCLImageButton? = null
        var export: FCLImageButton? = null
        var delete: FCLImageButton? = null
    }

    override fun getCount(): Int {
        return listProperty.getSize()
    }

    override fun getItem(i: Int): Any? {
        return listProperty.get(i)
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_world, null)
            viewHolder.parent = view.findViewById<FCLLinearLayout>(R.id.parent)
            viewHolder.name = view.findViewById<FCLTextView>(R.id.name)
            viewHolder.description = view.findViewById<FCLTextView>(R.id.description)
            viewHolder.datapack = view.findViewById<FCLImageButton>(R.id.datapack)
            viewHolder.export = view.findViewById<FCLImageButton>(R.id.export)
            viewHolder.delete = view.findViewById<FCLImageButton>(R.id.delete)
            view.setTag(viewHolder)
        } else {
            viewHolder = view.getTag() as ViewHolder
        }
        val worldListItem = listProperty.get(i)
        viewHolder.parent!!.setOnClickListener(View.OnClickListener { v: View? -> worldListItem.showInfo() })
        viewHolder.name!!.stringProperty().bind(worldListItem.titleProperty())
        viewHolder.description!!.stringProperty().bind(worldListItem.subtitleProperty())
        viewHolder.datapack!!.setOnClickListener(View.OnClickListener { v: View? -> worldListItem.manageDatapacks() })
        viewHolder.export!!.setOnClickListener(View.OnClickListener { v: View? -> worldListItem.export() })
        viewHolder.delete!!.setOnClickListener(View.OnClickListener { v: View? -> worldListItem.delete() })
        return view
    }
}
