package com.tungsten.fcllibrary.component;

import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.util.StringConverter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FCLCheckBoxTreeItem<T> {

    private final T data;
    private final StringConverter<T> stringConverter;

    @Nullable
    private String comment;
    @NotNull
    private final ObservableList<FCLCheckBoxTreeItem<T>> subItem;

    private final SimpleBooleanProperty expandedProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty indeterminateProperty = new SimpleBooleanProperty(false);

    public FCLCheckBoxTreeItem(T data, StringConverter<T> stringConverter, @NotNull ObservableList<FCLCheckBoxTreeItem<T>> subItem) {
        this.data = data;
        this.stringConverter = stringConverter;
        this.subItem = subItem;

        selectedProperty().addListener(observable -> {
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
        return stringConverter.toString(data);
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    @NotNull
    public ObservableList<FCLCheckBoxTreeItem<T>> getSubItem() {
        return subItem;
    }

    public SimpleBooleanProperty expandedProperty() {
        return expandedProperty;
    }

    public void setExpanded(boolean expanded) {
        this.expandedProperty.set(expanded);
    }

    public boolean isExpanded() {
        return expandedProperty.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selectedProperty;
    }

    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }

    public SimpleBooleanProperty indeterminateProperty() {
        return indeterminateProperty;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminateProperty.set(indeterminate);
    }

    public boolean isIndeterminate() {
        return indeterminateProperty.get();
    }
}
