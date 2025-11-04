package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLImageView extends AppCompatImageView {

    private ObjectProperty<Drawable> image;
    private boolean autoTint;
    private boolean useThemeColor;
    private BooleanProperty visibilityProperty;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            if (autoTint) {
                int[][] state = {
                        {

                        }
                };
                int[] color = {
                        ThemeEngine.getInstance().getTheme().getAutoTint()
                };
                setImageTintList(new ColorStateList(state, color));
            }
            if (useThemeColor && getBackground() != null) {
                getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor2());
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
            if (useThemeColor && getBackground() != null) {
                getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor2());
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
            if (useThemeColor && getBackground() != null) {
                getBackground().setTint(ThemeEngine.getInstance().getTheme().getColor2());
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


    public FCLImageView(@NonNull Context context) {
        super(context);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
        theme2Dark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty());
    }

    public FCLImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageView);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageView_auto_src_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageView_use_theme_color, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
        theme2Dark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty());
    }

    public FCLImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageView);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageView_auto_src_tint, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageView_use_theme_color, false);
        typedArray.recycle();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
        theme2Dark.bind(ThemeEngine.getInstance().getTheme().color2DarkProperty());
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setUseThemeColor(boolean useThemeColor) {
        this.useThemeColor = useThemeColor;
    }

    public boolean isUseThemeColor() {
        return useThemeColor;
    }

    public final void setImage(Drawable drawable) {
        imageProperty().set(drawable);
    }

    public final Drawable getImage() {
        return image == null ? null : image.get();
    }

    public final ObjectProperty<Drawable> imageProperty() {
        if (image == null) {
            image = new ObjectPropertyBase<Drawable>() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        Drawable drawable = get();
                        setBackground(drawable);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "image";
                }
            };
        }

        return this.image;
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
