package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import java.math.BigDecimal

/** 构造 JsonArray。 */
fun carr(vararg items: JsonElement): JsonArray {
    val a = JsonArray()
    for (item in items) a.add(item)
    return a
}

/**
 * control-converter 的 JSON 树工具（Gson JsonObject 保序树）。
 *
 * cc.py 基于 Python dict（插入序保序）；Gson 的 JsonObject 内部为 LinkedTreeMap，
 * add 方法对已存在键原位覆盖、新键追加尾部，与 Python dict 语义一致。
 * 解析时数字以原始字面量保留（如 "50.0"），写出时不转义 HTML（对齐 cc.py json.dumps）。
 */
object CcJson {

    /** 剥离行注释与块注释（状态机移植自 cc.py strip_json_comments）。 */
    fun stripJsonComments(text: String): String {
        val result = StringBuilder(text.length)
        var inString = false
        var escape = false
        var index = 0
        while (index < text.length) {
            val ch = text[index]
            val nextCh = if (index + 1 < text.length) text[index + 1] else ' '
            if (inString) {
                result.append(ch)
                when {
                    escape -> escape = false
                    ch == '\\' -> escape = true
                    ch == '"' -> inString = false
                }
                index++
            } else if (ch == '"') {
                inString = true
                result.append(ch)
                index++
            } else if (ch == '/' && nextCh == '/') {
                index += 2
                while (index < text.length && text[index] != '\r' && text[index] != '\n') index++
            } else if (ch == '/' && nextCh == '*') {
                index += 2
                while (index + 1 < text.length && !(text[index] == '*' && text[index + 1] == '/')) index++
                index += 2
            } else {
                result.append(ch)
                index++
            }
        }
        return result.toString()
    }

    /** 解析 JSON；失败时尝试剥离注释后重试（对齐 jsonio.rs load_json_bytes）。根须为对象。 */
    fun loadJson(text: String): JsonObject {
        val clean = runCatching { parse(text) }
            .recoverCatching { parse(stripJsonComments(text)) }
            .getOrThrow()
        if (!clean.isJsonObject) error("expected JSON object at root")
        return clean.asJsonObject
    }

    fun parse(text: String): JsonElement = JsonParser.parseString(text)

    /** pretty（2 空格缩进）加末尾换行；不转义 HTML（对齐 cc.py json.dump indent=2）。 */
    fun encodePretty(value: JsonElement): String {
        val gson = com.google.gson.GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        return gson.toJson(value) + "\n"
    }

    // --- 构造辅助 ---

    fun obj(vararg pairs: Pair<String, JsonElement>): JsonObject {
        val o = JsonObject()
        for ((k, v) in pairs) o.add(k, v)
        return o
    }

    fun str(s: String): JsonPrimitive = JsonPrimitive(s)
    fun bool(b: Boolean): JsonPrimitive = JsonPrimitive(b)
    fun inum(i: Long): JsonPrimitive = JsonPrimitive(i)
    fun inum(i: Int): JsonPrimitive = JsonPrimitive(i.toLong())

    /** 以 Python repr 精确字面量承载浮点数（如 50.0、1e-07）。 */
    fun pyNum(d: Double): JsonPrimitive = JsonPrimitive(PyLiteralNumber(CcUtils.pyFloatFormat(d)))

    /** Python int：float 经银行家舍入。 */
    fun pyInt(d: Double): JsonPrimitive = JsonPrimitive(CcUtils.pyRound(d))

    class PyLiteralNumber(private val literal: String) : Number() {
        private val value: Double by lazy { literal.toDoubleOrNull() ?: 0.0 }
        @Deprecated("Unclear conversion", ReplaceWith("toInt()"), DeprecationLevel.WARNING)
        override fun toByte(): Byte = value.toInt().toByte()
        override fun toDouble(): Double = value
        override fun toFloat(): Float = value.toFloat()
        override fun toInt(): Int = value.toInt()
        override fun toLong(): Long = value.toLong()
        @Deprecated("Unclear conversion", ReplaceWith("toInt()"), DeprecationLevel.WARNING)
        override fun toShort(): Short = value.toInt().toShort()
        override fun toString(): String = literal
    }

    // --- Python None 语义的访问辅助（顶层扩展，包内可见） ---

    /** Python str()（委托 CcUtils，便于统一调用点）。 */
    fun toStringV(value: JsonElement?): String = CcUtils.toStringV(value)

    fun deepCopy(value: JsonElement): JsonElement = value.deepCopy()

    /**
     * Python 比较语义的 JSON 深比较：对象按键集合与值（与键序无关），
     * 数组按元素序，数字跨 int 与 float 按数值比较，true 等于 1。
     */
    fun jsonEquals(a: JsonElement?, b: JsonElement?): Boolean {
        if (a == null || b == null) return a == null && b == null
        if (a.isJsonPrimitive && b.isJsonPrimitive) {
            val pa = a.asJsonPrimitive
            val pb = b.asJsonPrimitive
            val aNum = pa.isNumber
            val bNum = pb.isNumber
            if (aNum && bNum) return compareBigDecimal(pa.asString, pb.asString)
            if (aNum || bNum) {
                val num = if (aNum) pa else pb
                val other = if (aNum) pb else pa
                if (other.isBoolean) {
                    return compareBigDecimal(num.asString, if (other.asBoolean) "1" else "0")
                }
                return false
            }
            return pa.asString == pb.asString
        }
        if (a.isJsonObject && b.isJsonObject) {
            val oa = a.asJsonObject
            val ob = b.asJsonObject
            if (oa.size() != ob.size()) return false
            for ((k, v) in oa.entrySet()) {
                if (!ob.has(k)) return false
                if (!jsonEquals(v, ob.get(k))) return false
            }
            return true
        }
        if (a.isJsonArray && b.isJsonArray) {
            val aa = a.asJsonArray
            val ab = b.asJsonArray
            if (aa.size() != ab.size()) return false
            for (i in 0 until aa.size()) {
                if (!jsonEquals(aa[i], ab[i])) return false
            }
            return true
        }
        if (a.isJsonNull && b.isJsonNull) return true
        return false
    }

    private fun compareBigDecimal(x: String, y: String): Boolean = try {
        BigDecimal(x).compareTo(BigDecimal(y)) == 0
    } catch (e: NumberFormatException) {
        x == y
    }
}

// --- Python None 语义的访问辅助（顶层扩展，包内自动可见） ---

fun JsonElement?.asObjOrNull(): JsonObject? = if (this != null && isJsonObject) asJsonObject else null
fun JsonElement?.asArrOrNull(): JsonArray? = if (this != null && isJsonArray) asJsonArray else null

/** 等价于 Python obj.get(key)：缺失或 JSON null 返回 Kotlin null。 */
fun JsonObject?.opt(key: String): JsonElement? {
    if (this == null || !has(key)) return null
    val v = get(key)
    return if (v.isJsonNull) null else v
}

fun JsonObject?.optObj(key: String): JsonObject? = opt(key).asObjOrNull()
fun JsonObject?.optArr(key: String): JsonArray? = opt(key).asArrOrNull()

/** 等价于 Python `key in obj`（含值为 JSON null 的键）。 */
fun JsonObject?.hasKey(key: String): Boolean = this != null && has(key)

/** 数组迭代，非数组返回空列表。 */
fun JsonElement?.iterArr(): List<JsonElement> = asArrOrNull()?.toList() ?: emptyList()

/** 等价于 Python dict.update：已存在键原位覆盖、新键追加尾部。 */
fun JsonObject.putAll(other: JsonObject) {
    for ((k, v) in other.entrySet()) add(k, v)
}
