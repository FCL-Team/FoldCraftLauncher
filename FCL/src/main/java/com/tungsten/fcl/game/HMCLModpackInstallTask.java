package com.tungsten.fcl.game;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.MinecraftInstanceTask;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackInstallTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HMCLModpackInstallTask extends Task<Void> {
    private final File zipFile;
    private final String name;
    private final FCLGameRepository repository;
    private final DefaultDependencyManager dependency;
    private final Modpack modpack;
    private final List<Task<?>> dependencies = new ArrayList<>(1);
    private final List<Task<?>> dependents = new ArrayList<>(4);

    public HMCLModpackInstallTask(Profile profile, File zipFile, Modpack modpack, String name) {
        dependency = profile.getDependency();
        repository = profile.getRepository();
        this.zipFile = zipFile;
        this.name = name;
        this.modpack = modpack;

        File run = repository.getRunDirectory(name);
        File json = repository.getModpackConfiguration(name);
        if (repository.hasVersion(name) && !json.exists())
            throw new IllegalArgumentException("Version " + name + " already exists");

        dependents.add(dependency.gameBuilder().name(name).gameVersion(modpack.getGameVersion()).buildAsync());

        onDone().register(event -> {
            if (event.isFailed()) repository.removeVersionFromDisk(name);
        });

        ModpackConfiguration<Modpack> config = null;
        try {
            if (json.exists()) {
                config = JsonUtils.GSON.fromJson(FileUtils.readText(json), new TypeToken<ModpackConfiguration<Modpack>>() {
                }.getType());

                if (!HMCLModpackProvider.INSTANCE.getName().equals(config.getType()))
                    throw new IllegalArgumentException("Version " + name + " is not a HMCL modpack. Cannot update this version.");
            }
        } catch (JsonParseException | IOException ignore) {
        }
        dependents.add(new ModpackInstallTask<>(zipFile, run, modpack.getEncoding(), Collections.singletonList("/minecraft"), it -> !"pack.json".equals(it), config));
        dependents.add(new MinecraftInstanceTask<>(zipFile, modpack.getEncoding(), Collections.singletonList("/minecraft"), modpack, HMCLModpackProvider.INSTANCE, modpack.getName(), modpack.getVersion(), repository.getModpackConfiguration(name)).withStage("hmcl.modpack"));
    }

    @Override
    public List<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public List<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public void execute() throws Exception {
        String json = CompressingUtils.readTextZipEntry(zipFile, "minecraft/pack.json");
        Version originalVersion = JsonUtils.GSON.fromJson(json, Version.class).setId(name).setJar(null);
        LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(originalVersion);
        Task<Version> libraryTask = Task.supplyAsync(() -> originalVersion);
        // reinstall libraries
        // libraries of Forge and OptiFine should be obtained by installation.
        for (LibraryAnalyzer.LibraryMark mark : analyzer) {
            if (LibraryAnalyzer.LibraryType.MINECRAFT.getPatchId().equals(mark.getLibraryId()))
                continue;
            libraryTask = libraryTask.thenComposeAsync(version -> dependency.installLibraryAsync(modpack.getGameVersion(), version, mark.getLibraryId(), mark.getLibraryVersion()));
        }

        dependencies.add(libraryTask.thenComposeAsync(repository::saveAsync));
    }
}
