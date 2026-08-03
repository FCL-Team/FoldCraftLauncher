package com.tungsten.fclauncher.plugins;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** VPL-independent snapshot of an approved native plugin load. */
public final class NativePluginAuthorization {
    private final String packageName;
    private final String apkPath;
    private final String nativeLibraryDirectory;
    private final Long versionCode;
    private final Set<String> currentSignatures;

    public NativePluginAuthorization(
            String packageName,
            String apkPath,
            String nativeLibraryDirectory,
            Long versionCode,
            Set<String> currentSignatures
    ) {
        this.packageName = packageName;
        this.apkPath = apkPath;
        this.nativeLibraryDirectory = nativeLibraryDirectory;
        this.versionCode = versionCode;
        this.currentSignatures = Collections.unmodifiableSet(new LinkedHashSet<>(currentSignatures));
    }

    public String getPackageName() {
        return packageName;
    }

    public String getApkPath() {
        return apkPath;
    }

    public String getNativeLibraryDirectory() {
        return nativeLibraryDirectory;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public Set<String> getCurrentSignatures() {
        return currentSignatures;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof NativePluginAuthorization)) return false;
        NativePluginAuthorization that = (NativePluginAuthorization) other;
        return Objects.equals(packageName, that.packageName)
                && Objects.equals(apkPath, that.apkPath)
                && Objects.equals(nativeLibraryDirectory, that.nativeLibraryDirectory)
                && Objects.equals(versionCode, that.versionCode)
                && Objects.equals(currentSignatures, that.currentSignatures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, apkPath, nativeLibraryDirectory, versionCode, currentSignatures);
    }
}
