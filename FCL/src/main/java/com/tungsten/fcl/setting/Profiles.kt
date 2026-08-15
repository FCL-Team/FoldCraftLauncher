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
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyStringProperty
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyStringWrapper
import com.tungsten.fclcore.fakefx.collections.FXCollections
import java.io.File
import java.util.Optional
import java.util.TreeMap
import java.util.function.Consumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object Profiles {
    private var isFirstRefresh = true

    @JvmStatic
    val profiles =
        FXCollections.observableArrayList<Profile> { arrayOf<Observable>(it) }

    private val _selectedProfile = MutableStateFlow<Profile?>(null)

    /** 当前选中的 Profile（Repository 单例状态，Java 侧访问 getSelectedProfileFlow()） */
    @get:JvmName("getSelectedProfileFlow")
    val selectedProfile: StateFlow<Profile?> = _selectedProfile.asStateFlow()

    /** 选中 Profile 变化的监听者（setter 同步通知，调用线程即回调线程） */
    private val selectedProfileListeners = mutableListOf<Runnable>()

    init {
        profiles.addListener(FXUtils.onInvalidating { updateProfileStorages() })
        profiles.addListener(FXUtils.onInvalidating { checkProfiles() })
        // 列表变化时校验选中项仍在列表中（原 fakefx property invalidated 逻辑）
        profiles.addListener(FXUtils.onInvalidating {
            val current = _selectedProfile.value
            if (current != null && !profiles.contains(current)) {
                setSelectedProfileInternal(profiles[0])
            }
        })
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
        setSelectedProfileInternal(profile)
        holder.add(
            EventBus.EVENT_BUS.channel<RefreshedVersionsEvent?>(RefreshedVersionsEvent::class.java)
                .registerWeak { event ->
                    val profile = _selectedProfile.value ?: return@registerWeak
                    if (profile.repository === event!!.getSource()) {
                        selectedVersion.bind(profile.selectedVersionProperty())
                        for (listener in versionsListeners) listener.accept(profile)
                    }
                }
        )
        isFirstRefresh = false
    }

    /** 设置选中 Profile（统一走校验、保存与版本绑定逻辑，同步通知监听者） */
    private fun setSelectedProfileInternal(profile: Profile) {
        if (_selectedProfile.value === profile) return
        _selectedProfile.value = profile
        if (!initialized) return
        if (!profiles.contains(profile)) {
            _selectedProfile.value = profiles[0]
        } else {
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
        selectedProfileListeners.forEach { listener -> listener.run() }
        if (!isFirstRefresh) {
            profile.repository.refreshVersionsAsync().start()
        }
    }

    @JvmStatic
    fun getSelectedProfile(): Profile {
        checkProfiles()
        return _selectedProfile.value ?: profiles[0]
    }

    @JvmStatic
    fun setSelectedProfile(profile: Profile) {
        setSelectedProfileInternal(profile)
    }

    /** 注册选中 Profile 变化监听（Java 友好，内部基于 StateFlow collect） */
    @JvmStatic
    fun addSelectedProfileListener(listener: Runnable) {
        selectedProfileListeners.add(listener)
    }

    @JvmStatic
    fun removeSelectedProfileListener(listener: Runnable) {
        selectedProfileListeners.remove(listener)
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
        ArrayList(4)

    @JvmStatic
    fun registerVersionsListener(listener: Consumer<Profile>) {
        val profile = getSelectedProfile()
        if (profile.repository.isLoaded) listener.accept(profile)
        versionsListeners.add(listener)
    }

    @JvmStatic
    fun unregisterVersionsListener(listener: Consumer<Profile>) {
        versionsListeners.remove(listener)
    }

    fun getSelectedGameVersion(): String {
        val gameVersion: Optional<String> =
            getSelectedProfile().repository.getGameVersion(getSelectedVersion())
        return gameVersion.orElse("")
    }
}
