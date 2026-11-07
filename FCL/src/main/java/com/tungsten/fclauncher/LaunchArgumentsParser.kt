package com.tungsten.fclauncher

import java.io.IOException
import java.util.jar.Attributes
import java.util.jar.JarFile

/**
 * 直启（JNI_CreateJavaVM）的命令行拆分器，语义对齐 java launcher（JDK args.c）：
 *
 * - 选项阶段止于首个非选项 token——即主类，其后全部是应用参数（含 '-' 开头者）；
 * - classpath 类选项（-cp/-classpath/--class-path，含 --class-path= 等号形式）后写覆盖，
 *   最终以 -Djava.class.path 传给 JVM（与 java launcher 自身做法一致）；
 * - 模块路径（-p/--module-path，含 = 形式）与 --upgrade-module-path 转
 *   -Djdk.module.path / -Djdk.module.upgrade.path 系统属性（java launcher 内部行为）；
 * - --add-modules/--limit-modules/--add-exports/--add-opens/--add-reads/--patch-module/
 *   --enable-native-access 为 JVM 认识的选项：分离形式合并为「选项=值」单 token 传递
 *   （JNI_CreateJavaVM 逐条解析 JavaVMOption，分离的「选项 + 值」是两条互不相干的
 *   option，会被静默忽略）；= 形式原样透传；
 * - -jar 按 java 语义展开：jar 即 classpath（应用类加载器自行展开 manifest 的
 *   Class-Path），主类取 manifest 的 Main-Class，jar 之后的 token 全为应用参数；
 * - 模块模式（-m/--module）、launcher 自用命令（-version/--list-modules 等）与
 *   @argfile 显式拒绝——静默误判的破坏面更大；
 * - 已知偏差：java launcher 对未知 JVM 选项报错退出，这里经 ignoreUnrecognized 静默
 *   忽略（JVM 选项与 JRE 由启动器配对，版本错配的报错场景不存在，静默对启动更有利）。
 *
 * 解析为纯函数，无 Android 依赖，可单元测试。
 */
object LaunchArgumentsParser {

    /** 拆分结果：vmArgs 为 JVM 选项（不含 classpath 属性），classpath 供调用方自行决定传法 */
    data class LaunchSpec(
        val vmArgs: List<String>,
        val classpath: String?,
        val mainClass: String,
        val appArgs: List<String>,
    )

    /** JVM 逐条认识、须把分离值合并为「选项=值」的模块选项 */
    private val MODULE_OPTIONS = setOf(
        "--add-modules", "--limit-modules", "--add-exports", "--add-opens",
        "--add-reads", "--patch-module", "--enable-native-access",
    )

    /** java launcher 自用命令（本启动器不支持，显式拒绝防静默误判） */
    private val LAUNCHER_COMMANDS = setOf(
        "-version", "--version", "--show-version", "--show-properties", "-h", "-?", "--help",
        "--dry-run", "--list-modules", "--describe-module", "--validate-modules",
        "--show-module-resolution",
    )

    @JvmStatic
    fun parse(args: Array<String>): LaunchSpec {
        var classpath: String? = null
        var mainClass: String? = null
        val vmArgs = ArrayList<String>()
        val appArgs = ArrayList<String>()

        var i = 1 // args[0] 是 java 可执行占位
        while (i < args.size) {
            val a = args[i]
            if (mainClass != null) {
                appArgs.add(a) // 主类之后的 token 全部透传给应用，不再解析
                i++
                continue
            }
            when {
                a == "-jar" -> {
                    if (i + 1 >= args.size) throw IOException("Missing jar path after -jar")
                    classpath = args[++i] // jar 模式下 jar 独占 classpath（java 语义：覆盖 -cp）
                    mainClass = readJarManifestMainClass(classpath)
                }
                a == "-cp" || a == "-classpath" || a == "--class-path" -> classpath = value(args, i, a).also { i++ }
                a.startsWith("--class-path=") -> classpath = a.substringAfter('=')
                // -p 是 launcher 短名，VM 不认；模块路径必须以 --module-path= VM 选项原样传
                // （-Djdk.module.path 属性形式不会被模块系统读取，实测 Forge 1.20.1 bootstrap 失败）
                a == "-p" || a == "--module-path" -> vmArgs.add("--module-path=" + value(args, i, a).also { i++ })
                a.startsWith("--module-path=") -> vmArgs.add(a)
                a == "--upgrade-module-path" -> vmArgs.add("--upgrade-module-path=" + value(args, i, a).also { i++ })
                a.startsWith("--upgrade-module-path=") -> vmArgs.add(a)
                a in MODULE_OPTIONS -> vmArgs.add("$a=" + value(args, i, a).also { i++ })
                a.startsWith("--add-modules=") || a.startsWith("--limit-modules=")
                        || a.startsWith("--add-exports=") || a.startsWith("--add-opens=")
                        || a.startsWith("--add-reads=") || a.startsWith("--patch-module=")
                        || a.startsWith("--enable-native-access=") -> vmArgs.add(a)
                a == "-m" || a == "--module" -> throw IOException("Module mode (-m/--module) is not supported")
                a.startsWith("--module=") -> throw IOException("Module mode (--module=) is not supported")
                a in LAUNCHER_COMMANDS -> throw IOException("Launcher command '$a' is not supported by direct launch")
                a.startsWith("@") -> throw IOException("@argfile expansion is not supported ('$a')")
                a.startsWith("-") -> vmArgs.add(a)
                else -> mainClass = a
            }
            i++
        }
        val resolvedMain = mainClass
            ?: throw IOException("No main class found in launch arguments")
        return LaunchSpec(vmArgs, classpath, resolvedMain, appArgs)
    }

    /** 带值选项的分离值；越界立即报错而非静默吞 token */
    private fun value(args: Array<String>, index: Int, option: String): String {
        if (index + 1 >= args.size) throw IOException("Missing value after JVM option $option")
        return args[index + 1]
    }

    /** -jar 启动的 jar 的主类（java launcher 语义：取 manifest 的 Main-Class） */
    fun readJarManifestMainClass(jarPath: String): String {
        JarFile(jarPath).use { jar ->
            val attrs = jar.manifest?.mainAttributes
            val mainClass = attrs?.getValue(Attributes.Name.MAIN_CLASS)?.trim()
            if (mainClass.isNullOrEmpty())
                throw IOException("Main-Class manifest attribute not found in $jarPath")
            return mainClass
        }
    }
}
