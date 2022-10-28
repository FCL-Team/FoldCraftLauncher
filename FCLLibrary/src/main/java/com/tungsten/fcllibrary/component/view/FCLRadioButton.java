package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLRadioButton extends AppCompatRadioButton {

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

    public FCLRadioButton(Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

    public FCLRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerEvent(this, runnable);
    }

}
