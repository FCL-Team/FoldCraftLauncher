package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLTextView extends AppCompatTextView {

    private boolean autoTint;
    private boolean autoBackgroundTint;
    private boolean useThemeColor;
    private MutableStateFlow<String> stringFlow;
    private MutableStateFlow<Boolean> visibilityFlow;

    private void applyTheme() {
        if (autoTint) {
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            Drawable[] drawables = getCompoundDrawablesRelative();
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    drawable.setTint(ThemeEngine.getInstance().getTheme().getAutoTint());
                }
            }
        }
        if (autoBackgroundTint) {
            setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getColor()}));
        }
    }

    private void applyTheme2() {
        if (useThemeColor) {
            setTextColor(ThemeEngine.getInstance().getTheme().getColor2());
        }
    }

    private void applyThemeDark() {
        if (useThemeColor) {
            setTextColor(ThemeEngine.getInstance().getTheme().getColor2());
        }
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLTextView> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLTextView self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2Flow(), c2 -> {
            FCLTextView self = ref.get();
            if (self != null) {
                self.applyTheme2();
            }
        });
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2DarkFlow(), c3 -> {
            FCLTextView self = ref.get();
            if (self != null) {
                self.applyThemeDark();
            }
        });
    }

    public FCLTextView(@NonNull Context context) {
        super(context);
        autoTint = false;
        autoBackgroundTint = false;
        bindTheme();
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_background_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLTextView_use_theme_color, false);
        typedArray.recycle();
        bindTheme();
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_background_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLTextView_use_theme_color, false);
        typedArray.recycle();
        bindTheme();
    }

    public void alert() {
        setTextColor(Color.RED);
    }

    public void normal() {
        setTextColor(Color.GRAY);
    }

    public void emphasize() {
        setTextColor(Color.BLACK);
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setUseThemeColor(boolean useThemeColor) {
        this.useThemeColor = useThemeColor;
        setTextColor(ThemeEngine.getInstance().getTheme().getColor2());
    }

    public boolean isUseThemeColor() {
        return useThemeColor;
    }

    public void setAutoBackgroundTint(boolean autoBackgroundTint) {
        this.autoBackgroundTint = autoBackgroundTint;
    }

    public boolean isAutoBackgroundTint() {
        return autoBackgroundTint;
    }

    public final void setString(String string) {
        stringFlow().setValue(string);
    }

    public final String getString() {
        return stringFlow == null ? null : stringFlow.getValue();
    }

    public final MutableStateFlow<String> stringFlow() {
        if (stringFlow == null) {
            stringFlow = StateFlowKt.MutableStateFlow(null);
            WeakReference<FCLTextView> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(stringFlow, v -> {
                FCLTextView self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLTextView s = ref.get();
                        if (s != null) {
                            String string = s.stringFlow.getValue();
                            s.setText(string);
                        }
                    });
                }
            });
        }

        return stringFlow;
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
            WeakReference<FCLTextView> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLTextView self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLTextView s = ref.get();
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
}
