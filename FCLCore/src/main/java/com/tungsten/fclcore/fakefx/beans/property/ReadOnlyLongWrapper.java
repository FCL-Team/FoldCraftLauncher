package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyLongWrapper extends SimpleLongProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyLongWrapper}
     */
    public ReadOnlyLongWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyLongWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyLongWrapper(long initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyLongWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyLongProperty}
     * @param name
     *            the name of this {@code ReadOnlyLongProperty}
     */
    public ReadOnlyLongWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyLongWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyLongProperty}
     * @param name
     *            the name of this {@code ReadOnlyLongProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyLongWrapper(Object bean, String name, long initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyLongWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyLongProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyLongPropertyBase {

        @Override
        public long get() {
            return ReadOnlyLongWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyLongWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyLongWrapper.this.getName();
        }
    };
}
