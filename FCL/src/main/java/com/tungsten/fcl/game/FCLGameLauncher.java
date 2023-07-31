package com.tungsten.fcl.game;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.RuntimeUtils;
import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.launch.DefaultLauncher;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

public final class FCLGameLauncher extends DefaultLauncher {

    public FCLGameLauncher(Context context, GameRepository repository, Version version, AuthInfo authInfo, LaunchOptions options) {
        super(context, repository, version, authInfo, options);
    }

    @Override
    protected Map<String, String> getConfigurations() {
        Map<String, String> res = super.getConfigurations();
        res.put("${launcher_name}", FCLPath.CONTEXT.getString(R.string.app_name));
        res.put("${launcher_version}", FCLPath.CONTEXT.getString(R.string.app_version));
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

        if (optionsFile.exists()) {
            modifyOptions(optionsFile, false);
            return;
        }
        if (configFolder.isDirectory())
            if (findFiles(configFolder, "options.txt"))
                return;
        try {
            RuntimeUtils.copyAssets(context, "options.txt", optionsFile.getAbsolutePath());
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to generate options.txt", e);
        }

        modifyOptions(optionsFile, true);
    }

    private void modifyOptions(File optionsFile, boolean overwrite) {
        StringBuilder str = new StringBuilder();
        try (BufferedReader bfr = new BufferedReader(new FileReader(optionsFile))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.contains("lang:") && LocaleUtils.isChinese(context) && overwrite) {
                    str.append("lang:zh_CN\n");
                } else if (line.contains("forceUnicodeFont:")) {
                    str.append("forceUnicodeFont:false\n");
                } else {
                    str.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            Logging.LOG.log(Level.WARNING, "Unable to read options.txt.", e);
        }
        if (!"".equals(str.toString())) {
            try (FileWriter fw = new FileWriter(optionsFile)) {
                fw.write(str.toString());
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Unable to write options.txt.", e);
            }
        }
    }

    private void modifyIfConfigDetected(String config, String option, String replacement, boolean overwrite, FCLConfig.Renderer... renderers) {
        boolean patch = false;
        if (renderers.length == 0) {
            patch = true;
        } else {
            for (FCLConfig.Renderer renderer : renderers) {
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
                if (!overwrite && !str.toString().contains(replacement)) {
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
        FileUtils.deleteDirectoryQuietly(new File("/data/user_de/0/com.tungsten.fcl/code_cache"));
        generateOptionsTxt();
        // Sodium
        modifyIfConfigDetected("sodium-mixins.properties", "", "mixin.features.chunk_rendering=false", false, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        // Rubidium
        modifyIfConfigDetected("rubidium-mixins.properties", "", "mixin.features.chunk_rendering=false", false, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        // DraconicEvolution
        String config = "brandon3055/DraconicEvolution.cfg";
        modifyIfConfigDetected(config, "B:useShaders=", "B:useShaders=false", true, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        modifyIfConfigDetected(config, "B:\"crystalShaders\"=", "B:\"crystalShaders\"=false", true, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        modifyIfConfigDetected(config, "B:\"reactorShaders\"=", "B:\"reactorShaders\"=false", true, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        modifyIfConfigDetected(config, "B:\"guardianShaders\"=", "B:\"guardianShaders\"=false", true, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        modifyIfConfigDetected(config, "B:\"otherShaders\"=", "B:\"otherShaders\"=false", true, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        // Pixelmon
        modifyIfConfigDetected("pixelmon/config.yml", "use-discord-rich-presence:", "use-discord-rich-presence: false", true);
        // ImmersiveEngineering
        modifyIfConfigDetected("immersiveengineering-client.toml", "stencilBufferEnabled", "stencilBufferEnabled = false", true, FCLConfig.Renderer.RENDERER_GL4ES, FCLConfig.Renderer.RENDERER_VGPU);
        // Create
        modifyIfConfigDetected("flywheel-client.toml", "enabled", "enabled = false", true, FCLConfig.Renderer.RENDERER_GL4ES);
        modifyIfConfigDetected("flywheel-client.toml", "backend =", "backend = \"OFF\"", true, FCLConfig.Renderer.RENDERER_GL4ES);
        return super.launch();
    }
}
