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

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

import java.util.regex.Pattern;

public class FCLEditText extends AppCompatEditText {

    private boolean autoTint;
    public boolean fromUserOrSystem = false;
    private BooleanProperty visibilityProperty;
    private BooleanProperty disableProperty;
    private BooleanProperty focusedProperty;
    private StringProperty stringProperty;

    private final Thread focusListener = new Thread(() -> {
        if (focusedProperty == null) {
            focusedProperty = new BooleanPropertyBase() {

                public void invalidated() {

                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "focused";
                }
            };
        }
        while (true) {
            focusedProperty.set(isFocused());
        }
    });

    public void runFocusListener() {
        Schedulers.androidUIThread().execute(() -> {
            focusListener.setPriority(Thread.MIN_PRIORITY);
            focusListener.start();
        });
    }

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
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

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme";
        }
    };

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
                stringProperty().set(getText().toString());
                fromUserOrSystem = false;
            }
        });
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
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLEditText);
        autoTint = typedArray.getBoolean(R.styleable.FCLEditText_auto_edit_tint, false);
        typedArray.recycle();
        addTextWatcher();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLEditText);
        autoTint = typedArray.getBoolean(R.styleable.FCLEditText_auto_edit_tint, false);
        typedArray.recycle();
        addTextWatcher();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
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

    public final boolean getFocusedValue() {
        return focusedProperty == null || focusedProperty.get();
    }

    public final ReadOnlyBooleanProperty focusedProperty() {
        if (focusedProperty == null) {
            focusedProperty = new BooleanPropertyBase() {

                public void invalidated() {

                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "focused";
                }
            };
        }

        return focusedProperty;
    }

    public final void setStringValue(String string) {
        stringProperty().set(string);
    }

    public final String getStringValue() {
        return stringProperty == null ? null : stringProperty.get();
    }

    public final StringProperty stringProperty() {
        if (stringProperty == null) {
            stringProperty = new StringPropertyBase() {

                public void invalidated() {

                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "string";
                }
            };
        }

        return stringProperty;
    }
}
