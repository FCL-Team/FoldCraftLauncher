package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyBooleanWrapper extends SimpleBooleanProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyBooleanWrapper}
     */
    public ReadOnlyBooleanWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyBooleanWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyBooleanWrapper(boolean initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyBooleanWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyBooleanProperty}
     * @param name
     *            the name of this {@code ReadOnlyBooleanProperty}
     */
    public ReadOnlyBooleanWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyBooleanWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyBooleanProperty}
     * @param name
     *            the name of this {@code ReadOnlyBooleanProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyBooleanWrapper(Object bean, String name,
            boolean initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyBooleanWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyBooleanProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyBooleanPropertyBase {

        @Override
        public boolean get() {
            return ReadOnlyBooleanWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyBooleanWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyBooleanWrapper.this.getName();
        }
    };
}
