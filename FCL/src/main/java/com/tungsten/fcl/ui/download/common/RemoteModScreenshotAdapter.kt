package com.tungsten.fcl.ui.download.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mio.ui.adapter.ViewHolder
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ViewModScreenshotBinding
import com.tungsten.fclcore.mod.RemoteMod.Screenshot
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.dialog.FullImageDialog
import com.tungsten.fcllibrary.component.view.FCLTextView


class RemoteModScreenshotAdapter(
    val context: Context,
    private val screenshotList: List<Screenshot>
) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewModScreenshotBinding.inflate(LayoutInflater.from(context)).root
        )
    }

    override fun getItemCount(): Int = screenshotList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ViewModScreenshotBinding.bind(holder.itemView)
        val screenshot = screenshotList[position]

        binding.screenshot.setImageDrawable(null)
        loadScreenshotImage(binding, screenshot.imageUrl)

        binding.screenshot.setOnClickListener {
            loadScreenshotImage(binding, screenshot.imageUrl)
        }
        binding.screenshot.setOnLongClickListener {
            showFullImageDialog(screenshot.imageUrl)
            true
        }

        binding.title.setVisibleIfNotBlank(screenshot.title)
        binding.description.setVisibleIfNotBlank(screenshot.description)
    }

    private fun showFullImageDialog(imageUrl: String) {
        val dialog = FullImageDialog(context).apply { show() }
        Glide.with(dialog.getImageView())
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .into(dialog.getImageView())
    }

    private fun loadScreenshotImage(binding: ViewModScreenshotBinding, imageUrl: String) {
        binding.loading.visibility = View.VISIBLE
        Glide.with(binding.screenshot)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .error(R.drawable.ic_baseline_refresh_24)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loading.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loading.visibility = View.GONE
                    return false
                }
            })
            .into(binding.screenshot)
    }

    private fun FCLTextView.setVisibleIfNotBlank(text: String?) {
        visibility = if (StringUtils.isNotBlank(text)) View.VISIBLE else View.GONE
        this.text = text
    }
}