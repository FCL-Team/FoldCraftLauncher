package com.mio.plugin

import com.mio.datastore.PluginPreference
import com.mio.datastore.pluginDataStore
import com.tungsten.fcl.FCLApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * MioLibPatcher 管理：启用/禁用与功能开关，DataStore（plugin_settings.json）持久化。
 * 功能开关对应 MioLibPatcher README 的系统属性，默认全部关闭（与 README 默认一致）；
 * 启用状态默认 true，沿袭原先无条件的 -javaagent 注入行为。
 */
object MioLibPatcherManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val preference = MutableStateFlow<PluginPreference>(PluginPreference())

    @Volatile
    private var loaded = false

    @JvmStatic
    fun isEnabled(): Boolean = snapshot().miolibpatcherEnabled

    @JvmStatic
    fun isAlc10(): Boolean = snapshot().miolibpatcherAlc10

    @JvmStatic
    fun isSablerapier(): Boolean = snapshot().miolibpatcherSablerapier

    @JvmStatic
    fun isAsmBackport(): Boolean = snapshot().miolibpatcherAsmBackport

    @JvmStatic
    fun setEnabled(enabled: Boolean) {
        update { it.copy(miolibpatcherEnabled = enabled) }
    }

    @JvmStatic
    fun setAlc10(enabled: Boolean) {
        update { it.copy(miolibpatcherAlc10 = enabled) }
    }

    @JvmStatic
    fun setSablerapier(enabled: Boolean) {
        update { it.copy(miolibpatcherSablerapier = enabled) }
    }

    @JvmStatic
    fun setAsmBackport(enabled: Boolean) {
        update { it.copy(miolibpatcherAsmBackport = enabled) }
    }

    /** 启动 JVM 时按开关生成的功能属性参数（完整 -D 参数，启用时由启动器追加）。
     *  ALC10/ASM 后门默认关闭、开启时置属性；Sable Rapier 默认开启（不设置属性），
     *  关闭时设置 false 强制禁用（对应 MioLibPatcher README 的 override 逻辑） */
    @JvmStatic
    fun getJvmOptions(): List<String> {
        val pref = snapshot()
        return buildList {
            if (pref.miolibpatcherAlc10) add("-Dmiolibpatcher.alc10=true")
            if (!pref.miolibpatcherSablerapier) add("-Dmiolibpatcher.sablerapier=false")
            if (pref.miolibpatcherAsmBackport) add("-Dmiolibpatcher.asmBackport=true")
        }
    }

    /** 首次访问时同步加载持久化配置（小文件，仅一次），与 PluginManager 同模式 */
    private fun ensureLoaded() {
        if (loaded) return
        synchronized(this) {
            if (loaded) return
            preference.value = runBlocking { FCLApp.getAppContext().pluginDataStore.data.first() }
            loaded = true
        }
    }

    private fun snapshot(): PluginPreference {
        ensureLoaded()
        return preference.value
    }

    private fun update(transform: (PluginPreference) -> PluginPreference) {
        snapshot()
        preference.update(transform)
        scope.launch {
            FCLApp.getAppContext().pluginDataStore.updateData { transform(it) }
        }
    }
}