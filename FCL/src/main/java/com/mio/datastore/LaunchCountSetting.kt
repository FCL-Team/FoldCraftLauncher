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

val Context.launchCountDataStore: DataStore<LaunchCountSetting> by dataStore(
    fileName = "launch_count_settings.json",
    serializer = LaunchCountSettingSerializer,
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class LaunchCountSetting(
    val totalLaunchCount: Int = 0,
    val pityCounter: Int = 0,
    val pendingPopup: Boolean = false,
)

object LaunchCountSettingSerializer : Serializer<LaunchCountSetting> {
    override val defaultValue: LaunchCountSetting
        get() = LaunchCountSetting()

    override suspend fun readFrom(input: InputStream): LaunchCountSetting {
        return try {
            Json.decodeFromString<LaunchCountSetting>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: LaunchCountSetting,
        output: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }
}
