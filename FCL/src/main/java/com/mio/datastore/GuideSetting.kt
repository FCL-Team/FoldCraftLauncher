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
 * 功能引导持久化数据（DataStore）：
 * @param shownTags 已展示过的引导标识列表，避免引导重复弹出
 */
@Serializable
data class GuidePreference(
    val shownTags: List<String> = emptyList(),
)

val Context.guideDataStore: DataStore<GuidePreference> by dataStore(
    fileName = "guide_settings.json",
    serializer = GuidePreferenceSerializer,
)

object GuidePreferenceSerializer : Serializer<GuidePreference> {
    override val defaultValue: GuidePreference = GuidePreference()

    override suspend fun readFrom(input: InputStream): GuidePreference {
        return try {
            Json.decodeFromString<GuidePreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: GuidePreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}
