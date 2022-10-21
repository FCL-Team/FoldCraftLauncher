package com.tungsten.fclcore.fakefx;

public interface ChangeListener<T> {

    void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);
}