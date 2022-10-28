package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLSeekBar extends AppCompatSeekBar {

    private final Runnable runnable = () -> {
        int[][] state = {
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setThumbTintList(new ColorStateList(state, color));
        setProgressTintList(new ColorStateList(state, color));
    };

    public FCLSeekBar(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }
}
