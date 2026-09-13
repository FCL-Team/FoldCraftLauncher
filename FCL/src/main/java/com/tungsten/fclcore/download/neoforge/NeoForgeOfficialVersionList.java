package com.tungsten.fclcore.download.neoforge;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.tungsten.fclcore.download.ComponentVersionList;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.task.GetTask;
import com.tungsten.fclcore.task.Task;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class NeoForgeOfficialVersionList extends ComponentVersionList<NeoForgeRemoteVersion> {
    private final DownloadProvider downloadProvider;

    public NeoForgeOfficialVersionList(DownloadProvider downloadProvider) {
        this.downloadProvider = downloadProvider;
    }

    @Override
    public boolean hasType() {
        return true;
    }

    private static final String OLD_URL = "https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/forge";
    private static final String META_URL = "https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/neoforge";

    @Override
    public Optional<NeoForgeRemoteVersion> getVersion(String gameVersion, String remoteVersion) {
        if (gameVersion.equals("1.20.1")) {
            remoteVersion = NeoForgeRemoteVersion.normalize(remoteVersion);
        }
        return super.getVersion(gameVersion, remoteVersion);
    }

    @Override
    public Task<?> refreshAsync() {
        return Task.allOf(
                new GetTask(downloadProvider.injectURLWithCandidates(OLD_URL)).thenGetJsonAsync(OfficialAPIResult.class),
                new GetTask(downloadProvider.injectURLWithCandidates(META_URL)).thenGetJsonAsync(OfficialAPIResult.class)
        ).thenAcceptAsync(results -> {
            lock.writeLock().lock();

            try {
                versions.clear();

                // metadata API 结构异常时 versions 组件可能缺失为 null，跳过对应来源防止刷新整体失败
                List<String> oldVersions = ((OfficialAPIResult) results.get(0)).versions == null ? Collections.emptyList() : ((OfficialAPIResult) results.get(0)).versions;
                List<String> metaVersions = ((OfficialAPIResult) results.get(1)).versions == null ? Collections.emptyList() : ((OfficialAPIResult) results.get(1)).versions;

                for (String version : oldVersions) {
                    versions.put("1.20.1", new NeoForgeRemoteVersion(
                            "1.20.1", NeoForgeRemoteVersion.normalize(version),
                            Collections.singletonList(
                                    "https://maven.neoforged.net/releases/net/neoforged/forge/" + version + "/forge-" + version + "-installer.jar"
                            )
                    ));
                }

                for (String version : metaVersions) {
                    String mcVersion;

                    try {
                        int si1 = version.indexOf('.');
                        int si2 = version.indexOf('.', si1 + 1);
                        if (si1 < 0 || si2 < 0) {
                            LOG.warning("Unsupported NeoForge version: " + version);
                            continue;
                        }

                        int majorVersion = Integer.parseInt(version.substring(0, si1));
                        if (majorVersion == 0) { // Snapshot version.
                            mcVersion = version.substring(si1 + 1, si2);
                        } else {
                            if (majorVersion >= 26) {
                                int si3 = version.indexOf('.', si2 + 1);

                                if (si3 < 0) {
                                    LOG.warning("Unsupported NeoForge version: " + version);
                                    continue;
                                }

                                String ver = Integer.parseInt(version.substring(si2 + 1, si3)) == 0
                                        ? version.substring(0, si2)
                                        : version.substring(0, si3);

                                int separator = version.indexOf('+');
                                if (separator < 0)
                                    mcVersion = ver;
                                else
                                    mcVersion = ver + "-" + version.substring(separator + 1);
                            } else {
                                String ver = Integer.parseInt(version.substring(si1 + 1, si2)) == 0
                                        ? version.substring(0, si1)
                                        : version.substring(0, si2);
                                mcVersion = "1." + ver;
                            }
                        }
                    } catch (RuntimeException e) {
                        LOG.warning(String.format("Cannot parse NeoForge version %s for cracking its mc version. ", version) + e);
                        continue;
                    }
                    versions.put(mcVersion, new NeoForgeRemoteVersion(
                            mcVersion, NeoForgeRemoteVersion.normalize(version),
                            Collections.singletonList(
                                    "https://maven.neoforged.net/releases/net/neoforged/neoforge/" + version + "/neoforge-" + version + "-installer.jar"
                            )
                    ));
                }
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    private static final class OfficialAPIResult {
        final boolean isSnapshot;
        final List<String> versions;

        public OfficialAPIResult() {
            this(false, null);
        }

        public OfficialAPIResult(boolean isSnapshot, List<String> versions) {
            this.isSnapshot = isSnapshot;
            this.versions = versions;
        }
    }
}
