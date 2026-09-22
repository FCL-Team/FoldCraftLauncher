package com.tungsten.fcl.control.data

import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * 控件数据反序列化的空安全字段读取。
 * Gson 的 [JsonObject.get] 对显式 null 值返回 [com.google.gson.JsonNull] 而非 Java null，
 * 直接 getAsXxx 会抛 UnsupportedOperationException；此处将键缺失与显式 null 统一归一为 Java null。
 */
object JsonElements {

    @JvmStatic
    fun get(obj: JsonObject, key: String): JsonElement? =
        obj.get(key)?.takeUnless { it.isJsonNull }
}
