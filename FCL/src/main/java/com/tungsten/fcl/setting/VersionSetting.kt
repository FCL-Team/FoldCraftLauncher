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
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.plugins.RendererPlugin
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleDoubleProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty
import com.tungsten.fclcore.fakefx.beans.property.StringProperty
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
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
        SimpleStringProperty(this, "java", JavaVersion.JAVA_AUTO.versionName)
    var java: String
        get() = javaProperty.get()
        set(java) {
            javaProperty.set(java)
        }

    val permSizeProperty: StringProperty = SimpleStringProperty(this, "permSize", "")
    var permSize: String
        /**
         * The permanent generation size of JVM garbage collection.
         */
        get() = permSizeProperty.get()
        set(permSize) {
            permSizeProperty.set(permSize)
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

    val scaleFactorProperty: DoubleProperty = SimpleDoubleProperty(this, "scaleFactor", 1.0)
    var scaleFactor: Double
        get() = scaleFactorProperty.get()
        set(scaleFactor) {
            scaleFactorProperty.set(scaleFactor)
        }

    /**
     * 0 - .minecraft<br></br>
     * 1 - .minecraft/versions/&lt;version&gt;/<br></br>
     */
    val isolateGameDirProperty: BooleanProperty =
        SimpleBooleanProperty(this, "isolateGameDir", false)
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

    val rendererProperty: ObjectProperty<FCLConfig.Renderer> =
        SimpleObjectProperty(this, "render", FCLConfig.Renderer.RENDERER_GL4ES)
    var renderer: FCLConfig.Renderer
        get() = rendererProperty.get()
        set(renderer) {
            rendererProperty.set(renderer)
        }

    val customRendererProperty: ObjectProperty<String> =
        SimpleObjectProperty(this, "customRenderer", "")
    var customRenderer: String
        get() = customRendererProperty.get()
        set(renderer) {
            customRendererProperty.set(renderer)
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

    // launcher settings
    fun getJavaVersion(version: Version?): Task<JavaVersion> {
        return Task.runAsync(Schedulers.androidUIThread()) {
            if (java != JavaVersion.JAVA_AUTO.versionName &&
                java != JavaVersion.JAVA_8.versionName &&
                java != JavaVersion.JAVA_11.versionName &&
                java != JavaVersion.JAVA_17.versionName &&
                java != JavaVersion.JAVA_21.versionName
            ) {
                java = JavaVersion.JAVA_AUTO.versionName
            }
        }.thenSupplyAsync {
            if (java == JavaVersion.JAVA_AUTO.versionName) {
                return@thenSupplyAsync JavaVersion.getSuitableJavaVersion(version)
            } else {
                return@thenSupplyAsync JavaVersion.getJavaFromVersionName(java)
            }
        }
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
        permSizeProperty.addListener(listener)
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
        customRendererProperty.addListener(listener)
        driverProperty.addListener(listener)
        pojavBigCoreProperty.addListener(listener)
    }

    public override fun clone(): VersionSetting {
        return VersionSetting().also {
            it.isUsesGlobal = isUsesGlobal
            it.java = java
            it.permSize = permSize
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
            it.customRenderer = customRenderer
            it.driver = driver
            it.isPojavBigCore = isPojavBigCore
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
                addProperty("permSize", src.permSize)
                addProperty("serverIp", src.serverIp)
                addProperty("java", src.java)
                addProperty("scaleFactor", src.scaleFactor)
                addProperty("notCheckGame", src.isNotCheckGame)
                addProperty("notCheckJVM", src.isNotCheckJVM)
                addProperty("beGesture", src.isBeGesture)
                addProperty("vulkanDriverSystem", src.isVKDriverSystem)
                addProperty("controller", src.controller)
                addProperty("renderer", src.renderer.ordinal)
                addProperty("driver", src.driver)
                addProperty("isolateGameDir", src.isIsolateGameDir)
                addProperty("customRenderer", src.customRenderer)
                addProperty("pojavBigCore", src.isPojavBigCore)
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
                vs.permSize = json["permSize"]?.asString ?: ""
                vs.serverIp = json["serverIp"]?.asString ?: ""
                vs.java = json["java"]?.asString ?: JavaVersion.JAVA_AUTO.versionName
                vs.scaleFactor = json["scaleFactor"]?.asDouble ?: 1.0
                vs.isNotCheckGame = json["notCheckGame"]?.asBoolean ?: false
                vs.isNotCheckJVM = json["notCheckJVM"]?.asBoolean ?: false
                vs.isBeGesture = json["beGesture"]?.asBoolean ?: false
                vs.isVKDriverSystem = json["vulkanDriverSystem"]?.asBoolean ?: false
                vs.controller = json["controller"]?.asString ?: ("00000000")
                vs.renderer = FCLConfig.Renderer.entries.toTypedArray()[json["renderer"]?.asInt
                    ?: FCLConfig.Renderer.RENDERER_GL4ES.ordinal]
                vs.driver = json["driver"]?.asString ?: "Turnip"
                vs.isIsolateGameDir = json["isolateGameDir"]?.asBoolean ?: false
                vs.customRenderer = json["customRenderer"]?.asString ?: ""
                vs.isPojavBigCore = json["pojavBigCore"]?.asBoolean ?: false
                if (!RendererPlugin.isAvailable() && vs.customRenderer != "") {
                    vs.renderer = FCLConfig.Renderer.entries.toTypedArray()[0]
                    vs.customRenderer = ""
                }
            }
        }

        private fun parseJsonPrimitive(primitive: JsonPrimitive?, defaultValue: Int): Int {
            return if (primitive == null) defaultValue
            else if (primitive.isNumber) primitive.asInt
            else Lang.parseInt(primitive.asString, defaultValue)
        }
    }
}
