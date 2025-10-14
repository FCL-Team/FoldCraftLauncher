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
import com.mio.manager.RendererManager
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty
import com.tungsten.fclcore.fakefx.beans.property.StringProperty
import com.tungsten.fclcore.util.Lang
import com.tungsten.fclcore.util.platform.MemoryUtils
import java.lang.reflect.Type

@JsonAdapter(VersionSetting.Serializer::class)
class VersionSetting : Cloneable {
    var isGlobal: Boolean = false

    val usesGlobalProperty: BooleanProperty =
        SimpleBooleanProperty(this, "usesGlobal", true)
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
        get() = usesGlobalProperty.get()
        set(usesGlobal) {
            usesGlobalProperty.set(usesGlobal)
        }

    // java
    val javaProperty: StringProperty =
        SimpleStringProperty(this, "java", "Auto")
    var java: String
        get() = javaProperty.get()
        set(java) {
            javaProperty.set(java)
        }

    val uuidProperty: StringProperty = SimpleStringProperty(this, "uuid", "")
    var uuid: String
        get() = uuidProperty.get()
        set(value) {
            uuidProperty.set(value)
        }

    val maxMemoryProperty: IntegerProperty =
        SimpleIntegerProperty(this, "maxMemory", MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT))
    var maxMemory: Int
        /**
         * The maximum memory/MB that JVM can allocate for heap.
         */
        get() = maxMemoryProperty.get()
        set(maxMemory) {
            maxMemoryProperty.set(maxMemory)
        }

    /**
     * The minimum memory that JVM can allocate for heap.
     */
    val minMemoryProperty: ObjectProperty<Int?> =
        SimpleObjectProperty(this, "minMemory", null)
    var minMemory: Int?
        get() = minMemoryProperty.get()
        set(minMemory) {
            minMemoryProperty.set(minMemory)
        }

    val autoMemoryProperty: BooleanProperty = SimpleBooleanProperty(this, "autoMemory", true)
    var isAutoMemory: Boolean
        get() = autoMemoryProperty.get()
        set(memory) {
            autoMemoryProperty.set(memory)
        }

    // options
    val javaArgsProperty: StringProperty = SimpleStringProperty(this, "javaArgs", "")
    var javaArgs: String
        /**
         * The user customized arguments passed to JVM.
         */
        get() = javaArgsProperty.get()
        set(javaArgs) {
            javaArgsProperty.set(javaArgs)
        }

    val minecraftArgsProperty: StringProperty =
        SimpleStringProperty(this, "minecraftArgs", "")
    var minecraftArgs: String
        /**
         * The user customized arguments passed to Minecraft.
         */
        get() = minecraftArgsProperty.get()
        set(minecraftArgs) {
            minecraftArgsProperty.set(minecraftArgs)
        }

    val notCheckJVMProperty: BooleanProperty =
        SimpleBooleanProperty(this, "notCheckJVM", false)
    var isNotCheckJVM: Boolean
        /**
         * True if FCL does not check JVM validity.
         */
        get() = notCheckJVMProperty.get()
        set(notCheckJVM) {
            notCheckJVMProperty.set(notCheckJVM)
        }

    val notCheckGameProperty: BooleanProperty =
        SimpleBooleanProperty(this, "notCheckGame", false)
    var isNotCheckGame: Boolean
        /**
         * True if FCL does not check game's completeness.
         */
        get() = notCheckGameProperty.get()
        set(notCheckGame) {
            notCheckGameProperty.set(notCheckGame)
        }

    // Minecraft settings.
    val serverIpProperty: StringProperty = SimpleStringProperty(this, "serverIp", "")
    var serverIp: String
        /**
         * The server ip that will be entered after Minecraft successfully loaded ly.
         *
         *
         * Format: ip:port or without port.
         */
        get() = serverIpProperty.get()
        set(serverIp) {
            serverIpProperty.set(serverIp)
        }

    val scaleFactorProperty: IntegerProperty = SimpleIntegerProperty(this, "newScaleFactor", 100)
    var scaleFactor: Int
        get() = scaleFactorProperty.get()
        set(v) = scaleFactorProperty.set(v)

    /**
     * 0 - .minecraft<br></br>
     * 1 - .minecraft/versions/&lt;version&gt;/<br></br>
     */
    val isolateGameDirProperty: BooleanProperty =
        SimpleBooleanProperty(this, "isolateGameDir", true)
    var isIsolateGameDir: Boolean
        get() = isolateGameDirProperty.get()
        set(isolate) {
            isolateGameDirProperty.set(isolate)
        }

    val beGestureProperty: BooleanProperty = SimpleBooleanProperty(this, "beGesture", true)
    var isBeGesture: Boolean
        get() = beGestureProperty.get()
        set(beGesture) {
            beGestureProperty.set(beGesture)
        }

    val vkDriverSystemProperty: BooleanProperty =
        SimpleBooleanProperty(this, "vulkanDriverSystem", false)
    var isVKDriverSystem: Boolean
        get() = vkDriverSystemProperty.get()
        set(vulkanDriverSystem) {
            vkDriverSystemProperty.set(vulkanDriverSystem)
        }

    val controllerProperty: StringProperty =
        SimpleStringProperty(this, "controller", "00000000")
    var controller: String
        get() = controllerProperty.get()
        set(controller) {
            controllerProperty.set(controller)
        }

    val rendererProperty: StringProperty =
        SimpleStringProperty(this, "render", Renderer.ID_NGGL4ES)
    var renderer: String
        get() = rendererProperty.get()
        set(renderer) {
            rendererProperty.set(renderer)
        }

    val driverProperty: StringProperty =
        SimpleStringProperty(this, "driver", "Turnip")
    var driver: String
        get() = driverProperty.get()
        set(driver) {
            driverProperty.set(driver)
        }

    val pojavBigCoreProperty: BooleanProperty =
        SimpleBooleanProperty(this, "pojavBigCore", false)
    var isPojavBigCore: Boolean
        get() = pojavBigCoreProperty.get()
        set(pojavBigCore) {
            pojavBigCoreProperty.set(pojavBigCore)
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

    fun addPropertyChangedListener(listener: InvalidationListener?) {
        usesGlobalProperty.addListener(listener)
        javaProperty.addListener(listener)
        maxMemoryProperty.addListener(listener)
        minMemoryProperty.addListener(listener)
        autoMemoryProperty.addListener(listener)
        javaArgsProperty.addListener(listener)
        minecraftArgsProperty.addListener(listener)
        notCheckGameProperty.addListener(listener)
        notCheckJVMProperty.addListener(listener)
        serverIpProperty.addListener(listener)
        scaleFactorProperty.addListener(listener)
        isolateGameDirProperty.addListener(listener)
        beGestureProperty.addListener(listener)
        vkDriverSystemProperty.addListener(listener)
        controllerProperty.addListener(listener)
        rendererProperty.addListener(listener)
        driverProperty.addListener(listener)
        pojavBigCoreProperty.addListener(listener)
        uuidProperty.addListener(listener)
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
            it.scaleFactor = scaleFactor
            it.isIsolateGameDir = isIsolateGameDir
            it.isBeGesture = isBeGesture
            it.isVKDriverSystem = isVKDriverSystem
            it.controller = controller
            it.renderer = renderer
            it.driver = driver
            it.isPojavBigCore = isPojavBigCore
            it.uuid = uuid
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
                addProperty("newScaleFactor", src.scaleFactor)
                addProperty("notCheckGame", src.isNotCheckGame)
                addProperty("notCheckJVM", src.isNotCheckJVM)
                addProperty("beGesture", src.isBeGesture)
                addProperty("vulkanDriverSystem", src.isVKDriverSystem)
                addProperty("controller", src.controller)
                addProperty("renderer", src.renderer)
                addProperty("driver", src.driver)
                addProperty("isolateGameDir", src.isIsolateGameDir)
                addProperty("pojavBigCore", src.isPojavBigCore)
                addProperty("uuid", src.uuid)
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
                vs.scaleFactor = json["newScaleFactor"]?.asInt ?: 100
                vs.isNotCheckGame = json["notCheckGame"]?.asBoolean ?: false
                vs.isNotCheckJVM = json["notCheckJVM"]?.asBoolean ?: false
                vs.isBeGesture = json["beGesture"]?.asBoolean ?: false
                vs.isVKDriverSystem = json["vulkanDriverSystem"]?.asBoolean ?: false
                vs.controller = json["controller"]?.asString ?: ("00000000")
                vs.renderer =
                    json["renderer"]?.asString ?: Renderer.ID_NGGL4ES
                vs.driver = json["driver"]?.asString ?: "Turnip"
                vs.isIsolateGameDir = json["isolateGameDir"]?.asBoolean ?: false
                vs.isPojavBigCore = json["pojavBigCore"]?.asBoolean ?: false
                vs.uuid = json["uuid"]?.asString ?: ""
            }
        }

        private fun parseJsonPrimitive(primitive: JsonPrimitive?, defaultValue: Int): Int {
            return if (primitive == null) defaultValue
            else if (primitive.isNumber) primitive.asInt
            else Lang.parseInt(primitive.asString, defaultValue)
        }
    }
}
