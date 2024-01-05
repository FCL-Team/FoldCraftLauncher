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
package com.tungsten.fclcore.game;

import com.tungsten.fclcore.util.platform.OperatingSystem;

public final class OSRestriction {

    private final OperatingSystem name;
    private final String version;
    private final String arch;

    public OSRestriction() {
        this(OperatingSystem.UNKNOWN);
    }

    public OSRestriction(OperatingSystem name) {
        this(name, null);
    }

    public OSRestriction(OperatingSystem name, String version) {
        this(name, version, null);
    }

    public OSRestriction(OperatingSystem name, String version, String arch) {
        this.name = name;
        this.version = version;
        this.arch = arch;
    }

    public OperatingSystem getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getArch() {
        return arch;
    }

    public boolean allow() {
        return false;
    }

}
