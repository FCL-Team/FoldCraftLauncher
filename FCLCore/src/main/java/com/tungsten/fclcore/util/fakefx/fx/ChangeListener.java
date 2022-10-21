package com.tungsten.fclcore.util.fakefx.fx;

public interface ChangeListener<T> {

    void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);
}