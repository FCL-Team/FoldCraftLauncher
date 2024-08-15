package com.tungsten.fclcore.download.neoforge;

import static com.tungsten.fclcore.util.Lang.wrap;

import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.VersionList;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.HttpRequest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class NeoForgeOfficialVersionList extends VersionList<NeoForgeRemoteVersion> {
    private final DownloadProvider downloadProvider;

    public NeoForgeOfficialVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return false;
    }

    private static final String OLD_URL = "https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/forge";

    private static final String META_URL = "https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/neoforge";

    @Override
    public Optional<NeoForgeRemoteVersion> getVersion(String gameVersion, String remoteVersion) {
        if (gameVersion.equals("1.20.1")) {
            remoteVersion = NeoForgeRemoteVersion.fixInvalidVersion(remoteVersion);
            if (!remoteVersion.equals("47.1.82")) {
                remoteVersion = "1.20.1-" + remoteVersion;
            }
        }
        return super.getVersion(gameVersion, remoteVersion);
    }

    @Override
    public CompletableFuture<?> refreshAsync() {
        return CompletableFuture.supplyAsync(wrap(() -> new OfficialAPIResult[]{
                HttpRequest.GET(downloadProvider.injectURL(OLD_URL)).getJson(OfficialAPIResult.class),
                HttpRequest.GET(downloadProvider.injectURL(META_URL)).getJson(OfficialAPIResult.class)
        })).thenAccept(results -> {
            lock.writeLock().lock();

            try {
                versions.clear();

                for (String version : results[0].versions) {
                    versions.put("1.20.1", new NeoForgeRemoteVersion(
                            "1.20.1", StringUtils.removePrefix(version, "1.20.1-"),
                            Lang.immutableListOf(
                                    downloadProvider.injectURL("https://maven.neoforged.net/releases/net/neoforged/forge/" + version + "/forge-" + version + "-installer.jar")
                            )
                    ));
                }

                for (String version : results[1].versions) {
                    int si1 = version.indexOf('.'), si2 = version.indexOf('.', version.indexOf('.') + 1);
                    String mcVersion = "1." + version.substring(0, Integer.parseInt(version.substring(si1 + 1, si2)) == 0 ? si1 : si2);
                    versions.put(mcVersion, new NeoForgeRemoteVersion(
                            mcVersion, version,
                            Lang.immutableListOf(
                                    downloadProvider.injectURL("https://maven.neoforged.net/releases/net/neoforged/neoforge/" + version + "/neoforge-" + version + "-installer.jar")
                            )
                    ));
                }
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    private static final class OfficialAPIResult {
        private final boolean isSnapshot;

        private final List<String> versions;

        public OfficialAPIResult(boolean isSnapshot, List<String> versions) {
            this.isSnapshot = isSnapshot;
            this.versions = versions;
        }
    }
}