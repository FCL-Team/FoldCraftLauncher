package com.tungsten.fcl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class FCLApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static FCLApp instance;
    private static WeakReference<Activity> currentActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 在 attachBaseContext 赋值，早于任何 Activity/ContentProvider，保证全局可用
        instance = this;
    }

    @Override
    public void onCreate() {
        // enabledStrictMode();
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
//        PerfUtil.install();
    }

    @NotNull
    public static Context getAppContext() {
        if (instance == null) {
            throw new IllegalStateException("FCLApp is not initialized");
        }
        return instance;
    }

    public static Activity getActivity() {
        if (currentActivity != null) {
            return currentActivity.get();
        }
        return null;
    }

    private void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork()
                .detectCustomSlowCalls()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectActivityLeaks()
                .detectAll()
                .penaltyLog()
                .build());
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (currentActivity != null && currentActivity.get() == activity) {
            currentActivity = null;
        }
    }
}