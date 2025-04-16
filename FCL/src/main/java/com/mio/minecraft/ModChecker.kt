package com.mio.minecraft

import android.content.Context
import com.mio.util.AndroidUtil
import com.tungsten.fcl.R
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.plugins.FFmpegPlugin
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclcore.mod.LocalModFile
import kotlin.jvm.Throws

class ModChecker(val context: Context) {
    @Throws(ModCheckException::class)
    fun check(bridge: FCLBridge, mod: LocalModFile) {
        val exception = runCatching {
            when (mod.id) {
                "touchcontroller" -> {
                    bridge.setHasTouchController(true);
                }
                "physicsmod" -> {
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

                "mcef" -> {
                    throw ModCheckException(
                        context.getString(
                            R.string.mod_check_mcef,
                            mod.file.toFile().name
                        )
                    )
                }

                "valkyrienskies" -> {
                    throw ModCheckException(
                        context.getString(
                            R.string.mod_check_valkyrienskies,
                            mod.file.toFile().name
                        )
                    )
                }

                "yes_steve_model" -> {
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

                "imblocker", "ingameime" -> {
                    throw ModCheckException(
                        context.getString(
                            R.string.mod_check_imblocker,
                            mod.file.toFile().name
                        )
                    )
                }

                "replaymod" -> {
                    FFmpegPlugin.discover(context)
                    if (!FFmpegPlugin.isAvailable) {
                        throw ModCheckException(
                            context.getString(
                                R.string.mod_check_replay,
                                mod.file.toFile().name,
                                "https://github.com/FCL-Team/FoldCraftLauncher/releases/download/ffmpeg/Pojav.FFmpeg.Plugin.1.1.APK",
                                "https://pan.quark.cn/s/6201574edb62"
                            )
                        )
                    }
                }

                "borderlesswindow" -> {
                    throw ModCheckException(
                        context.getString(
                            R.string.mod_check_borderlesswindow,
                            mod.file.toFile().name
                        )
                    )
                }
            }
        }.exceptionOrNull()
        if (exception != null) {
            throw exception
        }
    }
}

class ModCheckException(val reason: String) : Exception(reason)