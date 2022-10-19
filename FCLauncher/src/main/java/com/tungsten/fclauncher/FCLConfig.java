package com.tungsten.fclauncher;

import android.content.Context;
import android.view.Surface;

import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

public class FCLConfig {

    public enum Renderer {
        RENDERER_GL4ES("libgl4es.so", "libgl4es_egl.so"),
        RENDERER_ZINK("libGL.so", "libEGL.so");

        private final String glLibName;
        private final String eglLibName;

        Renderer(String glLibName, String eglLibName) {
            this.glLibName = glLibName;
            this.eglLibName = eglLibName;
        }

        public String getGlLibName() {
            return glLibName;
        }

        public String getEglLibName() {
            return eglLibName;
        }
    }

    private final Context context;
    private final Surface surface;
    private final String logDir;
    private final String javaPath;
    private final String workingDir;
    private final Renderer renderer;
    private final String[] args;
    private final FCLBridgeCallback callback;

    public FCLConfig(Context context, Surface surface, String logDir, String javaPath, String workingDir, Renderer renderer, String[] args, FCLBridgeCallback callback) {
        this.context = context;
        this.surface = surface;
        this.logDir = logDir;
        this.javaPath = javaPath;
        this.workingDir = workingDir;
        this.renderer = renderer;
        this.args = args;
        this.callback = callback;
    }

    public Context getContext() {
        return context;
    }

    public Surface getSurface() {
        return surface;
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

    public FCLBridgeCallback getCallback() {
        return callback;
    }

}
