package com.tungsten.fcllibrary.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FCLPath.loadPaths(this);
        ThemeEngine.getInstance().setupThemeEngine(this);
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
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ThemeEngine.getInstance().applyFullscreen(getWindow(), ThemeEngine.getInstance().getTheme().isFullscreen());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ResultListener.onActivityResult(requestCode, resultCode, data);
    }
}
