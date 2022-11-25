package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLMenuView extends AppCompatImageButton {

    private boolean isSelected;
    private OnSelectListener onSelectListener;

    private final Runnable runnable = () -> {
        int[][] state = {
                {

                }
        };
        int[] colorNormal = {
                ThemeEngine.getInstance().getTheme().getAutoTint()
        };
        int[] colorSelected = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        int[] colorRipple = {
                ThemeEngine.getInstance().getTheme().getLtColor()
        };
        setImageTintList(new ColorStateList(state, isSelected ? colorSelected : colorNormal));
        RippleDrawable drawable = new RippleDrawable(new ColorStateList(state, colorRipple), null, null);
        drawable.setRadius(ConvertUtils.dip2px(getContext(), 20));
        setBackgroundDrawable(drawable);
    };

    private void init() {
        setPadding(
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f),
                ConvertUtils.dip2px(getContext(), 8f)
        );
        setScaleType(ScaleType.FIT_XY);
        setOnClickListener(view -> {
            if (!isSelected) {
                setSelected(true);
            }
        });
    }

    public void setSelected(boolean selected) {
        if (!isSelected && selected && onSelectListener != null) {
            onSelectListener.onSelect(this);
        }
        isSelected = selected;
        int[][] state = {
                {

                }
        };
        int[] colorNormal = {
                ThemeEngine.getInstance().getTheme().getAutoTint()
        };
        int[] colorSelected = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setImageTintList(new ColorStateList(state, isSelected ? colorSelected : colorNormal));
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public FCLMenuView(@NonNull Context context) {
        super(context);
        Schedulers.androidUIThread().execute(() -> {
            init();
            ThemeEngine.getInstance().registerEvent(this, runnable);
        });
    }

    public FCLMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Schedulers.androidUIThread().execute(() -> {
            init();
            ThemeEngine.getInstance().registerEvent(this, runnable);
        });
    }

    public FCLMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Schedulers.androidUIThread().execute(() -> {
            init();
            ThemeEngine.getInstance().registerEvent(this, runnable);
        });
    }

    public interface OnSelectListener {
        void onSelect(FCLMenuView view);
    }
}
