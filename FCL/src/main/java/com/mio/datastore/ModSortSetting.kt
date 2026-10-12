package com.mio.datastore

import com.tungsten.fcl.FCLApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 模组列表排序偏好（全局生效），DataStore 持久化。
 */
object ModSortManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val preference = MutableStateFlow(ModSortPreference())
    private val loaded = AtomicBoolean(false)

    @JvmStatic
    fun getField(): ModSortField = current().field

    @JvmStatic
    fun isAscending(): Boolean = current().ascending

    @JvmStatic
    fun set(field: ModSortField, ascending: Boolean) {
        current()
        preference.value = ModSortPreference(field, ascending)
        scope.launch {
            FCLApp.getAppContext().appSettingsDataStore.updateData { it.copy(modSort = ModSortPreference(field, ascending)) }
        }
    }

    private fun current(): ModSortPreference {
        if (loaded.compareAndSet(false, true)) {
            preference.value = runBlocking { FCLApp.getAppContext().appSettingsDataStore.data.first().modSort }
        }
        return preference.value
    }
}
