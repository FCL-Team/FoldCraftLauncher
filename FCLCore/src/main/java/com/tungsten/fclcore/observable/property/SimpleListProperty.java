package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.collections.ObservableList;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.SimpleListProperty}.
 */
public class SimpleListProperty<E> extends ListPropertyBase<E> {

    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";

    private final Object bean;
    private final String name;

    @Override
    public Object getBean() {
        return bean;
    }

    @Override
    public String getName() {
        return name;
    }

    public SimpleListProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleListProperty(ObservableList<E> initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleListProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    public SimpleListProperty(Object bean, String name, ObservableList<E> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
