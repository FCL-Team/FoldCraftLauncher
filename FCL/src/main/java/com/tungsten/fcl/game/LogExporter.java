package com.tungsten.fcl.game;

import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.Zipper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class LogExporter {
    private LogExporter() {
    }

    public static CompletableFuture<Void> exportLogs(Path zipFile, DefaultGameRepository gameRepository, String versionId, String logs, String launchScript) {
        Path runDirectory = gameRepository.getRunDirectory(versionId).toPath();
        Path baseDirectory = gameRepository.getBaseDirectory().toPath();
        List<String> versions = new ArrayList<>();

        String currentVersionId = versionId;
        HashSet<String> resolvedSoFar = new HashSet<>();
        while (true) {
            if (resolvedSoFar.contains(currentVersionId)) break;
            resolvedSoFar.add(currentVersionId);
            Version currentVersion = gameRepository.getVersion(currentVersionId);
            versions.add(currentVersionId);

            if (StringUtils.isNotBlank(currentVersion.getInheritsFrom())) {
                currentVersionId = currentVersion.getInheritsFrom();
            } else {
                break;
            }
        }

        return CompletableFuture.runAsync(() -> {
            try (Zipper zipper = new Zipper(zipFile)) {
                if (Files.exists(runDirectory.resolve("logs").resolve("debug.log"))) {
                    zipper.putFile(runDirectory.resolve("logs").resolve("debug.log"), "debug.log");
                }
                if (Files.exists(runDirectory.resolve("logs").resolve("latest.log"))) {
                    zipper.putFile(runDirectory.resolve("logs").resolve("latest.log"), "latest.log");
                }
                zipper.putTextFile(Logging.getLogs(), "fcl.log");
                zipper.putTextFile(logs, "minecraft.log");

                for (String id : versions) {
                    Path versionJson = baseDirectory.resolve("versions").resolve(id).resolve(id + ".json");
                    if (Files.exists(versionJson)) {
                        zipper.putFile(versionJson, id + ".json");
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
