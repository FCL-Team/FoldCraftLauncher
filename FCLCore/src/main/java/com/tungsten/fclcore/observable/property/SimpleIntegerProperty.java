package com.tungsten.fclcore.observable.property;

/**
 * Minimal reimplementation of {@code SimpleIntegerProperty}.
 */
public class SimpleIntegerProperty extends IntegerPropertyBase {

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

    public SimpleIntegerProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME, 0);
    }

    public SimpleIntegerProperty(int initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleIntegerProperty(Object bean, String name) {
        this(bean, name, 0);
    }

    public SimpleIntegerProperty(Object bean, String name, int initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
