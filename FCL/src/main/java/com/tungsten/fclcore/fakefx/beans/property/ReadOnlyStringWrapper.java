package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyStringWrapper extends SimpleStringProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyStringWrapper}
     */
    public ReadOnlyStringWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyStringWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyStringWrapper(String initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyStringWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyStringProperty}
     * @param name
     *            the name of this {@code ReadOnlyStringProperty}
     */
    public ReadOnlyStringWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyStringWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyStringProperty}
     * @param name
     *            the name of this {@code ReadOnlyStringProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyStringWrapper(Object bean, String name,
            String initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyStringWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyStringProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyStringPropertyBase {

        @Override
        public String get() {
            return ReadOnlyStringWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyStringWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyStringWrapper.this.getName();
        }
    };
}
