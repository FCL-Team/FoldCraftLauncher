package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLTabLayout extends TabLayout {

    private final Runnable runnable = () -> {
        int[][] state = {
                {
                        android.R.attr.state_selected
                },
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor(),
                Color.GRAY
        };
        setSelectedTabIndicatorColor(ThemeEngine.getInstance().getTheme().getDkColor());
        setTabTextColors(new ColorStateList(state, color));
        setTabIconTint(new ColorStateList(state, color));
    };

    public FCLTabLayout(@NonNull Context context) {
        super(context);
        Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().registerEvent(this, runnable));
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().registerEvent(this, runnable));
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().registerEvent(this, runnable));
    }
}
