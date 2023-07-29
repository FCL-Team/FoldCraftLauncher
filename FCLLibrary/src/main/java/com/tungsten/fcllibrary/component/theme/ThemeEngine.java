package com.tungsten.fcllibrary.component.theme;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ThemeEngine {

    public boolean initialized;
    public static ThemeEngine instance;
    public Handler handler;
    public HashMap<View, Runnable> runnables;
    public Theme theme;

    public ThemeEngine() {

    }

    public static ThemeEngine getInstance() {
        if (instance == null) {
            instance = new ThemeEngine();
        }
        return instance;
    }

    public void setupThemeEngine(Context context) {
        if (!initialized) {
            handler = new Handler();
            theme = Theme.getTheme(context);
            runnables = new HashMap<>();
            initialized = true;
        }
    }

    public void registerEvent(View view, Runnable runnable) {
        runnables.put(view, runnable);
        handler.post(runnable);
    }

    public void unregisterEvent(View view) {
        runnables.remove(view);
    }

    public Theme getTheme() {
        return theme;
    }

    public static boolean isNightMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    public static int getSystemAutoTint(Context context) {
        return isNightMode(context) ? Color.WHITE : Color.BLACK;
    }

    public void applyColor(int color) {
        theme.setColor(color);
        for (View view : runnables.keySet()) {
            if (view != null && runnables.get(view) != null) {
                handler.post(runnables.get(view));
            }
        }
    }

    public void applyFullscreen(Window window, boolean fullscreen) {
        theme.setFullscreen(fullscreen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && window != null) {
            if (fullscreen) {
                window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void applyBackground(Context context, View view, String ltPath, String dkPath) {
        try {
            if (ltPath != null && new File(ltPath).exists()) {
                FileUtils.copyFile(new File(ltPath), new File(FCLPath.LT_BACKGROUND_PATH));
            }
            if (dkPath != null && new File(dkPath).exists()) {
                FileUtils.copyFile(new File(dkPath), new File(FCLPath.DK_BACKGROUND_PATH));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap ltBitmap = !new File(FCLPath.LT_BACKGROUND_PATH).exists() ? ConvertUtils.getBitmapFromRes(context, R.drawable.background_light) : BitmapFactory.decodeFile(FCLPath.LT_BACKGROUND_PATH);
        Bitmap dkBitmap = !new File(FCLPath.DK_BACKGROUND_PATH).exists() ? ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark) : BitmapFactory.decodeFile(FCLPath.DK_BACKGROUND_PATH);
        BitmapDrawable lt = new BitmapDrawable(ltBitmap);
        BitmapDrawable dk = new BitmapDrawable(dkBitmap);
        theme.setBackgroundLt(lt);
        theme.setBackgroundDk(dk);
        boolean isNightMode = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        view.setBackground(isNightMode ? dk : lt);
    }

    public void applyAndSave(Context context, int color) {
        applyColor(color);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave(Context context, Window window, boolean fullscreen) {
        applyFullscreen(window, fullscreen);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave(Context context, View view, String lt, String dk) {
        applyBackground(context, view, lt, dk);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave(Context context, Window window, Theme theme) {
        applyColor(theme.getColor());
        applyFullscreen(window, theme.isFullscreen());
        Theme.saveTheme(context, theme);
    }

}
