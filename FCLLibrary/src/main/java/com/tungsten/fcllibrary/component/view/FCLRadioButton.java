package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLRadioButton extends AppCompatRadioButton {

    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> checkFlow;
    private MutableStateFlow<Boolean> disableFlow;
    private boolean textWithThemeColor = false;

    private void applyTheme() {
        int[][] state = {
                {
                        android.R.attr.state_checked
                },
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor(),
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setButtonTintList(new ColorStateList(state, color));
        if (textWithThemeColor) {
            setTextColor(ThemeEngine.getInstance().getTheme().getColor() | 0xFF000000);
        }
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLRadioButton> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLRadioButton self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public FCLRadioButton(Context context) {
        super(context);
        bindTheme();
    }

    public FCLRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLRadioButton);
        textWithThemeColor = typedArray.getBoolean(R.styleable.FCLRadioButton_text_use_theme_color, false);
        typedArray.recycle();
        bindTheme();
    }

    public FCLRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLRadioButton);
        textWithThemeColor = typedArray.getBoolean(R.styleable.FCLRadioButton_text_use_theme_color, false);
        typedArray.recycle();
        bindTheme();
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
            WeakReference<FCLRadioButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLRadioButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLRadioButton s = ref.get();
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

    public final void setCheckValue(boolean isChecked) {
        checkFlow().setValue(isChecked);
    }

    public final boolean getCheckValue() {
        return checkFlow == null || checkFlow.getValue();
    }

    public final MutableStateFlow<Boolean> checkFlow() {
        if (checkFlow == null) {
            checkFlow = StateFlowKt.MutableStateFlow(false);
            WeakReference<FCLRadioButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(checkFlow, v -> {
                FCLRadioButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLRadioButton s = ref.get();
                        if (s != null) {
                            boolean isCheck = s.checkFlow.getValue();
                            s.setChecked(isCheck);
                        }
                    });
                }
            });
        }

        return checkFlow;
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
            WeakReference<FCLRadioButton> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLRadioButton self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLRadioButton s = ref.get();
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
