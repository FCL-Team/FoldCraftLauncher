package com.tungsten.fcl.setting

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
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleDoubleProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty
import java.lang.reflect.Type

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

    val hideMenuViewViewProperty: BooleanProperty =
        SimpleBooleanProperty(this, "hideMenuView", false)
    var isHideMenuView: Boolean
        get() = hideMenuViewViewProperty.get()
        set(hideMenuView) {
            hideMenuViewViewProperty.set(hideMenuView)
        }

    val showFpsProperty: BooleanProperty =
        SimpleBooleanProperty(this, "showFps", false)
    var isShowFps: Boolean
        get() = showFpsProperty.get()
        set(v) {
            showFpsProperty.set(v)
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

    val autoShowLogProperty: BooleanProperty = SimpleBooleanProperty(this, "autoShowLog", false)
    var isAutoShowLog: Boolean
        get() = autoShowLogProperty.get()
        set(v) {
            autoShowLogProperty.set(v)
        }

    val performanceModeProperty: BooleanProperty =
        SimpleBooleanProperty(this, "performanceMode", false)
    var isPerformanceMode
        get() = performanceModeProperty.get()
        set(v) {
            performanceModeProperty.set(v)
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

    val disableLeftTouchProperty: BooleanProperty =
        SimpleBooleanProperty(this, "disableLeftTouch", false)
    var isDisableLeftTouch: Boolean
        get() = disableLeftTouchProperty.get()
        set(v) {
            disableLeftTouchProperty.set(v)
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

    val mouseSensitivityCursorProperty: DoubleProperty =
        SimpleDoubleProperty(this, "mouseSensitivityCursor", 2.0)
    var mouseSensitivityCursor: Double
        get() = mouseSensitivityCursorProperty.get()
        set(mouseSensitivityCursor) {
            mouseSensitivityCursorProperty.set(mouseSensitivityCursor)
        }

    val mouseSizeProperty: IntegerProperty = SimpleIntegerProperty(this, "mouseSize", 15)
    var mouseSize: Int
        get() = mouseSizeProperty.get()
        set(mouseSize) {
            mouseSizeProperty.set(mouseSize)
        }

    val physicalMouseMode: BooleanProperty = SimpleBooleanProperty(this, "physicalMouseMode", false)
    var isPhysicalMouseMode: Boolean
        get() = physicalMouseMode.get()
        set(v) {
            physicalMouseMode.set(v)
        }

    val gamepadDeadzoneProperty: DoubleProperty =
        SimpleDoubleProperty(this, "gamepadDeadzone", 1.0)
    var gamepadDeadzone: Double
        get() = gamepadDeadzoneProperty.get()
        set(gamepadDeadzone) {
            gamepadDeadzoneProperty.set(gamepadDeadzone)
        }

    fun addPropertyChangedListener(listener: InvalidationListener?) {
        autoFitProperty.addListener(listener)
        autoFitDistProperty.addListener(listener)
        lockMenuViewProperty.addListener(listener)
        hideMenuViewViewProperty.addListener(listener)
        showFpsProperty.addListener(listener)
        disableSoftKeyAdjustProperty.addListener(listener)
        showLogProperty.addListener(listener)
        autoShowLogProperty.addListener(listener)
        performanceModeProperty.addListener(listener)
        menuPositionXProperty.addListener(listener)
        menuPositionYProperty.addListener(listener)
        disableGestureProperty.addListener(listener)
        disableBEGestureProperty.addListener(listener)
        gestureModeProperty.addListener(listener)
        disableLeftTouchProperty.addListener(listener)
        enableGyroscopeProperty.addListener(listener)
        gyroscopeSensitivityProperty.addListener(listener)
        mouseMoveModeProperty.addListener(listener)
        mouseSensitivityProperty.addListener(listener)
        mouseSensitivityCursorProperty.addListener(listener)
        mouseSizeProperty.addListener(listener)
        physicalMouseMode.addListener(listener)
        itemBarScaleProperty.addListener(listener)
        windowScaleProperty.addListener(listener)
        cursorOffsetProperty.addListener(listener)
        gamepadDeadzoneProperty.addListener(listener)
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
                addProperty("hideMenuView", src.isHideMenuView)
                addProperty("showFps", src.isShowFps)
                addProperty("disableSoftKeyAdjust", src.isDisableSoftKeyAdjust)
                addProperty("showLog", src.isShowLog)
                addProperty("autoShowLog", src.isAutoShowLog)
                addProperty("performanceMode", src.isPerformanceMode)
                addProperty("menuPositionX", src.menuPositionX)
                addProperty("menuPositionY", src.menuPositionY)
                addProperty("disableGesture", src.isDisableGesture)
                addProperty("disableBEGesture", src.isDisableBEGesture)
                addProperty("gestureMode", src.gestureMode.id)
                addProperty("disableLeftTouch", src.isDisableLeftTouch)
                addProperty("enableGyroscope", src.isEnableGyroscope)
                addProperty("gyroscopeSensitivity", src.gyroscopeSensitivity)
                addProperty("mouseMoveMode", src.mouseMoveMode.id)
                addProperty("mouseSensitivity", src.mouseSensitivity)
                addProperty("mouseSensitivityCursor", src.mouseSensitivityCursor)
                addProperty("mouseSize", src.mouseSize)
                addProperty("physicalMouseMode", src.isPhysicalMouseMode)
                addProperty("itemBarScale", src.itemBarScale)
                addProperty("windowScale", src.windowScale)
                addProperty("cursorOffset", src.cursorOffset)
                addProperty("gamepadDeadzone", src.gamepadDeadzone)
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
                ms.isHideMenuView = json["hideMenuView"]?.asBoolean ?: false
                ms.isShowFps = json["showFps"]?.asBoolean ?: false
                ms.isDisableSoftKeyAdjust = json["disableSoftKeyAdjust"]?.asBoolean ?: false
                ms.isShowLog = json["showLog"]?.asBoolean ?: false
                ms.isAutoShowLog = json["autoShowLog"]?.asBoolean ?: false
                ms.isPerformanceMode = json["performanceMode"]?.asBoolean ?: false
                ms.menuPositionX = json["menuPositionX"]?.asDouble ?: 0.5
                ms.menuPositionY = json["menuPositionY"]?.asDouble ?: 0.5
                ms.isDisableGesture = json["disableGesture"]?.asBoolean ?: false
                ms.isDisableBEGesture = json["disableBEGesture"]?.asBoolean ?: false
                ms.gestureMode = GestureMode.getById(json["gestureMode"]?.asInt ?: 0)
                ms.isDisableLeftTouch = json["disableLeftTouch"]?.asBoolean ?: false
                ms.isEnableGyroscope = json["enableGyroscope"]?.asBoolean ?: false
                ms.gyroscopeSensitivity = json["gyroscopeSensitivity"]?.asInt ?: 10
                ms.mouseMoveMode = MouseMoveMode.getById(json["mouseMoveMode"]?.asInt ?: 0)
                ms.mouseSensitivity = json["mouseSensitivity"]?.asDouble ?: 1.0
                ms.mouseSensitivityCursor = json["mouseSensitivityCursor"]?.asDouble ?: 2.0
                ms.mouseSize = json["mouseSize"]?.asInt ?: 15
                ms.isPhysicalMouseMode = json["physicalMouseMode"]?.asBoolean ?: false
                ms.itemBarScale = json["itemBarScale"]?.asInt ?: 0
                ms.windowScale = json["windowScale"]?.asDouble ?: 1.0
                ms.cursorOffset = json["cursorOffset"]?.asDouble ?: 0.0
                ms.gamepadDeadzone = json["gamepadDeadzone"]?.asDouble ?: 0.2
            }
        }
    }
}
