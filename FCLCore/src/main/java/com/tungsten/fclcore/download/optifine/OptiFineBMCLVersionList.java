package com.tungsten.fclcore.download.optifine;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class OptiFineBMCLVersionList extends VersionList<OptiFineRemoteVersion> {
    private final String apiRoot;

    /**
     * @param apiRoot API Root of BMCLAPI implementations
     */
    public OptiFineBMCLVersionList(String apiRoot) {
        this.apiRoot = apiRoot;
    }

    @Override
    public boolean hasType() {
        return true;
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return HttpRequest.GET(apiRoot + "/optifine/versionlist").<List<OptiFineVersion>>getJsonAsync(new TypeToken<List<OptiFineVersion>>() {
        }.getType())
                .thenAcceptAsync(root -> {
                    lock.writeLock().lock();

                    try {
                        versions.clear();
                        Set<String> duplicates = new HashSet<>();
                        for (OptiFineVersion element : root) {
                            String version = element.getType() + "_" + element.getPatch();
                            String mirror = "https://bmclapi2.bangbang93.com/optifine/" + element.getGameVersion() + "/" + element.getType() + "/" + element.getPatch();
                            if (!duplicates.add(mirror))
                                continue;

                            boolean isPre = element.getPatch() != null && (element.getPatch().startsWith("pre") || element.getPatch().startsWith("alpha"));

                            if (StringUtils.isBlank(element.getGameVersion()))
                                continue;

                            String gameVersion = VersionNumber.normalize(element.getGameVersion());
                            versions.put(gameVersion, new OptiFineRemoteVersion(gameVersion, version, Collections.singletonList(mirror), isPre));
                        }
                    } finally {
                        lock.writeLock().unlock();
                    }
                });
    }

}
