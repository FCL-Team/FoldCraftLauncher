package com.mio.controlconverter

import com.google.gson.JsonObject

/**
 * ZL2 -> FCL 主流程（对应 cc.py zl_to_fcl 及其辅助函数）。
 */
object CcZlToFcl {

    /** Python str()（null -> "None"），用于键名/uuid/styleId 等字符串化。 */
    private fun pyStr(value: com.google.gson.JsonElement?): String {
        if (value == null || value.isJsonNull) return "None"
        return CcJson.toStringV(value)
    }

    /** cc.py `x or fallback`（字符串视角：空串视为假）。 */
    private fun truthyStr(value: com.google.gson.JsonElement?): String? {
        val s = CcJson.toStringV(value)
        return if (s.isEmpty()) null else s
    }

    fun makeBaseInfoFromZl(
        ctx: CcContext,
        button: JsonObject,
        layerVisibility: String?,
        strict: Boolean,
        label: String,
        styleName: String?,
        fclStyles: List<JsonObject>,
    ): JsonObject {
        val size = button.optObj("buttonSize") ?: JsonObject()
        val sizeKind = size.opt("type")?.let { CcJson.toStringV(it) } ?: ""
        val sizeType: String
        val absoluteWidth: Long
        val absoluteHeight: Long
        if (sizeKind == "absolute" || sizeKind == "dp") {
            sizeType = "ABSOLUTE"
            absoluteWidth = CcUtils.clampInt(size.opt("widthDp"), 50)
            absoluteHeight = CcUtils.clampInt(size.opt("heightDp"), 50)
        } else if (sizeKind == "wrap_content") {
            sizeType = "ABSOLUTE"
            val (w, h) = CcUtils.estimateWrapContentDp(button, styleName ?: "", fclStyles)
            absoluteWidth = w
            absoluteHeight = h
            val widgetLabel = label.ifEmpty {
                CcUtils.textDefault(button.opt("text")).ifEmpty {
                    CcJson.toStringV(button.opt("uuid")).ifEmpty { "<unnamed>" }
                }
            }
            ctx.warn(
                "ZL wrap_content size on widget \"$widgetLabel\" has no exact FCL equivalent; estimated dp size",
                strict,
                once = true,
            )
        } else {
            sizeType = "PERCENTAGE"
            absoluteWidth = CcUtils.clampInt(size.opt("widthDp"), 50)
            absoluteHeight = CcUtils.clampInt(size.opt("heightDp"), 50)
        }
        val visibilitySrc = button.opt("visibilityType")?.let { CcJson.toStringV(it) }?.takeIf { it.isNotEmpty() }
            ?: (layerVisibility ?: "")
        val position = button.optObj("position") ?: JsonObject()
        return CcJson.obj(
            "visibilityType" to CcJson.str(CcUtils.visibilityZlToFcl(visibilitySrc)),
            "xPosition" to CcJson.inum(CcUtils.scalePositionToFcl(position.opt("x"))),
            "yPosition" to CcJson.inum(CcUtils.scalePositionToFcl(position.opt("y"))),
            "sizeType" to CcJson.str(sizeType),
            "absoluteWidth" to CcJson.inum(absoluteWidth),
            "absoluteHeight" to CcJson.inum(absoluteHeight),
            "percentageWidth" to CcJson.obj(
                "reference" to CcJson.str(CcUtils.zlRefToFcl(CcJson.toStringV(size.opt("widthReference")))),
                "size" to CcJson.inum(CcUtils.scalePositionToFcl(size.opt("widthPercentage") ?: CcJson.inum(500))),
            ),
            "percentageHeight" to CcJson.obj(
                "reference" to CcJson.str(CcUtils.zlRefToFcl(CcJson.toStringV(size.opt("heightReference")))),
                "size" to CcJson.inum(CcUtils.scalePositionToFcl(size.opt("heightPercentage") ?: CcJson.inum(500))),
            ),
        )
    }

    private fun warnUnmappedLayerFlags(ctx: CcContext, layer: JsonObject, strict: Boolean) {
        val layerName = CcJson.toStringV(layer.opt("name")).ifEmpty {
            CcJson.toStringV(layer.opt("uuid")).ifEmpty { "Layer" }
        }
        if (CcUtils.pyTruthy(layer.opt("hideWhenMouse"))) {
            ctx.warn("ZL layer \"$layerName\" hideWhenMouse has no FCL equivalent; skipped", strict, once = true)
        }
        if (CcUtils.pyTruthy(layer.opt("hideWhenGamepad"))) {
            ctx.warn("ZL layer \"$layerName\" hideWhenGamepad has no FCL equivalent; skipped", strict, once = true)
        }
        if (CcUtils.pyTruthy(layer.opt("hideWhenJoystick"))) {
            ctx.warn("ZL layer \"$layerName\" hideWhenJoystick has no FCL equivalent; skipped", strict, once = true)
        }
    }

    private fun resolveZlButtonStyleName(
        styleUuid: com.google.gson.JsonElement?,
        styleMap: Map<String, String>,
        fallback: String,
    ): String {
        if (styleUuid == null || styleUuid.isJsonNull) return fallback
        return styleMap[pyStr(styleUuid)] ?: fallback
    }

    private fun overlaySharedFieldsFcl(
        ctx: CcContext,
        original: JsonObject,
        current: JsonObject,
        layerVisibility: String?,
        styleMap: Map<String, String>,
        strict: Boolean,
        fclStyles: List<JsonObject>,
    ): JsonObject {
        val restored = original.deepCopy().asJsonObject
        val styleUuid = current.opt("buttonStyle")
        val fallback = truthyStr(restored.opt("style")) ?: "ZL Native Default"
        val styleName = resolveZlButtonStyleName(styleUuid, styleMap, fallback)
        val restoredId = truthyStr(current.opt("uuid"))
            ?: truthyStr(restored.opt("id"))
            ?: ctx.fclId()
        val text = CcUtils.textDefault(current.opt("text"))
        val baseInfo = makeBaseInfoFromZl(ctx, current, layerVisibility, strict, text, styleName, fclStyles)
        restored.add("id", CcJson.str(restoredId))
        restored.add("text", CcJson.str(text))
        restored.add("style", CcJson.str(styleName))
        restored.add("baseInfo", baseInfo)
        return restored
    }

    private fun zlButtonToFcl(
        ctx: CcContext,
        button: JsonObject,
        layerVisibility: String?,
        styleMap: Map<String, String>,
        strict: Boolean,
        layerIdMap: Map<String, String>,
        fclStyles: List<JsonObject>,
        initialLayerState: Map<String, Boolean>,
        currentLayerId: String,
    ): JsonObject? {
        val original = metaOriginal(button, "fcl", "button")
        if (original != null) {
            val restored = overlaySharedFieldsFcl(ctx, original, button, layerVisibility, styleMap, strict, fclStyles)
            val originId = truthyStr(button.opt("uuid")) ?: truthyStr(restored.opt("id")) ?: ctx.fclId()
            return setMeta(restored, makeMeta("zl", "button", originId, button))
        }

        if (metaOriginal(button, "fcl", "direction") != null) return null

        val event = CcConstants.fclButtonEvent()
        val substitutions = mutableListOf<JsonObject>()
        val clickEvents = button.optArr("clickEvents") ?: emptyList()
        for (clickEvent in clickEvents) {
            val obj = clickEvent.asObjOrNull() ?: continue
            CcEvents.applyZlEventToFcl(ctx, obj, event, strict, substitutions)
        }
        val simulatedState = LinkedHashMap(initialLayerState)
        if (currentLayerId.isNotEmpty()) simulatedState[currentLayerId] = true
        val eventObjs = clickEvents.mapNotNull { it.asObjOrNull() }
        CcEvents.applyZlLayerEventsToFcl(
            ctx, eventObjs, event, strict, simulatedState, layerIdMap, substitutions,
        )

        if (CcUtils.pyTruthy(button.opt("isToggleable"))) {
            event.optObj("pressEvent")?.addProperty("autoKeep", true)
        }

        val styleName = resolveZlButtonStyleName(button.opt("buttonStyle"), styleMap, "ZL Native Default")
        val text = CcUtils.textDefault(button.opt("text"))
        val baseInfo = makeBaseInfoFromZl(ctx, button, layerVisibility, strict, text, styleName, fclStyles)
        val id = truthyStr(button.opt("uuid")) ?: ctx.fclId()
        val result = CcJson.obj(
            "id" to CcJson.str(id),
            "text" to CcJson.str(text),
            "style" to CcJson.str(styleName),
            "baseInfo" to baseInfo,
            "event" to event,
        )
        val originId = truthyStr(button.opt("uuid")) ?: CcJson.toStringV(result.opt("id"))
        val mapping = appendSubstitutions(null, substitutions)
        return setMeta(result, makeMeta("zl", "button", originId, button, mapping))
    }

    private fun zlTextboxToFcl(
        ctx: CcContext,
        textbox: JsonObject,
        layerVisibility: String?,
        styleMap: Map<String, String>,
        strict: Boolean,
        fclStyles: List<JsonObject>,
    ): JsonObject? {
        val original = metaOriginal(textbox, "fcl", "button")
        if (original != null) {
            val restored = overlaySharedFieldsFcl(ctx, original, textbox, layerVisibility, styleMap, strict, fclStyles)
            val originId = truthyStr(textbox.opt("uuid")) ?: truthyStr(restored.opt("id")) ?: ctx.fclId()
            return setMeta(restored, makeMeta("zl", "textbox", originId, textbox))
        }
        if (metaOriginal(textbox, "fcl", "direction") != null) return null

        val styleName = resolveZlButtonStyleName(textbox.opt("buttonStyle"), styleMap, "ZL Native Default")
        val text = CcUtils.textDefault(textbox.opt("text"))
        val baseInfo = makeBaseInfoFromZl(ctx, textbox, layerVisibility, strict, text, styleName, fclStyles)
        val id = truthyStr(textbox.opt("uuid")) ?: ctx.fclId()
        val result = CcJson.obj(
            "id" to CcJson.str(id),
            "text" to CcJson.str(text),
            "style" to CcJson.str(styleName),
            "baseInfo" to baseInfo,
            "event" to CcConstants.fclButtonEvent(),
        )
        val originId = truthyStr(textbox.opt("uuid")) ?: CcJson.toStringV(result.opt("id"))
        return setMeta(result, makeMeta("zl", "textbox", originId, textbox))
    }

    /** 装饰性按钮垫底（FCL 后加的 view 在上层）。 */
    fun orderFclButtonsForLayer(buttons: List<JsonObject>): List<JsonObject> {
        val decorated = buttons.mapIndexed { index, button -> Triple(index, CcEvents.fclButtonIsDecorative(button), button) }
        return decorated.sortedWith(compareBy({ if (it.second) 0 else 1 }, { it.first })).map { it.third }
    }

    private fun layerIsBackgroundLike(group: JsonObject): Boolean {
        val buttons = group.optObj("viewData")?.optArr("buttonList") ?: return false
        if (buttons.size() == 0) return false
        var decorative = 0
        for (b in buttons) {
            val obj = b.asObjOrNull() ?: continue
            if (CcEvents.fclButtonIsDecorative(obj)) decorative++
        }
        return decorative == buttons.size()
    }

    /** 全装饰的 viewGroup 垫底。 */
    fun orderFclViewGroups(groups: List<JsonObject>): List<JsonObject> {
        val decorated = groups.mapIndexed { index, group -> Triple(index, layerIsBackgroundLike(group), group) }
        return decorated.sortedWith(compareBy({ if (it.second) 0 else 1 }, { it.first })).map { it.third }
    }

    /** 推断互为伴随可见的图层（cc.py infer_visible_companion_layers）。 */
    fun inferVisibleCompanionLayers(data: JsonObject, layerIdMap: Map<String, String>): Map<String, Set<String>> {
        val companions = linkedMapOf<String, MutableSet<String>>()
        val layerIds = linkedSetOf<String>()
        val hiddenLayers = linkedSetOf<String>()
        for (layer in data.optArr("layers") ?: emptyList()) {
            val obj = layer.asObjOrNull() ?: continue
            val u = CcJson.toStringV(obj.opt("uuid"))
            if (u.isEmpty()) continue
            layerIds.add(u)
            if (CcUtils.pyTruthy(obj.opt("hide"))) hiddenLayers.add(u)
        }
        val openerTargets = linkedMapOf<String, MutableSet<String>>()

        for (layer in data.optArr("layers") ?: emptyList()) {
            val layerObj = layer.asObjOrNull() ?: continue
            val sourceId = CcJson.toStringV(layerObj.opt("uuid"))
            for (button in layerObj.optArr("normalButtons") ?: emptyList()) {
                val buttonObj = button.asObjOrNull() ?: continue
                val events = buttonObj.optArr("clickEvents") ?: continue
                val visibleTargets = mutableListOf<String>()
                for (event in events) {
                    val eventObj = event.asObjOrNull() ?: continue
                    val etype = eventObj.opt("type")?.let { CcJson.toStringV(it) } ?: ""
                    val rawKey = pyStr(eventObj.opt("key"))
                    val targetId = layerIdMap[rawKey] ?: rawKey
                    if ((etype != "show_layer" && etype != "switch_layer") || !layerIds.contains(targetId)) continue
                    visibleTargets.add(targetId)
                    if (sourceId.isNotEmpty()) {
                        openerTargets.getOrPut(targetId) { linkedSetOf() }.add(sourceId)
                    }
                }
                if (visibleTargets.size < 2) continue
                val group = visibleTargets.toCollection(linkedSetOf())
                for (targetId in group) {
                    companions.getOrPut(targetId) { linkedSetOf() }.addAll(group)
                }
            }
        }

        for ((targetId, sourceIds) in openerTargets.entries.toList()) {
            if (!hiddenLayers.contains(targetId)) continue
            val companionIds = companions[targetId] ?: linkedSetOf()
            val coOpened = companionIds.any { openerTargets.containsKey(it) }
            if (!coOpened) continue
            for (sourceId in sourceIds) {
                if (sourceId.isNotEmpty() && !companionIds.contains(sourceId) && hiddenLayers.contains(sourceId)) {
                    companions.getOrPut(targetId) { linkedSetOf() }.add(sourceId)
                }
            }
        }
        return companions
    }

    fun zlToFcl(ctx: CcContext, data: JsonObject, strict: Boolean): JsonObject {
        val rootOriginal = metaOriginal(data, "fcl", "controller")
        val info = data.optObj("info") ?: JsonObject()
        val stylesIn = data.optArr("styles") ?: emptyList()
        val (styles, styleMap) = CcStyles.zlStylesToFcl(
            ctx,
            stylesIn.mapNotNull { it.asObjOrNull() },
        )
        var existingDirectionStyles = rootOriginal?.optArr("directionStyles")
            ?.mapNotNull { it.asObjOrNull() }
            ?: emptyList()
        if (existingDirectionStyles.isEmpty()) {
            existingDirectionStyles = listOf(CcConstants.defaultFclDirectionStyle())
        }
        val joystickStylesIn = (data.optArr("joystickStyles") ?: emptyList()).mapNotNull { it.asObjOrNull() }
        val (joystickStyleStyles, joystickStyleNames) = CcStyles.zlJoystickStylesToFclDirectionStyles(
            ctx, joystickStylesIn, existingDirectionStyles,
        )
        val viewGroups = mutableListOf<JsonObject>()

        val layerIdMap = linkedMapOf<String, String>()
        val initialLayerState = linkedMapOf<String, Boolean>()
        for (layer in data.optArr("layers") ?: emptyList()) {
            val layerObj = layer.asObjOrNull() ?: continue
            val layerOriginal = metaOriginal(layerObj, "fcl", "viewGroup")
            val layerUuid = truthyStr(layerObj.opt("uuid"))
                ?: truthyStr(layerOriginal?.opt("id"))
                ?: ctx.fclId()
            val key = truthyStr(layerObj.opt("uuid")) ?: layerUuid
            layerIdMap[key] = layerUuid
            initialLayerState[layerUuid] = !CcUtils.pyTruthy(layerObj.opt("hide"))
        }
        val companionLayers = inferVisibleCompanionLayers(data, layerIdMap)

        for (layer in data.optArr("layers") ?: emptyList()) {
            val layerObj = layer.asObjOrNull() ?: continue
            val layerOriginal = metaOriginal(layerObj, "fcl", "viewGroup")
            warnUnmappedLayerFlags(ctx, layerObj, strict)
            val layerVisibility = layerObj.opt("visibilityType")?.let { CcJson.toStringV(it) } ?: "always"
            val layerUuid = CcJson.toStringV(layerObj.opt("uuid"))
            val currentLayerId = layerIdMap[layerUuid] ?: layerUuid
            val layerStateForButtons = LinkedHashMap(initialLayerState)
            val companions = companionLayers[currentLayerId]
            if (companions != null) {
                for (companionId in companions) layerStateForButtons[companionId] = true
            } else {
                layerStateForButtons[currentLayerId] = true
            }

            val buttons = mutableListOf<JsonObject>()
            for (button in layerObj.optArr("normalButtons") ?: emptyList()) {
                val buttonObj = button.asObjOrNull() ?: continue
                val converted = zlButtonToFcl(
                    ctx, buttonObj, layerVisibility, styleMap, strict,
                    layerIdMap, styles, layerStateForButtons, currentLayerId,
                )
                if (converted != null) buttons.add(converted)
            }
            for (textbox in layerObj.optArr("textBoxes") ?: emptyList()) {
                val textboxObj = textbox.asObjOrNull() ?: continue
                val converted = zlTextboxToFcl(ctx, textboxObj, layerVisibility, styleMap, strict, styles)
                if (converted != null) buttons.add(converted)
            }

            val restoredGroup = layerOriginal?.deepCopy()?.asJsonObject ?: JsonObject()
            val directionList = restoredGroup.optObj("viewData")?.optArr("directionList")
                ?.mapNotNull { it.asObjOrNull() }?.map { it.deepCopy().asJsonObject }?.toMutableList()
                ?: mutableListOf()
            val restoredDirectionIds = HashSet<String>()
            for (item in directionList) {
                truthyStr(item.opt("id"))?.let { restoredDirectionIds.add(it) }
            }
            for (joystick in layerObj.optArr("joystickButtons") ?: emptyList()) {
                val joystickObj = joystick.asObjOrNull() ?: continue
                val joystickOriginal = metaOriginal(joystickObj, "fcl", "direction")
                if (joystickOriginal != null) {
                    val origId = CcJson.toStringV(joystickOriginal.opt("id"))
                    if (restoredDirectionIds.contains(origId)) continue
                }
                val styleId = pyStr(joystickObj.opt("joystickStyleId"))
                var styleName = joystickStyleNames[styleId]
                if (styleName == null) {
                    val layerName = pyStr(layerObj.opt("name"))
                    ctx.warn(
                        "ZL joystick on layer \"$layerName\" references unknown joystickStyleId; using ROCKER style from its style definition",
                        strict,
                        once = true,
                    )
                    styleName = "ZL Joystick"
                }
                val convertedDirection = CcDirection.zlJoystickToFclDirection(
                    ctx, joystickObj, layerVisibility, strict, styleName,
                )
                if (convertedDirection != null) directionList.add(convertedDirection)
            }

            val groupId = truthyStr(layerObj.opt("uuid"))
                ?: truthyStr(restoredGroup.opt("id"))
                ?: ctx.fclId()
            val groupName = truthyStr(layerObj.opt("name"))
                ?: truthyStr(restoredGroup.opt("name"))
                ?: "Layer"
            val resultGroup = CcJson.obj(
                "id" to CcJson.str(groupId),
                "name" to CcJson.str(groupName),
                "visibility" to CcJson.str(if (CcUtils.pyTruthy(layerObj.opt("hide"))) "INVISIBLE" else "VISIBLE"),
                "viewData" to CcJson.obj(
                    "buttonList" to com.google.gson.JsonArray().also {
                        arr -> orderFclButtonsForLayer(buttons).forEach { btn -> arr.add(btn) }
                    },
                    "directionList" to com.google.gson.JsonArray().also {
                        arr -> directionList.forEach { d -> arr.add(d.deepCopy()) }
                    },
                ),
            )
            val originId = truthyStr(layerObj.opt("uuid")) ?: CcJson.toStringV(resultGroup.opt("id"))
            val meta = makeMeta("zl", "layer", originId, layerObj)
            if (directionList.isNotEmpty()) {
                val originalSection = meta.optObj("original")
                if (originalSection != null) {
                    val dirArr = com.google.gson.JsonArray()
                    for (d in directionList) dirArr.add(d.deepCopy())
                    originalSection.add("directionList", dirArr)
                }
            }
            viewGroups.add(setMeta(resultGroup, meta))
        }

        val result = rootOriginal?.deepCopy()?.asJsonObject ?: JsonObject()
        val newId = truthyStr(data.opt("id")) ?: truthyStr(result.opt("id")) ?: ctx.shortId().take(8)
        val newName = CcUtils.textDefault(info.opt("name")).ifEmpty {
            truthyStr(result.opt("name")) ?: "Converted from Zalith"
        }
        val newVersion = CcUtils.textDefault(info.opt("versionName")).ifEmpty {
            truthyStr(result.opt("version")) ?: "1.0"
        }
        val versionCodeDefault = CcUtils.clampInt(result.opt("versionCode"), 1)
        val newVersionCode = CcUtils.clampInt(info.opt("versionCode") ?: CcJson.inum(versionCodeDefault), 1)
        val newAuthor = CcUtils.textDefault(info.opt("author")).ifEmpty { truthyStr(result.opt("author")) ?: "" }
        val newDescription = CcUtils.textDefault(info.opt("description")).ifEmpty { truthyStr(result.opt("description")) ?: "" }
        val newControllerVersion = CcUtils.clampInt(
            result.opt("controllerVersion"), CcConstants.FCL_CONTROLLER_VERSION.toLong(),
        )

        result.add("id", CcJson.str(newId))
        result.add("name", CcJson.str(newName))
        result.add("version", CcJson.str(newVersion))
        result.add("versionCode", CcJson.inum(newVersionCode))
        result.add("author", CcJson.str(newAuthor))
        result.add("description", CcJson.str(newDescription))
        result.add("controllerVersion", CcJson.inum(newControllerVersion))
        result.add(
            "buttonStyles",
            com.google.gson.JsonArray().also { arr -> styles.forEach { arr.add(it) } },
        )
        result.add(
            "directionStyles",
            com.google.gson.JsonArray().also { arr ->
                existingDirectionStyles.forEach { arr.add(it.deepCopy()) }
                joystickStyleStyles.forEach { arr.add(it) }
            },
        )
        result.add(
            "viewGroups",
            com.google.gson.JsonArray().also { arr -> orderFclViewGroups(viewGroups).forEach { arr.add(it) } },
        )

        val originId = truthyStr(data.opt("id")) ?: CcJson.toStringV(result.opt("id"))
        return setMeta(result, makeMeta("zl", "layout", originId, data))
    }
}
