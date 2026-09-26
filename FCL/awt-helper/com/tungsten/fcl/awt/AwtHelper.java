package com.tungsten.fcl.awt;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.Map;
import java.util.concurrent.Exchanger;
import javax.swing.JOptionPane;

/**
 * 游戏进程内 AWT 辅助入口（经 -Xbootclasspath/a 注入游戏 JVM）
 *
 * 重新构建：用 JDK17 执行
 * javac --release 8 -cp cacio-tta-1.19.1-SNAPSHOT.jar -d out com/tungsten/fcl/awt/AwtHelper.java
 * jar cf fcl-awt-helper.jar -C out .
 */
public final class AwtHelper {

    private static final Exchanger<int[]> exchanger = new Exchanger<int[]>();

    private static volatile Thread monitorThread;

    private AwtHelper() {
    }

    /**
     * 在 EDT 上弹出消息对话框，调用方立即返回。
     * EDT 由 Java 线程首次触达 AWT 时启动，JNI 附加线程触达不会启动 EDT，
     * 因此必须用挂到 main 线程组的 Java 线程承载对话框。
     */
    public static void showMessageDialog(final String title, final String message) {
        Thread dialogThread = new Thread(findMainThreadGroup(), new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            }
        }, "FCL-AWT-Dialog");
        dialogThread.setDaemon(true);
        dialogThread.start();
    }

    /**
     * 取一份 AWT 虚拟屏快照：[screenW, screenH, count, x1, y1, w1, h1, ..., rgb...]，
     * 无可见窗口时返回 null。实际枚举与取帧由 main 线程组的 monitor 线程完成
     * （Window.getWindows 按线程上下文隔离，JNI 附加线程看不到对话框所在上下文的窗口）。
     */
    public static int[] pollAwtScreen() {
        ensureMonitor();
        try {
            return exchanger.exchange(new int[0]);
        } catch (InterruptedException e) {
            return null;
        }
    }

    private static synchronized void ensureMonitor() {
        if (monitorThread != null && monitorThread.isAlive()) {
            return;
        }
        Thread thread = new Thread(findMainThreadGroup(), new Runnable() {
            @Override
            public void run() {
                monitorLoop();
            }
        }, "FCL-AWT-Monitor");
        thread.setDaemon(true);
        thread.start();
        monitorThread = thread;
    }

    private static void monitorLoop() {
        while (true) {
            try {
                int[] frame = computeFrame();
                if (frame == null) {
                    frame = new int[0];
                }
                exchanger.exchange(frame);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private static int[] computeFrame() {
        try {
            int count = 0;
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window.isVisible()) {
                    count++;
                }
            }
            if (count == 0) {
                return null;
            }
            java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int screenW = (int) screen.getWidth();
            int screenH = (int) screen.getHeight();
            if (screenW <= 0 || screenH <= 0) {
                System.out.println("[AWT] invalid screen size " + screenW + "x" + screenH);
                return null;
            }
            int[] rgb = com.github.caciocavallosilano.cacio.ctc.CTCScreen.getCurrentScreenRGB();
            if (rgb == null || rgb.length < screenW * screenH) {
                return null;
            }
            int[] out = new int[3 + count * 4 + screenW * screenH];
            out[0] = screenW;
            out[1] = screenH;
            out[2] = count;
            int index = 3;
            for (Window window : windows) {
                if (!window.isVisible()) {
                    continue;
                }
                Rectangle bounds = window.getBounds();
                out[index++] = bounds.x;
                out[index++] = bounds.y;
                out[index++] = bounds.width;
                out[index++] = bounds.height;
            }
            System.arraycopy(rgb, 0, out, index, screenW * screenH);
            return out;
        } catch (Throwable t) {
            System.out.println("[AWT] poll failed: " + t);
            return null;
        }
    }

    private static ThreadGroup findMainThreadGroup() {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if ("main".equals(thread.getName())) {
                return thread.getThreadGroup();
            }
        }
        return Thread.currentThread().getThreadGroup();
    }

    /**
     * 打印游戏 JVM 全部线程的状态与栈顶帧到 stdout
     */
    public static void dumpAllStacks() {
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            Thread thread = entry.getKey();
            StringBuilder builder = new StringBuilder("[AWT-DUMP] " + thread.getName() + " state=" + thread.getState());
            StackTraceElement[] frames = entry.getValue();
            for (int i = 0; i < frames.length && i < 8; i++) {
                builder.append("\n[AWT-DUMP]   at ").append(frames[i]);
            }
            System.out.println(builder);
        }
    }
}
