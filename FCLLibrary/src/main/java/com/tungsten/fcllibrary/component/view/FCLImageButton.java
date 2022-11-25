package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLImageButton extends AppCompatImageButton {

    private boolean autoTint;

    private final Runnable runnable = () -> {
        int[][] state = {
                {

                }
        };
        int[] colorSrc = {
                ThemeEngine.getInstance().getTheme().getAutoTint()
        };
        int[] colorRipple = {
                ThemeEngine.getInstance().getTheme().getLtColor()
        };
        if (autoTint) {
            setImageTintList(new ColorStateList(state, colorSrc));
        }
        RippleDrawable drawable = new RippleDrawable(new ColorStateList(state, colorRipple), null, null);
        drawable.setRadius(ConvertUtils.dip2px(getContext(), 20));
        setBackgroundDrawable(drawable);
    };

    private void init() {
        setPadding(
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f)
        );
        setScaleType(ScaleType.FIT_XY);
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

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }
}
