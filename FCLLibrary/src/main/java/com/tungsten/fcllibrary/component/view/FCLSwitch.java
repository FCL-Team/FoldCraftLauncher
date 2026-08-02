package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLSwitch extends SwitchCompat {

    private boolean fromUserOrSystem = false;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> checkFlow;
    private MutableStateFlow<Boolean> disableFlow;

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
                ThemeEngine.getInstance().getTheme().getColor() | 0xFF000000
        };
        int[] subColor = {
                ThemeEngine.getInstance().getTheme().getColor() | 0xFF000000,
                Color.GRAY
        };
        setThumbTintList(new ColorStateList(state, color));
        setTrackTintList(new ColorStateList(state, subColor));
        setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLSwitch> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLSwitch self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public void addCheckedChangeListener() {
        setOnCheckedChangeListener((compoundButton, b) -> {
            fromUserOrSystem = true;
            checkFlow().setValue(b);
        });
    }

    public FCLSwitch(@NonNull Context context) {
        super(context);
        bindTheme();
    }

    public FCLSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bindTheme();
    }

    public FCLSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
            WeakReference<FCLSwitch> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLSwitch self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSwitch s = ref.get();
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
            WeakReference<FCLSwitch> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(checkFlow, v -> {
                FCLSwitch self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSwitch s = ref.get();
                        if (s != null) {
                            if (!s.fromUserOrSystem) {
                                boolean isCheck = s.checkFlow.getValue();
                                s.setChecked(isCheck);
                            }
                            s.fromUserOrSystem = false;
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
            WeakReference<FCLSwitch> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLSwitch self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSwitch s = ref.get();
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
