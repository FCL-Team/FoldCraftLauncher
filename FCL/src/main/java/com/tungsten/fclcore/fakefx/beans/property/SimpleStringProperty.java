package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a full implementation of a {@link Property} wrapping a
 * {@code String} value.
 *
 * @see StringPropertyBase
 *
 * @since JavaFX 2.0
 */
public class SimpleStringProperty extends StringPropertyBase {

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
     * The constructor of {@code StringProperty}
     */
    public SimpleStringProperty() {
        this(DEFAULT_BEAN, DEFAULT_NAME);
    }

    /**
     * The constructor of {@code StringProperty}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleStringProperty(String initialValue) {
        this(DEFAULT_BEAN, DEFAULT_NAME, initialValue);
    }

    /**
     * The constructor of {@code StringProperty}
     *
     * @param bean
     *            the bean of this {@code StringProperty}
     * @param name
     *            the name of this {@code StringProperty}
     */
    public SimpleStringProperty(Object bean, String name) {
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

    /**
     * The constructor of {@code StringProperty}
     *
     * @param bean
     *            the bean of this {@code StringProperty}
     * @param name
     *            the name of this {@code StringProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public SimpleStringProperty(Object bean, String name, String initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = (name == null) ? DEFAULT_NAME : name;
    }

}
