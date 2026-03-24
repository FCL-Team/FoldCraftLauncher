package com.tungsten.fclauncher;

import android.content.Context;

import com.mio.data.Renderer;

import java.io.Serializable;

public class FCLConfig implements Serializable {

    public static class InstalledModLoaders {
        private final boolean installForge;
        private final boolean installCleanroom;
        private final boolean installNeoForge;
        private final boolean installOptiFine;
        private final boolean installLiteLoader;
        private final boolean installFabric;
        private final boolean installQuilt;

        public InstalledModLoaders(boolean installForge,boolean installCleanroom, boolean installNeoForge, boolean installOptiFine, boolean installLiteLoader, boolean installFabric, boolean installQuilt) {
            this.installForge = installForge;
            this.installCleanroom = installCleanroom;
            this.installNeoForge = installNeoForge;
            this.installOptiFine = installOptiFine;
            this.installLiteLoader = installLiteLoader;
            this.installFabric = installFabric;
            this.installQuilt = installQuilt;
        }

        public boolean isInstallForge() {
            return installForge;
        }

        public boolean isInstallCleanroom() {
            return installCleanroom;
        }

        public boolean isInstallNeoForge() {
            return installNeoForge;
        }

        public boolean isInstallOptiFine() {
            return installOptiFine;
        }

        public boolean isInstallLiteLoader() {
            return installLiteLoader;
        }

        public boolean isInstallFabric() {
            return installFabric;
        }

        public boolean isInstallQuilt() {
            return installQuilt;
        }
    }

    private final Context context;
    private final String logDir;
    private final String javaPath;
    private final String workingDir;
    private final Renderer renderer;
    private final String[] args;

    private boolean useVKDriverSystem = false;
    private boolean pojavBigCore = false;
    private InstalledModLoaders installedModLoaders = null;

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

    public void setUseVKDriverSystem(boolean useVKDriverSystem) {
        this.useVKDriverSystem = useVKDriverSystem;
    }

    public boolean isUseVKDriverSystem() {
        return useVKDriverSystem;
    }

    public void setPojavBigCore(boolean pojavBigCore) {
        this.pojavBigCore = pojavBigCore;
    }

    public boolean isPojavBigCore() {
        return pojavBigCore;
    }

    public void setInstalledModLoaders(InstalledModLoaders installedModLoaders) {
        this.installedModLoaders = installedModLoaders;
    }

    public InstalledModLoaders getInstalledModLoaders() {
        return installedModLoaders;
    }
}
