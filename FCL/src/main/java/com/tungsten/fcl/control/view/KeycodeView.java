package com.tungsten.fcl.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;

import java.util.ArrayList;

public class KeycodeView extends androidx.appcompat.widget.AppCompatButton {

    private final Integer keycode;

    private OnKeycodeChangeListener onKeycodeChangeListener;

    private boolean selected = false;

    private long downTime;
    private float initialX;
    private float initialY;

    public KeycodeView(@NonNull Context context) {
        super(context);
        this.keycode = -1;
        checkSelection(new ArrayList<>());
        setEnabled(false);
    }

    public KeycodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeycodeView);
        this.keycode = typedArray.getInteger(R.styleable.KeycodeView_keycode, -1);
        typedArray.recycle();
        checkSelection(new ArrayList<>());
        setEnabled(keycode != -1);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                initialX = event.getX();
                initialY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(event.getX() - initialX) <= 10 && Math.abs(event.getY() - initialY) <= 10 && System.currentTimeMillis() - downTime <= 200) {
                    if (selected) {
                        this.setBackground(getContext().getDrawable(R.drawable.keycode_view_normal));
                        selected = false;
                        if (onKeycodeChangeListener != null) {
                            onKeycodeChangeListener.onKeycodeRemove(this, keycode);
                        }
                    } else {
                        this.setBackground(getContext().getDrawable(R.drawable.keycode_view_selected));
                        selected = true;
                        if (onKeycodeChangeListener != null) {
                            onKeycodeChangeListener.onKeycodeAdd(this, keycode);
                        }
                    }
                }
                break;
        }
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setSelectedWithoutCallback(boolean selected) {
        if (selected) {
            this.setBackground(getContext().getDrawable(R.drawable.keycode_view_selected));
        } else {
            this.setBackground(getContext().getDrawable(R.drawable.keycode_view_normal));
        }
        this.selected = selected;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void checkSelection(ArrayList<Integer> list) {
        if (list.contains(keycode)) {
            this.setBackground(getContext().getDrawable(R.drawable.keycode_view_selected));
            selected = true;
        } else {
            this.setBackground(getContext().getDrawable(R.drawable.keycode_view_normal));
            selected = false;
        }
    }

    public void setOnKeycodeChangeListener(OnKeycodeChangeListener onKeycodeChangeListener) {
        this.onKeycodeChangeListener = onKeycodeChangeListener;
    }

    public interface OnKeycodeChangeListener {
        void onKeycodeAdd(KeycodeView view, int keycode);
        void onKeycodeRemove(KeycodeView view, int keycode);
    }
}
