package com.mio.audio

import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.tungsten.fclcore.util.Logging
import java.util.logging.Level

/** Holds the game's communication route while its activity is visible. */
@RequiresApi(Build.VERSION_CODES.S)
class BluetoothMicrophoneRoute(private val audioManager: AudioManager) {
    private var started = false
    private var requestedDeviceId: Int? = null
    private val callbacks = object : AudioDeviceCallback() {
        override fun onAudioDevicesAdded(addedDevices: Array<AudioDeviceInfo>) = updateRoute()

        override fun onAudioDevicesRemoved(removedDevices: Array<AudioDeviceInfo>) = updateRoute()
    }

    fun start() {
        if (started) return
        started = true
        audioManager.registerAudioDeviceCallback(callbacks, Handler(Looper.getMainLooper()))
        updateRoute()
    }

    fun stop() {
        if (!started) return
        started = false
        audioManager.unregisterAudioDeviceCallback(callbacks)
        releaseRoute()
    }

    private fun updateRoute() {
        if (!started) return
        val available = audioManager.availableCommunicationDevices
        val preferredId = requestedDeviceId ?: audioManager.communicationDevice?.id
        val device = available.firstOrNull { it.id == preferredId && it.isBluetoothHeadset() }
            ?: available.firstOrNull { it.type == AudioDeviceInfo.TYPE_BLE_HEADSET }
            ?: available.firstOrNull { it.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO }
        if (device == null) {
            releaseRoute()
            return
        }
        if (device.id == requestedDeviceId) return

        if (audioManager.setCommunicationDevice(device)) {
            if (requestedDeviceId == null) {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            }
            requestedDeviceId = device.id
        } else {
            releaseRoute()
            // A connected headset can disappear between enumeration and the request.
            Logging.LOG.log(Level.FINE, "Bluetooth headset communication route was unavailable")
        }
    }

    private fun releaseRoute() {
        if (requestedDeviceId == null) return
        requestedDeviceId = null
        audioManager.clearCommunicationDevice()
        audioManager.mode = AudioManager.MODE_NORMAL
    }

    private fun AudioDeviceInfo.isBluetoothHeadset() =
        type == AudioDeviceInfo.TYPE_BLE_HEADSET || type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO

    companion object {
        const val PREFERENCE_KEY = "preferBluetoothMicrophone"
    }
}
