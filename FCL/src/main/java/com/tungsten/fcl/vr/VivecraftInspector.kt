package com.tungsten.fcl.vr

import com.tungsten.fclcore.util.io.CompressingUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.Locale

/** 检查 mods 目录中 Vivecraft 的存在与兼容性（须携带 Android OpenXR 加载器的构建） */
object VivecraftInspector {
    /** Android 版 Vivecraft 独有的类，作为兼容判据 */
    const val ANDROID_LOADER_CLASS = "org/vivecraft/util/VLoader.class"
    private const val VIVECRAFT_PACKAGE = "org/vivecraft"

    enum class Status {
        COMPATIBLE,
        MISSING,
        INCOMPATIBLE,
        CONFLICT,
        FAILED
    }

    @JvmStatic
    fun inspect(modsDirectory: File?): Status {
        return try {
            if (modsDirectory == null || !modsDirectory.exists()) return Status.MISSING
            if (!modsDirectory.isDirectory) return Status.FAILED
            val files = modsDirectory.listFiles() ?: return Status.FAILED
            inspectFiles(files.toList())
        } catch (e: SecurityException) {
            Status.FAILED
        }
    }

    @JvmStatic
    fun inspectFiles(files: Collection<File>?): Status {
        if (files == null) return Status.FAILED
        var vivecraftJars = 0
        var compatible = false
        var unreadable = false
        for (file in files) {
            if (!file.name.lowercase(Locale.ROOT).endsWith(".jar")) continue
            try {
                CompressingUtils.createReadOnlyZipFileSystem(file.toPath()).use { fs ->
                    val androidLoader = Files.exists(fs.getPath(ANDROID_LOADER_CLASS))
                    // 仅遍历 org/vivecraft 子树，避免对每个非 Vivecraft 的 jar 全量遍历条目
                    val packageRoot = fs.getPath(VIVECRAFT_PACKAGE)
                    val vivecraft = androidLoader || Files.isDirectory(packageRoot) &&
                            Files.walk(packageRoot).use { entries ->
                                entries.anyMatch { it.toString().endsWith(".class") }
                            }
                    if (vivecraft) {
                        vivecraftJars++
                        compatible = compatible || androidLoader
                    }
                }
            } catch (failure: IOException) {
                unreadable = true
            }
        }
        return when {
            vivecraftJars > 1 -> Status.CONFLICT
            compatible -> Status.COMPATIBLE
            vivecraftJars == 1 -> Status.INCOMPATIBLE
            unreadable -> Status.FAILED
            else -> Status.MISSING
        }
    }
}
