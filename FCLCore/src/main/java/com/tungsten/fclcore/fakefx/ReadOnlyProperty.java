package com.tungsten.fclcore.fakefx;

public interface ReadOnlyProperty<T> extends ObservableValue<T> {

    Object getBean();

    String getName();

}