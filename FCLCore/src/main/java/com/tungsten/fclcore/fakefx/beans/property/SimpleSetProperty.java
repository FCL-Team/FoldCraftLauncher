package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.collections.ObservableSet;

/**
 * This class provides a full implementation of a {@link Property} wrapping an
 * {@code ObservableSet}.
 *
 * @see SetPropertyBase
 *
 * @param <E> the type of the {@code Set} elements
 * @since JavaFX 2.1
 */
public class SimpleSetProperty<E> extends SetPropertyBase<E> {

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
     * The constructor of {@code SimpleSetProperty}
     */
    public SimpleSetProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code SimpleSetProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleSetProperty(ObservableSet<E> initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code SimpleSetProperty}
     *
     * @param bean
     *            the bean of this {@code SetProperty}
     * @param name
     *            the name of this {@code SetProperty}
     */
    public SimpleSetProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code SimpleSetProperty}
     *
     * @param bean
     *            the bean of this {@code SetProperty}
     * @param name
     *            the name of this {@code SetProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleSetProperty(Object bean, String name, ObservableSet<E> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
