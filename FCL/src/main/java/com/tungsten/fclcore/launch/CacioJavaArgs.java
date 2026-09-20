package com.tungsten.fclcore.launch;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.platform.CommandBuilder;

import java.io.File;
import java.util.Objects;

/**
 * Cacio（Java AWT 模拟环境）JVM 参数组装，供游戏与 Jar 执行器启动共用
 * <p>
 * 注意：caciocavallo17 资产必须使用 1.18-SNAPSHOT（assets/app_runtime/caciocavallo17，
 * 与 Amethyst 同款），不能升级到 1.19.1+：1.19.1 的 CTCGraphicsEnvironment 构造函数
 * 调用 sun.java2d.SurfaceManagerFactory.setInstance，而该类自 JDK 24 起已被移除，
 * 会导致 CTCGraphicsEnvironment 在 JDK25 上构造抛 NoSuchMethodError，
 * 被 GraphicsEnvironment.createGE 静默吞掉后回退 headless 环境，
 * 最终所有依赖 java.awt.Font 的 mod（如 GTNH 的 Malisis' Doors）在启动时崩溃。
 */
public final class CacioJavaArgs {

    private CacioJavaArgs() {
    }

    public static void addCacioArgs(CommandBuilder res, String screensize, String lookAndFeel, String javaagent, boolean usePreloadClassLoader, boolean isJava8) {
        res.addDefault("-Djava.awt.headless=", "false");
        res.addDefault("-Dcacio.managed.screensize=", screensize);
        res.addDefault("-Dcacio.font.fontmanager=", "sun.awt.X11FontManager");
        res.addDefault("-Dcacio.font.fontscaler=", "sun.font.FreetypeFontScaler");
        res.addDefault("-Dswing.defaultlaf=", lookAndFeel);
        if (isJava8) {
            res.addDefault("-Dawt.toolkit=", "net.java.openjdk.cacio.ctc.CTCToolkit");
            res.addDefault("-Djava.awt.graphicsenv=", "net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment");
        } else {
            res.addDefault("-Dawt.toolkit=", "com.github.caciocavallosilano.cacio.ctc.CTCToolkit");
            res.addDefault("-Djava.awt.graphicsenv=", "com.github.caciocavallosilano.cacio.ctc.CTCGraphicsEnvironment");
            if (usePreloadClassLoader) {
                res.addDefault("-Djava.system.class.loader=", "com.github.caciocavallosilano.cacio.ctc.CTCPreloadClassLoader");
            }
            if (javaagent != null) {
                res.addDefault("-javaagent:", javaagent);
            }
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
}