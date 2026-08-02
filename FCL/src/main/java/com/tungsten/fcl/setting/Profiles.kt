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
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.event.EventBus
import com.tungsten.fclcore.event.RefreshedVersionsEvent
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.IdentityHashMap
import java.util.Optional
import java.util.TreeMap
import java.util.function.Consumer

/**
 * 游戏目录仓库（阶段 4a）：列表/选中项/选中版本已全部 StateFlow 化。
 *
 * 元素冒泡（extractor 语义：Profile 内部任何变更 → 回写 configurations 并触发
 * Config 存盘）由对每个 Profile revisionFlow 的直接订阅承接；Config 存盘由
 * [updateProfileStorages] 末尾的 `config().invalidate()` 显式触发（StateFlow
 * 同值不发射，替代原 MapProperty.set 必失效语义），触发点与时机不变。
 */
object Profiles {
    private var isFirstRefresh = true

    private val profilesFlow = MutableStateFlow<List<Profile>>(emptyList())

    /** 列表任何变化（成员增删或元素内部变更）时递增的信号流（供 UI 刷新）。 */
    private val profilesSignal = MutableStateFlow(0L)

    private val profileSubscriptions = IdentityHashMap<Profile, FlowSubscriptions.Subscription>()

    private val selectedProfile = MutableStateFlow<Profile?>(null)

    private val selectedVersion = MutableStateFlow<String?>(null)

    /** 对齐原 ReadOnlyStringWrapper.bind(profile.selectedVersionProperty) 的挂摘。 */
    private var selectedVersionSubscription: FlowSubscriptions.Subscription? = null

    /** 目录列表快照（只读）；任何变化经 [profilesSignalFlow] 通知。 */
    @JvmStatic
    val profiles: List<Profile>
        get() = profilesFlow.value.toList()

    @JvmStatic
    fun profilesSignalFlow(): StateFlow<Long> = profilesSignal

    @JvmStatic
    fun addProfile(profile: Profile) {
        attachProfileSubscription(profile)
        profilesFlow.value = profilesFlow.value + profile
        onProfilesChanged()
    }

    @JvmStatic
    fun removeProfile(profile: Profile) {
        val newList = profilesFlow.value - profile
        if (newList.size != profilesFlow.value.size) {
            detachProfileSubscription(profile)
            profilesFlow.value = newList
            onProfilesChanged()
        }
    }

    private fun attachProfileSubscription(profile: Profile) {
        if (profileSubscriptions.containsKey(profile)) return
        profileSubscriptions[profile] =
            FlowSubscriptions.subscribe(profile.revisionFlow()) { onProfilesChanged() }
    }

    private fun detachProfileSubscription(profile: Profile) {
        profileSubscriptions.remove(profile)?.cancel()
    }

    /**
     * 对齐原 profiles 列表失效监听（注册序：选中项校验 → 回写存储 → 自检），
     * 成员增删与元素冒泡（revision）均走此入口。
     */
    private fun onProfilesChanged() {
        validateSelectedProfile()
        updateProfileStorages()
        checkProfiles()
    }

    private fun validateSelectedProfile() {
        if (!initialized) return
        val profile = selectedProfile.value
        if (!profilesFlow.value.contains(profile)) {
            selectedProfile.value = profilesFlow.value[0]
            return
        }
        ConfigHolder.config().selectedProfile = profile!!.name
        profile.gameDir.resolve(".nomedia").let {
            if (!it.exists()) {
                runCatching {
                    it.parentFile?.mkdirs()
                    it.createNewFile()
                }
            }
        }
        if (profile.repository.isLoaded) {
            bindSelectedVersion(profile)
        } else {
            selectedVersionSubscription?.cancel()
            selectedVersionSubscription = null
            selectedVersion.value = null
            // bind when repository was reloaded.
//            profile.repository.refreshVersionsAsync().start()
        }
    }

    /** 对齐原 selectedVersion.bind(profile.selectedVersionProperty())：先同步当前值再跟随。 */
    private fun bindSelectedVersion(profile: Profile) {
        selectedVersionSubscription?.cancel()
        selectedVersionSubscription =
            FlowSubscriptions.subscribeWithCurrent(profile.selectedVersionFlow()) {
                selectedVersion.value = it
            }
    }

    private fun checkProfiles() {
        if (profilesFlow.value.isEmpty()) {
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
            attachProfileSubscription(current)
            attachProfileSubscription(home)
            profilesFlow.value = listOf(current, home)
        }
    }

    /**
     * True if [.init] hasn't been called.
     */
    private var initialized = false

    init {
        // 对齐原 invalidated()（失效先于 change 触发）：selectedProfile 每次变更
        //（含校验回退）都先执行校验主体，再异步刷新其版本仓库。
        FlowSubscriptions.subscribe(selectedProfile) { validateSelectedProfile() }
        FlowSubscriptions.subscribe(selectedProfile) { newValue ->
            if (!isFirstRefresh) {
                newValue?.repository?.refreshVersionsAsync()?.start()
            }
        }
    }

    private fun updateProfileStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized) return
        // update storage
        val newConfigurations = TreeMap<String, Profile>()
        for (profile in profilesFlow.value) {
            newConfigurations[profile.name] = profile
        }
        ConfigHolder.config().configurations = newConfigurations
        // 阶段 4a：StateFlow 同值不发射，显式触发 Config 存盘
        //（对齐原 MapProperty.set 必失效语义，触发点不变）
        ConfigHolder.config().invalidate()
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
            attachProfileSubscription(profile)
            profilesFlow.value = profilesFlow.value + profile
        }
        checkProfiles()

        initialized = true
        val profile =
            profilesFlow.value.find { it.name == ConfigHolder.config().selectedProfile } ?: profilesFlow.value[0]
        profile.repository.refreshVersions()
        selectedProfile.value = profile
        holder.add(
            EventBus.EVENT_BUS.channel<RefreshedVersionsEvent?>(RefreshedVersionsEvent::class.java)
                .registerWeak { event ->
                    val current = selectedProfile.value
                    if (current!!.repository === event!!.getSource()) {
                        bindSelectedVersion(current)
                        for (listener in versionsListeners) listener.accept(current)
                    }
                }
        )
        isFirstRefresh = false
    }

    @JvmStatic
    fun getSelectedProfile(): Profile {
        checkProfiles()
        return selectedProfile.value ?: profilesFlow.value[0]
    }

    @JvmStatic
    fun setSelectedProfile(profile: Profile) {
        selectedProfile.value = profile
    }

    @JvmStatic
    fun selectedProfileFlow(): StateFlow<Profile?> {
        return selectedProfile
    }

    @JvmStatic
    fun selectedVersionFlow(): StateFlow<String?> {
        return selectedVersion
    }

    // Guaranteed that the repository is loaded.
    @JvmStatic
    fun getSelectedVersion(): String? {
        return selectedVersion.value
    }

    private val versionsListeners: MutableList<Consumer<Profile>> =
        ArrayList(4)

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
