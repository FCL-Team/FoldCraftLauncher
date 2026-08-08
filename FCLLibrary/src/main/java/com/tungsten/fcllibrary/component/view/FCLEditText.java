package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLEditText extends AppCompatEditText {

    private boolean autoTint;
    public boolean fromUserOrSystem = false;
    private MutableStateFlow<Boolean> visibilityFlow;
    private MutableStateFlow<Boolean> disableFlow;
    private MutableStateFlow<Boolean> focusedFlow;
    private MutableStateFlow<String> stringFlow;

    private final Thread focusListener = new Thread(() -> {
        if (focusedFlow == null) {
            focusedFlow = StateFlowKt.MutableStateFlow(false);
        }
        while (true) {
            focusedFlow.setValue(isFocused());
        }
    });

    public void runFocusListener() {
        Schedulers.androidUIThread().execute(() -> {
            focusListener.setPriority(Thread.MIN_PRIORITY);
            focusListener.start();
        });
    }

    private void applyTheme() {
        int[][] state = {
                {
                        android.R.attr.state_focused
                },
                {

                }
        };
        int[] color = {
                ThemeEngine.getInstance().getTheme().getColor(),
                ThemeEngine.getInstance().getTheme().getDkColor()
        };
        setBackgroundTintList(new ColorStateList(state, color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getTextCursorDrawable().setTint(ThemeEngine.getInstance().getTheme().getColor());
        }
        if (autoTint) {
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            setHintTextColor(ThemeEngine.getInstance().getTheme().getAutoHintTint());
        }
    }

    private void bindTheme() {
        // 弱引用自身：对齐原 theme.bind(...) 的弱监听语义，视图可被 GC
        WeakReference<FCLEditText> ref = new WeakReference<>(this);
        FlowSubscriptions.subscribeWithCurrent(ThemeEngine.getInstance().getTheme().colorFlow(), c -> {
            FCLEditText self = ref.get();
            if (self != null) {
                self.applyTheme();
            }
        });
    }

    public void addTextWatcher() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fromUserOrSystem = true;
                stringFlow().setValue(getText().toString());
                fromUserOrSystem = false;
            }
        });
    }

    public void addTextWatcher(@NonNull Callback callback) {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                callback.onTextChange(getText().toString());
            }
        });
    }

    public interface Callback {
        void onTextChange(String text);
    }

    private static final class SignedIntegerFilter implements InputFilter {
        private final Pattern pattern;

        SignedIntegerFilter(int min) {
            pattern = Pattern.compile("^" + (min < 0 ? "-?" : "") + "[0-9]*$");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.insert(dstart, source);
            if (!pattern.matcher(builder.toString()).matches()) {
                return "";
            }
            return source;
        }
    }

    public void setIntegerFilter(int min) {
        setFilters(new InputFilter[]{
                new SignedIntegerFilter(min)
        });
    }

    public FCLEditText(@NonNull Context context) {
        super(context);
        autoTint = false;
        addTextWatcher();
        bindTheme();
    }

    public FCLEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLEditText);
        autoTint = typedArray.getBoolean(R.styleable.FCLEditText_auto_edit_tint, false);
        typedArray.recycle();
        addTextWatcher();
        bindTheme();
    }

    public FCLEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLEditText);
        autoTint = typedArray.getBoolean(R.styleable.FCLEditText_auto_edit_tint, false);
        typedArray.recycle();
        addTextWatcher();
        bindTheme();
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public final void setVisibilityValue(boolean visibility) {
        visibilityFlow().setValue(visibility);
    }

    public final boolean getVisibilityValue() {
        return visibilityFlow == null || visibilityFlow.getValue();
    }

    public final MutableStateFlow<Boolean> visibilityFlow() {
        if (visibilityFlow == null) {
            visibilityFlow = StateFlowKt.MutableStateFlow(false);
            WeakReference<FCLEditText> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLEditText self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLEditText s = ref.get();
                        if (s != null) {
                            boolean visible = s.visibilityFlow.getValue();
                            s.setVisibility(visible ? VISIBLE : GONE);
                        }
                    });
                }
            });
        }

        return visibilityFlow;
    }

    public final void setDisableValue(boolean disableValue) {
        disableFlow().setValue(disableValue);
    }

    public final boolean getDisableValue() {
        return disableFlow == null || disableFlow.getValue();
    }

    public final MutableStateFlow<Boolean> disableFlow() {
        if (disableFlow == null) {
            disableFlow = StateFlowKt.MutableStateFlow(false);
            WeakReference<FCLEditText> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(disableFlow, v -> {
                FCLEditText self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLEditText s = ref.get();
                        if (s != null) {
                            boolean disable = s.disableFlow.getValue();
                            s.setEnabled(!disable);
                        }
                    });
                }
            });
        }

        return disableFlow;
    }

    public final boolean getFocusedValue() {
        return focusedFlow == null || focusedFlow.getValue();
    }

    public final StateFlow<Boolean> focusedFlow() {
        if (focusedFlow == null) {
            focusedFlow = StateFlowKt.MutableStateFlow(false);
        }

        return focusedFlow;
    }

    public final void setStringValue(String string) {
        stringFlow().setValue(string);
    }

    public final String getStringValue() {
        return stringFlow == null ? null : stringFlow.getValue();
    }

    public final MutableStateFlow<String> stringFlow() {
        if (stringFlow == null) {
            stringFlow = StateFlowKt.MutableStateFlow(null);
        }

        return stringFlow;
    }
}
