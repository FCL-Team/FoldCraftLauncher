package com.tungsten.fclcore.mod.server;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.GameBuilder;
import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.mod.MinecraftInstanceTask;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackInstallTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerModpackLocalInstallTask extends Task<Void> {

    private final File zipFile;
    private final Modpack modpack;
    private final ServerModpackManifest manifest;
    private final String name;
    private final DefaultGameRepository repository;
    private final List<Task<?>> dependencies = new ArrayList<>();
    private final List<Task<?>> dependents = new ArrayList<>(4);

    public ServerModpackLocalInstallTask(DefaultDependencyManager dependencyManager, File zipFile, Modpack modpack, ServerModpackManifest manifest, String name) {
        this.zipFile = zipFile;
        this.modpack = modpack;
        this.manifest = manifest;
        this.name = name;
        this.repository = dependencyManager.getGameRepository();
        File run = repository.getRunDirectory(name);

        File json = repository.getModpackConfiguration(name);
        if (repository.hasVersion(name) && !json.exists())
            throw new IllegalArgumentException("Version " + name + " already exists.");

        GameBuilder builder = dependencyManager.gameBuilder().name(name);
        for (ServerModpackManifest.Addon addon : manifest.getAddons()) {
            builder.version(addon.getId(), addon.getVersion());
        }

        dependents.add(builder.buildAsync());
        onDone().register(event -> {
            if (event.isFailed())
                repository.removeVersionFromDisk(name);
        });

        ModpackConfiguration<ServerModpackManifest> config = null;
        try {
            if (json.exists()) {
                config = JsonUtils.GSON.fromJson(FileUtils.readText(json), new TypeToken<ModpackConfiguration<ServerModpackManifest>>() {
                }.getType());

                if (!ServerModpackProvider.INSTANCE.getName().equals(config.getType()))
                    throw new IllegalArgumentException("Version " + name + " is not a Server modpack. Cannot update this version.");
            }
        } catch (JsonParseException | IOException ignore) {
        }
        dependents.add(new ModpackInstallTask<>(zipFile, run, modpack.getEncoding(), Collections.singletonList("/overrides"), any -> true, config).withStage("hmcl.modpack"));
        dependents.add(new MinecraftInstanceTask<>(zipFile, modpack.getEncoding(), Collections.singletonList("/overrides"), manifest, ServerModpackProvider.INSTANCE, modpack.getName(), modpack.getVersion(), repository.getModpackConfiguration(name)).withStage("hmcl.modpack"));
    }

    @Override
    public List<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public List<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public void execute() throws Exception {
    }
}
