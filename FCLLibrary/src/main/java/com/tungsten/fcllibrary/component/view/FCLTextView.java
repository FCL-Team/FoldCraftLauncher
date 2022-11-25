package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLTextView extends AppCompatTextView {

    private boolean autoTint;

    private final Runnable runnable = () -> {
        if (autoTint) {
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        }
    };

    public FCLTextView(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public void alert() {
        setTextColor(Color.RED);
    }

    public void normal() {
        setTextColor(Color.GRAY);
    }

    public void emphasize() {
        setTextColor(Color.BLACK);
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }
}
