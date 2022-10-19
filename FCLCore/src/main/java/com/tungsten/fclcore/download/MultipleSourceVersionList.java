package com.tungsten.fclcore.download;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MultipleSourceVersionList extends VersionList<RemoteVersion> {

    private final List<VersionList<?>> backends;

    MultipleSourceVersionList(List<VersionList<?>> backends) {
        this.backends = backends;

        assert (backends.size() >= 1);
    }

    @Override
    public boolean hasType() {
        boolean hasType = backends.get(0).hasType();
        assert (backends.stream().allMatch(versionList -> versionList.hasType() == hasType));
        return hasType;
    }

    @Override
    public CompletableFuture<?> loadAsync() {
        throw new UnsupportedOperationException("MultipleSourceVersionList does not support loading the entire remote version list.");
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        throw new UnsupportedOperationException("MultipleSourceVersionList does not support loading the entire remote version list.");
    }

    @Override
    public CompletableFuture<?> refreshAsync(String gameVersion) {
        versions.clear(gameVersion);
        return CompletableFuture.anyOf(backends.stream()
                .map(versionList -> versionList.refreshAsync(gameVersion)
                .thenRunAsync(() -> {
                    lock.writeLock().lock();

                    try {
                        versions.putAll(gameVersion, versionList.getVersions(gameVersion));
                    } finally {
                        lock.writeLock().unlock();
                    }
                }))
                .toArray(CompletableFuture[]::new));
    }
}
