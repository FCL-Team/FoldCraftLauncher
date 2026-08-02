package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;

public class FCLCheckedTextView extends androidx.appcompat.widget.AppCompatCheckedTextView {

    private boolean autoTint;
    private boolean autoBackgroundTint;

    private void applyTheme() {
        if (autoTint) {
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        }
        if (autoBackgroundTint) {
            setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[]{ ThemeEngine.getInstance().getTheme().getColor() }));
        }
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLCheckedTextView> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLCheckedTextView self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public FCLCheckedTextView(@NonNull Context context) {
        super(context);
        autoTint = false;
        autoBackgroundTint = false;
        bindTheme();
    }

    public FCLCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckedTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_background_tint, false);
        typedArray.recycle();
        bindTheme();
    }

    public FCLCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLCheckedTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLCheckedTextView_auto_checked_text_background_tint, false);
        typedArray.recycle();
        bindTheme();
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
