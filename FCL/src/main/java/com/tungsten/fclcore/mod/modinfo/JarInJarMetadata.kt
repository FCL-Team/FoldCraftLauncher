package com.tungsten.fclcore.mod.modinfo

import com.google.gson.JsonParseException
import com.tungsten.fclcore.util.gson.Validation

data class JarInJarMetadata(var jars: MutableList<EmbeddedJarMetadata>? = null) : Validation {
    @Throws(JsonParseException::class)
    override fun validate() {
        Validation.requireNonNull(jars, "jars")
        for (jar in jars!!) {
            jar.validate()
        }
    }
}
