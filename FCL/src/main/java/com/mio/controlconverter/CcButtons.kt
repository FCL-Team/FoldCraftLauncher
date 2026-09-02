package com.mio.controlconverter

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

/**
 * FCL 按钮到 ZL 按钮/文本框的转换（对应 cc.py fcl_button_to_zl /
 * fcl_button_to_zl_textbox / overlay_shared_fields_zl）。
 */
object CcButtons {

    fun overlaySharedFieldsZl(
        ctx: CcContext,
        original: JsonObject,
        current: JsonObject,
        styleMap: Map<String, String>,
        absoluteAsPercentage: Boolean,
        aspect: Double,
    ): JsonObject {
        val restored = original.deepCopy().asJsonObject
        val baseInfo = current.optObj("baseInfo") ?: JsonObject()
        val sourceText = current.opt("text")
        val textValue: JsonObject = if (sourceText != null && sourceText.isJsonObject) {
            CcUtils.translatable(CcUtils.textDefault(sourceText), sourceText.asJsonObject)
        } else {
            val prev = restored.opt("text").asObjOrNull()
            val textStr = if (CcUtils.pyTruthy(sourceText)) CcJson.toStringV(sourceText) else ""
            CcUtils.translatable(textStr, prev)
        }
        val uuid = CcUtils.firstTruthyStr(current.opt("id"), restored.opt("uuid"))
            ?: (ctx.shortId() + ctx.shortId().take(6))
        val styleName = CcUtils.firstTruthyStr(current.opt("style")) ?: "Default"
        val mapped = styleMap[styleName]
        val buttonStyle: JsonElement = mapped?.let { CcJson.str(it) }
            ?: restored.opt("buttonStyle") ?: JsonNull.INSTANCE

        restored.add("text", textValue)
        restored.add("uuid", CcJson.str(uuid))
        restored.add(
            "position",
            CcJson.obj(
                "x" to CcJson.inum(CcUtils.scalePositionToZl(baseInfo.opt("xPosition"))),
                "y" to CcJson.inum(CcUtils.scalePositionToZl(baseInfo.opt("yPosition"))),
            ),
        )
        restored.add("buttonSize", makeZlButtonSize(baseInfo, absoluteAsPercentage, aspect))
        restored.add("buttonStyle", buttonStyle)
        restored.add(
            "visibilityType",
            CcJson.str(CcUtils.visibilityFclToZl(CcJson.toStringV(baseInfo.opt("visibilityType")))),
        )
        return restored
    }

    /** 对应 cc.py make_zl_button_size。 */
    fun makeZlButtonSize(baseInfo: JsonObject, absoluteAsPercentage: Boolean, aspect: Double): JsonObject {
        val sizeTypeStr = CcJson.toStringV(baseInfo.opt("sizeType"))
        if (sizeTypeStr == "ABSOLUTE" && absoluteAsPercentage) {
            val screenHeightDp = 411.0
            val screenWidthDp = screenHeightDp * Math.max(0.1, CcUtils.clampFloat(CcJson.pyNum(aspect), 16.0 / 9.0))
            val widthDp = CcUtils.clampZlDp(baseInfo.opt("absoluteWidth"), 50.0)
            val heightDp = CcUtils.clampZlDp(baseInfo.opt("absoluteHeight"), 50.0)
            val widthPercentage = Math.max(100, Math.min(10000, CcUtils.pyRound(widthDp / screenWidthDp * 10000.0)))
            val heightPercentage = Math.max(100, Math.min(10000, CcUtils.pyRound(heightDp / screenHeightDp * 10000.0)))
            return CcJson.obj(
                "type" to CcJson.str("percentage"),
                "widthDp" to CcJson.pyNum(widthDp),
                "heightDp" to CcJson.pyNum(heightDp),
                "widthPercentage" to CcJson.inum(widthPercentage),
                "heightPercentage" to CcJson.inum(heightPercentage),
                "widthReference" to CcJson.str("screen_width"),
                "heightReference" to CcJson.str("screen_height"),
            )
        }
        val sizeType = if (sizeTypeStr == "ABSOLUTE") "dp" else "percentage"
        val pw = baseInfo.optObj("percentageWidth") ?: JsonObject()
        val ph = baseInfo.optObj("percentageHeight") ?: JsonObject()
        return CcJson.obj(
            "type" to CcJson.str(sizeType),
            "widthDp" to CcJson.pyNum(CcUtils.clampZlDp(baseInfo.opt("absoluteWidth"), 50.0)),
            "heightDp" to CcJson.pyNum(CcUtils.clampZlDp(baseInfo.opt("absoluteHeight"), 50.0)),
            "widthPercentage" to CcJson.inum(CcUtils.fclSizeToZl(pw.opt("size"))),
            "heightPercentage" to CcJson.inum(CcUtils.fclSizeToZl(ph.opt("size"))),
            "widthReference" to CcJson.str(CcUtils.fclRefToZl(CcJson.toStringV(pw.opt("reference")))),
            "heightReference" to CcJson.str(CcUtils.fclRefToZl(CcJson.toStringV(ph.opt("reference")))),
        )
    }

    /** 无 payload 的 FCL 显示按钮 -> ZL 文本框。 */
    fun fclButtonToZlTextbox(
        ctx: CcContext,
        button: JsonObject,
        styleMap: Map<String, String>,
        absoluteAsPercentage: Boolean,
        aspect: Double,
    ): JsonObject {
        val original = metaOriginal(button, "zl")
        if (original != null && !original.hasKey("clickEvents")) {
            val restored = overlaySharedFieldsZl(ctx, original, button, styleMap, absoluteAsPercentage, aspect)
            val originId = CcUtils.firstTruthyStr(button.opt("id"), restored.opt("uuid")) ?: ctx.shortId()
            val mapping = CcJson.obj(
                "synthetic" to CcJson.bool(true),
                "generatedFrom" to CcJson.str("decorative-textbox"),
            )
            return setMeta(restored, makeMeta("fcl", "button", originId, button, mapping))
        }

        val baseInfo = button.optObj("baseInfo") ?: JsonObject()
        val text = CcJson.toStringV(button.opt("text"))
        val styleName = CcUtils.firstTruthyStr(button.opt("style")) ?: "Default"
        val mapped = styleMap[styleName]
        val buttonStyle: JsonElement = mapped?.let { CcJson.str(it) } ?: JsonNull.INSTANCE
        val uuid = CcUtils.firstTruthyStr(button.opt("id")) ?: (ctx.shortId() + ctx.shortId().take(6))
        val result = CcJson.obj(
            "text" to CcUtils.translatable(text),
            "uuid" to CcJson.str(uuid),
            "position" to CcJson.obj(
                "x" to CcJson.inum(CcUtils.scalePositionToZl(baseInfo.opt("xPosition"))),
                "y" to CcJson.inum(CcUtils.scalePositionToZl(baseInfo.opt("yPosition"))),
            ),
            "buttonSize" to makeZlButtonSize(baseInfo, absoluteAsPercentage, aspect),
            "buttonStyle" to buttonStyle,
            "textAlignment" to CcJson.str("Center"),
            "textBold" to CcJson.bool(false),
            "textItalic" to CcJson.bool(false),
            "textUnderline" to CcJson.bool(false),
            "visibilityType" to CcJson.str(CcUtils.visibilityFclToZl(CcJson.toStringV(baseInfo.opt("visibilityType")))),
        )
        val mapping = CcJson.obj(
            "synthetic" to CcJson.bool(true),
            "generatedFrom" to CcJson.str("decorative-textbox"),
        )
        val originId = CcUtils.firstTruthyStr(button.opt("id"), result.opt("uuid")) ?: ctx.shortId()
        return setMeta(result, makeMeta("fcl", "button", originId, button, mapping))
    }

    /** FCL 按钮 -> ZL 按钮。 */
    fun fclButtonToZl(
        ctx: CcContext,
        button: JsonObject,
        styleMap: Map<String, String>,
        strict: Boolean,
        groupName: String,
        groupIdsByName: LinkedHashMap<String, String>,
        visualButton: JsonObject?,
        absoluteAsPercentage: Boolean,
        aspect: Double,
    ): JsonObject {
        val original = metaOriginal(button, "zl")
        if (original != null && original.hasKey("clickEvents")) {
            val vb = visualButton ?: button
            val restored = overlaySharedFieldsZl(ctx, original, vb, styleMap, absoluteAsPercentage, aspect)
            val originId = CcUtils.firstTruthyStr(button.opt("id"), restored.opt("uuid")) ?: ctx.shortId()
            return setMeta(restored, makeMeta("fcl", "button", originId, button))
        }

        val vb = visualButton ?: button
        val baseInfo = vb.optObj("baseInfo") ?: JsonObject()
        val eventRoot = button.optObj("event") ?: JsonObject()
        val text = CcUtils.firstTruthyStr(vb.opt("text"), button.opt("text")) ?: ""
        val clickEvents = mutableListOf<JsonObject>()
        val substitutions = mutableListOf<JsonObject>()

        val meaningfulEvents = mutableListOf<String>()
        for (eventName in CcEvents.EVENT_NAMES) {
            val event = eventRoot.optObj(eventName) ?: JsonObject()
            if (CcEvents.fclEventHasPayload(event)) meaningfulEvents.add(eventName)
        }
        for (eventName in CcEvents.EVENT_NAMES) {
            val event = eventRoot.optObj(eventName) ?: JsonObject()
            clickEvents.addAll(
                CcEvents.fclEventToZlEvents(ctx, event, strict, text, eventName, groupIdsByName, substitutions)
            )
        }
        val normalizedClickEvents = CcEvents.normalizeZlClickEvents(clickEvents)

        val pressEvent = eventRoot.optObj("pressEvent") ?: JsonObject()
        val pressKeycodes = CcUtils.fclKeycodeList(pressEvent.opt("outputKeycodes")).map { CcUtils.clampInt(it) }
        val canToggle = CcUtils.pyTruthy(pressEvent.opt("autoKeep")) &&
            pressKeycodes.isNotEmpty() &&
            meaningfulEvents.size == 1 && meaningfulEvents[0] == "pressEvent"

        if (CcUtils.pyTruthy(eventRoot.opt("Movable"))) {
            val reason = "FCL movable button cannot be represented in ZL layout JSON; preserved in metadata"
            ctx.warn("$reason on button \"$text\"", strict, once = true)
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_button_flag"), "key" to CcJson.str("Movable")),
                    CcJson.obj("type" to CcJson.str("metadata_only")),
                    reason,
                )
            )
        }
        if (CcUtils.pyTruthy(eventRoot.opt("pointerFollow")) &&
            !pressKeycodes.any { CcConstants.FCL_MOUSE_REVERSE.containsKey(it) }
        ) {
            val reason = "FCL pointerFollow cannot be represented exactly in ZL; preserved in metadata"
            ctx.warn("$reason on button \"$text\"", strict, once = true)
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_button_flag"), "key" to CcJson.str("pointerFollow")),
                    CcJson.obj("type" to CcJson.str("metadata_only")),
                    reason,
                )
            )
        }

        val isDecorative = normalizedClickEvents.isEmpty()
        val styleName = CcUtils.firstTruthyStr(vb.opt("style"), button.opt("style")) ?: "Default"
        val mapped = styleMap[styleName]
        val buttonStyle: JsonElement = mapped?.let { CcJson.str(it) } ?: JsonNull.INSTANCE
        val uuid = CcUtils.firstTruthyStr(button.opt("id")) ?: (ctx.shortId() + ctx.shortId().take(6))
        val result = CcJson.obj(
            "text" to CcUtils.translatable(text),
            "uuid" to CcJson.str(uuid),
            "position" to CcJson.obj(
                "x" to CcJson.inum(CcUtils.scalePositionToZl(baseInfo.opt("xPosition"))),
                "y" to CcJson.inum(CcUtils.scalePositionToZl(baseInfo.opt("yPosition"))),
            ),
            "buttonSize" to makeZlButtonSize(baseInfo, absoluteAsPercentage, aspect),
            "buttonStyle" to buttonStyle,
            "textAlignment" to CcJson.str("Center"),
            "textBold" to CcJson.bool(false),
            "textItalic" to CcJson.bool(false),
            "textUnderline" to CcJson.bool(false),
            "visibilityType" to CcJson.str(CcUtils.visibilityFclToZl(CcJson.toStringV(baseInfo.opt("visibilityType")))),
            "clickEvents" to normalizedClickEvents.fold(com.google.gson.JsonArray()) { arr, e -> arr.add(e); arr },
            "isSwipple" to CcJson.bool(isDecorative),
            "isPenetrable" to CcJson.bool(isDecorative),
            "isToggleable" to CcJson.bool(canToggle),
        )

        var mapping: JsonObject? = null
        if (vb !== button) {
            mapping = CcJson.obj(
                "synthetic" to CcJson.bool(true),
                "generatedFrom" to CcJson.str("overlay-merge"),
                "pairedVisualId" to CcJson.str(CcJson.toStringV(visualButton?.opt("id"))),
                "pairedEventId" to CcJson.str(CcJson.toStringV(button.opt("id"))),
            )
        }
        val finalMapping = appendSubstitutions(mapping, substitutions)
        val originId = CcUtils.firstTruthyStr(button.opt("id"), result.opt("uuid")) ?: ctx.shortId()
        return setMeta(result, makeMeta("fcl", "button", originId, button, finalMapping))
    }
}
