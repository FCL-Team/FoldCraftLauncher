package com.tungsten.fcl.ui.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mio.util.AnimUtil.Companion.playTranslationX
import com.tungsten.fcl.R
import com.tungsten.fcl.control.download.ControllerCategory
import com.tungsten.fcl.control.download.ControllerIndex
import com.tungsten.fcl.databinding.ItemRemoteVersionBinding
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.util.function.Consumer

class ControllerListAdapter(
    private val context: Context,
    source: Int,
    private val categories: ArrayList<ControllerCategory>,
    private val list: ArrayList<ControllerIndex>,
    private val callback: Callback
) : RecyclerView.Adapter<ControllerListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val repoUrl: String =
        if (source == 0) ControllerRepoPage.CONTROLLER_GITHUB else ControllerRepoPage.CONTROLLER_GIT_CN

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_remote_version, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val index = list[position]
        val binding = ItemRemoteVersionBinding.bind(holder.itemView)
        binding.parent.setOnClickListener {
            callback.onItemSelect(
                index
            )
        }
        binding.icon.setImageDrawable(null)
        val iconUrl = repoUrl + "repo_json/" + index.id + "/icon.png"
        Glide.with(context).load(iconUrl).into(binding.icon)
        binding.version.text = index.name
        val categories: MutableList<String?> = ControllerCategory.getLocaledCategories(
            context,
            this.categories,
            index.categories
        )
        val stringBuilder = StringBuilder()
        categories.forEach(Consumer { it: String? -> stringBuilder.append(it).append("   ") })
        val tag = StringUtils.removeSuffix(stringBuilder.toString(), "   ")
        binding.tag.text = tag
        binding.date.text = index.introduction
        playTranslationX(
            binding.root,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Callback {
        fun onItemSelect(mod: ControllerIndex?)
    }
}
