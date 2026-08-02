package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLCheckBox extends AppCompatCheckBox {

    private boolean autoTint;
    private boolean fromUserOrSystem = false;
    private boolean fromIndeterminate = false;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> checkFlow;
    private MutableStateFlow<Boolean> indeterminateFlow;
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
                ThemeEngine.getInstance().getTheme().getColor()
        };
        setButtonTintList(new ColorStateList(state, color));
        if (autoTint) {
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        }
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLCheckBox> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLCheckBox self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public void addCheckedChangeListener() {
        setOnCheckedChangeListener((compoundButton, b) -> {
            if (!fromIndeterminate) {
                fromUserOrSystem = true;
                checkFlow().setValue(b);
                indeterminateFlow().setValue(false);
                fromUserOrSystem = false;
            }
        });
    }

    public FCLCheckBox(@NonNull Context context) {
        super(context);
        bindTheme();
    }

    public FCLCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckBox);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckBox_auto_hint_tint, false);
        typedArray.recycle();
        bindTheme();
    }

    public FCLCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckBox);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckBox_auto_hint_tint, false);
        typedArray.recycle();
        bindTheme();
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
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
            WeakReference<FCLCheckBox> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLCheckBox self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLCheckBox s = ref.get();
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
            WeakReference<FCLCheckBox> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(checkFlow, v -> {
                FCLCheckBox self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLCheckBox s = ref.get();
                        if (s != null) {
                            if (!s.fromUserOrSystem) {
                                boolean isCheck = s.checkFlow.getValue();
                                s.setChecked(isCheck);
                            }
                        }
                    });
                }
            });
        }

        return checkFlow;
    }

    public final void setIndeterminate(boolean indeterminate) {
        checkFlow().setValue(indeterminate);
    }

    public final boolean isIndeterminate() {
        return indeterminateFlow().getValue();
    }

    public final MutableStateFlow<Boolean> indeterminateFlow() {
        if (indeterminateFlow == null) {
            indeterminateFlow = StateFlowKt.MutableStateFlow(false);
            WeakReference<FCLCheckBox> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(indeterminateFlow, v -> {
                FCLCheckBox self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLCheckBox s = ref.get();
                        if (s != null) {
                            if (!s.fromUserOrSystem) {
                                s.fromIndeterminate = true;
                                if (s.indeterminateFlow.getValue()) {
                                    s.setChecked(true);
                                } else {
                                    s.setChecked(s.checkFlow().getValue());
                                }
                                s.fromIndeterminate = false;
                            }
                        }
                    });
                }
            });
        }

        return indeterminateFlow;
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
            WeakReference<FCLCheckBox> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLCheckBox self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLCheckBox s = ref.get();
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
