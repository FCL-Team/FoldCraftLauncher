package com.tungsten.fclcore.mod.modinfo

import kotlinx.serialization.json.Json

/** 元数据解析统一配置：忽略未知键，非法值归并为默认值（缺失字段用构造器默认值兜底） */
val MOD_METADATA_JSON: Json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}
