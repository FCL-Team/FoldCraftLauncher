package com.tungsten.fcl.ui.version

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonParseException
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageVersionListBinding
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles.getSelectedProfile
import com.tungsten.fcl.setting.Profiles.profiles
import com.tungsten.fcl.setting.Profiles.registerVersionsListener
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.fakefx.beans.binding.Bindings
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import java.util.logging.Level
import java.util.stream.Collectors

class VersionListPage(context: Context?, id: Int, parent: FCLUILayout?, resId: Int) :
    FCLCommonPage(context, id, parent, resId), View.OnClickListener {
    private lateinit var binding: PageVersionListBinding
    private var adapter: VersionListAdapter? = null
    private lateinit var children: MutableList<VersionListItem>
    private var textWatcher: TextWatcher? = null

    override fun onCreate() {
        super.onCreate()
        binding = PageVersionListBinding.bind(contentView)
        binding.refresh.setOnClickListener(this)
        binding.newProfile.setOnClickListener(this)
        registerVersionsListener { loadVersions(it) }
        refreshProfile()
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val text = s.toString()
                if (text.isEmpty()) {
                    binding.versionList.adapter = adapter
                } else {
                    val newAdapter =
                        VersionListAdapter(context, children.filter {
                            it.version.lowercase(
                                Locale.getDefault()
                            ).contains(text.lowercase(Locale.getDefault()))
                        } as ArrayList)
                    binding.versionList.adapter = newAdapter
                }
            }
        }
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return Task.runAsync {}
    }

    fun refreshProfile() {
        val adapter = ProfileListAdapter(context, profiles)
        binding.profileList.adapter = adapter
    }

    private fun loadVersions(profile: Profile) {
        MainActivity.getInstance().lifecycleScope.launch {
            binding.search.removeTextChangedListener(textWatcher)
            binding.search.setText("")
            binding.refresh.isEnabled = false
            binding.versionList.visibility = View.GONE
            binding.progress.visibility = View.VISIBLE
        }
        val repository = profile.repository
        MainActivity.getInstance().lifecycleScope.launch {
            if (profile == getSelectedProfile()) {
                children = withContext(Dispatchers.IO) {
                    repository.displayVersions
                        .parallel()
                        .map { version: Version ->
                            val game = profile.repository.getGameVersion(version.id)
                            val libraries =
                                StringBuilder(game.orElse(context.getString(R.string.message_unknown)))
                            val analyzer = LibraryAnalyzer.analyze(
                                profile.repository.getResolvedPreservingPatchesVersion(
                                    version.id
                                ), game.orElse(null)
                            )
                            for (mark in analyzer) {
                                val libraryId = mark.libraryId
                                val libraryVersion = mark.libraryVersion
                                if (libraryId == LibraryAnalyzer.LibraryType.MINECRAFT.patchId) continue
                                if (AndroidUtils.hasStringId(
                                        context,
                                        "install_installer_" + libraryId.replace("-", "_")
                                    )
                                ) {
                                    libraries.append(", ").append(
                                        AndroidUtils.getLocalizedText(
                                            context,
                                            "install_installer_" + libraryId.replace("-", "_")
                                        )
                                    )
                                    if (libraryVersion != null) libraries.append(": ").append(
                                        libraryVersion.replace(
                                            ("(?i)$libraryId").toRegex(),
                                            ""
                                        )
                                    )
                                }
                            }
                            var tag: String? = null
                            try {
                                val config: ModpackConfiguration<*>? =
                                    profile.repository.readModpackConfiguration<Any?>(
                                        version.id
                                    )
                                if (config != null) tag = config.version
                            } catch (e: IOException) {
                                Logging.LOG.log(
                                    Level.WARNING,
                                    "Failed to read modpack configuration from $version",
                                    e
                                )
                            } catch (e: JsonParseException) {
                                Logging.LOG.log(
                                    Level.WARNING,
                                    "Failed to read modpack configuration from $version",
                                    e
                                )
                            }
                            return@map VersionListItem(
                                profile,
                                version.id,
                                libraries.toString(),
                                tag,
                                repository.getVersionIconImage(version.id)
                            )
                        }
                        .collect(Collectors.toList())
                }
                if (profile == getSelectedProfile()) {
                    adapter = VersionListAdapter(
                        context,
                        children as ArrayList
                    )
                    binding.versionList.adapter = adapter
                    binding.refresh.isEnabled = true
                    binding.versionList.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.search.addTextChangedListener(textWatcher)
                }
                children.forEach {
                    it.selectedProperty().bind(
                        Bindings.createBooleanBinding({
                            profile.selectedVersionProperty().get() == it.version
                        }, profile.selectedVersionProperty())
                    )
                }
            }
        }
    }

    override fun onClick(view: View?) {
        if (view === binding.refresh) {
            getSelectedProfile().repository.refreshVersionsAsync().start()
        }
        if (view === binding.newProfile) {
            val dialog = AddProfileDialog(context)
            dialog.show()
        }
    }
}
