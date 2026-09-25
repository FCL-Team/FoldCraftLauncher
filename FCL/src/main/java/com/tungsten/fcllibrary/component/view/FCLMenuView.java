package com.tungsten.fcllibrary.component.view;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLMenuView extends AppCompatImageButton {

    private boolean isSelected;
    private OnSelectListener onSelectListener;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            int[][] state = {
                    {

                    }
            };
            int[] colorNormal = {
                    ThemeEngine.getInstance().getTheme().getAutoTint()
            };
            int[] colorRipple = {
                    ThemeEngine.getInstance().getTheme().getLtColor()
            };
            setImageTintList(new ColorStateList(state, isSelected ? colorSelected() : colorNormal));
            RippleDrawable drawable = new RippleDrawable(new ColorStateList(state, colorRipple), null, null);
            drawable.setRadius(ConvertUtils.dip2px(getContext(), 20));
            setBackgroundDrawable(drawable);
    }

    /** 选中态色：dkColor 不透明形态，主题色透明度不影响菜单选中的可见性 */
    private int[] colorSelected() {
        return new int[]{ThemeEngine.getInstance().getTheme().getOpaqueDkColor()};
    }

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
        setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.xml.anim_scale_large));
    }

    public void setSelected(boolean selected) {
        final boolean oldSelect = isSelected;
        isSelected = selected;
        int[][] state = {
                {

                }
        };
        int[] colorNormal = {
                ThemeEngine.getInstance().getTheme().getAutoTint()
        };
        setImageTintList(new ColorStateList(state, isSelected ? colorSelected() : colorNormal));
        if (!oldSelect && selected && onSelectListener != null) {
            onSelectListener.onSelect(this);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public FCLMenuView(@NonNull Context context) {
        super(context);
        init();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public interface OnSelectListener {
        void onSelect(FCLMenuView view);
    }
}
