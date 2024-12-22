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

import com.mio.util.ImageUtil;
import com.tungsten.fclauncher.utils.FCLPath;
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
    private final IntegerProperty color2 = new SimpleIntegerProperty();
    private final IntegerProperty ltColor = new SimpleIntegerProperty();
    private final IntegerProperty dkColor = new SimpleIntegerProperty();
    private final IntegerProperty autoTint = new SimpleIntegerProperty();
    private final BooleanProperty fullscreen = new SimpleBooleanProperty();
    private final BooleanProperty closeSkinModel = new SimpleBooleanProperty();
    private final BooleanProperty modified = new SimpleBooleanProperty();
    private final IntegerProperty animationSpeed = new SimpleIntegerProperty();
    private final ObjectProperty<BitmapDrawable> backgroundLt = new SimpleObjectProperty<>();
    private final ObjectProperty<BitmapDrawable> backgroundDk = new SimpleObjectProperty<>();

    public Theme(int color, int color2, boolean fullscreen, boolean closeSkinModel, int animationSpeed, BitmapDrawable backgroundLt, BitmapDrawable backgroundDk, boolean modified) {
        float[] ltHsv = new float[3];
        Color.colorToHSV(color, ltHsv);
        ltHsv[1] -= (1 - ltHsv[1]) * 0.3f;
        ltHsv[2] += (1 - ltHsv[2]) * 0.3f;
        float[] dkHsv = new float[3];
        Color.colorToHSV(color, dkHsv);
        dkHsv[1] += (1 - dkHsv[1]) * 0.3f;
        dkHsv[2] -= (1 - dkHsv[2]) * 0.3f;
        this.color.set(color);
        this.color2.set(color2);
        this.ltColor.set(Color.HSVToColor(ltHsv));
        this.dkColor.set(Color.HSVToColor(dkHsv));
        this.fullscreen.set(fullscreen);
        this.closeSkinModel.set(closeSkinModel);
        this.modified.set(modified);
        this.animationSpeed.set(animationSpeed);
        this.autoTint.set(ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF"));
        this.backgroundLt.set(backgroundLt);
        this.backgroundDk.set(backgroundDk);
    }

    public int getColor() {
        return color.get();
    }

    public int getColor2() {
        return color2.get();
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

    public boolean isCloseSkinModel() {
        return closeSkinModel.get();
    }

    public boolean isModified() {
        return modified.get();
    }

    public int getAnimationSpeed() {
        return animationSpeed.get();
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

    public IntegerProperty color2Property() {
        return color2;
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

    public BooleanProperty ignoreSkinContainerProperty() {
        return fullscreen;
    }

    public BooleanProperty modifiedProperty() {
        return modified;
    }

    public IntegerProperty animationSpeedProperty() {
        return animationSpeed;
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
        this.color2.set(color);
    }

    public void setColor2(int color) {
        this.color2.set(color);
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen.set(fullscreen);
    }

    public void setiIgnoreSkinContainer(boolean ignoreSkinContainer) {
        this.closeSkinModel.set(ignoreSkinContainer);
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed.set(animationSpeed);
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
        int color2 = sharedPreferences.getInt("theme_color2", Color.parseColor("#7797CF"));
        boolean fullscreen = sharedPreferences.getBoolean("fullscreen", false);
        boolean closeSkinModel = sharedPreferences.getBoolean("close_skin_model", false);
        boolean modified = sharedPreferences.getBoolean("modified", false);
        int animationSpeed = sharedPreferences.getInt("animation_speed", 8);
        Bitmap lt = ImageUtil.load(context.getFilesDir().getAbsolutePath() + "/background/lt.png").orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_light));
        BitmapDrawable backgroundLt = new BitmapDrawable(context.getResources(), lt);
        Bitmap dk = ImageUtil.load(context.getFilesDir().getAbsolutePath() + "/background/dk.png").orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark));
        BitmapDrawable backgroundDk = new BitmapDrawable(context.getResources(), dk);
        return new Theme(color, color2, fullscreen, closeSkinModel, animationSpeed, backgroundLt, backgroundDk, modified);
    }

    public static void saveTheme(Context context, Theme theme) {
        saveTheme(context, theme, theme.isModified());
    }

    public static void saveTheme(Context context, Theme theme, boolean modified) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("theme_color", theme.getColor());
        editor.putInt("theme_color2", theme.getColor2());
        editor.putBoolean("fullscreen", theme.isFullscreen());
        editor.putInt("animation_speed", theme.getAnimationSpeed());
        editor.putBoolean("close_skin_model", theme.isCloseSkinModel());
        editor.putBoolean("modified", modified);
        editor.apply();
    }
}
