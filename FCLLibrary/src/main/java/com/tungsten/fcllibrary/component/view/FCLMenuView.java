package com.tungsten.fcllibrary.component.view;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLMenuView extends AppCompatImageButton {

    private boolean isSelected;
    private OnSelectListener onSelectListener;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
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
        int[] colorSelected = {
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setImageTintList(new ColorStateList(state, isSelected ? colorSelected : colorNormal));
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
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public interface OnSelectListener {
        void onSelect(FCLMenuView view);
    }
}
