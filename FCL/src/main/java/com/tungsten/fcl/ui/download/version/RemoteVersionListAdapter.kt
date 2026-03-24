package com.tungsten.fcl.ui.download.version

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.mio.util.AnimUtil
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemRemoteVersionBinding
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.download.fabric.FabricAPIRemoteVersion
import com.tungsten.fclcore.download.fabric.FabricRemoteVersion
import com.tungsten.fclcore.download.forge.ForgeRemoteVersion
import com.tungsten.fclcore.download.game.GameRemoteVersion
import com.tungsten.fclcore.download.liteloader.LiteLoaderRemoteVersion
import com.tungsten.fclcore.download.neoforge.NeoForgeRemoteVersion
import com.tungsten.fclcore.download.optifine.OptiFineRemoteVersion
import com.tungsten.fclcore.download.quilt.QuiltAPIRemoteVersion
import com.tungsten.fclcore.download.quilt.QuiltRemoteVersion
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils

class RemoteVersionListAdapter(val context: Context, private val list: ArrayList<RemoteVersion>, private val listener: OnRemoteVersionSelectListener) :
    RecyclerView.Adapter<RemoteVersionListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemRemoteVersionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val binding = ItemRemoteVersionBinding.bind(holder.itemView)
        val remoteVersion: RemoteVersion = list[position]
        binding.root.setOnClickListener {
            listener.onSelect(
                remoteVersion
            )
        }
        binding.icon.background = getIcon(remoteVersion)
        binding.version.text = remoteVersion.selfVersion
        binding.tag.background = AppCompatResources.getDrawable(context,R.drawable.bg_container_white)
        binding.tag.isAutoBackgroundTint = true
        binding.tag.setBackgroundTintList(
            ColorStateList(
                arrayOf<IntArray?>(intArrayOf()),
                intArrayOf(ThemeEngine.getInstance().getTheme().color)
            )
        )
        binding.tag.text = getTag(remoteVersion)
        binding.date.visibility = if (remoteVersion.releaseDate == null) View.GONE else View.VISIBLE
        binding.date.text = if (remoteVersion.releaseDate == null) "" else LocaleUtils.formatDateTime(
            context,
            remoteVersion.releaseDate
        )
        if (remoteVersion is GameRemoteVersion && (remoteVersion.versionType == RemoteVersion.Type.RELEASE || remoteVersion.versionType == RemoteVersion.Type.SNAPSHOT || remoteVersion.versionType == RemoteVersion.Type.UNOBFUSCATED)) {
            binding.wiki.setVisibility(View.VISIBLE)
            val wikiUrlSuffix: String =
                getWikiUrlSuffix(context, remoteVersion.gameVersion)
            binding.wiki.setOnClickListener {
                AndroidUtils.openLink(
                    context,
                    context.getString(R.string.wiki_game, wikiUrlSuffix)
                )
            }
        } else {
            binding.wiki.setVisibility(View.GONE)
        }
        if ((remoteVersion !is GameRemoteVersion) && (remoteVersion !is FabricAPIRemoteVersion) && (remoteVersion !is QuiltAPIRemoteVersion)) {
            binding.save.setVisibility(View.VISIBLE)
            binding.save.setOnClickListener {
                val urls = remoteVersion.urls
                AlertDialog.Builder(context)
                    .setTitle(R.string.message_select_url)
                    .setItems(
                        urls.toTypedArray<String?>()
                    ) { _: DialogInterface?, w: Int ->
                        AndroidUtils.openLink(
                            context,
                            urls[w]
                        )
                    }
                    .setNegativeButton(R.string.button_cancel, null)
                    .create().show()
            }
        } else {
            binding.save.setVisibility(View.GONE)
        }
        AnimUtil.playTranslationX(
            binding.root,
            ThemeEngine.getInstance().getTheme().animationSpeed * 30L,
            -100f,
            0f
        ).start()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getIcon(remoteVersion: RemoteVersion?): Drawable? {
        when (remoteVersion) {
            is LiteLoaderRemoteVersion -> return AppCompatResources.getDrawable(
                context,
                R.drawable.img_chicken
            )

            is OptiFineRemoteVersion -> return AppCompatResources.getDrawable(
                context,
                R.drawable.img_optifine
            )

            is ForgeRemoteVersion -> return AppCompatResources.getDrawable(
                context,
                R.drawable.img_forge
            )

            is NeoForgeRemoteVersion -> return AppCompatResources.getDrawable(
                context,
                R.drawable.img_neoforge
            )

            is FabricRemoteVersion, is FabricAPIRemoteVersion -> return AppCompatResources.getDrawable(
                context,
                R.drawable.img_fabric
            )

            is QuiltRemoteVersion, is QuiltAPIRemoteVersion -> return AppCompatResources.getDrawable(
                context,
                R.drawable.img_quilt
            )

            is GameRemoteVersion -> {
                when (remoteVersion.versionType) {
                    RemoteVersion.Type.RELEASE -> return AppCompatResources.getDrawable(
                        context,
                        R.drawable.img_grass
                    )

                    RemoteVersion.Type.PENDING, RemoteVersion.Type.UNOBFUSCATED, RemoteVersion.Type.SNAPSHOT -> {
                        if (GameVersionNumber.asGameVersion(remoteVersion.gameVersion)
                                .isAprilFools()
                        ) {
                            return AppCompatResources.getDrawable(context, R.drawable.april_fools)
                        }
                        return AppCompatResources.getDrawable(context, R.drawable.img_command)
                    }

                    else -> return AppCompatResources.getDrawable(context, R.drawable.img_craft_table)
                }
            }

            else -> {
                return AppCompatResources.getDrawable(context, R.drawable.img_grass)
            }
        }
    }

    private fun getTag(remoteVersion: RemoteVersion): String? {
        return if (remoteVersion is GameRemoteVersion) {
            when (remoteVersion.versionType) {
                RemoteVersion.Type.RELEASE -> context.getString(R.string.version_game_release)
                RemoteVersion.Type.UNOBFUSCATED, RemoteVersion.Type.PENDING, RemoteVersion.Type.SNAPSHOT -> context.getString(
                    R.string.version_game_snapshot
                )

                else -> context.getString(R.string.version_game_old)
            }
        } else {
            remoteVersion.gameVersion
        }
    }

    private fun getWikiUrlSuffix(context: Context, gameVersion: String): String {
        val id = gameVersion.lowercase()

        when (id) {
            "0.30-1", "0.30-2", "c0.30_01c" -> return context.getString(
                R.string.wiki_game_search,
                "Classic_0.30"
            )

            "in-20100206-2103" -> return context.getString(
                R.string.wiki_game_search,
                "Indev_20100206"
            )

            "inf-20100630-1" -> return context.getString(
                R.string.wiki_game_search,
                "Infdev_20100630"
            )

            "inf-20100630-2" -> return context.getString(R.string.wiki_game_search, "Alpha_v1.0.0")
            "1.19_deep_dark_experimental_snapshot-1" -> return "1.19-exp1"
            "in-20100130" -> return context.getString(
                R.string.wiki_game_search,
                "Indev_0.31_20100130"
            )

            "b1.6-tb3" -> return context.getString(
                R.string.wiki_game_search,
                "Beta_1.6_Test_Build_3"
            )
        }

        if (id.startsWith("1.0.0-rc2")) return "RC2"
        if (id.startsWith("2.0")) return context.getString(R.string.wiki_game_search, "2.0")
        if (id.startsWith("b1.8-pre1")) return "Beta_1.8-pre1"
        if (id.startsWith("b1.1-")) return context.getString(R.string.wiki_game_search, "Beta_1.1")
        if (id.startsWith("a1.1.0")) return "Alpha_v1.1.0"
        if (id.startsWith("a1.0.14")) return "Alpha_v1.0.14"
        if (id.startsWith("a1.0.13_01")) return "Alpha_v1.0.13_01"
        if (id.startsWith("in-20100214")) return context.getString(
            R.string.wiki_game_search,
            "Indev_20100214"
        )

        if (id.contains("experimental-snapshot")) {
            return id.replace("-experimental-snapshot", "-exp")
        }

        if (id.startsWith("inf-")) return id.replace("inf-", "Infdev_")
        if (id.startsWith("in-")) return id.replace("in-", "Indev_")
        if (id.startsWith("rd-")) return "pre-Classic_$id"
        if (id.startsWith("b")) return id.replace("b", "Beta_")
        if (id.startsWith("a")) return id.replace("a", "Alpha_v")
        if (id.startsWith("c")) return id.replace("c", "Classic_").replace("st", "SURVIVAL_TEST")

        return id
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    interface OnRemoteVersionSelectListener {
        fun onSelect(remoteVersion: RemoteVersion)
    }
}