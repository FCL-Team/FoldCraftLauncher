package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLSeekBar extends AppCompatSeekBar {

    private boolean fromUserOrSystem = false;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> disableFlow;
    private MutableStateFlow<Double> percentProgressFlow;
    private MutableStateFlow<Integer> progressFlow;

    private void applyTheme() {
        int[][] state = {
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setThumbTintList(new ColorStateList(state, color));
        setProgressTintList(new ColorStateList(state, color));
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLSeekBar> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLSeekBar self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public void addProgressListener() {
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fromUserOrSystem = true;
                progressFlow().setValue(i);
                percentProgressFlow().setValue((double) i / (double) getMax());
                fromUserOrSystem = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public FCLSeekBar(@NonNull Context context) {
        super(context);
        bindTheme();
    }

    public FCLSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bindTheme();
    }

    public FCLSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            WeakReference<FCLSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSeekBar s = ref.get();
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
            WeakReference<FCLSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSeekBar s = ref.get();
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

    public final void setPercentProgressValue(double percentProgressValue) {
        percentProgressFlow().setValue(percentProgressValue);
    }

    public final double getPercentProgressValue() {
        return percentProgressFlow == null ? -1 : percentProgressFlow.getValue();
    }

    public final MutableStateFlow<Double> percentProgressFlow() {
        if (percentProgressFlow == null) {
            percentProgressFlow = StateFlowKt.MutableStateFlow(0.0);
            WeakReference<FCLSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(percentProgressFlow, v -> {
                FCLSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSeekBar s = ref.get();
                        if (s != null) {
                            if (!s.fromUserOrSystem) {
                                double progress = s.percentProgressFlow.getValue();
                                s.setProgress((int) (progress * s.getMax()));
                            }
                        }
                    });
                }
            });
        }

        return percentProgressFlow;
    }

    public final void setProgressValue(int progressValue) {
        progressFlow().setValue(progressValue);
    }

    public final int getProgressValue() {
        return progressFlow == null ? -1 : progressFlow().getValue();
    }

    public final MutableStateFlow<Integer> progressFlow() {
        if (progressFlow == null) {
            progressFlow = StateFlowKt.MutableStateFlow(0);
            WeakReference<FCLSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(progressFlow, v -> {
                FCLSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSeekBar s = ref.get();
                        if (s != null) {
                            if (!s.fromUserOrSystem) {
                                int progress = s.progressFlow.getValue();
                                s.setProgress(progress);
                            }
                        }
                    });
                }
            });
        }

        return progressFlow;
    }

}
