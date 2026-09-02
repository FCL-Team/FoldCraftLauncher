package com.mio.controlconverter

import com.google.gson.JsonObject

/**
 * 方向控件转换（对应 cc.py fcl_direction_rect_to_zl_grid / direction_to_zl_joystick /
 * zl_joystick_to_fcl_direction 等）。
 */
object CcDirection {

    data class DirectionGrid(
        val widgetX: Long,
        val widgetY: Long,
        val size: Long,
        val p0: Long,
        val p1: Long,
        val p2: Long,
        val screenW: Double,
        val screenH: Double,
        val reference: String,
        val buttonSize: JsonObject,
        val childPx: Double,
    )

    fun fclDirectionRectToZlGrid(direction: JsonObject, style: JsonObject, aspect: Double, joined: Boolean): DirectionGrid {
        val base = direction.optObj("baseInfo") ?: JsonObject()
        val buttonStyle = style.optObj("buttonStyle") ?: JsonObject()
        val absolute = CcJson.toStringV(base.opt("sizeType")) == "ABSOLUTE"

        val screenH: Double
        val screenW: Double
        var referenceSize: Double
        var reference: String
        val viewSize: Long

        if (absolute) {
            screenH = 411.0
            screenW = screenH * Math.max(0.1, CcUtils.clampFloat(CcJson.pyNum(aspect), 16.0 / 9.0))
            reference = "SCREEN_HEIGHT"
            referenceSize = screenH
            viewSize = Math.max(1, CcUtils.clampInt(base.opt("absoluteWidth"), 50))
        } else {
            screenH = 10000.0
            screenW = screenH * Math.max(0.1, CcUtils.clampFloat(CcJson.pyNum(aspect), 16.0 / 9.0))
            val pw = base.optObj("percentageWidth") ?: JsonObject()
            reference = CcJson.toStringV(pw.opt("reference")).ifEmpty { "SCREEN_WIDTH" }
            referenceSize = if (reference == "SCREEN_HEIGHT") screenH else screenW
            viewSize = Math.max(1, (referenceSize * CcUtils.clampInt(pw.opt("size"), 0) / 1000.0).toLong())
        }

        val widgetX = ((screenW - viewSize) * CcUtils.clampInt(base.opt("xPosition"), 0) / 1000.0).toLong()
        val widgetY = ((screenH - viewSize) * CcUtils.clampInt(base.opt("yPosition"), 0) / 1000.0).toLong()
        val interval = Math.max(0, Math.min(499, CcUtils.clampInt(buttonStyle.opt("interval"), 50)))
        var childSize = Math.max(1, (viewSize * (1000 - 2 * interval) / 3000.0).toLong())

        val p0: Long
        val p1: Long
        val p2: Long
        if (joined) {
            if (!absolute) {
                reference = "SCREEN_HEIGHT"
                referenceSize = screenH
                childSize = Math.max(childSize, (screenH * 1350.0 / 10000.0).toLong())
            }
            val gap = Math.max(
                0,
                (childSize * 3.0 * interval / Math.max(1.0, (1000 - 2 * interval).toDouble())).toLong(),
            )
            p0 = 0
            p1 = childSize + gap
            p2 = (childSize + gap) * 2
        } else {
            p0 = 0
            p1 = childSize + (viewSize * interval / 1000.0).toLong()
            p2 = viewSize - childSize
        }

        val childPercentage = Math.max(100, Math.min(10000, CcUtils.pyRound(childSize / referenceSize * 10000.0)))
        val buttonSize: JsonObject = if (absolute) {
            CcJson.obj(
                "type" to CcJson.str("dp"),
                "widthDp" to CcJson.pyNum(CcUtils.clampZlDp(CcJson.inum(childSize), 50.0)),
                "heightDp" to CcJson.pyNum(CcUtils.clampZlDp(CcJson.inum(childSize), 50.0)),
                "widthPercentage" to CcJson.inum(childPercentage),
                "heightPercentage" to CcJson.inum(childPercentage),
                "widthReference" to CcJson.str("screen_height"),
                "heightReference" to CcJson.str("screen_height"),
            )
        } else {
            CcJson.obj(
                "type" to CcJson.str("percentage"),
                "widthDp" to CcJson.pyNum(50.0),
                "heightDp" to CcJson.pyNum(50.0),
                "widthPercentage" to CcJson.inum(childPercentage),
                "heightPercentage" to CcJson.inum(childPercentage),
                "widthReference" to CcJson.str(CcUtils.fclRefNameToZl(reference)),
                "heightReference" to CcJson.str(CcUtils.fclRefNameToZl(reference)),
            )
        }
        return DirectionGrid(widgetX, widgetY, childPercentage, p0, p1, p2, screenW, screenH, reference, buttonSize, childSize.toDouble())
    }

    fun directionViewSize(base: JsonObject, aspect: Double): Long {
        if (CcJson.toStringV(base.opt("sizeType")) == "ABSOLUTE") {
            return Math.max(1, CcUtils.clampInt(base.opt("absoluteWidth"), 50))
        }
        val screenH = 10000.0
        val screenW = screenH * Math.max(0.1, CcUtils.clampFloat(CcJson.pyNum(aspect), 16.0 / 9.0))
        val pw = base.optObj("percentageWidth") ?: JsonObject()
        val reference = CcJson.toStringV(pw.opt("reference")).ifEmpty { "SCREEN_WIDTH" }
        val referenceSize = if (reference == "SCREEN_HEIGHT") screenH else screenW
        return Math.max(1, (referenceSize * CcUtils.clampInt(pw.opt("size"), 0) / 1000.0).toLong())
    }

    fun pixelToZlPosition(pixel: Long, screen: Double, child: Double): Long {
        val available = Math.max(1.0, screen - child)
        return Math.max(0, Math.min(10000, CcUtils.pyRound(pixel / available * 10000.0)))
    }

    fun directionEventKeycodes(event: JsonObject, name: String, defaultKeycode: Long): List<com.google.gson.JsonElement> {
        val value = event.opt(name) ?: return listOf(CcJson.inum(defaultKeycode))
        val keycodes = CcUtils.fclKeycodeList(value)
        return if (keycodes.size() == 0) listOf(CcJson.inum(defaultKeycode)) else keycodes.toList()
    }

    fun zlKeyEventsFromKeycodes(
        ctx: CcContext,
        keycodes: List<com.google.gson.JsonElement>,
        strict: Boolean,
    ): List<JsonObject> {
        val events = mutableListOf<JsonObject>()
        for (kc in keycodes) {
            val converted = CcUtils.convertKeyToZl(ctx, CcUtils.clampInt(kc), strict)
            if (converted != null) {
                events.add(CcJson.obj("type" to CcJson.str(converted.eventType), "key" to CcJson.str(converted.key)))
            }
        }
        return events
    }

    /** FCL 方向控件 -> ZL 原生摇杆（JoystickData），editorVersion 12。 */
    fun directionToZlJoystick(
        ctx: CcContext,
        direction: JsonObject,
        style: JsonObject,
        joystickStyleUuid: String,
        strict: Boolean,
        aspect: Double,
    ): JsonObject {
        val base = direction.optObj("baseInfo") ?: JsonObject()
        val event = direction.optObj("event") ?: JsonObject()
        val grid = fclDirectionRectToZlGrid(direction, style, aspect, joined = true)
        val absolute = CcJson.toStringV(base.opt("sizeType")) == "ABSOLUTE"
        val viewSize = directionViewSize(base, aspect)

        val upKeys = directionEventKeycodes(event, "upKeycode", CcConstants.GLFW_TO_FCL.getValue("GLFW_KEY_W"))
        val downKeys = directionEventKeycodes(event, "downKeycode", CcConstants.GLFW_TO_FCL.getValue("GLFW_KEY_S"))
        val leftKeys = directionEventKeycodes(event, "leftKeycode", CcConstants.GLFW_TO_FCL.getValue("GLFW_KEY_A"))
        val rightKeys = directionEventKeycodes(event, "rightKeycode", CcConstants.GLFW_TO_FCL.getValue("GLFW_KEY_D"))
        val up = zlKeyEventsFromKeycodes(ctx, upKeys, strict)
        val down = zlKeyEventsFromKeycodes(ctx, downKeys, strict)
        val left = zlKeyEventsFromKeycodes(ctx, leftKeys, strict)
        val right = zlKeyEventsFromKeycodes(ctx, rightKeys, strict)

        val sizeType: String
        val sizeDp: Double
        val sizePercentage: Long
        if (absolute) {
            sizeType = "dp"
            sizeDp = CcUtils.clampZlDp(CcJson.inum(viewSize), 50.0)
            sizePercentage = 2500
        } else {
            sizeType = "percentage"
            sizePercentage = Math.max(2000, Math.min(10000, CcUtils.pyRound(viewSize / grid.screenH * 10000.0)))
            sizeDp = 200.0
        }

        val joystickObj = CcJson.obj(
            "uuid" to CcJson.str(ctx.shortId() + ctx.shortId().take(6)),
            "position" to CcJson.obj(
                "x" to CcJson.inum(pixelToZlPosition(grid.widgetX, grid.screenW, viewSize.toDouble())),
                "y" to CcJson.inum(pixelToZlPosition(grid.widgetY, grid.screenH, viewSize.toDouble())),
            ),
            "sizeType" to CcJson.str(sizeType),
            "sizeDp" to CcJson.pyNum(sizeDp),
            "sizePercentage" to CcJson.inum(sizePercentage),
            "visibilityType" to CcJson.str(CcUtils.visibilityFclToZl(CcJson.toStringV(base.opt("visibilityType")))),
            "joystickStyleId" to CcJson.str(joystickStyleUuid),
            "deadZoneRatio" to CcJson.pyNum(0.5),
            "lockThreshold" to CcJson.pyNum(0.3),
            "canLock" to CcJson.bool(true),
            "triggerMode" to CcJson.str("drag"),
            "directionEvents" to CcJson.obj(
                "north" to arrOfEvents(up),
                "north_east" to arrOfEvents(up + right),
                "north_west" to arrOfEvents(up + left),
                "south" to arrOfEvents(down),
                "south_east" to arrOfEvents(down + right),
                "south_west" to arrOfEvents(down + left),
                "east" to arrOfEvents(right),
                "west" to arrOfEvents(left),
            ),
            "lockEvents" to com.google.gson.JsonArray(),
        )
        val originId = CcJson.toStringV(direction.opt("id") ?: joystickObj.opt("uuid"))
        val mapping = CcJson.obj(
            "synthetic" to CcJson.bool(true),
            "generatedFrom" to CcJson.str("direction-joystick"),
        )
        return setMeta(joystickObj, makeMeta("fcl", "direction", originId, direction, mapping))
    }

    private fun arrOfEvents(events: List<JsonObject>): com.google.gson.JsonArray {
        val arr = com.google.gson.JsonArray()
        for (e in events) arr.add(e)
        return arr
    }

    // --- ZL 摇杆 -> FCL 方向控件（反向） ---

    /** ZL JoystickData -> FCL ControlDirection 的 baseInfo。 */
    fun makeDirectionBaseInfoFromZl(joystick: JsonObject, layerVisibility: String?): JsonObject {
        val sizeTypeRaw = joystick.opt("sizeType")?.let { CcJson.toStringV(it) }
        val sizeType = (sizeTypeRaw ?: "Percentage").lowercase()
        val fclSizeType: String
        val absolute: Long
        val percentage: Long
        if (sizeType == "dp" || sizeType == "dip" || sizeType == "absolute") {
            fclSizeType = "ABSOLUTE"
            absolute = Math.max(5, CcUtils.clampInt(joystick.opt("sizeDp"), 50))
            percentage = 300
        } else {
            fclSizeType = "PERCENTAGE"
            absolute = Math.max(5, CcUtils.clampInt(joystick.opt("sizeDp"), 50))
            percentage = Math.max(100, Math.min(1000, CcUtils.clampInt(joystick.opt("sizePercentage"), 2500) / 10))
        }
        return CcJson.obj(
            "visibilityType" to CcJson.str(
                CcUtils.visibilityZlToFcl(
                    CcUtils.firstTruthyStr(joystick.opt("visibilityType"), if (layerVisibility != null) CcJson.str(layerVisibility) else null)
                )
            ),
            "xPosition" to CcJson.inum(CcUtils.scalePositionToFcl(joystick.optObj("position")?.opt("x"))),
            "yPosition" to CcJson.inum(CcUtils.scalePositionToFcl(joystick.optObj("position")?.opt("y"))),
            "sizeType" to CcJson.str(fclSizeType),
            "absoluteWidth" to CcJson.inum(absolute),
            "absoluteHeight" to CcJson.inum(absolute),
            "percentageWidth" to CcJson.obj("reference" to CcJson.str("SCREEN_HEIGHT"), "size" to CcJson.inum(percentage)),
            "percentageHeight" to CcJson.obj("reference" to CcJson.str("SCREEN_HEIGHT"), "size" to CcJson.inum(percentage)),
        )
    }

    /** ZL JoystickData -> FCL ControlDirection（ROCKER）。 */
    fun zlJoystickToFclDirection(
        ctx: CcContext,
        joystick: JsonObject,
        layerVisibility: String?,
        strict: Boolean,
        styleName: String,
    ): JsonObject? {
        val original = metaOriginal(joystick, "fcl", "direction")
        if (original != null) {
            val restored = overlaySharedFieldsFclDirection(ctx, original, joystick, layerVisibility)
            val originId = CcUtils.firstTruthyStr(
                joystick.opt("uuid"),
                restored.opt("id"),
            ) ?: ctx.fclId()
            return setMeta(restored, makeMeta("zl", "joystick", originId, joystick))
        }

        val directionEvents = joystick.optObj("directionEvents") ?: JsonObject()
        fun keycodesFor(name: String): List<Long> {
            val keycodes = mutableListOf<Long>()
            for (event in directionEvents.optArr(name) ?: emptyList()) {
                val obj = event.asObjOrNull() ?: continue
                if (CcJson.toStringV(obj.opt("type")) != "key") continue
                val keycode = CcUtils.convertKeyToFcl(ctx, CcJson.toStringV(obj.opt("key")), strict)
                keycodes.add(keycode)
            }
            return keycodes
        }

        val directionObj = CcJson.obj(
            "id" to CcJson.str(
                CcUtils.firstTruthyStr(joystick.opt("uuid")) ?: ctx.fclId()
            ),
            "baseInfo" to makeDirectionBaseInfoFromZl(joystick, layerVisibility),
            "event" to CcJson.obj(
                "upKeycode" to arrOfNums(keycodesFor("north")),
                "downKeycode" to arrOfNums(keycodesFor("south")),
                "leftKeycode" to arrOfNums(keycodesFor("west")),
                "rightKeycode" to arrOfNums(keycodesFor("east")),
            ),
            "style" to CcJson.str(styleName),
        )
        return setMeta(
            directionObj,
            makeMeta("zl", "joystick", CcUtils.firstTruthyStr(joystick.opt("uuid"), directionObj.opt("id")), joystick)
        )
    }

    private fun arrOfNums(values: List<Long>): com.google.gson.JsonArray {
        val arr = com.google.gson.JsonArray()
        for (v in values) arr.add(CcJson.inum(v))
        return arr
    }

    /** meta 恢复路径：用当前摇杆几何覆写原 FCL direction 的共享字段。 */
    fun overlaySharedFieldsFclDirection(
        ctx: CcContext,
        original: JsonObject,
        joystick: JsonObject,
        layerVisibility: String?,
    ): JsonObject {
        val restored = original.deepCopy().asJsonObject
        restored.add(
            "id",
            CcJson.str(
                CcUtils.firstTruthyStr(joystick.opt("uuid"), restored.opt("id")) ?: ctx.fclId()
            ),
        )
        restored.add("baseInfo", makeDirectionBaseInfoFromZl(joystick, layerVisibility))
        return restored
    }
}
