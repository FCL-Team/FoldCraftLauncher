package com.tungsten.fclcore.mod.multimc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.tungsten.fclcore.game.Argument;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.game.VersionNotFoundException;
import com.tungsten.fclcore.game.VersionProvider;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证 MultiMC 整合包启动参数修复(2026-10)修改前后参数解析是否存在出入。
 *
 * <p>背景:meta.multimc.org 对 net.neoforged 等加载器的 Json-Patch 以 legacy minecraftArguments
 * 承载全套标准参数 + 加载器附加项;导入合并后版本 json 同时存在 legacy 与新式 arguments.game,
 * 修改前 DefaultLauncher 把两代格式各拼一遍,导致 ModLauncher 的 joptsimple 抛出
 * MultipleArgumentsForOptionException(Found multiple arguments for option gameDir)。
 *
 * <p>覆盖三层,其中"修改前/后拼接逻辑"以参照实现驱动真实的 {@link Arguments} 解析代码:
 * <ol>
 * <li>{@link MultiMCInstancePatch#resolveArtifact} 合成参数承载方式的变化(legacy 字符串 → 新式 game args 并剥离标准占位参数对)</li>
 * <li>{@link Version#addPatch} / {@link Version#resolve} 补丁合并后的版本参数形态</li>
 * <li>DefaultLauncher 游戏参数拼接逻辑的互斥修改:对原本只有单一代际格式的版本,修改前后输出必须完全一致(无出入)</li>
 * </ol>
 */
public class MultiMCLaunchArgsTest {

    /** meta.multimc.org/v1/net.neoforged/21.1.242.json 的 minecraftArguments 原文(实测) */
    private static final String NEOFORGE_LEGACY_ARGS =
            "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory}"
                    + " --assetsDir ${assets_root} --assetIndex ${assets_index_name}"
                    + " --uuid ${auth_uuid} --accessToken ${auth_access_token}"
                    + " --userType ${user_type} --versionType ${version_type}"
                    + " --fml.neoForgeVersion 21.1.242 --fml.fmlVersion 4.0.43"
                    + " --fml.mcVersion 1.21.1 --fml.neoFormVersion 20240808.144430"
                    + " --launchTarget forgeclient";

    /** 1.21.1 原版组件 Json-Patch 的关键字段:新式 arguments.game(标准参数 + clientId/xuid + 自定义分辨率规则) */
    private static final String VANILLA_121_PATCH_JSON = """
            {
              "id": "game",
              "version": "1.21.1",
              "priority": 0,
              "mainClass": "net.minecraft.client.main.Main",
              "arguments": {
                "game": [
                  "--username", "${auth_player_name}",
                  "--version", "${version_name}",
                  "--gameDir", "${game_directory}",
                  "--assetsDir", "${assets_root}",
                  "--assetIndex", "${assets_index_name}",
                  "--uuid", "${auth_uuid}",
                  "--accessToken", "${auth_access_token}",
                  "--clientId", "${clientid}",
                  "--xuid", "${auth_xuid}",
                  "--userType", "${user_type}",
                  "--versionType", "${version_type}",
                  {"rules": [{"action": "allow", "features": {"is_demo_user": true}}], "value": ["--demo"]},
                  {"rules": [{"action": "allow", "features": {"has_custom_resolution": true}}],
                   "value": ["--width", "${resolution_width}", "--height", "${resolution_height}"]}
                ]
              }
            }""";

    /** 1.8.9 原版组件 Json-Patch:仅 legacy minecraftArguments */
    private static final String VANILLA_8_PATCH_JSON = """
            {
              "id": "game",
              "version": "1.8.9",
              "priority": 0,
              "mainClass": "net.minecraft.client.main.Main",
              "minecraftArguments": "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userType ${user_type} --versionType ${version_type}"
            }""";

    /** 与 DefaultLauncher#getConfigurations 的键一致(含 ${} 前缀);FCL 不提供 clientid/auth_xuid,占位符原样保留 */
    private static final Map<String, String> CONFIGURATION = configuration();

    /** 与 DefaultLauncher#getFeatures 一致:FCL 始终提供自定义分辨率 */
    private static final Map<String, Boolean> FEATURES = Collections.singletonMap("has_custom_resolution", true);

    /** 合并过程不会触达 inheritsFrom(provider 仅供继承版本解析),触达即视为测试数据错误 */
    private static final VersionProvider PROVIDER = new VersionProvider() {
        @Override
        public boolean hasVersion(String id) {
            throw new AssertionError("Unexpected version lookup: " + id);
        }

        @Override
        public Version getVersion(String id) throws VersionNotFoundException {
            throw new AssertionError("Unexpected version lookup: " + id);
        }
    };

    private static Map<String, String> configuration() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("${auth_player_name}", "Tester");
        map.put("${version_name}", "天境冒险");
        map.put("${game_directory}", "/storage/game");
        map.put("${assets_root}", "/assets");
        map.put("${assets_index_name}", "17");
        map.put("${auth_uuid}", "uuid-x");
        map.put("${auth_access_token}", "token-x");
        map.put("${user_type}", "msa");
        map.put("${version_type}", "Fold Craft Launcher");
        map.put("${resolution_width}", "1920");
        map.put("${resolution_height}", "1080");
        return map;
    }

    // ---------- 参照实现:DefaultLauncher 游戏参数拼接(修改前 / 修改后) ----------

    /** 修改前:legacy 与新式 arguments.game 无条件各拼一遍,legacy 存在时再补 DEFAULT_GAME_ARGUMENTS */
    private static List<String> gameArgsBefore(Version merged) {
        List<String> res = new ArrayList<>();
        res.addAll(Arguments.parseStringArguments(
                merged.getMinecraftArguments().map(StringUtils::tokenize).orElseGet(ArrayList::new), CONFIGURATION));
        merged.getArguments().map(Arguments::getGame)
                .ifPresent(arguments -> res.addAll(Arguments.parseArguments(arguments, CONFIGURATION, FEATURES)));
        if (merged.getMinecraftArguments().isPresent()) {
            res.addAll(Arguments.parseArguments(Arguments.DEFAULT_GAME_ARGUMENTS, CONFIGURATION, FEATURES));
        }
        return res;
    }

    /** 修改后:legacy 与新式互斥;legacy 非空时仅补 DEFAULT_GAME_ARGUMENTS(width/height 一次) */
    private static List<String> gameArgsAfter(Version merged) {
        List<String> legacy = Arguments.parseStringArguments(
                merged.getMinecraftArguments().map(StringUtils::tokenize).orElseGet(ArrayList::new), CONFIGURATION);
        List<String> res = new ArrayList<>(legacy);
        if (legacy.isEmpty()) {
            merged.getArguments().map(Arguments::getGame)
                    .ifPresent(arguments -> res.addAll(Arguments.parseArguments(arguments, CONFIGURATION, FEATURES)));
        } else {
            res.addAll(Arguments.parseArguments(Arguments.DEFAULT_GAME_ARGUMENTS, CONFIGURATION, FEATURES));
        }
        return res;
    }

    // ---------- 构造辅助 ----------

    private static Version parseVersion(String json) {
        return JsonUtils.fromNonNullJson(json, Version.class);
    }

    /** 模拟设备上保存的版本:基础版本 addPatch 后 resolve 合并 */
    private static Version merge(Version... patches) throws VersionNotFoundException {
        return new Version("天境冒险").addPatch(patches).resolve(PROVIDER);
    }

    /** 修改前 resolveArtifact 的产物:合成参数以老格式 minecraftArguments 字符串承载 */
    private static Version oldStyleMultimcPatch() {
        List<Argument> jvmArguments = new ArrayList<>(Arguments.DEFAULT_JVM_ARGUMENTS);
        return new Version("multimc", null, 1, new Arguments().withJvm(jvmArguments),
                "io.github.zekerzhayard.forgewrapper.installer.Main", new ArrayList<>())
                .setMinecraftArguments(NEOFORGE_LEGACY_ARGS);
    }

    /** 修改后 resolveArtifact 的产物 */
    private static Version newStyleMultimcPatch() {
        MultiMCInstancePatch meta = MultiMCInstancePatch.read("net.neoforged", neoforgePatchJson(null));
        return MultiMCInstancePatch.resolveArtifact(Collections.singletonList(meta), "天境冒险").getPatch();
    }

    private static String neoforgePatchJson(String extraMinecraftArguments) {
        String args = extraMinecraftArguments != null ? extraMinecraftArguments : NEOFORGE_LEGACY_ARGS;
        return """
                {
                  "formatVersion": 1,
                  "uid": "net.neoforged",
                  "version": "21.1.242",
                  "mainClass": "io.github.zekerzhayard.forgewrapper.installer.Main",
                  "minecraftArguments": "%s"
                }""".formatted(args.replace("\"", "\\\""));
    }

    private static int count(List<String> args, String flag) {
        return Collections.frequency(args, flag);
    }

    // ---------- 用例:单一代际格式的版本,修改前后必须无出入 ----------

    @Test
    public void legacyOnlyVersion_argsUnchanged() throws VersionNotFoundException {
        Version merged = merge(parseVersion(VANILLA_8_PATCH_JSON));

        List<String> before = gameArgsBefore(merged);
        List<String> after = gameArgsAfter(merged);
        assertEquals(before, after);
        assertEquals(1, count(after, "--gameDir"));
        assertEquals(1, count(after, "--username"));
    }

    @Test
    public void argumentsOnlyVersion_argsUnchanged() throws VersionNotFoundException {
        Version merged = merge(parseVersion(VANILLA_121_PATCH_JSON));

        List<String> before = gameArgsBefore(merged);
        List<String> after = gameArgsAfter(merged);
        assertEquals(before, after);
        // has_custom_resolution 规则只产生一份 width/height;FCL 未提供 clientid/auth_xuid,占位符原样保留
        assertEquals(1, count(after, "--width"));
        assertEquals("${clientid}", after.get(after.indexOf("--clientId") + 1));
    }

    // ---------- 用例:MultiMC 整合包(两种格式并存) ----------

    /** 修改前缺陷复现:legacy + 新式双拼,--gameDir/--width 各出现两次(回归守卫) */
    @Test
    public void multimcImport_beforeFix_duplicatedStandardArgs() throws VersionNotFoundException {
        Version merged = merge(parseVersion(VANILLA_121_PATCH_JSON), oldStyleMultimcPatch());

        List<String> before = gameArgsBefore(merged);
        assertEquals(2, count(before, "--gameDir"));
        assertEquals(2, count(before, "--username"));
        assertEquals(2, count(before, "--width"));
    }

    /**
     * 修改后·存量导入实例(版本 json 已含 legacy minecraftArguments)走互斥的 legacy 路径:
     * 输出恰好一套标准参数 + 加载器附加项 + width/height 一次,与 MuMu 真机实测的启动参数一致。
     */
    @Test
    public void multimcImport_afterFixLauncher_existingImport_singleSet() throws VersionNotFoundException {
        Version merged = merge(parseVersion(VANILLA_121_PATCH_JSON), oldStyleMultimcPatch());

        List<String> after = gameArgsAfter(merged);
        assertEquals(
                Arrays.asList(
                        "--username", "Tester",
                        "--version", "天境冒险",
                        "--gameDir", "/storage/game",
                        "--assetsDir", "/assets",
                        "--assetIndex", "17",
                        "--uuid", "uuid-x",
                        "--accessToken", "token-x",
                        "--userType", "msa",
                        "--versionType", "Fold Craft Launcher",
                        "--fml.neoForgeVersion", "21.1.242",
                        "--fml.fmlVersion", "4.0.43",
                        "--fml.mcVersion", "1.21.1",
                        "--fml.neoFormVersion", "20240808.144430",
                        "--launchTarget", "forgeclient",
                        "--width", "1920",
                        "--height", "1080"),
                after);
    }

    /**
     * 修改后·新导入实例(resolveArtifact 合成参数改走新式):合并后 game args 为
     * 原版标准参数(含未解析的 ${clientid}/${auth_xuid},与常规应用内安装的版本一致)
     * + 自定义分辨率规则 + 加载器附加项,标准参数仅一套。
     */
    @Test
    public void multimcImport_afterFixImport_singleSet() throws VersionNotFoundException {
        Version merged = merge(parseVersion(VANILLA_121_PATCH_JSON), newStyleMultimcPatch());

        List<String> after = gameArgsAfter(merged);
        assertEquals(
                Arrays.asList(
                        "--username", "Tester",
                        "--version", "天境冒险",
                        "--gameDir", "/storage/game",
                        "--assetsDir", "/assets",
                        "--assetIndex", "17",
                        "--uuid", "uuid-x",
                        "--accessToken", "token-x",
                        "--clientId", "${clientid}",
                        "--xuid", "${auth_xuid}",
                        "--userType", "msa",
                        "--versionType", "Fold Craft Launcher",
                        "--width", "1920",
                        "--height", "1080",
                        "--fml.neoForgeVersion", "21.1.242",
                        "--fml.fmlVersion", "4.0.43",
                        "--fml.mcVersion", "1.21.1",
                        "--fml.neoFormVersion", "20240808.144430",
                        "--launchTarget", "forgeclient"),
                after);
    }

    /**
     * 修改前后有效参数等价性:从修改前输出中按多重集扣除修改后输出的全部参数,
     * 余量必须恰为重复拼接的部分——legacy 承载的一套标准参数与重复的 width/height。
     * 即修复只去除重复,不丢失、不改变任何有效参数。
     */
    @Test
    public void multimcImport_fixOnlyRemovesDuplicates_noArgsLost() throws VersionNotFoundException {
        Version mergedBefore = merge(parseVersion(VANILLA_121_PATCH_JSON), oldStyleMultimcPatch());
        Version mergedAfter = merge(parseVersion(VANILLA_121_PATCH_JSON), newStyleMultimcPatch());

        List<String> before = gameArgsBefore(mergedBefore);
        List<String> after = gameArgsAfter(mergedAfter);

        List<String> remainder = new ArrayList<>(before);
        for (String arg : after) {
            assertTrue("after contains arg missing in before: " + arg, remainder.remove(arg));
        }
        assertEquals(
                Arrays.asList(
                        "--username", "Tester",
                        "--version", "天境冒险",
                        "--gameDir", "/storage/game",
                        "--assetsDir", "/assets",
                        "--assetIndex", "17",
                        "--uuid", "uuid-x",
                        "--accessToken", "token-x",
                        "--userType", "msa",
                        "--versionType", "Fold Craft Launcher",
                        "--width", "1920",
                        "--height", "1080"),
                remainder);
    }

    // ---------- 用例:resolveArtifact 合成参数的剥离规则 ----------

    /** 合成 patch 的 game args 恰为加载器附加项:标准占位参数对被剥离,且不再写 minecraftArguments */
    @Test
    public void resolveArtifact_extrasContainOnlyLoaderArguments() {
        Version patch = newStyleMultimcPatch();

        assertTrue(patch.getMinecraftArguments().isEmpty());
        List<Argument> gameArgs = patch.getArguments().orElseThrow(AssertionError::new).getGame();
        List<String> extras = Arguments.parseArguments(gameArgs, CONFIGURATION, FEATURES);
        assertEquals(
                Arrays.asList("--fml.neoForgeVersion", "21.1.242",
                        "--fml.fmlVersion", "4.0.43",
                        "--fml.mcVersion", "1.21.1",
                        "--fml.neoFormVersion", "20240808.144430",
                        "--launchTarget", "forgeclient"),
                extras);
    }

    /** flag 带非占位符具体值时视为有意覆盖,原样保留 */
    @Test
    public void resolveArtifact_keepsConcreteStandardFlagOverride() {
        MultiMCInstancePatch meta = MultiMCInstancePatch.read("net.neoforged",
                neoforgePatchJson(NEOFORGE_LEGACY_ARGS + " --gameDir /custom/path"));
        Version patch = MultiMCInstancePatch.resolveArtifact(Collections.singletonList(meta), "天境冒险").getPatch();

        List<Argument> gameArgs = patch.getArguments().orElseThrow(AssertionError::new).getGame();
        List<String> extras = Arguments.parseArguments(gameArgs, CONFIGURATION, FEATURES);
        int gameDir = extras.indexOf("--gameDir");
        assertEquals(Arrays.asList("--gameDir", "/custom/path"), extras.subList(gameDir, gameDir + 2));
    }

    /** patch 未携带 minecraftArguments(如部分原版 meta):合成 patch 无 game args,基础版本参数原样生效 */
    @Test
    public void resolveArtifact_noMinecraftArguments_baseArgsIntact() throws VersionNotFoundException {
        MultiMCInstancePatch meta = MultiMCInstancePatch.read("net.minecraft", """
                {
                  "formatVersion": 1,
                  "uid": "net.minecraft",
                  "version": "1.21.1",
                  "mainClass": "net.minecraft.client.main.Main"
                }""");
        Version patch = MultiMCInstancePatch.resolveArtifact(Collections.singletonList(meta), "天境冒险").getPatch();

        assertTrue(patch.getMinecraftArguments().isEmpty());
        assertNull(patch.getArguments().orElseThrow(AssertionError::new).getGame());

        Version merged = merge(parseVersion(VANILLA_121_PATCH_JSON), patch);
        List<String> vanillaOnly = gameArgsAfter(merge(parseVersion(VANILLA_121_PATCH_JSON)));
        assertEquals(vanillaOnly, gameArgsAfter(merged));
        assertEquals(vanillaOnly, gameArgsBefore(merged));
    }
}
