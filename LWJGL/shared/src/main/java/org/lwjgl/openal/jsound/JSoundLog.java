package org.lwjgl.openal.jsound;

/** 极简日志输出到 stdout/stderr，随游戏日志被启动器一并捕获。 */
final class JSoundLog {

    private JSoundLog() {
    }

    static void info(String msg) {
        System.out.println("[MioJSound] " + msg);
    }

    static void error(String msg) {
        System.err.println("[MioJSound] " + msg);
    }

    static void error(String msg, Throwable t) {
        System.err.println("[MioJSound] " + msg);
        t.printStackTrace();
    }
}