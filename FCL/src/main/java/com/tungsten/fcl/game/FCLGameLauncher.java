package com.tungsten.fcl.game;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.launch.DefaultLauncher;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.LocaleUtils;

import java.io.File;
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

        if (optionsFile.exists())
            return;
        if (configFolder.isDirectory())
            if (findFiles(configFolder, "options.txt"))
                return;
        try {
            // TODO: Dirty implementation here
            if (LocaleUtils.getSystemLocale() == Locale.CHINA)
                FileUtils.writeText(optionsFile, "lang:zh_CN\nforceUnicodeFont:true\n");
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Unable to generate options.txt", e);
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
