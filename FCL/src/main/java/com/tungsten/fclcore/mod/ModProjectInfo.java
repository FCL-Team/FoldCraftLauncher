/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2026 huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fclcore.mod;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Localized metadata of a mod project.
 */
public final class ModProjectInfo extends AddonProjectInfo {

    private final @Nullable Object modIds;

    public ModProjectInfo(
            int mcmodId, String curseforgeId, String modrinthId,
            String mainName, String subName, String abbrName,
            String modIds) {
        super(mcmodId, curseforgeId, modrinthId, mainName, subName, abbrName);

        if (modIds.isEmpty()) {
            this.modIds = null;
        } else if (modIds.indexOf(';') < 0) {
            this.modIds = modIds;
        } else {
            this.modIds = List.of(modIds.split(";"));
        }
    }

    /**
     * Returns an unmodifiable list of recorded mod IDs, or an empty list if none are recorded.
     */
    @SuppressWarnings("unchecked")
    public List<String> getModIds() {
        if (modIds == null)
            return List.of();
        if (modIds instanceof String)
            return List.of((String) modIds);
        return (List<String>) modIds;
    }

    @Override
    protected String mcmodClass() {
        return "class";
    }

    @Override
    protected String curseforgeClass() {
        return "mc-mods";
    }

    @Override
    protected String modrinthClass() {
        return "mod";
    }
}
