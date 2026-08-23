package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a full implementation of a {@link Property} wrapping a
 * {@code float} value.
 *
 * @see FloatPropertyBase
 *
 * @since JavaFX 2.0
 */
public class SimpleFloatProperty extends FloatPropertyBase {

    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";

    private final Object bean;
    private final String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getBean() {
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The constructor of {@code FloatProperty}
     */
    public SimpleFloatProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code FloatProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleFloatProperty(float initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code FloatProperty}
     *
     * @param bean
     *            the bean of this {@code FloatProperty}
     * @param name
     *            the name of this {@code FloatProperty}
     */
    public SimpleFloatProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code FloatProperty}
     *
     * @param bean
     *            the bean of this {@code FloatProperty}
     * @param name
     *            the name of this {@code FloatProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleFloatProperty(Object bean, String name, float initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
