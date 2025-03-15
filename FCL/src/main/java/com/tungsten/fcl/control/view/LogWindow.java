package com.tungsten.fcl.control.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class LogWindow extends ScrollView {

    private boolean autoTint;
    private BooleanProperty visibilityProperty;
    private FCLTextView textView;
    private int lineCount;

    public LogWindow(Context context) {
        super(context);
        autoTint = false;
        init(context);
    }

    public LogWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogWindow);
        autoTint = typedArray.getBoolean(R.styleable.LogWindow_auto_log_tint, false);
        typedArray.recycle();
        init(context);
    }

    public LogWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogWindow);
        autoTint = typedArray.getBoolean(R.styleable.LogWindow_auto_log_tint, false);
        typedArray.recycle();
        init(context);
    }

    public LogWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogWindow);
        autoTint = typedArray.getBoolean(R.styleable.LogWindow_auto_log_tint, false);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        this.textView = new FCLTextView(context);
        textView.setAutoTint(autoTint);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(textView);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(15);
        textView.setLineSpacing(0, 1f);
        textView.setEllipsize(null);
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

                @Override
                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean visible = get();
                        setVisibility(visible ? VISIBLE : GONE);
                        if (!visible) {
                            cleanLog();
                        }
                    });
                }

                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "visibility";
                }
            };
        }

        return visibilityProperty;
    }

    public void appendLog(String str) {
        if (!getVisibilityValue()) {
            return;
        }
        lineCount++;
        this.post(() -> {
            if (textView != null) {
                if (lineCount < 100) {
                    textView.append(str);
                } else {
                    cleanLog();
                }
                fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void cleanLog() {
        this.textView.setText("");
        lineCount = 0;
    }
}
