package com.tungsten.fclcore.fakefx;

public interface ObservableValue<T> extends Observable {

    void addListener(ChangeListener<? super T> listener);

    void removeListener(ChangeListener<? super T> listener);

    T getValue();
}