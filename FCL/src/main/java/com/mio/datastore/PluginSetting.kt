@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.mio.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/** 插件管理持久化数据（DataStore）：被禁用的插件包名列表 */
@Serializable
data class PluginPreference(val disabledPlugins: List<String> = emptyList())

val Context.pluginDataStore: DataStore<PluginPreference> by dataStore(
    fileName = "plugin_settings.json",
    serializer = PluginPreferenceSerializer,
)

object PluginPreferenceSerializer : Serializer<PluginPreference> {
    override val defaultValue: PluginPreference = PluginPreference()

    override suspend fun readFrom(input: InputStream): PluginPreference {
        return try {
            Json.decodeFromString<PluginPreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: PluginPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}
