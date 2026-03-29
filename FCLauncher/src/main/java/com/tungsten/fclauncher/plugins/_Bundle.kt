package com.tungsten.fclauncher.plugins

import android.os.Bundle

fun Bundle.safeGetString(key: String): String? {
    return if (containsKey(key)) {
        runCatching { getString(key) }.getOrNull()
            ?: runCatching { getFloat(key).toString() }.getOrNull()
    } else null
}