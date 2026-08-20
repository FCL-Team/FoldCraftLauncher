package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLTabLayout extends TabLayout {

    private BooleanProperty visibilityProperty;
    private final boolean followTheme;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            int[][] state = {
                    {
                            android.R.attr.state_selected
                    },
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getInstance().getTheme().getDkColor(),
                    followTheme ? ThemeEngine.getInstance().getTheme().getAutoTint() : Color.GRAY
            };
            int[][] bgState = {
                    {

                    }
            };
            int[] bgColor = {
                    ThemeEngine.getInstance().getTheme().getLtColor()
            };
            setSelectedTabIndicatorColor(ThemeEngine.getInstance().getTheme().getDkColor());
            setTabTextColors(new ColorStateList(state, color));
            setTabIconTint(new ColorStateList(state, color));
            if (followTheme) {
                setBackgroundTintList(new ColorStateList(bgState, bgColor));
            }
    }

    public FCLTabLayout(@NonNull Context context) {
        super(context);
        followTheme = false;
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTabLayout);
        followTheme = typedArray.getBoolean(R.styleable.FCLTabLayout_follow_theme, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTabLayout);
        followTheme = typedArray.getBoolean(R.styleable.FCLTabLayout_follow_theme, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public boolean isFollowTheme() {
        return followTheme;
    }

    public final void setVisibilityValue(boolean visibility) {
        visibilityProperty().set(visibility);
    }

    public final boolean getVisibilityValue() {
        return visibilityProperty == null || visibilityProperty.get();
    }

    public final BooleanProperty visibilityProperty() {
        if (visibilityProperty == null) {
            visibilityProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean visible = get();
                        setVisibility(visible ? VISIBLE : GONE);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "visibility";
                }
            };
        }

        return visibilityProperty;
    }
}
