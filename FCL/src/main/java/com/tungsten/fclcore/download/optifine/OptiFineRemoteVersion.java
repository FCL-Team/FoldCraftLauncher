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
package com.tungsten.fclcore.download.optifine;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.ComponentRemoteVersion;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;

import java.util.List;

public class OptiFineRemoteVersion extends ComponentRemoteVersion {

    public OptiFineRemoteVersion(String gameVersion, String selfVersion, List<String> urls, boolean snapshot) {
        super(GameComponentType.OPTIFINE, gameVersion, selfVersion, null, snapshot ? Type.SNAPSHOT : Type.RELEASE, urls);
    }

    @Override
    public String getFullVersion() {
        return getGameVersion() + "_" + getSelfVersion();
    }

    @Override
    public Task<Version> getInstallTask(DefaultDependencyManager dependencyManager, Version baseVersion) {
        return new OptiFineInstallTask(dependencyManager, baseVersion, this);
    }
}
