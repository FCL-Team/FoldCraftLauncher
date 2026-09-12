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
package com.tungsten.fclcore.mod.multimc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * MultiMC 组件元信息与 meta 仓库地址。
 */
public final class MultiMCComponents {

    /** Minecraft 官方组件的 uid */
    public static final String GAME = "net.minecraft";

    private static final String META_URL = "https://meta.multimc.org/v1/%s/%s.json";

    private MultiMCComponents() {
    }

    /**
     * 组件未声明版本时使用的兜底版本。
     */
    private static String getFallbackVersion(String componentID, String mcVersion) {
        switch (componentID) {
            case "org.lwjgl":
                return "2.9.1";
            case "org.lwjgl3":
                return "3.1.2";
            case "net.fabricmc.intermediary":
            case "org.quiltmc.hashed":
                return mcVersion;
            default:
                return null;
        }
    }

    public static URL getMetaURL(String componentID, String version, String mcVersion) throws IOException {
        String actualVersion = version != null ? version : getFallbackVersion(componentID, mcVersion);
        try {
            return new URI(String.format(META_URL, componentID, actualVersion)).toURL();
        } catch (URISyntaxException e) {
            throw new IOException("Invalid MultiMC meta URL for " + componentID, e);
        }
    }
}
