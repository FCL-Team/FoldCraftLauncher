package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLProgressBar extends ProgressBar {

    private MutableStateFlow<Double> percentProgressFlow;
    private MutableStateFlow<Integer> firstProgressFlow;
    private MutableStateFlow<Integer> secondProgressFlow;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> disableFlow;

    private void applyTheme() {
        int[][] state = {
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setProgressTintList(new ColorStateList(state, color));
        setSecondaryProgressTintList(new ColorStateList(state, color));
        setIndeterminateTintList(new ColorStateList(state, color));
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLProgressBar> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLProgressBar self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public FCLProgressBar(Context context) {
        super(context);
        bindTheme();
    }

    public FCLProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        bindTheme();
    }

    public FCLProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindTheme();
    }

    public FCLProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        bindTheme();
    }

    public final MutableStateFlow<Double> percentProgressFlow() {
        if (percentProgressFlow == null) {
            percentProgressFlow = StateFlowKt.MutableStateFlow(0.0);
            WeakReference<FCLProgressBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(percentProgressFlow, v -> {
                FCLProgressBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLProgressBar s = ref.get();
                        if (s != null) {
                            // progress should >= 0, <= 1
                            double progress = s.percentProgressFlow.getValue();
                            s.setIndeterminate(progress < 0.0);
                            if (progress >= 0.0) {
                                s.setProgress((int) (progress * s.getMax()));
                            }
                        }
                    });
                }
            });
        }

        return percentProgressFlow;
    }

    public final MutableStateFlow<Integer> firstProgressFlow() {
        if (firstProgressFlow == null) {
            firstProgressFlow = StateFlowKt.MutableStateFlow(0);
            WeakReference<FCLProgressBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(firstProgressFlow, v -> {
                FCLProgressBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLProgressBar s = ref.get();
                        if (s != null) {
                            int progress = s.firstProgressFlow.getValue();
                            if (progress >= 0) {
                                s.setProgress(Math.min(progress, s.getMax()));
                            }
                        }
                    });
                }
            });
        }

        return firstProgressFlow;
    }

    public final MutableStateFlow<Integer> secondProgressFlow() {
        if (secondProgressFlow == null) {
            secondProgressFlow = StateFlowKt.MutableStateFlow(0);
            WeakReference<FCLProgressBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(secondProgressFlow, v -> {
                FCLProgressBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLProgressBar s = ref.get();
                        if (s != null) {
                            int progress = s.secondProgressFlow.getValue();
                            if (progress >= 0) {
                                s.setSecondaryProgress(Math.min(progress, s.getMax()));
                            }
                        }
                    });
                }
            });
        }

        return secondProgressFlow;
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
            WeakReference<FCLProgressBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLProgressBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLProgressBar s = ref.get();
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
            WeakReference<FCLProgressBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLProgressBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLProgressBar s = ref.get();
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
