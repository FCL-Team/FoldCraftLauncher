package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.util.io.HttpRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public final class GameVersionList extends VersionList<GameRemoteVersion> {
    private final DownloadProvider downloadProvider;

    public GameVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return true;
    }

    @Override
    protected Collection<GameRemoteVersion> getVersionsImpl(String gameVersion) {
        return versions.values();
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return HttpRequest.GET(downloadProvider.getVersionListURL()).getJsonAsync(GameRemoteVersions.class)
                .thenAcceptAsync(root -> {
                    lock.writeLock().lock();
                    try {
                        versions.clear();

                        for (GameRemoteVersionInfo remoteVersion : root.getVersions()) {
                            versions.put(remoteVersion.getGameVersion(), new GameRemoteVersion(
                                    remoteVersion.getGameVersion(),
                                    remoteVersion.getGameVersion(),
                                    Collections.singletonList(remoteVersion.getUrl()),
                                    remoteVersion.getType(), remoteVersion.getReleaseTime()));
                        }
                    } finally {
                        lock.writeLock().unlock();
                    }
                });
    }
}
