package com.mio.controlconverter

import com.google.gson.JsonObject

/**
 * 样式转换（对应 cc.py zl_styles_to_fcl / fcl_styles_to_zl / 摇杆样式互转）。
 */
object CcStyles {

    fun styleNameForZlStyle(ctx: CcContext, baseName: String, uuidValue: String): String {
        val suffix = if (uuidValue.isNotEmpty()) uuidValue.take(6) else ctx.shortId().take(6)
        return "ZL $baseName $suffix"
    }

    /** ZL 按钮样式 -> FCL buttonStyles；返回 (结果列表, zl uuid -> fcl 名称)。 */
    fun zlStylesToFcl(ctx: CcContext, styles: List<JsonObject>): Pair<List<JsonObject>, MutableMap<String, String>> {
        val result = mutableListOf<JsonObject>()
        val mapping = linkedMapOf<String, String>()
        val used = LinkedHashSet<String>()

        for (style in styles) {
            val uuidValue = CcJson.toStringV(style.opt("uuid"))
            val baseName = run {
                val n = style.opt("name")
                if (CcUtils.pyTruthy(n)) CcJson.toStringV(n) else if (uuidValue.isNotEmpty()) uuidValue else "Style"
            }
            var name = styleNameForZlStyle(ctx, baseName, uuidValue)
            var suffix = 2
            while (name in used) {
                name = styleNameForZlStyle(ctx, baseName, uuidValue) + "_" + suffix
                suffix++
            }
            used.add(name)
            if (uuidValue.isNotEmpty()) mapping[uuidValue] = name

            val light = style.optObj("lightStyle") ?: JsonObject()
            result.add(
                CcJson.obj(
                    "name" to CcJson.str(name),
                    "textColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("contentColor"), -1)),
                    "textSize" to CcJson.inum(CcUtils.clampInt(light.opt("fontSize"), 12)),
                    "strokeColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("borderColor"), -12303292)),
                    "strokeWidth" to CcJson.inum(CcUtils.clampInt(light.opt("borderWidth"), 1) * 10),
                    "cornerRadius" to CcJson.inum(CcUtils.zlShapeToFclRadius(light.optObj("borderRadius"))),
                    "fillColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("backgroundColor"), 0, light.opt("alpha"))),
                    "textColorPressed" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("pressedContentColor"), -1)),
                    "textSizePressed" to CcJson.inum(
                        CcUtils.clampInt(light.opt("pressedFontSize"), CcUtils.clampInt(light.opt("fontSize"), 12))
                    ),
                    "strokeColorPressed" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("pressedBorderColor"), -12303292)),
                    "strokeWidthPressed" to CcJson.inum(
                        CcUtils.clampInt(light.opt("pressedBorderWidth"), CcUtils.clampInt(light.opt("borderWidth"), 1)) * 10
                    ),
                    "cornerRadiusPressed" to CcJson.inum(CcUtils.zlShapeToFclRadius(light.optObj("pressedBorderRadius"))),
                    "fillColorPressed" to CcJson.inum(
                        CcUtils.zlColorToFcl(light.opt("pressedBackgroundColor"), -3355444, light.opt("pressedAlpha"))
                    ),
                )
            )
        }

        if (result.isEmpty()) {
            result.add(CcConstants.defaultZlFallbackFclStyle())
        } else if (result.none { CcJson.toStringV(it.opt("name")) == "ZL Native Default" }) {
            result.add(0, CcConstants.defaultZlFallbackFclStyle())
        }
        return Pair(result, mapping)
    }

    /** FCL buttonStyles -> ZL styles；返回 (结果列表, fcl 名称 -> zl uuid)。 */
    fun fclStylesToZl(ctx: CcContext, styles: List<JsonObject>): Pair<List<JsonObject>, MutableMap<String, String>> {
        val result = mutableListOf<JsonObject>()
        val mapping = linkedMapOf<String, String>()
        val effective = styles.ifEmpty { listOf(CcConstants.defaultFclStyle()) }
        for (style in effective) {
            val rawName = style.opt("name")
            val name = if (CcUtils.pyTruthy(rawName)) CcJson.toStringV(rawName) else "Default"
            val sid = ctx.shortId()
            mapping[name] = sid
            val radius = (CcUtils.clampFloat(style.opt("cornerRadius"), 0.0) / 10.0).coerceIn(0.0, 100.0)
            val pressedRadiusSrc = style.opt("cornerRadiusPressed") ?: style.opt("cornerRadius")
            val pressedRadius = (CcUtils.clampFloat(pressedRadiusSrc, 0.0) / 10.0).coerceIn(0.0, 100.0)
            val textSize = style.opt("textSize")
            val light = CcJson.obj(
                "alpha" to CcJson.pyNum(1.0),
                "pressedAlpha" to CcJson.pyNum(1.0),
                "backgroundColor" to CcJson.inum(CcUtils.fclArgbToZlColor(style.opt("fillColor"), 0)),
                "pressedBackgroundColor" to CcJson.inum(CcUtils.fclArgbToZlColor(style.opt("fillColorPressed"), -3355444)),
                "contentColor" to CcJson.inum(CcUtils.fclArgbToZlColor(style.opt("textColor"), -1)),
                "pressedContentColor" to CcJson.inum(CcUtils.fclArgbToZlColor(style.opt("textColorPressed"), -1)),
                "fontSize" to CcJson.inum(CcUtils.fclFontToZl(textSize, 12)),
                "pressedFontSize" to CcJson.inum(
                    CcUtils.fclFontToZl(style.opt("textSizePressed") ?: textSize, 12)
                ),
                "borderWidth" to CcJson.inum(CcUtils.clampZlBorderWidth(CcJson.inum(CcUtils.clampInt(style.opt("strokeWidth"), 10) / 10))),
                "pressedBorderWidth" to CcJson.inum(CcUtils.clampZlBorderWidth(CcJson.inum(CcUtils.clampInt(style.opt("strokeWidthPressed"), 10) / 10))),
                "borderColor" to CcJson.inum(CcUtils.fclArgbToZlColor(style.opt("strokeColor"), -12303292)),
                "pressedBorderColor" to CcJson.inum(CcUtils.fclArgbToZlColor(style.opt("strokeColorPressed"), -12303292)),
                "borderRadius" to shapeObj(radius),
                "pressedBorderRadius" to shapeObj(pressedRadius),
            )
            result.add(
                CcJson.obj(
                    "name" to CcJson.str(name),
                    "uuid" to CcJson.str(sid),
                    "animateSwap" to CcJson.bool(false),
                    "commonStyle" to CcJson.bool(true),
                    "lightStyle" to light,
                    "darkStyle" to light.deepCopy(),
                )
            )
        }
        return Pair(result, mapping)
    }

    private fun shapeObj(radius: Double): JsonObject = CcJson.obj(
        "topStart" to CcJson.pyNum(radius),
        "topEnd" to CcJson.pyNum(radius),
        "bottomEnd" to CcJson.pyNum(radius),
        "bottomStart" to CcJson.pyNum(radius),
    )

    /** FCL directionStyles 名称表（cc.py str(name) 语义：缺名 -> "None" 键）。 */
    fun directionStyleMap(styles: List<JsonObject>): LinkedHashMap<String, JsonObject> {
        val result = linkedMapOf<String, JsonObject>()
        for (item in styles) {
            val n = item.opt("name")
            val name = if (n == null) "None" else CcJson.toStringV(n)
            result[name] = item
        }
        return result
    }

    fun resolveDirectionStyle(direction: JsonObject, styles: Map<String, JsonObject>): JsonObject {
        val s = direction.opt("style")
        if (s != null && s.isJsonObject) return s.asJsonObject
        val key = if (s == null) "None" else CcJson.toStringV(s)
        return styles[key] ?: JsonObject()
    }

    private fun setConfigFrom(
        config: JsonObject,
        src: JsonObject,
        fillKey: String, fillDef: Long,
        textKey: String, textDef: Long,
        strokeKey: String, strokeDef: Long,
        strokeWidthKey: String, strokeWidthDef: Long,
        cornerKey: String, cornerDef: Long,
        joystickCornerKey: String, joystickCornerDef: Long,
        joystickSize: Double,
    ) {
        config.add(
            "backgroundColor",
            CcJson.inum(CcUtils.fclArgbToZlColor(src.opt(fillKey) ?: CcJson.inum(fillDef), 0))
        )
        config.add(
            "joystickColor",
            CcJson.inum(CcUtils.fclArgbToZlColor(src.opt(textKey) ?: CcJson.inum(textDef), 0))
        )
        config.add(
            "borderColor",
            CcJson.inum(CcUtils.fclArgbToZlColor(src.opt(strokeKey) ?: CcJson.inum(strokeDef), 0))
        )
        val w = CcUtils.clampInt(src.opt(strokeWidthKey) ?: CcJson.inum(strokeWidthDef), 0)
        config.add("borderWidthRatio", CcJson.inum(Math.max(0, Math.min(50, w / 10))))
        config.add(
            "backgroundShape",
            CcJson.inum(CcUtils.fclRadiusToZlPercent(src.opt(cornerKey) ?: CcJson.inum(cornerDef), 500))
        )
        config.add(
            "joystickShape",
            CcJson.inum(CcUtils.fclRadiusToZlPercent(src.opt(joystickCornerKey) ?: CcJson.inum(joystickCornerDef), 500))
        )
        config.add("joystickSize", CcJson.pyNum(joystickSize))
    }

    private fun nameOf(style: JsonObject?): String {
        val n = style?.opt("name")
        return if (CcUtils.pyTruthy(n)) CcJson.toStringV(n) else "Default"
    }

    /** FCL ROCKER 方向样式 -> ZL 摇杆样式。 */
    fun fclRockerStyleToZlJoystick(ctx: CcContext, style: JsonObject?): JsonObject {
        val rocker = style?.optObj("rockerStyle")?.takeIf { it.entrySet().isNotEmpty() } ?: JsonObject()
        val config = CcConstants.defaultZlJoystickStyleConfig()
        setConfigFrom(
            config, rocker,
            "bgFillColor", 0x80000000L,
            "rockerFillColor", 0x80FFFFFFL,
            "bgStrokeColor", 0xFFFFFFFFL,
            "bgStrokeWidth", 0,
            "bgCornerRadius", 500,
            "rockerCornerRadius", 500,
            CcUtils.fclRatioToZl(rocker.opt("rockerSize") ?: CcJson.inum(500), 500),
        )
        return CcJson.obj(
            "name" to CcJson.str(nameOf(style)),
            "uuid" to CcJson.str(ctx.shortId()),
            "commonStyle" to CcJson.bool(true),
            "lightStyle" to config,
            "darkStyle" to config.deepCopy(),
        )
    }

    /** FCL BUTTON（十字键）方向样式 -> ZL 摇杆样式。 */
    fun fclButtonStyleToZlJoystick(ctx: CcContext, style: JsonObject?): JsonObject {
        val btn = style?.optObj("buttonStyle")?.takeIf { it.entrySet().isNotEmpty() } ?: JsonObject()
        val config = CcConstants.defaultZlJoystickStyleConfig()
        setConfigFrom(
            config, btn,
            "fillColor", 0x80000000L,
            "textColor", 0x80FFFFFFL,
            "strokeColor", 0xFFFFFFFFL,
            "strokeWidth", 10,
            "cornerRadius", 100,
            "cornerRadius", 100,
            0.5,
        )
        return CcJson.obj(
            "name" to CcJson.str(nameOf(style)),
            "uuid" to CcJson.str(ctx.shortId()),
            "commonStyle" to CcJson.bool(true),
            "lightStyle" to config,
            "darkStyle" to config.deepCopy(),
        )
    }

    /** ZL 摇杆样式 -> FCL rockerStyle（fcl_rocker_style_to_zl_joystick 的逆）。 */
    fun zlJoystickStyleToFclRocker(style: JsonObject): JsonObject {
        val light = style.optObj("lightStyle") ?: JsonObject()
        val joystickSize = CcUtils.clampRange(light.opt("joystickSize"), 0.0, 1.0, 0.5)
        return CcJson.obj(
            "rockerSize" to CcJson.inum(Math.max(100, Math.min(1000, CcUtils.pyRound(joystickSize * 1000.0)))),
            "bgCornerRadius" to CcJson.inum(Math.max(0, Math.min(500, CcUtils.clampInt(light.opt("backgroundShape"), 50) * 10))),
            "bgStrokeWidth" to CcJson.inum(Math.max(0, Math.min(500, CcUtils.clampInt(light.opt("borderWidthRatio"), 0) * 10))),
            "bgStrokeColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("borderColor"), -12303292)),
            "bgFillColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("backgroundColor"), 0, light.opt("alpha"))),
            "rockerCornerRadius" to CcJson.inum(Math.max(0, Math.min(500, CcUtils.clampInt(light.opt("joystickShape"), 50) * 10))),
            "rockerStrokeWidth" to CcJson.inum(10),
            "rockerStrokeColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("joystickColor"), -12303292)),
            "rockerFillColor" to CcJson.inum(CcUtils.zlColorToFcl(light.opt("joystickColor"), -7829368, light.opt("alpha"))),
        )
    }

    /** 两个 FCL rockerStyle 语义相等比较（忽略 rockerStrokeColor/Width）。 */
    fun fclRockerStyleMatches(a: JsonObject?, b: JsonObject?): Boolean {
        val comparable = arrayOf(
            "rockerSize", "bgCornerRadius", "bgStrokeWidth", "bgStrokeColor",
            "bgFillColor", "rockerCornerRadius", "rockerFillColor",
        )
        for (key in comparable) {
            val av = a?.opt(key)
            val bv = b?.opt(key)
            if (!CcJson.jsonEquals(av, bv)) return false
        }
        return true
    }

    /**
     * ZL 摇杆样式 -> FCL ROCKER 方向样式。
     * 返回 Pair(要追加的样式列表, 摇杆样式 uuid -> FCL 样式名)。
     */
    fun zlJoystickStylesToFclDirectionStyles(
        ctx: CcContext,
        joystickStyles: List<JsonObject>,
        existingStyles: List<JsonObject>,
    ): Pair<List<JsonObject>, MutableMap<String, String>> {
        val result = mutableListOf<JsonObject>()
        val mapping = linkedMapOf<String, String>()
        val usedNames = LinkedHashSet<String>()
        for (style in existingStyles) usedNames.add(CcJson.toStringV(style.opt("name")))
        val existingByName = linkedMapOf<String, JsonObject>()
        for (style in existingStyles) {
            if (CcJson.toStringV(style.opt("styleType")) == "ROCKER") {
                existingByName[CcJson.toStringV(style.opt("name"))] = style
            }
        }
        val existingRockers = existingByName.values.toList()
        val defaultButtonStyle = CcConstants.defaultFclDirectionStyle().optObj("buttonStyle") ?: JsonObject()

        for (style in joystickStyles) {
            val uuidValue = CcJson.toStringV(style.opt("uuid"))
            val baseName = run {
                val n = style.opt("name")
                if (CcUtils.pyTruthy(n)) CcJson.toStringV(n) else if (uuidValue.isNotEmpty()) uuidValue else "Joystick"
            }
            val convertedRocker = zlJoystickStyleToFclRocker(style)
            var matchedName = ""
            val originalStyle = metaOriginal(style, "fcl", "directionStyle")
            if (originalStyle != null) {
                val candidate = CcJson.toStringV(originalStyle.opt("name"))
                if (existingByName.containsKey(candidate)) matchedName = candidate
            }
            if (matchedName.isEmpty()) {
                for (existing in existingRockers) {
                    if (fclRockerStyleMatches(convertedRocker, existing.optObj("rockerStyle"))) {
                        matchedName = CcJson.toStringV(existing.opt("name"))
                        break
                    }
                }
            }
            if (matchedName.isNotEmpty()) {
                if (uuidValue.isNotEmpty()) mapping[uuidValue] = matchedName
                continue
            }
            var name = styleNameForZlStyle(ctx, baseName, uuidValue)
            var suffix = 2
            while (name in usedNames) {
                name = styleNameForZlStyle(ctx, baseName, uuidValue) + "_" + suffix
                suffix++
            }
            usedNames.add(name)
            if (uuidValue.isNotEmpty()) mapping[uuidValue] = name
            result.add(
                CcJson.obj(
                    "name" to CcJson.str(name),
                    "styleType" to CcJson.str("ROCKER"),
                    "buttonStyle" to defaultButtonStyle.deepCopy(),
                    "rockerStyle" to convertedRocker,
                )
            )
        }
        return Pair(result, mapping)
    }
}
