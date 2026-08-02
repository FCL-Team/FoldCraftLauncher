package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLImageView extends AppCompatImageView {

    private MutableStateFlow<Drawable> imageFlow;
    private boolean autoTint;
    private boolean useThemeColor;
    private MutableStateFlow<Boolean> visibilityFlow;

    private void applyTheme() {
        if (autoTint) {
            int[][] state = {
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getInstance().getTheme().getAutoTint()
            };
            setImageTintList(new ColorStateList(state, color));
        }
        if (useThemeColor && getBackground() != null) {
            getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor2());
        }
    }

    private void applyTheme2() {
        if (useThemeColor && getBackground() != null) {
            getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor2());
        }
    }

    private void applyThemeDark() {
        if (useThemeColor && getBackground() != null) {
            getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor2());
        }
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLImageView> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLImageView self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
        WeakReference<FCLImageView> ref2 = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2Flow(), c2 -> {
            FCLImageView self2 = ref2.get();
            if (self2 != null) {
                self2.applyTheme2();
            }
        });
        WeakReference<FCLImageView> refDark = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().color2DarkFlow(), cDark -> {
            FCLImageView selfDark = refDark.get();
            if (selfDark != null) {
                selfDark.applyThemeDark();
            }
        });
    }

    public FCLImageView(@NonNull Context context) {
        super(context);
        bindTheme();
    }

    public FCLImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageView);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageView_auto_src_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageView_use_theme_color, false);
        typedArray.recycle();
        bindTheme();
    }

    public FCLImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageView);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageView_auto_src_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageView_use_theme_color, false);
        typedArray.recycle();
        bindTheme();
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setUseThemeColor(boolean useThemeColor) {
        this.useThemeColor = useThemeColor;
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
            WeakReference<FCLImageView> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(imageFlow, v -> {
                FCLImageView self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLImageView s = ref.get();
                        if (s != null) {
                            Drawable drawable = s.imageFlow.getValue();
                            s.setBackground(drawable);
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
            WeakReference<FCLImageView> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLImageView self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLImageView s = ref.get();
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
