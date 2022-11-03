package com.tungsten.fcl.game;

import com.google.gson.JsonParseException;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.MismatchedModpackTypeException;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackProvider;
import com.tungsten.fclcore.mod.ModpackUpdateTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;

import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public final class HMCLModpackProvider implements ModpackProvider {
    public static final HMCLModpackProvider INSTANCE = new HMCLModpackProvider();

    @Override
    public String getName() {
        return "HMCL";
    }

    @Override
    public Task<?> createCompletionTask(DefaultDependencyManager dependencyManager, String version) {
        return null;
    }

    @Override
    public Task<?> createUpdateTask(DefaultDependencyManager dependencyManager, String name, File zipFile, Modpack modpack) throws MismatchedModpackTypeException {
        if (!(modpack.getManifest() instanceof HMCLModpackManifest))
            throw new MismatchedModpackTypeException(getName(), modpack.getManifest().getProvider().getName());

        if (!(dependencyManager.getGameRepository() instanceof FCLGameRepository)) {
            throw new IllegalArgumentException("HMCLModpackProvider requires HMCLGameRepository");
        }

        FCLGameRepository repository = (FCLGameRepository) dependencyManager.getGameRepository();
        Profile profile = repository.getProfile();

        return new ModpackUpdateTask(dependencyManager.getGameRepository(), name, new HMCLModpackInstallTask(profile, zipFile, modpack, name));
    }

    @Override
    public Modpack readManifest(ZipFile file, Path path, Charset encoding) throws IOException, JsonParseException {
        String manifestJson = CompressingUtils.readTextZipEntry(file, "modpack.json");
        Modpack manifest = JsonUtils.fromNonNullJson(manifestJson, HMCLModpack.class).setEncoding(encoding);
        String gameJson = CompressingUtils.readTextZipEntry(file, "minecraft/pack.json");
        Version game = JsonUtils.fromNonNullJson(gameJson, Version.class);
        if (game.getJar() == null)
            if (StringUtils.isBlank(manifest.getVersion()))
                throw new JsonParseException("Cannot recognize the game version of modpack " + file + ".");
            else
                manifest.setManifest(HMCLModpackManifest.INSTANCE);
        else
            manifest.setManifest(HMCLModpackManifest.INSTANCE).setGameVersion(game.getJar());
        return manifest;
    }

    private static class HMCLModpack extends Modpack {
        @Override
        public Task<?> getInstallTask(DefaultDependencyManager dependencyManager, File zipFile, String name) {
            return new HMCLModpackInstallTask(((FCLGameRepository) dependencyManager.getGameRepository()).getProfile(), zipFile, this, name);
        }
    }

}
