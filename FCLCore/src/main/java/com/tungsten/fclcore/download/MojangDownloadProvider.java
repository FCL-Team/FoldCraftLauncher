package com.tungsten.fclcore.download;

import com.tungsten.fclcore.download.fabric.FabricAPIVersionList;
import com.tungsten.fclcore.download.fabric.FabricVersionList;
import com.tungsten.fclcore.download.forge.ForgeBMCLVersionList;
import com.tungsten.fclcore.download.game.GameVersionList;
import com.tungsten.fclcore.download.liteloader.LiteLoaderVersionList;
import com.tungsten.fclcore.download.optifine.OptiFineBMCLVersionList;
import com.tungsten.fclcore.download.quilt.QuiltAPIVersionList;
import com.tungsten.fclcore.download.quilt.QuiltVersionList;

/**
 * @see <a href="http://wiki.vg">http://wiki.vg</a>
 */
public class MojangDownloadProvider implements DownloadProvider {
    private final GameVersionList game;
    private final FabricVersionList fabric;
    private final FabricAPIVersionList fabricApi;
    private final ForgeBMCLVersionList forge;
    private final LiteLoaderVersionList liteLoader;
    private final OptiFineBMCLVersionList optifine;
    private final QuiltVersionList quilt;
    private final QuiltAPIVersionList quiltApi;

    public MojangDownloadProvider() {
        String apiRoot = "https://bmclapi2.bangbang93.com";

        this.game = new GameVersionList(this);
        this.fabric = new FabricVersionList(this);
        this.fabricApi = new FabricAPIVersionList(this);
        this.forge = new ForgeBMCLVersionList(apiRoot);
        this.liteLoader = new LiteLoaderVersionList(this);
        this.optifine = new OptiFineBMCLVersionList(apiRoot);
        this.quilt = new QuiltVersionList(this);
        this.quiltApi = new QuiltAPIVersionList(this);
    }

    @Override
    public String getVersionListURL() {
        return "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    }

    @Override
    public String getAssetBaseURL() {
        return "https://resources.download.minecraft.net/";
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
        return baseURL;
    }

    @Override
    public int getConcurrency() {
        return 6;
    }
}
