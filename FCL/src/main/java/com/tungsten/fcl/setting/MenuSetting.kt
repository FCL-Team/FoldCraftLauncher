package com.tungsten.fcl.setting

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.tungsten.fcl.control.GestureMode
import com.tungsten.fcl.control.MouseMoveMode
import com.tungsten.fclauncher.keycodes.GamepadKeycodeMap
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.MapProperty
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleDoubleProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleMapProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import com.tungsten.fclcore.fakefx.collections.FXCollections
import java.lang.reflect.Type
import java.util.Optional

@JsonAdapter(MenuSetting.Serializer::class)
class MenuSetting {
    val autoFitProperty: BooleanProperty = SimpleBooleanProperty(this, "autoFit", true)
    var isAutoFit: Boolean
        get() = autoFitProperty.get()
        set(autoFit) {
            autoFitProperty.set(autoFit)
        }

    val autoFitDistProperty: IntegerProperty = SimpleIntegerProperty(this, "autoFitDist", 0)
    var autoFitDist: Int
        get() = autoFitDistProperty.get()
        set(autoFitDist) {
            autoFitDistProperty.set(autoFitDist)
        }

    val lockMenuViewProperty: BooleanProperty =
        SimpleBooleanProperty(this, "lockMenuView", false)
    var isLockMenuView: Boolean
        get() = lockMenuViewProperty.get()
        set(lockMenuView) {
            lockMenuViewProperty.set(lockMenuView)
        }

    val disableSoftKeyAdjustProperty: BooleanProperty =
        SimpleBooleanProperty(this, "disableSoftKeyAdjust", false)
    var isDisableSoftKeyAdjust: Boolean
        get() = disableSoftKeyAdjustProperty.get()
        set(disableSoftKeyAdjust) {
            disableSoftKeyAdjustProperty.set(disableSoftKeyAdjust)
        }

    val showLogProperty: BooleanProperty = SimpleBooleanProperty(this, "showLog", false)
    var isShowLog: Boolean
        get() = showLogProperty.get()
        set(showLog) {
            showLogProperty.set(showLog)
        }

    val menuPositionXProperty: DoubleProperty =
        SimpleDoubleProperty(this, "menuPositionX", 0.5)
    var menuPositionX: Double
        get() = menuPositionXProperty.get()
        set(menuPositionX) {
            menuPositionXProperty.set(menuPositionX)
        }

    val menuPositionYProperty: DoubleProperty =
        SimpleDoubleProperty(this, "menuPositionY", 0.5)
    var menuPositionY: Double
        get() = menuPositionYProperty.get()
        set(menuPositionY) {
            menuPositionYProperty.set(menuPositionY)
        }

    val disableGestureProperty: BooleanProperty =
        SimpleBooleanProperty(this, "disableGesture", false)
    var isDisableGesture: Boolean
        get() = disableGestureProperty.get()
        set(disableGesture) {
            disableGestureProperty.set(disableGesture)
        }

    val disableBEGestureProperty: BooleanProperty =
        SimpleBooleanProperty(this, "disableBEGesture", false)
    var isDisableBEGesture: Boolean
        get() = disableBEGestureProperty.get()
        set(disableBEGesture) {
            disableBEGestureProperty.set(disableBEGesture)
        }

    val gestureModeProperty: ObjectProperty<GestureMode> =
        SimpleObjectProperty(this, "gestureMode", GestureMode.BUILD)
    var gestureMode: GestureMode
        get() = gestureModeProperty.get()
        set(gestureMode) {
            gestureModeProperty.set(gestureMode)
        }

    val enableGyroscopeProperty: BooleanProperty =
        SimpleBooleanProperty(this, "enableGyroscope", false)
    var isEnableGyroscope: Boolean
        get() = enableGyroscopeProperty.get()
        set(enableGyroscope) {
            enableGyroscopeProperty.set(enableGyroscope)
        }

    val gyroscopeSensitivityProperty: IntegerProperty =
        SimpleIntegerProperty(this, "gyroscopeSensitivity", 10)
    var gyroscopeSensitivity: Int
        get() = gyroscopeSensitivityProperty.get()
        set(gyroscopeSensitivity) {
            gyroscopeSensitivityProperty.set(gyroscopeSensitivity)
        }

    val mouseMoveModeProperty: ObjectProperty<MouseMoveMode> =
        SimpleObjectProperty(this, "mouseMoveMode", MouseMoveMode.CLICK)
    var mouseMoveMode: MouseMoveMode
        get() = mouseMoveModeProperty.get()
        set(mouseMoveMode) {
            mouseMoveModeProperty.set(mouseMoveMode)
        }

    val itemBarScaleProperty: IntegerProperty =
        SimpleIntegerProperty(this, "itemBarScale", 0)
    var itemBarScale: Int
        get() = itemBarScaleProperty.get()
        set(itemBarScale) {
            itemBarScaleProperty.set(itemBarScale)
        }

    val windowScaleProperty: DoubleProperty = SimpleDoubleProperty(this, "windowScale", 1.0)
    var windowScale: Double
        get() = windowScaleProperty.get()
        set(windowScale) {
            windowScaleProperty.set(windowScale)
        }

    val cursorOffsetProperty: DoubleProperty =
        SimpleDoubleProperty(this, "cursorOffset", 0.0)
    var cursorOffset: Double
        get() = cursorOffsetProperty.get()
        set(cursorOffset) {
            cursorOffsetProperty.set(cursorOffset)
        }

    val mouseSensitivityProperty: DoubleProperty =
        SimpleDoubleProperty(this, "mouseSensitivity", 1.0)
    var mouseSensitivity: Double
        get() = mouseSensitivityProperty.get()
        set(mouseSensitivity) {
            mouseSensitivityProperty.set(mouseSensitivity)
        }

    val mouseSizeProperty: IntegerProperty = SimpleIntegerProperty(this, "mouseSize", 15)
    var mouseSize: Int
        get() = mouseSizeProperty.get()
        set(mouseSize) {
            mouseSizeProperty.set(mouseSize)
        }

    val gamepadButtonBindingProperty: MapProperty<Int, Int> = SimpleMapProperty(
        this,
        "gamepadButtonBinding",
        FXCollections.observableMap(GamepadKeycodeMap.KEY_MAP)
    )

    val gamepadDeadzoneProperty: DoubleProperty =
        SimpleDoubleProperty(this, "gamepadDeadzone", 1.0)
    var gamepadDeadzone: Double
        get() = gamepadDeadzoneProperty.get()
        set(gamepadDeadzone) {
            gamepadDeadzoneProperty.set(gamepadDeadzone)
        }

    val gamepadAimAssistZoneProperty: DoubleProperty =
        SimpleDoubleProperty(this, "gamepadAimAssistZone", 0.95)
    var gamepadAimAssistZone: Double
        get() = gamepadAimAssistZoneProperty.get()
        set(gamepadAimAssistZone) {
            gamepadAimAssistZoneProperty.set(gamepadAimAssistZone)
        }

    fun addPropertyChangedListener(listener: InvalidationListener?) {
        autoFitProperty.addListener(listener)
        autoFitDistProperty.addListener(listener)
        lockMenuViewProperty.addListener(listener)
        disableSoftKeyAdjustProperty.addListener(listener)
        showLogProperty.addListener(listener)
        menuPositionXProperty.addListener(listener)
        menuPositionYProperty.addListener(listener)
        disableGestureProperty.addListener(listener)
        disableBEGestureProperty.addListener(listener)
        gestureModeProperty.addListener(listener)
        enableGyroscopeProperty.addListener(listener)
        gyroscopeSensitivityProperty.addListener(listener)
        mouseMoveModeProperty.addListener(listener)
        mouseSensitivityProperty.addListener(listener)
        mouseSizeProperty.addListener(listener)
        itemBarScaleProperty.addListener(listener)
        windowScaleProperty.addListener(listener)
        cursorOffsetProperty.addListener(listener)
        gamepadDeadzoneProperty.addListener(listener)
        gamepadAimAssistZoneProperty.addListener(listener)
        gamepadButtonBindingProperty.addListener(listener)
    }

    class Serializer : JsonSerializer<MenuSetting?>, JsonDeserializer<MenuSetting?> {
        override fun serialize(
            src: MenuSetting?,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            if (src == null) return JsonNull.INSTANCE
            return JsonObject().apply {
                addProperty("autoFit", src.isAutoFit)
                addProperty("autoFitDist", src.autoFitDist)
                addProperty("lockMenuView", src.isLockMenuView)
                addProperty("disableSoftKeyAdjust", src.isDisableSoftKeyAdjust)
                addProperty("showLog", src.isShowLog)
                addProperty("menuPositionX", src.menuPositionX)
                addProperty("menuPositionY", src.menuPositionY)
                addProperty("disableGesture", src.isDisableGesture)
                addProperty("disableBEGesture", src.isDisableBEGesture)
                addProperty("gestureMode", src.gestureMode.id)
                addProperty("enableGyroscope", src.isEnableGyroscope)
                addProperty("gyroscopeSensitivity", src.gyroscopeSensitivity)
                addProperty("mouseMoveMode", src.mouseMoveMode.id)
                addProperty("mouseSensitivity", src.mouseSensitivity)
                addProperty("mouseSize", src.mouseSize)
                addProperty("itemBarScale", src.itemBarScale)
                addProperty("windowScale", src.windowScale)
                addProperty("cursorOffset", src.cursorOffset)
                addProperty("gamepadDeadzone", src.gamepadDeadzone)
                addProperty("gamepadAimAssistZone", src.gamepadAimAssistZone)
                add(
                    "gamepadButtonBinding", Optional.of(src.gamepadButtonBindingProperty)
                        .map {
                            val ja = JsonArray()
                            it.forEach { k: Int?, v: Int? ->
                                ja.add(k)
                                ja.add(v)
                            }
                            ja
                        }.get()
                )
            }
        }

        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): MenuSetting? {
            if (json === JsonNull.INSTANCE || json !is JsonObject) return null
            return MenuSetting().also { ms ->
                ms.isAutoFit = json["autoFit"]?.asBoolean ?: true
                ms.autoFitDist = json["autoFitDist"]?.asInt ?: 0
                ms.isLockMenuView = json["lockMenuView"]?.asBoolean ?: false
                ms.isDisableSoftKeyAdjust = json["disableSoftKeyAdjust"]?.asBoolean ?: false
                ms.isShowLog = json["showLog"]?.asBoolean ?: false
                ms.menuPositionX = json["menuPositionX"]?.asDouble ?: 0.5
                ms.menuPositionY = json["menuPositionY"]?.asDouble ?: 0.5
                ms.isDisableGesture = json["disableGesture"]?.asBoolean ?: false
                ms.isDisableBEGesture = json["disableBEGesture"]?.asBoolean ?: false
                ms.gestureMode = GestureMode.getById(json["gestureMode"]?.asInt ?: 0)
                ms.isEnableGyroscope = json["enableGyroscope"]?.asBoolean ?: false
                ms.gyroscopeSensitivity = json["gyroscopeSensitivity"]?.asInt ?: 10
                ms.mouseMoveMode = MouseMoveMode.getById(json["mouseMoveMode"]?.asInt ?: 0)
                ms.mouseSensitivity = json["mouseSensitivity"]?.asDouble ?: 1.0
                ms.mouseSize = json["mouseSize"]?.asInt ?: 15
                ms.itemBarScale = json["itemBarScale"]?.asInt ?: 0
                ms.windowScale = json["windowScale"]?.asDouble ?: 1.0
                ms.cursorOffset = json["cursorOffset"]?.asDouble ?: 0.0
                ms.gamepadDeadzone = json["gamepadDeadzone"]?.asDouble ?: 0.2
                ms.gamepadAimAssistZone = json["gamepadAimAssistZone"]?.asDouble ?: 0.98
                ms.gamepadButtonBindingProperty.set(
                    Optional.ofNullable(json["gamepadButtonBinding"])
                        .map { it.asJsonArray }
                        .map {
                            if (it.isEmpty || (it.size() % 2 != 0)) {
                                return@map null
                            } else {
                                val iterator = it.iterator()
                                val map = HashMap<Int, Int>()
                                while (iterator.hasNext()) {
                                    map[iterator.next().asInt] = iterator.next().asInt
                                }
                                return@map map
                            }
                        }
                        .map { FXCollections.observableMap(it) }
                        .orElse(FXCollections.observableMap(GamepadKeycodeMap.KEY_MAP))
                )
            }
        }
    }
}
