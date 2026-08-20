package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLCheckedTextView extends androidx.appcompat.widget.AppCompatCheckedTextView {

    private boolean autoTint;
    private boolean autoBackgroundTint;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            if (autoTint) {
                setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            }
            if (autoBackgroundTint) {
                setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[]{ ThemeEngine.getInstance().getTheme().getColor() }));
            }
    }

    public FCLCheckedTextView(@NonNull Context context) {
        super(context);
        autoTint = false;
        autoBackgroundTint = false;
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckedTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_background_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckedTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_background_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setAutoBackgroundTint(boolean autoBackgroundTint) {
        this.autoBackgroundTint = autoBackgroundTint;
    }

    public boolean isAutoBackgroundTint() {
        return autoBackgroundTint;
    }
}
