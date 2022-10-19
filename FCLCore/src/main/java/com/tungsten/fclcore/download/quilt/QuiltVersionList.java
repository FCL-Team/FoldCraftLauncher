package com.tungsten.fclcore.download.quilt;

import static com.tungsten.fclcore.util.Lang.wrap;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class QuiltVersionList extends VersionList<QuiltRemoteVersion> {
    private final DownloadProvider downloadProvider;

    public QuiltVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return false;
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return CompletableFuture.runAsync(wrap(() -> {
            List<String> gameVersions = getGameVersions(GAME_META_URL);
            List<String> loaderVersions = getGameVersions(LOADER_META_URL);

            lock.writeLock().lock();

            try {
                for (String gameVersion : gameVersions)
                    for (String loaderVersion : loaderVersions)
                        versions.put(gameVersion, new QuiltRemoteVersion(gameVersion, loaderVersion,
                                Collections.singletonList(getLaunchMetaUrl(gameVersion, loaderVersion))));
            } finally {
                lock.writeLock().unlock();
            }
        }));
    }

    private static final String LOADER_META_URL = "https://meta.quiltmc.org/v3/versions/loader";
    private static final String GAME_META_URL = "https://meta.quiltmc.org/v3/versions/game";

    private List<String> getGameVersions(String metaUrl) throws IOException {
        String json = NetworkUtils.doGet(NetworkUtils.toURL(downloadProvider.injectURL(metaUrl)));
        return JsonUtils.GSON.<ArrayList<GameVersion>>fromJson(json, new TypeToken<ArrayList<GameVersion>>() {
        }.getType()).stream().map(GameVersion::getVersion).collect(Collectors.toList());
    }

    private static String getLaunchMetaUrl(String gameVersion, String loaderVersion) {
        return String.format("https://meta.quiltmc.org/v3/versions/loader/%s/%s", gameVersion, loaderVersion);
    }

    private static class GameVersion {
        private final String version;
        private final String maven;
        private final boolean stable;

        public GameVersion() {
            this("", null, false);
        }

        public GameVersion(String version, String maven, boolean stable) {
            this.version = version;
            this.maven = maven;
            this.stable = stable;
        }

        public String getVersion() {
            return version;
        }

        @Nullable
        public String getMaven() {
            return maven;
        }

        public boolean isStable() {
            return stable;
        }
    }
}
