package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyObjectWrapper<T> extends SimpleObjectProperty<T> {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyObjectWrapper}
     */
    public ReadOnlyObjectWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyObjectWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyObjectWrapper(T initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyObjectWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyObjectProperty}
     * @param name
     *            the name of this {@code ReadOnlyObjectProperty}
     */
    public ReadOnlyObjectWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyObjectWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyObjectProperty}
     * @param name
     *            the name of this {@code ReadOnlyObjectProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyObjectWrapper(Object bean, String name, T initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyObjectWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyObjectProperty<T> getReadOnlyProperty() {
        if (readOnlyProperty == null) {
            readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return readOnlyProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (readOnlyProperty != null) {
            readOnlyProperty.fireValueChangedEvent();
        }
    }

    private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase<T> {

        @Override
        public T get() {
            return ReadOnlyObjectWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyObjectWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyObjectWrapper.this.getName();
        }
    };
}
