package com.tungsten.fclcore.fakefx;

public class SimpleBooleanProperty extends BooleanPropertyBase {

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

    public SimpleBooleanProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    public SimpleBooleanProperty(boolean initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    public SimpleBooleanProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    public SimpleBooleanProperty(Object bean, String name, boolean initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}