package com.tungsten.fcl.ui.download

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
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ViewModScreenshotBinding
import com.tungsten.fclcore.mod.RemoteMod.Screenshot
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fcllibrary.component.view.FCLTextView


class RemoteModScreenshotAdapter(
    val context: Context,
    private val screenshotList: List<Screenshot>
) :
    RecyclerView.Adapter<RemoteModScreenshotAdapter.ScreenshotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotViewHolder {
        return ScreenshotViewHolder(
            ViewModScreenshotBinding.bind(
                LayoutInflater.from(context).inflate(
                    R.layout.view_mod_screenshot,
                    parent,
                    false
                )
            )
        )
    }

    override fun getItemCount(): Int = screenshotList.size

    override fun onBindViewHolder(holder: ScreenshotViewHolder, position: Int) {
        holder.setScreenshot(screenshotList[position])
    }

    class ScreenshotViewHolder(val binding: ViewModScreenshotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setScreenshot(screenshot: Screenshot) {
            binding.retry.setOnClickListener { loadScreenshotImage(screenshot.imageUrl) }

            binding.screenshot.setImageDrawable(null)
            loadScreenshotImage(screenshot.imageUrl)

            binding.title.setVisibleIfNotBlank(screenshot.title)
            binding.description.setVisibleIfNotBlank(screenshot.description)
        }

        private fun loadScreenshotImage(imageUrl: String) {
            binding.apply {
                setLoading(true)
                Glide.with(screenshot)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            setLoading(false)
                            setFailed()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            setLoading(false)
                            return false
                        }
                    })
                    .into(screenshot)
            }
        }

        private fun setLoading(loading: Boolean) {
            binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) binding.retry.visibility = View.GONE
        }

        private fun setFailed() {
            binding.retry.visibility = View.VISIBLE
        }

        private fun FCLTextView.setVisibleIfNotBlank(text: String?) {
            visibility = if (StringUtils.isNotBlank(text)) View.VISIBLE else View.GONE
            this.text = text
        }
    }
}