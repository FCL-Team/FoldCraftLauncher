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
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.reflect.Type

/**
 * 游戏菜单设置（阶段 4a）：全部属性已 StateFlow 化（`xxxFlow`），var 读写接口签名不变。
 * [addPropertyChangedListener] 对齐原"任一属性失效即回调"语义（GameMenu 据此落盘
 * menu_setting.json）。磁盘 JSON 由手写 [Serializer] 产出，格式不变。
 */
@JsonAdapter(MenuSetting.Serializer::class)
class MenuSetting {
    val autoFitFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isAutoFit: Boolean
        get() = autoFitFlow.value
        set(autoFit) {
            autoFitFlow.value = autoFit
        }

    val autoFitDistFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    var autoFitDist: Int
        get() = autoFitDistFlow.value
        set(autoFitDist) {
            autoFitDistFlow.value = autoFitDist
        }

    val lockMenuViewFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLockMenuView: Boolean
        get() = lockMenuViewFlow.value
        set(lockMenuView) {
            lockMenuViewFlow.value = lockMenuView
        }

    val hideMenuViewFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isHideMenuView: Boolean
        get() = hideMenuViewFlow.value
        set(hideMenuView) {
            hideMenuViewFlow.value = hideMenuView
        }

    val showFpsFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isShowFps: Boolean
        get() = showFpsFlow.value
        set(v) {
            showFpsFlow.value = v
        }

    val showMemoryFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isShowMemory: Boolean
        get() = showMemoryFlow.value
        set(v) {
            showMemoryFlow.value = v
        }

    val disableSoftKeyAdjustFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDisableSoftKeyAdjust: Boolean
        get() = disableSoftKeyAdjustFlow.value
        set(disableSoftKeyAdjust) {
            disableSoftKeyAdjustFlow.value = disableSoftKeyAdjust
        }

    val showLogFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isShowLog: Boolean
        get() = showLogFlow.value
        set(showLog) {
            showLogFlow.value = showLog
        }

    val autoShowLogFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isAutoShowLog: Boolean
        get() = autoShowLogFlow.value
        set(v) {
            autoShowLogFlow.value = v
        }

    val performanceModeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isPerformanceMode
        get() = performanceModeFlow.value
        set(v) {
            performanceModeFlow.value = v
        }

    val menuPositionXFlow: MutableStateFlow<Double> = MutableStateFlow(0.5)
    var menuPositionX: Double
        get() = menuPositionXFlow.value
        set(menuPositionX) {
            menuPositionXFlow.value = menuPositionX
        }

    val menuPositionYFlow: MutableStateFlow<Double> = MutableStateFlow(0.5)
    var menuPositionY: Double
        get() = menuPositionYFlow.value
        set(menuPositionY) {
            menuPositionYFlow.value = menuPositionY
        }

    val disableGestureFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDisableGesture: Boolean
        get() = disableGestureFlow.value
        set(disableGesture) {
            disableGestureFlow.value = disableGesture
        }

    val disableBEGestureFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDisableBEGesture: Boolean
        get() = disableBEGestureFlow.value
        set(disableBEGesture) {
            disableBEGestureFlow.value = disableBEGesture
        }

    val gestureModeFlow: MutableStateFlow<GestureMode> = MutableStateFlow(GestureMode.BUILD)
    var gestureMode: GestureMode
        get() = gestureModeFlow.value
        set(gestureMode) {
            gestureModeFlow.value = gestureMode
        }

    val disableLeftTouchFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isDisableLeftTouch: Boolean
        get() = disableLeftTouchFlow.value
        set(disableLeftTouch) {
            disableLeftTouchFlow.value = disableLeftTouch
        }

    val enableGyroscopeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isEnableGyroscope: Boolean
        get() = enableGyroscopeFlow.value
        set(enableGyroscope) {
            enableGyroscopeFlow.value = enableGyroscope
        }

    val invertGyroscopeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isInvertGyroscope: Boolean
        get() = invertGyroscopeFlow.value
        set(v) {
            invertGyroscopeFlow.value = v
        }

    val gyroscopeSensitivityFlow: MutableStateFlow<Int> = MutableStateFlow(10)
    var gyroscopeSensitivity: Int
        get() = gyroscopeSensitivityFlow.value
        set(gyroscopeSensitivity) {
            gyroscopeSensitivityFlow.value = gyroscopeSensitivity
        }

    val mouseMoveModeFlow: MutableStateFlow<MouseMoveMode> = MutableStateFlow(MouseMoveMode.CLICK)
    var mouseMoveMode: MouseMoveMode
        get() = mouseMoveModeFlow.value
        set(mouseMoveMode) {
            mouseMoveModeFlow.value = mouseMoveMode
        }

    val itemBarWidthFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    var itemBarWidth: Int
        get() = itemBarWidthFlow.value
        set(v) {
            itemBarWidthFlow.value = v
        }

    val itemBarHeightFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    var itemBarHeight: Int
        get() = itemBarHeightFlow.value
        set(v) {
            itemBarHeightFlow.value = v
        }

    val windowScaleFlow: MutableStateFlow<Double> = MutableStateFlow(1.0)
    var windowScale: Double
        get() = windowScaleFlow.value
        set(windowScale) {
            windowScaleFlow.value = windowScale
        }

    val cursorOffsetFlow: MutableStateFlow<Double> = MutableStateFlow(0.0)
    var cursorOffset: Double
        get() = cursorOffsetFlow.value
        set(cursorOffset) {
            cursorOffsetFlow.value = cursorOffset
        }

    val mouseSensitivityFlow: MutableStateFlow<Double> = MutableStateFlow(1.0)
    var mouseSensitivity: Double
        get() = mouseSensitivityFlow.value
        set(mouseSensitivity) {
            mouseSensitivityFlow.value = mouseSensitivity
        }

    val mouseSensitivityCursorFlow: MutableStateFlow<Double> = MutableStateFlow(2.0)
    var mouseSensitivityCursor: Double
        get() = mouseSensitivityCursorFlow.value
        set(mouseSensitivityCursor) {
            mouseSensitivityCursorFlow.value = mouseSensitivityCursor
        }

    val mouseSizeFlow: MutableStateFlow<Int> = MutableStateFlow(15)
    var mouseSize: Int
        get() = mouseSizeFlow.value
        set(v) {
            mouseSizeFlow.value = v
        }

    val mouseOffsetXFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    var mouseOffsetX: Int
        get() = mouseOffsetXFlow.value
        set(v) {
            mouseOffsetXFlow.value = v
        }

    val mouseOffsetYFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    var mouseOffsetY: Int
        get() = mouseOffsetYFlow.value
        set(v) {
            mouseOffsetYFlow.value = v
        }

    val physicalMouseModeFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isPhysicalMouseMode: Boolean
        get() = physicalMouseModeFlow.value
        set(v) {
            physicalMouseModeFlow.value = v
        }

    val gamepadDeadzoneFlow: MutableStateFlow<Double> = MutableStateFlow(1.0)
    var gamepadDeadzone: Double
        get() = gamepadDeadzoneFlow.value
        set(gamepadDeadzone) {
            gamepadDeadzoneFlow.value = gamepadDeadzone
        }

    private val allFlows: List<StateFlow<*>> by lazy {
        listOf(
            autoFitFlow, autoFitDistFlow, lockMenuViewFlow, hideMenuViewFlow,
            showFpsFlow, showMemoryFlow, disableSoftKeyAdjustFlow, showLogFlow,
            autoShowLogFlow, performanceModeFlow, menuPositionXFlow, menuPositionYFlow,
            disableGestureFlow, disableBEGestureFlow, gestureModeFlow,
            disableLeftTouchFlow, enableGyroscopeFlow, invertGyroscopeFlow,
            gyroscopeSensitivityFlow, mouseMoveModeFlow, mouseSensitivityFlow,
            mouseSensitivityCursorFlow, mouseSizeFlow, mouseOffsetXFlow, mouseOffsetYFlow,
            physicalMouseModeFlow, itemBarWidthFlow, itemBarHeightFlow, windowScaleFlow,
            cursorOffsetFlow, gamepadDeadzoneFlow,
        )
    }

    /** 对齐原"任一属性失效即回调"语义（同值 set 不触发；订阅不可取消，与原监听一致）。 */
    fun addPropertyChangedListener(listener: Runnable) {
        allFlows.forEach { FlowSubscriptions.subscribe(it) { listener.run() } }
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
                addProperty("showMemory", src.isShowMemory)
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
                addProperty("invertGyroscope", src.isInvertGyroscope)
                addProperty("gyroscopeSensitivity", src.gyroscopeSensitivity)
                addProperty("mouseMoveMode", src.mouseMoveMode.id)
                addProperty("mouseSensitivity", src.mouseSensitivity)
                addProperty("mouseSensitivityCursor", src.mouseSensitivityCursor)
                addProperty("mouseSize", src.mouseSize)
                addProperty("mouseOffsetX", src.mouseOffsetX)
                addProperty("mouseOffsetY", src.mouseOffsetY)
                addProperty("physicalMouseMode", src.isPhysicalMouseMode)
                addProperty("itemBarWidth", src.itemBarWidth)
                addProperty("itemBarHeight", src.itemBarHeight)
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
                ms.isShowMemory = json["showMemory"]?.asBoolean ?: false
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
                ms.isInvertGyroscope = json["invertGyroscope"]?.asBoolean ?: false
                ms.gyroscopeSensitivity = json["gyroscopeSensitivity"]?.asInt ?: 10
                ms.mouseMoveMode = MouseMoveMode.getById(json["mouseMoveMode"]?.asInt ?: 0)
                ms.mouseSensitivity = json["mouseSensitivity"]?.asDouble ?: 1.0
                ms.mouseSensitivityCursor = json["mouseSensitivityCursor"]?.asDouble ?: 2.0
                ms.mouseSize = json["mouseSize"]?.asInt ?: 15
                ms.mouseOffsetX = json["mouseOffsetX"]?.asInt ?: 0
                ms.mouseOffsetY = json["mouseOffsetY"]?.asInt ?: 0
                ms.isPhysicalMouseMode = json["physicalMouseMode"]?.asBoolean ?: false
                ms.itemBarWidth = json["itemBarWidth"]?.asInt ?: 0
                ms.itemBarHeight = json["itemBarHeight"]?.asInt ?: 0
                ms.windowScale = json["windowScale"]?.asDouble ?: 1.0
                ms.cursorOffset = json["cursorOffset"]?.asDouble ?: 0.0
                ms.gamepadDeadzone = json["gamepadDeadzone"]?.asDouble ?: 0.2
            }
        }
    }
}
