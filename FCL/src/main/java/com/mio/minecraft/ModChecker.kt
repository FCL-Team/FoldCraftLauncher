package com.mio.minecraft

import android.content.Context
import com.mio.util.AndroidUtil
import com.tungsten.fcl.R
import com.tungsten.fclcore.mod.LocalModFile
import kotlin.jvm.Throws

class ModChecker(val context: Context) {
    val checkMap = mutableMapOf<String, (LocalModFile) -> Unit>()

    init {
        checkMap.put("physicsmod") { mod ->
            val arch = AndroidUtil.getElfArchFromZip(
                mod.file.toFile(),
                "de/fabmax/physxjni/linux/libPhysXJniBindings_64.so"
            )
            if (arch.contains("x86"))
                throw ModCheckException(
                    context.getString(
                        R.string.mod_check_physics,
                        mod.file.toFile().name
                    )
                )
        }
    }

    @Throws(ModCheckException::class)
    fun check(mod: LocalModFile) {
        if (checkMap.containsKey(mod.id)) {
            val exception = runCatching {
                checkMap[mod.id]?.invoke(mod)
            }.exceptionOrNull()
            if (exception != null) {
                throw exception
            }
        }
    }
}

class ModCheckException(val reason: String) : Exception(reason)