package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.DoublePropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLProgressBar extends ProgressBar {

    private DoubleProperty progress;
    private IntegerProperty firstProgressProperty;
    private IntegerProperty secondProgressProperty;
    private BooleanProperty visibilityProperty;
    private BooleanProperty disableProperty;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            int[][] state = {
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getInstance().getTheme().getDkColor()
            };
            setProgressTintList(new ColorStateList(state, color));
            setSecondaryProgressTintList(new ColorStateList(state, color));
            setIndeterminateTintList(new ColorStateList(state, color));
    }

    public FCLProgressBar(Context context) {
        super(context);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public final DoubleProperty percentProgressProperty() {
        if (progress == null) {
            progress = new DoublePropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        // progress should >= 0, <= 1
                        double progress = get();
                        setIndeterminate(progress < 0.0);
                        if (progress >= 0.0) {
                            setProgress((int) (progress * getMax()));
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "percentProgress";
                }
            };
        }

        return progress;
    }

    public final IntegerProperty firstProgressProperty() {
        if (firstProgressProperty == null) {
            firstProgressProperty = new IntegerPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        int progress = get();
                        if (progress >= 0) {
                            setProgress(Math.min(progress, getMax()));
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "firstProgress";
                }
            };
        }

        return firstProgressProperty;
    }

    public final IntegerProperty secondProgressProperty() {
        if (secondProgressProperty == null) {
            secondProgressProperty = new IntegerPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        int progress = get();
                        if (progress >= 0) {
                            setSecondaryProgress(Math.min(progress, getMax()));
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "secondProgress";
                }
            };
        }

        return secondProgressProperty;
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
}
