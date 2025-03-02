package com.mio.minecraft

import android.content.Context
import com.mio.util.AndroidUtil
import com.tungsten.fcl.R
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclcore.mod.LocalModFile
import kotlin.jvm.Throws

class ModChecker(val context: Context) {
    val checkMap = mutableMapOf<String, (LocalModFile) -> Unit>()

    init {
        add("physicsmod") { mod ->
            val arch = AndroidUtil.getElfArchFromZip(
                mod.file.toFile(),
                "de/fabmax/physxjni/linux/libPhysXJniBindings_64.so"
            )
            if (arch.isBlank() or (!Architecture.isx86Device() and arch.contains("x86")))
                throw ModCheckException(
                    context.getString(
                        R.string.mod_check_physics,
                        mod.file.toFile().name
                    )
                )
        }
        add("mcef") {
            throw ModCheckException(
                context.getString(
                    R.string.mod_check_mcef,
                    it.file.toFile().name
                )
            )
        }
        add("valkyrienskies") {
            throw ModCheckException(
                context.getString(
                    R.string.mod_check_valkyrienskies,
                    it.file.toFile().name
                )
            )
        }
        add("yes_steve_model") { mod ->
            val arch = AndroidUtil.getElfArchFromZip(
                mod.file.toFile(),
                "META-INF/native/libysm-core.so"
            )
            if (arch.isNotBlank())
                throw ModCheckException(
                    context.getString(
                        R.string.mod_check_yes_steve_model,
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

    private fun add(modID: String, action: (LocalModFile) -> Unit) {
        checkMap.put(modID, action)
    }
}

class ModCheckException(val reason: String) : Exception(reason)