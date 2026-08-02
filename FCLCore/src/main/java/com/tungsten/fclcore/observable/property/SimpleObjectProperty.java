package com.tungsten.fclcore.observable.property;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.SimpleObjectProperty}.
 */
public class SimpleObjectProperty<T> extends ObjectPropertyBase<T> {

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

    public SimpleObjectProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME, null);
    }

    public SimpleObjectProperty(T initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleObjectProperty(Object bean, String name) {
        this(bean, name, null);
    }

    public SimpleObjectProperty(Object bean, String name, T initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
