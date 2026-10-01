@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.mio.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.tungsten.fcl.FCLApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 模组列表排序字段：DEFAULT 为扫描加载顺序（增量显示），其余为手动排序维度（全量加载后排序显示）
 */
enum class ModSortField {
    DEFAULT, NAME, FILE_NAME, SIZE, DATE
}

@Serializable
data class ModSortPreference(
    val field: ModSortField = ModSortField.DEFAULT,
    val ascending: Boolean = true,
)

val Context.modSortDataStore: DataStore<ModSortPreference> by dataStore(
    fileName = "mod_sort_settings.json",
    serializer = ModSortPreferenceSerializer,
)

object ModSortPreferenceSerializer : Serializer<ModSortPreference> {
    override val defaultValue: ModSortPreference = ModSortPreference()

    override suspend fun readFrom(input: InputStream): ModSortPreference {
        return try {
            Json.decodeFromString<ModSortPreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ModSortPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}

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
            FCLApp.getAppContext().modSortDataStore.updateData { ModSortPreference(field, ascending) }
        }
    }

    private fun current(): ModSortPreference {
        if (loaded.compareAndSet(false, true)) {
            preference.value = runBlocking { FCLApp.getAppContext().modSortDataStore.data.first() }
        }
        return preference.value
    }
}
