package com.tungsten.fcl.util;

import android.content.Context;
import android.system.Os;

import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.utils.Architecture;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.Pack200Utils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

public class RuntimeUtils {

    public static boolean isLatest(String targetDir, String srcDir) throws IOException {
        File targetFile = new File(targetDir + "/version");
        long version = Long.parseLong(IOUtils.readFullyAsString(RuntimeUtils.class.getResourceAsStream(srcDir + "/version"), StandardCharsets.UTF_8));
        return targetFile.exists() && Long.parseLong(FileUtils.readText(targetFile)) == version;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void install(Context context, String targetDir, String srcDir) throws IOException {
        FileUtils.deleteDirectory(new File(targetDir));
        new File(targetDir).mkdirs();
        copyAssets(context, srcDir, targetDir);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void installJava(Context context, String targetDir, String srcDir) throws IOException {
        FileUtils.deleteDirectory(new File(targetDir));
        new File(targetDir).mkdirs();
        String universalPath = srcDir + "/universal.tar.xz";
        String archPath = srcDir + "/bin-" + Architecture.archAsString(Architecture.getDeviceArchitecture()) + ".tar.xz";
        String version = IOUtils.readFullyAsString(RuntimeUtils.class.getResourceAsStream("/assets/" + srcDir + "/version"), StandardCharsets.UTF_8);
        uncompressTarXZ(context.getAssets().open(universalPath), new File(targetDir));
        uncompressTarXZ(context.getAssets().open(archPath), new File(targetDir));
        FileUtils.writeText(new File(targetDir + "/version"), version);
        patchJava(context, targetDir);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copyAssets(Context context, String src, String dest) throws IOException {
        String[] fileNames = context.getAssets().list(src);
        if (fileNames.length > 0) {
            File file = new File(dest);
            if (!file.exists())
                file.mkdirs();
            for (String fileName : fileNames) {
                if (!src.equals("")) {
                    copyAssets(context, src + File.separator + fileName, dest + File.separator + fileName);
                } else {
                    copyAssets(context, fileName, dest + File.separator + fileName);
                }
            }
        } else {
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
        dest.mkdirs();
        TarArchiveInputStream tarIn = new TarArchiveInputStream(new XZCompressorInputStream(tarFileInputStream));
        TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
        while (tarEntry != null) {
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
        if(!dest.exists())
            return;
        String libFolder = FCLauncher.getJreLibDir(javaPath);
        File ftIn = new File(dest, libFolder + "/libfreetype.so.6");
        File ftOut = new File(dest, libFolder + "/libfreetype.so");
        if (ftIn.exists() && (!ftOut.exists() || ftIn.length() != ftOut.length())) {
            ftIn.renameTo(ftOut);
        }
        File fileLib = new File(dest, "/" + libFolder + "/libawt_xawt.so");
        fileLib.delete();
        FileUtils.copyFile(new File(context.getApplicationInfo().nativeLibraryDir, "libawt_xawt.so"), fileLib);
    }

}
