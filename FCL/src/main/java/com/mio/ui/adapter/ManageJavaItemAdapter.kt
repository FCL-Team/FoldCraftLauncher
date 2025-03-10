package com.mio.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemManageJavaBinding
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.game.JavaVersion
import java.io.File

class ManageJavaItemAdapter(
    val context: Context,
    val versions: List<JavaVersion>,
    val action: (JavaVersion, Boolean) -> Unit
) :
    RecyclerView.Adapter<ManageJavaItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_manage_java, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemManageJavaBinding.bind(holder.itemView)
        val data = versions[position]
        binding.javaName.text = data.name
        binding.javaVersion.text = data.versionName
        if (File(FCLPath.JAVA_PATH, data.name).resolve("version").exists()) {
            binding.delete.visibility = View.INVISIBLE
            binding.javaName.text = "${data.name} (${context.getString(R.string.internal)})"
        } else {
            binding.delete.setOnClickListener {
                action.invoke(data, true)
            }
        }
        binding.root.setOnClickListener {
            action.invoke(data, false)
        }
    }

    override fun getItemCount(): Int {
        return versions.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}