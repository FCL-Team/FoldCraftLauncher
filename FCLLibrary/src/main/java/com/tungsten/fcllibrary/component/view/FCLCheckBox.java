package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLCheckBox extends AppCompatCheckBox {

    private final Runnable runnable = () -> {
        int[][] state = {
                {
                        android.R.attr.state_checked
                },
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor(),
                Color.GRAY
        };
        setButtonTintList(new ColorStateList(state, color));
    };

    public FCLCheckBox(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    public FCLCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerView(this, runnable);
    }

    public FCLCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerView(this, runnable);
    }
}
