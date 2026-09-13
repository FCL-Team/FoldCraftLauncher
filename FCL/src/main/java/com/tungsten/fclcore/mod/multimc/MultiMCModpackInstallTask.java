/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fclcore.mod.multimc;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.GameBuilder;
import com.tungsten.fclcore.download.game.GameAssetDownloadTask;
import com.tungsten.fclcore.download.game.GameDownloadTask;
import com.tungsten.fclcore.download.game.GameLibrariesTask;
import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.MinecraftInstanceTask;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackInstallTask;
import com.tungsten.fclcore.task.GetTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 把 MultiMC 整合包转换为官方启动器格式的安装任务,分为以下阶段:
 *
 * <ul>
 * <li>General Setup:计算校验和并解包 overrides 文件</li>
 * <li>Load Components:解析本地 Json-Patch,准备远程拉取</li>
 * <li>Resolve Json-Patch:拉取远程 Json-Patch 及其依赖</li>
 * <li>Build Artifact:把 Json-Patch 有损合成官方版本格式</li>
 * <li>Copy Embedded Files:拷贝内嵌 libraries 与图标,释放引导库</li>
 * <li>Assemble Game:准备主 jar、libraries、assets 下载</li>
 * <li>Download Game:下载游戏文件</li>
 * <li>Apply JAR mods:把 jarmods 合并进主 jar</li>
 * </ul>
 *
 * @implNote MMC 从未提供完整规范文档,部分行为参照其源码推断,游戏功能可能与 MMC 不完全一致。
 */
public final class MultiMCModpackInstallTask extends Task<MultiMCInstancePatch.ResolvedInstance> {

    private final File zipFile;
    private final Modpack modpack;
    private final MultiMCInstanceConfiguration manifest;
    private final String name;
    private final DefaultGameRepository repository;
    private final DefaultDependencyManager dependencyManager;
    private final List<Task<?>> dependents = new ArrayList<>(4);
    private final List<Task<?>> dependencies = new ArrayList<>(4);

    /** 更新模式下的旧整合包配置,新装为 null */
    private final @Nullable ModpackConfiguration<MultiMCInstanceConfiguration> config;

    public MultiMCModpackInstallTask(DefaultDependencyManager dependencyManager, File zipFile, Modpack modpack, MultiMCInstanceConfiguration manifest, String name) {
        this.zipFile = zipFile;
        this.modpack = modpack;
        this.manifest = manifest;
        this.name = name;
        this.dependencyManager = dependencyManager;
        this.repository = dependencyManager.getGameRepository();

        File json = repository.getModpackConfiguration(name);
        if (repository.hasVersion(name) && !json.exists())
            throw new IllegalArgumentException("Version " + name + " already exists.");

        ModpackConfiguration<MultiMCInstanceConfiguration> config = null;
        try {
            if (json.exists()) {
                config = JsonUtils.GSON.fromJson(FileUtils.readText(json), new TypeToken<ModpackConfiguration<MultiMCInstanceConfiguration>>() {
                }.getType());

                if (!MultiMCModpackProvider.INSTANCE.getName().equals(config.getType()))
                    throw new IllegalArgumentException("Version " + name + " is not a MultiMC modpack. Cannot update this version.");
            }
        } catch (JsonParseException | IOException ignore) {
        }
        this.config = config;

        // 只创建基础 Minecraft 版本,加载器一律由 Json-Patch 合成
        GameBuilder builder = dependencyManager.gameBuilder().name(name).gameVersion(manifest.getGameVersion());
        dependents.add(builder.buildAsync());

        onDone().register(event -> {
            if (event.isFailed())
                repository.removeVersionFromDisk(name);
        });
    }

    @Override
    public boolean doPreExecute() {
        return true;
    }

    @Override
    public void preExecute() throws Exception {
        File run = repository.getRunDirectory(name);

        try (FileSystem fs = CompressingUtils.readonly(zipFile.toPath()).setAutoDetectEncoding(true).setEncoding(modpack.getEncoding()).build()) {
            // Stage 0: General Setup
            String subDirectory;

            // /.minecraft
            if (Files.exists(fs.getPath("/.minecraft"))) {
                subDirectory = "/.minecraft";
                // /minecraft
            } else if (Files.exists(fs.getPath("/minecraft"))) {
                subDirectory = "/minecraft";
                // /[name]/.minecraft
            } else if (Files.exists(fs.getPath("/" + manifest.getName() + "/.minecraft"))) {
                subDirectory = "/" + manifest.getName() + "/.minecraft";
                // /[name]/minecraft
            } else if (Files.exists(fs.getPath("/" + manifest.getName() + "/minecraft"))) {
                subDirectory = "/" + manifest.getName() + "/minecraft";
            } else {
                subDirectory = "/" + manifest.getName() + "/.minecraft";
            }

            dependents.add(new ModpackInstallTask<>(zipFile, run, modpack.getEncoding(), Collections.singletonList(subDirectory), any -> true, config).withStage("fcl.modpack"));
            dependents.add(new MinecraftInstanceTask<>(zipFile, modpack.getEncoding(), Collections.singletonList(subDirectory), manifest, MultiMCModpackProvider.INSTANCE, manifest.getName(), null, repository.getModpackConfiguration(name)).withStage("fcl.modpack"));

            // Stage 1: Load Components
            Path root = MultiMCModpackProvider.getRootPath(fs.getPath("/"));

            List<MultiMCManifest.MultiMCManifestComponent> components = Objects.requireNonNull(
                    Objects.requireNonNull(manifest.getMmcPack(), "mmc-pack.json").getComponents(), "components");
            List<Task<MultiMCInstancePatch>> patches = new ArrayList<>();

            String mcVersion = null;
            for (MultiMCManifest.MultiMCManifestComponent component : components) {
                if (MultiMCComponents.GAME.equals(component.getUid())) {
                    mcVersion = component.getVersion();
                    break;
                }
            }
            if (mcVersion == null) {
                throw new IOException("Cannot load modpacks without Minecraft.");
            }

            for (MultiMCManifest.MultiMCManifestComponent component : components) {
                String componentID = Objects.requireNonNull(component.getUid(), "Component ID");
                Path patchPath = root.resolve(String.format("patches/%s.json", componentID));

                if (Files.exists(patchPath)) {
                    if (!Files.isRegularFile(patchPath)) {
                        throw new IOException("Json-Patch isn't a file: " + componentID);
                    }

                    MultiMCInstancePatch patch = MultiMCInstancePatch.read(componentID, FileUtils.readText(patchPath));
                    patches.add(Task.supplyAsync(() -> patch));
                } else {
                    patches.add(new GetTask(MultiMCComponents.getMetaURL(componentID, component.getVersion(), mcVersion))
                            .thenApplyAsync(s -> MultiMCInstancePatch.read(componentID, s)));
                }
            }
            dependents.add(new MMCInstancePatchesAssembleTask(patches, mcVersion));
        }
    }

    @Override
    public List<Task<?>> getDependents() {
        return dependents;
    }

    @Override
    public void execute() throws Exception {
        // Stage 2-3: Resolve Json-Patch and build artifact
        MMCInstancePatchesAssembleTask assembleTask = null;
        for (Task<?> task : dependents) {
            if (task instanceof MMCInstancePatchesAssembleTask) {
                assembleTask = (MMCInstancePatchesAssembleTask) task;
                break;
            }
        }
        Objects.requireNonNull(assembleTask, "assembleTask");
        MultiMCInstancePatch.ResolvedInstance artifact = MultiMCInstancePatch.resolveArtifact(assembleTask.getResult(), name);

        Version version = repository.readVersionJson(name);
        version = version.addPatch(artifact.getPatch());
        dependencies.add(repository.saveAsync(version));

        // Stage 4: Copy Embedded Files
        try (FileSystem fs = CompressingUtils.readonly(zipFile.toPath()).setAutoDetectEncoding(true).setEncoding(modpack.getEncoding()).build()) {
            Path root = MultiMCModpackProvider.getRootPath(fs.getPath("/"));
            Version instance = new Version(name);

            Path libraries = root.resolve("libraries");
            if (Files.exists(libraries))
                FileUtils.copyDirectory(libraries, repository.getVersionRoot(name).toPath().resolve("libraries"));

            for (Library library : artifact.getPatch().getLibraries()) {
                if ("local".equals(library.getHint())) {
                    // 同一文件按两种布局各存一份:实例 libraries 供 hint=local 解析,maven 路径供标准解析,保证最大兼容
                    Path from = repository.getLibraryFile(instance, library).toPath();
                    Path target = repository.getLibraryFile(instance, library.withoutCommunityFields()).toPath();
                    Files.createDirectories(target.getParent());
                    Files.copy(from, target, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            String iconKey = this.manifest.getIconKey();
            if (iconKey != null) {
                Path iconFile = root.resolve(iconKey + ".png");
                if (Files.exists(iconFile)) {
                    FileUtils.copyFile(iconFile, repository.getVersionRoot(name).toPath().resolve("icon.png"));
                }
            }
        }

        // Stage 5: Assemble Game
        Version resolved = version.resolve(repository);
        dependencies.add(new GameAssetDownloadTask(dependencyManager, version, true, true));
        dependencies.add(new GameLibrariesTask(dependencyManager, resolved, true,
                Lang.merge(resolved.getLibraries(), artifact.getMavenOnlyFiles())));
        dependencies.add(new GameDownloadTask(dependencyManager, artifact.getGameVersion(), version));

        setResult(artifact);
    }

    @Override
    public List<Task<?>> getDependencies() {
        // Stage 6: Download Game
        return dependencies;
    }

    @Override
    public boolean doPostExecute() {
        return true;
    }

    @Override
    public void postExecute() throws Exception {
        MultiMCInstancePatch.ResolvedInstance artifact = Objects.requireNonNull(getResult(), "ResolvedInstance");

        if (!isDependenciesSucceeded()) {
            return;
        }

        List<String> files = artifact.getJarModFileNames();
        if (files.isEmpty()) {
            return;
        }

        // Stage 7: Apply JAR mods
        try (FileSystem fs = CompressingUtils.readonly(zipFile.toPath()).setAutoDetectEncoding(true).setEncoding(modpack.getEncoding()).build()) {
            Path root = MultiMCModpackProvider.getRootPath(fs.getPath("/")).resolve("jarmods");

            try (FileSystem mc = CompressingUtils.writable(repository.getVersionJar(new Version(name)).toPath()).setAutoDetectEncoding(true).build()) {
                for (String fileName : files) {
                    try (FileSystem jm = CompressingUtils.readonly(root.resolve(fileName)).setAutoDetectEncoding(true).build()) {
                        FileUtils.copyDirectory(jm.getPath("/"), mc.getPath("/"));
                    }
                }
            }
        }
    }

    /**
     * 等待全部组件 Json-Patch 就绪,并按 requires 递归补齐缺失组件。
     */
    private static final class MMCInstancePatchesAssembleTask extends Task<List<MultiMCInstancePatch>> {
        private final List<Task<MultiMCInstancePatch>> patches;
        private final String mcVersion;

        public MMCInstancePatchesAssembleTask(List<Task<MultiMCInstancePatch>> patches, String mcVersion) {
            this.patches = patches;
            this.mcVersion = mcVersion;
        }

        @Override
        public Collection<? extends Task<?>> getDependents() {
            return patches;
        }

        @Override
        public void execute() throws Exception {
            Map<String, MultiMCInstancePatch> existed = new LinkedHashMap<>();
            for (Task<MultiMCInstancePatch> patch : patches) {
                MultiMCInstancePatch result = patch.getResult();

                existed.put(result.getID(), result);
            }

            checking:
            while (true) {
                for (MultiMCInstancePatch patch : existed.values()) {
                    for (MultiMCManifest.MultiMCManifestCachedRequires require : patch.getRequires()) {
                        String componentID = require.getUid();
                        if (!existed.containsKey(componentID)) {
                            Task<MultiMCInstancePatch> task = new GetTask(MultiMCComponents.getMetaURL(
                                    componentID, Lang.requireNonNullElse(require.getEqualsVersion(), require.getSuggests()), mcVersion
                            )).thenApplyAsync(s -> MultiMCInstancePatch.read(componentID, s));
                            task.run();

                            MultiMCInstancePatch result = Objects.requireNonNull(task.getResult());
                            existed.put(result.getID(), result);
                            continue checking;
                        }
                    }
                }

                break;
            }

            setResult(new ArrayList<>(existed.values()));
        }
    }
}
