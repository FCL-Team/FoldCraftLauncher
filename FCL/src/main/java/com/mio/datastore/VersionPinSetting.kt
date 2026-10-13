package com.mio.datastore

import com.tungsten.fcl.FCLApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 版本列表置顶（按 Profile 隔离，置顶顺序即显示顺序），DataStore 持久化。
 */
object VersionPinManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val preference = MutableStateFlow(VersionPinPreference())
    private val loaded = AtomicBoolean(false)

    val pinned: StateFlow<VersionPinPreference> = preference.asStateFlow()

    fun getPinned(profileName: String): List<String> = current().pinned[profileName].orEmpty()

    /** [ids] 为空即取消该 Profile 的全部置顶 */
    fun setPinned(profileName: String, ids: List<String>) {
        current()
        preference.value = VersionPinPreference(preference.value.pinned + (profileName to ids))
        scope.launch {
            FCLApp.getAppContext().appSettingsDataStore.updateData {
                it.copy(versionPin = it.versionPin.copy(pinned = it.versionPin.pinned + (profileName to ids)))
            }
        }
    }

    private fun current(): VersionPinPreference {
        if (loaded.compareAndSet(false, true)) {
            preference.value = runBlocking { FCLApp.getAppContext().appSettingsDataStore.data.first().versionPin }
        }
        return preference.value
    }
}
