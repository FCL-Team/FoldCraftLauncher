package com.tungsten.fcl.vr

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.mio.data.Renderer
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.utils.Architecture
import com.tungsten.fclcore.util.platform.CommandBuilder
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.exists

/** Quest VR 运行时装配：LWJGL 绑定 jar 安装、JVM 参数、渲染环境与 Vivecraft 实例配置 */
object QuestVrRuntime {
    /** 常规 LWJGL 目录缺少的 OpenXR / OpenGL ES 模块，VR 会话追加到 classpath */
    private val JARS = arrayOf("lwjgl-openxr.jar", "lwjgl-opengles.jar")

    /** VR 固定使用的 LWJGL 版本，须与上述模块及 liblwjgl_opengles.so 一致 */
    const val LWJGL_VERSION = "3.3.3"

    private const val OPENXR_LOADER = "libopenxr_loader.so"
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    /** Vivecraft 配置中由启动器托管的键（关闭 VR 时按备份还原） */
    private val OWNED_KEYS = arrayOf(
        "stereoProviderPluginID", "vrEnabled", "rememberVr", "displayMirrorMode", "vrHotswitchingEnabled",
        "closeWithRuntime"
    )

    /** 当前设备是否具备 Quest VR 条件：arm64 + 头部追踪（Meta 设备按厂商/型号兜底）+ OpenXR loader 就位 */
    @JvmStatic
    fun isSupported(context: Context): Boolean {
        // 进程内设备能力不变，首判结果缓存并留排查日志
        support?.let { return it }
        val result = checkSupported(context)
        android.util.Log.i("FCL-QuestVR", "Device VR support: $result " +
                "(manufacturer=${Build.MANUFACTURER}, model=${Build.MODEL}, " +
                "abi=${Build.SUPPORTED_ABIS.firstOrNull()}, loader=${OPENXR_LOADER})")
        support = result
        return result
    }

    @Volatile
    private var support: Boolean? = null

    private fun checkSupported(context: Context): Boolean {
        if (!Build.SUPPORTED_ABIS.contains("arm64-v8a")) return false
        if (!File(context.applicationInfo.nativeLibraryDir, OPENXR_LOADER).isFile) return false
        // Horizon OS 部分版本不向普通应用报告标准头部追踪特性，Meta 设备按厂商/型号兜底
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_VR_HEADTRACKING)) return true
        return Build.MANUFACTURER.equals("Meta", ignoreCase = true) ||
                Build.MODEL.contains("Quest", ignoreCase = true)
    }

    /** 将内置的 LWJGL 模块 jar 解包到私有目录（哈希不变则跳过） */
    @JvmStatic
    @Throws(IOException::class)
    fun install(context: Context) {
        val directory = context.getDir("quest_vr", Context.MODE_PRIVATE)
        for (name in JARS) {
            val destination = File(directory, name)
            val expected = context.assets.open("quest_vr/$name").use { digest(it) }
            if (destination.isFile) {
                destination.inputStream().use { existing ->
                    if (Arrays.equals(expected, digest(existing))) continue
                }
            }
            val temporary = File(directory, "$name.installing")
            context.assets.open("quest_vr/$name").use { source ->
                Files.copy(source, temporary.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            Files.move(temporary.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    /** VR 会话的 LWJGL 绑定 jar 绝对路径，供 classpath 最前置 */
    @JvmStatic
    fun getClasspath(context: Context): List<String> {
        val directory = context.getDir("quest_vr", Context.MODE_PRIVATE)
        return JARS.map { File(directory, it).absolutePath }
    }

    /** 写入/还原 Vivecraft 实例配置，并落盘渲染缩放与 FSAA 覆写 */
    @JvmStatic
    @Synchronized
    @Throws(IOException::class)
    fun configureInstance(
        gameDirectory: Path, enabled: Boolean,
        renderScalePercent: Int?, fsaaEnabled: Boolean?,
    ) {
        val scale = QuestVrGraphicsSettings.normalizeRenderScalePercent(renderScalePercent)
        val configuration = gameDirectory / QuestVrGraphicsSettings.VIVECRAFT_CONFIG
        val stateFile = gameDirectory / "config/.fcl-quest-vr-state.json"
        if (!enabled && !stateFile.exists()) {
            if (VivecraftInspector.inspect((gameDirectory / "mods").toFile()) == VivecraftInspector.Status.COMPATIBLE) {
                val config = QuestVrGraphicsSettings.readObject(configuration)
                config.addProperty("vrEnabled", "false")
                config.addProperty("vrHotswitchingEnabled", "false")
                writeObject(configuration, config)
            }
            return
        }
        val config = QuestVrGraphicsSettings.readObject(configuration)
        if (enabled) {
            // 首次接管前备份用户原值，关闭 VR 时按键还原
            val saved = QuestVrGraphicsSettings.readObject(stateFile)
            for (key in OWNED_KEYS) {
                if (!saved.has(key)) {
                    val previous = JsonObject()
                    previous.addProperty("present", config.has(key))
                    if (config.has(key)) previous.add("value", config.get(key).deepCopy())
                    saved.add(key, previous)
                }
            }
            writeObject(stateFile, saved)
            config.addProperty("stereoProviderPluginID", "OPENXR")
            if (!config.has("vrEnabled")) config.addProperty("vrEnabled", "true")
            config.addProperty("rememberVr", "true")
            config.addProperty("displayMirrorMode", "OFF")
            config.addProperty("vrHotswitchingEnabled", "false")
            config.addProperty("closeWithRuntime", "false")
            if (scale != null) {
                config.addProperty("renderScaleFactor", (scale / 100.0).toString())
            } else if (!config.has("renderScaleFactor")) {
                config.addProperty("renderScaleFactor", "0.8")
            }
            if (fsaaEnabled != null) config.addProperty("useFsaa", fsaaEnabled.toString())
        } else {
            val saved = QuestVrGraphicsSettings.readObject(stateFile)
            for (key in OWNED_KEYS) {
                if (!saved.has(key) || !saved.get(key).isJsonObject) {
                    if (key == "closeWithRuntime" && !saved.has(key)) continue
                    throw IOException("Invalid Quest VR configuration backup: $stateFile")
                }
                val previous = saved.getAsJsonObject(key)
                if (previous.get("present").asBoolean) {
                    config.add(key, previous.get("value").deepCopy())
                } else {
                    config.remove(key)
                }
            }
            config.addProperty("vrEnabled", "false")
            config.addProperty("vrHotswitchingEnabled", "false")
        }
        writeObject(configuration, config)
        if (!enabled) stateFile.deleteIfExists()
    }

    /** 覆写 LWJGL 加载路径与 GLFW 桩参数（VR 固定 1280×720 虚拟窗口） */
    @JvmStatic
    fun addJvmArguments(command: CommandBuilder, context: Context, renderer: Renderer) {
        val nativeDirectory = context.applicationInfo.nativeLibraryDir
        val lwjglDirectory = File(
            context.getDir("runtime", Context.MODE_PRIVATE),
            "lwjgl/$LWJGL_VERSION/natives/${Architecture.archAsStringAndroid(Architecture.getDeviceArchitecture())}"
        ).absolutePath
        replaceProperty(command, "org.lwjgl.opengl.libname", "$nativeDirectory/${renderer.glName}")
        replaceProperty(command, "org.lwjgl.opengles.libname", "/system/lib64/libGLESv3.so")
        replaceProperty(command, "org.lwjgl.egl.libname", "/system/lib64/libEGL.so")
        replaceProperty(command, "org.lwjgl.openxr.libname", "$nativeDirectory/libquest_openxr.so")
        replaceProperty(command, "org.lwjgl.librarypath", "$nativeDirectory:$lwjglDirectory")
        replaceProperty(command, "glfwstub.windowWidth", "1280")
        replaceProperty(command, "glfwstub.windowHeight", "720")
        replaceProperty(command, "fcl.quest.vr", "true")
        replaceProperty(command, "org.lwjgl.util.DebugLoader", "true")
    }

    /** 渲染器/驱动环境短路：VR 只接受 MobileGlues VR 渲染器 */
    @JvmStatic
    fun applyEnvironment(config: FCLConfig, environment: MutableMap<String, String>) {
        if (!config.isVrEnabled) {
            environment.remove("FCL_VR")
            return
        }
        val id = config.renderer.id
        require(id == Renderer.ID_QUEST_MOBILEGLUES) { "Unsupported Quest VR renderer: $id" }
        val nativeDirectory = config.context.applicationInfo.nativeLibraryDir
        environment["FCL_VR"] = "1"
        environment["FCL_VR_TRANSITION_ANIMATION"] =
            if (QuestVrPreferences.isTransitionAnimationEnabled(config.context)) "1" else "0"
        environment["FCL_VR_RENDERER"] = id
        environment["POJAV_RENDERER"] = id
        environment["POJAVEXEC_EGL"] = "$nativeDirectory/libmobileglues.so"
        environment.remove("SFPEW_LAYER")
        environment.remove("SFPEW_EGL")
        environment.remove("SFPEW_RELAXED_CONTEXT")
        environment["LIBGL_ES"] = "3"
        environment["LIBGL_GLES"] = "libGLESv3.so"
        environment["FORCE_VSYNC"] = "false"
        environment["FCL_VR_NATIVE_DIR"] = nativeDirectory
        environment["MG_DIR_PATH"] = File(config.context.filesDir, "quest-vr/mobileglues").absolutePath
        environment.remove("VULKAN_DRIVER_SYSTEM")
        environment.remove("VKSHIM_ENABLE")
        environment.remove("DRIVER_PATH")
        environment.remove("DRIVER_LIB")
        environment.remove("DRIVER_GPU")
    }

    private fun replaceProperty(command: CommandBuilder, key: String, value: String) {
        val prefix = "-D$key="
        command.removeIf { it.startsWith(prefix) }
        command.add(prefix + value)
    }

    @Throws(IOException::class)
    private fun digest(stream: InputStream): ByteArray {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(16384)
            while (true) {
                val count = stream.read(buffer)
                if (count == -1) break
                digest.update(buffer, 0, count)
            }
            digest.digest()
        } catch (impossible: NoSuchAlgorithmException) {
            throw IOException("SHA-256 is unavailable", impossible)
        }
    }

    @Throws(IOException::class)
    private fun writeObject(file: Path, `object`: JsonObject) {
        file.parent.createDirectories()
        val temporary = file.resolveSibling(file.fileName.toString() + ".tmp")
        Files.write(temporary, GSON.toJson(`object`).toByteArray(StandardCharsets.UTF_8))
        Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING)
    }
}
