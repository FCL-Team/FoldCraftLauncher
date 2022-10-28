package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLImageButton extends AppCompatImageButton {

    private boolean autoTint;

    private boolean isDown;

    private GradientDrawable drawableNormal;
    private GradientDrawable drawablePress;

    private final Runnable runnable = () -> {
        drawableNormal.setColor(Color.TRANSPARENT);
        drawablePress.setColor(ThemeEngine.getInstance().getTheme().getLtColor());
        if (isDown) {
            setBackgroundDrawable(drawablePress);
        }
        else {
            setBackgroundDrawable(drawableNormal);
        }
        if (autoTint) {
            int[][] state = {
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getInstance().getTheme().getAutoTint()
            };
            setImageTintList(new ColorStateList(state, color));
        }
    };

    private void init() {
        setPadding(
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f)
        );
        setScaleType(ScaleType.FIT_XY);
        drawableNormal = new GradientDrawable();
        drawablePress = new GradientDrawable();
        drawableNormal.setShape(GradientDrawable.OVAL);
        drawablePress.setShape(GradientDrawable.OVAL);
        drawableNormal.setColor(Color.TRANSPARENT);
        drawablePress.setColor(ThemeEngine.getInstance().getTheme().getLtColor());
    }

    public FCLImageButton(@NonNull Context context) {
        super(context);
        init();
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageButton);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageButton_auto_tint, false);
        typedArray.recycle();
        init();
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageButton);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageButton_auto_tint, false);
        typedArray.recycle();
        init();
        ThemeEngine.getInstance().registerEvent(this, runnable);
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

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }
}
