/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2022  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.download.legacyfabric;

import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.fabric.FabricInstallTask;
import com.tungsten.fclcore.game.Artifact;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.GameComponentType;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.GetTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class LegacyFabricInstallTask extends Task<Version> {

    private final DefaultDependencyManager dependencyManager;
    private final Version version;
    private final LegacyFabricRemoteVersion remote;
    private final GetTask launchMetaTask;
    private final List<Task<?>> dependencies = new ArrayList<>(1);

    public LegacyFabricInstallTask(DefaultDependencyManager dependencyManager, Version version, LegacyFabricRemoteVersion remoteVersion) {
        this.dependencyManager = dependencyManager;
        this.version = version;
        this.remote = remoteVersion;

        launchMetaTask = new GetTask(dependencyManager.getDownloadProvider().injectURLsWithCandidates(remoteVersion.getUrls()));
        launchMetaTask.setCacheRepository(dependencyManager.getCacheRepository());
    }

    @Override
    public boolean doPreExecute() {
        return true;
    }

    @Override
    public Collection<Task<?>> getDependents() {
        return Collections.singleton(launchMetaTask);
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean isRelyingOnDependencies() {
        return false;
    }

    @Override
    public void execute() throws IOException {
        FabricInstallTask.FabricInfo legacyFabricInfo = JsonUtils.GSON.fromJson(launchMetaTask.getResult(), FabricInstallTask.FabricInfo.class);
        if (legacyFabricInfo == null)
            throw new IOException("Legacy Fabric metadata is invalid");

        setResult(getPatch(legacyFabricInfo, remote.getGameVersion(), remote.getSelfVersion()));

        dependencies.add(dependencyManager.checkLibraryCompletionAsync(getResult(), true));
    }

    private Version getPatch(FabricInstallTask.FabricInfo legacyFabricInfo, String gameVersion, String loaderVersion) {
        JsonObject launcherMeta = legacyFabricInfo.getLauncherMeta();
        Arguments arguments = new Arguments();

        String mainClass;
        if (!launcherMeta.get("mainClass").isJsonObject()) {
            mainClass = launcherMeta.get("mainClass").getAsString();
        } else {
            mainClass = launcherMeta.get("mainClass").getAsJsonObject().get("client").getAsString();
        }

        if (launcherMeta.has("launchwrapper")) {
            String clientTweaker = launcherMeta.get("launchwrapper").getAsJsonObject().get("tweakers").getAsJsonObject().get("client").getAsJsonArray().get(0).getAsString();
            arguments = arguments.addGameArguments("--tweakClass", clientTweaker);
        }

        JsonObject librariesObject = launcherMeta.getAsJsonObject("libraries");
        List<Library> libraries = new ArrayList<>();

        // "common, server" is hard coded in fabric installer.
        // Don't know the purpose of ignoring client libraries.
        for (String side : new String[]{"common", "server"}) {
            for (JsonElement element : librariesObject.getAsJsonArray(side)) {
                libraries.add(JsonUtils.GSON.fromJson(element, Library.class));
            }
        }

        libraries.add(new Library(Artifact.fromDescriptor(legacyFabricInfo.getIntermediary().getMaven()), getMavenRepositoryByGroup(legacyFabricInfo.getIntermediary().getMaven()), null));
        libraries.add(new Library(Artifact.fromDescriptor(legacyFabricInfo.getLoader().getMaven()), getMavenRepositoryByGroup(legacyFabricInfo.getLoader().getMaven()), null));

        return new Version(GameComponentType.LEGACY_FABRIC.getPatchId(), loaderVersion, 30000, arguments, mainClass, libraries);
    }

    private static String getMavenRepositoryByGroup(String maven) {
        Artifact artifact = Artifact.fromDescriptor(maven);
        switch (artifact.getGroup()) {
            case "net.legacyfabric":
                return "https://maven.legacyfabric.net/";
            default:
                return "https://maven.fabricmc.net/";
        }
    }
}
