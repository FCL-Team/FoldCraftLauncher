package com.tungsten.fclcore.util;

import static com.tungsten.fclcore.util.gson.JsonUtils.GSON;

import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动前过滤/替换游戏版本 JSON 中的依赖库。
 * <p>
 * 过滤目的：
 * <ul>
 *   <li>移除 LWJGL 库——游戏运行时改用 FCL 自带的 LWJGL（app_runtime/lwjgl/3.3.3、3.4.1）</li>
 *   <li>移除 LWJGL2 时代的输入/直播辅助库（jinput-platform、twitch-platform），Android 上由 FCL 输入桥取代</li>
 *   <li>替换与 Java 17+ / 新版 Android 不兼容的旧版库（asm、jna、oshi）为已适配的版本</li>
 * </ul>
 */
public class LibFilter {

    // asm-all 5.0.4 是最后一个聚合 jar；asm < 5 无法解析 Java 17 编译的 class 文件，
    // 旧整合包（1.7.x/1.8.x 的 Forge）常携带 asm 4.x，需整体替换
    private static final String ASM_ALL_5_0_4_STRING = "{\n" +
            "      \"name\": \"org.ow2.asm:asm-all:5.0.4\",\n" +
            "      \"downloads\": {\n" +
            "        \"artifact\": {\n" +
            "          \"path\": \"org/ow2/asm/asm-all/5.0.4/asm-all-5.0.4.jar\",\n" +
            "          \"sha1\": \"e6244859997b3d4237a552669279780876228909\",\n" +
            "          \"url\": \"https://repo1.maven.org/maven2/org/ow2/asm/asm-all/5.0.4/asm-all-5.0.4.jar\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";
    // JNA 5.13 起支持新版 Android（API 30+ 的 System.loadLibrary 行为变更），旧版本会启动失败
    private static final String JNA_5_13_STRING = "{\n" +
            "      \"name\": \"net.java.dev.jna:jna:5.13.0\",\n" +
            "      \"downloads\": {\n" +
            "        \"artifact\": {\n" +
            "          \"path\": \"net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar\",\n" +
            "          \"sha1\": \"1200e7ebeedbe0d10062093f32925a912020e747\",\n" +
            "          \"url\": \"https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";
    // oshi-core 6.2 在 Android/Java 17 环境下获取系统信息异常，6.3 修复
    private static final String OSHI_6_3_STRING = "{\n" +
            "      \"name\": \"com.github.oshi:oshi-core:6.3.0\",\n" +
            "      \"downloads\": {\n" +
            "        \"artifact\": {\n" +
            "          \"path\": \"com/github/oshi/oshi-core/6.3.0/oshi-core-6.3.0.jar\",\n" +
            "          \"sha1\": \"9e98cf55be371cafdb9c70c35d04ec2a8c2b42ac\",\n" +
            "          \"url\": \"https://repo1.maven.org/maven2/com/github/oshi/oshi-core/6.3.0/oshi-core-6.3.0.jar\"\n" +
            "        }\n" +
            "      }\n" +
            "    }";

    private static final Library ASM_ALL_5_0_4 = GSON.fromJson(ASM_ALL_5_0_4_STRING, Library.class);
    private static final Library JNA_5_13 = GSON.fromJson(JNA_5_13_STRING, Library.class);
    private static final Library OSHI_6_3 = GSON.fromJson(OSHI_6_3_STRING, Library.class);

    /**
     * 过滤版本依赖，默认跳过 LWJGL 库
     */
    public static Version filter(Version version) {
        return version.setLibraries(filterLibs(version.getLibraries(), true));
    }

    /**
     * 过滤版本依赖，skipLwjgl 控制是否移除 LWJGL 库
     */
    public static Version filter(Version version, boolean skipLwjgl) {
        return version.setLibraries(filterLibs(version.getLibraries(), skipLwjgl));
    }

    public static List<Library> filterLibs(List<Library> libraries, boolean skipLwjgl) {
        ArrayList<Library> newLibraries = new ArrayList<>();
        for (Library library : libraries) {
            // 过滤 LWJGL 官方库（org.lwjgl:* / org.lwjgl.lwjgl:*）
            if (skipLwjgl && library.getName().contains("org.lwjgl"))
                continue;
            // jinput-platform / twitch-platform 是 LWJGL2 的输入与直播依赖，Android 上由 FCL 输入桥取代
            if (!library.getName().contains("jinput-platform") && !library.getName().contains("twitch-platform")) {
                String[] version = library.getName().split(":")[2].split("\\.");
                if (library.getArtifactId().equals("asm-all") && Integer.parseInt(version[0]) < 5) {
                    // asm < 5 无法解析 Java 17 的 class 文件，替换为 5.0.4
                    newLibraries.add(ASM_ALL_5_0_4);
                } else if (library.getName().startsWith("net.java.dev.jna:jna:")) {
                    if (Integer.parseInt(version[0]) >= 5 && Integer.parseInt(version[1]) >= 13) {
                        newLibraries.add(library);
                    } else {
                        // jna < 5.13 不兼容新版 Android，替换为 5.13.0
                        newLibraries.add(JNA_5_13);
                    }
                } else if (library.getName().startsWith("com.github.oshi:oshi-core:")) {
                    if (Integer.parseInt(version[0]) != 6 || Integer.parseInt(version[1]) != 2) {
                        newLibraries.add(library);
                    } else {
                        // oshi-core 6.2 在 Android 上获取系统信息异常，替换为 6.3.0
                        newLibraries.add(OSHI_6_3);
                    }
                } else {
                    newLibraries.add(library);
                }
            }
        }
        return newLibraries;
    }

}