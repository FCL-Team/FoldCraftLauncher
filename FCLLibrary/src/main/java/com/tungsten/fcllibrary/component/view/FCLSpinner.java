package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLSpinner<T> extends AppCompatSpinner {

    private boolean fromUserOrSystem = false;
    private ArrayList<T> dataList;
    private MutableStateFlow<T> selectedItemFlow;
    private MutableStateFlow<Boolean> visibilityFlow;

    public void addSelectListener() {
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList != null && dataList.size() > i) {
                    fromUserOrSystem = true;
                    selectedItemFlow().setValue(dataList.get(i));
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
        return selectedItemFlow == null ? null : selectedItemFlow.getValue();
    }

    public final MutableStateFlow<T> selectedItemFlow() {
        if (selectedItemFlow == null) {
            selectedItemFlow = StateFlowKt.MutableStateFlow(null);
            WeakReference<FCLSpinner<T>> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(selectedItemFlow, v -> {
                FCLSpinner<T> self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSpinner<T> s = ref.get();
                        if (s != null) {
                            if (!s.fromUserOrSystem) {
                                T data = s.selectedItemFlow.getValue();
                                s.setSelection(s.dataList.indexOf(data));
                            }
                        }
                    });
                }
            });
        }

        return selectedItemFlow;
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
            WeakReference<FCLSpinner<T>> ref = new WeakReference<>(this);
            FlowSubscriptions.subscribe(visibilityFlow, v -> {
                FCLSpinner<T> self = ref.get();
                if (self != null) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLSpinner<T> s = ref.get();
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
}
