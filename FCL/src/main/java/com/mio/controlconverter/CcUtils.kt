package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.Locale

/**
 * control-converter 数值/文本工具层（移植 cc-rs/src/utils.rs，语义基准 cc.py）。
 *
 * 关键约束（AGENTS.md 坑清单）：
 * - 所有 round 均为 Python 银行家舍入（round-half-to-even）
 * - scale_position_to_fcl 的除以 10 是浮点除 + 银行家舍入（4999 得 500），不是整除
 * - 浮点输出使用 Python repr（pyFloatFormat），如 50.0、1e-07
 */
object CcUtils {

    // --- Python 风格数值 ---

    /** Python round()：银行家舍入。 */
    fun pyRound(f: Double): Long {
        if (f.isNaN() || f.isInfinite()) return 0
        val t = f.toLong()
        if (Math.abs(f - t) == 0.5) {
            return if (t % 2 == 0L) t else if (f >= 0.0) t + 1 else t - 1
        }
        return Math.round(f)
    }

    /** Python repr(float)：整值带 .0（绝对值小于 1e16），其余最短往返表示。 */
    fun pyFloatFormat(v: Double): String {
        if (v.isNaN() || v.isInfinite()) return "null"
        if (v == Math.floor(v) && Math.abs(v) < 1e16) {
            if (v == 0.0 && java.lang.Double.doubleToRawLongBits(v) < 0) return "-0.0"
            return v.toLong().toString() + ".0"
        }
        return formatGShortest(v)
    }

    /** 最短往返数字串（等价 ryu 最短表示），科学计数阈值与 Python repr 一致。 */
    private fun formatGShortest(v: Double): String {
        val neg = java.lang.Double.doubleToRawLongBits(v) < 0
        val a = Math.abs(v)
        if (a == 0.0) return if (neg) "-0" else "0"
        val pair = shortestDigits(a)
        val digits = pair.first
        val dp = pair.second
        val body = if (dp <= -4 || dp > 16) fmtEMode(digits, dp - 1) else fmtFMode(digits, dp)
        return if (neg) "-" + body else body
    }

    /** 找最短能往返的十进制有效数字，返回 Pair(有效数字串, 小数点位置 dp)。 */
    private fun shortestDigits(v: Double): Pair<String, Int> {
        var s = ""
        var p = 1
        while (p < 17) {
            s = String.format(Locale.ROOT, "%." + (p - 1) + "E", v)
            if (s.toDouble() == v) break
            p++
        }
        if (p >= 17) s = String.format(Locale.ROOT, "%.16E", v)
        val eIdx = s.indexOf('E')
        val mant = s.substring(0, eIdx)
        val expPart = s.substring(eIdx + 1).toInt()
        val digitsAll = mant.filter { it.isDigit() }
        val trimmed = digitsAll.trimStart('0').trimEnd('0').ifEmpty { "0" }
        val dot = mant.indexOf('.')
        val dotPos = if (dot >= 0) dot else mant.length
        val leadingZeros = digitsAll.length - digitsAll.trimStart('0').length
        val e10 = dotPos - 1 + expPart - leadingZeros
        return Pair(trimmed, e10 + 1)
    }

    private fun fmtFMode(digits: String, dp: Int): String {
        val sb = StringBuilder()
        if (dp <= 0) {
            sb.append("0.")
            repeat(-dp) { sb.append('0') }
            sb.append(digits)
        } else if (dp >= digits.length) {
            sb.append(digits)
            repeat(dp - digits.length) { sb.append('0') }
        } else {
            sb.append(digits, 0, dp).append('.').append(digits, dp, digits.length)
        }
        return sb.toString()
    }

    /** Python repr 指数格式：指数至少两位（如 1e-05）。 */
    private fun fmtEMode(digits: String, exp: Int): String {
        val sb = StringBuilder()
        sb.append(digits[0])
        if (digits.length > 1) sb.append('.').append(digits, 1, digits.length)
        sb.append('e')
        sb.append(if (exp < 0) '-' else '+')
        val ae = Math.abs(exp)
        if (ae >= 10) sb.append(ae) else sb.append('0').append(ae)
        return sb.toString()
    }

    // --- Python 取值语义 ---

    private fun isIntegralLiteral(lit: String): Boolean =
        !lit.any { it == '.' || it == 'e' || it == 'E' }

    /** Python float()：数字按值、bool 为 1.0/0.0、字符串尽力解析，其余失败。 */
    fun toFloat(value: JsonElement?): Double? = when {
        value == null -> null
        value.isJsonNull -> null
        value.isJsonPrimitive -> when {
            value.asJsonPrimitive.isNumber -> value.asJsonPrimitive.asString.toDoubleOrNull()
            value.asJsonPrimitive.isBoolean -> if (value.asBoolean) 1.0 else 0.0
            else -> value.asString.trim().toDoubleOrNull()
        }
        else -> null
    }

    /** Python isinstance(x, int) 视角：整数字面量取值、bool 为 1/0，浮点字面量不算。 */
    fun toLongVal(value: JsonElement?): Long? = when {
        value == null || value.isJsonNull -> null
        value.isJsonPrimitive && value.asJsonPrimitive.isNumber -> {
            val lit = value.asJsonPrimitive.asString
            if (isIntegralLiteral(lit)) lit.toLongOrNull() else null
        }
        value.isJsonPrimitive && value.asJsonPrimitive.isBoolean -> if (value.asBoolean) 1L else 0L
        else -> null
    }

    /** Python str()：bool 输出 True/False，float 用 repr，int 用原字面量，null 输出空串。 */
    fun toStringV(value: JsonElement?): String = when {
        value == null || value.isJsonNull -> ""
        value.isJsonPrimitive -> when {
            value.asJsonPrimitive.isBoolean -> if (value.asBoolean) "True" else "False"
            value.asJsonPrimitive.isNumber -> {
                val lit = value.asJsonPrimitive.asString
                if (isIntegralLiteral(lit)) lit
                else lit.toDoubleOrNull()?.let { pyFloatFormat(it) } ?: lit
            }
            else -> value.asString
        }
        value.isJsonObject || value.isJsonArray -> value.toString()
        else -> ""
    }

    /** Python bool()。 */
    fun pyTruthy(value: JsonElement?): Boolean = when {
        value == null || value.isJsonNull -> false
        value.isJsonPrimitive -> when {
            value.asJsonPrimitive.isBoolean -> value.asBoolean
            value.asJsonPrimitive.isNumber -> (value.asJsonPrimitive.asString.toDoubleOrNull() ?: 0.0) != 0.0
            else -> value.asString.isNotEmpty()
        }
        value.isJsonArray -> value.asJsonArray.size() > 0
        value.isJsonObject -> value.asJsonObject.size() > 0
        else -> false
    }

    // --- clamp 系列（对齐 cc.py） ---

    fun clampInt(value: JsonElement?, default: Long = 0): Long {
        val f = toFloat(value) ?: return default
        return pyRound(f)
    }

    fun clampFloat(value: JsonElement?, default: Double = 0.0): Double {
        val f = toFloat(value) ?: return default
        return if (f.isFinite()) f else default
    }

    fun clampRange(value: JsonElement?, minimum: Double, maximum: Double, default: Double): Double =
        Math.max(minimum, Math.min(maximum, clampFloat(value, default)))

    fun clampZlDp(value: JsonElement?, default: Double = 50.0): Double =
        Math.max(5.0, clampFloat(value, default))

    fun clampZlShape(value: JsonElement?, default: Double = 0.0): Double =
        clampRange(value, 0.0, 100.0, default)

    fun clampZlBorderWidth(value: JsonElement?, default: Long = 0): Long =
        Math.max(0, Math.min(50, clampInt(value, default)))

    /** 万分比转千分比：浮点除 + 银行家舍入（4999 得 500）。 */
    fun scalePositionToFcl(value: JsonElement?): Long {
        val c = clampInt(value)
        return Math.max(0, Math.min(1000, pyRound(c.toDouble() / 10.0)))
    }

    fun scalePositionToZl(value: JsonElement?): Long =
        Math.max(0, Math.min(10000, clampInt(CcJson.inum(clampInt(value) * 10))))

    fun zlRefToFcl(ref: String?): String = if (ref == "screen_height") "SCREEN_HEIGHT" else "SCREEN_WIDTH"

    fun fclRefToZl(ref: String?): String = if (ref == "SCREEN_HEIGHT") "screen_height" else "screen_width"

    fun fclRefNameToZl(reference: String?): String =
        if (reference == "SCREEN_HEIGHT") "screen_height" else "screen_width"

    fun visibilityZlToFcl(value: String?): String = when (value ?: "always") {
        "always" -> "ALWAYS"
        "in_game" -> "IN_GAME"
        "menu", "in_menu" -> "MENU"
        else -> "ALWAYS"
    }

    fun visibilityFclToZl(value: String?): String = when (value ?: "ALWAYS") {
        "ALWAYS" -> "always"
        "IN_GAME" -> "in_game"
        "MENU" -> "in_menu"
        else -> "always"
    }

    fun zlShapeToFclRadius(shape: JsonObject?): Long {
        if (shape == null) return 100
        var sum = 0.0
        for (k in SHAPE_KEYS) {
            sum += clampZlShape(shape.opt(k), 0.0)
        }
        val radius = pyRound(sum / 4.0 * 10.0)
        return Math.max(0, Math.min(500, radius))
    }

    // --- 颜色（ZL 有符号 Long 高 32 位 ARGB 与 FCL 有符号 int 互转） ---

    fun signedInt32(value: Long): Long {
        val v = value and 0xFFFFFFFFL
        return if (v >= 0x80000000L) v - 0x100000000L else v
    }

    fun applyArgbAlpha(color: Long, alphaValue: Double): Long {
        if (alphaValue >= 0.999) return color
        val argb = color and 0xFFFFFFFFL
        val a = (argb ushr 24) and 0xFFL
        val scaled = Math.max(0, Math.min(255, pyRound(a.toDouble() * alphaValue)))
        return signedInt32((scaled shl 24) or (argb and 0x00FFFFFFL))
    }

    fun zlColorToFcl(color: JsonElement?, fallback: Long, alpha: JsonElement? = null): Long {
        val alphaValue = clampRange(alpha, 0.0, 1.0, 1.0)
        val c = toLongVal(color)
        if (c != null) {
            val argb = (c ushr 32) and 0xFFFFFFFFL
            if (argb != 0L || c == 0L) {
                return applyArgbAlpha(signedInt32(argb), alphaValue)
            }
            if (c in -2147483648L..2147483647L) {
                return applyArgbAlpha(c, alphaValue)
            }
        }
        return applyArgbAlpha(fallback, alphaValue)
    }

    fun fclArgbToZlColor(color: Long): Long = (color and 0xFFFFFFFFL) shl 32

    fun fclArgbToZlColor(color: JsonElement?, fallback: Long = 0): Long =
        (clampInt(color, fallback) and 0xFFFFFFFFL) shl 32

    fun fclFontToZl(value: JsonElement?, default: Long = 12): Long =
        Math.max(2, Math.min(30, clampInt(value, default)))

    fun fclRadiusToZlPercent(value: JsonElement?, default: Long = 500): Long {
        val v = clampInt(value, default)
        return Math.max(0, Math.min(50, v / 10))
    }

    fun fclRatioToZl(value: JsonElement?, default: Long = 500): Double =
        Math.max(0.0, Math.min(1.0, clampInt(value, default) / 1000.0))

    /** FCL 千分比尺寸转 ZL 百分比（100..10000）。 */
    fun fclSizeToZl(value: JsonElement?): Long {
        val inner = clampInt(value, 50)
        return Math.max(100, Math.min(10000, clampInt(CcJson.inum(inner * 10))))
    }

    fun fclKeycodeList(value: JsonElement?): JsonArray = when {
        value != null && value.isJsonArray -> value.asJsonArray
        value == null || value.isJsonNull -> JsonArray()
        else -> {
            val a = JsonArray()
            a.add(value)
            a
        }
    }

    // --- 文本 ---

    fun textDefault(value: JsonElement?): String {
        val obj = value.asObjOrNull()
        if (obj != null) {
            val d = obj.opt("default") ?: return ""
            return toStringV(d)
        }
        if (value == null || value.isJsonNull) return ""
        return toStringV(value)
    }

    /** ZL TranslatableString 构造（cc.py 语义：default 缺失时用 text，存在但为空保留空）。 */
    fun translatable(text: String, source: JsonObject? = null): JsonObject {
        if (source != null) {
            val defaultVal: String = if (source.hasKey("default")) {
                val raw = source.opt("default")
                if (pyTruthy(raw)) toStringV(raw) else ""
            } else {
                text
            }
            val mq = source.optArr("matchQueue")
            if (mq != null) {
                return CcJson.obj(
                    "default" to CcJson.str(defaultVal),
                    "matchQueue" to mq.deepCopy(),
                )
            }
        }
        return CcJson.obj(
            "default" to CcJson.str(text),
            "matchQueue" to JsonArray(),
        )
    }

    // --- 键名归一化与键码换算 ---

    fun normalizeZlKey(eventKey: String?): String {
        var key = (eventKey ?: "").trim()
        val upperKey = key.uppercase()
        if (upperKey.startsWith("GLFW_") || upperKey.startsWith("MOUSE_")) key = upperKey
        return CcConstants.ZL_KEY_ALIASES[key] ?: key
    }

    fun convertKeyToFcl(
        ctx: CcContext,
        eventKey: String,
        strict: Boolean,
        substitutions: MutableList<JsonObject>? = null,
    ): Long {
        val key = normalizeZlKey(eventKey)
        CcConstants.FCL_MOUSE[key]?.let { return it }
        CcConstants.GLFW_TO_FCL[key]?.let { return it }
        CcConstants.ZL_TO_FCL_FALLBACKS[key]?.let { pair ->
            val keycode = pair.first
            val reason = pair.second
            ctx.warn("ZL key event '$key' has no exact FCL equivalent; $reason", strict)
            substitutions?.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("key"), "key" to CcJson.str(key)),
                    CcJson.obj("type" to CcJson.str("fcl_keycode"), "keycode" to CcJson.inum(keycode)),
                    reason,
                    "keys",
                )
            )
            return keycode
        }
        val knownReason = CcConstants.UNSUPPORTED_ZL_KEY_REASONS[key]
        if (knownReason != null) {
            ctx.warn("ZL key event '$key' has no FCL control keycode equivalent: $knownReason; substituted with UNKNOWN", strict)
        } else {
            ctx.warn("unsupported ZL key event '$key'; substituted with UNKNOWN", strict)
        }
        val fallback = CcConstants.GLFW_TO_FCL.getValue("GLFW_KEY_UNKNOWN")
        substitutions?.add(
            substitution(
                ctx,
                CcJson.obj("type" to CcJson.str("key"), "key" to CcJson.str(key)),
                CcJson.obj("type" to CcJson.str("fcl_keycode"), "keycode" to CcJson.inum(fallback)),
                "No known FCL equivalent; substituted with UNKNOWN",
                "keys",
            )
        )
        return fallback
    }

    data class ZlEvent(val eventType: String, val key: String)

    fun convertKeyToZl(
        ctx: CcContext,
        keycode: Long,
        strict: Boolean,
        autoClick: Boolean = false,
        label: String = "",
        substitutions: MutableList<JsonObject>? = null,
    ): ZlEvent? {
        if (keycode == -1L && label.trim() == "*") {
            return ZlEvent("key", "GLFW_KEY_KP_MULTIPLY")
        }
        CcConstants.FCL_MOUSE_REVERSE[keycode]?.let { return ZlEvent("key", it) }
        CcConstants.FCL_SCROLL_REVERSE[keycode]?.let { pair ->
            val single = pair.first
            val long = pair.second
            return ZlEvent("launcher_event", if (autoClick) long else single)
        }
        CcConstants.FCL_TO_GLFW[keycode]?.let { return ZlEvent("key", it) }
        CcConstants.FCL_TO_ZL_FALLBACKS[keycode]?.let { outer ->
            val etype = outer.first.first
            val key = outer.first.second
            val reason = outer.second
            ctx.warn("FCL keycode $keycode has no exact ZL equivalent; $reason", strict)
            substitutions?.add(
                substitution(
                    ctx,
                    CcJson.obj("type" to CcJson.str("fcl_keycode"), "keycode" to CcJson.inum(keycode)),
                    CcJson.obj("type" to CcJson.str(etype), "key" to CcJson.str(key)),
                    reason,
                    "keys",
                )
            )
            return ZlEvent(etype, key)
        }
        val knownReason = CcConstants.UNSUPPORTED_FCL_KEY_REASONS[keycode]
        if (knownReason != null) {
            ctx.warn("FCL keycode $keycode has no ZL control event equivalent: $knownReason; substituted with GLFW_KEY_UNKNOWN", strict)
        } else {
            ctx.warn("unsupported FCL keycode $keycode; substituted with GLFW_KEY_UNKNOWN", strict)
        }
        substitutions?.add(
            substitution(
                ctx,
                CcJson.obj("type" to CcJson.str("fcl_keycode"), "keycode" to CcJson.inum(keycode)),
                CcJson.obj("type" to CcJson.str("key"), "key" to CcJson.str("GLFW_KEY_UNKNOWN")),
                "No known ZL equivalent; substituted with GLFW_KEY_UNKNOWN",
                "keys",
            )
        )
        return ZlEvent("key", "GLFW_KEY_UNKNOWN")
    }

    // --- wrap_content 估算 ---

    /** Python str.splitlines() 等价切分（含全部换行族分隔符）。 */
    private fun pythonSplitLines(text: String): List<String> {
        if (text.isEmpty()) return emptyList()
        val lines = mutableListOf<String>()
        val sb = StringBuilder()
        var i = 0
        val n = text.length
        while (i < n) {
            val c = text[i]
            val code = c.code
            when {
                c == '\r' -> {
                    lines.add(sb.toString())
                    sb.setLength(0)
                    if (i + 1 < n && text[i + 1] == '\n') i++
                }
                c == '\n' || code == 0x0B || code == 0x0C ||
                    code in 0x1C..0x1E || code == 0x85 || code == 0x2028 || code == 0x2029 -> {
                    lines.add(sb.toString())
                    sb.setLength(0)
                }
                else -> sb.append(c)
            }
            i++
        }
        lines.add(sb.toString())
        return lines
    }

    fun estimateWrapContentDp(widget: JsonObject, styleName: String?, fclStyles: List<JsonObject>): Pair<Long, Long> {
        val text = textDefault(widget.opt("text"))
        val style = fclStyles.firstOrNull { CcJson.toStringV(it.opt("name")) == styleName }
            ?: CcConstants.defaultZlFallbackFclStyle()
        val fontSize = Math.max(2, clampInt(style.opt("textSize"), 14))
        val lines = pythonSplitLines(text).ifEmpty { listOf("") }
        var longest = 0L
        for (line in lines) longest = Math.max(longest, line.length.toLong())
        val width = Math.max(5, Math.min(480, pyRound(longest * fontSize * 0.62 + 8.0)))
        val height = Math.max(5, Math.min(240, pyRound(lines.size.toLong() * fontSize * 1.25 + 6.0)))
        return Pair(width, height)
    }

    // --- 控件文本归一化（启发式推断用；坑 4：alnum 与 CJK 必须分开成 run） ---

    private fun isAlnumAscii(c: Char): Boolean = c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

    private fun isCjk(c: Char): Boolean = c.code in 0x4E00..0x9FFF

    fun normalizedControlText(text: String?): String {
        val sb = StringBuilder()
        for (c in text ?: "") {
            if (isAlnumAscii(c) || isCjk(c)) sb.append(c.lowercaseChar())
        }
        return sb.toString()
    }

    private fun controlTextRuns(text: String?): List<String> {
        val runs = mutableListOf<String>()
        val current = StringBuilder()
        var currentClass: Boolean? = null
        for (c in text ?: "") {
            val cls: Boolean? = when {
                isAlnumAscii(c) -> false
                isCjk(c) -> true
                else -> null
            }
            when {
                cls != null && currentClass == cls -> current.append(c.lowercaseChar())
                cls != null -> {
                    if (current.isNotEmpty()) {
                        runs.add(current.toString())
                        current.setLength(0)
                    }
                    currentClass = cls
                    current.append(c.lowercaseChar())
                }
                else -> {
                    if (current.isNotEmpty()) {
                        runs.add(current.toString())
                        current.setLength(0)
                    }
                    currentClass = null
                }
            }
        }
        if (current.isNotEmpty()) runs.add(current.toString())
        return runs
    }

    fun normalizedControlWords(text: String?): LinkedHashSet<String> {
        val words = LinkedHashSet<String>()
        for (raw in controlTextRuns(text)) {
            if (raw.length < 2) continue
            words.add(raw)
            if (raw.all { isCjk(it) }) {
                val chars = raw.toCharArray()
                val maxSize = Math.min(5, chars.size)
                for (size in 2..maxSize) {
                    for (start in 0..(chars.size - size)) {
                        words.add(String(chars, start, size))
                    }
                }
            }
        }
        return words
    }

    private val EVENT_KEY_SEPARATOR = 0.toChar()

    fun dedupeEvents(events: List<JsonObject>): List<JsonObject> {
        val result = mutableListOf<JsonObject>()
        val seen = HashSet<String>()
        for (event in events) {
            val eventType = toStringV(event.opt("type"))
            val key = toStringV(event.opt("key"))
            val k = eventType + EVENT_KEY_SEPARATOR + key
            if (seen.add(k)) result.add(event)
        }
        return result
    }

    private val SHAPE_KEYS = arrayOf("topStart", "topEnd", "bottomEnd", "bottomStart")

    /** Python `a or b or c` 字符串链：取第一个真值元素的 str，全假返回 null。 */
    fun firstTruthyStr(vararg values: JsonElement?): String? {
        for (v in values) {
            if (pyTruthy(v)) return toStringV(v)
        }
        return null
    }
}
