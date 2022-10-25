package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLEditText extends AppCompatEditText {

    private final Runnable runnable = () -> {
        int[][] state = {
                {
                        android.R.attr.state_focused
                },
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getColor(),
                Color.GRAY
        };
        setBackgroundTintList(new ColorStateList(state, color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getTextCursorDrawable().setTint(ThemeEngine.getInstance().getTheme().getColor());
        }
    };

    public FCLEditText(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    public FCLEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    public FCLEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerView(this, runnable);
    }
}
