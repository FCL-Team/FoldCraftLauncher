package com.tungsten.fcllibrary.component.theme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    public void registerView(View view, Runnable runnable) {
        runnables.put(view, runnable);
        handler.post(runnable);
    }

    public void unregisterView(View view) {
        runnables.remove(view);
    }

    public Theme getTheme() {
        return theme;
    }

    public void applyColor(int color) {
        theme.setColor(color);
        for (View view : runnables.keySet()) {
            if (view != null && runnables.get(view) != null) {
                handler.post(runnables.get(view));
            }
        }
    }

    public void applyFullscreen(Object object, boolean fullscreen) {
        theme.setFullscreen(fullscreen);
        Window window = null;
        if (object instanceof Activity) {
            window = ((Activity) object).getWindow();
        }
        if (object instanceof Dialog) {
            window = ((Dialog) object).getWindow();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && window != null) {
            if (fullscreen) {
                window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            } else {
                window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }

    public void applyAndSave(Context context, int color) {
        applyColor(color);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave(Context context, Object object, boolean fullscreen) {
        applyFullscreen(object, fullscreen);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave(Context context, Object object, Theme theme) {
        applyColor(theme.getColor());
        applyFullscreen(object, theme.isFullscreen());
        Theme.saveTheme(context, theme);
    }

}
