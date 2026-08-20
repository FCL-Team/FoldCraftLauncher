package com.tungsten.fcllibrary.component.theme

import android.annotation.SuppressLint
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

/** 主题持久化数据（DataStore，替代原 SharedPreferences("theme")） */
@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class ThemePreference(
    val color: Int = 0xFF7797CF.toInt(),
    val color2: Int = 0xFF000000.toInt(),
    val color2Dark: Int = 0xFFFFFFFF.toInt(),
    val fullscreen: Boolean = false,
    val closeSkinModel: Boolean = false,
    val animationSpeed: Int = 8
)

val Context.themeDataStore: DataStore<ThemePreference> by dataStore(
    fileName = "theme.json",
    serializer = ThemePreferenceSerializer
)

object ThemePreferenceSerializer : Serializer<ThemePreference> {
    override val defaultValue: ThemePreference = ThemePreference()

    override suspend fun readFrom(input: InputStream): ThemePreference {
        return try {
            Json.decodeFromString<ThemePreference>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ThemePreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}
