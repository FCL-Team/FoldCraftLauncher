package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLImageButton extends AppCompatImageButton {

    private MutableStateFlow<Drawable> imageFlow;
    private boolean autoTint;
    private boolean noPadding;
    private boolean useThemeColor;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> disableFlow;

    private void applyTheme() {
        refreshStyle();
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLImageButton> ref1 = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLImageButton self1 = ref1.get();
            if (self1 != null) {
                self1.applyTheme();
            }
        });
        WeakReference<FCLImageButton> ref2 = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2Flow(), c -> {
            FCLImageButton self2 = ref2.get();
            if (self2 != null) {
                self2.applyTheme();
            }
        });
        WeakReference<FCLImageButton> ref3 = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2DarkFlow(), c -> {
            FCLImageButton self3 = ref3.get();
            if (self3 != null) {
                self3.applyTheme();
            }
        });
    }

    public void refreshStyle() {
        int[][] state = {
                {

                }
        };
        int[] colorSrc = {
                ThemeEngine.getInstance().getTheme().getAutoTint()
        };
        int[] colorRipple = {
                ThemeEngine.getInstance().getTheme().getLtColor()
        };
        if (autoTint) {
            setImageTintList(new ColorStateList(state, colorSrc));
        }
        if (useThemeColor && getDrawable() != null) {
            getDrawable().setTint(ThemeEngine.getInstance().getTheme().getColor2());
        }
        RippleDrawable drawable = new RippleDrawable(new ColorStateList(state, colorRipple), null, null);
        drawable.setRadius(ConvertUtils.dip2px(getContext(), noPadding ? 12 : 20));
        setBackgroundDrawable(drawable);
    }

    private void init() {
        if (!noPadding) {
            setPadding(
                    ConvertUtils.dip2px(getContext(), 8f),
                    ConvertUtils.dip2px(getContext(), 8f),
                    ConvertUtils.dip2px(getContext(), 8f),
                    ConvertUtils.dip2px(getContext(), 8f)
            );
        } else {
            setPadding(0, 0, 0, 0);
        }
        setScaleType(ScaleType.FIT_XY);
    }

    public FCLImageButton(@NonNull Context context) {
        super(context);
        init();
        bindTheme();
    }

    public FCLImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageButton);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageButton_auto_tint, false);
        noPadding = typedArray.getBoolean(R.styleable.FCLImageButton_no_padding, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageButton_use_theme_color, false);
        typedArray.recycle();
        init();
        bindTheme();
    }

    public FCLImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageButton);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageButton_auto_tint, false);
        noPadding = typedArray.getBoolean(R.styleable.FCLImageButton_no_padding, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageButton_use_theme_color, false);
        typedArray.recycle();
        init();
        bindTheme();
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
        refreshStyle();
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setNoPadding(boolean noPadding) {
        this.noPadding = noPadding;
        refreshStyle();
    }

    public boolean isNoPadding() {
        return noPadding;
    }

    public void setUseThemeColor(boolean useThemeColor) {
        this.useThemeColor = useThemeColor;
        refreshStyle();
    }

    public boolean isUseThemeColor() {
        return useThemeColor;
    }

    public final void setImage(Drawable drawable) {
        imageFlow().setValue(drawable);
    }

    public final Drawable getImage() {
        return imageFlow == null ? null : imageFlow.getValue();
    }

    public final MutableStateFlow<Drawable> imageFlow() {
        if (imageFlow == null) {
            imageFlow = StateFlowKt.MutableStateFlow(null);
            WeakReference<FCLImageButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(imageFlow, v -> {
                FCLImageButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLImageButton s = ref.get();
                        if (s != null) {
                            Drawable drawable = s.imageFlow.getValue();
                            s.setImageDrawable(drawable);
                        }
                    });
                }
            });
        }

        return this.imageFlow;
    }

    public final void setVisibilityValue(boolean visibility) {
        visibilityFlow().setValue(visibility);
    }

    public final boolean getVisibilityValue() {
        return visibilityFlow == null || visibilityFlow.getValue();
    }

    public final MutableStateFlow<Boolean> visibilityFlow() {
        if (visibilityFlow == null) {
            visibilityFlow = StateFlowKt.MutableStateFlow(false);
            WeakReference<FCLImageButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLImageButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLImageButton s = ref.get();
                        if (s != null) {
                            boolean visible = s.visibilityFlow.getValue();
                            s.setVisibility(visible ? VISIBLE : GONE);
                        }
                    });
                }
            });
        }

        return visibilityFlow;
    }

    public final void setDisableValue(boolean disableValue) {
        disableFlow().setValue(disableValue);
    }

    public final boolean getDisableValue() {
        return disableFlow == null || disableFlow.getValue();
    }

    public final MutableStateFlow<Boolean> disableFlow() {
        if (disableFlow == null) {
            disableFlow = StateFlowKt.MutableStateFlow(false);
            WeakReference<FCLImageButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLImageButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLImageButton s = ref.get();
                        if (s != null) {
                            boolean disable = s.disableFlow.getValue();
                            s.setEnabled(!disable);
                        }
                    });
                }
            });
        }

        return disableFlow;
    }
}
