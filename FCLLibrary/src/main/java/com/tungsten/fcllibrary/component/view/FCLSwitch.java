package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLSwitch extends SwitchCompat {

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
                Color.LTGRAY
        };
        int[] subColor = {
                ThemeEngine.getInstance().getTheme().getLtColor(),
                Color.GRAY
        };
        setThumbTintList(new ColorStateList(state, color));
        setTrackTintList(new ColorStateList(state, subColor));
    };

    public FCLSwitch(@NonNull Context context) {
        super(context);
        Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().registerEvent(this, runnable));
    }

    public FCLSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().registerEvent(this, runnable));
    }

    public FCLSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().registerEvent(this, runnable));
    }
}
