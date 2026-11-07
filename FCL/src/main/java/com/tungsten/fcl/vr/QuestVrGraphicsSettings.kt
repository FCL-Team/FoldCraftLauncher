package com.tungsten.fcl.vr

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.IOException
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.math.roundToInt

/**
 * Vivecraft 实例侧图形设置（config/vivecraft-client-config.json 的只读视图），
 * 供版本设置页在用户未显式覆写时展示实例当前值。
 */
class QuestVrGraphicsSettings private constructor(
    /** 渲染缩放百分比（Vivecraft 的 renderScaleFactor × 100） */
    val renderScalePercent: Int,
    val isFsaaEnabled: Boolean,
) {
    companion object {
        const val MIN_RENDER_SCALE_PERCENT = 10
        const val MAX_RENDER_SCALE_PERCENT = 900
        private const val DEFAULT_RENDER_SCALE_PERCENT = 80
        private const val DEFAULT_FSAA_ENABLED = false

        fun defaults() = QuestVrGraphicsSettings(DEFAULT_RENDER_SCALE_PERCENT, DEFAULT_FSAA_ENABLED)

        /** 读取实例当前的 Vivecraft 图形设置；缺项或越界回落默认值 */
        @Throws(IOException::class)
        fun read(gameDirectory: Path): QuestVrGraphicsSettings {
            val config = readObject(gameDirectory.resolve(VIVECRAFT_CONFIG))
            val percent = config.get("renderScaleFactor")
                ?.takeIf(::isNumberOrString)
                ?.let { element -> runCatching { element.asDouble }.getOrNull() }
                ?.let { factor ->
                    if (factor.isFinite() && factor >= MIN_RENDER_SCALE_PERCENT / 100.0
                        && factor <= MAX_RENDER_SCALE_PERCENT / 100.0
                    ) (factor * 100.0).roundToInt() else null
                }
                ?: DEFAULT_RENDER_SCALE_PERCENT
            return QuestVrGraphicsSettings(percent, parseFsaaEnabled(config.get("useFsaa")) ?: DEFAULT_FSAA_ENABLED)
        }

        /** 越界值归一为 null（视为未设置） */
        fun normalizeRenderScalePercent(value: Int?): Int? =
            value?.takeIf { it in MIN_RENDER_SCALE_PERCENT..MAX_RENDER_SCALE_PERCENT }

        fun parseRenderScalePercent(value: JsonElement?): Int? {
            val text = value?.takeIf(::isNumberOrString)?.asString ?: return null
            return runCatching { normalizeRenderScalePercent(BigDecimal(text).intValueExact()) }.getOrNull()
        }

        /** 兼容 Vivecraft 以字符串存布尔的写法（"true"/"false"，大小写不敏感） */
        fun parseFsaaEnabled(value: JsonElement?): Boolean? {
            val primitive = value?.takeIf { it.isJsonPrimitive }?.asJsonPrimitive ?: return null
            return when {
                primitive.isBoolean -> primitive.asBoolean
                primitive.isString -> primitive.asString.lowercase().toBooleanStrictOrNull()
                else -> null
            }
        }

        private fun isNumberOrString(value: JsonElement): Boolean =
            value.isJsonPrimitive && (value.asJsonPrimitive.isNumber || value.asJsonPrimitive.isString)

        /** 读取 JSON 对象文件；缺失视为空对象，格式错误包装为 IOException */
        @Throws(IOException::class)
        internal fun readObject(file: Path): JsonObject {
            if (!Files.exists(file)) return JsonObject()
            try {
                val parsed = JsonParser.parseString(file.readText())
                require(parsed.isJsonObject) { "Expected a JSON object" }
                return parsed.asJsonObject
            } catch (failure: RuntimeException) {
                throw IOException("Cannot read Vivecraft configuration: $file", failure)
            }
        }

        internal const val VIVECRAFT_CONFIG = "config/vivecraft-client-config.json"
    }
}
