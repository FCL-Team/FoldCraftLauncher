package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * control-converter Kotlin 移植的回归测试。
 *
 * 金样来自 control-converter-master/go/testdata（Go 版确定性 ID 输出，
 * 与 cc.py 字节级对拍过的基准）。
 */
class ControlConverterTest {

    private fun loadRes(name: String): String =
        javaClass.getResourceAsStream("/controlconverter/$name")!!
            .readBytes().toString(Charsets.UTF_8)

    /** Go 正向输出含 HTML 转义（\u003c 等），还原后与 Python/cc.py 行为一致。 */
    private fun unescapeGo(text: String): String = text
        .replace("\\u003c", "<")
        .replace("\\u003e", ">")
        .replace("\\u0026", "&")

    // --- 数值语义定点测试 ---

    @Test
    fun pyRoundIsBankerRounding() {
        assertEquals(0L, CcUtils.pyRound(0.5))
        assertEquals(2L, CcUtils.pyRound(1.5))
        assertEquals(2L, CcUtils.pyRound(2.5))
        assertEquals(-2L, CcUtils.pyRound(-1.5))
        assertEquals(3L, CcUtils.pyRound(2.6))
        assertEquals(-3L, CcUtils.pyRound(-2.6))
        // 坑 13：万分比转千分比走浮点除 + 银行家舍入（4999 -> 500，整除会得 499）
        assertEquals(500L, CcUtils.scalePositionToFcl(CcJson.inum(4999)))
        assertEquals(500L, CcUtils.scalePositionToFcl(CcJson.inum(5000)))
    }

    @Test
    fun pyFloatFormatMatchesPythonRepr() {
        assertEquals("50.0", CcUtils.pyFloatFormat(50.0))
        assertEquals("-3.0", CcUtils.pyFloatFormat(-3.0))
        assertEquals("-0.0", CcUtils.pyFloatFormat(-0.0))
        assertEquals("0.5", CcUtils.pyFloatFormat(0.5))
        assertEquals("123456.0", CcUtils.pyFloatFormat(123456.0))
        assertEquals("0.0001", CcUtils.pyFloatFormat(0.0001))
        assertEquals("1e-05", CcUtils.pyFloatFormat(0.00001))
        assertEquals("1e+16", CcUtils.pyFloatFormat(1e16))
        assertEquals("0.62", CcUtils.pyFloatFormat(0.62))
    }

    @Test
    fun colorConversions() {
        // ZL 颜色是有符号 Long、ARGB 在高 32 位（Long.MIN_VALUE = 50% 黑，不是透明）
        assertEquals(-2147483648L, CcUtils.zlColorToFcl(CcJson.inum(Long.MIN_VALUE), 0))
        // 0xFFFFFFFF_00000000（高 32 位 ARGB 白色）-> FCL int -1
        assertEquals(-1L, CcUtils.zlColorToFcl(CcJson.inum(-4294967296L), 0))
        // FCL int ARGB -> ZL：值左移 32 位
        assertEquals(-4294967296L, CcUtils.fclArgbToZlColor(-1L))
        assertEquals(Long.MIN_VALUE, CcUtils.fclArgbToZlColor(-2147483648L))
    }

    @Test
    fun keyFallbackSubstitutions() {
        val ctx = CcContext()
        // 侧键 4 -> 滚轮上（1003）
        assertEquals(1003L, CcUtils.convertKeyToFcl(ctx, "GLFW_MOUSE_BUTTON_4", false))
        // F25 -> F24（194）
        assertEquals(194L, CcUtils.convertKeyToFcl(ctx, "GLFW_KEY_F25", false))
        // 旧版键名别名归一化
        assertEquals(17L, CcUtils.convertKeyToFcl(ctx, "key.keyboard.w", false))
        // FCL 键码 121 (KPCOMMA) -> 小数点
        assertEquals(
            CcUtils.ZlEvent("key", "GLFW_KEY_KP_DECIMAL"),
            CcUtils.convertKeyToZl(ctx, 121, false),
        )
        // FCL 键码 0 (RESERVED) -> UNKNOWN(240)
        assertEquals(
            CcUtils.ZlEvent("key", "GLFW_KEY_UNKNOWN"),
            CcUtils.convertKeyToZl(ctx, 0, false),
        )
        // 鼠标滚轮连续滚动 -> launcher_event
        assertEquals(
            CcUtils.ZlEvent("launcher_event", "launcher.event.scroll_up.single"),
            CcUtils.convertKeyToZl(ctx, 1003, false),
        )
    }

    // --- 金样对拍 ---

    /** FCL -> ZL：与 Go 基准（确定性 ID、lossless=true）语义一致。 */
    @Test
    fun goldenFclToZlSemanticMatch() {
        val input = CcJson.loadJson(loadRes("test_fcl_layout.json"))
        val expected = CcJson.parse(unescapeGo(loadRes("output_go.json")))
        val actual = CcConverter.convertFclToZl(input, deterministic = true)
        val diff = firstDiff(expected.asJsonObject, actual, "$")
        assertTrue("semantic diff at $diff", diff == null)
    }

    /** FCL -> ZL：与 Go 基准逐字节一致（还原 HTML 转义后）。 */
    @Test
    fun goldenFclToZlByteMatch() {
        val input = CcJson.loadJson(loadRes("test_fcl_layout.json"))
        val actualText = CcJson.encodePretty(
            CcConverter.convertFclToZl(input, deterministic = true)
        )
        java.io.File("build/golden_actual.json").writeText(actualText)
        val expectedText = unescapeGo(loadRes("output_go.json"))
        assertEquals(expectedText, actualText)
    }

    /**
     * FCL -> ZL -> FCL：lossless 元数据还原。
     *
     * cc.py 语义：按钮/分组/方向控件/事件/几何完整还原；样式名会重命名为
     * "ZL {原名} {uuid前6}"（按钮的 style 引用同步改写，功能一致）。
     */
    @Test
    fun losslessRoundtripFcl() {
        val input = CcJson.loadJson(loadRes("test_fcl_layout.json"))
        val zl1 = CcConverter.convertFclToZl(input, deterministic = true)
        val restored = CcConverter.convertZlToFcl(zl1, deterministic = true)
        java.io.File("build/roundtrip_actual.json").writeText(CcJson.encodePretty(restored))

        // 1) viewGroups（按钮/事件/几何/方向控件）逐字节还原（样式名引用除外）；
        //    组顺序因 fcl<->zl 层级保序转换而反转，按 id 排序后比较
        val inputGroups = cloneWithoutButtonStyle(input.optArr("viewGroups") ?: JsonArray())
        val restoredGroups = cloneWithoutButtonStyle(restored.optArr("viewGroups") ?: JsonArray())
        val diff = firstDiff(sortById(inputGroups), sortById(restoredGroups), "$.viewGroups")
        assertTrue("roundtrip viewGroups diff at $diff", diff == null)

        // 2) 原样式全部保留（改名），且注入了 ZL Native Default
        val restoredNames = restored.optArr("buttonStyles")!!.map { it.asJsonObject.opt("name")!!.asString }
        assertTrue(restoredNames.contains("ZL Native Default"))
        for (style in input.optArr("buttonStyles") ?: JsonArray()) {
            val originName = style.asJsonObject.opt("name")!!.asString
            assertTrue(
                "style $originName not renamed-preserved",
                restoredNames.any { it.startsWith("ZL $originName ") },
            )
        }

        // 3) 再转回 ZL：与第一次 ZL 输出按钮级一致（meta 链路收敛；层序反转按 uuid 排序比较）
        val zl2 = CcConverter.convertFclToZl(restored, deterministic = true)
        val diff2 = firstDiff(
            sortById(cloneWithoutButtonStyle(zl1.optArr("layers") ?: JsonArray())),
            sortById(cloneWithoutButtonStyle(zl2.optArr("layers") ?: JsonArray())),
            "$.layers",
        )
        assertTrue("zl roundtrip layers diff at $diff2", diff2 == null)
    }

    /** 按对象内 id/uuid 字段排序数组（顺序无关比较用）。 */
    private fun sortById(arr: JsonArray): JsonArray {
        val sorted = arr.toList().sortedBy { element ->
            val obj = element.asObjOrNull()
            (obj?.opt("id") ?: obj?.opt("uuid"))?.let { CcJson.toStringV(it) } ?: ""
        }
        val out = JsonArray()
        for (item in sorted) out.add(item)
        return out
    }

    /** 深拷贝、剥离 meta、去掉按钮样式名引用（用于还原比较）；组序/按钮序按 id 排序。 */
    private fun cloneWithoutButtonStyle(arr: JsonArray): JsonArray {
        val out = stripConverterMeta(arr.deepCopy()).asJsonArray
        for (item in out.toList()) {
            val obj = item.asObjOrNull() ?: continue
            // zl 层经 zl->fcl->zl 后会带上 cc.py 注入的 directionList（ZL 侧无此字段）
            obj.remove("directionList")
            obj.optObj("viewData")?.let { viewData ->
                viewData.optArr("buttonList")?.let { buttons ->
                    viewData.add("buttonList", sortById(buttons))
                }
            }
            obj.optArr("normalButtons")?.let { buttons ->
                obj.add("normalButtons", sortById(buttons))
            }
            obj.optArr("joystickButtons")?.let { joysticks ->
                obj.add("joystickButtons", sortById(joysticks))
            }
            for (b in obj.optArr("normalButtons") ?: JsonArray()) {
                b.asJsonObject.remove("buttonStyle")
                b.asJsonObject.remove("style")
            }
            for (j in obj.optArr("joystickButtons") ?: JsonArray()) {
                j.asJsonObject.remove("joystickStyleId")
            }
            for (b in obj.optObj("viewData")?.optArr("buttonList") ?: JsonArray()) {
                b.asJsonObject.remove("style")
            }
        }
        return sortById(out)
    }

    /** 真实 ZL2 布局（ZalithLauncher2 assets/default_layout.json，v10）可转换为合法 FCL 并回转。 */
    @Test
    fun zl2DefaultLayoutConvertsAndBack() {
        val zlInput = CcJson.loadJson(loadRes("zl2_default_layout.json"))
        val fcl = CcConverter.convertZlToFcl(zlInput)
        assertEquals(21L, fcl.opt("controllerVersion")?.asLong)
        assertTrue(fcl.hasKey("viewGroups"))
        assertTrue(fcl.hasKey("buttonStyles"))
        // 回转：meta 恢复原 v10 布局（editorVersion 保留 10 是 cc.py 无损语义）
        val zlBack = CcConverter.convertFclToZl(fcl)
        assertEquals(10L, zlBack.opt("editorVersion")?.asLong)
        val layersIn = zlInput.optArr("layers")?.size() ?: 0
        val layersOut = zlBack.optArr("layers")?.size() ?: 0
        assertEquals(layersIn, layersOut)
        // 首层按钮的文本与坐标无损还原（层序因层级保序转换而反转，按 uuid 查找）
        val firstLayer = zlInput.optArr("layers")!![0].asJsonObject
        val firstLayerUuid = firstLayer.opt("uuid")!!.asString
        val backLayer = (zlBack.optArr("layers")!!.first {
            it.asJsonObject.opt("uuid")?.asString == firstLayerUuid
        }).asJsonObject
        val firstButtonIn = firstLayer.optArr("normalButtons")!![0].asJsonObject
        val firstButtonOut = backLayer.optArr("normalButtons")!![0].asJsonObject
        assertEquals(
            CcUtils.textDefault(firstButtonIn.opt("text")),
            CcUtils.textDefault(firstButtonOut.opt("text")),
        )
        assertEquals(firstButtonIn.optObj("position"), firstButtonOut.optObj("position"))
        // 回转结果仍是合法 ZL2 布局（可再次被检测器识别）
        assertTrue(CcConverter.isZl2Layout(CcJson.encodePretty(zlBack)))
    }

    @Test
    fun detectFormat() {
        val fcl = CcJson.loadJson(loadRes("test_fcl_layout.json"))
        assertEquals("fcl", CcConverter.detectFormat(fcl))
        val zl = CcConverter.convertFclToZl(fcl)
        assertEquals("zl", CcConverter.detectFormat(zl))
    }

    // --- 简易语义差异定位（供断言信息用） ---

    private fun stripMetaForCompare(v: JsonObject): JsonObject {
        val out = v.deepCopy().asJsonObject
        out.remove(CcConstants.META_KEY)
        return out
    }

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
