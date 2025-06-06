/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.setting

import com.tungsten.fcl.R
import com.tungsten.fcl.util.FXUtils
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.event.EventBus
import com.tungsten.fclcore.event.RefreshedVersionsEvent
import com.tungsten.fclcore.fakefx.beans.Observable
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyStringProperty
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyStringWrapper
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import java.io.File
import java.util.Optional
import java.util.TreeMap
import java.util.function.Consumer

object Profiles {
    private var isFirstRefresh = true

    @JvmStatic
    val profiles =
        FXCollections.observableArrayList<Profile> { arrayOf<Observable>(it) }

    private val selectedProfile =
        object : SimpleObjectProperty<Profile>() {
            init {
                profiles.addListener(FXUtils.onInvalidating { this.invalidated() })
            }

            override fun invalidated() {
                if (!initialized) return
                val profile = get()
                if (!profiles.contains(profile)) {
                    set(profiles[0])
                    return
                }
                ConfigHolder.config().selectedProfile = profile.name
                ConfigHolder.config().selectedProfile = profile.name
                profile.gameDir.resolve(".nomedia").let {
                    if (!it.exists()) {
                        runCatching {
                            it.parentFile?.mkdirs()
                            it.createNewFile()
                        }
                    }
                }
                if (profile.repository.isLoaded) {
                    selectedVersion.bind(profile.selectedVersionProperty())
                } else {
                    selectedVersion.unbind()
                    selectedVersion.set(null)
                    // bind when repository was reloaded.
//                    profile.repository.refreshVersionsAsync().start()
                }
            }
        }

    private fun checkProfiles() {
        if (profiles.isEmpty()) {
            val current = Profile(
                FCLPath.CONTEXT.getString(R.string.profile_shared),
                File(FCLPath.SHARED_COMMON_DIR),
                VersionSetting(),
                null
            )
            val home = Profile(
                FCLPath.CONTEXT.getString(R.string.profile_private),
                File(FCLPath.PRIVATE_COMMON_DIR)
            )
            profiles.addAll(current, home)
        }
    }

    /**
     * True if [.init] hasn't been called.
     */
    private var initialized = false

    init {
        profiles.addListener(FXUtils.onInvalidating { updateProfileStorages() })
        profiles.addListener(FXUtils.onInvalidating { checkProfiles() })

        selectedProfile.addListener { _, _, newValue ->
            if (!isFirstRefresh) {
                newValue.repository.refreshVersionsAsync().start()
            }
        }
    }

    private fun updateProfileStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized) return
        // update storage
        val newConfigurations = TreeMap<String, Profile>()
        for (profile in profiles) {
            newConfigurations.put(profile.name, profile)
        }
        ConfigHolder.config().configurations.value =
            FXCollections.observableMap<String, Profile>(newConfigurations)
    }

    /**
     * Called when it's ready to load profiles from [ConfigHolder.config].
     */
    private val holder = WeakListenerHolder()

    @JvmStatic
    fun init() {
        if (initialized) return

        val names = HashSet<String>()
        ConfigHolder.config().configurations.forEach { (name, profile) ->
            if (!names.add(name)) return@forEach
            profile.name = name
            profiles.add(profile)
        }
        checkProfiles()

        initialized = true
        val profile =
            profiles.find { it.name == ConfigHolder.config().selectedProfile } ?: profiles[0]
        profile.repository.refreshVersions()
        selectedProfile.set(profile)
        holder.add(
            EventBus.EVENT_BUS.channel<RefreshedVersionsEvent?>(RefreshedVersionsEvent::class.java)
                .registerWeak { event ->
                    val profile = selectedProfile.get()
                    if (profile.repository === event.getSource()) {
                        selectedVersion.bind(profile.selectedVersionProperty())
                        for (listener in versionsListeners) listener.accept(profile)
                    }
                }
        )
        isFirstRefresh = false
    }

    @JvmStatic
    fun getSelectedProfile(): Profile {
        return selectedProfile.get() ?: profiles[0]
    }

    @JvmStatic
    fun setSelectedProfile(profile: Profile) {
        selectedProfile.set(profile)
    }

    fun selectedProfileProperty(): ObjectProperty<Profile?> {
        return selectedProfile
    }

    private val selectedVersion = ReadOnlyStringWrapper()

    fun selectedVersionProperty(): ReadOnlyStringProperty {
        return selectedVersion.getReadOnlyProperty()
    }

    // Guaranteed that the repository is loaded.
    @JvmStatic
    fun getSelectedVersion(): String? {
        return selectedVersion.get()
    }

    private val versionsListeners: MutableList<Consumer<Profile>> =
        ArrayList<Consumer<Profile>>(4)

    @JvmStatic
    fun registerVersionsListener(listener: Consumer<Profile>) {
        val profile = getSelectedProfile()
        if (profile.repository.isLoaded) listener.accept(profile)
        versionsListeners.add(listener)
    }

    fun getSelectedGameVersion(): String {
        val gameVersion: Optional<String> =
            getSelectedProfile().repository.getGameVersion(getSelectedVersion())
        return gameVersion.orElse("")
    }
}
