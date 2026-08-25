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

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.DoublePropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcl.R;
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
    private ShapeDrawable thumbDrawable;
    private Rect textBounds;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
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

    /** 动态设置数值后缀（% / MS 等），触发 thumb 重建 */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.thumbDrawable = null;
        invalidate();
    }

    public void addProgressListener() {
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // 仅用户操作时同步属性，程序性变化（setMax/setMin 造成的进度钳制）
                // 不进入进度属性，避免误触发布置在属性上的监听
                if (b) {
                    fromUserOrSystem = true;
                    progressProperty().set(i);
                    fromUserOrSystem = false;
                }
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
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
        init(null);
    }

    public FCLNumberSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
        init(attrs);
    }

    public FCLNumberSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
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

    @Override
    public void setProgress(int progress) {
        super.setProgress(progress);
        // 程序性设置进度时同步属性，触发监听与保存（onProgressChanged 仅用户操作才同步）
        progressProperty().set(progress);
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
        String text = getProgress() + suffix;
        float textWidth = textPaint.measureText(text);
        // 数值文本居中绘制在滑块处（CENTER 对齐），贴近两端时会超出布局被裁剪，
        // 这里限制文本中心点，使文本完整落在 padding 范围内
        float centerX = computeThumbX();
        if (centerX - textWidth / 2f < getPaddingStart()) {
            centerX = getPaddingStart() + textWidth / 2f;
        }
        if (centerX + textWidth / 2f > getWidth() - getPaddingEnd()) {
            centerX = getWidth() - getPaddingEnd() - textWidth / 2f;
        }
        canvas.drawText(text, centerX, textY, textPaint);
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
