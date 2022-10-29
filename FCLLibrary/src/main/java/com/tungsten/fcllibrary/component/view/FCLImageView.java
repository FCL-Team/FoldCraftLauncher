package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLImageView extends AppCompatImageView {

    private boolean autoTint;

    private final Runnable runnable = () -> {
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

    public FCLImageView(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageView);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageView_auto_src_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageView);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageView_auto_src_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }
}
