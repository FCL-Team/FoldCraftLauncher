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

val Context.skinAnimationDataStore: DataStore<SkinAnimationSetting> by dataStore(
    fileName = "skin_animation_settings.json",
    serializer = SkinAnimationSettingSerializer,
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class SkinAnimationSetting(val animationId: String = "walking")

object SkinAnimationSettingSerializer : Serializer<SkinAnimationSetting> {
    override val defaultValue: SkinAnimationSetting
        get() = SkinAnimationSetting(animationId = "walking")

    override suspend fun readFrom(input: InputStream): SkinAnimationSetting {
        return try {
            Json.decodeFromString<SkinAnimationSetting>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: SkinAnimationSetting,
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
