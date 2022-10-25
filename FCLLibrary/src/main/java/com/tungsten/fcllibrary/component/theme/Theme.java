package com.tungsten.fcllibrary.component.theme;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

public class Theme {

    private int color;
    private int ltColor;
    private int dkColor;
    private int autoTint;
    private boolean fullscreen;

    public Theme() {
        this(Color.parseColor("#9EFF4A"), false);
    }

    public Theme(int color, boolean fullscreen) {
        float[] ltHsv = new float[3];
        Color.colorToHSV(color, ltHsv);
        ltHsv[1] -= (1 - ltHsv[1]) * 0.3f;
        ltHsv[2] += (1 - ltHsv[2]) * 0.3f;
        float[] dkHsv = new float[3];
        Color.colorToHSV(color, dkHsv);
        dkHsv[1] += (1 - dkHsv[1]) * 0.3f;
        dkHsv[2] -= (1 - dkHsv[2]) * 0.3f;
        this.color = color;
        this.ltColor = Color.HSVToColor(ltHsv);
        this.dkColor = Color.HSVToColor(dkHsv);
        this.fullscreen = fullscreen;
        this.autoTint = ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF");
    }

    public int getColor() {
        return color;
    }

    public int getLtColor() {
        return ltColor;
    }

    public int getDkColor() {
        return dkColor;
    }

    public int getAutoTint() {
        return autoTint;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setColor(int color) {
        float[] ltHsv = new float[3];
        Color.colorToHSV(color, ltHsv);
        ltHsv[1] -= (1 - ltHsv[1]) * 0.3f;
        ltHsv[2] += (1 - ltHsv[2]) * 0.3f;
        float[] dkHsv = new float[3];
        Color.colorToHSV(color, dkHsv);
        dkHsv[1] += (1 - dkHsv[1]) * 0.3f;
        dkHsv[2] -= (1 - dkHsv[2]) * 0.3f;
        this.color = color;
        this.ltColor = Color.HSVToColor(ltHsv);
        this.dkColor = Color.HSVToColor(dkHsv);
        this.autoTint = ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF");
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public static Theme getTheme(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        int color = sharedPreferences.getInt("theme_color", Color.parseColor("#9EFF4A"));
        boolean fullscreen = sharedPreferences.getBoolean("fullscreen", false);
        return new Theme(color, fullscreen);
    }

    public static void saveTheme(Context context, Theme theme) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("theme_color", theme.getColor());
        editor.putBoolean("fullscreen", theme.isFullscreen());
        editor.apply();
    }
}
