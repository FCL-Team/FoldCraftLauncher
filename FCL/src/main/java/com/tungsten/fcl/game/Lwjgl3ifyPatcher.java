package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.google.gson.JsonObject;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GTNH(GT New Horizons) / lwjgl3ify 兼容层（参考 Amethyst-Android 的 LWJGL3ify 支持）。
 * <p>
 * lwjgl3ify 3.x 的 jar 内嵌了一份完整的 version.json（RetroFuturaBootstrap 主类、
 * Java 17+ 的 --add-opens JVM 参数以及 Forge 1.7.10 全量依赖库）。
 * 启动前检测实例 mods 目录中的 lwjgl3ify，将这份 version.json 合并进实例版本并落盘，
 * 使其以 RFB 入口 + Java 17+ 启动，而不是原有的纯净 1.7.10 / Forge 版本。
 * <p>
 * 该 version.json 的库列表未携带下载地址（lwjgl3ify 官方源 libraries.minecraft.net
 * 与 Forge Maven 均不含 com.github.GTNewHorizons 构件），因此同时负责：
 * 1. 为 Forge/Scala/GTNH 系库重写 Maven 下载源；
 * 2. 优先从 lwjgl3ify jar 内嵌的 forgePatches.zip 还原
 *    com.github.GTNewHorizons:lwjgl3ify:*:forgePatches（离线可用）。
 */
public final class Lwjgl3ifyPatcher {

    public static final String RFB_MAIN_CLASS_PREFIX = "com.gtnewhorizons.retrofuturabootstrap";
    public static final String GTNH_MAVEN = "https://nexus.gtnewhorizons.com/repository/public/";
    public static final String FORGE_MAVEN = "https://maven.minecraftforge.net/";
    private static final String EMBEDDED_VERSION_JSON = "me/eigenraven/lwjgl3ify/relauncher/version.json";
    private static final String EMBEDDED_FORGE_PATCHES = "me/eigenraven/lwjgl3ify/relauncher/forgePatches.zip";

    private Lwjgl3ifyPatcher() {
    }

    /**
     * 在启动流程最早处调用（MaintainTask 之后、Java 选择与依赖补全之前）。
     * 若实例的 mods 目录存在 lwjgl3ify 且其内嵌 version.json 符合 RFB 格式，
     * 则改写版本 JSON 并同步内存中的版本对象。
     */
    public static void patchIfNeeded(@NotNull FCLGameRepository repository,
                                     @NotNull String versionId,
                                     @NotNull AtomicReference<Version> versionRef) {
        try {
            Version current = versionRef.get();
            List<File> jars = findLwjgl3ifyJars(new File(repository.getRunDirectory(versionId), "mods"));
            if (jars.isEmpty()) return;

            File lwjgl3ifyJar = jars.get(0);
            if (jars.size() > 1) {
                LOG.log(Level.WARNING, "Multiple lwjgl3ify jars found in mods folder, using " + lwjgl3ifyJar.getName());
            }

            Version embedded = readEmbeddedVersionJson(lwjgl3ifyJar);
            if (embedded == null) {
                LOG.log(Level.WARNING, lwjgl3ifyJar.getName() + " does not contain a valid lwjgl3ify relauncher version.json, skipping");
                return;
            }

            ensureForgePatchesFile(repository, embedded, current, lwjgl3ifyJar);

            if (isPatchedWith(current, embedded)) {
                // 已合并过同一版本，但需确保磁盘 JSON 不含 resolved 版本残留的 root/patches 字段，
                // 否则 resolve() 会走合成根分支丢失 mainClass，导致 isModded 误判
                cleanVersionJsonOnDisk(repository.getVersionJson(versionId));
                return;
            }

            Version patched = patchVersion(current, embedded);
            if (patched == null) return;

            backupOriginalVersionJson(repository.getVersionJson(versionId));
            writeCleanVersionJson(repository.getVersionJson(versionId), patched);
            repository.reloadVersionFromDisk(versionId);
            versionRef.set(patched);
            LOG.log(Level.INFO, "Patched version " + versionId + " with lwjgl3ify from " + lwjgl3ifyJar.getName()
                    + ", mainClass=" + patched.getMainClass()
                    + ", java=" + (patched.getJavaVersion() == null ? null : patched.getJavaVersion().getMajorVersion()));
        } catch (Throwable e) {
            // 兼容失败不应阻断正常启动流程
            LOG.log(Level.WARNING, "Failed to apply lwjgl3ify patch", e);
        }
    }

    @Nullable
    private static Version patchVersion(Version current, Version embedded) {
        String mainClass = embedded.getMainClass();
        // 仅处理 RFB 入口的 version.json（lwjgl3ify 2.x/3.x 的现代格式）
        if (mainClass == null || !mainClass.startsWith(RFB_MAIN_CLASS_PREFIX)) return null;
        Arguments arguments = embedded.getArguments().orElse(null);
        if (arguments == null || arguments.getJvm() == null || arguments.getJvm().isEmpty()) return null;

        Version result = current
                .setMinecraftArguments(null)
                .setArguments(arguments)
                .setMainClass(mainClass);
        if (embedded.getJavaVersion() != null) {
            result = result.setJavaVersion(embedded.getJavaVersion());
        }
        // 内嵌 version.json 的库列表即 1.7.10-Forge-lwjgl3ify 的完整闭包，整体替换
        result = result.setLibraries(patchLibraryUrls(embedded.getLibraries()));
        return result;
    }

    /**
     * 内嵌 version.json 的库没有下载地址，默认落在 libraries.minecraft.net，
     * 而 Forge/Scala 与 GTNH 系构件并不存在于该源，需要重写为正确的 Maven 源。
     */
    private static List<Library> patchLibraryUrls(List<Library> libraries) {
        List<Library> result = new ArrayList<>(libraries.size());
        for (Library library : libraries) {
            if (library.hasDownloadURL()) {
                result.add(library);
                continue;
            }
            String base = resolveLibraryBase(library);
            if (base == null) {
                result.add(library);
            } else {
                result.add(withUrl(library, base));
            }
        }
        return result;
    }

    @Nullable
    private static String resolveLibraryBase(Library library) {
        String groupId = library.getGroupId();
        if (groupId.startsWith("com.github.GTNewHorizons")) {
            return GTNH_MAVEN;
        }
        // 1.7.10 Forge 传递依赖（Forge/Scala/Akka）托管在 Forge Maven
        if (groupId.startsWith("net.minecraftforge")
                || groupId.startsWith("org.scala-lang")
                || groupId.startsWith("com.typesafe")) {
            return FORGE_MAVEN;
        }
        return null; // 其余库 libraries.minecraft.net 均可命中
    }

    private static Library withUrl(Library library, String url) {
        JsonObject obj = JsonUtils.GSON.toJsonTree(library).getAsJsonObject();
        obj.addProperty("url", url);
        return JsonUtils.GSON.fromJson(obj, Library.class);
    }

    /**
     * 优先从 lwjgl3ify jar 内嵌的 forgePatches.zip 还原库文件，避免依赖 GTNH Nexus。
     */
    private static void ensureForgePatchesFile(FCLGameRepository repository, Version embedded, Version version, File lwjgl3ifyJar) {
        findForgePatchesLibrary(embedded).ifPresent(library -> {
            File target = repository.getLibraryFile(version, library);
            if (target.isFile()) return;
            try (ZipFile zip = new ZipFile(lwjgl3ifyJar)) {
                ZipEntry entry = zip.getEntry(EMBEDDED_FORGE_PATCHES);
                if (entry == null) {
                    LOG.log(Level.WARNING, EMBEDDED_FORGE_PATCHES + " not found in " + lwjgl3ifyJar.getName());
                    return;
                }
                File parent = target.getParentFile();
                if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Cannot create directory " + parent);
                }
                try (InputStream is = zip.getInputStream(entry); OutputStream os = new FileOutputStream(target)) {
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
                LOG.log(Level.INFO, "Extracted embedded forgePatches to " + target);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to extract embedded forgePatches, will download from " + GTNH_MAVEN, e);
            }
        });
    }

    private static Optional<Library> findForgePatchesLibrary(Version version) {
        if (version.getLibraries() == null) return Optional.empty();
        return version.getLibraries().stream()
                .filter(library -> "com.github.GTNewHorizons".equals(library.getGroupId())
                        && "lwjgl3ify".equals(library.getArtifactId())
                        && "forgePatches".equals(library.getClassifier()))
                .findFirst();
    }

    /**
     * 当前版本是否已经合并过同版本的 lwjgl3ify version.json。
     */
    private static boolean isPatchedWith(Version current, Version embedded) {
        String mainClass = current.getMainClass();
        if (mainClass == null || !mainClass.startsWith(RFB_MAIN_CLASS_PREFIX)) return false;
        return findForgePatchesLibrary(embedded)
                .map(library -> Objects.equals(library.getName(), getCurrentForgePatchesName(current)))
                .orElse(false);
    }

    @Nullable
    private static String getCurrentForgePatchesName(Version current) {
        return current.getLibraries().stream()
                .filter(library -> "com.github.GTNewHorizons".equals(library.getGroupId())
                        && "lwjgl3ify".equals(library.getArtifactId())
                        && "forgePatches".equals(library.getClassifier()))
                .map(Library::getName)
                .findFirst().orElse(null);
    }

    private static List<File> findLwjgl3ifyJars(File modsDir) {
        List<File> result = new ArrayList<>();
        File[] files = modsDir.listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".jar"));
        if (files == null) return result;
        for (File file : files) {
            if (file.getName().toLowerCase().startsWith("lwjgl3ify-")) {
                result.add(file);
            }
        }
        // 存在多个时取版本号最高的一个
        result.sort((a, b) -> VersionNumber.asVersion(versionFromFileName(b))
                .compareTo(VersionNumber.asVersion(versionFromFileName(a))));
        return result;
    }

    private static String versionFromFileName(File jar) {
        String name = jar.getName();
        String lower = name.toLowerCase();
        if (lower.startsWith("lwjgl3ify-") && lower.endsWith(".jar")) {
            return name.substring("lwjgl3ify-".length(), name.length() - ".jar".length());
        }
        return "0.0";
    }

    /**
     * 首次合并前备份原版本 JSON（仅当备份不存在时创建，不覆盖已有备份），
     * 便于用户在移除 lwjgl3ify 或合并不符合预期时手工恢复。
     */
    private static void backupOriginalVersionJson(File target) {
        try {
            File backup = new File(target.getParentFile(), target.getName() + ".before-lwjgl3ify");
            if (target.isFile() && !backup.exists()) {
                java.nio.file.Files.copy(target.toPath(), backup.toPath());
                LOG.log(Level.INFO, "Backed up original version json to " + backup.getName());
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to back up version json " + target, e);
        }
    }

    /**
     * 将版本写入磁盘并清除 resolved/maintained 版本对象携带的
     * {@code root}/{@code patches} 字段：它们是内存合并的内部状态，
     * 一旦写入 JSON 会让 {@code resolve()} 走合成根分支丢弃 mainClass 等关键字段。
     */
    private static void writeCleanVersionJson(File target, Version version) throws IOException {
        JsonObject obj = JsonUtils.GSON.toJsonTree(version).getAsJsonObject();
        obj.remove("root");
        obj.remove("patches");
        obj.remove("hidden");
        FileUtils.writeText(target, obj.toString());
    }

    /**
     * 清理已存在磁盘版本 JSON 中的 resolved 残留字段（旧版本补丁产物）。
     */
    private static void cleanVersionJsonOnDisk(File target) {
        try {
            if (!target.isFile()) return;
            JsonObject obj = JsonUtils.GSON.fromJson(FileUtils.readText(target), JsonObject.class);
            if (obj == null) return;
            boolean dirty = obj.remove("root") != null || obj.remove("patches") != null || obj.remove("hidden") != null;
            if (dirty) {
                FileUtils.writeText(target, obj.toString());
                LOG.log(Level.INFO, "Cleaned resolved leftovers (root/patches) from " + target);
            }
        } catch (IOException | com.google.gson.JsonParseException e) {
            LOG.log(Level.WARNING, "Failed to clean version json " + target, e);
        }
    }

    @Nullable
    private static Version readEmbeddedVersionJson(File lwjgl3ifyJar) {
        try (ZipFile zip = new ZipFile(lwjgl3ifyJar)) {
            ZipEntry entry = zip.getEntry(EMBEDDED_VERSION_JSON);
            if (entry == null) return null;
            String text = readText(zip.getInputStream(entry));
            return JsonUtils.fromNonNullJson(text, Version.class);
        } catch (IOException | com.google.gson.JsonParseException e) {
            LOG.log(Level.WARNING, "Cannot read lwjgl3ify relauncher version.json from " + lwjgl3ifyJar, e);
            return null;
        }
    }

    private static String readText(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while ((length = is.read(buffer)) > 0) {
            bos.write(buffer, 0, length);
        }
        is.close();
        return bos.toString("UTF-8");
    }
}
