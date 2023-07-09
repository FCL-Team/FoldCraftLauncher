package com.tungsten.fclcore.download.game;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.google.gson.JsonParseException;
import com.tungsten.fclcore.download.AbstractDependencyManager;
import com.tungsten.fclcore.game.AssetIndex;
import com.tungsten.fclcore.game.AssetIndexInfo;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * This task is to download asset index file provided in minecraft.json.
 */
public final class GameAssetIndexDownloadTask extends Task<Void> {

    private final AbstractDependencyManager dependencyManager;
    private final Version version;
    private final boolean forceDownloading;
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    /**
     * Constructor.
     *
     * @param dependencyManager the dependency manager that can provides {@link com.tungsten.fclcore.game.GameRepository}
     * @param version the <b>resolved</b> version
     */
    public GameAssetIndexDownloadTask(AbstractDependencyManager dependencyManager, Version version, boolean forceDownloading) {
        this.dependencyManager = dependencyManager;
        this.version = version;
        this.forceDownloading = forceDownloading;
        setSignificance(TaskSignificance.MODERATE);
    }

    @Override
    public List<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() {
        AssetIndexInfo assetIndexInfo = version.getAssetIndex();
        Path assetIndexFile = dependencyManager.getGameRepository().getIndexFile(version.getId(), assetIndexInfo.getId());
        boolean verifyHashCode = StringUtils.isNotBlank(assetIndexInfo.getSha1()) && assetIndexInfo.getUrl().contains(assetIndexInfo.getSha1());

        if (Files.exists(assetIndexFile) && !forceDownloading) {
            // verify correctness of file content
            if (verifyHashCode) {
                try {
                    String actualSum = DigestUtils.digestToString("SHA-1", assetIndexFile);
                    if (actualSum.equalsIgnoreCase(assetIndexInfo.getSha1()))
                        return;
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to calculate sha1sum of file " + assetIndexInfo, e);
                    // continue downloading
                }
            } else {
                try {
                    JsonUtils.fromNonNullJson(FileUtils.readText(assetIndexFile), AssetIndex.class);
                    return;
                } catch (IOException | JsonParseException ignore) {
                }
            }
        }

        // We should not check the hash code of asset index file since this file is not consistent
        // And Mojang will modify this file anytime. So assetIndex.hash might be outdated.
        FileDownloadTask task = new FileDownloadTask(
                dependencyManager.getDownloadProvider().injectURLWithCandidates(assetIndexInfo.getUrl()),
                assetIndexFile.toFile(),
                verifyHashCode ? new FileDownloadTask.IntegrityCheck("SHA-1", assetIndexInfo.getSha1()) : null
        );
        task.setCacheRepository(dependencyManager.getCacheRepository());
        dependencies.add(task);
    }

    public static class GameAssetIndexMalformedException extends IOException {
    }
}
