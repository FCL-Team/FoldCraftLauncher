package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.collections.ObservableList;

/**
 * This class provides a full implementation of a {@link Property} wrapping an
 * {@code ObservableList}.
 *
 * @see ListPropertyBase
 *
 * @param <E> the type of the {@code List} elements
 * @since JavaFX 2.1
 */
public class SimpleListProperty<E> extends ListPropertyBase<E> {

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
     * The constructor of {@code SimpleListProperty}
     */
    public SimpleListProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code SimpleListProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleListProperty(ObservableList<E> initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code SimpleListProperty}
     *
     * @param bean
     *            the bean of this {@code SetProperty}
     * @param name
     *            the name of this {@code SetProperty}
     */
    public SimpleListProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code SimpleListProperty}
     *
     * @param bean
     *            the bean of this {@code ListProperty}
     * @param name
     *            the name of this {@code ListProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleListProperty(Object bean, String name, ObservableList<E> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
