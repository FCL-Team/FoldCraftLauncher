package com.tungsten.fclcore.mod.modinfo

import com.google.gson.JsonParseException
import com.tungsten.fclcore.util.gson.Validation

data class EmbeddedJarMetadata(
    var path: String? = null,
    var isObfuscated: Boolean = false
) : Validation {
    @Throws(JsonParseException::class)
    override fun validate() {
        Validation.requireNonNull(path, "path")
    }
}
