/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.game;

import android.content.Context;

import com.mio.data.Renderer;
import com.mio.device.VulkanCheckManager;
import com.mio.manager.RendererManager;
import com.mio.util.LauncherUtilKt;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.util.RuntimeUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fcl.FCLApp;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.launch.DefaultLauncher;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.versioning.GameVersionNumber;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

public final class FCLGameLauncher extends DefaultLauncher {

    private final VersionSetting setting;

    public FCLGameLauncher(Context context, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options, VersionSetting setting) {
        super(context, repository, version, authInfo, options);
        this.setting = setting;
    }

    @Override
    protected Map<String, String> getConfigurations() {
        Map<String, String> res = super.getConfigurations();
        res.put("${launcher_name}", LauncherUtilKt.getLauncherName(FCLApp.getAppContext()));
        res.put("${launcher_version}", FCLApp.getAppContext().getString(R.string.app_version));
        return res;
    }

    private void generateOptionsTxt() {
        File optionsFile = new File(repository.getRunDirectory(version.getId()), "options.txt");
        File configFolder = new File(repository.getRunDirectory(version.getId()), "config");

        if (!configFolder.exists() && !configFolder.isDirectory()) {
            configFolder.mkdirs();
        }
        File splashFile = new File(configFolder, "splash.properties");
        try {
            FileUtils.writeText(splashFile, "enabled=false");
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to disable forge animation", e);
        }
        // FML 早期加载屏在 Vulkan 后端下会崩或冻死：26.2 的早期窗口先以 OpenGL 渲染，游戏复用该窗口
        // 创建 Vulkan surface 时在 vkCreateAndroidSurfaceKHR 处崩溃；26.3+ 的 ANGLE/Vulkan 渲染线程还会
        // 在启动扫描期反复分配 gralloc 缓冲，与启动器 UI 的 present 在 vendor gralloc 锁上互堵（冻死直至 ANR）。
        // 26.2+ 仅在显式选择 OpenGL 后端时保留早期加载屏，default 无法判断游戏实际后端、与 Vulkan 一并关闭；
        // 更早版本无此冲突，清掉残留的关闭键以恢复模组加载动画。
        File fmlToml = new File(configFolder, "fml.toml");
        boolean hasVulkanBackend = VulkanCheckManager.INSTANCE.hasVulkanBackend(
                repository.getGameVersion(version).orElse(null));
        boolean disableEarlyWindow = hasVulkanBackend && !setting.getGraphicsBackend().equals("opengl");
        applyFmlEarlyWindowControl(fmlToml, disableEarlyWindow);
        GameOption gameOption = null;
        if (!optionsFile.exists()) {
            try {
                RuntimeUtils.copyAssets(context, "options.txt", optionsFile.getAbsolutePath());
                gameOption = new GameOption(optionsFile.getParent());
                gameOption.set("lang", LocaleUtils.isChinese(context) ? "zh_cn" : "en_us");
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Unable to generate options.txt", e);
            }
        }
        if (gameOption == null)
            gameOption = new GameOption(optionsFile.getParent());
        GameVersionNumber gameVersion = GameVersionNumber.asGameVersion(repository.getGameVersion(version).orElse("0.0"));
        // 13w16a~13w23b 快照缺少非英语语言的外部语言资源，即使格式正确，选了不可用语言也会卡在 Mojang 加载界面，
        // 历史已写入的 lang 一并重置为默认英语；置于 fixLang 之前，随其规范成该时代的 en_US 格式
        if (isLangUnavailableSnapshot(gameVersion))
            gameOption.set("lang", "en_us");
        fixLang(gameOption);
        fixOptions(gameOption);
        gameOption.save();
    }

    /**
     * 读取 fml.toml 并合并 earlyWindowControl 键，内容没有变化时不写盘。
     * FML 加载后会按 spec 回写补全注释与其余键。
     */
    private static void applyFmlEarlyWindowControl(File fmlToml, boolean disableEarlyWindow) {
        if (!fmlToml.isFile()) {
            if (!disableEarlyWindow) {
                return;
            }
            try {
                FileUtils.writeText(fmlToml, "earlyWindowControl = false\n");
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Unable to write fml.toml", e);
            }
            return;
        }
        try {
            String content = FileUtils.readText(fmlToml);
            String merged = buildFmlTomlContent(content, disableEarlyWindow);
            if (!merged.equals(content)) {
                FileUtils.writeText(fmlToml, merged);
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to write fml.toml", e);
        }
    }

    /**
     * 生成合并后的 fml.toml 内容：需要关闭时把关闭键写到文件头部（不落入任何表作用域），
     * 并移除文件中已有的同名键；无需关闭时只移除残留的关闭键。
     * 文件中已有唯一位于根作用域的关闭键时原样返回，避免改动用户的文件排版。
     */
    static String buildFmlTomlContent(String content, boolean disableEarlyWindow) {
        String[] lines = content.split("\n", -1);
        int lineCount = lines.length;
        if (lineCount > 0 && lines[lineCount - 1].isEmpty()) {
            lineCount--;
        }
        if (disableEarlyWindow && hasRootDisabledKey(lines, lineCount)) {
            return content;
        }
        StringBuilder str = new StringBuilder(disableEarlyWindow ? "earlyWindowControl = false\n" : "");
        for (int i = 0; i < lineCount; i++) {
            if (!lines[i].strip().startsWith("earlyWindowControl")) {
                str.append(lines[i]).append('\n');
            }
        }
        return str.toString();
    }

    /**
     * 文件中是否存在唯一的、表作用域之外的 earlyWindowControl = false 键
     */
    private static boolean hasRootDisabledKey(String[] lines, int lineCount) {
        boolean inTable = false;
        boolean found = false;
        for (int i = 0; i < lineCount; i++) {
            String stripped = lines[i].strip();
            if (stripped.startsWith("[")) {
                inTable = true;
            } else if (stripped.startsWith("earlyWindowControl")) {
                int equals = stripped.indexOf('=');
                if (found || inTable || equals < 0
                        || !stripped.substring(equals + 1).strip().equals("false")) {
                    return false;
                }
                found = true;
            }
        }
        return found;
    }

    private void fixOptions(GameOption gameOption) {
        GameVersionNumber gameVersion = GameVersionNumber.asGameVersion(repository.getGameVersion(version).orElse("0.0"));
        gameOption.set("touchscreen", "false");
        if (gameVersion.compareTo("1.13") < 0) {
            gameOption.set("key_key.fullscreen", "0");
            gameOption.set("key_key.streamStartStop", "0");
        }
    }

    private static boolean isLangUnavailableSnapshot(GameVersionNumber gameVersion) {
        return gameVersion instanceof GameVersionNumber.LegacySnapshot
                && gameVersion.compareTo("13w16a") >= 0 && gameVersion.compareTo("13w23b") <= 0;
    }

    private void fixLang(GameOption gameOption) {
        GameVersionNumber gameVersion = GameVersionNumber.asGameVersion(repository.getGameVersion(version).orElse("0.0"));
        if (gameVersion.compareTo("1.1") < 0) {
            return;
        }
        boolean toUpper = gameVersion.compareTo("1.11") < 0;
        String lang;
        lang = gameOption.get("lang");
        if (lang == null) return;
        String[] parts = lang.split("_", 2);
        if (parts.length != 2) return;
        lang = parts[0] + "_" + (toUpper ? parts[1].toUpperCase() : parts[1].toLowerCase());
        gameOption.set("lang", lang);
    }

    private void modifyIfConfigDetected(String config, String option, String replacement, boolean overwrite, Renderer... renderers) {
        boolean patch = false;
        if (renderers.length == 0) {
            patch = true;
        } else {
            for (Renderer renderer : renderers) {
                if (renderer == options.getRenderer()) {
                    patch = true;
                    break;
                }
            }
        }
        File configFolder = new File(repository.getRunDirectory(version.getId()), "config");
        if (patch && configFolder.exists() && new File(configFolder, config).exists()) {
            File configFile = new File(configFolder, config);
            StringBuilder str = new StringBuilder();
            try (BufferedReader bfr = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = bfr.readLine()) != null) {
                    if (overwrite && line.contains(option)) {
                        str.append(replacement).append("\n");
                    } else {
                        str.append(line).append("\n");
                    }
                }
                if (!overwrite && !str.toString().contains(replacement.replace("false", "").replace("true", ""))) {
                    str.append(replacement);
                }
            } catch (Exception e) {
                Logging.LOG.log(Level.WARNING, "Unable to read " + config + ".", e);
            }
            if (!"".equals(str.toString())) {
                try (FileWriter fw = new FileWriter(configFile)) {
                    fw.write(str.toString());
                } catch (IOException e) {
                    Logging.LOG.log(Level.WARNING, "Unable to write " + config + ".", e);
                }
            }
        }
    }

    private boolean findFiles(File folder, String fileName) {
        File[] fs = folder.listFiles();
        if (fs != null) {
            for (File f : fs) {
                if (f.isDirectory())
                    if (f.listFiles((dir, name) -> name.equals(fileName)) != null)
                        return true;
                if (f.getName().equals(fileName))
                    return true;
            }
        }
        return false;
    }

    @Override
    public FCLBridge launch() throws IOException, InterruptedException {
        generateOptionsTxt();
        // Sodium
        modifyIfConfigDetected("sodium-mixins.properties", "", "mixin.features.chunk_rendering=false", false, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU);
        // Rubidium
        modifyIfConfigDetected("rubidium-mixins.properties", "", "mixin.features.chunk_rendering=false", false, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU);
        // DraconicEvolution
        String config = "brandon3055/DraconicEvolution.cfg";
        modifyIfConfigDetected(config, "B:useShaders=", "B:useShaders=false", true, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU, RendererManager.RENDERER_NGGL4ES);
        modifyIfConfigDetected(config, "B:\"crystalShaders\"=", "B:\"crystalShaders\"=false", true, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU, RendererManager.RENDERER_NGGL4ES);
        modifyIfConfigDetected(config, "B:\"reactorShaders\"=", "B:\"reactorShaders\"=false", true, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU, RendererManager.RENDERER_NGGL4ES);
        modifyIfConfigDetected(config, "B:\"guardianShaders\"=", "B:\"guardianShaders\"=false", true, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU, RendererManager.RENDERER_NGGL4ES);
        modifyIfConfigDetected(config, "B:\"otherShaders\"=", "B:\"otherShaders\"=false", true, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU, RendererManager.RENDERER_NGGL4ES);
        // Pixelmon
        modifyIfConfigDetected("pixelmon/config.yml", "use-discord-rich-presence:", "use-discord-rich-presence: false", true);
        // ImmersiveEngineering
        modifyIfConfigDetected("immersiveengineering-client.toml", "stencilBufferEnabled", "stencilBufferEnabled = false", true, RendererManager.RENDERER_GL4ES, RendererManager.RENDERER_VGPU, RendererManager.RENDERER_NGGL4ES);
        return super.launch();
    }
}
