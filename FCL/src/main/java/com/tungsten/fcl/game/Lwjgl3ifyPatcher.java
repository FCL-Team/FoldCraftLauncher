package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.google.gson.JsonObject;
import com.tungsten.fclcore.download.MaintainTask;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * 3.x 的内嵌 version.json 已为各库自带正确的下载地址；仅当个别库缺地址
 * （旧版 lwjgl3ify）时按 groupId 重写 Maven 源兜底，并优先从 jar 内嵌的
 * forgePatches.zip 还原 com.github.GTNewHorizons:lwjgl3ify:*:forgePatches（离线可用）。
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
     * mods 为启动链已扫描的 mod 列表，null 时自行扫描。
     */
    public static void patchIfNeeded(@NotNull FCLGameRepository repository,
                                     @NotNull String versionId,
                                     @Nullable List<LocalModFile> mods,
                                     @NotNull AtomicReference<Version> versionRef) {
        try {
            Version current = versionRef.get();
            LocalModFile lwjgl3ifyMod = findLwjgl3ifyMod(repository, versionId, mods);
            if (lwjgl3ifyMod == null) {
                // RFB 入口残留会让实例无法启动，从备份还原
                restoreIfNeeded(repository, versionId, versionRef);
                return;
            }
            File lwjgl3ifyJar = lwjgl3ifyMod.getFile().toFile();
            LOG.log(Level.INFO, "Detected lwjgl3ify " + lwjgl3ifyMod.getVersion() + " in mods folder");

            Version embedded;
            try (ZipFile zip = new ZipFile(lwjgl3ifyJar)) {
                embedded = readEmbeddedVersionJson(zip);
                if (embedded == null) {
                    LOG.log(Level.WARNING, lwjgl3ifyJar.getName() + " does not contain a valid lwjgl3ify relauncher version.json, skipping");
                    return;
                }
                ensureForgePatchesFile(repository, embedded, current, zip);
            }
            ensureLwjgl3ifyConfig(new File(repository.getRunDirectory(versionId), "config"));

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

    /**
     * 版本仍为 RFB 入口且备份存在时，从备份还原版本 JSON。
     * 还原后按启动链原有流程（resolve + MaintainTask.maintain）重建内存版本对象。
     */
    private static void restoreIfNeeded(FCLGameRepository repository, String versionId, AtomicReference<Version> versionRef) {
        try {
            File json = repository.getVersionJson(versionId);
            File backup = new File(json.getParentFile(), json.getName() + ".before-lwjgl3ify");
            if (!json.isFile() || !backup.isFile()) return;
            Version disk = JsonUtils.fromNonNullJson(FileUtils.readText(json), Version.class);
            if (disk.getMainClass() == null || !disk.getMainClass().startsWith(RFB_MAIN_CLASS_PREFIX)) return;
            java.nio.file.Files.copy(backup.toPath(), json.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            repository.reloadVersionFromDisk(versionId);
            versionRef.set(MaintainTask.maintain(repository, repository.getResolvedVersion(versionId)));
            LOG.log(Level.INFO, "lwjgl3ify no longer present, restored version " + versionId + " from " + backup.getName());
        } catch (Throwable e) {
            LOG.log(Level.WARNING, "Failed to restore version json from lwjgl3ify backup", e);
        }
    }

    /**
     * 从 mod 列表中取启用且版本最高的 lwjgl3ify（按 mcmod.info 的 modid 识别，改名不影响）。
     * mods 为 null 时回退自行扫描。
     */
    @Nullable
    private static LocalModFile findLwjgl3ifyMod(FCLGameRepository repository, String versionId,
                                                 @Nullable List<LocalModFile> mods) throws IOException {
        if (mods == null) mods = repository.getModManager(versionId).getMods();
        LocalModFile result = null;
        for (LocalModFile mod : mods) {
            if (!"lwjgl3ify".equals(mod.getId()) || !mod.isActive()) continue;
            if (result == null || VersionNumber.asVersion(mod.getVersion())
                    .compareTo(VersionNumber.asVersion(result.getVersion())) > 0) {
                result = mod;
            }
        }
        return result;
    }

    /** Angelica 与 lwjgl3ify 同时启用时跳过渲染器版本警告（1.7.10 走 LWJGL3 渲染路径） */
    public static boolean shouldSkipRendererCheck(@Nullable List<LocalModFile> mods) {
        if (mods == null) return false;
        boolean hasAngelica = false, hasLwjgl3ify = false;
        for (LocalModFile mod : mods) {
            if (!mod.isActive()) continue;
            String id = mod.getId();
            if ("angelica".equals(id)) hasAngelica = true;
            else if ("lwjgl3ify".equals(id)) hasLwjgl3ify = true;
        }
        return hasAngelica && hasLwjgl3ify;
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
     * 为缺下载地址的库（旧版 lwjgl3ify）重写 Maven 源，
     * 3.x 内嵌 json 已自带正确地址，此分支基本不触发。
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
     * 优先从内嵌 forgePatches.zip 还原库文件，避免依赖 GTNH Nexus。
     * 写临时文件后原子替换，中断不会留下截断的 jar 遮蔽重解压。
     */
    private static void ensureForgePatchesFile(FCLGameRepository repository, Version embedded, Version version, ZipFile zip) {
        findForgePatchesLibrary(embedded).ifPresent(library -> {
            File target = repository.getLibraryFile(version, library);
            if (target.isFile()) return;
            ZipEntry entry = zip.getEntry(EMBEDDED_FORGE_PATCHES);
            if (entry == null) {
                LOG.log(Level.WARNING, EMBEDDED_FORGE_PATCHES + " not found in " + zip.getName());
                return;
            }
            File parent = target.getParentFile();
            File temp = null;
            try {
                if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Cannot create directory " + parent);
                }
                temp = File.createTempFile(target.getName(), ".tmp", parent);
                try (InputStream is = zip.getInputStream(entry); OutputStream os = new FileOutputStream(temp)) {
                    IOUtils.copyTo(is, os);
                }
                java.nio.file.Files.move(temp.toPath(), target.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                temp = null;
                LOG.log(Level.INFO, "Extracted embedded forgePatches to " + target);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Failed to extract embedded forgePatches, will download from " + GTNH_MAVEN, e);
            } finally {
                if (temp != null && temp.isFile() && !temp.delete()) {
                    LOG.log(Level.WARNING, "Cannot delete temp file " + temp);
                }
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
     * 确保实例 config/lwjgl3ify.cfg 关闭 linuxCreateAppDesktopEntry：
     * lwjgl3ify 默认会在首次初始化时向 XDG_DATA_HOME（或 ~/.local/share）写入桌面快捷方式，
     * Android 上两者均不存在，会抛 RuntimeException 杀死 RFB 主线程导致游戏静默退出
     * （GTNH 官方包自带此配置，自组包需要补上）。
     * 已有 cfg 但缺该键时优先插入已有 window 段内，无该段才在文件尾追加。
     */
    private static void ensureLwjgl3ifyConfig(File configDir) {
        try {
            if (!configDir.isDirectory() && !configDir.mkdirs()) return;
            File cfg = new File(configDir, "lwjgl3ify.cfg");
            final String entry = "B:linuxCreateAppDesktopEntry=false";
            if (!cfg.isFile()) {
                FileUtils.writeText(cfg, "# Configuration file\n\nwindow {\n    " + entry + "\n}\n");
                LOG.log(Level.INFO, "Created lwjgl3ify.cfg with linuxCreateAppDesktopEntry=false");
                return;
            }
            String text = FileUtils.readText(cfg);
            if (containsActiveLine(text, "linuxCreateAppDesktopEntry")) return;
            int insertAt = -1;
            Matcher window = Pattern.compile("(?m)^[ \t]*window\\s*\\{").matcher(text);
            if (window.find()) {
                int lineEnd = text.indexOf('\n', window.end());
                if (lineEnd >= 0) insertAt = lineEnd;
            }
            if (insertAt >= 0) {
                FileUtils.writeText(cfg, text.substring(0, insertAt) + "\n    " + entry + text.substring(insertAt));
                LOG.log(Level.INFO, "Inserted linuxCreateAppDesktopEntry=false into existing window section of lwjgl3ify.cfg");
            } else {
                FileUtils.writeText(cfg, text + "\nwindow {\n    " + entry + "\n}\n");
                LOG.log(Level.INFO, "Appended window section with linuxCreateAppDesktopEntry=false to lwjgl3ify.cfg");
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to ensure lwjgl3ify.cfg", e);
        }
    }

    /** 跳过 # 与 // 注释行后，文本中是否存在含 key 的行 */
    private static boolean containsActiveLine(String text, String key) {
        for (String line : text.split("\n", -1)) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("#") && !trimmed.startsWith("//") && trimmed.contains(key)) return true;
        }
        return false;
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
            boolean dirty = obj.remove("root") != null || obj.remove("patches") != null;
            if (dirty) {
                FileUtils.writeText(target, obj.toString());
                LOG.log(Level.INFO, "Cleaned resolved leftovers (root/patches) from " + target);
            }
        } catch (IOException | com.google.gson.JsonParseException e) {
            LOG.log(Level.WARNING, "Failed to clean version json " + target, e);
        }
    }

    @Nullable
    private static Version readEmbeddedVersionJson(ZipFile zip) {
        try {
            ZipEntry entry = zip.getEntry(EMBEDDED_VERSION_JSON);
            if (entry == null) return null;
            String text = readText(zip.getInputStream(entry));
            return JsonUtils.fromNonNullJson(text, Version.class);
        } catch (IOException | com.google.gson.JsonParseException e) {
            LOG.log(Level.WARNING, "Cannot read lwjgl3ify relauncher version.json from " + zip.getName(), e);
            return null;
        }
    }

    private static String readText(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while ((length = is.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }
        is.close();
        return bos.toString("UTF-8");
    }
}
