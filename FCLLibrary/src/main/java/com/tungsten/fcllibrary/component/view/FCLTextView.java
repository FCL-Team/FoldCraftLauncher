package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
    private StringProperty string;
    private BooleanProperty visibilityProperty;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            if (autoTint) {
                setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
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

    public FCLTextView(@NonNull Context context) {
        super(context);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTextView);
        autoTint = typedArray.getBoolean(R.styleable.FCLTextView_auto_text_tint, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
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
