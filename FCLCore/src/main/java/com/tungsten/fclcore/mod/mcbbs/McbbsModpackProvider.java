package com.tungsten.fclcore.mod.mcbbs;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.mod.MismatchedModpackTypeException;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackProvider;
import com.tungsten.fclcore.mod.ModpackUpdateTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

public final class McbbsModpackProvider implements ModpackProvider {
    public static final McbbsModpackProvider INSTANCE = new McbbsModpackProvider();

    @Override
    public String getName() {
        return "Mcbbs";
    }

    @Override
    public Task<?> createCompletionTask(DefaultDependencyManager dependencyManager, String version) {
        return new McbbsModpackCompletionTask(dependencyManager, version);
    }

    @Override
    public Task<?> createUpdateTask(DefaultDependencyManager dependencyManager, String name, File zipFile, Modpack modpack) throws MismatchedModpackTypeException {
        if (!(modpack.getManifest() instanceof McbbsModpackManifest))
            throw new MismatchedModpackTypeException(getName(), modpack.getManifest().getProvider().getName());

        return new ModpackUpdateTask(dependencyManager.getGameRepository(), name, new McbbsModpackLocalInstallTask(dependencyManager, zipFile, modpack, (McbbsModpackManifest) modpack.getManifest(), name));
    }

    @Override
    public void injectLaunchOptions(String modpackConfigurationJson, LaunchOptions.Builder builder) {
        ModpackConfiguration<McbbsModpackManifest> config = JsonUtils.GSON.fromJson(modpackConfigurationJson, new TypeToken<ModpackConfiguration<McbbsModpackManifest>>() {
        }.getType());

        if (!getName().equals(config.getType())) {
            throw new IllegalArgumentException("Incorrect manifest type, actual=" + config.getType() + ", expected=" + getName());
        }

        config.getManifest().injectLaunchOptions(builder);
    }

    private static Modpack fromManifestFile(InputStream json, Charset encoding) throws IOException, JsonParseException {
        McbbsModpackManifest manifest = JsonUtils.fromNonNullJsonFully(json, McbbsModpackManifest.class);
        return manifest.toModpack(encoding);
    }

    @Override
    public Modpack readManifest(ZipFile zip, Path file, Charset encoding) throws IOException, JsonParseException {
        ZipArchiveEntry mcbbsPackMeta = zip.getEntry("mcbbs.packmeta");
        if (mcbbsPackMeta != null) {
            return fromManifestFile(zip.getInputStream(mcbbsPackMeta), encoding);
        }
        ZipArchiveEntry manifestJson = zip.getEntry("manifest.json");
        if (manifestJson != null) {
            return fromManifestFile(zip.getInputStream(manifestJson), encoding);
        }
        throw new IOException("`mcbbs.packmeta` or `manifest.json` cannot be found");
    }
}
