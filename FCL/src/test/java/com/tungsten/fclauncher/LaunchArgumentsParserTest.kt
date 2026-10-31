package com.tungsten.fclauncher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import java.util.jar.Attributes
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

/**
 * 直启拆分器的语义测试。样例形状来自真机抓包（1.12.2-Legacy Fabric 游戏、
 * NeoForge 26.3 ConsoleTool processor、Forge FART/binarypatch processor）与
 * 各加载器的典型命令行，断言对齐 java launcher（JDK args.c）的处理结果。
 */
class LaunchArgumentsParserTest {

    @Rule
    @JvmField
    val tmp = TemporaryFolder()

    private fun parse(vararg args: String) = LaunchArgumentsParser.parse(arrayOf("java", *args))

    @Test
    fun vanilla1122_legacy_jdk8() {
        val spec = parse(
            "-Xmx2675m", "-Xms512m", "-Dfile.encoding=UTF-8", "-Dlog4j2.formatMsgNoLookups=true",
            "-XX:ActiveProcessorCount=4", "-Dfml.ignoreInvalidMinecraftCertificates=true",
            "-cp", "/cp/minecraft.jar",
            "mio.Wrapper", "net.minecraft.client.main.Main",
            "--username", "Tester", "--version", "1.12.2", "--accessToken", "secret", "--gameDir", "/gamedir",
        )
        assertEquals(
            listOf("-Xmx2675m", "-Xms512m", "-Dfile.encoding=UTF-8", "-Dlog4j2.formatMsgNoLookups=true",
                "-XX:ActiveProcessorCount=4", "-Dfml.ignoreInvalidMinecraftCertificates=true"),
            spec.vmArgs,
        )
        assertEquals("/cp/minecraft.jar", spec.classpath)
        assertEquals("mio.Wrapper", spec.mainClass)
        assertEquals(
            listOf("net.minecraft.client.main.Main", "--username", "Tester",
                "--version", "1.12.2", "--accessToken", "secret", "--gameDir", "/gamedir"),
            spec.appArgs,
        )
    }

    @Test
    fun fabric1211_separateModuleOptionsMergedToEqForm() {
        val spec = parse(
            "-Xmx4G",
            "--add-exports", "java.base/jdk.internal.misc=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "-Dfoo=1", "-cp", "/cp/game.jar",
            "mio.Wrapper", "net.fabricmc.loader.impl.launch.knot.KnotClient", "--gameDir", "/g",
        )
        assertEquals(
            listOf("-Xmx4G",
                "--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED",
                "--add-opens=java.base/java.lang=ALL-UNNAMED",
                "-Dfoo=1"),
            spec.vmArgs,
        )
        assertEquals("mio.Wrapper", spec.mainClass)
    }

    @Test
    fun forge1201_modulePathSeparateForm() {
        val spec = parse(
            "-p", "/mods",
            "--add-opens", "java.base/java.io=ALL-UNNAMED",
            "-cp", "/cp/forge.jar",
            "mio.Wrapper", "net.minecraftforge.bootstrap.Bootstrap",
        )
        assertTrue(spec.vmArgs.contains("--module-path=/mods"))
        assertTrue(spec.vmArgs.contains("--add-opens=java.base/java.io=ALL-UNNAMED"))
        assertEquals("mio.Wrapper", spec.mainClass)
    }

    @Test
    fun modulePathEqFormAndUpgradePath() {
        val spec = parse("--module-path=/m", "--upgrade-module-path", "/up", "Main")
        assertEquals(listOf("--module-path=/m", "--upgrade-module-path=/up"), spec.vmArgs)
        assertEquals("Main", spec.mainClass)
    }

    @Test
    fun forge1201_realDeviceModuleChain() {
        // 真机 Forge 1.20.1：-p 之后跟分离形式的模块 opens（值含 =），缺一不可
        val spec = parse(
            "-p", "/lib/bootstraplauncher.jar:/lib",
            "--add-modules", "ALL-MODULE-PATH",
            "--add-opens", "java.base/java.lang.invoke=cpw.mods.securejarhandler",
            "--add-exports", "jdk.naming.dns/com.sun.jndi.dns=java.naming",
            "-javaagent:/plugin/authlib-injector.jar=http://localhost:33243",
            "-cp", "/cp/forge.jar",
            "mio.Wrapper", "net.minecraftforge.bootstrap.Bootstrap",
        )
        assertEquals(
            listOf(
                "--module-path=/lib/bootstraplauncher.jar:/lib",
                "--add-modules=ALL-MODULE-PATH",
                "--add-opens=java.base/java.lang.invoke=cpw.mods.securejarhandler",
                "--add-exports=jdk.naming.dns/com.sun.jndi.dns=java.naming",
                "-javaagent:/plugin/authlib-injector.jar=http://localhost:33243",
            ),
            spec.vmArgs,
        )
        assertEquals("mio.Wrapper", spec.mainClass)
    }

    @Test
    fun neoforge263_consoleToolProcessor() {
        // 真机抓包：26.3-NeoForge 安装 processor（jre17）
        val spec = parse(
            "-cp", "/lib/installertools-4.0.17-fatjar.jar",
            "net.neoforged.installertools.ConsoleTool",
            "--task", "PROCESS_MINECRAFT_JAR", "--no-mod-manifest",
            "--input", "/v/26.3-NeoForge.jar",
            "--output", "/lib/minecraft-client-patched-26.3.0.99.jar",
            "--extract-libraries-to", "/lib/",
            "--apply-patches", "/cache/patches.tmp",
        )
        assertEquals("net.neoforged.installertools.ConsoleTool", spec.mainClass)
        assertEquals("/lib/installertools-4.0.17-fatjar.jar", spec.classpath)
        assertEquals(
            listOf("--task", "PROCESS_MINECRAFT_JAR", "--no-mod-manifest",
                "--input", "/v/26.3-NeoForge.jar",
                "--output", "/lib/minecraft-client-patched-26.3.0.99.jar",
                "--extract-libraries-to", "/lib/", "--apply-patches", "/cache/patches.tmp"),
            spec.appArgs,
        )
    }

    @Test
    fun forge_fartProcessor() {
        val spec = parse(
            "-cp", "/lib/fart.jar",
            "net.minecraftforge.fart.Main",
            "--input", "/v/client.jar", "--output", "/lib/client-official.jar",
            "--names", "/lib/mappings.tsrg", "--ann-fix", "--ids-fix", "--src-fix",
            "--record-fix", "--strip-sigs", "--reverse",
        )
        assertEquals("net.minecraftforge.fart.Main", spec.mainClass)
        assertEquals(12, spec.appArgs.size)
        assertTrue(spec.appArgs.contains("--reverse"))
    }

    @Test
    fun forge_binarypatchProcessor() {
        val spec = parse(
            "-cp", "/lib/binpatcher.jar",
            "net.minecraftforge.binarypatcher.ConsoleTool",
            "--clean", "--output", "/lib/forge-client.jar",
            "--apply", "/cache/data.tmp", "--data", "--unpatched",
        )
        assertEquals("net.minecraftforge.binarypatcher.ConsoleTool", spec.mainClass)
        assertEquals(listOf("--clean", "--output", "/lib/forge-client.jar",
            "--apply", "/cache/data.tmp", "--data", "--unpatched"), spec.appArgs)
    }

    @Test
    fun jarOption_expandsManifestMainClass_andOverridesCp() {
        val jar = tempJar("com.example.Hello")
        val spec = parse("-cp", "/other/ignored.jar", "-jar", jar.absolutePath, "--app-flag")
        assertEquals(jar.absolutePath, spec.classpath)
        assertEquals("com.example.Hello", spec.mainClass)
        assertEquals(listOf("--app-flag"), spec.appArgs)
    }

    @Test
    fun lastClasspathWins() {
        val spec = parse("--class-path=/cp1", "-cp", "/cp2", "-classpath", "/cp3", "Main")
        assertEquals("/cp3", spec.classpath)
    }

    @Test
    fun eqFormClasspath() {
        val spec = parse("--class-path=/cp-eq", "Main")
        assertEquals("/cp-eq", spec.classpath)
    }

    @Test
    fun optionsAfterMainClassAreAppArgs() {
        // 游戏参数里含 '-' 开头的 token（-Dfoo=1/--flag）不得被再解析为 JVM 选项
        val spec = parse("-Xmx1G", "Main", "-Dshould.stay.app=1", "--flag", "value")
        assertEquals(listOf("-Xmx1G"), spec.vmArgs)
        assertEquals(listOf("-Dshould.stay.app=1", "--flag", "value"), spec.appArgs)
    }

    @Test
    fun vmOptionPassthrough() {
        val spec = parse("-ea", "-splash:icon.png", "-XX:+UseG1GC", "-Xverify:none",
            "-Xbootclasspath/p:/cacio.jar:/hack.jar", "Main")
        assertEquals(
            listOf("-ea", "-splash:icon.png", "-XX:+UseG1GC", "-Xverify:none",
                "-Xbootclasspath/p:/cacio.jar:/hack.jar"),
            spec.vmArgs,
        )
        assertEquals("Main", spec.mainClass)
    }

    @Test
    fun addModulesEqFormPassthrough() {
        val spec = parse("--add-modules=jdk.incubator.vector", "--enable-native-access=ALL-UNNAMED", "Main")
        assertEquals(
            listOf("--add-modules=jdk.incubator.vector", "--enable-native-access=ALL-UNNAMED"),
            spec.vmArgs,
        )
    }

    @Test
    fun rejections() {
        assertThrows(IOException::class.java) { parse("-m", "mymod/main.Main") }
        assertThrows(IOException::class.java) { parse("--module", "mymod") }
        assertThrows(IOException::class.java) { parse("--module=mymod") }
        assertThrows(IOException::class.java) { parse("-version", "Main") }
        assertThrows(IOException::class.java) { parse("--list-modules", "Main") }
        assertThrows(IOException::class.java) { parse("--help", "Main") }
        assertThrows(IOException::class.java) { parse("@args.txt", "Main") }
        assertThrows(IOException::class.java) { parse("-cp") } // 缺值
        assertThrows(IOException::class.java) { parse("-Xmx1G") } // 无主类
        assertThrows(IOException::class.java) { parse("-jar", "/nonexistent.jar") } // 无 manifest
    }

    @Test
    fun deviceGame1122Sample_smoke() {
        // 真机 1.12.2-Legacy Fabric 实际命令行的截取形状（cacio 大 -D 群 + wrapper）
        val spec = parse(
            "-Djava.awt.headless=false", "-Dcacio.managed.screensize=1324x745",
            "-Xbootclasspath/p:/cacio/cacio-shared.jar:/cacio/ResConfHack.jar",
            "-Xmx2675m", "-Xms512m", "-Djdk.lang.Process.launchMechanism=FORK",
            "-cp", "/cp/legacyfabric.jar",
            "mio.Wrapper", "net.fabricmc.loader.impl.launch.knot.KnotClient",
            "--username", "Tester", "--gameDir", "/gamedir",
        )
        assertEquals("mio.Wrapper", spec.mainClass)
        assertEquals("net.fabricmc.loader.impl.launch.knot.KnotClient", spec.appArgs[0])
        assertTrue(spec.vmArgs.contains("-Xbootclasspath/p:/cacio/cacio-shared.jar:/cacio/ResConfHack.jar"))
    }

    private fun tempJar(mainClass: String): File {
        val manifest = Manifest().apply {
            mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0"
            mainAttributes[Attributes.Name.MAIN_CLASS] = mainClass
        }
        val file = File(tmp.root, "app.jar")
        JarOutputStream(file.outputStream(), manifest).use { it.write(ByteArray(0)) }
        return file
    }
}
