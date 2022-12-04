package com.tungsten.fclauncher;

import android.content.Context;

import java.io.Serializable;

public class FCLConfig implements Serializable {

    public enum Renderer implements Serializable {
        RENDERER_GL4ES("libgl4es.so:libgl4es_egl.so"),
        RENDERER_ZINK("libGL.so:libEGL.so");

        private final String glInfo;

        Renderer(String glInfo) {
            this.glInfo = glInfo;
        }

        public String getGlLibName() {
            return glInfo.split(":")[0];
        }

        public String getEglLibName() {
            return glInfo.split(":")[1];
        }

        public String getGlInfo() {
            return glInfo;
        }
    }

    private final Context context;
    private final String logDir;
    private final String javaPath;
    private final String workingDir;
    private final Renderer renderer;
    private final String[] args;

    public FCLConfig(Context context, String logDir, String javaPath, String workingDir, Renderer renderer, String[] args) {
        this.context = context;
        this.logDir = logDir;
        this.javaPath = javaPath;
        this.workingDir = workingDir;
        this.renderer = renderer;
        this.args = args;
    }

    public Context getContext() {
        return context;
    }

    public String getLogDir() {
        return logDir;
    }

    public String getJavaPath() {
        return javaPath;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public String[] getArgs() {
        return args;
    }

}
