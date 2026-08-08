/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.setting

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.mio.JavaManager
import com.mio.data.Renderer
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Lang
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fclcore.util.platform.MemoryUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.reflect.Type

/**
 * 版本设置（阶段 4a）：全部属性已 StateFlow 化（`xxxFlow`），var 读写接口签名不变。
 * [addPropertyChangedListener] 对齐原"任一属性失效即回调"语义：订阅所有 Flow
 * （同值 set 不发射，与原 Property 同值不失效一致），供 FCLGameRepository 落盘、
 * Profile 冒泡 revision 使用。
 *
 * 磁盘 JSON 由手写 [Serializer] 产出，与属性类型无关，格式不变。
 */
@JsonAdapter(VersionSetting.Serializer::class)
class VersionSetting : Cloneable {
    var isGlobal: Boolean = false

    val usesGlobalFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isUsesGlobal: Boolean
        /**
         * FCL Version Settings have been divided into 2 parts.
         * 1. Global settings.
         * 2. Version settings.
         * If a version claims that it uses global settings, its version setting will be disabled.
         *
         *
         * Defaults false because if one version uses global first, custom version file will not be generated.
         */
        get() = usesGlobalFlow.value
        set(usesGlobal) {
            usesGlobalFlow.value = usesGlobal
        }

    // java
    val javaFlow: MutableStateFlow<String> = MutableStateFlow("Auto")
    var java: String
        get() = javaFlow.value
        set(java) {
            javaFlow.value = java
        }

    val uuidFlow: MutableStateFlow<String> = MutableStateFlow("")
    var uuid: String
        get() = uuidFlow.value
        set(value) {
            uuidFlow.value = value
        }

    val maxMemoryFlow: MutableStateFlow<Int> =
        MutableStateFlow(MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT))
    var maxMemory: Int
        /**
         * The maximum memory/MB that JVM can allocate for heap.
         */
        get() = maxMemoryFlow.value
        set(maxMemory) {
            maxMemoryFlow.value = maxMemory
        }

    /**
     * The minimum memory that JVM can allocate for heap.
     */
    val minMemoryFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    var minMemory: Int?
        get() = minMemoryFlow.value
        set(minMemory) {
            minMemoryFlow.value = minMemory
        }

    val autoMemoryFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isAutoMemory: Boolean
        get() = autoMemoryFlow.value
        set(memory) {
            autoMemoryFlow.value = memory
        }

    // options
    val javaArgsFlow: MutableStateFlow<String> = MutableStateFlow("")
    var javaArgs: String
        /**
         * The user customized arguments passed to JVM.
         */
        get() = javaArgsFlow.value
        set(javaArgs) {
            javaArgsFlow.value = javaArgs
        }

    val minecraftArgsFlow: MutableStateFlow<String> = MutableStateFlow("")
    var minecraftArgs: String
        /**
         * The user customized arguments passed to Minecraft.
         */
        get() = minecraftArgsFlow.value
        set(minecraftArgs) {
            minecraftArgsFlow.value = minecraftArgs
        }

    val notCheckJVMFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isNotCheckJVM: Boolean
        /**
         * True if FCL does not check JVM validity.
         */
        get() = notCheckJVMFlow.value
        set(notCheckJVM) {
            notCheckJVMFlow.value = notCheckJVM
        }

    val notCheckGameFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isNotCheckGame: Boolean
        /**
         * True if FCL does not check game's completeness.
         */
        get() = notCheckGameFlow.value
        set(notCheckGame) {
            notCheckGameFlow.value = notCheckGame
        }

    // Minecraft settings.
    val serverIpFlow: MutableStateFlow<String> = MutableStateFlow("")
    var serverIp: String
        /**
         * The server ip that will be entered after Minecraft successfully loaded ly.
         *
         *
         * Format: ip:port or without port.
         */
        get() = serverIpFlow.value
        set(serverIp) {
            serverIpFlow.value = serverIp
        }

    /**
     * 0 - .minecraft<br></br>
     * 1 - .minecraft/versions/&lt;version&gt;/<br></br>
     */
    val isolateGameDirFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isIsolateGameDir: Boolean
        get() = isolateGameDirFlow.value
        set(isolate) {
            isolateGameDirFlow.value = isolate
        }

    val graphicsBackendFlow: MutableStateFlow<String> = MutableStateFlow("default")
    var graphicsBackend: String
        get() = graphicsBackendFlow.value
        set(v) {
            graphicsBackendFlow.value = v
        }

    val vkDriverSystemFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isVKDriverSystem: Boolean
        get() = vkDriverSystemFlow.value
        set(vulkanDriverSystem) {
            vkDriverSystemFlow.value = vulkanDriverSystem
        }

    val controllerFlow: MutableStateFlow<String> = MutableStateFlow("00000000")
    var controller: String
        get() = controllerFlow.value
        set(controller) {
            controllerFlow.value = controller
        }

    val rendererFlow: MutableStateFlow<String> = MutableStateFlow(Renderer.ID_NGGL4ES)
    var renderer: String
        get() = rendererFlow.value
        set(renderer) {
            rendererFlow.value = renderer
        }

    val driverFlow: MutableStateFlow<String> = MutableStateFlow("Turnip")
    var driver: String
        get() = driverFlow.value
        set(driver) {
            driverFlow.value = driver
        }

    val pojavBigCoreFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isPojavBigCore: Boolean
        get() = pojavBigCoreFlow.value
        set(pojavBigCore) {
            pojavBigCoreFlow.value = pojavBigCore
        }

    val notCheckModFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isNotCheckMod: Boolean
        get() = notCheckModFlow.value
        set(value) {
            notCheckModFlow.value = value
        }

    val debugLogFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDebugLog: Boolean
        get() = debugLogFlow.value
        set(value) {
            debugLogFlow.value = value
        }

    val forceResolutionFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isForceResolution: Boolean
        get() = forceResolutionFlow.value
        set(value) {
            forceResolutionFlow.value = value
        }

    fun checkController() {
        Controllers.addCallback {
            Controllers.checkControllers()
            val controller = Controllers.getControllers().stream()
                .filter { it.id == controller }
                .findFirst()
                .orElse(Controllers.getControllers()[0])
            this.controller = controller.id
        }
    }

    private val allFlows: List<StateFlow<*>> by lazy {
        listOf(
            usesGlobalFlow, javaFlow, maxMemoryFlow, minMemoryFlow, autoMemoryFlow,
            javaArgsFlow, minecraftArgsFlow, notCheckGameFlow, notCheckJVMFlow,
            serverIpFlow, isolateGameDirFlow, graphicsBackendFlow,
            vkDriverSystemFlow, controllerFlow, rendererFlow, driverFlow,
            pojavBigCoreFlow, uuidFlow, notCheckModFlow, debugLogFlow, forceResolutionFlow,
        )
    }

    /** 对齐原"任一属性失效即回调"语义（同值 set 不触发；订阅不可取消，与原监听一致）。 */
    fun addPropertyChangedListener(listener: Runnable) {
        allFlows.forEach { FlowSubscriptions.subscribe(it) { listener.run() } }
    }

    public override fun clone(): VersionSetting {
        return VersionSetting().also {
            it.isUsesGlobal = isUsesGlobal
            it.java = java
            it.maxMemory = maxMemory
            it.minMemory = minMemory
            it.isAutoMemory = isAutoMemory
            it.javaArgs = javaArgs
            it.minecraftArgs = minecraftArgs
            it.isNotCheckGame = isNotCheckGame
            it.isNotCheckJVM = isNotCheckJVM
            it.serverIp = serverIp
            it.isIsolateGameDir = isIsolateGameDir
            it.graphicsBackend = graphicsBackend
            it.isVKDriverSystem = isVKDriverSystem
            it.controller = controller
            it.renderer = renderer
            it.driver = driver
            it.isPojavBigCore = isPojavBigCore
            it.uuid = uuid
            it.isNotCheckMod = isNotCheckMod
            it.isDebugLog = isDebugLog
            it.isForceResolution = isForceResolution
        }
    }

    class Serializer : JsonSerializer<VersionSetting?>, JsonDeserializer<VersionSetting?> {
        override fun serialize(
            src: VersionSetting?,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            if (src == null) return JsonNull.INSTANCE
            return JsonObject().apply {
                addProperty("usesGlobal", src.isUsesGlobal)
                addProperty("javaArgs", src.javaArgs)
                addProperty("minecraftArgs", src.minecraftArgs)
                addProperty(
                    "maxMemory",
                    if (src.maxMemory <= 0) MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT) else src.maxMemory
                )
                addProperty("minMemory", src.minMemory)
                addProperty("autoMemory", src.isAutoMemory)
                addProperty("serverIp", src.serverIp)
                addProperty("java", src.java)
                addProperty("notCheckGame", src.isNotCheckGame)
                addProperty("notCheckJVM", src.isNotCheckJVM)
                addProperty("graphicsBackend", src.graphicsBackend)
                addProperty("vulkanDriverSystem", src.isVKDriverSystem)
                addProperty("controller", src.controller)
                addProperty("renderer", src.renderer)
                addProperty("driver", src.driver)
                addProperty("isolateGameDir", src.isIsolateGameDir)
                addProperty("pojavBigCore", src.isPojavBigCore)
                addProperty("uuid", src.uuid)
                addProperty("notCheckMod", src.isNotCheckMod)
                addProperty("debugLog", src.isDebugLog)
                addProperty("forceResolution", src.isForceResolution)
            }
        }

        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): VersionSetting? {
            if (json === JsonNull.INSTANCE || json !is JsonObject) return null

            var maxMemoryN = parseJsonPrimitive(
                json["maxMemory"]?.asJsonPrimitive,
                MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT)
            )
            if (maxMemoryN <= 0) maxMemoryN = MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT)
            return VersionSetting().also { vs ->
                vs.isUsesGlobal = json["usesGlobal"]?.asBoolean ?: false
                vs.javaArgs = json["javaArgs"]?.asString ?: ""
                vs.minecraftArgs = json["minecraftArgs"]?.asString ?: ""
                vs.maxMemory = maxMemoryN
                vs.minMemory = json["minMemory"]?.asInt
                vs.isAutoMemory = json["autoMemory"]?.asBoolean ?: true
                vs.serverIp = json["serverIp"]?.asString ?: ""
                vs.java =
                    JavaManager.javaList.find { it.name == json["java"]?.asString }?.name
                        ?: "Auto"
                vs.isNotCheckGame = json["notCheckGame"]?.asBoolean ?: false
                vs.isNotCheckJVM = json["notCheckJVM"]?.asBoolean ?: false
                vs.graphicsBackend = json["graphicsBackend"]?.asString ?: "default"
                vs.isVKDriverSystem = json["vulkanDriverSystem"]?.asBoolean ?: false
                vs.controller = json["controller"]?.asString ?: ("00000000")
                vs.renderer =
                    json["renderer"]?.asString ?: Renderer.ID_NGGL4ES
                vs.driver = json["driver"]?.asString ?: "Turnip"
                vs.isIsolateGameDir = json["isolateGameDir"]?.asBoolean ?: false
                vs.isPojavBigCore = json["pojavBigCore"]?.asBoolean ?: false
                vs.uuid = json["uuid"]?.asString ?: ""
                vs.isNotCheckMod = json["notCheckMod"]?.asBoolean ?: false
                vs.isDebugLog = json["debugLog"]?.asBoolean ?: false
                vs.isForceResolution = json["forceResolution"]?.asBoolean ?: false
            }
        }

        private fun parseJsonPrimitive(primitive: JsonPrimitive?, defaultValue: Int): Int {
            return if (primitive == null) defaultValue
            else if (primitive.isNumber) primitive.asInt
            else Lang.parseInt(primitive.asString, defaultValue)
        }
    }
}
