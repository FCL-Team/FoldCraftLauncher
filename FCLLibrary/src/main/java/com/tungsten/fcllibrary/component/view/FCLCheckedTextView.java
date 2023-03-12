package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLCheckedTextView extends androidx.appcompat.widget.AppCompatCheckedTextView {

    private boolean autoTint;
    private boolean autoBackgroundTint;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            if (autoTint) {
                setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            }
            if (autoBackgroundTint) {
                setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[]{ ThemeEngine.getInstance().getTheme().getColor() }));
            }
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

    public FCLCheckedTextView(@NonNull Context context) {
        super(context);
        autoTint = false;
        autoBackgroundTint = false;
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckedTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_background_tint, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckedTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_background_tint, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
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
