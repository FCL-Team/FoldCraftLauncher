package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.DoublePropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.util.Optional;

public class FCLNumberSeekBar extends AppCompatSeekBar {

    private boolean fromUserOrSystem = false;
    private BooleanProperty visibilityProperty;
    private BooleanProperty disableProperty;
    private DoubleProperty percentProgressProperty;
    private IntegerProperty progressProperty;
    private Paint textPaint;
    private String suffix;
    private GestureDetector gestureDetector;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
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

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme";
        }
    };

    public void addProgressListener() {
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fromUserOrSystem = true;
                progressProperty().set(i);
                percentProgressProperty().set((double) i / (double) getMax());
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
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        init(null);
    }

    public FCLNumberSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        init(attrs);
    }

    public FCLNumberSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FCLNumberSeekBar);
            suffix = Optional.ofNullable(typedArray.getString(R.styleable.FCLNumberSeekBar_suffix)).orElse("");
            typedArray.recycle();
        }
        setThumb(AppCompatResources.getDrawable(getContext(), R.drawable.transparent));
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {
                EditDialog dialog = new EditDialog(getContext(), s -> {
                    try {
                        int i = Integer.parseInt(s);
                        if (i >= getMin() && i <= getMax()) {
                            setProgress(i);
                        }
                    } catch (Throwable ignore) {
                    }
                });
                dialog.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                dialog.show();
                return true;
            }
        });
    }

    public final void setVisibilityValue(boolean visibility) {
        visibilityProperty().set(visibility);
    }

    public final boolean getVisibilityValue() {
        return visibilityProperty == null || visibilityProperty.get();
    }

    public final BooleanProperty visibilityProperty() {
        if (visibilityProperty == null) {
            visibilityProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean visible = get();
                        setVisibility(visible ? VISIBLE : GONE);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "visibility";
                }
            };
        }

        return visibilityProperty;
    }

    public final void setDisableValue(boolean disableValue) {
        disableProperty().set(disableValue);
    }

    public final boolean getDisableValue() {
        return disableProperty == null || disableProperty.get();
    }

    public final BooleanProperty disableProperty() {
        if (disableProperty == null) {
            disableProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean disable = get();
                        setEnabled(!disable);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "disable";
                }
            };
        }

        return disableProperty;
    }

    public final void setPercentProgressValue(double percentProgressValue) {
        percentProgressProperty().set(percentProgressValue);
    }

    public final double getPercentProgressValue() {
        return percentProgressProperty == null ? -1 : percentProgressProperty.get();
    }

    public final DoubleProperty percentProgressProperty() {
        if (percentProgressProperty == null) {
            percentProgressProperty = new DoublePropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        if (!fromUserOrSystem) {
                            double progress = get();
                            setProgress((int) (progress * getMax()));
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "percentProgress";
                }
            };
        }

        return percentProgressProperty;
    }

    public final void setProgressValue(int progressValue) {
        progressProperty().set(progressValue);
    }

    public final int getProgressValue() {
        return progressProperty == null ? -1 : progressProperty().get();
    }

    public final IntegerProperty progressProperty() {
        if (progressProperty == null) {
            progressProperty = new IntegerPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        if (!fromUserOrSystem) {
                            int progress = get();
                            setProgress(progress);
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "progress";
                }
            };
        }

        return progressProperty;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth() - getPaddingStart() - getPaddingEnd();
        float progressRatio = (float) (getProgress() - getMin()) / (getMax() - getMin());
        float thumbX = getPaddingStart() + (width * progressRatio);
        float textY = getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
        canvas.drawText(getProgress() + suffix, thumbX, textY, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
