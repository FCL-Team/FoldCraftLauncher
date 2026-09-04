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

/**
 * 插件管理持久化数据（DataStore）：
 * @param disabledPlugins 被禁用的插件包名列表
 * @param rendererEnvPrefs v2 渲染器插件的用户环境变量配置，
 *   外层 key 为插件包名，内层 key 为环境变量名；值按类型存：
 *   selectable 存选中值、customizable 存输入文本、toggleable 存 "true"/"false"
 */
@Serializable
data class PluginPreference(
    val disabledPlugins: List<String> = emptyList(),
    val rendererEnvPrefs: Map<String, Map<String, String>> = emptyMap(),
)

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
