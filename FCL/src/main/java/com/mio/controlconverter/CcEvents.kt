package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * 事件转换（对应 cc.py fcl_event_to_zl_events / apply_zl_event_to_fcl /
 * apply_zl_layer_events_to_fcl / normalize_zl_click_events）。
 */
object CcEvents {

    fun fclEventHasPayload(event: JsonObject): Boolean {
        val kcl = CcUtils.fclKeycodeList(event.opt("outputKeycodes"))
        if (kcl.size() > 0) return true
        if (CcUtils.pyTruthy(event.opt("input"))) return true
        if (CcUtils.pyTruthy(event.opt("openMenu"))) return true
        if (CcJson.toStringV(event.opt("outputText")).isNotEmpty()) return true
        if ((event.optArr("bindViewGroup")?.size() ?: 0) > 0) return true
        if (CcUtils.pyTruthy(event.opt("switchTouchMode"))) return true
        if (CcUtils.pyTruthy(event.opt("switchMouseMode"))) return true
        if (CcUtils.pyTruthy(event.opt("quickInput"))) return true
        return false
    }

    fun fclButtonHasPayload(button: JsonObject): Boolean {
        val eventRoot = button.optObj("event") ?: JsonObject()
        for (eventName in EVENT_NAMES) {
            val event = eventRoot.optObj(eventName) ?: JsonObject()
            if (fclEventHasPayload(event)) return true
        }
        return false
    }

    fun fclButtonIsDecorative(button: JsonObject): Boolean {
        if (fclButtonHasPayload(button)) return false
        val eventRoot = button.optObj("event") ?: JsonObject()
        if (CcUtils.pyTruthy(eventRoot.opt("pointerFollow")) || CcUtils.pyTruthy(eventRoot.opt("Movable"))) return false
        return true
    }

    /** Go strconv.Quote 简化版（仅告警文案用）。 */
    private fun quote(s: String): String {
        val b = StringBuilder("\"")
        for (r in s) {
            when (r) {
                '"' -> b.append("\\\"")
                '\\' -> b.append("\\\\")
                '\n' -> b.append("\\n")
                '\r' -> b.append("\\r")
                '\t' -> b.append("\\t")
                else -> b.append(r)
            }
        }
        b.append("\"")
        return b.toString()
    }

    /**
     * FCL 事件对象 -> ZL clickEvents 列表（cc.py fcl_event_to_zl_events）。
     */
    fun fclEventToZlEvents(
        ctx: CcContext,
        event: JsonObject,
        strict: Boolean,
        label: String,
        eventName: String,
        groupIdsByName: Map<String, String>,
        substitutions: MutableList<JsonObject>,
    ): List<JsonObject> {
        val result = mutableListOf<JsonObject>()
        val autoClick = CcUtils.pyTruthy(event.opt("autoClick"))
        val keycodes = CcUtils.fclKeycodeList(event.opt("outputKeycodes")).map { CcUtils.clampInt(it) }

        if ((eventName == "clickEvent" || eventName == "doubleClickEvent") && fclEventHasPayload(event)) {
            val reason = "FCL $eventName has no exact ZL timing equivalent; converted to a normal ZL press/release event"
            ctx.warn("$reason on button ${quote(label)}", strict, once = true)
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_event"), "event" to CcJson.str(eventName)),
                    CcJson.obj("type" to CcJson.str("zl_click_events")),
                    reason,
                )
            )
        }
        if (eventName == "longPressEvent" && fclEventHasPayload(event)) {
            val reason = "FCL longPressEvent has no exact ZL timing equivalent; converted to a normal event"
            ctx.warn("$reason on button ${quote(label)}", strict, once = true)
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_event"), "event" to CcJson.str(eventName)),
                    CcJson.obj("type" to CcJson.str("zl_click_events")),
                    reason,
                )
            )
        }
        if (autoClick && keycodes.any { !CcConstants.FCL_SCROLL_REVERSE.containsKey(it) }) {
            val reason = "FCL autoClick only has a ZL equivalent for scroll events; non-scroll keys are converted as normal press events"
            ctx.warn(reason, strict, once = true)
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_auto_click"), "event" to CcJson.str(eventName)),
                    CcJson.obj("type" to CcJson.str("zl_normal_press")),
                    reason,
                )
            )
        }
        for (keycode in keycodes) {
            val converted = CcUtils.convertKeyToZl(ctx, keycode, strict, autoClick, label, substitutions)
            if (converted != null) {
                result.add(
                    CcJson.obj("type" to CcJson.str(converted.eventType), "key" to CcJson.str(converted.key))
                )
            }
        }
        if (CcUtils.pyTruthy(event.opt("input"))) {
            result.add(CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_ime")))
        }
        if (CcUtils.pyTruthy(event.opt("openMenu"))) {
            result.add(CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_menu")))
        }
        if (CcUtils.pyTruthy(event.opt("switchTouchMode"))) {
            val reason = "FCL switchTouchMode has no ZL equivalent; substituted with launcher menu toggle"
            ctx.warn(reason, strict, once = true)
            result.add(CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_menu")))
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_event"), "key" to CcJson.str("switchTouchMode")),
                    CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_menu")),
                    reason,
                )
            )
        }
        if (CcUtils.pyTruthy(event.opt("switchMouseMode"))) {
            val reason = "FCL switchMouseMode has no ZL equivalent; substituted with launcher menu toggle"
            ctx.warn(reason, strict, once = true)
            result.add(CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_menu")))
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_event"), "key" to CcJson.str("switchMouseMode")),
                    CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_menu")),
                    reason,
                )
            )
        }
        if (CcUtils.pyTruthy(event.opt("quickInput"))) {
            val reason = "FCL quickInput has no ZL equivalent; substituted with input method toggle"
            ctx.warn(reason, strict, once = true)
            result.add(CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_ime")))
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_event"), "key" to CcJson.str("quickInput")),
                    CcJson.obj("type" to CcJson.str("launcher_event"), "key" to CcJson.str("launcher.event.switch_ime")),
                    reason,
                )
            )
        }
        val outputText = CcJson.toStringV(event.opt("outputText"))
        if (outputText.isNotEmpty()) {
            result.add(CcJson.obj("type" to CcJson.str("send_text"), "key" to CcJson.str(outputText)))
        }
        val bindGroups = (event.optArr("bindViewGroup") ?: JsonArray()).map { CcJson.toStringV(it) }
        val chatId = groupIdsByName["聊天"] ?: ""
        var suppressChatLayer = false
        if (chatId.isNotEmpty()) {
            val hasKeyT = result.any {
                CcJson.toStringV(it.opt("type")) == "key" && CcJson.toStringV(it.opt("key")) == "GLFW_KEY_T"
            }
            if (hasKeyT && bindGroups.any { it == chatId }) suppressChatLayer = true
        }
        for (groupId in bindGroups) {
            if (suppressChatLayer && groupId == chatId) continue
            result.add(CcJson.obj("type" to CcJson.str("switch_layer"), "key" to CcJson.str(groupId)))
        }
        return result
    }

    fun normalizeZlClickEvents(events: List<JsonObject>): List<JsonObject> {
        val deduped = CcUtils.dedupeEvents(events)
        val otherEvents = mutableListOf<JsonObject>()
        var firstSendText: JsonObject? = null
        for (event in deduped) {
            val isSendText = CcJson.toStringV(event.opt("type")) == "send_text" &&
                CcJson.toStringV(event.opt("key")).isNotEmpty()
            if (isSendText) {
                if (firstSendText == null) firstSendText = event
            } else {
                otherEvents.add(event)
            }
        }
        firstSendText?.let { otherEvents.add(it) }
        return otherEvents
    }

    // --- ZL -> FCL 方向 ---

    /** 单个 ZL click event 应用到 FCL 事件对象（图层类事件由 layer 版本处理）。 */
    fun applyZlEventToFcl(
        ctx: CcContext,
        event: JsonObject,
        fclEvent: JsonObject,
        strict: Boolean,
        substitutions: MutableList<JsonObject>? = null,
    ) {
        val etype = event.opt("type")?.let { CcJson.toStringV(it) }
        val rawKey = event.opt("key")?.let { CcJson.toStringV(it) } ?: ""
        val key = CcUtils.normalizeZlKey(rawKey)
        val pressEvent = fclEvent.optObj("pressEvent")!!
        val clickEvent = fclEvent.optObj("clickEvent")!!

        when (etype) {
            "key" -> {
                val keycode = CcUtils.convertKeyToFcl(ctx, key, strict, substitutions)
                pressEvent.optArr("outputKeycodes")?.add(CcJson.inum(keycode))
            }
            "launcher_event" -> {
                val mouse = CcConstants.FCL_MOUSE[key]
                when {
                    mouse != null -> pressEvent.optArr("outputKeycodes")?.add(CcJson.inum(mouse))
                    key == "launcher.event.switch_ime" -> clickEvent.addProperty("input", true)
                    key == "launcher.event.switch_menu" -> clickEvent.addProperty("openMenu", true)
                    key == "launcher.event.scroll_up.single" -> clickEvent.optArr("outputKeycodes")?.add(CcJson.inum(1003))
                    key == "launcher.event.scroll_down.single" -> clickEvent.optArr("outputKeycodes")?.add(CcJson.inum(1004))
                    key == "launcher.event.scroll_up" || key == "launcher.event.scroll_down" -> {
                        val code = if (key.endsWith("scroll_up")) 1003 else 1004
                        pressEvent.addProperty("autoClick", true)
                        pressEvent.optArr("outputKeycodes")?.add(CcJson.inum(code.toLong()))
                    }
                    else -> {
                        val keycode = CcUtils.convertKeyToFcl(ctx, key, strict, substitutions)
                        pressEvent.optArr("outputKeycodes")?.add(CcJson.inum(keycode))
                    }
                }
            }
            "switch_layer", "show_layer", "hide_layer" -> {
                // 图层事件在 applyZlLayerEventsToFcl 中按可见状态模拟转换
            }
            "send_text" -> clickEvent.addProperty("outputText", rawKey)
            null -> {}
            else -> {
                val reason = "unsupported ZL event type '$etype'; substituted with no-op text event"
                ctx.warn(reason, strict)
                substitutions?.add(
                    substitution(
                        ctx,
                        CcJson.obj("type" to CcJson.str(etype), "key" to CcJson.str(rawKey)),
                        CcJson.obj("type" to CcJson.str("send_text"), "key" to CcJson.str("")),
                        reason,
                    )
                )
                if (clickEvent.opt("outputText") == null) clickEvent.addProperty("outputText", "")
            }
        }
    }

    /**
     * ZL 图层事件 -> FCL bindViewGroup（模拟初始可见状态，仅输出奇数次 toggle）。
     */
    fun applyZlLayerEventsToFcl(
        ctx: CcContext,
        events: List<JsonObject>,
        fclEvent: JsonObject,
        strict: Boolean,
        initialLayerState: Map<String, Boolean>,
        layerIdMap: Map<String, String>? = null,
        substitutions: MutableList<JsonObject>? = null,
    ) {
        val localState = LinkedHashMap(initialLayerState)
        val toggles = mutableListOf<String>()

        for (event in events) {
            val etype = event.opt("type")?.let { CcJson.toStringV(it) }
            if (etype != "switch_layer" && etype != "show_layer" && etype != "hide_layer") continue
            val rawKey = event.opt("key")?.let { CcJson.toStringV(it) } ?: ""
            val targetId = layerIdMap?.get(rawKey)?.takeIf { it.isNotEmpty() } ?: rawKey
            if (targetId.isEmpty()) continue

            val current = localState[targetId] ?: false
            var shouldToggle = false
            when (etype) {
                "switch_layer" -> {
                    shouldToggle = true
                    localState[targetId] = !current
                }
                "show_layer" -> if (!current) {
                    shouldToggle = true
                    localState[targetId] = true
                }
                "hide_layer" -> if (current) {
                    shouldToggle = true
                    localState[targetId] = false
                }
            }

            if (shouldToggle) {
                toggles.add(targetId)
            } else if (etype != "switch_layer" && substitutions != null) {
                substitutions.add(
                    substitution(
                        ctx,
                        CcJson.obj("type" to CcJson.str(etype!!), "key" to CcJson.str(rawKey)),
                        CcJson.obj("type" to CcJson.str("no_op"), "key" to CcJson.str(targetId)),
                        "Layer already ${if (etype == "show_layer") "visible" else "hidden"} in the simulated ZL state; skipped FCL toggle",
                        "layers",
                    )
                )
            }
        }

        // FCL 的 bindViewGroup 每项都是 toggle；同一图层偶数次会抵消，只输出奇数次
        val counts = LinkedHashMap<String, Int>()
        for (t in toggles) counts[t] = (counts[t] ?: 0) + 1
        val orderedUnique = mutableListOf<String>()
        val seen = HashSet<String>()
        for (targetId in toggles) {
            if (targetId in seen || (counts[targetId] ?: 0) % 2 == 0) continue
            seen.add(targetId)
            orderedUnique.add(targetId)
        }
        fclEvent.optObj("clickEvent")?.optArr("bindViewGroup")?.let { bind ->
            for (id in orderedUnique) bind.add(CcJson.str(id))
        }

        if (substitutions != null && events.any {
                val t = it.opt("type")?.let { e -> CcJson.toStringV(e) }
                t == "show_layer" || t == "hide_layer"
            }
        ) {
            val sourceEvents = JsonArray()
            for (e in events) {
                val t = e.opt("type")?.let { CcJson.toStringV(it) }
                if (t == "switch_layer" || t == "show_layer" || t == "hide_layer") sourceEvents.add(e.deepCopy())
            }
            substitutions.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("zl_layer_state_events"), "events" to sourceEvents),
                    CcJson.obj(
                        "type" to CcJson.str("fcl_bindViewGroup"),
                        "keys" to JsonArray().also { arr -> orderedUnique.forEach { arr.add(CcJson.str(it)) } },
                    ),
                    "Converted ZL show/hide/switch layer events by simulating initial layer visibility and emitting only necessary FCL toggles",
                    "layers",
                )
            )
        }
    }

    val EVENT_NAMES = arrayOf("pressEvent", "clickEvent", "doubleClickEvent", "longPressEvent")
}
