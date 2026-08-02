package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLPreciseSeekBar extends RelativeLayout {

    private FCLImageButton minus;
    private FCLImageButton add;
    private FCLSeekBar seekBar;

    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> disableFlow;

    public FCLPreciseSeekBar(@NonNull Context context) {
        super(context);
        init(false, 0, 100);
    }

    public FCLPreciseSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLPreciseSeekBar);
        boolean autoTint = typedArray.getBoolean(R.styleable.FCLPreciseSeekBar_auto_button_tint, false);
        int min = typedArray.getInteger(R.styleable.FCLPreciseSeekBar_min_value, 0);
        int max = typedArray.getInteger(R.styleable.FCLPreciseSeekBar_max_value, 100);
        typedArray.recycle();
        init(autoTint, min, max);
    }

    public FCLPreciseSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLPreciseSeekBar);
        boolean autoTint = typedArray.getBoolean(R.styleable.FCLPreciseSeekBar_auto_button_tint, false);
        int min = typedArray.getInteger(R.styleable.FCLPreciseSeekBar_min_value, 0);
        int max = typedArray.getInteger(R.styleable.FCLPreciseSeekBar_max_value, 100);
        typedArray.recycle();
        init(autoTint, min, max);
    }

    private void init(boolean autoTint, int min, int max) {
        add = new FCLImageButton(getContext());
        minus = new FCLImageButton(getContext());
        seekBar = new FCLSeekBar(getContext());

        int[][] state = {{}};
        int[] colorSrc = {
                Color.GRAY
        };
        add.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_baseline_add_24));
        minus.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_baseline_remove_24));
        add.setImageTintList(new ColorStateList(state, colorSrc));
        minus.setImageTintList(new ColorStateList(state, colorSrc));

        add.setNoPadding(true);
        minus.setNoPadding(true);
        setAutoTint(autoTint);
        seekBar.setMin(min);
        seekBar.setMax(max);
        seekBar.addProgressListener();

        add.setOnClickListener(v -> {
            if (seekBar.getProgress() < seekBar.getMax()) {
                setProgressValue(seekBar.getProgress() + 1);
            }
        });
        minus.setOnClickListener(v -> {
            if (seekBar.getProgress() > seekBar.getMin()) {
                setProgressValue(seekBar.getProgress() - 1);
            }
        });

        addView(minus, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(seekBar, new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(add, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        post(() -> add.post(() -> seekBar.post(() -> minus.post(() -> {
            if (add.getMeasuredHeight() >= seekBar.getMeasuredHeight()) {
                seekBar.setY((add.getMeasuredHeight() - seekBar.getMeasuredHeight()) / 2f);
            } else {
                add.setY((seekBar.getMeasuredHeight() - add.getMeasuredHeight()) / 2f);
                minus.setY((seekBar.getMeasuredHeight() - minus.getMeasuredHeight()) / 2f);
            }
            minus.setX(0);
            add.setX(getMeasuredWidth() - add.getMeasuredWidth());
            ViewGroup.LayoutParams layoutParams = seekBar.getLayoutParams();
            layoutParams.width = getMeasuredWidth() - (add.getMeasuredWidth() + minus.getMeasuredWidth());
            seekBar.setLayoutParams(layoutParams);
            seekBar.setX(minus.getMeasuredWidth());
        }))));
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(() -> {
            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        });
    }

    public void setMax(int max) {
        seekBar.setMax(max);
    }

    public void setMin(int min) {
        seekBar.setMin(min);
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setAutoTint(boolean autoTint) {
        add.setAutoTint(autoTint);
        minus.setAutoTint(autoTint);
    }

    public final void setPercentProgressValue(double percentProgressValue) {
        seekBar.setPercentProgressValue(percentProgressValue);
    }

    public final double getPercentProgressValue() {
        return seekBar.getPercentProgressValue();
    }

    public final MutableStateFlow<Double> percentProgressFlow() {
        return seekBar.percentProgressFlow();
    }

    public final void setProgressValue(int progressValue) {
        seekBar.setProgressValue(progressValue);
    }

    public final int getProgressValue() {
        return seekBar.getProgressValue();
    }

    public final MutableStateFlow<Integer> progressFlow() {
        return seekBar.progressFlow();
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
            WeakReference<FCLPreciseSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLPreciseSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLPreciseSeekBar s = ref.get();
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
            WeakReference<FCLPreciseSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLPreciseSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLPreciseSeekBar s = ref.get();
                        if (s != null) {
                            boolean disable = s.disableFlow.getValue();
                            s.add.setEnabled(!disable);
                            s.minus.setEnabled(!disable);
                            s.seekBar.setEnabled(!disable);
                        }
                    });
                }
            });
        }

        return disableFlow;
    }
}
