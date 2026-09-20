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

import com.tungsten.fclcore.download.cleanroom.CleanroomVersionList;
import com.tungsten.fclcore.download.fabric.FabricAPIVersionList;
import com.tungsten.fclcore.download.fabric.FabricVersionList;
import com.tungsten.fclcore.download.forge.ForgeVersionList;
import com.tungsten.fclcore.download.game.GameVersionList;
import com.tungsten.fclcore.download.legacyfabric.LegacyFabricAPIVersionList;
import com.tungsten.fclcore.download.legacyfabric.LegacyFabricVersionList;
import com.tungsten.fclcore.download.liteloader.LiteLoaderVersionList;
import com.tungsten.fclcore.download.neoforge.NeoForgeOfficialVersionList;
import com.tungsten.fclcore.download.optifine.OptiFineBMCLVersionList;
import com.tungsten.fclcore.download.quilt.QuiltAPIVersionList;
import com.tungsten.fclcore.download.quilt.QuiltVersionList;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.util.io.NetworkUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @see <a href="http://wiki.vg">http://wiki.vg</a>
 */
public class MojangDownloadProvider implements DownloadProvider {
    private final GameVersionList game;
    private final FabricVersionList fabric;
    private final FabricAPIVersionList fabricApi;
    private final ForgeVersionList forge;
    private final CleanroomVersionList cleanroom;
    private final NeoForgeOfficialVersionList neoforge;
    private final LiteLoaderVersionList liteLoader;
    private final OptiFineBMCLVersionList optifine;
    private final QuiltVersionList quilt;
    private final QuiltAPIVersionList quiltApi;
    private final LegacyFabricVersionList legacyFabric;
    private final LegacyFabricAPIVersionList legacyFabricApi;

    public MojangDownloadProvider() {
        // If there is no official download channel available, fallback to BMCLAPI.
        String apiRoot = "https://bmclapi2.bangbang93.com";

        this.game = new GameVersionList(this);
        this.fabric = new FabricVersionList(this);
        this.fabricApi = new FabricAPIVersionList(this);
        this.forge = new ForgeVersionList(this);
        this.neoforge = new NeoForgeOfficialVersionList(this);
        this.cleanroom = new CleanroomVersionList(this);
        this.liteLoader = new LiteLoaderVersionList(this);
        this.optifine = new OptiFineBMCLVersionList(apiRoot);
        this.quilt = new QuiltVersionList(this);
        this.quiltApi = new QuiltAPIVersionList(this);
        this.legacyFabric = new LegacyFabricVersionList(this);
        this.legacyFabricApi = new LegacyFabricAPIVersionList(this);
    }

    @Override
    public List<URL> getVersionListURLs() {
        return Collections.singletonList(NetworkUtils.toURL("https://piston-meta.mojang.com/mc/game/version_manifest.json"));
    }

    @Override
    public List<URL> getAssetObjectCandidates(String assetObjectLocation) {
        return Collections.singletonList(NetworkUtils.toURL("https://resources.download.minecraft.net/" + assetObjectLocation));
    }

    @Override
    public ComponentVersionList<?> getVersionList(GameComponentType componentType) {
        switch (componentType) {
            case GAME:
                return game;
            case FABRIC:
                return fabric;
            case FABRIC_API:
                return fabricApi;
            case FORGE:
                return forge;
            case CLEANROOM:
                return cleanroom;
            case NEO_FORGE:
                return neoforge;
            case LITELOADER:
                return liteLoader;
            case OPTIFINE:
                return optifine;
            case QUILT:
                return quilt;
            case QUILT_API:
                return quiltApi;
            case LEGACY_FABRIC:
                return legacyFabric;
            case LEGACY_FABRIC_API:
                return legacyFabricApi;
            default:
                throw new IllegalArgumentException("Unrecognized component type: " + componentType);
        }
    }

    @Override
    public String injectURL(String baseURL) {
        return baseURL;
    }

    /**
     * libraries.minecraft.net 不托管部分第三方构件（如 1.7.10 Forge 的 Scala/Akka 依赖、
     * GTNH 的 lwjgl3ify forgePatches），在纯官方源模式下追加镜像与 Maven Central 候选，
     * 避免单一 404 直接导致启动中断。
     */
    @Override
    public List<URL> injectURLWithCandidates(String baseURL) {
        if (baseURL.startsWith("https://libraries.minecraft.net/")) {
            String path = baseURL.substring("https://libraries.minecraft.net/".length());
            return Arrays.asList(
                    NetworkUtils.toURL(baseURL),
                    NetworkUtils.toURL("https://bmclapi2.bangbang93.com/maven/" + path),
                    NetworkUtils.toURL("https://repo1.maven.org/maven2/" + path));
        }
        return DownloadProvider.super.injectURLWithCandidates(baseURL);
    }

    @Override
    public int getConcurrency() {
        return 6;
    }
}
