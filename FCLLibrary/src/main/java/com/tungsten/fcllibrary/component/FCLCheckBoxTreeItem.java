package com.tungsten.fcllibrary.component;

import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class FCLCheckBoxTreeItem<T> {

    private final T data;
    private final Function<T, String> stringConverter;

    @Nullable
    private String comment;
    @NotNull
    private final List<FCLCheckBoxTreeItem<T>> subItem;

    private final MutableStateFlow<Boolean> expandedFlow = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<Boolean> selectedFlow = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<Boolean> indeterminateFlow = StateFlowKt.MutableStateFlow(false);

    public FCLCheckBoxTreeItem(T data, Function<T, String> stringConverter, @NotNull List<FCLCheckBoxTreeItem<T>> subItem) {
        this.data = data;
        this.stringConverter = stringConverter;
        this.subItem = subItem;

        FlowSubscriptions.subscribe(selectedFlow, selected -> {
            if (!fromCheck) {
                subItem.forEach(it -> it.setSelected(isSelected()));
            }
        });
    }

    private boolean fromCheck = false;

    public void checkProperty() {
        if (subItem.stream().anyMatch(FCLCheckBoxTreeItem::isIndeterminate)) {
            if (!isIndeterminate()) {
                setIndeterminate(true);
            }
        } else if (subItem.stream().noneMatch(FCLCheckBoxTreeItem::isSelected)) {
            if (isIndeterminate()) {
                setIndeterminate(false);
            }
            if (isSelected()) {
                fromCheck = true;
                setSelected(false);
                fromCheck = false;
            }
        } else if (subItem.stream().allMatch(FCLCheckBoxTreeItem::isSelected)) {
            if (isIndeterminate()) {
                setIndeterminate(false);
            }
            if (!isSelected()) {
                fromCheck = true;
                setSelected(true);
                fromCheck = false;
            }
        } else if (subItem.stream().anyMatch(FCLCheckBoxTreeItem::isSelected) && subItem.stream().anyMatch(it -> !it.isSelected())) {
            if (!isIndeterminate()) {
                setIndeterminate(true);
            }
        }
    }

    public T getData() {
        return data;
    }

    public String getText() {
        if (data instanceof String && stringConverter == null)
            return (String) data;
        else if (stringConverter == null)
            return data.toString();
        return stringConverter.apply(data);
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    @NotNull
    public List<FCLCheckBoxTreeItem<T>> getSubItem() {
        return subItem;
    }

    public MutableStateFlow<Boolean> expandedFlow() {
        return expandedFlow;
    }

    public void setExpanded(boolean expanded) {
        this.expandedFlow.setValue(expanded);
    }

    public boolean isExpanded() {
        return expandedFlow.getValue();
    }

    public MutableStateFlow<Boolean> selectedFlow() {
        return selectedFlow;
    }

    public void setSelected(boolean selected) {
        this.selectedFlow.setValue(selected);
    }

    public boolean isSelected() {
        return selectedFlow.getValue();
    }

    public MutableStateFlow<Boolean> indeterminateFlow() {
        return indeterminateFlow;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminateFlow.setValue(indeterminate);
    }

    public boolean isIndeterminate() {
        return indeterminateFlow.getValue();
    }
}
