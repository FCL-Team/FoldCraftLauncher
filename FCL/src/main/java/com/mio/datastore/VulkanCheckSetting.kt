@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.mio.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.mio.device.VulkanCheckRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Vulkan 检测持久化数据（DataStore）：
 * @param record 最近一次设备 Vulkan 能力检测结果，null 表示尚未检测过
 */
@Serializable
data class VulkanCheckPreference(
    val record: VulkanCheckRecord? = null,
)

val Context.vulkanCheckDataStore: DataStore<VulkanCheckPreference> by dataStore(
    fileName = "vulkan_check.json",
    serializer = VulkanCheckPreferenceSerializer,
)

object VulkanCheckPreferenceSerializer : Serializer<VulkanCheckPreference> {
    override val defaultValue: VulkanCheckPreference = VulkanCheckPreference()

    override suspend fun readFrom(input: InputStream): VulkanCheckPreference {
        return try {
            Json.decodeFromString<VulkanCheckPreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: VulkanCheckPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}
