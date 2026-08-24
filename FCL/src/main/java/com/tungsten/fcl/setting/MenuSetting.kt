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
import java.lang.reflect.Type

/**
 * 游戏内菜单设置数据模型。
 *
 * 使用普通类型字段替代原 fakefx property，属性变化通过 [addOnChangeListener] 通知
 * （用于自动保存与页面刷新），不再依赖 fakefx 监听机制。
 */
@JsonAdapter(MenuSetting.Serializer::class)
class MenuSetting {
    var isAutoFit: Boolean = true
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var autoFitDist: Int = 0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isLockMenuView: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isHideMenuView: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isShowFps: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isShowMemory: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isDisableSoftKeyAdjust: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isShowLog: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isAutoShowLog: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isPerformanceMode: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var menuPositionX: Double = 0.5
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var menuPositionY: Double = 0.5
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isDisableGesture: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var gestureMode: GestureMode = GestureMode.BUILD
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isDisableLeftTouch: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isEnableGyroscope: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isInvertGyroscope: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var gyroscopeSensitivity: Int = 10
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var mouseMoveMode: MouseMoveMode = MouseMoveMode.CLICK
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var itemBarWidth: Int = 0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var itemBarHeight: Int = 0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var windowScale: Double = 1.0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var cursorOffset: Double = 0.0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var mouseSensitivity: Double = 1.0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var mouseSensitivityCursor: Double = 2.0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var mouseSize: Int = 15
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var mouseOffsetX: Int = 0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var mouseOffsetY: Int = 0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var isPhysicalMouseMode: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    var gamepadDeadzone: Double = 1.0
        set(value) {
            if (field == value) return
            field = value
            changed()
        }

    private val changeListeners = mutableListOf<Runnable>()

    /** 注册属性变化监听（替代原 fakefx property 监听，用于自动保存与页面刷新） */
    fun addOnChangeListener(listener: Runnable) {
        changeListeners.add(listener)
    }

    fun removeOnChangeListener(listener: Runnable) {
        changeListeners.remove(listener)
    }

    private fun changed() {
        // 复制后遍历：回调内可能增删监听，避免并发修改
        changeListeners.toList().forEach { it.run() }
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