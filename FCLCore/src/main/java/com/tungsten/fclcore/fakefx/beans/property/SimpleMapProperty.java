package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.collections.ObservableMap;

/**
 * This class provides a full implementation of a {@link Property} wrapping an
 * {@code ObservableMap}.
 *
 * @see MapPropertyBase
 *
 * @param <K> the type of the key elements of the {@code Map}
 * @param <V> the type of the value elements of the {@code Map}
 * @since JavaFX 2.1
 */
public class SimpleMapProperty<K, V> extends MapPropertyBase<K, V> {

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
     * The constructor of {@code SimpleMapProperty}
     */
    public SimpleMapProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code SimpleMapProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleMapProperty(ObservableMap<K, V> initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code SimpleMapProperty}
     *
     * @param bean
     *            the bean of this {@code MapProperty}
     * @param name
     *            the name of this {@code MapProperty}
     */
    public SimpleMapProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code SimpleMapProperty}
     *
     * @param bean
     *            the bean of this {@code MapProperty}
     * @param name
     *            the name of this {@code MapProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleMapProperty(Object bean, String name, ObservableMap<K, V> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
