package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLTextView extends AppCompatTextView {

    private boolean autoTint;
    private boolean autoBackgroundTint;
    private boolean useThemeColor;
    private StringProperty string;
    private BooleanProperty visibilityProperty;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            if (autoTint) {
                setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
                Drawable[] drawables = getCompoundDrawablesRelative();
                for (Drawable drawable : drawables) {
                    if (drawable != null) {
                        drawable.setTint(ThemeEngine.getInstance().getTheme().getAutoTint());
                    }
                }
            }
            if (autoBackgroundTint) {
                setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getColor()}));
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

    private final IntegerProperty theme2 = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            if (useThemeColor) {
                setTextColor(ThemeEngine.getInstance().getTheme().getColor2());
            }
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme2";
        }
    };

    private final IntegerProperty theme2Dark = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            if (useThemeColor) {
                setTextColor(ThemeEngine.getInstance().getTheme().getColor2());
            }
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme2Dark";
        }
    };

    public FCLTextView(@NonNull Context context) {
        super(context);
        autoTint = false;
        autoBackgroundTint = false;
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
        theme2Dark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty());
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_background_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLTextView_use_theme_color, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
        theme2Dark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty());
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        autoBackgroundTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_background_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLTextView_use_theme_color, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
        theme2Dark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty());
    }

    public void alert() {
        setTextColor(Color.RED);
    }

    public void normal() {
        setTextColor(Color.GRAY);
    }

    public void emphasize() {
        setTextColor(Color.BLACK);
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setUseThemeColor(boolean useThemeColor) {
        this.useThemeColor = useThemeColor;
        setTextColor(ThemeEngine.getInstance().getTheme().getColor2());
    }

    public boolean isUseThemeColor() {
        return useThemeColor;
    }

    public void setAutoBackgroundTint(boolean autoBackgroundTint) {
        this.autoBackgroundTint = autoBackgroundTint;
    }

    public boolean isAutoBackgroundTint() {
        return autoBackgroundTint;
    }

    public final void setString(String string) {
        stringProperty().set(string);
    }

    public final String getString() {
        return string == null ? null : string.get();
    }

    public final StringProperty stringProperty() {
        if (string == null) {
            string = new StringPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        String string = get();
                        setText(string);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "string";
                }
            };
        }

        return string;
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
}
