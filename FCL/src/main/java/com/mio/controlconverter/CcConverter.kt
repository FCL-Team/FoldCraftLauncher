package com.mio.controlconverter

import com.google.gson.JsonObject

/**
 * control-converter Kotlin 版入口（语义基准 cc.py）。
 *
 * FCL -> ZL2 默认参数与原 Go JNI 一致：
 * includeDirections=false, strict=false, aspect=16/9, lossless=true, absoluteAsPercentage=false。
 */
object CcConverter {

    const val DEFAULT_ASPECT = 16.0 / 9.0

    /** cc.py detect_format。 */
    fun detectFormat(data: JsonObject): String = when {
        data.hasKey("layers") && data.hasKey("editorVersion") -> "zl"
        data.hasKey("viewGroups") && data.hasKey("controllerVersion") -> "fcl"
        else -> throw IllegalArgumentException("cannot detect input format; use zl2fcl or fcl2zl explicitly")
    }

    /** FCL 控制布局 -> ZL2 控制布局。返回结果根对象（内嵌 lossless 元数据）。 */
    fun convertFclToZl(
        data: JsonObject,
        strict: Boolean = false,
        includeDirections: Boolean = false,
        aspect: Double = DEFAULT_ASPECT,
        lossless: Boolean = true,
        absoluteAsPercentage: Boolean = false,
        stripMeta: Boolean = false,
        deterministic: Boolean = false,
    ): JsonObject {
        val ctx = CcContext()
        ctx.deterministic = deterministic
        val result = CcFclToZl.convertFclToZl(
            ctx, data, includeDirections, strict, aspect, lossless, absoluteAsPercentage,
        )
        return if (stripMeta) stripConverterMeta(result).asJsonObject else result
    }

    /** ZL2 控制布局 -> FCL 控制布局。 */
    fun convertZlToFcl(
        data: JsonObject,
        strict: Boolean = false,
        stripMeta: Boolean = false,
        deterministic: Boolean = false,
    ): JsonObject {
        val ctx = CcContext()
        ctx.deterministic = deterministic
        val result = CcZlToFcl.zlToFcl(ctx, data, strict)
        return if (stripMeta) stripConverterMeta(result).asJsonObject else result
    }

    /** 自动检测方向并转换。 */
    fun convertAuto(
        data: JsonObject,
        strict: Boolean = false,
        stripMeta: Boolean = false,
    ): JsonObject = when (detectFormat(data)) {
        "zl" -> convertZlToFcl(data, strict, stripMeta)
        else -> convertFclToZl(data, strict = strict, stripMeta = stripMeta)
    }

    /** 输入文本是否为 ZL2 布局（用于导入自动识别）。 */
    fun isZl2Layout(jsonText: String): Boolean = runCatching {
        detectFormat(CcJson.loadJson(jsonText)) == "zl"
    }.getOrDefault(false)

    /** 输入文本是否为 FCL 控件布局。 */
    fun isFclLayout(jsonText: String): Boolean = runCatching {
        detectFormat(CcJson.loadJson(jsonText)) == "fcl"
    }.getOrDefault(false)
}
