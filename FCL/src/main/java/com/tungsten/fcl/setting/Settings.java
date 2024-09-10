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
package com.tungsten.fcl.setting;

import com.tungsten.fcl.game.FCLCacheRepository;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.Schedulers;
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
        Schedulers.defaultScheduler().execute(Controllers::init);
        AuthlibInjectorServers.init();

        CacheRepository.setInstance(FCLCacheRepository.REPOSITORY);
        FCLCacheRepository.REPOSITORY.setDirectory(FCLPath.CACHE_DIR);
    }

}
