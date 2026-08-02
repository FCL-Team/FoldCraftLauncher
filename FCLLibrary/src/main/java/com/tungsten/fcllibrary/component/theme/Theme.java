package com.tungsten.fcllibrary.component.theme;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.graphics.ColorUtils;

import com.mio.util.ImageUtil;
import com.tungsten.fcl.FCLApplication;
import com.tungsten.fclcore.observable.property.BooleanProperty;
import com.tungsten.fclcore.observable.property.IntegerProperty;
import com.tungsten.fclcore.observable.property.ObjectProperty;
import com.tungsten.fclcore.observable.property.SimpleBooleanProperty;
import com.tungsten.fclcore.observable.property.SimpleIntegerProperty;
import com.tungsten.fclcore.observable.property.SimpleObjectProperty;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.util.ConvertUtils;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 主题数据（阶段 4a）：字段已切换为 StateFlow，Compose 侧直接 collect
 * （FCLTheme.kt / LauncherSettingViewModel / ListItemAnimation）。
 *
 * <p>保留原生 View 侧（FCLLibrary component/view 24 文件，后续批次改造）仍通过
 * {@code xxxProperty()} 单向 bind 主题属性：这里为每个 Flow 维护一个同步镜像
 * Property（subscribeWithCurrent 初值同步 + 跟随后续发射），视图绑定链路不变。</p>
 */
public class Theme {

    private final MutableStateFlow<Integer> color = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<Integer> color2 = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<Integer> color2Dark = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<Integer> ltColor = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<Integer> dkColor = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<Integer> autoTint = StateFlowKt.MutableStateFlow(0);
    private final MutableStateFlow<Boolean> fullscreen = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<Boolean> closeSkinModel = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<Integer> animationSpeed = StateFlowKt.MutableStateFlow(8);
    private final MutableStateFlow<BitmapDrawable> backgroundLt = StateFlowKt.MutableStateFlow(null);
    private final MutableStateFlow<BitmapDrawable> backgroundDk = StateFlowKt.MutableStateFlow(null);

    // ---- 原生 View 绑定用的镜像属性（Flow → Property 单向同步；生命周期与 Theme 一致） ----
    private final IntegerProperty colorMirror = new SimpleIntegerProperty();
    private final IntegerProperty color2Mirror = new SimpleIntegerProperty();
    private final IntegerProperty color2DarkMirror = new SimpleIntegerProperty();
    private final IntegerProperty ltColorMirror = new SimpleIntegerProperty();
    private final IntegerProperty dkColorMirror = new SimpleIntegerProperty();
    private final IntegerProperty autoTintMirror = new SimpleIntegerProperty();
    private final BooleanProperty fullscreenMirror = new SimpleBooleanProperty();
    private final BooleanProperty closeSkinModelMirror = new SimpleBooleanProperty();
    private final IntegerProperty animationSpeedMirror = new SimpleIntegerProperty();
    private final ObjectProperty<BitmapDrawable> backgroundLtMirror = new SimpleObjectProperty<>();
    private final ObjectProperty<BitmapDrawable> backgroundDkMirror = new SimpleObjectProperty<>();

    public Theme(int color, int color2, int color2Dark, boolean fullscreen, boolean closeSkinModel, int animationSpeed, BitmapDrawable backgroundLt, BitmapDrawable backgroundDk) {
        attachMirrors();
        float[] ltHsv = new float[3];
        Color.colorToHSV(color, ltHsv);
        ltHsv[1] -= (1 - ltHsv[1]) * 0.3f;
        ltHsv[2] += (1 - ltHsv[2]) * 0.3f;
        float[] dkHsv = new float[3];
        Color.colorToHSV(color, dkHsv);
        dkHsv[1] += (1 - dkHsv[1]) * 0.3f;
        dkHsv[2] -= (1 - dkHsv[2]) * 0.3f;
        this.color.setValue(color);
        this.color2.setValue(color2);
        this.color2Dark.setValue(color2Dark);
        this.ltColor.setValue(Color.HSVToColor(ltHsv));
        this.dkColor.setValue(Color.HSVToColor(dkHsv));
        this.fullscreen.setValue(fullscreen);
        this.closeSkinModel.setValue(closeSkinModel);
        this.animationSpeed.setValue(animationSpeed);
        this.autoTint.setValue(ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF"));
        this.backgroundLt.setValue(backgroundLt);
        this.backgroundDk.setValue(backgroundDk);
    }

    private void attachMirrors() {
        FlowSubscriptions.subscribeWithCurrent(color, colorMirror::set);
        FlowSubscriptions.subscribeWithCurrent(color2, color2Mirror::set);
        FlowSubscriptions.subscribeWithCurrent(color2Dark, color2DarkMirror::set);
        FlowSubscriptions.subscribeWithCurrent(ltColor, ltColorMirror::set);
        FlowSubscriptions.subscribeWithCurrent(dkColor, dkColorMirror::set);
        FlowSubscriptions.subscribeWithCurrent(autoTint, autoTintMirror::set);
        FlowSubscriptions.subscribeWithCurrent(fullscreen, fullscreenMirror::set);
        FlowSubscriptions.subscribeWithCurrent(closeSkinModel, closeSkinModelMirror::set);
        FlowSubscriptions.subscribeWithCurrent(animationSpeed, animationSpeedMirror::set);
        FlowSubscriptions.subscribeWithCurrent(backgroundLt, backgroundLtMirror::set);
        FlowSubscriptions.subscribeWithCurrent(backgroundDk, backgroundDkMirror::set);
    }

    public int getColor() {
        return color.getValue();
    }

    public int getColor2() {
        boolean isNightMode = (FCLApplication.getCurrentActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        return isNightMode ? color2Dark.getValue() : color2.getValue();
    }

    public int _getColor2() {
        return color2.getValue();
    }
    public int getColor2Dark() {
        return color2Dark.getValue();
    }

    public int getLtColor() {
        return ltColor.getValue();
    }

    public int getDkColor() {
        return dkColor.getValue();
    }

    public int getAutoTint() {
        return autoTint.getValue();
    }

    public int getAutoHintTint() {
        return ColorUtils.calculateLuminance(getColor()) >= 0.5 ? Color.parseColor("#99000000") : Color.parseColor("#99FFFFFF");
    }

    public boolean isFullscreen() {
        return fullscreen.getValue();
    }

    public boolean isCloseSkinModel() {
        return closeSkinModel.getValue();
    }

    public int getAnimationSpeed() {
        return animationSpeed.getValue();
    }

    public BitmapDrawable getBackgroundLt() {
        return backgroundLt.getValue();
    }

    public BitmapDrawable getBackgroundDk() {
        return backgroundDk.getValue();
    }

    // ---- StateFlow 访问器（Compose/新代码消费侧） ----

    public StateFlow<Integer> colorFlow() {
        return color;
    }

    public StateFlow<Integer> color2Flow() {
        return color2;
    }

    public StateFlow<Integer> color2DarkFlow() {
        return color2Dark;
    }

    public StateFlow<Integer> ltColorFlow() {
        return ltColor;
    }

    public StateFlow<Integer> dkColorFlow() {
        return dkColor;
    }

    public StateFlow<Integer> autoTintFlow() {
        return autoTint;
    }

    public StateFlow<Boolean> fullscreenFlow() {
        return fullscreen;
    }

    public StateFlow<Boolean> closeSkinModelFlow() {
        return closeSkinModel;
    }

    public StateFlow<Integer> animationSpeedFlow() {
        return animationSpeed;
    }

    public StateFlow<BitmapDrawable> ltBackgroundFlow() {
        return backgroundLt;
    }

    public StateFlow<BitmapDrawable> dkBackgroundFlow() {
        return backgroundDk;
    }

    // ---- 遗留 observable 访问器（原生 View 绑定接缝，随 view 批次删除） ----

    public IntegerProperty colorProperty() {
        return colorMirror;
    }

    public IntegerProperty color2Property() {
        return color2Mirror;
    }

    public IntegerProperty color2DarkProperty() {
        return color2DarkMirror;
    }

    public IntegerProperty ltColorProperty() {
        return ltColorMirror;
    }

    public IntegerProperty dkColorProperty() {
        return dkColorMirror;
    }

    public IntegerProperty autoTintProperty() {
        return autoTintMirror;
    }

    public BooleanProperty fullscreenProperty() {
        return fullscreenMirror;
    }

    /** 注意：遗留 bug——本访问器错误返回 fullscreen 镜像而非 closeSkinModel（保留不改）。 */
    public BooleanProperty ignoreSkinContainerProperty() {
        return fullscreenMirror;
    }

    public IntegerProperty animationSpeedProperty() {
        return animationSpeedMirror;
    }

    public ObjectProperty<BitmapDrawable> ltBackgroundProperty() {
        return backgroundLtMirror;
    }

    public ObjectProperty<BitmapDrawable> dkBackgroundProperty() {
        return backgroundDkMirror;
    }

    public BitmapDrawable getBackground(Context context) {
        boolean isNightMode = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        return isNightMode ? backgroundDk.getValue() : backgroundLt.getValue();
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
        this.ltColor.setValue(Color.HSVToColor(ltHsv));
        this.dkColor.setValue(Color.HSVToColor(dkHsv));
        this.autoTint.setValue(ColorUtils.calculateLuminance(color) >= 0.5 ? Color.parseColor("#FF000000") : Color.parseColor("#FFFFFFFF"));
        this.color.setValue(color);
    }

    public void setColor2(int color) {
        this.color2.setValue(color);
    }

    public void setColor2Dark(int color) {
        this.color2Dark.setValue(color);
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen.setValue(fullscreen);
    }

    public void setiIgnoreSkinContainer(boolean ignoreSkinContainer) {
        this.closeSkinModel.setValue(ignoreSkinContainer);
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed.setValue(animationSpeed);
    }

    public void setBackgroundLt(BitmapDrawable backgroundLt) {
        this.backgroundLt.setValue(backgroundLt);
    }

    public void setBackgroundDk(BitmapDrawable backgroundDk) {
        this.backgroundDk.setValue(backgroundDk);
    }

    public static Theme getTheme(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        int color = sharedPreferences.getInt("theme_color", Color.parseColor("#7797CF"));
        int color2 = sharedPreferences.getInt("theme_color2", Color.parseColor("#000000"));
        int color2Dark = sharedPreferences.getInt("theme_color2_dark", Color.parseColor("#FFFFFF"));
        boolean fullscreen = sharedPreferences.getBoolean("fullscreen", false);
        boolean closeSkinModel = sharedPreferences.getBoolean("close_skin_model", false);
        int animationSpeed = sharedPreferences.getInt("animation_speed", 8);
        Bitmap lt = ImageUtil.load(context.getFilesDir().getAbsolutePath() + "/background/lt.png").orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_light));
        BitmapDrawable backgroundLt = new BitmapDrawable(context.getResources(), lt);
        Bitmap dk = ImageUtil.load(context.getFilesDir().getAbsolutePath() + "/background/dk.png").orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark));
        BitmapDrawable backgroundDk = new BitmapDrawable(context.getResources(), dk);
        return new Theme(color, color2, color2Dark, fullscreen, closeSkinModel, animationSpeed, backgroundLt, backgroundDk);
    }

    public static void saveTheme(Context context, Theme theme) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("theme_color", theme.getColor());
        editor.putInt("theme_color2", theme._getColor2());
        editor.putInt("theme_color2_dark", theme.getColor2Dark());
        editor.putBoolean("fullscreen", theme.isFullscreen());
        editor.putInt("animation_speed", theme.getAnimationSpeed());
        editor.putBoolean("close_skin_model", theme.isCloseSkinModel());
        editor.apply();
    }
}
