package com.tungsten.fcllibrary.component;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mio.util.DisplayUtil;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fcllibrary.browser.FileBrowserLauncher;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.List;
import java.util.Map;

public class FCLActivity extends AppCompatActivity {
    public FileBrowserLauncher fileLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultLauncher<Intent> activityLauncher;
    private Runnable permissionCallback;
    private ActivityResultCallback<ActivityResult> activityCallback;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeEngine.getInstance().setupThemeEngine(this);
        ThemeEngine.getInstance().applyFullscreen(getWindow(), ThemeEngine.getInstance().getTheme().isFullscreen());
        super.onCreate(savedInstanceState);
        applySavedNightMode();
        DisplayUtil.updateWindowSize(this);
        boolean hasPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hasPermission = Environment.isExternalStorageManager();
        } else {
            hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        if (hasPermission) {
            FCLPath.loadPaths(this);
        }
        fileLauncher = new FileBrowserLauncher(this);
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> o) {
                if (permissionCallback != null) {
                    permissionCallback.run();
                    permissionCallback = null;
                }
            }
        });
        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (activityCallback != null) {
                activityCallback.onActivityResult(result);
                activityCallback = null;
            }
        });
    }

    protected void applySavedNightMode() {
        int themeMode = getSharedPreferences("launcher", MODE_PRIVATE).getInt("themeMode", 0);
        int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        if (themeMode != 0) {
            mode = themeMode == 1 ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public void requestPermissions(String[] permissions, Runnable callback) {
        permissionCallback = callback;
        permissionLauncher.launch(permissions);
    }

    public void startActivityForResult(Intent intent, ActivityResultCallback<ActivityResult> callback) {
        activityCallback = callback;
        activityLauncher.launch(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ThemeEngine.getInstance().applyFullscreen(getWindow(), ThemeEngine.getInstance().getTheme().isFullscreen());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleUtils.setLanguage(base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.setLanguage(this);
        DisplayUtil.refreshDisplayMetrics(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ThemeEngine.getInstance().applyFullscreen(getWindow(), ThemeEngine.getInstance().getTheme().isFullscreen());
    }
}
