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


val Context.gameItemBarDataStore: DataStore<GameItemBarSetting> by dataStore(
    fileName = "game_item_bar_settings.json",
    serializer = GameItemBarSettingSerializer,
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class GameItemBarSetting(val slideSelection: Boolean, val doubleTapSwapHands: Boolean)
object GameItemBarSettingSerializer : Serializer<GameItemBarSetting> {
    override val defaultValue: GameItemBarSetting
        get() = GameItemBarSetting(slideSelection = true, doubleTapSwapHands = true)

    override suspend fun readFrom(input: InputStream): GameItemBarSetting {
        try {
            return Json.decodeFromString<GameItemBarSetting>(input.readBytes().decodeToString())
        } catch (_: SerializationException) {
            return defaultValue
        }
    }

    override suspend fun writeTo(
        t: GameItemBarSetting,
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
