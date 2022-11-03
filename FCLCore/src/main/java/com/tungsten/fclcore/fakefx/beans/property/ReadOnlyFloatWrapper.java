package com.tungsten.fclcore.fakefx.beans.property;

/**
 * This class provides a convenient class to define read-only properties. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @since JavaFX 2.0
 */
public class ReadOnlyFloatWrapper extends SimpleFloatProperty {

    private ReadOnlyPropertyImpl readOnlyProperty;

    /**
     * The constructor of {@code ReadOnlyFloatWrapper}
     */
    public ReadOnlyFloatWrapper() {
    }

    /**
     * The constructor of {@code ReadOnlyFloatWrapper}
     *
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyFloatWrapper(float initialValue) {
        super(initialValue);
    }

    /**
     * The constructor of {@code ReadOnlyFloatWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyFloatProperty}
     * @param name
     *            the name of this {@code ReadOnlyFloatProperty}
     */
    public ReadOnlyFloatWrapper(Object bean, String name) {
        super(bean, name);
    }

    /**
     * The constructor of {@code ReadOnlyFloatWrapper}
     *
     * @param bean
     *            the bean of this {@code ReadOnlyFloatProperty}
     * @param name
     *            the name of this {@code ReadOnlyFloatProperty}
     * @param initialValue
     *            the initial value of the wrapped value
     */
    public ReadOnlyFloatWrapper(Object bean, String name, float initialValue) {
        super(bean, name, initialValue);
    }

    /**
     * Returns the readonly property, that is synchronized with this
     * {@code ReadOnlyFloatWrapper}.
     *
     * @return the readonly property
     */
    public ReadOnlyFloatProperty getReadOnlyProperty() {
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

    private class ReadOnlyPropertyImpl extends ReadOnlyFloatPropertyBase {

        @Override
        public float get() {
            return ReadOnlyFloatWrapper.this.get();
        }

        @Override
        public Object getBean() {
            return ReadOnlyFloatWrapper.this.getBean();
        }

        @Override
        public String getName() {
            return ReadOnlyFloatWrapper.this.getName();
        }
    };
}
