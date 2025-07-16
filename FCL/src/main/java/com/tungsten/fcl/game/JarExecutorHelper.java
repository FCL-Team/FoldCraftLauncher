package com.tungsten.fcl.game;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.mio.JavaManager;
import com.mio.manager.RendererManager;
import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fcl.control.MenuType;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.game.JavaVersion;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.FCLActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarExecutorHelper {

    public static void start(FCLActivity activity, Context context) {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".jar");
        FileBrowser.Builder builder = new FileBrowser.Builder(context);
        builder.setInitDir(Environment.getExternalStorageDirectory().getAbsolutePath());
        builder.setLibMode(LibMode.FILE_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        builder.setSuffix(suffix);
        builder.create().browse(activity, RequestCodes.SELECT_MANUAL_INSTALLER_CODE, ((requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_MANUAL_INSTALLER_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String path = FileBrowser.getSelectedFiles(data).get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    path = AndroidUtils.copyFileToDir(activity, uri, new File(FCLPath.CACHE_DIR));
                }
                if (new File(path).exists()) {
                    launchJarExecutor(context, new File(path));
                }
            }
        }));
    }

    private static void launchJarExecutor(Context context, File file) {
        exec(context, file, getJava(file), null);
    }

    public static void exec(Context context, File file, int javaVersion, String args) {
        JarExecutorLauncher launcher = new JarExecutorLauncher(context);
        launcher.setInfo(file == null ? null : file.getAbsolutePath(), javaVersion);
        try {
            FCLBridge fclBridge = launcher.launch(args);
            Intent intent = new Intent(context, JVMActivity.class);
            fclBridge.setScaleFactor(1f);
            fclBridge.setController(null);
            fclBridge.setGameDir(null);
            fclBridge.setRenderer(RendererManager.RENDERER_GL4ES.getName());
            fclBridge.setJava(javaVersion + "");
            JVMActivity.setFCLBridge(fclBridge, MenuType.JAR_EXECUTOR);
            LOG.log(Level.INFO, "Start JVMActivity!");
            context.startActivity(intent);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getJava(File file) {
        int javaVersion = JavaVersion.JAVA_VERSION_8;
        if (file != null) {
            int version = getJavaVersion(file);
            javaVersion = getNearestJavaVersion(version);
        }
        Profile profile = Profiles.getSelectedProfile();
        if (profile != null) {
            String java = profile.getGlobal().getJava();
            if (!java.equals(JavaVersion.JAVA_AUTO.getName())) {
                javaVersion = JavaManager.getJavaFromVersionName(java).getVersion();
            }
        }
        return javaVersion;
    }

    private static int getNearestJavaVersion(int majorVersion) {
        if (majorVersion > JavaVersion.JAVA_VERSION_17)
            return JavaVersion.JAVA_VERSION_21;
        if (majorVersion > JavaVersion.JAVA_VERSION_11)
            return JavaVersion.JAVA_VERSION_17;
        if (majorVersion > JavaVersion.JAVA_VERSION_8)
            return JavaVersion.JAVA_VERSION_11;
        return JavaVersion.JAVA_VERSION_8;
    }

    private static int getJavaVersion(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry manifest = zipFile.getEntry("META-INF/MANIFEST.MF");
            if (manifest == null)
                return -1;

            String manifestString = IOUtils.readFullyAsStringWithClosing(zipFile.getInputStream(manifest));
            String mainClass = extractUntilCharacter(manifestString, "Main-Class:", '\n');
            if (mainClass == null)
                return -1;

            mainClass = mainClass.trim().replace('.', '/') + ".class";
            ZipEntry mainClassFile = zipFile.getEntry(mainClass);
            if (mainClassFile == null)
                return -1;

            InputStream classStream = zipFile.getInputStream(mainClassFile);
            byte[] bytesWeNeed = new byte[8];
            int readCount = classStream.read(bytesWeNeed);
            classStream.close();
            if (readCount < 8)
                return -1;

            ByteBuffer byteBuffer = ByteBuffer.wrap(bytesWeNeed);
            if (byteBuffer.getInt() != 0xCAFEBABE)
                return -1;
            short minorVersion = byteBuffer.getShort();
            short majorVersion = byteBuffer.getShort();
            Log.i("JavaGUILauncher", majorVersion + ", " + minorVersion);
            return classVersionToJavaVersion(majorVersion);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int classVersionToJavaVersion(int majorVersion) {
        if (majorVersion < 46)
            return 2;
        return majorVersion - 44;
    }

    public static String extractUntilCharacter(String input, String whatFor, char terminator) {
        int whatForStart = input.indexOf(whatFor);
        if (whatForStart == -1)
            return null;
        whatForStart += whatFor.length();
        int terminatorIndex = input.indexOf(terminator, whatForStart);
        if (terminatorIndex == -1)
            return null;
        return input.substring(whatForStart, terminatorIndex);
    }
}
