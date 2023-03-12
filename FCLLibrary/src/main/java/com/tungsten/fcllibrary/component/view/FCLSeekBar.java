package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.DoublePropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLSeekBar extends AppCompatSeekBar {

    private boolean fromUserOrSystem = false;
    private BooleanProperty visibilityProperty;
    private BooleanProperty disableProperty;
    private DoubleProperty percentProgressProperty;
    private IntegerProperty progressProperty;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            int[][] state = {
                    {

                    }
            };
            int[] color = {
                    ThemeEngine.getInstance().getTheme().getDkColor()
            };
            setThumbTintList(new ColorStateList(state, color));
            setProgressTintList(new ColorStateList(state, color));
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

    public void addProgressListener() {
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fromUserOrSystem = true;
                progressProperty().set(i);
                percentProgressProperty().set((double) i / (double) getMax());
                fromUserOrSystem = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public FCLSeekBar(@NonNull Context context) {
        super(context);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
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

    public final void setPercentProgressValue(double percentProgressValue) {
        percentProgressProperty().set(percentProgressValue);
    }

    public final double getPercentProgressValue() {
        return percentProgressProperty == null ? -1 : percentProgressProperty.get();
    }

    public final DoubleProperty percentProgressProperty() {
        if (percentProgressProperty == null) {
            percentProgressProperty = new DoublePropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        if (!fromUserOrSystem) {
                            double progress = get();
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

        return percentProgressProperty;
    }

    public final void setProgressValue(int progressValue) {
        progressProperty().set(progressValue);
    }

    public final int getProgressValue() {
        return progressProperty == null ? -1 : progressProperty().get();
    }

    public final IntegerProperty progressProperty() {
        if (progressProperty == null) {
            progressProperty = new IntegerPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        if (!fromUserOrSystem) {
                            int progress = get();
                            setProgress(progress);
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "progress";
                }
            };
        }

        return progressProperty;
    }

}
