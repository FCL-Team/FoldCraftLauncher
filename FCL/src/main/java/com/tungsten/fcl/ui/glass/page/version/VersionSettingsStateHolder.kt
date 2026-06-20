package com.tungsten.fcl.ui.glass.page.version

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Lang

class VersionSettingsStateHolder(
    private val profile: Profile,
    val version: String
) {
    private val setting: VersionSetting = profile.getVersionSetting(version)
    private val preferences = FCLPath.CONTEXT?.getSharedPreferences("launcher", Context.MODE_PRIVATE)

    var java by mutableStateOf(setting.java)
    var maxMemory by mutableStateOf(setting.maxMemory.toString())
    var minMemory by mutableStateOf(setting.minMemory?.toString() ?: "")
    var javaArgs by mutableStateOf(setting.javaArgs)
    var minecraftArgs by mutableStateOf(setting.minecraftArgs)
    var serverIp by mutableStateOf(setting.serverIp)
    var autoMemory by mutableStateOf(setting.isAutoMemory)
    var fullscreen by mutableStateOf(setting.isForceResolution)

    private val resolution = parseResolution(preferences?.getString("force_resolution", "1920x1080"))
    var windowWidth by mutableStateOf(resolution.first.toString())
    var windowHeight by mutableStateOf(resolution.second.toString())

    var isolateGameDir by mutableStateOf(setting.isIsolateGameDir)
    var notCheckGame by mutableStateOf(setting.isNotCheckGame)
    var notCheckJVM by mutableStateOf(setting.isNotCheckJVM)

    fun save() {
        setting.java = java
        setting.maxMemory = Lang.parseInt(maxMemory, setting.maxMemory).coerceAtLeast(0)
        setting.minMemory = Lang.parseInt(minMemory, setting.minMemory ?: -1).takeIf { it > 0 }
        setting.javaArgs = javaArgs
        setting.minecraftArgs = minecraftArgs
        setting.serverIp = serverIp
        setting.isAutoMemory = autoMemory
        setting.isForceResolution = fullscreen
        setting.isIsolateGameDir = isolateGameDir
        setting.isNotCheckGame = notCheckGame
        setting.isNotCheckJVM = notCheckJVM

        val width = Lang.parseInt(windowWidth, 1920)
        val height = Lang.parseInt(windowHeight, 1080)
        preferences?.edit()?.putString("force_resolution", "${width}x${height}")?.apply()

        profile.repository.saveVersionSetting(version)
    }

    private fun parseResolution(value: String?): Pair<Int, Int> {
        val text = value ?: "1920x1080"
        return try {
            val parts = text.lowercase().split("x")
            if (parts.size == 2) {
                parts[0].trim().toInt() to parts[1].trim().toInt()
            } else {
                1920 to 1080
            }
        } catch (e: Exception) {
            1920 to 1080
        }
    }
}
