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
package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Lang.toIterable;
import static com.tungsten.fclcore.util.Pair.pair;

import android.os.Environment;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fclcore.mod.MismatchedModpackTypeException;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.ModpackCompletionException;
import com.tungsten.fclcore.mod.ModpackConfiguration;
import com.tungsten.fclcore.mod.ModpackProvider;
import com.tungsten.fclcore.mod.ModpackUpdateTask;
import com.tungsten.fclcore.mod.UnsupportedModpackException;
import com.tungsten.fclcore.mod.curse.CurseManifest;
import com.tungsten.fclcore.mod.curse.CurseManifestFile;
import com.tungsten.fclcore.mod.curse.CurseModpackProvider;
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackManifest;
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackProvider;
import com.tungsten.fclcore.mod.modrinth.ModrinthManifest;
import com.tungsten.fclcore.mod.modrinth.ModrinthModpackProvider;
import com.tungsten.fclcore.mod.multimc.MultiMCInstanceConfiguration;
import com.tungsten.fclcore.mod.multimc.MultiMCModpackProvider;
import com.tungsten.fclcore.mod.server.ServerModpackManifest;
import com.tungsten.fclcore.mod.server.ServerModpackProvider;
import com.tungsten.fclcore.mod.server.ServerModpackRemoteInstallTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fclcore.util.function.ExceptionalRunnable;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Stream;

public final class ModpackHelper {
    private ModpackHelper() {
    }

    private static final Map<String, ModpackProvider> providers = mapOf(
            pair(CurseModpackProvider.INSTANCE.getName(), CurseModpackProvider.INSTANCE),
            pair(McbbsModpackProvider.INSTANCE.getName(), McbbsModpackProvider.INSTANCE),
            pair(ModrinthModpackProvider.INSTANCE.getName(), ModrinthModpackProvider.INSTANCE),
            pair(MultiMCModpackProvider.INSTANCE.getName(), MultiMCModpackProvider.INSTANCE),
            pair(ServerModpackProvider.INSTANCE.getName(), ServerModpackProvider.INSTANCE),
            pair(HMCLModpackProvider.INSTANCE.getName(), HMCLModpackProvider.INSTANCE)
    );

    @Nullable
    public static ModpackProvider getProviderByType(String type) {
        return providers.get(type);
    }

    /**
     * 本地探测整合包补全任务将自动恢复的缺失文件（镜像各 Provider 补全任务的存在性判定），
     * 供启动前弹窗确认。文件名未知的 Curse 条目（首次补全前无法本地核实）不列入；
     * Mcbbs/Server 补全任务的远端清单更新（下载新增条目、哈希过期覆盖、删除远端已移除条目）不在探测范围。
     * 任何异常一律返回空表，绝不阻断启动。
     * 注意：不能直接用传入 configuration.getManifest()——ModpackConfiguration<?> 通配反序列化
     * 得到的 manifest 是 LinkedTreeMap 而非具体类型，须按类型重读清单。
     */
    public static List<String> getMissingModpackFiles(FCLGameRepository repository, String version, ModpackConfiguration<?> configuration) {
        try {
            List<String> result = new ArrayList<>();
            ModManager modManager = repository.getModManager(version);
            ModpackProvider provider = getProviderByType(configuration.getType());
            File configurationFile = repository.getModpackConfiguration(version);

            if (provider instanceof McbbsModpackProvider) {
                // 与 McbbsModpackCompletionTask 同源：按具体类型重读 modpack.cfg
                ModpackConfiguration<McbbsModpackManifest> typed = JsonUtils.GSON.fromJson(FileUtils.readText(configurationFile),
                        new TypeToken<ModpackConfiguration<McbbsModpackManifest>>() {
                        }.getType());
                if (typed == null || typed.getManifest() == null)
                    return Collections.emptyList();
                McbbsModpackManifest manifest = typed.getManifest();
                // 可更新包（有 fileApi）才会走 phase 1 补全 AddonFile；CurseFile 由 phase 2 两路径都补
                boolean updatable = !StringUtils.isBlank(manifest.getFileApi());
                for (McbbsModpackManifest.File file : manifest.getFiles()) {
                    if (file instanceof McbbsModpackManifest.CurseFile) {
                        String fileName = ((McbbsModpackManifest.CurseFile) file).getFileName();
                        if (StringUtils.isNotBlank(fileName) && !modManager.hasSimpleMod(fileName))
                            result.add(fileName);
                    } else if (updatable && file instanceof McbbsModpackManifest.AddonFile) {
                        String path = ((McbbsModpackManifest.AddonFile) file).getPath();
                        if (!Files.exists(repository.getRunDirectory(version).toPath().resolve(path)))
                            result.add(path);
                    }
                }
            } else if (provider instanceof CurseModpackProvider) {
                // 与 CurseCompletionTask 同源：provider 不传清单，任务只读版本根目录 manifest.json，读不到时任务空转，故不回退 modpack.cfg
                CurseManifest manifest = readJsonFile(new File(repository.getVersionRoot(version), "manifest.json"), CurseManifest.class);
                if (manifest == null)
                    return Collections.emptyList();
                // 资源包/光影包根目录与补全任务 guessFilePath 保持同一表达式（版本根目录）
                File resourcePacksRoot = new File(repository.getVersionRoot(version), "resourcepacks");
                File shaderPacksRoot = new File(repository.getVersionRoot(version), "shaderpacks");
                for (CurseManifestFile file : manifest.getFiles()) {
                    String fileName = file.getFileName();
                    if (StringUtils.isNotBlank(fileName)
                            && !modManager.hasSimpleMod(fileName)
                            && !new File(resourcePacksRoot, fileName).exists()
                            && !new File(shaderPacksRoot, fileName).exists())
                        result.add(fileName);
                }
            } else if (provider instanceof ServerModpackProvider) {
                // 与 ServerModpackCompletionTask 同源：按具体类型重读 modpack.cfg
                ModpackConfiguration<ServerModpackManifest> typed = JsonUtils.GSON.fromJson(FileUtils.readText(configurationFile),
                        new TypeToken<ModpackConfiguration<ServerModpackManifest>>() {
                        }.getType());
                if (typed == null || typed.getManifest() == null
                        || StringUtils.isBlank(typed.getManifest().getFileApi()))
                    // fileApi 为空时补全任务直接空转，视为无需恢复
                    return Collections.emptyList();
                for (ModpackConfiguration.FileInformation file : typed.getManifest().getFiles()) {
                    if (!Files.exists(repository.getVersionRoot(version).toPath().resolve(file.getPath())))
                        result.add(file.getPath());
                }
            } else if (provider instanceof ModrinthModpackProvider) {
                // 与 ModrinthCompletionTask 同源：provider 不传清单，任务只读版本根目录 modrinth.index.json，读不到时任务空转，故不回退 modpack.cfg
                ModrinthManifest manifest = readJsonFile(new File(repository.getVersionRoot(version), "modrinth.index.json"), ModrinthManifest.class);
                if (manifest == null)
                    return Collections.emptyList();
                Path runDirectory = repository.getRunDirectory(version).toPath();
                Path modsDirectory = runDirectory.resolve("mods");
                for (ModrinthManifest.File file : manifest.getFiles()) {
                    if (file.getEnv() != null && file.getEnv().getOrDefault("client", "required").equals("unsupported"))
                        continue;
                    if (file.getDownloads() == null || file.getDownloads().isEmpty())
                        continue;
                    Path filePath = runDirectory.resolve(file.getPath());
                    if (!Files.exists(filePath)) {
                        if (modsDirectory.equals(filePath.getParent()) && modManager.hasSimpleMod(FileUtils.getName(filePath)))
                            continue;
                        result.add(file.getPath());
                    }
                }
            }
            return result;
        } catch (Throwable e) {
            Logging.LOG.log(Level.WARNING, "Unable to detect missing modpack files", e);
            return Collections.emptyList();
        }
    }

    @Nullable
    private static <T> T readJsonFile(File file, Class<T> clazz) {
        try {
            if (!file.exists())
                return null;
            return JsonUtils.GSON.fromJson(FileUtils.readText(file), clazz);
        } catch (IOException | JsonParseException e) {
            return null;
        }
    }

    public static boolean isFileModpackByExtension(File file) {
        String ext = FileUtils.getExtension(file).toLowerCase(Locale.ROOT);
        return "zip".equals(ext) || "mrpack".equals(ext);
    }

    public static Modpack readModpackManifest(Path file, Charset charset) throws UnsupportedModpackException, ManuallyCreatedModpackException {
        String fileName = file.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".7z") || fileName.endsWith(".rar")) {
            throw new ManuallyCreatedModpackException(file);
        }

        try (ZipFile zipFile = CompressingUtils.openZipFile(file, charset)) {
            // Order for trying detecting manifest is necessary here.
            // Do not change to iterating providers.
            for (ModpackProvider provider : new ModpackProvider[]{
                    McbbsModpackProvider.INSTANCE,
                    CurseModpackProvider.INSTANCE,
                    ModrinthModpackProvider.INSTANCE,
                    HMCLModpackProvider.INSTANCE,
                    MultiMCModpackProvider.INSTANCE,
                    ServerModpackProvider.INSTANCE}) {
                try {
                    return provider.readManifest(zipFile, file, charset);
                } catch (Exception ignored) {
                }
            }
        } catch (IOException ignored) {
        }

        try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(file, charset)) {
            findMinecraftDirectoryInManuallyCreatedModpack(file.toString(), fs);
            throw new ManuallyCreatedModpackException(file);
        } catch (Throwable e) {
            // 忽略：文件损坏（含 zipfs 抛 ZipError）或非整合包，统一走 UnsupportedModpackException
        }

        throw new UnsupportedModpackException(file.toString());
    }

    public static Path findMinecraftDirectoryInManuallyCreatedModpack(String modpackName, FileSystem fs) throws IOException, UnsupportedModpackException {
        Path root = fs.getPath("/");
        if (isMinecraftDirectory(root)) return root;
        try (Stream<Path> firstLayer = Files.list(root)) {
            for (Path dir : toIterable(firstLayer)) {
                if (isMinecraftDirectory(dir)) return dir;

                try (Stream<Path> secondLayer = Files.list(dir)) {
                    for (Path subdir : toIterable(secondLayer)) {
                        if (isMinecraftDirectory(subdir)) return subdir;
                    }
                } catch (IOException ignored) {
                }
            }
        } catch (IOException ignored) {
        }
        throw new UnsupportedModpackException(modpackName);
    }

    private static boolean isMinecraftDirectory(Path path) {
        return Files.isDirectory(path.resolve("versions")) &&
                (path.getFileName() == null || ".minecraft".equals(FileUtils.getName(path)));
    }

    public static ModpackConfiguration<?> readModpackConfiguration(File file) throws IOException {
        if (!file.exists())
            throw new FileNotFoundException(file.getPath());
        else
            try {
                return JsonUtils.GSON.fromJson(FileUtils.readText(file), new TypeToken<ModpackConfiguration<?>>() {
                }.getType());
            } catch (JsonParseException e) {
                throw new IOException("Malformed modpack configuration");
            }
    }

    public static Task<?> getInstallTask(Profile profile, ServerModpackManifest manifest, String name, Modpack modpack) {
        profile.getRepository().markVersionAsModpack(name);

        ExceptionalRunnable<?> success = () -> {
            FCLGameRepository repository = profile.getRepository();
            repository.refreshVersions();
            VersionSetting vs = repository.specializeVersionSetting(name);
            repository.undoMark(name);
            if (vs != null)
                vs.setIsolateGameDir(true);
        };

        ExceptionalConsumer<Exception, ?> failure = ex -> {
            if (ex instanceof ModpackCompletionException && !(ex.getCause() instanceof FileNotFoundException)) {
                success.run();
                // This is tolerable and we will not delete the game
            }
        };

        return new ServerModpackRemoteInstallTask(profile.getDependency(), manifest, name)
                .whenComplete(Schedulers.defaultScheduler(), success, failure)
                .withStagesHint(Arrays.asList("fcl.modpack", "fcl.modpack.download"));
    }

    public static boolean isExternalGameNameConflicts(String name) {
        return Files.exists(Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL").resolve(name));
    }

    public static Task<?> getInstallManuallyCreatedModpackTask(Profile profile, File zipFile, String name, Charset charset) {
        if (isExternalGameNameConflicts(name)) {
            throw new IllegalArgumentException("name existing");
        }

        return new ManuallyCreatedModpackInstallTask(profile, zipFile.toPath(), charset, name)
                .thenAcceptAsync(Schedulers.androidUIThread(), location -> {
                    Profile newProfile = new Profile(name, location.toFile());
                    Profiles.addProfile(newProfile);
                    Profiles.setSelectedProfile(newProfile);
                });
    }

    public static Task<?> getInstallTask(Profile profile, File zipFile, String name, Modpack modpack) {
        profile.getRepository().markVersionAsModpack(name);

        ExceptionalRunnable<?> success = () -> {
            FCLGameRepository repository = profile.getRepository();
            repository.refreshVersions();
            VersionSetting vs = repository.specializeVersionSetting(name);
            repository.undoMark(name);
            if (vs != null)
                vs.setIsolateGameDir(true);
        };

        ExceptionalConsumer<Exception, ?> failure = ex -> {
            if (ex instanceof ModpackCompletionException && !(ex.getCause() instanceof FileNotFoundException)) {
                success.run();
                // This is tolerable and we will not delete the game
            }
        };

        if (modpack.getManifest() instanceof MultiMCInstanceConfiguration)
            return modpack.getInstallTask(profile.getDependency(), zipFile, name)
                    .whenComplete(Schedulers.defaultScheduler(), success, failure)
                    .thenComposeAsync(createMultiMCPostInstallTask(profile, (MultiMCInstanceConfiguration) modpack.getManifest(), name));
        else if (modpack.getManifest() instanceof McbbsModpackManifest)
            return modpack.getInstallTask(profile.getDependency(), zipFile, name)
                    .whenComplete(Schedulers.defaultScheduler(), success, failure)
                    .thenComposeAsync(createMcbbsPostInstallTask(profile, (McbbsModpackManifest) modpack.getManifest(), name));
        else
            return modpack.getInstallTask(profile.getDependency(), zipFile, name)
                    .whenComplete(Schedulers.androidUIThread(), success, failure);
    }

    public static Task<Void> getUpdateTask(Profile profile, ServerModpackManifest manifest, Charset charset, String name, ModpackConfiguration<?> configuration) throws UnsupportedModpackException {
        switch (configuration.getType()) {
            case ServerModpackRemoteInstallTask.MODPACK_TYPE:
                return new ModpackUpdateTask(profile.getRepository(), name, new ServerModpackRemoteInstallTask(profile.getDependency(), manifest, name))
                        .withStagesHint(Arrays.asList("fcl.modpack", "fcl.modpack.download"));
            default:
                throw new UnsupportedModpackException();
        }
    }

    public static Task<?> getUpdateTask(Profile profile, File zipFile, Charset charset, String name, ModpackConfiguration<?> configuration) throws UnsupportedModpackException, ManuallyCreatedModpackException, MismatchedModpackTypeException {
        Modpack modpack = ModpackHelper.readModpackManifest(zipFile.toPath(), charset);
        ModpackProvider provider = getProviderByType(configuration.getType());
        if (provider == null) {
            throw new UnsupportedModpackException();
        }
        return provider.createUpdateTask(profile.getDependency(), name, zipFile, modpack);
    }

    public static void toVersionSetting(MultiMCInstanceConfiguration c, VersionSetting vs) {
        vs.setUsesGlobal(false);
        vs.setIsolateGameDir(true);

        if (c.isOverrideMemory()) {
            if (c.getMaxMemory() != null)
                vs.setMaxMemory(c.getMaxMemory());
            vs.setMinMemory(c.getMinMemory());
        }

        if (c.isOverrideJavaArgs()) {
            vs.setJavaArgs(Lang.nonNull(c.getJvmArgs(), ""));
        }
    }

    private static Task<Void> createMultiMCPostInstallTask(Profile profile, MultiMCInstanceConfiguration manifest, String version) {
        return Task.runAsync(Schedulers.androidUIThread(), () -> {
            VersionSetting vs = Objects.requireNonNull(profile.getRepository().specializeVersionSetting(version));
            ModpackHelper.toVersionSetting(manifest, vs);
        });
    }

    private static Task<Void> createMcbbsPostInstallTask(Profile profile, McbbsModpackManifest manifest, String version) {
        return Task.runAsync(Schedulers.androidUIThread(), () -> {
            VersionSetting vs = Objects.requireNonNull(profile.getRepository().specializeVersionSetting(version));
            if (manifest.getLaunchInfo().getMinMemory() > vs.getMaxMemory())
                vs.setMaxMemory(manifest.getLaunchInfo().getMinMemory());
        });
    }
}
