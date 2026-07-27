package com.tungsten.fcl.game;

import android.content.Context;

import com.mio.data.Renderer;
import com.tungsten.fcl.R;
import com.tungsten.fclauncher.plugins.DriverPlugin;
import com.tungsten.fclauncher.plugins.FFmpegPlugin;
import com.tungsten.fclauncher.plugins.NativeLibPlugin;
import com.tungsten.fclauncher.plugins.RendererPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Single source of truth for APK plugins inspected by VerifiedPluginLoad. */
public final class PluginCandidateRepository {
    private PluginCandidateRepository() {
    }

    public static List<PluginCandidate> forLaunch(Context context, Renderer renderer) {
        Map<String, PluginCandidate> candidates = new LinkedHashMap<>();
        add(candidates, RendererPlugin.getPluginPackageName(renderer), R.string.plugin_type_renderer);

        DriverPlugin.Driver driver = DriverPlugin.getSelected();
        add(candidates, driver.getPackageName(), R.string.plugin_type_driver);

        for (NativeLibPlugin.NativePlugin plugin : NativeLibPlugin.getPluginList()) {
            add(candidates, plugin.getPackageName(), R.string.plugin_type_native);
        }

        FFmpegPlugin.discover(context);
        if (FFmpegPlugin.isAvailable) {
            add(candidates, FFmpegPlugin.PACKAGE_NAME, R.string.plugin_type_ffmpeg);
        }
        return new ArrayList<>(candidates.values());
    }

    public static List<PluginCandidate> allInstalled(Context context) {
        Map<String, PluginCandidate> candidates = new LinkedHashMap<>();
        for (Renderer renderer : RendererPlugin.getRendererList()) {
            add(candidates, RendererPlugin.getPluginPackageName(renderer), R.string.plugin_type_renderer);
        }
        for (DriverPlugin.Driver driver : DriverPlugin.getDriverList()) {
            add(candidates, driver.getPackageName(), R.string.plugin_type_driver);
        }
        for (NativeLibPlugin.NativePlugin plugin : NativeLibPlugin.getPluginList()) {
            add(candidates, plugin.getPackageName(), R.string.plugin_type_native);
        }
        FFmpegPlugin.discover(context);
        if (FFmpegPlugin.isAvailable) {
            add(candidates, FFmpegPlugin.PACKAGE_NAME, R.string.plugin_type_ffmpeg);
        }
        return new ArrayList<>(candidates.values());
    }

    private static void add(Map<String, PluginCandidate> candidates, String packageName, int typeNameRes) {
        if (packageName == null || packageName.isBlank()) return;
        candidates.putIfAbsent(packageName, new PluginCandidate(packageName, typeNameRes));
    }

    public static final class PluginCandidate {
        private final String packageName;
        private final int typeNameRes;

        private PluginCandidate(String packageName, int typeNameRes) {
            this.packageName = packageName;
            this.typeNameRes = typeNameRes;
        }

        public String getPackageName() {
            return packageName;
        }

        public int getTypeNameRes() {
            return typeNameRes;
        }
    }
}
