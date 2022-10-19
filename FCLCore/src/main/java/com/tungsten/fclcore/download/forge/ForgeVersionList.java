package com.tungsten.fclcore.download.forge;

import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.HttpRequest;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ForgeVersionList extends VersionList<ForgeRemoteVersion> {
    private final DownloadProvider downloadProvider;

    public ForgeVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return false;
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return HttpRequest.GET(downloadProvider.injectURL(FORGE_LIST)).getJsonAsync(ForgeVersionRoot.class)
                .thenAcceptAsync(root -> {
                    lock.writeLock().lock();

                    try {
                        if (root == null)
                            return;
                        versions.clear();

                        for (Map.Entry<String, int[]> entry : root.getGameVersions().entrySet()) {
                            String gameVersion = VersionNumber.normalize(entry.getKey());
                            for (int v : entry.getValue()) {
                                ForgeVersion version = root.getNumber().get(v);
                                if (version == null)
                                    continue;
                                String jar = null;
                                for (String[] file : version.getFiles())
                                    if (file.length > 1 && "installer".equals(file[1])) {
                                        String classifier = version.getGameVersion() + "-" + version.getVersion()
                                                + (StringUtils.isNotBlank(version.getBranch()) ? "-" + version.getBranch() : "");
                                        String fileName = root.getArtifact() + "-" + classifier + "-" + file[1] + "." + file[0];
                                        jar = root.getWebPath() + classifier + "/" + fileName;
                                    }

                                if (jar == null)
                                    continue;
                                versions.put(gameVersion, new ForgeRemoteVersion(
                                        version.getGameVersion(), version.getVersion(), null, Collections.singletonList(jar)
                                ));
                            }
                        }
                    } finally {
                        lock.writeLock().unlock();
                    }
                });
    }

    public static final String FORGE_LIST = "https://files.minecraftforge.net/maven/net/minecraftforge/forge/json";
}
