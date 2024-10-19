package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectPropertyBase;
import com.tungsten.fclcore.task.Schedulers;

import java.util.ArrayList;

public class FCLSpinner<T> extends AppCompatSpinner {

    private boolean fromUserOrSystem = false;
    private ArrayList<T> dataList;
    private ObjectProperty<T> selectedItemProperty;
    private BooleanProperty visibilityProperty;

    public void addSelectListener() {
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList != null && dataList.size() > i) {
                    fromUserOrSystem = true;
                    selectedItemProperty().set(dataList.get(i));
                    fromUserOrSystem = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public FCLSpinner(@NonNull Context context) {
        super(context);
    }

    public FCLSpinner(@NonNull Context context, int mode) {
        super(context, mode);
    }

    public FCLSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FCLSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FCLSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public FCLSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public final Object getSelectedItemValue() {
        return selectedItemProperty == null ? null : selectedItemProperty.get();
    }

    public final ObjectProperty<T> selectedItemProperty() {
        if (selectedItemProperty == null) {
            selectedItemProperty = new ObjectPropertyBase<T>() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        if (!fromUserOrSystem) {
                            T data = get();
                            setSelection(dataList.indexOf(data));
                        }
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "selectedItem";
                }
            };
        }

        return selectedItemProperty;
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
