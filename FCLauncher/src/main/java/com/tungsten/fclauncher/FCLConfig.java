package com.tungsten.fclauncher;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class FCLConfig implements Serializable {

    public enum Renderer implements Serializable {
        RENDERER_GL4ES("Holy-GL4ES:libgl4es_114.so:libEGL.so"),
        RENDERER_ZINK("VirGLRenderer:libGL.so:libEGL.so"),
        RENDERER_ANGLE("ANGLE:libtinywrapper.so:libEGL_angle.so"),
        RENDERER_VGPU("VGPU:libvgpu.so:libEGL.so");

        private final String glInfo;
        private String glVersion;

        Renderer(String glInfo) {
            this.glInfo = glInfo;
        }

        public String getGlLibName() {
            return glInfo.split(":")[1];
        }

        public String getEglLibName() {
            return glInfo.split(":")[2];
        }

        public String getGlInfo() {
            return glInfo;
        }

        public void setGlVersion(String glVersion) {
            this.glVersion = glVersion;
        }

        public String getGlVersion() {
            return glVersion;
        }

        @NonNull
        @Override
        public String toString() {
            return glInfo.split(":")[0];
        }
    }

    private final Context context;
    private final String logDir;
    private final String javaPath;
    private final String workingDir;
    private final Renderer renderer;
    private final String[] args;
    private final boolean isLwjgl3;

    public FCLConfig(Context context, String logDir, String javaPath, String workingDir, Renderer renderer, String[] args, boolean isLwjgl3) {
        this.context = context;
        this.logDir = logDir;
        this.javaPath = javaPath;
        this.workingDir = workingDir;
        this.renderer = renderer;
        this.args = args;
        this.isLwjgl3 = isLwjgl3;
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

    public boolean isLwjgl3() {
        return isLwjgl3;
    }
}
