package com.tungsten.fcllibrary.component.theme;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.graphics.ColorUtils;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class Theme {

    private int color;
    private int ltColor;
    private int dkColor;
    private int autoTint;
    private boolean fullscreen;
    private BitmapDrawable backgroundLt;
    private BitmapDrawable backgroundDk;

    public Theme() {
        this(Color.parseColor("#9EFF4A"), false, null, null);
    }

    public Theme(int color, boolean fullscreen, BitmapDrawable backgroundLt, BitmapDrawable backgroundDk) {
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
        this.backgroundLt = backgroundLt;
        this.backgroundDk = backgroundDk;
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

    public BitmapDrawable getBackgroundLt() {
        return backgroundLt;
    }

    public BitmapDrawable getBackgroundDk() {
        return backgroundDk;
    }

    public BitmapDrawable getBackground(Context context) {
        boolean isNightMode = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        return isNightMode ? backgroundDk : backgroundLt;
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

    public void setBackgroundLt(BitmapDrawable backgroundLt) {
        this.backgroundLt = backgroundLt;
    }

    public void setBackgroundDk(BitmapDrawable backgroundDk) {
        this.backgroundDk = backgroundDk;
    }

    public static Theme getTheme(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        int color = sharedPreferences.getInt("theme_color", Color.parseColor("#9EFF4A"));
        boolean fullscreen = sharedPreferences.getBoolean("fullscreen", false);
        Bitmap lt = ConvertUtils.stringToBitmap(sharedPreferences.getString("background_light", null)) == null ? ConvertUtils.getBitmapFromRes(context, R.drawable.background_light) : ConvertUtils.stringToBitmap(sharedPreferences.getString("background_light", null));
        BitmapDrawable backgroundLt = new BitmapDrawable(lt);
        Bitmap dk = ConvertUtils.stringToBitmap(sharedPreferences.getString("background_dark", null)) == null ? ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark) : ConvertUtils.stringToBitmap(sharedPreferences.getString("background_dark", null));
        BitmapDrawable backgroundDk = new BitmapDrawable(dk);
        return new Theme(color, fullscreen, backgroundLt, backgroundDk);
    }

    public static void saveTheme(Context context, Theme theme) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("theme_color", theme.getColor());
        editor.putBoolean("fullscreen", theme.isFullscreen());
        editor.putString("background_light", ConvertUtils.bitmapToString(theme.getBackgroundLt().getBitmap()));
        editor.putString("background_dark", ConvertUtils.bitmapToString(theme.getBackgroundDk().getBitmap()));
        editor.apply();
    }
}
