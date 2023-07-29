package com.tungsten.fcl.setting;

import com.tungsten.fcl.game.FCLCacheRepository;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.CacheRepository;

public final class Settings {

    private static Settings instance;

    public static Settings instance() {
        if (instance == null) {
            throw new IllegalStateException("Settings hasn't been initialized");
        }
        return instance;
    }

    /**
     * Should be called from {@link ConfigHolder#init()}.
     */
    static void init() {
        instance = new Settings();
    }

    private Settings() {
        DownloadProviders.init();
        Accounts.init();
        Profiles.init();
        Controllers.init();
        AuthlibInjectorServers.init();

        CacheRepository.setInstance(FCLCacheRepository.REPOSITORY);
        FCLCacheRepository.REPOSITORY.setDirectory(FCLPath.CACHE_DIR);
    }

}
