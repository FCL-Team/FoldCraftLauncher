package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.collections.ObservableSet;

/**
 * Minimal reimplementation of {@code SimpleSetProperty}.
 */
public class SimpleSetProperty<E> extends SetPropertyBase<E> {

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

    public SimpleSetProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleSetProperty(ObservableSet<E> initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleSetProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    public SimpleSetProperty(Object bean, String name, ObservableSet<E> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
