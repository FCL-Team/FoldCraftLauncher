package com.tungsten.fcl.game;

import android.content.Context;
import android.util.Log;

import com.tungsten.fcl.BuildConfig;
import com.tungsten.fclcore.task.Task;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoad;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoadBlocking;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoadConfig;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoadRegistry;
import com.tungsten.verifiedpluginload.model.TrustListRefreshResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Coordinates the one remote trust-list refresh started by MainActivity for this process. */
public final class PluginTrustListSync {
    private static final String TAG = "VerifiedPluginLoad";
    private static final Object LOCK = new Object();
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "VerifiedPluginLoad-startup-refresh");
        thread.setDaemon(true);
        return thread;
    });

    private static CompletableFuture<Void> startupRefresh;

    private PluginTrustListSync() {
    }

    /** Starts the refresh once; calls from recreated activities reuse the same in-flight work. */
    public static void start(Context context) {
        synchronized (LOCK) {
            if (startupRefresh != null) return;
            VerifiedPluginLoad vpl = configure(context.getApplicationContext());
            startupRefresh = CompletableFuture.runAsync(() -> refresh(vpl), EXECUTOR);
        }
    }

    /**
     * Returns a task completed after the startup refresh attempt. A failed refresh still permits
     * verification against the last valid local or built-in list.
     */
    public static Task<Void> awaitStartupRefresh(Context context) {
        start(context);
        CompletableFuture<Void> refresh;
        synchronized (LOCK) {
            refresh = startupRefresh;
        }
        CompletableFuture<Void> completed = refresh.handle((ignored, failure) -> null);
        return Task.fromCompletableFuture(completed);
    }

    private static void refresh(VerifiedPluginLoad vpl) {
        try {
            TrustListRefreshResult result = VerifiedPluginLoadBlocking.refreshTrustList(vpl);
            Log.i(TAG, "Startup trust-list refresh: " + result.getStatus()
                    + ", version=" + result.getTrustListVersion());
        } catch (Exception e) {
            Log.w(TAG, "Startup trust-list refresh failed; using the last valid list", e);
        }
    }

    private static VerifiedPluginLoad configure(Context context) {
        List<String> prefixes = new ArrayList<>();
        for (String prefix : BuildConfig.VPL_TRUST_LIST_URL_PREFIXES) {
            if (prefix != null && !prefix.trim().isEmpty()) prefixes.add(prefix.trim());
        }
        String jsonSuffix = emptyToNull(BuildConfig.VPL_TRUST_LIST_JSON_SUFFIX);
        String signatureSuffix = emptyToNull(BuildConfig.VPL_TRUST_LIST_SIGNATURE_SUFFIX);
        boolean remoteConfigured = !prefixes.isEmpty() && jsonSuffix != null && signatureSuffix != null;
        return VerifiedPluginLoadRegistry.configure(
                context,
                new VerifiedPluginLoadConfig(
                        new File(context.getFilesDir(), "verified-plugin-load"),
                        remoteConfigured ? prefixes : Collections.emptyList(),
                        remoteConfigured ? jsonSuffix : null,
                        remoteConfigured ? signatureSuffix : null,
                        5_000,
                        1_048_576
                )
        );
    }

    private static String emptyToNull(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
