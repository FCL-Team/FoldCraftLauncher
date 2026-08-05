package com.tungsten.fcl.ui.glass.page.download

import com.tungsten.fclcore.mod.RemoteMod

object RemoteModCache {
    private val cache = mutableMapOf<String, RemoteMod>()

    fun put(key: String, mod: RemoteMod) {
        cache[key] = mod
    }

    fun get(key: String): RemoteMod? {
        return cache[key]
    }

    fun clear() {
        cache.clear()
    }
}
