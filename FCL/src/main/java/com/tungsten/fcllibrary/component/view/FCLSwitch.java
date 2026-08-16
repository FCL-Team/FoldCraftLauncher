package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLSwitch extends SwitchCompat {

    private boolean fromUserOrSystem = false;
    private BooleanProperty visibilityProperty;
    private BooleanProperty checkProperty;
    private BooleanProperty disableProperty;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            int[][] state = {
                    {
                            android.R.attr.state_checked
                    },
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getInstance().getTheme().getDkColor(),
                    ThemeEngine.getInstance().getTheme().getColor() | 0xFF000000
            };
            int[] subColor = {
                    ThemeEngine.getInstance().getTheme().getColor() | 0xFF000000,
                    Color.GRAY
            };
            setThumbTintList(new ColorStateList(state, color));
            setTrackTintList(new ColorStateList(state, subColor));
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
    }

    public void addCheckedChangeListener() {
        setOnCheckedChangeListener((compoundButton, b) -> {
            fromUserOrSystem = true;
            checkProperty().set(b);
        });
    }

    public FCLSwitch(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
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

    public final void setCheckValue(boolean isChecked) {
        checkProperty().set(isChecked);
    }

    public final boolean getCheckValue() {
        return checkProperty == null || checkProperty.get();
    }

    public final BooleanProperty checkProperty() {
        if (checkProperty == null) {
            checkProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        if (!fromUserOrSystem) {
                            boolean isCheck = get();
                            setChecked(isCheck);
                        }
                        fromUserOrSystem = false;
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "check";
                }
            };
        }

        return checkProperty;
    }

    public final void setDisableValue(boolean disableValue) {
        disableProperty().set(disableValue);
    }

    public final boolean getDisableValue() {
        return disableProperty == null || disableProperty.get();
    }

    public final BooleanProperty disableProperty() {
        if (disableProperty == null) {
            disableProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean disable = get();
                        setEnabled(!disable);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "disable";
                }
            };
        }

        return disableProperty;
    }
}
