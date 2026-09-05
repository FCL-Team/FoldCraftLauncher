package com.mio.controlconverter

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.UUID
import java.util.logging.Logger

/** strict 模式下告警升级为异常（对齐 cc.py --strict）。 */
class CcStrictException(message: String) : IllegalArgumentException(message)

/**
 * 转换上下文：警告去重、替换计数、ID 生成（替代 cc.py 的模块级全局状态）。
 * 每次转换新建一个实例。
 */
class CcContext {
    val warnedMessages = HashSet<String>()
    val substitutionCounts = linkedMapOf("keys" to 0L, "events" to 0L, "layers" to 0L, "directions" to 0L)

    /** 确定性 ID 模式（对齐 CC_DETERMINISTIC=1），仅用于对拍测试。 */
    var deterministic = false
    var deterministicCounter = 0L

    fun warn(message: String, strict: Boolean = false, once: Boolean = false) {
        if (strict) throw CcStrictException(message)
        if (once && !warnedMessages.add(message)) return
        LOGGER.warning("control-converter: $message")
    }

    fun bump(category: String) {
        if (substitutionCounts.containsKey(category)) substitutionCounts[category] = substitutionCounts[category]!! + 1
    }

    fun substitutionSummary(): String? {
        val total = substitutionCounts.values.sum()
        if (total == 0L) return null
        return "conversion substitutions: keys=${substitutionCounts["keys"]}, events=${substitutionCounts["events"]}, " +
            "layers=${substitutionCounts["layers"]}, directions=${substitutionCounts["directions"]}"
    }

    /** 32 位 hex；确定性模式下与 Go/Rust 计数器方案逐字节一致。 */
    fun newHex32(): String {
        if (deterministic) {
            deterministicCounter += 1
            return "%016x%s".format(deterministicCounter, "ab".repeat(8))
        }
        val uuid = UUID.randomUUID()
        return "%016x%016x".format(uuid.mostSignificantBits, uuid.leastSignificantBits)
    }

    fun shortId(): String = newHex32().take(12)

    /** str(uuid.UUID(hex))：保留原始 128 位、不改写 version/variant 位（AGENTS.md 坑 11）。 */
    fun fclId(): String {
        val h = newHex32()
        return "${h.substring(0, 8)}-${h.substring(8, 12)}-${h.substring(12, 16)}-${h.substring(16, 20)}-${h.substring(20, 32)}"
    }

    companion object {
        private val LOGGER = Logger.getLogger("ControlConverter")
    }
}

// --- 元数据（lossless 往返）辅助 ---

fun getMeta(obj: JsonObject?): JsonObject? = obj.opt(CcConstants.META_KEY).asObjOrNull()

fun setMeta(target: JsonObject, meta: JsonObject?): JsonObject {
    if (meta != null) target.add(CcConstants.META_KEY, meta)
    return target
}

fun makeMeta(
    originFormat: String,
    originKind: String,
    originId: String?,
    original: JsonElement,
    mapping: JsonObject? = null,
): JsonObject {
    val meta = CcJson.obj(
        "schema" to CcJson.inum(CcConstants.META_SCHEMA_VERSION.toLong()),
        "originFormat" to CcJson.str(originFormat),
        "originKind" to CcJson.str(originKind),
        "originId" to CcJson.str(originId ?: ""),
        "original" to stripConverterMeta(original),
    )
    if (mapping != null) meta.add("mapping", mapping.deepCopy())
    return meta
}

fun metaOriginal(value: JsonObject, expectedFormat: String, expectedKind: String? = null): JsonObject? {
    val meta = getMeta(value) ?: return null
    if (CcJson.toStringV(meta.opt("originFormat")) != expectedFormat) return null
    if (expectedKind != null && CcJson.toStringV(meta.opt("originKind")) != expectedKind) return null
    val original = meta.opt("original").asObjOrNull() ?: return null
    return original.deepCopy()
}

fun metaKind(value: JsonObject): String {
    val kind = getMeta(value)?.opt("originKind") ?: return ""
    return CcJson.toStringV(kind)
}

fun substitution(
    ctx: CcContext,
    source: JsonElement,
    target: JsonElement,
    reason: String,
    category: String = "events",
): JsonObject {
    ctx.bump(category)
    return CcJson.obj(
        "source" to source.deepCopy(),
        "target" to target.deepCopy(),
        "reason" to CcJson.str(reason),
    )
}

fun appendSubstitutions(mapping: JsonObject?, substitutions: List<JsonObject>): JsonObject? {
    if (substitutions.isEmpty()) return mapping?.deepCopy()
    val result = (mapping?.deepCopy() ?: JsonObject())
    val merged = JsonArray()
    for (e in result.optArr("substitutions") ?: JsonArray()) merged.add(e.deepCopy())
    for (s in substitutions) merged.add(s.deepCopy())
    result.add("substitutions", merged)
    return result
}

fun stripConverterMeta(value: JsonElement): JsonElement = when {
    value.isJsonObject -> {
        val out = JsonObject()
        for ((k, v) in value.asJsonObject.entrySet()) {
            if (k == CcConstants.META_KEY) continue
            out.add(k, stripConverterMeta(v))
        }
        out
    }
    value.isJsonArray -> {
        val out = JsonArray()
        for (v in value.asJsonArray) out.add(stripConverterMeta(v))
        out
    }
    else -> value.deepCopy()
}
