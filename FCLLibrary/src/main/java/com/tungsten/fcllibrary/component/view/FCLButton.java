package com.tungsten.fcllibrary.component.view;

import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLButton extends AppCompatButton {

    private BooleanProperty visibilityProperty;
    private BooleanProperty disableProperty;

    private final boolean ripple;
    private boolean isDown;

    private GradientDrawable drawableNormal;
    private GradientDrawable drawablePress;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            drawableNormal.setColor(Color.TRANSPARENT);
            drawablePress.setColor(ThemeEngine.getInstance().getTheme().getLtColor());
            if (!ripple) {
                if (isDown) {
                    setBackgroundDrawable(drawablePress);
                    setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
                }
                else {
                    setBackgroundDrawable(drawableNormal);
                    setTextColor(ThemeEngine.getInstance().getTheme().getLtColor());
                }
            } else {
                setRipple();
            }
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

    private void init(int shape, boolean autoPadding) {
        setSingleLine(true);
        setAllCaps(false);
        setGravity(Gravity.CENTER);
        setMinWidth(0);
        setMinHeight(0);
        setMinimumWidth(0);
        setMinimumHeight(0);
        setStateListAnimator(null);
        if (autoPadding) {
            setPadding(
                    ConvertUtils.dip2px(getContext(), shape == GradientDrawable.RECTANGLE ? 16f : 10f),
                    ConvertUtils.dip2px(getContext(), 10f),
                    ConvertUtils.dip2px(getContext(), shape == GradientDrawable.RECTANGLE ? 16f : 10f),
                    ConvertUtils.dip2px(getContext(), 10f)
            );
        }
        drawableNormal = new GradientDrawable();
        drawablePress = new GradientDrawable();
        drawableNormal.setShape(shape);
        drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(), 8));
        drawableNormal.setColor(Color.TRANSPARENT);
        drawablePress.setShape(shape);
        drawablePress.setCornerRadius(ConvertUtils.dip2px(getContext(), 8));
        drawablePress.setColor(ThemeEngine.getInstance().getTheme().getLtColor());
        setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.xml.anim_scale));
    }

    public FCLButton(@NonNull Context context) {
        super(context);
        this.ripple = false;
        init(GradientDrawable.RECTANGLE, true);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLButton);
        boolean ripple = typedArray.getBoolean(R.styleable.FCLButton_ripple, false);
        int shape = typedArray.getInteger(R.styleable.FCLButton_shape, GradientDrawable.RECTANGLE);
        boolean autoPadding = typedArray.getBoolean(R.styleable.FCLButton_auto_padding, true);
        this.ripple = ripple;
        init(shape, autoPadding);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLButton);
        boolean ripple = typedArray.getBoolean(R.styleable.FCLButton_ripple, false);
        int shape = typedArray.getInteger(R.styleable.FCLButton_shape, GradientDrawable.RECTANGLE);
        boolean autoPadding = typedArray.getBoolean(R.styleable.FCLButton_auto_padding, true);
        this.ripple = ripple;
        init(shape, autoPadding);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!ripple) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                isDown = true;
                setBackgroundDrawable(drawablePress);
                setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            }
            if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                isDown = false;
                setBackgroundDrawable(drawableNormal);
                setTextColor(ThemeEngine.getInstance().getTheme().getLtColor());
            }
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setRipple() {
        setBackgroundDrawable(getContext().getDrawable(R.drawable.fcl_button));
        int[][] state = {
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getColor()
        };
        setBackgroundTintList(new ColorStateList(state, color));
        setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
    }

    public boolean isRipple() {
        return ripple;
    }

    public void setShape(int shape) {
        drawableNormal.setShape(shape);
        drawablePress.setShape(shape);
    }

    public int getShape() {
        return ((GradientDrawable) getBackground()).getShape();
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
}
