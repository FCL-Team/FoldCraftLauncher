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

import java.util.ArrayList;
import java.util.List;

public class LogWindow extends ScrollView {

    private final boolean autoTint;
    private FCLTextView textView;
    private int lineCount;
    private final List<Long> timeList = new ArrayList<>();

    public LogWindow(Context context) {
        super(context);
        autoTint = false;
        init(context);
    }

    public LogWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogWindow)) {
            autoTint = typedArray.getBoolean(R.styleable.LogWindow_auto_log_tint, false);
        }
        init(context);
    }

    public LogWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogWindow)) {
            autoTint = typedArray.getBoolean(R.styleable.LogWindow_auto_log_tint, false);
        }
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

    public final void setVisibility(boolean visibility) {
        setVisibility(visibility ? VISIBLE : GONE);
        if (!visibility)
            cleanLog();
    }

    public void appendLog(String str) {
        if (getVisibility() != VISIBLE) {
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
        long now = System.currentTimeMillis();
        synchronized (timeList) {
            timeList.removeIf(time -> now - time > 200);
            timeList.add(now);
            if (timeList.size() > 200) {
                setVisibility(false);
            }
        }
    }

    public void cleanLog() {
        this.textView.setText("");
        lineCount = 0;
    }
}
