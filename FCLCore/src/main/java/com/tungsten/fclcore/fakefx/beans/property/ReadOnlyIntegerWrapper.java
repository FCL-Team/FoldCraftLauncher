package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyIntegerWrapper extends SimpleIntegerProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyIntegerWrapper}
     */
    public ReadOnlyIntegerWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyIntegerWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyIntegerWrapper(int initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyIntegerWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyIntegerProperty}
     * @param name
     *            the name of this {@code ReadOnlyIntegerProperty}
     */
    public ReadOnlyIntegerWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyIntegerWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyIntegerProperty}
     * @param name
     *            the name of this {@code ReadOnlyIntegerProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyIntegerWrapper(Object bean, String name, int initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyIntegerWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyIntegerProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyIntegerPropertyBase {

        @Override
        public int get() {
            return ReadOnlyIntegerWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyIntegerWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyIntegerWrapper.this.getName();
        }
    };
}
