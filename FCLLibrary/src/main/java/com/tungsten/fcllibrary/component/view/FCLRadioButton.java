package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLRadioButton extends AppCompatRadioButton {

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
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
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme";
        }
    };

    public FCLRadioButton(Context context) {
        super(context);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

}
