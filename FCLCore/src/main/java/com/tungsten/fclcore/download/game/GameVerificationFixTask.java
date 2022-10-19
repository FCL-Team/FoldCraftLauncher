package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Remove class digital verification file in game jar
 */
public final class GameVerificationFixTask extends Task<Void> {
    private final DefaultDependencyManager dependencyManager;
    private final String gameVersion;
    private final Version version;
    private final List<Task<?>> dependencies = new ArrayList<>();

    public GameVerificationFixTask(DefaultDependencyManager dependencyManager, String gameVersion, Version version) {
        this.dependencyManager = dependencyManager;
        this.gameVersion = gameVersion;
        this.version = version;

        if (!version.isResolved()) {
            throw new IllegalArgumentException("GameVerificationFixTask requires a resolved game version");
        }

        setSignificance(TaskSignificance.MODERATE);
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() throws IOException {
        File jar = dependencyManager.getGameRepository().getVersionJar(version);
        LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(version);

        if (jar.exists() && VersionNumber.VERSION_COMPARATOR.compare(gameVersion, "1.6") < 0 && analyzer.has(LibraryAnalyzer.LibraryType.FORGE)) {
            try (FileSystem fs = CompressingUtils.createWritableZipFileSystem(jar.toPath(), StandardCharsets.UTF_8)) {
                Files.deleteIfExists(fs.getPath("META-INF/MOJANG_C.DSA"));
                Files.deleteIfExists(fs.getPath("META-INF/MOJANG_C.SF"));
            }
        }
    }
    
}
