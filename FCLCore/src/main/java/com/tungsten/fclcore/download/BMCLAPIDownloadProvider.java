package com.tungsten.fclcore.download;

import com.tungsten.fclcore.download.fabric.FabricAPIVersionList;
import com.tungsten.fclcore.download.fabric.FabricVersionList;
import com.tungsten.fclcore.download.forge.ForgeBMCLVersionList;
import com.tungsten.fclcore.download.game.GameVersionList;
import com.tungsten.fclcore.download.liteloader.LiteLoaderBMCLVersionList;
import com.tungsten.fclcore.download.optifine.OptiFineBMCLVersionList;
import com.tungsten.fclcore.download.quilt.QuiltAPIVersionList;
import com.tungsten.fclcore.download.quilt.QuiltVersionList;

public class BMCLAPIDownloadProvider implements DownloadProvider {
    private final String apiRoot;
    private final GameVersionList game;
    private final FabricVersionList fabric;
    private final FabricAPIVersionList fabricApi;
    private final ForgeBMCLVersionList forge;
    private final LiteLoaderBMCLVersionList liteLoader;
    private final OptiFineBMCLVersionList optifine;
    private final QuiltVersionList quilt;
    private final QuiltAPIVersionList quiltApi;

    public BMCLAPIDownloadProvider(String apiRoot) {
        this.apiRoot = apiRoot;
        this.game = new GameVersionList(this);
        this.fabric = new FabricVersionList(this);
        this.fabricApi = new FabricAPIVersionList(this);
        this.forge = new ForgeBMCLVersionList(apiRoot);
        this.liteLoader = new LiteLoaderBMCLVersionList(this);
        this.optifine = new OptiFineBMCLVersionList(apiRoot);
        this.quilt = new QuiltVersionList(this);
        this.quiltApi = new QuiltAPIVersionList(this);
    }

    public String getApiRoot() {
        return apiRoot;
    }

    @Override
    public String getVersionListURL() {
        return apiRoot + "/mc/game/version_manifest.json";
    }

    @Override
    public String getAssetBaseURL() {
        return apiRoot + "/assets/";
    }

    @Override
    public VersionList<?> getVersionListById(String id) {
        switch (id) {
            case "game":
                return game;
            case "fabric":
                return fabric;
            case "fabric-api":
                return fabricApi;
            case "forge":
                return forge;
            case "liteloader":
                return liteLoader;
            case "optifine":
                return optifine;
            case "quilt":
                return quilt;
            case "quilt-api":
                return quiltApi;
            default:
                throw new IllegalArgumentException("Unrecognized version list id: " + id);
        }
    }

    @Override
    public String injectURL(String baseURL) {
        return baseURL
                .replace("https://bmclapi2.bangbang93.com", apiRoot)
                .replace("https://launchermeta.mojang.com", apiRoot)
                .replace("https://piston-meta.mojang.com", apiRoot)
                .replace("https://piston-data.mojang.com", apiRoot)
                .replace("https://launcher.mojang.com", apiRoot)
                .replace("https://libraries.minecraft.net", apiRoot + "/libraries")
                .replaceFirst("https?://files\\.minecraftforge\\.net/maven", apiRoot + "/maven")
                .replace("https://maven.minecraftforge.net", apiRoot + "/maven")
                .replace("http://dl.liteloader.com/versions/versions.json", apiRoot + "/maven/com/mumfrey/liteloader/versions.json")
                .replace("http://dl.liteloader.com/versions", apiRoot + "/maven")
                .replace("https://meta.fabricmc.net", apiRoot + "/fabric-meta")
                .replace("https://maven.fabricmc.net", apiRoot + "/maven")
                .replace("https://authlib-injector.yushi.moe", apiRoot + "/mirrors/authlib-injector")
                .replace("https://repo1.maven.org/maven2", "https://maven.aliyun.com/repository/central");
    }

    @Override
    public int getConcurrency() {
        return Math.max(Runtime.getRuntime().availableProcessors() * 2, 6);
    }
}
