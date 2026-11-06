package com.mio.skin

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mio.datastore.appSettingsDataStore
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 皮肤模型点击音效（SoundPool 短音效）：
 * [preload] 幂等初始化并从设置异步校正开关状态，[play] 在开关关闭或未加载完成时静默跳过。
 */
object SkinTapSound {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var soundPool: SoundPool? = null
    private var soundId = 0

    @Volatile
    private var loaded = false

    /** 是否播放点击音效（内存态，与 DataStore 的 skinAnimation.tapSoundEnabled 同步） */
    @Volatile
    var isEnabled = true
        private set

    /** 幂等预加载：创建 SoundPool、载入音效资源，并异步读取持久化的开关状态 */
    fun preload(context: Context) {
        if (soundPool != null) return
        synchronized(this) {
            if (soundPool != null) return
            // usage 为 MEDIA：走媒体音频流，响度跟随系统媒体音量（音量键可调）
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val pool = SoundPool.Builder().setMaxStreams(1).setAudioAttributes(attributes).build()
            pool.setOnLoadCompleteListener { _, _, status ->
                loaded = status == 0
            }
            soundId = pool.load(context.applicationContext, R.raw.skin_tap, 1)
            soundPool = pool
            scope.launch {
                isEnabled = FCLApp.getAppContext()
                    .appSettingsDataStore.data.first().skinAnimation.tapSoundEnabled
            }
        }
    }

    /** 播放点击音效（开关关闭或资源未就绪时不发声） */
    fun play() {
        if (!isEnabled || !loaded) return
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    /** 设置是否播放点击音效，同时更新内存态并持久化 */
    fun setEnabled(value: Boolean) {
        isEnabled = value
        scope.launch {
            FCLApp.getAppContext().appSettingsDataStore.updateData {
                it.copy(skinAnimation = it.skinAnimation.copy(tapSoundEnabled = value))
            }
        }
    }
}
