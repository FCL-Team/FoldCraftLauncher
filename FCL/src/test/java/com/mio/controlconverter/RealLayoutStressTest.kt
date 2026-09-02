package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 真实 FCL 布局（仿原神键位 4bf3d919.json，2.2MB / 100 控件组 / 964 控件，
 * control-converter 项目 AGENTS.md 记录的最大最复杂回归实例）压测：
 *
 * 1. FCL -> ZL2：按 ZalithLauncher2 kotlinx 数据模型逐字段结构校验
 * 2. ZL2 -> FCL 无损往返：控件 id/事件/几何逐一还原（样式改名除外，cc.py 语义）
 * 3. 格式检测与默认参数行为
 */
class RealLayoutStressTest {

    private fun loadRes(name: String): String =
        javaClass.getResourceAsStream("/controlconverter/$name")!!
            .readBytes().toString(Charsets.UTF_8)

    // --- ZL2 结构校验（依据 D:\Project\ZalithLauncher2 LayerController 数据模型） ---

    private val zlVisibility = setOf("always", "in_game", "in_menu")
    private val zlSizeType = setOf("dp", "percentage", "wrap_content")
    private val zlReference = setOf("screen_width", "screen_height")
    private val zlEventTypes = setOf("key", "launcher_event", "switch_layer", "show_layer", "hide_layer", "send_text")
    private val zlTriggerMode = setOf("drag", "touch")
    private val zlDirectionKeys = setOf(
        "north", "north_east", "north_west", "south", "south_east", "south_west", "east", "west",
    )

    private fun validateZlLayout(root: JsonObject) {
        assertEquals(12L, root.opt("editorVersion")?.asLong)
        assertTrue(root.optObj("info") != null)
        val info = root.optObj("info")!!
        assertTrue(info.optObj("name")!!.hasKey("default"))
        assertTrue(info.optObj("author")!!.hasKey("default"))
        assertTrue(info.optObj("description")!!.hasKey("default"))

        val layerUuids = HashSet<String>()
        for (layer in root.optArr("layers") ?: JsonArray()) {
            val obj = layer.asObjOrNull() ?: continue
            layerUuids.add(obj.opt("uuid")!!.asString)
        }

        var buttonCount = 0
        var textboxCount = 0
        var joystickCount = 0
        val styleUuids = HashSet<String>()
        for (style in root.optArr("styles") ?: JsonArray()) {
            styleUuids.add(style.asJsonObject.opt("uuid")!!.asString)
        }
        val joystickStyleUuids = HashSet<String>()
        for (style in root.optArr("joystickStyles") ?: JsonArray()) {
            val obj = style.asJsonObject
            assertTrue(obj.hasKey("commonStyle"))
            assertTrue(obj.optObj("lightStyle") != null)
            assertTrue(obj.optObj("darkStyle") != null)
            joystickStyleUuids.add(obj.opt("uuid")!!.asString)
        }

        for (layer in root.optArr("layers") ?: JsonArray()) {
            val obj = layer.asObjOrNull() ?: continue
            assertTrue(obj.hasKey("name"))
            assertTrue(obj.hasKey("uuid"))
            assertTrue(obj.hasKey("hide"))
            assertTrue(CcJson.toStringV(obj.opt("visibilityType")) in zlVisibility)
            assertTrue(obj.hasKey("hideWhenMouse"))
            assertTrue(obj.hasKey("hideWhenGamepad"))
            assertTrue(obj.hasKey("hideWhenJoystick"))
            for (key in arrayOf("normalButtons", "textBoxes", "joystickButtons")) {
                assertTrue("layer ${obj.opt("uuid")} missing $key", obj.hasKey(key))
            }

            for (button in obj.optArr("normalButtons") ?: JsonArray()) {
                buttonCount++
                validateZlButton(button.asJsonObject, layerUuids, styleUuids)
            }
            for (textbox in obj.optArr("textBoxes") ?: JsonArray()) {
                textboxCount++
                val t = textbox.asJsonObject
                assertTrue(t.hasKey("text") && t.hasKey("uuid") && t.hasKey("buttonSize"))
            }
            for (joystick in obj.optArr("joystickButtons") ?: JsonArray()) {
                joystickCount++
                validateZlJoystick(joystick.asJsonObject, joystickStyleUuids)
            }
        }
        println(
            "ZL2 output stats: layers=${root.optArr("layers")?.size()}" +
                " buttons=$buttonCount textBoxes=$textboxCount joysticks=$joystickCount" +
                " styles=${root.optArr("styles")?.size()} joystickStyles=${root.optArr("joystickStyles")?.size()}"
        )
    }

    private fun validateZlButton(button: JsonObject, layerUuids: Set<String>, styleUuids: Set<String>) {
        assertTrue(button.optObj("text")!!.hasKey("default"))
        assertTrue(button.hasKey("uuid"))
        val position = button.optObj("position")!!
        assertTrue(position.opt("x")!!.asLong in 0..10000)
        assertTrue(position.opt("y")!!.asLong in 0..10000)
        val size = button.optObj("buttonSize")!!
        assertTrue(CcJson.toStringV(size.opt("type")) in zlSizeType)
        assertTrue(CcJson.toStringV(size.opt("widthReference")) in zlReference)
        assertTrue(CcJson.toStringV(size.opt("heightReference")) in zlReference)
        assertTrue(size.opt("widthPercentage")!!.asLong in 100..10000)
        assertTrue(size.opt("heightPercentage")!!.asLong in 100..10000)
        val styleRef = button.opt("buttonStyle")
        if (styleRef != null && styleRef.asString.isNotEmpty()) {
            assertTrue("unknown buttonStyle ref ${styleRef.asString}", styleRef.asString in styleUuids)
        }
        assertTrue(CcJson.toStringV(button.opt("visibilityType")) in zlVisibility)
        assertTrue(button.hasKey("isSwipple") && button.hasKey("isPenetrable") && button.hasKey("isToggleable"))
        for (event in button.optArr("clickEvents") ?: JsonArray()) {
            val e = event.asJsonObject
            assertTrue("bad event type ${e.opt("type")}", CcJson.toStringV(e.opt("type")) in zlEventTypes)
        }
    }

    private fun validateZlJoystick(joystick: JsonObject, joystickStyleUuids: Set<String>) {
        assertTrue(joystick.hasKey("uuid"))
        val position = joystick.optObj("position")!!
        assertTrue(position.opt("x")!!.asLong in 0..10000)
        assertTrue(position.opt("y")!!.asLong in 0..10000)
        assertTrue(CcJson.toStringV(joystick.opt("sizeType")) in setOf("dp", "percentage"))
        assertTrue(CcJson.toStringV(joystick.opt("visibilityType")) in zlVisibility)
        assertTrue(CcJson.toStringV(joystick.opt("triggerMode")) in zlTriggerMode)
        val styleId = joystick.opt("joystickStyleId")?.asString
        if (!styleId.isNullOrEmpty()) {
            assertTrue("unknown joystickStyleId $styleId", styleId in joystickStyleUuids)
        }
        val directionEvents = joystick.optObj("directionEvents")!!
        for (key in zlDirectionKeys) {
            assertTrue("missing directionEvents.$key", directionEvents.hasKey(key))
        }
    }

    // --- 测试 ---

    /** FCL -> ZL2：整体转换 + ZL2 结构校验。 */
    @Test
    fun realFclToZl2Structure() {
        val input = CcJson.loadJson(loadRes("real_4bf3d919_fcl.json"))
        assertEquals(100, input.optArr("viewGroups")?.size())

        val start = System.currentTimeMillis()
        val zl = CcConverter.convertFclToZl(input)
        val elapsed = System.currentTimeMillis() - start
        println("FCL->ZL2 conversion took ${elapsed}ms")

        assertTrue(CcConverter.isZl2Layout(CcJson.encodePretty(zl)))
        validateZlLayout(zl)
    }

    /** ZL2 -> FCL 无损往返：id 集合、事件、几何逐一还原。 */
    @Test
    fun realLayoutLosslessRoundtrip() {
        val input = CcJson.loadJson(loadRes("real_4bf3d919_fcl.json"))
        val zl = CcConverter.convertFclToZl(input)
        val restored = CcConverter.convertZlToFcl(zl)
        java.io.File("build/real_roundtrip.json").writeText(CcJson.encodePretty(restored))

        // 合并按钮集合：zl1 中 meta.mapping.generatedFrom == overlay-merge 的按钮，
        // 其文字取自配对的显示按钮，回写 FCL 时会覆盖原事件按钮的 text（cc.py 语义）
        val mergedIds = HashSet<String>()
        for (layer in zl.optArr("layers") ?: JsonArray()) {
            val obj = layer.asObjOrNull() ?: continue
            for (button in obj.optArr("normalButtons") ?: JsonArray()) {
                val b = button.asJsonObject
                val generatedFrom = b.opt(CcConstants.META_KEY).asObjOrNull()
                    ?.optObj("mapping")?.opt("generatedFrom")?.asString
                if (generatedFrom == "overlay-merge") {
                    mergedIds.add(b.opt("uuid")!!.asString)
                }
            }
        }

        // 控件组集合一致
        val inputGroups = input.optArr("viewGroups")!!.mapNotNull { it.asObjOrNull() }
        val restoredGroups = restored.optArr("viewGroups")!!.mapNotNull { it.asObjOrNull() }
        assertEquals(
            inputGroups.map { it.opt("id")!!.asString }.toSet(),
            restoredGroups.map { it.opt("id")!!.asString }.toSet(),
        )

        // 每个控件（含方向控件）的完整内容还原，仅样式名引用不同（cc.py 改名语义）
        val inputButtons = LinkedHashMap<String, JsonObject>()
        val inputDirections = LinkedHashMap<String, JsonObject>()
        for (group in inputGroups) {
            val viewData = group.optObj("viewData")!!
            for (b in viewData.optArr("buttonList") ?: JsonArray()) {
                val obj = b.asJsonObject
                inputButtons[obj.opt("id")!!.asString] = obj
            }
            for (d in viewData.optArr("directionList") ?: JsonArray()) {
                val obj = d.asJsonObject
                inputDirections[obj.opt("id")!!.asString] = obj
            }
        }
        val restoredButtons = LinkedHashMap<String, JsonObject>()
        val restoredDirections = LinkedHashMap<String, JsonObject>()
        for (group in restoredGroups) {
            val viewData = group.optObj("viewData")!!
            for (b in viewData.optArr("buttonList") ?: JsonArray()) {
                val obj = b.asJsonObject
                restoredButtons[obj.opt("id")!!.asString] = obj
            }
            for (d in viewData.optArr("directionList") ?: JsonArray()) {
                val obj = d.asJsonObject
                restoredDirections[obj.opt("id")!!.asString] = obj
            }
        }
        // 控件 id 集合：被「显示/事件按钮合并」消费的纯显示按钮在正向转换中会并入
        // 事件按钮（cc.py match_fcl_overlay_buttons 语义），因此允许其消失，
        // 但消失者必须是"无 payload 且有文字"的可合并显示按钮，且不得新增 id。
        val missingIds = inputButtons.keys - restoredButtons.keys
        val extraIds = restoredButtons.keys - inputButtons.keys
        assertTrue("unexpected new button ids: $extraIds", extraIds.isEmpty())
        val nonMergeable = missingIds.filter { id ->
            val b = inputButtons[id]!!
            CcEvents.fclButtonHasPayload(b) || CcJson.toStringV(b.opt("text")).trim().isEmpty()
        }
        assertTrue(
            "non-mergeable buttons disappeared: $nonMergeable (merged away: ${missingIds.size})",
            nonMergeable.isEmpty(),
        )
        println(
            "roundtrip: ${inputButtons.size} input buttons, " +
                "${missingIds.size} merged into event buttons, ${restoredButtons.size} restored"
        )

        var diffCount = 0
        var firstDiffPath: String? = null
        for ((id, restoredButtonFull) in restoredButtons) {
            val origin = inputButtons[id]!!
            val restoredButton = stripConverterMeta(restoredButtonFull.deepCopy()).asJsonObject
            restoredButton.remove("style")
            val expected = origin.deepCopy().asJsonObject
            expected.remove("style")
            if (id in mergedIds) {
                // 合并按钮 = 显示按钮的文字/几何 + 事件按钮的负载（cc.py 设计），故只比较 event
                val expectedEvent = CcJson.obj("event" to expected.opt("event")!!)
                val actualEvent = CcJson.obj("event" to restoredButton.opt("event")!!)
                if (!CcJson.jsonEquals(expectedEvent, actualEvent)) {
                    diffCount++
                    if (firstDiffPath == null) {
                        firstDiffPath = "merged button $id: " + (firstDiff(expectedEvent, actualEvent, id) ?: "unknown")
                    }
                }
                continue
            }
            // ZL2 格式下限：百分比尺寸最小 1%（100/10=10）、绝对尺寸最小 5dp，
            // 低于下限的原值经往返会被钳制（cc.py fcl_size_to_zl / clamp_zl_dp 语义）
            applyZlFloors(expected)
            applyZlFloors(restoredButton)
            if (!CcJson.jsonEquals(expected, restoredButton)) {
                diffCount++
                if (firstDiffPath == null) {
                    firstDiffPath = "button $id: " + (firstDiff(expected, restoredButton, id) ?: "unknown")
                }
            }
        }
        assertEquals("buttons not fully restored, first: $firstDiffPath", 0, diffCount)

        for ((id, origin) in inputDirections) {
            val restoredDirection = stripConverterMeta(restoredDirections[id]!!.deepCopy()).asJsonObject
            restoredDirection.remove("style")
            val expected = origin.deepCopy().asJsonObject
            expected.remove("style")
            assertTrue(
                "direction $id not restored: ${firstDiff(expected, restoredDirection, id)}",
                CcJson.jsonEquals(expected, restoredDirection),
            )
        }

        // 事件总键码数守恒（不含样式改名的干扰）
        var inputKeycodeCount = 0
        for (button in inputButtons.values) {
            val event = button.optObj("event") ?: continue
            for (name in CcEvents.EVENT_NAMES) {
                inputKeycodeCount += (event.optObj(name)?.optArr("outputKeycodes")?.size() ?: 0)
            }
        }
        var restoredKeycodeCount = 0
        for (button in restoredButtons.values) {
            val event = button.optObj("event") ?: continue
            for (name in CcEvents.EVENT_NAMES) {
                restoredKeycodeCount += (event.optObj(name)?.optArr("outputKeycodes")?.size() ?: 0)
            }
        }
        assertEquals("keycode count drift", inputKeycodeCount, restoredKeycodeCount)
    }

    /** 检测器与转换入口行为。 */
    @Test
    fun realLayoutDetection() {
        val text = loadRes("real_4bf3d919_fcl.json")
        assertTrue(CcConverter.isFclLayout(text))
        assertTrue(!CcConverter.isZl2Layout(text))
        val data = CcJson.loadJson(text)
        assertEquals("fcl", CcConverter.detectFormat(data))
        val auto = CcConverter.convertAuto(data)
        assertTrue(CcConverter.isZl2Layout(CcJson.encodePretty(auto)))
    }

    /** 把低于 ZL2 下限的尺寸值抬到下限（百分比 10 千分比 / 绝对 5dp），便于还原比较。 */
    private fun applyZlFloors(button: JsonObject) {
        val baseInfo = button.optObj("baseInfo") ?: return
        for (key in arrayOf("percentageWidth", "percentageHeight")) {
            val block = baseInfo.optObj(key) ?: continue
            val size = block.opt("size")
            if (size != null && size.asLong < 10) block.addProperty("size", 10)
        }
        for (key in arrayOf("absoluteWidth", "absoluteHeight")) {
            val v = baseInfo.opt(key) ?: continue
            if (v.asLong < 5) baseInfo.addProperty(key, 5)
        }
    }

    /** 转换真实布局并导出 ZL2 文件（供真机导入 ZalithLauncher2 实测渲染）。 */
    @Test
    fun exportConvertedZl2File() {
        val input = CcJson.loadJson(loadRes("real_4bf3d919_fcl.json"))
        val zl = CcConverter.convertFclToZl(input)
        val out = java.io.File("build/converted/4bf3d919_zl2.json")
        out.parentFile.mkdirs()
        out.writeText(CcJson.encodePretty(zl))
        println("exported ${out.absolutePath} (${out.length()} bytes)")
        assertTrue(CcConverter.isZl2Layout(CcJson.encodePretty(zl)))
    }

    // --- 差异定位 ---

    private fun firstDiff(a: com.google.gson.JsonElement, b: com.google.gson.JsonElement, path: String): String? {
        if (CcJson.jsonEquals(a, b)) return null
        if (a.isJsonObject && b.isJsonObject) {
            val oa = a.asJsonObject
            val ob = b.asJsonObject
            for ((k, v) in oa.entrySet()) {
                if (!ob.has(k)) return "$path.$k missing in actual"
                val d = firstDiff(v, ob.get(k), "$path.$k") ?: continue
                return d
            }
            for (k in ob.keySet()) {
                if (!oa.has(k)) return "$path.$k unexpected in actual"
            }
            return null
        }
        if (a.isJsonArray && b.isJsonArray) {
            val aa = a.asJsonArray
            val ab = b.asJsonArray
            if (aa.size() != ab.size()) return "$path size ${aa.size()} != ${ab.size()}"
            for (i in 0 until aa.size()) {
                val d = firstDiff(aa[i], ab[i], "$path[$i]") ?: continue
                return d
            }
            return null
        }
        return "$path: expected=$a actual=$b"
    }
}
