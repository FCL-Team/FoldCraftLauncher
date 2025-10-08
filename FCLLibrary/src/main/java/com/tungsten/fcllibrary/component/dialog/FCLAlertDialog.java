package com.tungsten.fcllibrary.component.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLAlertDialog extends FCLDialog implements View.OnClickListener {

    private String titleString;

    private ButtonListener positiveListener;
    private ButtonListener negativeListener;
    private ButtonListener neutralListener;

    private View parent;
    private ImageFilterView icon;
    private FCLTextView title;
    private ScrollView scrollView;
    private FCLTextView message;
    private FCLButton positive;
    private FCLButton negative;
    private FCLButton neutral;

    @SuppressLint("UseCompatLoadingForDrawables")
    public FCLAlertDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_alert);

        parent = findViewById(R.id.parent);

        icon = findViewById(R.id.image);
        title = findViewById(R.id.title);
        scrollView = findViewById(R.id.text_scroll);
        message = findViewById(R.id.text);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        neutral = findViewById(R.id.neutral);

        checkHeight();

        positive.setVisibility(View.GONE);
        negative.setVisibility(View.GONE);
        neutral.setVisibility(View.GONE);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
        neutral.setOnClickListener(this);

        icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_info_24));
        title.setText(getContext().getString(R.string.dialog_info));
    }

    private void checkHeight() {
        parent.post(() -> message.post(() -> {
            WindowManager wm = getWindow().getWindowManager();
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            int maxHeight = point.y - ConvertUtils.dip2px(getContext(), 30);
            if (parent.getMeasuredHeight() < maxHeight) {
                ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                layoutParams.height = message.getMeasuredHeight();
                scrollView.setLayoutParams(layoutParams);
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            } else {
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, maxHeight);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        ButtonListener listener = null;
        if (view == positive) listener = positiveListener;
        else if (view == negative) listener = negativeListener;
        else if (view == neutral) listener = neutralListener;

        if (listener != null) {
            listener.onClick();
        }
        dismiss();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setAlertLevel(AlertLevel alertLevel) {
        switch (alertLevel) {
            case ALERT:
                icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_warning_24));
                if (titleString == null) {
                    title.setText(getContext().getString(R.string.dialog_alert));
                }
                break;
            default:
                icon.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_info_24));
                if (titleString == null) {
                    title.setText(getContext().getString(R.string.dialog_info));
                }
                break;
        }
    }

    public void setTitle(String title) {
        titleString = title;
        this.title.setText(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
        checkHeight();
    }

    public void setMessage(CharSequence message) {
        this.message.setText(message);
        checkHeight();
    }

    public void setMessage(Spanned message) {
        this.message.setText(message);
        checkHeight();
    }

    public void setPositiveButton(String text, ButtonListener listener) {
        positive.setVisibility(View.VISIBLE);
        positive.setText(text);
        positiveListener = listener;
    }

    public void setNegativeButton(String text, ButtonListener listener) {
        negative.setVisibility(View.VISIBLE);
        negative.setText(text);
        negativeListener = listener;
    }

    public void setNeutralButton(String text, ButtonListener listener) {
        neutral.setVisibility(View.VISIBLE);
        neutral.setText(text);
        neutralListener = listener;
    }

    public static class Builder {

        private Context context;
        private FCLAlertDialog dialog;

        public Builder(Context context) {
            this.context = context;
            dialog = new FCLAlertDialog(context);
        }

        public FCLAlertDialog create() {
            return dialog;
        }

        public Builder setAlertLevel(AlertLevel alertLevel) {
            dialog.setAlertLevel(alertLevel);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        public Builder setTitle(String title) {
            dialog.setTitle(title);
            return this;
        }

        public Builder setMessage(String message) {
            dialog.setMessage(message);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            dialog.setMessage(message);
            return this;
        }

        public Builder setPositiveButton(ButtonListener listener) {
            dialog.setPositiveButton(context.getString(R.string.dialog_positive), listener);
            return this;
        }

        public Builder setPositiveButton(String text, ButtonListener listener) {
            dialog.setPositiveButton(text, listener);
            return this;
        }

        public Builder setNegativeButton(ButtonListener listener) {
            dialog.setNegativeButton(context.getString(R.string.dialog_negative), listener);
            return this;
        }

        public Builder setNegativeButton(String text, ButtonListener listener) {
            dialog.setNegativeButton(text, listener);
            return this;
        }

        public Builder setNeutralButton(String text, ButtonListener listener) {
            dialog.setNeutralButton(text, listener);
            return this;
        }
    }

    public enum AlertLevel {
        ALERT,
        INFO
    }

    public interface ButtonListener {
        void onClick();
    }

}
