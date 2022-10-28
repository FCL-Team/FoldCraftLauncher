package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLProgressBar extends ProgressBar {

    private final Runnable runnable = () -> {
        int[][] state = {
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setProgressTintList(new ColorStateList(state, color));
        setIndeterminateTintList(new ColorStateList(state, color));
    };

    public FCLProgressBar(Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }
}
