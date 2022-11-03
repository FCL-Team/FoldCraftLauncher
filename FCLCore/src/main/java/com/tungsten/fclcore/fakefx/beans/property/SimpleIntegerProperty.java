package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a full implementation of a {@link Property} wrapping a
 * {@code int} value.
 *
 * @see IntegerPropertyBase
 *
 * @since JavaFX 2.0
 */
public class SimpleIntegerProperty extends IntegerPropertyBase {

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
     * The constructor of {@code IntegerProperty}
     */
    public SimpleIntegerProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code IntegerProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleIntegerProperty(int initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code IntegerProperty}
     *
     * @param bean
     *            the bean of this {@code IntegerProperty}
     * @param name
     *            the name of this {@code IntegerProperty}
     */
    public SimpleIntegerProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code IntegerProperty}
     *
     * @param bean
     *            the bean of this {@code IntegerProperty}
     * @param name
     *            the name of this {@code IntegerProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleIntegerProperty(Object bean, String name, int initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
