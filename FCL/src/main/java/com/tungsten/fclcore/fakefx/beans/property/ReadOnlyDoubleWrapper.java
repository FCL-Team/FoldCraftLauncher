package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyDoubleWrapper extends SimpleDoubleProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyDoubleWrapper}
     */
    public ReadOnlyDoubleWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyDoubleWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyDoubleWrapper(double initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyDoubleWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyDoubleProperty}
     * @param name
     *            the name of this {@code ReadOnlyDoubleProperty}
     */
    public ReadOnlyDoubleWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyDoubleWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyDoubleProperty}
     * @param name
     *            the name of this {@code ReadOnlyDoubleProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyDoubleWrapper(Object bean, String name,
            double initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the read-only property, that is synchronized with this
     * {@code ReadOnlyDoubleWrapper}.
     *
     * @return the read-only property
     */
    public ReadOnlyDoubleProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyDoublePropertyBase {

        @Override
        public double get() {
            return ReadOnlyDoubleWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyDoubleWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyDoubleWrapper.this.getName();
        }
    };
}
