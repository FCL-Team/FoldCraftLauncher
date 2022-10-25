package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLButton extends AppCompatButton {

    private boolean isDown;

    private GradientDrawable drawableNormal;
    private GradientDrawable drawablePress;

    private final Runnable runnable = () -> {
        drawableNormal.setStroke(ConvertUtils.dip2px(getContext(), 1.5f), Color.GRAY);
        drawableNormal.setColor(Color.TRANSPARENT);
        drawablePress.setStroke(ConvertUtils.dip2px(getContext(), 1.5f), ThemeEngine.getInstance().getTheme().getColor());
        drawablePress.setColor(ThemeEngine.getInstance().getTheme().getLtColor());
        if (isDown) {
            setBackgroundDrawable(drawablePress);
        }
        else {
            setBackgroundDrawable(drawableNormal);
        }
    };

    private void init(int shape) {
        setSingleLine(true);
        setAllCaps(false);
        setGravity(Gravity.CENTER);
        setMinWidth(0);
        setMinHeight(0);
        setMinimumWidth(0);
        setMinimumHeight(0);
        setPadding(
                ConvertUtils.dip2px(getContext(), shape == GradientDrawable.RECTANGLE ? 16f : 10f),
                ConvertUtils.dip2px(getContext(), 10f),
                ConvertUtils.dip2px(getContext(), shape == GradientDrawable.RECTANGLE ? 16f : 10f),
                ConvertUtils.dip2px(getContext(), 10f)
        );
        drawableNormal = new GradientDrawable();
        drawablePress = new GradientDrawable();
        drawableNormal.setShape(shape);
        drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(), 8));
        drawableNormal.setStroke(ConvertUtils.dip2px(getContext(), 1.5f), Color.GRAY);
        drawableNormal.setColor(Color.TRANSPARENT);
        drawablePress.setShape(shape);
        drawablePress.setCornerRadius(ConvertUtils.dip2px(getContext(), 5));
        drawablePress.setStroke(ConvertUtils.dip2px(getContext(), 1.5f), Color.GRAY);
        drawablePress.setColor(ThemeEngine.getInstance().getTheme().getLtColor());
    }

    public FCLButton(@NonNull Context context) {
        super(context);
        init(GradientDrawable.RECTANGLE);
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    public FCLButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLButton);
        int shape = typedArray.getInteger(R.styleable.FCLButton_shape, GradientDrawable.RECTANGLE);
        init(shape);
        typedArray.recycle();
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    public FCLButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLButton);
        int shape = typedArray.getInteger(R.styleable.FCLButton_shape, GradientDrawable.RECTANGLE);
        init(shape);
        typedArray.recycle();
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            isDown = true;
            setBackgroundDrawable(drawablePress);
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            isDown = false;
            setBackgroundDrawable(drawableNormal);
        }
        return super.onTouchEvent(event);
    }

    public void setShape(int shape) {
        drawableNormal.setShape(shape);
        drawablePress.setShape(shape);
    }

    public int getShape() {
        return ((GradientDrawable) getBackground()).getShape();
    }
}
