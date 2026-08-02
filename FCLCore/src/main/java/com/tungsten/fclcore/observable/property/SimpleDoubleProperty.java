package com.tungsten.fclcore.observable.property;

/**
 * Minimal reimplementation of {@code SimpleDoubleProperty}.
 */
public class SimpleDoubleProperty extends DoublePropertyBase {

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

    public SimpleDoubleProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME, 0.0);
    }

    public SimpleDoubleProperty(double initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleDoubleProperty(Object bean, String name) {
        this(bean, name, 0.0);
    }

    public SimpleDoubleProperty(Object bean, String name, double initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
