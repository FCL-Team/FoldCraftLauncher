package com.tungsten.fcllibrary.component;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mio.util.DisplayUtil;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.LocaleUtils;

public class FCLActivity extends AppCompatActivity {
    public boolean callback = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeEngine.getInstance().setupThemeEngine(this);
        ThemeEngine.getInstance().applyFullscreen(getWindow(), ThemeEngine.getInstance().getTheme().isFullscreen());
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback) {
            ResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }
}
