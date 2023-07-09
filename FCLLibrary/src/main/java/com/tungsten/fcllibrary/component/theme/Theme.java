package com.tungsten.fcllibrary.component.theme;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.graphics.ColorUtils;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.io.File;

public class Theme {

    private final IntegerProperty color = new SimpleIntegerProperty();
    private final IntegerProperty ltColor = new SimpleIntegerProperty();
    private final IntegerProperty dkColor = new SimpleIntegerProperty();
    private final IntegerProperty autoTint = new SimpleIntegerProperty();
    private final BooleanProperty fullscreen = new SimpleBooleanProperty();
    private final ObjectProperty<BitmapDrawable> backgroundLt = new SimpleObjectProperty<>();
    private final ObjectProperty<BitmapDrawable> backgroundDk = new SimpleObjectProperty<>();

    public Theme(int color, boolean fullscreen, BitmapDrawable backgroundLt, BitmapDrawable backgroundDk) {
        float[] ltHsv = new float[3];
        Color.colorToHSV(color, ltHsv);
        ltHsv[1] -= (1 - ltHsv[1]) * 0.3f;
        ltHsv[2] += (1 - ltHsv[2]) * 0.3f;
        float[] dkHsv = new float[3];
        Color.colorToHSV(color, dkHsv);
        dkHsv[1] += (1 - dkHsv[1]) * 0.3f;
        dkHsv[2] -= (1 - dkHsv[2]) * 0.3f;
        this.color.set(color);
        this.ltColor.set(Color.HSVToColor(ltHsv));
        this.dkColor.set(Color.HSVToColor(dkHsv));
        this.fullscreen.set(fullscreen);
        this.autoTint.set(ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF"));
        this.backgroundLt.set(backgroundLt);
        this.backgroundDk.set(backgroundDk);
    }

    public int getColor() {
        return color.get();
    }

    public int getLtColor() {
        return ltColor.get();
    }

    public int getDkColor() {
        return dkColor.get();
    }

    public int getAutoTint() {
        return autoTint.get();
    }

    public int getAutoHintTint() {
        return ColorUtils.calculateLuminance(getColor()) >= 0.5 ? Color.parseColor("#99000000") : Color.parseColor("#99FFFFFF");
    }

    public boolean isFullscreen() {
        return fullscreen.get();
    }

    public BitmapDrawable getBackgroundLt() {
        return backgroundLt.get();
    }

    public BitmapDrawable getBackgroundDk() {
        return backgroundDk.get();
    }

    public IntegerProperty colorProperty() {
        return color;
    }

    public IntegerProperty ltColorProperty() {
        return ltColor;
    }

    public IntegerProperty dkColorProperty() {
        return dkColor;
    }

    public IntegerProperty autoTintProperty() {
        return autoTint;
    }

    public BooleanProperty fullscreenProperty() {
        return fullscreen;
    }

    public ObjectProperty<BitmapDrawable> ltBackgroundProperty() {
        return backgroundLt;
    }

    public ObjectProperty<BitmapDrawable> dkBackgroundProperty() {
        return backgroundDk;
    }

    public BitmapDrawable getBackground(Context context) {
        boolean isNightMode = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        return isNightMode ? backgroundDk.get() : backgroundLt.get();
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
        this.ltColor.set(Color.HSVToColor(ltHsv));
        this.dkColor.set(Color.HSVToColor(dkHsv));
        this.autoTint.set(ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF"));
        this.color.set(color);
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen.set(fullscreen);
    }

    public void setBackgroundLt(BitmapDrawable backgroundLt) {
        this.backgroundLt.set(backgroundLt);
    }

    public void setBackgroundDk(BitmapDrawable backgroundDk) {
        this.backgroundDk.set(backgroundDk);
    }

    public static Theme getTheme(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        int color = sharedPreferences.getInt("theme_color", Color.parseColor("#7797CF"));
        boolean fullscreen = sharedPreferences.getBoolean("fullscreen", false);
        Bitmap lt = !new File(context.getFilesDir().getAbsolutePath() + "/background/lt.png").exists() ? ConvertUtils.getBitmapFromRes(context, R.drawable.background_light) : BitmapFactory.decodeFile(context.getFilesDir().getAbsolutePath() + "/background/lt.png");
        BitmapDrawable backgroundLt = new BitmapDrawable(lt);
        Bitmap dk = !new File(context.getFilesDir().getAbsolutePath() + "/background/dk.png").exists() ? ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark) : BitmapFactory.decodeFile(context.getFilesDir().getAbsolutePath() + "/background/dk.png");
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
        editor.apply();
    }
}
