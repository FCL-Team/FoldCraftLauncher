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
package com.tungsten.fclcore.download;

import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.util.io.NetworkUtils;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * The service provider that provides Minecraft online file downloads.
 */
public interface DownloadProvider {

    List<URL> getVersionListURLs();

    List<URL> getAssetObjectCandidates(String assetObjectLocation);

    /**
     * Inject into original URL provided by Mojang and Forge.
     *
     * Since there are many provided URLs that are written in JSONs and are unmodifiable,
     * this method provides a way to change them.
     *
     * @param baseURL original URL provided by Mojang and Forge.
     * @return the URL that is equivalent to [baseURL], but belongs to your own service provider.
     */
    String injectURL(String baseURL);

    /**
     * Inject into original URL provided by Mojang and Forge.
     *
     * Since there are many provided URLs that are written in JSONs and are unmodifiable,
     * this method provides a way to change them.
     *
     * @param baseURL original URL provided by Mojang and Forge.
     * @return the URL that is equivalent to [baseURL], but belongs to your own service provider.
     */
    default List<URL> injectURLWithCandidates(String baseURL) {
        return Collections.singletonList(NetworkUtils.toURL(injectURL(baseURL)));
    }

    default List<URL> injectURLsWithCandidates(List<String> urls) {
        LinkedHashSet<URL> result = new LinkedHashSet<>();
        for (String url : urls) {
            result.addAll(injectURLWithCandidates(url));
        }
        return List.copyOf(result);
    }

    /**
     * the specific version list that this download provider provides. i.e. "fabric", "forge", "liteloader", "game", "optifine"
     *
     * @param componentType the component type of specific version list that this download provider provides. i.e. "fabric", "forge", "liteloader", "game", "optifine"
     * @return the version list
     * @throws IllegalArgumentException if the version list does not exist
     */
    ComponentVersionList<?> getVersionList(GameComponentType componentType);

    /**
     * The maximum download concurrency that this download provider supports.
     * @return the maximum download concurrency.
     */
    int getConcurrency();
}
