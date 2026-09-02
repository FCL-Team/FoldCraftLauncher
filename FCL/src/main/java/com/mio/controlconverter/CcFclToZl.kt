package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * FCL -> ZL2 主流程（对应 cc.py fcl_to_zl / normalize_zl_layout）。
 *
 * 默认调用参数与原 Go JNI 一致：includeDirections=false, strict=false,
 * aspect=16/9, lossless=true, absoluteAsPercentage=false。
 * lossless=true 时 includeDirections 亦为真（方向控件统一转 ZL 摇杆）。
 */
object CcFclToZl {

    /** 排序键：把 ZL 按钮几何换算回 FCL 面积占比（cc.py fcl_to_zl 内联逻辑）。 */
    fun zlButtonAreaRatio(button: JsonObject, aspect: Double): Double {
        val pos = button.optObj("position") ?: JsonObject()
        val buttonSize = button.optObj("buttonSize") ?: JsonObject()
        val tempButton = CcJson.obj(
            "baseInfo" to CcJson.obj(
                "xPosition" to CcJson.pyNum(CcUtils.clampInt(pos.opt("x")) / 10.0),
                "yPosition" to CcJson.pyNum(CcUtils.clampInt(pos.opt("y")) / 10.0),
                "sizeType" to CcJson.str("PERCENTAGE"),
                "percentageWidth" to CcJson.obj(
                    "reference" to CcJson.str("SCREEN_WIDTH"),
                    "size" to CcJson.pyNum(CcUtils.clampInt(buttonSize.opt("widthPercentage")) / 10.0),
                ),
                "percentageHeight" to CcJson.obj(
                    "reference" to CcJson.str("SCREEN_WIDTH"),
                    "size" to CcJson.pyNum(CcUtils.clampInt(buttonSize.opt("heightPercentage")) / 10.0),
                ),
            ),
        )
        return CcGeometry.fclButtonAreaRatio(tempButton, aspect)
    }

    fun fclToZl(
        ctx: CcContext,
        data: JsonObject,
        includeDirections: Boolean,
        strict: Boolean,
        aspect: Double,
        lossless: Boolean,
        absoluteAsPercentage: Boolean,
    ): JsonObject {
        val effectiveIncludeDirections = includeDirections || lossless
        val rootOriginal = metaOriginal(data, "zl", "layout")

        val stylesList = (data.optArr("buttonStyles") ?: JsonArray())
            .mapNotNull { it.asObjOrNull() }
            .ifEmpty { listOf(CcConstants.defaultFclStyle()) }
        val (styles, styleMap) = CcStyles.fclStylesToZl(ctx, stylesList)

        val dirStylesInput = (data.optArr("directionStyles") ?: JsonArray())
            .mapNotNull { it.asObjOrNull() }
            .ifEmpty { listOf(CcConstants.defaultFclDirectionStyle()) }
        val directionStyles = CcStyles.directionStyleMap(dirStylesInput)

        val joystickStyles = mutableListOf<JsonObject>()
        rootOriginal?.optArr("joystickStyles")?.mapNotNull { it.asObjOrNull() }?.let { joystickStyles.addAll(it) }
        val joystickStyleUuids = linkedMapOf<String, String>()
        for (item in joystickStyles) {
            val name = CcJson.toStringV(item.opt("name"))
            if (name.isNotEmpty() && !joystickStyleUuids.containsKey(name)) {
                joystickStyleUuids[name] = CcJson.toStringV(item.opt("uuid"))
            }
        }
        var warnedJoystickSettings = false

        val groupIdsByName = LinkedHashMap<String, String>()
        for (item in data.optArr("viewGroups") ?: JsonArray()) {
            val group = item.asObjOrNull() ?: continue
            val id = CcJson.toStringV(group.opt("id"))
            if (id.isEmpty()) continue
            val name = CcUtils.firstTruthyStr(group.opt("name")) ?: "Layer"
            groupIdsByName[name] = id
        }

        val reciprocalOpeners = CcGeometry.inferReciprocalLayerOpeners(data, aspect)

        val layers = mutableListOf<JsonObject>()
        val viewGroups = data.optArr("viewGroups") ?: JsonArray()
        for (groupIndex in viewGroups.size() - 1 downTo 0) {
            val group = viewGroups[groupIndex].asObjOrNull() ?: continue
            val layerOriginal = metaOriginal(group, "zl", "layer")
            val viewData = group.optObj("viewData") ?: JsonObject()
            val groupName = CcUtils.firstTruthyStr(group.opt("name")) ?: "Layer"

            val buttons = mutableListOf<JsonObject>()
            val textBoxes = mutableListOf<JsonObject>()
            val fclButtons = (viewData.optArr("buttonList") ?: JsonArray()).mapNotNull { it.asObjOrNull() }

            val (overlayMatches, consumedDisplayIndices) = CcGeometry.matchFclOverlayButtons(fclButtons, aspect)
            val gridIndices = CcGeometry.inferableGridIndices(fclButtons)

            for ((index, button) in fclButtons.withIndex()) {
                if (index in consumedDisplayIndices) continue
                val hasPayload = CcEvents.fclButtonHasPayload(button)
                if (hasPayload) {
                    val visualButton = overlayMatches[index]?.let { fclButtons[it] }
                    val convertedButton = CcButtons.fclButtonToZl(
                        ctx, button, styleMap, strict, groupName, groupIdsByName,
                        visualButton, absoluteAsPercentage, aspect,
                    )
                    if ((convertedButton.optArr("clickEvents")?.size() ?: 0) > 0) {
                        buttons.add(convertedButton)
                    } else {
                        val vb = visualButton ?: button
                        textBoxes.add(CcButtons.fclButtonToZlTextbox(ctx, vb, styleMap, absoluteAsPercentage, aspect))
                    }
                } else {
                    val buttonId = CcJson.toStringV(button.opt("id"))
                    val openerTarget = reciprocalOpeners[buttonId]
                    var inferredEvents: List<JsonObject> = emptyList()
                    if (!openerTarget.isNullOrEmpty()) {
                        inferredEvents = listOf(
                            CcJson.obj("type" to CcJson.str("switch_layer"), "key" to CcJson.str(openerTarget))
                        )
                    }
                    if (inferredEvents.isEmpty() && index in gridIndices) {
                        inferredEvents = CcGeometry.inferEventsFromGroupNames(button, groupIdsByName, groupName)
                    }
                    if (inferredEvents.isEmpty() && index in gridIndices) {
                        inferredEvents = CcGeometry.inferBuiltinMenuEvents(button)
                    }
                    if (inferredEvents.isNotEmpty()) {
                        val inferredButton = CcButtons.fclButtonToZl(
                            ctx, button, styleMap, strict, groupName, groupIdsByName,
                            null, absoluteAsPercentage, aspect,
                        )
                        val eventsArr = JsonArray()
                        for (e in inferredEvents) eventsArr.add(e)
                        inferredButton.add("clickEvents", eventsArr)
                        inferredButton.addProperty("isSwipple", false)
                        inferredButton.addProperty("isPenetrable", false)
                        buttons.add(inferredButton)
                    } else {
                        buttons.add(
                            CcButtons.fclButtonToZl(
                                ctx, button, styleMap, strict, groupName, groupIdsByName,
                                null, absoluteAsPercentage, aspect,
                            )
                        )
                    }
                }
            }

            val directions = viewData.optArr("directionList") ?: JsonArray()
            val joystickButtons = mutableListOf<JsonObject>()
            if (directions.size() > 0 && !effectiveIncludeDirections) {
                ctx.warn(
                    "skipped ${directions.size()} FCL direction control(s) in group \"${CcJson.toStringV(group.opt("name"))}\"; use --include-directions to convert them",
                    strict,
                )
            }
            if (effectiveIncludeDirections) {
                for (dirItem in directions) {
                    val direction = dirItem.asObjOrNull() ?: continue
                    val directionStyle = CcStyles.resolveDirectionStyle(direction, directionStyles)
                    val isRocker = CcJson.toStringV(directionStyle.opt("styleType")) == "ROCKER"
                    val styleName = CcUtils.firstTruthyStr(directionStyle.opt("name")) ?: "Default"
                    var styleUuid = joystickStyleUuids[styleName]
                    if (styleUuid == null || styleUuid.isEmpty()) {
                        var joystickStyle = if (isRocker) {
                            CcStyles.fclRockerStyleToZlJoystick(ctx, directionStyle)
                        } else {
                            CcStyles.fclButtonStyleToZlJoystick(ctx, directionStyle)
                        }
                        styleUuid = CcJson.toStringV(joystickStyle.opt("uuid"))
                        joystickStyleUuids[styleName] = styleUuid
                        joystickStyle = setMeta(
                            joystickStyle,
                            makeMeta("fcl", "directionStyle", styleName, directionStyle),
                        )
                        joystickStyles.add(joystickStyle)
                    }
                    if (!warnedJoystickSettings) {
                        ctx.warn(
                            "converted FCL direction controls (ROCKER and BUTTON styles) to ZL joystickButtons and joystickStyles (ZL editor v12)",
                            strict,
                        )
                        warnedJoystickSettings = true
                    }
                    joystickButtons.add(
                        CcDirection.directionToZlJoystick(ctx, direction, directionStyle, styleUuid, strict, aspect)
                    )
                    ctx.bump("directions")
                }
            }

            // 大按钮在前（底层），小按钮在后（顶层）
            buttons.sortByDescending { zlButtonAreaRatio(it, aspect) }

            val layerObj = (layerOriginal?.deepCopy()?.asJsonObject) ?: JsonObject()
            layerObj.add("name", CcJson.str(groupName))
            layerObj.add(
                "uuid",
                CcJson.str(CcUtils.firstTruthyStr(group.opt("id"), layerObj.opt("uuid")) ?: ctx.shortId()),
            )
            layerObj.addProperty("hide", CcJson.toStringV(group.opt("visibility")) == "INVISIBLE")
            layerObj.addProperty("hideWhenMouse", CcUtils.pyTruthy(layerObj.opt("hideWhenMouse")))
            layerObj.addProperty("hideWhenGamepad", CcUtils.pyTruthy(layerObj.opt("hideWhenGamepad")))
            layerObj.addProperty("hideWhenJoystick", CcUtils.pyTruthy(layerObj.opt("hideWhenJoystick")))
            run {
                val vt = layerObj.opt("visibilityType")
                layerObj.add("visibilityType", CcJson.str(CcUtils.firstTruthyStr(vt) ?: "always"))
            }
            layerObj.add("normalButtons", JsonArray().also { arr -> buttons.forEach { arr.add(it) } })
            layerObj.add("textBoxes", JsonArray().also { arr -> textBoxes.forEach { arr.add(it) } })
            layerObj.add("joystickButtons", JsonArray().also { arr -> joystickButtons.forEach { arr.add(it) } })

            val originId = CcUtils.firstTruthyStr(group.opt("id"), layerObj.opt("uuid")) ?: ctx.shortId()
            val layerWithMeta = setMeta(layerObj, makeMeta("fcl", "viewGroup", originId, group))
            layers.add(layerWithMeta)
        }

        // 过滤指向不存在图层的 switch/show/hide 事件
        val layerIds = HashSet<String>()
        for (layer in layers) layerIds.add(CcJson.toStringV(layer.opt("uuid")))
        for (layer in layers) {
            val normalButtons = layer.optArr("normalButtons") ?: continue
            for (item in normalButtons) {
                val button = item.asObjOrNull() ?: continue
                val clickEvents = button.optArr("clickEvents") ?: JsonArray()
                val filtered = JsonArray()
                for (eventItem in clickEvents) {
                    val obj = eventItem.asObjOrNull()
                    if (obj == null) {
                        filtered.add(eventItem)
                        continue
                    }
                    val eventType = CcJson.toStringV(obj.opt("type"))
                    val eventKey = CcJson.toStringV(obj.opt("key"))
                    if ((eventType == "switch_layer" || eventType == "show_layer" || eventType == "hide_layer") &&
                        !layerIds.contains(eventKey)
                    ) {
                        continue
                    }
                    filtered.add(eventItem)
                }
                button.add("clickEvents", filtered)
                if (filtered.size() == 0) {
                    button.addProperty("isSwipple", true)
                    button.addProperty("isPenetrable", true)
                    button.addProperty("isToggleable", false)
                }
            }
        }

        val result = (rootOriginal?.deepCopy()?.asJsonObject) ?: JsonObject()
        val resultInfo = result.optObj("info") ?: JsonObject()
        val versionCodeDefault = CcUtils.clampInt(resultInfo.opt("versionCode"), 1)
        val editorVersion = CcUtils.clampInt(result.opt("editorVersion"), CcConstants.ZL_EDITOR_VERSION.toLong())

        val info = CcJson.obj(
            "name" to CcUtils.translatable(
                CcUtils.firstTruthyStr(data.opt("name")) ?: "Converted from FCL",
                resultInfo.optObj("name"),
            ),
            "author" to CcUtils.translatable(
                CcUtils.firstTruthyStr(data.opt("author")) ?: "",
                resultInfo.optObj("author"),
            ),
            "description" to CcUtils.translatable(
                CcUtils.firstTruthyStr(data.opt("description")) ?: "",
                resultInfo.optObj("description"),
            ),
            "versionCode" to CcJson.inum(
                Math.max(0, CcUtils.clampInt(data.opt("versionCode") ?: CcJson.inum(versionCodeDefault), 0))
            ),
            "versionName" to CcJson.str(
                CcUtils.firstTruthyStr(data.opt("version"), resultInfo.opt("versionName")) ?: "1.0"
            ),
        )
        result.add("info", info)
        result.add("layers", JsonArray().also { arr -> layers.forEach { arr.add(it) } })

        val existingStyles = result.optArr("styles")
        val stylesValue: JsonElement = if (existingStyles != null && existingStyles.size() > 0) {
            existingStyles.deepCopy()
        } else {
            JsonArray().also { arr -> styles.forEach { arr.add(it) } }
        }
        result.add("styles", stylesValue)
        result.add("joystickStyles", JsonArray().also { arr -> joystickStyles.forEach { arr.add(it) } })
        result.addProperty("editorVersion", editorVersion)

        var resultId = CcUtils.firstTruthyStr(data.opt("id"), result.optObj("info")?.opt("name")) ?: ctx.shortId()
        val meta = makeMeta("fcl", "controller", resultId, data)
        return setMeta(result, meta)
    }

    /** 补齐当前 ZL kotlinx 模型要求的缺省字段（不改语义）。 */
    fun normalizeZlLayout(layout: JsonObject): JsonObject {
        val result = layout
        if (!result.hasKey("joystickStyles")) result.add("joystickStyles", JsonArray())
        for (layer in result.optArr("layers") ?: JsonArray()) {
            val obj = layer.asObjOrNull() ?: continue
            if (!obj.hasKey("hideWhenMouse")) obj.addProperty("hideWhenMouse", true)
            if (!obj.hasKey("hideWhenGamepad")) obj.addProperty("hideWhenGamepad", true)
            if (!obj.hasKey("hideWhenJoystick")) obj.addProperty("hideWhenJoystick", false)
            if (!obj.hasKey("normalButtons")) obj.add("normalButtons", JsonArray())
            if (!obj.hasKey("textBoxes")) obj.add("textBoxes", JsonArray())
            if (!obj.hasKey("joystickButtons")) obj.add("joystickButtons", JsonArray())
        }
        return result
    }

    fun convertFclToZl(
        ctx: CcContext,
        data: JsonObject,
        includeDirections: Boolean,
        strict: Boolean,
        aspect: Double,
        lossless: Boolean,
        absoluteAsPercentage: Boolean,
    ): JsonObject = normalizeZlLayout(
        fclToZl(ctx, data, includeDirections, strict, aspect, lossless, absoluteAsPercentage)
    )
}
