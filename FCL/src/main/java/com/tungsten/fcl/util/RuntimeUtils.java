package com.tungsten.fcl.util;

import android.content.Context;
import android.system.Os;

import com.tungsten.fcl.R;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.utils.Architecture;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.Pack200Utils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.Unzipper;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class RuntimeUtils {

    /**
     * 安装进度回调，回调运行在后台线程，实现方需自行切换到主线程刷新 UI。
     */
    public interface InstallListener {

        /**
         * 正在处理的文件（相对路径、压缩包内条目名等）。
         */
        void onUpdate(String detail);

        /**
         * 进入某个阶段，参数为 R.string 资源 id。
         */
        void onStage(int resId);
    }

    public static boolean isLatest(String targetDir, String srcDir) throws IOException {
        File targetFile = new File(targetDir + "/version");
        try (InputStream stream = RuntimeUtils.class.getResourceAsStream(srcDir + "/version")) {
            if (stream == null) {
                return true;
            }
        }
        if (!targetFile.exists()) return false;
        long version = Long.parseLong(IOUtils.readFullyAsString(RuntimeUtils.class.getResourceAsStream(srcDir + "/version")));
        String installedVersion = FileUtils.readText(targetFile);
        if (installedVersion.isEmpty()) return false;
        return targetFile.exists() && Long.parseLong(installedVersion) == version;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void install(Context context, String targetDir, String srcDir) throws IOException {
        install(context, targetDir, srcDir, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void install(Context context, String targetDir, String srcDir, InstallListener listener) throws IOException {
        FileUtils.deleteDirectory(new File(targetDir));
        new File(targetDir).mkdirs();
        copyAssets(context, srcDir, targetDir, listener);
    }

    public static void installJna(Context context, String targetDir, String srcDir) throws IOException {
        installJna(context, targetDir, srcDir, null);
    }

    public static void installJna(Context context, String targetDir, String srcDir, InstallListener listener) throws IOException {
        FileUtils.deleteDirectory(new File(targetDir));
        new File(targetDir).mkdirs();
        copyAssets(context, srcDir, targetDir, listener);
        File file = new File(FCLPath.JNA_PATH, "jna-arm64.zip");
        new Unzipper(file, new File(FCLPath.RUNTIME_DIR)).setFilter((zipEntry, isDirectory, destFile, entryPath) -> {
            if (listener != null && !isDirectory) {
                listener.onUpdate(entryPath);
            }
            return true;
        }).unzip();
        file.delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void installJava(Context context, String targetDir, String srcDir) throws IOException {
        installJava(context, targetDir, srcDir, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void installJava(Context context, String targetDir, String srcDir, InstallListener listener) throws IOException {
        FileUtils.deleteDirectory(new File(targetDir));
        new File(targetDir).mkdirs();
        String universalPath = srcDir + "/universal.tar.xz";
        String archName = "bin-" + Architecture.archAsString(Architecture.getDeviceArchitecture()) + ".tar.xz";
        String archPath = srcDir + "/" + archName;
        String version = IOUtils.readFullyAsString(RuntimeUtils.class.getResourceAsStream("/assets/" + srcDir + "/version"));
        if (listener != null) {
            listener.onUpdate("universal.tar.xz");
        }
        uncompressTarXZ(context.getAssets().open(universalPath), new File(targetDir), listener);
        if (listener != null) {
            listener.onUpdate(archName);
        }
        uncompressTarXZ(context.getAssets().open(archPath), new File(targetDir), listener);
        FileUtils.writeText(new File(targetDir + "/version"), version);
        if (listener != null) {
            listener.onStage(R.string.splash_runtime_patching);
        }
        patchJava(context, targetDir);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copyAssets(Context context, String src, String dest) throws IOException {
        copyAssets(context, src, dest, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copyAssets(Context context, String src, String dest, InstallListener listener) throws IOException {
        int total = countAssetFiles(context, src);
        AtomicInteger done = new AtomicInteger();
        copyAssetsInternal(context, src, dest, src, total, done, listener);
    }

    private static int countAssetFiles(Context context, String path) throws IOException {
        String[] fileNames = context.getAssets().list(path);
        if (fileNames == null || fileNames.length == 0) {
            return 1;
        }
        int count = 0;
        for (String fileName : fileNames) {
            count += countAssetFiles(context, path.isEmpty() ? fileName : path + "/" + fileName);
        }
        return count;
    }

    private static void copyAssetsInternal(Context context, String src, String dest, String root, int total, AtomicInteger done, InstallListener listener) throws IOException {
        String[] fileNames = context.getAssets().list(src);
        if (fileNames != null && fileNames.length > 0) {
            File file = new File(dest);
            if (!file.exists())
                file.mkdirs();
            for (String fileName : fileNames) {
                copyAssetsInternal(context,
                        src.isEmpty() ? fileName : src + "/" + fileName,
                        dest + File.separator + fileName, root, total, done, listener);
            }
        } else {
            if (listener != null) {
                // 单文件直接复制时（src == root），相对路径取文件名
                String relative = src.equals(root) ? new File(src).getName() : src.substring(root.length() + 1);
                listener.onUpdate(relative + " (" + done.incrementAndGet() + "/" + total + ")");
            }
            File outFile = new File(dest);
            InputStream is = context.getAssets().open(src);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void uncompressTarXZ(final InputStream tarFileInputStream, final File dest) throws IOException {
        uncompressTarXZ(tarFileInputStream, dest, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void uncompressTarXZ(final InputStream tarFileInputStream, final File dest, final InstallListener listener) throws IOException {
        dest.mkdirs();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new XZCompressorInputStream(tarFileInputStream));
        TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
        while (tarEntry != null) {
            if (listener != null && !tarEntry.isDirectory()) {
                listener.onUpdate(tarEntry.getName());
            }
            if (tarEntry.getSize() <= 20480) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ignored) {

                }
            }
            File destPath = new File(dest, tarEntry.getName());
            if (tarEntry.isSymbolicLink()) {
                Objects.requireNonNull(destPath.getParentFile()).mkdirs();
                try {
                    Os.symlink(tarEntry.getLinkName().replace("..", dest.getAbsolutePath()), new File(dest, tarEntry.getName()).getAbsolutePath());
                } catch (Throwable e) {
                    Logging.LOG.log(Level.WARNING, e.getMessage());
                }
            } else if (tarEntry.isDirectory()) {
                destPath.mkdirs();
                destPath.setExecutable(true);
            } else if (!destPath.exists() || destPath.length() != tarEntry.getSize()) {
                Objects.requireNonNull(destPath.getParentFile()).mkdirs();
                destPath.createNewFile();
                FileOutputStream os = new FileOutputStream(destPath);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = tarIn.read(buffer)) != -1) {
                    os.write(buffer, 0, byteCount);
                }
                os.close();
            }
            tarEntry = tarIn.getNextTarEntry();
        }
        tarIn.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void patchJava(Context context, String javaPath) throws IOException {
        Pack200Utils.unpack(context.getApplicationInfo().nativeLibraryDir, javaPath);
        File dest = new File(javaPath);
        if (!dest.exists())
            return;
        String libFolder = FCLauncher.getJavaLibDir(javaPath);
        if (FCLauncher.isJDK8(javaPath)) {
            libFolder = "/jre" + libFolder;
        }
        File ftIn = new File(dest, libFolder + "/libfreetype.so.6");
        File ftOut = new File(dest, libFolder + "/libfreetype.so");
        if (ftIn.exists() && (!ftOut.exists() || ftIn.length() != ftOut.length())) {
            ftIn.renameTo(ftOut);
        }
        ftIn = new File(dest, FCLauncher.getJavaLibDir(javaPath) + "/libfreetype.so");
        if (FCLauncher.isJDK8(javaPath) && ftIn.exists()) {
            ftIn.renameTo(ftOut);
        }
        File fileLib = new File(dest, libFolder + "/libawt_xawt.so");
        fileLib.delete();
        FileUtils.copyFile(new File(context.getApplicationInfo().nativeLibraryDir, "libawt_xawt.so"), fileLib);
    }

}
