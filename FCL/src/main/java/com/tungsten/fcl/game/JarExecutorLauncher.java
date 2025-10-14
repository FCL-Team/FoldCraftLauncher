package com.tungsten.fcl.game;

import android.content.Context;

import com.mio.manager.RendererManager;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.launch.Launcher;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.CommandBuilder;
import com.tungsten.fclcore.util.platform.MemoryUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JarExecutorLauncher extends Launcher {

    private String destJarPath;
    private int javaVersion;

    public JarExecutorLauncher(Context context) {
        super(context);
    }

    public void setInfo(String destJarPath, int javaVersion) {
        this.destJarPath = destJarPath;
        this.javaVersion = javaVersion;
    }

    private CommandBuilder generateCommandLine(String args) {
        CommandBuilder res = new CommandBuilder();

        getCacioJavaArgs(res, javaVersion == 8);

        res.addDefault("-Xms", MemoryUtils.findBestRAMAllocation(context) + "m");
        res.addDefault("-Xmx", MemoryUtils.findBestRAMAllocation(context) + "m");

        res.addDefault("-Duser.home=", Profiles.getSelectedProfile().getGameDir().getParent());
        res.addDefault("-Djava.io.tmpdir=", FCLPath.CACHE_DIR);
        res.addDefault("-Dorg.lwjgl.opengl.libname=", "${gl_lib_name}");

        if (args != null) {
            for (String s : args.split(" ")) {
                res.add(s);
            }
        } else {
            res.add("-jar");
            res.add(destJarPath);
        }
        return res;
    }

    public static void getCacioJavaArgs(CommandBuilder res, boolean isJava8) {
        res.addDefault("-Djava.awt.headless=", "false");
        res.addDefault("-Dcacio.managed.screensize=", FCLBridge.DEFAULT_WIDTH + "x" + FCLBridge.DEFAULT_HEIGHT);
        res.addDefault("-Dcacio.font.fontmanager=", "sun.awt.X11FontManager");
        res.addDefault("-Dcacio.font.fontscaler=", "sun.font.FreetypeFontScaler");
        res.addDefault("-Dswing.defaultlaf=", "javax.swing.plaf.metal.MetalLookAndFeel");
        if (isJava8) {
            res.addDefault("-Dawt.toolkit=", "net.java.openjdk.cacio.ctc.CTCToolkit");
            res.addDefault("-Djava.awt.graphicsenv=", "net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment");
        } else {
            res.addDefault("-Dawt.toolkit=", "com.github.caciocavallosilano.cacio.ctc.CTCToolkit");
            res.addDefault("-Djava.awt.graphicsenv=", "com.github.caciocavallosilano.cacio.ctc.CTCGraphicsEnvironment");
            res.addDefault("-Djava.system.class.loader=", "com.github.caciocavallosilano.cacio.ctc.CTCPreloadClassLoader");

            res.add("--add-exports=java.desktop/java.awt=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/java.awt.peer=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.java2d=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/java.awt.dnd.peer=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt.event=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED");
            res.add("--add-exports=java.desktop/sun.font=ALL-UNNAMED");
            res.add("--add-exports=java.base/sun.security.action=ALL-UNNAMED");
            res.add("--add-opens=java.base/java.util=ALL-UNNAMED");
            res.add("--add-opens=java.desktop/java.awt=ALL-UNNAMED");
            res.add("--add-opens=java.desktop/sun.font=ALL-UNNAMED");
            res.add("--add-opens=java.desktop/sun.java2d=ALL-UNNAMED");
            res.add("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED");
            res.add("--add-opens=java.base/java.net=ALL-UNNAMED");
        }

        StringBuilder cacioClasspath = new StringBuilder();
        cacioClasspath.append("-Xbootclasspath/").append(isJava8 ? "p" : "a");
        File cacioDir = new File(isJava8 ? FCLPath.CACIOCAVALLO_8_DIR : FCLPath.CACIOCAVALLO_17_DIR);
        if (cacioDir.exists() && cacioDir.isDirectory()) {
            for (File file : Objects.requireNonNull(cacioDir.listFiles())) {
                if (file.getName().endsWith(".jar")) {
                    cacioClasspath.append(":").append(file.getAbsolutePath());
                }
            }
        }
        res.add(cacioClasspath.toString());
    }

    @Override
    public FCLBridge launch() throws IOException, InterruptedException {
        return null;
    }

    public FCLBridge launch(String args) throws IOException, InterruptedException {
        final CommandBuilder command = generateCommandLine(args);

        List<String> rawCommandLine = command.asList();

        if (rawCommandLine.stream().anyMatch(StringUtils::isBlank)) {
            throw new IllegalStateException("Illegal command line " + rawCommandLine);
        }

        String[] finalArgs = rawCommandLine.toArray(new String[0]);

        FCLConfig config = new FCLConfig(
                context,
                FCLPath.LOG_DIR,
                javaVersion == 8 ? FCLPath.JAVA_8_PATH : javaVersion == 11 ? FCLPath.JAVA_11_PATH : javaVersion == 17 ? FCLPath.JAVA_17_PATH : FCLPath.JAVA_21_PATH,
                Profiles.getSelectedProfile().getGameDir().getAbsolutePath(),
                RendererManager.RENDERER_GL4ES,
                finalArgs
        );
        return FCLauncher.launchJarExecutor(config);
    }
}
