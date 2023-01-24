package com.tungsten.fcl.control.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.task.Schedulers;

public class LogWindow extends ListView {

    private BooleanProperty visibilityProperty;

    public LogWindow(Context context) {
        super(context);
    }

    public LogWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LogWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
