package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a full implementation of a {@link Property} wrapping a
 * {@code long} value.
 *
 * @see LongPropertyBase
 *
 * @since JavaFX 2.0
 */
public class SimpleLongProperty extends LongPropertyBase {

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
     * The constructor of {@code LongProperty}
     */
    public SimpleLongProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code LongProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleLongProperty(long initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code LongProperty}
     *
     * @param bean
     *            the bean of this {@code LongProperty}
     * @param name
     *            the name of this {@code LongProperty}
     */
    public SimpleLongProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code LongProperty}
     *
     * @param bean
     *            the bean of this {@code LongProperty}
     * @param name
     *            the name of this {@code LongProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleLongProperty(Object bean, String name, long initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
