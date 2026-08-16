package com.tungsten.fcllibrary.component.theme;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mio.util.ImageUtil;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcl.R;
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

    /** 主题整体刷新监听（非 View 级控件，如主界面背景） */
    private final List<Runnable> refreshListeners = new ArrayList<>();

    public void addRefreshListener(Runnable runnable) {
        refreshListeners.add(runnable);
    }

    public void removeRefreshListener(Runnable runnable) {
        refreshListeners.remove(runnable);
    }

    /** 主题状态变化（如亮暗模式切换）后刷新所有注册控件。
     *  颜色取值（如 [Theme.getColor2]）按当前亮暗模式动态计算，
     *  控件重新执行注册的 runnable 即可切换到亮/暗配色。 */
    public void refreshTheme() {
        if (theme == null) return;
        for (View view : runnables.keySet()) {
            if (view != null && runnables.get(view) != null) {
                handler.post(runnables.get(view));
            }
        }
        for (Runnable runnable : refreshListeners) {
            handler.post(runnable);
        }
        // 触发绑定 color2/color2Dark 的控件（FCLTextView 等走 fakefx 属性绑定，
        // 不在 runnables 里，属性值未变需强制失效才能重新取色）
        theme.refreshColorProperties();
    }

    public Theme getTheme() {
        return theme;
    }

    public static boolean isNightMode(Context context) {
        // FCL 自身的主题模式：0=跟随系统 1=强制亮色 2=强制暗色
        // （AppCompat 强制夜间模式在 configChanges 含 uiMode 时不更新 Resources 配置，
        //  亮暗判断不能只依赖 uiMode，需读自身设置）
        int themeMode = context.getSharedPreferences("launcher", Context.MODE_PRIVATE).getInt("themeMode", 0);
        if (themeMode == 1) return false;
        if (themeMode == 2) return true;
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

    public void applyColor2(int color) {
        theme.setColor2(color);
        for (View view : runnables.keySet()) {
            if (view != null && runnables.get(view) != null) {
                handler.post(runnables.get(view));
            }
        }
    }

    public void applyColor2Dark(int color) {
        theme.setColor2Dark(color);
        for (View view : runnables.keySet()) {
            if (view != null && runnables.get(view) != null) {
                handler.post(runnables.get(view));
            }
        }
    }

    public void applyFullscreen(Window window, boolean fullscreen) {
        theme.setFullscreen(fullscreen);
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams params = window.getAttributes();
                if (fullscreen) {
                    params.layoutInDisplayCutoutMode = WindowManager.LayoutParams. LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES ;
                } else {
                    params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
                }
                window.setAttributes(params);
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
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
        final Bitmap ltBitmap = ImageUtil.load(FCLPath.LT_BACKGROUND_PATH).orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_light));
        final Bitmap dkBitmap = ImageUtil.load(FCLPath.DK_BACKGROUND_PATH).orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark));
        BitmapDrawable lt = new BitmapDrawable(context.getResources(), ltBitmap);
        BitmapDrawable dk = new BitmapDrawable(context.getResources(), dkBitmap);
        theme.setBackgroundLt(lt);
        theme.setBackgroundDk(dk);
        boolean isNightMode = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        ImageUtil.loadInto(view, isNightMode ? dk : lt);
    }

    public void applyAndSave(Context context, int color) {
        applyColor(color);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave2(Context context, int color) {
        applyColor2(color);
        Theme.saveTheme(context, theme);
    }

    public void applyAndSave2Dark(Context context, int color) {
        applyColor2Dark(color);
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

    public static int getWallpaperColor(Context context) {
        int color = Color.parseColor("#7797CF");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            WallpaperColors colors = WallpaperManager.getInstance(context).getWallpaperColors(WallpaperManager.FLAG_SYSTEM);
            if (colors != null) {
                color = colors.getPrimaryColor().toArgb();
            }
        }
        return color;
    }
}
