package com.tungsten.fcl.game;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.RuntimeUtils;
import com.tungsten.fclauncher.FCLPath;
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

        if (optionsFile.exists())
            return;
        if (configFolder.isDirectory())
            if (findFiles(configFolder, "options.txt"))
                return;
        try {
            RuntimeUtils.copyAssets(context, "options.txt", optionsFile.getAbsolutePath());
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to generate options.txt", e);
        }

        // TODO: Dirty implementation here
        if (!LocaleUtils.getSystemLocale().getDisplayName().equals(Locale.CHINA.getDisplayName())) {
            StringBuilder str = new StringBuilder();
            try (BufferedReader bfr = new BufferedReader(new FileReader(optionsFile))) {
                String line;
                while ((line = bfr.readLine()) != null) {
                    if (line.contains("lang:")) {
                        str.append("lang:en_us\n");
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
        return super.launch();
    }
}
