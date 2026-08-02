package com.tungsten.fclcore.observable.property;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.SimpleStringProperty}.
 */
public class SimpleStringProperty extends StringPropertyBase {

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

    public SimpleStringProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME, null);
    }

    public SimpleStringProperty(String initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleStringProperty(Object bean, String name) {
        this(bean, name, null);
    }

    public SimpleStringProperty(Object bean, String name, String initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
