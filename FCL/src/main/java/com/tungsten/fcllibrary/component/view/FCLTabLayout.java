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
    private final boolean autoTextTint;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            int[][] state = {
                    {
                            android.R.attr.state_selected
                    },
                    {

                    }
            };
            // 图标 Tab 与界面文字同色系：选中为对比色，未选中为半透明对比色（当前仅游戏菜单使用图标 Tab）
            int[] iconColor = {
                    ThemeEngine.getInstance().getTheme().getAutoTint(),
                    ThemeEngine.getInstance().getTheme().getAutoHintTint()
            };
            // 文字 Tab 默认选中为主题深色、未选中灰；autoTextTint 开启后与图标 Tab 同色系
            int[] color = autoTextTint ? iconColor : new int[]{
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
            setTabIconTint(new ColorStateList(state, iconColor));
            if (followTheme) {
                setBackgroundTintList(new ColorStateList(bgState, bgColor));
            }
    }

    public FCLTabLayout(@NonNull Context context) {
        super(context);
        followTheme = false;
        autoTextTint = false;
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTabLayout);
        followTheme = typedArray.getBoolean(R.styleable.FCLTabLayout_follow_theme, false);
        autoTextTint = typedArray.getBoolean(R.styleable.FCLTabLayout_auto_text_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTabLayout);
        followTheme = typedArray.getBoolean(R.styleable.FCLTabLayout_follow_theme, false);
        autoTextTint = typedArray.getBoolean(R.styleable.FCLTabLayout_auto_text_tint, false);
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
