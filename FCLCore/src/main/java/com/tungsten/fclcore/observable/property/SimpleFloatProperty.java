package com.tungsten.fclcore.observable.property;

/**
 * Minimal reimplementation of {@code fakefx.beans.property.SimpleFloatProperty}.
 */
public class SimpleFloatProperty extends FloatPropertyBase {

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

    public SimpleFloatProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME, 0.0f);
    }

    public SimpleFloatProperty(float initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleFloatProperty(Object bean, String name) {
        this(bean, name, 0.0f);
    }

    public SimpleFloatProperty(Object bean, String name, float initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
