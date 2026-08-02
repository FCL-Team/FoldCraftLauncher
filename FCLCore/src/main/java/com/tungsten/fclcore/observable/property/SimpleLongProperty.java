package com.tungsten.fclcore.observable.property;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.SimpleLongProperty}.
 */
public class SimpleLongProperty extends LongPropertyBase {

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

    public SimpleLongProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME, 0L);
    }

    public SimpleLongProperty(long initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleLongProperty(Object bean, String name) {
        this(bean, name, 0L);
    }

    public SimpleLongProperty(Object bean, String name, long initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
