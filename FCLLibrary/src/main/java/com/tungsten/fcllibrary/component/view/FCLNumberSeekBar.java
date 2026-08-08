package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;
import java.util.Optional;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLNumberSeekBar extends AppCompatSeekBar {

    private boolean fromUserOrSystem = false;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> disableFlow;
    private MutableStateFlow<Double> percentProgressFlow;
    private MutableStateFlow<Integer> progressFlow;
    private Paint textPaint;
    private String suffix;
    private GestureDetector gestureDetector;
    private ShapeDrawable thumbDrawable;
    private Rect textBounds;

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
        WeakReference<FCLNumberSeekBar> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLNumberSeekBar self = ref.get();
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

    public FCLNumberSeekBar(@NonNull Context context) {
        super(context);
        bindTheme();
        init(null);
    }

    public FCLNumberSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bindTheme();
        init(attrs);
    }

    public FCLNumberSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindTheme();
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FCLNumberSeekBar);
            suffix = Optional.ofNullable(typedArray.getString(R.styleable.FCLNumberSeekBar_suffix)).orElse("");
            typedArray.recycle();
        }
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                if (e.getX() >= computeThumbX() - textBounds.width() / 2f && e.getX() <= computeThumbX() + textBounds.width() / 2f) {
                    EditDialog dialog = new EditDialog(getContext(), s -> {
                        try {
                            int i = Integer.parseInt(s);
                            if (i >= getMin() && i <= getMax()) {
                                setProgress(i);
                            }
                        } catch (Throwable ignore) {
                        }
                    });
                    dialog.appendTitle("(" + getMin() +" ~ " + getMax() + ")");
                    dialog.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
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
            WeakReference<FCLNumberSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLNumberSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLNumberSeekBar s = ref.get();
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
            WeakReference<FCLNumberSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLNumberSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLNumberSeekBar s = ref.get();
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

    public final void setProgressValue(int progressValue) {
        progressFlow().setValue(progressValue);
    }

    public final int getProgressValue() {
        return progressFlow == null ? -1 : progressFlow().getValue();
    }

    public final MutableStateFlow<Integer> progressFlow() {
        if (progressFlow == null) {
            progressFlow = StateFlowKt.MutableStateFlow(0);
            WeakReference<FCLNumberSeekBar> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(progressFlow, v -> {
                FCLNumberSeekBar self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLNumberSeekBar s = ref.get();
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

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (thumbDrawable == null) {
            textBounds = new Rect();
            textPaint.getTextBounds(getMax() + suffix, 0, (getMax() + suffix).length(), textBounds);
            thumbDrawable = new ShapeDrawable();
            thumbDrawable.setShape(new OvalShape());
            thumbDrawable.getPaint().setColor(Color.TRANSPARENT);
            thumbDrawable.setIntrinsicHeight(getHeight());
            thumbDrawable.setIntrinsicWidth(textBounds.width());
            setThumb(thumbDrawable);
        }
        textPaint.setTextSize(getHeight() / 1.5f);
        float textY = getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
        canvas.drawText(getProgress() + suffix, computeThumbX(), textY, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private float computeThumbX() {
        float width = getWidth() - getPaddingStart() - getPaddingEnd();
        float progressRatio = (float) (getProgress() - getMin()) / (getMax() - getMin());
        return getPaddingStart() + (width * progressRatio);
    }
}
