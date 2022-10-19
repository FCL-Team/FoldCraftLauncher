package com.tungsten.fclcore.mod;

import com.google.gson.JsonParseException;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.game.LaunchOptions;
import com.tungsten.fclcore.task.Task;

import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public interface ModpackProvider {

    String getName();

    Task<?> createCompletionTask(DefaultDependencyManager dependencyManager, String version);

    Task<?> createUpdateTask(DefaultDependencyManager dependencyManager, String name, File zipFile, Modpack modpack) throws MismatchedModpackTypeException;

    /**
     * @param zipFile the opened modpack zip file.
     * @param file the modpack zip file path.
     * @param encoding encoding of zip file.
     * @throws IOException if the file is not a valid zip file.
     * @throws JsonParseException if the manifest.json is missing or malformed.
     * @return the manifest.
     */
    Modpack readManifest(ZipFile zipFile, Path file, Charset encoding) throws IOException, JsonParseException;

    default void injectLaunchOptions(String modpackConfigurationJson, LaunchOptions.Builder builder) {
    }
}
